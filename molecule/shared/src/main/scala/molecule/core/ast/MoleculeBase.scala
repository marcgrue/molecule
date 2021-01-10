package molecule.core.ast
import molecule.core.ast.elements.Model
import molecule.core.ast.query.Query

/** Base Molecule interface. */
trait MoleculeBase {

  /** Internal [[molecule.core.ast.elements.Model Model]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _model: Model

  /** Internal [[molecule.core.ast.query.Query Query]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _query: Query

  /** Internal optional [[molecule.core.ast.query.Query Query]] representation of nested molecule with added entity search for each level.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _nestedQuery: Option[Query]

  /** Internal un-optimized [[molecule.core.ast.query.Query Query]] representation molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _rawQuery: Query

  /** Internal un-optimized optional [[molecule.core.ast.query.Query Query]] representation of nested molecule with added entity search for each level.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _rawNestedQuery: Option[Query]
}
