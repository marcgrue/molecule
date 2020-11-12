package molecule.datomic

import molecule.core.api.{EntityOps, Keywords, TxMethods}
import molecule.core.expression.LogicImplicits
import molecule.core.factory._
import molecule.core.generic.GenericLog
import molecule.core.generic.index.{GenericAEVT, GenericAVET, GenericEAVT, GenericVAET}
import molecule.core.generic.schema.GenericSchema

/** Molecule API to be imported into your project to use Molecule with the Datomic Peer API.
  * <br><br>
  * To start using Molecule involves 2 initial steps:
  *
  *  - Define your schema: [[molecule.core.schema.definition Docs]] | [[http://www.scalamolecule.org/manual/schema/ Manual]]
  *  - `sbt compile` your project to let the sbt-molecule plugin generate your custom molecule DSL.
  *
  * Then you can start using your DSL and create molecules by importing the api, your DSL
  * and assign a Datomic connection to an implicit val:
  * {{{
  *   import molecule.datomic.api._                       // import Molecule API
  *   import molecule.datomic.peer.facade.Datomic_Peer._  // import system api
  *   import path.to.dsl.yourDomain._                     // auto-generated custom DSL
  *   import path.to.schema.YourDomainSchema              // auto-generated custom Schema Transaction data
  *
  *   implicit val conn = recreateDbFrom(YourDomainDefiniton) // Only once
  *
  *   // Create molecules
  *   Person.name("Ben").age(42).save
  *   val benAge = Person.name_("Ben").age.get.head // 42
  *   // etc..
  * }}}
  * */
trait api
  extends Keywords
    with LogicImplicits
    with EntityOps
    with TxMethods
    with GenericSchema
    with GenericLog
    with GenericAEVT
    with GenericAVET
    with GenericEAVT
    with GenericVAET

