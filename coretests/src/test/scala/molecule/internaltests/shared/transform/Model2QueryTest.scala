package molecule.internaltests.shared.transform

import molecule.ast.model._
import molecule.ast.query.{AggrExpr, DataClause, Empty, Find, ImplDS, In, KW, NoBinding, Var, Where, With}
//import molecule.ast.query._
import molecule.ast.query.Query
import molecule.transform.Model2Query
import molecule.util.MoleculeSpec

class Model2QueryTest extends MoleculeSpec {


  "example adhoc" >> {


    Model2Query(Model(List(
      Atom("Obj", "meanRadius", "Double", 1, Fn("sum", None), None, Seq(), Seq()),
      Atom("Obj", "meanRadius", "Double", 1, Fn("avg", None), None, Seq(), Seq())
    )))._1 ===
      Query(
        Find(List(
          AggrExpr("sum", Seq(), Var("b")),
          AggrExpr("avg", Seq(), Var("c")))),
        With(List(
          "a")),
        In(Nil, Nil, Nil),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Obj", "meanRadius", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Obj", "meanRadius", ""), Var("c"), Empty, NoBinding))))


    Model2Query(Model(List(
      Atom("Obj", "meanRadius", "Double", 1, Fn("sum", None), None, Seq(), Seq()),
      Atom("Obj", "meanRadius", "Double", 1, Fn("avg", None), None, Seq(), Seq())
    )))._1 ===
      Query(
        Find(List(
          AggrExpr("sum", Seq(), Var("b")),
          AggrExpr("avg", Seq(), Var("b")))),
        With(List("a")),
        In(Nil, Nil, Nil),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Obj", "meanRadius", ""), Var("b"), Empty, NoBinding)
        )))
  }
}
