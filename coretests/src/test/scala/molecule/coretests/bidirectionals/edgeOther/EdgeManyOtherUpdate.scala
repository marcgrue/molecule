package molecule.coretests.bidirectionals.edgeOther

import molecule.imports._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util._

class EdgeManyOtherUpdate extends MoleculeSpec {

  class setup extends Setup {
    val ann     = Person.name("Ann").save.eid
    
    val animalsCloseTo = m(Person.name_(?).CloseTo.*(CloseTo.weight.Animal.name))
    val personsCloseTo = m(Animal.name_(?).CloseTo.*(CloseTo.weight.Person.name))

    // Separate edges
    val Seq(
    closeToBob,
    closeToDot,
    closeToGus,
    closeToHip,
    closeZoeax,
    closeToPix,
    closeToZoe
    ): Seq[Long] = CloseTo.weight.Animal.name.insert(List(
      (2, "Bob"),
      (3, "Dot"),
      (4, "Gus"),
      (5, "Hip"),
      (6, "Max"),
      (7, "Pix"),
      (8, "Zoe")
    )).eids.grouped(3).map(_.head).toSeq
  }


  "add edges" in new setup {

    // vararg
    Person(ann).closeTo.add(closeToBob, closeToDot).update

    // Seq
    Person(ann).closeTo.add(Seq(closeToGus)).update

    // Empty list of edges has no effect
    Person(ann).closeTo.add(Seq()).update

    animalsCloseTo("Ann").get === List(List((2, "Bob"), (3, "Dot"), (4, "Gus")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List(List((3, "Ann")))
    personsCloseTo("Gus").get === List(List((4, "Ann")))
  }


  "replace edges" in new setup {

    // current friends
    Person(ann).closeTo.add(closeToBob, closeToDot, closeToGus, closeToZoe).update

    animalsCloseTo("Ann").get === List(List((2, "Bob"), (3, "Dot"), (4, "Gus"), (8, "Zoe")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List(List((3, "Ann")))
    personsCloseTo("Gus").get === List(List((4, "Ann")))
    personsCloseTo("Zoe").get === List(List((8, "Ann")))

    // Replace who Ann closeTo
    Person(ann).closeTo.replace(closeToBob -> closeToHip, closeToDot -> closeZoeax).update
    Person(ann).closeTo.replace(Seq(closeToGus -> closeToPix)).update

    // All friends have been replaced
    animalsCloseTo("Ann").get === List(List((5, "Hip"), (6, "Max"), (7, "Pix"), (8, "Zoe")))
    personsCloseTo("Hip").get === List(List((5, "Ann")))
    personsCloseTo("Max").get === List(List((6, "Ann")))
    personsCloseTo("Pix").get === List(List((7, "Ann")))
    personsCloseTo("Zoe").get === List(List((8, "Ann"))) // Hasn't been replace by empty Seq

    // Replace with empty Seq has no effect
    Person(ann).closeTo.replace(Seq()).update
    animalsCloseTo("Ann").get === List(List((5, "Hip"), (6, "Max"), (7, "Pix"), (8, "Zoe")))
  }


  "remove edges" in new setup {

    // current friends
    Person(ann).closeTo.add(closeToBob, closeToDot, closeToGus, closeToZoe).update

    animalsCloseTo("Ann").get === List(List((2, "Bob"), (3, "Dot"), (4, "Gus"), (8, "Zoe")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List(List((3, "Ann")))
    personsCloseTo("Gus").get === List(List((4, "Ann")))
    personsCloseTo("Zoe").get === List(List((8, "Ann")))


    // Remove who Ann closeTo
    Person(ann).closeTo.remove(closeToBob, closeToDot).update

    // All friends have been replaced
    animalsCloseTo("Ann").get === List(List((4, "Gus"), (8, "Zoe")))
    personsCloseTo("Bob").get === List()
    personsCloseTo("Dot").get === List()
    personsCloseTo("Gus").get === List(List((4, "Ann")))
    personsCloseTo("Zoe").get === List(List((8, "Ann")))

    // Remove Seq of edges
    Person(ann).closeTo.remove(Seq(closeToGus)).update

    // All friends have been replaced
    animalsCloseTo("Ann").get === List(List((8, "Zoe")))
    personsCloseTo("Bob").get === List()
    personsCloseTo("Dot").get === List()
    personsCloseTo("Gus").get === List()
    personsCloseTo("Zoe").get === List(List((8, "Ann")))

    // Remove empty Seq of edges has no effect
    Person(ann).closeTo.remove(Seq()).update

    // All friends have been replaced
    animalsCloseTo("Ann").get === List(List((8, "Zoe")))
  }


  "apply edges" in new setup {

    // current friends
    Person(ann).closeTo.add(closeToBob, closeToDot, closeToGus).update

    animalsCloseTo("Ann").get === List(List((2, "Bob"), (3, "Dot"), (4, "Gus")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List(List((3, "Ann")))
    personsCloseTo("Gus").get === List(List((4, "Ann")))
    personsCloseTo("Zoe").get === List()


    // State who Ann closeTo now
    Person(ann).closeTo(closeToBob, closeToZoe).update

    // Bob remains, Zoe added
    animalsCloseTo("Ann").get === List(List((2, "Bob"), (8, "Zoe")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List()
    personsCloseTo("Gus").get === List()
    personsCloseTo("Zoe").get === List(List((8, "Ann")))

    // Apply Seq of edges
    Person(ann).closeTo(Seq(closeToHip)).update

    animalsCloseTo("Ann").get === List(List((5, "Hip")))
    personsCloseTo("Bob").get === List()
    personsCloseTo("Dot").get === List()
    personsCloseTo("Gus").get === List()
    personsCloseTo("Hip").get === List(List((5, "Ann")))
    personsCloseTo("Zoe").get === List()

    // Applying empty Seq retracts all edges from Ann!
    Person(ann).closeTo(Seq()).update

    animalsCloseTo("Ann").get === List()
    personsCloseTo("Hip").get === List()


    // Applying no values removes all!

    Person(ann).CloseTo.weight(7).Animal.name("Bob").update
    animalsCloseTo("Ann").get === List(List((7, "Bob")))

    Person(ann).closeTo().update
    animalsCloseTo("Ann").get === List()
  }


  "retract edge" in new setup {

    // current friends
    Person(ann).closeTo.add(closeToBob, closeToDot).update

    animalsCloseTo("Ann").get === List(List((2, "Bob"), (3, "Dot")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List(List((3, "Ann")))

    // Retract single edge
    closeToBob.retract

    animalsCloseTo("Ann").get === List(List((3, "Dot")))
    personsCloseTo("Bob").get === List()
    personsCloseTo("Dot").get === List(List((3, "Ann")))
  }


  "retract base/target entity" in new setup {

    // current friends
    Person(ann).closeTo.add(closeToBob, closeToDot).update

    animalsCloseTo("Ann").get === List(List((2, "Bob"), (3, "Dot")))
    personsCloseTo("Bob").get === List(List((2, "Ann")))
    personsCloseTo("Dot").get === List(List((3, "Ann")))

    // Retract base entity
    ann.retract

    // All knowing to/from Ann retracted
    animalsCloseTo("Ann").get === List()
    personsCloseTo("Bob").get === List()
    personsCloseTo("Dot").get === List()
  }


  "no nested in update molecules" in new setup {

    // Can't update multiple values of cardinality-one attribute `name`
    (Person(ann).CloseTo.weight(7).Animal.name("Max", "Liz").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
      "\n  Animal ... name(Max, Liz)"

    // As with save molecules nesting is not allowed in update molecules
    (Person(ann).CloseTo.*(CloseTo.weight(4)).Animal.name("Max").update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    (Person(ann).CloseTo.*(CloseTo.weight(4)).animal(42L).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    (Person(ann).CloseTo.*(CloseTo.weight(4).Animal.name("Max")).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    (Person(ann).CloseTo.*(CloseTo.weight(4).animal(42L)).update must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't have nested data structures like `CloseTo`."

    // Note that an edge always have only one target entity.
    // So we can't add multiple (won't compile)
    // Person(ann).CloseTo.weight(6).animal(42L, 43L).update

    // Each edge has only 1 target entity so we can't use nested structures on the target namespace
    // (Person.name("Bob").CloseTo.weight(7).Animal.*(Animal.name("Max")).update
    //                                                ^ nesting of edge target namespace not available
  }
}
