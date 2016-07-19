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
    (Person(ben).closeTo.add(42L).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Adding edge ids with " +
      s"`edgeAttr.add(someEdgeId)` is not allowed. It could be an indication that you are trying to " +
      s"use an existing edge twice which is not allowed."

    // Adding an edge from an existing entity to another entity therefore involves
    // creating the edge entity itself having a reference to either an existing target
    // entity or a newly created one.

    val ben = Person.name("Ben").save.eid

    // New edge and new target entity
    // Update Ben with new friendship to new Rex
    Person(ben).CloseTo.weight(7).Animal.name("Rex").update

    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))

    // New edge with reference to existing target entity
    val zip = Animal.name("Zip").save.eid

    // Update Ben with new friendship to existing Zip
    Person(ben).CloseTo.weight(6).animal(zip).update

    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((6, "Zip"), (7, "Rex"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List((6, "Ben"))


    // Can't update multiple values of cardinality-one attribute `name`
    (Person(ben).CloseTo.weight(7).Animal.name("Zip", "Zup").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
      "\n  Animal ... name(Zip, Zup)"

    // As with save molecules nesting is not allowed in update molecules
    (Person(ben).CloseTo.*(CloseTo.weight(4)).Animal.name("Zip").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    (Person(ben).CloseTo.*(CloseTo.weight(4)).animal(zip).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    (Person(ben).CloseTo.*(CloseTo.weight(4).Animal.name("Zip")).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    (Person(ben).CloseTo.*(CloseTo.weight(4).animal(zip)).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."


    // Note that an edge always have only one target entity.
    // So we can't add multiple (won't compile)
    // Person(ben).CloseTo.weight(6).animal(42L, 43L).update

    // Each edge has only 1 target entity so we can't use nested structures on the target namespace
    // (Person.name("Rex").CloseTo.weight(7).Animal.*(Animal.name("Zip")).update
    //                                                   ^ nesting of edge target namespace not available
  }


  "replace" in new Setup {

    Person.name.CloseTo.*(CloseTo.weight.Animal.name) insert List(
      ("Ben", List((6, "Gus"), (7, "Leo"))),
      ("Don", List((9, "Rex")))
    )

    // Entities
    val List(ben, don)      = Person.e.name.get.sortBy(_._2).map(_._1)
    val List(gus, leo, rex) = Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, _, benLeo, _, donRex, _) = CloseTo.e.weight_.get

    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((6, "Gus"), (7, "Leo"))
    Person.name_("Don").CloseTo.weight.Animal.name.get === List((9, "Rex"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List((6, "Ben"))
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((7, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((9, "Don"))

    // edgeAttr.replace(old -> new) not available for edges
    // To enforce consistency, edges are not allowed to be replaced with each other
    (Person(ben).closeTo.replace(benGus -> donRex).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Replacing edge ids " +
      s"with `edgeAttr.replace(old -> new)` is not allowed. It could be an indication that you are " +
      s"trying to replace the old edge with an existing edge which is not allowed."

    // Replace edge in 2 steps instead:

    // 1. Remove friendship with Gus
    Person(ben).closeTo.remove(benGus).update

    // 2. Update Ben with new friendship to existing Rex
    Person(ben).CloseTo.weight(8).animal(rex).update

    // Now both Ben and Don are close to Rex
    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Leo"), (8, "Rex"))
    Person.name_("Don").CloseTo.weight.Animal.name.get === List((9, "Rex"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((7, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((8, "Ben"), (9, "Don"))
  }


  "remove" in new Setup {

    val ids = Person.name.CloseTo.*(CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    // Entities
    val ben                = ids.head
    val List(gus, leo, rex, zip, zup) = Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, benLeo, benRex, benZip, benZap) = Person(ben).CloseTo.e.Animal.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Remove single edge
    Person(ben).closeTo.remove(benGus).update

    // Remove multiple edges
    Person(ben).closeTo.remove(benLeo, benRex).update

    // Remove Seq of edges
    Person(ben).closeTo.remove(Seq(benZip)).update

    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((9, "Zup"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))
  }



  "remove all / apply" in new Setup {

    val ids = Person.name.CloseTo.*(CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    val ben                           = ids.head
    val List(gus, leo, rex, zip, zup) = Animal.e.name.get.sortBy(_._2).map(_._1)


    // Applyin no values from other end
    Animal(rex).closeTo().update

    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (8, "Zip"), (9, "Zup"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))


    // Remove all edges (apply no values)
    Person(ben).closeTo().update

    Person.name_("Ben").CloseTo.weight.Animal.name.get === List()

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List()
  }


  "retract edge" in new Setup {

    val ids = Person.name.CloseTo.*(CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    val ben                           = ids.head
    val List(gus, leo, rex, zip, zup) = Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, benLeo, benRex, benZip, benZap) = Person(ben).CloseTo.e.Animal.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Retract single edge
    benGus.retract

    // Retract multiple edges
    Seq(benLeo, benRex, benZip).map(_.retract)

    // Edges in both directions have been retracted
    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((9, "Zup"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Retract all edges
    CloseTo.person_.e.get.map(_.retract)
    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List()

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List()
  }


  "retract base/target entity" in new Setup {

    val ids = Person.name.CloseTo.*(CloseTo.weight.Animal.name) insert List(
      ("Ben", List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup")))
    ) eids

    val ben                           = ids.head
    val List(gus, leo, rex, zip, zup) = Animal.e.name.get.sortBy(_._2).map(_._1)

    // Edges
    val List(benGus, benLeo, benRex, benZip, benZap) = Person(ben).CloseTo.e.Animal.name.get.sortBy(_._2).map(_._1)

    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"), (9, "Zup"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List((9, "Ben"))

    // Retract target entity
    zup.retract

    // Edges are components of the retracted entity, so they also are retracted when the entity is
    Person.name_("Ben").CloseTo.weight.Animal.name.get.sorted === List((1, "Gus"), (2, "Leo"), (4, "Rex"), (8, "Zip"))

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List((1, "Ben"))
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List((2, "Ben"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((4, "Ben"))
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List((8, "Ben"))
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List()

    // Retract base entity with multiple edges
    ben.retract

    // All edges from/to Ben have been retracted too
    Person.name_("Ben").CloseTo.weight.Animal.name.get === List()

    Animal.name_("Gus").CloseTo.weight.Person.name.get === List()
    Animal.name_("Leo").CloseTo.weight.Person.name.get === List()
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zip").CloseTo.weight.Person.name.get === List()
    Animal.name_("Zup").CloseTo.weight.Person.name.get === List()

    // Animals not retracted are still there
    Animal.name.get.sorted === List("Gus", "Leo", "Rex", "Zip")
  }
}
