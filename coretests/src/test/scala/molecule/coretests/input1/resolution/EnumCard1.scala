package molecule.coretests.input1.resolution

import molecule.api.in1_out2._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.input.exception.InputMoleculeException


class EnumCard1 extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.str.enum$ insert List(
      (str1, Some(enum1)),
      (str2, Some(enum2)),
      (str3, Some(enum3)),
      (str4, None)
    )
  }

  "Mandatory" >> {

    "Eq" in new OneSetup {
      val inputMolecule = m(Ns.enum(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "enum", ""), Var("b2"), Some(":ns.enum/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


      // Note semantic differences:

      // Can return other mandatory attribute values having missing tacit attribute value
      Ns.str.enum_().get === List(str4)
      Ns.str.enum_(Nil).get === List(str4)
      Ns.str.enum$(None).get === List((str4, None))

      // Can't return mandatory attribute value that is missing
      Ns.str.enum().get === Nil
      // Ns.str.enum(Nil).get === Nil // not allowed to compile (mandatory/Nil is contradictive)
      // same as
      inputMolecule(Nil).get === Nil
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            InVar(CollectionBinding(Var("b2")), Seq(Seq()))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))

      inputMolecule(List(enum1)).get === List(enum1)
      inputMolecule(List(enum1))._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            InVar(ScalarBinding(Var("b2")), Seq(Seq("enum1")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))

      inputMolecule(List(enum1, enum1)).get === List(enum1)

      inputMolecule(List(enum1, enum2)).get === List(enum2, enum1)
      inputMolecule(List(enum1, enum2))._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            InVar(CollectionBinding(Var("b2")), Seq(Seq("enum1", "enum2")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))

      // Varargs
      inputMolecule(enum1).get === List(enum1)
      inputMolecule(enum1, enum2).get === List(enum2, enum1)

      // `or`
      inputMolecule(enum1 or enum2).get === List(enum2, enum1)
    }


    "!=" in new OneSetup {
      val inputMolecule = m(Ns.enum.not(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "enum", ""), Var("b3"), Some(":ns.enum/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
          Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
          Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding))))

      inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b2"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


      inputMolecule(List(enum1)).get.sorted === List(enum2, enum3)
      inputMolecule(List(enum1))._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            InVar(ScalarBinding(Var("b3")), Seq(Seq("enum1")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
          Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
          Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding))))

      inputMolecule(List(enum1, enum1)).get.sorted === List(enum2, enum3)


      inputMolecule(List(enum1, enum2)).get.sorted === List(enum3)
      inputMolecule(List(enum1, enum2))._query === Query(
        Find(List(
          Var("b2"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
          Funct(".compareTo ^String", Seq(Var("b2"), Val("enum1")), ScalarBinding(Var("b2_1"))),
          Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding),
          Funct(".compareTo ^String", Seq(Var("b2"), Val("enum2")), ScalarBinding(Var("b2_2"))),
          Funct("!=", Seq(Var("b2_2"), Val(0)), NoBinding))))
    }


    ">" in new OneSetup {
      val inputMolecule = m(Ns.enum.>(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "enum", ""), Var("b3"), Some(":ns.enum/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
          Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
          Funct(">", Seq(Var("b2_1"), Val(0)), NoBinding))))


      inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b2"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))))))


      inputMolecule(List(enum2)).get.sorted === List(enum3)
      inputMolecule(List(enum2))._query === Query(
        Find(List(
          Var("b2"))),
        In(
          List(
            InVar(ScalarBinding(Var("b3")), Seq(Seq("enum2")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("b"), KW("db", "ident", ""), Var("b1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("b1")), ScalarBinding(Var("b2"))),
          Funct(".compareTo ^String", Seq(Var("b2"), Var("b3")), ScalarBinding(Var("b2_1"))),
          Funct(">", Seq(Var("b2_1"), Val(0)), NoBinding))))

      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    ">=" in new OneSetup {
      val inputMolecule = m(Ns.enum.>=(?))
      inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
      inputMolecule(List(enum2)).get.sorted === List(enum2, enum3)
      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<" in new OneSetup {
      val inputMolecule = m(Ns.enum.<(?))
      inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
      inputMolecule(List(enum2)).get === List(enum1)
      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<=" in new OneSetup {
      val inputMolecule = m(Ns.enum.<=(?))
      inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
      inputMolecule(List(enum2)).get.sorted === List(enum1, enum2)
      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }
  }


  "Tacit" >> {

    "Eq" in new OneSetup {
      val inputMolecule = m(Ns.str.enum_(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "enum", ""), Var("c2"), Some(":ns.enum/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))))))


      // Note semantic differences:

      // Can't return mandatory attribute value that is missing
      Ns.str.enum().get === Nil
      // Ns.str.enum(Nil).get === Nil // not allowed to compile (mandatory/Nil is contradictive)

      // Can return other mandatory attribute values having missing tacit attribute value
      Ns.str.enum_().get === List(str4)
      Ns.str.enum_(Nil).get === List(str4)
      Ns.str.enum$(None).get === List((str4, None))
      // same as
      inputMolecule(Nil).get === List(str4)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          Funct("missing?", Seq(DS(""), Var("a"), KW("ns", "enum", "")), NoBinding))))


      inputMolecule(List(enum1)).get === List(str1)
      inputMolecule(List(enum1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq(":ns.enum/enum1")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding))))

      inputMolecule(List(enum1, enum1)).get === List(str1)


      inputMolecule(List(enum1, enum2)).get === List(str1, str2)
      inputMolecule(List(enum1, enum2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("c")), Seq(Seq(":ns.enum/enum1", ":ns.enum/enum2")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding))))
    }


    "!=" in new OneSetup {
      val inputMolecule = m(Ns.str.enum_.not(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "enum", ""), Var("c3"), Some(":ns.enum/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
          Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding))))


      inputMolecule(Nil).get.sorted === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(enum1)).get.sorted === List(str2, str3)
      inputMolecule(List(enum1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c3")), Seq(Seq("enum1")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
          Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding))))

      inputMolecule(List(enum1, enum1)).get.sorted === List(str2, str3)


      inputMolecule(List(enum1, enum2)).get === List(str3)
      inputMolecule(List(enum1, enum2))._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          Funct(".compareTo ^String", Seq(Var("c2"), Val("enum1")), ScalarBinding(Var("c2_1"))),
          Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding),
          Funct(".compareTo ^String", Seq(Var("c2"), Val("enum2")), ScalarBinding(Var("c2_2"))),
          Funct("!=", Seq(Var("c2_2"), Val(0)), NoBinding))))
    }


    ">" in new OneSetup {
      val inputMolecule = m(Ns.str.enum_.>(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "enum", ""), Var("c3"), Some(":ns.enum/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
          Funct(">", Seq(Var("c2_1"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(enum2)).get === List(str3)
      inputMolecule(List(enum2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c3")), Seq(Seq("enum2")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "enum", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          Funct(".compareTo ^String", Seq(Var("c2"), Var("c3")), ScalarBinding(Var("c2_1"))),
          Funct(">", Seq(Var("c2_1"), Val(0)), NoBinding))))

      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    ">=" in new OneSetup {
      val inputMolecule = m(Ns.str.enum_.>=(?))

      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(List(enum2)).get === List(str2, str3)
      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<" in new OneSetup {
      val inputMolecule = m(Ns.str.enum_.<(?))

      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(List(enum2)).get === List(str1)
      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<=" in new OneSetup {
      val inputMolecule = m(Ns.str.enum_.<=(?))

      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(List(enum2)).get === List(str1, str2)
      (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }
  }
}