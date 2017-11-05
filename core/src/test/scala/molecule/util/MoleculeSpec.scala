package molecule.util
import molecule.api.{InputMolecule, Molecule}
import molecule.ast.model.Model
import molecule.ast.query._
import molecule.facade.Conn
import molecule.ops.QueryOps._
import molecule.ast.transaction._
import molecule.transform.{Model2Transaction, Query2String}
import org.specs2.mutable._

import scala.language.postfixOps


trait MoleculeSpec extends Specification {

  def typed[T](t: => T) {}

  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def formatTx(tx: Seq[Statement]) = {
    val longestAction = tx.map(stmt => stmt.action.length).max
    val longestAttr = tx.map(stmt => stmt.a.toString.length).max
    val longestValue = tx.map(stmt => stmt.v.toString.length).max

    // Increment temporary ids in a controlled way so that we can test
    val ids = tx.foldLeft(Map[String, String]()) { case (idStmts, stmt) =>
      val rawId = stmt.e.toString
      rawId match {
        case r"#db/id\[:db.part/user -\d{7}\]" =>
          if (idStmts.contains(rawId)) idStmts else idStmts + (rawId -> ("#db/id[:db.part/user -" + (1000001 + idStmts.size) + "]"))
        case r"#db/id\[:db.part/tx -\d{7}\]" =>
          if (idStmts.contains(rawId)) idStmts else idStmts + (rawId -> ("#db/id[:db.part/tx   -" + (1000001 + idStmts.size) + "]"))
        case r"(\d{14})$id" =>
          if (idStmts.contains(rawId)) idStmts else idStmts + (rawId -> (id + "                "))
        case r"#db/id\[:(\w+)$part -\d{7}\]" =>
          if (idStmts.contains(rawId)) idStmts else idStmts + (rawId -> (s"#db/id[:$part -" + (1000001 + idStmts.size) + "]"))
        case other                             => idStmts + (other.toString -> other.toString)
      }
    }
    val tx2 = tx.map { stmt =>
      val newId = ids.getOrElse(stmt.e.toString, sys.error("missing stmt id"))
      val newValue = ids.getOrElse(stmt.v.toString, stmt.v.toString)
      List(
        stmt.action + " " * (longestAction - stmt.action.toString.length),
        newId,
        stmt.a + " " * (longestAttr - stmt.a.toString.length),
        newValue + " " * (longestValue - newValue.length)
      )
    }

    tx2.map(l => l.mkString("List(", ",  ", ")")).mkString("List(\n  ", ",\n  ", "\n)")
  }

  def formatInputs(query: Query) = {
    val rules = if (query.i.rules.isEmpty) ""
    else {
      val p = (expr: QueryExpr) => Query2String(query).p(expr)
      query.i.rules map p mkString("[", "\n     ", "]")
    }
    val first = if (query.i.rules.isEmpty) Seq("datomic.db.Db@xxx") else Seq("datomic.db.Db@xxx", rules)
    val allInputs = first ++ query.inputs
    if (allInputs.size == 1)
      ""
    else
      "\n\nINPUTS:" + allInputs.zipWithIndex.map(e => (e._2 + 1) + " " + e._1).mkString("\nList(\n  ", "\n  ", "\n)")
  }

  implicit class dsl2model2query2string(molecule: Molecule)(implicit conn: Conn) {
    def -->(model: Model) = new {
      molecule._model === model

      // Molecule -> query
      def -->(query: Query) = new {
        molecule._query === query
        def -->(queryString: String) = {
          query.datalog(30) + formatInputs(query) === queryString
        }
      }

      // Input molecule + insert data
      def -->(data: Seq[Seq[Any]]) = new {
        def -->(txString: String) = {
          //          val (tx, _) = Model2Transaction(conn, model).tx(data)
          val tx = Model2Transaction(conn, model).insertStmts(data).flatten
          formatTx(tx) === txString
        }
        // Debug
        def --->(txString: String) = {
          val tx = Model2Transaction(conn, model).insertStmts(data).flatten
          tx foreach println
          formatTx(tx) === txString
        }
      }
    }

    def -->(queryString: String) = {
      molecule._query.datalog(30) + formatInputs(molecule._query) === queryString
    }

    def -->(data: Seq[Seq[Any]]) = new {
      def -->(txString: String) = {
        //        val (tx, _) = Model2Transaction(conn, molecule._model).tx(data)
        val tx = Model2Transaction(conn, molecule._model).insertStmts(data).flatten
        formatTx(tx) === txString
      }
    }
  }

  implicit class inputDsl2model2query2string(inputMolecule: InputMolecule) {
    def -->(model: Model) = new {
      inputMolecule._model === model
      def -->(query: Query) = new {
        inputMolecule._query === query
        def -->(queryString: String) = {
          query.datalog(30) === queryString
        }
      }
    }

    def -->(queryString: String) = {
      inputMolecule._query.datalog(30) === queryString
    }
  }

  def testUpdateMolecule(molecule: Molecule)(implicit conn: Conn) = new {
    def -->(model: Model) = new {
      molecule._model === model
      def -->(txString: String) = {
        val tx = Model2Transaction(conn, molecule._model).updateStmts()
        formatTx(tx) === txString
      }
    }

    def -->(txString: String) = {
      val tx = Model2Transaction(conn, molecule._model).updateStmts()
      formatTx(tx) === txString
    }
  }

  def testInsertMolecule(molecule: Molecule, ids: Seq[Long] = Seq())(implicit conn: Conn) = new {
    def -->(model: Model) = new {
      molecule._model === model
      def -->(txString: String) = {
        val tx = Model2Transaction(conn, model).saveStmts()
        formatTx(tx) === txString
      }
    }

    def -->(txString: String) = {
      val tx = Model2Transaction(conn, molecule._model).saveStmts()
      formatTx(tx) === txString
    }
  }
}