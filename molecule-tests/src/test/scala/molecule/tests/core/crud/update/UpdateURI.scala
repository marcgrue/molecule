package molecule.tests.core.crud.update

import java.net.URI
import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out1._
import molecule.datomic.base.transform.exception.Model2TransactionException
import molecule.core.ops.exception.VerifyModelException
import molecule.setup.TestSpec

class UpdateURI extends TestSpec {

  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.uri(uri2).save.eid

      // Apply value (retracts current value)
      Ns(eid).uri(uri1).update
      Ns.uri.get.head === uri1

      // Apply new value
      Ns(eid).uri(uri2).update
      Ns.uri.get.head === uri2

      // Delete value (apply no value)
      Ns(eid).uri().update
      Ns.uri.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).uri(uri2, uri3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... uri($uri2, $uri3)"
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.uris(uri1).save.eid

      // Assert value
      Ns(eid).uris.assert(uri2).update
      Ns.uris.get.head === Set(uri1, uri2)

      // Assert existing value (no effect)
      Ns(eid).uris.assert(uri2).update
      Ns.uris.get.head === Set(uri1, uri2)

      // Assert multiple values
      Ns(eid).uris.assert(uri3, uri4).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).uris.assert(Seq(uri4, uri5)).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4, uri5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(uri6, uri7)
      Ns(eid).uris.assert(values).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4, uri5, uri6, uri7)

      // Assert empty Seq of values (no effect)
      Ns(eid).uris.assert(Seq[URI]()).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4, uri5, uri6, uri7)


      // Reset
      Ns(eid).uris().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).uris.assert(uri1, uri2, uri2).update
      Ns.uris.get.head === Set(uri1, uri2)

      // Equal values are coalesced (at runtime)
      val other3 = uri3
      Ns(eid).uris.assert(uri2, uri3, other3).update
      Ns.uris.get.head === Set(uri3, uri2, uri1)
    }


    "replace" in new CoreSetup {

      val eid = Ns.uris(uri1, uri2, uri3, uri4, uri5, uri6).save.eid

      // Replace value
      Ns(eid).uris.replace(uri6 -> uri8).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4, uri5, uri8)

      // Replace value to existing value simply retracts it
      Ns(eid).uris.replace(uri5 -> uri8).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4, uri8)

      // Replace multiple values (vararg)
      Ns(eid).uris.replace(uri3 -> uri6, uri4 -> uri7).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri6, uri7, uri8)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).uris.replace(Seq(uri2 -> uri5)).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri5, uri6, uri7, uri8)
      Ns.uris.get.head.toList.sorted === List(uri1, uri5, uri6, uri7, uri8)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(uri1 -> uri4)
      Ns(eid).uris.replace(values).update
      Ns.uris.get.head.toList.sorted === List(uri4, uri5, uri6, uri7, uri8)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).uris.replace(Seq[(URI, URI)]()).update
      Ns.uris.get.head.toList.sorted === List(uri4, uri5, uri6, uri7, uri8)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).uris.replace(uri7 -> uri8, uri8 -> uri8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/uris`:" +
          "\n__ident__uri8")

      expectCompileError(
        """Ns(eid).uris.replace(Seq(uri7 -> uri8, uri8 -> uri8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/uris`:" +
          "\n__ident__uri8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = uri8

      (Ns(eid).uris.replace(uri7 -> uri8, uri8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/uris`:" +
        "\nuri8"

      // Conflicting new values
      (Ns(eid).uris.replace(Seq(uri7 -> uri8, uri8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/uris`:" +
        "\nuri8"
    }


    "retract" in new CoreSetup {

      val eid = Ns.uris(uri1, uri2, uri3, uri4, uri5, uri6).save.eid

      // Retract value
      Ns(eid).uris.retract(uri6).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4, uri5)

      // Retracting non-existing value has no effect
      Ns(eid).uris.retract(uri7).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4, uri5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).uris.retract(uri5, uri5).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4)

      // Retract multiple values (vararg)
      Ns(eid).uris.retract(uri3, uri4).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2)

      // Retract Seq of values
      Ns(eid).uris.retract(Seq(uri2)).update
      Ns.uris.get.head.toList.sorted === List(uri1)

      // Retract Seq of values as variable
      val values = Seq(uri1)
      Ns(eid).uris.retract(values).update
      Ns.uris.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).uris(uri1).update
      Ns(eid).uris.retract(Seq[URI]()).update
      Ns.uris.get.head.toList.sorted === List(uri1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.uris(uri2, uri3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).uris(uri1).update
      Ns.uris.get.head.toList.sorted === List(uri1)

      // Apply multiple values (vararg)
      Ns(eid).uris(uri2, uri3).update
      Ns.uris.get.head.toList.sorted === List(uri2, uri3)

      // Apply Seq of values
      Ns(eid).uris(Set(uri4)).update
      Ns.uris.get.head.toList.sorted === List(uri4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).uris(Set[URI]()).update
      Ns.uris.get === List()

      // Apply Seq of values as variable
      val values = Set(uri1, uri2)
      Ns(eid).uris(values).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2)

      // Delete all (apply no values)
      Ns(eid).uris().update
      Ns.uris.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).uris(uri1, uri2, uri2).update
      Ns.uris.get.head === Set(uri1, uri2)

      // Equal values are coalesced (at runtime)
      val other3 = uri3
      Ns(eid).uris(uri2, uri3, other3).update
      Ns.uris.get.head === Set(uri2, uri3)
    }
  }
}
