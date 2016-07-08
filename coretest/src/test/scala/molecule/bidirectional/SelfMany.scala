package molecule
package bidirectional

import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec


class SelfMany extends MoleculeSpec {


  "Save" >> {

    "1 new" in new Setup {

      // Save Ann, Ben and bidirectional references between them
      living_Person.name("Ann").Friends.name("Ben").save.eids

      // Reference is bidirectional - both point to each other
      living_Person.name.Friends.name.get.sorted === List(
        ("Ann", "Ben"),
        // Reverse reference:
        ("Ben", "Ann")
      )

      // We can now use a uniform query from both ends:
      // Ann and Ben are friends with each other
      living_Person.name_("Ann").Friends.name.get === List("Ben")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
    }


    "n new" in new Setup {

      // Can't save multiple values to cardinality-one attribute
      // It could become unwieldy if different referenced attributes had different number of
      // values (arities) - how many related entities should be created then?
      (living_Person.name("Ann").Friends.name("Ben", "Joe").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noConflictingCardOneValues] Can't save multiple values for cardinality-one attribute:\n" +
        "  living_Person ... name(Ben, Joe)"

      // We can save a single value though...
      living_Person.name("Ann").Friends.name("Joe").save

      living_Person.name.Friends.name.get === List(
        ("Ann", "Joe"),
        ("Joe", "Ann")
      )

      // Ann and Ben are friends with each other
      living_Person.name_("Ann").Friends.name.get === List("Joe")
      living_Person.name_("Joe").Friends.name.get === List("Ann")

      // Can't `save` nested data structures - use nested `insert` instead for that (see tests further down)
      (living_Person.name("Ann").Friends.*(living_Person.name("Ben")).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"

      // So, we can't create multiple referenced entities in one go with the `save` command.
      // Use `insert` for this or save existing entity ids (see below).
    }


    "1 existing" in new Setup {

      val ben = living_Person.name.insert("Ben").eid

      // Save Ann with bidirectional ref to existing Ben
      living_Person.name("Ann").friends(ben).save.eid

      living_Person.name.Friends.name.get.sorted === List(
        ("Ann", "Ben"),
        ("Ben", "Ann")
      )

      // Ann and Ben are friends with each other
      living_Person.name_("Ann").Friends.name.get === List("Ben")
      living_Person.name_("Ben").Friends.name.get === List("Ann")

      // Saveing reference to generic `e` not allowed.
      // (instead apply ref to ref attribute as shown above)
      (living_Person.name("Ann").Friends.e(ben).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noGenerics] Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
        s"not allowed in save molecules. Found `e($ben)`"
    }


    "n existing" in new Setup {

      val benJoeSet = living_Person.name.insert("Ben", "Joe").eidSet

      // Save Ann with bidirectional ref to existing Ben and Joe
      living_Person.name("Ann").friends(benJoeSet).save.eid

      living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")
    }
  }


  "Insert" >> {

    "1 new" in new Setup {

      // We can treat card-many attributes as card-one attributes:

      // Insert 2 pairs of bidirectionally referenced entities (as with card-one insert)
      living_Person.name.Friends.name insert List(
        ("Ann", "Joe"),
        ("Ben", "Tim")
      ) eids

      // Bidirectional references have been inserted
      living_Person.name_("Ann").Friends.name.get === List("Joe")
      living_Person.name_("Ben").Friends.name.get === List("Tim")
      living_Person.name_("Joe").Friends.name.get === List("Ann")
      living_Person.name_("Tim").Friends.name.get === List("Ben")
    }


    "n new" in new Setup {

      // Create multiple bidirectionally referenced entities with a nested molecule
      living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe")),
        ("Don", List("Tim", "Tom"))
      )

      // Bidirectional references have been inserted
      living_Person.name.Friends.*(living_Person.name).get.sortBy(_._1) === List(
        ("Ann", List("Ben", "Joe")),
        ("Ben", List("Ann")),
        ("Don", List("Tim", "Tom")),
        ("Joe", List("Ann")),
        ("Tim", List("Don")),
        ("Tom", List("Don"))
      )
    }


    "1 existing" in new Setup {

      val List(joe, tim) = living_Person.name insert List("Joe", "Tim") eids

      // Insert 2 living_Persons and befriend them with existing living_Persons
      living_Person.name.friends insert List(
        ("Ann", Set(joe)),
        ("Ben", Set(tim))
      )

      // Bidirectional references have been inserted
      living_Person.name.Friends.name.get.sorted === List(
        ("Ann", "Joe"),
        ("Ben", "Tim"),
        // Reverse refs:
        ("Joe", "Ann"),
        ("Tim", "Ben")
      )
    }


    "n existing" in new Setup {

      val List(ben, joe, tim) = living_Person.name insert List("Ben", "Joe", "Tim") eids

      // Insert 2 living_Persons and connect them with existing living_Persons
      living_Person.name.friends insert List(
        ("Ann", Set(ben, joe)),
        ("Don", Set(ben, tim))
      )

      // Bidirectional references have been inserted - not how Ben got 2 (reverse) friendships
      living_Person.name.Friends.*(living_Person.name).get.sortBy(_._1) === List(
        ("Ann", List("Ben", "Joe")),
        ("Ben", List("Ann", "Don")),
        ("Don", List("Ben", "Tim")),
        ("Joe", List("Ann")),
        ("Tim", List("Don"))
      )
    }
  }


