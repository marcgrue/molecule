package molecule.core.util
import java.util.{List => jList}
import molecule.datomic.base.facade.TxReport
import molecule.core.ast.model._
import molecule.core.ast.transactionModel._
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

private[molecule] case class Debug(
  clazz: String,
  threshold: Int,
  max: Int = 9999,
  showStackTrace: Boolean = false,
  maxLevel: Int = 99,
  showBi: Boolean = false
) {

  // Helpers ..........................................

  def padS(longest: Int, str: String) = pad(longest, str.length) + "  "
  def pad(longest: Int, shorter: Int): String = if (longest > shorter) " " * (longest - shorter) + " " else ""


  def apply(id: Int, params: Any*): Unit = {
    val stackTrace = if (showStackTrace) Thread.currentThread.getStackTrace mkString "\n" else ""
    if (id >= threshold && id <= max) {
      var lastId: Long      = 0L
      var ids   : Set[Long] = Set()

      def traverse(x: Any, level: Int, i: Int): String = {
        if (!x.isInstanceOf[datomic.db.Datum]) {
          lastId = 0L
          ids = Set()
        }
        val pad1   = if (i == 0) "" else "  " * level
        val indent = if (i == 0) "" else pad1
        val max    = level >= maxLevel
        x match {
          case Add(e, a, stmts: Seq[_], bi) =>
            val biStr = if (showBi && bi != NoValue) s"      <$bi>" else ""
            indent + ":db/add" + padS(10, ":db/add") + e + padS(32, e.toString) + a + padS(20, a) + s"list($biStr\n" +
              stmts.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"

          case add@Add(e, a, v, bi) =>
            //            val biStr = if (showBi && bi != NoValue) padS(60, v.toString) + "   " + bi else ""
            //            if (i < 3)
            //              indent + ":db/add" + padS(10, ":db/add") + e + padS(32, e.toString) + a + padS(20, a.toString) + v + biStr
            //            else
            indent + add //+ ","

          case ret@Retract(e, a, v, bi) =>
            //            val biStr = if (showBi && bi != NoValue) padS(60, v.toString) + "   " + bi else ""
            //            if (i < 3)
            //              indent + ":db/retract" + padS(10, ":db/retract") + e + padS(34, e.toString) + a + padS(20, a.toString) + v + biStr
            //            else
            indent + ret //+ ","

          case RetractEntity(e) =>
            indent + ":db/retractEntity" + padS(22, ":db/retractEntity") + e

          case l: java.util.List[_] if l.size() == 4 && l.asScala.head.toString.take(4) == ":db/" => {
            val List(action, e, a, v) = l.asScala.toList
            indent + action + padS(13, action.toString) + e + padS(34, e.toString) + a + padS(26, a.toString) + "   " + v
          }

          case l: jList[_] if max       => indent + "JavaList(" + l.asScala.mkString(",   ") + ")"
          case l: jList[_]              => indent + "JavaList(\n" + l.asScala.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case l: ArrayBuffer[_] if max => indent + "ArrayBuffer(" + l.zipWithIndex.mkString(",   ") + ")"
          case l: ArrayBuffer[_]        => indent + "ArrayBuffer(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case l: Map[_, _] if max      => indent + "Map(" + l.mkString(",   ") + ")"
          case l: Map[_, _]             => indent + "Map(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"

          case l: Iterable[_] =>
            l.headOption match {
              case None                         =>
                indent + "List()"

              case Some(stmt: Statement) =>
                if (stmt.v.isInstanceOf[AbstractValue]) {
                  if (max)
                    indent + "List(" + l.mkString(",   ") + ")"
                  else
                    indent + "List(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString(",\n") + ")"
                } else {
                  // Enable copy/paste for datomic.Util.list to raw transactions
                  if (max)
                    indent + "list(" + l.mkString(",   ") + ")"
                  else
                    indent + "list(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString(",\n") + ")"
                }

              case _ if max =>
                indent + "List(" + l.mkString(",   ") + ")"
              case _        =>
                indent + "List(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString(",\n") + ")"
            }

          case Nested(bond, nested)     => indent + "Nested(\n" + (bond +: nested).zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case TxMetaData(elements)     => indent + "TxMetaData(\n" + elements.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case Composite(elements)      => indent + "Composite(\n" + elements.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case m: Model                 => indent + "Model(\n" + m.elements.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case m: java.util.Map[_, _]   => {
            if (m.size() == 4 && m.asScala.keys.map(_.toString).toSeq.contains(":db-before")) {
              val tx = m.asScala.toList
              indent + "Transaction(\n" +
                traverse(tx(0), level + 1, 1) + "\n" +
                traverse(tx(1), level + 1, 2) + "\n" +
                traverse(tx(2), level + 1, 3) + "\n" +
                traverse(":tempids(\n" + tx(3)._2.asInstanceOf[java.util.Map[_, _]].asScala.iterator.zipWithIndex.map { case (y, j) => traverse(y, level + 2, j + 1) }.mkString("\n") + "))", level + 1, 4)
            } else if (max)
              indent + "JavaMap(" + m.asScala.iterator.mkString(",   ") + ")"
            else
              indent + "JavaMap(\n" + m.asScala.iterator.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          }

          case (a, b) => {
            val bb = b match {
              case it: Iterable[_]             => traverse(it, level, 0)
              case it: java.util.Collection[_] => traverse(it, level, 0)
              case other                       => other
            }
            indent + s"$a -> " + bb
          }

          // Peer Datom
          case d: datomic.db.Datum => {
            val (entitySep, no) = if (lastId == 0L) ("", 1) else if (lastId != d.e) ("\n", ids.size + 1) else ("", "")
            lastId = d.e
            ids += d.e
            val pad3  = " " * (5 - no.toString.length)
            val pad4  = " " * (6 - i.toString.length)
            val added = if (d.added) "true " else "false"
            val r     = if (d.added) " " else "-"
            val datum = List("added: " + added, "t: " + d.tx, "e: " + d.e, "a: " + d.a, "v: " + d.v)
            entitySep + pad1 + no + pad3 + i + pad4 + datum.mkString(",  " + r)
          }

          // Client Datom
          case d: datomicScala.client.api.Datom => {
            val (entitySep, no) = if (lastId == 0L) ("", 1) else if (lastId != d.e) ("\n", ids.size + 1) else ("", "")
            lastId = d.e
            ids += d.e
            val pad3  = " " * (5 - no.toString.length)
            val pad4  = " " * (6 - i.toString.length)
            val added = if (d.added) "true " else "false"
            val r     = if (d.added) " " else "-"
            val datum = List("added: " + added, "t: " + d.tx, "e: " + d.e, "a: " + d.a, "v: " + d.v)
            entitySep + pad1 + no + pad3 + i + pad4 + datum.mkString(",  " + r)
          }

          case txReport: TxReport => txReport.toString

          case value => indent + value //+ s" TYPE: " + (if (value == null) "Null" else value.getClass)
        }
      }

      println(
        s"## $id ## $clazz \n================================================================================================================\n" +
          params.toList.zipWithIndex.map {
            case (x, i) => traverse(x, 0, i + 1)
          }.mkString("\n----------------------------------------------------------------------------------------------------------------\n") +
          s"\n================================================================================================================\n$stackTrace"
      )
    }
  }
}