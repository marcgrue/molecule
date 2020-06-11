![](project/resources/Molecule-logo.png)


Molecule is a type safe and intuitive Scala meta-DSL for the
[Datomic][datomic] database. 

Visit [ScalaMolecule.org](http://ScalaMolecule.org) to learn more or visit the [Molecule forum](https://groups.google.com/forum/#!forum/molecule-dsl)

[![Gitter](https://badges.gitter.im/scalamolecule/Lobby.svg)](https://gitter.im/scalamolecule/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

## A meta-DSL

Molecule is a "meta-DSL" in the sense that _your domain terms_ form the tokens of your queries and 
transactions. 

So instead of fitting in your domain terms between commands like `SELECT name, age FROM Person` etc, 
or other query constructs in other database languages, you directly use your domain terms as tokens:

```scala
Person.name.age.get
```

This is possible since a schema is initially defined based on your domain terms:


```scala
trait Person {
  val name = oneString
  val age  = oneInt
}
```
When you compile your project with `sbt compile`, Molecule generates the necessary boilerplate code 
in order to be able to write the more intuitive query. Since the types of each attribute `name` and
`age` is encoded in the schema definition we'll also get typed data back from our query.

```scala
val personsWithAge: List[(String, Int)] = Person.name.age.get

// or asynchronously
val personsWithAgeFut: Future[List[(String, Int)]] = Person.name.age.getAsync
```

## Sync and Async APIs
Molecule offers both a synchronous and an asynchronous API for all query getters and transaction operations.


## Producing Datalog
Molecule transform "molecules" like `Person.name.age.get` to [Datalog](https://docs.datomic.com/on-prem/query.html) queries 
for Datomic. The returned untyped data from Datomic is then casted by Molecule to the expected Scala type.

All queries are prepared at compile time by macros. So there is no overhead at runtime when running the queries.

   
## Getting started

- [Introduction](http://www.scalamolecule.org/manual/quick-start/introduction/) to Datomic/Molecule
- [Quick-start](http://www.scalamolecule.org/manual/): define a schema and create a new Datomic database
- [Setup](http://www.scalamolecule.org/manual/setup/): populate a Datomic database with Molecule
- [Molecule Seattle tutorial](http://www.scalamolecule.org/resources/tutorials/seattle/) examples of using Molecule


## Try demo project

1. `git clone https://github.com/scalamolecule/molecule-demo.git`
2. `cd molecule-demo`
3. `sbt compile`
4. Open in your IDE
5. Run tests and poke around...


## Molecule in Scala project

Molecule is currently available for Scala (JVM, version 8 and later) and Scala.js (JavaScript).
Scala 2.12 and Scala 2.13 are supported.

Add the following to your build files: 

`project/build.properties`:

```scala
sbt.version=1.3.12
```

`project/buildinfo.sbt`:

```scala
addSbtPlugin("org.scalamolecule" % "sbt-molecule" % "0.9.0")
```

`build.sbt`:

```scala
lazy val yourProject = project.in(file("app"))
  .enablePlugins(MoleculePlugin)
  .settings(
    resolvers ++= Seq(
      "datomic" at "http://files.datomic.com/maven",
      "clojars" at "http://clojars.org/repo",
      Resolver.sonatypeRepo("releases")
    ),
    libraryDependencies ++= Seq(
      "org.scalamolecule" %% "molecule" % "0.22.4",
      "com.datomic" % "datomic-free" % "0.9.5697"
    ),
    moleculeSchemas := Seq("app") // paths to your schema definition files...
  )
```
Molecule cross-compilations available at maven central for Scala 
[2.13](https://repo1.maven.org/maven2/org/scalamolecule/molecule_2.13/) and
[2.12](https://repo1.maven.org/maven2/org/scalamolecule/molecule_2.12/).


## Molecule in a Scala.js project

Molecule AST's and other generic interfaces have been ported to Scala.js so
that you can also work with Molecule on the client side. 
See the [molecule-admin](https://github.com/scalamolecule/molecule-admin) project for
an example of how Molecule is used both on the server and client side.

`project/buildinfo.sbt`:

```scala
addSbtPlugin("org.scalamolecule" % "sbt-molecule" % "0.9.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.1.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
```

`build.sbt` (CrossType.Full example):

```scala
import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val yourProject = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))

lazy val yourProjectJVM = yourProject.jvm
  .enablePlugins(MoleculePlugin)
  .settings(
    resolvers ++= Seq(
      "datomic" at "http://files.datomic.com/maven",
      "clojars" at "http://clojars.org/repo",
      Resolver.sonatypeRepo("releases")
    ),
    libraryDependencies ++= Seq(
      "org.scalamolecule" %% "molecule" % "0.22.4",
      "com.datomic" % "datomic-free" % "0.9.5697"
    ),
    moleculeSchemas := Seq("app") // paths to your schema definition files...
  )

lazy val yourProjectJS = yourProject.js
  .settings(
    libraryDependencies ++= Seq(
      ("org.scalamolecule" %%% "molecule" % "0.22.4")
        .exclude("com.datomic", "datomic-free")
    ),
    moleculeSchemas := Seq("app") // paths to your schema definition files...
  )
```
Note how we exclude the Datomic dependency on the js side (since Datomic is obviously not 
compiled to javascript).

Molecule js-transpiled cross-compilations available at maven central for Scala 
[2.13](https://repo1.maven.org/maven2/org/scalamolecule/molecule_sjs1_2.13/) and
[2.12](https://repo1.maven.org/maven2/org/scalamolecule/molecule_sjs1_2.12/).

## Test
Test in IDE or with `sbt`:
```
sbt
sbt:molecule> test
sbt:molecule> testOnly molecule.coretests.expression.Comparison
```

#### Author
Marc Grue

#### Credits/inspiration/acknowledgements
- [Datomisca](https://github.com/pellucidanalytics/datomisca), a Scala API for Datomic

#### License
Molecule is licensed under the [Apache License 2.0](http://en.wikipedia.org/wiki/Apache_license)

[datomic]: http://www.datomic.com
