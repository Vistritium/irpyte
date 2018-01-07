lazy val lockScreen = RootProject(file("../lock-screen"))

name := "irpyte-runner"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

dependsOn(lockScreen)