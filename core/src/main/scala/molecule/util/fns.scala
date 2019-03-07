package molecule.util
import java.text.SimpleDateFormat
import java.util.{Date, List => jList}
import datomic.{Database, Util}

object fns {

  def bind[T](v: T): T = v

  def date(s: String): Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(s)

  def partNs(v: String): Array[String] = v.split("_") match {
    case Array(ns)       => Array("db.part/user", ns)
    case Array(part, ns) => Array(part, ns)
  }

  def live(nsFull: String): Boolean = !nsFull.startsWith("-") && !nsFull.startsWith(":-")
}
