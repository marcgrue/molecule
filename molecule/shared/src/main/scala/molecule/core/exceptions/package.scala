package molecule.core

import molecule.core.ast.elements.Model
import molecule.core.ast.query.{Query, QueryExpr}

/** Exceptions thrown by Molecule. */
package object exceptions {


  class MoleculeException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
    def this(message: String) = {
      this(message, null)
    }
  }

  class MoleculeCompileException(message: String, cause: Throwable) extends MoleculeException(message, cause) {
    def this(message: String) = {
      this(message, null)
    }
  }


  /** Query exception */
  class QueryException(ex: Throwable,
                       model: Model,
                       query: Query,
                       ins: Seq[_],
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
       |RULES: ${if (query.i.rules.isEmpty) "none" else query.i.rules.map(p).mkString("[\n ", "\n ", "\n]")}
       |
       |INPUTS: ${if (ins.isEmpty) "none\n\n" else "\n" + ins.zipWithIndex.map(r => s"${r._2 + 1}: ${r._1}").mkString("\n")}
       |#############################################################################
   """.stripMargin, ex
  )

}
