package molecule.coretests.time

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out1._
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import molecule.datomic.peer.facade.Datomic_Peer._

class GetSince extends CoreSpec {


  "List" >> {
    "Appended" in new CoreSetup {

      val tx1 = Ns.int(1).save
      val tx2 = Ns.int(2).save
      val tx3 = Ns.int(3).save

      // Current values
      Ns.int.get === List(1, 2, 3)

      // Exclussive tx1 value
      Ns.int.getSince(tx1) === List(2, 3)
      Ns.int.getSince(tx2) === List(3)
      Ns.int.getSince(tx3) === List()
    }


    "Appended and updated" in new CoreSetup {

      val tx1 = Ns.int(1).save
      val tx2 = Ns.int(2).save
      val tx3 = Ns.int(3).save

      val e2  = tx2.eid
      val tx4 = Ns(e2).int(4).update

      // Current values
      Ns.int.get === List(1, 3, 4)

      Ns.int.getSince(tx1) === List(3, 4)
      Ns.int.getSince(tx2) === List(3, 4)
      Ns.int.getSince(tx3) === List(4)
      Ns.int.getSince(tx4) === List()
    }


    "Appended and retracted" in new CoreSetup {

      val tx1 = Ns.int(1).save
      val tx2 = Ns.int(2).save
      val tx3 = Ns.int(3).save
      val tx4 = Ns.int(4).save
      val tx5 = tx2.eid.retract

      // Current values
      Ns.int.get === List(1, 3, 4)

      Ns.int.getSince(tx1) === List(3, 4)
      Ns.int.getSince(tx2) === List(3, 4)
      Ns.int.getSince(tx3) === List(4)
      Ns.int.getSince(tx4) === List()
    }
  }


  "Iterable" >> {

    "t" in new CoreSetup {

      // 3 transaction times `t`
      val t1 = Ns.str("Ann").save.t
      val t2 = Ns.str("Ben").save.t
      val t3 = Ns.str("Cay").save.t

      // Current values as Iterable
      Ns.str.getIterable.iterator.toList === List("Ann", "Ben", "Cay")

      // Ben and Cay added since transaction time t1
      Ns.str.getIterableSince(t1).iterator.toList === List("Ben", "Cay")

      // Cay added since transaction time t2
      Ns.str.getIterableSince(t2).iterator.toList === List("Cay")

      // Nothing added since transaction time t3
      Ns.str.getIterableSince(t3).iterator.toList === Nil
    }


    "tx report" in new CoreSetup {

      // Get tx reports for 3 transactions
      val tx1 = Ns.str("Ann").save
      val tx2 = Ns.str("Ben").save
      val tx3 = Ns.str("Cay").save

      // Current values
      Ns.str.getIterable.iterator.toList === List("Ann", "Ben", "Cay")

      // Ben and Cay added since tx1
      Ns.str.getIterableSince(tx1).iterator.toList === List("Ben", "Cay")

      // Cay added since tx2
      Ns.str.getIterableSince(tx2).iterator.toList === List("Cay")

      // Nothing added since tx3
      Ns.str.getIterableSince(tx3).iterator.toList === Nil
    }


    "date" in new CoreSetup {

      // Transact 3 times (`inst` retrieves transaction time from tx report)
      val date1 = Ns.str("Ann").save.inst
      val date2 = Ns.str("Ben").save.inst
      val date3 = Ns.str("Cay").save.inst

      // Current values
      Ns.str.getIterable.iterator.toList === List("Ann", "Ben", "Cay")

      // Ben and Cay added since human time 1
      Ns.str.getIterableSince(date1).iterator.toList === List("Ben", "Cay")

      // Cay added since human time 2
      Ns.str.getIterableSince(date2).iterator.toList === List("Cay")

      // Nothing added since human time 3
      Ns.str.getIterableSince(date3).iterator.toList === Nil
    }
  }


  "Raw" >> {

    "t" in new CoreSetup {

      // 3 transaction times `t`
      val t1 = Ns.str("Ann").save.t
      val t2 = Ns.str("Ben").save.t
      val t3 = Ns.str("Cay").save.t

      // Current values as Iterable
      Ns.str.get === List("Ann", "Ben", "Cay")

      // Ben and Cay added since transaction time t1
      val raw1: java.util.Collection[java.util.List[AnyRef]] = Ns.str.getRawSince(t1)
      raw1.toString === """[["Ben"], ["Cay"]]"""

      // Cay added since transaction time t2
      Ns.str.getRawSince(t2).toString === """[["Cay"]]"""

      // Nothing added since transaction time t3
      Ns.str.getRawSince(t3).toString === """[]"""
    }


    "tx report" in new CoreSetup {

      // Get tx reports for 3 transactions
      val tx1 = Ns.str("Ann").save
      val tx2 = Ns.str("Ben").save
      val tx3 = Ns.str("Cay").save

      // Current values
      Ns.str.get === List("Ann", "Ben", "Cay")

      // Ben and Cay added since tx1
      val raw1: java.util.Collection[java.util.List[AnyRef]] = Ns.str.getRawSince(tx1)
      raw1.toString === """[["Ben"], ["Cay"]]"""

      // Cay added since tx2
      Ns.str.getRawSince(tx2).toString === """[["Cay"]]"""

      // Nothing added since tx3
      Ns.str.getRawSince(tx3).toString === """[]"""
    }


    "date" in new CoreSetup {

      // Transact 3 times (`inst` retrieves transaction time from tx report)
      val date1 = Ns.str("Ann").save.inst
      val date2 = Ns.str("Ben").save.inst
      val date3 = Ns.str("Cay").save.inst

      // Current values
      Ns.str.getIterable.iterator.toList === List("Ann", "Ben", "Cay")

      // Ben and Cay added since human time 1
      Ns.str.getIterableSince(date1).iterator.toList === List("Ben", "Cay")

      // Cay added since human time 2
      Ns.str.getIterableSince(date2).iterator.toList === List("Cay")

      // Nothing added since human time 3
      Ns.str.getIterableSince(date3).iterator.toList === Nil
    }
  }
}