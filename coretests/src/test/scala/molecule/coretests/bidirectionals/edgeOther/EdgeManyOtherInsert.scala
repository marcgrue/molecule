package molecule.coretests.bidirectionals.edgeOther

import molecule.api._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.ops.exception.VerifyModelException
import molecule.util._

class EdgeManyOtherInsert extends MoleculeSpec {

  class setup extends Setup {
    val animalsCloseTo = m(Person.name_(?).CloseTo.*(CloseTo.weight.Animal.name))
    val personsCloseTo = m(Animal.name_(?).CloseTo.*(CloseTo.weight.Person.name))
  }

  "base/edge/target" >> {

    "new target" in new setup {

      // Insert molecules allow nested data structures. So we can conveniently
      // insert 2 entities each connected to 2 target entites via property edges
      Person.name.CloseTo.*(CloseTo.weight.Animal.name) insert List(("Ann", List((7, "Gus"), (6, "Leo"))))

      animalsCloseTo("Ann").get === List(List((7, "Gus"), (6, "Leo")))
      personsCloseTo("Gus").get === List(List((7, "Ann")))
      personsCloseTo("Leo").get === List(List((6, "Ann")))
    }


    "existing targets" in new setup {

      val List(gus, leo) = Animal.name.insert("Gus", "Leo").eids

      // Insert 2 Persons and connect them with existing Persons
      Person.name.CloseTo.*(CloseTo.weight.animal) insert List(("Ann", List((7, gus), (6, leo))))

      animalsCloseTo("Ann").get === List(List((7, "Gus"), (6, "Leo")))
      personsCloseTo("Gus").get === List(List((7, "Ann")))
      personsCloseTo("Leo").get === List(List((6, "Ann")))
    }

    "nested edge only not allowed" in new Setup {

      // Can't save nested edges without including target entity
      (Person.name.CloseTo.*(CloseTo.weight).Animal.name insert List(
        ("Ann", List(7, 8), "Gus")
      ) must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[noNestedEdgesWithoutTarget]  Nested edge ns `CloseTo` should link to " +
        s"target ns within the nested group of attributes."
    }
  }


  "base + edge/target" >> {

    "new targets" in new setup {

      // Create edges to new target entities
      val Seq(closeToGus, closeToLeo): Seq[Long] = CloseTo.weight.Animal.name.insert(List((7, "Gus"), (8, "Leo"))).eids.grouped(3).map(_.head).toSeq

      // Connect base entity to edges
      Person.name.closeTo.insert("Ann", Set(closeToGus, closeToLeo))

      // Bidirectional property edges have been inserted
      animalsCloseTo("Ann").get.head === List((7, "Gus"), (8, "Leo"))
      personsCloseTo("Gus").get.head === List((7, "Ann"))
      personsCloseTo("Leo").get.head === List((8, "Ann"))
    }

    "existing targets" in new setup {

      val Seq(gus , leo) = Animal.name.insert("Gus", "Leo").eids

      // Create edges to existing target entities
      val Seq(closeToGus, closeToLeo): Seq[Long] = CloseTo.weight.animal.insert(List((7, gus), (8, leo))).eids.grouped(3).map(_.head).toSeq

      // Connect base entity to edges
      Person.name.closeTo.insert("Ann", Set(closeToGus, closeToLeo))

      // Bidirectional property edges have been inserted
      animalsCloseTo("Ann").get.head === List((7, "Gus"), (8, "Leo"))
      personsCloseTo("Gus").get.head === List((7, "Ann"))
      personsCloseTo("Leo").get.head === List((8, "Ann"))
    }
  }


  // Edge consistency checks

  "base - edge - <missing target>" in new Setup {

    // Can't allow edge without ref to target entity
    (Person.name.CloseTo.weight.insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace after edge namespace `CloseTo`."
  }

  "<missing base> - edge - <missing target>" in new Setup {

    // Edge always have to have a ref to a target entity
    (CloseTo.weight.insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace somewhere after edge property `CloseTo/weight`."
  }

}
