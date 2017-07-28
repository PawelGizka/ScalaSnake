name := "ScalaSnake"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.3",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
    