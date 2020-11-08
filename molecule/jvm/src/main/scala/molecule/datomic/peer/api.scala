package molecule.datomic.peer

import molecule.api.{EntityOps, Keywords, TxMethods}
import molecule.expression.LogicImplicits
import molecule.facade.Datomic
import molecule.factory._
import molecule.generic.GenericLog
import molecule.generic.index.{GenericAEVT, GenericAVET, GenericEAVT, GenericVAET}
import molecule.generic.schema.GenericSchema

/** Molecule API to be imported into your project to use Molecule with the Datomic Peer API.
  * <br><br>
  * To start using Molecule involves 2 initial steps:
  *
  *  - Define your schema: [[molecule.schema.definition Docs]] | [[http://www.scalamolecule.org/manual/schema/ Manual]]
  *  - `sbt compile` your project to let the sbt-molecule plugin generate your custom molecule DSL.
  *
  * Then you can start using your DSL and create molecules by importing the api, your DSL
  * and assign a Datomic connection to an implicit val:
  * {{{
  *   import molecule.datomic.peer.api._     // import Molecule API for Datomic Peer api
  *   import path.to.dsl.yourDomain._        // auto-generated custom DSL
  *   import path.to.schema.YourDomainSchema // auto-generated custom Schema Transaction data
  *
  *   implicit val conn = recreateDbFrom(YourDomainDefiniton) // Only once
  *
  *   // Create molecules
  *   Person.name("Ben").age(42).save
  *   val benAge = Person.name_("Ben").age.get.head // 42
  *   // etc..
  * }}}
  * */
object api extends Datomic
  with Keywords
  with LogicImplicits
  with EntityOps
  with TxMethods

  with Molecule_Factory
  with Molecule_In_1_Factory
  with Molecule_In_2_Factory
  with Molecule_In_3_Factory

  with Composite_Factory
  with Composite_In_1_Factory
  with Composite_In_2_Factory
  with Composite_In_3_Factory

  with GenericSchema
  with GenericLog
  with GenericAEVT
  with GenericAVET
  with GenericEAVT
  with GenericVAET
