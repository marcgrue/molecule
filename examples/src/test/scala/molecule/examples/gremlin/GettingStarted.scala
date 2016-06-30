package molecule
package examples.gremlin

import molecule.examples.gremlin.dsl.modernGraph._
import molecule.examples.gremlin.schema.ModernGraphSchema
import molecule.util.MoleculeSpec


class GettingStarted extends MoleculeSpec with DatomicFacade {


  "Bidirectional card-one" >> {

    implicit val conn = recreateDbFrom(ModernGraphSchema)


    // Marriage - create ref via ref namespace
    val List(marko, lisa) = Person.name.Spouse.name insert Seq(
      ("marko", "lisa")
    ) eids

    // Marko and Lisa are married
    Person.name_("marko").Spouse.name.get === List("lisa")
    Person.name_("lisa").Spouse.name.get === List("marko")

    // When we traverse forth and back we end at the starting point (marko) as expected
    Person.name_("marko").Spouse.name.Spouse.name.get === List(("lisa", "marko"))
    Person.name_("lisa").Spouse.name.Spouse.name.get === List(("marko", "lisa"))

    // Divorce (retract both spouse refs)
    Person(marko).spouse().update

    // Both Marko and Lisa are now singles
    Person(marko).spouse.get === List()
    Person(lisa).spouse.get === List()

    // Anna comes into the picture
    val anna = Person.name.insert("anna").eid

    // Marko marries again, now with Anna
    // Add ref as value to ref attr
    // This automatically retracts ref from lisa to marko!
    Person(marko).spouse(anna).update

    // Marko and Anna are married
    Person(marko).spouse.get === List(anna)
    Person(anna).spouse.get === List(marko)

    // Lisa is single
    Person(lisa).spouse.get === List()

    // Rebecca pops up
    val rebecca = Person.name.insert("rebecca").eid

    // Directly to third marriage...
    // Molecule performs three operations:
    // 1. Retract reverse ref from anna to marko
    // 2. Add new ref from marko to rebecca
    // 3. Add reverse ref from rebecca to marko
    // Since the spouse ref is of cardinality one, Datomic automatically retracts
    // the old ref from marko to anna before adding the new ref to rebecca (2).
    Person(marko).spouse(rebecca).update

    // Both Marko and Rebecca are married to each other
    Person(marko).spouse.get === List(rebecca)
    Person(rebecca).spouse.get === List(marko)

    // Anna has been made single too as part of the last update
    Person(anna).spouse.get === List()

    // Rebecca dies - Datomic takes care of retracting any ref pointing to
    // an entity being retracted. Ref from Marko to Rebecca is therefore
    // automatically retracted when Rebecca entity is retracted.
    rebecca.retract

    // Marko is single again
    Person(marko).spouse.get === List()
    Person(rebecca).spouse.get === List() // Rebecca doesn't even exist anymore
  }


