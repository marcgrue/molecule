package molecule.coretests.bidirectionals.edgeSelf

import molecule.api.in1_out4._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.ops.exception.VerifyModelException
import molecule.util._

class EdgeOneSelfInsert extends MoleculeSpec {

  class setup extends Setup {
    val loveOf = m(Person.name_(?).Loves.weight.Person.name)
  }

  "base/edge/target" >> {

    "new target" in new setup {

      // Insert 1 pair of entities with bidirectional property edge between them
      Person.name.Loves.weight.Person.name.insert("Ann", 7, "Ben")

      // Bidirectional property edge has been inserted
      loveOf("Ann").get === List((7, "Ben"))
      loveOf("Ben").get === List((7, "Ann"))
    }

    "existing target" in new setup {

      val ben = Person.name.insert("Ben").eid

      // Insert Ann with bidirectional property edge to existing Ben
      Person.name.Loves.weight.person.insert("Ann", 7, ben)

      // Bidirectional property edge has been inserted
      loveOf("Ann").get === List((7, "Ben"))
      loveOf("Ben").get === List((7, "Ann"))
    }
  }


  "base + edge/target" >> {

    "new target" in new Setup {

      // Create edges and Ben
      val List(lovesBen, benLoves, ben) = Loves.weight.Person.name.insert(7, "Ben").eids

      /*
          Ben and bidirectional edges created

                (-->)  lovesBen  -->
          (Ann)                       Ben
                (<--)  benLoves  <--

      */

      // lovesBen edge points to Ben
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

      // Base entity Ann points to one of the edges (doesn't matter which of them - Molecule connects Ann to both)
      Person.name.loves.insert("Ann", lovesBen)

      // Ann loves Ben and Ben loves Ann - that is 70% love
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
      Person.name.loves.insert("Ann", benLovesAnn)

      // Ann loves Ben and Ben loves Ann - that is 70% love
      Person.name.Loves.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        ("Ben", 7, "Ann")
      )
    }
  }


  "base/edge - <missing target>" in new Setup {
    // Can't allow edge without ref to target entity
    (Person.name.Loves.weight.insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace after edge namespace `Loves`."
  }

  "<missing base> - edge - <missing target>" in new Setup {
    // Edge always have to have a ref to a target entity
    (Loves.weight.insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace somewhere after edge property `Loves/weight`."
  }
}
