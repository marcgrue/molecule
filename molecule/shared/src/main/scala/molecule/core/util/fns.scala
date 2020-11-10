package molecule.core.util
import java.util.Date

object fns extends DateHandling {

  def bind[T](v: T): T = v

  def str2date(s: String): Date = super.str2date(s)
  def date2str(d: Date): String = super.date2str(d)

  def partNs(v: String): Array[String] = v.split("_") match {
    case Array(ns)       => Array("db.part/user", ns)
    case Array(part, ns) => Array(part, ns)
  }

  def live(nsFull: String): Boolean = !nsFull.startsWith("-") && !nsFull.startsWith(":-")
}
