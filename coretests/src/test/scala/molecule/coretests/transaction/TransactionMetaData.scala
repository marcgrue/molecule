package molecule.coretests.transaction

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class TransactionMetaData extends CoreSpec {

  // See molecule.examples.dayOfDatomic.Provenance for more examples

  "Basic insert/retrieval" in new CoreSetup {

    // Can't add transaction meta data along other data of molecule
    (m(Ns.int.tx_(Ns.str)).insert(0, "a") must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.transform.Model2Transaction.stmtsModel]  Please apply transaction meta data directly to transaction attribute: `Ns.str(<metadata>)`"

    // Can't apply transaction meta data to mandatory transaction attributes
    // Ns.int.tx_(Ns.str("use case 42")).insert(0)

    // .. make transaction attributes tacit instead:
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

    // Data with and without tx meta data created
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


  "Save" in new CoreSetup {

    // `tx` and meta attributes with/without underscore gives same result
    Ns.int(1).tx_(Ns.str_("a")).save
    Ns.int(2).tx_(Ns.str("b")).save
    Ns.int(3).tx(Ns.str("c")).save

    // Without the tx value
    Ns.int.tx_(Ns.str).get.sorted === List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )
  }


  "Update" in new CoreSetup {

    // tx 1: save
    val e = Ns.int(1).tx(Ns.str("a")).save.eid

    // tx2: Update without tx meta data
    Ns(e).int(2).update

    // tx3: Update with tx meta data
    Ns(e).int(3).tx(Ns.str("b")).update


    // History without tx meta data
    Ns(e).int.t.op.getHistory.toSeq.sortBy(r => (r._2, r._3)) === List(
      // tx 1
      (1, 1028, true), // 1 asserted (save)

      // tx 2
      (1, 1030, false), // 1 retracted
      (2, 1030, true), // 2 asserted (update)

      // tx 3
      (2, 1031, false), // 2 retracted
      (3, 1031, true) // 3 asserted (update)
    )

    // History with tx meta data
    Ns(e).int.t.op.tx_(Ns.str).getHistory.toSeq.sortBy(r => (r._2, r._3)) === List(
      // tx 1
      (1, 1028, true, "a"), // 1 asserted (save)

      // (tx2 has no tx meta data)

      // tx 3
      (2, 1031, false, "b"), // 2 retracted
      (3, 1031, true, "b") // 3 asserted (update)
    )
  }


  "Retract" in new CoreSetup {

    val e = Ns.int(1).tx(Ns.str("a")).save.eid

    // Retract entity with tx meta data
    e.tx(Ns.str("b")).retract

    Ns(e).int.t.op.getHistory.toSeq.sortBy(r => (r._2, r._3)) === List(
      (1, 1028, true), // 1 asserted (save)
      (1, 1030, false) // 1 retracted (delete)
    )

    Ns(e).int.t.op.tx_(Ns.str).getHistory.toSeq.sortBy(r => (r._2, r._3)) === List(
      (1, 1028, true, "a"),

      // 1 was retracted with tx meta data "b"
      (1, 1030, false, "b")
    )
  }


  "Retract multiple" in new CoreSetup {

    val List(e1, e2, e3, tx) = Ns.int.tx_(Ns.str_("a")) insert List(1, 2, 3) eids

    // Retract multiple entities with tx meta data
    retract(Seq(e1, e2), Ns.str("b"))

    Ns.int.t.op.getHistory.toSeq.sortBy(r => (r._2, r._1)) === List(
      (1, 1028, true),
      (2, 1028, true),
      (3, 1028, true),

      (1, 1032, false),
      (2, 1032, false)
    )

    Ns.int.t.op.tx_(Ns.str).getHistory.toSeq.sortBy(r => (r._2, r._1, r._3)) === List(
      (1, 1028, true, "a"),
      (2, 1028, true, "a"),
      (3, 1028, true, "a"),

      // 1 and 2 were retracted with tx meta data "b"
      (1, 1032, false, "b"),
      (2, 1032, false, "b")
    )
  }
}