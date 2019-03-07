package molecule.coretests.input1

import java.net.URI
import molecule.api.in1_out2._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.input.exception.InputMoleculeException


class Input1URI extends CoreSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.uri$ insert List(
        (str1, Some(uri1)),
        (str2, Some(uri2)),
        (str3, Some(uri3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.uri(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(uri1)).get === List(uri1)
        inputMolecule(List(uri1, uri1)).get === List(uri1)
        inputMolecule(List(uri1, uri2)).get.sorted === List(uri1, uri2)

        // Varargs
        inputMolecule(uri1).get === List(uri1)
        inputMolecule(uri1, uri2).get.sorted === List(uri1, uri2)

        // `or`
        inputMolecule(uri1 or uri2).get.sorted === List(uri1, uri2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.uri.not(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3)

        inputMolecule(List(uri1)).get.sorted === List(uri2, uri3)

        inputMolecule(List(uri1, uri1)).get.sorted === List(uri2, uri3)

        inputMolecule(List(uri1, uri2)).get.sorted === List(uri3)
        inputMolecule(List(uri1, uri2))._query === Query(
          Find(List(
            Var("b"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "uri"), Var("b"), Empty, NoBinding),
            Funct("""ground (java.net.URI. "uri1")""", Seq(Empty), ScalarBinding(Var("b_1a"))),
            Funct(".compareTo ^java.net.URI", Seq(Var("b"), Var("b_1a")), ScalarBinding(Var("b_1b"))),
            Funct("!=", Seq(Var("b_1b"), Val(0)), NoBinding),
            Funct("""ground (java.net.URI. "uri2")""", Seq(Empty), ScalarBinding(Var("b_2a"))),
            Funct(".compareTo ^java.net.URI", Seq(Var("b"), Var("b_2a")), ScalarBinding(Var("b_2b"))),
            Funct("!=", Seq(Var("b_2b"), Val(0)), NoBinding))))
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.uri.>(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3)
        inputMolecule(List(uri2)).get.sorted === List(uri3)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.uri.>=(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3)
        inputMolecule(List(uri2)).get.sorted === List(uri2, uri3)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.uri.<(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3)
        inputMolecule(List(uri2)).get === List(uri1)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.uri.<=(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3)
        inputMolecule(List(uri2)).get.sorted === List(uri1, uri2)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.uri_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(uri1)).get === List(str1)
        inputMolecule(List(uri1, uri1)).get === List(str1)
        inputMolecule(List(uri1, uri2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.uri_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uri1)).get.sorted === List(str2, str3)
        inputMolecule(List(uri1, uri1)).get.sorted === List(str2, str3)
        inputMolecule(List(uri1, uri2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.uri_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uri2)).get.sorted === List(str3)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.uri_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uri2)).get.sorted === List(str2, str3)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.uri_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uri2)).get === List(str1)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.uri_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uri2)).get.sorted === List(str1, str2)
        (inputMolecule(List(uri2, uri3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.uri.uris$ insert List(
        (uri1, Some(Set(uri1, uri2))),
        (uri2, Some(Set(uri2, uri3))),
        (uri3, Some(Set(uri3, uri4))),
        (uri4, Some(Set(uri4, uri5))),
        (uri5, Some(Set(uri4, uri5, uri6))),
        (uri6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris(?))
        inputMolecule._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(
              Placeholder(Var("a"), KW("Ns", "uris"), Var("c"), None)),
            List(),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "uri"), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c"), Empty, NoBinding))))


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
            DataClause(ImplDS, Var("a"), KW("Ns", "uri"), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c"), Empty, NoBinding))))

        inputMolecule(List(Set[URI]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(uri1))).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(List(Set(uri1)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                Funct("""ground (java.net.URI. "uri1")""", Seq(Empty), ScalarBinding(Var("c_uri1"))),
                DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c_uri1"), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "uri"), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))

        inputMolecule(List(Set(uri1, uri1))).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(List(Set(uri1, uri2))).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(List(Set(uri1, uri2)))._query === Query(
          Find(List(
            Var("b"),
            AggrExpr("distinct", Seq(), Var("c")))),
          In(
            List(),
            List(
              Rule("rule1", Seq(Var("a")), Seq(
                Funct("""ground (java.net.URI. "uri1")""", Seq(Empty), ScalarBinding(Var("c_uri1"))),
                DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c_uri1"), Empty, NoBinding),
                Funct("""ground (java.net.URI. "uri2")""", Seq(Empty), ScalarBinding(Var("c_uri2"))),
                DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c_uri2"), Empty, NoBinding)))),
            List(DS)),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "uri"), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "uris"), Var("c"), Empty, NoBinding),
            RuleInvocation("rule1", Seq(Var("a"))))))


        inputMolecule(List(Set(uri1, uri3))).get === Nil
        inputMolecule(List(Set(uri2, uri3))).get === List((uri2, Set(uri3, uri2)))
        inputMolecule(List(Set(uri4, uri5))).get === List((uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        // 1 arg
        inputMolecule(Set(uri1)).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1, uri1)).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1, uri2)).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1, uri3)).get === Nil
        inputMolecule(Set(uri2, uri3)).get === List((uri2, Set(uri3, uri2)))
        inputMolecule(Set(uri4, uri5)).get === List((uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(uri1, uri2), Set[URI]())).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(List(Set(uri1), Set(uri1))).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(List(Set(uri1), Set(uri2))).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)))
        inputMolecule(List(Set(uri1), Set(uri3))).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(List(Set(uri1, uri2), Set(uri3))).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(List(Set(uri1), Set(uri2, uri3))).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)))
        inputMolecule(List(Set(uri1), Set(uri2), Set(uri3))).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(List(Set(uri1, uri2), Set(uri3, uri4))).get === List((uri1, Set(uri1, uri2)), (uri3, Set(uri4, uri3)))


        // Multiple varargs
        inputMolecule(Set(uri1, uri2), Set[URI]()).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1), Set(uri1)).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1), Set(uri2)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)))
        inputMolecule(Set(uri1), Set(uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(Set(uri1, uri2), Set(uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(Set(uri1), Set(uri2, uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)))
        inputMolecule(Set(uri1), Set(uri2), Set(uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(Set(uri1, uri2), Set(uri3, uri4)).get === List((uri1, Set(uri1, uri2)), (uri3, Set(uri4, uri3)))

        // `or`
        inputMolecule(Set(uri1, uri2) or Set[URI]()).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1) or Set(uri1)).get === List((uri1, Set(uri1, uri2)))
        inputMolecule(Set(uri1) or Set(uri2)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)))
        inputMolecule(Set(uri1) or Set(uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(Set(uri1, uri2) or Set(uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(Set(uri1) or Set(uri2, uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)))
        inputMolecule(Set(uri1) or Set(uri2) or Set(uri3)).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)))
        inputMolecule(Set(uri1, uri2) or Set(uri3, uri4)).get === List((uri1, Set(uri1, uri2)), (uri3, Set(uri4, uri3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (uri1, Set(uri1, uri2, uri3)),
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )
        Ns.uri.uris insert all
        val inputMolecule = m(Ns.uri.uris.not(?)) // or m(Ns.uri.uris.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[URI]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(uri1).get === ...
        // inputMolecule(List(uri1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(uri1)).get === List(
          // (uri1, Set(uri1, uri2, uri3)),  // uri1 match
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )
        // Same as
        inputMolecule(List(Set(uri1))).get === List(
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )

        inputMolecule(Set(uri2)).get === List(
          // (uri1, Set(uri1, uri2, uri3)),  // uri2 match
          // (uri2, Set(uri2, uri3, uri4)),  // uri2 match
          (uri3, Set(uri3, uri4, uri5))
        )

        inputMolecule(Set(uri3)).get === Nil // uri3 match all


        inputMolecule(Set(uri1), Set(uri2)).get === List(
          // (uri1, Set(uri1, uri2, uri3)),  // uri1 match, uri2 match
          // (uri2, Set(uri2, uri3, uri4)),  // uri2 match
          (uri3, Set(uri3, uri4, uri5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(uri1, uri2)).get === List(
          // (uri1, Set(uri1, uri2, uri3)),  // uri1 AND uri2 match
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )


        inputMolecule(Set(uri1), Set(uri3)).get === Nil // uri3 match all
        inputMolecule(Set(uri1, uri3)).get === List(
          // (uri1, Set(uri1, uri2, uri3)),  // uri1 AND uri3 match
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )


        inputMolecule(Set(uri1), Set(uri2), Set(uri3)).get === Nil // uri3 match all
        inputMolecule(Set(uri1, uri2, uri3)).get === List(
          // (uri1, Set(uri1, uri2, uri3)),  // uri1 AND uri2 AND uri3 match
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )


        inputMolecule(Set(uri1, uri2), Set(uri1)).get === List(
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )
        inputMolecule(Set(uri1, uri2), Set(uri2)).get === List(
          (uri3, Set(uri3, uri4, uri5))
        )
        inputMolecule(Set(uri1, uri2), Set(uri3)).get === Nil
        inputMolecule(Set(uri1, uri2), Set(uri4)).get === Nil
        inputMolecule(Set(uri1, uri2), Set(uri5)).get === List(
          (uri2, Set(uri2, uri3, uri4))
        )

        inputMolecule(Set(uri1, uri2), Set(uri2, uri3)).get === List(
          (uri3, Set(uri3, uri4, uri5))
        )
        inputMolecule(Set(uri1, uri2), Set(uri4, uri5)).get === List(
          (uri2, Set(uri2, uri3, uri4))
        )
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris.>(?))

        inputMolecule(Nil).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))
        inputMolecule(List(Set[URI]())).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        // (uri3, uri4), (uri4, uri5), (uri4, uri5, uri6)
        inputMolecule(List(Set(uri2))).get === List((uri2, Set(uri3)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris.>=(?))

        inputMolecule(Nil).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))
        inputMolecule(List(Set[URI]())).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        // (uri2, uri4), (uri3, uri4), (uri4, uri5), (uri4, uri5, uri6)
        inputMolecule(List(Set(uri2))).get === List((uri1, Set(uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris.<(?))

        inputMolecule(Nil).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))
        inputMolecule(List(Set[URI]())).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        inputMolecule(List(Set(uri2))).get === List((uri1, Set(uri1)))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris.<=(?))

        inputMolecule(Nil).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))
        inputMolecule(List(Set[URI]())).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri3)), (uri4, Set(uri4, uri5)), (uri5, Set(uri4, uri6, uri5)))

        inputMolecule(List(Set(uri2))).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri2)))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.uris(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[URI]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(uri1))).get === List(Set(uri1, uri2))
        inputMolecule(List(Set(uri2))).get === List(Set(uri1, uri2, uri3)) // (uri1, uri2) + (uri2, uri3)
        inputMolecule(List(Set(uri3))).get === List(Set(uri2, uri3, uri4)) // (uri2, uri3) + (uri3, uri4)

        inputMolecule(List(Set(uri1, uri2))).get === List(Set(uri1, uri2))
        inputMolecule(List(Set(uri1, uri3))).get === Nil
        inputMolecule(List(Set(uri2, uri3))).get === List(Set(uri2, uri3))
        inputMolecule(List(Set(uri4, uri5))).get === List(Set(uri4, uri5, uri6)) // (uri4, uri5) + (uri4, uri5, uri6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(uri1), Set(uri1))).get === List(Set(uri1, uri2))
        inputMolecule(List(Set(uri1), Set(uri2))).get === List(Set(uri1, uri2, uri3)) // (uri1, uri2) + (uri2, uri3)
        inputMolecule(List(Set(uri1), Set(uri3))).get === List(Set(uri1, uri4, uri3, uri2)) // (uri1, uri2) + (uri2, uri3) + (uri3, uri4)
        inputMolecule(List(Set(uri2), Set(uri3))).get === List(Set(uri1, uri2, uri3, uri4)) // (uri1, uri2) + (uri2, uri3) + (uri3, uri4)

        inputMolecule(List(Set(uri1, uri2), Set(uri3))).get === List(Set(uri1, uri2, uri3, uri4)) // (uri1, uri2) + (uri2, uri3) + (uri3, uri4)
        inputMolecule(List(Set(uri1), Set(uri2, uri3))).get === List(Set(uri1, uri3, uri2)) // (uri1, uri2) + (uri2, uri3)
        inputMolecule(List(Set(uri1), Set(uri2), Set(uri3))).get === List(Set(uri1, uri2, uri3, uri4)) // (uri1, uri2) + (uri2, uri3) + (uri3, uri4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (uri1, Set(uri1, uri2, uri3)),
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )
        Ns.uri.uris insert all
        val inputMolecule = m(Ns.uris.not(?)) // or m(Ns.uri.uris.!=(?))

        inputMolecule(Nil).get === List(Set(uri1, uri2, uri3, uri4, uri5))
        inputMolecule(Set[URI]()).get === List(Set(uri1, uri2, uri3, uri4, uri5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(uri1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(uri1)).get === List(Set(uri2, uri3, uri4, uri5))
        // Same as
        inputMolecule(List(Set(uri1))).get === List(Set(uri2, uri3, uri4, uri5))

        inputMolecule(Set(uri2)).get === List(Set(uri3, uri4, uri5))
        inputMolecule(Set(uri3)).get === Nil // uri3 match all

        inputMolecule(Set(uri1), Set(uri2)).get === List(Set(uri3, uri4, uri5))
        inputMolecule(Set(uri1), Set(uri3)).get === Nil // uri3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(uri1, uri2)).get === List(Set(uri2, uri3, uri4, uri5))
        inputMolecule(Set(uri1, uri3)).get === List(Set(uri2, uri3, uri4, uri5))

        inputMolecule(Set(uri1), Set(uri2), Set(uri3)).get === Nil // uri3 match all
        inputMolecule(Set(uri1, uri2, uri3)).get === List(Set(uri2, uri3, uri4, uri5))

        inputMolecule(Set(uri1, uri2), Set(uri1)).get === List(Set(uri2, uri3, uri4, uri5))
        inputMolecule(Set(uri1, uri2), Set(uri2)).get === List(Set(uri3, uri4, uri5))
        inputMolecule(Set(uri1, uri2), Set(uri3)).get === Nil
        inputMolecule(Set(uri1, uri2), Set(uri4)).get === Nil
        inputMolecule(Set(uri1, uri2), Set(uri5)).get === List(Set(uri2, uri3, uri4))

        inputMolecule(Set(uri1, uri2), Set(uri2, uri3)).get === List(Set(uri3, uri4, uri5))
        inputMolecule(Set(uri1, uri2), Set(uri4, uri5)).get === List(Set(uri2, uri3, uri4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.uris.>(?))

        inputMolecule(Nil).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))
        inputMolecule(List(Set[URI]())).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))

        inputMolecule(List(Set(uri2))).get === List(Set(uri3, uri4, uri5, uri6))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.uris.>=(?))

        inputMolecule(Nil).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))
        inputMolecule(List(Set[URI]())).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))

        inputMolecule(List(Set(uri2))).get === List(Set(uri2, uri3, uri4, uri5, uri6))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.uris.<(?))

        inputMolecule(Nil).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))
        inputMolecule(List(Set[URI]())).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))

        inputMolecule(List(Set(uri2))).get === List(Set(uri1))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.uris.<=(?))

        inputMolecule(Nil).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))
        inputMolecule(List(Set[URI]())).get === List(Set(uri1, uri2, uri3, uri4, uri5, uri6))

        inputMolecule(List(Set(uri2))).get === List(Set(uri1, uri2))

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris_(?))

        inputMolecule(Nil).get === List(uri6)
        inputMolecule(List(Set[URI]())).get === List(uri6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(uri1))).get === List(uri1)
        inputMolecule(List(Set(uri2))).get.sorted === List(uri1, uri2)
        inputMolecule(List(Set(uri3))).get.sorted === List(uri2, uri3)

        inputMolecule(List(Set(uri1, uri1))).get === List(uri1)
        inputMolecule(List(Set(uri1, uri2))).get === List(uri1)
        inputMolecule(List(Set(uri1, uri3))).get === Nil
        inputMolecule(List(Set(uri2, uri3))).get === List(uri2)
        inputMolecule(List(Set(uri4, uri5))).get.sorted === List(uri4, uri5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(uri1, uri2), Set[URI]())).get === List(uri1)
        inputMolecule(List(Set(uri1), Set(uri1))).get === List(uri1)
        inputMolecule(List(Set(uri1), Set(uri2))).get.sorted === List(uri1, uri2)
        inputMolecule(List(Set(uri1), Set(uri3))).get.sorted === List(uri1, uri2, uri3)

        inputMolecule(List(Set(uri1, uri2), Set(uri3))).get.sorted === List(uri1, uri2, uri3)
        inputMolecule(List(Set(uri1), Set(uri2, uri3))).get.sorted === List(uri1, uri2)
        inputMolecule(List(Set(uri1), Set(uri2), Set(uri3))).get.sorted === List(uri1, uri2, uri3)

        inputMolecule(List(Set(uri1, uri2), Set(uri3, uri4))).get.sorted === List(uri1, uri3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (uri1, Set(uri1, uri2, uri3)),
          (uri2, Set(uri2, uri3, uri4)),
          (uri3, Set(uri3, uri4, uri5))
        )
        Ns.uri.uris insert all
        val inputMolecule = m(Ns.uri.uris_.not(?)) // or m(Ns.uri.uris.!=(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3)
        inputMolecule(Set[URI]()).get.sorted === List(uri1, uri2, uri3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(uri1).get === ...
        // inputMolecule(List(uri1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(uri1)).get.sorted === List(uri2, uri3)
        // Same as
        inputMolecule(List(Set(uri1))).get.sorted === List(uri2, uri3)

        inputMolecule(Set(uri2)).get === List(uri3)
        inputMolecule(Set(uri3)).get === Nil // uri3 match all


        inputMolecule(Set(uri1), Set(uri2)).get === List(uri3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(uri1, uri2)).get.sorted === List(uri2, uri3)

        inputMolecule(Set(uri1), Set(uri3)).get === Nil // uri3 match all
        inputMolecule(Set(uri1, uri3)).get.sorted === List(uri2, uri3)

        inputMolecule(Set(uri1), Set(uri2), Set(uri3)).get === Nil // uri3 match all
        inputMolecule(Set(uri1, uri2, uri3)).get.sorted === List(uri2, uri3)

        inputMolecule(Set(uri1, uri2), Set(uri1)).get.sorted === List(uri2, uri3)
        inputMolecule(Set(uri1, uri2), Set(uri2)).get === List(uri3)
        inputMolecule(Set(uri1, uri2), Set(uri3)).get === Nil
        inputMolecule(Set(uri1, uri2), Set(uri4)).get === Nil
        inputMolecule(Set(uri1, uri2), Set(uri5)).get === List(uri2)

        inputMolecule(Set(uri1, uri2), Set(uri2, uri3)).get === List(uri3)
        inputMolecule(Set(uri1, uri2), Set(uri4, uri5)).get === List(uri2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris_.>(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3, uri4, uri5)
        inputMolecule(List(Set[URI]())).get.sorted === List(uri1, uri2, uri3, uri4, uri5)

        // (uri3, uri4), (uri4, uri5), (uri4, uri5, uri6)
        inputMolecule(List(Set(uri2))).get.sorted === List(uri2, uri3, uri4, uri5)

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris_.>=(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3, uri4, uri5)
        inputMolecule(List(Set[URI]())).get.sorted === List(uri1, uri2, uri3, uri4, uri5)

        // (uri2, uri4), (uri3, uri4), (uri4, uri5), (uri4, uri5, uri6)
        inputMolecule(List(Set(uri2))).get.sorted === List(uri1, uri2, uri3, uri4, uri5)

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris_.<(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3, uri4, uri5)
        inputMolecule(List(Set[URI]())).get.sorted === List(uri1, uri2, uri3, uri4, uri5)

        inputMolecule(List(Set(uri2))).get === List(uri1)

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.uri.uris_.<=(?))

        inputMolecule(Nil).get.sorted === List(uri1, uri2, uri3, uri4, uri5)
        inputMolecule(List(Set[URI]())).get.sorted === List(uri1, uri2, uri3, uri4, uri5)

        inputMolecule(List(Set(uri2))).get.sorted === List(uri1, uri2)

        (inputMolecule(List(Set(uri2, uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uri2), Set(uri3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}