package molecule
package bidirectional

import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec


class SelfMany extends MoleculeSpec {


  "Save new" >> {

    "1 new" in new Setup {

      // Save Ben, Ida and bidirectional references between them
      living_Person.name("Ben").Friends.name("Ida").save.eids

      // Reference is bidirectional - both point to each other
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        // Reverse reference:
        ("Ida", "Ben")
      )
    }


    "n new" in new Setup {

      // Can't save multiple values to cardinality-one attribute
      // It could become unwieldy if different referenced attributes had different number of
      // values (arities) - how many related entities should be created then?
      (living_Person.name("Ben").Friends.name("Ida", "Liz").save must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[output.Molecule:noConflictingCardOneValues (1)] Can't save multiple values for cardinality-one attribute:\n" +
        "  living_Person ... name(Ida, Liz)"

      // We can save a single value though...
      living_Person.name("Ben").Friends.name("Liz").save

      living_Person.name.Friends.name.get === List(
        ("Ben", "Liz"),
        ("Liz", "Ben")
      )


      // Can't save nested data structures
      (living_Person.name("Ben").Friends.*(living_Person.name("Ida")).save must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[output.Molecule.noNested] Nested data structures not allowed in save molecules"

      // So, we can't create multiple referenced entities with the `save` command.
      // Use `insert` for this or save existing entity ids (see below).
    }
  }


  "Save ids" >> {

    "1 id" in new Setup {

      val ida = living_Person.name.insert("Ida").eid

      // Save Ben with bidirectional ref to existing Ida
      living_Person.name("Ben").friends(ida).save.eid

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )

      // Saveing reference to generic `e` not allowed.
      // (instead apply ref to ref attribute as shown above)
      (living_Person.name("Ben").Friends.e(ida).save must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[output.Molecule.noGenerics] Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
        s"not allowed in save molecules. Found `e($ida)`"
    }


    "n ids" in new Setup {

      val idaLizSet = living_Person.name.insert("Ida", "Liz").eidSet

      // Save Ben with bidirectional ref to existing Ida and Liz
      living_Person.name("Ben").friends(idaLizSet).save.eid

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ben", "Liz"),
        // Reverse references:
        ("Ida", "Ben"),
        ("Liz", "Ben")
      )
    }
  }


  "Insert new" >> {

    "1 new" in new Setup {

      // We can treat card-many attributes as card-one attributes:

      // Insert 2 pairs of bidirectionally referenced entities (as with card-one insert)
      living_Person.name.Friends.name insert List(
        ("Ben", "Ida"),
        ("Don", "Tim")
      ) eids

      // Bidirectional references have been inserted
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Don", "Tim"),
        // Reverse references:
        ("Ida", "Ben"),
        ("Tim", "Don")
      )
    }

    "n new" in new Setup {

      // Create multiple bidirectionally referenced entities with a nested molecule
      living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ben", List("Ida", "Liz")),
        ("Don", List("Tim", "Tom"))
      )

      // Bidirectional references have been inserted
      living_Person.name.Friends.*(living_Person.name).get.sortBy(_._1) === List(
        ("Ben", List("Ida", "Liz")),
        ("Don", List("Tim", "Tom")),
        // Reverse refs:
        ("Ida", List("Ben")),
        ("Liz", List("Ben")),
        ("Tim", List("Don")),
        ("Tom", List("Don"))
      )

