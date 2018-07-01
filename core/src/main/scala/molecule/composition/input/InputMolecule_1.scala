package molecule.composition.input

import molecule.action.Molecule
import molecule.action.MoleculeOut._
import molecule.ast.model._
import molecule.ast.query._
import molecule.facade.Conn


// 1 input X outputs

trait InputMolecule_1[I1] extends InputMolecule {

  protected def bindValues1(query: Query, in1: Seq[I1]) = {
    val (inVars, prefixes) = varsAndPrefixes(query).unzip
    val prefix = prefixes.head
    val values = if (prefix != "")
      in1.flatMap {
        case set: Set[_] => Seq(set.toSeq.map(setValue => prefix + setValue.toString))
        case one         => Seq(Seq(prefix + one.toString))
      }
    else
      in1.flatMap {
        case map: Map[_, _] => map.toSeq.map { case (k, v) => Seq(k, v) }
        case set: Set[_]    => Seq(set.toSeq)
        case one            => Seq(Seq(one))
      }

    if (inVars.size > 1)
      query.copy(i = In(Seq(InVar(RelationBinding(inVars), values)), query.i.rules, query.i.ds))
    else if (values.size > 1)
      query.copy(i = In(Seq(InVar(CollectionBinding(inVars.head), Seq(values.flatten))), query.i.rules, query.i.ds))
    else
      query.copy(i = In(Seq(InVar(ScalarBinding(inVars.head), values)), query.i.rules, query.i.ds))
  }



  /** == OR semantics with Seq of values ==
    *
    * Resolve input molecule by applying a Set of values that the attribute is expected to have (OR semantics).
    * {{{
    *   val inputMolecule = Ns.attr(?)
    *
    *   inputMolecule.apply(Seq(V1, V2))
    *
    *   // Same as
    *   inputMolecule(Set(V1, V2))
    *   inputMolecule(V1 or V2)
    *   inputMolecule(V1, V2)
    * }}}
    * <p>
    *   Querying the molecule will get all entities having `attr` set to the values applied.
    * @note Only distinct values are matched.
    */
  def apply(args: Seq[I1])         (implicit conn: Conn): Molecule


  /** == OR semantics with Set of values ==
    *
    * Resolve input molecule by applying a Set of values that the attribute is expected to have (OR semantics).
    * {{{
    *   val inputMolecule = Ns.attr(?)
    *
    *   inputMolecule.apply(Set(V1, V2))
    *
    *   // Same as
    *   inputMolecule(Seq(V1, V2))
    *   inputMolecule(V1 or V2)
    *   inputMolecule(V1, V2)
    * }}}
    * <p>
    *   Querying the molecule will get all entities having `attr` set to the values applied.
    * @note Since a Set can only have distinct values, no duplicate values will be matched.
    */
  def apply(args: Set[I1])         (implicit conn: Conn): Molecule


  /** == OR semantics with `or`-separated values ==
    *
    * Resolve input molecule by applying two or more or-separated values that the attribute is expected to have.
    * {{{
    *   val inputMolecule = Ns.attr(?)
    *
    *   inputMolecule.apply(V1 or V2) // (or V3 or etc...)
    *
    *   // Same as
    *   inputMolecule(V1, V2)
    *   inputMolecule(Seq(V1, V2))
    *   inputMolecule(Set(V1, V2))
    * }}}
    * <p>
    *   Querying the molecule will get all entities having `attr` set to the values applied.
    */
  def apply(or: Or[I1])            (implicit conn: Conn): Molecule


  /** == OR semantics with comma-separated values ==
    *
    * Resolve input molecule by applying two or more comma-separated values that the attribute is expected to have (OR semantics).
    * {{{
    *   val inputMolecule = Ns.attr(?)
    *
    *   inputMolecule.apply(V1, V2) // (or more values...)
    *
    *   // Same as
    *   inputMolecule(V1 or V2)
    *   inputMolecule(Seq(V1, V2))
    *   inputMolecule(Set(V1, V2))
    * }}}
    * <p>
    *   Querying the molecule will get all entities having `attr` set to the values applied.
    */
  def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule
}


object InputMolecule_1 {

  abstract class InputMolecule_1_00[I1](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule00
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule00 = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule00 = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule00 = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_01[I1, A](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule01[A]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule01[A] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule01[A] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule01[A] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_02[I1, A, B](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule02[A, B]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule02[A, B] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule02[A, B] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_03[I1, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule03[A, B, C]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule03[A, B, C] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule03[A, B, C] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_04[I1, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule04[A, B, C, D] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_05[I1, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_06[I1, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_07[I1, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_08[I1, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(arg +: moreArgs)(conn)
  }
  
  abstract class InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(args: Seq[I1])         (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(args: Set[I1])         (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(args.toSeq)
    def apply(or: Or[I1])            (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr(or))(conn)
    def apply(arg: I1, moreArgs: I1*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(arg +: moreArgs)(conn)
  }
}
