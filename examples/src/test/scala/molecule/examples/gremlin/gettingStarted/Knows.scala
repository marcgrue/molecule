package molecule.examples.gremlin.gettingStarted

import molecule._
import molecule.examples.gremlin.dsl.modernGraph2._
import molecule.examples.gremlin.schema.ModernGraph2Schema
import molecule.util.MoleculeSpec
import org.specs2.specification.Scope

/*
  Full property edge version (with more features than demonstrated in the tutorial)

  Gremlin comparison using tutorial at
  http://tinkerpop.apache.org/docs/current/tutorials/getting-started/

  In the tutorial, the weight property of the edges are not used. To see what we can use
  properties for we


      Person --> Knows(weight) --> Person
      Person <-- Knows(weight) <-- Person

  We also have the uni-directional relationship Created from Person to Software:

      Person --> Created(weight) ---> Software


  But here we'll use the
  property edge called `Knows` with a property `weight`

  at all and we therefor use a simple
  bidirectional self-reference called `knows` that points back to the same `Person` namespace.

  Then, to explore using the properties of edges, we'll use the original `Knows` relationship
  that also has a `weight` property.

  See schema definition in
  examples/src/main/scala/molecule/examples/gremlin/schema/ModernGraphDefinition.scala
*/

class Knows extends MoleculeSpec with DatomicFacade {

  class BidirectionalPropertyEdgeSetup extends Scope with DatomicFacade {
    implicit val conn = recreateDbFrom(ModernGraph2Schema)

    /*
      Sample bidirectional friendship graph (with extra vadas--peter friendship added for more fun examples)

        marko (29) --1.0-- josh (32)
          |
         0.5
          |
        vadas (27) --0.7-- peter (35)
    */

    // Software
    val List(lop, ripple) = Software.name.lang insert Seq(
      ("lop", "java"),
      ("riple", "java")
    ) eids

    // People and software created
    val List(
    marko, markoLop,
    vadas,
    josh, joshLop, joshRipple,
    peter, peterLop
    ) = m(Person.name.age.Created * Created.software.weight) insert Seq(
      ("marko", 29, Seq((lop, 0.4))),
      ("vadas", 27, Seq()),
      ("josh", 32, Seq((lop, 0.4), (ripple, 1.0))),
      ("peter", 35, Seq((lop, 0.2)))
    ) eids

    // Weighed friendships (property edges)

    // Creating multiple edges
    val markoKnows = Knows.person.weight.insert(List((vadas, 0.5), (josh, 1.0))).eids
    Person(marko).knows(markoKnows).update

    // Extra friendship not in the tutorial so that we can make fof queries
    Person(vadas).Knows.weight(0.7).person(peter).update
  }


  "The first 5 minutes" in new BidirectionalPropertyEdgeSetup {

    // Marko's name
    // gremlin> g.V(1).values('name')
    Person(marko).name.one === "marko"

    // Marko knows (entity ids)
    // g.V(1).outE('knows')
    Person(marko).Knows.person.get === Seq(josh, vadas)

    // Marko knows (by name)
    // g.V(1).outE('knows').inV().values('name')
    // g.V(1).out('knows').values('name')
    Person(marko).Knows.Person.name.get === Set("vadas", "josh")
    Person(marko).Knows.person.get === Seq(josh, vadas)

    // We can uniformly query in the other direction too
    // vadas and josh also know marko
    Person(vadas).Knows.person.get === Seq(peter, marko)
    Person(josh).Knows.person.get === Seq(marko)

    // "Markos knows older than 30"
    // g.V(1).out('knows').has('age', gt(30)).values('name')
    Person(marko).Knows.Person.name.age_.>(30).get === Seq("josh")
  }


