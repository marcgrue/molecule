package molecule.input

import molecule.api.Molecule._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query._
import molecule.facade.Conn
import molecule.input.exception.InputMolecule_2_Exception


/** Shared interfaces of input molecules awaiting 2 inputs.
  * {{{
  *   // Sample data set
  *   Person.name.profession.age insert List(
  *     ("Ann", "doctor", 37),
  *     ("Ben", "teacher", 37),
  *     ("Joe", "teacher", 32),
  *     ("Liz", "teacher", 28)
  *   )
  *
  *   // Input molecule awaiting 2 inputs for `profession` and `age`
  *   val profAge = m(Person.name.profession_(?).age_(?))
  *
  *
  *   // A. Pairs of input .................................
  *
  *   // One pair as params
  *   profAge("doctor", 37).get === List("Ann")
  *
  *   // One or more pairs
  *   profAge(("doctor", 37)).get === List("Ann")
  *   profAge(("doctor", 37), ("teacher", 37)).get.sorted === List("Ann", "Ben")
  *
  *   // One or more logical pairs
  *   // [pair-expression] or [pair-expression] or ...
  *   profAge(("doctor" and 37) or ("teacher" and 32)).get.sorted === List("Ann", "Joe")
  *   profAge(Seq(("doctor", 37), ("teacher", 37))).get.sorted === List("Ann", "Ben")
  *
  *   // List of pairs
  *   profAge(Seq(("doctor", 37))).get === List("Ann")
  *
  *
  *   // B. 2 groups of input, one for each input attribute .................................
  *
  *   // Two expressions
  *   // [profession-expression] and [age-expression]
  *   profAge("doctor" and 37).get === List("Ann")
  *   profAge(("doctor" or "teacher") and 37).get.sorted === List("Ann", "Ben")
  *   profAge(("doctor" or "teacher") and (32 or 28)).get.sorted === List("Joe", "Liz")
  *
  *   // Two Lists
  *   profAge(Seq("doctor"), Seq(37)).get === List("Ann")
  *   profAge(Seq("doctor", "teacher"), Seq(37)).get.sorted === List("Ann", "Ben")
  *   profAge(Seq("teacher"), Seq(37, 32)).get.sorted === List("Ben", "Joe")
  *   profAge(Seq("doctor", "teacher"), Seq(37, 32)).get.sorted === List("Ann", "Ben", "Joe")
  * }}}
  *
  * @see [[molecule.input.InputMolecule]]
  *      | [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  * @tparam I1 Type of input matching first attribute with `?` marker (profession: String)
  * @tparam I2 Type of input matching second attribute with `?` marker (age: Int)
  */
trait InputMolecule_2[I1, I2] extends InputMolecule {

  protected def resolveOr2(or: Or2[I1, I2]): Seq[(I1, I2)] = {
    def traverse(expr: Or2[I1, I2]): Seq[(I1, I2)] = expr match {
      case Or2(And2(TermValue(a1), TermValue(a2)), And2(TermValue(b1), TermValue(b2))) => Seq((a1, a2), (b1, b2))
      case Or2(And2(TermValue(a1), TermValue(a2)), or2: Or2[I1, I2])                   => (a1, a2) +: traverse(or2)
      case Or2(or1: Or2[I1, I2], And2(TermValue(b1), TermValue(b2)))                   => traverse(or1) :+ (b1, b2)
      case Or2(or1: Or2[I1, I2], or2: Or2[I1, I2])                                     => traverse(or1) ++ traverse(or2)
      case _                                                                           => throw new InputMolecule_2_Exception(s"Unexpected Or2 expression: " + expr)
    }
    traverse(or).distinct
  }

  protected def resolveAnd2(and2: And2[I1, I2]): (Seq[I1], Seq[I2]) = and2 match {
    case And2(TermValue(v1), TermValue(v2)) => (Seq(v1), Seq(v2))
    case And2(or1: Or[I1], TermValue(v2))   => (resolveOr(or1), Seq(v2))
    case And2(TermValue(v1), or2: Or[I2])   => (Seq(v1), resolveOr(or2))
    case And2(or1: Or[I1], or2: Or[I2])     => (resolveOr(or1), resolveOr(or2))
    case _                                  => throw new InputMolecule_2_Exception(s"Unexpected And2 expression: " + and2)
  }


