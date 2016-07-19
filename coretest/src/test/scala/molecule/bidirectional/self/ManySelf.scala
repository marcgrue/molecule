package molecule.bidirectional.self

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._


class ManySelf extends MoleculeSpec {


  "Save" >> {

    "1 new" in new Setup {

      // Save Ann, Ben and bidirectional references between them
      Person.name("Ann").Friends.name("Ben").save.eids

      // Reference is bidirectional - both point to each other
      Person.name.Friends.name.get.sorted === List(
        ("Ann", "Ben"),
        // Reverse reference:
        ("Ben", "Ann")
      )

      // We can now use a uniform query from both ends:
      // Ann and Ben are friends with each other
      Person.name_("Ann").Friends.name.get === List("Ben")
      Person.name_("Ben").Friends.name.get === List("Ann")
    }


    "n new" in new Setup {

      // Can't save multiple values to cardinality-one attribute
      // It could become unwieldy if different referenced attributes had different number of
      // values (arities) - how many related entities should be created then?
      (Person.name("Ann").Friends.name("Ben", "Joe").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't save multiple values for cardinality-one attribute:" +
        "\n  Person ... name(Ben, Joe)"

      // We can save a single value though...
      Person.name("Ann").Friends.name("Joe").save

      Person.name.Friends.name.get === List(
        ("Ann", "Joe"),
        ("Joe", "Ann")
      )

      // Ann and Ben are friends with each other
      Person.name_("Ann").Friends.name.get === List("Joe")
      Person.name_("Joe").Friends.name.get === List("Ann")

      // Can't `save` nested data structures - use nested `insert` instead for that (see tests further down)
      (Person.name("Ann").Friends.*(Person.name("Ben")).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"

      // So, we can't create multiple referenced entities in one go with the `save` command.
      // Use `insert` for this or save existing entity ids (see below).
    }


    "1 existing" in new Setup {

      val ben = Person.name.insert("Ben").eid

      // Save Ann with bidirectional ref to existing Ben
      Person.name("Ann").friends(ben).save.eid

      Person.name.Friends.name.get.sorted === List(
        ("Ann", "Ben"),
        ("Ben", "Ann")
      )

      // Ann and Ben are friends with each other
      Person.name_("Ann").Friends.name.get === List("Ben")
      Person.name_("Ben").Friends.name.get === List("Ann")

      // Saveing reference to generic `e` not allowed.
      // (instead apply ref to ref attribute as shown above)
      (Person.name("Ann").Friends.e(ben).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
        s"not allowed in save molecules. Found `e($ben)`"
    }


    "n existing" in new Setup {

      val benJoe = Person.name.insert("Ben", "Joe").eids

      // Save Ann with bidirectional ref to existing Ben and Joe
      Person.name("Ann").friends(benJoe).save.eid

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
    }
  }


  "Insert" >> {

    "1 new" in new Setup {

      // Insert two bidirectionally connected entities
      Person.name.Friends.name.insert("Ann", "Ben")

      // Bidirectional references have been inserted
      Person.name_("Ann").Friends.name.get === List("Ben")
      Person.name_("Ben").Friends.name.get === List("Ann")
    }

    "1 existing" in new Setup {

      val ben = Person.name("Ben").save.eid

      // Insert Ann with bidirectional ref to existing Ben
      Person.name.friends.insert("Ann", Set(ben))

      // Bidirectional references have been inserted
      Person.name_("Ann").Friends.name.get === List("Ben")
      Person.name_("Ben").Friends.name.get === List("Ann")
    }


    "multiple new" in new Setup {

      // Insert 2 pairs of entities with bidirectional references between them
      Person.name.Friends.name insert List(
        ("Ann", "Joe"),
        ("Ben", "Tim")
      )

      // Bidirectional references have been inserted
      Person.name_("Ann").Friends.name.get === List("Joe")
      Person.name_("Ben").Friends.name.get === List("Tim")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Tim").Friends.name.get === List("Ben")
    }

    "multiple existing" in new Setup {

      val List(joe, tim) = Person.name.insert("Joe", "Tim").eids

      // Insert 2 entities with bidirectional refs to existing entities
      Person.name.friends insert List(
        ("Ann", Set(joe)),
        ("Ben", Set(tim))
      )

      // Bidirectional references have been inserted
      Person.name_("Ann").Friends.name.get === List("Joe")
      Person.name_("Ben").Friends.name.get === List("Tim")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Tim").Friends.name.get === List("Ben")
    }


    "nested new" in new Setup {

      // Insert molecules allow nested data structures. So we can conveniently
      // insert 2 entities each connected to 2 target entites
      Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe")),
        ("Don", List("Tim", "Tom"))
      )

      // Bidirectional references have been inserted
      Person.name.Friends.*(Person.name).get.sortBy(_._1) === List(
        ("Ann", List("Ben", "Joe")),
        ("Ben", List("Ann")),
        ("Don", List("Tim", "Tom")),
        ("Joe", List("Ann")),
        ("Tim", List("Don")),
        ("Tom", List("Don"))
      )
    }

    "nested existing" in new Setup {

      val List(ben, joe, tim) = Person.name insert List("Ben", "Joe", "Tim") eids

      // Insert 2 Persons and connect them with existing Persons
      Person.name.friends insert List(
        ("Ann", Set(ben, joe)),
        ("Don", Set(ben, tim))
      )

      // Bidirectional references have been inserted - not how Ben got 2 (reverse) friendships
      Person.name.Friends.*(Person.name).get.sortBy(_._1) === List(
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

      val List(ann, ben, joe, liz, tom) = Person.name.insert("Ann", "Ben", "Joe", "Liz", "Tom").eids

      // Add friendships in various ways

      // Single reference
      Person(ann).friends.add(ben).update

      // Multiple references (vararg)
      Person(ann).friends.add(joe, liz).update

      // Set of references
      Person(ann).friends.add(Seq(tom)).update

      // Friendships have been added in both directions
      Person.name_("Ann").Friends.name.get.sorted === List("Ben", "Joe", "Liz", "Tom")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Liz").Friends.name.get === List("Ann")
      Person.name_("Tom").Friends.name.get === List("Ann")
    }


    "remove" in new Setup {

      // Insert Ann and friends
      val List(ann, ben, joe, liz, tom, ulf) = Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe", "Liz", "Tom", "Ulf"))
      ) eids

      // Friendships have been inserted in both directions
      Person.name_("Ann").Friends.name.get.sorted === List("Ben", "Joe", "Liz", "Tom", "Ulf")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Liz").Friends.name.get === List("Ann")
      Person.name_("Tom").Friends.name.get === List("Ann")
      Person.name_("Ulf").Friends.name.get === List("Ann")

      // Remove some friendships in various ways

      // Single reference
      Person(ann).friends.remove(ben).update

      // Multiple references (vararg)
      Person(ann).friends.remove(joe, liz).update

      // Set of references
      Person(ann).friends.remove(Seq(tom)).update

      // Correct friendships have been removed in both directions
      Person.name_("Ann").Friends.name.get === List("Ulf")
      Person.name_("Ben").Friends.name.get === List()
      Person.name_("Joe").Friends.name.get === List()
      Person.name_("Liz").Friends.name.get === List()
      Person.name_("Tom").Friends.name.get === List()
      Person.name_("Ulf").Friends.name.get === List("Ann")
    }


    "replace 1" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name)
        .insert("Ann", List("Ben", "Joe")).eids

      val tim = Person.name("Tim").save.eid

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Tim").Friends.name.get === List()

      // Ann replaces Ben with Tim
      Person(ann).friends.replace(ben -> tim).update

      // Ann now friends with Tim instead of Ben
      Person.name_("Ann").Friends.name.get === List("Tim", "Joe")
      Person.name_("Ben").Friends.name.get === List()
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Tim").Friends.name.get === List("Ann")
    }

    "replace multiple" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name)
        .insert("Ann", List("Ben", "Joe")).eids

