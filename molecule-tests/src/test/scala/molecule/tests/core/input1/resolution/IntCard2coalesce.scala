package molecule.tests.core.input1.resolution

import molecule.datomic.base.ast.query._
import molecule.core.exceptions.MoleculeException
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in1_out2._
import molecule.setup.TestSpec


class IntCard2coalesce extends TestSpec {

  class ManySetup extends CoreSetup {
    Ns.int.ints$ insert List(
      (int1, Some(Set(int1, int2))),
      (int2, Some(Set(int2, int3))),
      (int3, Some(Set(int3, int4))),
      (int4, Some(Set(int4, int5))),
      (int5, Some(Set(int4, int5, int6))),
      (int6, None)
    )
  }

  "Eq" in new ManySetup {

    val inputMolecule = m(Ns.ints(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("b"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(Nil).get === Nil
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(CollectionBinding(Var("b")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))

    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(CollectionBinding(Var("b")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === Nil
    inputMolecule(List(Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(CollectionBinding(Var("b")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set(int1))).get === List(Set(int1, int2))
    inputMolecule(List(Set(int1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
    inputMolecule(List(Set(int3))).get === List(Set(int2, int3, int4)) // (int2, int3) + (int3, int4)


    inputMolecule(List(Set(int1, int1))).get === List(Set(int1, int2))
    inputMolecule(List(Set(int1, int1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1, int2))).get === List(Set(int1, int2))
    inputMolecule(List(Set(int1, int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(int1, int3))).get === Nil
    inputMolecule(List(Set(int2, int3))).get === List(Set(int2, int3))
    inputMolecule(List(Set(int4, int5))).get === List(Set(int4, int5, int6)) // (int4, int5) + (int4, int5, int6)


    inputMolecule(List(Set(int1, int2), Set[Int]())).get === List(Set(int1, int2))
    inputMolecule(List(Set(int1, int2), Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int1, int2))
    inputMolecule(List(Set(int1), Set(int1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
    inputMolecule(List(Set(int1), Set(int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(int1), Set(int3))).get === List(Set(int1, int4, int3, int2)) // (int1, int2) + (int2, int3) + (int3, int4)


    inputMolecule(List(Set(int1, int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
    inputMolecule(List(Set(int1, int2), Set(int3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int2, int3))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
    inputMolecule(List(Set(int1), Set(int2, int3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3)
    inputMolecule(List(Set(int1), Set(int2), Set(int3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1, int2), Set(int3, int4))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int3, int4)
    inputMolecule(List(Set(int1, int2), Set(int3, int4)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int4), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))
  }


  "!=" in new CoreSetup {

    val all = List(
      (int1, Set(int1, int2, int3)),
      (int2, Set(int2, int3, int4)),
      (int3, Set(int3, int4, int5))
    )
    Ns.int.ints insert all

    val inputMolecule = m(Ns.ints.not(?)) // or m(Ns.ints.!=(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("b1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(int1, int2, int3, int4, int5))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4, int5))
    inputMolecule(List(Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set(int1))).get === List(Set(int2, int3, int4, int5))
    inputMolecule(List(Set(int1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding))))))

    inputMolecule(List(Set(int2))).get === List(Set(int3, int4, int5))
    inputMolecule(List(Set(int3))).get === Nil


    inputMolecule(List(Set(int1, int2))).get === List(Set(int2, int3, int4, int5))
    inputMolecule(List(Set(int1, int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))))))


    // nothing omitted
    inputMolecule(List(Set(int1, int3))).get === List(Set(int2, int3, int4, int5))
    inputMolecule(List(Set(int1, int3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding))))))


    inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int2, int3, int4, int5))
    inputMolecule(List(Set(int1), Set(int1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding))))))


    inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int3, int4, int5))
    inputMolecule(List(Set(int1), Set(int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(2), Empty, NoBinding))))))


    inputMolecule(List(Set(int1), Set(int3))).get === Nil
    inputMolecule(List(Set(int1), Set(int4))).get === Nil

    inputMolecule(List(Set(int1, int2), Set(int1))).get === List(Set(int2, int3, int4, int5))
    inputMolecule(List(Set(int1, int2), Set(int2))).get === List(Set(int3, int4, int5))
    inputMolecule(List(Set(int1, int2), Set(int3))).get === Nil
    inputMolecule(List(Set(int1, int2), Set(int3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(2), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(3), Empty, NoBinding))))))


    inputMolecule(List(Set(int1, int2), Set(int2, int3))).get === List(Set(int3, int4, int5))
    inputMolecule(List(Set(int1, int2), Set(int2, int3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding))))))
  }


  ">" in new ManySetup {

    val inputMolecule = m(Ns.ints.>(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("b1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct(">", Seq(Var("b2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(List(Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    // (int3, int4), (int4, int5), (int4, int5, int6)
    inputMolecule(List(Set(int2))).get === List(Set(int4, int6, int3, int5))
    inputMolecule(List(Set(int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct(">", Seq(Var("b2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {

    val inputMolecule = m(Ns.ints.>=(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("b1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct(">=", Seq(Var("b2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(List(Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    // (int2, int4), (int3, int4), (int4, int5), (int4, int5, int6)
    inputMolecule(List(Set(int2))).get === List(Set(int5, int6, int2, int3, int4))
    inputMolecule(List(Set(int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct(">=", Seq(Var("b2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {

    val inputMolecule = m(Ns.ints.<(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("b1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct("<", Seq(Var("b2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(List(Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set(int2))).get === List(Set(int1))
    inputMolecule(List(Set(int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct("<", Seq(Var("b2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {

    val inputMolecule = m(Ns.ints.<=(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("b1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct("<=", Seq(Var("b2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
    inputMolecule(List(Set[Int]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding))))


    inputMolecule(List(Set(int2))).get === List(Set(int1, int2))
    inputMolecule(List(Set(int2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b")))),
      In(
        List(
          InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("b"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
        Funct("<=", Seq(Var("b2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}