  // Pairs
  protected def bindValues(query: Query, inputTuples: Seq[(I1, I2)]): Query = {
    val ph1@Placeholder(e1@Var(ex1), kw1@KW(ns1, attr1, _), v1@Var(w1), enumPrefix1) = query.i.inputs.head
    val ph2@Placeholder(e2@Var(ex2), kw2@KW(ns2, attr2, _), v2@Var(w2), enumPrefix2) = query.i.inputs(1)
    val (v1_, v2_) = (w1.filter(_.isLetter), w2.filter(_.isLetter))
    val (isTacit1, isTacit2) = (isTacit(ns1, attr1), isTacit(ns2, attr2))
    val (isExpr1, isExpr2) = (isExpression(ns1, attr1), isExpression(ns2, attr2))
    val hasExpression = isExpr1 || isExpr2
    val es = List(e1, e2).distinct // same or different namespaces

    // Discard placeholders
    val q0 = query.copy(i = In(Nil, query.i.rules, query.i.ds))

    inputTuples.distinct match {

      case Nil if !isTacit1 || !isTacit2 =>
        throw new InputMolecule_2_Exception("Can only apply empty list of pairs (Nil) to two tacit attributes")

      case Nil => {
        // Both input attributes to be non-asserted
        val newClauses1 = addNilClause(query.wh.clauses, e1, kw1, v1)
        val newClauses2 = addNilClause(newClauses1, e2, kw2, v2)

        // Remove placeholders
        q0.copy(wh = Where(newClauses2))
      }

      case pairs if pairs.size > 1 && hasExpression => throw new InputMolecule_2_Exception(
        "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)")

      // 1 pair, possibly with expressions
      case Seq((arg1, arg2)) => {
        val q1 = resolveInput(q0, ph1, Seq(arg1), "rule1", true)
        val q2 = resolveInput(q1, ph2, Seq(arg2), "rule1", true)
        q2
      }

      // Multiple pairs without expressions
      case pairs => {

        // Add rule invocation clause to first input attribute clause (only 1 rule invocation needed)
        val rule = RuleInvocation("rule1", es)
        val ident1 = Var(v1_ + 1)
        val ident2 = Var(v2_ + 1)
        val (done, resolvedClauses) = query.wh.clauses.foldLeft(0, Seq.empty[Seq[Clause]]) {
          case ((n, acc), cl@DataClause(_, _, KW("db", "ident", _), `ident1`, _, _)) if isTacit1 => (n, acc)
          case ((_, acc), cl@DataClause(_, _, _, `v1`, _, _)) if isTacit1                        => (1, acc :+ Seq(rule))
          case ((_, acc), cl@DataClause(_, _, _, `v1`, _, _))                                    => (1, acc :+ Seq(cl, rule))
          case ((n, acc), cl@DataClause(_, _, `kw1`, Var(v), _, _)) if isTacit1 && v == v1_      => (n, acc)
          case ((_, acc), fn@Funct(_, List(_, `v1`), _))                                         => (1, acc :+ Seq(fn, rule))
          case ((_, acc), fn@Funct(_, List(_, _, `v1`), _))                                      => (1, acc :+ Seq(fn, rule))
          case ((_, acc), fn@Funct(_, _, ScalarBinding(`v1`))) if isTacit1                       => (1, acc :+ Seq(rule))
          case ((_, acc), fn@Funct(_, _, ScalarBinding(`v1`)))                                   => (1, acc :+ Seq(fn, rule))

          case ((n, acc), cl@DataClause(_, _, KW("db", "ident", _), `ident2`, _, _)) if isTacit2 => (n, acc)
          case ((n, acc), cl@DataClause(_, _, _, `v2`, _, _)) if isTacit2                        => (n + 1, acc)
          case ((n, acc), cl@DataClause(_, _, _, `v2`, _, _))                                    => (n + 1, acc :+ Seq(cl))
          case ((n, acc), cl@DataClause(_, _, `kw2`, Var(v), _, _)) if isTacit2 && v == v2_      => (n, acc)
          case ((n, acc), fn@Funct(_, List(_, `v2`), _))                                         => (n + 1, acc :+ Seq(fn))
          case ((n, acc), fn@Funct(_, List(_, _, `v2`), _))                                      => (n + 1, acc :+ Seq(fn))
          case ((n, acc), fn@Funct(_, _, ScalarBinding(`v2`))) if isTacit2                       => (n + 1, acc)
          case ((n, acc), fn@Funct(_, _, ScalarBinding(`v2`)))                                   => (n + 1, acc :+ Seq(fn))
          case ((n, acc), otherClause)                                                           => (n, acc :+ Seq(otherClause))
        }

        val newClauses = if (done == 2) resolvedClauses.flatten else
          throw new InputMolecule_2_Exception(s"Couldn't find clauses in query that match placeholders:" +
            s"\nplaceholder 1: " + ph1 +
            s"\nplaceholder 2: " + ph2 +
            s"\nnew clauses  :\n  " + resolvedClauses.mkString("\n  ") +
            s"\n" + query
          )

        val rules = pairs.map { case (arg1, arg2) =>
          Rule("rule1", es, valueClauses(ex1, kw1, enumPrefix1, arg1) ++ valueClauses(ex2, kw2, enumPrefix2, arg2))
        }
        q0.copy(i = In(Nil, query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
      }
    }
  }


  protected def bindSeqs(query: Query, inputRaw1: Seq[I1], inputRaw2: Seq[I2]) = {
    val (input1, input2) = (inputRaw1.distinct, inputRaw2.distinct)
    val List(
    ph1@Placeholder(_, KW(nsFull1, attr1, _), _, _),
    ph2@Placeholder(_, KW(nsFull2, attr2, _), _, _)
    ) = query.i.inputs

    def resolve[T](query: Query, ph: Placeholder, input: Seq[T], ruleName: String, tacit: Boolean, expr: Boolean): Query = {
      val Placeholder(_, KW(nsFull, attr, _), _, _) = ph
      input match {
        case Nil if !tacit =>
          throw new InputMolecule_2_Exception(s"Can only apply empty list (Nil) to a tacit input attribute. Please make input attr tacit: `$attr` --> `${attr}_`")

        case in if expr && in.size > 1 =>
          throw new InputMolecule_2_Exception(s"Can't apply multiple values to input attribute `:$nsFull/$attr` having expression (<, >, <=, >=, !=)")

        case in =>
          resolveInput(query, ph, in, ruleName)
      }
    }

    // Discard placeholders
    val q0 = query.copy(i = In(Seq(), query.i.rules, query.i.ds))

    // Resolve inputs
    val q1 = resolve(q0, ph1, input1, "rule1", isTacit(nsFull1, attr1), isExpression(nsFull1, attr1))
    val q2 = resolve(q1, ph2, input2, "rule2", isTacit(nsFull2, attr2), isExpression(nsFull2, attr2))
    q2
  }


  // Paired inputs -----------------------------------------------------------------------------

  /** Resolve input molecule by applying 2 input values as individual args
    * {{{
    *   // Sample data set
    *   Person.name.profession.age insert List(
    *     ("Ann", "doctor", 37),
    *     ("Ben", "teacher", 37),
    *     ("Joe", "teacher", 32),
    *     ("Liz", "teacher", 28)
    *   )
    *
    *   // Input molecule awaiting 2 inputs for `profession` and `age`
    *   val profAge = m(Person.name.profession_(?).age_(?))
    *
    *   // Apply 2 input values
    *   profAge("doctor", 37).get === List("Ann")
    * }}}
    *
    * @param i1   Input value matching first input attribute (profession: String)
    * @param i2   Input value matching second input attribute (age: Int)
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(i1: I1, i2: I2)(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying one or more pairs of expressions, each matching both input attributes
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age insert List(
    *     ("Ann", "doctor", 37),
    *     ("Ben", "teacher", 37),
    *     ("Joe", "teacher", 32),
    *     ("Liz", "teacher", 28)
    *   )
    *
    *   // Input molecule awaiting 2 inputs for `profession` and `age`
    *   val profAge = m(Person.name.profession_(?).age_(?))
    *
    *   // Apply two or more pair expressions, each matching both input attributes
    *   // [profession/age-expression] or [profession/age-expression] or ...
    *   profAge("doctor" and 37).get.sorted === List("Ann")
    *   profAge(("doctor" and 37) or ("teacher" and 32)).get.sorted === List("Ann", "Joe")
    * }}}
    *
    * @param or   Two or more pair-wise expressions separated by `or`
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(or: Or2[I1, I2])(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying one or more value pairs
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age insert List(
    *     ("Ann", "doctor", 37),
    *     ("Ben", "teacher", 37),
    *     ("Joe", "teacher", 32),
    *     ("Liz", "teacher", 28)
    *   )
    *
    *   // Input molecule awaiting 2 inputs for `profession` and `age`
    *   val profAge = m(Person.name.profession_(?).age_(?))
    *
    *   // Apply one or more value pairs, each matching both input attributes
    *   profAge(("doctor", 37)).get === List("Ann")
    *   profAge(("doctor", 37), ("teacher", 37)).get.sorted === List("Ann", "Ben")
    * }}}
    *
    * @param tpl  First pair of values matching both input attributes
    * @param tpls Optional more pairs of values matching both input attributes
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying Seq of value pairs
    * {{{
    *   // Sample data set
    *   Person.name.profession.age insert List(
    *     ("Ann", "doctor", 37),
    *     ("Ben", "teacher", 37),
    *     ("Joe", "teacher", 32),
    *     ("Liz", "teacher", 28)
    *   )
    *
    *   // Input molecule awaiting 2 inputs for `profession` and `age`
    *   val profAge = m(Person.name.profession_(?).age_(?))
    *
    *   // Apply Seq of one or more value pairs, each matching both input attributes
    *   profAge(Seq(("doctor", 37))).get === List("Ann")
    *   profAge(Seq(("doctor", 37), ("teacher", 37))).get.sorted === List("Ann", "Ben")
    * }}}
    *
    * @param ins  Seq of value pairs, each matching both input attributes
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): MoleculeBase


  // Separate inputs -----------------------------------------------------------------------------

  /** Resolve input molecule by applying 2 expressions, one for each input attribute
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age insert List(
    *     ("Ann", "doctor", 37),
    *     ("Ben", "teacher", 37),
    *     ("Joe", "teacher", 32),
    *     ("Liz", "teacher", 28)
    *   )
    *
    *   // Input molecule awaiting 2 inputs for `profession` and `age`
    *   val profAge = m(Person.name.profession_(?).age_(?))
    *
    *   // Apply 2 expressions, one for each input attribute
    *   // [profession-expression] and [age-expression]
    *   profAge("doctor" and 37).get === List("Ann")
    *   profAge(("doctor" or "teacher") and 37).get.sorted === List("Ann", "Ben")
    *   profAge(("doctor" or "teacher") and (32 or 28)).get.sorted === List("Joe", "Liz")
    * }}}
    *
    * @param and  First input expr `and` second input expr
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(and: And2[I1, I2])(implicit conn: Conn): MoleculeBase


  /** Resolve input molecule by applying 2 Seq of values, one for each input attribute
    *
    * {{{
    *   // Sample data set
    *   Person.name.profession.age insert List(
    *     ("Ann", "doctor", 37),
    *     ("Ben", "teacher", 37),
    *     ("Joe", "teacher", 32),
    *     ("Liz", "teacher", 28)
    *   )
    *
    *   // Input molecule awaiting 2 inputs for profession and age
    *   val profAge = m(Person.name.profession_(?).age_(?))
    *
    *   // Apply 2 Seq of values, each matching one of the input attributes
    *   profAge(Seq("doctor"), Seq(37)).get === List("Ann")
    *   profAge(Seq("doctor", "teacher"), Seq(37, 32)).get.sorted === List("Ann", "Ben", "Joe")
    *
    *   // Number of arguments in each Seq don't have to match but can be asymmetric
    *   profAge(Seq("doctor", "teacher"), Seq(37)).get.sorted === List("Ann", "Ben")
    *   profAge(Seq("teacher"), Seq(37, 32)).get.sorted === List("Ben", "Joe")
    * }}}
    *
    * @param in1  Seq of values matching first input attribute (professions: Seq[String])
    * @param in2  Seq of values matching second input attribute (ages: Seq[Int])
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): MoleculeBase
}


/** Implementations of input molecules awaiting 2 inputs, output arity 1-22 */
object InputMolecule_2 {

  abstract class InputMolecule_2_01[I1, I2, A](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2)))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule01[A] = apply(tpl +: tpls)
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule01[A] = apply(resolveOr2(or))
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule01[A] // generated by macro

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule01[A] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule01[A] // generated by macro
  }

