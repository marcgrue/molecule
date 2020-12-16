package molecule.tests.core.bidirectionals.edgeSelf

import molecule.core.transform.exception.Model2TransactionException
import molecule.tests.core.bidirectionals.dsl.bidirectional._
import molecule.datomic.api.out3._
import molecule.TestSpec

class EdgeOneSelfSave extends TestSpec {

  "base/edge/target" >> {

    "new target" in new BidirectionalSetup {

      /*
          When a "property edge" is created, Molecule automatically creates a reverse reference in the opposite direction:

          Ann --> annLovesBen (7) -->  Ben
            \                         /
              <-- benLovesAnn (7) <--

          This allow us to query from Ann to Ben and Ben to Ann in a uniform way.

          So we get 4 entities:
      */
      val List(ann, annLovesBen, benLovesAnn, ben) =
        Person.name("Ann").Loves.weight(7).Person.name("Ben").save.eids

      // Bidirectional property edges have been saved
      Person.name.Loves.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        // Reverse edge:
        ("Ben", 7, "Ann")
      )

      ann.touchMax(1) === Map(
        ":db/id" -> ann,
        ":Person/loves" -> annLovesBen,
        ":Person/name" -> "Ann"
      )

      ben.touchMax(1) === Map(
        ":db/id" -> ben,
        ":Person/loves" -> benLovesAnn,
        ":Person/name" -> "Ben"
      )

      ann.touchMax(2) === Map(
        ":db/id" -> ann,
        ":Person/loves" -> Map(
          ":db/id" -> annLovesBen,
          ":Loves/person" -> ben,
          ":Loves/weight" -> 7,
          ":molecule_Meta/otherEdge" -> benLovesAnn),
        ":Person/name" -> "Ann"
      )
    }


    "existing target" in new BidirectionalSetup {

      val ben = Person.name.insert("Ben").eid

      // Save Ann with weighed relationship to existing Ben
      Person.name("Ann").Loves.weight(7).person(ben).save.eids

      // Ann and Ben know each other with a weight of 7
      Person.name.Loves.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }
  }


  "base + edge/target" >> {

    "new target" in new BidirectionalSetup {

      /*
          Create love edges to/from Ben
          Ben and bidirectional edges created

                (-->)  lovesBen  -->
          (Ann)                       Ben
                (<--)  benLoves  <--
      */
      val List(lovesBen, benLoves, ben) = Loves.weight(7).Person.name("Ben").save.eids

      // lovesBen edge points to Ben
      ben.touchMax(1) === Map(
        ":db/id" -> ben,
        ":Person/loves" -> benLoves,
        ":Person/name" -> "Ben"
      )

      lovesBen.touchMax(1) === Map(
        ":db/id" -> lovesBen,
        ":Loves/person" -> ben,
        ":Loves/weight" -> 7,
        ":molecule_Meta/otherEdge" -> benLoves // To be able to find the other edge later
      )

      // Ben points to edge benLoves
      ben.touchMax(1) === Map(
        ":db/id" -> ben,
        ":Person/loves" -> benLoves,
        ":Person/name" -> "Ben"
      )

      // benLoves edge is ready to point back to a base entity (Ann)
      benLoves.touchMax(1) === Map(
        ":db/id" -> benLoves,
        ":Loves/weight" -> 7,
        ":molecule_Meta/otherEdge" -> lovesBen // To be able to find the other edge later
      )

      // Two edges pointing to and from Ben
      Loves.e.weight_(7).Person.name_("Ben").get === Seq(lovesBen)
      Person.name_("Ben").Loves.weight_(7).e.get === Seq(benLoves)

      // Base entity Ann points to one of the edges (doesn't matter which of them)
      val ann = Person.name("Ann").loves(lovesBen).save.eid

      ben.touchMax(1) === Map(
        ":db/id" -> ben,
        ":Person/loves" -> benLoves,
        ":Person/name" -> "Ben"
      )

      // Narcissistic tendencies not allowed
      (Person(ann).loves(ann).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        s"[valueStmts:biEdgeRefAttr]  Current entity and referenced entity ids can't be the same."

      // Ann and Ben know each other with a weight of 7
      Person.name.Loves.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }

    "existing target" in new BidirectionalSetup {

      val ben = Person.name.insert("Ben").eid

      // Create edges to existing Ben
      val List(annLovesBen, benLovesAnn) = Loves.weight.person.insert(7, ben).eids

      // Base entity Ann points to one of the edges (doesn't matter which of them - Molecule connects Ann to both)
      Person.name("Ann").loves(benLovesAnn).save

      // Ann loves Ben and Ben loves Ann - that is 70% love
      Person.name.Loves.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }
  }
}
