package molecule.coretests.input1.resolution

import molecule.api.in1_out2._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.input.exception.InputMoleculeException


class IntCard2 extends CoreSpec {

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

    val inputMolecule = m(Ns.int.ints(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("c"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(Nil).get === Nil
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          InVar(CollectionBinding(Var("c")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    // AND semantics when applying int1 Set of values matching input attribute values of int1 entity

    inputMolecule(List(Set[Int]())).get === Nil
    inputMolecule(List(Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          InVar(CollectionBinding(Var("c")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set(int1))).get === List((int1, Set(int1, int2)))
    inputMolecule(List(Set(int1)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
    inputMolecule(List(Set(int3))).get === List((int2, Set(int3, int2)), (int3, Set(int4, int3)))


    inputMolecule(List(Set(int1, int1))).get === List((int1, Set(int1, int2)))
    inputMolecule(List(Set(int1, int1)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1, int2))).get === List((int1, Set(int1, int2)))
    inputMolecule(List(Set(int1, int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(int1, int3))).get === Nil
    inputMolecule(List(Set(int2, int3))).get === List((int2, Set(int3, int2)))
    inputMolecule(List(Set(int4, int5))).get === List((int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))


    inputMolecule(List(Set(int1, int2), Set[Int]())).get === List((int1, Set(int1, int2)))
    inputMolecule(List(Set(int1, int2), Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int1))).get === List((int1, Set(int1, int2)))
    inputMolecule(List(Set(int1), Set(int1)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
    inputMolecule(List(Set(int1), Set(int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(int1), Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))


    inputMolecule(List(Set(int1, int2), Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
    inputMolecule(List(Set(int1, int2), Set(int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
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
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int2, int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
    inputMolecule(List(Set(int1), Set(int2, int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
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
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
    inputMolecule(List(Set(int1), Set(int2), Set(int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
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
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))


    inputMolecule(List(Set(int1, int2), Set(int3, int4))).get === List((int1, Set(int1, int2)), (int3, Set(int4, int3)))
    inputMolecule(List(Set(int1, int2), Set(int3, int4)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
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
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        RuleInvocation("rule1", Seq(Var("a"))))))
  }


  "!=" in new CoreSetup {

    val all = List(
      (1, Set(int1, int2, int3)),
      (2, Set(int2, int3, int4)),
      (3, Set(int3, int4, int5))
    )
    Ns.int.ints insert all

    val inputMolecule = m(Ns.int.ints.not(?)) // or m(Ns.int.ints.!=(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("c1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === all
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === all
    inputMolecule(List(Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set(int1))).get === List(
      // (1, Set(int1, int2, int3)),  // int1 match
      (2, Set(int2, int3, int4)),
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int1)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding))))))

    inputMolecule(List(Set(int2))).get === List(
      // (1, Set(int1, int2, int3)),  // int2 match
      // (2, Set(int2, int3, int4)),  // int2 match
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int3))).get === Nil // int3 match all


    inputMolecule(List(Set(int1, int2))).get === List(
      // (1, Set(int1, int2, int3)),  // int1 AND int2 match
      (2, Set(int2, int3, int4)),
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int1, int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))))))


    inputMolecule(List(Set(int1, int3))).get === List(
      // (1, Set(int1, int2, int3)),  // int1 AND int3 match
      (2, Set(int2, int3, int4)),
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int1, int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding))))))


    inputMolecule(List(Set(int2, int3))).get === List(
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int2, int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding))))))


    inputMolecule(List(Set(int1), Set(int1))).get === List(
      // (1, Set(int1, int2, int3)),  // int1 match
      (2, Set(int2, int3, int4)),
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int1), Set(int1)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding))))))


    inputMolecule(List(Set(int1), Set(int2))).get === List(
      // (1, Set(int1, int2, int3)),  // int1 match, int2 match
      // (2, Set(int2, int3, int4)),  // int2 match
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int1), Set(int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(2), Empty, NoBinding))))))


    inputMolecule(List(Set(int1), Set(int3))).get === Nil
    inputMolecule(List(Set(int1), Set(int4))).get === Nil
    inputMolecule(List(Set(int2), Set(int3))).get === Nil

    inputMolecule(List(Set(int1, int2), Set(int3))).get === Nil
    inputMolecule(List(Set(int1, int2), Set(int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(2), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(3), Empty, NoBinding))))))


    inputMolecule(List(Set(int1, int2), Set(int2, int3))).get === List(
      (3, Set(int3, int4, int5))
    )
    inputMolecule(List(Set(int1, int2), Set(int2, int3)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int1), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int2), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Val(int3), Empty, NoBinding))))))
  }


  ">" in new ManySetup {

    val inputMolecule = m(Ns.int.ints.>(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("c1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(List(Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    // (int3, int4), (int4, int5), (int4, int5, int6)
    inputMolecule(List(Set(int2))).get === List((int2, Set(int3)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(List(Set(int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {

    val inputMolecule = m(Ns.int.ints.>=(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("c1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(List(Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    // (int2, int4), (int3, int4), (int4, int5), (int4, int5, int6)
    inputMolecule(List(Set(int2))).get === List((int1, Set(int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(List(Set(int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {

    val inputMolecule = m(Ns.int.ints.<(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("c1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(List(Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set(int2))).get === List((int1, Set(int1)))
    inputMolecule(List(Set(int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {

    val inputMolecule = m(Ns.int.ints.<=(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "ints"), Var("c1"), None)),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
    inputMolecule(List(Set[Int]()))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int2)))
    inputMolecule(List(Set(int2)))._query === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c")))),
      In(
        List(
          InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ints"), Var("c"), Empty, NoBinding),
        Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
        Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


    (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."


    (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}