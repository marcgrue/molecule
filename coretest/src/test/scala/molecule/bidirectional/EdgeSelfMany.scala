package molecule
package bidirectional

import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec


class EdgeSelfMany extends MoleculeSpec {


  "Save new" >> {

    "1 new" in new Setup {

      // Save Ben, Ida and bidirectional references between them
//      living_Person.name("Ben").Knows2.weight(6).Person2.name("Ida").saveD

//      living_Person.name("Ben").Knows.weight(7).saveD
//      living_Person.name("Ben").Knows.weight(7).save
//      living_Knows.weight.Person.name.get.sorted === List()

//      living_Knows.weight(7).Person.name("Ida").saveD
//      living_Knows.weight(7).Person.name("Ida").save
//
//      living_Knows.weight.Person.name.get === List((7, "Ida"))
//      living_Person.name.Knows.weight.get === List(("Ida", 7))

      living_Person.name("Ben").Knows.weight(7).friendshipType(42L).Person.name("Ida").saveD
//      living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save
//
//      // Reference is bidirectional - both point to each other
////      living_Person.name.Knows.weight.get.sorted === List()
//

      living_Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ben", 7, "Ida"),
        // Reverse reference:
        ("Ida", 7, "Ben")
      )

//      ok
    }
    /*

## 1 ## output.Molecule.saveD
========================================================================
1          Model(
  1          Atom(living_Person,name,String,1,Eq(List(Ben)),None,List(),List())
  2          Bond(living_Person,knows,living_Knows,2,edgeRef@:living_Knows/person)
  3          Atom(living_Knows,weight,Long,1,Eq(List(7)),None,List(EdgePropAttr),List())
  4          Atom(living_Knows,friendshipType,Long,1,Eq(List(42)),None,List(EdgePropRef),List())
  5          Bond(living_Knows,person,living_Person,1,targetRef@:living_Person/knows)
  6          Atom(living_Person,name,String,1,Eq(List(Ida)),None,List(),List()))
------------------------------------------------
2          List(
  1          :db/add       'tempId                            :living_Person/name            Values(Eq(List(Ben)),None)
  2          :db/add       'e                                 :living_Person/knows           :living_Knows                           edgeRef@2@:living_Knows/person
  3          :db/add       'v                                 :living_Knows/weight           Values(Eq(List(7)),None)
  4          :db/add       'e                                 :living_Knows/friendshipType   Values(Eq(List(42)),None)
  5          :db/add       'e                                 :living_Knows/person           :living_Person                          targetRef@1@:living_Person/knows
  6          :db/add       'v                                 :living_Person/name            Values(Eq(List(Ida)),None)              )
------------------------------------------------
3          List(
  1          :db/add       #db/id[:living -1000015]           :living_Person/name            Ben

  2          :db/add       #db/id[:living -1000016]           :living_Knows/person           #db/id[:living -1000015]
  3          :db/add       #db/id[:living -1000015]           :living_Person/knows           #db/id[:living -1000016]

  4          :db/add       #db/id[:living -1000016]           :living_Knows/weight           7

  5          :db/add       #db/id[:living -1000016]           :living_Knows/friendshipType   42

  6          :db/add       #db/id[:living -1000017]           :living_Person/knows           #db/id[:living -1000016]
  7          :db/add       #db/id[:living -1000016]           :living_Knows/person           #db/id[:living -1000017]

  8          :db/add       #db/id[:living -1000017]           :living_Person/name            Ida                                     )
========================================================================


3          List(
  1          :db/add       #db/id[:living -1000015]           :living_Person/name            Ben

  3          :db/add       #db/id[:living -1000015]           :living_Person/knows           #db/id[:living -1000016]     // A --> X
  2          :db/add       #db/id[:living -1000017]           :living_Knows/person           #db/id[:living -1000015]     // Y --> A
  2          :db/add       #db/id[:living -1000016]           :molecule_Edge/otherId         #db/id[:living -1000017]     // X --> Y
  2          :db/add       #db/id[:living -1000017]           :molecule_Edge/otherId         #db/id[:living -1000016]     // Y --> X

  4          :db/add       #db/id[:living -1000016]           :living_Knows/weight           7                            // edgePropAttr X
  4          :db/add       #db/id[:living -1000017]           :living_Knows/weight           7                            // edgePropAttr Y

  5          :db/add       #db/id[:living -1000016]           :living_Knows/friendshipType   42                           // edgePropRef X
  5          :db/add       #db/id[:living -1000017]           :living_Knows/friendshipType   42                           // edgePropRef Y

  7          :db/add       #db/id[:living -1000016]           :living_Knows/person           #db/id[:living -1000018]     // X --> B
  6          :db/add       #db/id[:living -1000018]           :living_Person/knows           #db/id[:living -1000017]     // B --> Y

  8          :db/add       #db/id[:living -1000018]           :living_Person/name            Ida                                     )





  */


//    "n new" in new BidirectionalSetup {
//
//      // Can't save multiple values to cardinality-one attribute
//      (Person.name("Ben").Knows.weight(7).Person.name("Ida", "Liz").save must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//        s"[output.Molecule:noConflictingCardOneValues (1)] Can't save multiple values for cardinality-one attribute:\n" +
//        "  living_Person ... name(Ida, Liz)"
//
//      // Can't save nested data structures
////      (Person.name("Ben").Knows.weight(7).Person.*(Person.name("Ida")).save must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
////        s"[output.Molecule.noNested] Nested data structures not allowed in save molecules"
//
//      // So, we can't create multiple referenced entities with the `save` command.
//      // Use `insert` for this or save existing entity ids (see below).
//    }
  }


