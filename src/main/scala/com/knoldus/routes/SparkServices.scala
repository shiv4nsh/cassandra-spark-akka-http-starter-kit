package com.knoldus.routes

import java.util.UUID

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import com.knoldus.domain.User
import com.knoldus.factories.DatabaseAccess
import net.liftweb.json._
import java.util.Date
import net.liftweb.json.Extraction._


trait SparkService extends DatabaseAccess {

  implicit val system:ActorSystem
  implicit val materializer:ActorMaterializer
  val logger = Logging(system, getClass)


  implicit def myExceptionHandler =
    ExceptionHandler {
      case e: ArithmeticException =>
        extractUri { uri =>
          complete(HttpResponse(StatusCodes.InternalServerError, entity = s"Data is not persisted and something went wrong"))
        }
    }

  implicit val formats: Formats = new DefaultFormats {
    outer =>
    override val typeHintFieldName = "type"
    override
    val typeHints = ShortTypeHints(List(classOf[String], classOf[Date]))
  }

  val sparkRoutes: Route = {
    get {
      path("create" / "name" / Segment / "email" / Segment) { (name: String, email: String) =>
        complete {
          val documentId = "user::" + UUID.randomUUID().toString
          try {
            val user = User(documentId,name,email)
            val isPersisted = create(user)
            if (isPersisted) {
              HttpResponse(StatusCodes.Created, entity = s"Data is successfully persisted with id $documentId")
            } else {
              HttpResponse(StatusCodes.InternalServerError, entity = s"Error found for id : $documentId")
            }
          } catch {
            case ex: Throwable =>
              logger.error(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError, entity = s"Error found for id : $documentId")
          }
        }
      }
    } ~ path("retrieve" / "id" / Segment) { (listOfIds: String) =>
      get {
        complete {
          try {
            val idAsRDD: Option[Array[User]] = retrieve(listOfIds)
            idAsRDD match {
              case Some(data) => HttpResponse(StatusCodes.OK, entity = data.headOption.fold("")(x => compact(render(decompose(x)))))
              case None => HttpResponse(StatusCodes.InternalServerError, entity = s"Data is not fetched and something went wrong")
            }
          } catch {
            case ex: Throwable =>
              logger.error(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError, entity = s"Error found for ids : $listOfIds")
          }
        }
      }
    }
  }
}
