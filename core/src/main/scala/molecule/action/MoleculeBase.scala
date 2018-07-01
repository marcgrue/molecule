package molecule.action

import molecule.ast.model.Model
import molecule.ast.query.Query

private[molecule] trait MoleculeBase {
  val _model: Model
  val _query: Query
}
