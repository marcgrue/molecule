package molecule.api

import molecule.Conn
import molecule.ast.model._
import molecule.ast.query._


// 2 inputs X outputs

trait InputMolecule_2[I1, I2] extends InputMolecule {

  def resolveAnd(and: And2[I1, I2])(implicit conn: Conn): (Seq[I1], Seq[I2]) = {
    def traverse(expr: And2[I1, I2]): (Seq[I1], Seq[I2]) = expr match {
      case And2(TermValue(v1), TermValue(v2)) => (Seq(v1), Seq(v2))
      case And2(or1: Or[I1], or2: Or[I2])     => (resolveOr(or1), resolveOr(or2))
      case _                                  => sys.error(s"Unexpected expression: " + expr)
    }
    traverse(and)
  }

  def resolveOr(or: Or2[I1, I2])(implicit conn: Conn): Seq[(I1, I2)] = {
    type V[T] = TermValue[T]
    def traverse(expr: Or2[I1, I2]): Seq[(I1, I2)] = expr match {
      case Or2(And2(TermValue(a1), TermValue(a2)), or2: Or2[I1, I2])                   => (a1, a2) +: traverse(or2)
      case Or2(And2(TermValue(a1), TermValue(a2)), And2(TermValue(b1), TermValue(b2))) => Seq((a1, a2), (b1, b2))
      case _                                                                           => sys.error(s"Unexpected expression: " + expr)
    }
    traverse(or)
  }

  def bindValues1(query: Query, inputTuples: Seq[(I1, I2)]) = {
    val (vars, Seq(p1, p2)) = varsAndPrefixes(query).unzip
    val values = inputTuples.map(tpl => Seq(p1 + tpl._1, p2 + tpl._2))
    query.copy(i = In(Seq(InVar(RelationBinding(vars), values)), query.i.rules, query.i.ds))
  }

  def inputValues1(query: Query, inputTuples: Seq[(I1, I2)]) = {
    val (vars, Seq(p1, p2)) = varsAndPrefixes(query).unzip
    inputTuples.map(tpl => Seq(p1 + tpl._1, p2 + tpl._2))
//    val values = inputTuples.map(tpl => Seq(p1 + tpl._1, p2 + tpl._2))
//    values.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n")
  }


//  def bindValues2(query: Query, inputLists: (Seq[I1], Seq[I2])) = {
  def bindValues2(query: Query, input1: Seq[I1], input2: Seq[I2]) = {

    // Extract placeholder info and discard placeholders
    val varsAndPrefixes = query.i.inputs.collect {
      case Placeholder(_, kw, enumPrefix, e) => (kw, enumPrefix, e)
    }
    val query1 = query.copy(i = In(Seq(), query.i.rules, query.i.ds))

    if (varsAndPrefixes.size != 2)
      sys.error(s"[InputMolecule_2] Query should expect exactly 2 inputs:\nQuery: ${query.datalog}")

    // Add rules for each list of inputs
//    val query2 = inputLists.productIterator.toList.zip(varsAndPrefixes).foldLeft(query1) {
    val query2 = Seq(input1, input2).zip(varsAndPrefixes).foldLeft(query1) {
      case (q, (inputList: Seq[_], (kw, enumPrefix, e))) => {
        // Add rule for each input value
        val ruleName = "rule" + (q.i.rules.map(_.name).distinct.size + 1)
        val newRules = inputList.foldLeft(q.i.rules) { case (rules, input) =>
          val value = if (enumPrefix.isDefined) enumPrefix.get + input else input
          val dataClause = DataClause(ImplDS, Var(e), kw, Val(value), Empty)
          val rule = Rule(ruleName, Seq(Var(e)), Seq(dataClause))
          rules :+ rule
        }
        val newIn = q.i.copy(ds = (q.i.ds :+ DS).distinct, rules = newRules)
        val newWhere = Where(q.wh.clauses :+ RuleInvocation(ruleName, Seq(Var(e))))
        q.copy(i = newIn, wh = newWhere)
      }
    }
    query2
  }

//  def inputValues2(inputLists: (Seq[I1], Seq[I2])) = {
//    val (vars, Seq(p1, p2)) = varsAndPrefixes.unzip
//    val query2 = inputLists.productIterator.toList.zip(varsAndPrefixes).map {
//          case (inputList: Seq[_], (kw, tpeS, enumPrefix, e)) => {
//
//
//            // Add rule for each input value
//            val ruleName = "rule" + (q.i.rules.map(_.name).distinct.size + 1)
//            val newRules = inputList.foldLeft(q.i.rules) { case (rules, input) =>
//              val value = if (enumPrefix.isDefined) enumPrefix.get + input else input
//              val dataClause = DataClause(ImplDS, Var(e, tpeS), kw, Val(value, tpeS), Empty)
//              val rule = Rule(ruleName, Seq(Var(e, tpeS)), Seq(dataClause))
//              rules :+ rule
//            }
//            val newIn = q.i.copy(ds = (q.i.ds :+ DS).distinct, rules = newRules)
//            val newWhere = Where(q.wh.clauses :+ RuleInvocation(ruleName, Seq(Var(e, "Long"))))
//            q.copy(i = newIn, wh = newWhere)
//          }
//        }
//
//    val values = inputTuples.map(tpl => Seq(p1 + tpl._1, p2 + tpl._2))
//    values.zipWithIndex.map(e => "\n" + (e._2 + 1) + " " + e._1) + "\n"
//  }
}

