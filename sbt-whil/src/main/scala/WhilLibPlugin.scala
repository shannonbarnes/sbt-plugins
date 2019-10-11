package com.whil.sbt

import ohnosequences.sbt.SbtS3Resolver.autoImport._
import sbt.Keys._
import sbt._

import scala.sys.process._

object WhilLibPlugin extends AutoPlugin {
  import autoImport._

  override def requires = ohnosequences.sbt.SbtS3Resolver

  object autoImport {
    val ivyBucket = SettingKey[String]("default DB name")
  }

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
      ivyBucket := "whil-ivy",
      publishMavenStyle := false,


      publishTo := {
      //val prefix = if (isSnapshot.value) "snapshots" else "releases"
      Some(s3resolver.value(s"Whil Ivy Repository", s3(ivyBucket.value)) withIvyPatterns)
    }

    //dockerBaseImage := "java:8",
    //dockerUpdateLatest := true,
    //Docker / version := version.value  +
    //  Process("git tag").lineStream.reverse.headOption.getOrElse("") +
    //  "-b" + sys.props.getOrElse("build_number", default = "local-dev"),
    // dockerRepository := Some("whil")
  )

}
