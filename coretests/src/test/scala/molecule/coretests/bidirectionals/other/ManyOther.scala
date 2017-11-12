package molecule.coretests.bidirectionals.other

import molecule.Imports._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util._


class ManyOther extends MoleculeSpec {

  class setup extends Setup {
    val animalBuddiesOf = m(Person.name_(?).Buddies.name)
    val personBuddiesOf = m(Animal.name_(?).Buddies.name)
  }

  "Save" >> {

    "1 new" in new setup {

      // Save Ann, Gus and bidirectional references between them
      Person.name("Ann").Buddies.name("Gus").save.eids

      // Reference is bidirectional - both point to each other
      Person.name.Buddies.name.get === List(
        ("Ann", "Gus")
      )
      Animal.name.Buddies.name.get === List(
        ("Gus", "Ann")
      )

      // We can now use a uniform query from both ends:
      // Ann and Gus are buddies with each other
      animalBuddiesOf("Ann").get === List("Gus")
      personBuddiesOf("Gus").get === List("Ann")

      // Forth and back should bring os to the starting point
      Person.name_("Ann").Buddies.Buddies.name.get === List("Ann")
      Animal.name_("Gus").Buddies.Buddies.name.get === List("Gus")
    }


    "n new" in new setup {

      // Can't save multiple values to cardinality-one attribute
      // It could become unwieldy if different referenced attributes had different number of
      // values (arities) - how many related entities should be created then?
      (Person.name("Ann").Buddies.name("Gus", "Leo").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't save multiple values for cardinality-one attribute:" +
        "\n  Animal ... name(Gus, Leo)"

      // We can save a single value though...
      Person.name("Ann").Buddies.name("Leo").save

      Person.name.Buddies.name.get === List(
        ("Ann", "Leo")
      )
      Animal.name.Buddies.name.get === List(
        ("Leo", "Ann")
      )

      // Ann and Gus are buddies with each other
      animalBuddiesOf("Ann").get === List("Leo")
      personBuddiesOf("Leo").get === List("Ann")

      // Can't `save` nested data structures - use nested `insert` instead for that (see tests further down)
      (Person.name("Ann").Buddies.*(Animal.name("Gus")).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"

      // So, we can't create multiple referenced entities in one go with the `save` command.
      // Use `insert` for this or save existing entity ids (see below).
    }


    "1 existing" in new Setup {

      val gus = Animal.name.insert("Gus").eid

      // Save Ann with bidirectional ref to existing Gus
      Person.name("Ann").buddies(gus).save.eid

      // Ann and Gus are buddies with each other
      Person.name.Buddies.name.get === List(
        ("Ann", "Gus")
      )
      Animal.name.Buddies.name.get === List(
        ("Gus", "Ann")
      )

      // Saving reference to generic `e` not allowed.
      // (instead apply ref to ref attribute as shown above)
      (Person.name("Ann").Buddies.e(gus).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `t`, `txInstant` and `op` " +
        s"not allowed in save molecules. Found `e($gus)`"
    }


    "n existing" in new setup {

      val gusLeo = Animal.name.insert("Gus", "Leo").eids

      // Save Ann with bidirectional ref to existing Gus and Leo
      Person.name("Ann").buddies(gusLeo).save.eid

      animalBuddiesOf("Ann").get.toSeq.sorted === List("Gus", "Leo")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
    }
  }


  "Insert" >> {

    "1 new" in new setup {

      // Insert two bidirectionally connected entities
      Person.name.Buddies.name.insert("Ann", "Gus")

      // Bidirectional references have been inserted
      animalBuddiesOf("Ann").get === List("Gus")
      personBuddiesOf("Gus").get === List("Ann")
    }

    "1 existing" in new setup {

      val gus = Animal.name("Gus").save.eid

      // Insert Ann with bidirectional ref to existing Gus
      Person.name.buddies.insert("Ann", Set(gus))

      // Bidirectional references have been inserted
      animalBuddiesOf("Ann").get === List("Gus")
      personBuddiesOf("Gus").get === List("Ann")
    }


    "multiple new" in new setup {

      // Insert 2 pairs of entities with bidirectional references between them
      Person.name.Buddies.name insert List(
        ("Ann", "Gus"),
        ("Bob", "Leo")
      )

      // Bidirectional references have been inserted
      animalBuddiesOf("Ann").get === List("Gus")
      animalBuddiesOf("Bob").get === List("Leo")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Bob")
    }

    "multiple existing" in new setup {

      val List(gus, leo) = Animal.name.insert("Gus", "Leo").eids

      // Insert 2 entities with bidirectional refs to existing entities
      Person.name.buddies insert List(
        ("Ann", Set(gus)),
        ("Bob", Set(leo))
      )

      // Bidirectional references have been inserted
      animalBuddiesOf("Ann").get === List("Gus")
      animalBuddiesOf("Bob").get === List("Leo")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Bob")
    }


    "nested new" in new Setup {

      // Insert molecules allow nested data structures. So we can conveniently
      // insert 2 entities each connected to 2 target entites
      Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo")),
        ("Bob", List("Rex", "Zip"))
      )

      // Bidirectional references have been inserted
      Person.name.Buddies.*(Animal.name).get.toSeq.sortBy(_._1) === List(
        ("Ann", List("Gus", "Leo")),
        ("Bob", List("Rex", "Zip"))
      )
      Animal.name.Buddies.*(Person.name).get.toSeq.sortBy(_._1) === List(
        ("Gus", List("Ann")),
        ("Leo", List("Ann")),
        ("Rex", List("Bob")),
        ("Zip", List("Bob"))
      )
    }

    "nested existing" in new Setup {

      val List(gus, leo, rex) = Animal.name insert List("Gus", "Leo", "Rex") eids

      // Insert 2 Persons and connect them with existing Persons
      Person.name.buddies insert List(
        ("Ann", Set(gus, leo)),
        ("Bob", Set(gus, rex))
      )

      // Bidirectional references have been inserted - not how Gus got 2 (reverse) friendships
      Person.name.Buddies.*(Animal.name).get.toSeq.sortBy(_._1) === List(
        ("Ann", List("Gus", "Leo")),
        ("Bob", List("Gus", "Rex"))
      )
      Animal.name.Buddies.*(Person.name).get.toSeq.sortBy(_._1) === List(
        ("Gus", List("Ann", "Bob")),
        ("Leo", List("Ann")),
        ("Rex", List("Bob"))
      )
    }
  }


  "Update" >> {

    "add" in new setup {

      val ann                      = Person.name("Ann").save.eid
      val List(gus, leo, rex, zip) = Animal.name.insert("Gus", "Leo", "Rex", "Zip").eids

      // Add buddieships in various ways

      // Single reference
      Person(ann).buddies.add(gus).update

      // Multiple references (vararg)
      Person(ann).buddies.add(leo, rex).update

      // Set of references
      Person(ann).buddies.add(Seq(zip)).update

      // Buddieships have been added in both directions
      animalBuddiesOf("Ann").get.toSeq.sorted === List("Gus", "Leo", "Rex", "Zip")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List("Ann")
      personBuddiesOf("Zip").get === List("Ann")
    }


    "remove" in new setup {

      // Insert Ann and buddies
      val List(ann, gus, leo, rex, zip, zup) = Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo", "Rex", "Zip", "Zup"))
      ) eids

      // Buddieships have been inserted in both directions
      animalBuddiesOf("Ann").get.toSeq.sorted === List("Gus", "Leo", "Rex", "Zip", "Zup")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List("Ann")
      personBuddiesOf("Zip").get === List("Ann")
      personBuddiesOf("Zup").get === List("Ann")

      // Remove some buddieships in various ways

      // Single reference
      Person(ann).buddies.remove(gus).update

      // Multiple references (vararg)
      Person(ann).buddies.remove(leo, rex).update

      // Set of references
      Person(ann).buddies.remove(Seq(zip)).update

      // Correct buddieships have been removed in both directions
      animalBuddiesOf("Ann").get === List("Zup")
      personBuddiesOf("Gus").get === List()
      personBuddiesOf("Leo").get === List()
      personBuddiesOf("Rex").get === List()
      personBuddiesOf("Zip").get === List()
      personBuddiesOf("Zup").get === List("Ann")
    }


    "replace 1" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name)
        .insert("Ann", List("Gus", "Leo")).eids

      val rex = Animal.name("Rex").save.eid

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List()

      // Ann replaces Gus with Rex
      Person(ann).buddies.replace(gus -> rex).update

      // Ann now buddies with Rex instead of Gus
      animalBuddiesOf("Ann").get === List("Leo", "Rex")
      personBuddiesOf("Gus").get === List()
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List("Ann")
    }

