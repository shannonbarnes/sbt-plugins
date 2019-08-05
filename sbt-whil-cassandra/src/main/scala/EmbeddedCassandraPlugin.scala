package com.whil.sbt

import com.datastax.driver.core.Cluster
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.FileCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import sbt._
import sbt.Keys._

object EmbeddedCassandraPlugin extends AutoPlugin {
  //Name of the file containing preload script
  val scriptName = SettingKey[Option[String]]("setup-script-name")
  val scriptBase = SettingKey[File]("setup-script-base")
  val setupScript = SettingKey[Option[File]]("embedded-db-setup-script")

  private def checkFile(f: File, s: Keys.TaskStreams) =
    if (f.exists())
      Some(f)
    else {
      s.log.info("Embedded Cassandra: setup script is not found at " + f.getAbsolutePath)
      None
    }

  private def setupStep(s: Keys.TaskStreams, fileOpt: Option[File]) = Tests.Setup( () => {
    s.log.info("Embedded Cassandra: starting")
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()

    fileOpt match {
      case None =>
        s.log.debug("Embedded Cassandra: skip setup step as setup script is not provided")
      case Some(someFile) =>
        s.log.info("Embedded Cassandra: loading test data " + someFile.getAbsolutePath)

        checkFile(someFile, s) match {
          case None =>
            s.log.error("Embedded Cassandra: failed to locate setup CQL. Defined as " + someFile.getAbsolutePath)
          case Some(f) =>
            val cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9142).build().connect
            val dataLoader = new CQLDataLoader(cluster)
            dataLoader.load(new FileCQLDataSet(f.getAbsolutePath))
            s.log.debug("Embedded Cassandra: ready")
        }
      }
    }
  )

  private def cleanupStep(s: Keys.TaskStreams) = Tests.Cleanup(() => {
    s.log.debug("Embedded Cassandra: stopping")
    try {
      EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
      s.log.debug("Embedded Cassandra stopped")
    } catch {
      case t:Exception => s.log.warn("Failed to stop Embedded Cassandra due to: " + t)
    }
  })

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    scriptBase := (Test / resourceDirectory).value,
    scriptName := Some("test-schema.cql"),
    setupScript := scriptName.value map (n => scriptBase.value / n),

    Test / testOptions ++= Seq(
      setupStep(streams.value, setupScript.value),
      cleanupStep(streams.value)
    )
  )
}
