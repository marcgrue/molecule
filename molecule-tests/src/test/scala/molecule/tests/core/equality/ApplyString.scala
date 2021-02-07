package molecule.tests.core.equality

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out4._
import molecule.TestSpec

class ApplyString extends TestSpec {


  "Card one" >> {

    "Special characters" in new CoreSetup {
      Ns.str insert List("", " ", ",", ".", "?", "A", "B", "a", "b")

      // Empty string can be saved
      Ns.str("").get === List("")

      // White space only can be saved
      Ns.str(" ").get === List(" ")

      // Characters and symbols
      Ns.str(",").get === List(",")
      Ns.str(".").get === List(".")
      Ns.str("?").get === List("?")
      Ns.str("A").get === List("A")
      Ns.str("B").get === List("B")
      Ns.str("a").get === List("a")
      Ns.str("b").get === List("b")
    }

    class OneSetup extends CoreSetup {
      Ns.int.str$ insert List(
        (1, Some("a")),
        (2, Some("b")),
        (3, Some("c")),
        (4, None)
      )
    }


    "Quoting" in new CoreSetup {

      Ns.int(1).str("""Hi "Ann"""").save

      Ns.str.get === List("""Hi "Ann"""")
      Ns.str("""Hi "Ann"""").get === List("""Hi "Ann"""")

      val str: String = """Hi "Ann""""
      Ns.str(str).get === List("""Hi "Ann"""")

      Ns.int.str_("""Hi "Ann"""").get === List(1)
      Ns.int.str_(str).get === List(1)

      Ns.int.str$(Some("""Hi "Ann"""")).get === List((1, Some("""Hi "Ann"""")))

      val some = Some("""Hi "Ann"""")
      Ns.int.str$(some).get === List((1, Some("""Hi "Ann"""")))
    }


    "Escaping 1" in new CoreSetup {
      val expr = """Hello "\d" expression"""
      Ns.str(expr).save
      Ns.str.get === List(expr)
      Ns.str(expr).get === List(expr)
    }

    "Escaping 2" in new CoreSetup {
      val expr = "Hello \"\\d\" expression"
      Ns.str(expr).save
      Ns.str.get === List(expr)
      Ns.str(expr).get === List(expr)
    }


    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.str.apply("a").get === List("a")
      Ns.str.apply("b").get === List("b")
      Ns.str.apply("a", "b").get === List("a", "b")

      // `or`
      Ns.str.apply("a" or "b").get === List("a", "b")
      Ns.str.apply("a" or "b" or "c").get === List("a", "b", "c")

      // Seq
      Ns.str.apply().get === Nil
      Ns.str.apply(Nil).get === Nil
      Ns.str.apply(List("a")).get === List("a")
      Ns.str.apply(List("b")).get === List("b")
      Ns.str.apply(List("a", "b")).get === List("a", "b")
      Ns.str.apply(List("a"), List("b")).get === List("a", "b")
      Ns.str.apply(List("a", "b"), List("c")).get === List("a", "b", "c")
      Ns.str.apply(List("a"), List("b", "c")).get === List("a", "b", "c")
      Ns.str.apply(List("a", "b", "c")).get === List("a", "b", "c")
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.str_.apply("a").get === List(1)
      Ns.int.str_.apply("b").get === List(2)
      Ns.int.str_.apply("a", "b").get === List(1, 2)

      // `or`
      Ns.int.str_.apply("a" or "b").get === List(1, 2)
      Ns.int.str_.apply("a" or "b" or "c").get === List(1, 2, 3)

      // Seq
      Ns.int.str_.apply().get === List(4)
      Ns.int.str_.apply(Nil).get === List(4)
      Ns.int.str_.apply(List("a")).get === List(1)
      Ns.int.str_.apply(List("b")).get === List(2)
      Ns.int.str_.apply(List("a", "b")).get === List(1, 2)
      Ns.int.str_.apply(List("a"), List("b")).get === List(1, 2)
      Ns.int.str_.apply(List("a", "b"), List("c")).get === List(1, 2, 3)
      Ns.int.str_.apply(List("a"), List("b", "c")).get === List(1, 2, 3)
      Ns.int.str_.apply(List("a", "b", "c")).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.strs$ insert List(
        (1, Some(Set("a", "b"))),
        (2, Some(Set("b", "c"))),
        (3, Some(Set("c", "d"))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.strs.apply("a").get === List((1, Set("a", "b")))
      Ns.int.strs.apply("b").get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply("a", "b").get === List((1, Set("a", "b")), (2, Set("b", "c")))

      // `or`
      Ns.int.strs.apply("a" or "b").get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply("a" or "b" or "c").get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))

      // Seq
      Ns.int.strs.apply().get === Nil
      Ns.int.strs.apply(Nil).get === Nil
      Ns.int.strs.apply(List("a")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(List("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(List("a", "b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(List("a"), List("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(List("a", "b"), List("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      Ns.int.strs.apply(List("a"), List("b", "c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      Ns.int.strs.apply(List("a", "b", "c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))


      // AND semantics

      // Set
      Ns.int.strs.apply(Set[String]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.strs.apply(Set("a")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(Set("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(Set("a", "b")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(Set("a", "c")).get === Nil
      Ns.int.strs.apply(Set("b", "c")).get === List((2, Set("b", "c")))
      Ns.int.strs.apply(Set("a", "b", "c")).get === Nil

      Ns.int.strs.apply(Set("a", "b"), Set[String]()).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(Set("a", "b"), Set("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(Set("a", "b"), Set("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      Ns.int.strs.apply(Set("a", "b"), Set("d")).get === List((1, Set("a", "b")), (3, Set("c", "d")))
      Ns.int.strs.apply(Set("a", "b"), Set("b"), Set("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))

      Ns.int.strs.apply(Set("a", "b"), Set("b", "c")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(Set("a", "b"), Set("b", "d")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(Set("a", "b"), Set("c", "d")).get === List((1, Set("a", "b")), (3, Set("c", "d")))

      // `and`
      Ns.int.strs.apply("a" and "b").get === List((1, Set("a", "b")))
      Ns.int.strs.apply("a" and "c").get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.strs.apply("a").get === List(Set("a", "b"))
      Ns.strs.apply("b").get === List(Set("a", "c", "b"))
      Ns.strs.apply("a", "b").get === List(Set("a", "c", "b"))

      // `or`
      Ns.strs.apply("a" or "b").get === List(Set("a", "c", "b"))
      Ns.strs.apply("a" or "b" or "c").get === List(Set("a", "d", "c", "b"))

      // Seq
      Ns.strs.apply().get === Nil
      Ns.strs.apply(Nil).get === Nil
      Ns.strs.apply(List("a")).get === List(Set("a", "b"))
      Ns.strs.apply(List("b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(List("a", "b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(List("a"), List("b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(List("a", "b"), List("c")).get === List(Set("a", "d", "c", "b"))
      Ns.strs.apply(List("a"), List("b", "c")).get === List(Set("a", "d", "c", "b"))
      Ns.strs.apply(List("a", "b", "c")).get === List(Set("a", "d", "c", "b"))


      // AND semantics

      // Set
      Ns.strs.apply(Set[String]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.strs.apply(Set("a")).get === List(Set("a", "b"))
      Ns.strs.apply(Set("b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(Set("a", "b")).get === List(Set("a", "b"))
      Ns.strs.apply(Set("a", "c")).get === Nil
      Ns.strs.apply(Set("b", "c")).get === List(Set("b", "c"))
      Ns.strs.apply(Set("a", "b", "c")).get === Nil

      Ns.strs.apply(Set("a", "b"), Set("b")).get === List(Set("a", "b", "c"))
      Ns.strs.apply(Set("a", "b"), Set("c")).get === List(Set("a", "b", "c", "d"))
      Ns.strs.apply(Set("a", "b"), Set("d")).get === List(Set("a", "b", "c", "d"))
      Ns.strs.apply(Set("a", "b"), Set("b"), Set("c")).get === List(Set("a", "b", "c", "d"))

      Ns.strs.apply(Set("a", "b"), Set("b", "c")).get === List(Set("a", "b", "c"))
      Ns.strs.apply(Set("a", "b"), Set("b", "d")).get === List(Set("a", "b"))
      Ns.strs.apply(Set("a", "b"), Set("c", "d")).get === List(Set("a", "b", "c", "d"))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.strs.apply("a" and "b").get === List(Set("a", "b"))
      Ns.strs.apply("a" and "c").get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.strs_.apply("a").get === List(1)
      Ns.int.strs_.apply("b").get === List(1, 2)
      Ns.int.strs_.apply("a", "b").get === List(1, 2)

      // `or`
      Ns.int.strs_.apply("a" or "b").get === List(1, 2)
      Ns.int.strs_.apply("a" or "b" or "c").get === List(1, 2, 3)

      // Seq
      Ns.int.strs_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.strs_.apply(Nil).get === List(4)
      Ns.int.strs_.apply(List("a")).get === List(1)
      Ns.int.strs_.apply(List("b")).get === List(1, 2)
      Ns.int.strs_.apply(List("a", "b")).get === List(1, 2)
      Ns.int.strs_.apply(List("a"), List("b")).get === List(1, 2)
      Ns.int.strs_.apply(List("a", "b"), List("c")).get === List(1, 2, 3)
      Ns.int.strs_.apply(List("a"), List("b", "c")).get === List(1, 2, 3)
      Ns.int.strs_.apply(List("a", "b", "c")).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.strs_.apply(Set[String]()).get === List(4)
      Ns.int.strs_.apply(Set("a")).get === List(1)
      Ns.int.strs_.apply(Set("b")).get === List(1, 2)
      Ns.int.strs_.apply(Set("a", "b")).get === List(1)
      Ns.int.strs_.apply(Set("a", "c")).get === Nil
      Ns.int.strs_.apply(Set("b", "c")).get === List(2)
      Ns.int.strs_.apply(Set("a", "b", "c")).get === Nil

      Ns.int.strs_.apply(Set("a", "b"), Set("b")).get === List(1, 2)
      Ns.int.strs_.apply(Set("a", "b"), Set("c")).get === List(1, 2, 3)
      Ns.int.strs_.apply(Set("a", "b"), Set("d")).get === List(1, 3)
      Ns.int.strs_.apply(Set("a", "b"), Set("b"), Set("c")).get === List(1, 2, 3)

      Ns.int.strs_.apply(Set("a", "b"), Set("b", "c")).get === List(1, 2)
      Ns.int.strs_.apply(Set("a", "b"), Set("b", "d")).get === List(1)
      Ns.int.strs_.apply(Set("a", "b"), Set("c", "d")).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.strs_.apply("a" and "b").get === List(1)
      Ns.int.strs_.apply("a" and "c").get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[String]()

      val l1 = List(str1)
      val l2 = List(str2)

      val s1 = Set(str1)
      val s2 = Set(str2)

      val l12 = List(str1, str2)
      val l23 = List(str2, str3)

      val s12 = Set(str1, str2)
      val s23 = Set(str2, str3)


      // OR semantics

      // Vararg
      Ns.int.strs_.apply(str1, str2).get === List(1, 2)

      // `or`
      Ns.int.strs_.apply(str1 or str2).get === List(1, 2)

      // Seq
      Ns.int.strs_.apply(seq0).get === List(4)
      Ns.int.strs_.apply(List(str1), List(str2)).get === List(1, 2)
      Ns.int.strs_.apply(l1, l2).get === List(1, 2)
      Ns.int.strs_.apply(List(str1, str2)).get === List(1, 2)
      Ns.int.strs_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.strs_.apply(set0).get === List(4)

      Ns.int.strs_.apply(Set(str1)).get === List(1)
      Ns.int.strs_.apply(s1).get === List(1)

      Ns.int.strs_.apply(Set(str2)).get === List(1, 2)
      Ns.int.strs_.apply(s2).get === List(1, 2)

      Ns.int.strs_.apply(Set(str1, str2)).get === List(1)
      Ns.int.strs_.apply(s12).get === List(1)

      Ns.int.strs_.apply(Set(str2, str3)).get === List(2)
      Ns.int.strs_.apply(s23).get === List(2)

      Ns.int.strs_.apply(Set(str1, str2), Set(str2, str3)).get === List(1, 2)
      Ns.int.strs_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.strs_.apply(str1 and str2).get === List(1)
    }


    "String interpolation" in new ManySetup {

      val world = "world"

      // Applying string-interpolated value not allowed to compile
      // Ns.str(s"hello $world").get

      ok
    }
  }
}