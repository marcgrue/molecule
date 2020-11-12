//package molecule.datomic.peer
//
//import molecule.core.api.{EntityOps, Keywords, TxMethods}
//import molecule.core.expression.LogicImplicits
//import molecule.core.factory._
//import molecule.core.generic.GenericLog
//import molecule.core.generic.index.{GenericAEVT, GenericAVET, GenericEAVT, GenericVAET}
//import molecule.core.generic.schema.GenericSchema
//import molecule.datomic.peer.facade.Datomic_Peer
//
///** Molecule API to be imported into your project to use Molecule with the Datomic Peer API.
//  * <br><br>
//  * To start using Molecule involves 2 initial steps:
//  *
//  *  - Define your schema: [[molecule.api.schema.definition Docs]] | [[http://www.scalamolecule.org/manual/schema/ Manual]]
//  *  - `sbt compile` your project to let the sbt-molecule plugin generate your custom molecule DSL.
//  *
//  * Then you can start using your DSL and create molecules by importing the api, your DSL
//  * and assign a Datomic connection to an implicit val:
//  * {{{
//  *   import molecule.datomic.api._     // import Molecule API for Datomic Peer api
//  *   import path.to.dsl.yourDomain._        // auto-generated custom DSL
//  *   import path.to.schema.YourDomainSchema // auto-generated custom Schema Transaction data
//  *
//  *   implicit val conn = recreateDbFrom(YourDomainDefiniton) // Only once
//  *
//  *   // Create molecules
//  *   Person.name("Ben").age(42).save
//  *   val benAge = Person.name_("Ben").age.get.head // 42
//  *   // etc..
//  * }}}
//  * */
//trait api extends Datomic_Peer
//  with Keywords
//  with LogicImplicits
//  with EntityOps
//  with TxMethods
//  with GenericSchema
//  with GenericLog
//  with GenericAEVT
//  with GenericAVET
//  with GenericEAVT
//  with GenericVAET
//
//object api extends api
//  // Default max arities (can become a burden on compilation time if number of molecules is in the thousands)
//  with Molecule_Factory22
//  with Molecule_In_1_Factory22
//  with Molecule_In_2_Factory22
//  with Molecule_In_3_Factory22
//  with Composite_Factory22
//  with Composite_In_1_Factory22
//  with Composite_In_2_Factory22
//  with Composite_In_3_Factory22 {
//
//  // Optional optimization api's for reducing compilation time
//  trait out1 extends api with Molecule_Factory1
//  trait out2 extends api with Molecule_Factory2 with Composite_Factory2
//  trait out3 extends api with Molecule_Factory3 with Composite_Factory3
//  trait out4 extends api with Molecule_Factory4 with Composite_Factory4
//  trait out5 extends api with Molecule_Factory5 with Composite_Factory5
//  trait out6 extends api with Molecule_Factory6 with Composite_Factory5
//  trait out7 extends api with Molecule_Factory7 with Composite_Factory5
//  trait out8 extends api with Molecule_Factory8 with Composite_Factory5
//  trait out9 extends api with Molecule_Factory9 with Composite_Factory5
//  trait out10 extends api with Molecule_Factory10 with Composite_Factory5
//  trait out11 extends api with Molecule_Factory11 with Composite_Factory5
//  trait out12 extends api with Molecule_Factory12 with Composite_Factory5
//  trait out13 extends api with Molecule_Factory13 with Composite_Factory5
//  trait out14 extends api with Molecule_Factory14 with Composite_Factory5
//  trait out15 extends api with Molecule_Factory15 with Composite_Factory5
//  trait out16 extends api with Molecule_Factory16 with Composite_Factory5
//  trait out17 extends api with Molecule_Factory17 with Composite_Factory5
//  trait out18 extends api with Molecule_Factory18 with Composite_Factory5
//  trait out19 extends api with Molecule_Factory19 with Composite_Factory5
//  trait out20 extends api with Molecule_Factory20 with Composite_Factory5
//  trait out21 extends api with Molecule_Factory21 with Composite_Factory5
//  trait out22 extends api with Molecule_Factory22 with Composite_Factory5
//
//  trait in1_out1 extends api with Molecule_Factory1 with Molecule_In_1_Factory1
//  trait in1_out2 extends api with Molecule_Factory2 with Molecule_In_1_Factory2 with Composite_Factory2 with Composite_In_1_Factory2
//  trait in1_out3 extends api with Molecule_Factory3 with Molecule_In_1_Factory3 with Composite_Factory3 with Composite_In_1_Factory3
//  trait in1_out4 extends api with Molecule_Factory4 with Molecule_In_1_Factory4 with Composite_Factory4 with Composite_In_1_Factory4
//  trait in1_out5 extends api with Molecule_Factory5 with Molecule_In_1_Factory5 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out6 extends api with Molecule_Factory6 with Molecule_In_1_Factory6 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out7 extends api with Molecule_Factory7 with Molecule_In_1_Factory7 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out8 extends api with Molecule_Factory8 with Molecule_In_1_Factory8 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out9 extends api with Molecule_Factory9 with Molecule_In_1_Factory9 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out10 extends api with Molecule_Factory10 with Molecule_In_1_Factory10 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out11 extends api with Molecule_Factory11 with Molecule_In_1_Factory11 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out12 extends api with Molecule_Factory12 with Molecule_In_1_Factory12 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out13 extends api with Molecule_Factory13 with Molecule_In_1_Factory13 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out14 extends api with Molecule_Factory14 with Molecule_In_1_Factory14 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out15 extends api with Molecule_Factory15 with Molecule_In_1_Factory15 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out16 extends api with Molecule_Factory16 with Molecule_In_1_Factory16 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out17 extends api with Molecule_Factory17 with Molecule_In_1_Factory17 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out18 extends api with Molecule_Factory18 with Molecule_In_1_Factory18 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out19 extends api with Molecule_Factory19 with Molecule_In_1_Factory19 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out20 extends api with Molecule_Factory20 with Molecule_In_1_Factory20 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out21 extends api with Molecule_Factory21 with Molecule_In_1_Factory21 with Composite_Factory5 with Composite_In_1_Factory5
//  trait in1_out22 extends api with Molecule_Factory22 with Molecule_In_1_Factory22 with Composite_Factory22 with Composite_In_1_Factory22
//
//  trait in2_out1 extends api with Molecule_Factory1 with Molecule_In_1_Factory1 with Molecule_In_2_Factory1
//  trait in2_out2 extends api with Molecule_Factory2 with Molecule_In_1_Factory2 with Molecule_In_2_Factory2 with Composite_Factory2 with Composite_In_1_Factory2 with Composite_In_2_Factory2
//  trait in2_out3 extends api with Molecule_Factory3 with Molecule_In_1_Factory3 with Molecule_In_2_Factory3 with Composite_Factory3 with Composite_In_1_Factory3 with Composite_In_2_Factory3
//  trait in2_out4 extends api with Molecule_Factory4 with Molecule_In_1_Factory4 with Molecule_In_2_Factory4 with Composite_Factory4 with Composite_In_1_Factory4 with Composite_In_2_Factory4
//  trait in2_out5 extends api with Molecule_Factory5 with Molecule_In_1_Factory5 with Molecule_In_2_Factory5 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out6 extends api with Molecule_Factory6 with Molecule_In_1_Factory6 with Molecule_In_2_Factory6 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out7 extends api with Molecule_Factory7 with Molecule_In_1_Factory7 with Molecule_In_2_Factory7 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out8 extends api with Molecule_Factory8 with Molecule_In_1_Factory8 with Molecule_In_2_Factory8 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out9 extends api with Molecule_Factory9 with Molecule_In_1_Factory9 with Molecule_In_2_Factory9 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out10 extends api with Molecule_Factory10 with Molecule_In_1_Factory10 with Molecule_In_2_Factory10 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out11 extends api with Molecule_Factory11 with Molecule_In_1_Factory11 with Molecule_In_2_Factory11 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out12 extends api with Molecule_Factory12 with Molecule_In_1_Factory12 with Molecule_In_2_Factory12 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out13 extends api with Molecule_Factory13 with Molecule_In_1_Factory13 with Molecule_In_2_Factory13 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out14 extends api with Molecule_Factory14 with Molecule_In_1_Factory14 with Molecule_In_2_Factory14 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out15 extends api with Molecule_Factory15 with Molecule_In_1_Factory15 with Molecule_In_2_Factory15 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out16 extends api with Molecule_Factory16 with Molecule_In_1_Factory16 with Molecule_In_2_Factory16 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out17 extends api with Molecule_Factory17 with Molecule_In_1_Factory17 with Molecule_In_2_Factory17 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out18 extends api with Molecule_Factory18 with Molecule_In_1_Factory18 with Molecule_In_2_Factory18 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out19 extends api with Molecule_Factory19 with Molecule_In_1_Factory19 with Molecule_In_2_Factory19 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out20 extends api with Molecule_Factory20 with Molecule_In_1_Factory20 with Molecule_In_2_Factory20 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out21 extends api with Molecule_Factory21 with Molecule_In_1_Factory21 with Molecule_In_2_Factory21 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
//  trait in2_out22 extends api with Molecule_Factory22 with Molecule_In_1_Factory22 with Molecule_In_2_Factory22 with Composite_Factory22 with Composite_In_1_Factory5 with Composite_In_2_Factory22
//
//  trait in3_out1 extends api with Molecule_Factory1 with Molecule_In_1_Factory1 with Molecule_In_2_Factory1 with Molecule_In_3_Factory1
//  trait in3_out2 extends api with Molecule_Factory2 with Molecule_In_1_Factory2 with Molecule_In_2_Factory2 with Molecule_In_3_Factory2 with Composite_Factory2 with Composite_In_1_Factory2 with Composite_In_2_Factory2 with Composite_In_3_Factory2
//  trait in3_out3 extends api with Molecule_Factory3 with Molecule_In_1_Factory3 with Molecule_In_2_Factory3 with Molecule_In_3_Factory3 with Composite_Factory3 with Composite_In_1_Factory3 with Composite_In_2_Factory3 with Composite_In_3_Factory3
//  trait in3_out4 extends api with Molecule_Factory4 with Molecule_In_1_Factory4 with Molecule_In_2_Factory4 with Molecule_In_3_Factory4 with Composite_Factory4 with Composite_In_1_Factory4 with Composite_In_2_Factory4 with Composite_In_3_Factory4
//  trait in3_out5 extends api with Molecule_Factory5 with Molecule_In_1_Factory5 with Molecule_In_2_Factory5 with Molecule_In_3_Factory5 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out6 extends api with Molecule_Factory6 with Molecule_In_1_Factory6 with Molecule_In_2_Factory6 with Molecule_In_3_Factory6 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out7 extends api with Molecule_Factory7 with Molecule_In_1_Factory7 with Molecule_In_2_Factory7 with Molecule_In_3_Factory7 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out8 extends api with Molecule_Factory8 with Molecule_In_1_Factory8 with Molecule_In_2_Factory8 with Molecule_In_3_Factory8 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out9 extends api with Molecule_Factory9 with Molecule_In_1_Factory9 with Molecule_In_2_Factory9 with Molecule_In_3_Factory9 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out10 extends api with Molecule_Factory10 with Molecule_In_1_Factory10 with Molecule_In_2_Factory10 with Molecule_In_3_Factory10 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out11 extends api with Molecule_Factory11 with Molecule_In_1_Factory11 with Molecule_In_2_Factory11 with Molecule_In_3_Factory11 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out12 extends api with Molecule_Factory12 with Molecule_In_1_Factory12 with Molecule_In_2_Factory12 with Molecule_In_3_Factory12 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out13 extends api with Molecule_Factory13 with Molecule_In_1_Factory13 with Molecule_In_2_Factory13 with Molecule_In_3_Factory13 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out14 extends api with Molecule_Factory14 with Molecule_In_1_Factory14 with Molecule_In_2_Factory14 with Molecule_In_3_Factory14 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out15 extends api with Molecule_Factory15 with Molecule_In_1_Factory15 with Molecule_In_2_Factory15 with Molecule_In_3_Factory15 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out16 extends api with Molecule_Factory16 with Molecule_In_1_Factory16 with Molecule_In_2_Factory16 with Molecule_In_3_Factory16 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out17 extends api with Molecule_Factory17 with Molecule_In_1_Factory17 with Molecule_In_2_Factory17 with Molecule_In_3_Factory17 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out18 extends api with Molecule_Factory18 with Molecule_In_1_Factory18 with Molecule_In_2_Factory18 with Molecule_In_3_Factory18 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out19 extends api with Molecule_Factory19 with Molecule_In_1_Factory19 with Molecule_In_2_Factory19 with Molecule_In_3_Factory19 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out20 extends api with Molecule_Factory20 with Molecule_In_1_Factory20 with Molecule_In_2_Factory20 with Molecule_In_3_Factory20 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out21 extends api with Molecule_Factory21 with Molecule_In_1_Factory21 with Molecule_In_2_Factory21 with Molecule_In_3_Factory21 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
//  trait in3_out22 extends api with Molecule_Factory22 with Molecule_In_1_Factory22 with Molecule_In_2_Factory22 with Molecule_In_3_Factory22 with Composite_Factory22 with Composite_In_1_Factory22 with Composite_In_2_Factory22 with Composite_In_3_Factory22
//
//  object out1 extends out1
//  object out2 extends out2
//  object out3 extends out3
//  object out4 extends out4
//  object out5 extends out5
//  object out6 extends out6
//  object out7 extends out7
//  object out8 extends out8
//  object out9 extends out9
//  object out10 extends out10
//  object out11 extends out11
//  object out12 extends out12
//  object out13 extends out13
//  object out14 extends out14
//  object out15 extends out15
//  object out16 extends out16
//  object out17 extends out17
//  object out18 extends out18
//  object out19 extends out19
//  object out20 extends out20
//  object out21 extends out21
//  object out22 extends out22
//
//  object in1_out1 extends in1_out1
//  object in1_out2 extends in1_out2
//  object in1_out3 extends in1_out3
//  object in1_out4 extends in1_out4
//  object in1_out5 extends in1_out5
//  object in1_out6 extends in1_out6
//  object in1_out7 extends in1_out7
//  object in1_out8 extends in1_out8
//  object in1_out9 extends in1_out9
//  object in1_out10 extends in1_out10
//  object in1_out11 extends in1_out11
//  object in1_out12 extends in1_out12
//  object in1_out13 extends in1_out13
//  object in1_out14 extends in1_out14
//  object in1_out15 extends in1_out15
//  object in1_out16 extends in1_out16
//  object in1_out17 extends in1_out17
//  object in1_out18 extends in1_out18
//  object in1_out19 extends in1_out19
//  object in1_out20 extends in1_out20
//  object in1_out21 extends in1_out21
//  object in1_out22 extends in1_out22
//
//  object in2_out1 extends in2_out1
//  object in2_out2 extends in2_out2
//  object in2_out3 extends in2_out3
//  object in2_out4 extends in2_out4
//  object in2_out5 extends in2_out5
//  object in2_out6 extends in2_out6
//  object in2_out7 extends in2_out7
//  object in2_out8 extends in2_out8
//  object in2_out9 extends in2_out9
//  object in2_out10 extends in2_out10
//  object in2_out11 extends in2_out11
//  object in2_out12 extends in2_out12
//  object in2_out13 extends in2_out13
//  object in2_out14 extends in2_out14
//  object in2_out15 extends in2_out15
//  object in2_out16 extends in2_out16
//  object in2_out17 extends in2_out17
//  object in2_out18 extends in2_out18
//  object in2_out19 extends in2_out19
//  object in2_out20 extends in2_out20
//  object in2_out21 extends in2_out21
//  object in2_out22 extends in2_out22
//
//  object in3_out1 extends in3_out1
//  object in3_out2 extends in3_out2
//  object in3_out3 extends in3_out3
//  object in3_out4 extends in3_out4
//  object in3_out5 extends in3_out5
//  object in3_out6 extends in3_out6
//  object in3_out7 extends in3_out7
//  object in3_out8 extends in3_out8
//  object in3_out9 extends in3_out9
//  object in3_out10 extends in3_out10
//  object in3_out11 extends in3_out11
//  object in3_out12 extends in3_out12
//  object in3_out13 extends in3_out13
//  object in3_out14 extends in3_out14
//  object in3_out15 extends in3_out15
//  object in3_out16 extends in3_out16
//  object in3_out17 extends in3_out17
//  object in3_out18 extends in3_out18
//  object in3_out19 extends in3_out19
//  object in3_out20 extends in3_out20
//  object in3_out21 extends in3_out21
//  object in3_out22 extends in3_out22
//}
//
