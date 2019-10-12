package com.whil.sbt

import sbt._

object WhilLibPlugin extends AutoPlugin {

  override def requires = fm.sbt.S3ResolverPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    Publish.settings ++
    Versioning.settings ++
    Release.settings ++
    (ContinuousIntegration.buildType := ContinuousIntegration.ScalaLib) ++
    ContinuousIntegration.settings


}
