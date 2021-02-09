package molecule.internaltests.shared.transform.optimize

import molecule.datomic.base.ast.query.{Query, _}
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out2._
import molecule.setup.TestSpec


class ClauseOrder extends TestSpec {
  sequential


  "Equality" in new CoreSetup {
    val m1 = m(Ns.str.int(42))

    m1._rawQuery === Query(
      Find(List(
        Var("b"),
        Var("c"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "str", ""), Var("b"), Empty, NoBinding),
        Funct("ground 42", Seq(Empty), ScalarBinding(Var("c"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("c"), Empty, NoBinding))))

    // Binding 42 is promoted as first clause in optimized query
    m1._query === Query(
      Find(List(
        Var("b"),
        Var("c"))),
      Where(List(
        Funct("ground 42", Seq(Empty), ScalarBinding(Var("c"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "str", ""), Var("b"), Empty, NoBinding))))
  }


  "Optional" in new CoreSetup {
    val m1 = m(Ns.str.int$)

    m1._rawQuery === Query(
      Find(List(
        Var("b"),
        Pull("a__1", "Ns", "int", None))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "str", ""), Var("b"), Empty, NoBinding),
        Funct("identity", Seq(Var("a")), ScalarBinding(Var("a__1")))
      )))

    m1._query === Query(
      Find(List(
        Var("b"),
        Pull("a__1", "Ns", "int", None))),
      Where(List(
        Funct("identity", Seq(Var("a")), ScalarBinding(Var("a__1"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "str", ""), Var("b"), Empty, NoBinding))))
  }


  "Fulltext" in new CoreSetup {
    val m1 = m(Ns.int.str.contains("hello"))

    m1._rawQuery === Query(
      Find(List(
        Var("b"),
        Var("c"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
        Funct("fulltext", Seq(DS, KW("Ns", "str", ""), Val("hello")), RelationBinding(List(Var("a"), Var("c")))))))

    m1._query === Query(
      Find(List(
        Var("b"),
        Var("c"))),
      Where(List(
        Funct("fulltext", Seq(DS, KW("Ns", "str", ""), Val("hello")), RelationBinding(List(Var("a"), Var("c")))),
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding))))
  }


  "not" in new CoreSetup {
    val m1 = m(Ns.int.str_(Nil))

    m1._rawQuery === Query(
      Find(List(
        Var("b"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
        NotClause(Var("a"), KW("Ns", "str", "")))))

    m1._query === Query(
      Find(List(
        Var("b"))),
      Where(List(
        NotClause(Var("a"), KW("Ns", "str", "")),
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding))))
  }
}