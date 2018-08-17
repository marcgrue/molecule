package molecule.coretests

//import molecule.api._
//import molecule.coretests.util.dsl.coreTest._
import molecule.api._
import molecule.ast.query._
import molecule.coretests.util.CoreSetup
import molecule.coretests.util.dsl.coreTest._
import molecule.input.exception.InputMolecule_1_Exception
import molecule.transform.exception.Model2QueryException


//class AdHocTest extends CoreSpec {
class Input1IntQuery extends ApplyBase {

  sequential


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.int$ insert List(
        ("a", Some(int1)),
        ("b", Some(int2)),
        ("c", Some(int3)),
        ("d", Some(int4)),
        ("e", None)
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


        inputMolecule(Nil).get === List(int1, int2, int3, int4)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(int1)).get === List(int2, int3, int4)
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


        inputMolecule(List(int1, int2)).get === List(int3, int4)
        inputMolecule(List(int1, int2))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Val(int1)), ScalarBinding(Var("b2"))),
            Funct(".compareTo ^Long", Seq(Var("b"), Val(int2)), ScalarBinding(Var("b2"))),
            Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))

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


        inputMolecule(Nil).get === List(int1, int2, int3, int4)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(int2)).get === List(int3, int4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === List(int1, int2, int3, int4)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(int2)).get === List(int2, int3, int4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === List(int1, int2, int3, int4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === List(int1, int2, int3, int4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === Nil
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(CollectionBinding(Var("c")), Seq(Seq()))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


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


        inputMolecule(Nil).get === List(str1, str2, str3, str4)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(int1)).get === List(str2, str3, str4)
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


        inputMolecule(List(int1, int2)).get === List(str3, str4)
        inputMolecule(List(int1, int2))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Val(int1)), ScalarBinding(Var("c2"))),
            Funct(".compareTo ^Long", Seq(Var("c"), Val(int2)), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))
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


        inputMolecule(Nil).get === List(str1, str2, str3, str4)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(int2)).get === List(str3, str4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === List(str1, str2, str3, str4)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "str", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(int2)).get === List(str2, str3, str4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === List(str1, str2, str3, str4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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


        inputMolecule(Nil).get === List(str1, str2, str3, str4)
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


        (inputMolecule(List(int2, int3)).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

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

    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {

        val inputMolecule = m(Ns.ints(?))
        inputMolecule._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("b"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(Nil).get === Nil
        inputMolecule(Nil)._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(CollectionBinding(Var("b")), Seq(Seq()))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        // AND semantics when applying int1 Set of values matching input attribute values of int1 entity

        inputMolecule(List(Set[Int]())).get === Nil
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(CollectionBinding(Var("b")), Seq(Seq()))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set(int1))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))

        inputMolecule(List(Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
        inputMolecule(List(Set(int3))).get === List(Set(int2, int3, int4)) // (int2, int3) + (int3, int4)


        inputMolecule(List(Set(int1, int1))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1, int1)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set(int1, int2))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1, int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))

        inputMolecule(List(Set(int1, int3))).get === Nil
        inputMolecule(List(Set(int2, int3))).get === List(Set(int2, int3))
        inputMolecule(List(Set(int4, int5))).get === List(Set(int4, int5, int6)) // (int4, int5) + (int4, int5, int6)


        inputMolecule(List(Set(int1, int2), Set[Int]())).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1, int2), Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1), Set(int1)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
        inputMolecule(List(Set(int1), Set(int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))

        inputMolecule(List(Set(int1), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)


        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
        inputMolecule(List(Set(int1, int2), Set(int3)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int2, int3))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
        inputMolecule(List(Set(int1), Set(int2, int3)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
        inputMolecule(List(Set(int1), Set(int2), Set(int3)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1, int2), Set(int3, int4))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int3, int4)
        inputMolecule(List(Set(int1, int2), Set(int3, int4)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int4), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))
      }


      "!=" in new ManySetup {

        val inputMolecule = m(Ns.ints.not(?)) // or m(Ns.ints.!=(?))
        inputMolecule._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("b1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(Nil)._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set(int1))).get === List(Set(int2, int3, int4, int5, int6)) // (int1, int2) omitted
        inputMolecule(List(Set(int1)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(ScalarBinding(Var("b1")), Seq(Seq(int1)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))

        inputMolecule(List(Set(int2))).get === List(Set(int5, int1, int6, int3, int4)) // (int1, int2), (int2, int3) omitted
        inputMolecule(List(Set(int3))).get === List(Set(int1, int2, int4, int5, int6)) // (int2, int3), (int3, int4) omitted


        // (int1, int2) omitted, (int2, int3) incl
        inputMolecule(List(Set(int1, int2))).get === List(Set(int5, int6, int2, int3, int4))
        inputMolecule(List(Set(int1, int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))))))


        // nothing omitted
        inputMolecule(List(Set(int1, int3))).get === List(Set(int1, int2, int3, int4, int5, int6))
        inputMolecule(List(Set(int1, int3)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        // (int2, int3) omitted, (int1, int2)+(int3, int4) incl - same as nothing omitted
        inputMolecule(List(Set(int2, int3))).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(List(Set(int2, int3)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        // (int1, int2), (int2, int3) omitted
        inputMolecule(List(Set(int1, int2), Set(int2, int3))).get === List(Set(int5, int6, int3, int4))
        inputMolecule(List(Set(int1, int2), Set(int2, int3)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int2, int3, int4, int5, int6)) // (int1, int2) omitted
        inputMolecule(List(Set(int1), Set(int1)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(ScalarBinding(Var("b1")), Seq(Seq(int1)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("!=", Seq(Var("b2"), Val(0)), NoBinding))))


        inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int3, int4, int5, int6)) // (int1, int2), (int2, int3) omitted
        inputMolecule(List(Set(int1), Set(int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))))))


        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int1), Set(int3))).get === List(Set(int4, int5, int6))

        // (int1, int2), (int3, int4), (int4, int5), (int4, int5, int6) omitted
        inputMolecule(List(Set(int1), Set(int4))).get === List(Set(int3, int2))

        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int2), Set(int3))).get === List(Set(int4, int6, int5))

        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(Set(int4, int5, int6))
      }


      ">" in new ManySetup {

        val inputMolecule = m(Ns.ints.>(?))
        inputMolecule._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("b1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct(">", Seq(Var("b2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(Nil)._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        // (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List(Set(int4, int6, int3, int5))
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct(">", Seq(Var("b2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {

        val inputMolecule = m(Ns.ints.>=(?))
        inputMolecule._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("b1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct(">=", Seq(Var("b2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(Nil)._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        // (int2, int4), (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List(Set(int5, int6, int2, int3, int4))
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct(">=", Seq(Var("b2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {

        val inputMolecule = m(Ns.ints.<(?))
        inputMolecule._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("b1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("<", Seq(Var("b2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(Nil)._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set(int2))).get === List(Set(int1))
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("<", Seq(Var("b2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {

        val inputMolecule = m(Ns.ints.<=(?))
        inputMolecule._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("b1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("<=", Seq(Var("b2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(Nil)._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(Set(int5, int1, int6, int2, int3, int4))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding))))


        inputMolecule(List(Set(int2))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            AggrExpr("distinct", Seq(), Var("b")))),
          In(
            List(
              InVar(ScalarBinding(Var("b1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
            Funct("<=", Seq(Var("b2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, multiple attrs unify" >> {

      "Eq" in new ManySetup {

        val inputMolecule = m(Ns.int.ints(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))

        inputMolecule(List(Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(List(Set(int3))).get === List((int2, Set(int3, int2)), (int3, Set(int4, int3)))


        inputMolecule(List(Set(int1, int1))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1, int1)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1, int2))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1, int2)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
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
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int1))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1), Set(int1)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1), Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(List(Set(int1), Set(int2)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
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
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
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
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
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
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
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
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int4), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))
      }


      "!=" in new ManySetup {

        val inputMolecule = m(Ns.int.ints.not(?)) // or m(Ns.int.ints.!=(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1))).get === List((int1, Set(int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int1)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int1)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))

        inputMolecule(List(Set(int2))).get === List((int1, Set(int1)), (int2, Set(int3)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int2)), (int3, Set(int4)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))


        // (int1, int2) omitted, (int2, int3) incl
        inputMolecule(List(Set(int1, int2))).get === List((int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int1, int2)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))))))


        // nothing omitted
        inputMolecule(List(Set(int1, int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int1, int3)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        // (int2, int3) omitted, (int1, int2)+(int3, int4) incl - same as nothing omitted
        inputMolecule(List(Set(int2, int3))).get === List((int1, Set(int1, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int2, int3)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        // (int1, int2), (int2, int3) omitted
        inputMolecule(List(Set(int1, int2), Set(int2, int3))).get === List((int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int1, int2), Set(int2, int3)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        inputMolecule(List(Set(int1), Set(int1))).get === List((int1, Set(int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int1), Set(int1)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int1)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(List(Set(int1), Set(int2))).get === List((int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set(int1), Set(int2)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))))))


        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int1), Set(int3))).get === List((int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        // (int1, int2), (int3, int4), (int4, int5), (int4, int5, int6) omitted
        inputMolecule(List(Set(int1), Set(int4))).get === List((int2, Set(int3, int2)))

        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int2), Set(int3))).get === List((int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int1, int2), Set(int3))).get === List((int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
      }


      ">" in new ManySetup {

        val inputMolecule = m(Ns.int.ints.>(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
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
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {

        val inputMolecule = m(Ns.int.ints_(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(Nil).get === Nil
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(CollectionBinding(Var("c")), Seq(Seq()))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        // AND semantics when applying int1 Set of values matching input attribute values of int1 entity

        inputMolecule(List(Set[Int]())).get === Nil
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(CollectionBinding(Var("c")), Seq(Seq()))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1))).get === List(int1)
        inputMolecule(List(Set(int1)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))

        inputMolecule(List(Set(int2))).get === List(int1, int2)
        inputMolecule(List(Set(int3))).get === List(int2, int3)


        inputMolecule(List(Set(int1, int1))).get === List(int1)
        inputMolecule(List(Set(int1, int1)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1, int2))).get === List(int1)
        inputMolecule(List(Set(int1, int2)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))

        inputMolecule(List(Set(int1, int3))).get === Nil
        inputMolecule(List(Set(int2, int3))).get === List(int2)
        inputMolecule(List(Set(int4, int5))).get === List(int4, int5)


        inputMolecule(List(Set(int1, int2), Set[Int]())).get === List(int1)
        inputMolecule(List(Set(int1, int2), Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int1))).get === List(int1)
        inputMolecule(List(Set(int1), Set(int1)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1), Set(int2))).get === List(int1, int2)
        inputMolecule(List(Set(int1), Set(int2)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))

        inputMolecule(List(Set(int1), Set(int3))).get === List(int1, int2, int3)


        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(int1, int2, int3)
        inputMolecule(List(Set(int1, int2), Set(int3)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int2, int3))).get === List(int1, int2)
        inputMolecule(List(Set(int1), Set(int2, int3)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(int1, int2, int3)
        inputMolecule(List(Set(int1), Set(int2), Set(int3)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(int1, int2), Set(int3, int4))).get === List(int1, int3)
        inputMolecule(List(Set(int1, int2), Set(int3, int4)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
              Rule("rule1", Seq(Var("a")), Seq(
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding),
                DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int4), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))
      }


      "!=" in new ManySetup {

        val inputMolecule = m(Ns.int.ints_.not(?)) // or m(Ns.int.ints_.!=(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int1))).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set(int1)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int1)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))

        inputMolecule(List(Set(int2))).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set(int3))).get === List(int1, int2, int3, int4, int5)


        // (int1, int2) omitted, (int2, int3) incl
        inputMolecule(List(Set(int1, int2))).get === List(int2, int3, int4, int5)
        inputMolecule(List(Set(int1, int2)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))))))


        // nothing omitted
        inputMolecule(List(Set(int1, int3))).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set(int1, int3)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        // (int2, int3) omitted, (int1, int2)+(int3, int4) incl - same as nothing omitted
        inputMolecule(List(Set(int2, int3))).get === List(int1, int3, int4, int5)
        inputMolecule(List(Set(int2, int3)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        // (int1, int2), (int2, int3) omitted
        inputMolecule(List(Set(int1, int2), Set(int2, int3))).get === List(int3, int4, int5)
        inputMolecule(List(Set(int1, int2), Set(int2, int3)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int3), Empty, NoBinding))))))


        inputMolecule(List(Set(int1), Set(int1))).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set(int1), Set(int1)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int1)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("!=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(List(Set(int1), Set(int2))).get === List(int3, int4, int5)
        inputMolecule(List(Set(int1), Set(int2)))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int1), Empty, NoBinding))),
            NotClauses(Seq(
              DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Val(int2), Empty, NoBinding))))))


        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int1), Set(int3))).get === List(int4, int5)

        // (int1, int2), (int3, int4), (int4, int5), (int4, int5, int6) omitted
        inputMolecule(List(Set(int1), Set(int4))).get === List(int2)

        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int2), Set(int3))).get === List(int4, int5)

        // (int1, int2), (int2, int3), (int3, int4) omitted
        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(int4, int5)
      }


      ">" in new ManySetup {

        val inputMolecule = m(Ns.int.ints_.>(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        // (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List(int2, int3, int4, int5)
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {

        val inputMolecule = m(Ns.int.ints_.>=(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        // (int2, int4), (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct(">=", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {

        val inputMolecule = m(Ns.int.ints_.<(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int2))).get === List(int1)
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {

        val inputMolecule = m(Ns.int.ints_.<=(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              Placeholder(Var("a"), KW("ns", "ints", ""), Var("c1"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(Nil)._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]()))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding))))


        inputMolecule(List(Set(int2))).get === List(int1, int2)
        inputMolecule(List(Set(int2)))._query === Query(
          Find(List(
            Var("b"))),
          In(
            List(
              InVar(ScalarBinding(Var("c1")), Seq(Seq(int2)))),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("ns", "ints", ""), Var("c"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("c"), Var("c1")), ScalarBinding(Var("c2"))),
            Funct("<=", Seq(Var("c2"), Val(0)), NoBinding))))


        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."


        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMolecule_1_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_1_Exception: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  //  "Map" >> {
  //
  //  }
  //  "Keyed map" >> {
  //
  //  }

}