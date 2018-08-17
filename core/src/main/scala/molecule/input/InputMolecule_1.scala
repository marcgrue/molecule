package molecule.input

import molecule.action.Molecule._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query.{Funct, _}
import molecule.facade.Conn
import molecule.input.exception.{InputMoleculeException, InputMolecule_1_Exception}


/** Shared interfaces of input molecules awaiting 1 input.
  * {{{
  *   // Sample data set
  *   Person.name.age insert List(
  *     ("Joe", 42),
  *     ("Liz", 34)
  *   )
  *
  *   // Input molecule awaiting 1 input for `name`
  *   val ageOfPersons = m(Person.name_(?).age)
  *
  *   // Resolve input molecule with name input in various ways
  *   ageOfPersons("Joe").get === List(42)
  *   ageOfPersons("Joe" or "Liz").get === List(42, 34)
  *   ageOfPersons("Joe", "Liz").get === List(42, 34)
  *   ageOfPersons(Seq("Joe", "Liz")).get === List(42, 34) // Accepts Iterable[I1]
  * }}}
  *
  * @see [[molecule.input.InputMolecule]]
  *      | [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  * @tparam I1 Type of input matching attribute with `?` marker
  */
trait InputMolecule_1[I1] extends InputMolecule {


  protected def bindTuples0(query: Query, in1: Seq[I1]) = {
    val (inVars, prefixes) = varsAndPrefixes(query).unzip
    val prefix = prefixes.head
    val values = if (prefix.nonEmpty) {
      in1.flatMap {
        case set: Set[_] => Seq(set.toSeq.map(setValue => prefix + setValue.toString))
        case one         => Seq(Seq(prefix + one.toString))
      }
    } else {
      in1.flatMap {
        case map: Map[_, _] => map.toSeq.map { case (k, v) => Seq(k, v) }
        case set: Set[_]    => Seq(set.toSeq)
        case one            => Seq(Seq(one))
      }
    }

    //    if (inVars.size > 1) {
    //      query.copy(i = In(Seq(InVar(RelationBinding(inVars), values)), query.i.rules, query.i.ds))
    //
    //    } else
    if (values.size > 1) {
      query.copy(i = In(Seq(InVar(CollectionBinding(inVars.head), Seq(values.flatten))), query.i.rules, query.i.ds))

    } else if (values.nonEmpty && values.head.size > 1) {
      val In(List(Placeholder(_, kw, v0, enumPrefix)), _, _) = query.i
      val v = v0.toString
      val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
        case ((_, acc), DataClause(_, Var(e), _, Var(`v`), _, _)) => (e, acc :+ RuleInvocation("rule1", List(Var(e))))
        case ((e, acc), other)                                    => (e, acc :+ other)
      }
      val rules = values.head.map(value =>
        Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(value), Empty, NoBinding)))
      )
      query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))

    } else {
      query.copy(i = In(Seq(InVar(ScalarBinding(inVars.head), values)), query.i.rules, query.i.ds))
    }
  }


  protected def bindTuples(query: Query, inputs0: Seq[I1]): Query = {
    val inputs = inputs0.distinct
    val List(Placeholder(e@Var(e_), kw@KW(ns, attr, _), v@Var(w), prefix)) = query.i.inputs
    val v_ = w.filter(_.isLetter)
    def inGroup(v: String) = v.filter(_.isLetter) == v_

    val mandatory = query.f.outputs.exists {
      case Var(v) if v.startsWith(v_)                 => true
      case AggrExpr(_, _, Var(v)) if v.startsWith(v_) => true
      case _                                          => false
    }
    val tacit = !mandatory

    val (before, clauses, after) = query.wh.clauses.foldLeft(Seq.empty[Clause], Seq.empty[Clause], Seq.empty[Clause]) {
      case ((bef, cur, aft), cl@DataClause(_, `e`, `kw`, `v`, _, _))                  => (bef, cur :+ cl, aft)
      case ((bef, cur, aft), cl@DataClause(_, `e`, `kw`, Var(v), _, _)) if inGroup(v) => (bef, cur :+ cl, aft)
      case ((bef, cur, aft), cl@Funct(_, List(_, `v`), _))                            => (bef, cur :+ cl, aft)
      case ((bef, cur, aft), cl@Funct(_, List(Var(v), _), _)) if inGroup(v)           => (bef, cur :+ cl, aft)
      case ((bef, Nil, aft), cl)                                                      => (bef :+ cl, Nil, aft)
      case ((bef, cur, aft), cl)                                                      => (bef, cur, aft :+ cl)
    }

    val argss: Seq[Seq[_]] = inputs.flatMap {
      case map: Map[_, _]                 => map.toSeq.map { case (k, v) => Seq(k, v) }
      case set: Set[_] if set.isEmpty     => Nil
      case set: Set[_] if prefix.nonEmpty => Seq(set.toSeq.map(setValue => prefix + setValue.toString))
      case set: Set[_]                    => Seq(set.toSeq)
      case arg if prefix.nonEmpty         => Seq(Seq(prefix + arg.toString))
      case arg                            => Seq(Seq(arg))
    }
    val args: Seq[Any] = inputs.flatMap {
      case _: Map[_, _]                   => throw new InputMolecule_1_Exception("Mapped attribute can't have flat args")
      case set: Set[_] if prefix.nonEmpty => set.toSeq.map(setValue => prefix + setValue.toString)
      case set: Set[_]                    => set.toSeq
      case arg if prefix.nonEmpty         => Seq(prefix + arg.toString)
      case arg                            => Seq(arg)
    }
    val nil = args.isEmpty
    val one = args.size == 1

    val (newIns, newRules, newClauses): (Seq[Input], Seq[Rule], Seq[Clause]) = cardinality(ns, attr) match {

      case 4 => (Nil, Nil, Nil)

      case 3 => (Nil, Nil, Nil)

      case 2 => clauses match {

        // Qm
        case Seq(dc: DataClause) if nil => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(dc))
        case Seq(dc: DataClause) if one => (Nil, Nil, Seq(DataClause(ImplDS, e, kw, Val(args.head), dc.tx, dc.op), dc))
        case Seq(dc: DataClause)        => (
          Nil,
          argss.map(args =>
            Rule("rule1", Seq(e), args.map(arg =>
              DataClause(ImplDS, e, kw, Val(arg), dc.tx, dc.op)))),
          Seq(dc, RuleInvocation("rule1", Seq(e)))
        )

        // Neq(Seq(Qm))
        case Seq(dc, Funct(_, Seq(_, `v`), _), Funct("!=", _, _)) if nil    => (Nil, Nil, Seq(dc))
        case cls@Seq(_, Funct(_, Seq(_, `v`), _), Funct("!=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
        case Seq(dc, Funct(_, Seq(_, `v`), _), Funct("!=", _, _))           => (
          Nil,
          Nil,
          dc +: argss.map(args =>
            NotClauses(args.map(arg =>
              DataClause(ImplDS, e, kw, Val(arg), Empty, NoBinding))))
        )

        // Todo: Fulltext(Seq(Qm))

        // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
        case Seq(dc, Funct(_, Seq(_, `v`), _), _) if nil    => (Nil, Nil, Seq(dc))
        case cls@Seq(_, Funct(_, Seq(_, `v`), _), _) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
        case Seq(dc, Funct(n, Seq(_, `v`), _), _)           => throw new InputMolecule_1_Exception("Can't apply multiple values to comparison function.")
      }


      case 1 => clauses match {

        // Qm
        case Seq(dc: DataClause) if nil => (Seq(InVar(CollectionBinding(v), Seq(Nil))), Nil, Seq(dc))
        case Seq(dc: DataClause) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, Seq(dc))
        case Seq(dc: DataClause)        => (Seq(InVar(CollectionBinding(v), Seq(args))), Nil, Seq(dc))

        // Neq(Seq(Qm))
        case Seq(dc, Funct(_, Seq(_, `v`), _), Funct("!=", _, _)) if nil    => (Nil, Nil, Seq(dc))
        case cls@Seq(_, Funct(_, Seq(_, `v`), _), Funct("!=", _, _)) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
        case Seq(dc, Funct(n, Seq(v1, `v`), v2), f2@Funct("!=", _, _))      => (Nil, Nil, dc +: args.map(arg => Funct(n, Seq(v1, Val(arg)), v2)) :+ f2)

        // Todo: Fulltext(Seq(Qm))

        // Gt(Qm), Ge(Qm), Lt(Qm), Le(Qm)
        case Seq(dc, Funct(_, Seq(_, `v`), _), _) if nil    => (Nil, Nil, Seq(dc))
        case cls@Seq(_, Funct(_, Seq(_, `v`), _), _) if one => (Seq(InVar(ScalarBinding(v), Seq(args))), Nil, cls)
        case Seq(dc, Funct(n, Seq(_, `v`), _), _)           => throw new InputMolecule_1_Exception("Can't apply multiple values to comparison function.")
      }

    }


    query.copy(i = In(newIns, newRules, query.i.ds), wh = Where(before ++ newClauses ++ after))
  }


  protected def bindTuples2(query: Query, inputs0: Seq[I1]): Query = {
    val List(Placeholder(_, _, v@Var(w), prefix)) = query.i.inputs

    //    resolveVarsInputs[I1](query, Var(v), inputs0, prefix)

    //    val Var(w) = v
    val inputs = inputs0.distinct

    val isApply = query.wh.clauses.collectFirst {
      case DataClause(_, _, _, `v`, _, _) => true
    }.getOrElse(false)

    val kw0@KW(ns0, attr, _) = query.i.inputs.collectFirst {
      case Placeholder(_, kw0@KW(_, _, _), `v`, _) => kw0
    }.getOrElse(KW("", "", ""))

    val isComparison1 = isComparison(ns0, attr)
    val isTacit1 = isTacit(ns0, attr)


    inputs match {

      // Empty input ...........................................

      case Nil => {
        val mandatory = query.f.outputs.exists {
          case `v`                 => true
          case AggrExpr(_, _, `v`) => true
          case _                   => false
        }
        val newClauses = query.wh.clauses.flatMap {
          case varClause@DataClause(ds, e, a, `v`, _, _) if mandatory => Seq(NotClause(e, a), varClause)
          case DataClause(ds, e, a, `v`, _, _)                        => Seq(NotClause(e, a))
          case otherClause                                            => Seq(otherClause)
        }
        query.copy(i = newIn(query, v), wh = Where(newClauses))
      }


      // Single input ...........................................

      case Seq(set: Set[_]) if isApply && set.isEmpty => {
        val mandatory = query.f.outputs.exists {
          case `v`                 => true
          case AggrExpr(_, _, `v`) => true
          case _                   => false
        }
        val newClauses = query.wh.clauses.flatMap {
          case varClause@DataClause(ds, e, a, `v`, _, _) if mandatory => Seq(NotClause(e, a), varClause)
          case DataClause(ds, e, a, `v`, _, _)                        => Seq(NotClause(e, a))
          case otherClause                                            => Seq(otherClause)
        }
        query.copy(i = newIn(query, v), wh = Where(newClauses))
      }

      case Seq(set: Set[_]) if isApply => {
        val newClauses = query.wh.clauses.flatMap {
          case varClause@DataClause(ds, e, a, `v`, tx, op) => set.toList.zipWithIndex.flatMap {
            case (value: java.net.URI, i) => List(
              Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(w + "_uri" + (i + 1)))),
              DataClause(ImplDS, e, a, Var(w + "_uri" + (i + 1)), Empty, NoBinding)
            )
            case (value, _)               => List(DataClause(ds, e, a, Val(pre(prefix, value)), tx, op))
          } :+ varClause
          case other                                       => Seq(other)
        }
        query.copy(i = newIn(query, v), wh = Where(newClauses))
      }

      case Seq(set: Set[_]) if set.size == 1 => {
        val newClauses = query.wh.clauses.flatMap {
          case Funct(fn, List(v1, `v`), v2) => List(Funct(fn, List(v1, Val(set.head)), v2))
          case other                        => Seq(other)
        }
        query.copy(i = newIn(query, v), wh = Where(newClauses))
      }

      case Seq(set: Set[_]) =>
        throw new InputMoleculeException("Unexpected input molecule:\n" + query + "\ninputs: " + inputs)


      case Seq(value) => {
        val newInputs = query.i.inputs.map {
          case Placeholder(_, _, `v`, _) if isComparison1 => InVar(ScalarBinding(v), Seq(Seq(value)))
          case Placeholder(_, _, `v`, _)                  => InVar(ScalarBinding(v), Seq(Seq(pre(prefix, value))))
          case otherPlaceholder                           => otherPlaceholder
        }
        val newIn = query.i.copy(inputs = newInputs)
        query.copy(i = newIn)
      }


      // Multiple inputs ...........................................

      // Card-many
      case ins if ins.head.isInstanceOf[Set[_]] => {

        val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
          case ((_, acc), cl@DataClause(_, Var(e), _, `v`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
          case ((e, acc), other)                                  => (e, acc :+ other)
        }
        val values: Seq[Any] = ins.flatMap { case set: Set[_] => set.toSeq }
        val rules = values.map {
          case value: java.net.URI =>
            Rule("rule1", List(Var(e)), List(
              Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
              DataClause(ImplDS, Var(e), kw0, Var(e + "_uri"), Empty, NoBinding)))
          case value               =>
            Rule("rule1", List(Var(e)), List(
              DataClause(ImplDS, Var(e), kw0, Val(pre(prefix, value)), Empty, NoBinding)))
        }

        query.copy(i = In(newInputs(query, v), query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
      }

      case ins if w == "a" && ns0.isEmpty => {
        query.copy(i = In(query.i.inputs :+ InVar(CollectionBinding(v), Seq(ins)), query.i.rules, query.i.ds))
      }

      case ins if isApply => {
        val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
          case ((_, acc), cl@DataClause(_, Var(e), _, `v`, _, _))    => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
          case (("", acc), cl@DataClause(_, Var(e), `kw0`, _, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
          case ((e, acc), other)                                     => (e, acc :+ other)
        }
        val rules = ins.map {
          case value: java.net.URI => Rule("rule1", List(Var(e)), List(
            Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
            DataClause(ImplDS, Var(e), kw0, Var(e + "_uri"), Empty, NoBinding)
          ))
          case value               => Rule("rule1", List(Var(e)), List(
            DataClause(ImplDS, Var(e), kw0, Val(pre(prefix, value)), Empty, NoBinding)
          ))
        }
        query.copy(i = In(newInputs(query, v), query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
      }

      case ins => {
        val newClauses2 = addValueClause(query.wh.clauses, kw0, isTacit1, v, prefix, ins)
        query.copy(i = In(newInputs(query, v), query.i.rules, query.i.ds), wh = Where(newClauses2))
      }
    }
  }

  // (Tuple1)
  protected def bindTuples3(query: Query, inputs0: Seq[I1]): Query = {
    val inputs = inputs0.distinct
    val (inVars, prefixes) = varsAndPrefixes(query).unzip
    val prefix = prefixes.head

    //    println("....................................")
    //    println(query)
    //    println("inVars: " + inVars)
    //    println("inputs: " + inputs)
    //    println("prefix: " + prefix)

    (inVars, inputs) match {

      case (Seq(inVar), Nil) => {
        println("@@@@@ 0 @@@@@ retrieve with non-asserted")
        val mandatory = query.f.outputs.exists {
          case `inVar`                 => true
          case AggrExpr(_, _, `inVar`) => true
          case _                       => false
        }
        val newClauses = query.wh.clauses.flatMap {
          case varClause@DataClause(ds, e, a, `inVar`, _, _) if mandatory => Seq(NotClause(e, a), varClause)
          case DataClause(ds, e, a, `inVar`, _, _)                        => Seq(NotClause(e, a))
          case otherClause                                                => Seq(otherClause)
        }
        query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))
      }

      case (Seq(inVar), Seq(in)) => in match {
        case set: Set[_] =>
          val Var(v1) = inVar
          val isApply = query.wh.clauses.collectFirst {
            case DataClause(_, _, _, `inVar`, _, _) => true
          }.getOrElse(false)

          if (isApply) {

            println("@@@@@ 1 b @@@@@  apply AND semantics from Set of input values")
            val newClauses = query.wh.clauses.flatMap {
              case varClause@DataClause(ds, e, a, `inVar`, tx, op) => set.toList.zipWithIndex.flatMap {
                case (value, _) if prefix.nonEmpty => List(DataClause(ds, e, a, Val(prefix + value), tx, op))
                case (value: java.net.URI, i)      => List(
                  Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(v1 + "_uri" + (i + 1)))),
                  DataClause(ImplDS, e, a, Var(v1 + "_uri" + (i + 1)), Empty, NoBinding)
                )
                case (value, _)                    => List(DataClause(ds, e, a, Val(value), tx, op))
              } :+ varClause
              case other                                           => Seq(other)
            }
            val query1 = query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))
            query1

          } else if (set.size == 1) {

            println("@@@@@ 1 c @@@@@ compare to etc...")
            val newClauses = query.wh.clauses.flatMap {
              case Funct(fn, List(v, `inVar`), v2) => List(Funct(fn, List(v, Val(set.head)), v2))
              case other                           => Seq(other)
            }
            query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))


          } else {
            throw new InputMolecule_1_Exception("Unexpected input molecule:\n" + query + "\ninputs0: " + inputs0)
          }


        case value0 =>
          println("@@@@@ 1 d @@@@@")
          val value = if (prefix.nonEmpty) prefix + value0 else value0
          query.copy(i = In(Seq(InVar(ScalarBinding(inVars.head), Seq(Seq(value)))), query.i.rules, query.i.ds))
      }

      case (Seq(inVar), ins) => {
        val In(List(Placeholder(_, kw@KW(ns, attr, _), e0, _)), _, _) = query.i
        ins.head match {
          case _: Set[_] =>
            println("@@@@@ X a @@@@@  OR semantics from Set of input values")
            val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
              case ((_, acc), cl@DataClause(_, Var(e), _, `inVar`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
              case ((e, acc), other)                                      => (e, acc :+ other)
            }
            val values: Seq[Any] = ins.flatMap { case set: Set[_] => set.toSeq }
            val rules = values.map {
              case value if prefix.nonEmpty => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(prefix + value), Empty, NoBinding)))
              case value: java.net.URI      => Rule("rule1", List(Var(e)), List(
                Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
                DataClause(ImplDS, Var(e), kw, Var(e + "_uri"), Empty, NoBinding)
              ))
              case value                    => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(value), Empty, NoBinding)))
            }
            query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))

          // Ns(e1).int.get
          case _ if e0 == "a" && ns.isEmpty =>
            println("@@@@@ X b @@@@@")
            query.copy(i = In(Seq(InVar(CollectionBinding(inVars.head), Seq(ins))), query.i.rules, query.i.ds))


          case _ =>
            println("@@@@@ X c @@@@@")
            val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
              case ((_, acc), cl@DataClause(_, Var(e), _, `inVar`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
              case ((e, acc), other)                                      => (e, acc :+ other)
            }
            val rules = ins.map {
              case value if prefix.nonEmpty => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(prefix + value), Empty, NoBinding)))
              case value: java.net.URI      => Rule("rule1", List(Var(e)), List(
                Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
                DataClause(ImplDS, Var(e), kw, Var(e + "_uri"), Empty, NoBinding)
              ))
              case value                    => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(value), Empty, NoBinding)))
            }
            query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))
        }
      }

      case (_, ins) =>
        println("@@@@@ 4 a @@@@@")
        query
    }

    //    if (inVars.size > 1) {
    //      query.copy(i = In(Seq(InVar(RelationBinding(inVars), values)), query.i.rules, query.i.ds))
    //
    //    } else if (values.size > 1) {
    //      query.copy(i = In(Seq(InVar(CollectionBinding(inVars.head), Seq(values.flatten))), query.i.rules, query.i.ds))
    //
    //    } else if (values.nonEmpty && values.head.size > 1) {
    //      val In(List(Placeholder(v0, kw, enumPrefix, _)), _, _) = query.i
    //      val v = v0.toString
    //      val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
    //        case ((_, acc), DataClause(_, Var(e), _, Var(`v`), _, _)) => (e, acc :+ RuleInvocation("rule1", List(Var(e))))
    //        case ((e, acc), other)                                    => (e, acc :+ other)
    //      }
    //      val rules = values.head.map(value =>
    //        Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(value), Empty, NoBinding)))
    //      )
    //      query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))
    //
    //    } else {
    //      //      query.copy(i = In(Seq(InVar(ScalarBinding(inVars.head), values)), query.i.rules, query.i.ds))
    //
    //      val In(List(Placeholder(v0, kw, enumPrefix, _)), _, _) = query.i
    //      val v = v0.toString
    //      val newClauses = query.wh.clauses.flatMap {
    //        case varClause@DataClause(ds, Var(e), a, Var(`v`), tx, op) =>
    //
    //          val inputClauses = in1.flatMap {
    //            case set: Set[_] if prefix.nonEmpty => set.toSeq.map(value => DataClause(ds, Var(e), a, Val(prefix + value), tx, op))
    //            case set: Set[_]                    => set.toSeq.map(value => DataClause(ds, Var(e), a, Val(value), tx, op))
    //            case value if prefix.nonEmpty       => Seq(DataClause(ds, Var(e), a, Val(prefix + value), tx, op))
    //            case value                          => Seq(DataClause(ds, Var(e), a, Val(value), tx, op))
    //          }
    //          inputClauses :+ varClause
    //
    //        case other => Seq(other)
    //      }
    //      query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))
    //    }
  }


  /** Apply one or more input values to resolve input molecule.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply one or more input value(s)
    *   ageOfPersons.apply("Ben", "Liz") // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons(Set("Ben", "Liz"))
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(arg: I1)(implicit conn: Conn): MoleculeBase


  /** Apply one or more input values to resolve input molecule.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply one or more input value(s)
    *   ageOfPersons.apply("Ben", "Liz") // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons(Set("Ben", "Liz"))
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): MoleculeBase


  /** Apply Seq of input values with OR semantics to resolve input molecule.
    * <br><br>
    * Resolve input molecule by applying a Set of values that the attribute is expected to have (OR semantics).
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply Seq of one or more input value(s)
    *   ageOfPersons.apply(Seq("Ben", "Liz"))
    *
    *   // Same as
    *   ageOfPersons(Set("Ben", "Liz"))
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons("Ben", "Liz")
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(args: Seq[I1])(implicit conn: Conn): MoleculeBase


  /** Apply Set of input values with OR semantics to resolve input molecule.
    * <br><br>
    * Input value type matches attribute having `?` marker.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply Set of one or more input value(s)
    *   ageOfPersons.apply(Set("Ben", "Liz")) // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons("Ben", "Liz")
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Since a Set can only have distinct values, only distinct values will be matched.
    * @return Resolved molecule that can queried
    */
  //  def apply(args: Set[I1])(implicit conn: Conn): MoleculeBase


  /** Apply OR expression of input values to resolve input molecule.
    * <br><br>
    * Input value type matches attribute having `?` marker.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply OR expression of two or more input values
    *   ageOfPersons.apply("Ben" or "Liz") // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons("Ben", "Liz")
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons(Set("Ben", "Liz"))
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the values applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(or: Or[I1])(implicit conn: Conn): MoleculeBase
}


/** Implementations of input molecules awaiting 1 input, output arity 1-22 */
object InputMolecule_1 {

  abstract class InputMolecule_1_01[I1, A](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule01[A] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule01[A] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule01[A] // generated by macros
    def apply(or: Or[I1])(implicit conn: Conn): Molecule01[A] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_02[I1, A, B](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule02[A, B] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule02[A, B] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule02[A, B]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule02[A, B] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_03[I1, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule03[A, B, C] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule03[A, B, C] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule03[A, B, C]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_04[I1, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_05[I1, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_06[I1, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_07[I1, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_08[I1, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr(or))
  }
}
/*

{

  abstract class InputMolecule_1_01[I1, A](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule01[A] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule01[A] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule01[A] // generated by macros
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule01[A] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_02[I1, A, B](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule02[A, B] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule02[A, B] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule02[A, B]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_03[I1, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule03[A, B, C] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule03[A, B, C] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule03[A, B, C]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_04[I1, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule04[A, B, C, D] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_05[I1, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_06[I1, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_07[I1, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_08[I1, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr(or))
  }
}


*/
