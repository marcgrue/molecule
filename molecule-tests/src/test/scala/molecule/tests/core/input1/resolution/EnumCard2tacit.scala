package molecule.tests.core.input1.resolution

import datomic.Util
import molecule.datomic.base.ast.query._
import molecule.core.exceptions.MoleculeException
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in1_out2._
import molecule.setup.TestSpec


class EnumCard2tacit extends TestSpec {

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
    inputMolecule._rawQuery === Query(
      Find(List(
        Var("b2"))),
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
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        Funct("missing?", Seq(DS(""), Var("a"), KW("Ns", "enums")), NoBinding))))

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
    inputMolecule._rawQuery === Query(
      Find(List(
        Var("b2"))),
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
        Funct("!=", Seq(Var("c2_1"), Val(0)), NoBinding))))


    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3)
    inputMolecule(List(Set[String]()))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding))))


    inputMolecule(List(Set(enum1))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))))))

    inputMolecule(List(Set(enum2))).get === List(enum3)
    inputMolecule(List(Set(enum3))).get === Nil


    inputMolecule(List(Set(enum1, enum2))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1, enum2)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))))))


    // nothing omitted
    inputMolecule(List(Set(enum1, enum3))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1, enum3)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum2, enum3))).get === List(enum3)
    inputMolecule(List(Set(enum2, enum3)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum1))).get.sorted === List(enum2, enum3)
    inputMolecule(List(Set(enum1), Set(enum1)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum2))).get === List(enum3)
    inputMolecule(List(Set(enum1), Set(enum2)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1), Set(enum4))).get === Nil

    inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === Nil
    inputMolecule(List(Set(enum1, enum2), Set(enum3)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3))).get === List(enum3)
    inputMolecule(List(Set(enum1, enum2), Set(enum2, enum3)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum3"), Empty, NoBinding))))))


    inputMolecule(List(Set(enum1, enum2), Set(enum4, enum5))).get === List(enum2)
    inputMolecule(List(Set(enum1, enum2), Set(enum4, enum5)))._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
        Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum1"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum2"), Empty, NoBinding))),
        NotClauses(Seq(
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum4"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Val("__enum__:Ns.enums/enum5"), Empty, NoBinding))))))
  }


  ">" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.>(?))
    inputMolecule._rawQuery === Query(
      Find(List(
        Var("b2"))),
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


    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(Nil)._rawQuery === Query(
      Find(List(
        Var("b2"))),
      Where(List(
        DataClause(ImplDS, Var("a"), KW("Ns", "enum"), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("b"), KW("db", "ident"), Var("b1"), Empty, NoBinding),
        Funct("name", Seq(Var("b1")), ScalarBinding(Var("b2"))),
        DataClause(ImplDS, Var("a"), KW("Ns", "enums"), Var("c"), Empty, NoBinding))))

    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    // (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
    inputMolecule(List(Set(enum2))).get.sorted === List(enum2, enum3, enum4, enum5)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  ">=" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.>=(?))

    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    // (enum2, enum4), (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
    inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.<(?))

    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    inputMolecule(List(Set(enum2))).get === List(enum1)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }


  "<=" in new ManySetup {
    val inputMolecule = m(Ns.enum.enums_.<=(?))

    inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

    inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2)

    (inputMolecule(List(Set(enum2, enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."

    (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Can't apply multiple values to comparison function."
  }
}
