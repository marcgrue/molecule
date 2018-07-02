package molecule
import molecule.action.CompositeInserts
import molecule.composition.Expr
import molecule.composition.implicits._
import molecule.entity.EntityImplicit
import molecule.facade.Datomic

/** Molecule base API
  *
  * Import this object to use Molecule:
  *
  * {{{import molecule.imports._}}}
  *
  * Consists of various interfaces and implicits that make Molecule
  * creation and queries possible.
  * */

object imports
  extends Datomic
    with Expr
    with CompositeInserts
    with EntityImplicit
    with MoleculeImplicits
    with InputMolecule1Implicits
    with InputMolecule2Implicits
    with InputMolecule3Implicits
    with CompositeImplicits

