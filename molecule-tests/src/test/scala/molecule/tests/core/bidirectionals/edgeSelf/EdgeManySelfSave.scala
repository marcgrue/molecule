package molecule.tests.core.bidirectionals.edgeSelf

import molecule.tests.core.bidirectionals.dsl.Bidirectional._
import molecule.datomic.api.in1_out3._
import molecule.core.ops.exception.VerifyModelException
import molecule.setup.TestSpec

class EdgeManySelfSave extends TestSpec {

  class Setup extends BidirectionalSetup {
    val knownBy = m(Person.name_(?).Knows.weight.Person.name)
  }


  "base/edge/target" >> {

    "no nesting in save molecules" in new Setup {

      (Person.name("Ann").Knows.*(Knows.weight(7)).Person.name("Ben").save must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        s"[noNested]  Nested data structures not allowed in save molecules"

      // Insert entities, each having one or more connected entities with relationship properties
      val ben = Person.name.insert("Ben").eid
      (Person.name("Ben").Knows.*(Knows.weight(7).person(ben)).save must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        s"[noNested]  Nested data structures not allowed in save molecules"
    }


    // Since we can't nest in save-molecules, saves will be the same for cardinality one/many,
    // and the following two test will be the same as for tests in EdgeOneSelfSave

    "new target" in new Setup {

      Person.name("Ann").Knows.weight(7).Person.name("Ben").save.eids

      // Ann and Ben know each other with a weight of 7
      Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }


    "existing target" in new Setup {

      val ben = Person.name.insert("Ben").eid

      // Save Ann with weighed relationship to existing Ben
      Person.name("Ann").Knows.weight(7).person(ben).save.eids

      // Ann and Ben know each other with a weight of 7
      Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }
  }


  "base + edge/target" >> {

    "new target" in new Setup {

      // Create edges to new target entities
      val knowsBen = Knows.weight(7).Person.name("Ben").save.eid
      val knowsJoe = Knows.weight(8).Person.name("Joe").save.eid

      // Connect multiple edges
      Person.name("Ann").knows(knowsBen, knowsJoe).save

      // Ann and Ben know each other with a weight of 7
      Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ann", 8, "Joe"),
        ("Ben", 7, "Ann"),
        ("Joe", 8, "Ann")
      )
    }

    "existing target" in new Setup {

      val List(ben, joe) = Person.name.insert("Ben", "Joe").eids

      // Create edges to existing target entities
      val knowsBen = Knows.weight(7).person(ben).save.eid
      val knowsJoe = Knows.weight(8).person(joe).save.eid

      // Connect multiple edges
      Person.name("Ann").knows(knowsBen, knowsJoe).save

      // Ann and Ben know each other with a weight of 7
      Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ann", 8, "Joe"),
        ("Ben", 7, "Ann"),
        ("Joe", 8, "Ann")
      )
    }
  }


  // Edge consistency checks.
  // Any edge should always be connected to both a base and a target entity.
  "base/edge - <missing target>" in new Setup {
    // Can't save edge missing the target namespace (`Person`)
    // The edge needs to be complete at all times to preserve consistency.
    (Person.name("Ann").Knows.weight(5).save must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace after edge namespace `Knows`."
  }

  "<missing base> - edge - <missing target>" in new Setup {
    (Knows.weight(7).save must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace somewhere after edge property `Knows/weight`."
  }
}
