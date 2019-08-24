package com.whil.sbt

import com.datastax.driver.core.Cluster
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.FileCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import sbt._
import sbt.Keys._

object EmbeddedCassandraPlugin extends AutoPlugin {

  object autoImport {

    val scriptName = SettingKey[Option[String]]("setup-script-name")
    val scriptBase = SettingKey[File]("setup-script-base")
    val setupScript = SettingKey[Option[File]]("embedded-db-setup-script")

    val startCassandra = taskKey[Unit]("starts embedded cassandra")
    val loadCassandra = taskKey[Unit]("loads embedded-db-setup-script")
    val stopCassandra = taskKey[Unit]("stops embedded cassandra")

  }

  import autoImport._

  val defaultScriptName = "test-schema.cql"

  private def startCassandraTask(log: Logger) =  {
    log.info("Embedded Cassandra: starting")
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
  }

  private def stopCassandraTask(log: Logger) =  {
    log.info("Embedded Cassandra: stopping")
    try {
      EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
      log.info("Embedded Cassandra stopped")
    } catch {
      case t: Exception => log.warn(s"Failed to stop Embedded Cassandra due to: $t")
    }
  }

  private def loadCassandraTask(log: Logger, fileOpt: Option[File]) =
    fileOpt match {
      case None =>
        log.debug("Embedded Cassandra: skip setup step as setup script is not provided")
      case Some(file) if file.exists =>
        log.info("Embedded Cassandra: loading test data " + file.getAbsolutePath)
        val cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9142).build().connect
        val dataLoader = new CQLDataLoader(cluster)
        dataLoader.load(new FileCQLDataSet(file.getAbsolutePath))
        log.debug("Embedded Cassandra: ready")
      case Some(file) =>
        log.error("Embedded Cassandra: failed to locate setup CQL. Defined as " + file.getAbsolutePath)
    }

  private def setupStep(log: Logger, fileOpt: Option[File]) = Tests.Setup { () =>
    startCassandraTask(log)
    loadCassandraTask(log, fileOpt)
  }

  private def cleanupStep(log: Logger) = Tests.Cleanup { () =>
    stopCassandraTask(log)
  }

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    scriptBase := (Test / resourceDirectory).value,
    scriptName := Some(defaultScriptName),
    setupScript := scriptName.value map (n => scriptBase.value / n),

    startCassandra := startCassandraTask(sLog.value),
    loadCassandra := loadCassandraTask(sLog.value, setupScript.value),
    stopCassandra := stopCassandraTask(sLog.value),

    Test / testOptions ++= Seq(
      setupStep(sLog.value, setupScript.value),
      cleanupStep(sLog.value)
    )
  )
}
