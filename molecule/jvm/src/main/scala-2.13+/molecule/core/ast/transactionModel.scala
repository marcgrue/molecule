package molecule.core.ast

import java.util.{List => jList}
import clojure.lang.Keyword
import molecule.core.ast.model._
import molecule.core.util.JavaUtil
import scala.jdk.CollectionConverters._
import datomic.Util.read


/** Datomic transaction representation and operations. */
object transactionModel extends JavaUtil {

  /** Transaction statement. */
  sealed trait Statement {
    val action: String
    val e     : Any
    val a     : String
    val v     : Any
    val gv    : GenericValue
    val oldV: Any = null

    def value(v: Any): Object = (v match {
      case i: Int             => i.toLong
      case f: Float           => f.toDouble
      case bigInt: BigInt     => bigInt.bigInteger
      case bigDec: BigDecimal => bigDec.bigDecimal
      //      case kw: Keyword        => kw
      case other => other
    }).asInstanceOf[Object]

    // Convert actions and attribute names to Clojure Keywords
    def toJava: jList[_] = this match {
      case _: RetractEntity => Util.list(read(action), e.asInstanceOf[Object])
      case _: Cas           => Util.list(read(action), e.asInstanceOf[Object], read(a), value(oldV), value(v))
      case _                => Util.list(read(action), e.asInstanceOf[Object], read(a), value(v))
    }
  }

  private def eid(e: Any): String = {
    val e1  = if (e.isInstanceOf[Long]) s"${e}L" else e.toString
    val pad = " " * (8 - e1.length)
    e1 + pad
  }

  case class Add(e: Any, a: String, v: Any, gv: GenericValue) extends Statement {
    val action = ":db/add"
    override def toString: String = {
      val pad = " " * (25 - a.length)
      if (v.isInstanceOf[AbstractValue])
        s"""List(":db/add",     ${eid(e)}, "$a", $pad $v, $gv)"""
      else
        s"""list(":db/add",     ${eid(e)}, "$a", $pad $v)"""
    }
  }

  // Todo: Implement in updates?
  // Current Add's let Datomic automatically create a retract datom for each new update.
  case class Cas(e: Any, a: String, override val oldV: Any, v: Any, gv: GenericValue) extends Statement {
    val action = ":db.fn/cas"
  }

  case class Retract(e: Any, a: String, v: Any, gv: GenericValue = NoValue) extends Statement {
    val action = ":db/retract"
    override def toString: String = {
      val pad = " " * (25 - a.length)
      if (v.isInstanceOf[AbstractValue])
        s"""List(":db/retract", ${eid(e)}, "$a", $pad $v, $gv)"""
      else
        s"""list(":db/retract", ${eid(e)}, "$a", $pad $v)"""
    }
  }

  case class RetractEntity(e: Any) extends Statement {
    val action = ":db/retractEntity"
    val a      = ""
    val v      = ""
    val gv     = NoValue

    override def toString: String = {
      s"""list(":db/retractEntity", ${eid(e)})"""
    }
  }

  trait AbstractValue
  case class Eid(id: Long) extends AbstractValue
  case class Eids(ids: Seq[Any]) extends AbstractValue
  case class Prefix(s: String) extends AbstractValue
  case class Values(vs: Any, prefix: Option[String] = None) extends AbstractValue


  def toJava(stmtss: Seq[Seq[Statement]]): jList[jList[_]] = stmtss.flatten.map {
    case Add(e, a, i: Int, bi)             => Add(e, a, i.toLong: java.lang.Long, bi).toJava
    case Add(e, a, f: Float, bi)           => Add(e, a, f.toDouble: java.lang.Double, bi).toJava
    case Add(e, a, bigInt: BigInt, bi)     => Add(e, a, bigInt.bigInteger, bi).toJava
    case Add(e, a, bigDec: BigDecimal, bi) => Add(e, a, bigDec.bigDecimal, bi).toJava
    case other                             => other.toJava
  }.asJava
}
