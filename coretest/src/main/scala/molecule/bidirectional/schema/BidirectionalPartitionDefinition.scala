package molecule.bidirectional.schema

import molecule.dsl.schemaDefinition._

@InOut(2, 4)
object BidirectionalPartitionDefinition {

  // One partition
  object living {

    trait Person {
      val name = oneString

      val mother = one[Person]

      // Single bidirectional reference (with no extra properties)
      val spouse = oneBi[Person]

      // Multiple bidirectional references (with no extra properties)
      val friends = manyBi[Person]
      val friends2 = many[Person]

      // Single bidirectional outgoing reference - adding properties to the relationship
      val bestFriend = oneBi[Knows.person.type]

      // Multiple bidirectional outgoing references - adding properties to the relationships
      // (re-using the reverse ref is ok ??)
      val knows = manyBi[Knows.person.type]
      val knows2 = many[Knows]

      // Multiple bidirectional outgoing references accross partitions
      val knows3 = manyBi[artificial.InterferesWith.robot.type]

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


    trait Knows {
      // Property of this "edge"
      val weight = oneInt

      // Reverse refs pointing back to ns of defining bidirectional attrs
      val person = rev[Person]
      val person2 = one[Person]


      val animal = rev[Animal]
    }
    object Knows extends Knows


    trait Animal {
      val name = oneString

      val knows = oneBi[Knows.animal.type]
    }

  }


  // Other partition
  object artificial {

    trait Robot {
      val name = oneString
      val interferesWith = manyBi[InterferesWith.robot.type]
    }
    object Robot extends Robot

    trait InterferesWith {
      val intensity = oneInt
      val robot = rev[Robot]
    }
    object InterferesWith extends InterferesWith
  }
}