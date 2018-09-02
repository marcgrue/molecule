package molecule.coretests.input1.resolution

//import molecule.api._
//import molecule.coretests.util.dsl.coreTest._
import molecule.api._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.input.exception.InputMoleculeException


class EnumCard2coalesce extends CoreSpec {

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
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("b2"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(Nil).get === Nil
    inputMolecule(Nil)._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          InVar(CollectionBinding(Var("b2")), Seq(Seq()))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))

    inputMolecule(List(Set[String]())).get === Nil

    // Values of 1 Set match values of 1 card-many attribute at a time

    inputMolecule(List(Set(enum1))).get === List(Set(enum1, enum2))
    inputMolecule(List(Set(enum1)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(),
        List(
          Rule("rule1", Seq(Var("a")), Seq(
            DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding)))),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
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
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("b3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5))
    inputMolecule(Nil)._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5))
    inputMolecule(List(Set[String]()))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(List(Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding))))))

    inputMolecule(List(Set(enum2))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum3))).get === Nil


    inputMolecule(List(Set(enum1, enum2))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))))))


    // nothing omitted
    inputMolecule(List(Set(enum1, enum3))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum3)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1), Set(enum1)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum2))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum1), Set(enum2)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1), Set(enum4))).get === Nil

    inputMolecule(List(Set(enum1, enum2), Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2), Set(enum2))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1, enum2), Set(enum3)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3))).get === List(Set(enum3, enum4, enum5))
    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))
  }


  ">" in new ManySetup {
    val inputMolecule = m(Ns.enums.>(?))
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("b3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct(">", Seq(Var("b2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(Nil)._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum3, enum4, enum5, enum6))
    inputMolecule(List(Set(enum2)))._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          InVar(ScalarBinding(Var("b3")), Seq(Seq("enum2")))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct(">", Seq(Var("b2_1"), Val(0)), NoBinding))))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {
    val inputMolecule = m(Ns.enums.>=(?))
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("b3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct(">=", Seq(Var("b2_1"), Val(0)), NoBinding))))

    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum2, enum3, enum4, enum5, enum6))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {
    val inputMolecule = m(Ns.enums.<(?))
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("b3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("<", Seq(Var("b2_1"), Val(0)), NoBinding))))

    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum1))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {
    val inputMolecule = m(Ns.enums.<=(?))
    inputMolecule._query === Query(
      Find(List(
        AggrExpr("distinct", Seq(), Var("b2")))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("b3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
        Funct("<=", Seq(Var("b2_1"), Val(0)), NoBinding))))

    inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
    inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

    inputMolecule(List(Set(enum2))).get === List(Set(enum1, enum2))

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}
