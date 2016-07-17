package molecule.bidirectional.other

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec


class OneOther extends MoleculeSpec {


  "Save new" in new Setup {

    // Save Ben, Rex and bidirectional references between them
    val List(ben, rex) = living_Person.name("Ben").Pet.name("Rex").save.eids

    // Reference in both directions saved
    // Since we use different names for the ref attributes on each end,
    // we query from each end. We could as well have had the same attr name.
    living_Person(ben).Pet.name.one === "Rex"
    living_Animal(rex).Master.name.one === "Ben"

    // Traversing forth and back should bring us to the initial value
    living_Person(ben).Pet.name_.Master.name.one === "Ben"
    living_Animal(rex).Master.name_.Pet.name.one === "Rex"
  }


  "Save new in reverse" in new Setup {

    // Building from the other end gives the same result

    // Save Ben, Rex and bidirectional references between them
    val List(rex, ben) = living_Animal.name("Rex").Master.name("Ben").save.eids

    // Reference in both directions saved
    // Since we use different names for the ref attributes on each end,
    // we query from each end. We could as well have had the same attr name.
    living_Animal(rex).Master.name.one === "Ben"
    living_Person(ben).Pet.name.one === "Rex"

    // Traversing forth and back should bring us to the initial value
    living_Animal(rex).Master.Pet.name.one === "Rex"
    living_Person(ben).Pet.Master.name.one === "Ben"
  }


  "Save id" in new Setup {

    val rex = living_Animal.name.insert("Rex").eid

    // Save Ben with bidirectional ref to existing Rex
    val ben = living_Person.name("Ben").pet(rex).save.eid

    living_Animal.name.Master.name.get === List(
      ("Rex", "Ben")
    )
    living_Person.name.Pet.name.get === List(
      ("Ben", "Rex")
    )

    // Saving reference to generic `e` not allowed.
    // (instead apply ref to ref attribute as shown above)
    (living_Person.name("Ben").Pet.e(rex).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
      s"not allowed in save molecules. Found `e($rex)`"
  }


  "Insert new" in new Setup {

    // Insert 2 pairs of bidirectionally referenced entities
    living_Person.name.Pet.name insert List(
      ("Ben", "Rex"),
      ("Kim", "Zip")
    ) eids

    // Bidirectional references have been inserted
    living_Person.name.Pet.name.get.sorted === List(
      ("Ben", "Rex"),
      ("Kim", "Zip")
    )
    living_Animal.name.Master.name.get.sorted === List(
      ("Rex", "Ben"),
      ("Zip", "Kim")
    )
  }

  "Insert id" in new Setup {

    val List(rex, zip) = living_Animal.name insert List("Rex", "Zip") eids

    // Insert 2 new entities and pair them with existing entities
    living_Person.name.pet insert List(
      ("Ben", rex),
      ("Kim", zip)
    )

    // Bidirectional references have been inserted
    living_Person.name.Pet.name.get.sorted === List(
      ("Ben", "Rex"),
      ("Kim", "Zip")
    )
    living_Animal.name.Master.name.get.sorted === List(
      ("Rex", "Ben"),
      ("Zip", "Kim")
    )
  }


  "Update new" >> {

    "creating ref to new" in new Setup {

      val ben = living_Person.name.insert("Ben").eid
      living_Person(ben).Pet.name("Rex").update

      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Rex")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Rex", "Ben")
      )

      // Insert from other end

      val zip = living_Animal.name.insert("Zip").eid
      living_Animal(zip).Master.name("Kim").update

      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Rex"),
        ("Kim", "Zip")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Rex", "Ben"),
        ("Zip", "Kim")
      )
    }


    "replacing ref to new" in new Setup {

      val List(ben, rex) = living_Person.name("Ben").Pet.name("Rex").save.eids

      // Bidirectional references created
      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Rex")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Rex", "Ben")
      )

      // Update Ben with creation of Zip and replacing bidirectional reference with Rex to created Zip
      living_Person(ben).Pet.name("Guz").update

      // Bidirectional references to Rex have been replaced with refs to/from Zip
      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Guz")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Guz", "Ben")
      )
    }
  }


  "Update id" >> {

    "creating ref to existing" in new Setup {

      // Ben haven't got Rex yet
      val ben = living_Person.name.insert("Ben").eid
      val rex = living_Animal.name.insert("Rex").eid

      // Update Ben with creation of bidirectional reference to existing Rex
      living_Person(ben).pet(rex).update

      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Rex")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Rex", "Ben")
      )

      // Referencing the same id is not allowed
      (living_Person(ben).pet(ben).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:biSelfRef]  Current entity and referenced entity ids can't be the same."
    }


    "replacing ref to other existing" in new Setup {

      val List(ben, rex) = living_Person.name("Ben").Pet.name("Rex").save.eids

      // Bidirectional references created
      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Rex")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Rex", "Ben")
      )

      // Update Ben, replacing bidirectional reference with Rex to existing Zip
      val zip = living_Animal.name.insert("Zip").eid
      living_Person(ben).pet(zip).update

      // Bidirectional references to Rex have been replaced with refs to/from Zip
      living_Person.name.Pet.name.get.sorted === List(
        ("Ben", "Zip")
      )
      living_Animal.name.Master.name.get.sorted === List(
        ("Zip", "Ben")
      )
    }
  }


  "Update removing reference" in new Setup {

    val List(ben, rex) = living_Person.name("Ben").Pet.name("Rex").save.eids

    // Bidirectional references created
    living_Person.name.Pet.name.get.sorted === List(
      ("Ben", "Rex")
    )
    living_Animal.name.Master.name.get.sorted === List(
      ("Rex", "Ben")
    )

    // Retract ref between them by applying no value
    living_Person(ben).pet().update

    // Bidirectional references retracted
    living_Person.name.Pet.name.get.sorted === List()
  }


  "Retract" in new Setup {

    val ben = living_Person.name.insert("Ben").eid

    // Create and reference Rex to Ben
    val rex = living_Person(ben).Pet.name("Rex").update.eid

    living_Person.name.Pet.name.get.sorted === List(
      ("Ben", "Rex")
    )
    living_Animal.name.Master.name.get.sorted === List(
      ("Rex", "Ben")
    )

    // Retract Rex and all references to/from Ben
    rex.retract

    // Rex remains and both references retracted
    living_Person.name.get === List("Ben")
    living_Animal.name.get === List()

    living_Person(ben).Pet.name.get === List()
    living_Animal(rex).Master.name.get === List()

    living_Person.name.Pet.name.get.sorted === List()
    living_Animal.name.Master.name.get.sorted === List()
  }
}
