package molecule.input
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query.{Placeholder, _}
import molecule.exceptions.MoleculeException
import molecule.input.exception.{InputMoleculeException, InputMolecule_1_Exception, InputMolecule_2_Exception}

/** Shared interface of all input molecules.
  * <br><br>
  * Input molecules are molecules that awaits one or more inputs at runtime. When input value is applied,
  * the input molecule is resolved and a standard molecule is returned that we can then call actions on.
  * <br><br>
  * Input molecule queries are cached by Datomic. So there is a runtime performance gain in using input molecules. Furthermore,
  * input molecules are a good fit for re-use for queries where only a few parameters change.
  * <br><br>
  * Input molecules can await 1, 2 or 3 inputs and are constructed by applying the [[molecule.expression.AttrExpressions.? ?]] marker
  * to attributes. If one marker is applied, we get a [[molecule.input.InputMolecule_1 InputMolecule_1]], 2 inputs creates
  * an [[molecule.input.InputMolecule_1 InputMolecule_3]] and 3 an [[molecule.input.InputMolecule_3 InputMolecule_3]].
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

  protected def flatValues[T](in: Seq[T], prefix: String = ""): Seq[Seq[Any]] = if (prefix != "") {
    in.flatMap {
      case set: Set[_] => Seq(set.toSeq.map(setValue => prefix + setValue.toString))
      case one         => Seq(Seq(prefix + one.toString))
    }
  } else {
    in.flatMap {
      case map: Map[_, _] => map.toSeq.map { case (k, v) => Seq(k, v) }
      case set: Set[_]    => Seq(set.toSeq)
      case one            => Seq(Seq(one))
    }
  }

  def pre[T](enumPrefix: Option[String], arg: T) = if (enumPrefix.isDefined) enumPrefix.get + arg.toString else arg

  def newInputs(query: Query, v: Var): Seq[Input] = query.i.inputs.flatMap {
    case Placeholder(_, _, `v`, _) => None
    case other                     => Some(other)
  }

  def newIn(query: Query, v: Var): In = query.i.copy(inputs = newInputs(query, v))


  def isTacit(ns: String, attr: String): Boolean = {
    val attr_ = attr + "_"
    def isTacit_(elements: Seq[Element]): Option[Boolean] = elements.foldLeft(Option.empty[Boolean]) {
      case (result, Atom(`ns`, `attr_`, _, _, _, _, _, _)) => Some(true)
      case (result, Atom(`ns`, `attr`, _, _, _, _, _, _))  => Some(false)
      case (result, Nested(_, elements2))                  => isTacit_(elements2)
      case (result, Composite(elements2))                  => isTacit_(elements2)
      case (result, _)                                     => result
    }
    isTacit_(_model.elements) match {
      case Some(result) => result
      case None         => throw new InputMoleculeException(s"Couldn't find atom of attribute `:$ns/$attr` in model:\n" + _model)
    }
  }

  def cardinality(ns: String, attr: String): Int = {
    val attr_ = attr + "_"
    def isTacit_(elements: Seq[Element]): Option[Int] = elements.foldLeft(Option.empty[Int]) {
      case (result, Atom(`ns`, `attr` | `attr_`, _, card, _, _, _, _)) => Some(card)
      case (result, Nested(_, elements2))                              => isTacit_(elements2)
      case (result, Composite(elements2))                              => isTacit_(elements2)
      case (result, _)                                                 => result
    }
    isTacit_(_model.elements) match {
      case Some(result) => result
      case None         => throw new InputMoleculeException(s"Couldn't find atom of attribute `:$ns/$attr` in model:\n" + _model)
    }
  }

  def isComparison(ns: String, attr: String): Boolean = {
    val attr_ = attr + "_"
    def isComparison_(elements: Seq[Element]): Boolean = elements.foldLeft(false) {
      case (b, Atom(`ns`, `attr` | `attr_`, _, _, Lt(_) | Gt(_) | Le(_) | Ge(_), _, _, _)) => true
      case (b, Nested(_, elements2))                                                       => isComparison_(elements2)
      case (b, Composite(elements2))                                                       => isComparison_(elements2)
      case (b, _)                                                                          => b
    }
    isComparison_(_model.elements)
  }

  def addNilClause(clauses: Seq[Clause], e: Var, kw: KW, v0: Var) = {
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
      val KW(ns, attr, _) = kw
      throw new InputMoleculeException(s"Couldn't find input attribute `:$ns/$attr` placeholder variable `$v0` to be null among clauses:\n" + clauses.mkString("\n"))
    }
  }

  def addValueClause(clauses: Seq[Clause], kw: KW, isTacit: Boolean, v: Var, enumPrefix: Option[String], args: Seq[Any]): Seq[Clause] = {
    val (done, resolvedClauses) = clauses.reverse.foldLeft(false, Seq.empty[Seq[Clause]]) {
      case ((done, acc), Funct(name, List(v0, `v`), outs))                                   => (true, acc :+ args.map(arg => Funct(name, List(v0, Val(pre(enumPrefix, arg))), outs)))
      case ((done, acc), DataClause(ImplDS, Var(e), `kw`, `v`, Empty, NoBinding)) if isTacit => (true, acc :+ args.flatMap(arg => dataClause(e, kw, enumPrefix, arg)))
      case ((done, acc), cl@DataClause(ImplDS, Var(e), `kw`, `v`, Empty, NoBinding))         => (true, acc :+ (cl +: args.flatMap(arg => dataClause(e, kw, enumPrefix, arg))))
      case ((done, acc), otherClause)                                                        => (done, acc :+ Seq(otherClause))
    }
    if (done) resolvedClauses.flatten.reverse else {
      val KW(ns, attr, _) = kw
      throw new InputMoleculeException(s"Couldn't find clause with input attribute `:$ns/$attr` placeholder variable `$v` among clauses:\n" + clauses.mkString("\n"))
    }
  }

  def addArgClause(clauses: Seq[Clause], e: Var, kw: KW, v: Var, isComparison: Boolean, isTacit: Boolean, enumPrefix: Option[String], arg0: Any): Seq[Clause] = arg0 match {
    case set: Set[_] if set.size == 1 => addValueClause(clauses, kw, isTacit, v, enumPrefix, Seq(set.head))
    case set: Set[_] if set.isEmpty   => addNilClause(clauses, e, kw, v)
    case set: Set[_] if isComparison  => throw new InputMoleculeException("Can only apply 1 cardinality-many value to a comparison function. Got: Set(" + set.mkString(", ") + ")")
    case set: Set[_]                  => addValueClause(clauses, kw, isTacit, v, enumPrefix, set.toSeq)
    case arg                          => addValueClause(clauses, kw, isTacit, v, enumPrefix, Seq(arg))
  }

  def dataClause(e: String, kw: KW, enumPrefix: Option[String], arg: Any) = arg match {
    case value: java.net.URI => Seq(
      Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
      DataClause(ImplDS, Var(e), kw, Var(e + "_uri"), Empty, NoBinding)
    )
    case value               => Seq(
      DataClause(ImplDS, Var(e), kw, Val(pre(enumPrefix, arg)), Empty, NoBinding)
    )
  }

  def valueClauses[TT](e: String, kw: KW, enumPrefix: Option[String], args: TT): Seq[Clause] = args match {
    case set: Set[_] if set.isEmpty => Seq(NotClause(Var(e), kw))
    case set: Set[_]                => set.toSeq.flatMap(arg => dataClause(e, kw, enumPrefix, arg))
    case arg                        => dataClause(e, kw, enumPrefix, arg)
  }


  //  protected def resolveVarsInputs[In](query: Query, v: Var, inputs0: Seq[In], prefix: Option[String]): Query = {
  //    val Var(w) = v
  //    val inputs = inputs0.distinct
  //
  //    val isApply = query.wh.clauses.collectFirst {
  //      case DataClause(_, _, _, `v`, _, _) => true
  //    }.getOrElse(false)
  //
  //    val kw0@KW(ns0, attr, _) = query.i.inputs.collectFirst {
  //      case Placeholder(`w`, kw0@KW(ns0, _, _), _, _) => kw0
  //    }.getOrElse(KW("", "", ""))
  //
  //    val isComparison1 = isComparison(ns0, attr)
  //
  //
  //    inputs match {
  //
  //      // Empty input ...........................................
  //
  //      case Nil => {
  //        val mandatory = query.f.outputs.exists {
  //          case `v`                 => true
  //          case AggrExpr(_, _, `v`) => true
  //          case _                   => false
  //        }
  //        val newClauses = query.wh.clauses.flatMap {
  //          case varClause@DataClause(ds, e, a, `v`, _, _) if mandatory => Seq(NotClause(ds, e, a), varClause)
  //          case DataClause(ds, e, a, `v`, _, _)                        => Seq(NotClause(ds, e, a))
  //          case otherClause                                            => Seq(otherClause)
  //        }
  //        query.copy(i = newIn(query, w), wh = Where(newClauses))
  //      }
  //
  //
  //      // Single input ...........................................
  //
  //      case Seq(set: Set[_]) if isApply && set.isEmpty => {
  //        val mandatory = query.f.outputs.exists {
  //          case `v`                 => true
  //          case AggrExpr(_, _, `v`) => true
  //          case _                   => false
  //        }
  //        val newClauses = query.wh.clauses.flatMap {
  //          case varClause@DataClause(ds, e, a, `v`, _, _) if mandatory => Seq(NotClause(ds, e, a), varClause)
  //          case DataClause(ds, e, a, `v`, _, _)                        => Seq(NotClause(ds, e, a))
  //          case otherClause                                            => Seq(otherClause)
  //        }
  //        query.copy(i = newIn(query, w), wh = Where(newClauses))
  //      }
  //
  //      case Seq(set: Set[_]) if isApply => {
  //        val newClauses = query.wh.clauses.flatMap {
  //          case varClause@DataClause(ds, e, a, `v`, tx, op) => set.toList.zipWithIndex.flatMap {
  //            case (value: java.net.URI, i) => List(
  //              Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(w + "_uri" + (i + 1)))),
  //              DataClause(ImplDS, e, a, Var(w + "_uri" + (i + 1)), Empty, NoBinding)
  //            )
  //            case (value, _)               => List(DataClause(ds, e, a, Val(pre(prefix, value)), tx, op))
  //          } :+ varClause
  //          case other                                       => Seq(other)
  //        }
  //        query.copy(i = newIn(query, w), wh = Where(newClauses))
  //      }
  //
  //      case Seq(set: Set[_]) if set.size == 1 => {
  //        val newClauses = query.wh.clauses.flatMap {
  //          case Funct(fn, List(v1, `v`), v2) => List(Funct(fn, List(v1, Val(set.head)), v2))
  //          case other                        => Seq(other)
  //        }
  //        query.copy(i = newIn(query, w), wh = Where(newClauses))
  //      }
  //
  //      case Seq(set: Set[_]) =>
  //        throw new InputMoleculeException("Unexpected input molecule:\n" + query + "\ninputs: " + inputs)
  //
  //
  //      case Seq(value) => {
  //        val newInputs = query.i.inputs.map {
  //          case Placeholder(`w`, _, _, _) if isComparison1 => InVar(ScalarBinding(v), Seq(Seq(value)))
  //          case Placeholder(`w`, _, _, _)                  => InVar(ScalarBinding(v), Seq(Seq(pre(prefix, value))))
  //          case otherPlaceholder                           => otherPlaceholder
  //        }
  //        val newIn = query.i.copy(inputs = newInputs)
  //        query.copy(i = newIn)
  //      }
  //
  //
  //      // Multiple inputs ...........................................
  //
  //      // Card-many
  //      case ins if ins.head.isInstanceOf[Set[_]] => {
  //
  //        val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
  //          case ((_, acc), cl@DataClause(_, Var(e), _, `v`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
  //          case ((e, acc), other)                                  => (e, acc :+ other)
  //        }
  //        val values: Seq[Any] = ins.flatMap { case set: Set[_] => set.toSeq }
  //        val rules = values.map {
  //          case value: java.net.URI =>
  //            Rule("rule1", List(Var(e)), List(
  //              Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
  //              DataClause(ImplDS, Var(e), kw0, Var(e + "_uri"), Empty, NoBinding)))
  //          case value               =>
  //            Rule("rule1", List(Var(e)), List(
  //              DataClause(ImplDS, Var(e), kw0, Val(pre(prefix, value)), Empty, NoBinding)))
  //        }
  //
  //        query.copy(i = In(newInputs(query, w), query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
  //      }
  //
  //      case ins if w == "a" && ns0.isEmpty => {
  //        query.copy(i = In(query.i.inputs :+ InVar(CollectionBinding(v), Seq(ins)), query.i.rules, query.i.ds))
  //      }
  //
  //      case ins => {
  //        val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
  //          case ((_, acc), cl@DataClause(_, Var(e), _, `v`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
  //          case ((e, acc), other)                                  => (e, acc :+ other)
  //        }
  //        val rules = ins.map {
  //          case value: java.net.URI => Rule("rule1", List(Var(e)), List(
  //            Funct( s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
  //            DataClause(ImplDS, Var(e), kw0, Var(e + "_uri"), Empty, NoBinding)
  //          ))
  //          case value               => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw0, Val(pre(prefix, value)), Empty, NoBinding)))
  //        }
  //        query.copy(i = In(newInputs(query, w), query.i.rules ++ rules, query.i.ds), wh = Where(newClauses))
  //
  //      }
  //    }
  //  }

  //  // (Tuple1)
  //  protected def bindOne[I1](query: Query, inputs0: Seq[I1]): Query = {
  //    val inputs = inputs0.distinct
  //    val (List(Var(v1)), List(enumPrefix)) = varsAndPrefixes(query).unzip
  ////    val prefix = prefixes.headOption.getOrElse("")
  //
  //    //    println("....................................")
  //    //    println(query)
  //    //    println("inVars: " + inVars)
  //    //    println("inputs: " + inputs)
  //    //    println("prefix: " + prefix)
  //
  //    resolveVarsInputs[I1](query, Var(v1), inputs, enumPrefix)
  //
  //
  //
  ////    val (inVars, prefixes) = varsAndPrefixes(query).unzip
  ////    val prefix = prefixes.headOption.getOrElse("")
  //
  //    //    println("....................................")
  //    //    println(query)
  //    //    println("inVars: " + inVars)
  //    //    println("inputs: " + inputs)
  //    //    println("prefix: " + prefix)
  //
  //
  //
  ////    (inVars, inputs) match {
  ////
  ////      case (Seq(inVar), Nil) => {
  ////        //        println("@@@@@ 1 @@@@@ retrieve with non-asserted")
  ////        val mandatory = query.f.outputs.exists {
  ////          case `inVar`                 => true
  ////          case AggrExpr(_, _, `inVar`) => true
  ////          case _                       => false
  ////        }
  ////        val newClauses = query.wh.clauses.flatMap {
  ////          case varClause@DataClause(ds, e, a, `inVar`, _, _) if mandatory => Seq(NotClause(ds, e, a), varClause)
  ////          case DataClause(ds, e, a, `inVar`, _, _)                        => Seq(NotClause(ds, e, a))
  ////          case otherClause                                                => Seq(otherClause)
  ////        }
  ////        query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))
  ////      }
  ////
  ////      case (Seq(inVar), Seq(in)) => in match {
  ////        case set: Set[_] =>
  ////          val Var(v1) = inVar
  ////          val isApply = query.wh.clauses.collectFirst {
  ////            case DataClause(_, _, _, `inVar`, _, _) => true
  ////          }.getOrElse(false)
  ////
  ////          if (isApply) {
  ////
  ////            //            println("@@@@@ 2 a @@@@@  apply AND semantics from Set of input values")
  ////            val newClauses = query.wh.clauses.flatMap {
  ////              case varClause@DataClause(ds, e, a, `inVar`, tx, op) => set.toList.zipWithIndex.flatMap {
  ////                case (value, _) if prefix.nonEmpty => List(DataClause(ds, e, a, Val(prefix + value), tx, op))
  ////                case (value: java.net.URI, i)      => List(
  ////                  Funct(s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(v1 + "_uri" + (i + 1)))),
  ////                  DataClause(ImplDS, e, a, Var(v1 + "_uri" + (i + 1)), Empty, NoBinding)
  ////                )
  ////                case (value, _)                    => List(DataClause(ds, e, a, Val(value), tx, op))
  ////              } :+ varClause
  ////              case other                                           => Seq(other)
  ////            }
  ////            query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))
  ////
  ////          } else if (set.size == 1) {
  ////
  ////            //            println("@@@@@ 2 b @@@@@ compare to etc...")
  ////            val newClauses = query.wh.clauses.flatMap {
  ////              case Funct(fn, List(v, `inVar`), v2) => List(Funct(fn, List(v, Val(set.head)), v2))
  ////              case other                           => Seq(other)
  ////            }
  ////            query.copy(i = In(Nil, Nil, Nil), wh = Where(newClauses))
  ////
  ////
  ////          } else {
  ////            throw new InputMoleculeException("Unexpected input molecule:\n" + query + "\ninputs0: " + inputs0)
  ////          }
  ////
  ////
  ////        case value0 =>
  ////          //          println("@@@@@ 2 c @@@@@")
  ////          val value = if (prefix.nonEmpty) prefix + value0 else value0
  ////          query.copy(i = In(Seq(InVar(ScalarBinding(inVars.head), Seq(Seq(value)))), query.i.rules, query.i.ds))
  ////      }
  ////
  ////      case (Seq(inVar), ins) => {
  ////        val In(List(Placeholder(e0, kw@KW(ns, attr, _), _, _)), _, _) = query.i
  ////        ins.head match {
  ////          case _: Set[_] =>
  ////            //            println("@@@@@ 3 a @@@@@  OR semantics from Set of input values")
  ////            val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
  ////              case ((_, acc), cl@DataClause(_, Var(e), _, `inVar`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
  ////              case ((e, acc), other)                                      => (e, acc :+ other)
  ////            }
  ////            val values: Seq[Any] = ins.flatMap {
  ////              case set: Set[_] => set.toSeq
  ////            }
  ////            val rules = values.map {
  ////              case value if prefix.nonEmpty => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(prefix + value), Empty, NoBinding)))
  ////              case value: java.net.URI      => Rule("rule1", List(Var(e)), List(
  ////                Funct(s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
  ////                DataClause(ImplDS, Var(e), kw, Var(e + "_uri"), Empty, NoBinding)
  ////              ))
  ////              case value                    => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(value), Empty, NoBinding)))
  ////            }
  ////            query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))
  ////
  ////          // Ns(e1).int.get
  ////          case _ if e0 == "a" && ns.isEmpty =>
  ////            //            println("@@@@@ 3 b @@@@@")
  ////            query.copy(i = In(Seq(InVar(CollectionBinding(inVars.head), Seq(ins))), query.i.rules, query.i.ds))
  ////
  ////
  ////          case _ =>
  ////            //            println("@@@@@ 3 c @@@@@")
  ////            val (e, newClauses) = query.wh.clauses.foldLeft("", Seq.empty[Clause]) {
  ////              case ((_, acc), cl@DataClause(_, Var(e), _, `inVar`, _, _)) => (e, acc :+ cl :+ RuleInvocation("rule1", List(Var(e))))
  ////              case ((e, acc), other)                                      => (e, acc :+ other)
  ////            }
  ////            val rules = ins.map {
  ////              case value if prefix.nonEmpty => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(prefix + value), Empty, NoBinding)))
  ////              case value: java.net.URI      => Rule("rule1", List(Var(e)), List(
  ////                Funct(s"""ground (java.net.URI. "$value")""", Nil, ScalarBinding(Var(e + "_uri"))),
  ////                DataClause(ImplDS, Var(e), kw, Var(e + "_uri"), Empty, NoBinding)
  ////              ))
  ////              case value                    => Rule("rule1", List(Var(e)), List(DataClause(ImplDS, Var(e), kw, Val(value), Empty, NoBinding)))
  ////            }
  ////            query.copy(i = In(Nil, rules, query.i.ds), wh = Where(newClauses))
  ////        }
  ////      }
  ////
  ////      case (_, ins) =>
  ////        println("@@@@@ 4 a @@@@@")
  ////        query
  ////    }
  //  }

}
