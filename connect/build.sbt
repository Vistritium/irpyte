name := "irpyte-connect"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.squareup.okhttp3" % "okhttp" % "3.9.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.3",
  "com.jsuereth" %% "scala-arm" % "2.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.2"
)