//package molecule.examples.gremlin.schema
//import molecule.dsl.schemaDefinition._
//
//// http://blog.datomic.com/2013/05/a-whirlwind-tour-of-datomic-query_16.html
//
//@InOut(0, 6)
//trait TinkerGraphDefinition {
//  trait Person {
//    val name    = oneString
//    val age     = oneInt
//    val knows   = many[Person].property("weight" -> oneFloat)
//    val created = many[Project].property("weight" -> oneFloat)
//  }
//  trait Project {
//    val name = oneString
//    val lang = oneString
//  }
//}
//
//trait GraphOfGodsDefinition1 {
//  trait Being {
//    val name    = oneString
//    val age     = oneInt
//    val `type`  = oneEnum('titan, 'location, 'demigod, 'god, 'human, 'monster)
//    val father  = one[Being]
//    val mother  = one[Being]
//    val brother = many[Being]
//    val pet     = many[Being]
//  }
//  trait Battled extends PropertyEdge[Being, Being] {
//    val time  = oneInt
//    val place = oneString
//  }
//  trait Lives extends PropertyEdge[Being, Location] {
//    val reason = oneString
//  }
//  trait Location {
//    val name = oneString
//  }
//}
//
////trait Knows extends PropertyEdge[Person, Person] {
////  val weight = oneInt
////}
////trait Created extends PropertyEdge[Person, Project] {
////  val weight = oneInt
////}
//
//@InOut(0, 6)
//trait GraphOfGodsDefinition {
//  trait Being {
//    val name    = oneString
//    val age     = oneInt
//    val `type`  = oneEnum('titan, 'location, 'demigod, 'god, 'human, 'monster)
//    val father  = one[Being]
//    val mother  = one[Being]
//    val brother = many[Being]
//    val pet     = many[Being]
//    val battled = many[Being].property("time" -> oneInt).property("place" -> oneString)
//    val lives = one[Location].property("reason" -> oneString)
//  }
//  trait Location {
//    val name = oneString
//  }
//}
//
//
//trait GraphOfGodsDefinition2 {
//  trait Being {
//    val name    = oneString
//    val age     = oneInt
//    val `type`  = oneEnum('titan, 'location, 'demigod, 'god, 'human, 'monster)
//    val father  = one[Being]
//    val mother  = one[Being]
//    val brother = many[Being]
//    val pet     = many[Being]
//  }
//  trait Battled extends PropertyEdge[Being, Being] {
//    val time  = oneInt
//    val place = oneString
//  }
//  trait Lives extends PropertyEdge[Being, Location] {
//    val reason = oneString
//  }
//  trait Location {
//    val name = oneString
//  }
//}
//
//
