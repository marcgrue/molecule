package molecule.manipulation.update

import java.net.URI

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateURI extends CoreSpec {


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

      (Ns(eid).uri(uri2, uri3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... uri($uri2, $uri3)"
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.uris(uri1).save.eid

      // Add value
      Ns(eid).uris.add(uri2).update
      Ns.uris.get.head === Set(uri1, uri2)

      // Add exisiting value (no effect)
      Ns(eid).uris.add(uri2).update
      Ns.uris.get.head === Set(uri1, uri2)

      // Add multiple values
      Ns(eid).uris.add(uri3, uri4).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).uris.add(Seq(uri4, uri5)).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4, uri5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(uri6, uri7)
      Ns(eid).uris.add(values).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4, uri5, uri6, uri7)

      // Add empty Seq of values (no effect)
      Ns(eid).uris.add(Seq[URI]()).update
      Ns.uris.get.head === Set(uri1, uri2, uri3, uri4, uri5, uri6, uri7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).uris.add(uri5, uri5, uri6, uri6, uri7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/uris`:" +
          "\n__ident__uri6" +
          "\n__ident__uri5")

      // Seq
      expectCompileError(
        """Ns(eid).uris.add(Seq(uri5, uri5, uri6, uri6, uri7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/uris`:" +
          "\n__ident__uri6" +
          "\n__ident__uri5")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (uri5, uri6)

      // vararg
      (Ns(eid).uris.add(other5, uri5, uri6, other6, uri7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/uris`:" +
        "\nuri6" +
        "\nuri5"

      // Seq
      (Ns(eid).uris.add(Seq(other5, uri5, uri6, other6, uri7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/uris`:" +
        "\nuri6" +
        "\nuri5"
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
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/uris`:" +
          "\n__ident__uri8")

      expectCompileError(
        """Ns(eid).uris.replace(Seq(uri7 -> uri8, uri8 -> uri8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/uris`:" +
          "\n__ident__uri8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = uri8

      (Ns(eid).uris.replace(uri7 -> uri8, uri8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/uris`:" +
        "\nuri8"

      // Conflicting new values
      (Ns(eid).uris.replace(Seq(uri7 -> uri8, uri8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/uris`:" +
        "\nuri8"
    }


    "remove" in new CoreSetup {

      val eid = Ns.uris(uri1, uri2, uri3, uri4, uri5, uri6).save.eid

      // Remove value
      Ns(eid).uris.remove(uri6).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4, uri5)

      // Removing non-existing value has no effect
      Ns(eid).uris.remove(uri7).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4, uri5)

      // Removing duplicate values removes the distinc value
      Ns(eid).uris.remove(uri5, uri5).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2, uri3, uri4)

      // Remove multiple values (vararg)
      Ns(eid).uris.remove(uri3, uri4).update
      Ns.uris.get.head.toList.sorted === List(uri1, uri2)

      // Remove Seq of values
      Ns(eid).uris.remove(Seq(uri2)).update
      Ns.uris.get.head.toList.sorted === List(uri1)

      // Remove Seq of values as variable
      val values = Seq(uri1)
      Ns(eid).uris.remove(values).update
      Ns.uris.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).uris(uri1).update
      Ns(eid).uris.remove(Seq[URI]()).update
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


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).uris(uri2, uri2, uri3, uri4, uri3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/uris`:" +
          "\n__ident__uri3" +
          "\n__ident__uri2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (uri2, uri3)

      (Ns(eid).uris(uri2, other2, uri3, uri4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/uris`:" +
        "\nuri3" +
        "\nuri2"
    }
  }
}