      // Flat list
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ben", "Liz"),
        ("Don", "Tim"),
        ("Don", "Tom"),
        // Reverse refs:
        ("Ida", "Ben"),
        ("Liz", "Ben"),
        ("Tim", "Don"),
        ("Tom", "Don")
      )
    }
  }


  "Insert existing" >> {

    "new - ref - 1 existing" in new Setup {

      val List(ida, tim) = living_Person.name insert List("Ida", "Tim") eids

      // Insert 2 living_Persons and befriend them with existing living_Persons
      living_Person.name.friends insert List(
        ("Ben", Set(ida, 41L)),
        ("Don", Set(tim))
      )

      // Bidirectional references have been inserted
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Don", "Tim"),
        // Reverse refs:
        ("Ida", "Ben"),
        ("Tim", "Don")
      )
    }

    "new - ref - n existing" in new Setup {

      val List(ida, liz, tim) = living_Person.name insert List("Ida", "Liz", "Tim") eids

      // Insert 2 living_Persons and befriend them with existing living_Persons
      living_Person.name.friends insert List(
        ("Ben", Set(ida, liz)),
        ("Don", Set(ida, tim))
      )

      // Bidirectional references have been inserted - not how Ida got 2 (reverse) friendships
      living_Person.name.Friends.*(living_Person.name).get.sortBy(_._1) === List(
        ("Ben", List("Ida", "Liz")),
        ("Don", List("Ida", "Tim")),
        ("Ida", List("Ben", "Don")),
        ("Liz", List("Ben")),
        ("Tim", List("Don"))
      )

      // Bidirectional references have been inserted
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ben", "Liz"),
        ("Don", "Ida"),
        ("Don", "Tim"),
        ("Ida", "Ben"),
        ("Ida", "Don"),
        ("Liz", "Ben"),
        ("Tim", "Don")
      )
    }
  }


  "Update new" >> {

    "creating ref to 1 new" in new Setup {

      val ben = living_Person.name.insert("Ben").eid

      // Update a with creation of b and bidirectional reference between a and b
      living_Person(ben).Friends.name("Ida").update

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )
    }


    "creating refs to multiple new" in new Setup {

      val ben = living_Person.name.insert("Ben").eid


      // Can't update multiple values of cardinality-one attribute `name`
      (living_Person.name("Ben").Friends.name("Ida", "Liz").update must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[output.Molecule:noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
        "  living_Person ... name(Ida, Liz)"

      // Can't update nested data structures
      (living_Person.name("Ben").Friends.*(living_Person.name("Ida")).update must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[output.Molecule.noNested] Nested data structures not allowed in update molecules"

      // So, we can't create multiple referenced entities with the `update` command.
      // Update ref attribute with already created entity ids instead (see below).
    }


    "replacing ref to 1 new" in new Setup {

      val List(ben, ida) = living_Person.name("Ben").Friends.name("Ida").save.eids

      // Bidirectional references created
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )

      // Update Ben with creation of Liz and bidirectional references to/from her.
      // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
      // and will not cause his friendship to Ida to be retracted as it would be with a
      // cardinality one reference!
      living_Person(ben).Friends.name("Liz").update

      // Bidirectional references to/from Liz has been added.
      // OBS: see note above and difference to cardinality-one updates!
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ben", "Liz"),
        ("Ida", "Ben"),
        ("Liz", "Ben")
      )

      // To remove the friendship to Ida we need to remove it manually
      living_Person(ben).friends.remove(ida).update

      // Now the friendship with Ida has been replaced with the friendship with Liz
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Liz"),
        ("Liz", "Ben")
      )
    }
  }


  "replacing refs to multiple new" in new Setup {

    val List(ben, ida) = living_Person.name("Ben").Friends.name("Ida").save.eids

    // Bidirectional references created
    living_Person.name.Friends.name.get.sorted === List(
      ("Ben", "Ida"),
      ("Ida", "Ben")
    )

    // Update Ben with creation of Liz and bidirectional references to/from her
    // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
    // and will not cause his friendship to Ida to be retracted!
    living_Person(ben).Friends.name("Liz").update

    // Bidirectional references to/from Liz has been added.
    // OBS: see note above and difference to cardinality-one updates!
    living_Person.name.Friends.name.get.sorted === List(
      ("Ben", "Ida"),
      ("Ben", "Liz"),
      ("Ida", "Ben"),
      ("Liz", "Ben")
    )

    // Can't update multiple values of cardinality-one attribute `name`
    (living_Person(ben).Friends.name("Ida", "Liz").update must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[output.Molecule:noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
      "  living_Person ... name(Ida, Liz)"

    // Can't update nested data structures
    (living_Person(ben).Friends.*(living_Person.name("Ida")).update must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[output.Molecule.noNested] Nested data structures not allowed in update molecules"

    // So, we can't create multiple referenced entities with the `update` command.
    // Update ref attribute with existing created entity ids instead (see below).
  }


  "Update existing" >> {

    "creating ref to 1 existing" in new Setup {

      val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids

      // Update a with creation of bidirectional reference to existing b
      living_Person(ben).friends(ida).update

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )
    }

    "creating ref to 1 existing with explicit `add`" in new Setup {

      val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids

      // Update a with creation of bidirectional reference to existing b
      living_Person(ben).friends.add(ida).update

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )
    }


    "creating refs to multiple existing, vararg" in new Setup {

      val List(ben, ida, liz) = living_Person.name.insert("Ben", "Ida", "Liz").eids

      // Update Ben with two new friendships
      living_Person(ben).friends(ida, liz).update

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ben", "Liz"),
        ("Ida", "Ben"),
        ("Liz", "Ben")
      )
    }


    "creating refs to multiple existing, Set" in new Setup {

      val List(ben, ida, liz) = living_Person.name.insert("Ben", "Ida", "Liz").eids

      // Update Ben with two new friendships supplied as a Set
      living_Person(ben).friends(Set(ida, liz)).update

      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ben", "Liz"),
        ("Ida", "Ben"),
        ("Liz", "Ben")
      )
    }
  }


  "Update replacing ref to other existing" in new Setup {

    // Save Ben, Ida and bidirectional references between them
    val List(ben, ida) = living_Person.name("Ben").Friends.name("Ida").save.eids

    // Bidirectional references created
    living_Person.name.Friends.name.get.sorted === List(
      ("Ben", "Ida"),
      ("Ida", "Ben")
    )

    // Update Ben by adding Liz and bidirectional references to/from her
    // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
    // and will not cause his friendship to Ida to be retracted!
    val liz = living_Person.name.insert("Liz").eid
    living_Person(ben).friends(liz).update

    // Bidirectional references to/from Liz has been added.
    // OBS: see note above and difference to cardinality-one updates!
    living_Person.name.Friends.name.get.sorted === List(
      ("Ben", "Ida"),
      ("Ben", "Liz"),
      ("Ida", "Ben"),
      ("Liz", "Ben")
    )

    // To remove the friendship to Ida we need to remove it manually as we did above
    living_Person(ben).friends.remove(ida).update

    // Now the friendship with Ida has been replaced with the friendship with Liz
    living_Person.name.Friends.name.get.sorted === List(
      ("Ben", "Liz"),
      ("Liz", "Ben")
    )
  }


  "Update removing all references" >> {

    "retracting ref" in new Setup {

      // Save Ben, Ida and bidirectional references between them
      val List(ben, ida) = living_Person.name("Ben").Friends.name("Ida").save.eids

      // Bidirectional references created
      living_Person.name.Friends.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )

      // Retract ref between them by applying no value
      living_Person(ben).friends().update

      // Bidirectional references retracted - Ben has no friends anymore
      living_Person.name.Friends.name.get.sorted === List()
    }
  }


  "Retract" in new Setup {

    // (Same as for cardinality-one)

    val ben = living_Person.name.insert("Ben").eid

    // Create and reference b to a
    val ida = living_Person(ben).Friends.name("Ida").update.eid

    living_Person(ben).Friends.name.get === List("Ida")
    living_Person(ida).Friends.name.get === List("Ben")

    living_Person.name.Friends.name.get.sorted === List(
      ("Ben", "Ida"),
      ("Ida", "Ben")
    )

    // Retract a and all references from/to a
    ben.retract

    // Woa remains and both references retracted
    living_Person.name.get === List("Ida")
    living_Person(ben).Friends.name.get === List()
    living_Person(ida).Friends.name.get === List()
    living_Person.name.Friends.name.get.sorted === List()
  }
}
