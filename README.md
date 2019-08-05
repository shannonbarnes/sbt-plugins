# Whil SBT Plugins



## sbt-whil-cassandra

Plugin provides support for use of embedded Cassandra in the unit tests.

### Usage

Add the following to `plugins.sbt`:

````

addSbtPlugin("com.whil" %% "sbt-whil-cassandra" % "x.x.x")

````

And then add `enablePlugins(EmbeddedCassandraPlugin)` to your `build.sbt`.

This will start embedded cassandra instance is at `localhost:9142` before tests are started and then stopped after tests are completed.

It is also possible to run setup script to initialize datastore, pass location of the CQL script using `EmbeddedCassandra.setupScript` setting.
For example

````
EmbeddedCassandra.setupScript := Some(baseDirectory.value / "test/resources/test-dataset.cql")`.
````

## Contributing to sbt-plugins

Contributions are very welcome. Before you submit pull request make sure existing tests are passing and your new code is covered by tests.

To run the tests use:

````
sbt scripted
````

To run tests in one of the projects use the following:

````
sbt "project sbt-uplynk-cassandra" scripted
````



