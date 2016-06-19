//package molecule.examples.gremlin
//
//import molecule.dsl.schemaDefinition._
//
//
//@InOut(0, 4)
//object ModernGraph2Definition {
//
//  // Vertex namespaces
//  trait Person {
//    val name    = oneString
//    val age     = oneInt
//    val created = many[Created]
//
//    // weighed knows (edge with property) - separate entity, ref to other namespace
//    val knows     = many[Knows].bidirectional.person
//    val teamMates = many[TeamMates].bidirectional.players
//
//    // friends - ref to same namespace
//    val friends = many[Person].bidirectional
//
//    // Single bidirectional, weighed
//    val knows2 = one[Knows].bidirectional.person
//
//    // Single bidirectional
//    val spouse = one[Person].bidirectional
//
//    // One-way many-ref (celebrities don't know fans)
//    val fanOf = many[Person]
//
//    // One-way one-ref to single person
//    val father = one[Person]
//  }
//  trait Software {
//    val name = oneString
//    val lang = oneString
//  }
//
//  // Property edges
//  trait Knows {
//    val person = one[Person]
//
//    val persons = many[Person]
//    val weight  = oneDouble
//  }
//  trait Created {
//    val software = one[Software]
//    val weight   = oneDouble
//  }
//
//  trait TeamMates {
//    val players = many[Person]
//    val club    = oneString
//  }
//}