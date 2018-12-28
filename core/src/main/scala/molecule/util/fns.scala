package molecule.util
import java.text.SimpleDateFormat
import java.util.{Date, List => jList}
import datomic.{Database, Util}

object fns {

  def bind[T](v: T): T = v

  def date(s: String): Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(s)

  def partNs(v: String): Array[String] = v.split("_") match {
    case Array(ns)       => Array("", ns.toString.head.toLower + ns.toString.tail)
    case Array(part, ns) => Array(part, ns.toString.head.toLower + ns.toString.tail)
  }
}
