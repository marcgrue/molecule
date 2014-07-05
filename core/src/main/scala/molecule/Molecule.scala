package molecule
import java.util.{Date => jDate}

import molecule.ast.model.Model
import molecule.ast.query.Query
import molecule.db.DatomicFacade


abstract class Molecule(val _model: Model, val _query: Query) extends DatomicFacade {
  override def toString: String = _query.toList
  def p = _query.pretty
  def ids: Seq[Long]
  def size: Int = ids.size

  // Kind of a hack...?
  def asOf(date: jDate) = { dbOp = AsOf(date); this }
  def since(date: jDate) = { dbOp = Since(date); this }
  def imagine(tx: java.util.List[Object]) = { dbOp = Imagine(tx); this }
}
