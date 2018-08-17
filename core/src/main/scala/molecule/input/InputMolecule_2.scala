package molecule.input

import molecule.action.Molecule._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query._
import molecule.facade.Conn
import molecule.input.exception.{InputMoleculeException, InputMolecule_2_Exception}


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
  *   // 6 ways of resolving input molecule:
  *
  *   // 1. Two input values
  *   profAge("doctor", 37).get === List("Ann")
  *
  *   // 2. Seq of one or more value pairs matching input attributes
  *   profAge(Seq(("doctor", 37))).get === List("Ann")
  *   profAge(Seq(("doctor", 37), ("teacher", 37))).get.sorted === List("Ann", "Ben")
  *
  *   // 3. One or more value pairs matching input attributes
  *   profAge(("doctor", 37)).get === List("Ann")
  *   profAge(("doctor", 37), ("teacher", 37)).get.sorted === List("Ann", "Ben")
  *
  *   // 4. Two Seq of values, one for each input attribute
  *   profAge(Seq("doctor"), Seq(37)).get === List("Ann")
  *   profAge(Seq("doctor", "teacher"), Seq(37)).get.sorted === List("Ann", "Ben")
  *   profAge(Seq("teacher"), Seq(37, 32)).get.sorted === List("Ben", "Joe")
  *   profAge(Seq("doctor", "teacher"), Seq(37, 32)).get.sorted === List("Ann", "Ben", "Joe")
  *
  *   // 5. Two expressions, one for each input attribute
  *   // [profession-expression] and [age-expression]
  *   profAge("doctor" and 37).get === List("Ann")
  *   profAge(("doctor" or "teacher") and 37).get.sorted === List("Ann", "Ben")
  *   profAge(("doctor" or "teacher") and (32 or 28)).get.sorted === List("Joe", "Liz")
  *
  *   // 6. Two or more pair-wise expressions, each matching both attributes
  *   // [pair-expression] or [pair-expression] or ...
  *   profAge(("doctor" and 37) or ("teacher" and 32)).get.sorted === List("Ann", "Joe")
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

  //  private def checkNils(query: Query, inputTuples: Seq[(I1, I2)]): (Int, Int) = {
  //    val (nils1, nils2) = inputTuples.distinct.foldLeft(0, 0) {
  //      case ((nils1, nils2), (a: Set[_], b: Set[_])) if a.isEmpty && b.isEmpty => (nils1 + 1, nils2 + 1)
  //      case ((nils1, nils2), (a: Set[_], b)) if a.isEmpty                      => (nils1 + 1, nils2 + 0)
  //      case ((nils1, nils2), (a, b: Set[_])) if b.isEmpty                      => (nils1 + 0, nils2 + 1)
  //      case ((nils1, nils2), (a, b))                                           => (nils1 + 0, nils2 + 0)
  //    }
  //    //    println("nils1: " + nils1)
  //    //    println("nils2: " + nils2)
  //
  //    if (nils1 > 0 && nils1 != inputTuples.size)
  //      query.i.inputs.collectFirst {
  //        case Placeholder(_, KW(ns, attr, _), _, _) => throw new InputMolecule_2_Exception(
  //          s"[InputMolecule_2] Can't mix empty and non-empty Sets of first input data for card-many attribute `:$ns/$attr`:\n" + inputTuples)
  //      }.getOrElse(throw new InputMolecule_2_Exception(s"[InputMolecule_2] Can't find placeholder for first input"))
  //
  //    if (nils2 > 0 && nils2 != inputTuples.size) {
  //      val kws = query.i.inputs.collect {
  //        case Placeholder(_, KW(ns, attr, _), _, _) => (ns, attr)
  //      }
  //      if (kws.size != 2)
  //        throw new InputMolecule_2_Exception(s"[InputMolecule_2] Query should expect exactly 2 inputs:\nQuery: ${query.datalog}")
  //
  //      val (ns, attr) = kws.last
  //      throw new InputMolecule_2_Exception(s"[InputMolecule_2] Can't mix empty and non-empty Sets of second input data for card-many attribute `:$ns/$attr`:\n" + inputTuples)
  //    }
  //    (nils1, nils2)
  //  }
  //
  //  private def bindNil(query: Query, inputTuples: Seq[(I1, I2)]) = {
  //
  //    val inputs = inputTuples.distinct.flatMap {
  //      case (set1: Set[_], set2: Set[_]) => for {i1 <- set1; i2 <- set2} yield Seq(i1, i2)
  //      case (a, set2: Set[_])            => for {i2 <- set2} yield Seq(a, i2)
  //      case (set1: Set[_], b)            => for {i1 <- set1} yield Seq(i1, b)
  //      case (a, b)                       => Seq(Seq(a, b))
  //    }
  //    val query1 = if (inputTuples.isEmpty) {
  //      // Both input attributes to be non-asserted
  //      val vars = query.i.inputs.collect { case Placeholder(v, _, _, _) => v }
  //      val newWhere = Where(query.wh.clauses.map {
  //        case DataClause(_, e, a, Var(v), _, _) if vars.contains(v) => NotClause(ImplDS, e, a)
  //        case otherClause                                           => otherClause
  //      })
  //      // Remove placeholders
  //      val newIn = In(Seq(), query.i.rules, query.i.ds)
  //      query.copy(i = newIn, wh = newWhere)
  //    } else {
  //      val (vars, _) = varsAndPrefixes(query).unzip
  //      query.copy(i = In(Seq(InVar(RelationBinding(vars), inputs)), query.i.rules, query.i.ds))
  //    }
  //    //    println("@@@@ bindPairs @@@@@@@@@@@@@@@@@@@@")
  //    //    println("inputTuples0: " + inputTuples)
  //    //    println("inputs      : " + inputs)
  //    //    println(query)
  //    //    println("-------------")
  //    //    println(query1)
  //
  //    query1
  //  }
  //
  //
  //  private def bindSingle[In](query: Query, inputs: Seq[In], i: Int) = {
  //    val List(Placeholder(v1, kw1, enumPrefix1, e1), Placeholder(v0, kw0, enumPrefix0, e0)) = if (i == 1) query.i.inputs else query.i.inputs.reverse
  //    val remainingPlaceholders = query.i.inputs.collect {
  //      case ph@Placeholder(`v1`, `kw1`, `enumPrefix1`, `e1`) => ph
  //    }
  //
  //    // Nil non-asserted
  //    val newWhere = Where(query.wh.clauses.flatMap {
  //      case DataClause(_, Var(`e0`), `kw0`, Var(`v0`), _, _) => Seq(NotClause(ImplDS, Var(e0), kw0))
  //      case otherClause                                      => Seq(otherClause)
  //    })
  //    // Remove placeholder
  //    val newIn = query.i.copy(inputs = remainingPlaceholders)
  //    val query1 = query.copy(i = newIn, wh = newWhere)
  //    val query2 = resolveVarsInputs[In](query1, Var(v1), inputs, enumPrefix1)
  //
  //    //    println("@@@@ bindSingle @@@@@@@@@@@@@@@@@@@@")
  //    //    println("inputs      : " + inputs)
  //    //    println(query)
  //    //    println("-------------")
  //    //    println(query1)
  //    //    println("-------------")
  //    //    println(query2)
  //
  //    query2
  //  }
  //
  //  private def bindPair(query: Query, inputTuples: Seq[(I1, I2)]) = {
  //    val inputs = inputTuples.distinct.flatMap {
  //      case (set1: Set[_], set2: Set[_]) => for {i1 <- set1; i2 <- set2} yield Seq(i1, i2)
  //      case (a, set2: Set[_])            => for {i2 <- set2} yield Seq(a, i2)
  //      case (set1: Set[_], b)            => for {i1 <- set1} yield Seq(i1, b)
  //      case (a, b)                       => Seq(Seq(a, b))
  //    }
  //    val query1 = if (inputTuples.isEmpty) {
  //      // Both input attributes to be non-asserted
  //      val vars = query.i.inputs.collect { case Placeholder(v, _, _, _) => v }
  //      val newWhere = Where(query.wh.clauses.map {
  //        case DataClause(_, e, a, Var(v), _, _) if vars.contains(v) => NotClause(ImplDS, e, a)
  //        case otherClause                                           => otherClause
  //      })
  //      // Remove placeholders
  //      val newIn = In(Seq(), query.i.rules, query.i.ds)
  //      query.copy(i = newIn, wh = newWhere)
  //    } else {
  //      val (vars, _) = varsAndPrefixes(query).unzip
  //      query.copy(i = In(Seq(InVar(RelationBinding(vars), inputs)), query.i.rules, query.i.ds))
  //    }
  //    //    println("@@@@ bindPairs @@@@@@@@@@@@@@@@@@@@")
  //    //    println("inputTuples0: " + inputTuples)
  //    //    println("inputs      : " + inputs)
  //    //    println(query)
  //    //    println("-------------")
  //    //    println(query1)
  //
  //    query1
  //  }

  protected def bindTuples(query: Query, inputTuples: Seq[(I1, I2)]) = {

    val List(Placeholder(e1@Var(ex1), kw1@KW(ns1, attr1, _), v1, enumPrefix1), Placeholder(e2@Var(ex2), kw2@KW(ns2, attr2, _), v2, enumPrefix2)) = query.i.inputs
    val (isTacit1, isTacit2) = (isTacit(ns1, attr1), isTacit(ns2, attr2))
    val (isComparison1, isComparison2) = (isComparison(ns1, attr1), isComparison(ns2, attr2))

    inputTuples match {

      case Nil => {
        // Both input attributes to be non-asserted
        val newClauses1 = addNilClause(query.wh.clauses, e1, kw1, v1)
        val newClauses2 = addNilClause(newClauses1, e2, kw2, v2)

        // Remove placeholders
        val newIn = In(Seq(), query.i.rules, query.i.ds)
        query.copy(i = newIn, wh = Where(newClauses2))
      }

      case pairs if isComparison1 || isComparison2 => {
        if (pairs.size > 1)
          throw new InputMolecule_2_Exception(s"Can't apply multiple pairs to input molecule with one or more comparison functions (each expecting a single comparison value).")

        val newClauses1 = addArgClause(query.wh.clauses, e1, kw1, v1, isComparison1, isTacit1, enumPrefix1, pairs.head._1)
        val newClauses2 = addArgClause(newClauses1, e2, kw2, v2, isComparison2, isTacit2, enumPrefix2, pairs.head._2)
        val newIn = query.i.copy(inputs = Nil)
        query.copy(i = newIn, wh = Where(newClauses2))
      }

      case pairs => {
        // Add rule invocation clause to first input attribute clause (only 1 rule invocation needed)
        val (done, resolvedClauses) = query.wh.clauses.foldLeft(false, Seq.empty[Seq[Clause]]) {
          case ((done, acc), DataClause(_, _, _, `v1`, _, _)) if isTacit1 => (true, acc :+ Seq(RuleInvocation("rule1", List(e1))))
          case ((done, acc), cl@DataClause(_, _, _, `v1`, _, _))          => (true, acc :+ Seq(cl, RuleInvocation("rule1", List(e1))))
          case ((done, acc), DataClause(_, _, _, `v2`, _, _)) if isTacit2 => (done, acc)
          case ((done, acc), otherClause)                                 => (done, acc :+ Seq(otherClause))
        }
        val newClauses = if (done) resolvedClauses.flatten else
          throw new InputMolecule_2_Exception(s"Couldn't find clause with first input attribute `:$ns1/$attr1` placeholder variable `$v1` in query:\n" + query)

        val rules = pairs.map { case (arg1, arg2) =>
          Rule("rule1", List(e1), valueClauses(ex1, kw1, enumPrefix1, arg1) ++ valueClauses(ex2, kw2, enumPrefix2, arg2))
        }
        query.copy(i = In(Nil, query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
      }
    }
  }

  protected def bindSeqs(query: Query, input10: Seq[I1], input20: Seq[I2]) = {
    val (input1, input2) = (input10.distinct, input20.distinct)

    // Extract placeholder info and discard placeholders
    val varsAndPrefixes = query.i.inputs.collect {
      case Placeholder(e, kw, _, enumPrefix) => (kw, enumPrefix, e)
    }
    val query1 = query.copy(i = In(Seq(), query.i.rules, query.i.ds))

    if (varsAndPrefixes.size != 2)
      throw new InputMolecule_2_Exception(s"[InputMolecule_2] Query should expect exactly 2 inputs:\nQuery: ${query.datalog}")


    def addRulesAndClauses[T](query: Query, ruleName: String, kw: KW, enumPrefix: Option[String], e: Var, inputs: Seq[T]): Query = {
      val newRules: Seq[Rule] = query.wh.clauses.flatMap {
        case DataClause(_, `e`, `kw`, _, _, _) if inputs.isEmpty => Nil
        case DataClause(_, `e`, `kw`, _, _, _)                   => inputs.flatMap {
          case set: Set[_] => set.toSeq.map { arg => Rule(ruleName, Seq(e), Seq(DataClause(ImplDS, e, kw, Val(pre(enumPrefix, arg)), Empty))) }
          case arg         => Seq(Rule(ruleName, Seq(e), Seq(DataClause(ImplDS, e, kw, Val(pre(enumPrefix, arg)), Empty))))
        }
        case _                                                   => Nil
      }

      val newClauses: Seq[Clause] = query.wh.clauses.flatMap {
        case DataClause(_, `e`, `kw`, _, _, _) if inputs.isEmpty => List(NotClause(e, kw))
        case cl@DataClause(_, `e`, `kw`, _, _, _)                => List(cl, RuleInvocation(ruleName, Seq(e)))
        case otherClause                                         => List(otherClause)
      }

      val newIn = query.i.copy(rules = query.i.rules ++ newRules)
      query.copy(i = newIn, wh = Where(newClauses))
    }

    val List((kw1, enumPrefix1, e1), (kw2, enumPrefix2, e2)) = varsAndPrefixes
    val query2 = addRulesAndClauses(query1, "rule1", kw1, enumPrefix1, e1, input1)
    val query3 = addRulesAndClauses(query2, "rule2", kw2, enumPrefix2, e2, input2)
    query3
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

  abstract class InputMolecule_2_01[I1, I2, A](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2)))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule01[A] = apply(tpl +: tpls)
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule01[A] = apply(resolveOr2(or))
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule01[A] // generated by macro

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule01[A] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule01[A] // generated by macro
  }

  abstract class InputMolecule_2_02[I1, I2, A, B](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule02[A, B] = apply(Seq((i1, i2)))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule02[A, B] = apply(tpl +: tpls)
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule02[A, B] = apply(resolveOr2(or))
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule02[A, B]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule02[A, B] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule02[A, B]
  }

  abstract class InputMolecule_2_03[I1, I2, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule03[A, B, C] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule03[A, B, C] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule03[A, B, C]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule03[A, B, C] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule03[A, B, C]
  }

  abstract class InputMolecule_2_04[I1, I2, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule04[A, B, C, D]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule04[A, B, C, D] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule04[A, B, C, D]
  }

  abstract class InputMolecule_2_05[I1, I2, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule05[A, B, C, D, E]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule05[A, B, C, D, E] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule05[A, B, C, D, E]
  }

  abstract class InputMolecule_2_06[I1, I2, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule06[A, B, C, D, E, F]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule06[A, B, C, D, E, F]
  }

  abstract class InputMolecule_2_07[I1, I2, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
  }

  abstract class InputMolecule_2_08[I1, I2, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
  }

  abstract class InputMolecule_2_09[I1, I2, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
  }

  abstract class InputMolecule_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
  }

  abstract class InputMolecule_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
  }

  abstract class InputMolecule_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
  }

  abstract class InputMolecule_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
  }

  abstract class InputMolecule_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  }

  abstract class InputMolecule_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  }

  abstract class InputMolecule_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  }

  abstract class InputMolecule_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  }

  abstract class InputMolecule_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  }

  abstract class InputMolecule_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  }

  abstract class InputMolecule_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  }

  abstract class InputMolecule_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  }

  abstract class InputMolecule_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]

    def apply(and: And2[I1, I2])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = {val (in1, in2) = resolveAnd2(and); apply(in1, in2)}
    def apply(in1: Seq[I1], in2: Seq[I2])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  }
}

