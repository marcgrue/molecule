package molecule.coretests.transaction

import molecule.core.ops.exception.VerifyModelException
import molecule.core.util.{DatomicPeer, DatomicPeerServer}
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.datomic.api.out10._

class TxMetaData extends CoreSpec {

  // See molecule.examples.dayOfDatomic.Provenance for more examples

  "Save" >> {

    "Basic" in new CoreSetup {

      // `tx` being tacit or mandatory has same effect
      // tx meta attributes can be in any mode
      val txR = Ns.int(1).Tx(Ns.str_("str tacit")).save
      val tx1 = txR.tx
      val t1  = txR.t
      //      val tx1 = Ns.int(1).Tx(Ns.str_("str tacit")).save.tx
      val tx2 = Ns.int(2).Tx(Ns.str("str mandatory")).save.tx
      val tx3 = Ns.int(3).Tx(Ns.str("attr mandatory")).save.tx
      val tx4 = Ns.int(4).Tx(Ns.str_("attr tacit")).save.tx
      val tx5 = Ns.int(5).Tx(Ns.str$(Some("attr optional with value"))).save.tx
      val tx6 = Ns.int(6).Tx(Ns.str$(None)).save.tx // attr optional without value

      // Optional tx meta data
      Ns.int.Tx(Ns.str$).get.sortBy(_._1) === List(
        (1, Some("str tacit")),
        (2, Some("str mandatory")),
        (3, Some("attr mandatory")),
        (4, Some("attr tacit")),
        (5, Some("attr optional with value")),
        (6, None) // attr optional without value
      )


      // Mandatory tx meta data
      Ns.int.Tx(Ns.str).get.sortBy(_._1) === List(
        (1, "str tacit"),
        (2, "str mandatory"),
        (3, "attr mandatory"),
        (4, "attr tacit"),
        (5, "attr optional with value")
      )

      // Transactions without tx meta data
      Ns.int.Tx(Ns.str_(Nil)).get === List(6)

      // Transaction meta data expressions
      Ns.int.<(3).Tx(Ns.str("str mandatory")).get === List(
        (2, "str mandatory")
      )
      Ns.int.<(3).Tx(Ns.str.not("attr mandatory")).get === List(
        (1, "str tacit"),
        (2, "str mandatory")
      )

      // Fulltext search only available for Peer
      if (system == DatomicPeer) {
        Ns.int.Tx(Ns.str.contains("mandatory")).get.sortBy(_._1) === List(
          (2, "str mandatory"),
          (3, "attr mandatory")
        )
        Ns.int.<(3).Tx(Ns.str.contains("mandatory")).get === List(
          (2, "str mandatory")
        )
      }

      // tx entity id can be returned too
      Ns.int.tx.Tx(Ns.str$).get.sortBy(_._1) === List(
        (1, tx1, Some("str tacit")),
        (2, tx2, Some("str mandatory")),
        (3, tx3, Some("attr mandatory")),
        (4, tx4, Some("attr tacit")),
        (5, tx5, Some("attr optional with value")),
        (6, tx6, None) // attr optional without value
      )
    }


    "Multiple tx attributes with mixed modes" in new CoreSetup {

      // Modes mixed in different transactions
      Ns.int(1).Tx(Ns.str("a").long_(10L).bool$(Some(false))).save
      Ns.int(2).Tx(Ns.str_("b").long$(None).bool_(true)).save

      // Expected tx meta data
      Ns.int.Tx(Ns.str.long$.bool$).get.sortBy(_._1) === List(
        (1, "a", Some(10L), Some(false)),
        (2, "b", None, Some(true))
      )
    }


    "Tx refs" in new CoreSetup {

      // Saving tx meta data (Ns.str) that references another namespace attribute (Ref1.int1)
      Ns.int(1).Tx(Ns.str("a").Ref1.int1(10)).save

      // Tx meta data with ref attr
      Ns.int.Tx(Ns.str.Ref1.int1).get === List(
        (1, "a", 10)
      )

      // Tx meta data
      Ns.int.Tx(Ns.str).get === List(
        (1, "a")
      )

      // OBS: Ref1.int1 is not asserted with tx entity, but with ref from tx entity!
      Ns.int.Tx(Ref1.int1).get === Nil

      // Saving multiple tx refs
      Ns.int(2).Tx(Ns.str("b").Ref1.int1(20).Ref2.int2(200)).save

      // Getting multiple tx refs
      Ns.int.Tx(Ns.str.Ref1.int1.Ref2.int2).get === List(
        (2, "b", 20, 200)
      )
      Ns.int.Tx(Ns.str.Ref1.int1).get.sorted === List(
        (1, "a", 10), // First insert matches too
        (2, "b", 20)
      )
      Ns.int.Tx(Ns.str).get.sorted === List(
        (1, "a"),
        (2, "b")
      )
    }


    "Multiple tx groups" in new CoreSetup {

      // Tx meta data that is not connected by references can be saved by applying
      // individual tx molecules to separate tx's
      Ns.int(1).Tx(Ns.str("a")).Tx(Ref1.int1(10)).save

      // This is not the same as a composite since the composite would be its own entity
      // and not the tx entity. (won't compile either)
      // Ns.int.Tx(Ns.str + Ref1.int1).get

      // Ns.str has no own reference to Ref1.int1
      Ns.int.Tx(Ns.str.Ref1.int1).get === Nil

      // Tx entity has Ns.str asserted
      Ns.int.Tx(Ns.str).get === List(
        (1, "a")
      )

      // Tx entity also has Ref1.int1 asserted
      Ns.int.Tx(Ref1.int1).get === List(
        (1, 10)
      )

      // Tx entity has both Ns.str and Ref1.int1 asserted
      Ns.int.Tx(Ns.str).Tx(Ref1.int1).get === List(
        (1, "a", 10)
      )
    }
  }


