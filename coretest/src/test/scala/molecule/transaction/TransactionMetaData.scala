package molecule.transaction

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class TransactionMetaData extends CoreSpec {

  // See molecule.examples.dayOfDatomic.Provenance for more examples


  "Basic insert/retrieval" in new CoreSetup {

    // Can't add transaction meta data along other data of molecule
    (m(Ns.int.tx_(Ns.str)).insert(0, "a") must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
      s"[Model2Transaction:stmtsModel] Please apply transaction meta data directly to transaction attribute: `Ns.str(<metadata>)`"

    // Can't apply transaction meta data to mandatory transaction attributes
    // Ns.int.tx_(Ns.str("use case 42")).insert(0)

    // .. make transaction attributes tacet instead:
    Ns.int.tx_(Ns.str_("a")).insert(0)

    // Data without tx meta data
    Ns.int.insert(1)

    // Base data having tx meta data
    Ns.int.tx_(Ns.str_).get === List(0)

    // All base data
    Ns.int.get === List(1, 0)

    // Transaction molecules don't have to be related to the base molecule
    Ns.int.tx_(Ref1.str1_("b")).insert(2)
    Ns.int.tx_(Ref2.str2_("c")).insert(3)

    Ns.int.tx_(Ref1.str1_).get === List(2)
    Ns.int.tx_(Ref2.str2_).get === List(3)

    // Base data with tx meta data
    Ns.int.tx_(Ns.str).get === List((0, "a"))
    Ns.int.tx_(Ref1.str1).get === List((2, "b"))
    Ns.int.tx_(Ref2.str2).get === List((3, "c"))
  }


  "Large tx meta data" in new CoreSetup {

    Ns.str.tx_(Ns
      .int_(int1)
      .long_(long1)
      .float_(float1)
      .double_(double1)
      .bool_(bool1)
      .date_(date1)
      .uuid_(uuid1)
    ).insert("With tx meta data")

    // Add data without tx meta data
    Ns.str.insert("Without tx meta data")

    // Data with an without tx meta data created
    Ns.str.get === List(
      "With tx meta data",
      "Without tx meta data"
    )

    // Use transaction meta data to filter
    Ns.str.tx_(Ns.int_(int1)).get === List("With tx meta data")
    Ns.str.tx_(Ns.long_(long1)).get === List("With tx meta data")
    Ns.str.tx_(Ns.float_(float1)).get === List("With tx meta data")
    Ns.str.tx_(Ns.double_(double1)).get === List("With tx meta data")
    Ns.str.tx_(Ns.bool_(bool1)).get === List("With tx meta data")
    Ns.str.tx_(Ns.date_(date1)).get === List("With tx meta data")
    Ns.str.tx_(Ns.uuid_(uuid1)).get === List("With tx meta data")

    // All tx meta data present
    Ns.str.tx_(Ns
      .int_(int1)
      .long_(long1)
      .float_(float1)
      .double_(double1)
      .bool_(bool1)
      .date_(date1)
      .uuid_(uuid1)
    ).get === List(
      "With tx meta data"
    )
  }


  "Composite with multiple tx meta data molecules" in new CoreSetup {

    insert(
      Ns.str, Ns.int
    )(Seq(
      ("with tx meta data", 1)
    ))(
      // 2 tx meta data molecules - no limit on the number!
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
        .floats_(Set(8f, 9f)),
      Ns
        .long_(7L)
        .longs_(Set(8L, 9L))
        .ref1_(701L)
        .refSub1_(702L)
        .uuid_(uuid7)
        .uuids_(Set(uuid8))
    )

    Ns.str.int.insert("without tx meta data", 2)

    // Since both attributes are from the same namespace
    // the two following queries will return both entities
    m(Ns.str.int).get.sorted === List(
      ("with tx meta data", 1),
      ("without tx meta data", 2)
    )
    m(Ns.str ~ Ns.int).get.sorted === List(
      ("with tx meta data", 1),
      ("without tx meta data", 2)
    )

    // Find by some meta data
    m(Ns.str.int.tx_(Ns.float_(7f))).get.sorted === List(("with tx meta data", 1))
    m(Ns.str ~ Ns.int.tx_(Ns.float_(7f))).get.sorted === List(("with tx meta data", 1))

    // Find by other meta data
    m(Ns.str.int.tx_(Ns.long_(7L))).get.sorted === List(("with tx meta data", 1))
    m(Ns.str ~ Ns.int.tx_(Ns.long_(7L))).get.sorted === List(("with tx meta data", 1))

    // Find by two meta values
    m(Ns.str.int.tx_(Ns.float_(7f).long_(7L))).get === List(("with tx meta data", 1))
    m(Ns.str ~ Ns.int.tx_(Ns.float_(7f).long_(7L))).get === List(("with tx meta data", 1))

    // Entities _without_ meta data
    m(Ns.str.int.tx_(Ns.long_(nil))).get.sorted === List(("without tx meta data", 2))
    m(Ns.str ~ Ns.int.tx_(Ns.long_(nil))).get.sorted === List(("without tx meta data", 2))
  }
}