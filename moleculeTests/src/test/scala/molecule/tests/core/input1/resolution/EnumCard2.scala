package molecule.tests.core.input1.resolution

import datomic.Util
import molecule.core.ast.query._
import molecule.core.input.exception.InputMoleculeException
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.in1_out2._
import molecule.TestSpec


class EnumCard2 extends TestSpec {

  class ManySetup extends CoreSetup {
    Ns.enum.enums$ insert List(
      (enum1, Some(Set(enum1, enum2))),
      (enum2, Some(Set(enum2, enum3))),
      (enum3, Some(Set(enum3, enum4))),
      (enum4, Some(Set(enum4, enum5))),
      (enum5, Some(Set(enum4, enum5, enum6))),
      (enum6, None)
    )
  }

  "Eq" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("c2"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))))))

    inputMolecule(Nil).get === Nil
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(
          InVar(CollectionBinding(Var("c2")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))))))

    inputMolecule(List(Set[String]())).get === Nil


    // Values of 1 Set match values of 1 card-many attribute at a time

    inputMolecule(List(Set(enum1))).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(List(Set(enum1)))._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(enum1, enum1))).get === List((enum1, Set(enum1, enum2)))

    inputMolecule(List(Set(enum1, enum2))).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(List(Set(enum1, enum2)))._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(enum1, enum3))).get === Nil
    inputMolecule(List(Set(enum2, enum3))).get === List((enum2, Set(enum3, enum2)))
    inputMolecule(List(Set(enum4, enum5))).get === List((enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

    // 1 arg
    inputMolecule(Set(enum1)).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1, enum1)).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1, enum2)).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1, enum3)).get === Nil
    inputMolecule(Set(enum2, enum3)).get === List((enum2, Set(enum3, enum2)))
    inputMolecule(Set(enum4, enum5)).get === List((enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))


    // Values of each Set matches values of 1 card-many attributes respectively

    inputMolecule(List(Set(enum1, enum2), Set[String]())).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(List(Set(enum1), Set(enum1))).get === List((enum1, Set(enum1, enum2)))

    inputMolecule(List(Set(enum1), Set(enum2))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
    inputMolecule(List(Set(enum1), Set(enum2)))._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(enum1), Set(enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(List(Set(enum1), Set(enum2, enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
    inputMolecule(List(Set(enum1), Set(enum2), Set(enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))

    inputMolecule(List(Set(enum1, enum2), Set(enum3, enum4))).get === List((enum1, Set(enum1, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(List(Set(enum1, enum2), Set(enum3, enum4)))._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum4"), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        RuleInvocation("rule1", Seq(Var("a"))))))


    // Multiple varargs
    inputMolecule(Set(enum1, enum2), Set[String]()).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1), Set(enum1)).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1), Set(enum2)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
    inputMolecule(Set(enum1), Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(Set(enum1, enum2), Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(Set(enum1), Set(enum2, enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
    inputMolecule(Set(enum1), Set(enum2), Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(Set(enum1, enum2), Set(enum3, enum4)).get === List((enum1, Set(enum1, enum2)), (enum3, Set(enum4, enum3)))

    // `or`
    inputMolecule(Set(enum1, enum2) or Set[String]()).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1) or Set(enum1)).get === List((enum1, Set(enum1, enum2)))
    inputMolecule(Set(enum1) or Set(enum2)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
    inputMolecule(Set(enum1) or Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(Set(enum1, enum2) or Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(Set(enum1) or Set(enum2, enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
    inputMolecule(Set(enum1) or Set(enum2) or Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
    inputMolecule(Set(enum1, enum2) or Set(enum3, enum4)).get === List((enum1, Set(enum1, enum2)), (enum3, Set(enum4, enum3)))
  }


  "!=" in new CoreSetup {

    val all = List(
      (1, Set(enum1, enum2, enum3)),
      (2, Set(enum2, enum3, enum4)),
      (3, Set(enum3, enum4, enum5))
    )
    Ns.int.enums insert all

    val inputMolecule = m(Ns.int.enums.not(?)) // or m(Ns.int.enums.!=(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("c3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
        Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === all
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))))))


    inputMolecule(List(Set[String]())).get === all
    inputMolecule(List(Set[String]()))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))))))


    inputMolecule(List(Set(enum1))).get === List(
      // (1, Set(enum1, enum2, enum3)),  // enum1 match
      (2, Set(enum2, enum3, enum4)),
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum1)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))))))

    inputMolecule(List(Set(enum2))).get === List(
      // (1, Set(enum1, enum2, enum3)),  // enum2 match
      // (2, Set(enum2, enum3, enum4)),  // enum2 match
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum3))).get === Nil // enum3 match all


    inputMolecule(List(Set(enum1, enum2))).get === List(
      // (1, Set(enum1, enum2, enum3)),  // enum1 AND enum2 match
      (2, Set(enum2, enum3, enum4)),
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum1, enum2)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum3))).get === List(
      // (1, Set(enum1, enum2, enum3)),  // enum1 AND enum3 match
      (2, Set(enum2, enum3, enum4)),
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum1, enum3)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum2, enum3))).get === List(
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum2, enum3)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum1))).get === List(
      // (1, Set(enum1, enum2, enum3)),  // enum1 match
      (2, Set(enum2, enum3, enum4)),
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum1), Set(enum1)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum2))).get === List(
      // (1, Set(enum1, enum2, enum3)),  // enum1 match, enum2 match
      // (2, Set(enum2, enum3, enum4)),  // enum2 match
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum1), Set(enum2)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1), Set(enum4))).get === Nil
    inputMolecule(List(Set(enum2), Set(enum3))).get === Nil

    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1, enum2), Set(enum3)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3))).get === List(
      (3, Set(enum3, enum4, enum5))
    )
    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3)))._rawQuery === Query(
      Find(List(
        Var("b"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "int"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))
  }


  ">" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums.>(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("c3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
        Funct(">", Seq(Var("c2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))))))

    inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

    // (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
    inputMolecule(List(Set(enum2))).get === List((enum2, Set(enum3)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
    inputMolecule(List(Set(enum2)))._rawQuery === Query(
      Find(List(
        Var("b2"),
        AggrExpr("distinct", Seq(), Var("c2")))),
      In(
        List(
          InVar(ScalarBinding(Var("c3")), Seq(Seq("enum2")))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
        Funct(">", Seq(Var("c2_1"), Val(0)), NoBinding))))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums.>=(?))

    inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
    inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

    // (enum2, enum4), (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
    inputMolecule(List(Set(enum2))).get === List((enum1, Set(enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums.<(?))

    inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
    inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

    inputMolecule(List(Set(enum2))).get === List((enum1, Set(enum1)))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums.<=(?))

    inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
    inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

    inputMolecule(List(Set(enum2))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum2)))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}
