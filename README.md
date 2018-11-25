![](project/resources/Molecule-logo.png)


Molecule is a type safe and intuitive Scala meta-DSL for the
[Datomic][datomic] database. 

Visit [ScalaMolecule.org](http://ScalaMolecule.org) to learn more or visit the [Molecule forum](https://groups.google.com/forum/#!forum/molecule-dsl)


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
```


## Producing Datalog
Molecule transform "molecules" like `Person.name.age.get` to [Datalog](https://docs.datomic.com/on-prem/query.html) queries 
for Datomic. The returned untyped data from Datomic is then casted by Molecule to the expected Scala type.

All queries are prepared at compile time by macros. So there is no overhead at runtime when running the queries. All
queries are ready to fire.


## Sync and Async APIs
Molecule offers both a synchronous and an asynchronous API for all query getters and transaction operations.


   
## Getting started

- [Introduction](http://scalamolecule.org/home/introduction) to Datomic/Molecule
- [Getting started](http://scalamolecule.org/manual/getting-started): define a schema and create a new Datomic database
- [Populate Database](http://scalamolecule.org/manual/insert): populate a Datomic database with Molecule
- [Molecule Seattle tutorial](http://scalamolecule.org/tutorials/seattle) examples of using Molecule


## Try demo project

1. `git clone https://github.com/scalamolecule/molecule-demo.git`
2. `cd molecule-demo`
3. `sbt compile`
4. Open in your IDE
5. Run tests and poke around...


## Use in your own project

Add the following to your build files: 

`project/build.properties`:

```scala
sbt.version=1.1.6
```

`project/buildinfo.sbt`:

```scala
addSbtPlugin("org.scalamolecule" % "sbt-molecule" % "0.6.2")
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
      "org.scalamolecule" %% "molecule" % "0.16.0",
      "com.datomic" % "datomic-free" % "0.9.5697"
    ),
    moleculeSchemas := Seq("app") // paths to your schema definition files...
  )
```
Molecule 0.16.0 for Scala 2.12.7 is available at
[Sonatype](https://oss.sonatype.org/content/repositories/releases/org/scalamolecule/molecule_2.12/).


#### Author
Marc Grue

#### Credits/inspiration/acknowledgements
- [Rogue](https://github.com/foursquare/rogue), type-safe Scala DSL for MongoDB
- [Datomisca](https://github.com/pellucidanalytics/datomisca), a Scala API for Datomic
- [SqlTyped](https://github.com/jonifreeman/sqltyped), Embedding SQL as an external DSL into Scala
- [JScala](https://github.com/nau/jscala), Scala macro that produces JavaScript from Scala code
- [Parboiled2](https://github.com/sirthias/parboiled2), A macro-based PEG parser generator for Scala

#### License
Molecule is licensed under the [Apache License 2.0](http://en.wikipedia.org/wiki/Apache_license)

[datomic]: http://www.datomic.com
