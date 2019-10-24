package molecule.input

import molecule.api.Molecule._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query._
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
  *   // Input molecule awaiting 3 inputs for `profession`, `age` and `score`
  *   val profAgeScore = m(Person.name.profession_(?).age_(?).score_(?))
  *
  *
  *   // A. Triples of input .................................
  *
  *   // One triple as params
  *   profAgeScore.apply("doctor", 37, 1.0).get === List("Ann")
  *
  *   // One or more triples
  *   profAgeScore.apply(("doctor", 37, 1.0)).get === List("Ann")
  *   profAgeScore.apply(("doctor", 37, 1.0), ("teacher", 37, 1.0)).get.sorted === List("Ann", "Ben")
  *
  *   // One or more logical triples
  *   // [triple-expression] or [triple-expression] or ...
  *   profAgeScore.apply(("doctor" and 37 and 1.0) or ("teacher" and 32 and 1.0)).get.sorted === List("Ann", "Joe")
  *
  *   // List of triples
  *   profAgeScore.apply(Seq(("doctor", 37, 1.0))).get === List("Ann")
  *   profAgeScore.apply(Seq(("doctor", 37, 1.0), ("teacher", 37, 1.0))).get.sorted === List("Ann", "Ben")
  *
  *
  *   // B. 3 groups of input, one for each input attribute .................................
  *
  *   // Three expressions
  *   // [profession-expression] and [age-expression] and [score-expression]
  *   profAgeScore.apply("doctor" and 37 and 1.0).get === List("Ann")
  *   profAgeScore.apply(("doctor" or "teacher") and 37 and 1.0).get.sorted === List("Ann", "Ben")
  *   profAgeScore.apply(("doctor" or "teacher") and (37 or 32) and 1.0).get.sorted === List("Ann", "Ben", "Joe")
  *   profAgeScore.apply(("doctor" or "teacher") and (37 or 32) and (1.0 or 2.0)).get.sorted === List("Ann", "Ben", "Joe")
  *
  *   // Three lists
  *   profAgeScore.apply(Seq("doctor"), Seq(37), Seq(1.0)).get === List("Ann")
  *   profAgeScore.apply(Seq("doctor", "teacher"), Seq(37), Seq(1.0)).get.sorted === List("Ann", "Ben")
  * }}}
  *
  * @see [[molecule.input.InputMolecule]]
  *      | [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
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

  protected def resolveAnd3(and3: And3[I1, I2, I3])(implicit conn: Conn): (Seq[I1], Seq[I2], Seq[I3]) = and3 match {
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

  // Triples
  protected def bindValues(query: Query, inputTuples: Seq[(I1, I2, I3)]) = {
    val ph1@Placeholder(e1@Var(ex1), kw1@KW(ns1, attr1, _), v1@Var(w1), enumPrefix1) = query.i.inputs.head
    val ph2@Placeholder(e2@Var(ex2), kw2@KW(ns2, attr2, _), v2@Var(w2), enumPrefix2) = query.i.inputs(1)
    val ph3@Placeholder(e3@Var(ex3), kw3@KW(ns3, attr3, _), v3@Var(w3), enumPrefix3) = query.i.inputs(2)
    val (v1_, v2_, v3_) = (w1.filter(_.isLetter), w2.filter(_.isLetter), w3.filter(_.isLetter))
    val (isTacit1, isTacit2, isTacit3) = (isTacit(ns1, attr1), isTacit(ns2, attr2), isTacit(ns3, attr3))
    val (isExpr1, isExpr2, isExpr3) = (isExpression(ns1, attr1), isExpression(ns2, attr2), isExpression(ns3, attr3))
    val hasExpression = isExpr1 || isExpr2 || isExpr3
    val es = List(e1, e2, e3).distinct // same or different namespaces

    // Discard placeholders
    val q0 = query.copy(i = In(Nil, query.i.rules, query.i.ds))

    inputTuples.distinct match {

      case Nil if !isTacit1 || !isTacit2 || !isTacit3 =>
        throw new InputMolecule_3_Exception("Can only apply empty list of pairs (Nil) to two tacit attributes")

      case Nil => {
        // Both input attributes to be non-asserted
        val newClauses1 = addNilClause(query.wh.clauses, e1, kw1, v1)
        val newClauses2 = addNilClause(newClauses1, e2, kw2, v2)
        val newClauses3 = addNilClause(newClauses2, e3, kw3, v3)
        q0.copy(wh = Where(newClauses3))
      }

      case triples if triples.size > 1 && hasExpression => throw new InputMolecule_3_Exception(
        "Can't apply multiple triples to input attributes with one or more expressions (<, >, <=, >=, !=)")

      // 1 triple possibly with expressions
      case Seq((arg1, arg2, arg3)) => {
        val q1 = resolveInput(q0, ph1, Seq(arg1), "rule1", true)
        val q2 = resolveInput(q1, ph2, Seq(arg2), "rule1", true)
        val q3 = resolveInput(q2, ph3, Seq(arg3), "rule1", true)
        q3
      }

      // Multiple triples without expressions (only Eq)
      case triples => {

        def resolve2[A, B](query: Query,
                           ph1: Placeholder, ph2: Placeholder,
                           es: Seq[Var],
                           ex1: String, ex2: String,
                           enumPrefix1: Option[String], enumPrefix2: Option[String],
                           kw1: KW, kw2: KW,
                           v1: Var, v2: Var,
                           v1_ : String, v2_ : String,
                           isTacit1: Boolean, isTacit2: Boolean,
                           pairs: Seq[(A, B)]
                          ) = {
          val rule = RuleInvocation("rule2", es)
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
            throw new InputMolecule_3_Exception(s"Couldn't find clauses in query that match placeholders:" +
              s"\nplaceholder 1: " + ph1 +
              s"\nplaceholder 2: " + ph2 +
              s"\nnew clauses  :\n  " + resolvedClauses.mkString("\n  ") +
              s"\n" + query
            )

          val rules = pairs.map { case (arg1, arg2) =>
            Rule("rule2", es, valueClauses(ex1, kw1, enumPrefix1, arg1) ++ valueClauses(ex2, kw2, enumPrefix2, arg2))
          }
          query.copy(i = In(Nil, query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
        }


        triples.head match {

          // 2 input attributes share rule

          case (set: Set[_], _, _) if set.size <= 1 || isExpr1 => {
            val (args, pairs) = triples.map { case (arg1, arg2, arg3) => (arg1, (arg2, arg3)) }.unzip
            val q1 = resolveInput(q0, ph1, args, "rule1")
            resolve2(q1, ph2, ph3, Seq(e2, e3).distinct, ex2, ex3, enumPrefix2, enumPrefix3, kw2, kw3, v2, v3, v2_, v3_, isTacit2, isTacit3, pairs)
          }
          case (_, set: Set[_], _) if set.size <= 1 || isExpr2 => {
            val (args, pairs) = triples.map { case (arg1, arg2, arg3) => (arg2, (arg1, arg3)) }.unzip
            val q1 = resolveInput(q0, ph2, args, "rule1")
            resolve2(q1, ph1, ph3, Seq(e1, e3).distinct, ex1, ex3, enumPrefix1, enumPrefix3, kw1, kw3, v1, v3, v1_, v3_, isTacit1, isTacit3, pairs)
          }
          case (_, _, set: Set[_]) if set.size <= 1 || isExpr3 => {
            val (args, pairs) = triples.map { case (arg1, arg2, arg3) => (arg3, (arg1, arg2)) }.unzip
            val q1 = resolveInput(q0, ph3, args, "rule1")
            resolve2(q1, ph1, ph2, Seq(e1, e2).distinct, ex1, ex2, enumPrefix1, enumPrefix2, kw1, kw2, v1, v2, v1_, v2_, isTacit1, isTacit2, pairs)
          }

          // 3 input attributes share rule
          case _ => {
            // Add rule invocation clause to first input attribute clause (only 1 rule invocation needed)
            val rule = RuleInvocation("rule1", es)
            val ident1 = Var(v1_ + 1)
            val ident2 = Var(v2_ + 1)
            val ident3 = Var(v3_ + 1)
            val (done, resolvedClauses) = query.wh.clauses.foldLeft(0, Seq.empty[Seq[Clause]]) {
              case ((n, acc), cl@DataClause(_, _, KW("db", "ident", _), `ident1`, _, _)) if isTacit1 => (n, acc)
              case ((_, acc), cl@DataClause(_, _, _, `v1`, _, _)) if isTacit1                        => (1, acc :+ Seq(rule))
              case ((n, acc), cl@DataClause(_, _, `kw1`, Var(v), _, _)) if isTacit1 && v == v1_      => (n, acc)
              case ((_, acc), cl@DataClause(_, _, _, `v1`, _, _))                                    => (1, acc :+ Seq(cl, rule))
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

              case ((n, acc), cl@DataClause(_, _, KW("db", "ident", _), `ident3`, _, _)) if isTacit2 => (n, acc)
              case ((n, acc), cl@DataClause(_, _, _, `v3`, _, _)) if isTacit3                        => (n + 1, acc)
              case ((n, acc), cl@DataClause(_, _, _, `v3`, _, _))                                    => (n + 1, acc :+ Seq(cl))
              case ((n, acc), cl@DataClause(_, _, `kw3`, Var(v), _, _)) if isTacit3 && v == v3_      => (n, acc)
              case ((n, acc), fn@Funct(_, List(_, `v3`), _))                                         => (n + 1, acc :+ Seq(fn))
              case ((n, acc), fn@Funct(_, List(_, _, `v3`), _))                                      => (n + 1, acc :+ Seq(fn))
              case ((n, acc), fn@Funct(_, _, ScalarBinding(`v3`))) if isTacit3                       => (n + 1, acc)
              case ((n, acc), fn@Funct(_, _, ScalarBinding(`v3`)))                                   => (n + 1, acc :+ Seq(fn))

              case ((n, acc), otherClause) => (n, acc :+ Seq(otherClause))
            }

            val newClauses = if (done == 3) resolvedClauses.flatten else
              throw new InputMolecule_3_Exception(s"Couldn't find clauses in query that match placeholders:" +
                s"\nplaceholder 1: " + ph1 +
                s"\nplaceholder 2: " + ph2 +
                s"\nplaceholder 3: " + ph3 +
                s"\nnew clauses  :\n  " + resolvedClauses.mkString("\n  ") +
                s"\n" + query
              )

            val rules = triples.map { case (arg1, arg2, arg3) =>
              Rule("rule1", es, valueClauses(ex1, kw1, enumPrefix1, arg1) ++ valueClauses(ex2, kw2, enumPrefix2, arg2) ++ valueClauses(ex3, kw3, enumPrefix3, arg3))
            }
            q0.copy(i = In(Nil, query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
          }
        }
      }
    }
  }


  protected def bindSeqs(query: Query, inputRaw1: Seq[I1], inputRaw2: Seq[I2], inputRaw3: Seq[I3]) = {
    val (input1, input2, input3) = (inputRaw1.distinct, inputRaw2.distinct, inputRaw3.distinct)
    val List(
    ph1@Placeholder(_, KW(ns1, attr1, _), _, _),
    ph2@Placeholder(_, KW(ns2, attr2, _), _, _),
    ph3@Placeholder(_, KW(ns3, attr3, _), _, _)
    ) = query.i.inputs

    def resolve[T](query: Query, ph: Placeholder, input: Seq[T], ruleName: String, tacit: Boolean, expr: Boolean): Query = {
      val Placeholder(_, KW(nsFull, attr, _), _, _) = ph
      input match {
        case Nil if !tacit =>
          throw new InputMolecule_3_Exception(s"Can only apply empty list (Nil) to a tacit input attribute. Please make input attr tacit: `$attr` --> `${attr}_`")

        case in if expr && in.size > 1 =>
          throw new InputMolecule_3_Exception(s"Can't apply multiple values to input attribute `:$nsFull/$attr` having expression (<, >, <=, >=, !=)")

        case in =>
          resolveInput(query, ph, in, ruleName)
      }
    }

    // Discard placeholders
    val q0 = query.copy(i = In(Seq(), query.i.rules, query.i.ds))

    // Resolve inputs
    val q1 = resolve(q0, ph1, input1, "rule1", isTacit(ns1, attr1), isExpression(ns1, attr1))
    val q2 = resolve(q1, ph2, input2, "rule2", isTacit(ns2, attr2), isExpression(ns2, attr2))
    val q3 = resolve(q2, ph3, input3, "rule3", isTacit(ns3, attr3), isExpression(ns3, attr3))
    q3
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
    *   // Apply 3 input values (1 triple)
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
    * @param tpls Optional more triples of values matching both input attributes
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): MoleculeBase


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
    *   // Apply two or more triple expressions, each matching all 3 input attributes
    *   // [profession/age/score-expression] or [profession/age/score-expression] or ...
    *   profAgeScore.apply(("doctor" and 37 and 1.0) or ("teacher" and 32 and 1.0)).get.sorted === List("Ann", "Joe")
    * }}}
    *
    * @param or   Two or more tuple3 expressions separated by `or`
    * @param conn Implicit [[molecule.facade.Conn Conn]] in scope
    * @return Resolved molecule that can be queried
    */
  def apply(or: Or3[I1, I2, I3])(implicit conn: Conn): MoleculeBase


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


  /** Resolve input molecule by applying 3 groups of expressions, one for each of the 3 input attributes
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


  /** Resolve input molecule by applying 3 groups of values, one for each of the 3 input attributes
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
}


/** Implementations of input molecules awaiting 3 inputs, output arity 1-22 */
object InputMolecule_3 {

  abstract class InputMolecule_3_01[I1, I2, I3, A](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule01[A] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule01[A] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule01[A] // generated by macros

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule01[A] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule01[A] // generated by macros
  }

  abstract class InputMolecule_3_02[I1, I2, I3, A, B](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule02[A, B] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule02[A, B] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule02[A, B]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule02[A, B] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule02[A, B]
  }

  abstract class InputMolecule_3_03[I1, I2, I3, A, B, C](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule03[A, B, C] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule03[A, B, C] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule03[A, B, C]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule03[A, B, C] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule03[A, B, C]
  }

  abstract class InputMolecule_3_04[I1, I2, I3, A, B, C, D](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule04[A, B, C, D] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule04[A, B, C, D] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule04[A, B, C, D]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule04[A, B, C, D] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule04[A, B, C, D]
  }

  abstract class InputMolecule_3_05[I1, I2, I3, A, B, C, D, E](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule05[A, B, C, D, E]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule05[A, B, C, D, E] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule05[A, B, C, D, E]
  }

  abstract class InputMolecule_3_06[I1, I2, I3, A, B, C, D, E, F](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule06[A, B, C, D, E, F]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule06[A, B, C, D, E, F]
  }

  abstract class InputMolecule_3_07[I1, I2, I3, A, B, C, D, E, F, G](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
  }

  abstract class InputMolecule_3_08[I1, I2, I3, A, B, C, D, E, F, G, H](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
  }

  abstract class InputMolecule_3_09[I1, I2, I3, A, B, C, D, E, F, G, H, I](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
  }

  abstract class InputMolecule_3_10[I1, I2, I3, A, B, C, D, E, F, G, H, I, J](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
  }

  abstract class InputMolecule_3_11[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
  }

  abstract class InputMolecule_3_12[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
  }

  abstract class InputMolecule_3_13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
  }

  abstract class InputMolecule_3_14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  }

  abstract class InputMolecule_3_15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  }

  abstract class InputMolecule_3_16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  }

  abstract class InputMolecule_3_17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  }

  abstract class InputMolecule_3_18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  }

  abstract class InputMolecule_3_19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  }

  abstract class InputMolecule_3_20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  }

  abstract class InputMolecule_3_21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  }

  abstract class InputMolecule_3_22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, queryData: (Query, Option[Query])) extends InputMolecule_3[I1, I2, I3] {
    val (_query, _nestedQuery) = queryData
    def apply(i1: I1, i2: I2, i3: I3)                  (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2, i3)))
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)  (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(tpl +: tpls)
    def apply(or: Or3[I1, I2, I3])                     (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr3(or))
    def apply(ins: Seq[(I1, I2, I3)])                  (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]

    def apply(and: And3[I1, I2, I3])                   (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = { val (in1, in2, in3) = resolveAnd3(and); apply(in1, in2, in3) }
    def apply(in1: Seq[I1], in2: Seq[I2], in3: Seq[I3])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  }
}
