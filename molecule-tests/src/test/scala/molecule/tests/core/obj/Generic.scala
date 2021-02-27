package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.datomic.api.in1_out12._
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

    val txR3       = Ns(e).int(3).update
    val t3         = txR3.t
    val tx3        = txR3.tx
    val txInstant3 = txR3.inst
  }

  "Datom" in new Setup {
    // Current datom values
    val o = Ns(e).int.e.a.v.t.tx.txInstant.getObj
    o.int === 3
    o.e === e
    o.a === ":Ns/int"
    o.v === 3
    o.t === t3
    o.tx === tx3
    o.txInstant === txInstant3
  }


  "Log" in new Setup {

    // Transaction t until t2 (not inclusive) - meaning only the first transaction
    Log(Some(t), Some(t2)).t.e.a.v.op.get === List(
      (t, tx, ":db/txInstant", txInstant, true),
      (t, e, ":Ns/int", 1, true)
    )

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


    Schema.part.id.a.inspectGet

    Schema.part.id.a.inspectGet

    Schema.part.id.a.nsFull.inspectGet
    Schema.part.id.a.nsFull.ns.inspectGet
    Schema.part.id.a.nsFull.ns.attr.tpe.card.doc.inspectGet

    val o1 = Schema.part.id.a.nsFull.ns.attr.tpe.card.doc.getObj
    o1.part === "db.part/user"
    o1.id ===  75
    o1.a ===  ""
    o1.part ===   ""
    o1.nsFull === ""
    o1.ns === ""
    o1.attr ===   ""
    o1.tpe === ""
    o1.card ===   1
    o1.doc === ""

    Schema.index.getObj.index === true
    Schema.unique.getObj.unique === true
    Schema.fulltext.getObj.fulltext === true
    Schema.isComponent.getObj.isComponent === true
    Schema.noHistory.getObj.noHistory === true
    Schema.enum.getObj.enum === true
    Schema.t.getObj.t === 11
    Schema.tx.getObj.tx === 11
    Schema.txInstant.getObj.txInstant === true






  }

}
