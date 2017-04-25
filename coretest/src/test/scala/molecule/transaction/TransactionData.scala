package molecule.transaction

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class TransactionData extends CoreSpec {


  "history" in new CoreSetup {

    val result = Ns.str("Fred").int(25).tx_(Ref1.str1_("Marc").int1_(1)).save
    val eid    = result.eid
    val t1     = result.t
    Ns(eid).int.one === 25

    val t2 = Ns(eid).int(26).tx_(Ref1.str1_("Marc").int1_(2)).update.t
    Ns(eid).int.one === 26

    Ns(eid).int.asOf(t1).one === 25
    Ns(eid).int.asOf(t2).one === 26

    Ns(eid).int.history.get === List(25, 26)
    Ns(eid).int.op_(false).history.get === List(25)

    Ns.str.int.op_(false).history.get === List(
      ("Fred", 25))


    Ns(eid).int.op.history.get.sortBy(_._1) === List(
      (25, true),
      (25, false),
      (26, true)
    )

    Ns.str.int.op.history.get.sortBy(_._2) === List(
      ("Fred", 25, true),
      ("Fred", 25, false),
      ("Fred", 26, true)
    )

    Ns.int.op.str.history.get.sortBy(_._1) === List(
      (25, true, "Fred"),
      (25, false, "Fred"),
      (26, true, "Fred")
    )

    Ns.int.str.op.history.get.sortBy(_._1) === List(
      (25, "Fred", true),
      (26, "Fred", true)
    )

    Ns.str.op.int.history.get.sortBy(_._1) === List(
      ("Fred", true, 25),
      ("Fred", true, 26)
    )


    Ns.str.int.op_(false).history.get === List(
      ("Fred", 25)
    )

    Ns.str("marc").bool(true).save

    Ns.int.op_(false).history.get === List(25)
    Ns.int.op_(true).history.get === List(25, 26)

    Ns(eid).int.op.history.get.sortBy(_._1) === List(
      (25, true),
      (25, false),
      (26, true)
    )
  }


  "imagine" in new CoreSetup {

    val fred = Ns.str("Fred").int(42).save.eid

    Ns.str.int.get === List(("Fred", 42))

    Ns.str.int.imagine(
      Ns(fred).int(43).updateTx
    ).get === List(("Fred", 43))

    Ns.str.int.get === List(("Fred", 42))

    Ns.str.int.imagine(
      Ns.str("John").int(44).saveTx
    ).get.sorted === List(
      ("Fred", 42), // production value
      ("John", 44) // insertion worked
    )

    Ns.str.int.get === List(("Fred", 42))


    Ns.str.int.imagine(
      Ns.str("John").int(44).saveTx,
      Ns.str.int insertTx List(
        ("Lisa", 23),
        ("Pete", 24)
      ),
      Ns(fred).int(43).updateTx
    ).get.sorted === List(
      ("Fred", 43), // Updated
      ("John", 44), // Saved
      ("Lisa", 23), // Inserted
      ("Pete", 24) // Inserted
    )

    val saveJohn      = Ns.str("John").int(44).saveTx
    val insertMembers = Ns.str.int insertTx List(("Lisa", 23), ("Pete", 24))
    val updateFred    = Ns(fred).int(43).updateTx

    Ns.str.int.imagine(
      saveJohn,
      insertMembers,
      updateFred
    ).get.sorted === List(
      ("Fred", 43), // Updated
      ("John", 44), // Saved
      ("Lisa", 23), // Inserted
      ("Pete", 24) // Inserted
    )
  }
}