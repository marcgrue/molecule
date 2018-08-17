package molecule.coretests

//import molecule.api._
//import molecule.coretests.util.dsl.coreTest._
import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.ops.exception.VerifyModelException
import molecule.transform.exception.{Model2QueryException, Model2TransactionException}
import molecule.util.expectCompileError
import scala.collection.immutable
import molecule.ast.model._
import molecule.ast.query._
import molecule.input.exception.InputMoleculeException


//class AdHocTest extends CoreSpec {
class AdHocTest extends ApplyBase {

  sequential


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.int$ insert List(
        ("a", Some(1)),
        ("b", Some(2)),
        ("c", Some(3)),
        ("d", Some(4)),
        ("e", None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {

        val inputMolecule = m(Ns.int(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(int1)).get === List(int1)

        inputMolecule(List(int1, int1)).get === List(int1)
        inputMolecule(List(int1, int2)).get === List(int1, int2)
      }


      //      "!=" in new OneSetup {
      //
      //        val inputMolecule = m(Ns.int.not(?))
      //
      //        inputMolecule(Nil).get === List(int1, int2, int3, int4)
      //
      //        inputMolecule(List(int1)).get === List(int2, int3, int4)
      //
      //        inputMolecule(List(int1, int1)).get === List(int2, int3, int4)
      //        inputMolecule(List(int1, int2)).get === List(int3, int4)
      //      }
      //
      //
      //      ">" in new OneSetup {
      //
      //        val inputMolecule = m(Ns.int.>(?))
      //
      //        inputMolecule(Nil).get === Nil
      //
      //        inputMolecule(List(int2)).get === List(int3, int4)
      //
      //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
      //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
      //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
      //      }
      //
      //
      //      ">=" in new OneSetup {
      //
      //        val inputMolecule = m(Ns.int.>=(?))
      //
      //        inputMolecule(Nil).get === Nil
      //
      //        inputMolecule(List(int2)).get === List(int2, int3, int4)
      //
      //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
      //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
      //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
      //      }
      //
      //
      //      "<" in new OneSetup {
      //
      //        val inputMolecule = m(Ns.int.<(?))
      //
      //        inputMolecule(Nil).get === Nil
      //
      //        inputMolecule(List(int2)).get === List(int1)
      //
      //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
      //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
      //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
      //      }
      //
      //
      //      "<=" in new OneSetup {
      //
      //        val inputMolecule = m(Ns.int.<=(?))
      //
      //        inputMolecule(Nil).get === Nil
      //
      //        inputMolecule(List(int2)).get === List(int1, int2)
      //
      //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
      //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
      //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
      //      }
    }


    //    "Tacit" >> {
    //
    //      "Eq" in new OneSetup {
    //
    //        val inputMolecule = m(Ns.str.int_(?))
    //
    //        inputMolecule(Nil).get === Nil
    //
    //        inputMolecule(List(int1)).get === List(str1)
    //
    //        inputMolecule(List(int1, int1)).get === List(str1)
    //        inputMolecule(List(int1, int2)).get === List(str1, str2)
    //      }
    //
    //
    //      "!=" in new OneSetup {
    //
    //        val inputMolecule = m(Ns.str.int_.not(?))
    //
    //        inputMolecule(Nil).get === List(str1, str2, str3, str4)
    //
    //        inputMolecule(List(int1)).get === List(str2, str3, str4)
    //
    //        inputMolecule(List(int1, int1)).get === List(str2, str3, str4)
    //        inputMolecule(List(int1, int2)).get === List(str3, str4)
    //      }
    //
    //
    //      ">" in new OneSetup {
    //
    //        val inputMolecule = m(Ns.str.int_.>(?))
    //
    //        inputMolecule(Nil).get === Nil
    //
    //        inputMolecule(List(int2)).get === List(str3, str4)
    //
    //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
    //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
    //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
    //      }
    //
    //
    //      ">=" in new OneSetup {
    //
    //        val inputMolecule = m(Ns.str.int_.>=(?))
    //
    //        inputMolecule(Nil).get === Nil
    //
    //        inputMolecule(List(int2)).get === List(str2, str3, str4)
    //
    //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
    //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
    //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
    //      }
    //
    //
    //      "<" in new OneSetup {
    //
    //        val inputMolecule = m(Ns.str.int_.<(?))
    //
    //        inputMolecule(Nil).get === Nil
    //
    //        inputMolecule(List(int2)).get === List(str1)
    //
    //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
    //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
    //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
    //      }
    //
    //
    //      "<=" in new OneSetup {
    //
    //        val inputMolecule = m(Ns.str.int_.<=(?))
    //
    //        inputMolecule(Nil).get === Nil
    //
    //        inputMolecule(List(int2)).get === List(str1, str2)
    //
    //        (inputMolecule(List(int2, int3)).get must throwA[Model2QueryException])
    //          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
    //          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/int"
    //      }
    //    }
  }


//  "Card many" >> {
//
//    class ManySetup extends CoreSetup {
//
//      Ns.int.ints$ insert List(
//        (1, Some(Set(1, 2))),
//        (2, Some(Set(2, 3))),
//        (3, Some(Set(3, 4))),
//        (4, Some(Set(4, 5))),
//        (5, Some(Set(4, 5, 6))),
//        (6, None)
//      )
//    }
//
//    "Mandatory, single attr coalesce" >> {
//
//      "Eq" in new OneSetup {
//
//        val inputMolecule = m(Ns.ints(?))
//
//        inputMolecule(Nil).get === Nil
//
//
//        // AND semantics when applying 1 Set of values matching input attribute values of 1 entity
//
//        inputMolecule(List(Set[Int]())).get === Nil
//        inputMolecule(List(Set(int1))).get === List(Set(int1, int2))
//        inputMolecule(List(Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
//        inputMolecule(List(Set(int3))).get === List(Set(int2, int3, int4)) // (int2, int3) + (int3, int4)
//
//        inputMolecule(List(Set(int1, int2))).get === List(Set(int1, int2))
//        inputMolecule(List(Set(int1, int3))).get === Nil
//        inputMolecule(List(Set(int2, int3))).get === List(Set(int2, int3))
//        inputMolecule(List(Set(int4, int5))).get === List(Set(int4, int5, int6)) // (int4, int5) + (int4, int5, int6)
//
//
//        // OR semantics when applying multiple Sets - all input values are flattened
//
//        inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int1, int2))
//        inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
//        inputMolecule(List(Set(int1), Set(int3))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
//        inputMolecule(List(Set(int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
//
//        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
//        inputMolecule(List(Set(int1), Set(int2, int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
//        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
//      }
//
//
//      "!=" in new OneSetup {
//
//        val inputMolecule = m(Ns.ints.not(?)) // or m(Ns.ints.!=(?))
//
//        inputMolecule(Nil).get === Nil
//        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4, int5, int6))
//
//
//        // AND semantics when applying 1 Set of values matching input attribute values of 1 entity
//
//        inputMolecule(List(Set(int1))).get === List(Set(int2, int3, int4, int5, int6)) // (int1, int2) omitted
//        inputMolecule(List(Set(int2))).get === List(Set(int3, int4, int5, int6)) // (int1, int2), (int2, int3) omitted
//        inputMolecule(List(Set(int3))).get === List(Set(int1, int2, int4, int5, int6)) // (int2, int3), (int3, int4) omitted
//
//        inputMolecule(List(Set(int1, int2))).get === List(Set(int2, int3, int4, int5, int6)) // (int1, int2) omitted
//        inputMolecule(List(Set(int1, int3))).get === List(Set(int1, int2, int3, int4, int5, int6)) // nothing omitted
//        inputMolecule(List(Set(int2, int3))).get === List(Set(int1, int3, int4, int5, int6)) // (int2, int3) omitted
//        inputMolecule(List(Set(int2, int3, int4))).get === List(Set(int1, int2, int3, int4, int5, int6)) // nothing omitted
//        inputMolecule(List(Set(int4, int5, int6))).get === List(Set(int1, int2, int3, int4, int5)) // (int4, int5, int6) omitted
//
//
//        // OR semantics when applying multiple Sets - all input values are flattened
//
//        inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int2, int3, int4, int5, int6)) // (int1, int2) omitted
//        inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int3, int4, int5, int6)) // (int1, int2), (int2, int3) omitted
//        inputMolecule(List(Set(int1), Set(int3))).get === List(Set(int4, int5, int6)) // (int1, int2), (int2, int3), (int3, int4) omitted
//        inputMolecule(List(Set(int1), Set(int4))).get === Nil // (int1, int2), (int2, int3), (int3, int4), (int4, int5), (int4, int5, 6) omitted
//        inputMolecule(List(Set(int2), Set(int3))).get === List(Set(int1, int3, int4, int5, int6)) // (int2, int3) omitted
//
//        // (int1, int2), (int2, int3), (int3, int4) omitted
//        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(Set(int4, int5, int6))
//        inputMolecule(List(Set(int1), Set(int2, int3))).get === List(Set(int4, int5, int6))
//        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(Set(int4, int5, int6))
//
//        // (int4, int5, int6) omitted
//        inputMolecule(List(Set(int4, int5), Set(int6))).get === List(Set(int1, int2, int3, int4, int5))
//        inputMolecule(List(Set(int4), Set(int5, int6))).get === List(Set(int1, int2, int3, int4, int5))
//        inputMolecule(List(Set(int4), Set(int5), Set(int6))).get === List(Set(int1, int2, int3, int4, int5))
//      }
//
//
//      ">" in new OneSetup {
//
//        val inputMolecule = m(Ns.ints.>(?))
//
//        inputMolecule(Nil).get === Nil
//        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4))
//        inputMolecule(List(Set(int2))).get === List(Set(int3, int4))
//        (inputMolecule(List(Set(int2, int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//      }
//
//
//      ">=" in new OneSetup {
//
//        val inputMolecule = m(Ns.ints.>=(?))
//
//        inputMolecule(Nil).get === Nil
//        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4))
//        inputMolecule(List(Set(int2))).get === List(Set(int2, int3, int4))
//        (inputMolecule(List(Set(int2, int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//      }
//
//
//      "<" in new OneSetup {
//
//        val inputMolecule = m(Ns.ints.<(?))
//
//        inputMolecule(Nil).get === Nil
//        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4))
//        inputMolecule(List(Set(int2))).get === List(Set(int1))
//        (inputMolecule(List(Set(int2, int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//      }
//
//
//      "<=" in new OneSetup {
//
//        val inputMolecule = m(Ns.ints.<=(?))
//
//        inputMolecule(Nil).get === Nil
//        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4))
//        inputMolecule(List(Set(int2))).get === List(Set(int1, int2))
//        (inputMolecule(List(Set(int2, int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[Model2QueryException])
//          .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
//          "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"
//      }
//    }
//
////    "Mandatory, multiple attrs unify" in new OneSetup {
////      ok
////    }
////
////    "Tacit" in new OneSetup {
////      ok
////    }
//  }


  //  "Map" >> {
  //
  //  }
  //  "Keyed map" >> {
  //
  //  }

}