
name := "akka-persistence-fsm"

organization := "pl.mkubala"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.19" 

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "org.scalatest" %%  "scalatest" % "3.0.5" % "test"
)
