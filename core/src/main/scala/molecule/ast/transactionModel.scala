package molecule.ast
import java.util.{List => jList, Map => jMap}

import datomic.Util
import molecule.ast.model._

import scala.collection.JavaConverters._

/** Datomic transaction representation and operations. */
object transactionModel {

  /** Transaction statement. */
  sealed trait Statement {
    val action: String
    val e     : Any
    val a     : String
    val v     : Any
    val bi    : Generic
    val oldV: Any = null

    def value(v: Any): Object = (v match {
      case i: Int             => i.toLong
      case f: Float           => f.toDouble
      case bigInt: BigInt     => bigInt.bigInteger
      case bigDec: BigDecimal => bigDec.bigDecimal
      case other              => other
    }).asInstanceOf[Object]

    def toJava: jList[_] = this match {
      case _: RetractEntity => Util.list(action, e.asInstanceOf[Object])
      case _: Cas           => Util.list(action, e.asInstanceOf[Object], a.asInstanceOf[Object], value(oldV), value(v))
      case _                => Util.list(action, e.asInstanceOf[Object], a.asInstanceOf[Object], value(v))
    }
  }

  case class Add(e: Any, a: String, v: Any, bi: Generic) extends Statement {
    val action = ":db/add"
  }

  // Todo: Implement in updates?
  // Current Add's let Datomic automatically create a retract datom for each new update.
  case class Cas(e: Any, a: String, override val oldV: Any, v: Any, bi: Generic) extends Statement {
    val action = ":db.fn/cas"
  }

  case class Retract(e: Any, a: String, v: Any, bi: Generic = NoValue) extends Statement {
    val action = ":db/retract"
  }

  case class RetractEntity(e: Any) extends Statement {
    val action = ":db.fn/retractEntity"
    val a      = ""
    val v      = ""
    val bi     = NoValue
  }

  case class Eid(id: Long)
  case class Eids(ids: Seq[Any])
  case class Prefix(s: String)
  case class Values(vs: Any, prefix: Option[String] = None)


  def toJava(stmtss: Seq[Seq[Statement]]): jList[jList[_]] = stmtss.flatten.map {
    case Add(e, a, i: Int, bi)             => Add(e, a, i.toLong: java.lang.Long, bi).toJava
    case Add(e, a, f: Float, bi)           => Add(e, a, f.toDouble: java.lang.Double, bi).toJava
    case Add(e, a, bigInt: BigInt, bi)     => Add(e, a, bigInt.bigInteger, bi).toJava
    case Add(e, a, bigDec: BigDecimal, bi) => Add(e, a, bigDec.bigDecimal, bi).toJava
    case other                             => other.toJava
  }.asJava
}