  "Update" >> {

    "add" in new Setup {

      val List(ann, ben, joe, liz, tom) = living_Person.name.insert("Ann", "Ben", "Joe", "Liz", "Tom").eids

      // Add friendships in various ways

      // Single reference
      living_Person(ann).friends.add(ben).update

      // Multiple references (vararg)
      living_Person(ann).friends.add(joe, liz).update

      // Set of references
      living_Person(ann).friends.add(Set(tom)).update

      // Friendships have been added in both directions
      living_Person.name_("Ann").Friends.name.get.sorted === List("Ben", "Joe", "Liz", "Tom")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")
      living_Person.name_("Liz").Friends.name.get === List("Ann")
      living_Person.name_("Tom").Friends.name.get === List("Ann")
    }


    "remove" in new Setup {

      // Insert Ann and friends
      val List(ann, ben, joe, liz, tom, ulf) = living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe", "Liz", "Tom", "Ulf"))
      ) eids

      // Friendships have been inserted in both directions
      living_Person.name_("Ann").Friends.name.get.sorted === List("Ben", "Joe", "Liz", "Tom", "Ulf")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")
      living_Person.name_("Liz").Friends.name.get === List("Ann")
      living_Person.name_("Tom").Friends.name.get === List("Ann")
      living_Person.name_("Ulf").Friends.name.get === List("Ann")

      // Remove some friendships in various ways

      // Single reference
      living_Person(ann).friends.remove(ben).update

      // Multiple references (vararg)
      living_Person(ann).friends.remove(joe, liz).update

      // Set of references
      living_Person(ann).friends.remove(Set(tom)).update

      // Correct friendships have been removed in both directions
      living_Person.name_("Ann").Friends.name.get === List("Ulf")
      living_Person.name_("Ben").Friends.name.get === List()
      living_Person.name_("Joe").Friends.name.get === List()
      living_Person.name_("Liz").Friends.name.get === List()
      living_Person.name_("Tom").Friends.name.get === List()
      living_Person.name_("Ulf").Friends.name.get === List("Ann")
    }


    "replace all with 1" in new Setup {

      val List(ann, ben, joe) = living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")

      // Ann now only has Ben as friend
      living_Person(ann).friends(ben).update

      // Joe and Ann no longer friends
      living_Person.name_("Ann").Friends.name.get === List("Ben")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List()
    }


    "replace all with multiple (apply varargs)" in new Setup {

      val List(ann, ben, joe) = living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      val liz = living_Person.name.insert("Liz").eid

      living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")
      living_Person.name_("Liz").Friends.name.get === List()

      // Ann now has Ben and Liz as friends
      living_Person(ann).friends(ben, liz).update

      // Ann and Liz new friends
      // Ann and Joe no longer friends
      living_Person.name_("Ann").Friends.name.get === List("Ben", "Liz")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List()
      living_Person.name_("Liz").Friends.name.get === List("Ann")
    }


    "replace all with multiple (apply Set)" in new Setup {

      val List(ann, ben, joe) = living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      val liz = living_Person.name.insert("Liz").eid

      living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")
      living_Person.name_("Liz").Friends.name.get === List()

      // Ann now has Ben and Liz as friends
      living_Person(ann).friends(Set(ben, liz)).update

      // Ann and Liz new friends
      // Ann and Joe no longer friends
      living_Person.name_("Ann").Friends.name.get === List("Ben", "Liz")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List()
      living_Person.name_("Liz").Friends.name.get === List("Ann")
    }


    "remove all (apply no values)" in new Setup {

      val List(ann, ben, joe) = living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")

      // Ann has no friends any longer
      living_Person(ann).friends().update

      // Ann's friendships replaced with no friendships (in both directions)
      living_Person.name_("Ann").Friends.name.get === List()
      living_Person.name_("Ben").Friends.name.get === List()
      living_Person.name_("Joe").Friends.name.get === List()
    }


    "remove all (apply empty Set)" in new Setup {

      val List(ann, ben, joe) = living_Person.name.Friends.*(living_Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      living_Person.name_("Ben").Friends.name.get === List("Ann")
      living_Person.name_("Joe").Friends.name.get === List("Ann")

      // Ann has no friends any longer
      val noFriends = Set.empty[Long]
      living_Person(ann).friends(noFriends).update

      // Ann's friendships replaced with no friendships (in both directions)
      living_Person.name_("Ann").Friends.name.get === List()
      living_Person.name_("Ben").Friends.name.get === List()
      living_Person.name_("Joe").Friends.name.get === List()
    }
  }


  "Retract" in new Setup {

    val List(ann, ben, joe) = living_Person.name.Friends.*(living_Person.name) insert List(
      ("Ann", List("Ben", "Joe"))
    ) eids

    living_Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
    living_Person.name_("Ben").Friends.name.get === List("Ann")
    living_Person.name_("Joe").Friends.name.get === List("Ann")

    // Retract Ann and all her friendships
    ann.retract

    // Ann doesn't exist anymore
    living_Person.name("Ann").get === List()
    living_Person.name_("Ann").Friends.name.get === List()

    // Ben and Joe are no longer friends with Ann
    living_Person.name_("Ben").Friends.name.get === List()
    living_Person.name_("Joe").Friends.name.get === List()
  }
}
