package molecule.bidirectional.edgeSelf

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeManySelfUpdate extends MoleculeSpec {


  "add" in new Setup {

    // In order to maintain data consistency we can't create property edges in isolation.
    // We can therefore not "add" them to existing entities as we could with simple
    // reference values as we saw in the update:replace1/multiple tests in `SelfMany`.
    (living_Person(ann).knows.add(42L).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Adding edge ids with " +
      s"`edgeAttr.add(someEdgeId)` is not allowed. It could be an indication that you are trying to " +
      s"use an existing edge twice which is not allowed."

    // Adding an edge from an existing entity to another entity therefore involves
    // creating the edge entity itself having a reference to either an existing target
    // entity or a newly created one.

    val ann = living_Person.name("Ann").save.eid

    // New edge and new target entity
    // Update Ann with new friendship to new Ben
    living_Person(ann).Knows.weight(7).Person.name("Ben").update

    // New edge with reference to existing target entity
    val joe = living_Person.name("Joe").save.eid

    // Update Ann with new friendship to existing Joe
    living_Person(ann).Knows.weight(6).person(joe).update

    // Both bidirectional edges have been added from/to Ann
    living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"), (6, "Joe"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((6, "Ann"))


    // Can't update multiple values of cardinality-one attribute `name`
    (living_Person(ann).Knows.weight(7).Person.name("Joe", "Liz").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
      "\n  living_Person ... name(Joe, Liz)"

    // As with save molecules nesting is not allowed in update molecules
    (living_Person(ann).Knows.*(living_Knows.weight(4)).Person.name("Joe").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_Knows`."

    (living_Person(ann).Knows.*(living_Knows.weight(4)).person(joe).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_Knows`."

    (living_Person(ann).Knows.*(living_Knows.weight(4).Person.name("Joe")).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_Knows`."

    (living_Person(ann).Knows.*(living_Knows.weight(4).person(joe)).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_Knows`."


    // Note that an edge always have only one target entity.
    // So we can't add multiple (won't compile)
    // living_Person(ann).Knows.weight(6).person(42L, 43L).update

    // Each edge has only 1 target entity so we can't use nested structures on the target namespace
    // (living_Person.name("Ben").Knows.weight(7).Person.*(living_Person.name("Joe")).update
    //                                                   ^ nesting of edge target namespace not available
  }


  "replace" in new Setup {

    val ids = living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
      ("Ann", List((6, "Ben"), (7, "Joe"))),
      ("Liz", List((9, "Tom")))
    ) eids

    // Entities
    val List(ann, ben, joe, liz, tom) = living_Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, _, annJoe, _, lizTom, _) = living_Knows.e.weight_.get

    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((6, "Ben"), (7, "Joe"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((6, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((9, "Tom"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((9, "Liz"))

    // edgeAttr.replace(old -> new) not available for edges
    // To enforce consistency, edges are not allowed to be replaced with each other
    (living_Person(ann).knows.replace(annBen -> lizTom).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Replacing edge ids " +
      s"with `edgeAttr.replace(old -> new)` is not allowed. It could be an indication that you are " +
      s"trying to replace the old edge with an existing edge which is not allowed."

    // Replace edge in 2 steps instead:

    // 1. Remove friendship with Ben
    living_Person(ann).knows.remove(annBen).update

    // 2. Update Ann with new friendship to existing Liz
    living_Person(ann).Knows.weight(8).person(liz).update

    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((7, "Joe"), (8, "Liz"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((9, "Tom"), (8, "Ann"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((9, "Liz"))
  }


  "remove" in new Setup {

    living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = living_Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, annJoe, annLiz, annTom, annUlf) = living_Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove single edge
    living_Person(ann).knows.remove(annBen).update

    // Remove multiple edges
    living_Person(ann).knows.remove(annJoe, annLiz).update

    // Remove Seq of edges
    living_Person(ann).knows.remove(Seq(annTom)).update

    living_Person.name_("Ann").Knows.weight.Person.name.get === List((9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove all edges (apply no values)
    living_Person(ann).knows().update

    living_Person.name_("Ann").Knows.weight.Person.name.get === List()
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List()
  }


  "apply" in new Setup {

    living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = living_Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, annJoe, annLiz, annTom, annUlf) = living_Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove single edge
    living_Person(ann).knows.remove(annBen).update

    // Remove multiple edges
    living_Person(ann).knows.remove(annJoe, annLiz).update

    // Remove Seq of edges
    living_Person(ann).knows.remove(Seq(annTom)).update

    living_Person.name_("Ann").Knows.weight.Person.name.get === List((9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove all edges (apply no values)
    living_Person(ann).knows().update

    living_Person.name_("Ann").Knows.weight.Person.name.get === List()
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List()
  }


  "retract edge" in new Setup {

    living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = living_Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, annJoe, annLiz, annTom, annUlf) = living_Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Retract single edge
    annBen.retract

    // Retract multiple edges
    Seq(annJoe, annLiz, annTom).map(_.retract)

    // Edges in both directions have been retracted
    living_Person.name_("Ann").Knows.weight.Person.name.get === List((9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Retract all edges
    living_Knows.person_.e.get.map(_.retract)

    living_Person.name_("Ann").Knows.weight.Person.name.get === List()
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List()
  }


  "retract base/target entity" in new Setup {

    living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = living_Person.e.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Retract target entity
    ulf.retract

    // Edges are components of the retracted entity, so they also are retracted when the entity is
    living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"))
    living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    living_Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    living_Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List()

    // Retract base entity with multiple edges
    ann.retract

    // All edges from/to Ann have been retracted too
    living_Person.name_("Ann").Knows.weight.Person.name.get === List()
    living_Person.name_("Ben").Knows.weight.Person.name.get === List()
    living_Person.name_("Joe").Knows.weight.Person.name.get === List()
    living_Person.name_("Liz").Knows.weight.Person.name.get === List()
    living_Person.name_("Tom").Knows.weight.Person.name.get === List()
    living_Person.name_("Ulf").Knows.weight.Person.name.get === List()

    // Persons not retracted are still there
    living_Person.name.get.sorted === List("Ben", "Joe", "Liz", "Tom")
  }
}
