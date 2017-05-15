/**
  * Main Molecule API
  *
  * To use Molecule, this object is imported
  *
  * ```import molecule._```
  *
  * Consists of various interfaces and implicits that make Molecule
  * creation and queries possible (see below)
  *
  * Note that facade.Conn is also included directly in the molecule package level
  * in order ofr it to be available without extra imports in client code.
  **/
package object molecule

/**
  * Implicit Molecule factory methods `m`
  *
  * val explicitMoleculeResult = m(Person.name.age).get
  * val implicitMoleculeResult = Person.name.age.get
  **/
  extends factory.MacroImplicits

    /**
      * Facade to Datomic with selected methods
      **/
    with facade.Datomic

    /**
      * Expression keywords
      *
      * Pre-defined keywords are used for specific expressions
      *
      * `count` can be used to aggregate attribute values:
      * val howManyJohns = Person.name_("John").e(count).get.head
      **/
    with dsl.expr

    /**
      * Composite inserts
      *
      * To insert wide data sets with many attributes it can be helpful
      * to compose a bigger molecule of several sub-molecules and then
      * supply the data for insertion as tuples of tuples
      **/
    with api.CompositeInserts

    /**
      * Entity facade implicit
      *
      * Implicit conversion of entity ids to EntityFacades to allow accessing
      * the Entity API directly from an entity id (of type Long).
      **/
    with api.EntityImplicit