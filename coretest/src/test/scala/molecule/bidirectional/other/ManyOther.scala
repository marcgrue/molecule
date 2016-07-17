package molecule.bidirectional.other

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._


class ManyOther extends MoleculeSpec {


  "Save" >> {

    "1 new" in new Setup {

      // Save Ann, Gus and bidirectional references between them
      living_Person.name("Ann").Buddies.name("Gus").save.eids

      // Reference is bidirectional - both point to each other
      living_Person.name.Buddies.name.get === List(
        ("Ann", "Gus")
      )
      living_Animal.name.Buddies.name.get === List(
        ("Gus", "Ann")
      )

      // We can now use a uniform query from both ends:
      // Ann and Gus are buddies with each other
      living_Person.name_("Ann").Buddies.name.get === List("Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")

      // Forth and back should bring os to the starting point
      living_Person.name_("Ann").Buddies.Buddies.name.get === List("Ann")
      living_Animal.name_("Gus").Buddies.Buddies.name.get === List("Gus")
    }


    "n new" in new Setup {

      // Can't save multiple values to cardinality-one attribute
      // It could become unwieldy if different referenced attributes had different number of
      // values (arities) - how many related entities should be created then?
      (living_Person.name("Ann").Buddies.name("Gus", "Leo").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't save multiple values for cardinality-one attribute:" +
        "\n  living_Animal ... name(Gus, Leo)"

      // We can save a single value though...
      living_Person.name("Ann").Buddies.name("Leo").save

      living_Person.name.Buddies.name.get === List(
        ("Ann", "Leo")
      )
      living_Animal.name.Buddies.name.get === List(
        ("Leo", "Ann")
      )

      // Ann and Gus are buddies with each other
      living_Person.name_("Ann").Buddies.name.get === List("Leo")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")

      // Can't `save` nested data structures - use nested `insert` instead for that (see tests further down)
      (living_Person.name("Ann").Buddies.*(living_Animal.name("Gus")).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"

      // So, we can't create multiple referenced entities in one go with the `save` command.
      // Use `insert` for this or save existing entity ids (see below).
    }


    "1 existing" in new Setup {

      val gus = living_Animal.name.insert("Gus").eid

      // Save Ann with bidirectional ref to existing Gus
      living_Person.name("Ann").buddies(gus).save.eid

      // Ann and Gus are buddies with each other
      living_Person.name.Buddies.name.get === List(
        ("Ann", "Gus")
      )
      living_Animal.name.Buddies.name.get === List(
        ("Gus", "Ann")
      )

      // Saving reference to generic `e` not allowed.
      // (instead apply ref to ref attribute as shown above)
      (living_Person.name("Ann").Buddies.e(gus).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
        s"not allowed in save molecules. Found `e($gus)`"
    }


    "n existing" in new Setup {

      val gusLeo = living_Animal.name.insert("Gus", "Leo").eids

      // Save Ann with bidirectional ref to existing Gus and Leo
      living_Person.name("Ann").buddies(gusLeo).save.eid

      living_Person.name_("Ann").Buddies.name.get.sorted === List("Gus", "Leo")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
    }
  }


  "Insert" >> {

    "1 new" in new Setup {

      // Insert two bidirectionally connected entities
      living_Person.name.Buddies.name.insert("Ann", "Gus")

      // Bidirectional references have been inserted
      living_Person.name_("Ann").Buddies.name.get === List("Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
    }

    "1 existing" in new Setup {

      val gus = living_Animal.name("Gus").save.eid

      // Insert Ann with bidirectional ref to existing Gus
      living_Person.name.buddies.insert("Ann", Set(gus))

      // Bidirectional references have been inserted
      living_Person.name_("Ann").Buddies.name.get === List("Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
    }


    "multiple new" in new Setup {

      // Insert 2 pairs of entities with bidirectional references between them
      living_Person.name.Buddies.name insert List(
        ("Ann", "Gus"),
        ("Bob", "Leo")
      )

      // Bidirectional references have been inserted
      living_Person.name_("Ann").Buddies.name.get === List("Gus")
      living_Person.name_("Bob").Buddies.name.get === List("Leo")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Bob")
    }

    "multiple existing" in new Setup {

      val List(gus, leo) = living_Animal.name.insert("Gus", "Leo").eids

      // Insert 2 entities with bidirectional refs to existing entities
      living_Person.name.buddies insert List(
        ("Ann", Set(gus)),
        ("Bob", Set(leo))
      )

      // Bidirectional references have been inserted
      living_Person.name_("Ann").Buddies.name.get === List("Gus")
      living_Person.name_("Bob").Buddies.name.get === List("Leo")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Bob")
    }


    "nested new" in new Setup {

      // Insert molecules allow nested data structures. So we can conveniently
      // insert 2 entities each connected to 2 target entites
      living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo")),
        ("Bob", List("Rex", "Zip"))
      )

      // Bidirectional references have been inserted
      living_Person.name.Buddies.*(living_Animal.name).get.sortBy(_._1) === List(
        ("Ann", List("Gus", "Leo")),
        ("Bob", List("Rex", "Zip"))
      )
      living_Animal.name.Buddies.*(living_Person.name).get.sortBy(_._1) === List(
        ("Gus", List("Ann")),
        ("Leo", List("Ann")),
        ("Rex", List("Bob")),
        ("Zip", List("Bob"))
      )
    }

    "nested existing" in new Setup {

      val List(gus, leo, rex) = living_Animal.name insert List("Gus", "Leo", "Rex") eids

      // Insert 2 living_Persons and connect them with existing living_Persons
      living_Person.name.buddies insert List(
        ("Ann", Set(gus, leo)),
        ("Bob", Set(gus, rex))
      )

      // Bidirectional references have been inserted - not how Gus got 2 (reverse) friendships
      living_Person.name.Buddies.*(living_Animal.name).get.sortBy(_._1) === List(
        ("Ann", List("Gus", "Leo")),
        ("Bob", List("Gus", "Rex"))
      )
      living_Animal.name.Buddies.*(living_Person.name).get.sortBy(_._1) === List(
        ("Gus", List("Ann", "Bob")),
        ("Leo", List("Ann")),
        ("Rex", List("Bob"))
      )
    }
  }


  "Update" >> {

    "add" in new Setup {

      val ann                      = living_Person.name("Ann").save.eid
      val List(gus, leo, rex, zip) = living_Animal.name.insert("Gus", "Leo", "Rex", "Zip").eids

      // Add buddieships in various ways

      // Single reference
      living_Person(ann).buddies.add(gus).update

      // Multiple references (vararg)
      living_Person(ann).buddies.add(leo, rex).update

      // Set of references
      living_Person(ann).buddies.add(Seq(zip)).update

      // Buddieships have been added in both directions
      living_Person.name_("Ann").Buddies.name.get.sorted === List("Gus", "Leo", "Rex", "Zip")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
      living_Animal.name_("Zip").Buddies.name.get === List("Ann")
    }


    "remove" in new Setup {

      // Insert Ann and buddies
      val List(ann, gus, leo, rex, zip, zup) = living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo", "Rex", "Zip", "Zup"))
      ) eids

      // Buddieships have been inserted in both directions
      living_Person.name_("Ann").Buddies.name.get.sorted === List("Gus", "Leo", "Rex", "Zip", "Zup")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
      living_Animal.name_("Zip").Buddies.name.get === List("Ann")
      living_Animal.name_("Zup").Buddies.name.get === List("Ann")

      // Remove some buddieships in various ways

      // Single reference
      living_Person(ann).buddies.remove(gus).update

      // Multiple references (vararg)
      living_Person(ann).buddies.remove(leo, rex).update

      // Set of references
      living_Person(ann).buddies.remove(Seq(zip)).update

      // Correct buddieships have been removed in both directions
      living_Person.name_("Ann").Buddies.name.get === List("Zup")
      living_Animal.name_("Gus").Buddies.name.get === List()
      living_Animal.name_("Leo").Buddies.name.get === List()
      living_Animal.name_("Rex").Buddies.name.get === List()
      living_Animal.name_("Zip").Buddies.name.get === List()
      living_Animal.name_("Zup").Buddies.name.get === List("Ann")
    }


    "replace 1" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name)
        .insert("Ann", List("Gus", "Leo")).eids

      val rex = living_Animal.name("Rex").save.eid

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List()

      // Ann replaces Gus with Rex
      living_Person(ann).buddies.replace(gus -> rex).update

      // Ann now buddies with Rex instead of Gus
      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Rex")
      living_Animal.name_("Gus").Buddies.name.get === List()
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
    }

    "replace multiple" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name)
        .insert("Ann", List("Gus", "Leo")).eids

      val List(rex, zip) = living_Animal.name.insert("Rex", "Zip").eids

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List()
      living_Animal.name_("Zip").Buddies.name.get === List()

      // Ann replaces Gus and Leo with Rex and Zip
      living_Person(ann).buddies.replace(gus -> rex, leo -> zip).update

      // Ann is now buddies with Rex and Zip instead of Gus and Leo
      // Gus and Leo are no longer buddies with Ann either
      living_Person.name_("Ann").Buddies.name.get === List("Zip", "Rex")
      living_Animal.name_("Gus").Buddies.name.get === List()
      living_Animal.name_("Leo").Buddies.name.get === List()
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
      living_Animal.name_("Zip").Buddies.name.get === List("Ann")
    }


