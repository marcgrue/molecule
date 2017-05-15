package molecule.transaction
import molecule._
import molecule.util.dsl.coreTest.Ns
import molecule.util.schema.CoreTestSchema
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class TransactionData extends Specification {

  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)



  "2 attr + 1 op" >> {

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


    Ns(eid).int.op.getHistory.toSeq.sortBy(t => (t._1, !t._2)) === List(
      (1, true),
      (1, false),
      (2, true)
    )

    Ns.str.int.op.getHistory.toSeq.sortBy(t => (t._2, !t._3)) === List(
      ("Fred", 1, true),
      ("Fred", 1, false),
      ("Fred", 2, true)
    )

    Ns.int.op.str.getHistory.toSeq.sortBy(t => (t._1, !t._2)) === List(
      (1, true, "Fred"),
      (1, false, "Fred"),
      (2, true, "Fred")
    )

    Ns.int.str.op.getHistory.toSeq.sortBy(_._1) === List(
      (1, "Fred", true),
      (2, "Fred", true)
    )

    Ns.str.op.int.getHistory.toSeq.sortBy(_._1) === List(
      ("Fred", true, 1),
      ("Fred", true, 2)
    )


    Ns.str.int.op_(false).getHistory === List(
      ("Fred", 1)
    )

    Ns.str("marc").bool(true).save

    Ns.int.op_(false).getHistory === List(1)
    Ns.int.op_(true).getHistory === List(1, 2)

    Ns(eid).int.op.getHistory.toSeq.sortBy(t => (t._1, !t._2)) === List(
      (1, true),
      (1, false),
      (2, true)
    )
  }

}
