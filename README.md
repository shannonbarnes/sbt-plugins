# Whil SBT Plugins

A responsitory for various SBT plugins

## sbt-whil-cassandra

Plugin provides support for use of embedded Cassandra in unit tests.

### Usage

Add the following to `plugins.sbt`:

````

addSbtPlugin("com.whil" %% "sbt-whil-cassandra" % "x.x.x")

````

And then add `enablePlugins(EmbeddedCassandraPlugin)` to your `build.sbt`.

This will start embedded cassandra instance at `localhost:9142` before tests are started and then stopped after tests are completed.

It is also possible to run setup script to initialize datastore, pass location of the CQL script using `EmbeddedCassandra.setupScript` setting.
For example (this value is also the default:

````
EmbeddedCassandra.setupScript := Some(baseDirectory.value / "test/resources/test-schema.cq")`.
````





