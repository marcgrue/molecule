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
    (Person(ann).knows.add(42L).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Adding edge ids with " +
      s"`edgeAttr.add(someEdgeId)` is not allowed. It could be an indication that you are trying to " +
      s"use an existing edge twice which is not allowed."

    // Adding an edge from an existing entity to another entity therefore involves
    // creating the edge entity itself having a reference to either an existing target
    // entity or a newly created one.

    val ann = Person.name("Ann").save.eid

    // New edge and new target entity
    // Update Ann with new friendship to new Ben
    Person(ann).Knows.weight(7).Person.name("Ben").update

    // New edge with reference to existing target entity
    val joe = Person.name("Joe").save.eid

    // Update Ann with new friendship to existing Joe
    Person(ann).Knows.weight(6).person(joe).update

    // Both bidirectional edges have been added from/to Ann
    Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"), (6, "Joe"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((6, "Ann"))


    // Can't update multiple values of cardinality-one attribute `name`
    (Person(ann).Knows.weight(7).Person.name("Joe", "Liz").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
      "\n  Person ... name(Joe, Liz)"

    // As with save molecules nesting is not allowed in update molecules
    (Person(ann).Knows.*(Knows.weight(4)).Person.name("Joe").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `Knows`."

    (Person(ann).Knows.*(Knows.weight(4)).person(joe).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `Knows`."

    (Person(ann).Knows.*(Knows.weight(4).Person.name("Joe")).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `Knows`."

    (Person(ann).Knows.*(Knows.weight(4).person(joe)).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `Knows`."


    // Note that an edge always have only one target entity.
    // So we can't add multiple (won't compile)
    // Person(ann).Knows.weight(6).person(42L, 43L).update

    // Each edge has only 1 target entity so we can't use nested structures on the target namespace
    // (Person.name("Ben").Knows.weight(7).Person.*(Person.name("Joe")).update
    //                                                   ^ nesting of edge target namespace not available
  }


  "replace" in new Setup {

    val ids = Person.name.Knows.*(Knows.weight.Person.name) insert List(
      ("Ann", List((6, "Ben"), (7, "Joe"))),
      ("Liz", List((9, "Tom")))
    ) eids

    // Entities
    val List(ann, ben, joe, liz, tom) = Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, _, annJoe, _, lizTom, _) = Knows.e.weight_.get

    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((6, "Ben"), (7, "Joe"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((6, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((9, "Tom"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((9, "Liz"))

    // edgeAttr.replace(old -> new) not available for edges
    // To enforce consistency, edges are not allowed to be replaced with each other
    (Person(ann).knows.replace(annBen -> lizTom).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Replacing edge ids " +
      s"with `edgeAttr.replace(old -> new)` is not allowed. It could be an indication that you are " +
      s"trying to replace the old edge with an existing edge which is not allowed."

    // Replace edge in 2 steps instead:

    // 1. Remove friendship with Ben
    Person(ann).knows.remove(annBen).update

    // 2. Update Ann with new friendship to existing Liz
    Person(ann).Knows.weight(8).person(liz).update

    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((7, "Joe"), (8, "Liz"))
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((9, "Tom"), (8, "Ann"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((9, "Liz"))
  }


  "remove" in new Setup {

    Person.name.Knows.*(Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, annJoe, annLiz, annTom, annUlf) = Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove single edge
    Person(ann).knows.remove(annBen).update

    // Remove multiple edges
    Person(ann).knows.remove(annJoe, annLiz).update

    // Remove Seq of edges
    Person(ann).knows.remove(Seq(annTom)).update

    Person.name_("Ann").Knows.weight.Person.name.get === List((9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove all edges (apply no values)
    Person(ann).knows().update

    Person.name_("Ann").Knows.weight.Person.name.get === List()
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List()
  }


  "apply" in new Setup {

    Person.name.Knows.*(Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, annJoe, annLiz, annTom, annUlf) = Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove single edge
    Person(ann).knows.remove(annBen).update

    // Remove multiple edges
    Person(ann).knows.remove(annJoe, annLiz).update

    // Remove Seq of edges
    Person(ann).knows.remove(Seq(annTom)).update

    Person.name_("Ann").Knows.weight.Person.name.get === List((9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Remove all edges (apply no values)
    Person(ann).knows().update

    Person.name_("Ann").Knows.weight.Person.name.get === List()
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List()
  }


  "retract edge" in new Setup {

    Person.name.Knows.*(Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = Person.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(annBen, annJoe, annLiz, annTom, annUlf) = Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Retract single edge
    annBen.retract

    // Retract multiple edges
    Seq(annJoe, annLiz, annTom).map(_.retract)

    // Edges in both directions have been retracted
    Person.name_("Ann").Knows.weight.Person.name.get === List((9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Retract all edges
    Knows.person_.e.get.map(_.retract)

    Person.name_("Ann").Knows.weight.Person.name.get === List()
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List()
  }


  "retract base/target entity" in new Setup {

    Person.name.Knows.*(Knows.weight.Person.name) insert List(
      ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
    )

    // Entities
    val List(ann, ben, joe, liz, tom, ulf) = Person.e.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))

    // Retract target entity
    ulf.retract

    // Edges are components of the retracted entity, so they also are retracted when the entity is
    Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"))
    Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
    Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
    Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
    Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
    Person.name_("Ulf").Knows.weight.Person.name.get === List()

    // Retract base entity with multiple edges
    ann.retract

    // All edges from/to Ann have been retracted too
    Person.name_("Ann").Knows.weight.Person.name.get === List()
    Person.name_("Ben").Knows.weight.Person.name.get === List()
    Person.name_("Joe").Knows.weight.Person.name.get === List()
    Person.name_("Liz").Knows.weight.Person.name.get === List()
    Person.name_("Tom").Knows.weight.Person.name.get === List()
    Person.name_("Ulf").Knows.weight.Person.name.get === List()

    // Persons not retracted are still there
    Person.name.get.sorted === List("Ben", "Joe", "Liz", "Tom")
  }
}
