package com.whil.sbt

import sbt.Def
import sbt.Keys.version

object Versioning {

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    version := {if (Release.isReleaseBuild.value) version.value.toUpperCase.stripSuffix("-SNAPSHOT") else version.value}
  )

}