/*

/** Implementations of input molecules awaiting 2 inputs, output arity 1-22 */
object InputMolecule_2 {

  abstract class InputMolecule_2_01[I1, I2, A](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2)))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule01[A] = apply(tpl +: tpls)
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule01[A] = apply(resolveOr2(or))
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule01[A] // generated by macro

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule01[A] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule01[A] // generated by macro
  }

  abstract class InputMolecule_2_02[I1, I2, A, B](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule02[A, B] = apply(Seq((i1, i2)))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule02[A, B] = apply(tpl +: tpls)
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr2(or))
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule02[A, B]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule02[A, B] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule02[A, B]
  }

  abstract class InputMolecule_2_03[I1, I2, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule03[A, B, C] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule03[A, B, C] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule03[A, B, C]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule03[A, B, C] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule03[A, B, C]
  }

  abstract class InputMolecule_2_04[I1, I2, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule04[A, B, C, D] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule04[A, B, C, D]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule04[A, B, C, D] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule04[A, B, C, D]
  }

  abstract class InputMolecule_2_05[I1, I2, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule05[A, B, C, D, E]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule05[A, B, C, D, E] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule05[A, B, C, D, E]
  }

  abstract class InputMolecule_2_06[I1, I2, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule06[A, B, C, D, E, F]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
  }

  abstract class InputMolecule_2_07[I1, I2, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
  }

  abstract class InputMolecule_2_08[I1, I2, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
  }

  abstract class InputMolecule_2_09[I1, I2, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
  }

  abstract class InputMolecule_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
  }

  abstract class InputMolecule_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
  }

  abstract class InputMolecule_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
  }

  abstract class InputMolecule_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
  }

  abstract class InputMolecule_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  }

  abstract class InputMolecule_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  }

  abstract class InputMolecule_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  }

  abstract class InputMolecule_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  }

  abstract class InputMolecule_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  }

  abstract class InputMolecule_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  }

  abstract class InputMolecule_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  }

  abstract class InputMolecule_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  }

  abstract class InputMolecule_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
    def apply(i1: I1, i2: I2)                (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2)))
    def apply(or: Or2[I1, I2])               (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr2(or))
    def apply(tpl: (I1, I2), tpls: (I1, I2)*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(tpl +: tpls)
    def apply(ins: Seq[(I1, I2)])            (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]

    def apply(and: And2[I1, I2])             (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = { val (in1, in2) = resolveAnd2(and); apply(in1, in2) }
    def apply(in1: Seq[I1], in2: Seq[I2])    (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  }
}

*/