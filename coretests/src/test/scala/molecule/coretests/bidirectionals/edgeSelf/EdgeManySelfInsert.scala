package molecule.coretests.bidirectionals.edgeSelf

import molecule.imports._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util._

class EdgeManySelfInsert extends MoleculeSpec {

  class setup extends Setup {
    val knownBy  = m(Person.name_(?).Knows.*(Knows.weight.Person.name))
  }

  "base/edge/target" >> {

    "new targets" in new setup {

      // Create Ann with multiple edges to new target entities Ben and Joe
      Person.name.Knows.*(Knows.weight.Person.name).insert("Ann", List((7, "Ben"), (8, "Joe")))

      // Bidirectional property edges have been inserted
knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
knownBy("Ben").get.head === List((7, "Ann"))
knownBy("Joe").get.head === List((8, "Ann"))
    }

    "existing targets" in new setup {

      val Seq(ben, joe) = Person.name.insert("Ben", "Joe").eids

      // Create Ann with multiple edges to existing target entities Ben and Joe
      Person.name.Knows.*(Knows.weight.person).insert("Ann", List((7, ben), (8, joe))).eids

      // Bidirectional property edges have been inserted
      knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
      knownBy("Ben").get.head === List((7, "Ann"))
      knownBy("Joe").get.head === List((8, "Ann"))
    }

    "nested edge only not allowed" in new Setup {

      // Can't save nested edges without including target entity
      (Person.name.Knows.*(Knows.weight).Person.name insert List(
        ("Ben", List(7, 8), "Joe")
      ) must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.ops.VerifyModel.noNestedEdgesWithoutTarget]  Nested edge ns `Knows` should link to " +
        s"target ns within the nested group of attributes."
    }
  }


  "base + edge/target" >> {

    "new targets" in new setup {

      // Create edges to new target entities
      val Seq(knowsBen, knowsJoe): Seq[Long] = Knows.weight.Person.name.insert(List((7, "Ben"), (8, "Joe"))).eids.grouped(3).map(_.head).toSeq

      // Connect base entity to edges
      Person.name.knows.insert("Ann", Set(knowsBen, knowsJoe))

      // Bidirectional property edges have been inserted
      knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
      knownBy("Ben").get.head === List((7, "Ann"))
      knownBy("Joe").get.head === List((8, "Ann"))
    }

    "existing targets" in new setup {

      val Seq(ben , joe) = Person.name.insert("Ben", "Joe").eids

      // Create edges to existing target entities
      val Seq(knowsBen, knowsJoe): Seq[Long] = Knows.weight.person.insert(List((7, ben), (8, joe))).eids.grouped(3).map(_.head).toSeq

      // Connect base entity to edges
      Person.name.knows.insert("Ann", Set(knowsBen, knowsJoe))

      // Bidirectional property edges have been inserted
      knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
      knownBy("Ben").get.head === List((7, "Ann"))
      knownBy("Joe").get.head === List((8, "Ann"))
    }
  }


  // Edge consistency checks

  "base/edge - <missing target>" in new Setup {

    // Can't allow edge without ref to target entity
    (Person.name.Knows.weight.insert must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.edgeComplete]  Missing target namespace after edge namespace `Knows`."
  }

  "<missing base> - edge - <missing target>" in new Setup {

    // Edge always have to have a ref to a target entity
    (Knows.weight.insert must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.edgeComplete]  Missing target namespace somewhere after edge property `Knows/weight`."
  }
}
