package molecule.coretests.json

import molecule.imports._
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
        |[{"ref2.int2": 2}, {"ns.int": 22}],
        |[{"ref2.int2": 1}, {"ns.int": 11}]
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
        |[{"ref2.int2": 1}, {"ns.int": 11, "ns.str": "aa"}],
        |[{"ref2.int2": 2}, {"ns.int": 22, "ns.str": "bb"}]
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
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ns.int": 22}],
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ns.int": 11}]
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
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ns.int": 11, "ns.str": "aa"}],
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ns.int": 22, "ns.str": "bb"}]
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
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ref1.int1": 11, "ref1.str1": "aa"}],
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ref1.int1": 22, "ref1.str1": "bb"}]
        |]""".stripMargin

    // .. including transaction meta data
    m(Ref2.int2.str2 ~ Ref1.int1.str1.tx_(Ns.str)).getJson ===
      """[
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ref1.int1": 11, "ref1.str1": "aa", "tx.ns.str": "Tx meta data"}],
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ref1.int1": 22, "ref1.str1": "bb", "tx.ns.str": "Tx meta data"}]
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
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ns.int": 11, "ref1.str1": "aa"}],
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ns.int": 22, "ref1.str1": "bb"}]
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
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ns.int": 11, "ref1.str1": "aa"}],
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ns.int": 22, "ref1.str1": "bb"}]
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
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ref1.int1": 22}],
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ref1.int1": 11}]
        |]""".stripMargin

    m(Ref2.int2.str2 ~ Ns.refs1).getJson ===
      """[
        |[{"ref2.int2": 1, "ref2.str2": "a"}, {"ns.refs1": [17592186045446]}],
        |[{"ref2.int2": 2, "ref2.str2": "b"}, {"ns.refs1": [17592186045448]}]
        |]""".stripMargin
  }
}