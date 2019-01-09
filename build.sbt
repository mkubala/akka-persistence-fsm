
name := "akka-persistence-fsm"

organization := "pl.mkubala"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.15"

resolvers += "dnvriend" at "http://dl.bintray.com/dnvriend/maven"
libraryDependencies ++= Seq(
  "com.typesafe.akka"     %% "akka-persistence"             % akkaVersion,
  "com.github.dnvriend"   %% "akka-persistence-inmemory"    % "2.5.15.1"
)
