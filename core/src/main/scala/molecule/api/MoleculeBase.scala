package molecule.api

import molecule.ast.model.Model
import molecule.ast.query.Query

trait MoleculeBase {
  val _model: Model
  val _query: Query
}
