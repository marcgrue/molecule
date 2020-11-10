package molecule.internaltests.shared.transform

import molecule.core.ast.model._
import molecule.core.ast.query.{AggrExpr, DataClause, Empty, Find, ImplDS, In, KW, NoBinding, Query, Var, Where, With}
import molecule.core.transform.Model2Query
import molecule.core.util.MoleculeSpec

class Model2QueryTest extends MoleculeSpec {


  "Multiple aggregates share attribute value" >> {

    Model2Query(Model(List(
      Atom("Obj", "meanRadius", "Double", 1, Fn("sum", None), None, Seq(), Seq()),
      Atom("Obj", "meanRadius", "Double", 1, Fn("avg", None), None, Seq(), Seq())
    )))._1.toString ===
      Query(
        Find(List(
          AggrExpr("sum", Seq(), Var("b")),
          AggrExpr("avg", Seq(), Var("b")))),
        With(List("a")),
        In(Nil, Nil, Nil),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Obj", "meanRadius", ""), Var("b"), Empty, NoBinding)
        ))).toString


    Model2Query(Model(List(
      Atom("Obj", "meanRadius", "Double", 2, Fn("sum", None), None, Seq(), Seq()),
      Atom("Obj", "meanRadius", "Double", 2, Fn("avg", None), None, Seq(), Seq())
    )))._1.toString ===
      Query(
        Find(List(
          AggrExpr("sum", Seq(), Var("b")),
          AggrExpr("avg", Seq(), Var("b")))),
        With(List("a")),
        In(Nil, Nil, Nil),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Obj", "meanRadius", ""), Var("b"), Empty, NoBinding)
        ))).toString


  }
}
