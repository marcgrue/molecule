package molecule.coretests.json

import molecule.api.out3._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class Composite extends CoreSpec {


  "1 + 1" in new CoreSetup {
    Ref2.int2 + Ns.int insert Seq(
      // Two rows of data
      (1, 11),
      (2, 22)
    )

    m(Ref2.int2 + Ns.int).getJson ===
      """[
        |[{"Ref2.int2": 2}, {"Ns.int": 22}],
        |[{"Ref2.int2": 1}, {"Ns.int": 11}]
        |]""".stripMargin
  }


  "1 + 2" in new CoreSetup {
    Ref2.int2 + Ns.int.str insert Seq(
      // Two rows of data
      (1, (11, "aa")),
      (2, (22, "bb"))
    )

    m(Ref2.int2 + Ns.int.str).getJson ===
      """[
        |[{"Ref2.int2": 1}, {"Ns.int": 11, "Ns.str": "aa"}],
        |[{"Ref2.int2": 2}, {"Ns.int": 22, "Ns.str": "bb"}]
        |]""".stripMargin
  }


  "2 + 1" in new CoreSetup {
    Ref2.int2.str2 + Ns.int insert Seq(
      // Two rows of data
      ((1, "a"), 11),
      ((2, "b"), 22)
    )

    m(Ref2.int2.str2 + Ns.int).getJson ===
      """[
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ns.int": 22}],
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ns.int": 11}]
        |]""".stripMargin
  }


  "2 + 2" in new CoreSetup {
    Ref2.int2.str2 + Ns.int.str insert Seq(
      ((1, "a"), (11, "aa")),
      ((2, "b"), (22, "bb"))
    )


    m(Ref2.int2.str2 + Ns.int.str).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ns.int": 11, "Ns.str": "aa"}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ns.int": 22, "Ns.str": "bb"}]
        |]""".stripMargin
  }


  "2 + 3 (2 + 1tx)" in new CoreSetup {
    Ref2.int2.str2 + Ref1.int1.str1.Tx(Ns.str_("Tx meta data")) insert Seq(
      ((1, "a"), (11, "aa")),
      ((2, "b"), (22, "bb"))
    )

    m(Ref2.int2.str2 + Ref1.int1.str1).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ref1.int1": 11, "Ref1.str1": "aa"}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ref1.int1": 22, "Ref1.str1": "bb"}]
        |]""".stripMargin

    // .. including transaction meta data
    m(Ref2.int2.str2 + Ref1.int1.str1.Tx(Ns.str)).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ref1.int1": 11, "Ref1.str1": "aa", "tx.Ns.str": "Tx meta data"}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ref1.int1": 22, "Ref1.str1": "bb", "tx.Ns.str": "Tx meta data"}]
        |]""".stripMargin
  }


  "2 + 3 (2 + 2tx with ref)" in new CoreSetup {
    Ref2.int2.str2 + Ref1.int1.str1.Tx(Ns.str_("Tx meta data").Ref1.int1_(42)) insert Seq(
      ((1, "a"), (11, "aa")),
      ((2, "b"), (22, "bb"))
    )

    // Note how ref attr in tx meta data has both a `tx` and `ref1` prefix
    m(Ref2.int2.str2 + Ref1.int1.str1.Tx(Ns.str.Ref1.int1)).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ref1.int1": 11, "Ref1.str1": "aa", "tx.Ns.str": "Tx meta data", "tx.ref1.Ref1.int1": 42}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ref1.int1": 22, "Ref1.str1": "bb", "tx.Ns.str": "Tx meta data", "tx.ref1.Ref1.int1": 42}]
        |]""".stripMargin
  }


  "Card-one ref" in new CoreSetup {
    Ref2.int2.str2 + Ns.int.Ref1.str1 insert Seq(
      ((1, "a"), (11, "aa")),
      ((2, "b"), (22, "bb"))
    )

    m(Ref2.int2.str2 + Ns.int.Ref1.str1).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ns.int": 11, "ref1.Ref1.str1": "aa"}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ns.int": 22, "ref1.Ref1.str1": "bb"}]
        |]""".stripMargin
  }


  "Card-many ref - one value" in new CoreSetup {
    Ref2.int2.str2 + Ns.int.Refs1.str1 insert Seq(
      ((1, "a"), (11, "aa")),
      ((2, "b"), (22, "bb"))
    )

    m(Ref2.int2.str2 + Ns.int.Refs1.str1).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ns.int": 11, "refs1.Ref1.str1": "aa"}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ns.int": 22, "refs1.Ref1.str1": "bb"}]
        |]""".stripMargin
  }


  "Card-many ref - one value 2" in new CoreSetup {
    Ref2.int2.str2 + Ns.Refs1.int1 insert Seq(
      ((1, "a"), 11),
      ((2, "b"), 22)
    )

    m(Ref2.int2.str2 + Ns.Refs1.int1).getJson ===
      """[
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"refs1.Ref1.int1": 22}],
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"refs1.Ref1.int1": 11}]
        |]""".stripMargin

    m(Ref2.int2.str2 + Ns.refs1).getJson ===
      """[
        |[{"Ref2.int2": 1, "Ref2.str2": "a"}, {"Ns.refs1": [17592186045455]}],
        |[{"Ref2.int2": 2, "Ref2.str2": "b"}, {"Ns.refs1": [17592186045457]}]
        |]""".stripMargin
  }
}