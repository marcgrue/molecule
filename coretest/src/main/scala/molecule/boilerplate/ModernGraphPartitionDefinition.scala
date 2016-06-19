package molecule.boilerplate

import molecule.dsl.schemaDefinition._


@InOut(0, 5)
object ModernGraphPartitionDefinition {

  object vertex {
    // Vertex namespaces
    trait Person {
      val name    = oneString
      val age     = oneInt
      val created = many[edge.Created]

      // weighed knows (edge with property) - separate entity, ref to other namespace
      val knows     = many[edge.Knows].bidirectional

//      // friends - ref to same namespace
//      val friends = many[Person].bidirectional
//
//      // Single bidirectional, weighed
//      val knows2 = one[edge.Knows].bidirectionalAttr("person")
//
//      // Single bidirectional
//      val spouse = one[Person].bidirectional
//
//      // One-way many-ref (celebrities don't know fans)
//      val fanOf = many[Person]
//
//      // One-way one-ref to single person
//      val father = one[Person]
    }
    trait Software {
      val name = oneString
      val lang = oneString
    }
  }

  object edge {
    // Property edges
    trait Knows {
      val person = one[vertex.Person]

//      val persons = many[vertex.Person]
      val weight  = oneDouble
    }
    trait Created {
      val software = one[vertex.Software]
      val weight   = oneDouble
    }

//    trait TeamMates {
//      val players = many[vertex.Person]
//      val club    = oneString
//    }
  }
}