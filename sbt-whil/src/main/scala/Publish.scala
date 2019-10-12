package com.whil.sbt

import sbt.Keys.{publishMavenStyle, publishTo}
import sbt._
import Keys._

object Publish {

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    Compile / packageDoc / publishArtifact := false,
    publishMavenStyle := true,
    publishTo := {if (Release.isReleaseBuild.value) Some("whil-repository" at "s3://whil-ivy") else None},
    publishConfiguration := publishConfiguration.value.withOverwrite(true)
  )

}
