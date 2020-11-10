package molecule.examples.gremlin.gettingStarted

import molecule.core.util.MoleculeSpec
import molecule.datomic.peer.api.out4._
import molecule.examples.gremlin.dsl.modernGraph1._
import molecule.examples.gremlin.schema.ModernGraph1Schema
import org.specs2.specification.Scope

/*
  Bidirectional references - comparing with Gremlin tutorial at:

  http://tinkerpop.apache.org/docs/current/tutorials/getting-started/

  In the tutorial, the weight property is not used at all and we therefor here use a
  bidirectional self-reference `Friends` without the weight property:

      Person <--> Friends[Person]

  We also have a normal uni-directional relationship from `Person` to `Software`:

      Person --> Software

  See schema definition in
  examples/src/main/scala/molecule/examples/gremlin/schema/ModernGraphDefinition1.scala
*/

class Friends extends MoleculeSpec {

  class BidirectionalSelfRefSetup extends Scope {
    implicit val conn = recreateDbFrom(ModernGraph1Schema)

    /*
      Sample bidirectional friendships graph (with extra vadas--peter friendship added for more fun examples)

        marko (29) ---- josh (32)
          |
          |
        vadas (27) ---- peter (35)
    */

    // Software
    val List(lop, ripple) = Software.name.lang insert Seq(
      ("lop", "java"),
      ("ripple", "java")
    ) eids

    // People and software created
    val List(marko, vadas, josh, peter) = Person.name.age.software insert Seq(
      ("marko", 29, Set(lop)),
      ("vadas", 27, Set[Long]()),
      ("josh", 32, Set(lop, ripple)),
      ("peter", 35, Set(lop))
    ) eids

    // Friendships
    Person(marko).friends(vadas, josh).update

    // Extra friendship not in the tutorial so that we can make fof queries
    Person(vadas).friends.assert(peter).update
  }


  "The first 5 minutes" in new BidirectionalSelfRefSetup {

    // Marko's name
    // gremlin> g.V(1).values('name')
    Person(marko).name.get.head === "marko"

    // Marko knows (entity ids)
    // g.V(1).outE('knows')
    Person(marko).friends.get.head === Set(vadas, josh)

    // Marko knows (by name)
    // g.V(1).outE('knows').inV().values('name')
    // g.V(1).out('knows').values('name')
    Person(marko).Friends.name.get === Seq("vadas", "josh")
    Person(marko).friends.get.head === Set(vadas, josh)

    // We can uniformly query in the other direction too
    // vadas and josh also know marko
    Person(vadas).friends.get.head === Set(peter, marko)
    Person(josh).friends.get.head === Set(marko)

    // "Markos friends older than 30"
    // g.V(1).out('knows').has('age', gt(30)).values('name')
    Person(marko).Friends.name.age_.>(30).get === Seq("josh")
  }


  "Next 15 minutes" in new BidirectionalSelfRefSetup {

    // Find Marko id
    // g.V().has('name','marko')
    Person.e.name_("marko").get === Seq(marko)

    // Marko created software named...
    // g.V().has('name','marko').out('created')
    // g.V().has('name','marko').out('created').values('name')
    Person.name_("marko").Software.name.get === Seq("lop")


    // Ages of marko and vadas
    // g.V().has('name',within('vadas','marko')).values('age')
    Person.name_("marko", "vadas").age.get === Seq(27, 29)

    // Mean/average age of marko and vadas
    // g.V().has('name',within('vadas','marko')).values('age').mean()
    Person.name_("marko", "vadas").age(avg).get.head === 28.0


    // Who are the people that marko develops software with?
    // g.V().has('name','marko').out('created').in('created').values('name')

    // It's idiomatic for Datomic to split such query and use the output of
    // one query as input for the next one.

    // First find ids of software projects that marko has participated in
    val markoSoftware = Person.name_("marko").software.get.head.toSeq

    // Then find names of persons that have participated in those projects
    Person.name.software_(markoSoftware).get === List("peter", "josh", "marko")

    // Excluding marko from the result (re-using the first sub-query)
    // g.V().has('name','marko').as('exclude').out('created').in('created').where(neq('exclude')).values('name')
    Person.name.not("marko").software_(markoSoftware).get === List("peter", "josh")

    // Untyped generic queries are not directly handled by Molecule.
    // With a namespace prefix we though can similar and type safe results
    // g.V().group().by(label).by('name')
    Person.name.get === List("peter", "vadas", "josh", "marko")
    Software.name.get === List("ripple", "lop")
  }


  "What else could we ask?" in new BidirectionalSelfRefSetup {

    // What friends does each person have (nested lists)
    m(Person.name.Friends * Person.name).get === List(
      ("marko", List("vadas", "josh")),
      ("vadas", List("marko", "peter")),
      ("josh", List("marko")),
      ("peter", List("vadas"))
    )

    // Who has most friends
    Person.name.friends(count).get.sortBy(_._2).reverse === List(
      ("vadas", 2),
      ("marko", 2),
      ("peter", 1),
      ("josh", 1)
    )

    // Markos friends older than 30
    Person(marko).Friends.name.age.>(30).get === List(("josh", 32))

    // Josh's friends older than 30 (none)
    Person(josh).Friends.name.age.>(30).get === List()

    // Who knows young people?
    // Since we save bidirectional references we get friendships in both directions:
    Person.name.Friends.name.age.<(30).get === List(
      ("vadas", "marko", 29), // vadas knows marko who is 29
      ("josh", "marko", 29), // josh knows marko who is 29
      ("marko", "vadas", 27), // marko knows vadas who is 27
      ("peter", "vadas", 27) // peter knows vadas who is 27
    )

    // Which older people have young friends
    Person.name.age.>=(30).Friends.name.age.<(30).get === List(
      ("peter", 35, "vadas", 27), // peter (35) knows vadas (27)
      ("josh", 32, "marko", 29) // josh (32) knows marko (29)
    )

    // Older friends of Vadas + Vadas age
    Person.name.age.>=(30).Friends.e_(vadas).age.get === List(
      ("peter", 35, 27)
    )

    // How many young friends does the older people have?
    Person.name.age.>=(30).Friends.e(count).age_.<(30).get === List(
      ("josh", 32, 1), // josh (32) knows 1
      ("peter", 35, 1) // peter (35) knows 1
    )

    // Marko's friends and their friends
    Person.name("marko").Friends.name.Friends.name.get === List(
      ("marko", "vadas", "peter"),
      ("marko", "josh", "marko"),
      ("marko", "vadas", "marko")
    )

    // Marko's friends and their friends, nested
    m(
      Person.name("marko").Friends.*(
        Person.name.Friends.*(
          Person.name))).get === List(
      (
        "marko",
        List(
          ("vadas", List("marko", "peter")),
          ("josh", List("marko")))
      )
    )

    // Marko's friends and their friends (excluding marko)
    Person.name("marko").Friends.name.Friends.name.not("marko").get === List(
      ("marko", "vadas", "peter")
    )

    // Marko's friends' friends
    Person.name_("marko").Friends.Friends.name.not("marko").get === List(
      "peter"
    )

    // Marko's friends' friends that are not already marko's friends (or marko)
    val markoFriends = Person(marko).Friends.name.get.toSeq :+ "marko"
    Person(marko).Friends.Friends.name.not(markoFriends).get === List(
      "peter"
    )
  }
}