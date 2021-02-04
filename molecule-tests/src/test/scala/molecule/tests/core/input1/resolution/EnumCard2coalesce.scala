package molecule.tests.core.input1.resolution

import datomic.Util
import molecule.datomic.ast.query._
import molecule.core.input.exception.MoleculeException
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.in1_out2._
import molecule.TestSpec


class EnumCard2coalesce extends TestSpec {

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
    val inputMolecule = m(Ns.enums(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b2"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(Nil).get === Nil
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          InVar(CollectionBinding(Var("b2")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))))))

    inputMolecule(List(Set[String]())).get === Nil

    // Values of 1 Set match values of 1 card-many attribute at a time

    inputMolecule(List(Set(enum1))).get === List(Set(enum1, enum2))
    inputMolecule(List(Set(enum1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        RuleInvocation("rule1", Seq(Var("a"))))))

    inputMolecule(List(Set(enum2))).get === List(Set(enum1, enum2, enum3)) // (enum1, enum2) + (enum2, enum3)
    inputMolecule(List(Set(enum3))).get === List(Set(enum2, enum3, enum4)) // (enum2, enum3) + (enum3, enum4)

    inputMolecule(List(Set(enum1, enum2))).get === List(Set(enum1, enum2))
    inputMolecule(List(Set(enum1, enum3))).get === Nil
    inputMolecule(List(Set(enum2, enum3))).get === List(Set(enum2, enum3))
    inputMolecule(List(Set(enum4, enum5))).get === List(Set(enum4, enum5, enum6)) // (enum4, enum5) + (enum4, enum5, enum6)


    // Values of each Set matches values of 1 card-many attributes respectively

    inputMolecule(List(Set(enum1), Set(enum1))).get === List(Set(enum1, enum2))
    inputMolecule(List(Set(enum1), Set(enum2))).get === List(Set(enum1, enum2, enum3)) // (enum1, enum2) + (enum2, enum3)
    inputMolecule(List(Set(enum1), Set(enum3))).get === List(Set(enum1, enum4, enum3, enum2)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)
    inputMolecule(List(Set(enum2), Set(enum3))).get === List(Set(enum1, enum2, enum3, enum4)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)

    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === List(Set(enum1, enum2, enum3, enum4)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)
    inputMolecule(List(Set(enum1), Set(enum2, enum3))).get === List(Set(enum1, enum3, enum2)) // (enum1, enum2) + (enum2, enum3)
    inputMolecule(List(Set(enum1), Set(enum2), Set(enum3))).get === List(Set(enum1, enum2, enum3, enum4)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)
  }


  "!=" in new CoreSetup {

    val all = List(
      (enum1, Set(enum1, enum2, enum3)),
      (enum2, Set(enum2, enum3, enum4)),
      (enum3, Set(enum3, enum4, enum5))
    )
    Ns.enum.enums insert all

    val inputMolecule = m(Ns.enums.not(?)) // or m(Ns.enums.!=(?))

    // Un-optimized query following same structure as model
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding))))

    // Optimized query where most specific Where clauses come first
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
      )))


    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5))
    inputMolecule(List(Set[String]()))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(List(Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))))))

    inputMolecule(List(Set(enum2))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum3))).get === Nil


    inputMolecule(List(Set(enum1, enum2))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))))))


    // nothing omitted
    inputMolecule(List(Set(enum1, enum3))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1), Set(enum1)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum2))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum1), Set(enum2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1), Set(enum4))).get === Nil

    inputMolecule(List(Set(enum1, enum2), Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2), Set(enum2))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1, enum2), Set(enum3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))
  }


  ">" in new ManySetup {
    val inputMolecule = m(Ns.enums.>(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct(">", Seq(Var("b2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum3, enum4, enum5, enum6))
    inputMolecule(List(Set(enum2)))._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          InVar(ScalarBinding(Var("b3")), Seq(Seq("enum2")))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct(">", Seq(Var("b2_1"), Val(0)), NoBinding))))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {
    val inputMolecule = m(Ns.enums.>=(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct(">=", Seq(Var("b2_1"), Val(0)), NoBinding))))

    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum2, enum3, enum4, enum5, enum6))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {
    val inputMolecule = m(Ns.enums.<(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("<", Seq(Var("b2_1"), Val(0)), NoBinding))))

    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum1))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {
    val inputMolecule = m(Ns.enums.<=(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("Ns", "enums"), Var("b3"), Some(":Ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("<=", Seq(Var("b2_1"), Val(0)), NoBinding))))

    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum1, enum2))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}
