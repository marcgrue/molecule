package molecule.ast
import datomic.Util
import molecule.ast.model._

object transaction {

  sealed trait Statement {
    val action: String
    val e     : Any
    val a     : String
    val v     : Any
    val bi    : Generic
    def toJava = Util.list(action, e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object])
  }

  case class Add(e: Any, a: String, v: Any, bi: Generic = NoValue) extends Statement {
    val action = ":db/add"
  }

  case class Retract(e: Any, a: String, v: Any, bi: Generic = NoValue) extends Statement {
    val action = ":db/retract"
  }

  case class Eid(id: Long)
  case class Prefix(s: String)
  case class Values(vs: Any, prefix: Option[String] = None)
}
