package molecule
package bidirectional

import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec

class SelfOne extends MoleculeSpec {


  "Save new" in new Setup {

    // Save Adam, Lisa and bidirectional references between them
    val List(adam, lisa) = living_Person.name("Adam").Spouse.name("Lisa").save.eids

    // Reference is bidirectional - both point to each other
    living_Person.name.Spouse.name.get.sorted === List(
      ("Adam", "Lisa"),
      // Reverse reference:
      ("Lisa", "Adam")
    )

    living_Person(adam).Spouse.name.one === "Lisa"
    living_Person(lisa).Spouse.name.one === "Adam"

    // Forth and back should bring us to the initial value (given Adam cardinality one ref)
    living_Person(adam).Spouse.name_.Spouse.name.one === "Adam"
  }

  "Save id" in new Setup {

    val lisa = living_Person.name.insert("Lisa").eid

    // Save Adam with bidirectional ref to existing  Lisa
    val adam = living_Person.name("Adam").spouse(lisa).save.eid

    living_Person.name.Spouse.name.get.sorted === List(
      ("Adam", "Lisa"),
      ("Lisa", "Adam")
    )

    // Saving reference to generic `e` not allowed.
    // (instead apply ref to ref attribute as shown above)
    (living_Person.name("Adam").Spouse.e(lisa).save must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[api.CheckModel.noGenerics] Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
      s"not allowed in save molecules. Found `e($lisa)`"
  }


  "Insert new" in new Setup {

    // Insert 2 pairs of bidirectionally referenced entities
    living_Person.name.Spouse.name insert List(
      ("Adam", "Lisa"),
      ("John", "Nina")
    ) eids

    // Bidirectional references have been inserted
    living_Person.name.Spouse.name.get.sorted === List(
      ("Adam", "Lisa"),
      ("John", "Nina"),
      // Reverse references:
      ("Lisa", "Adam"),
      ("Nina", "John")
    )
  }

  "Insert id" in new Setup {

    val List(lisa, nina) = living_Person.name insert List("Lisa", "Nina") eids

    // Insert 2 men and pair them with existing women
    living_Person.name.spouse insert List(
      ("Adam", lisa),
      ("John", nina)
    )

    // Bidirectional references have been inserted
    living_Person.name.Spouse.name.get.sorted === List(
      ("Adam", "Lisa"),
      ("John", "Nina"),
      ("Lisa", "Adam"),
      ("Nina", "John")
    )
  }


  "Update new" >> {

    "creating ref to new" in new Setup {

      val adam = living_Person.name.insert("Adam").eid

      // Update Adam with creation of Lisa and bidirectional reference between Adam and Lisa
      living_Person(adam).Spouse.name("Lisa").update

      living_Person.name.Spouse.name.get.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )
    }


    "replacing ref to new" in new Setup {

      val List(adam, lisa) = living_Person.name("Adam").Spouse.name("Lisa").save.eids

      // Bidirectional references created
      living_Person.name.Spouse.name.get.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )

      // Update Adam with creation of Nina and replacing bidirectional reference with Lisa to created Nina
      living_Person(adam).Spouse.name("Nina").update

      // Bidirectional references to Lisa have been replaced with refs to/from Nina
      living_Person.name.Spouse.name.get.sorted === List(
        ("Adam", "Nina"),
        ("Nina", "Adam")
      )
    }
  }


  "Update id" >> {

    "creating ref to existing" in new Setup {

      // Adam and Lisa not married yet
      val adam = living_Person.name.insert("Adam").eid
      val lisa = living_Person.name.insert("Lisa").eid

      // Update Adam with creation of bidirectional reference to existing Lisa
      living_Person(adam).spouse(lisa).update

      living_Person.name.Spouse.name.get.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )

      // Referencing the same id is not allowed
      (living_Person(adam).spouse(adam).update must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        "Current entity and referenced entity ids can't be the same"
    }


    "replacing ref to other existing" in new Setup {

      val List(adam, lisa) = living_Person.name("Adam").Spouse.name("Lisa").save.eids

      // Bidirectional references created
      living_Person.name.Spouse.name.get.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )

      // Update a, replacing bidirectional reference with Lisa to existing Nina
      val nina = living_Person.name.insert("Nina").eid
      living_Person(adam).spouse(nina).update

      // Bidirectional references to Lisa have been replaced with refs to/from Nina
      living_Person.name.Spouse.name.get.sorted === List(
        ("Adam", "Nina"),
        ("Nina", "Adam")
      )
    }
  }


  "Update removing reference" in new Setup {

    val List(adam, lisa) = living_Person.name("Adam").Spouse.name("Lisa").save.eids

    // Bidirectional references created
    living_Person.name.Spouse.name.get.sorted === List(
      ("Adam", "Lisa"),
      ("Lisa", "Adam")
    )

    // Retract ref between them by applying no value
    living_Person(adam).spouse().update

    // Bidirectional references retracted
    living_Person.name.Spouse.name.get.sorted === List()
  }


  "Retract" in new Setup {

    val adam = living_Person.name.insert("Adam").eid

    // Create and reference Lisa to a
    val lisa = living_Person(adam).Spouse.name("Lisa").update.eid

    living_Person(adam).Spouse.name.get === List("Lisa")
    living_Person(lisa).Spouse.name.get === List("Adam")

    living_Person.name.Spouse.name.get.sorted === List(
      ("Adam", "Lisa"),
      ("Lisa", "Adam")
    )

    // Retract Adam and all references to/from Adam
    adam.retract

    // Lisa remains and both references retracted
    living_Person.name.get === List("Lisa")
    living_Person(adam).Spouse.name.get === List()
    living_Person(lisa).Spouse.name.get === List()
    living_Person.name.Spouse.name.get.sorted === List()
  }
}
