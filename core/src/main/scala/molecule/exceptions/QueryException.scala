package molecule.exceptions
import molecule.ast.model.Model
import molecule.ast.query._



case class QueryException(e: Throwable, model: Model, query: Query, allInputs: Seq[_], p: QueryExpr => String) extends RuntimeException(
  s"""
     |#############################################################################
     |$e
     |
     |$model
     |
     |$query
     |
     |${query.datalog}
     |
     |RULES: ${if (query.i.rules.isEmpty) "none" else query.i.rules map p mkString("[\n ", "\n ", "\n]")}
     |
     |INPUTS: ${allInputs.zipWithIndex.map(e => "\n" + (e._2 + 1) + " " + e._1)}
     |#############################################################################
   """.stripMargin
)