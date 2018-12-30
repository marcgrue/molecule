package molecule.coretests

import java.lang
import java.util.Date
import datomic.Datom
import molecule.api.in3_out22._
import molecule.ast.model._
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.facade.{Conn, TxReport}
import molecule.transform.Model2Query
import scala.collection.JavaConverters._


class AdHocTest extends CoreSpec {


  //  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)
  //  implicit val conn = recreateDbFrom(PartitionTestSchema)


//      final class Datom(val underlying: datomic.Datom) extends AnyVal {
//        def e: Long = underlying.e.asInstanceOf[Long]
//        def attrId: Int = underlying.a.asInstanceOf[Integer]
//        def ns: String = underlying.a.asInstanceOf[String]
//        def ident: String = underlying.a.asInstanceOf[String]
//        def a: String = underlying.a.asInstanceOf[String]
//  //      def v: Any = Convert.toScala(underlying.v)
//        def tx: Long = underlying.tx.asInstanceOf[Long]
//        def op: Boolean = underlying.added
//      }
//
//  //    object Datom {
//  //      def unapply(da: Datom): Option[(Long, Int, Any, Long, Boolean)] =
//  //        Some((da.e, da.a, da.v, da.tx, da.op))
//  //    }
//
//      object EAVT {
//        def apply(e: Long)(implicit conn: Conn): Iterable[Datom] = new Iterable[Datom] {
//          private val jIterable = conn.db.datoms(datomic.Database.EAVT, e.asInstanceOf[Object])
//          override def iterator = new Iterator[Datom] {
//            private val jIter = jIterable.iterator
//            override def hasNext = jIter.hasNext
//            override def next() = new Datom(jIter.next())
//          }
//          override def equals(arg: Any) = jIterable.equals(arg)
//          override def hashCode = jIterable.hashCode
//          override def toString = s"${classOf[datomic.Database].getName}.datoms.Iterable@${Integer.toHexString(jIterable.hashCode)}"
//        }
//
//        def e: Long =
//      }


  //  "adhoc" in new CoreSetup {
  "adhoc" >> {


    // First entity

    val txR1 = Ns.str("a").int(1).save
    val tx1 = txR1.tx
    val e1 = txR1.eid
    val t1 = txR1.t
    val d1 = txR1.inst

    val txR2 = Ns(e1).str("b").update
    val tx2 = txR2.tx
    val t2 = txR2.t
    val d2 = txR2.inst

    val txR3 = Ns(e1).int(2).update
    val tx3 = txR3.tx
    val t3 = txR3.t
    val d3 = txR3.inst


    // Second entity

    val txR4 = Ns.str("x").int(4).save
    val tx4 = txR4.tx
    val e2 = txR4.eid
    val t4 = txR4.t
    val d4 = txR4.inst

    val txR5 = Ns(e2).int(5).update
    val tx5 = txR5.tx
    val t5 = txR5.t
    val d5 = txR5.inst


    // Third entity, a ref

    val txR6 = Ref1.str1("hello").save
    val r1 = txR6.eid
    val tx6 = txR6.tx
    val t6 = txR6.t
    val d6 = txR6.inst

    val txR7 = Ns(e2).ref1(r1).update
    val tx7 = txR7.tx
    val t7 = txR7.t
    val d7 = txR7.inst
//
//    println("e1: " + e1)
//    println("e2: " + e2)
//    println(Seq(1, tx1, t1).mkString("   "))
//    println(Seq(2, tx2, t2).mkString("   "))
//    println(Seq(3, tx3, t3).mkString("   "))
//    println(Seq(4, tx4, t4).mkString("   "))
//    println(Seq(5, tx5, t5).mkString("   "))
//    println(Seq(6, tx6, t6).mkString("   "))
//    println(Seq(7, tx7, t7).mkString("   "))
//



//    EAVT.e.a.v.t.tx.txInstant.op.getIterable
//    EAVT(e1).e.a.v.t.tx.txInstant.op.getIterable
//    EAVT(e1, "str").e.a.v.t.tx.txInstant.op.getIterable
//    EAVT(e1, "str", "hejsa").e.a.v.t.tx.txInstant.op.getIterable
//    EAVT(e1, "str", "hejsa", 1031).e.a.v.t.tx.txInstant.op.getIterable
//    EAVT(e1, "str", "hejsa", tx7).e.a.v.t.tx.txInstant.op.getIterable
//    EAVT(e1, "str", "hejsa", d7).e.a.v.t.tx.txInstant.op.getIterable
//
//    val b = AEVT("str").e.a.v.t.tx.txInstant.op
//
//    m(Schema.a).get

//    conn.db.datoms(datomic.Database.EAVT, Set(17592186045445L).asInstanceOf[Object])
    conn.db.datoms(datomic.Database.EAVT, e1.asInstanceOf[Object])
      .forEach(d =>
//         println(d.a.asInstanceOf[java.lang.Integer] + "   "   + conn.db.ident( d.a.asInstanceOf[java.lang.Integer]))
        println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]")
      )
    println("------------")
    Ns(e1).e.a.v.tx.op.debugGet
//    EAVT(e1).e.a.v.tx.op.debugGet
//    EAVT(e1).e.a.v.tx.op.getIterable

//    conn.db.datoms(datomic.Database.EAVT, ":ns/str")
//      .forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
//    println("------------")
//
//    //    conn.db.datoms(datomic.Database.EAVT, true.asInstanceOf[Object])
//    conn.db.datoms(datomic.Database.EAVT, 40.asInstanceOf[Object])
//      .forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
//    println("------------")

    //    conn.db.datoms(datomic.Database.EAVT, e1.asInstanceOf[Object]).forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //    conn.db.datoms(datomic.Database.EAVT).forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //
    //    println("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§")
    //
    //    conn.db.datoms(datomic.Database.AEVT, ":ns/int").forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //
    //    println("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§")
    //
    //    conn.db.datoms(datomic.Database.AVET, ":ns/int").forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //
    //
    //    conn.db.datoms(datomic.Database.VAET).forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    //
    //    conn.datomicConn.log.txRange(1028, null).iterator().asScala.toList.foreach { map =>
    conn.datomicConn.log.txRange(17592186045445L, null).iterator().asScala.toList.foreach { map =>
      val it = map.values().iterator()
      val t = it.next
      val datoms = it.next.asInstanceOf[java.util.Collection[datomic.Datom]]
      println(t)
      datoms.forEach(d => println(s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"))
    }

    ok
  }

}