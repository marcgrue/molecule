package molecule.boilerplate.schema

//import molecule.dsl.schemaDefinition._
import molecule.dsl.schemaDefinition._

@InOut(0, 5)
object ModernGraphDefinition {

  // Vertex namespace
  trait Person {
    val name    = oneString
    val age     = oneInt
    val created = many[Created]

    // Bidirectional card-many ref to property edge namespace
    val knows = many[Knows].bidirectional
    // Works also
    //    val `knows` = many[Knows].bidirectional
    //    val friends = many[Knows].bidirectional
    //
    //    // Bidirectional card-one ref to property edge namespace
    //    val mentor = one[Knows].bidirectional


    // Bidirectional card-many ref (self-reference)
    //    val friends = many[Person].bidirectional

    // Bidirectional card-one ref (self-reference)
    //    val spouse = one[Person].bidirectional
    //    val `spouse` = one[Person].bidirectional
  }

  trait Software {
    val name = oneString
    val lang = oneString
  }

  // Property edge namespace
  trait Knows {

    // Mandatory card-one reverse ref pointing back to `Person`
    val person = one[Person]
    // works also
    //    val buddy = one[Person]
    //    val `person` = one[Person]
    // No reverse ref
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] Can't find reverse ref in namespace `Knows` pointing back to `Person`.
    //      [error] Expecting something like:
    //      [error]   val person = one[Person]

    // Can't define bidrectionallity in edge namespace (circular ref)
    //    val person = one[Person].bidirectional
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] Add the `bidirectional` only to the ref in the "vertex namespace" `Person`
    //      [error] that points to the edge namespace `Knows`.
    //      [error] In the edge namespace `Knows` simply omit the `bidirectional` option:
    //      [error]   val person = one[Person]


    // val person2 = one[Person]
    // Along with `person`:
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] Can only have one reverse ref in namespace `Knows`pointing back to `Person`.
    //      [error] Found 2 reverse references pointing back to `Person`:
    //      [error]   val person = one[Person]
    //      [error]   val person2 = one[Person]
    //      [error] Expected something like:
    //      [error]   val person = one[Person]


    // val person3 = many[Person]
    // Along with `person`:
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] Can only have one reverse ref in namespace `Knows`pointing back to `Person`.
    //      [error] Found 2 reverse references pointing back to `Person`:
    //      [error]   val person = one[Person]
    //      [error]   val person3 = many[Person]
    //      [error] Expected something like:
    //      [error]   val person = one[Person]

    // `person3` only:
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] Reverse ref `person3` in namespace `Knows` is expected
    //      [error] to be a cardinality one ref. Please change to:
    //      [error]   val person3 = one[Person]

    // Ref to other namespace
    // val other = one[OtherNamespace]
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] "Property edge" namespace `Knows` is only allowed to have reverse references
    //      [error] pointing back to namespace `Person` (like `val person = one[Person]`).
    //      [error] Found un-allowed reference in property edge namespace `Knows`:
    //      [error]   val other = one[OtherNamespace]

    // Edge property
    val weight = oneDouble

    // If no edge properties defined
    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
    //      [error] Edge namespaces like `Knows` should have at least 1 edge property (attribute/enum) defined.
    //      [error] If no edge properties are needed, you should skip the separate namespace `Knows` and simply
    //      [error] only have a bidirectional self-reference in `Person`, like:
    //      [error]   val knows = one[Person]
  }

  trait Created {
    val software = one[Software]
    val weight   = oneDouble
  }
}