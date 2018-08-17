package molecule.input

import molecule.action.Molecule._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query._
import molecule.exceptions.MoleculeException
import molecule.facade.Conn
import molecule.input.exception.InputMolecule_3_Exception


/** Shared interfaces of input molecules awaiting 3 inputs.
  * {{{
  *   // Sample data set
  *   Person.name.profession.age.score insert List(
  *     ("Ann", "doctor", 37, 1.0),
  *     ("Ben", "teacher", 37, 1.0),
  *     ("Joe", "teacher", 32, 1.0),
  *     ("Liz", "teacher", 28, 2.0)
  *   )
  *  
  *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
  *  
  *   // 6 ways of querying
  *  
  *   // 1. Individual args matching input attributes
  *   profAgeScore.apply("doctor", 37, 1.0).get === List("Ann")
  *  
  *   // 2. Seq of pair of values matching input attributes
  *   profAgeScore.apply(Seq(("doctor", 37, 1.0))).get === List("Ann")
  *   profAgeScore.apply(Seq(("doctor", 37, 1.0), ("teacher", 37, 1.0))).get.sorted === List("Ann", "Ben")
  *  
  *   // 3. One or more triples matching input attributes
  *   profAgeScore.apply(("doctor", 37, 1.0)).get === List("Ann")
  *   profAgeScore.apply(("doctor", 37, 1.0), ("teacher", 37, 1.0)).get.sorted === List("Ann", "Ben")
  *  
  *   // 4. Two sequences, each matching corresponding input attribute
  *   profAgeScore.apply(Seq("doctor"), Seq(37), Seq(1.0)).get === List("Ann")
  *   profAgeScore.apply(Seq("doctor", "teacher"), Seq(37), Seq(1.0)).get.sorted === List("Ann", "Ben")
  *  
  *   // 5. Three expressions, one for each input attribute
  *   // [profession-expression] and [age-expression] and [score-expression]
  *   profAgeScore.apply("doctor" and 37 and 1.0).get === List("Ann")
  *   profAgeScore.apply(("doctor" or "teacher") and 37 and 1.0).get.sorted === List("Ann", "Ben")
  *   profAgeScore.apply(("doctor" or "teacher") and (37 or 32) and 1.0).get.sorted === List("Ann", "Ben", "Joe")
  *   profAgeScore.apply(("doctor" or "teacher") and (37 or 32) and (1.0 or 2.0)).get.sorted === List("Ann", "Ben", "Joe")
  *  
  *   // 6. [triple-expression] or [triple-expression] or ...
  *   profAgeScore.apply(("doctor" and 37 and 1.0) or ("teacher" and 32 and 1.0)).get.sorted === List("Ann", "Joe")
  * }}}
  *
  * @see [[molecule.input.InputMolecule]]
  *     | [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  * @tparam I1 Type of input matching first attribute with `?` marker (profession: String)
  * @tparam I2 Type of input matching second attribute with `?` marker (age: Int)
  * @tparam I3 Type of input matching third attribute with `?` marker (score: Double)
  */
trait InputMolecule_3[I1, I2, I3] extends InputMolecule {

  protected def resolveOr3(or: Or3[I1, I2, I3])(implicit conn: Conn): Seq[(I1, I2, I3)] = {
    def traverse(expr: Or3[I1, I2, I3]): Seq[(I1, I2, I3)] = expr match {
      case Or3(
      And3(TermValue(a1), TermValue(a2), TermValue(a3)),
      And3(TermValue(b1), TermValue(b2), TermValue(b3)),
      or3: Or3[I1, I2, I3]
      )      => Seq((a1, a2, a3), (b1, b2, b3)) ++ traverse(or3)
      case Or3(
      And3(TermValue(a1), TermValue(a2), TermValue(a3)),
      And3(TermValue(b1), TermValue(b2), TermValue(b3)),
      And3(TermValue(c1), TermValue(c2), TermValue(c3))
      )      => Seq((a1, a2, a3), (b1, b2, b3), (c1, c2, c3))
      case _ => throw new InputMolecule_3_Exception(s"Unexpected Or3 expression: " + expr)
    }
    traverse(or).distinct
  }

