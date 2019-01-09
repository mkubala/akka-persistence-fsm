
name := "akka-persistence-fsm"

organization := "pl.mkubala"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.15"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion
)
