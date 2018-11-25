package molecule.util
import java.text.SimpleDateFormat
import java.util.{Date, List => jList}
import datomic.{Database, Util}

object fns {

  def bind(e: Long): Long = e

  def date(s: String): Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(s)

}
