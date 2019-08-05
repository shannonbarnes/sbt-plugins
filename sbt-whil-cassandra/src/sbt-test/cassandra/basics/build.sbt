import com.whil.sbt._

scalaVersion := "2.12.8"

enablePlugins(EmbeddedCassandraPlugin)

libraryDependencies ++= Seq(
  "org.scalatest"  %% "scalatest" % "3.0.5" % Test,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.4.0"
)