abstract class InputMolecule_2_0[I1, I2](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule0
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule0 = apply(Seq((i1, i2)))(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule0 = apply(resolveOr(or))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule0 = apply(head +: tail)(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule0
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule0 = and match {
    case And2(TermValue(v1), TermValue(v2)) => apply(Seq((v1, v2)))(conn)
    case And2(or1: Or[I1], or2: Or[I2])     => apply(resolveOr(or1), resolveOr(or2))(conn)
    case _                                  => sys.error(s"[InputMolecule_2] Unexpected `and`: " + and)
  }
}

abstract class InputMolecule_2_1[I1, I2, A](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule1[A]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule1[A] = apply(Seq((i1, i2)))(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule1[A] = apply(resolveOr(or))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule1[A] = apply(head +: tail)(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule1[A]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule1[A] = and match {
    case And2(TermValue(v1), TermValue(v2)) => apply(Seq((v1, v2)))(conn)
    case And2(or1: Or[I1], or2: Or[I2])     => apply(resolveOr(or1), resolveOr(or2))(conn)
    case _                                  => sys.error(s"[InputMolecule_2] Unexpected `and`: " + and)
  }
}

abstract class InputMolecule_2_2[I1, I2, A, B](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule2[A, B]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule2[A, B] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule2[A, B] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule2[A, B] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule2[A, B]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule2[A, B] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_3[I1, I2, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule3[A, B, C]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule3[A, B, C] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule3[A, B, C] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule3[A, B, C] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule3[A, B, C]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule3[A, B, C] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_4[I1, I2, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule4[A, B, C, D]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule4[A, B, C, D] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule4[A, B, C, D] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule4[A, B, C, D] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule4[A, B, C, D]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule4[A, B, C, D] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_5[I1, I2, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule5[A, B, C, D, E]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule5[A, B, C, D, E] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule5[A, B, C, D, E] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule5[A, B, C, D, E] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule5[A, B, C, D, E]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule5[A, B, C, D, E] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_6[I1, I2, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule6[A, B, C, D, E, F]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule6[A, B, C, D, E, F] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule6[A, B, C, D, E, F] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule6[A, B, C, D, E, F] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule6[A, B, C, D, E, F]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule6[A, B, C, D, E, F] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_7[I1, I2, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule7[A, B, C, D, E, F, G]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule7[A, B, C, D, E, F, G] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule7[A, B, C, D, E, F, G] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule7[A, B, C, D, E, F, G] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule7[A, B, C, D, E, F, G]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule7[A, B, C, D, E, F, G] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_8[I1, I2, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule8[A, B, C, D, E, F, G, H]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule8[A, B, C, D, E, F, G, H] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule8[A, B, C, D, E, F, G, H] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule8[A, B, C, D, E, F, G, H] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule8[A, B, C, D, E, F, G, H]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule8[A, B, C, D, E, F, G, H] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_9[I1, I2, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule9[A, B, C, D, E, F, G, H, I]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule9[A, B, C, D, E, F, G, H, I] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule9[A, B, C, D, E, F, G, H, I] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule9[A, B, C, D, E, F, G, H, I] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule9[A, B, C, D, E, F, G, H, I]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule9[A, B, C, D, E, F, G, H, I] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}

abstract class InputMolecule_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_2[I1, I2] {
  def apply(ins: Seq[(I1, I2)])             (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  def apply(i1: I1, i2: I2)                 (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(Seq((i1, i2)))(conn)
  def apply(head: (I1, I2), tail: (I1, I2)*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(head +: tail)(conn)
  def apply(or: Or2[I1, I2])                (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr(or))(conn)
  def apply(in1: Seq[I1], in2: Seq[I2])     (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  def apply(and: And2[I1, I2])              (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = {
    val (in1, in2) = resolveAnd(and)
    apply(in1, in2)(conn)
  }
}
