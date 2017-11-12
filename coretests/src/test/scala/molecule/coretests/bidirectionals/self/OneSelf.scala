package molecule.coretests.bidirectionals.self

import molecule.Imports._
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util.MoleculeSpec
import molecule.coretests.bidirectionals.Setup


class OneSelf extends MoleculeSpec {

  class setup extends Setup {
    val spouses = m(Person.name.Spouse.name)
  }

  "Save new" in new Setup {

    // Save Adam, Lisa and bidirectional references between them
    val List(adam, lisa) = Person.name("Adam").Spouse.name("Lisa").save.eids

    // Reference is bidirectional - both point to each other
    Person.name.Spouse.name.get.toSeq.sorted === List(
      ("Adam", "Lisa"),
      // Reverse reference:
      ("Lisa", "Adam")
    )

    Person(adam).Spouse.name.get.head === "Lisa"
    Person(lisa).Spouse.name.get.head === "Adam"

    // Forth and back should bring us to the initial value (given Adam cardinality one ref)
    Person(adam).Spouse.name_.Spouse.name.get.head === "Adam"
    Person(lisa).Spouse.name_.Spouse.name.get.head === "Lisa"
  }

  "Save id" in new Setup {

    val lisa = Person.name.insert("Lisa").eid

    // Save Adam with bidirectional ref to existing  Lisa
    val adam = Person.name("Adam").spouse(lisa).save.eid

    Person.name.Spouse.name.get.toSeq.sorted === List(
      ("Adam", "Lisa"),
      ("Lisa", "Adam")
    )

    // Saving reference to generic `e` not allowed.
    // (instead apply ref to ref attribute as shown above)
    (Person.name("Adam").Spouse.e(lisa).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `t`, `txInstant` and `op` " +
      s"not allowed in save molecules. Found `e($lisa)`"
  }


  "Insert new" in new Setup {

    // Insert 2 pairs of bidirectionally referenced entities
    Person.name.Spouse.name insert List(
      ("Adam", "Lisa"),
      ("John", "Nina")
    ) eids

    // Bidirectional references have been inserted
    Person.name.Spouse.name.get.toSeq.sorted === List(
      ("Adam", "Lisa"),
      ("John", "Nina"),
      // Reverse references:
      ("Lisa", "Adam"),
      ("Nina", "John")
    )
  }

  "Insert id" in new Setup {

    val List(lisa, nina) = Person.name insert List("Lisa", "Nina") eids

    // Insert 2 new entities and pair them with existing entities
    Person.name.spouse insert List(
      ("Adam", lisa),
      ("John", nina)
    )

    // Bidirectional references have been inserted
    Person.name.Spouse.name.get.toSeq.sorted === List(
      ("Adam", "Lisa"),
      ("John", "Nina"),
      ("Lisa", "Adam"),
      ("Nina", "John")
    )
  }


  "Update new" >> {

    "creating ref to new" in new Setup {

      val adam = Person.name.insert("Adam").eid

      // Update Adam with creation of Lisa and bidirectional reference between Adam and Lisa
      Person(adam).Spouse.name("Lisa").update

      Person.name.Spouse.name.get.toSeq.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )
    }


    "replacing ref to new" in new Setup {

      val List(adam, lisa) = Person.name("Adam").Spouse.name("Lisa").save.eids

      // Bidirectional references created
      Person.name.Spouse.name.get.toSeq.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )

      // Update Adam with creation of Nina and replacing bidirectional reference with Lisa to created Nina
      Person(adam).Spouse.name("Nina").update

      // Bidirectional references to Lisa have been replaced with refs to/from Nina
      Person.name.Spouse.name.get.toSeq.sorted === List(
        ("Adam", "Nina"),
        ("Nina", "Adam")
      )
    }
  }


  "Update id" >> {

    "creating ref to existing" in new Setup {

      // Adam and Lisa not married yet
      val adam = Person.name.insert("Adam").eid
      val lisa = Person.name.insert("Lisa").eid

      // Update Adam with creation of bidirectional reference to existing Lisa
      Person(adam).spouse(lisa).update

      Person.name.Spouse.name.get.toSeq.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )

      // Referencing the same id is not allowed
      (Person(adam).spouse(adam).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:biSelfRef]  Current entity and referenced entity ids can't be the same."
    }


    "replacing ref to other existing" in new Setup {

      val List(adam, lisa) = Person.name("Adam").Spouse.name("Lisa").save.eids

      // Bidirectional references created
      Person.name.Spouse.name.get.toSeq.sorted === List(
        ("Adam", "Lisa"),
        ("Lisa", "Adam")
      )

      // Update Adam, replacing bidirectional reference with Lisa to existing Nina
      val nina = Person.name.insert("Nina").eid
      Person(adam).spouse(nina).update

      // Bidirectional references to Lisa have been replaced with refs to/from Nina
      Person.name.Spouse.name.get.toSeq.sorted === List(
        ("Adam", "Nina"),
        ("Nina", "Adam")
      )
    }
  }


  "Update removing reference" in new Setup {

    val List(adam, lisa) = Person.name("Adam").Spouse.name("Lisa").save.eids

    // Bidirectional references created
    Person.name.Spouse.name.get.toSeq.sorted === List(
      ("Adam", "Lisa"),
      ("Lisa", "Adam")
    )

    // Retract ref between them by applying no value
    Person(adam).spouse().update

    // Bidirectional references retracted
    Person.name.Spouse.name.get.toSeq.sorted === List()
  }


  "Retract" in new Setup {

    val adam = Person.name.insert("Adam").eid

    // Create and reference Lisa to Adam
    val lisa = Person(adam).Spouse.name("Lisa").update.eid

    Person(adam).Spouse.name.get === List("Lisa")
    Person(lisa).Spouse.name.get === List("Adam")

    Person.name.Spouse.name.get.toSeq.sorted === List(
      ("Adam", "Lisa"),
      ("Lisa", "Adam")
    )

    // Retract Adam and all references to/from Adam
    adam.retract

    // Lisa remains and both references retracted
    Person.name.get === List("Lisa")
    Person(adam).Spouse.name.get === List()
    Person(lisa).Spouse.name.get === List()
    Person.name.Spouse.name.get.toSeq.sorted === List()
  }
}
