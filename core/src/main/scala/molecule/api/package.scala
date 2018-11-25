package molecule
import molecule.factory._

/** Public interface to be imported to use Molecule.
  *
  * To make the Molecule macro materializations as fast as possible we try to
  * import as few macro implicits as possible. If your application code build molecules
  * with at the most 10 attributes, then you can do the following import to start using Molecule:
  * {{{
  *   import molecule.api.out10._
  * }}}
  * `out` means "output molecule" , and `10` the maximum arity or number of attributes
  * of your molecules.
  *
  * If you use input molecules awaiting an input then you can add `inX` where X is
  * how many inputs (1, 2 or 3) you will use, for instance:
  * {{{
  *   import molecule.api.in2_out10._
  * }}}
  * This way we keep the implicit macro def lookups to a minimum and compilation speed
  * as fast as possible.
  *
  * Arities can be changed anytime you like. But not to a lower arity than that of the
  * molecules you use in scope of the import.
  * */
package object api {

  object out1 extends core with Molecule_Factory1
  object out2 extends core with Molecule_Factory2 with Composite_Factory2
  object out3 extends core with Molecule_Factory3 with Composite_Factory3
  object out4 extends core with Molecule_Factory4 with Composite_Factory4
  object out5 extends core with Molecule_Factory5 with Composite_Factory5
  object out6 extends core with Molecule_Factory6 with Composite_Factory5
  object out7 extends core with Molecule_Factory7 with Composite_Factory5
  object out8 extends core with Molecule_Factory8 with Composite_Factory5
  object out9 extends core with Molecule_Factory9 with Composite_Factory5
  object out10 extends core with Molecule_Factory10 with Composite_Factory5
  object out11 extends core with Molecule_Factory11 with Composite_Factory5
  object out12 extends core with Molecule_Factory12 with Composite_Factory5
  object out13 extends core with Molecule_Factory13 with Composite_Factory5
  object out14 extends core with Molecule_Factory14 with Composite_Factory5
  object out15 extends core with Molecule_Factory15 with Composite_Factory5
  object out16 extends core with Molecule_Factory16 with Composite_Factory5
  object out17 extends core with Molecule_Factory17 with Composite_Factory5
  object out18 extends core with Molecule_Factory18 with Composite_Factory5
  object out19 extends core with Molecule_Factory19 with Composite_Factory5
  object out20 extends core with Molecule_Factory20 with Composite_Factory5
  object out21 extends core with Molecule_Factory21 with Composite_Factory5
  object out22 extends core with Molecule_Factory22 with Composite_Factory5

  object in1_out1 extends core with Molecule_Factory1 with Molecule_In_1_Factory1
  object in1_out2 extends core with Molecule_Factory2 with Molecule_In_1_Factory2 with Composite_Factory2 with Composite_In_1_Factory2
  object in1_out3 extends core with Molecule_Factory3 with Molecule_In_1_Factory3 with Composite_Factory3 with Composite_In_1_Factory3
  object in1_out4 extends core with Molecule_Factory4 with Molecule_In_1_Factory4 with Composite_Factory4 with Composite_In_1_Factory4
  object in1_out5 extends core with Molecule_Factory5 with Molecule_In_1_Factory5 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out6 extends core with Molecule_Factory6 with Molecule_In_1_Factory6 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out7 extends core with Molecule_Factory7 with Molecule_In_1_Factory7 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out8 extends core with Molecule_Factory8 with Molecule_In_1_Factory8 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out9 extends core with Molecule_Factory9 with Molecule_In_1_Factory9 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out10 extends core with Molecule_Factory10 with Molecule_In_1_Factory10 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out11 extends core with Molecule_Factory11 with Molecule_In_1_Factory11 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out12 extends core with Molecule_Factory12 with Molecule_In_1_Factory12 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out13 extends core with Molecule_Factory13 with Molecule_In_1_Factory13 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out14 extends core with Molecule_Factory14 with Molecule_In_1_Factory14 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out15 extends core with Molecule_Factory15 with Molecule_In_1_Factory15 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out16 extends core with Molecule_Factory16 with Molecule_In_1_Factory16 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out17 extends core with Molecule_Factory17 with Molecule_In_1_Factory17 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out18 extends core with Molecule_Factory18 with Molecule_In_1_Factory18 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out19 extends core with Molecule_Factory19 with Molecule_In_1_Factory19 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out20 extends core with Molecule_Factory20 with Molecule_In_1_Factory20 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out21 extends core with Molecule_Factory21 with Molecule_In_1_Factory21 with Composite_Factory5 with Composite_In_1_Factory5
  object in1_out22 extends core with Molecule_Factory22 with Molecule_In_1_Factory22 with Composite_Factory5 with Composite_In_1_Factory5

  object in2_out1 extends core with Molecule_Factory1 with Molecule_In_1_Factory1 with Molecule_In_2_Factory1
  object in2_out2 extends core with Molecule_Factory2 with Molecule_In_1_Factory2 with Molecule_In_2_Factory2 with Composite_Factory2 with Composite_In_1_Factory2 with Composite_In_2_Factory2
  object in2_out3 extends core with Molecule_Factory3 with Molecule_In_1_Factory3 with Molecule_In_2_Factory3 with Composite_Factory3 with Composite_In_1_Factory3 with Composite_In_2_Factory3
  object in2_out4 extends core with Molecule_Factory4 with Molecule_In_1_Factory4 with Molecule_In_2_Factory4 with Composite_Factory4 with Composite_In_1_Factory4 with Composite_In_2_Factory4
  object in2_out5 extends core with Molecule_Factory5 with Molecule_In_1_Factory5 with Molecule_In_2_Factory5 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out6 extends core with Molecule_Factory6 with Molecule_In_1_Factory6 with Molecule_In_2_Factory6 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out7 extends core with Molecule_Factory7 with Molecule_In_1_Factory7 with Molecule_In_2_Factory7 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out8 extends core with Molecule_Factory8 with Molecule_In_1_Factory8 with Molecule_In_2_Factory8 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out9 extends core with Molecule_Factory9 with Molecule_In_1_Factory9 with Molecule_In_2_Factory9 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out10 extends core with Molecule_Factory10 with Molecule_In_1_Factory10 with Molecule_In_2_Factory10 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out11 extends core with Molecule_Factory11 with Molecule_In_1_Factory11 with Molecule_In_2_Factory11 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out12 extends core with Molecule_Factory12 with Molecule_In_1_Factory12 with Molecule_In_2_Factory12 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out13 extends core with Molecule_Factory13 with Molecule_In_1_Factory13 with Molecule_In_2_Factory13 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out14 extends core with Molecule_Factory14 with Molecule_In_1_Factory14 with Molecule_In_2_Factory14 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out15 extends core with Molecule_Factory15 with Molecule_In_1_Factory15 with Molecule_In_2_Factory15 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out16 extends core with Molecule_Factory16 with Molecule_In_1_Factory16 with Molecule_In_2_Factory16 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out17 extends core with Molecule_Factory17 with Molecule_In_1_Factory17 with Molecule_In_2_Factory17 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out18 extends core with Molecule_Factory18 with Molecule_In_1_Factory18 with Molecule_In_2_Factory18 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out19 extends core with Molecule_Factory19 with Molecule_In_1_Factory19 with Molecule_In_2_Factory19 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out20 extends core with Molecule_Factory20 with Molecule_In_1_Factory20 with Molecule_In_2_Factory20 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out21 extends core with Molecule_Factory21 with Molecule_In_1_Factory21 with Molecule_In_2_Factory21 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5
  object in2_out22 extends core with Molecule_Factory22 with Molecule_In_1_Factory22 with Molecule_In_2_Factory22 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5

  object in3_out1 extends core with Molecule_Factory1 with Molecule_In_1_Factory1 with Molecule_In_2_Factory1 with Molecule_In_3_Factory1
  object in3_out2 extends core with Molecule_Factory2 with Molecule_In_1_Factory2 with Molecule_In_2_Factory2 with Molecule_In_3_Factory2 with Composite_Factory2 with Composite_In_1_Factory2 with Composite_In_2_Factory2 with Composite_In_3_Factory2
  object in3_out3 extends core with Molecule_Factory3 with Molecule_In_1_Factory3 with Molecule_In_2_Factory3 with Molecule_In_3_Factory3 with Composite_Factory3 with Composite_In_1_Factory3 with Composite_In_2_Factory3 with Composite_In_3_Factory3
  object in3_out4 extends core with Molecule_Factory4 with Molecule_In_1_Factory4 with Molecule_In_2_Factory4 with Molecule_In_3_Factory4 with Composite_Factory4 with Composite_In_1_Factory4 with Composite_In_2_Factory4 with Composite_In_3_Factory4
  object in3_out5 extends core with Molecule_Factory5 with Molecule_In_1_Factory5 with Molecule_In_2_Factory5 with Molecule_In_3_Factory5 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out6 extends core with Molecule_Factory6 with Molecule_In_1_Factory6 with Molecule_In_2_Factory6 with Molecule_In_3_Factory6 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out7 extends core with Molecule_Factory7 with Molecule_In_1_Factory7 with Molecule_In_2_Factory7 with Molecule_In_3_Factory7 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out8 extends core with Molecule_Factory8 with Molecule_In_1_Factory8 with Molecule_In_2_Factory8 with Molecule_In_3_Factory8 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out9 extends core with Molecule_Factory9 with Molecule_In_1_Factory9 with Molecule_In_2_Factory9 with Molecule_In_3_Factory9 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out10 extends core with Molecule_Factory10 with Molecule_In_1_Factory10 with Molecule_In_2_Factory10 with Molecule_In_3_Factory10 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out11 extends core with Molecule_Factory11 with Molecule_In_1_Factory11 with Molecule_In_2_Factory11 with Molecule_In_3_Factory11 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out12 extends core with Molecule_Factory12 with Molecule_In_1_Factory12 with Molecule_In_2_Factory12 with Molecule_In_3_Factory12 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out13 extends core with Molecule_Factory13 with Molecule_In_1_Factory13 with Molecule_In_2_Factory13 with Molecule_In_3_Factory13 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out14 extends core with Molecule_Factory14 with Molecule_In_1_Factory14 with Molecule_In_2_Factory14 with Molecule_In_3_Factory14 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out15 extends core with Molecule_Factory15 with Molecule_In_1_Factory15 with Molecule_In_2_Factory15 with Molecule_In_3_Factory15 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out16 extends core with Molecule_Factory16 with Molecule_In_1_Factory16 with Molecule_In_2_Factory16 with Molecule_In_3_Factory16 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out17 extends core with Molecule_Factory17 with Molecule_In_1_Factory17 with Molecule_In_2_Factory17 with Molecule_In_3_Factory17 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out18 extends core with Molecule_Factory18 with Molecule_In_1_Factory18 with Molecule_In_2_Factory18 with Molecule_In_3_Factory18 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out19 extends core with Molecule_Factory19 with Molecule_In_1_Factory19 with Molecule_In_2_Factory19 with Molecule_In_3_Factory19 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out20 extends core with Molecule_Factory20 with Molecule_In_1_Factory20 with Molecule_In_2_Factory20 with Molecule_In_3_Factory20 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out21 extends core with Molecule_Factory21 with Molecule_In_1_Factory21 with Molecule_In_2_Factory21 with Molecule_In_3_Factory21 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5
  object in3_out22 extends core with Molecule_Factory22 with Molecule_In_1_Factory22 with Molecule_In_2_Factory22 with Molecule_In_3_Factory22 with Composite_Factory5 with Composite_In_1_Factory5 with Composite_In_2_Factory5 with Composite_In_3_Factory5

}
