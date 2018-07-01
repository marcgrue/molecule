package molecule.composition.input

import molecule.action.Molecule
import molecule.action.MoleculeOut._
import molecule.ast.model._
import molecule.ast.query._
import molecule.facade.Conn


// 3 inputs X outputs

trait InputMolecule_3[I1, I2, I3] extends InputMolecule {

  // Todo
  //  def resolveAnd(and: And3[(I1, I2, I3)])                (implicit conn: Conn): Seq[(I1, I2, I3)] = {
  //    def recurse(expr: And3[(I1, I2, I3)]): Seq[(I1, I2, I3)] = expr match {
  //      case And3(TermValue(v1), TermValue(v2), TermValue(v3))   => Seq((v1, v2, v3))
  //      case And3(and1: And3[I1, I2, I3], and2: And3[(I1, I2, I3)]) => recurse(and1) ++ recurse(and2)
  //    }
  //    recurse(and)
  //  }
  //
  //  def resolveOr(or: Or[(I1, I2, I3)])                (implicit conn: Conn): Seq[(I1, I2, I3)] = {
  //    def recurse(expr: Or[(I1, I2, I3)]): Seq[(I1, I2, I3)] = expr match {
  //      case Or(or1: Or[(I1, I2, I3)], And3(t4: TermValue[I1, I2, I3], t5: TermValue[I2], t6: TermValue[I3])) =>
  //        recurse(or1) :+ (t4.term, t5.term, t6.term)
  //      case Or(
  //      And3(t1: TermValue[I1, I2, I3], t2: TermValue[I2], t3: TermValue[I3]),
  //      And3(t4: TermValue[I1, I2, I3], t5: TermValue[I2], t6: TermValue[I3]))                                =>
  //        Seq((t1.term, t2.term, t3.term), (t4.term, t5.term, t6.term))
  //    }
  //    recurse(or)
  //  }


  def bindValues1(query: Query, inputTuples: Seq[(I1, I2, I3)]) = {
    val (vars, Seq(p1 ,p2,p3)) = varsAndPrefixes(query).unzip
    val values = inputTuples.map(tpl => Seq(p1 + tpl._1, p2 + tpl._2, p3 + tpl._3))
    query.copy(i = In(Seq(InVar(RelationBinding(vars), values)), query.i.rules, query.i.ds))
  }

  def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule
  def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule
  def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule
}


object InputMolecule_3 {

  abstract class InputMolecule_3_00[I1, I2, I3](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule00
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule00 = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule00 = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_01[I1, I2, I3, A](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule01[A]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule01[A] = apply(tpl +: tpls)(conn)
    // todo?
    //  def apply(and: And3[(I1, I2, I3)])                (implicit conn: Conn): Molecule01[A] = apply(resolveAnd(and))(conn)
    //  def apply(or: Or[(I1, I2, I3)])                (implicit conn: Conn): Molecule01[A] = apply(Seq((i1, i2, i3)))(conn)
  }
  
  abstract class InputMolecule_3_02[I1, I2, I3, A, B](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule02[A, B]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule02[A, B] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule02[A, B] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_03[I1, I2, I3, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule03[A, B, C]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule03[A, B, C] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule03[A, B, C] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_04[I1, I2, I3, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule04[A, B, C, D] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_05[I1, I2, I3, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_06[I1, I2, I3, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_07[I1, I2, I3, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_08[I1, I2, I3, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_09[I1, I2, I3, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_10[I1, I2, I3, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_11[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_12[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(tpl +: tpls)(conn)
  }
  
  abstract class InputMolecule_3_22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_3[I1, I2, I3] {
    def apply(ins: Seq[(I1, I2, I3)])                (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(i1: I1, i2: I2, i3: I3)                (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2, i3)))(conn)
    def apply(tpl: (I1, I2, I3), tpls: (I1, I2, I3)*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(tpl +: tpls)(conn)
  }
}