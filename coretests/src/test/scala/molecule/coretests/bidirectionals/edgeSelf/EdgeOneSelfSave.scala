package molecule.coretests.bidirectionals.edgeSelf

import molecule.imports._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util._

class EdgeOneSelfSave extends MoleculeSpec {


  "base/edge/target" >> {

    "new target" in new Setup {

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
    }


    "existing target" in new Setup {

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

    "new target" in new Setup {

      // Create love edges to/from Ben
      val List(lovesBen, benLoves, ben) = Loves.weight(7).Person.name("Ben").save.eids

      /*
          Ben and bidirectional edges created

                (-->)  lovesBen  -->
          (Ann)                       Ben
                (<--)  benLoves  <--

      */

      // lovesBen edge points to Ben
      lovesBen.touch(1) === Map(
        ":db/id" -> lovesBen,
        ":loves/person" -> ben,
        ":loves/weight" -> 7,
        ":molecule_Meta/otherEdge" -> benLoves // To be able to find the other edge later
      )

      // Ben points to edge benLoves
      ben.touch(1) === Map(
        ":db/id" -> ben,
        ":person/loves" -> benLoves,
        ":person/name" -> "Ben"
      )

      // benLoves edge is ready to point back to a base entity (Ann)
      benLoves.touch(1) === Map(
        ":db/id" -> benLoves,
        ":loves/weight" -> 7,
        ":molecule_Meta/otherEdge" -> lovesBen // To be able to find the other edge later
      )

      // Two edges pointing to and from Ben
      Loves.e.weight_(7).Person.name_("Ben").get === Seq(lovesBen)
      Person.name_("Ben").Loves.weight_(7).e.get === Seq(benLoves)

      // Base entity Ann points to one of the edges (doesn't matter which of them)
      val ann = Person.name("Ann").loves(lovesBen).save.eid

      // Narcissistic tendencies not allowed
      (Person(ann).loves(ann).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Current entity and referenced entity ids can't be the same."

      // Ann and Ben know each other with a weight of 7
      Person.name.Loves.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }

    "existing target" in new Setup {

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
