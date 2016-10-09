package com.knoldus.factories

import com.knoldus.domain.User
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by shivansh on 9/5/16.
  */
trait DatabaseAccess {
  def create(user: User): Boolean = {
    true
  }

  def update(user: User): Boolean = {
    true
  }

  def delete(id: String): Boolean = {
    true
  }

  def retrieve(id: String): User = {
    User("", "", "")
  }
}

object DatabaseAccess extends DatabaseAccess


object Context {
  val config = ConfigFactory.load()
  val url = config.getString("cassandra.url")
  val sparkConf: SparkConf = new SparkConf().setAppName("Saprk-cassandra-akka-rest-example").setMaster("local[4]")
    .set("spark.cassandra.connection.host", url)
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()
  val sc = spark.sparkContext
}