    "replace all with 1" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids
      val rex                 = living_Animal.name.insert("Rex").eid

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List()

      // Applying value(s) replaces all existing values!

      // Rex is the new friend
      living_Person(ann).buddies(rex).update

      // Leo and Ann no longer buddies
      living_Person.name_("Ann").Buddies.name.get === List("Rex")
      living_Animal.name_("Gus").Buddies.name.get === List()
      living_Animal.name_("Leo").Buddies.name.get === List()
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
    }


    "replace all with multiple (apply varargs)" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      val rex = living_Animal.name.insert("Rex").eid

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List()

      // Ann now has Gus and Rex as buddies
      living_Person(ann).buddies(gus, rex).update

      // Ann and Rex new buddies
      // Ann and Leo no longer buddies
      living_Person.name_("Ann").Buddies.name.get === List("Rex", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List()
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
    }


    "replace all with multiple (apply Set)" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      val rex = living_Animal.name.insert("Rex").eid

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")
      living_Animal.name_("Rex").Buddies.name.get === List()

      // Ann now has Gus and Rex as buddies
      living_Person(ann).buddies(Seq(gus, rex)).update

      // Ann and Rex new buddies
      // Ann and Leo no longer buddies
      living_Person.name_("Ann").Buddies.name.get === List("Rex", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List()
      living_Animal.name_("Rex").Buddies.name.get === List("Ann")
    }


    "remove all (apply no values)" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")

      // Ann has no buddies any longer
      living_Person(ann).buddies().update

      // Ann's buddieships replaced with no buddieships (in both directions)
      living_Person.name_("Ann").Buddies.name.get === List()
      living_Animal.name_("Gus").Buddies.name.get === List()
      living_Animal.name_("Leo").Buddies.name.get === List()
    }


    "remove all (apply empty Set)" in new Setup {

      val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name) insert List(
        ("Ann", List("Gus", "Leo"))
      ) eids

      living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
      living_Animal.name_("Gus").Buddies.name.get === List("Ann")
      living_Animal.name_("Leo").Buddies.name.get === List("Ann")

      // Ann has no buddies any longer
      val noBuddies = Seq.empty[Long]
      living_Person(ann).buddies(noBuddies).update

      // Ann's buddieships replaced with no buddieships (in both directions)
      living_Person.name_("Ann").Buddies.name.get === List()
      living_Animal.name_("Gus").Buddies.name.get === List()
      living_Animal.name_("Leo").Buddies.name.get === List()
    }
  }


  "Retract" in new Setup {

    val List(ann, gus, leo) = living_Person.name.Buddies.*(living_Animal.name) insert List(
      ("Ann", List("Gus", "Leo"))
    ) eids

    living_Person.name_("Ann").Buddies.name.get === List("Leo", "Gus")
    living_Animal.name_("Gus").Buddies.name.get === List("Ann")
    living_Animal.name_("Leo").Buddies.name.get === List("Ann")

    // Retract Leo and his relationship to Ann
    leo.retract

    // Leo is gone
    living_Person.name.get === List("Ann")
    living_Animal.name.get === List("Gus")

    // Ann and Gus remain buddies
    living_Person(ann).Buddies.name.get === List("Gus")
    living_Animal(gus).Buddies.name.get === List("Ann")
    living_Animal(leo).Buddies.name.get === List()
  }
}
