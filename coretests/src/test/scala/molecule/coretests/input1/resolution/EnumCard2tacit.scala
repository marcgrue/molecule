package molecule.coretests.input1.resolution

//import molecule.api._
//import molecule.coretests.util.dsl.coreTest._
import molecule.api._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.input.exception.InputMoleculeException


class EnumCard2tacit extends CoreSpec {

  class ManySetup extends CoreSetup {
    Ns.enum.enums$ insert List(
      (enum1, Some(Set(enum1, enum2))),
      (enum2, Some(Set(enum2, enum3))),
      (enum3, Some(Set(enum3, enum4))),
      (enum4, Some(Set(enum4, enum5))),
      (enum5, Some(Set(enum4, enum5, enum6))),
      (enum6, None),
    )
  }

  "Eq" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b2"))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("c2"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))))))


    Ns.enum.enums$(None).get === List(("enum6", None))

    // Note semantic differences:

    // Can return other mandatory attribute values having missing tacit attribute value
    Ns.enum.enums_().get === List(enum6)
    Ns.enum.enums_(Nil).get === List(enum6)
    Ns.enum.enums$(None).get === List((enum6, None))

    // Can't return mandatory attribute value that is missing
    Ns.enum.enums().get === Nil
    // Ns.enum.enums(Nil).get === Nil // not allowed to compile (mandatory/Nil is contradictive)
    // same as
    inputMolecule(Nil).get === List(enum6)
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct("missing?", Seq(DS(""), Var("a"), KW("ns", "enums", "")), NoBinding))))

    inputMolecule(List(Set[String]())).get === List(enum6)


    // Values of 1 Set match values of 1 card-many attribute at a time

    inputMolecule(List(Set(enum1))).get === List(enum1)
    inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2)
    inputMolecule(List(Set(enum3))).get.sorted === List(enum2, enum3)

    inputMolecule(List(Set(enum1, enum1))).get === List(enum1)
    inputMolecule(List(Set(enum1, enum2))).get === List(enum1)
    inputMolecule(List(Set(enum1, enum3))).get === Nil
    inputMolecule(List(Set(enum2, enum3))).get === List(enum2)
    inputMolecule(List(Set(enum4, enum5))).get.sorted === List(enum4, enum5)


    // Values of each Set matches values of 1 card-many attributes respectively

    inputMolecule(List(Set(enum1, enum2), Set[String]())).get === List(enum1)
    inputMolecule(List(Set(enum1), Set(enum1))).get === List(enum1)
    inputMolecule(List(Set(enum1), Set(enum2))).get.sorted === List(enum1, enum2)
    inputMolecule(List(Set(enum1), Set(enum3))).get.sorted === List(enum1, enum2, enum3)

    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get.sorted === List(enum1, enum2, enum3)
    inputMolecule(List(Set(enum1), Set(enum2, enum3))).get.sorted === List(enum1, enum2)
    inputMolecule(List(Set(enum1), Set(enum2), Set(enum3))).get.sorted === List(enum1, enum2, enum3)

    inputMolecule(List(Set(enum1, enum2), Set(enum3, enum4))).get.sorted === List(enum1, enum3)
  }


  "!=" in new CoreSetup {

    val all = List(
      (enum1, Set(enum1, enum2, enum3)),
      (enum2, Set(enum2, enum3, enum4)),
      (enum3, Set(enum3, enum4, enum5))
    )
    Ns.enum.enums insert all

    val inputMolecule = m(Ns.enum.enums_.not(?)) // or m(Ns.enum.enums_.!=(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b2"))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("c3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
        Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3)
    inputMolecule(List(Set[String]()))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set(enum1))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding))))))

    inputMolecule(List(Set(enum2))).get === List(enum3)
    inputMolecule(List(Set(enum3))).get === Nil


    inputMolecule(List(Set(enum1, enum2))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1, enum2)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))))))


    // nothing omitted
    inputMolecule(List(Set(enum1, enum3))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1, enum3)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum2, enum3))).get === List(enum3)
    inputMolecule(List(Set(enum2, enum3)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum1))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1), Set(enum1)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum2))).get === List(enum3)
    inputMolecule(List(Set(enum1), Set(enum2)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1), Set(enum4))).get === Nil

    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1, enum2), Set(enum3)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3))).get === List(enum3)
    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum4, enum5))).get === List(enum2)
    inputMolecule(List(Set(enum1, enum2), Set(enum4, enum5)))._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum4"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Val(":ns.enums/enum5"), Empty, NoBinding))))))
  }


  ">" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.>(?))
    inputMolecule._query === Query(
      Find(List(
        Var("b2"))),
      In(
        List(
          Placeholder(Var("a"), KW("ns", "enums", ""), Var("c3"), Some(":ns.enums/"))),
        List(),
        List(DS)),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
        Funct(">", Seq(Var("c2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(Nil)._query === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
        Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("ns", "enums", ""), Var("c"), Empty, NoBinding))))

    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    // (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
    inputMolecule(List(Set(enum2))).get.sorted === List(enum2, enum3, enum4, enum5)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.>=(?))

    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    // (enum2, enum4), (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
    inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.<(?))

    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    inputMolecule(List(Set(enum2))).get === List(enum1)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.<=(?))

    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
      .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}
