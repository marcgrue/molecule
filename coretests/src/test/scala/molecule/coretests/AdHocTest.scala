package molecule.coretests

import java.util.Date
import molecule.api.in3_out22._
import molecule.api.out10.retract
import molecule.ast.model._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.facade.TxReport
import molecule.transform.Model2Query
import scala.collection.JavaConverters._


class AdHocTest extends CoreSpec {


  //  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)

//  // First entity
//
//  val txR1 = Ns.str("a").int(1).save
//  val tx1  = txR1.tx
//  val e1   = txR1.eid
//  val t1   = txR1.t
//  val d1   = txR1.inst
//
//  val txR2 = Ns(e1).str("b").update
//  val tx2  = txR2.tx
//  val t2   = txR2.t
//  val d2   = txR2.inst
//
//  val txR3 = Ns(e1).int(2).update
//  val tx3  = txR3.tx
//  val t3   = txR3.t
//  val d3   = txR3.inst
//
//
//  // Second entity
//
//  val txR4 = Ns.str("x").int(4).save
//  val tx4  = txR4.tx
//  val e2   = txR4.eid
//  val t4   = txR4.t
//  val d4   = txR4.inst
//
//  val txR5 = Ns(e2).int(5).update
//  val tx5  = txR5.tx
//  val t5   = txR5.t
//  val d5   = txR5.inst
//
//
//  // Third entity, a ref
//
//  val txR6 = Ref1.str1("hello").save
//  val r1   = txR6.eid
//  val tx6  = txR6.tx
//  val t6   = txR6.t
//  val d6   = txR6.inst
//
//  val txR7 = Ns(e2).ref1(r1).update
//  val tx7  = txR7.tx
//  val t7   = txR7.t
//  val d7   = txR7.inst
//
//  println("e1: " + e1)
//  println("e2: " + e2)
//  println(Seq(1, tx1, t1).mkString("   "))
//  println(Seq(2, tx2, t2).mkString("   "))
//  println(Seq(3, tx3, t3).mkString("   "))
//  println(Seq(4, tx4, t4).mkString("   "))
//  println(Seq(5, tx5, t5).mkString("   "))
//  println(Seq(6, tx6, t6).mkString("   "))
//  println(Seq(7, tx7, t7).mkString("   "))


  val tx1: TxReport = Ns.int(1).save
  val tx2: TxReport = Ns.int(2).save
  val tx3: TxReport = Ns.int(3).save

  val t1: Long = tx1.t
  val t2: Long = tx2.t
  val t3: Long = tx3.t

  val d1: Date = tx1.inst
  val d2: Date = tx2.inst
  val d3: Date = tx3.inst


