package molecule.coretests.input1.resolution

//import molecule.api._
//import molecule.coretests.util.dsl.coreTest._
import molecule.api._
import molecule.ast.query._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._
import molecule.input.exception.InputMoleculeException


class IntCard1 extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.str.int$ insert List(
      (str1, Some(int1)),
      (str2, Some(int2)),
      (str3, Some(int3)),
      (str4, None)
    )
  }

  "Mandatory" >> {

    "Eq" in new OneSetup {

      val inputMolecule = m(Ns.int(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("b"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      // Note semantic differences:

      // Can return other mandatory attribute values having missing tacit attribute value
      Ns.str.int_().get === List(str4)
      Ns.str.int_(Nil).get === List(str4)
      Ns.str.int$(None).get === List((str4, None))

      // Can't return mandatory attribute value that is missing
      Ns.str.int().get === Nil
      // Ns.str.int(Nil).get === Nil // not allowed to compile (mandatory/Nil is contradictive)
      // same as
      inputMolecule(Nil).get === Nil
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("b")), Seq(Seq()))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int1)).get === List(int1)
      inputMolecule(List(int1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b")), Seq(Seq(int1)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int1, int1)).get === List(int1)
      inputMolecule(List(int1, int1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b")), Seq(Seq(int1)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int1, int2)).get === List(int1, int2)
      inputMolecule(List(int1, int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("b")), Seq(Seq(int1, int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))
    }


    "!=" in new OneSetup {

      val inputMolecule = m(Ns.int.not(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(int1, int2, int3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int1)).get === List(int2, int3)
      inputMolecule(List(int1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq(int1)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))


      inputMolecule(List(int1, int2)).get === List(int3)
      inputMolecule(List(int1, int2))._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Val(1)), ScalarBinding(Var("b2_1"))),
          Funct("!=", Seq(Var("b2_1"), Val(0)), NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Val(2)), ScalarBinding(Var("b2_2"))),
          Funct("!=", Seq(Var("b2_2"), Val(0)), NoBinding))))

    }


    ">" in new OneSetup {

      val inputMolecule = m(Ns.int.>(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct(">", Seq(Var("b2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(int1, int2, int3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(int3)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct(">", Seq(Var("b2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    ">=" in new OneSetup {

      val inputMolecule = m(Ns.int.>=(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct(">=", Seq(Var("b2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(int1, int2, int3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(int2, int3)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct(">=", Seq(Var("b2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<" in new OneSetup {

      val inputMolecule = m(Ns.int.<(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<", Seq(Var("b2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(int1, int2, int3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(int1)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<", Seq(Var("b2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<=" in new OneSetup {

      val inputMolecule = m(Ns.int.<=(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<=", Seq(Var("b2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(int1, int2, int3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(int1, int2)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<=", Seq(Var("b2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }
  }


  "Tacit" >> {

    "Eq" in new OneSetup {

      val inputMolecule = m(Ns.str.int_(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("c"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      // Note semantic differences:

      // Can't return mandatory attribute value that is missing
      Ns.str.int().get === Nil
      // Ns.str.int(Nil).get === Nil // not allowed to compile (mandatory/Nil is contradictive)

      // Can return other mandatory attribute values having missing tacit attribute value
      Ns.str.int$(None).get === List((str4, None))
      Ns.str.int_().get === List(str4)
      Ns.str.int_(Nil).get === List(str4)
      // same as
      inputMolecule(Nil).get === List(str4)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          Funct("missing?", Seq(DS(""), Var("a"), KW("ns", "int", "")), NoBinding))))


      inputMolecule(List(int1)).get === List(str1)
      inputMolecule(List(int1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq(int1)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(int1, int2)).get === List(str1, str2)
      inputMolecule(List(int1, int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("c")), Seq(Seq(int1, int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))
    }


    "!=" in new OneSetup {

      val inputMolecule = m(Ns.str.int_.not(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("c1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(int1)).get === List(str2, str3)
      inputMolecule(List(int1))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c1")), Seq(Seq(int1)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


      inputMolecule(List(int1, int2)).get === List(str3)
      inputMolecule(List(int1, int2))._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Val(1)), ScalarBinding(Var("c2_1"))),
          Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Val(2)), ScalarBinding(Var("c2_2"))),
          Funct("!=", Seq(Var("c2_2"), Val(0)), NoBinding))))
    }


    ">" in new OneSetup {

      val inputMolecule = m(Ns.str.int_.>(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("c1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(str3)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    ">=" in new OneSetup {

      val inputMolecule = m(Ns.str.int_.>=(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("c1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(str2, str3)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<" in new OneSetup {

      val inputMolecule = m(Ns.str.int_.<(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("c1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(str1)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }


    "<=" in new OneSetup {

      val inputMolecule = m(Ns.str.int_.<=(?))
      inputMolecule._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("ns", "int", ""), Var("c1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


      inputMolecule(Nil).get === List(str1, str2, str3)
      inputMolecule(Nil)._query === Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


      inputMolecule(List(int2)).get === List(str1, str2)
      inputMolecule(List(int2))._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
          Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
          Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


      (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."
    }
  }
}