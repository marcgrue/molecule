package molecule.time

import datomic.Peer
import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.expectCompileError
import molecule.util.schema.CoreTestSchema
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.collection.JavaConverters._


class GetHistory extends Specification {

  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)

  // First entity - 3 transactions

  val tx1 = Ns.str("a").int(1).save
  val e1  = tx1.eid
  val t1  = tx1.t

  val tx2 = Ns(e1).str("b").update
  val t2  = tx2.t

  val tx3 = Ns(e1).int(2).update
  val t3  = tx3.t


  // Second entity - 2 transactions

  val tx4 = Ns.str("x").int(4).save
  val e2  = tx4.eid
  val t4  = tx4.t

  val tx5 = Ns(e2).int(5).update
  val t5  = tx5.t


  "1 entity, 1 attr" >> {

    // Current values are always the last asserted value
    Ns(e1).int.op_(true).get === List(2)
    Ns(e1).int.op_(false).get === List()

    // str updated at t2
    Ns(e1).str.txT.op.getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      ("a", t1, true), // "a" asserted
      ("a", t2, false), // "a" retracted
      ("b", t2, true) // "b" asserted
    )

    // int updated at t3
    Ns(e1).int.txT.op.getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      (1, t1, true), // 1 asserted
      (1, t3, false), // 1 retracted
      (2, t3, true) // 2 asserted
    )

    // int history with entity
    Ns.e.int.txT.op.getHistory.toSeq.sortBy(t => (t1, t._3, t._4)) === List(
      // e1
      (e1, 1, t1, true),
      (e1, 1, t3, false),
      (e1, 2, t3, true),
      //e2
      (e2, 4, t4, true),
      (e2, 4, t5, false),
      (e2, 5, t5, true)
    )

    // int values over time
    Ns(e1).int.getHistory === List(1, 2)

    // Asserted int values of entity e1 over time
    Ns(e1).int.op_(true).getHistory === List(1, 2)

    // Retracted int values of entity e1 over time
    Ns(e1).int.op_(false).getHistory === List(1)
  }


  "Multiple domain attrs" >> {

    // Mixing the "timeline" of two user-defined "domain" attributes gives
    // us some redundant repetition from unified attribute values.
    // To illustrate, let's revisit the str datoms:

    // str updated at t2
    Ns(e1).str.txT.op.getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      ("a", t1, true), // "a" asserted
      ("a", t2, false), // "a" retracted
      ("b", t2, true) // "b" asserted
    )

    // Adding the int attribute will cause its historic values 1 and 2 to repeatedly
    // be unified with each str value from above so that we get 3 x 2 datoms:
    Ns(e1).str.txT.op.int.getHistory.toSeq.sortBy(t => (t._2, t._3, t._4)) === List(
      ("a", t1, true, 1),
      ("a", t1, true, 2),

      ("a", t2, false, 1),
      ("a", t2, false, 2),

      ("b", t2, true, 1),
      ("b", t2, true, 2)
    )

    // Without a given entity, this approach quickly explodes and becomes useless:
    Ns.str.txT.op.int.getHistory.toSeq.sortBy(t => (t._2, t._3, t._4)) === List(
      ("a", t1, true, 1),
      ("a", t1, true, 2),

      ("a", t2, false, 1),
      ("a", t2, false, 2),

      ("b", t2, true, 1),
      ("b", t2, true, 2),

      // Note how str("x") was never retracted and stays the same for both int values
      ("x", t4, true, 4),
      ("x", t4, true, 5)
    )

    // Additional attributes are better used to filter the result
    // "str operations on enties having had an int value 1"
    Ns.str.txT.op.int(1).getHistory.toSeq.sortBy(t => (t._2, t._3, t._4)) === List(
      ("a", t1, true, 1),
      ("a", t1, true, 2),

      ("a", t2, false, 1),
      ("a", t2, false, 2),

      ("b", t2, true, 1),
      ("b", t2, true, 2)
    )

    // ..and even better as tacet attributes
    // "str operations on entities having had an int value of 1"
    Ns.str.txT.op.int_(1).getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      ("a", t1, true),
      ("a", t2, false),
      ("b", t2, true)
    )

    // Giving the int value 5 we get to the second entity
    // "str operations on entities having had an int value of 5"
    Ns.str.txT.op.int_(5).getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      ("x", t4, true)
    )


    // This is not so useful. So instead, we might want to use the int
    // attribute to filter

    // Reversing the attributes we get to the first entity via a or b:
    // "int operations on entities having had an int value of a"
    Ns.int.txT.op.str_("a").getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      (1, t1, true),
      (1, t3, false),
      (2, t3, true)
    )
    // "int operations on entities having had a str value of b"
    Ns.int.txT.op.str_("b").getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      (1, t1, true),
      (1, t3, false),
      (2, t3, true)
    )

    // Getting historic operations on second entity via str value x
    // "int operations on entities having had a str value of x"
    Ns.int.txT.op.str_("x").getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      (4, t4, true),
      (4, t5, false),
      (5, t5, true)
    )

    // Order of attributes is free.
    // All generic attributes always relate to the previous domain attribute (`int` here)
    Ns.str_("x").int.txT.op.getHistory.toSeq.sortBy(t => (t._2, t._3)) === List(
      (4, t4, true),
      (4, t5, false),
      (5, t5, true)
    )
  }


  "Multiple attrs" >> {

    // Imagine we were to retrieve tx data from multiple attribute histories:
    //   Ns.str.txT.op.int.txT.op.getHistory
    // Then Datomic would unify two timelines of history for each attribute (str, int)
    // and the result is not so useful:
    Peer.q(
      s"""
         |[:find  ?c ?txT ?op ?d ?txT2 ?op2
         | :where [$e1 :ns/str ?c ?tx ?op]
         |        [(datomic.Peer/toT ^Long ?tx) ?txT]
         |        [$e1 :ns/int ?d ?tx2 ?op2]
         |        [(datomic.Peer/toT ^Long ?tx2) ?txT2]]
      """.stripMargin, conn.db.history).asScala.map(_.asScala.toSeq) === List(
      Seq("a", 1030, false, 1, 1031, false),
      Seq("b", 1030, true, 1, 1028, true),
      Seq("b", 1030, true, 2, 1031, true),
      Seq("a", 1028, true, 1, 1028, true),
      Seq("b", 1030, true, 1, 1031, false),
      Seq("a", 1028, true, 1, 1031, false),
      Seq("a", 1030, false, 2, 1031, true),
      Seq("a", 1030, false, 1, 1028, true),
      Seq("a", 1028, true, 2, 1031, true)
    )

    // Instead we would likely look only for transactional generic data as shown in the previous test

    // Consequently, generic tx data like `v`, `tx`, `txT`, `txInstant`, `op` is only allowed on one attribute
    expectCompileError(
      "m(Ns.str.txT.op.int.txT.op)",
      "[Dsl2Model:apply (16)] Generics (`v`, `tx`, `txT`, `txInstant`, `op`) not allowed on multiple attributes")
    ok
  }


  "Entity history" >> {

    // Instead of building a history of an entity with multiple fixed attributes
    // as a molecule we can also look for _any_ attribute involved in an entity's history:

    // All attribute assertions/retractions of entity e1
    Ns(e1).a.v.txT.op.getHistory.toSeq.sortBy(t => (t._3, t._4)) === List(
      (":ns/str", "a", t1, true),
      (":ns/int", 1, t1, true),
      (":ns/str", "a", t2, false),
      (":ns/str", "b", t2, true),
      (":ns/int", 1, t3, false),
      (":ns/int", 2, t3, true)
    )

    // All attribute assertions of entity e1
    Ns(e1).a.v.txT.op(true).getHistory.toSeq.sortBy(t => (t._3, t._4)) === List(
      (":ns/str", "a", t1, true),
      (":ns/int", 1, t1, true),
      (":ns/str", "b", t2, true),
      (":ns/int", 2, t3, true)
    )

    // All attribute retractions of entity e1
    Ns(e1).a.v.txT.op(false).getHistory.toSeq.sortBy(t => (t._3, t._4)) === List(
      (":ns/str", "a", t2, false),
      (":ns/int", 1, t3, false)
    )

    // All attribute assertions/retractions of entity e1 at t2
    Ns(e1).a.v.txT(t2).op.getHistory.toSeq.sortBy(t => t._4) === List(
      // str value was updated from "a" to "b"
      (":ns/str", "a", t2, false),
      (":ns/str", "b", t2, true)
    )

    // All attribute retractions of entity e1 at t2
    Ns(e1).a.v.txT(t2).op(false).getHistory.toSeq.sortBy(t => (t._3, t._4)) === List(
      // str value "a" was retracted at t2
      (":ns/str", "a", t2, false)
    )

    // All attribute assertions of entity e1 at t2
    Ns(e1).a.v.txT(t2).op(true).getHistory.toSeq.sortBy(t => (t._1, t._3)) === List(
      // str value "b" was asserted at t2
      (":ns/str", "b", t2, true)
    )
  }


  "Tacet generic attrs" >> {

    // Transaction dates
    val date2 = tx2.inst
    val date3 = tx3.inst
    val date5 = tx5.inst

    // Entities with retractions
    Ns.e.a.v.txT.op_(false).getHistory.toSeq.sortBy(t => (t._1, t._4)) === List(
      (e1, ":ns/str", "a", t2),
      (e1, ":ns/int", 1, t3),
      (e2, ":ns/int", 4, t5)
    )

    Ns.e.a.v.txInstant.op_(false).getHistory.toSeq.sortBy(t => (t._1, t._4)) === List(
      (e1, ":ns/str", "a", date2),
      (e1, ":ns/int", 1, date3),
      (e2, ":ns/int", 4, date5)
    )

    // Entities involved in transaction t2
    // Note how the transaction itself is included
    Ns.e.a.v.txT_(t2).op.getHistory.toSeq.sortBy(t => (t._1, t._4)) === List(
      (tx2.tx, ":db/txInstant", tx2.inst, true), // tx instant
      (e1, ":ns/str", "a", false),
      (e1, ":ns/str", "b", true)
    )

    // Using transaction date
    // Entities involved in transaction as of date2
    Ns.e.a.v.txInstant_(date2).op.getHistory.toSeq.sortBy(t => (t._1, t._4)) === List(
      (tx2.tx, ":db/txInstant", tx2.inst, true), // tx instant
      (e1, ":ns/str", "a", false),
      (e1, ":ns/str", "b", true)
    )
  }
}