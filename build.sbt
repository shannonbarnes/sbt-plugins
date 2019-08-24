

  ThisBuild / organization := "com.whil"
  ThisBuild / version      := "1.1.0-SNAPSHOT"

  lazy val `sbt-plugins` = (project in file("."))
    .settings(
      publish / skip := true)
    .aggregate(
      `sbt-whil`, `sbt-whil-test-databases`
    )
  

  lazy val `sbt-whil-test-databases` = project
    .enablePlugins(SbtPlugin)
    .settings(
      scriptedLaunchOpts := { scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
      },
      libraryDependencies ++= Seq(
       "com.datastax.cassandra" % "cassandra-driver-core" % "3.5.0",
       "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"))

  lazy val `sbt-whil` = project
    .enablePlugins(SbtPlugin)
    .settings(
      addSbtPlugin("com.typesafe.sbt"  % "sbt-native-packager"   % "1.3.25"),
      scriptedLaunchOpts := { scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
      }
    )

