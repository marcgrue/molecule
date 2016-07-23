package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeManyOtherSave extends MoleculeSpec {

  class setup extends Setup {
    val animalsCloseTo = m(Person.name_(?).CloseTo.*(CloseTo.weight.Animal.name))
    val personsCloseTo = m(Animal.name_(?).CloseTo.*(CloseTo.weight.Person.name))
  }


  "base/edge/target" >> {

    "no nesting in save molecules" in new Setup {

      (Person.name("Ann").CloseTo.*(CloseTo.weight(7)).Animal.name("Rex").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"

      // Insert entities, each having one or more connected entities with relationship properties
      val rex = Animal.name.insert("Rex").eid
      (Person.name("Rex").CloseTo.*(CloseTo.weight(7).animal(rex)).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"
    }


    // Since we can't nest in save-molecules, saves will be the same for cardinality one/many,
    // and the following two test will be the same as for tests in EdgeOneOtherSave

    "new target" in new setup {

      Person.name("Ann").CloseTo.weight(7).Animal.name("Rex").save.eids

      // Bidirectional property edges have been saved
      animalsCloseTo("Ann").get === List(List((7, "Rex")))
      personsCloseTo("Rex").get === List(List((7, "Ann")))
    }


    "existing target" in new setup {

      val rex = Animal.name.insert("Rex").eid

      // Save Ann with weighed relationship to existing Rex
      Person.name("Ann").CloseTo.weight(7).animal(rex).save.eids

      // Ann and Rex each others CloseTo with a weight of 7
      animalsCloseTo("Ann").get === List(List((7, "Rex")))
      personsCloseTo("Rex").get === List(List((7, "Ann")))
    }
  }


  "base + edge/target" >> {

    "new target" in new setup {

      // Create edges to new target entities
      val closeToGus = CloseTo.weight(7).Animal.name("Gus").save.eid
      val closeToLeo = CloseTo.weight(8).Animal.name("Leo").save.eid

      // Connect multiple edges
      Person.name("Ann").closeTo(closeToGus, closeToLeo).save

      // Ann and Gus know each other with a weight of 7
      animalsCloseTo("Ann").get === List(List((7, "Gus"), (8, "Leo")))
      personsCloseTo("Gus").get === List(List((7, "Ann")))
      personsCloseTo("Leo").get === List(List((8, "Ann")))
    }

    "existing target" in new setup {

      val List(gus, leo) = Animal.name.insert("Gus", "Leo").eids

      // Create edges to existing target entities
      val closeToGus = CloseTo.weight(7).animal(gus).save.eid
      val closeToLeo = CloseTo.weight(8).animal(leo).save.eid

      // Connect multiple edges
      Person.name("Ann").closeTo(closeToGus, closeToLeo).save

      // Ann and Gus know each other with a weight of 7
      animalsCloseTo("Ann").get === List(List((7, "Gus"), (8, "Leo")))
      personsCloseTo("Gus").get === List(List((7, "Ann")))
      personsCloseTo("Leo").get === List(List((8, "Ann")))
    }
  }


  // Edge consistency checks.

  "base - edge - <missing target>" in new Setup {

    // Can't allow edge without ref to target entity
    (Person.name("Gus").CloseTo.weight(5).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.edgeComplete]  Missing target namespace after edge namespace `CloseTo`."
  }

  "<missing base> - edge - <missing target>" in new Setup {

    // Edge always have to have a ref to a target entity
    (CloseTo.weight(7).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.edgeComplete]  Missing target namespace somewhere after edge property `CloseTo/weight`."
  }

}
