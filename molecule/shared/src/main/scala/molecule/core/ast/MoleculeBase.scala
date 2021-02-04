package molecule.core.ast

import molecule.core.ast.elements.Model
import molecule.datomic.ast.query.Query

/** Base Molecule interface. */
trait MoleculeBase {

  /** Internal [[elements.Model Model]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _model: Model

  /** Internal [[molecule.datomic.ast.query.Query Query]] representation of molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _query: Query

  /** Internal optional [[molecule.datomic.ast.query.Query Query]] representation of nested molecule with added entity search for each level.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _nestedQuery: Option[Query]

  /** Internal un-optimized [[molecule.datomic.ast.query.Query Query]] representation molecule.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _rawQuery: Query

  /** Internal un-optimized optional [[molecule.datomic.ast.query.Query Query]] representation of nested molecule with added entity search for each level.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    *
    * @group internal
    **/
  val _rawNestedQuery: Option[Query]
}
