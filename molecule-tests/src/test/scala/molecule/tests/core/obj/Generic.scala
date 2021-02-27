package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.datomic.api.in1_out18._
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Generic extends TestSpec with Helpers {

  class Setup extends CoreSetup {
    val txR       = Ns.int(1).save
    val e         = txR.eid
    val t         = txR.t
    val tx        = txR.tx
    val txInstant = txR.inst

    val txR2       = Ns(e).int(2).update
    val t2         = txR2.t
    val tx2        = txR2.tx
    val txInstant2 = txR2.inst

    val txR3       = Ns(e).str("a").update
    val t3         = txR3.t
    val tx3        = txR3.tx
    val txInstant3 = txR3.inst
  }

  "Datom" in new Setup {
    // Current datom values
    val d1 = Ns(e).int.e.a.v.t.tx.txInstant.getObj
    d1.int === 2
    d1.e === e
    d1.a === ":Ns/int"
    d1.v === 2
    d1.t === t2 // last transaction involving `int`
    d1.tx === tx2
    d1.txInstant === txInstant2

    val d2 = Ns(e).str.e.a.v.t.tx.txInstant.getObj
    d2.str === "a"
    d2.e === e
    d2.a === ":Ns/str"
    d2.v === "a"
    d2.t === t3 // last transaction involving `str`
    d2.tx === tx3
    d2.txInstant === txInstant3
  }


  "Log" in new Setup {
    // Transaction t until t2 (not inclusive) - meaning only the first transaction
    val List(txDatom, intDatom) = Log(Some(t), Some(t2)).e.a.v.t.tx.txInstant.op.getObjList

    // Transaction datom
    txDatom.e === tx
    txDatom.a === ":db/txInstant"
    txDatom.v === txInstant
    txDatom.t === t
    txDatom.tx === tx
    txDatom.txInstant === txInstant
    txDatom.op === true

    intDatom.e === e
    intDatom.a === ":Ns/int"
    intDatom.v === 1
    intDatom.t === t
    intDatom.tx === tx
    intDatom.txInstant === txInstant
    intDatom.op === true
  }


  "Schema" in new Setup {
    // :Ns/int attribute
    val o1 = Schema
      .part.id.a(":Ns/int").nsFull.ns.attr.tpe.card.doc
      .index$.unique$.fulltext$.isComponent$.noHistory$.enum$
      .t.tx.txInstant.getObj

    o1.part === "db.part/user"
    o1.id === 73
    o1.a === ":Ns/int"
    o1.nsFull === "Ns"
    o1.ns === "Ns"
    o1.attr === "int"
    o1.tpe === "long"
    o1.card === "one"
    o1.doc === "Card one Int attribute"
    o1.index$ === Some(true)
    o1.unique$ === None
    o1.fulltext$ === None
    o1.isComponent$ === None
    o1.noHistory$ === None
    o1.enum$ === None
    o1.t // t of creation transaction
    o1.tx // tx of creation transaction
    o1.txInstant // txInstant of creation transaction
  }


  "EAVT" in new Setup {
    // Entity `e`
    val List(d1, d2) = EAVT(e).e.a.v.t.tx.txInstant.op.getObjList

    d1.e === e
    d1.a === ":Ns/str"
    d1.v === "a"
    d1.t === t3 // last transaction involving `str`
    d1.tx === tx3
    d1.txInstant === txInstant3
    d1.op === true

    d2.e === e
    d2.a === ":Ns/int"
    d2.v === 2
    d2.t === t2 // last transaction involving `int`
    d2.tx === tx2
    d2.txInstant === txInstant2
    d2.op === true
  }

  "AEVT" in new Setup {
    val txR4       = Ns.int(4).save
    val e4         = txR4.eid
    val t4         = txR4.t
    val tx4        = txR4.tx
    val txInstant4 = txR4.inst

    // :Ns/int attribute
    val List(d1, d2) = AEVT(":Ns/int").e.a.v.t.tx.txInstant.op.getObjList

    d1.e === e
    d1.a === ":Ns/int"
    d1.v === 2
    d1.t === t2
    d1.tx === tx2
    d1.txInstant === txInstant2
    d1.op === true

    d2.e === e4
    d2.a === ":Ns/int"
    d2.v === 4
    d2.t === t4
    d2.tx === tx4
    d2.txInstant === txInstant4
    d2.op === true
  }

  "AVET" in new Setup {
    // :Ns/int / value 2
    val List(d1) = AVET(":Ns/int", 2).e.a.v.t.tx.txInstant.op.getObjList

    d1.e === e
    d1.a === ":Ns/int"
    d1.v === 2
    d1.t === t2
    d1.tx === tx2
    d1.txInstant === txInstant2
    d1.op === true
  }

  "VAET" in new Setup {
    val ref        = Ref1.int1(11).save.eid
    val txR4       = Ns(e).ref1(ref).update
    val t4         = txR4.t
    val tx4        = txR4.tx
    val txInstant4 = txR4.inst

    // Reference
    val List(d1) = VAET(ref).e.a.v.t.tx.txInstant.op.getObjList

    d1.e === e
    d1.a === ":Ns/ref1"
    d1.v === ref
    d1.t === t4
    d1.tx === tx4
    d1.txInstant === txInstant4
    d1.op === true
  }
}
