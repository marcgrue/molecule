package molecule
import molecule.ast.model.Model
import molecule.ast.query.{Query, QueryExpr}

/** Exceptions thrown by Molecule. */
package object exceptions {


  class MoleculeException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
    def this(message: String) {
      this(message, null)
    }
  }

  class MoleculeCompileException(message: String, cause: Throwable) extends MoleculeException(message, cause) {
    def this(message: String) {
      this(message, null)
    }
  }


  /** Query exception */
  class QueryException(ex: Throwable,
                       model: Model,
                       query: Query,
                       allInputs: Seq[_],
                       p: QueryExpr => String,
                       chain: Seq[String] = Nil) extends MoleculeException(
    s"""
       |#############################################################################
       |Query failed with cause: $ex${if(chain.nonEmpty) "\nin processing chain:\n" + chain.mkString("[", ";\n", "]")}
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
   """.stripMargin, ex
  )

}
