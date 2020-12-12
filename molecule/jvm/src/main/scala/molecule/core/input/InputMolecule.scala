package molecule.core.input

import java.net.URI
import java.util.Date
import datomic.Util
import molecule.core.ast.MoleculeBase
import molecule.core.ast.model._
import molecule.core.ast.query.{DataClause, _}
import molecule.core.input.exception.InputMoleculeException
import molecule.core.util.fns

/** Shared interface of all input molecules.
  * <br><br>
  * Input molecules are molecules that awaits one or more inputs at runtime. When input value is applied,
  * the input molecule is resolved and a standard molecule is returned that we can then call actions on.
  * <br><br>
  * Input molecule queries are cached by Datomic. So there is a runtime performance gain in using input molecules. Furthermore,
  * input molecules are a good fit for re-use for queries where only a few parameters change.
  * <br><br>
  * Input molecules can await 1, 2 or 3 inputs and are constructed by applying the [[molecule.core.expression.AttrExpressions.? ?]] marker
  * to attributes. If one marker is applied, we get a [[molecule.core.input.InputMolecule_1 InputMolecule_1]], 2 inputs creates
  * an [[molecule.core.input.InputMolecule_1 InputMolecule_3]] and 3 an [[molecule.core.input.InputMolecule_3 InputMolecule_3]].
  * <br><br>
  * The three input molecule interfaces come in arity-versions corresponding to the number of non-?-marked attributes
  * in the input molecule. Let's see a simple example:
  * {{{
  *   // Sample data
  *   Person.name.age insert List(
  *     ("Joe", 42),
  *     ("Liz", 34)
  *   )
  *
  *   // Input molecule created at compile time. Awaits a name of type String
  *   val ageOfPersons: InputMolecule_1.InputMolecule_1_01[String, Int] = m(Person.name_(?).age)
  *
  *   // Resolved molecule. "Joe" input is matched against name attribute
  *   val ageOfPersonsNamedJoe: Molecule.Molecule01[Int] = ageOfPersons.apply("Joe")
  *
  *   // Calling action on resolved molecule.
  *   // (Only age is returned since name was marked as tacit with the underscore notation)
  *   ageOfPersonsNamedJoe.get === List(42)
  *
  *   // Or we can re-use the input molecule straight away
  *   ageOfPersons("Liz").get === List(34)
  * }}}
  *
  * @see [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  */
trait InputMolecule extends MoleculeBase {

  protected def resolveOr[I1](or: Or[I1]): Seq[I1] = {
    def traverse(or0: Or[I1]): Seq[I1] = or0 match {
      case Or(TermValue(v1), TermValue(v2)) => Seq(v1, v2)
      case Or(or1: Or[I1], TermValue(v2))   => traverse(or1) :+ v2
      case Or(TermValue(v1), or2: Or[I1])   => v1 +: traverse(or2)
      case Or(or1: Or[I1], or2: Or[I1])     => traverse(or1) ++ traverse(or2)
      case _                                => throw new InputMoleculeException(s"Unexpected expression: " + or0)
    }
    traverse(or)
  }

  protected def varsAndPrefixes(query: Query): Seq[(Var, String)] = query.i.inputs.collect {
    case Placeholder(_, _, v, enumPrefix) => (v, enumPrefix.getOrElse(""))
  }

  protected def isTacit(nsFull: String, attr: String): Boolean = {
    val nsFull_                = nsFull + "_"
    val (attr_, attrK, attrK_) = (attr + "_", attr + "K", attr + "K_")
    def isTacit_(elements: Seq[Element], tacit0: Option[Boolean]): Option[Boolean] = elements.foldLeft(tacit0) {
      case (tacit, Generic(_, `attr`, `nsFull`, _))                                  => Some(true)
      case (tacit, Atom(`nsFull` | `nsFull_`, `attr_` | `attrK_`, _, _, _, _, _, _)) => Some(true)
      case (tacit, Atom(`nsFull_`, `attr` | `attrK`, _, _, _, _, _, _))              => Some(true)
      case (tacit, Atom(`nsFull`, `attr` | `attrK`, _, _, _, _, _, _))               => Some(false)
      case (tacit, Nested(_, elements2))                                             => isTacit_(elements2, tacit)
      case (tacit, Composite(elements2))                                             => isTacit_(elements2, tacit)
      case (tacit, _)                                                                => tacit
    }
    isTacit_(_model.elements, None) match {
      case Some(result) => result
      case None         => throw new InputMoleculeException(s"Couldn't find atom of attribute `:$nsFull/$attr` in model:\n" + _model)
    }
  }