object api extends api
  // Default max arities (can become a burden on compilation time if number of molecules is in the thousands)
  with Molecule_Factory22
  with Molecule_In_1_Factory22
  with Molecule_In_2_Factory22
  with Molecule_In_3_Factory22
  with Composite_Factory22
  with Composite_In_1_Factory22
  with Composite_In_2_Factory22
  with Composite_In_3_Factory22 {

  // Optional optimization api's for reducing compilation time
  object out1 extends api with Molecule_Factory1
  object out2 extends api with Molecule_Factory2 with Composite_Factory2
  object out3 extends api with Molecule_Factory3 with Composite_Factory3
  object out4 extends api with Molecule_Factory4 with Composite_Factory4
  object out5 extends api with Molecule_Factory5 with Composite_Factory5
  object out6 extends api with Molecule_Factory6 with Composite_Factory5
  object out7 extends api with Molecule_Factory7 with Composite_Factory5
  object out8 extends api with Molecule_Factory8 with Composite_Factory5
  object out9 extends api with Molecule_Factory9 with Composite_Factory5
  object out10 extends api with Molecule_Factory10 with Composite_Factory5
  object out11 extends api with Molecule_Factory11 with Composite_Factory5
  object out12 extends api with Molecule_Factory12 with Composite_Factory5
  object out13 extends api with Molecule_Factory13 with Composite_Factory5
  object out14 extends api with Molecule_Factory14 with Composite_Factory5
  object out15 extends api with Molecule_Factory15 with Composite_Factory5
  object out16 extends api with Molecule_Factory16 with Composite_Factory5
  object out17 extends api with Molecule_Factory17 with Composite_Factory5
  object out18 extends api with Molecule_Factory18 with Composite_Factory5
  object out19 extends api with Molecule_Factory19 with Composite_Factory5
  object out20 extends api with Molecule_Factory20 with Composite_Factory5
  object out21 extends api with Molecule_Factory21 with Composite_Factory5
  object out22 extends api with Molecule_Factory22 with Composite_Factory5

  object in1_out1 extends api with Molecule_Factory1 with Molecule_In_1_Factory1
  object in1_out2 extends api with Molecule_Factory2 with Molecule_In_1_Factory2 with Composite_Factory2 with Composite_In_1_Factory2
  object in1_out3 extends api with Molecule_Factory3 with Molecule_In_1_Factory3 with Composite_Factory3 with Composite_In_1_Factory3
  object in1_out4 extends api with Molecule_Factory4 with Molecule_In_1_Factory4 with Composite_Factory4 with Composite_In_1_Factory4
  object in1_out5 extends api with Molecule_Factory5 with Molecule_In_1_Factory5 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out6 extends api with Molecule_Factory6 with Molecule_In_1_Factory6 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out7 extends api with Molecule_Factory7 with Molecule_In_1_Factory7 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out8 extends api with Molecule_Factory8 with Molecule_In_1_Factory8 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out9 extends api with Molecule_Factory9 with Molecule_In_1_Factory9 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out10 extends api with Molecule_Factory10 with Molecule_In_1_Factory10 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out11 extends api with Molecule_Factory11 with Molecule_In_1_Factory11 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out12 extends api with Molecule_Factory12 with Molecule_In_1_Factory12 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out13 extends api with Molecule_Factory13 with Molecule_In_1_Factory13 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out14 extends api with Molecule_Factory14 with Molecule_In_1_Factory14 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out15 extends api with Molecule_Factory15 with Molecule_In_1_Factory15 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out16 extends api with Molecule_Factory16 with Molecule_In_1_Factory16 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out17 extends api with Molecule_Factory17 with Molecule_In_1_Factory17 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out18 extends api with Molecule_Factory18 with Molecule_In_1_Factory18 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out19 extends api with Molecule_Factory19 with Molecule_In_1_Factory19 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out20 extends api with Molecule_Factory20 with Molecule_In_1_Factory20 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out21 extends api with Molecule_Factory21 with Molecule_In_1_Factory21 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out22 extends api with Molecule_Factory22 with Molecule_In_1_Factory22 with Composite_Factory22 with Composite_In_1_Factory22

  object in2_out1 extends api with Molecule_Factory1 with Molecule_In_1_Factory1 with Molecule_In_2_Factory1
  object in2_out2 extends api with Molecule_Factory2 with Molecule_In_1_Factory2 with Molecule_In_2_Factory2 with Composite_Factory2 with Composite_In_1_Factory2 with Composite_In_2_Factory2
  object in2_out3 extends api with Molecule_Factory3 with Molecule_In_1_Factory3 with Molecule_In_2_Factory3 with Composite_Factory3 with Composite_In_1_Factory3 with Composite_In_2_Factory3
  object in2_out4 extends api with Molecule_Factory4 with Molecule_In_1_Factory4 with Molecule_In_2_Factory4 with Composite_Factory4 with Composite_In_1_Factory4 with Composite_In_2_Factory4
  object in2_out5 extends api with Molecule_Factory5 with Molecule_In_1_Factory5 with Molecule_In_2_Factory5 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out6 extends api with Molecule_Factory6 with Molecule_In_1_Factory6 with Molecule_In_2_Factory6 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out7 extends api with Molecule_Factory7 with Molecule_In_1_Factory7 with Molecule_In_2_Factory7 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out8 extends api with Molecule_Factory8 with Molecule_In_1_Factory8 with Molecule_In_2_Factory8 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out9 extends api with Molecule_Factory9 with Molecule_In_1_Factory9 with Molecule_In_2_Factory9 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out10 extends api with Molecule_Factory10 with Molecule_In_1_Factory10 with Molecule_In_2_Factory10 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out11 extends api with Molecule_Factory11 with Molecule_In_1_Factory11 with Molecule_In_2_Factory11 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out12 extends api with Molecule_Factory12 with Molecule_In_1_Factory12 with Molecule_In_2_Factory12 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out13 extends api with Molecule_Factory13 with Molecule_In_1_Factory13 with Molecule_In_2_Factory13 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out14 extends api with Molecule_Factory14 with Molecule_In_1_Factory14 with Molecule_In_2_Factory14 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out15 extends api with Molecule_Factory15 with Molecule_In_1_Factory15 with Molecule_In_2_Factory15 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out16 extends api with Molecule_Factory16 with Molecule_In_1_Factory16 with Molecule_In_2_Factory16 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out17 extends api with Molecule_Factory17 with Molecule_In_1_Factory17 with Molecule_In_2_Factory17 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out18 extends api with Molecule_Factory18 with Molecule_In_1_Factory18 with Molecule_In_2_Factory18 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out19 extends api with Molecule_Factory19 with Molecule_In_1_Factory19 with Molecule_In_2_Factory19 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out20 extends api with Molecule_Factory20 with Molecule_In_1_Factory20 with Molecule_In_2_Factory20 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out21 extends api with Molecule_Factory21 with Molecule_In_1_Factory21 with Molecule_In_2_Factory21 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out22 extends api with Molecule_Factory22 with Molecule_In_1_Factory22 with Molecule_In_2_Factory22 with Composite_Factory22 with Composite_In_1_Factory5 with Composite_In_2_Factory22

  object in3_out1 extends api with Molecule_Factory1 with Molecule_In_1_Factory1 with Molecule_In_2_Factory1 with Molecule_In_3_Factory1
  object in3_out2 extends api with Molecule_Factory2 with Molecule_In_1_Factory2 with Molecule_In_2_Factory2 with Molecule_In_3_Factory2 with Composite_Factory2 with Composite_In_1_Factory2 with Composite_In_2_Factory2 with Composite_In_3_Factory2
  object in3_out3 extends api with Molecule_Factory3 with Molecule_In_1_Factory3 with Molecule_In_2_Factory3 with Molecule_In_3_Factory3 with Composite_Factory3 with Composite_In_1_Factory3 with Composite_In_2_Factory3 with Composite_In_3_Factory3
  object in3_out4 extends api with Molecule_Factory4 with Molecule_In_1_Factory4 with Molecule_In_2_Factory4 with Molecule_In_3_Factory4 with Composite_Factory4 with Composite_In_1_Factory4 with Composite_In_2_Factory4 with Composite_In_3_Factory4
  object in3_out5 extends api with Molecule_Factory5 with Molecule_In_1_Factory5 with Molecule_In_2_Factory5 with Molecule_In_3_Factory5 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out6 extends api with Molecule_Factory6 with Molecule_In_1_Factory6 with Molecule_In_2_Factory6 with Molecule_In_3_Factory6 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out7 extends api with Molecule_Factory7 with Molecule_In_1_Factory7 with Molecule_In_2_Factory7 with Molecule_In_3_Factory7 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out8 extends api with Molecule_Factory8 with Molecule_In_1_Factory8 with Molecule_In_2_Factory8 with Molecule_In_3_Factory8 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out9 extends api with Molecule_Factory9 with Molecule_In_1_Factory9 with Molecule_In_2_Factory9 with Molecule_In_3_Factory9 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out10 extends api with Molecule_Factory10 with Molecule_In_1_Factory10 with Molecule_In_2_Factory10 with Molecule_In_3_Factory10 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out11 extends api with Molecule_Factory11 with Molecule_In_1_Factory11 with Molecule_In_2_Factory11 with Molecule_In_3_Factory11 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out12 extends api with Molecule_Factory12 with Molecule_In_1_Factory12 with Molecule_In_2_Factory12 with Molecule_In_3_Factory12 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out13 extends api with Molecule_Factory13 with Molecule_In_1_Factory13 with Molecule_In_2_Factory13 with Molecule_In_3_Factory13 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out14 extends api with Molecule_Factory14 with Molecule_In_1_Factory14 with Molecule_In_2_Factory14 with Molecule_In_3_Factory14 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out15 extends api with Molecule_Factory15 with Molecule_In_1_Factory15 with Molecule_In_2_Factory15 with Molecule_In_3_Factory15 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out16 extends api with Molecule_Factory16 with Molecule_In_1_Factory16 with Molecule_In_2_Factory16 with Molecule_In_3_Factory16 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out17 extends api with Molecule_Factory17 with Molecule_In_1_Factory17 with Molecule_In_2_Factory17 with Molecule_In_3_Factory17 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out18 extends api with Molecule_Factory18 with Molecule_In_1_Factory18 with Molecule_In_2_Factory18 with Molecule_In_3_Factory18 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out19 extends api with Molecule_Factory19 with Molecule_In_1_Factory19 with Molecule_In_2_Factory19 with Molecule_In_3_Factory19 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out20 extends api with Molecule_Factory20 with Molecule_In_1_Factory20 with Molecule_In_2_Factory20 with Molecule_In_3_Factory20 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out21 extends api with Molecule_Factory21 with Molecule_In_1_Factory21 with Molecule_In_2_Factory21 with Molecule_In_3_Factory21 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out22 extends api with Molecule_Factory22 with Molecule_In_1_Factory22 with Molecule_In_2_Factory22 with Molecule_In_3_Factory22 with Composite_Factory22 with Composite_In_1_Factory22 with Composite_In_2_Factory22 with Composite_In_3_Factory22
}

