package com.whil.sbt

import sbt.{Def, SettingKey}

object Release {

  lazy val isReleaseBuild = SettingKey[Boolean]("isReleaseBuild")

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    isReleaseBuild := sys.env.get("RELEASE_BUILD").isDefined
  )

}
