package molecule.coretests.input

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.imports._


class Input1String extends CoreSpec {


  // Parameterized molecules have a `?` placeholder for an expected input value
  // and we call them "Input molecules".

  // Input molecules have the benefit that you can assign them to a variable
  // and then re-use them with various input values. This also allows Datomic
  // to cache and optimize the query and thereby improve runtime performance.


  "Introduction" in new CoreSetup {

    Ns.str.int insert List(("John", 37), ("Lisa", 28), ("Ben", 28), ("Ann", 14))


    // "Input-molecule" with a `?` placeholder for an expected input value
    // Often we are not interested in returning the input value so
    // we make the integer attribute tacit by adding an underscore
    val personOfAge = m(Ns.str.int_(?))

    // We can now use the input-molecule as a query template

    // Equality
    personOfAge(37).get === List("John")
    personOfAge(28).get === List("Ben", "Lisa")
    personOfAge(10).get === List()

    val personsYoungerThan = m(Ns.str.int_.<(?))

    // Apply expression value
    personsYoungerThan.apply(30).get === List("Ann", "Ben", "Lisa")
    // or just
    personsYoungerThan(30).get === List("Ann", "Ben", "Lisa")

    // We don't have to assign an input-molecule to a variable
    m(Ns.str.int_.<(?))(30).get === List("Ann", "Ben", "Lisa")
    // ...or saving a bit of typing with method `m` being implicit
    Ns.str.int_.<(?)(30).get === List("Ann", "Ben", "Lisa")

    // Although then it would be easier to just say
    Ns.str.int_.<(30).get === List("Ann", "Ben", "Lisa")

    // For brevity we test some more expressions in the short form
    Ns.str.int_.>(?)(30).get === List("John")
    Ns.str.int_.<=(?)(28).get === List("Ann", "Ben", "Lisa")
    Ns.str.int_.>=(?)(28).get === List("John", "Ben", "Lisa")
    Ns.str.int_.!=(?)(30).get === List("Ann", "John", "Ben", "Lisa")
    Ns.str.int_.!=(?)(28).get === List("Ann", "John")
    Ns.str.int_.not(?)(28).get === List("Ann", "John")
  }

  class OneSetup extends CoreSetup {
    Ns.int.str insert List((1, "a"), (2, "b"), (3, "c"))
  }

  class ManySetup extends CoreSetup {
    Ns.int.strs insert List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
  }

  "Cardinality one" >> {

    "Expressions" in new OneSetup {
      Ns.int_.str(?).apply("b").get === List("b")
      Ns.int_.str.<(?)("b").get === List("a")
      Ns.int_.str.>(?)("b").get === List("c")
      Ns.int_.str.<=(?)("b").get === List("a", "b")
      Ns.int_.str.>=(?)("b").get === List("b", "c")
      Ns.int_.str.!=(?)("b").get === List("a", "c")
      Ns.int_.str.not(?)("b").get === List("a", "c")
    }

    "Tacit expressions" in new OneSetup {
      Ns.int.str_(?)("b").get === List(2)
      Ns.int.str_.<(?)("b").get === List(1)
      Ns.int.str_.>(?)("b").get === List(3)
      Ns.int.str_.<=(?)("b").get.sorted === List(1, 2)
      Ns.int.str_.>=(?)("b").get.sorted === List(2, 3)
      Ns.int.str_.!=(?)("b").get.sorted === List(1, 3)
      Ns.int.str_.not(?)("b").get.sorted === List(1, 3)
    }
    
    // Optional attributes are not supported in input molecules.

    "OR-logic" in new OneSetup {

      // `or`-separated values
      Ns.int.str_(?).apply("a" or "b").get.sorted === List(1, 2)
      Ns.int.str_(?).apply("a" or "b" or "c").get.sorted === List(1, 2, 3)

      // Comma-separated values
      Ns.int.str_(?).apply("a", "b").get.sorted === List(1, 2)

      // Set of values
      Ns.int.str_(?).apply(Set("a", "b")).get.sorted === List(1, 2)

      // Seq of values
      Ns.int.str_(?).apply(Seq("a", "b")).get.sorted === List(1, 2)

      // Only distinct values are matched
      Ns.int.str_(?).apply(Seq("a", "b", "b")).get.sorted === List(1, 2)


      // Values assigned to variables
      Ns.int.str_(?)(str1 or str2).get.sorted === List(1, 2)
      Ns.int.str_(?)(str1, str2).get.sorted === List(1, 2)
      Ns.int.str_(?)(Set(str1, str2)).get.sorted === List(1, 2)
      Ns.int.str_(?)(Seq(str1, str2)).get.sorted === List(1, 2)

      val seq = Seq(str1, str2)
      Ns.int.str_(?)(seq).get.sorted === List(1, 2)

      val set = Set(str1, str2)
      Ns.int.str_(?)(set).get.sorted === List(1, 2)

      // Order of input of no importance
      Ns.int.str_(?)("b", "a").get.sorted === List(1, 2)
    }


    "Aggregates" in new OneSetup {

      // Todo - disallow applying aggregate keywords as input ?
      ok
    }

    // Only for String attributes having fulltext option defined in schema.
    "Fulltext search" in new CoreSetup {
      Ns.str insert List("The quick fox jumps", "Ten slow monkeys")
      Ns.str.contains(?)("jumps").get === List("The quick fox jumps")

      // Obs: only whole words are matched
      Ns.str.contains(?)("jump").get === Nil
    }
  }


  "Cardinality many" >> {


    "Cardinality-many expressions" in new ManySetup {

      // Retrieving sets of Strings containing a specific value
      //    Ns.int_.strs(?)(Set("a")).debug

      // Asking for a card-many value gives us just that value
      Ns.int.strs(?)(Set("b")).get === List((1, Set("b")), (2, Set("b")))

      // So we will more likely use the tacit notation and skip the value itself
      Ns.int.strs_(?)(Set("b")).get.sorted === List(1, 2)

      // If we want the full sets containing the matching value we can't use an
      // input-molecule anymore but will have to map a full result set
      Ns.int.strs.get.filter(_._2.contains("b")) === List((1, Set("a", "b")), (2, Set("b", "c")))


      // Input-molecules with tacit expression

      Ns.int.strs_(?)(Set("a")).get.sorted === List(1)
      Ns.int.strs_(?)(Set("b")).get.sorted === List(1, 2)
      Ns.int.strs_(?)(Set("c")).get.sorted === List(2, 3)
      Ns.int.strs_(?)(Set("d")).get.sorted === List(3)
      Ns.int.strs_.<(?)(Set("b")).get.sorted === List(1)
      Ns.int.strs_.>(?)(Set("b")).get.sorted === List(2, 3)
      Ns.int.strs_.<=(?)(Set("b")).get.sorted === List(1, 2)
      Ns.int.strs_.>=(?)(Set("b")).get.sorted === List(1, 2, 3)
      // All sets have some value not being "b"
      Ns.int.strs_.!=(?)(Set("b")).get.sorted === List(1, 2, 3)
      Ns.int.strs_.not(?)(Set("b")).get.sorted === List(1, 2, 3)
    }
  }
}