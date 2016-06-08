package molecule

import java.util.Date

import molecule.util.dsl.coreTest.{Ns, _}
import molecule.util.{CoreSetup, CoreSpec}

class TransactionAnnotations extends CoreSpec {


  //  class Setup extends CoreSetup {
  //
  //    // age - name - beverage - rating
  ////    m(Ns.int.str.Refs1 * Ref1.str1.int1) insert List(
  ////      (23, "Joe", List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
  ////      (25, "Ben", List(("Coffee", 2), ("Tea", 3))),
  ////      (23, "Liz", List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))
  //  }


  //  "Insert annotations" in new CoreSetup {
  //
  //    // Transaction annotation attributes have to have a value to be saved with the transaction
  //    (m(Ns.int.tx_(Ns.str)).insert(0, "a") must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
  //      s"[Model2Transaction:stmtsModel] Missing transaction annotation value for `Ns.str`"
  //
  //    // Can't annotate values with mandatory transaction attributes - make them tacet!
  //    // Ns.int.tx_(Ns.str("a")).insert(0)
  //
  //    // Tacet annotation attributes with an applied value is how you add transaction annotations
  //    Ns.int.tx_(Ns.str_("a")).insert(0)
  //
  //    // Data without annotations
  //    Ns.int.insert(1)
  //
  //
  //    // Annotated data
  //    Ns.int.tx_(Ns.str_).get === List(0)
  //
  //    // All data
  //    Ns.int.get === List(1, 0)
  //
  //
  //    // Any molecule will do as annotation
  //    Ns.int.tx_(Ref1.str1_("b")).insert(2)
  //    Ns.int.tx_(Ref2.str2_("c")).insert(3)
  //
  //    Ns.int.tx_(Ref1.str1_).get === List(2)
  //    Ns.int.tx_(Ref2.str2_).get === List(3)
  //
  //    // Check annotation data
  //    Ns.int.tx_(Ns.str).get === List((0, "a"))
  //    Ns.int.tx_(Ref1.str1).get === List((2, "b"))
  //    Ns.int.tx_(Ref2.str2).get === List((3, "c"))
  //  }


