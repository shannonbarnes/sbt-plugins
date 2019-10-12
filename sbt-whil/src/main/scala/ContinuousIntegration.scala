package com.whil.sbt

import sbt.{Def, SettingKey, TaskKey}
import sbt._
import Keys._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._

object ContinuousIntegration {
  val DockerImage = "DockerImage"
  val ScalaLib = "ScalaLib"

  private val doPublish = Def.taskDyn {
    if(buildType.value == DockerImage)
      Def.task {
        (Docker / publish).value
      }
    else
      Def.task {
        publish.value
      }
  }

  lazy val buildType = SettingKey[String]("buildType")
  lazy val ciPublish = TaskKey[Unit]("ciPublish")
  lazy val ciTest = TaskKey[Unit]("ciTest")

  lazy val settings: Seq[Def.Setting[_]] = Seq(
      ciPublish := doPublish.value,
      ciTest := (Test / test).value
  )

}
