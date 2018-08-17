package molecule.coretests.transaction
import molecule.api._
import molecule.coretests.util.CoreSetup
import molecule.coretests.util.dsl.coreTest.Ns
import molecule.util.expectCompileError
import org.specs2.mutable.Specification


class TransactionData extends Specification {


  "2 attr + 1 op" in new CoreSetup {

    val result = Ns.str("Fred").int(1).save
    val eid    = result.eid
    val t1     = result.t
    Ns(eid).int.get.head === 1

    val t2 = Ns(eid).int(2).update.t
    Ns(eid).int.get.head === 2

    Ns(eid).int.getAsOf(t1).head === 1
    Ns(eid).int.getAsOf(t2).head === 2

    Ns(eid).int.getHistory === List(1, 2)
    Ns(eid).int.op_(false).getHistory === List(1)

    Ns.str.int.op_(false).getHistory === List(
      ("Fred", 1))


    Ns(eid).int.op.getHistory.sortBy(t => (t._1, !t._2)) === List(
      (1, true),
      (1, false),
      (2, true)
    )

    Ns.str.int.op.getHistory.sortBy(t => (t._2, !t._3)) === List(
      ("Fred", 1, true),
      ("Fred", 1, false),
      ("Fred", 2, true)
    )

    Ns.int.op.str.getHistory.sortBy(t => (t._1, !t._2)) === List(
      (1, true, "Fred"),
      (1, false, "Fred"),
      (2, true, "Fred")
    )

    Ns.int.str.op.getHistory.sortBy(_._1) === List(
      (1, "Fred", true),
      (2, "Fred", true)
    )

    Ns.str.op.int.getHistory.sortBy(_._1) === List(
      ("Fred", true, 1),
      ("Fred", true, 2)
    )


    Ns.str.int.op_(false).getHistory === List(
      ("Fred", 1)
    )

    Ns.str("marc").bool(true).save

    Ns.int.op_(false).getHistory === List(1)
    Ns.int.op_(true).getHistory === List(1, 2)

    Ns(eid).int.op.getHistory.sortBy(t => (t._1, !t._2)) === List(
      (1, true),
      (1, false),
      (2, true)
    )
  }


  "Multiple tx attributes" in new CoreSetup {
    val result1 = Ns.str("Fred").save
    val tx1     = result1.tx
    val eid     = result1.eid

    val tx2 = Ns(eid).int(38).update.tx

    // Tacit attributes can be followed by generic attributes
    Ns(eid).str_.tx.get.head === tx1
    Ns(eid).int_.tx.get.head === tx2

    Ns(eid).str_.tx.int_.tx.get.head === (tx1, tx2)
  }


  "Optional tx data not allowed" in new CoreSetup {
    expectCompileError(
      """m(Ns.int$.tx.str)""",
      "[Dsl2Model:dslStructure] Optional attributes (`int$`) can't be followed by generic attributes (`tx`).")

    expectCompileError(
      """m(Ns.int$.t.str)""",
      "[Dsl2Model:dslStructure] Optional attributes (`int$`) can't be followed by generic attributes (`t`).")

    expectCompileError(
      """m(Ns.int$.txInstant.str)""",
      "[Dsl2Model:dslStructure] Optional attributes (`int$`) can't be followed by generic attributes (`txInstant`).")

    expectCompileError(
      """m(Ns.int$.op.str)""",
      "[Dsl2Model:dslStructure] Optional attributes (`int$`) can't be followed by generic attributes (`op`).")
  }
}