  abstract class InputMolecule_2_02[I1, I2, A, B](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule02[A, B] = apply(Seq((i1, i2)))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule02[A, B] = apply(tpl +: tpls)
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr2(or))
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule02[A, B]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule02[A, B] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule02[A, B]
  }

  abstract class InputMolecule_2_03[I1, I2, A, B, C](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule03[A, B, C] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule03[A, B, C] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule03[A, B, C]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule03[A, B, C] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule03[A, B, C]
  }

  abstract class InputMolecule_2_04[I1, I2, A, B, C, D](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule04[A, B, C, D] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule04[A, B, C, D]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule04[A, B, C, D] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule04[A, B, C, D]
  }

  abstract class InputMolecule_2_05[I1, I2, A, B, C, D, E](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule05[A, B, C, D, E]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule05[A, B, C, D, E] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule05[A, B, C, D, E]
  }

  abstract class InputMolecule_2_06[I1, I2, A, B, C, D, E, F](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule06[A, B, C, D, E, F]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
  }

  abstract class InputMolecule_2_07[I1, I2, A, B, C, D, E, F, G](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
  }

  abstract class InputMolecule_2_08[I1, I2, A, B, C, D, E, F, G, H](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
  }

  abstract class InputMolecule_2_09[I1, I2, A, B, C, D, E, F, G, H, I](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
  }

  abstract class InputMolecule_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
  }

  abstract class InputMolecule_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
  }

  abstract class InputMolecule_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
  }

  abstract class InputMolecule_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
  }

  abstract class InputMolecule_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  }

  abstract class InputMolecule_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  }

  abstract class InputMolecule_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  }

  abstract class InputMolecule_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  }

  abstract class InputMolecule_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  }

  abstract class InputMolecule_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  }

  abstract class InputMolecule_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  }

  abstract class InputMolecule_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  }

  abstract class InputMolecule_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_2[I1, I2] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  }
}
