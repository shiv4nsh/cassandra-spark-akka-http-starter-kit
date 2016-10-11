
name := "cassandra-spark-akka-http-starter-kit"

version := "1.0"

scalaVersion := "2.11.8"

organization := "com.knoldus"

val akkaV = "2.4.5"
libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.0.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0",
  "com.typesafe.akka" %% "akka-http-core" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.0-M3",
  "net.liftweb" % "lift-json_2.11" % "2.6.2"

)

assembleArtifact in assemblyPackageScala := false // We don't need the Scala library, Spark already includes it

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}
fork in run := true
