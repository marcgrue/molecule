package molecule.coretests.bidirectionals.other

import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.in1_out3._


class OneOther extends CoreSpec {

  class setup extends BidirectionalSetup {
    val personPet    = m(Person.name.Pet.name)
    val animalMaster = m(Animal.name.Master.name)
  }

  "Save new" in new setup {

    // Save Ben, Rex and bidirectional references between them
    val List(ben, rex) = Person.name("Ben").Pet.name("Rex").save.eids

    // Reference in both directions saved
    // Since we use different names for the ref attributes on each end,
    // we query from each end. We could as well have had the same attr name.
    Person(ben).Pet.name.get.head === "Rex"
    Animal(rex).Master.name.get.head === "Ben"

    // Traversing forth and back should bring us to the initial value
    Person(ben).Pet.name_.Master.name.get.head === "Ben"
    Animal(rex).Master.name_.Pet.name.get.head === "Rex"
  }


  "Save new in reverse" in new setup {

    // Building from the other end gives the same result

    // Save Ben, Rex and bidirectional references between them
    val List(rex, ben) = Animal.name("Rex").Master.name("Ben").save.eids

    // Reference in both directions saved
    // Since we use different names for the ref attributes on each end,
    // we query from each end. We could as well have had the same attr name.
    Animal(rex).Master.name.get.head === "Ben"
    Person(ben).Pet.name.get.head === "Rex"

    // Traversing forth and back should bring us to the initial value
    Animal(rex).Master.Pet.name.get.head === "Rex"
    Person(ben).Pet.Master.name.get.head === "Ben"
  }


  "Save id" in new setup {

    val rex = Animal.name.insert("Rex").eid

    // Save Ben with bidirectional ref to existing Rex
    val ben = Person.name("Ben").pet(rex).save.eid

    animalMaster.get === List(
      ("Rex", "Ben")
    )
    personPet.get === List(
      ("Ben", "Rex")
    )

    // Saving reference to generic `e` not allowed.
    // (instead apply ref to ref attribute as shown above)
    (Person.name("Ben").Pet.e(rex).save must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `t`, `txInstant` and `op` " +
      s"not allowed in save molecules. Found `e($rex)`"
  }


  "Insert new" in new setup {

    // Insert 2 pairs of bidirectionally referenced entities
    Person.name.Pet.name insert List(
      ("Ben", "Rex"),
      ("Kim", "Zip")
    ) eids

    // Bidirectional references have been inserted
    personPet.get.sorted === List(
      ("Ben", "Rex"),
      ("Kim", "Zip")
    )
    animalMaster.get.sorted === List(
      ("Rex", "Ben"),
      ("Zip", "Kim")
    )
  }

  "Insert id" in new setup {

    val List(rex, zip) = Animal.name insert List("Rex", "Zip") eids

    // Insert 2 new entities and pair them with existing entities
    Person.name.pet insert List(
      ("Ben", rex),
      ("Kim", zip)
    )

    // Bidirectional references have been inserted
    personPet.get.sorted === List(
      ("Ben", "Rex"),
      ("Kim", "Zip")
    )
    animalMaster.get.sorted === List(
      ("Rex", "Ben"),
      ("Zip", "Kim")
    )
  }


  "Update new" >> {

    "creating ref to new" in new setup {

      val ben = Person.name.insert("Ben").eid
      Person(ben).Pet.name("Rex").update

      personPet.get.sorted === List(
        ("Ben", "Rex")
      )
      animalMaster.get.sorted === List(
        ("Rex", "Ben")
      )

      // Insert from other end

      val zip = Animal.name.insert("Zip").eid
      Animal(zip).Master.name("Kim").update

      personPet.get.sorted === List(
        ("Ben", "Rex"),
        ("Kim", "Zip")
      )
      animalMaster.get.sorted === List(
        ("Rex", "Ben"),
        ("Zip", "Kim")
      )
    }


    "replacing ref to new" in new setup {

      val List(ben, rex) = Person.name("Ben").Pet.name("Rex").save.eids

      // Bidirectional references created
      personPet.get.sorted === List(
        ("Ben", "Rex")
      )
      animalMaster.get.sorted === List(
        ("Rex", "Ben")
      )

      // Update Ben with creation of Zip and replacing bidirectional reference with Rex to created Zip
      Person(ben).Pet.name("Guz").update

      // Bidirectional references to Rex have been replaced with refs to/from Zip
      personPet.get.sorted === List(
        ("Ben", "Guz")
      )
      animalMaster.get.sorted === List(
        ("Guz", "Ben")
      )
    }
  }


  "Update id" >> {

    "creating ref to existing" in new setup {

      // Ben haven't got Rex yet
      val ben = Person.name.insert("Ben").eid
      val rex = Animal.name.insert("Rex").eid

      // Update Ben with creation of bidirectional reference to existing Rex
      Person(ben).pet(rex).update

      personPet.get.sorted === List(
        ("Ben", "Rex")
      )
      animalMaster.get.sorted === List(
        ("Rex", "Ben")
      )

      // Referencing the same id is not allowed
      (Person(ben).pet(ben).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:biSelfRef]  Current entity and referenced entity ids can't be the same."
    }


    "replacing ref to other existing" in new setup {

      val List(ben, rex) = Person.name("Ben").Pet.name("Rex").save.eids

      // Bidirectional references created
      personPet.get.sorted === List(
        ("Ben", "Rex")
      )
      animalMaster.get.sorted === List(
        ("Rex", "Ben")
      )

      // Update Ben, replacing bidirectional reference with Rex to existing Zip
      val zip = Animal.name.insert("Zip").eid
      Person(ben).pet(zip).update

      // Bidirectional references to Rex have been replaced with refs to/from Zip
      personPet.get.sorted === List(
        ("Ben", "Zip")
      )
      animalMaster.get.sorted === List(
        ("Zip", "Ben")
      )
    }
  }


  "Update removing reference" in new setup {

    val List(ben, rex) = Person.name("Ben").Pet.name("Rex").save.eids

    // Bidirectional references created
    personPet.get.sorted === List(
      ("Ben", "Rex")
    )
    animalMaster.get.sorted === List(
      ("Rex", "Ben")
    )

    // Retract ref between them by applying no value
    Person(ben).pet().update

    // Bidirectional references retracted
    personPet.get.sorted === List()
  }


  "Retract" in new setup {

    val ben = Person.name.insert("Ben").eid

    // Create and reference Rex to Ben
    val rex = Person(ben).Pet.name("Rex").update.eid

    personPet.get.sorted === List(
      ("Ben", "Rex")
    )
    animalMaster.get.sorted === List(
      ("Rex", "Ben")
    )

    // Retract Rex and all references to/from Ben
    rex.retract

    // Rex remains and both references retracted
    Person.name.get === List("Ben")
    Animal.name.get === List()

    Person(ben).Pet.name.get === List()
    Animal(rex).Master.name.get === List()

    personPet.get.sorted === List()
    animalMaster.get.sorted === List()
  }
}