    "replace multiple" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name)
        .insert("Ann", List("Gus", "Leo")).eids

      val List(rex, zip) = Animal.name.insert("Rex", "Zip").eids

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List()
      personBuddiesOf("Zip").get === List()

      // Ann replaces Gus and Leo with Rex and Zip
      Person(ann).buddies.replace(gus -> rex, leo -> zip).update

      // Ann is now buddies with Rex and Zip instead of Gus and Leo
      // Gus and Leo are no longer buddies with Ann either
      animalBuddiesOf("Ann").get === List("Zip", "Rex")
      personBuddiesOf("Gus").get === List()
      personBuddiesOf("Leo").get === List()
      personBuddiesOf("Rex").get === List("Ann")
      personBuddiesOf("Zip").get === List("Ann")
    }


    "replace all with 1" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids
      val rex                 = Animal.name.insert("Rex").eid

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Rex").get === List()

      // Applying value(s) replaces all existing values!

      // Rex is the new friend
      Person(ann).buddies(rex).update

      // Leo and Ann no longer buddies
      animalBuddiesOf("Ann").get === List("Rex")
      personBuddiesOf("Gus").get === List()
      personBuddiesOf("Leo").get === List()
      personBuddiesOf("Rex").get === List("Ann")
    }


    "replace all with multiple (apply varargs)" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      val rex = Animal.name.insert("Rex").eid

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List()

      // Ann now has Gus and Rex as buddies
      Person(ann).buddies(gus, rex).update

      // Ann and Rex new buddies
      // Ann and Leo no longer buddies
      animalBuddiesOf("Ann").get === List("Rex", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List()
      personBuddiesOf("Rex").get === List("Ann")
    }


    "replace all with multiple (apply Set)" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      val rex = Animal.name.insert("Rex").eid

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")
      personBuddiesOf("Rex").get === List()

      // Ann now has Gus and Rex as buddies
      Person(ann).buddies(Seq(gus, rex)).update

      // Ann and Rex new buddies
      // Ann and Leo no longer buddies
      animalBuddiesOf("Ann").get === List("Rex", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List()
      personBuddiesOf("Rex").get === List("Ann")
    }


    "remove all (apply no values)" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")

      // Ann has no buddies any longer
      Person(ann).buddies().update

      // Ann's buddieships replaced with no buddieships (in both directions)
      animalBuddiesOf("Ann").get === List()
      personBuddiesOf("Gus").get === List()
      personBuddiesOf("Leo").get === List()
    }


    "remove all (apply empty Set)" in new setup {

      val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      animalBuddiesOf("Ann").get === List("Leo", "Gus")
      personBuddiesOf("Gus").get === List("Ann")
      personBuddiesOf("Leo").get === List("Ann")

      // Ann has no buddies any longer
      val noBuddies = Seq.empty[Long]
      Person(ann).buddies(noBuddies).update

      // Ann's buddieships replaced with no buddieships (in both directions)
      animalBuddiesOf("Ann").get === List()
      personBuddiesOf("Gus").get === List()
      personBuddiesOf("Leo").get === List()
    }
  }


  "Retract" in new setup {

    val List(ann, gus, leo) = Person.name.Buddies.*(Animal.name) insert List(
      ("Ann", List("Gus", "Leo"))
    ) eids

    animalBuddiesOf("Ann").get === List("Leo", "Gus")
    personBuddiesOf("Gus").get === List("Ann")
    personBuddiesOf("Leo").get === List("Ann")

    // Retract Leo and his relationship to Ann
    leo.retract

    // Leo is gone
    Person.name.get === List("Ann")
    Animal.name.get === List("Gus")

    // Ann and Gus remain buddies
    Person(ann).Buddies.name.get === List("Gus")
    Animal(gus).Buddies.name.get === List("Ann")
    Animal(leo).Buddies.name.get === List()
  }
}
