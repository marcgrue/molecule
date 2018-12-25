package molecule.util
import java.util.{List => jList}
import molecule.ast.model._
import molecule.ast.transactionModel._
import scala.collection.mutable.ArrayBuffer

//import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

private[molecule] case class Debug(clazz: String, threshold: Int, max: Int = 9999, showStackTrace: Boolean = false, maxLevel: Int = 99) {

  // Helpers ..........................................

  def padS(longest: Int, str: String) = pad(longest, str.length)
  def pad(longest: Int, shorter: Int) = if (longest > shorter) " " * (longest - shorter) + " " else ""


  def apply(id: Int, params: Any*): Unit = {
    val stackTrace = if (showStackTrace) Thread.currentThread.getStackTrace mkString "\n" else ""
    if (id >= threshold && id <= max) {
      var lastId: Long = 0L
      var ids: Set[Long] = Set()

      def traverse(x: Any, level: Int, i: Int): String = {
        if (!x.isInstanceOf[datomic.db.Datum]) {lastId = 0L; ids = Set()}
        val pad1 = if (i == 0) "" else "  " * level
        val pad2 = if (i < 10) "          " else "         "
        val indent = if (i == 0) "" else pad1 + i + pad2
        val max = level >= maxLevel
        x match {
          case Add(e, a, stmts: Seq[_], bi) =>
            val biStr = if (bi != NoValue) s"      <$bi>" else ""
            indent + ":db/add" + padS(13, ":db/add") + e + padS(34, e.toString) + a + padS(30, a.toString) + s"List($biStr\n" +
              stmts.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case Add(e, a, v, bi)             =>
            val biStr = if (bi != NoValue) bi else ""
            indent + ":db/add" + padS(13, ":db/add") + e + padS(34, e.toString) + a + padS(30, a.toString) + v + padS(60, v.toString) + "   " + biStr

          case Retract(e, a, v, bi) =>
            val biStr = if (bi != NoValue) bi else ""
            indent + ":db/retract" + padS(13, ":db/retract") + e + padS(34, e.toString) + a + padS(30, a.toString) + v + padS(60, v.toString) + "   " + biStr
          case RetractEntity(e)     =>
            indent + ":db.fn/retractEntity" + padS(22, ":db.fn/retractEntity") + e

          case l: java.util.List[_] if l.size() == 4 && l.asScala.head.toString.take(4) == ":db/" => {
            val List(action, e, a, v) = l.asScala.toList
            indent + action + padS(13, action.toString) + e + padS(34, e.toString) + a + padS(26, a.toString) + "   " + v
          }

          case l: List[_] if max        => indent + "List(" + l.mkString(",   ") + ")"
          case l: List[_]               => indent + "List(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case l: jList[_] if max       => indent + "JavaList(" + l.asScala.mkString(",   ") + ")"
          case l: jList[_]              => indent + "JavaList(\n" + l.asScala.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case l: ArrayBuffer[_] if max => indent + "ArrayBuffer(" + l.zipWithIndex.mkString(",   ") + ")"
          case l: ArrayBuffer[_]        => indent + "ArrayBuffer(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
          case l: Map[_, _] if max      => indent + "Map(" + l.mkString(",   ") + ")"
          case l: Map[_, _]             => indent + "Map(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
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

          case d: datomic.db.Datum => {
            val (entitySep, no) = if (lastId == 0L) ("", 1) else if (lastId != d.e) ("\n", ids.size + 1) else ("", "")
            lastId = d.e
            ids += d.e
            val pad3 = " " * (5 - no.toString.length)
            val pad4 = " " * (6 - i.toString.length)
            val added = if (d.added) "true " else "false"
            val r = if (d.added) " " else "-"
            val datum = List("added: " + added, "t: " + d.tx, "e: " + d.e, "a: " + d.a, "v: " + d.v)
            entitySep + pad1 + no + pad3 + i + pad4 + datum.mkString(",  " + r)
          }

          case value => indent + value //+ s" TYPE: " + (if(value == null) "Null" else value.getClass)
        }
      }

      println(
        s"## $id ## $clazz \n========================================================================\n" +
          params.toList.zipWithIndex.map {
            case (e, i) => traverse(e, 0, i + 1)
          }.mkString("\n------------------------------------------------\n") +
          s"\n========================================================================\n$stackTrace"
      )
    }
  }
}