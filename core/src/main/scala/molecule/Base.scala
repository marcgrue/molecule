package molecule

/**
  * Molecule base API
  *
  * To use Molecule, this object is imported
  *
  * ```import molecule.Base._```
  *
  * Consists of various interfaces and implicits that make Molecule
  * creation and queries possible.
  *
  * Note that facade.Conn is also included to be available without extra imports in client code.
  *
  * Import `molecule.Json._` instead to enable json output.
  **/

trait Base
/**
  * Implicit Molecule factory methods `m`
  *
  * val explicitMoleculeResult = m(Person.name.age).get
  * val implicitMoleculeResult = Person.name.age.get
  **/
  extends molecule.factory.MacroImplicits

    /**
      * Facade to Datomic with selected methods
      **/
    with molecule.facade.Datomic

    /**
      * Expression keywords
      *
      * Pre-defined keywords are used for specific expressions
      *
      * `count` can be used to aggregate attribute values:
      * val howManyJohns = Person.name_("John").e(count).get.head
      **/
    with molecule.dsl.expr

    /**
      * Composite inserts
      *
      * To insert wide data sets with many attributes it can be helpful
      * to compose a bigger molecule of several sub-molecules and then
      * supply the data for insertion as tuples of tuples
      **/
    with molecule.api.CompositeInserts

    /**
      * Entity facade implicit
      *
      * Implicit conversion of entity ids to EntityFacades to allow accessing
      * the Entity API directly from an entity id (of type Long).
      **/
    with molecule.api.EntityImplicit

object Base extends Base