//  "Save ids" >> {
//
//    "1 id" in new BidirectionalSetup {
//
//      val ida = living_Person.name.insert("Ida").eid
//
//      // Save Ben with bidirectional ref to existing Ida
//      living_Person.name("Ben").Knows.weight(7).person(ida).save.eid
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//
//      // Saveing reference to generic `e` not allowed.
//      // (instead apply ref to ref attribute as shown above)
//      (Person.name("Ben").Knows.weight(7).Person.e(ida).save must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//        s"[output.Molecule.noGenerics] Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
//        s"not allowed in save molecules. Found `e($ida)`"
//    }
//
//
//    "n ids" in new BidirectionalSetup {
//
//      val idaLizSet = living_Person.name.insert("Ida", "Liz").eidSet
//
//      // Save Ben with bidirectional ref to existing Ida and Liz
//      living_Person.name("Ben").Knows.weight(7).person(idaLizSet).save.eid
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        // Reverse references:
//        ("Ida", "Ben"),
//        ("Liz", "Ben")
//      )
//    }
//  }


//  "Insert new" >> {
//
//    "1 new" in new BidirectionalSetup {
//
//      // We can treat card-many attributes as card-one attributes:
//
//      // Insert 2 pairs of bidirectionally referenced entities (as with card-one insert)
//      living_Person.name.Knows.weight.Person.name insert List(
//        ("Ben", "Ida"),
//        ("Don", "Tim")
//      ) eids
//
//      // Bidirectional references have been inserted
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Don", "Tim"),
//        // Reverse references:
//        ("Ida", "Ben"),
//        ("Tim", "Don")
//      )
//    }
//
//    "n new" in new BidirectionalSetup {
//
//      // Create multiple bidirectionally referenced entities with a nested molecule
//      living_Person.name.Knows.weight.Person.*(Person.name) insert List(
//        ("Ben", List("Ida", "Liz")),
//        ("Don", List("Tim", "Tom"))
//      )
//
//      // Bidirectional references have been inserted
//      living_Person.name.Knows.weight.Person.*(Person.name).get.sortBy(_._1) === List(
//        ("Ben", List("Ida", "Liz")),
//        ("Don", List("Tim", "Tom")),
//        // Reverse refs:
//        ("Ida", List("Ben")),
//        ("Liz", List("Ben")),
//        ("Tim", List("Don")),
//        ("Tom", List("Don"))
//      )
//
//      // Flat list
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Don", "Tim"),
//        ("Don", "Tom"),
//        // Reverse refs:
//        ("Ida", "Ben"),
//        ("Liz", "Ben"),
//        ("Tim", "Don"),
//        ("Tom", "Don")
//      )
//    }
//  }
//
//
//  "Insert existing" >> {
//
//    "new - ref - 1 existing" in new BidirectionalSetup {
//
//      val List(ida, tim) = living_Person.name insert List("Ida", "Tim") eids
//
//      // Insert 2 living_Persons and befriend them with existing living_Persons
//      living_Person.name.friends insert List(
//        ("Ben", Set(ida, 41L)),
//        ("Don", Set(tim))
//      )
//
//      // Bidirectional references have been inserted
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Don", "Tim"),
//        // Reverse refs:
//        ("Ida", "Ben"),
//        ("Tim", "Don")
//      )
//    }
//
//    "new - ref - n existing" in new BidirectionalSetup {
//
//      val List(ida, liz, tim) = living_Person.name insert List("Ida", "Liz", "Tim") eids
//
//      // Insert 2 living_Persons and befriend them with existing living_Persons
//      living_Person.name.friends insert List(
//        ("Ben", Set(ida, liz)),
//        ("Don", Set(ida, tim))
//      )
//
//      // Bidirectional references have been inserted - not how Ida got 2 (reverse) friendships
//      living_Person.name.Knows.weight.Person.*(Person.name).get.sortBy(_._1) === List(
//        ("Ben", List("Ida", "Liz")),
//        ("Don", List("Ida", "Tim")),
//        ("Ida", List("Ben", "Don")),
//        ("Liz", List("Ben")),
//        ("Tim", List("Don"))
//      )
//
//      // Bidirectional references have been inserted
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Don", "Ida"),
//        ("Don", "Tim"),
//        ("Ida", "Ben"),
//        ("Ida", "Don"),
//        ("Liz", "Ben"),
//        ("Tim", "Don")
//      )
//    }
//  }
//
//
//  "Update new" >> {
//
//    "creating ref to 1 new" in new BidirectionalSetup {
//
//      val ben = living_Person.name.insert("Ben").eid
//
//      // Update a with creation of b and bidirectional reference between a and b
//      living_Person(ben).Knows.weight(7).Person.name("Ida").update
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//    }
//
//
//    "creating refs to multiple new" in new BidirectionalSetup {
//
//      val ben = living_Person.name.insert("Ben").eid
//
//
//      // Can't update multiple values of cardinality-one attribute `name`
//      (Person.name("Ben").Knows.weight(7).Person.name("Ida", "Liz").update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//        s"[output.Molecule:noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
//        "  living_Person ... name(Ida, Liz)"
//
//      // Can't update nested data structures
//      (Person.name("Ben").Knows.weight(7).Person.*(Person.name("Ida")).update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//        s"[output.Molecule.noNested] Nested data structures not allowed in update molecules"
//
//      // So, we can't create multiple referenced entities with the `update` command.
//      // Update ref attribute with already created entity ids instead (see below).
//    }
//
//
//    "replacing ref to 1 new" in new BidirectionalSetup {
//
//      val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//      // Bidirectional references created
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//
//      // Update Ben with creation of Liz and bidirectional references to/from her.
//      // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//      // and will not cause his friendship to Ida to be retracted as it would be with a
//      // cardinality one reference!
//      living_Person(ben).Knows.weight(7).Person.name("Liz").update
//
//      // Bidirectional references to/from Liz has been added.
//      // OBS: see note above and difference to cardinality-one updates!
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Ida", "Ben"),
//        ("Liz", "Ben")
//      )
//
//      // To remove the friendship to Ida we need to remove it manually
//      living_Person(ben).Knows.weight(7).Person.remove(ida).update
//
//      // Now the friendship with Ida has been replaced with the friendship with Liz
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Liz"),
//        ("Liz", "Ben")
//      )
//    }
//  }
//
//
//  "replacing refs to multiple new" in new BidirectionalSetup {
//
//    val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//    // Bidirectional references created
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ida", "Ben")
//    )
//
//    // Update Ben with creation of Liz and bidirectional references to/from her
//    // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//    // and will not cause his friendship to Ida to be retracted!
//    living_Person(ben).Knows.weight(7).Person.name("Liz").update
//
//    // Bidirectional references to/from Liz has been added.
//    // OBS: see note above and difference to cardinality-one updates!
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ben", "Liz"),
//      ("Ida", "Ben"),
//      ("Liz", "Ben")
//    )
//
//    // Can't update multiple values of cardinality-one attribute `name`
//    (Person(ben).Knows.weight(7).Person.name("Ida", "Liz").update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//      s"[output.Molecule:noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
//      "  living_Person ... name(Ida, Liz)"
//
//    // Can't update nested data structures
//    (Person(ben).Knows.weight(7).Person.*(Person.name("Ida")).update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//      s"[output.Molecule.noNested] Nested data structures not allowed in update molecules"
//
//    // So, we can't create multiple referenced entities with the `update` command.
//    // Update ref attribute with existing created entity ids instead (see below).
//  }
//
//
//  "Update existing" >> {
//
//    "creating ref to 1 existing" in new BidirectionalSetup {
//
//      val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids
//
//      // Update a with creation of bidirectional reference to existing b
//      living_Person(ben).Knows.weight(7).Person(ida).update
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//    }
//
//    "creating ref to 1 existing with explicit `add`" in new BidirectionalSetup {
//
//      val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids
//
//      // Update a with creation of bidirectional reference to existing b
//      living_Person(ben).Knows.weight(7).Person.add(ida).update
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//    }
//
//
//    "creating refs to multiple existing, vararg" in new BidirectionalSetup {
//
//      val List(ben, ida, liz) = living_Person.name.insert("Ben", "Ida", "Liz").eids
//
//      // Update Ben with two new friendships
//      living_Person(ben).Knows.weight(7).Person(ida, liz).update
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Ida", "Ben"),
//        ("Liz", "Ben")
//      )
//    }
//
//
//    "creating refs to multiple existing, Set" in new BidirectionalSetup {
//
//      val List(ben, ida, liz) = living_Person.name.insert("Ben", "Ida", "Liz").eids
//
//      // Update Ben with two new friendships supplied as a Set
//      living_Person(ben).Knows.weight(7).Person(Set(ida, liz)).update
//
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Ida", "Ben"),
//        ("Liz", "Ben")
//      )
//    }
//  }
//
//
//  "Update replacing ref to other existing" in new BidirectionalSetup {
//
//    // Save Ben, Ida and bidirectional references between them
//    val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//    // Bidirectional references created
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ida", "Ben")
//    )
//
//    // Update Ben by adding Liz and bidirectional references to/from her
//    // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//    // and will not cause his friendship to Ida to be retracted!
//    val liz = living_Person.name.insert("Liz").eid
//    living_Person(ben).Knows.weight(7).Person(liz).update
//
//    // Bidirectional references to/from Liz has been added.
//    // OBS: see note above and difference to cardinality-one updates!
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ben", "Liz"),
//      ("Ida", "Ben"),
//      ("Liz", "Ben")
//    )
//
//    // To remove the friendship to Ida we need to remove it manually as we did above
//    living_Person(ben).Knows.weight(7).Person.remove(ida).update
//
//    // Now the friendship with Ida has been replaced with the friendship with Liz
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Liz"),
//      ("Liz", "Ben")
//    )
//  }
//
//
//  "Update removing all references" >> {
//
//    "retracting ref" in new BidirectionalSetup {
//
//      // Save Ben, Ida and bidirectional references between them
//      val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//      // Bidirectional references created
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//
//      // Retract ref between them by applying no value
//      living_Person(ben).Knows.weight(7).Person().update
//
//      // Bidirectional references retracted - Ben has no friends anymore
//      living_Person.name.Knows.weight.Person.name.get.sorted === List()
//    }
//  }
//
//
//  "Retract" in new BidirectionalSetup {
//
//    // (Same as for cardinality-one)
//
//    val ben = living_Person.name.insert("Ben").eid
//
//    // Create and reference b to a
//    val ida = living_Person(ben).Knows.weight(7).Person.name("Ida").update.eid
//
//    living_Person(ben).Knows.weight(7).Person.name.get === List("Ida")
//    living_Person(ida).Knows.weight(7).Person.name.get === List("Ben")
//
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ida", "Ben")
//    )
//
//    // Retract a and all references from/to a
//    ben.retract
//
//    // Woa remains and both references retracted
//    living_Person.name.get === List("Ida")
//    living_Person(ben).Knows.weight(7).Person.name.get === List()
//    living_Person(ida).Knows.weight(7).Person.name.get === List()
//    living_Person.name.Knows.weight.Person.name.get.sorted === List()
//  }
}
