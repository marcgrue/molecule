package molecule.bidirectional.schema

import molecule.schema.definition._

@InOut(1, 9)
object BidirectionalDefinition {

  object living {

    object Person extends Person
    trait Person {
      // A ==> a
      val spouse  = oneBi[Person]
      val friends = manyBi[Person]

      // A ==> b
      val pet     = oneBi[Animal.master.type]
      val buddies = manyBi[Animal.buddies.type]

      // A ==> edge -- a
      val loves = oneBiEdge[Loves.person.type]
      val knows = manyBiEdge[Knows.person.type]

      // A ==> edge -- b
      val favorite = oneBiEdge[Favorite.animal.type]
      val closeTo  = manyBiEdge[CloseTo.animal.type]

      val name = oneString
    }

    // Property edges to same namespace ..........................

    object Loves extends Loves
    trait Loves {
      // a --- edge ==> a
      val person: AnyRef = target[Person.loves.type]
//      val person: AnyRef = target[Person.knows.type]

      // Edge properties
      val weight          = oneInt
      val howWeMet        = oneEnum('inSchool, 'atWork, 'throughFriend)
      val commonInterests = manyString
      val commonLicences  = manyEnum('climbing, 'diving, 'parachuting, 'flying)
      val commonScores    = mapInt
      val coreQuality     = one[Quality]
      val inCommon        = many[Quality]
    }

    object Knows extends Knows
    trait Knows {
      // a --- edge ==> a
      val person: AnyRef = target[Person.knows.type]

      // Edge properties
      val weight          = oneInt
      val howWeMet        = oneEnum('inSchool, 'atWork, 'throughFriend)
      val commonInterests = manyString
      val commonLicences  = manyEnum('climbing, 'diving, 'parachuting, 'flying)
      val commonScores    = mapInt
      val coreQuality     = one[Quality]
      val inCommon        = many[Quality]
    }

    // Property edges to other namespace ..........................

    object Favorite extends Favorite
    trait Favorite {
      // a --- edge ==> b
      val animal: AnyRef = target[Animal.favorite.type]
      // a <== edge --- b
//      val person: AnyRef = target[Animal.favorite.type]
      val person: AnyRef = target[Person.favorite.type]

      // Edge properties
      val weight          = oneInt
      val howWeMet        = oneEnum('inSchool, 'atWork, 'throughFriend)
      val commonInterests = manyString
      val commonLicences  = manyEnum('climbing, 'diving, 'parachuting, 'flying)
      val commonScores    = mapInt
      val coreQuality     = one[Quality]
      val inCommon        = many[Quality]
    }

    object CloseTo extends CloseTo
    trait CloseTo {
      // a --- edge ==> b
      val animal: AnyRef = target[Animal.closeTo.type]
      // a <== edge --- b
      val person: AnyRef = target[Person.closeTo.type]

      // Edge properties
      val weight          = oneInt
      val howWeMet        = oneEnum('inSchool, 'atWork, 'throughFriend)
      val commonInterests = manyString
      val commonLicences  = manyEnum('climbing, 'diving, 'parachuting, 'flying)
      val commonScores    = mapInt
      val coreQuality     = one[Quality]
      val inCommon        = many[Quality]
    }


    // Sample ns to demonstrate edge ref property
    trait Quality {
      val name = oneString
    }



    object Animal extends Animal
    trait Animal {
      // Other end/start point of other ref
      // a <== B
      val master : AnyRef = oneBi[Person.pet.type]
      val buddies: AnyRef = manyBi[Person.buddies.type]

      // Other end/start point of edges between different namespaces
      // a -- edge <== B
      val favorite = oneBiEdge[Favorite.person.type]
      val closeTo  = manyBiEdge[CloseTo.person.type]

      val name = oneString
    }
  }
}

//    // Multiple bidirectional references (with no extra properties)
//    val friends2 = many[Person]
//
//    val spouse2        = oneBiSame
//    val spouse3        = oneBiSame[Animal]
//    val closestContact = oneBiRef[Animal]
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