      val List(tim, tom) = Person.name.insert("Tim", "Tom").eids

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Tim").Friends.name.get === List()
      Person.name_("Tom").Friends.name.get === List()

      // Ann replaces Ben and Joe with Tim and Tom
      Person(ann).friends.replace(ben -> tim, joe -> tom).update

      // Ann is now friends with Tim and Tom instead of Ben and Joe
      // Ben and Joe are no longer friends with Ann either
      Person.name_("Ann").Friends.name.get === List("Tom", "Tim")
      Person.name_("Ben").Friends.name.get === List()
      Person.name_("Joe").Friends.name.get === List()
      Person.name_("Tim").Friends.name.get === List("Ann")
      Person.name_("Tom").Friends.name.get === List("Ann")
    }


    "replace all with 1" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids
      val liz                 = Person.name.insert("Liz").eid

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Liz").Friends.name.get === List()

      // Applying value(s) replaces all existing values!

      // Liz is the new friend
      Person(ann).friends(liz).update

      // Joe and Ann no longer friends
      Person.name_("Ann").Friends.name.get === List("Liz")
      Person.name_("Ben").Friends.name.get === List()
      Person.name_("Joe").Friends.name.get === List()
      Person.name_("Liz").Friends.name.get === List("Ann")
    }


    "replace all with multiple (apply varargs)" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      val liz = Person.name.insert("Liz").eid

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Liz").Friends.name.get === List()

      // Ann now has Ben and Liz as friends
      Person(ann).friends(ben, liz).update

      // Ann and Liz new friends
      // Ann and Joe no longer friends
      Person.name_("Ann").Friends.name.get === List("Ben", "Liz")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List()
      Person.name_("Liz").Friends.name.get === List("Ann")
    }


    "replace all with multiple (apply Set)" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      val liz = Person.name.insert("Liz").eid

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")
      Person.name_("Liz").Friends.name.get === List()

      // Ann now has Ben and Liz as friends
      Person(ann).friends(Seq(ben, liz)).update

      // Ann and Liz new friends
      // Ann and Joe no longer friends
      Person.name_("Ann").Friends.name.get === List("Ben", "Liz")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List()
      Person.name_("Liz").Friends.name.get === List("Ann")
    }


    "remove all (apply no values)" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")

      // Ann has no friends any longer
      Person(ann).friends().update

      // Ann's friendships replaced with no friendships (in both directions)
      Person.name_("Ann").Friends.name.get === List()
      Person.name_("Ben").Friends.name.get === List()
      Person.name_("Joe").Friends.name.get === List()
    }


    "remove all (apply empty Set)" in new Setup {

      val List(ann, ben, joe) = Person.name.Friends.*(Person.name) insert List(
        ("Ann", List("Ben", "Joe"))
      ) eids

      Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
      Person.name_("Ben").Friends.name.get === List("Ann")
      Person.name_("Joe").Friends.name.get === List("Ann")

      // Ann has no friends any longer
      val noFriends = Seq.empty[Long]
      Person(ann).friends(noFriends).update

      // Ann's friendships replaced with no friendships (in both directions)
      Person.name_("Ann").Friends.name.get === List()
      Person.name_("Ben").Friends.name.get === List()
      Person.name_("Joe").Friends.name.get === List()
    }
  }


  "Retract" in new Setup {

    val List(ann, ben, joe) = Person.name.Friends.*(Person.name) insert List(
      ("Ann", List("Ben", "Joe"))
    ) eids

    Person.name_("Ann").Friends.name.get === List("Ben", "Joe")
    Person.name_("Ben").Friends.name.get === List("Ann")
    Person.name_("Joe").Friends.name.get === List("Ann")

    // Retract Ann and all her friendships
    ann.retract

    // Ann doesn't exist anymore
    Person.name("Ann").get === List()
    Person.name_("Ann").Friends.name.get === List()

    // Ben and Joe are no longer friends with Ann
    Person.name_("Ben").Friends.name.get === List()
    Person.name_("Joe").Friends.name.get === List()
  }
}
