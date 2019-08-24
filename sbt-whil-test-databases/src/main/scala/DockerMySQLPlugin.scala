package com.whil.sbt

import sbt.Keys._
import sbt._

import scala.sys.process._

object DockerMySQLPlugin extends AutoPlugin {

  object autoImport {

    val scriptBase = SettingKey[File]("setup-script-base")
    val dbName = SettingKey[String]("default DB name")

    val startMySQL = taskKey[Unit]("starts docker mysql")
    val stopMySQL = taskKey[Unit]("stops docker mysql")

  }

  import autoImport._

  val dockerName = "mysql-test"
  val defaultDbName = "test_db"

  private def startMySQLTask(log: Logger, dbName: String, scriptBase: File) =  {
    log.info("Docker MySQL: starting")

    val command = s"docker run --rm --name $dockerName -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_DATABASE=$dbName -d --mount type=bind,source=${scriptBase.getAbsolutePath},target=/docker-entrypoint-initdb.d -p 3306:3306 mysql:latest"

    log.info(command)

    command !
  }

  private def stopMySQLTask(log: Logger) =  {
    log.info("Docker MySQL: stopping")
    s"docker stop $dockerName" !
  }

  private def setupStep(log: Logger, dbName: String, scriptBase: File) = Tests.Setup { () =>
    startMySQLTask(log, dbName, scriptBase)
  }

  private def cleanupStep(log: Logger) = Tests.Cleanup { () =>
    stopMySQLTask(log)
  }

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    scriptBase := (Test / resourceDirectory).value,
    dbName := defaultDbName,

    startMySQL := startMySQLTask(sLog.value, dbName.value, scriptBase.value),
    stopMySQL := stopMySQLTask(sLog.value),

    Test / testOptions ++= Seq(
      setupStep(sLog.value, dbName.value, scriptBase.value),
      cleanupStep(sLog.value)
    )
  )
}
