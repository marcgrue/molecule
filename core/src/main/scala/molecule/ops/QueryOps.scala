package molecule
package ops
import java.net.URI
import java.util.{Date, UUID}
import datomic.Util
import molecule.ast.model._
import molecule.ast.query._
import molecule.ops.exception.QueryOpsException
import molecule.util.Helpers


/** Query operations */
object QueryOps extends Helpers {

  implicit class QueryOps(q: Query) {

    // Find ..........................................

    def find(fn: String, args: Seq[Any], v: String, gs: Seq[Generic]): Query =
      find(AggrExpr(fn, args, Var(v)), gs)

    def findD(v: String, gs: Seq[Generic]): Query =
      find("distinct", Seq(), v, gs)

    def find(v: String, gs: Seq[Generic]): Query =
      find(Var(v), gs)

    def find(gs: Seq[Generic]): Query =
      find(NoVal, gs, "")

    def find(fn: String, args: Seq[String], v: String, gs: Seq[Generic], attrV: String): Query =
      find(AggrExpr(fn, args, Var(v)), gs, attrV)

    def find(v: String, gs: Seq[Generic], attrV: String): Query =
      find(Var(v), gs, attrV)

    def find(o: Output, gs: Seq[Generic], attrV: String = ""): Query = {

      val genericVars = if (gs.isEmpty) Nil else {
        // unique transaction find variable
        def gV(g: String): String = g + q.f.outputs.foldLeft(Option.empty[Int]) {
          case (None, Var(v)) if g == v        => Some(2)
          case (Some(i), Var(v)) if g + i == v => Some(i + 1)
          case (count, _)                      => count
        }.getOrElse("")

        gs.flatMap {
          case AttrVar(v)         => Some(Var(attrV))
          case TxValue(_)         => Some(Var(gV("tx")))
          case TxValue_(_)        => None
          case TxTValue(_)        => Some(Var(gV("txT")))
          case TxTValue_(_)       => None
          case TxInstantValue(_)  => Some(Var(gV("txInst")))
          case TxInstantValue_(_) => None
          case OpValue(_)         => Some(Var(gV("op")))
          case OpValue_(_)        => None
          case other              => None
        }.distinct
      }

      val moreOutputs = o match {
        case NoVal => genericVars
        case _     => o +: genericVars
      }
      q.copy(f = Find(q.f.outputs ++ moreOutputs))
    }


    // Pull ..........................................

    def pull(e: String, atom: Atom): Query =
      q.copy(f = Find(q.f.outputs :+ Pull(e + "_" + atom.name, atom.ns, atom.name)))
        .func("molecule.util.fns/bind", Seq(Var(e)), ScalarBinding(Var(e + "_" + atom.name)))

    def pullEnum(e: String, atom: Atom): Query =
      q.copy(f = Find(q.f.outputs :+ Pull(e + "_" + atom.name, atom.ns, atom.name, atom.enumPrefix)))
        .func("molecule.util.fns/bind", Seq(Var(e)), ScalarBinding(Var(e + "_" + atom.name)))


    // In ..........................................

    def in(e: String, a: Atom, enumPrefix: Option[String], v: String): Query =
      q.copy(i = q.i.copy(inputs = q.i.inputs :+ Placeholder(Var(e), KW(a.ns, a.name), Var(v), enumPrefix)))

    def in(v: String, ns: String, attr: String, e: String): Query =
      q.copy(i = q.i.copy(inputs = q.i.inputs :+ Placeholder(Var(e), KW(ns, attr), Var(v), None)))

    def in(vs: Seq[Any], v: String): Query =
      q.copy(i = q.i.copy(inputs = q.i.inputs :+ InVar(CollectionBinding(Var(v)), Seq(vs))))

    def in(e: String): Query =
      q.copy(i = q.i.copy(inputs = q.i.inputs :+ Placeholder(Var(e), KW("", ""), Var(e), None)))


    // With ...........................................

    def widh(v: String): Query = q.copy(wi = With((q.wi.variables :+ v).distinct))


    // Where ..........................................

    def where(e: String, ns: String, attr: String, v: QueryValue, refNs: String, gs: Seq[Generic]): Query = {
      val attrClauses = if (gs.isEmpty) {
        (ns, attr) match {
          //          case ("?", "ns" | "attr")  => Seq(DataClause(ImplDS, KW("db.part", "db"), KW("db.install", "attribute"), v, Empty))
          case ("db", "ns" | "a") => Seq(DataClause(ImplDS, KW("db.part", "db"), KW("db.install", "attribute"), v, Empty))
          case _                  => Seq(DataClause(ImplDS, Var(e), KW(ns, attr, refNs), v, Empty))
        }

      } else {
        val opValue: Option[OpValue] = gs.collectFirst {
          case ov: OpValue => Some(ov)
        }.flatten

        // unique tx variable
        val tx = "tx" + q.wh.clauses.foldLeft(Option.empty[Int]) {
          case (None, DataClause(_, _, _, _, Var(txV), _)) if "tx" == txV        => Some(2)
          case (Some(i), DataClause(_, _, _, _, Var(txV), _)) if "tx" + i == txV => Some(i + 1)
          case (count, _)                                                        => count
        }.getOrElse("")

        val extendedClause = if (opValue.nonEmpty) {
          // unique operation variable
          val op = "op" + q.wh.clauses.foldLeft(Option.empty[Int]) {
            case (None, DataClause(_, _, _, _, _, Var(opV))) if "op" == opV        => Some(2)
            case (Some(i), DataClause(_, _, _, _, _, Var(opV))) if "op" + i == opV => Some(i + 1)
            case (count, _)                                                        => count
          }.getOrElse("")

          DataClause(ImplDS, Var(e), KW(ns, attr, refNs), v, Var(tx), Var(op))
        } else if (
          gs.collectFirst {
            case TxValue(_)         => true
            case TxValue_(_)        => true
            case TxTValue(_)        => true
            case TxTValue_(_)       => true
            case TxInstantValue(_)  => true
            case TxInstantValue_(_) => true
          }.getOrElse(false)
        ) {
          DataClause(ImplDS, Var(e), KW(ns, attr, refNs), v, Var(tx), NoBinding)
        } else {
          DataClause(ImplDS, Var(e), KW(ns, attr, refNs), v, NoBinding, NoBinding)
        }

        // unique txT/txInstant variable
        def gV(g: String): String = g + q.wh.clauses.foldLeft(Option.empty[Int]) {
          case (None, Funct(_, _, ScalarBinding(Var(`g`))))                    => Some(2)
          case (None, DataClause(_, _, _, Var(`g`), _, _))                     => Some(2)
          case (Some(i), Funct(_, _, ScalarBinding(Var(txV)))) if g + i == txV => Some(i + 1)
          case (Some(i), DataClause(_, _, _, Var(txV), _, _)) if g + i == txV  => Some(i + 1)
          case (count, _)                                                      => count
        }.getOrElse("")

        val extraClauses = gs.flatMap {
          case TxTValue(None)              => Seq(
            Funct("datomic.Peer/toT ^Long", Seq(Var(tx)), ScalarBinding(Var(gV("txT"))))
          )
          case TxTValue(Some(t))           => Seq(
            Funct("datomic.Peer/toT ^Long", Seq(Var(tx)), ScalarBinding(Var(gV("txT")))),
            Funct("=", Seq(Var(gV("txT")), Val(t)), NoBinding)
          )
          case TxTValue_(Some(t))          => Seq(
            Funct("datomic.Peer/toT ^Long", Seq(Var(tx)), ScalarBinding(Var(gV("txT")))),
            Funct("=", Seq(Var(gV("txT")), Val(t)), NoBinding)
          )
          case TxInstantValue(None)        => Seq(
            DataClause(ImplDS, Var(tx), KW("db", "txInstant", ""), Var(gV("txInst")), Empty)
          )
          case TxInstantValue(Some(date))  => Seq(
            DataClause(ImplDS, Var(tx), KW("db", "txInstant", ""), Var(gV("txInst")), Empty),
            Funct("=", Seq(Var("txInst"), Val(date)), NoBinding)
          )
          case TxInstantValue_(Some(date)) => Seq(
            DataClause(ImplDS, Var(tx), KW("db", "txInstant", ""), Val(date), Empty)
          )
          case OpValue(Some(added))        => Seq(
            DataClause(ImplDS, Var(e), KW(ns, attr, refNs), v, Var(tx), Val(added))
          )
          case OpValue_(Some(added))       => Seq(
            DataClause(ImplDS, Var(e), KW(ns, attr, refNs), v, Var(tx), Val(added))
          )
          case _                           => Nil
        }
        extendedClause +: extraClauses
      }
      q.copy(wh = Where(q.wh.clauses ++ attrClauses))
    }

    def where(e: KW, a: KW, v: String, gs: Seq[Generic]): Query =
      q.copy(wh = Where(q.wh.clauses :+ DataClause(ImplDS, e, a, Var(v), NoBinding)))

    def where(e: String, a: Atom, v: String, gs: Seq[Generic]): Query =
      where(e, a.ns, a.name, Var(v), "", gs)

    def where(e: String, a: Atom, qv: Val, gs: Seq[Generic]): Query =
      where(e, a.ns, a.name, qv, "", gs)

    def whereAnd[T](e: String, a: Atom, v: String, args: Seq[T], uriV: String = ""): Query =
      args.zipWithIndex.foldLeft(q) {
        case (q1, (arg, i)) if uriV.nonEmpty => q1.where(e, a, v + "_uri" + (i + 1), Nil).func( s"""ground (java.net.URI. "$arg")""", Empty, v + "_uri" + (i + 1))
        case (q1, (arg, i))                  => q1.where(e, a, Val(arg), Nil)
      }.where(e, a, v, Nil)


    def whereAndEnum[T](e: String, a: Atom, v: String, prefix: String, args: Seq[T]): Query = {
      args.foldLeft(q) { case (q1, arg) => q1.where(e, a, Val(prefix + arg), Nil) }.enum(e, a, v, Nil)
    }


    // Null ..........................................

    def pre(a: Atom, arg: Any): Any = if (a.enumPrefix.isDefined) a.enumPrefix.get + arg else arg

    def not(e: String, a: Atom): Query =
      q.copy(wh = Where(q.wh.clauses :+ NotClause(Var(e), KW(a.ns, a.name))))

    def nots(e: String, a: Atom, v: String, argss: Seq[Any]): Query = {
      argss.zipWithIndex.foldLeft(q) {
        //        case (q1, (set: Set[_], i)) if set.size == 1            =>
        //          q1.compareTo("!=", a, v, Val(set.head), i + 1)
        case (q1, (set: Set[_], i)) if a.tpeS == "java.net.URI" =>
          val notClauses = set.toSeq.zipWithIndex.flatMap { case (uri, j) =>
            val x = Var(v + "_" + (j + 1))
            Seq(
              DataClause(ImplDS, Var(e), KW(a.ns, a.name), x, Empty),
              Funct( s"""ground (java.net.URI. "${esc(uri)}")""", Nil, ScalarBinding(x))
            )
          }
          q1.copy(wh = Where(q1.wh.clauses :+ NotJoinClauses(Seq(Var(e)), notClauses)))
        case (q1, (set: Set[_], _))                             =>
          val notClauses = set.toSeq.map(arg =>
            DataClause(ImplDS, Var(e), KW(a.ns, a.name), Val(pre(a, arg)), Empty)
          )
          q1.copy(wh = Where(q1.wh.clauses :+ NotClauses(notClauses)))
        case _                                                  =>
          throw new QueryOpsException(s"Expected Seq[Set[T]], got: " + argss)
      }
    }


    // Generic/Meta attributes ..........................................

    def genericE(e: String, v: String, v1: String, singleElement: Boolean = false): Query = {
      if (singleElement) {
        q.where(e, "?", e + "_attr", Var(v), "", Nil)
          .ident(e + "_attr", v1)
          .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
          .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
          .func("!=", Seq(Var(v + "_ns"), Val("db")))
          .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
      } else if (q.wh.clauses.isEmpty) {
        q
      } else {
        q.wh.clauses.reverse.collectFirst {
          case Funct("namespace", _, _)                                                   =>
            q
          case DataClause(_, Var(e0), KW("db", "ident", _), _, _, _) if e0 == e + "_attr" =>
            q
          case DataClause(_, _, KW("?", attr, _), _, _, _) if attr == e + "_attr"         =>
            q.ident(e + "_attr", v1)
          case DataClause(_, Var(`e`), _, _, _, _)                                        =>
            q
          case DataClause(_, Var("a"), _, _, _, _)                                        =>
            q
        } getOrElse
          q.where(e, "?", e + "_attr", Var(v), "", Nil)
            .ident(e + "_attr", v1)
            .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
            .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
            .func("!=", Seq(Var(v + "_ns"), Val("db")))
            .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
        //          q.where(e, "?", e + "_attr", Var(v), "", Nil)
        //            .ident(e + "_attr", v1)
        //            .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
        //            .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
        //            .func("!=", Seq(Var(v + "_ns"), Val("db")))
        //            .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
      }
    }

    def genericNs(e: String, v: String, v1: String): Query = {
      q.wh.clauses.reverse.collectFirst {
        case Funct("namespace", _, _)                                                   =>
          q
        case DataClause(_, Var(e0), KW("db", "ident", _), _, _, _) if e0 == e + "_attr" =>
          q.func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
        case DataClause(_, _, KW("?", attr, _), _, _, _) if attr == e + "_attr"         =>
          q.ident(e + "_attr", v1)
            .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
        case DataClause(_, Var(`e`), KW(ns, attr, _), _, _, _)                          =>
          q.where(KW(ns, attr), KW("db", "ident"), v1, Nil)
            .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
      }.getOrElse(
        q.where(e, "?", e + "_attr", Var(v), "", Nil)
          .ident(e + "_attr", v1)
          .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
          .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
          .func("!=", Seq(Var(v + "_ns"), Val("db")))
          .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
      )
    }

    def genericA(e: String, v: String, v1: String): Query = {
      var nss = false
      q.wh.clauses.reverse.collectFirst {
        case DataClause(_, Var(e0), KW("db", "ident", _), _, _, _) if e0 == e + "_attr" =>
          q.func("name", Seq(Var(v1)), ScalarBinding(Var(v + "_a")))
        case DataClause(_, _, KW("?", attr, _), _, _, _) if attr == e + "_attr"         =>
          q.ident(e + "_attr", v1)
            .func("name", Seq(Var(v1)), ScalarBinding(Var(v + "_a")))
        case DataClause(_, Var(`e`), KW(ns, attr, _), _, _, _)                          =>
          q.where(KW(ns, attr), KW("db", "ident"), v1, Nil)
            .func("name", Seq(Var(v1)), ScalarBinding(Var(v + "_a")))
      }.getOrElse(
        q.where(e, "?", e + "_attr", Var(v), "", Nil)
          .ident(e + "_attr", v1)
          .func("name", Seq(Var(v1)), ScalarBinding(Var(v + "_a")))
          .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
          .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
          .func("!=", Seq(Var(v + "_ns"), Val("db")))
          .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
      )
    }

    def genericV(e: String, v: String, v1: String): Query = {
      q.wh.clauses.reverse.collectFirst {
        case DataClause(_, Var(e0), KW("db", "ident", _), _, _, _) if e0 == e + "_attr" =>
          q
        case DataClause(_, _, KW("?", attr, _), _, _, _) if attr == e + "_attr"         =>
          q.ident(e + "_attr", v1)
        case DataClause(_, Var(`e`), KW(ns, attr, _), _, _, _)                          =>
          q.func("molecule.util.fns/bind", Seq(Var(v)), ScalarBinding(Var(v + "_v")))
      } getOrElse
        q.where(e, "?", e + "_attr", Var(v), "", Nil)
          .ident(e + "_attr", v1)
          .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
          .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
          .func("!=", Seq(Var(v + "_ns"), Val("db")))
          .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
    }


    def compareToMany2[T](op: String, v: String, args: Seq[T]): Query = args.foldLeft(q) {
      case (q1, arg) => q1.func(op, Seq(Var(v), Val(arg)))
    }

    def genericTx(e: String, v: String, v1: String): Query = {
      // Ensure tx value is present
      val (ok, cls0): (Int, Seq[Clause]) = q.wh.clauses.foldRight(0, Seq.empty[Clause]) {
        case (cl@DataClause(_, Var(`e`), _, _, Empty | Var("_"), _), (0, acc))           => (1, cl.copy(tx = Var(v + "_tx")) +: acc)
        case (cl@DataClause(_, Var(`e`), _, _, Var(tx), _), (0, acc)) if tx == v + "_tx" => (1, cl +: acc)
        case (cl@DataClause(_, _, _, Var(`e`), Empty | Var("_"), _), (0, acc))           => (1, cl.copy(tx = Var(v + "_tx")) +: acc)
        case (cl@DataClause(_, _, _, Var(`e`), Var(tx), _), (0, acc)) if tx == v + "_tx" => (1, cl +: acc)
        case (cl, (ok, acc))                                                             => (ok, cl +: acc)
      }
      val cls: Seq[Clause] = if (ok == 1) cls0 else Seq(
        DataClause(ImplDS, Var(e), KW("?", e + "_attr"), Var(v), Var(v + "_tx"))
      )
      val q1 = q.copy(wh = Where(cls))

      // Add necessary bind to ident to prepare working with tx value
      q1.wh.clauses.reverse.collectFirst {
        case DataClause(_, Var(e0), KW("db", "ident", _), _, _, _) if e0 == e + "_attr" =>
          q1
        case DataClause(_, Var(`e`), KW("?", attr, _), _, _, _) if attr == e + "_attr"  =>
          q1.ident(e + "_attr", v1)
            .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
            .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
            .func("!=", Seq(Var(v + "_ns"), Val("db")))
            .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
        case DataClause(_, Var(`e`), _, _, _, _)                                        =>
          q1
      } getOrElse
        q1.ident(e + "_attr", v1)
          .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
          .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
          .func("!=", Seq(Var(v + "_ns"), Val("db")))
          .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
    }

    def genericT(e: String, v: String, v1: String): Query = {
      q.wh.clauses.reverse.collectFirst {
        case DataClause(_, _, _, _, Var(tx), _) if tx == v + "_tx" =>
          q.func("datomic.Peer/toT ^Long", Seq(Var(v + "_tx")), ScalarBinding(Var(v + "_t")))
      } getOrElse
        q.genericTx(e, v, v1)
          .func("datomic.Peer/toT ^Long", Seq(Var(v + "_tx")), ScalarBinding(Var(v + "_t")))
    }

    def genericTxInstant(e: String, v: String, v1: String): Query = {
      q.wh.clauses.reverse.collectFirst {
        case DataClause(_, _, _, _, Var(tx), _) if tx == v + "_tx" =>
          q.where(v + "_tx", "db", "txInstant", Var(v + "_txInstant"), "", Nil)
      } getOrElse
        q.genericTx(e, v, v1)
          .where(v + "_tx", "db", "txInstant", Var(v + "_txInstant"), "", Nil)
    }

    def genericOp(e: String, v: String, v1: String): Query = {
      val (ok, cls0): (Int, Seq[Clause]) = q.wh.clauses.foldRight(0, Seq.empty[Clause]) {
        case (cl@DataClause(_, Var(`e`), _, _, Empty, NoBinding), (0, acc))              => (1, cl.copy(tx = Var("_"), op = Var(v + "_op")) +: acc)
        case (cl@DataClause(_, Var(`e`), _, _, _, NoBinding), (0, acc))                  => (1, cl.copy(op = Var(v + "_op")) +: acc)
        case (cl@DataClause(_, Var(`e`), _, _, _, Var(op)), (0, acc)) if op == v + "_op" => (1, cl +: acc)
        case (cl@DataClause(_, _, _, Var(`e`), Empty, NoBinding), (0, acc))              => (1, cl.copy(tx = Var("_"), op = Var(v + "_op")) +: acc)
        case (cl@DataClause(_, _, _, Var(`e`), _, NoBinding), (0, acc))                  => (1, cl.copy(op = Var(v + "_op")) +: acc)
        case (cl@DataClause(_, _, _, Var(`e`), _, Var(op)), (0, acc)) if op == v + "_op" => (1, cl +: acc)
        case (cl, (ok, acc))                                                             => (ok, cl +: acc)
      }
      val cls: Seq[Clause] = if (ok == 1) cls0 else Seq(
        DataClause(ImplDS, Var(e), KW("?", e + "_attr"), Var(v), Var("_"), Var(v + "_op"))
      )
      val q1 = q.copy(wh = Where(cls))

      // Add necessary bind to ident to prepare working with tx value
      q1.wh.clauses.reverse.collectFirst {
//        case DataClause(_, _, _, _, _, Var(op0)) if op0 == v + "_op" && cls.size > 1    =>
        case DataClause(_, _, _, _, _, Var(op0)) if op0 == v + "_op" && ok == 1    =>
          q1
        case DataClause(_, Var(e0), KW("db", "ident", _), _, _, _) if e0 == e + "_attr" =>
          q1
      } getOrElse
        q1.ident(e + "_attr", v1)
          .func("namespace", Seq(Var(v1)), ScalarBinding(Var(v + "_ns")))
          .func("!=", Seq(Var(v + "_ns"), Val("db.install")))
          .func("!=", Seq(Var(v + "_ns"), Val("db")))
          .func("!=", Seq(Var(v + "_ns"), Val("fressian")))
    }


    // Db ns/attr ..........................................

    def findWithValue(v: String, gs: Seq[Generic]): Query = {
      // Build on from `ns` ident if it is already there
      q.wh.clauses.reverse.collectFirst {
        // `ns`
        case DataClause(_, KW("db.part", "db", _), KW("db.install", "attribute", _), Var(b), _, _) => q.find(v, gs, b)
        // `a`
        case DataClause(_, _, KW("?", "attr", _), Var(b), _, _) => q.find(v, gs, b)
      }.getOrElse(throw new QueryOpsException("Generic `v` has to have a generic attribute `a` before."))
    }

    def attr(e: String, v: String, v1: String, v2: String, gs: Seq[Generic]): Query = {
      // Build on from `ns` ident if it preceeds `a`
      q.wh.clauses.collectFirst {
        case DataClause(_, _, KW("db", "ident", _), Var(b), _, _) =>
          q.func("name", Seq(Var(b)), ScalarBinding(Var(v2)))
      } getOrElse
        q.where(e, "?", "attr", Var(v), "", gs)
          .ident(v, v1)
          .func("name", Seq(Var(v1)), ScalarBinding(Var(v2)))
    }

    def ns(e: String, v: String, v1: String, v2: String, gs: Seq[Generic]): Query = {
      // Build on from `attr` ident if it is already there
      q.wh.clauses.collectFirst {
        case DataClause(_, Var("attr"), KW("db", "ident", _), Var(b), _, _) =>
          q.func(".getNamespace ^clojure.lang.Keyword", Seq(Var(b)), ScalarBinding(Var(v2)))
      } getOrElse
        q.where(e, "?", "ns", Var(v), "", gs)
          .ident(v, v1)
          .func(".getNamespace ^clojure.lang.Keyword", Seq(Var(v1)), ScalarBinding(Var(v2)))
    }

    def attrId(v: String): Query =
      q.copy(wh = Where(q.wh.clauses :+ DataClause(ImplDS, KW("db.part", "db"), KW("db.install", "attribute"), Var(v), Empty)))


    // Extra ..........................................

    def enum(e: String, a: Atom, v: String, gs: Seq[Generic] = Seq()): Query =
      q.where(e, a, v, gs).ident(v, v + 1).kw(v + 1, v + 2)

    def ident(v: String, v1: String, gs: Seq[Generic] = Seq()): Query =
      q.where(v, "db", "ident", Var(v1), "", gs)

    def kw(v1: String, v2: String): Query =
      q.func(".getName ^clojure.lang.Keyword", Seq(Var(v1)), ScalarBinding(Var(v2)))

    def cast(v1: String, v2: String): Query =
      q.func(".toString", Seq(Var(v1)), ScalarBinding(Var(v2)))


    def castStr(tpe: String): String = tpe match {
      case "Int"   => "Long"
      case "Float" => "Double"
      case other   => other
    }

    def compareToMany[T](op: String, a: Atom, v: String, args: Seq[T]): Query =
      args.zipWithIndex.foldLeft(q) {
        case (q1, (arg: URI, i)) =>
          q1.func( s"""ground (java.net.URI. "$arg")""", Empty, v + "_" + (i + 1) + "a")
            .func(".compareTo ^java.net.URI", Seq(Var(v), Var(v + "_" + (i + 1) + "a")), ScalarBinding(Var(v + "_" + (i + 1) + "b")))
            .func(op, Seq(Var(v + "_" + (i + 1) + "b"), Val(0)))
        case (q1, (arg, i))      =>
          q1.compareTo(op, a, v, Val(arg), i + 1)
      }

    def compareTo(op: String, a: Atom, v: String, qv: QueryValue, i: Int = 0): Query =
      compareTo2(op, a.tpeS, v, qv, i)

    def compareTo2(op: String, tpeS: String, v: String, qv: QueryValue, i: Int = 0): Query = {
      val w = Var(if (i > 0) v + "_" + i else v + 2)
      val q1 = tpeS match {
        case "BigInt"       => q.func(".compareTo ^java.math.BigInteger", Seq(Var(v), qv), ScalarBinding(w))
        case "BigDecimal"   => q.func(".compareTo ^java.math.BigDecimal", Seq(Var(v), qv), ScalarBinding(w))
        case "java.net.URI" => qv match {
          case Val(arg) =>
            q.func( s"""ground (java.net.URI. "$arg")""", Empty, v + "_" + (i + 1) + "a")
              .func(".compareTo ^java.net.URI", Seq(Var(v), Var(v + "_" + (i + 1) + "a")), ScalarBinding(w))
          case other    =>
            q.func(".compareTo ^" + castStr(tpeS), Seq(Var(v), qv), ScalarBinding(w))
        }
        case "ns" | "a"     => q.func(".compareTo ^String", Seq(Var(v), qv), ScalarBinding(w))
        case _              => q.func(".compareTo ^" + castStr(tpeS), Seq(Var(v), qv), ScalarBinding(w))
      }
      q1.func(op, Seq(w, Val(0)))
    }

    def fulltext(e: String, a: Atom, v: String, qv: QueryValue): Query =
      q.func("fulltext", Seq(DS, KW(a.ns, a.name), qv), RelationBinding(Seq(Var(e), Var(v))))

    def mappings(e: String, a: Atom, args0: Seq[(String, Any)]): Query = {
      val ruleName = "rule" + (q.i.rules.map(_.name).distinct.size + 1)
      val newRules = args0.foldLeft(q.i.rules) { case (rules, (key, value)) =>
        val dataClauses = Seq(Funct(".matches ^String", Seq(Var(e), Val("^(" + key + ")@(" + value + ")$")), NoBinding))
        val rule = Rule(ruleName, Seq(Var(e)), dataClauses)
        rules :+ rule
      }
      val newIn = q.i.copy(ds = (q.i.ds :+ DS).distinct, rules = newRules)
      val newWhere = Where(q.wh.clauses :+ RuleInvocation(ruleName, Seq(Var(e))))
      q.copy(i = newIn, wh = newWhere)
    }

    def matches(v: String, inVar: Var): Query =
      q.func(".matches ^String", Seq(Var(v), inVar))

    def matches(v: String, regEx: String): Query =
      q.func(".matches ^String", Seq(Var(v), Val(regEx)))

    def matches(v: String, keys: Seq[String], valueRegEx: String): Query = {
      val keyRegEx = if (keys.isEmpty) ".+" else "(" + keys.mkString("|") + ")"
      q.func(".matches ^String", Seq(Var(v), Val(keyRegEx + "@" + valueRegEx)))
    }

    def mapCompareTo(op: String, e: String, a: Atom, v: String, keys: Seq[String], arg: Any, gs: Seq[Generic] = Seq()): Query = {
      lazy val q1 = keys match {
        case Nil   =>
          q.where(e, a, v, gs)
            .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
            .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))
        case keys_ =>
          q.where(e, a, v, gs)
            .func(".matches ^String", Seq(Var(v), Val("(" + keys_.mkString("|") + ")" + "@.*")))
            .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
            .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))
      }
      arg match {
        case _: String  => q1.compareTo(op, a, v + 2, Val(arg), 1)
        case _: UUID    => q1.compareTo(op, a, v + 2, Val(arg), 1)
        case _: URI     => q1.compareTo(op, a, v + 2, Val(arg), 1)
        case _: Boolean => q1.compareTo(op, a, v + 2, Val(arg), 1)
        case _: Date    => q1
          .func(".compareTo ^String", Seq(Var(v + 2), Val(f2(arg))), ScalarBinding(Var(v + 3)))
          .func(op, Seq(Var(v + 3), Val(0)))
        case number     => q1
          .func("read-string", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))
          .func(op, Seq(Var(v + 3), Val(number)))
      }
    }

    def mapInCompareTo(op: String, e: String, a: Atom, v: String, gs: Seq[Generic] = Seq()): Query = {
      val q1 = q
        // Concatenate search string from input vars
        .func("str", Seq(Val("("), Var(v + "Key"), Val(")@.*")), ScalarBinding(Var(v + 1)))
        // match key(s) (could be regex)
        .func(".matches ^String", Seq(Var(v), Var(v + 1)))
        // extract value
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))
      a.tpeS match {
        case "String"         => q1.compareTo(op, a, v + 3, Var(v + "Value"), 1)
        case "UUID"           => q1.compareTo(op, a, v + 3, Var(v + "Value"), 1)
        case "URI"            => q1.compareTo(op, a, v + 3, Var(v + "Value"), 1)
        case "Boolean"        => q1.compareTo(op, a, v + 3, Var(v + "Value"), 1)
        case "java.util.Date" => q1
          .func("java.text.SimpleDateFormat.", Seq(Val("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")), ScalarBinding(Var(v + 4)))
          .func(".format ^java.text.SimpleDateFormat", Seq(Var(v + 4), Var(v + "Value")), ScalarBinding(Var(v + 5)))
          .func(".compareTo ^String", Seq(Var(v + 3), Var(v + 5)), ScalarBinding(Var(v + 6)))
          .func(op, Seq(Var(v + 6), Val(0)))
        case number           => q1
          .func("read-string", Seq(Var(v + 3)), ScalarBinding(Var(v + 4)))
          .func(op, Seq(Var(v + 4), Var(v + "Value")))
      }
    }

    def mapInCompareToK(op: String, e: String, a: Atom, v: String, key: String, gs: Seq[Generic] = Seq()): Query = {
      val q1 = q
        // match key(s) (could be regex)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@.*")))
        // extract value
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))
      a.tpeS match {
        case "String"         => q1.compareTo(op, a, v + 2, Var(v + "Value"), 1)
        case "UUID"           => q1.compareTo(op, a, v + 2, Var(v + "Value"), 1)
        case "URI"            => q1.compareTo(op, a, v + 2, Var(v + "Value"), 1)
        case "Boolean"        => q1.compareTo(op, a, v + 2, Var(v + "Value"), 1)
        case "java.util.Date" => q1
          .func("java.text.SimpleDateFormat.", Seq(Val("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")), ScalarBinding(Var(v + 3)))
          .func(".format ^java.text.SimpleDateFormat", Seq(Var(v + 3), Var(v + "Value")), ScalarBinding(Var(v + 4)))
          .func(".compareTo ^String", Seq(Var(v + 2), Var(v + 4)), ScalarBinding(Var(v + 5)))
          .func(op, Seq(Var(v + 5), Val(0)))
        case number           => q1
          .func("read-string", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))
          .func(op, Seq(Var(v + 3), Var(v + "Value")))
      }
    }

    def mapIn(e: String, a: Atom, v: String, gs: Seq[Generic]): Query =
      q.in(e, a, None, v + "Key").in(e, a, None, v + "Value").where(e, a, v, gs)

    def mapIn2(e: String, a: Atom, v: String, gs: Seq[Generic]): Query =
      q.in(e, a, None, v + "Value").where(e, a, v, gs)

    def matchRegEx(v: String, regex: Seq[QueryTerm]): Query =
      q.func("str", regex, ScalarBinding(Var(v + 1))).matches(v, Var(v + 1))


    def esc(arg: Any) = arg match {
      case s: String => s.replaceAll("\"", "\\\\\"")
      case other     => other
    }

    def orRules2(v: String, args: Seq[Any]): Query = {
      val ruleName = "rule" + (q.i.rules.map(_.name).distinct.size + 1)
      val orRules: Seq[Rule] = args.distinct.map { arg =>
        Rule(ruleName, Seq(Var(v)), Seq(Funct("=", Seq(Var(v), Val(arg)), NoBinding)))
      }
      val newIn = q.i.copy(ds = (q.i.ds :+ DS).distinct, rules = q.i.rules ++ orRules)
      val newWhere = Where(q.wh.clauses :+ RuleInvocation(ruleName, Seq(Var(v))))
      q.copy(i = newIn, wh = newWhere)
    }

    def orRules(e: String, a: Atom, args: Seq[Any], specialV: String = "", flag: Boolean = false): Query = {
      val ruleName = "rule" + (q.i.rules.map(_.name).distinct.size + 1)
      val orRules = if (flag && a.card == 2) {
        // Fulltext search for card-many attribute
        val ruleClauses = args.zipWithIndex.map { case (arg, i) =>
          Funct("fulltext", Seq(DS(""), KW(a.ns, a.name), Val(arg)), RelationBinding(List(Var(e), Var(e + "_" + (i + 1)))))
        }
        Seq(Rule(ruleName, Seq(Var(e)), ruleClauses))
      } else {
        args.zipWithIndex.distinct.flatMap { case (arg, i) =>
          val ruleClauses = arg match {
            case set: Set[_] if specialV.nonEmpty => set.toSeq.zipWithIndex.flatMap { case (uri, j) =>
              val x = Var(specialV + "_" + (j + 1))
              Seq(
                DataClause(ImplDS, Var(e), KW(a.ns, a.name), x, Empty),
                Funct( s"""ground (java.net.URI. "${esc(uri)}")""", Nil, ScalarBinding(x))
              )
            }
            case set: Set[_]                      => set.toSeq.map(arg =>
              DataClause(ImplDS, Var(e), KW(a.ns, a.name), Val(pre(a, arg)), Empty)
            )
            case mapArg if a.card == 3            => Seq(
              Funct(".matches ^String", Seq(Var(e), Val(".+@" + esc(mapArg))), NoBinding)
            )
            case ns if specialV.nonEmpty && flag  => Seq(
              Funct("=", Seq(Var(specialV), Val(arg)), NoBinding)
            )
            case uri if specialV.nonEmpty         => Seq(
              DataClause(ImplDS, Var(e), KW(a.ns, a.name), Var(specialV), Empty),
              Funct( s"""ground (java.net.URI. "${esc(uri)}")""", Nil, ScalarBinding(Var(specialV)))
            )
            case fulltext if flag                 => Seq(
              Funct("fulltext", Seq(DS(""), KW(a.ns, a.name), Val(arg)), RelationBinding(List(Var(e), Var(e + "_" + (i + 1)))))
            )
            case _                                => Seq(
              DataClause(ImplDS, Var(e), KW(a.ns, a.name), Val(pre(a, esc(arg))), Empty)
            )
          }
          if (ruleClauses.isEmpty) None else Some(Rule(ruleName, Seq(Var(e)), ruleClauses))
        }
      }
      val newIn = q.i.copy(ds = (q.i.ds :+ DS).distinct, rules = q.i.rules ++ orRules)
      val newWhere = Where(q.wh.clauses :+ RuleInvocation(ruleName, Seq(Var(e))))
      q.copy(i = newIn, wh = newWhere)
    }

    def func(name: String, qt: QueryTerm, v: String): Query =
      q.copy(wh = Where(q.wh.clauses :+ Funct(name, Seq(qt), ScalarBinding(Var(v)))))

    def func(name: String, ins: Seq[QueryTerm], outs: Binding = NoBinding): Query =
      q.copy(wh = Where(q.wh.clauses :+ Funct(name, ins, outs)))

    def ref(e: String, ns: String, refAttr: String, v: String, refNs: String): Query =
      q.copy(wh = Where(q.wh.clauses :+ DataClause(ImplDS, Var(e), KW(ns, refAttr, refNs), Var(v), Empty)))


    // Java conversions ...........................................................

    private def cast(a: Any): AnyRef = a match {
      case i: Int           => i.toLong.asInstanceOf[Object]
      case f: Float         => f.toDouble.asInstanceOf[Object]
      case bigI: BigInt     => bigI.bigInteger
      case bigD: BigDecimal => bigD.bigDecimal
      case other            => other.asInstanceOf[Object]
    }

    def inputs: Seq[AnyRef] = q.i.inputs.map {
      case InVar(RelationBinding(_), Nil)         => Util.list()
      case InVar(RelationBinding(_), argss)       => Util.list(argss.map(args => Util.list(args map cast: _*)): _*)
      case InVar(CollectionBinding(_), Nil)       => Util.list()
      case InVar(CollectionBinding(_), argss)     => Util.list(argss.head map cast: _*)
      case InVar(_, Nil)                          => Util.list()
      case InVar(_, argss) if argss.head.size > 1 => Nil
      case InVar(_, argss)                        => cast(argss.head.head)
      case InDataSource(_, Nil)                   => Util.list()
      case InDataSource(_, argss)                 => cast(argss.head.head)
      case other                                  => throw new QueryOpsException(s"UNEXPECTED input: $other\nquery:\n$q")
    }
  }
}