  protected def cardinality(nsFull: String, attr: String): Int = {
    val nsFull_                = nsFull + "_"
    val (attr_, attrK, attrK_) = (attr + "_", attr + "K", attr + "K_")
    def isTacit_(elements: Seq[Element], cardOpt0: Option[Int]): Option[Int] = elements.foldLeft(cardOpt0) {
      case (cardOpt, Generic(_, `attr`, `nsFull`, _))                                                        => Some(2)
      case (cardOpt, Atom(`nsFull` | `nsFull_`, `attr` | `attr_` | `attrK` | `attrK_`, _, card, _, _, _, _)) => Some(card)
      case (cardOpt, Nested(_, elements2))                                                                   => isTacit_(elements2, cardOpt)
      case (cardOpt, Composite(elements2))                                                                   => isTacit_(elements2, cardOpt)
      case (cardOpt, e)                                                                                      => cardOpt
    }
    isTacit_(_model.elements, None) match {
      case Some(result) => result
      case None         => throw new InputMoleculeException(s"Couldn't find atom of attribute `:$nsFull/$attr` in model:\n" + _model)
    }
  }

  protected def isExpression(nsFull: String, attr: String): Boolean = {
    val attr_ = attr + "_"
    def isExpression_(elements: Seq[Element], isExpression: Boolean): Boolean = elements.foldLeft(isExpression) {
      case (expr, Atom(`nsFull`, `attr` | `attr_`, _, _, Neq(_) | Lt(_) | Gt(_) | Le(_) | Ge(_) | Fulltext(_), _, _, _)) => true
      case (expr, Nested(_, elements2))                                                                                  => isExpression_(elements2, expr)
      case (expr, Composite(elements2))                                                                                  => isExpression_(elements2, expr)
      case (expr, _)                                                                                                     => expr
    }
    isExpression_(_model.elements, false)
  }

  protected def addNilClause(clauses: Seq[Clause], e: Var, kw: KW, v0: Var): Seq[Clause] = {
    val (found, _, newClauses) = clauses.foldLeft(false, "", Seq.empty[Clause]) {
      case ((found, v, acc), DataClause(_, `e`, `kw`, `v0`, _, _))   => (true, v, acc :+ NotClause(e, kw))
      case ((found, v, acc), DataClause(_, `e`, `kw`, Var(w), _, _)) => (true, w, acc :+ NotClause(e, kw))

      // Remove subsequent function clauses related to main clause
      case ((found, v, acc), Funct(_, List(Var(w), _), ScalarBinding(Var(x)))) if w == v => (found, x, acc)
      case ((found, v, acc), Funct(_, List(Var(w), _), _)) if w == v                     => (found, w, acc)

      // Leave non-related clauses as is
      case ((found, v, acc), otherClause) => (found, v, acc :+ otherClause)
    }
    if (found) newClauses else {
      val KW(nsFull, attr, _) = kw
      throw new InputMoleculeException(s"Couldn't find input attribute `:$nsFull/$attr` placeholder variable `$v0` to be null among clauses:\n" + clauses.mkString("\n"))
    }
  }

