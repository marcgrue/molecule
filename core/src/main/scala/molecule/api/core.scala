package molecule.api

import molecule.expression.{AggregateKeywords, AttrExpressions, LogicImplicits}
import molecule.facade.Datomic
import molecule.generic.GenericLog
import molecule.generic.index.{GenericAVET, GenericEAVT, GenericVAET}
import molecule.generic.index.{GenericAEVT, GenericAVET, GenericEAVT, GenericVAET}
import molecule.generic.schema.GenericSchema


/** Molecule API to be imported into your project to use Molecule.
  * <br><br>
  * To start using Molecule involves 2 initial steps:
  *
  *  - Define your schema: [[molecule.schema.definition Docs]] | [[http://www.scalamolecule.org/manual/schema/ Manual]]
  *  - `sbt compile` your project to let the sbt-molecule plugin generate your custom molecule DSL.
  *
  * Then you can start using your DSL and create molecules by importing the api, your DSL
  * and assign a Datomic connection to an implicit val:
  * {{{
  *   import molecule.api.All._                  // import Molecule API
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
  * For brevity, arity 3-22 interfaces and empty companion traits are left ungrouped.
  * */

private[molecule] trait core extends Datomic
  with AttrExpressions
  with AggregateKeywords
  with LogicImplicits
  with EntityOps
  with TxMethods
  with GenericSchema
  with GenericLog
  with GenericAEVT
  with GenericAVET
  with GenericEAVT
  with GenericVAET
{

  object ? extends molecule.expression.AttrExpressions.?
  object unify extends molecule.api.core.unify

  object count extends molecule.api.core.count
  object countDistinct extends molecule.api.core.countDistinct
  object distinct extends molecule.api.core.distinct
  object max extends molecule.api.core.max
  object min extends molecule.api.core.min
  object rand extends molecule.api.core.rand
  object sample extends molecule.api.core.sample
  object avg extends molecule.api.core.avg
  object median extends molecule.api.core.median
  object stddev extends molecule.api.core.stddev
  object sum extends molecule.api.core.sum
  object variance extends molecule.api.core.variance
}

object core extends core