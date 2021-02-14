package molecule.tests.core.input1.expression

import molecule.datomic.api.in1_out2._
import molecule.datomic.base.util.SystemPeer
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Input1StringIntro extends TestSpec {

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
    m(Ns.str.int_.<(?))(30).get === List("Ann", "Ben", "Lisa")

    // Although then it would be easier to just say
    Ns.str.int_.<(30).get === List("Ann", "Ben", "Lisa")

    // For brevity we test some more expressions in the short form
    m(Ns.str.int_.>(?))(30).get === List("John")
    m(Ns.str.int_.<=(?))(28).get === List("Ann", "Ben", "Lisa")
    m(Ns.str.int_.>=(?))(28).get === List("John", "Ben", "Lisa")
    m(Ns.str.int_.!=(?))(30).get === List("Ann", "John", "Ben", "Lisa")
    m(Ns.str.int_.!=(?))(28).get === List("Ann", "John")
    m(Ns.str.int_.not(?))(28).get === List("Ann", "John")
  }

  class OneSetup extends CoreSetup {
    Ns.int.str insert List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )
  }

  class ManySetup extends CoreSetup {
    Ns.int.strs insert List(
      (1, Set("a", "b")),
      (2, Set("b", "c")),
      (3, Set("c", "d"))
    )
  }

  "Cardinality one" >> {

    "Expressions" in new OneSetup {
      m(Ns.int_.str(?)).apply("b").get === List("b")
      m(Ns.int_.str.<(?))("b").get === List("a")
      m(Ns.int_.str.>(?))("b").get === List("c")
      m(Ns.int_.str.<=(?))("b").get === List("a", "b")
      m(Ns.int_.str.>=(?))("b").get === List("b", "c")
      m(Ns.int_.str.!=(?))("b").get === List("a", "c")
      m(Ns.int_.str.not(?))("b").get === List("a", "c")
    }

    "Tacit expressions" in new OneSetup {
      m(Ns.int.str_(?))("b").get === List(2)
      m(Ns.int.str_.<(?))("b").get === List(1)
      m(Ns.int.str_.>(?))("b").get === List(3)
      m(Ns.int.str_.<=(?))("b").get.sorted === List(1, 2)
      m(Ns.int.str_.>=(?))("b").get.sorted === List(2, 3)
      m(Ns.int.str_.!=(?))("b").get.sorted === List(1, 3)
      m(Ns.int.str_.not(?))("b").get.sorted === List(1, 3)
    }

    // Optional attributes are not supported in input molecules.

    "OR-logic" in new OneSetup {

      // `or`-separated values
      m(Ns.int.str_(?)).apply("a" or "b").get.sorted === List(1, 2)
      m(Ns.int.str_(?)).apply("a" or "b" or "c").get.sorted === List(1, 2, 3)

      // Comma-separated values
      m(Ns.int.str_(?)).apply("a", "b").get.sorted === List(1, 2)

      // Seq of values
      m(Ns.int.str_(?)).apply(Seq("a", "b")).get.sorted === List(1, 2)

      // Only distinct values are matched
      m(Ns.int.str_(?)).apply(Seq("a", "b", "b")).get.sorted === List(1, 2)

      // No input elements returns Nil
      m(Ns.int.str_(?)).apply(Nil).get.sorted === Nil


      // Values assigned to variables
      m(Ns.int.str_(?))(str1 or str2).get.sorted === List(1, 2)
      m(Ns.int.str_(?))(str1, str2).get.sorted === List(1, 2)
      m(Ns.int.str_(?))(Seq(str1, str2)).get.sorted === List(1, 2)

      val seq = Seq(str1, str2)
      m(Ns.int.str_(?))(seq).get.sorted === List(1, 2)

      // Order of input of no importance
      m(Ns.int.str_(?))("b", "a").get.sorted === List(1, 2)
    }


    "Aggregates" in new OneSetup {

      // Todo - disallow applying aggregate keywords as input ?
      ok
    }

    // Only for String attributes having fulltext option defined in schema.
    "Fulltext search" in new CoreSetup {

      // fulltext only implemented in Datomic Peer
      if (system == SystemPeer) {

        // https://groups.google.com/forum/#!searchin/datomic/fulltext$20rule|sort:date/datomic/tOm__ftT27c/uTfU_ZsnJiIJ

        Ns.str insert List("The quick fox jumps", "Ten slow monkeys")
        val inputMolecule = m(Ns.str.contains(?))

        inputMolecule(Nil).get === Nil

        inputMolecule("jumps").get === List("The quick fox jumps")

        inputMolecule("jumps", "fox").get === List("The quick fox jumps")
        inputMolecule("jumps" or "fox").get === List("The quick fox jumps")
        inputMolecule(Seq("jumps", "fox")).get === List("The quick fox jumps")

        // Obs: only whole words are matched
        inputMolecule("jump").get === Nil
      }
    }
  }


  "Cardinality many" >> {

    // See expression.equality.Apply<type> test for equality/OR/AND examples

    "Comparison" in new ManySetup {

      // If we want the full sets containing the matching value we can't use an
      // input-molecule anymore but will have to map a full result set
      Ns.int.strs.get.filter(_._2.contains("b")) === List((1, Set("a", "b")), (2, Set("b", "c")))


      // Input-molecules with tacit expression

      m(Ns.int.strs_(?))(Set("a")).get.sorted === List(1)
      m(Ns.int.strs_(?))(Set("b")).get.sorted === List(1, 2)
      m(Ns.int.strs_(?))(Set("c")).get.sorted === List(2, 3)
      m(Ns.int.strs_(?))(Set("d")).get.sorted === List(3)

      m(Ns.int.strs_.<(?))(Set("b")).get.sorted === List(1)
      m(Ns.int.strs_.>(?))(Set("b")).get.sorted === List(2, 3)
      m(Ns.int.strs_.<=(?))(Set("b")).get.sorted === List(1, 2)
      m(Ns.int.strs_.>=(?))(Set("b")).get.sorted === List(1, 2, 3)
      m(Ns.int.strs_.!=(?))(Set("b")).get.sorted === List(3)
      m(Ns.int.strs_.not(?))(Set("b")).get.sorted === List(3)
    }

    // Only for String attributes having fulltext option defined in schema.
    "Fulltext search" in new CoreSetup {

      // fulltext only implemented in Datomic Peer
      if (system == SystemPeer) {

        Ns.int.strs insert List(
          (1, Set("The quick fox jumps", "Ten slow monkeys")),
          (2, Set("lorem ipsum", "Going slow"))
        )
        val inputMolecule = m(Ns.int.strs.contains(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(Set("slow")).get === List(
          (1, Set("The quick fox jumps", "Ten slow monkeys")),
          (2, Set("lorem ipsum", "Going slow"))
        )

        inputMolecule(Set("ipsum")).get === List(
          (2, Set("lorem ipsum", "Going slow"))
        )

        // Only 1 entity has a `strs` attribute with both values
        inputMolecule(Set("fox", "slow")).get === List(
          (1, Set("The quick fox jumps", "Ten slow monkeys"))
        )

        inputMolecule(Set("fox"), Set("slow")).get === List(
          (1, Set("The quick fox jumps", "Ten slow monkeys")),
          (2, Set("lorem ipsum", "Going slow"))
        )

        inputMolecule(Set("fox") or Set("slow")).get === List(
          (1, Set("The quick fox jumps", "Ten slow monkeys")),
          (2, Set("lorem ipsum", "Going slow"))
        )

        inputMolecule(List(Set("fox"), Set("slow"))).get === List(
          (1, Set("The quick fox jumps", "Ten slow monkeys")),
          (2, Set("lorem ipsum", "Going slow"))
        )
      }
    }
  }
}