  "Bidirectional card-many" >> {

    implicit val conn = recreateDbFrom(ModernGraphSchema)

    // Marriage - create ref via ref namespace
    val List(marko, lisa) = Person.name.Spouses.name insert Seq(
      ("marko", "lisa")
    ) eids

    // Marko and Lisa are married
    Person(marko).spouses.get === List(Set(lisa))
    Person(lisa).spouses.get === List(Set(marko))

    Person.name_("marko").Spouses.name.get === List("lisa")
    Person.name_("lisa").Spouses.name.get === List("marko")

    // When we traverse forth and back we end at the starting point (marko) as expected
    Person.name_("marko").Spouses.name.Spouses.name.get === List(("lisa", "marko"))
    Person.name_("lisa").Spouses.name.Spouses.name.get === List(("marko", "lisa"))

    // Divorce (retract both spouses refs)
    Person(marko).spouses().update

    // Both Marko and Lisa are now singles
    Person(marko).spouses.get === List()
    Person(lisa).spouses.get === List()

    // Anna comes into the picture
    val anna = Person.name.insert("anna").eid

    //    println("anna   : " + anna)

    // Marko marries again, now with Anna
    // Add ref as value to ref attr
    // Molecule automatically retracts ref from lisa to marko!
    Person(marko).spouses(anna).update

    // Marko and Anna are married
    Person(marko).spouses.get === List(Set(anna))
    Person(anna).spouses.get === List(Set(marko))

    // Lisa is single
    Person.name_("lisa").spouses.get === List()

    // Rebecca is flirting
    val rebecca = Person.name.insert("rebecca").eid

    //    println("rebecca: " + rebecca)

    // Directly to third marriage...
    Person(marko).spouses(rebecca).update

    // Surprise! Marko now has two wives!
    Person(marko).spouses.get === List(Set(rebecca, anna))
    // .. and anna hasn't divorced marko yet
    Person(anna).spouses.get === List(Set(marko))

    // Since the spouses ref is now of cardinality many, Datomic doesn't automatically
    // retract any old refs when adding new ones since it can't know which to retract.
    // So the old ref from marko to anna still remains after adding the new ref to rebecca!

    // Molecule only performs two operations for card-many refs
    // 1. Add new ref from marko to rebecca
    // 2. Add reverse ref from rebecca to marko

    // As with any non-bidirectional cardinality-many ref we need to remove unwanted
    // reference values manually
    Person(marko).spouses.remove(anna).update

    // Now marko has the expected number of wives and anna is divorced
    Person(marko).spouses.get === List(Set(rebecca))
    Person(anna).spouses.get === List()

    // .. and rebecca is married to marko
    Person(rebecca).spouses.get === List(Set(marko))

    // Rebecca dies. Datomic takes care of retracting any ref pointing to
    // an entity being retracted. All refs from Marko to Rebecca (1 in this case)
    // is therefore automatically retracted when the Rebecca entity is retracted.
    rebecca.retract

    // Marko is single again
    Person(marko).spouses.get === List()
    // and rebecca doesn't exist anymore
    Person(rebecca).name.spouses.get === List()
  }


