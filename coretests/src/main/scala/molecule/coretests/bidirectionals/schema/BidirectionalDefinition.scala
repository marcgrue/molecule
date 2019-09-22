package molecule.coretests.bidirectionals.schema
import molecule.schema.definition._

@InOut(1, 9)
object BidirectionalDefinition {


  // Base namespace ......................................................

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


  // Property edges to same namespace ....................................

  // (card-one)
  object Loves extends Loves
  trait Loves {
    // a --- edge ==> a
    val person: AnyRef = target[Person.loves.type]

    // Edge properties
    val weight          = oneInt
    val howWeMet        = oneEnum("inSchool", "atWork", "throughFriend")
    val commonInterests = manyString
    val commonLicences  = manyEnum("climbing", "diving", "parachuting", "flying")
    val commonScores    = mapInt
    val coreQuality     = one[Quality]
    val inCommon        = many[Quality]
  }

  // (card-many)
  object Knows extends Knows
  trait Knows {
    // a --- edge ==> a
    val person: AnyRef = target[Person.knows.type]

    // Edge properties
    val weight          = oneInt
    val howWeMet        = oneEnum("inSchool", "atWork", "throughFriend")
    val commonInterests = manyString
    val commonLicences  = manyEnum("climbing", "diving", "parachuting", "flying")
    val commonScores    = mapInt
    val coreQuality     = one[Quality]
    val inCommon        = many[Quality]
  }


  // Property edges to other namespace ..................................

  // (card-one)
  object Favorite extends Favorite
  trait Favorite {
    // a <== edge --- b
    val person: AnyRef = target[Person.favorite.type]
    // a --- edge ==> b
    val animal: AnyRef = target[Animal.favorite.type]

    // Edge properties
    val weight          = oneInt
    val howWeMet        = oneEnum("inSchool", "atWork", "throughFriend")
    val commonInterests = manyString
    val commonLicences  = manyEnum("climbing", "diving", "parachuting", "flying")
    val commonScores    = mapInt
    val coreQuality     = one[Quality]
    val inCommon        = many[Quality]
  }

  // (card-many)
  object CloseTo extends CloseTo
  trait CloseTo {
    // a <== edge --- b
    val person: AnyRef = target[Person.closeTo.type]
    // a --- edge ==> b
    val animal: AnyRef = target[Animal.closeTo.type]

    // Edge properties
    val weight          = oneInt
    val howWeMet        = oneEnum("inSchool", "atWork", "throughFriend")
    val commonInterests = manyString
    val commonLicences  = manyEnum("climbing", "diving", "parachuting", "flying")
    val commonScores    = mapInt
    val coreQuality     = one[Quality]
    val inCommon        = many[Quality]
  }

  // Sample ns to demonstrate edge ref property
  trait Quality {
    val name = oneString
  }


  // Other connected namespace .......................................

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