  "Insert" >> {

    "Insert 1 or more" in new CoreSetup {

      // Can't add transaction meta data along other data of molecule
      (Ns.int.Tx(Ns.str).insert(0, "a") must throwA[VerifyModelException]).message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        s"[onlyTacitTxAttrs]  For inserts, tx meta data can only be applied to tacit attributes, like: `Ns.str_(<metadata>)`"

      // Can't both apply meta data to tx attribute and insert meta data
      (Ns.int.Tx(Ns.str("a")).insert(List((0, "b"))) must throwA[VerifyModelException]).message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        s"[onlyTacitTxAttrs]  For inserts, tx meta data can only be applied to tacit attributes, like: `Ns.str_(<metadata>)`"

      // Can't both apply meta data to optional tx attribute and insert meta data
      (Ns.int.Tx(Ns.str$(Some("a"))).insert(List((0, Some("b")))) must throwA[VerifyModelException]).message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        s"[onlyTacitTxAttrs]  For inserts, tx meta data can only be applied to tacit attributes, like: `Ns.str_(<metadata>)`"

      // Apply tx meta data to tacit tx attributes:
      Ns.int.Tx(Ns.str_("a")).insert(0)

      // Data without tx meta data
      Ns.int.insert(1)

      // Base data having tx meta data
      Ns.int.Tx(Ns.str_).get === List(0)

      // All base data
      Ns.int.get === List(1, 0)

      // Transaction molecules don't have to be related to the base molecule
      Ns.int.Tx(Ref1.str1_("b")).insert(2)
      Ns.int.Tx(Ref2.str2_("c")).insert(3)

      Ns.int.Tx(Ref1.str1_).get === List(2)
      Ns.int.Tx(Ref2.str2_).get === List(3)

      // Base data with tx meta data
      Ns.int.Tx(Ns.str).get === List((0, "a"))
      Ns.int.Tx(Ref1.str1).get === List((2, "b"))
      Ns.int.Tx(Ref2.str2).get === List((3, "c"))
    }


    "Large tx meta data" in new CoreSetup {

      Ns.str.Tx(Ns
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
      Ns.str.Tx(Ns.int_(int1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.long_(long1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.float_(float1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.double_(double1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.bool_(bool1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.date_(date1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.uuid_(uuid1)).get === List("With tx meta data")

      // All tx meta data present
      Ns.str.Tx(Ns
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
  }

  "Composite with multiple tx meta data molecules" in new CoreSetup {

    m(Ns.str + Ns.int
      .Tx(
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
          .floats_(Set(8f, 9f)))
      .Tx(
        Ns
          .long_(7L)
          .longs_(Set(8L, 9L))
          .ref1_(701L)
          .refSub1_(702L)
          .uuid_(uuid7)
          .uuids_(Set(uuid8))
      )
    ) insert Seq(
      ("with tx meta data", 1)
    )

    Ns.str.int.insert("without tx meta data", 2)

    // Since both attributes are from the same namespace
    // the two following queries will return both entities
    m(Ns.str.int).get.sorted === List(
      ("with tx meta data", 1),
      ("without tx meta data", 2)
    )
    m(Ns.str + Ns.int).get.sorted === List(
      ("with tx meta data", 1),
      ("without tx meta data", 2)
    )

    // Find by some meta data
    m(Ns.str.int.Tx(Ns.float_(7f))).get.sorted === List(("with tx meta data", 1))
    m(Ns.str + Ns.int.Tx(Ns.float_(7f))).get.sorted === List(("with tx meta data", 1))

    // Find by other meta data
    m(Ns.str.int.Tx(Ns.long_(7L))).get.sorted === List(("with tx meta data", 1))
    m(Ns.str + Ns.int.Tx(Ns.long_(7L))).get.sorted === List(("with tx meta data", 1))

    // Find by two meta values
    m(Ns.str.int.Tx(Ns.float_(7f).long_(7L))).get === List(("with tx meta data", 1))
    m(Ns.str + Ns.int.Tx(Ns.float_(7f).long_(7L))).get === List(("with tx meta data", 1))

    // Entities _without_ meta data
    m(Ns.str.int.Tx(Ns.long_(Nil))).get.sorted === List(("without tx meta data", 2))
    m(Ns.str + Ns.int.Tx(Ns.long_(Nil))).get.sorted === List(("without tx meta data", 2))
  }


  "Update" in new CoreSetup {

    // tx 1: save
    val txR1 = Ns.int(1).Tx(Ns.str("a")).save
    val e    = txR1.eid
    val tx1  = txR1.tx

    // tx2: Update without tx meta data
    val tx2 = Ns(e).int(2).update.tx

    // tx3: Update with tx meta data
    val tx3 = Ns(e).int(3).Tx(Ns.str("b")).update.tx


    // History without tx meta data
    Ns(e).int.tx.op.getHistory.sortBy(r => (r._2, r._3)) === List(
      // tx 1
      (1, tx1, true), // 1 asserted (save)

      // tx 2
      (1, tx2, false), // 1 retracted
      (2, tx2, true), // 2 asserted (update)

      // tx 3
      (2, tx3, false), // 2 retracted
      (3, tx3, true) // 3 asserted (update)
    )

    // History with tx meta data
    Ns(e).int.tx.op.Tx(Ns.str).getHistory.sortBy(r => (r._2, r._3)) === List(
      // tx 1
      (1, tx1, true, "a"), // 1 asserted (save)

      // (tx2 has no tx meta data)

      // tx 3
      (2, tx3, false, "b"), // 2 retracted
      (3, tx3, true, "b") // 3 asserted (update)
    )
  }


  "1 entity" in new CoreSetup {

    val e = Ns.int(1).save.eid

    // Retract entity with tx meta data
    val tx2 = e.Tx(Ns.str("meta")).retract.tx

    // What was retracted and with what tx meta data
    if (system == DatomicPeerServer) {
      Ns.e.int.tx.op.Tx(Ns.str).getHistory.filter(_._3 >= basisTx) === List(
        // 1 was retracted with tx meta data "meta"
        (e, 1, tx2, false, "meta")
      )
    } else {
      Ns.e.int.tx.op.Tx(Ns.str).getHistory === List(
        // 1 was retracted with tx meta data "meta"
        (e, 1, tx2, false, "meta")
      )
    }
  }


  "Multiple entities without tx meta data" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

    Ns.int.get === List(1, 2, 3)

    // Retract multiple entities (without tx meta data)
    retract(Seq(e1, e2))

    Ns.int.get === List(3)
  }


  "Multiple entities" in new CoreSetup {

    // Insert multiple entities with tx meta data
    val txR1                  = Ns.int.Tx(Ns.str_("a")) insert List(1, 2, 3)
    val List(e1, e2, e3, tx1) = txR1.eids

    // Retract multiple entities with tx meta data
    val tx2 = retract(Seq(e1, e2), Ns.str("b")).tx

    if (system == DatomicPeerServer) {
      val (t1, t2) = (conn.getT(tx1), conn.getT(tx2))

      // History with transaction data
      Ns.int.tx.t.op.Tx(Ns.str).getHistory
        .filter(_._2 >= basisTx)
        .sortBy(r => (r._2, r._1, r._4)) === List(
        (1, tx1, t1, true, "a"),
        (2, tx1, t1, true, "a"),
        (3, tx1, t1, true, "a"),

        // 1 and 2 were retracted with tx meta data "b"
        (1, tx2, t2, false, "b"),
        (2, tx2, t2, false, "b")
      )

      // Entities and int values that were retracted with tx meta data "b"
      Ns.e.int.tx.op(false).Tx(Ns.str("b")).getHistory.filter(_._3 >= basisTx).sortBy(_._2) === List(
        (e1, 1, tx2, false, "b"),
        (e2, 2, tx2, false, "b")
      )

      // Or: What int values were retracted with tx meta data "b"?
      Ns.int.tx.op_(false).Tx(Ns.str_("b")).getHistory.filter(_._2 >= basisTx).sortBy(_._1) === List(
        (1, tx2),
        (2, tx2)
      )
    } else {
      // History with transaction data
      Ns.int.tx.op.Tx(Ns.str).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
        (1, tx1, true, "a"),
        (2, tx1, true, "a"),
        (3, tx1, true, "a"),

        // 1 and 2 were retracted with tx meta data "b"
        (1, tx2, false, "b"),
        (2, tx2, false, "b")
      )

      // Entities and int values that were retracted with tx meta data "b"
      Ns.e.int.op(false).Tx(Ns.str("b")).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
        (e1, 1, false, "b"),
        (e2, 2, false, "b")
      )

      // Or: What int values were retracted with tx meta data "b"?
      Ns.int.op_(false).Tx(Ns.str_("b")).getHistory === List(1, 2)
    }
  }


  "Multiple entities with tx meta data including ref" in new CoreSetup {

    // Insert multiple entities with tx meta data including ref
    val List(e1, e2, e3, tx1, r1) = Ns.int.Tx(Ns.str_("a").Ref1.int1_(7)) insert List(1, 2, 3) eids

    // Add tx meta data to retracting multiple entities
    val tx2 = retract(Seq(e1, e2), Ns.str("b").Ref1.int1(8)).tx

    if (system == DatomicPeerServer) {
      // History with transaction data
      Ns.int.tx.op.Tx(Ns.str.Ref1.int1).getHistory.filter(_._2 >= basisTx).sortBy(r => (r._2, r._1, r._3)) === List(
        (1, tx1, true, "a", 7),
        (2, tx1, true, "a", 7),
        (3, tx1, true, "a", 7),

        // 1 and 2 were retracted with tx meta data "b"
        (1, tx2, false, "b", 8),
        (2, tx2, false, "b", 8)
      )

      // Entities and int values that was retracted in tx "b"
      Ns.e.int.tx.op(false).Tx(Ns.str("b").Ref1.int1(8)).getHistory.filter(_._3 >= basisTx).sortBy(_._2) === List(
        (e1, 1, tx2, false, "b", 8),
        (e2, 2, tx2, false, "b", 8)
      )

      // Or: What int values where retracted in tx "b"?
      Ns.int.tx.op_(false).Tx(Ns.str_("b").Ref1.int1_(8)).getHistory.filter(_._2 >= basisTx).sortBy(_._1) === List(
        (1, tx2),
        (2, tx2)
      )

      // OBS: Note how referenced tx meta data is not asserted directly with the tx entity:
      Ns.e.int.tx.op(false).Tx(Ref1.int1(8)).getHistory.filter(_._3 >= basisTx) === Nil
      // While Ns.str is:
      Ns.e.int.tx.op(false).Tx(Ns.str("b")).getHistory.filter(_._3 >= basisTx).sortBy(_._2) === List(
        (e1, 1, tx2, false, "b"),
        (e2, 2, tx2, false, "b")
      )
    } else {

      // History with transaction data
      Ns.int.tx.op.Tx(Ns.str.Ref1.int1).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
        (1, tx1, true, "a", 7),
        (2, tx1, true, "a", 7),
        (3, tx1, true, "a", 7),

        // 1 and 2 were retracted with tx meta data "b"
        (1, tx2, false, "b", 8),
        (2, tx2, false, "b", 8)
      )

      // Entities and int values that was retracted in tx "b"
      Ns.e.int.op(false).Tx(Ns.str("b").Ref1.int1(8)).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
        (e1, 1, false, "b", 8),
        (e2, 2, false, "b", 8)
      )

      // Or: What int values where retracted in tx "b"?
      Ns.int.op_(false).Tx(Ns.str_("b").Ref1.int1_(8)).getHistory === List(1, 2)

      // OBS: Note how referenced tx meta data is not asserted directly with the tx entity:
      Ns.e.int.op(false).Tx(Ref1.int1(8)).getHistory === Nil
      // While Ns.str is:
      Ns.e.int.op(false).Tx(Ns.str("b")).getHistory.sortBy(_._2) === List(
        (e1, 1, false, "b"),
        (e2, 2, false, "b")
      )
    }
  }


  "Save tx meta ref with multiple attrs" in new CoreSetup {
    Ns.int(1).Tx(Ns.str("a").Ref1.str1("b").int1(2)).save

    Ns.int.Tx(Ns.str.Ref1.str1.int1).get === List(
      (1, "a", "b", 2)
    )
  }

  "Save multiple tx meta refs with multiple attrs" in new CoreSetup {
    Ns.int(1).Tx(Ns.str("a").Ref1.str1("b").int1(2).Ref2.str2("c").int2(3)).save

    Ns.int.Tx(Ns.str.Ref1.str1.int1.Ref2.str2.int2).get === List(
      (1, "a", "b", 2, "c", 3)
    )
  }


  "Insert tx meta ref with multiple attrs" in new CoreSetup {
    Ns.int.Tx(Ns.str_("a").Ref1.str1_("b").int1_(7)) insert List(1, 2, 3)

    Ns.int.Tx(Ns.str.Ref1.str1.int1).get.sortBy(_._1) === List(
      (1, "a", "b", 7),
      (2, "a", "b", 7),
      (3, "a", "b", 7)
    )
  }

  "Insert multiple tx meta refs with multiple attrs" in new CoreSetup {
    Ns.int.Tx(Ns.str_("a").Ref1.str1_("b").int1_(7).Ref2.str2_("c").int2_(8)) insert List(1, 2, 3)

    Ns.int.Tx(Ns.str.Ref1.str1.int1.Ref2.str2.int2).get.sortBy(_._1) === List(
      (1, "a", "b", 7, "c", 8),
      (2, "a", "b", 7, "c", 8),
      (3, "a", "b", 7, "c", 8)
    )
  }
}