  "Bidirectional card-many queries" >> {

    implicit val conn = recreateDbFrom(ModernGraphSchema)


    // Person
    val List(marko, vadas, josh, peter) = Person.name.age insert Seq(
      ("marko", 29),
      ("vadas", 27),
      ("josh", 32),
      ("peter", 35)
    ) eids

    // Software
    val List(lop, ripple) = Software.name.lang insert Seq(
      ("lop", "java"),
      ("riple", "java")
    ) eids

    /* Sample bidirectional friendships graph (
    (from http://tinkerpop.apache.org/docs/current/tutorials/getting-started/)

      marko (29) ---- josh (32)
        |
        |
      vadas (27) ---- peter (35)

    */
    Person(marko).friends(vadas, josh).update
    Person(peter).friends(vadas).update

    // Created
    val markoCreations = Created.software.weight.insert(lop, 0.4).eidSet
    Person(marko).created(markoCreations).update

    val joshCreations = Created.software.weight.insert(Seq((lop, 0.4), (ripple, 1.0))).eidSet
    Person(josh).created(joshCreations).update

    val peterCreations = Created.software.weight.insert(lop, 0.2).eidSet
    Person(peter).created(peterCreations).update


    // "Getting started" .........................................

    // Marko's name
    // gremlin> g.V(1).values('name')
    Person(marko).name.one === "marko"

    // Marko knows (entity ids)
    // g.V(1).outE('knows')
    Person(marko).friends.one === Set(vadas, josh)

    // Marko knows (by name)
    // g.V(1).outE('knows').inV().values('name')
    // g.V(1).out('knows').values('name')
    Person(marko).Friends.name.get === Set("vadas", "josh")
    Person(marko).friends.one === Set(vadas, josh)

    // vadas and josh also know marko
    Person(vadas).friends.one === Set(peter, marko)
    Person(josh).friends.one === Set(marko)

    // "Markos friends older than 30"
    // g.V(1).out('knows').has('age', gt(30)).values('name')
    Person(marko).Friends.name.age_.>(30).one === "josh"


    // "Next 15 minutes" ......................................

    // Find Marko id
    // g.V().has('name','marko')
    Person.e.name_("marko").get === Seq(marko)

    // Marko created (refs to Created)
    // g.V().has('name','marko').out('created')
    Person.name_("marko").created.get === Seq(markoCreations)

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

    // Generic queries as
    // g.V().group().by(label).by('name')
    // are not so interesting in Molecule since we deal with namespaced/typed attributes only


    // What else could we ask? ........................................................

    // What friends does each person have (nested list)
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

    // How many young friends does the older people have?
    Person.name.age.>=(30).Friends.e(count).age_.<(30).get === List(
      ("josh", 32, 1), // josh (32) knows 1
      ("peter", 35, 1) // peter (35) knows 1
    )

    // My friends and their friends
    Person.name("marko").Friends.name.Friends.name.get === List(
      ("marko", "vadas", "peter"),
      ("marko", "josh", "marko"),
      ("marko", "vadas", "marko")
    )

    // Same, nested
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

    // My friends and their friends (excluding myself)
    Person.name("marko").Friends.name.Friends.name.not("marko").get === List(
      ("marko", "vadas", "peter")
    )

    // My friends' friends
    Person.name_("marko").Friends.Friends.name.not("marko").get === List(
      "peter"
    )

    // My friends' friends that are not already my friends (or myself)
    val markoFriends = Person(marko).Friends.name.get :+ "marko"
    Person(marko).Friends.Friends.name.not(markoFriends).get === List(
      "peter"
    )

    // etc...
  }

//  "First 5 minutes" in new ModernGraphSetup(ModernGraphSchema) {
//
//    ok
//
//    // We find by the created entity id saved in the val `marko`
//
//    // Marko's name
//    // gremlin> g.V(1).values('name')
//    Person(marko).name.one === "marko"
//
//    // Marko knows (entity ids)
//    // g.V(1).outE('knows')
//    Person(marko).knows.get === Seq(markoFriends)
//
//    // Marko knows (by name)
//    // g.V(1).outE('knows').inV().values('name')
//    // g.V(1).out('knows').values('name')
//    Person(marko).Knows.Person.name.get === Seq("vadas", "josh")
//
//    // "Markos friends older than 30"
//    // g.V(1).out('knows').has('age', gt(30)).values('name')
//    Person(marko).Knows.Person.name.age_.>(30).one === "josh"
//  }


//  "Next 15 minutes" in new ModernGraphSetup(ModernGraphSchema) {
//
//    // (For the creation part - se GettingStartedSetup above...)
//
//    // Find Marko id
//    // g.V().has('name','marko')
//    Person.e.name_("marko").get === Seq(marko)
//
//    // Marko created (refs to Created)
//    // g.V().has('name','marko').out('created')
//    Person.name_("marko").created.get === Seq(markoCreations)
//
//    // Marko created software named...
//    // g.V().has('name','marko').out('created').values('name')
//    Person.name_("marko").Created.Software.name.get === Seq("lop")
//
//
//    // Ages of marko and vadas
//    // g.V().has('name',within('vadas','marko')).values('age')
//    Person.name_("marko", "vadas").age.get === Seq(27, 29)
//
//    // Mean/average age of marko and vadas
//    // g.V().has('name',within('vadas','marko')).values('age').mean()
//    Person.name_("marko", "vadas").age(avg).one === 28.0
//
//
//    // Who are the people that marko develops software with?
//
//    // It's ideomatic for Datomic to split such query and use the output of
//    // one query as input for the next one.
//
//    // g.V().has('name','marko').out('created').in('created').values('name')
//
//    // First find ids of software projects that marko has participated in
//    val markoSoftware = Person.name_("marko").Created.software.get
//
//    // Then find names of persons that have participated in those projects
//    Person.name.Created.software_(markoSoftware).get === List("peter", "josh", "marko")
//
//    // Excluding marko from the result (re-using the first sub-query)
//    // g.V().has('name','marko').as('exclude').out('created').in('created').where(neq('exclude')).values('name')
//    Person.name.not("marko").Created.software_(markoSoftware).get === List("peter", "josh")
//
//    // Generic queries as
//    // g.V().group().by(label).by('name')
//    // are not so interesting in Molecule since we deal with namespaced/typed attributes only
//  }
//
//
//  "What else could we ask?" in new ModernGraphSetup(ModernGraphSchema) {
//
//    // Markos friends older than 30
//    Person(marko).Knows.Person.name.age.>(30).get === List(("josh", 32))
//
//    // Who knows young people?
//    // Since Datomic references are directed from one entity
//    // to the referenced entity, we only get the "knows" in one direction:
//    Person.name.Knows.Person.name.age.<(30).get === List(
//      ("marko", "vadas", 27) // marko knows vadas who is 27
//    )
//    // We can though go backwards also:
//    Person.name.age.<(30).Knows.Person.name.get === List(
//      ("marko", 29, "josh"), // marko who is 29 is known by josh
//      ("marko", 29, "vadas") // marko who is 29 is known by vadas
//    )
//
//    // Language-wise we don't care about database directions and would have
//    // intuitively expected this result below. To get this we could merge the two
//    // results sets after re-arranging the order of attribute values of the second result:
//    Person.name.Knows.Person.name.age.<(30).get ++
//      Person.name.age.<(30).Knows.Person.name.get.map(t => (t._3, t._1, t._2)) === List(
//      ("marko", "vadas", 27), // marko knows vadas who is 27
//      ("josh", "marko", 29),  // josh knows marko who is 29
//      ("vadas", "marko", 29)  // vadas knows marko who is 29
//    )
//
//  }

//
//  "Bidirectional" in new ModernGraphSetup(ModernGraphSchema) {
//
//    // Markos friends older than 30
//    Person(marko).Knows.Person.name.age.>(30).get === List(("josh", 32))
//
//    // Who knows young people?
//    // Since Datomic references are directed from one entity
//    // to the referenced entity, we only get the "knows" in one direction:
//    Person.name.Knows.Person.name.age.<(30).get === List(
//      ("marko", "vadas", 27) // marko knows vadas who is 27
//    )
//    // We can though go backwards also:
//    Person.name.age.<(30).Knows.Person.name.get === List(
//      ("marko", 29, "josh"), // marko who is 29 is known by josh
//      ("marko", 29, "vadas") // marko who is 29 is known by vadas
//    )
//
//    // Language-wise we don't care about database directions and would have
//    // intuitively expected this result below. To get this we could merge the two
//    // results sets after re-arranging the order of attribute values of the second result:
//    Person.name.Knows.Person.name.age.<(30).get ++
//      Person.name.age.<(30).Knows.Person.name.get.map(t => (t._3, t._1, t._2)) === List(
//      ("marko", "vadas", 27), // marko knows vadas who is 27
//      ("josh", "marko", 29), // josh knows marko who is 29
//      ("vadas", "marko", 29) // vadas knows marko who is 29
//    )
//  }


//  "more..." in new ModernGraphSetup(ModernGraphSchema) {
    //    // https://github.com/tinkerpop/gremlin/wiki/Backtrack-Pattern
    //    // "get the names of those songs that follow Dark Star and are sung by Jerry Garcia"
    //    Song.id(89).FollowedBy.name.Singer.name("Jerry Garcia").get(3) === List("Eyes of the World", "Sing me back home", "Morning Dew")
    //
    //    // https://github.com/tinkerpop/gremlin/wiki/Except-Retain-Pattern
    //    // “Who are my friends friends that are not already my friends?”
    //    Person(marko).Knows.Person('friend).Knows.Person.!=('friend).getFirst === 5
    //
    //    // https://github.com/tinkerpop/gremlin/wiki/Flow-Rank-Pattern
    //    // "Who created projects"
    //    Person.e.Created.Project.e === List(
    //      (marko, lop),
    //      (josh, lop),
    //      (peter, lop),
    //      (josh, riple)
    //    )
    //    // "Who created most projects"
    //    Person.e(count).Created.Project === List(
    //      marko -> 1,
    //      josh -> 2,
    //      peter -> 1
    //    )
    //
    //    // https://github.com/thinkaurelius/titan/wiki/Gremlin-Query-Language
    //
    //    Being.name("hercules").Father.Father.name.getFirst === "saturn"
    //
    //    Being.name("hercules").e === 123
    //
    //    Being.name("hercules").Father.`type`.Mother.`type` === List("god", "human")
    //
    //    // "Who did hercules battle?"
    //    Being.name("hercules").Battled.Being.name.`type` === List(
    //      ("nemean", "monster"),
    //      ("hydra", "monster"),
    //      ("cererus", "monster")
    //    )
//    ok
//  }

}