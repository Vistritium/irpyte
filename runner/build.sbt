lazy val lockScreen = RootProject(file("../win-wallpaper-changer"))
lazy val connect = RootProject(file("../connect"))

name := "irpyte-runner"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe" % "config" % "1.3.2",
  "net.harawata" % "appdirs" % "1.0.1",
  "com.squareup.okhttp3" % "okhttp" % "3.9.1"
)

dependsOn(connect, lockScreen)