package molecule.coretests.json

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
//import molecule.util.expectCompileError

class Composite extends CoreSpec {


  "1 + 1" in new CoreSetup {
    insert(
      Ref2.int2, Ns.int
    )(Seq(
      // Two rows of data
      (1, 11),
      (2, 22)
    ))()

    m(Ref2.int2 ~ Ns.int).getJson ===
      """[
        |[{"int2": 2}, {"int": 22}],
        |[{"int2": 1}, {"int": 11}]
        |]""".stripMargin
  }


  "1 + 2" in new CoreSetup {
    insert(
      Ref2.int2, Ns.int.str
    )(Seq(
      // Two rows of data
      (1, (11, "aa")),
      (2, (22, "bb"))
    ))()

    m(Ref2.int2 ~ Ns.int.str).getJson ===
      """[
        |[{"int2": 1}, {"int": 11, "str": "aa"}],
        |[{"int2": 2}, {"int": 22, "str": "bb"}]
        |]""".stripMargin
  }


  "2 + 1" in new CoreSetup {
    insert(
      Ref2.int2.str2, Ns.int
    )(Seq(
      // Two rows of data
      ((1, "a"), 11),
      ((2, "b"), 22)
    ))()

    m(Ref2.int2.str2 ~ Ns.int).getJson ===
      """[
        |[{"int2": 2, "str2": "b"}, {"int": 22}],
        |[{"int2": 1, "str2": "a"}, {"int": 11}]
        |]""".stripMargin
  }


  "2 + 2" in new CoreSetup {
    insert(
      Ref2.int2.str2, Ns.int.str
    )(
      Seq(
        ((1, "a"), (11, "aa")),
        ((2, "b"), (22, "bb"))
      )
    )()

    m(Ref2.int2.str2 ~ Ns.int.str).getJson ===
      """[
        |[{"int2": 1, "str2": "a"}, {"int": 11, "str": "aa"}],
        |[{"int2": 2, "str2": "b"}, {"int": 22, "str": "bb"}]
        |]""".stripMargin
  }


  "2 + 3 (2+1tx)" in new CoreSetup {
    insert(
      Ref2.int2.str2, Ref1.int1.str1
    )(
      Seq(
        ((1, "a"), (11, "aa")),
        ((2, "b"), (22, "bb"))
      )
    )(
      Ns.str_("Tx meta data")
    )

    m(Ref2.int2.str2 ~ Ref1.int1.str1).getJson ===
      """[
        |[{"int2": 1, "str2": "a"}, {"int1": 11, "str1": "aa"}],
        |[{"int2": 2, "str2": "b"}, {"int1": 22, "str1": "bb"}]
        |]""".stripMargin

    // .. including transaction meta data
    m(Ref2.int2.str2 ~ Ref1.int1.str1.tx_(Ns.str)).getJson ===
      """[
        |[{"int2": 1, "str2": "a"}, {"int1": 11, "str1": "aa", "tx_str": "Tx meta data"}],
        |[{"int2": 2, "str2": "b"}, {"int1": 22, "str1": "bb", "tx_str": "Tx meta data"}]
        |]""".stripMargin
  }


  "Card-one ref" in new CoreSetup {
    insert(
      Ref2.int2.str2, Ns.int.Ref1.str1
    )(
      Seq(
        ((1, "a"), (11, "aa")),
        ((2, "b"), (22, "bb"))
      )
    )()

    m(Ref2.int2.str2 ~ Ns.int.Ref1.str1).getJson ===
      """[
        |[{"int2": 1, "str2": "a"}, {"int": 11, "str1": "aa"}],
        |[{"int2": 2, "str2": "b"}, {"int": 22, "str1": "bb"}]
        |]""".stripMargin
  }


  "Card-many ref - one value" in new CoreSetup {
    insert(
      Ref2.int2.str2, Ns.int.Refs1.str1
    )(
      Seq(
        ((1, "a"), (11, "aa")),
        ((2, "b"), (22, "bb"))
      )
    )()

    m(Ref2.int2.str2 ~ Ns.int.Refs1.str1).getJson ===
      """[
        |[{"int2": 1, "str2": "a"}, {"int": 11, "str1": "aa"}],
        |[{"int2": 2, "str2": "b"}, {"int": 22, "str1": "bb"}]
        |]""".stripMargin
  }


  "Card-many ref - one value 2" in new CoreSetup {
    insert(
      Ref2.int2.str2, Ns.Refs1.int1
    )(
      Seq(
        ((1, "a"), 11),
        ((2, "b"), 22)
      )
    )()

    m(Ref2.int2.str2 ~ Ns.Refs1.int1).getJson ===
      """[
        |[{"int2": 2, "str2": "b"}, {"int1": 22}],
        |[{"int2": 1, "str2": "a"}, {"int1": 11}]
        |]""".stripMargin

    m(Ref2.int2.str2 ~ Ns.refs1).getJson ===
      """[
        |[{"int2": 1, "str2": "a"}, {"refs1": [17592186045446]}],
        |[{"int2": 2, "str2": "b"}, {"refs1": [17592186045448]}]
        |]""".stripMargin
  }
}