  protected def dataClause(e: String, kw: KW, enumPrefix: Option[String], arg: Any, i: Int): Seq[Clause] = arg match {
    case value: java.net.URI       =>
      val uriVar = Var(e + "_uri" + i)
      Seq(
        Funct(s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(uriVar)),
        DataClause(ImplDS, Var(e), kw, uriVar, Empty, NoBinding)
      )
    case _ if enumPrefix.isDefined => Seq(
      DataClause(ImplDS, Var(e), kw, KW(enumPrefix.get.init.tail, arg.toString), Empty, NoBinding)
    )
    case _                         => Seq(
      DataClause(ImplDS, Var(e), kw, Val(arg), Empty, NoBinding)
    )
  }

  protected def valueClauses[TT](e: String, kw: KW, enumPrefix: Option[String], args: TT): Seq[Clause] = args match {
    case set: Set[_] if set.isEmpty => Seq(NotClause(Var(e), kw))
    case set: Set[_]                => set.toSeq.zipWithIndex.flatMap { case (arg, i) => dataClause(e, kw, enumPrefix, arg, i + 1) }
    case arg                        => dataClause(e, kw, enumPrefix, arg, 1)
  }

  protected def deepNil(args: Seq[Any]) = args match {
    case Nil                                 => true
    case (seq: Seq[_]) :: Nil if seq.isEmpty => true
    case (set: Set[_]) :: Nil if set.isEmpty => true
    case _                                   => false
  }

