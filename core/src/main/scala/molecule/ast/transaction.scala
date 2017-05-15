package molecule.ast
import datomic.Util
import molecule.ast.model._
import java.util.{List => jList, Map => jMap}

import scala.collection.JavaConverters._

object transaction {

  sealed trait Statement {
    val action: String
    val e     : Any
    val a     : String
    val v     : Any
    val bi    : Generic

    def toJava = if (action == ":db.fn/retractEntity")
      Util.list(action, e.asInstanceOf[Object])
    else {
      val v2 = v match {
          case i: Int             => i.toLong
          case f: Float           => f.toDouble
          case bigInt: BigInt     => bigInt.bigInteger
          case bigDec: BigDecimal => bigDec.bigDecimal
          //    case Add(e, a, d: Date)  => Add(e, a, d).toJava
          //    case Add(e, a, v: UUID)  => Add(e, a, v).toJava
          //    case Add(e, a, v: URI)   => Add(e, a, v).toJava
          case other => other
        }
      Util.list(action, e.asInstanceOf[Object], a.asInstanceOf[Object], v2.asInstanceOf[Object])

//      Util.list(action, e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object])
    }
  }

  case class Add(e: Any, a: String, v: Any, bi: Generic) extends Statement {
    val action = ":db/add"
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
    case Add(e, a, i: Int, meta)             => Add(e, a, i.toLong: java.lang.Long, meta).toJava
    case Add(e, a, f: Float, meta)           => Add(e, a, f.toDouble: java.lang.Double, meta).toJava
    case Add(e, a, bigInt: BigInt, meta)     => Add(e, a, bigInt.bigInteger, meta).toJava
    case Add(e, a, bigDec: BigDecimal, meta) => Add(e, a, bigDec.bigDecimal, meta).toJava
    //    case Add(e, a, d: Date)  => Add(e, a, d).toJava
    //    case Add(e, a, v: UUID)  => Add(e, a, v).toJava
    //    case Add(e, a, v: URI)   => Add(e, a, v).toJava
    case other => other.toJava
  }.asJava
}
