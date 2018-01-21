lazy val lockScreen = RootProject(file("../win-wallpaper-changer"))
lazy val connect = RootProject(file("../connect"))

name := "irpyte"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe" % "config" % "1.3.2",
  "net.harawata" % "appdirs" % "1.0.1",
  "com.squareup.okhttp3" % "okhttp" % "3.9.1",
  "com.github.sarxos" % "windows-registry-util" % "0.3"
)

dependsOn(connect, lockScreen)

enablePlugins(JavaAppPackaging)
enablePlugins(JDKPackagerPlugin)

javaOptions in Universal ++= Seq(
  "-J-Xmx200m",
  "-J-Xms20m",
  "-J-XX:MinHeapFreeRatio=20",
  "-J-XX:MaxHeapFreeRatio=30",
  "-J-XX:GCTimeRatio=18",
  "-XX:+UseG1GC"
)

maintainer := "Maciej Nowicki"
packageSummary := "Changes to new wallpaper everyday"
packageDescription := "Changes to new wallpaper everyday. Put search phrase and app will download relevant wallpapers everyday."

jdkAppIcon := Some(file("build/favicon.ico"))
jdkPackagerType := "msi"
jdkPackagerJVMArgs := Seq(
  "-Xmx200m",
  "-Xms20m",
  "-XX:MinHeapFreeRatio=20",
  "-XX:MaxHeapFreeRatio=30",
  "-XX:GCTimeRatio=18",
  "-XX:+UseG1GC"
)
jdkPackagerBasename := "irpyte"
jdkPackagerProperties := Map(
  "app.name" -> "irpyte",
  "app.version" -> version.value
)
jdkPackagerAppArgs := Seq(maintainer.value, packageSummary.value, packageDescription.value)