  //  "adhoc" in new CoreSetup {
  "adhoc" >> {


    Ns.int.getSince(d3) === List()



//
//    Ns.op(true).debugGet
//    Ns.op(true).get === List(true)


//    // Insert multiple entities with tx meta data
//    val List(e1, e2, e3, tx) = Ns.int.Tx(Ns.str_("a")) insert List(1, 2, 3) eids
//
//    // Retract multiple entities with tx meta data
//    retract(Seq(e1, e2), Ns.str("b"))
//
////    // History with transaction data
//    Ns.int.t.op.Tx(Ns.str).debugGet
////    Ns.int.t.op.Tx(Ns.str).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
////      (1, 1028, true, "a"),
////      (2, 1028, true, "a"),
////      (3, 1028, true, "a"),
////
////      // 1 and 2 were retracted with tx meta data "b"
////      (1, 1032, false, "b"),
////      (2, 1032, false, "b")
////    )
//
//    // Entities and int values that were retracted with tx meta data "b"
//    Ns.e.int.op(false).Tx(Ns.str("b")).debugGet
//
//    Ns.e.int.op(false).Tx(Ns.str("b")).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
//      (e1, 1, false, "b"),
//      (e2, 2, false, "b")
//    )
//
////    // Or: What int values were retracted with tx meta data "b"?
//    Ns.int.op_(false).Tx(Ns.str_("b")).getHistory === List(1, 2)


//    // `tx` being tacit or mandatory has same effect
//    // tx meta attributes can be in any mode
//    val tx1 = Ns.int(1).Tx(Ns.str_("str tacit")).save.tx
//    val tx2 = Ns.int(2).Tx(Ns.str("str mandatory")).save.tx
//    val tx3 = Ns.int(3).Tx(Ns.str("attr mandatory")).save.tx
//    val tx4 = Ns.int(4).Tx(Ns.str_("attr tacit")).save.tx
//    val tx5 = Ns.int(5).Tx(Ns.str$(Some("attr optional with value"))).save.tx
//    val tx6 = Ns.int(6).Tx(Ns.str$(None)).save.tx // attr optional without value
//
//
////    Ns.int.Tx(Ns.str$).debugGet
//
//    Ns.int.Tx(Ns.str$).get.sortBy(_._1) === List(
//      (1, Some("str tacit")),
//      (2, Some("str mandatory")),
//      (3, Some("attr mandatory")),
//      (4, Some("attr tacit")),
//      (5, Some("attr optional with value")),
//      (6, None) // attr optional without value
//    )
//
//    // Mandatory tx meta data
//    Ns.int.Tx(Ns.str).get.sortBy(_._1) === List(
//      (1, "str tacit"),
//      (2, "str mandatory"),
//      (3, "attr mandatory"),
//      (4, "attr tacit"),
//      (5, "attr optional with value")
//    )
//
//    // Transactions without tx meta data
//    Ns.int.Tx(Ns.str_(Nil)).get === List(6)
//
//    // Transaction meta data expressions
//    Ns.int.Tx(Ns.str.contains("mandatory")).get.sortBy(_._1) === List(
//      (2, "str mandatory"),
//      (3, "attr mandatory")
//    )
//    Ns.int.<(3).Tx(Ns.str.contains("mandatory")).get === List(
//      (2, "str mandatory")
//    )
//
//
////    Ns.int.tx.Tx(Ns.str$).debugGet
//
//    Ns.int.tx.Tx(Ns.str$).get.sortBy(_._1) === List(
//      (1, tx1, Some("str tacit")),
//      (2, tx2, Some("str mandatory")),
//      (3, tx3, Some("attr mandatory")),
//      (4, tx4, Some("attr tacit")),
//      (5, tx5, Some("attr optional with value")),
//      (6, tx6, None) // attr optional without value
//    )


//    Ns(e1).str_.tx.debugGet
//    Ns(e1).str_.tx.get.head === tx2



//    Ns.str.int.t.op.debugGet
//    Ns.str.int.t.op.getHistory === List(
//      ("a", 1, t1, true)
//    )
//
//    Ns.str.t.int.op.getHistory === List(
//      ("a", t1, 1, true)
//    )
//
//    Ns.str.t.op.int.getHistory === List(
//      ("a", t1, true, 1)
//    )


//    val result = Ns.str("Fred").int(1).save
//    val eid    = result.eid
//    val t1     = result.t
//
//    val t2 = Ns(eid).int(2).update.t
//
//
//    Ns.int.op.str.debugGet
//    Ns.int.op.str.getHistory.sortBy(t => (t._1, !t._2)) === List(
//      (1, true, "Fred"),
//      (1, false, "Fred"),
//      (2, true, "Fred")
//    )


//    Ns.e.str.tx.debugGet
//    Ns.e.str.tx.t.debugGet
//    Ns.e_(e1).str.tx.t.debugGet
////    Ns.e_(e1).str.tx.t.int.debugGet
//
//    Ns.e_(e1).str.tx.txInstant.t.op.int.debugGet
//
//    val e1 = 42L
//    val t1 = 1001
//    val t2 = 1002

//    Ns(e1).str.t.op.int.t.op.debugGet
//
//    Ns(e1).str.t.op.int.t.op.getHistory === List(
//      ("a", 1030, false, 1, 1031, false),
//      ("b", 1030, true, 1, 1028, true),
//      ("b", 1030, true, 2, 1031, true),
//      ("a", 1028, true, 1, 1028, true),
//      ("b", 1030, true, 1, 1031, false),
//      ("a", 1028, true, 1, 1031, false),
//      ("a", 1030, false, 2, 1031, true),
//      ("a", 1030, false, 1, 1028, true),
//      ("a", 1028, true, 2, 1031, true)
//    )


//    Ns.str.t.op.int.debugGet
//
//    Ns.str.t.op.int.getHistory.sortBy(t => (t._2, t._3, t._4)) === List(
//      ("a", t1, true, 1),
//      ("a", t1, true, 2),
//
//      ("a", t2, false, 1),
//      ("a", t2, false, 2),
//
//      ("b", t2, true, 1),
//      ("b", t2, true, 2),
//
//      // Note how str("x") was never retracted and stays the same for both int values
//      ("x", t4, true, 4),
//      ("x", t4, true, 5)
//    )


//    Ns(e1).str.t.op.int.debugGet
//
//    Ns(e1).str.t.op.int.getHistory.sortBy(t => (t._2, t._3, t._4)) === List(
//      ("a", t1, true, 1),
//      ("a", t1, true, 2),
//
//      ("a", t2, false, 1),
//      ("a", t2, false, 2),
//
//      ("b", t2, true, 1),
//      ("b", t2, true, 2)
//    )



//    Ns.tx(tx3).debugGet
//    Ns.op(true).debugGet

    //    Ns.int.Ref1.str1.debugGet

//    conn.q("""[:find  ?b ?d ?d
//             | :where [?a :ns/int ?b]
//             |        [?a :ns/ref1 ?c]
//             |        [?c :ref1/str1 ?d]
//             |        [?c ?c_attr ?d]
//             |        [?c_attr :db/ident ?d1]
//             |        [(namespace ?d1) ?d_ns]
//             |        [(!= ?d_ns "db.install")]
//             |        [(!= ?d_ns "db")]
//             |        [(!= ?d_ns "fressian")]]""".stripMargin) foreach println
//
//
//    conn.q("""[:find  ?b ?d ?d
//             | :where [?a :ns/int ?b]
//             |        [?a :ns/ref1 ?c]
//             |        [?c :ref1/str1 ?d]
//             |        [?c ?c_attr ?d]
//             |        [?c_attr :db/ident ?d1]
//             |        [(namespace ?d1) ?d_ns]]""".stripMargin) foreach println

//    Ns.int.Ref1.str1.v.debugGet
//    Ns.int.Ref1.str1.v.get === List((5, "hello", "hello"))


//    Ns(e1, e2).e.t.a.v.debugGet
//
//    Ns(e1, e2).e.t.a.v.get.sortBy(t => (t._1, t._2)) === List(
//      (e1, t2, "str", "b"),
//      (e1, t3, "int", 2),
//      (e2, t4, "str", "x"),
//      (e2, t5, "int", 5),
//      (e2, t7, "ref1", r1)
//    )
//
//    Ns.tx.not(tx3).debugGet
//    Ns.tx.not(tx3).get === List(tx2, tx4, tx5, tx6, tx7)


//    Ns.tx(tx3).debugGet
//    Ns.tx(tx3).get === List(tx3)


//    val n = 2
//
//    Ns.int.e.debugGet
//    Ns.int.ns.debugGet
//    Ns.int.a.debugGet
//    Ns.int.v.debugGet
//    Ns.int.tx.debugGet
//    Ns.int.t.debugGet
//    Ns.int.txInstant.debugGet
//    Ns.int.op.debugGet



//    Ns.int.Ref1.v.debugGet

//    Ns.int.Ref1.v.str1.debugGet
//    Ns.int.Ref1.v.str1.get === List((5, "hello", "hello"))

//    Ns.int.tx.debugGet
//    Ns.str.v.debugGet
//    Ns.int.Ref1.str1.v.get === List((5, "hello", "hello"))

    //    Ns.int.Ref1.str1.debugGet
//    Ns(e2).int.Ref1.str1.e.debugGet
//    Ns(e2).int.Ref1.e.str1.debugGet

    //    Ns.int.Ref1.str1.debugGet
    //    Ns.int.Ref1.str1.e(count).debugGet
    //    Ns.int.Ref1.e(count).str1.debugGet

    //    Ns.e.a.v_(2).debugGet


    //    Ns.e.not(e1).debugGet
    //    Ns.e.not(e1).get === List(e2, r1)

    //    Ns.ns("ns").debugGet
    //    Ns.int.ns_("ns").debugGet
    //
    //    Ns.int.ns_("ns").get === List(2, 5)
    //    Ns.int.ns_("ns", "ref1").get === List(2, 5)
    //    Ns.int.ns_.not("ns").get === Nil
    //    Ns.int.ns_.not("ref1").get === List(2, 5)
    //    Ns.int.ns_.not("ns", "ref1").get === Nil
    //
    //    Ns.int.a_("int").get === List(2, 5)
    //    Ns.int.a_("str").get === Nil
    //    Ns.int.a_("str", "int").get === List(2, 5)
    //    Ns.int.a_.not("str").get === List(2, 5)
    //    Ns.int.a_.not("str", "int").get === Nil
    //
    //    Ns.int.v_(2).get === List(2)
    //    // Value only relates to previous custom datom
    //    Ns.int.v_("b").get === Nil
    //    Ns.int.v_(2, "b").get === List(2)
    //    Ns.int.v_.not(2).get === List(5)
    //    Ns.int.v_.not(2, "b").get === List(5)
    //    Ns.int.v_.not(2, 5).get === Nil
    //
    //    Ns.int.tx_(tx3).get === List(2)
    //    Ns.int.tx_(tx3, tx5).get === List(2, 5)
    //    Ns.int.tx_.not(tx3).get === List(5)
    //    Ns.int.tx_.not(tx3, tx5).get === Nil
    //
    //    Ns.int.t_(t3).get === List(2)
    //    Ns.int.t_(t3, t5).get === List(2, 5)
    //    Ns.int.t_.not(t3).get === List(5)
    //    Ns.int.t_.not(t3, t5).get === Nil
    //
    //    Ns.int.txInstant_(d3).get === List(2)
    //    Ns.int.txInstant_(d3, d5).get === List(2, 5)
    //    Ns.int.txInstant_.not(d3).get === List(5)
    //    Ns.int.txInstant_.not(d3, d5).get === Nil
    //
    //    Ns.int.op_(true).get === List(2, 5)
    //    Ns.int.op_(true, false).get === List(2, 5)
    //    Ns.int.op_.not(true).get === Nil
    //    Ns.int.op_.not(false).get === List(2, 5)
    //    Ns.int.op_.not(true, false).get === Nil

    //    val n = 42L
    //    val n = e2

    //    Ns(42).int.debugGet
    //    Ns.e(n).int.debugGet
    //    Ns.e_(n).int.debugGet
    //    val x1 = Ns(n).int.get
    //    val x2 = Ns.e_(n).int.get
    //    Ns(e1).int.get === 7
    //    Ns(e1).int.debugGet
    //    Ns.e_(e1).int.debugGet
    //    Ns.int.e_(e1).debugGet

    //    Ns(e1, e2).int.debugGet
    //    Ns.e_(e1, e2).int.debugGet
    //    Ns.e_.not(e1).int.debugGet
    //    Ns.e_.not(e1, e2).int.debugGet
    //
    //    Ns.int.e_(e1, e2).debugGet
    //    Ns.int.e_.not(e1).debugGet
    //    Ns.int.e_.not(e1, e2).debugGet

    //    Ns.int.e_(e1).get === List(2)
    //    Ns.int.e_(e1, e2).get === List(2, 5)
    //    Ns.int.e_.not(e1).get === List(5)
    //    Ns.int.e_.not(e1, e2).get === Nil

    //    Ns.e.tx(tx2).debugGet
    //    Ns.ns.tx(tx2).debugGet
    //    Ns.a.tx(tx2).debugGet
    //    Ns.v.tx(tx2).debugGet
    //    Ns.t.tx(tx2).debugGet
    //    Ns.txInstant.tx(tx2).debugGet
    //    Ns.op.tx(tx2).debugGet
    //
    //    Ns.t(t1).debugGet
    //    Ns.t(t2).debugGet

    //    Ns.v.not(2).debugGet
    //    Ns.v.not(2).get.map(_.toString).sorted === 349


    //    Ns.v.not(2).get.size === 349

    //    Ns(e2).date(d6).update
    //
    //    Ns.v(2).debugGet
    //    Ns.v(2).get === List(2)
    //
    //    Ns.v(2, "b").debugGet
    ////    Ns.v(2, "b").get === List(2, "b")
    //
    //    Ns.v(2, d6).debugGet
    //    Ns.v(2, d6).get === List(d6, 2)

    //    Ns.ns("ns", "ref1").debugGet


    //    Schema.a.debugGet

    //    println(tx1)
    //    println(tx2)
    //    println(tx3)
    //    println(tx4)
    //    println(tx5)
    //    println(tx6)
    //    println(tx7)
    //
    //    println(txR1)
    //    println(txR2)
    //    println(txR3)
    //    println(txR4)
    //    println(txR5)
    //    println(txR6)
    //    println(txR7)

    //    Ns.int(1).save
    //    Ns.int(2).save

    //    conn.q(
    //      s"""[:find  ?b_tx ?b1 ?b
    //         | :where [?a ?a_attr ?b ?b_tx]
    //         |        [?a_attr :db/ident ?b1]
    //         |        ]""".stripMargin).toList.sortBy(_.head.toString) foreach println
    //
    //    println("-----------------")
    //    conn.q(
    //      s"""[:find  ?b_tx ?b_ns ?b_a
    //         | :where [?a ?a_attr ?b ?b_tx]
    //         |        [?a_attr :db/ident ?b1]
    //         |        [(name ?b1) ?b_a]
    //         |        [(namespace ?b1) ?b_ns]
    //         |        ]""".stripMargin).toList.sortBy(_.head.toString) foreach println

    //    println(conn.q(s"""[:find  ?b_tx ?b_ns
    //             | :where [?a ?a_attr ?b ?b_tx]
    //             |        [?a_attr :db/ident ?b1]
    //             |        [(namespace ?b1) ?b_ns]
    //             |        [(= ?b_tx $tx1)]
    //             |        ]""".stripMargin)) //foreach println

    //    println(conn.q(s"""[:find  ?b_tx
    //             | :where [?a ?a_attr ?b ?b_tx]
    //             |        [?a_attr :db/ident ?b1]
    //             |        [(namespace ?b1) ?b_ns]
    //             |        [(!= ?b_ns "db.install")]
    //             |        [(!= ?b_ns "db")]
    //             |        [(!= ?b_ns "fressian")]
    //             |        [(= ?b_tx $tx4)]
    //             |
    //             |        ]""".stripMargin)) foreach println
    //
    //    println(conn.q("""[:find  ?b_tx
    //             | :in    $ [?b_tx ...]
    //             | :where [?a ?a_attr ?b ?b_tx]
    //             |        [?a_attr :db/ident ?b1]
    //             |        [(namespace ?b1) ?b_ns]
    //             |        [(!= ?b_ns "db.install")]
    //             |        [(!= ?b_ns "db")]
    //             |        [(!= ?b_ns "fressian")]]""".stripMargin, tx2))

    //    Ns.tx.not(tx3).debugGet

    //    Ns.tx.not(tx3).debugGet


    //        Ns.e.ns.debugGet
    //        Ns.e.ns.not("db.install", "db", "fressian").debugGet
    //        Ns.e.ns.not("ref1").debugGet
    //        Ns.e.ns("ns").ns.not("db.install", "db", "fressian").debugGet
    //        Ns.e.not(e1).ns.not("db.install", "db", "fressian").debugGet
    //    Ns.e.not(e1).debugGet
    //    Ns.e.not(e1).get.size === 163


    //    Ns.int.ns_("ns").get === List("ns")
    //    Ns.int.ns_("ns", "ref1").get === List("ns", "ref1")
    //    Ns.int.ns_.not("ns").get === List("ref1", "db.install", "db", "fressian")
    //    Ns.int.ns_.not("ns", "ref1").get === List("db.install", "db", "fressian")
    //
    //    Ns.int.a_("str").get === List("str")
    //    Ns.int.a_("str", "int").get === List("str", "int")
    //    Ns.int.a_.not("str").get(5) === List("code", "ref1", "str1", "index", "txInstant")
    //    Ns.int.a_.not("str", "ref1").get(5) === List("code", "str1", "index", "txInstant", "cardinality")
    //
    //    Ns.int.v_(2).get === List(2)
    //    Ns.int.v_("hello").get === List("hello")
    //    Ns.int.v_("non-existing value").get === Nil
    //    Ns.int.v_(2, "b").get === List(2, "b")
    //    Ns.int.v_.not(2).get.size === 349
    //    Ns.int.v_.not(2, "b").get.size === 348
    //
    //    Ns.int.tx_(tx3).get === List(tx3)
    //    Ns.int.tx_(tx3, tx5).get === List(tx3, tx5)
    //    Ns.int.tx_.not(tx3).get.size === 12
    //    Ns.int.tx_.not(tx3, tx5).get.size === 11
    //
    //    Ns.int.t_(t3).get === List(t3)
    //    Ns.int.t_(t3, t5).get === List(t3, t5)
    //    Ns.int.t_.not(t3).get.size === 12
    //    Ns.int.t_.not(t3, t5).get.size === 11
    //
    //    Ns.int.txInstant(d2).get === List(d2)
    //    Ns.int.txInstant(d2, d3).get.toString === List(d2, d3).toString
    //    Ns.int.txInstant_.not(d2).get.size === 9
    //    Ns.int.txInstant_.not(d2, d3).get.size === 8
    //
    //    Ns.int.op_(true).get === List(true)
    //    Ns.int.op_(true, false).get === List(true)
    //    Ns.int.op_.not(true).get === Nil
    //    Ns.int.op_.not(true, false).get === Nil


    //    Ns.e(e1).debugGet
    //    Ns.e(e1, e2).debugGet
    //    Ns.e.not(e1).debugGet
    //    Ns.a.e.not(e1).debugGet
    //    Ns.e.not(e1).a.debugGet
    //    Ns.e.not(e1, e2).debugGet


    //    Ns.e(e1).get === List(e1)
    //    Ns.e(e1, e2).get === List(e1, e2)
    //    Ns.e.not(e1).get.size === 163
    //    Ns.e.not(e1, e2).get.size === 162


    //    Ns.ns("ns").get === List("ns")
    //    Ns.ns("ns", "ref1").get === List("ns", "ref1")
    //    Ns.ns.not("ns").get === List("ref1", "db.install", "db", "fressian")
    //    Ns.ns.not("ns", "ref1").get === List("db.install", "db", "fressian")
    //
    //    Ns.a("str").get === List("str")
    //    Ns.a("str", "int").get === List("str", "int")
    //    Ns.a.not("str").get(5) === List("code", "ref1", "str1", "index", "txInstant")
    //    Ns.a.not("str", "ref1").get(5) === List("code", "str1", "index", "txInstant", "cardinality")
    //
    //    Ns.v(2).get === List(2)
    //    Ns.v(2, "b").get === List(2, "b")
    //    Ns.v.not(2).get.size === 349
    //    Ns.v.not(2, "b").get.size === 348
    //
    //    Ns.tx(tx3).get === List(tx3)
    //    Ns.tx(tx3, tx5).get === List(tx3, tx5)
    //    Ns.tx.not(tx3).get.size === 12
    //    Ns.tx.not(tx3, tx5).get.size === 11
    //
    //    Ns.t(t3).get === List(t3)
    //    Ns.t(t3, t5).get === List(t3, t5)
    //    Ns.t.not(t3).get.size === 12
    //    Ns.t.not(t3, t5).get.size === 11
    //
    //    Ns.txInstant(d2).get === List(d2)
    //    Ns.txInstant(d2, d3).get.toString === List(d2, d3).toString
    //    Ns.txInstant.not(d2).get.size === 9
    //    Ns.txInstant.not(d2, d3).get.size === 8
    //
    //    Ns.op(true).get === List(true)
    //    Ns.op(true, false).get === List(true)
    //    Ns.op.not(true).get === Nil
    //    Ns.op.not(true, false).get === Nil
    //


    //    Ns.a("str", "int").debugGet
    //
    //    Ns.a.not("str", "xxx").debugGet

    //    Ns.a(xx).get === List()
    //    Ns.a("str").get === List()
    //    Ns.e.a("str").v.t.get === List()


    //    conn.datomicConn.sync(t3).get
    //    println(conn.q("""[:find ?e :where [?e :ns/int 2]]""".stripMargin))
    //    println(conn.q("""[:find ?e :where [?e _ 2]]""".stripMargin))

    //    println(conn.q("""[:find ?e :where [?e 40 21]]""".stripMargin))
    //    println(conn.q("""[:find ?e :where [?e _ 21]]""".stripMargin))


    //    println(conn.db.datoms(VAET, 2.asInstanceOf[AnyRef]))
    //    println(conn.db.datoms(VAET, ""))
    //    conn.db.datoms(datomic.Database.EAVT, 17592186045445L.asInstanceOf[Object])
    //      .forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))

    //    conn.db.datoms(datomic.Database.EAVT, e1.asInstanceOf[Object]).forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //        conn.db.datoms(datomic.Database.EAVT).forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //    println("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§")
    //    conn.db.datoms(datomic.Database.AEVT, ":ns/int").forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //    println("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§")
    //    conn.db.datoms(datomic.Database.AVET, ":ns/int").forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))


    //    conn.db.datoms(datomic.Database.VAET).forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))

    //    conn.datomicConn.log.txRange(1028, null).iterator().asScala.toList.foreach { map =>
    //      val it = map.values().iterator()
    //      val t = it.next
    //      val datoms = it.next.asInstanceOf[java.util.Collection[datomic.Datom]]
    //      println(t)
    //      datoms.forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //    }

    //    println(conn.q(
    //      """[:find  ?e :where [?e ?attr 2][?attr :db/ident _]]]""".stripMargin))
    //
    //
    //    println(conn.q(
    //      """[:find  ?e
    //        | :where [?e ?attr 2]
    //        |        [?attr :db/ident _]]
    //        | ]""".stripMargin))
    //
    //    println(conn.q(
    //      """[:find  ?a
    //        | :where [?a ?a_attr ?b]
    //        |        [?a_attr :db/ident _]
    //        |        [(str ?b) ?b1]
    //        |        [(= ?b1 "2")]
    //        | ]""".stripMargin))
    //
    //    println(conn.q(
    //      """[:find  ?a
    //        | :where [?a ?a_attr ?b 13194139534343]]""".stripMargin))
    //
    //    println(conn.q(
    //      """[:find  ?b_tx
    //        | :where [?a ?a_attr ?b ?b_tx]
    //        |        [(= ?b_tx 13194139534343)]]""".stripMargin))


    // odo: disallow full scans
    //    Ns.e.debugGet
    //    Ns.e.debugGet
    //    Ns.ns.debugGet
    //    Ns.a.debugGet
    //    Ns.v.debugGet
    //    Ns.t.debugGet
    //    Ns.tx.debugGet
    //    Ns.txInstant.debugGet
    //    Ns.op.debugGet


    //        println(Model2Query(Model(List(
    //          Bond("ns", "ref1", "ref1", 1, Seq()),
    //          Meta("?", "ns", "ns", NoValue, NoValue)))))
    //
    //        Ns.Ref1.e.debugGet
    //        Ns.Ref1.ns.debugGet
    //        Ns.Ref1.a.debugGet
    //        Ns.Ref1.v.debugGet
    //        Ns.Ref1.tx.debugGet
    //        Ns.Ref1.t.debugGet
    //        Ns.Ref1.txInstant.debugGet
    //        Ns.Ref1.op.debugGet


    //    Ns.e_(42L).int.debugGet
    //    Ns.e.int.ns.debugGet
    //    Ns.ns.int.debugGet
    //    Ns.a.int.debugGet
    //    Ns.v.int.debugGet
    //    Ns.int.tx.op.debugGet
    //    Ns.tx.op.int.debugGet

    //    Ns.t.int.debugGet
    //    Ns.txInstant.int.debugGet
    //    Ns.op.int.debugGet
    //
    //
    //
    //    Ns(e1).int.tx.debugGet
    //    Ns(e1).int.t.debugGet
    //    Ns(e1).int.txInstant.debugGet
    //
    //    Ns(e1).tx.int.debugGet
    //    Ns(e1).t.int.debugGet
    //    Ns(e1).txInstant.int.debugGet
    //
    //    Ns(e1).int.e.debugGet
    //    Ns(e1).int.ns.debugGet
    //    Ns(e1).int.a.debugGet
    //    Ns(e1).int.v.debugGet
    //    Ns(e1).int.op.debugGet
    //
    //    Ns(e1).e.int.debugGet
    //    Ns(e1).ns.int.debugGet
    //    Ns(e1).a.int.debugGet
    //    Ns(e1).v.int.debugGet
    //    Ns(e1).op.int.debugGet
    //
    //
    //
    //
    //
    //
    //
    //    Ns(e1).int.t.debugGetHistory
    //    Ns(e1).int.tx.debugGetHistory
    //    Ns(e1).int.txInstant.debugGetHistory
    //
    //    Ns(e1).t.int.debugGetHistory
    //    Ns(e1).tx.int.debugGetHistory
    //    Ns(e1).txInstant.int.debugGetHistory
    //
    //    Ns(e1).int.e.debugGetHistory
    //    Ns(e1).int.ns.debugGetHistory
    //    Ns(e1).int.a.debugGetHistory
    //    Ns(e1).int.v.debugGetHistory
    //    Ns(e1).int.op.debugGetHistory
    //
    //    Ns(e1).e.int.debugGetHistory
    //    Ns(e1).ns.int.debugGetHistory
    //    Ns(e1).a.int.debugGetHistory
    //    Ns(e1).v.int.debugGetHistory
    //    Ns(e1).op.int.debugGetHistory


    //    Ns(e1).int.op_(true).debugGet
    //    Ns(e1).int.op_(false).debugGet

    // All attribute assertions/retractions of entity e1 at t2
    //    Ns(e1).a.debugGet
    //    Ns(e1).a.v.debugGet
    //    Ns(e1).a.v.t(t2).debugGet
    //    Ns(e1).a.v.t(t2).op.debugGet

    //    Ns(e1).a.v.t(t2).op.getHistory.sortBy(t => t._4) === List(
    //      // str value was updated from "a" to "b"
    //      (":ns/str", "a", t2, false),
    //      (":ns/str", "b", t2, true)
    //    )

    //        val rows = conn.q("""[:find  ?b ?b2
    //                            | :where [?a :ns/str ?b]
    //                            |        [?a ?attr _]
    //                            |        [?attr :db/ident ?b1]
    //                            |        [(.toString ^clojure.lang.Keyword ?b1) ?b2]]""".stripMargin)
    //
    //    val rows = conn.q("""[:find  ?id ?ident ?ident2 ?ns ?attr
    //             | :where [:db.part/db :db.install/attribute ?id]
    //             |        [?id :db/ident ?ident]
    //             |        [(str ?ident) ?ident2]
    //             |        [(namespace ?ident) ?ns]
    //             |        [(name ?ident) ?attr]]""".stripMargin)
    //
    //
    //    println(rows.head(2).asInstanceOf[String])
    //    println(rows.head(2).isInstanceOf[String])
    //
    //    println(rows.head(1).isInstanceOf[clojure.lang.Keyword])
    //    println(rows.head(1).isInstanceOf[Object])
    //
    //        rows foreach println
    //    Ns.ns.debugGet


    //    val List(_, e2) = Ns.strs("a").Ref1.int1(1).save.eids

    //    Ns.int.Ref1.e(count).debugGet
    //    Ns.e(count).Ref1.e(count).debugGet

    //    Ns.e(count).Ref1.int1_(1).debugGet
    //    val two = 2
    //    m(Ns.strs.Ref1.e.int1.<(two)).debugGet
    //    Ns.strs.Ref1.e(count).int1.<(two).debugGet
    //    Ns.strs.Ref1.int1.<(two).debugGet

    //    Schema.tpe.apply(count).get
    //    Schema.tpe.apply(max).get

    //    Ns.e.str.debugGet
    //    //    Ns.ns.str.debugGet
    //    //    Ns.a.str.debugGet
    //    //    Ns.v.str.debugGet
    //    //    Ns.t.str.debugGet
    //
    //    Ns.str.e.debugGet
    //    Ns.str.ns.debugGet
    //    Ns.str.a.debugGet
    //    //    Ns.str.v.debugGet
    //    Ns.str.t.debugGet
    //    Ns.str.op.debugGet
    //
    //    Ns.str.ns.a.v.debugGet

    //    Schema.ns.a.debugGet
    //    m(Schema.ns + Ns.str.t).debugGet

    //    Schema.ns.a.v.debugGet
    //    Schema.ns.v._query.datalog ===
    //      """[:find  ?a ?c2 ?c
    //        | :where [?a ?attr ?c]
    //        |        [?attr :db/ident ?c1]
    //        |        [(name ?c1) ?c2]]""".stripMargin
    //
    //    Schema.e.a.v._query.datalog ===
    //      """[:find  ?a ?c2 ?c
    //        | :where [?a ?attr ?c]
    //        |        [?attr :db/ident ?c1]
    //        |        [(name ?c1) ?c2]]""".stripMargin
    //
    //    Schema.e.ns.a.v._query.datalog ===
    //      """[:find  ?a ?c2 ?d2 ?c
    //        | :where [:db.part/db :db.install/attribute ?c]
    //        |        [?ns :db/ident ?c1]
    //        |        [(.getNamespace ^clojure.lang.Keyword ?c1) ?c2]
    //        |        [(name ?c1) ?d2]]""".stripMargin
    //
    //    def clean2(tpl: (String, Any)): (String, Any) = tpl match {
    //      case (a, v: clojure.lang.Keyword) => (a, v.toString)
    //      case (a, v)                       => (a, v)
    //    }
    //    def clean(tpl: (String, String, Any)): (String, String, Any) = tpl match {
    //      case (ns, a, v: clojure.lang.Keyword) => (ns, a, v.toString)
    //      case (ns, a, v)                       => (ns, a, v)
    //    }
    //
    //
    //    // :db.type/long
    //    Schema.e_(22L).a.v.debugGet
    //    Schema.e_(22L).ns.a.v.debugGet
    //    Schema.e_(22L).a.v.get.map(clean2) === List(
    //      ("doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
    //      ("tag", ":int"),
    //      ("ident", ":db.type/long"),
    //    )
    //
    //    Schema.e_(22L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
    //      ("db", "ident", ":db.type/long"),
    //      ("fressian", "tag", ":int")
    //    )
    //
    //    // :db.type/string
    //    Schema.e_(23L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "Value type for strings."),
    //      ("db", "ident", ":db.type/string"),
    //      ("fressian", "tag", ":string")
    //    )
    //
    //    // :db.cardinality/one
    //    Schema.e_(35L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
    //      ("db", "ident", ":db.cardinality/one")
    //    )
    //
    //    // :db.cardinality/many
    //    Schema.e_(36L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
    //      ("db", "ident", ":db.cardinality/many")
    //    )
    //
    //    // :ns/str
    //    Schema.e_(63L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "cardinality", 35),
    //      ("db", "fulltext", true),
    //      ("db", "ident", ":ns/str"),
    //      ("db", "index", true),
    //      ("db", "valueType", 23)
    //    )
    //
    //    // :ns/ints
    //    Schema.e_(80L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "cardinality", 36),
    //      ("db", "ident", ":ns/ints"),
    //      ("db", "index", true),
    //      ("db", "valueType", 22) // Uses Datomic long internally
    //    )


    //    Schema.a.debugGet

    //    Schema.a.get.sorted.take(10) === List(
    //      "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec",
    //      "bigDecMap",
    //      "bigDecMapK",
    //      "bigDecs",
    //      "bigInt",
    //      "bigIntMap"
    //    )

    //    Schema.ns(count).debugGet
    //    Schema.a(count).debugGet
    //    Schema.ns(count).get.head === 9
    //    Schema.a(count).get.head === 93

    //    Schema.ns.a.debugGet
    //    Schema.ns.a.get.sorted.take(10) === List(
    //      ("db", "cardinality"),
    //      ("db", "code"),
    //      ("db", "doc"),
    //      ("db", "excise"),
    //      ("db", "fn"),
    //      ("db", "fulltext"),
    //      ("db", "ident"),
    //      ("db", "index"),
    //      ("db", "isComponent"),
    //      ("db", "lang"),
    //    )
    //
    //
    //    // eq
    //    Schema.ns_("db.install").a.get.sorted === List(
    //      "attribute",
    //      "function",
    //      "partition",
    //      "valueType"
    //    )
    //
    //    // + count
    //    Schema.ns_("db.install").a(count).get.head === 4
    //
    //    // eq multiple
    //    Schema.ns_("db.install", "fressian").a.get.sorted === List(
    //      "attribute",
    //      "function",
    //      "partition",
    //      "tag", // fressian
    //      "valueType"
    //    )
    //
    //    // neq
    //    Schema.ns_.not("db").a.get.sorted.take(5) === List(
    //      "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec"
    //    )
    //
    //    // neq multiple
    //    Schema.ns_.not("db", "ns").a.get.sorted.take(4) === List(
    //      "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //    )
    //
    //    // eq
    //    Schema.a("lang").get === List("lang")
    //    Schema.ns.a("lang").get === List(("db", "lang"))
    //
    //    Schema.a("valueType").get === List("valueType")
    //    // `valueType` attribute is defined in 2 namespaces
    //    Schema.ns.a("valueType").get === List(("db", "valueType"), ("db.install", "valueType"))
    //
    //    // eq multiple
    //    Schema.a("lang", "txInstant").get.sorted === List("lang", "txInstant")
    //    Schema.ns.a("lang", "txInstant").get.sorted === List(("db", "lang"), ("db", "txInstant"))
    //    Schema.ns.a("lang", "valueType").get.sorted === List(("db", "lang"), ("db", "valueType"), ("db.install", "valueType"))
    //
    //    // neq
    //    Schema.a.not("attribute").get.sorted.take(9) === List(
    //      // "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec",
    //      "bigDecMap",
    //      "bigDecMapK",
    //      "bigDecs",
    //      "bigInt",
    //      "bigIntMap"
    //    )
    //
    //    // neq multiple
    //    Schema.a.not("attribute", "attrs").get.sorted.take(8) === List(
    //      // "attribute",
    //      // "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec",
    //      "bigDecMap",
    //      "bigDecMapK",
    //      "bigDecs",
    //      "bigInt",
    //      "bigIntMap"
    //    )
    //
    //
    //    // eq
    //    Schema.ns.a_("lang").get === List("db")
    //
    //    // `valueType` is in two namespaces
    //    Schema.ns.a_("valueType").get.sorted === List("db", "db.install")
    //
    //    // eq multiple
    //    Schema.ns.a_("lang", "tag").get.sorted === List("db", "fressian")
    //
    //    // neq
    //    Schema.ns.a_.not("tag").get.sorted.take(13) === List(
    //      "db",
    //      "db.alter",
    //      "db.excise",
    //      "db.install",
    //      "db.sys",
    //      // "fressian", - only attribute named `tag` is in `not` clause
    //      "ns",
    //      "ref1",
    //      "ref2"
    //    )
    //
    //    // neq multiple
    //    Schema.ns.a_.not("lang", "tag").get.sorted.take(11) === List(
    //      "db", // `db` namespace has `lang` but also has other attributes and is therefore not excluded
    //      "db.alter",
    //      "db.excise",
    //      "db.install",
    //      "db.sys",
    //      // "fressian", - only attribute named `tag` is in `not` clause
    //      "ns",
    //      "ref1",
    //      "ref2"
    //    )

    //        val model1 = Model(List(
    //          Atom("ns", "int", "Int", 1, VarValue, None, Seq(NoValue), Seq()),
    //          Bond("ns", "ref1", "ref1", 1, Seq()),
    //          Meta("ref1", "ref1", "e", NoValue, Fn("count", None)),
    //          Atom("ref1", "int1", "Int", 1, Lt(2), None, Seq(), Seq())))
    //        val model1 = Model(List(
    //          Atom("ns", "int", "Int", 1, VarValue, None, Seq(NoValue), Seq()),
    //          Bond("ns", "ref1", "ref1", 1, Seq()),
    //          Meta("?", "e", "e", NoValue, Fn("count", None)),
    //          Atom("ref1", "int1", "Int", 1, Lt(2), None, Seq(), Seq())))
    //        val query1 = Model2Query(model1)
    //
    //        val model2 = Model(List(
    //          Atom("ns", "int", "Int", 1, VarValue, None, Seq(NoValue), Seq()),
    //          Bond("ns", "ref1", "ref1", 1, Seq()),
    //          Meta("?", "e", "e", NoValue, Fn("count", None)),
    //          Atom("ref1", "int1", "Int", 1, Lt(2), None, Seq(), Seq())))
    //        val query2 = Model2Query(model2)
    //        println(model)
    //        println(query)

    //        Schema.a.debugGet
    //        Schema.a("attribute").debugGet
    //        Schema.a("attribute", "attrs").debugGet
    //        Schema.a.not("attribute").debugGet
    //        Schema.a.not("attribute", "attrs").debugGet

    //        Schema.ns.debugGet
    //        Schema.ns("ref1").debugGet
    //        Schema.ns("ref1", "ref2").debugGet
    //        Schema.ns.not("ref1").debugGet
    //        Schema.ns.not("ref1", "ref2").debugGet
    //
    //    Schema.ns.debugGet
    //    Schema.ns(count).debugGet
    //
    //
    //    Ns.str.apply(count).debugGet
    //    Schema.a.apply(count).debugGet
    //    Schema.a.apply(countDistinct).debugGet
    //    Schema.a.apply("lang").debugGet
    //    Schema.a(count).valueType(countDistinct).get.head === (124, 8)
    //
    //
    //    Ns.ns.Ref1.a.debugGet
    //    Ns.ns(count).a("aa").v.debugGet
    //    Schema.ns(count).a("aa").v.debugGet
    //    Log.ns(count).a("aa").v.debugGet
    //
    //    println(Schema.ns._model)
    //

    //    Schema.ns.debugGet

    //    Ns.ns.a.v.debugGet
    //    Ns.ns.Ref1.a.debugGet
    //    Schema.ns.a.v.debugGet
    //    Log.ns.a.v.debugGet

    //    Ns.e(count).int_.>(1).get.head === 2
    //    Ns.int.e(count).get.head === 2
    //    Ns.int_.>(1).e(count).get.head === 2

    //    Ns.e.int.debugGet
    //    Ns.int.e.debugGet

    //        val e1 = 42L
    //        Ns.long(e1).debugGet
    //        Ns(e1).str.get === List((e1, "a"))
    //        Ns.e(e1).int.get === List((e1, 1))
    //        Ns(e1).int.get === List((e1, 1))
    //    Schema.e(e1).get === List((e1, 1))


    //    Schema.e.ns.a.v.tx.t.txInstant.op.debugGet

    //    EAVT.e.a.

    //    Ref1.e(42L).str1("xx").get === 7


    //    Schema.ns.a.debugGet
    //    Schema.22L).a.v.debugGet
    //    Ns(e1).a.v.debugGet
    //
    //    Ns(e1).a.v.t.op.debugGetHistory
    //
    //    Schema.e_(22L).a.v.debugGet
    //
    //    Ns(e1).a.v.t.op.getHistory.sortBy(t => (t._3, t._4)) === List(
    //      (":ns/str", "a", t1, true),
    //      (":ns/int", 1, t1, true),
    //      (":ns/str", "a", t2, false),
    //      (":ns/str", "b", t2, true),
    //      (":ns/int", 1, t3, false),
    //      (":ns/int", 2, t3, true)
    //    )

    //    val e = Ns.int(1).save.eid
    //
    //    Ns.e.int.get === List((e, 1))
    //
    //    e.retract
    //
    //    Ns.e.int.get === Nil
    //
    //
    //
    //    Ns.int_.tx.debugGet
    //    Ns.int_.tx.debugGetHistory
    ////    Ns.e.int.t.op.debugGetHistory
    //
    ////    Ns.e.int.t.op.debugGetHistory
    ////
    ////    Ns.e.int.t.op.getHistory.sortBy(t => (t1, t._3, t._4)) === List(
    ////      // e1
    ////      (e1, 1, t1, true),
    ////      (e1, 1, t3, false),
    ////      (e1, 2, t3, true),
    ////      //e2
    ////      (e2, 4, t4, true),
    ////      (e2, 4, t5, false),
    ////      (e2, 5, t5, true)
    ////    )


    //    Schema.ns(count).get === List("db.install")


    ok
  }

}