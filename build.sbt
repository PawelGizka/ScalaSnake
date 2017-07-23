name := "ScalaSnake"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.11.0-M7",
  "com.typesafe.akka" %% "akka-stream" % "2.5.3",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
    