  protected def triple(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3]) = {
    val combinations = for {
      i1 <- in1.distinct
      i2 <- in2.distinct
      i3 <- in3.distinct
    } yield (i1, i2, i3)
    if (combinations.size > 1000)
      throw new InputMolecule_3_Exception(s"Number of input combinations exceed a limit of 1000. " +
        s"Was (${in1.distinct.size} x ${in2.distinct.size} x ${in3.distinct.size}) = ${combinations.size}")
    combinations
  }

  protected def resolveAnd3(and3: And3[I1, I2, I3])(implicit conn: Conn): Seq[(I1, I2, I3)] = {
    val (in1, in2, in3): (Seq[I1], Seq[I2], Seq[I3]) = and3 match {
      case And3(TermValue(v1), TermValue(v2), TermValue(v3)) => (Seq(v1), Seq(v2), Seq(v3))
      case And3(or1: Or[I1], TermValue(v2), TermValue(v3))   => (resolveOr(or1), Seq(v2), Seq(v3))
      case And3(TermValue(v1), or2: Or[I2], TermValue(v3))   => (Seq(v1), resolveOr(or2), Seq(v3))
      case And3(TermValue(v1), TermValue(v2), or3: Or[I3])   => (Seq(v1), Seq(v2), resolveOr(or3))
      case And3(or1: Or[I1], or2: Or[I2], TermValue(v3))     => (resolveOr(or1), resolveOr(or2), Seq(v3))
      case And3(or1: Or[I1], TermValue(v2), or3: Or[I3])     => (resolveOr(or1), Seq(v2), resolveOr(or3))
      case And3(TermValue(v1), or2: Or[I2], or3: Or[I3])     => (Seq(v1), resolveOr(or2), resolveOr(or3))
      case And3(or1: Or[I1], or2: Or[I2], or3: Or[I3])       => (resolveOr(or1), resolveOr(or2), resolveOr(or3))
      case _                                                 => throw new InputMolecule_3_Exception(s"Unexpected And3 expression: " + and3)

    }
    triple(in1, in2, in3)
  }

  protected def bindTuples(query: Query, inputTuples: Seq[(I1, I2, I3)]) = {
    val (vars, Seq(p1, p2, p3)) = varsAndPrefixes(query).unzip
    val values = inputTuples.map { tpl =>
      val v1 = if (p1.nonEmpty) p1 + tpl._1 else tpl._1
      val v2 = if (p2.nonEmpty) p2 + tpl._2 else tpl._2
      val v3 = if (p3.nonEmpty) p3 + tpl._3 else tpl._3
      Seq(v1, v2, v3)
    }
    query.copy(i = In(Seq(InVar(RelationBinding(vars), values)), query.i.rules, query.i.ds))
  }


  /** Resolve input molecule by applying 3 input values as individual args
    * {{{
    *   // Sample data set
    *   Person.name.profession.age.score insert List(
    *     ("Ann", "doctor", 37, 1.0),
    *     ("Ben", "teacher", 37, 1.0),
    *     ("Joe", "teacher", 32, 1.0),
    *     ("Liz", "teacher", 28, 2.0)
    *   )
    *
    *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
    *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
    *
    *   // Apply 3 input values
    *   profAgeScore.apply("doctor", 37, 1.0).get === List("Ann")
    * }}}
    *
    * @param i1   Input value matching first input attribute (profession: String)
    * @param i2   Input value matching second input attribute (age: Int)
    * @param i3   Input value matching third input attribute (score: Double)
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(i1: I1, i2: I2, i3: I3)(implicit conn: Conn): MoleculeBase

  
  /** Resolve input molecule by applying Seq of value triples
    * {{{
    *   // Sample data set
    *   Person.name.profession.age.score insert List(
    *     ("Ann", "doctor", 37, 1.0),
    *     ("Ben", "teacher", 37, 1.0),
    *     ("Joe", "teacher", 32, 1.0),
    *     ("Liz", "teacher", 28, 2.0)
    *   )
    *
    *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
    *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
    *
    *   // Apply Seq of one or more value triples, each matching all 3 input attributes
    *   profAgeScore.apply(Seq(("doctor", 37, 1.0))).get === List("Ann")
    *   profAgeScore.apply(Seq(("doctor", 37, 1.0), ("teacher", 37, 1.0))).get.sorted === List("Ann", "Ben")
    * }}}
    *
    * @param ins  Seq of value triples, each matching the 3 input attributes
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(ins: Seq[(I1, I2, I3)])(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying one or more value triples
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age.score insert List(
    *     ("Ann", "doctor", 37, 1.0),
    *     ("Ben", "teacher", 37, 1.0),
    *     ("Joe", "teacher", 32, 1.0),
    *     ("Liz", "teacher", 28, 2.0)
    *   )
    *
    *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
    *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
    *
    *   // Apply one or more value triples, each matching all 3 input attributes
    *   profAgeScore.apply(("doctor", 37, 1.0)).get === List("Ann")
    *   profAgeScore.apply(("doctor", 37, 1.0), ("teacher", 37, 1.0)).get.sorted === List("Ann", "Ben")
    * }}}
    *
    * @param tpl  First triple of values matching the 3 input attributes
    * @param tpls Optional more pairs of values matching both input attributes
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying 3 Seq of values, one for each of the 3 input attributes
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age.score insert List(
    *     ("Ann", "doctor", 37, 1.0),
    *     ("Ben", "teacher", 37, 1.0),
    *     ("Joe", "teacher", 32, 1.0),
    *     ("Liz", "teacher", 28, 2.0)
    *   )
    *
    *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
    *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
    *
    *   // Apply 3 Seq of values, each matching one of the input attributes
    *   proOfAge(Seq("doctor"), Seq(37)).get === List("Ann")
    *   proOfAge(Seq("doctor", "teacher"), Seq(37, 32)).get.sorted === List("Ann", "Ben", "Joe")
    *
    *   // Number of arguments in each Seq don't have to match but can be asymmetric
    *   profAgeScore.apply(Seq("doctor"), Seq(37), Seq(1.0)).get === List("Ann")
    *   profAgeScore.apply(Seq("doctor", "teacher"), Seq(37), Seq(1.0)).get.sorted === List("Ann", "Ben")
    * }}}
    *
    * @param in1  Seq of values matching first input attribute (professions: Seq[String])
    * @param in2  Seq of values matching second input attribute (ages: Seq[Int])
    * @param in3  Seq of values matching third input attribute (scores: Seq[Double])
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying 3 expressions, one for each of the 3 input attributes
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age.score insert List(
    *     ("Ann", "doctor", 37, 1.0),
    *     ("Ben", "teacher", 37, 1.0),
    *     ("Joe", "teacher", 32, 1.0),
    *     ("Liz", "teacher", 28, 2.0)
    *   )
    *
    *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
    *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
    *
    *   // Apply 3 expressions, one for each input attribute
    *   // [profession-expression] and [age-expression] and [score-expression]
    *   profAgeScore.apply("doctor" and 37 and 1.0).get === List("Ann")
    *   profAgeScore.apply(("doctor" or "teacher") and 37 and 1.0).get.sorted === List("Ann", "Ben")
    *   profAgeScore.apply(("doctor" or "teacher") and (37 or 32) and 1.0).get.sorted === List("Ann", "Ben", "Joe")
    *   profAgeScore.apply(("doctor" or "teacher") and (37 or 32) and (1.0 or 2.0)).get.sorted === List("Ann", "Ben", "Joe")
    * }}}
    *
    * @param and  First input expr `and` second input expr `and` third input expr
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(and: And3[I1, I2, I3])(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying one or more triples of expressions, each matching the 3 input attributes
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age.score insert List(
    *     ("Ann", "doctor", 37, 1.0),
    *     ("Ben", "teacher", 37, 1.0),
    *     ("Joe", "teacher", 32, 1.0),
    *     ("Liz", "teacher", 28, 2.0)
    *   )
    *
    *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
    *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
    *
    *   // Apply two or more tripple expressions, each matchin all 3 input attributes
    *   // [profession/age/score-expression] or [profession/age/score-expression] or ...
    *   profAgeScore.apply(("doctor" and 37 and 1.0) or ("teacher" and 32 and 1.0)).get.sorted === List("Ann", "Joe")
    * }}}
    *
    * @param or   Two or more tuple3 expressions separated by `or`
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(or: Or3[I1, I2, I3])(implicit conn: Conn): MoleculeBase
}


/** Implementations of input molecules awaiting 3 inputs, output arity 1-22 */
object InputMolecule_3 {

  abstract class InputMolecule_3_01[I1, I2, I3, A](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule01[A] // generated by macros
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule01[A] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule01[A] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule01[A] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule01[A] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_02[I1, I2, I3, A, B](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule02[A, B] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule02[A, B]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule02[A, B] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule02[A, B] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule02[A, B] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_03[I1, I2, I3, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule03[A, B, C] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule03[A, B, C]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule03[A, B, C] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule03[A, B, C] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_04[I1, I2, I3, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule04[A, B, C, D] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule04[A, B, C, D] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule04[A, B, C, D] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_05[I1, I2, I3, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_06[I1, I2, I3, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_07[I1, I2, I3, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_08[I1, I2, I3, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_09[I1, I2, I3, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_10[I1, I2, I3, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_11[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_12[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr3(or))(conn)
  }
  
  abstract class InputMolecule_3_22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2, i3)))(conn)
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(tpl +: tpls)(conn)
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(triple(in1, in2, in3))(conn)
    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveAnd3(and))(conn)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr3(or))(conn)
  }
}