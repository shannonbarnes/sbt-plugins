
resolvers += ("whil-ivy" atS3 "s3://whil-ivy")

resolvers += "FrugalMechanic Snapshots" at "s3://whil-ivy"

libraryDependencies +=
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("com.frugalmechanic" % "fm-sbt-s3-resolver" % "0.19.0")

val whilPluginVersion = "1.1.11"

addSbtPlugin("com.whil" %% "sbt-whil" % whilPluginVersion)