  "Next 15 minutes" in new BidirectionalPropertyEdgeSetup {

    // Find Marko id
    // g.V().has('name','marko')
    Person.e.name_("marko").get === Seq(marko)

    // Marko created (refs to Created)
    // g.V().has('name','marko').out('created')
    Person.name_("marko").created.one === Set(markoLop)

    // Marko created software named...
    // g.V().has('name','marko').out('created').values('name')
    Person.name_("marko").Created.Software.name.get === Seq("lop")


    // Ages of marko and vadas
    // g.V().has('name',within('vadas','marko')).values('age')
    Person.name_("marko", "vadas").age.get === Seq(27, 29)

    // Mean/average age of marko and vadas
    // g.V().has('name',within('vadas','marko')).values('age').mean()
    Person.name_("marko", "vadas").age(avg).one === 28.0


    // Who are the people that marko develops software with?
    // g.V().has('name','marko').out('created').in('created').values('name')

    // It's idiomatic for Datomic to split such query and use the output of
    // one query as input for the next one.

    // First find ids of software projects that marko has participated in
    val markoSoftware = Person.name_("marko").Created.software.get

    // Then find names of persons that have participated in those projects
    Person.name.Created.software_(markoSoftware).get === List("peter", "josh", "marko")

    // Excluding marko from the result (re-using the first sub-query)
    // g.V().has('name','marko').as('exclude').out('created').in('created').where(neq('exclude')).values('name')
    Person.name.not("marko").Created.software_(markoSoftware).get === List("peter", "josh")

    // Untyped generic queries are not directly handled by Molecule.
    // With a namespace prefix we though can similar and type safe results
    // g.V().group().by(label).by('name')
    Person.name.get === List("peter", "vadas", "josh", "marko")
    Software.name.get === List("riple", "lop")
  }


  "What else could we ask?" in new BidirectionalPropertyEdgeSetup {

    // What friends does each person have (nested lists)
    m(Person.name.Knows * Person.name).get === List(
      ("marko", List("vadas", "josh")),
      ("vadas", List("marko", "peter")),
      ("josh", List("marko")),
      ("peter", List("vadas"))
    )

    // Who has most friends
    Person.name.Knows.person(count).get.sortBy(_._2).reverse === List(
      ("vadas", 2),
      ("marko", 2),
      ("peter", 1),
      ("josh", 1)
    )

    // Markos friends older than 30
    Person(marko).Knows.Person.name.age.>(30).get === List(("josh", 32))

    // Josh's friends older than 30 (none)
    Person(josh).Knows.Person.name.age.>(30).get === List()

    // Who friends young people?
    // Since we save bidirectional references we get friendships in both directions:
    Person.name.Knows.Person.name.age.<(30).get === List(
      ("vadas", "marko", 29), // vadas knows marko who is 29
      ("josh", "marko", 29), // josh knows marko who is 29
      ("marko", "vadas", 27), // marko knows vadas who is 27
      ("peter", "vadas", 27) // peter knows vadas who is 27
    )

    // Which older people have young friends
    Person.name.age.>=(30).Knows.Person.name.age.<(30).get === List(
      ("peter", 35, "vadas", 27), // peter (35) knows vadas (27)
      ("josh", 32, "marko", 29) // josh (32) knows marko (29)
    )

    // How many young friends does the older people have?
    Person.name.age.>=(30).Knows.Person.e(count).age_.<(30).get === List(
//    Person.name.age.>=(30).Knows.e(count).Person.age_.<(30).get === List(
      ("josh", 32, 1), // josh (32) knows 1
      ("peter", 35, 1) // peter (35) knows 1
    )

    // My friends and their friends
    Person.name("marko").Knows.Person.name.Knows.Person.name.get === List(
      ("marko", "vadas", "peter"),
      ("marko", "josh", "marko"),
      ("marko", "vadas", "marko")
    )

    // Same, nested
    m(
      Person.name("marko").Knows.*(
        Person.name.Knows.*(
          Person.name))).get === List(
      (
        "marko",
        List(
          ("vadas", List("marko", "peter")),
          ("josh", List("marko")))
        )
    )

    // My friends and their friends (excluding myself)
    Person.name("marko").Knows.Person.name.Knows.Person.name.not("marko").get === List(
      ("marko", "vadas", "peter")
    )

    // My friends' friends
    Person.name_("marko").Knows.Person.Knows.Person.name.not("marko").get === List(
      "peter"
    )

    // My friends' friends that are not already my friends (or myself)
    val ownCircle = Person(marko).Knows.Person.name.get :+ "marko"
    Person(marko).Knows.Person.Knows.Person.name.not(ownCircle).get === List(
      "peter"
    )
  }


  "What else could we ask?" in new BidirectionalPropertyEdgeSetup {

  }
}