  protected def resolveInput[T](
    query: Query,
    ph: Placeholder,
    inputs: Seq[T],
    ruleName: String = "rule1",
    unifyRule: Boolean = false
  ): Query = {
    val Placeholder(e@Var(e_), kw@KW(nsFull, attr, _), v@Var(w), prefix) = ph
    val card                                                             = cardinality(nsFull, attr)

    // Mapped key attributes
    if (card == 4) {

      val values = inputs.map {
        case d: Date => Seq(fns.date2str(d)) // compare standardized format
        case v       => Seq(v)
      }
      if (inputs.size > 1) {
        query.copy(i = In(Seq(InVar(CollectionBinding(v), Seq(values.flatten))), query.i.rules, query.i.ds))
      } else if (values.nonEmpty && values.head.size > 1) {
        val In(List(Placeholder(_, kw, v, _)), _, _) = query.i
        val (e, newClauses)                          = query.wh.clauses.foldLeft(null: QueryValue, Seq.empty[Clause]) {
          case ((_, acc), DataClause(_, e, _, `v`, _, _)) => (e, acc :+ RuleInvocation(ruleName, List(e)))
          case ((e, acc), other)                          => (e, acc :+ other)
        }
        val rules                                    = values.head.map(value =>
          Rule(ruleName, List(e), List(DataClause(ImplDS, e, kw, Val(value), Empty, NoBinding)))
        )
        query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))
      } else {
        query.copy(i = In(Seq(InVar(ScalarBinding(v), values)), query.i.rules, query.i.ds))
      }

    } else {

      // Card-one/many

      val v_ = w.filter(_.isLetter)
      def inGroup(v: String) = v.filter(_.isLetter) == v_ || v == v_ + "_casted"
      val tacit = isTacit(nsFull, attr)

      val (before, clauses, after) = query.wh.clauses.foldLeft(Seq.empty[Clause], Seq.empty[Clause], Seq.empty[Clause]) {
        case ((bef, cur, aft), cl@DataClause(_, `e`, `kw`, `v`, _, _))                  => (bef, cur :+ cl, aft)
        case ((bef, cur, aft), cl@DataClause(_, `e`, `kw`, Var(v), _, _)) if inGroup(v) => (bef, cur :+ cl, aft)
        case ((bef, cur, aft), cl@DataClause(_, Var(`v_`), _, _, _, _))                 => (bef, cur :+ cl, aft)
        case ((bef, cur, aft), cl@Funct(_, List(_, `v`), _))                            => (bef, cur :+ cl, aft)
        case ((bef, cur, aft), cl@Funct(_, List(_, _, `v`), _))                         => (bef, cur :+ cl, aft)
        case ((bef, cur, aft), cl@Funct(_, List(Var(v), _), _)) if inGroup(v)           => (bef, cur :+ cl, aft)
        case ((bef, cur, aft), cl@Funct(_, List(Var(v)), _)) if inGroup(v)              => (bef, cur :+ cl, aft)
        case ((bef, Nil, aft), cl)                                                      => (bef :+ cl, Nil, aft)
        case ((bef, cur, aft), cl)                                                      => (bef, cur, aft :+ cl)
      }

      val argss: Seq[Seq[_]] = inputs.flatMap {
        case map: Map[_, _]             => throw new InputMoleculeException("Unexpected Map input: " + map)
        case set: Set[_] if set.isEmpty => Nil
        case set: Set[_]                => Seq(set.toSeq)
        case arg                        => Seq(Seq(arg))
      }
      val args : Seq[Any]    = inputs.flatMap {
        case map: Map[_, _] => throw new InputMoleculeException("Unexpected Map input: " + map)
        case set: Set[_]    => set.toSeq
        case arg            => Seq(arg)
      }
      val nil                = deepNil(args)
      val one                = args.size == 1
      val uri                = if (nil) false else args.head.isInstanceOf[URI]

      val (newIns, newRules, newClauses): (Seq[Input], Seq[Rule], Seq[Clause]) = card match {

        // Applying entity ids to Namespace: `m(Ns(?).int).apply(42L)`
        case 2 if attr == "e_" => (Seq(InVar(CollectionBinding(v), Seq(args))), Nil, clauses)


        // Card-many enum attribute ...................................................................

        case 2 if prefix.isDefined => clauses match {

          // Neq(Seq(Qm))
          case Seq(enum, _, _, _, Funct("!=", _, _)) if nil && tacit  => (Nil, Nil, Seq(enum))
          case Seq(enum, ident, getName, _, Funct("!=", _, _)) if nil => (Nil, Nil, Seq(enum, ident, getName))
          case Seq(enum, ident, getName, _, Funct("!=", _, _))        => (Nil, Nil,
            Seq(enum, ident, getName) ++ argss.map(args =>
              NotClauses(
                args.map(arg => DataClause(ImplDS, e, kw, Val("__enum__" + prefix.get + arg), Empty, NoBinding))
              )
            )
          )

          // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
          case Seq(enum, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil && tacit  => (Nil, Nil, Seq(enum))
          case Seq(enum, ident, getName, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil => (Nil, Nil, Seq(enum, ident, getName))
          case cls@Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if one          => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
          case Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _))                     => throw new InputMoleculeException("Can't apply multiple values to comparison function.")

          // Qm
          case _ if nil && tacit    => (Nil, Nil, Seq(Funct("missing?", Seq(DS(), e, kw), NoBinding)))
          case cls if nil           => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, cls)
          case Seq(enum, ident, fn) => (
            Nil,
            argss.map(args =>
              Rule(ruleName, Seq(e), args.map(arg =>
                DataClause(ImplDS, e, kw, Val("__enum__" + prefix.get + arg), Empty, NoBinding)
              ))),
            if (tacit) Seq(enum, RuleInvocation(ruleName, Seq(e))) else Seq(enum, ident, fn, RuleInvocation(ruleName, Seq(e)))
          )
        }


        // Card-many attribute ...................................................................

        case 2 => clauses match {

          // BigInt has extra cast function
          case Seq(_, Funct("biginteger", _, _), _, _, _) => {
            clauses match {
              // Neq(Seq(Qm))
              case Seq(dc, _, _, _, Funct("!=", _, _)) if nil => (Nil, Nil, Seq(dc))
              case Seq(dc, _, _, _, Funct("!=", _, _))        =>
                (Nil, Nil,
                  dc +: argss.map(args =>
                    NotClauses(args.map(arg =>
                      DataClause(ImplDS, e, kw, Val(arg), Empty, NoBinding))
                    )
                  )
                )

              // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
              case Seq(dc, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil    => (Nil, Nil, Seq(dc))
              case cls@Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
              case Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _))            => throw new InputMoleculeException("Can't apply multiple values to comparison function.")
            }
          }

          // Neq(Seq(Qm))
          case Seq(dc, _, Funct("!=", _, _)) if nil => (Nil, Nil, Seq(dc))

          case Seq(dc, _, Funct("!=", _, _)) if uri => (Nil, Nil,
            dc +: argss.map { args =>
              val clauses = args.zipWithIndex.flatMap { case (arg, i) =>
                val uriVar = Var(w + "_uri" + (i + 1))
                Seq(
                  DataClause(ImplDS, e, kw, uriVar, Empty, NoBinding),
                  Funct(s"""ground (java.net.URI. "$arg")""", Seq(Empty), ScalarBinding(uriVar))
                )
              }
              NotJoinClauses(Seq(e), clauses)
            }
          )

          case Seq(dc, _, Funct("!=", _, _)) => (Nil, Nil,
            dc +: argss.map(args =>
              NotClauses(args.map(arg =>
                DataClause(ImplDS, e, kw, Val(arg), Empty, NoBinding))
              )
            )
          )

          // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
          case Seq(dc, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil    => (Nil, Nil, Seq(dc))
          case cls@Seq(_, _, Funct(">" | ">=" | "<" | "<=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
          case Seq(_, _, Funct(">" | ">=" | "<" | "<=", _, _))            => throw new InputMoleculeException("Can't apply multiple values to comparison function.")

          // Fulltext(Seq(Qm))
          case Seq(f@Funct("fulltext", _, _)) if nil => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(f))
          case Seq(f@Funct("fulltext", _, _))        => (
            Nil,
            argss.map(args =>
              Rule(ruleName, Seq(e), args.zipWithIndex.map { case (arg, i) =>
                Funct("fulltext", Seq(DS(), kw, Val(arg)), RelationBinding(List(e, Var(w + "_" + (i + 1)))))
              })),
            Seq(
              DataClause(ImplDS, e, kw, Var(v_), Empty, NoBinding),
              RuleInvocation(ruleName, Seq(e))
            )
          )

          // Qm
          case Seq(dc: DataClause) if nil && tacit => (Nil, Nil, Seq(Funct("missing?", Seq(DS(), e, kw), NoBinding)))
          case Seq(dc: DataClause) if nil          => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(dc))
          case Seq(dc: DataClause) if uri          => (
            Nil,
            argss.map(args =>
              Rule(ruleName, Seq(e), args.zipWithIndex.flatMap { case (arg, i) =>
                val uriVar = Var(w + "_uri" + (i + 1))
                Seq(
                  Funct(s"""ground (java.net.URI. "$arg")""", Seq(Empty), ScalarBinding(uriVar)),
                  DataClause(ImplDS, e, kw, uriVar, dc.tx, dc.op)
                )
              })),
            Seq(dc, RuleInvocation(ruleName, Seq(e)))
          )
          case Seq(dc: DataClause)                 => (
            Nil,
            argss.map(args =>
              Rule(ruleName, Seq(e), args.map(arg =>
                DataClause(ImplDS, e, kw, Val(arg), dc.tx, dc.op)))),
            Seq(dc, RuleInvocation(ruleName, Seq(e)))
          )
        }


        // Card-one enum attribute ...................................................................

        case 1 if prefix.isDefined => clauses match {

          // Neq(Seq(Qm))
          case Seq(enum, ident, getName, _, Funct("!=", _, _)) if nil && tacit                  => (Nil, Nil, Seq(enum))
          case Seq(enum, ident, getName, _, Funct("!=", _, _)) if nil                           => (Nil, Nil, Seq(enum, ident, getName))
          case cls@Seq(_, _, _, _, Funct("!=", _, _)) if one                                    => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
          case Seq(enum, ident, getName, Funct(n, Seq(Var(v2), `v`), _), not@Funct("!=", _, _)) => (Nil, Nil,
            Seq(enum, ident, getName) ++ args.zipWithIndex.flatMap { case (arg, i) =>
              Seq(
                Funct(n, Seq(Var(v2), Val(arg)), ScalarBinding(Var(v2 + "_" + (i + 1)))),
                Funct("!=", Seq(Var(v2 + "_" + (i + 1)), Val(0)), NoBinding)
              )
            })

          // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
          case Seq(enum, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil && tacit  => (Nil, Nil, Seq(enum))
          case Seq(enum, ident, getName, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil => (Nil, Nil, Seq(enum, ident, getName))
          case cls@Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if one          => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
          case Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _))                     => throw new InputMoleculeException("Can't apply multiple values to comparison function.")

          // Fulltext(Seq(Qm))
          case Seq(f@Funct("fulltext", _, _)) if nil => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(f))
          case Seq(f@Funct("fulltext", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, Seq(f))
          case Seq(f@Funct("fulltext", _, _))        => (Seq(InVar(CollectionBinding(v), Seq(args))), Nil, Seq(f))

          // Qm
          case cls if nil && tacit             => (Nil, Nil, Seq(Funct("missing?", Seq(DS(), e, kw), NoBinding)))
          case cls if nil                      => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, cls)
          case Seq(enum, _, _) if one && tacit => (Seq(InVar(ScalarBinding(Var(v_)), Seq(Seq("__enum__" + prefix.get + args.head)))), Nil, Seq(enum))
          case cls if one                      => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
          case Seq(enum, _, _) if tacit        => (Seq(InVar(CollectionBinding(Var(v_)), Seq(args.map(arg => "__enum__" + prefix.get + arg)))), Nil, Seq(enum))
          case cls                             => (Seq(InVar(CollectionBinding(v), Seq(args))), Nil, cls)
        }


        // Card-one attribute ...................................................................

        case 1 => clauses match {

          case Nil if nsFull == "ns" && attr == "?" => (Nil, Nil, Seq(Funct("=", Seq(v, Val(args.head)), NoBinding)))

          // BigInt has extra cast function
          case Seq(_, Funct("biginteger", _, _), _, _, _) => {
            clauses match {
              // Neq(Seq(Qm)) BigInt
              case Seq(dc, _, _, _, Funct("!=", _, _)) if nil =>
                (Nil, Nil, Seq(dc))

              case cls@Seq(_, _, _, _, Funct("!=", _, _)) if one =>
                (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)

              case Seq(dc,
              Funct("biginteger", Seq(Var(v0)), ScalarBinding(Var(v0_casted))),
              Funct("biginteger", Seq(Var(v1)), ScalarBinding(Var(v1_casted))),
              Funct(fn, _, ScalarBinding(Var(v2))),
              Funct("!=", _, _)
              ) =>
                val bigIntNegations = args.zipWithIndex.flatMap { case (arg, i) =>
                  val suffix      = "_" + (i + 1)
                  val inputCasted = if (i == 0)
                    Seq(Funct("biginteger", Seq(Var(v0)), ScalarBinding(Var(v0_casted)))) else Nil

                  inputCasted ++ Seq(
                    Funct(s"biginteger $arg", Seq(), ScalarBinding(Var(v1_casted + suffix))),
                    Funct(fn, Seq(Var(v0_casted), Var(v1_casted + suffix)), ScalarBinding(Var(v2 + suffix))),
                    Funct("!=", Seq(Var(v2 + suffix), Val(0)), NoBinding)
                  )
                }
                (Nil, Nil, dc +: bigIntNegations)

              // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm) BigInt
              case Seq(dc, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil    => (Nil, Nil, Seq(dc))
              case cls@Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
              case Seq(_, _, _, _, Funct(">" | ">=" | "<" | "<=", _, _))            => throw new InputMoleculeException("Can't apply multiple values to comparison function.")
            }
          }

          // Neq(Seq(Qm))

          case Seq(dc, _, Funct("!=", _, _)) if nil => (Nil, Nil, Seq(dc))

          case cls@Seq(_, _, Funct("!=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)

          case Seq(dc, Funct(fn, Seq(v1, `v`), _), Funct("!=", _, _)) if uri =>
            (Nil, Nil,
              dc +: args.zipWithIndex.flatMap { case (arg, i) =>
                val (x, y) = (Var(v_ + "_" + (i + 1) + "a"), Var(v_ + "_" + (i + 1) + "b"))
                Seq(
                  Funct(s"""ground (java.net.URI. "$arg")""", Seq(Empty), ScalarBinding(x)),
                  Funct(fn, Seq(v1, x), ScalarBinding(y)),
                  Funct("!=", Seq(y, Val(0)), NoBinding)
                )
              }
            )

          case Seq(dc, Funct(fn, Seq(v1, `v`), ScalarBinding(Var(v2))), Funct("!=", _, _)) =>
            (Nil, Nil,
              dc +: args.zipWithIndex.flatMap { case (arg, i) =>
                val vx = Var(v2 + "_" + (i + 1))
                Seq(
                  Funct(fn, Seq(v1, Val(arg)), ScalarBinding(vx)),
                  Funct("!=", Seq(vx, Val(0)), NoBinding)
                )
              }
            )

          // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
          case Seq(dc, _, Funct(">" | ">=" | "<" | "<=", _, _)) if nil    => (Nil, Nil, Seq(dc))
          case cls@Seq(_, _, Funct(">" | ">=" | "<" | "<=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
          case Seq(_, _, Funct(">" | ">=" | "<" | "<=", _, _))            => throw new InputMoleculeException("Can't apply multiple values to comparison function.")

          // Fulltext(Seq(Qm))
          case Seq(f@Funct("fulltext", _, _)) if nil => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(f))
          case Seq(f@Funct("fulltext", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, Seq(f))
          case Seq(f@Funct("fulltext", _, _))        => (Seq(InVar(CollectionBinding(v), Seq(args))), Nil, Seq(f))

          // Qm
          case _ if nil && tacit          => (Nil, Nil, Seq(Funct("missing?", Seq(DS(), e, kw), NoBinding)))
          case Seq(dc: DataClause) if nil => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(dc))
          case Seq(dc: DataClause) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, Seq(dc))
          case Seq(dc: DataClause)        => (Seq(InVar(CollectionBinding(v), Seq(args))), Nil, Seq(dc))
        }
      }

      val (newRules2, newClauses2): (Seq[Rule], Seq[Clause]) = if (unifyRule && newRules.size == 1 && argss.size == 1) {
        val curRules    = query.i.rules.collect {
          case r@Rule(`ruleName`, _, _) => r
        }
        val newClauses0 = before ++ newClauses ++ after
        curRules.size match {
          case 0          => (newRules, newClauses0)
          case 1          => {
            // Collect rule clauses to be unified
            val (ruleVars, unifiedRules) = query.i.rules.foldLeft(Seq.empty[QueryValue], Seq.empty[Rule]) {
              case ((_, rules), r@Rule(`ruleName`, vars, cls)) =>
                ((vars ++ newRules.head.args).distinct, rules :+ Rule(ruleName, (vars ++ newRules.head.args).distinct, cls ++ newRules.head.clauses))
              case ((vs, rules), other)                        => (vs, rules)
            }
            // Unify rule invocations
            val newClauses1: Seq[Clause] = newClauses0.foldRight(0, Seq.empty[Clause]) {
              case (RuleInvocation(`ruleName`, vars), (0, cls)) => (1, RuleInvocation(ruleName, (vars ++ ruleVars).distinct) +: cls)
              case (RuleInvocation(`ruleName`, _), (1, cls))    => (1, cls)
              case (cl, (done, cls))                            => (done, cl +: cls)
            }._2
            (unifiedRules, newClauses1)
          }
          case unexpected => throw new InputMoleculeException(s"Didn't expect $unexpected rules to be unified in query:\n" + query)
        }
      } else {
        (query.i.rules ++ newRules, before ++ newClauses ++ after)
      }

      query.copy(i = In(query.i.inputs ++ newIns, newRules2, query.i.ds), wh = Where(newClauses2))
    }
  }
}
