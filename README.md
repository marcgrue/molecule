![](project/resources/Molecule-logo.png)


Molecule is a type safe and intuitive Scala query/modelling DSL for 
[Datomic][datomic] - the immutable database of facts. 

Visit [ScalaMolecule.org](http://ScalaMolecule.org) to learn more or visit the [Molecule forum](https://groups.google.com/forum/#!forum/molecule-dsl)


# What Molecule does

As an example: to find

_Names of twitter/facebook_page communities in neighborhoods of southern districts_
 
we can compose a "molecule query" that is very close to our
human sentence:

```scala
Community.name.`type`("twitter" or "facebook_page")
  .Neighborhood.District.region("sw" or "s" or "se")
```

Molecule transforms this at compile time (with macros) to a little more elaborate Datalog query string and
 input rules that finds those communities in the Datomic database:

<pre>
[:find ?a
 :in $ %
 :where
   [?ent :community/name ?a]
   (rule1 ?ent)
   [?ent :community/neighborhood ?c]
   [?c :neighborhood/district ?d]
   (rule2 ?d)]

INPUTS:
List(
  datomic.db.Db@xxx,
  [[[rule1 ?ent] [?ent :community/type ":community.type/twitter"]]
   [[rule1 ?ent] [?ent :community/type ":community.type/facebook_page"]]
   [[rule2 ?d] [?d :district/region ":district.region/sw"]]
   [[rule2 ?d] [?d :district/region ":district.region/s"]]
   [[rule2 ?d] [?d :district/region ":district.region/se"]]]
)
</pre>

By not having to write such complex Datalog queries and rules "by hand", Molecule 
allows you to

- Type less
- Make type safe queries with inferred return types
- Use your domain terms directly as query building blocks
- Model complex queries intuitively (easier to understand and maintain)


### Try demo

1. `git clone https://github.com/scalamolecule/molecule-demo.git`
2. `cd molecule-demo`
3. `sbt compile`
4. Open in your IDE
5. Run tests and poke around...


## Use in your own project

Add the following to your build files: 

`project/build.properties`:

```scala
sbt.version=0.13.13
```

`project/buildinfo.sbt`:

```scala
addSbtPlugin("org.scalamolecule" % "sbt-molecule" % "0.3.3")
```

`build.sbt`:

```scala
lazy val yourProject = project.in(file("demo"))
  .enablePlugins(MoleculePlugin)
  .settings(
    resolvers ++= Seq(
      "datomic" at "http://files.datomic.com/maven",
      "clojars" at "http://clojars.org/repo",
      Resolver.sonatypeRepo("releases")
    ),
    libraryDependencies ++= Seq(
      "org.scalamolecule" %% "molecule" % "0.11.0",
      "com.datomic" % "datomic-free" % "0.9.5561"
    ),
    moleculeSchemas := Seq("demo") // paths to your schema definition files...
  )
```
Molecule 0.11.0 for Scala 2.12.1 is available at 
[Sonatype](https://oss.sonatype.org/content/repositories/releases/org/scalamolecule/molecule_2.12/).

[Getting started](http://scalamolecule.org/manual/getting-started)...



   
### Read more

- [Introduction](http://scalamolecule.org/home/introduction) to Datomic/Molecule
- [Getting started](http://scalamolecule.org/manual/getting-started): define schema an initiate a Datomic database
- [Populate Database](http://scalamolecule.org/manual/insert): populate a Datomic database with Molecule
- [Molecule Seattle tutorial](http://scalamolecule.org/tutorials/seattle) examples of using Molecule (based on the 
[Datomic Seattle tutorial](http://docs.datomic.com/tutorial.html))


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
[seattle]: http://docs.datomic.com/tutorial.html
[moleculegroup]: https://groups.google.com/forum/#!forum/molecule-dsl
[pullrequests]: https://github.com/scalamolecule/molecule/pulls
[issues]: https://github.com/scalamolecule/molecule/issues
[moleculesbt]: https://github.com/scalamolecule/molecule/blob/master/project/build.scala

[setup]: http://scalamolecule.org/manual/setup
[schema]: http://scalamolecule.org/manual/schema
[deffile]: http://scalamolecule.org/molecule/blob/master/examples/src/main/scala/molecule/examples/seattle/schema/SeattleDefinition.scala
[populate]: http://scalamolecule.org/manual/insert
[tutorial]: http://scalamolecule.org/tutorials/seattle
[tutorialcode]: http://scalamolecule.org/molecule/blob/master/examples/src/test/scala/molecule/examples/seattle/SeattleTests.scala
[tutorialqueries]: http://scalamolecule.org/molecule/blob/master/examples/src/test/scala/molecule/examples/seattle/SeattleQueryTests.scala
[tutorialtransformations]: https://scalamolecule.org/molecule/blob/master/examples/src/test/scala/molecule/examples/seattle/SeattleTransformationTests.scala
