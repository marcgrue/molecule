package molecule.tests.core.bidirectionals.edgeSelf

import molecule.tests.core.bidirectionals.dsl.Bidirectional._
import molecule.datomic.api.in1_out3._
import molecule.TestSpec
import molecule.core.ops.exception.VerifyModelException

class EdgeManySelfInsert extends TestSpec {

  class Setup extends BidirectionalSetup {
    val knownBy = m(Person.name_(?).Knows.*(Knows.weight.Person.name))
  }

  "base/edge/target" >> {

    "new targets" in new Setup {

      // Create Ann with multiple edges to new target entities Ben and Joe
      Person.name.Knows.*(Knows.weight.Person.name).insert("Ann", List((7, "Ben"), (8, "Joe")))

      // Bidirectional property edges have been inserted
      knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
      knownBy("Ben").get.head === List((7, "Ann"))
      knownBy("Joe").get.head === List((8, "Ann"))
    }

    "existing targets" in new Setup {

      val Seq(ben, joe) = Person.name.insert("Ben", "Joe").eids

      // Create Ann with multiple edges to existing target entities Ben and Joe
      Person.name.Knows.*(Knows.weight.person).insert("Ann", List((7, ben), (8, joe)))

      // Bidirectional property edges have been inserted
      knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
      knownBy("Ben").get.head === List((7, "Ann"))
      knownBy("Joe").get.head === List((8, "Ann"))
    }

    "nested edge only not allowed" in new Setup {

      // Can't save nested edges without including target entity
      (Person.name.Knows.*(Knows.weight).Person.name insert List(
        ("Ben", List(7, 8), "Joe")
      ) must throwA[VerifyModelException]).message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        s"[noNestedEdgesWithoutTarget]  Nested edge ns `Knows` should link to " +
        s"target ns within the nested group of attributes."
    }
  }


  "base + edge/target" >> {

    "new targets" in new Setup {

      // Create edges to new target entities
      val Seq(knowsBen, knowsJoe): Seq[Long] = Knows.weight.Person.name.insert(List((7, "Ben"), (8, "Joe"))).eids.grouped(3).map(_.head).toSeq

      // Connect base entity to edges
      Person.name.knows.insert("Ann", Set(knowsBen, knowsJoe))

      // Bidirectional property edges have been inserted
      knownBy("Ann").get.head === List((7, "Ben"), (8, "Joe"))
      knownBy("Ben").get.head === List((7, "Ann"))
      knownBy("Joe").get.head === List((8, "Ann"))
    }

    "existing targets" in new Setup {

      val Seq(ben, joe) = Person.name.insert("Ben", "Joe").eids

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
    (Person.name.Knows.weight.insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace after edge namespace `Knows`."
  }

  "<missing base> - edge - <missing target>" in new Setup {

    // Edge always have to have a ref to a target entity
    (Knows.weight.insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[edgeComplete]  Missing target namespace somewhere after edge property `Knows/weight`."
  }
}
