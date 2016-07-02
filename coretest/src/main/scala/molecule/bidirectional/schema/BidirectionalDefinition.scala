package molecule.bidirectional.schema

import molecule.dsl.schemaDefinition._

@InOut(0, 4)
object BidirectionalDefinition {

  object living {

    // Base namespace
    trait Person {
      val name = oneString


      // A ==> a

      val spouse  = oneBi[Person]
      val friends = manyBi[Person]


      // A ==> b

      val companion  = oneBi[Animal]
      val companions = manyBi[Animal]


      // A ==> x -- a

      //    val bestFriend = oneBi[Knows.person.type]
      val knows = manyBi[Knows.person.type]


      // A ==> x -- b

      //    val favorite = oneBi[Knows.animal.type]
      //    val closeTo  = manyBi[Knows.animal.type]


      //    // Multiple bidirectional references (with no extra properties)
      //    val friends2 = many[Person]
      //
      //    val spouse2        = oneBiSame
      //    val spouse3        = oneBiSame[Animal]
      //    val closestContact = oneBiOther[Animal]
      //
      //    // Single bidirectional outgoing reference - adding properties to the relationship
      //    val bestFriend = oneBiVia[Knows.person.type]
      //
      //    // Multiple bidirectional outgoing references - adding properties to the relationships
      //    // (re-using the reverse ref is ok ??)
      //    val knows2 = many[Knows]
      //
      //    val inContactWith = manyBi[Knows.animal.type]


      // self-ref ............................................................................

      // Correct
      //      val one = oneBi[Person]
      //      val `val` = oneBi[Person]

      //      val many = manyBi[Person]
      //      val `type` = manyBi[Person]

      //      val noGood = oneBi[BidirectionalPartitionDefinition.partitionA.Person.noGood.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and don't need to have the attribute name specified. This is enough:
      //        [error]   val noGood = oneBi[Person]

      //      val noGood = oneBi[partitionA.Person.noGood.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and doesn't need to have the attribute name specified. This is enough:
      //        [error]   val noGood = oneBi[Person]

      //      val noGood = oneBi[Person.noGood.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and doesn't need to have the attribute name specified. This is enough:
      //        [error]   val noGood = oneBi[Person]

      //      val noGood = oneBi[noGood.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `partitionA_Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and only needs the current namespace as type argument:
      //        [error]   val noGood = oneBi[Person]


      // Bidirectional ref to other ns .....................................................................

      //      val outRefMany = manyBi[Knows.revRef2.type]

      //      val noGood = oneBi[BidirectionalPartitionDefinition.partitionA.Knows.revRefAttr.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `partitionA_Person`
      //                of BidirectionalPartitionDefinition.scala should have only the namespace prefix in the type argument:
      //        [error]   val noGood: AnyRef = oneBi[Knows.revRefAttr.type]


      //            val noGood = oneBi[partitionA.Knows.revRefAttr.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and doesn't need to have the attribute name specified. This is enough:
      //        [error]   val noGood = oneBi[Person]

      //      val noGood = oneBi[Knows.revRefAttr.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and doesn't need to have the attribute name specified. This is enough:
      //        [error]   val noGood = oneBi[Person]

      //      val noGood = oneBi[noGood.type]
      //        [error] (moleculeCoretest/compile:managedSources) Bidirectional reference `noGood` in `partitionA_Person`
      //                of BidirectionalPartitionDefinition.scala is a self-reference and only needs the current namespace as type argument:
      //        [error]   val noGood = oneBi[Person]

      // ref to other ns in other partition ..................................................


      // No type arg


      //        [error] (moleculeCoretest/compile:managedSources) Please add reverse ref attr type to bidirectional ref definition `noGood` in `partitionA_Person`
      //                of BidirectionalPartitionDefinition.scala. Something like:
      //        [error]   val noGood = oneBi[partitionA_Person]                             // for self-reference, or:
      //        [error]   val noGood = oneBi[<refNamespace>.<revRefAttr>.type] // for reverse ref attr in referenced namespace


      // Random type arg - any type arg that is not defined should be rejected

      //      val noGood = oneBi[String]
    }


    // "Property edge"

    trait Knows {
      val weight         = oneInt
      val friendshipType = one[FriendshipType]

      // a -- X ==> a
      val person = target[Person]

      // a -- X ==> b
      //    val animal = target[Animal]
    }
    object Knows extends Knows

    // Other namespace

    trait Animal {
      val name = oneString

      // a <== B

      val companion  = oneBi[Person]
      val companions = manyBi[Person]


      // a -- x <== B

      //    val favorite = oneBi[Knows.person.type]
      //    val closeTo  = manyBi[Knows.person.type]
    }


    trait FriendshipType {
      val name = oneString
    }
    object FriendshipType extends FriendshipType
  }
}