package molecule.coretests.json

//import molecule._
import molecule.Json._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}


class Nested_ extends CoreSpec {


      "1 nested attr" in new CoreSetup {
        (Ns.int.str.Refs1 * Ref1.int1) insert List(
          (1, "a", List(10, 11)),
          (2, "b", List(20, 21))
        )

        (Ns.int.str.Refs1 * Ref1.int1).getJson ===
          """[
            |{"int": 1, "str": "a", "refs1": [
            |   {"int1": 10},
            |   {"int1": 11}]},
            |{"int": 2, "str": "b", "refs1": [
            |   {"int1": 20},
            |   {"int1": 21}]}
            |]""".stripMargin
      }


//      "1 nested attr" in new CoreSetup {
//        m(Ns.str.Refs1 * Ref1.int1) insert List(
//          ("a", List(1, 2)),
//          ("b", List(3, 4))
//        )
//        m(Ns.str.Refs1 * Ref1.int1).getJson ===
//          """[
//            |{"str": "a", "refs1": [{"int1": 1}, {"int1": 2}]},
//            |{"str": "b", "refs1": [{"int1": 3}, {"int1": 4}]}
//            |]""".stripMargin
//      }
//
//      "2 nested attrs" in new CoreSetup {
//        m(Ns.str.Refs1 * Ref1.int1.str1) insert List(
//          ("a", List(
//            (1, "k"),
//            (2, "l")
//          )),
//          ("b", List(
//            (3, "m"),
//            (4, "n")
//          ))
//        )
//        m(Ns.str.Refs1 * Ref1.int1).getJson ===
//          """[
//            |{"str": "a", "refs1": [{"int1": 1, "str1": "k"}, {"int1": 2, "str1": "l"}]},
//            |{"str": "b", "refs1": [{"int1": 3, "str1": "m"}, {"int1": 4, "str1": "n"}]}
//            |]""".stripMargin
//      }
}
