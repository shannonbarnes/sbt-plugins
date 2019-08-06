

  ThisBuild / organization := "com.whil"
  ThisBuild / version      := "1.0.0-SNAPSHOT"

  lazy val `sbt-plugins` = (project in file("."))
    .settings(
      publish / skip := true)
    .aggregate(
      `sbt-whil-cassandra`
    )
  

  lazy val `sbt-whil-cassandra` = project
    .enablePlugins(SbtPlugin)
    .settings(
      scriptedLaunchOpts := { scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
      },
      libraryDependencies ++= Seq(
       "com.datastax.cassandra" % "cassandra-driver-core" % "3.5.0",
       "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"))

