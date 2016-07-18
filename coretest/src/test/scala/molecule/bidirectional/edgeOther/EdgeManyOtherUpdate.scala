package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeManyOtherUpdate extends MoleculeSpec {


  "add" in new Setup {

    // In order to maintain data consistency we can't create property edges in isolation.
    // We can therefore not "add" them to existing entities as we could with simple
    // reference values as we saw in the update:replace1/multiple tests in `SelfMany`.
    (living_Person(ben).closeTo.add(42L).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Adding edge ids with " +
      s"`edgeAttr.add(someEdgeId)` is not allowed. It could be an indication that you are trying to " +
      s"use an existing edge twice which is not allowed."

    // Adding an edge from an existing entity to another entity therefore involves
    // creating the edge entity itself having a reference to either an existing target
    // entity or a newly created one.

    val ben = living_Person.name("Ben").save.eid

    // New edge and new target entity
    // Update Ben with new friendship to new Rex
    living_Person(ben).CloseTo.weight(7).Animal.name("Rex").update

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))

    // New edge with reference to existing target entity
    val zip = living_Animal.name("Zip").save.eid

    // Update Ben with new friendship to existing Zip
    living_Person(ben).CloseTo.weight(6).animal(zip).update

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((6, "Zip"), (7, "Rex"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((6, "Ben"))


    // Can't update multiple values of cardinality-one attribute `name`
    (living_Person(ben).CloseTo.weight(7).Animal.name("Zip", "Zup").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
      "\n  living_Animal ... name(Zip, Zup)"

    // As with save molecules nesting is not allowed in update molecules
    (living_Person(ben).CloseTo.*(living_CloseTo.weight(4)).Animal.name("Zip").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_CloseTo`."

    (living_Person(ben).CloseTo.*(living_CloseTo.weight(4)).animal(zip).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_CloseTo`."

    (living_Person(ben).CloseTo.*(living_CloseTo.weight(4).Animal.name("Zip")).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_CloseTo`."

    (living_Person(ben).CloseTo.*(living_CloseTo.weight(4).animal(zip)).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `living_CloseTo`."


    // Note that an edge always have only one target entity.
    // So we can't add multiple (won't compile)
    // living_Person(ben).CloseTo.weight(6).animal(42L, 43L).update

    // Each edge has only 1 target entity so we can't use nested structures on the target namespace
    // (living_Person.name("Rex").CloseTo.weight(7).Animal.*(living_Animal.name("Zip")).update
    //                                                   ^ nesting of edge target namespace not available
  }


  "replace" in new Setup {

    living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name) insert List(
      ("Ben", List((6, "Gus"), (7, "Leo"))),
      ("Don", List((9, "Rex")))
    )

    // Entities
    val List(ben, don)      = living_Person.e.name.get.sortBy(_._2).map(_._1)
    val List(gus, leo, rex) = living_Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, _, benLeo, _, donRex, _) = living_CloseTo.e.weight_.get

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((6, "Gus"), (7, "Leo"))
    living_Person.name_("Don").CloseTo.weight.Animal.name.get === List((9, "Rex"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List((6, "Ben"))
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((9, "Don"))

    // edgeAttr.replace(old -> new) not available for edges
    // To enforce consistency, edges are not allowed to be replaced with each other
    (living_Person(ben).closeTo.replace(benGus -> donRex).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Replacing edge ids " +
      s"with `edgeAttr.replace(old -> new)` is not allowed. It could be an indication that you are " +
      s"trying to replace the old edge with an existing edge which is not allowed."

    // Replace edge in 2 steps instead:

    // 1. Remove friendship with Gus
    living_Person(ben).closeTo.remove(benGus).update

    // 2. Update Ben with new friendship to existing Rex
    living_Person(ben).CloseTo.weight(8).animal(rex).update

    // Now both Ben and Don are close to Rex
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Leo"), (8, "Rex"))
    living_Person.name_("Don").CloseTo.weight.Animal.name.get === List((9, "Rex"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((8, "Ben"), (9, "Don"))
  }


  "remove" in new Setup {

    val ids = living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    // Entities
    val ben                = ids.head
    val List(gus, leo, rex, zip, zup) = living_Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, benLeo, benRex, benZip, benZap) = living_Person(ben).CloseTo.e.Animal.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Remove single edge
    living_Person(ben).closeTo.remove(benGus).update

    // Remove multiple edges
    living_Person(ben).closeTo.remove(benLeo, benRex).update

    // Remove Seq of edges
    living_Person(ben).closeTo.remove(Seq(benZip)).update

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((9, "Zup"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))
  }



  "remove all / apply" in new Setup {

    val ids = living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    val ben                           = ids.head
    val List(gus, leo, rex, zip, zup) = living_Animal.e.name.get.sortBy(_._2).map(_._1)


    // Applyin no values from other end
    living_Animal(rex).closeTo().update

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (8, "Zip"), (9, "Zup"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))


    // Remove all edges (apply no values)
    living_Person(ben).closeTo().update

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List()

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List()
  }


  "retract edge" in new Setup {

    val ids = living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    val ben                           = ids.head
    val List(gus, leo, rex, zip, zup) = living_Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, benLeo, benRex, benZip, benZap) = living_Person(ben).CloseTo.e.Animal.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Retract single edge
    benGus.retract

    // Retract multiple edges
    Seq(benLeo, benRex, benZip).map(_.retract)

    // Edges in both directions have been retracted
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((9, "Zup"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Retract all edges
    living_CloseTo.person_.e.get.map(_.retract)
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List()

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List()
  }


  "retract base/target entity" in new Setup {

    val ids = living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    val ben                           = ids.head
    val List(gus, leo, rex, zip, zup) = living_Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, benLeo, benRex, benZip, benZap) = living_Person(ben).CloseTo.e.Animal.name.get.sortBy(_._2).map(_._1)

    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Retract target entity
    zup.retract

    // Edges are components of the retracted entity, so they also are retracted when the entity is
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"))

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List()

    // Retract base entity with multiple edges
    ben.retract

    // All edges from/to Ben have been retracted too
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List()

    living_Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    living_Animal.name_("Zup").CloseTo.weight.Person.name.get === List()

    // Animals not retracted are still there
    living_Animal.name.get.sorted === List("Gus", "Leo", "Rex", "Zip")
  }
}