  "Base / annottee arities" in new CoreSetup {

    //    "22 + 22+" in new CoreSetup {

    // Since we supply annotations values with tacet attributes we can allow the base molecule to have full length (arity 22).
    // We could even add more than 22 annotation values since the tacet attributes won't affect the arity of the molecule!
//    val (e1, e2, txId) = Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums.tx_(Ns
//      .str_(str1)
//      .int_(int1)
//      .long_(long1)
//      .float_(float1)
//      .double_(double1)
//      .bool_(bool1)
//      .date_(date1)
//      .uuid_(uuid1)
//      .uri_(uri1)
//      .enum_(enum1)
//      .ref1_(10L)
//      .refSub1_(20L)
//      .strs_(strs1)
//      .ints_(ints1)
//      .longs_(longs1)
//      .floats_(floats1)
//      .doubles_(doubles1)
//      .bools_(bools1)
//      .dates_(dates1)
//      .uuids_(uuids1)
//      .uris_(uris1)
//      .enums_(enums1)
//    ) insert List(
//      (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 11L, 12L, strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
//      (str2, int2, long2, float2, double2, bool2, date2, uuid2, uri2, enum2, 10L, 20L, strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2)
//    ) eids

    //    trait insert[T1, T2](m1: MoleculeOut[T1], m2: MoleculeOut[T2]) (d1: Seq[T1], d2: Seq[T1]) {
    //      def hej = 7
    //    }


    //    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1,
    //    Ns.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums

    //    (str1, int1, long1, float1, double1, date1, uuid1, uri1, enum1),
    //    (str2, int2, long2, float2, double2, date2, uuid2, uri2, enum2)

    //    import insertX._

    //      import api._
    //      import ast.model._
    //
    //      private val x = Debug("DatomicFacade", 1, 99, false, 3)


    val data1 = Seq(
      (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 11L, 12L),
      (str2, int2, long2, float2, double2, bool2, date2, uuid2, uri2, enum2, 21L, 22L)
    )
    val data2 = Seq(
      (strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
      (strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2)
    )

    val data1x = Seq(
      (str1, date1),
      (str2, date2)
    )
    val data2x = Seq(
      (strs1, ints1),
      (strs2, ints2)
    )

    //    Ns.str.int.insert(data1)

    //      inserts(Ns.str.int, Ns.strs.ints)(data1, data2).eids

    //    7 === 8
    val datex = new Date(1000L)


//    Ns.str.int.insert(str1, int1)
//    Ns.str.date.insert(str1, datex)
//    m(Ns.str.date)

//    val List(e1, e2, txId) =


    splitInsertTx(
//        Ns.str.date,
//        Ns.strs.ints
        Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1,
        Ns.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums
      )(
        data1,
        data2
      )(
        // Transaction meta data can be split up in sub-molecules too, independent of
        // the above split of molecules/data
        Ns
          .bool_(true)
          .bools_(Set(false))
          .date_(date7)
          .dates_(Set(date8, date9))
          .double_(7.0)
          .doubles_(Set(8.0, 9.0))
          .enum_(enum7)
          .enums_(Set(enum8, enum9))
          .float_(7f)
          .floats_(Set(8f, 9f))
          .int_(7)
          .ints_(Set(8, 9)),
        Ns
          .long_(7L)
          .longs_(Set(8L, 9L))
          .ref1_(701L)
          .refSub1_(702L)
          .str_("use case")
          .strs_(Set("John, Lisa"))
          .uri_(uri7)
          .uris_(Set(uri8, uri9))
          .uuid_(uuid7)
          .uuids_(Set(uuid8))

//        Ns.str_("user: Marc").int_(42)
//          Ns
//                .str_(str1)
//                .int_(int1)
//                .long_(long1)
//                .float_(float1)
//                .double_(double1)
//                .bool_(bool1)
//                .date_(date1)
//                .uuid_(uuid1)
//                .uri_(uri1)
//                .enum_(enum1)
//                .ref1_(10L)
//                .refSub1_(20L),
//          Ns
//                .strs_(strs1)
//                .ints_(ints1)
//                .longs_(longs1)
//                .floats_(floats1)
//                .doubles_(doubles1)
//                .bools_(bools1)
//                .dates_(dates1)
//                .uuids_(uuids1)
//                .uris_(uris1)
//                .enums_(enums1)
      ).eids === 7


    //      .apply(Seq((str1, int1)))


    //
    //    // We can't though retrieve all the above information in one go since to total arity well exceeds 22.
    //    Ns(megaId).tx_(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums).one ===(
    //      str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 10L, 20L, strs1, ints1, longs1, floats1, doubles1, dates1, uuids1, uris1, enums1)


    //      val id = Ns.str.tx_(Ns
    //        .str_(str1)
    //        .int_(int1)
    //        .long_(long1)
    //        .float_(float1)
    //        .double_(double1)
    //        .bool_(bool1)
    //        .date_(date1)
    //        .uuid_(uuid1)
    //        .uri_(uri1)
    //        .enum_(enum1)
    //      ).insert(
    //        str2
    //      ).eid


    //      ok
    //    }

    //  "1 + 21" in new CoreSetup {
    //    Ns.str.tx_(Ns
    //      .str_(str1)
    //      .int_(int1)
    //      .long_(long1)
    //      .float_(float1)
    //      .double_(double1)
    //      .bool_(bool1)
    //      .date_(date1)
    //      .uuid_(uuid1)
    //      .uri_(uri1)
    //      .enum_(enum1)
    //      .ref1_(10L)
    //      .strs_(strs1)
    //      .ints_(ints1)
    //      .longs_(longs1)
    //      .floats_(floats1)
    //      .doubles_(doubles1)
    //      .bools_(bools1)
    //      .dates_(dates1)
    //      .uuids_(uuids1)
    //      .uris_(uris1)
    //      .enums_(enums1)
    //    ).insert("base")
    //
    //    Ns.str.tx_(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums).one ===(
    //      str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 10L, strs1, ints1, longs1, floats1, doubles1, dates1, uuids1, uris1, enums1)
    //  }
    //
    ok
  }




  //  "Reference annotations" in new CoreSetup {ok}
  //
  //  "Nested annotations" in new CoreSetup {ok}
  //
  //  "Modifying annotations" in new CoreSetup {ok}
  //
  //  "Retracting annotations" in new CoreSetup {
  //    ok
  //  }
}


//expectCompileError(
//  """Ns.str("str1" and "str2").get""",
//  "[Dsl2Model:apply (4)] Card-one attribute `str` cannot return multiple values.\n"
//    + "A tacet attribute can though have AND expressions to make a self-join.\n"
//    + "If you want this, please make the attribute tacet by appending an underscore: `str_`")