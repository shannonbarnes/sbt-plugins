package com.whil.sbt

import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import sbt.Keys.{version, _}
import sbt._

import scala.sys.process._

object WhilBackEndDockerPlugin extends AutoPlugin {

  override def requires = com.typesafe.sbt.packager.docker.DockerPlugin &&
    com.typesafe.sbt.packager.archetypes.JavaAppPackaging


  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    dockerBaseImage := "java:8",
    dockerUpdateLatest := true,
    Docker / version := version.value  +
      Process("git tag").lineStream.reverse.headOption.getOrElse("") +
      "-b" + sys.props.getOrElse("build_number", default = "local-dev"),
     dockerRepository := Some("whil")
  )

}
