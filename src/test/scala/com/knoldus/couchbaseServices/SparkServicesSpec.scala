

package com.knoldus.couchbaseServices

import java.util.UUID

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.domain.User
import com.knoldus.routes.SparkService
import org.scalatest.{Matchers, WordSpec}

class SparkServicesSpec extends WordSpec with Matchers with ScalatestRouteTest with SparkService {

  val documentId = "user::" + UUID.randomUUID().toString
  val jsonObject = User("1", "Shivansh", "shivansh@knoldus.com")
  create(jsonObject)
  "The service" should {

    "be able to insert data in the couchbase" in {
      Get("/create/name/Shivansh/email/shiv4nsh@gmail.com") ~> sparkRoutes ~> check {
        responseAs[String].contains("Data is successfully persisted with id") shouldEqual true
      }
    }

    "to be able to retrieve data via N1Ql" in {
      Get("/retrieve/id/1") ~> sparkRoutes ~> check {
        responseAs[String].contains("shivansh@knoldus.com") shouldEqual true
      }
    }}}
