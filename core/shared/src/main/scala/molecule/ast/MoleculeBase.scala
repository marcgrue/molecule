package molecule.ast
import molecule.ast.model.Model
import molecule.ast.query.Query

/** Base Molecule interface shared by [[molecule.api.Molecule Molecule]] and [[molecule.input.InputMolecule InputMolecule]]. */
trait MoleculeBase {

  /** Internal [[molecule.ast.model.Model Model]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _model: Model

  /** Internal [[molecule.ast.query.Query Query]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _query: Query

  /** Internal optional [[molecule.ast.query.Query Query]] representation of nested molecule with added entity search for each level.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _nestedQuery: Option[Query]

  /** Internal un-optimized [[molecule.ast.query.Query Query]] representation molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _rawQuery: Query

  /** Internal un-optimized optional [[molecule.ast.query.Query Query]] representation of nested molecule with added entity search for each level.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _rawNestedQuery: Option[Query]
}
