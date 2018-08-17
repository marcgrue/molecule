package molecule.ast
import molecule.ast.model.Model
import molecule.ast.query.Query

/** Base Molecule interface shared by [[molecule.action.Molecule Molecule]] and [[molecule.input.InputMolecule InputMolecule]]. */
trait MoleculeBase {

  /** Internal [[molecule.ast.model.Model Model]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    * */
  val _model: Model

  /** Internal [[molecule.ast.query.Query Query]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    * */
  val _query: Query
}
