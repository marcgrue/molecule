package molecule.coretests

import molecule.api.in3_out22._
import molecule.api.out4.m
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional.{Animal, Person, Quality}
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.facade.Conn
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._

class AdHocTest extends CoreSpec {


  "adhoc" in new CoreSetup {


    m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1) insert List(("book", "John", List("Marc")))


    m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).getJson ===
      """[
        |{"ns.str": "book", "ref1.ref1.str1": "John", "ns.refs1": [
        |   {"ref1.str1": "Marc"}]}
        |]""".stripMargin

    m(Ns.str.Ref1.str1._Ns.Refs1.str1).getJson ===
      """[
        |{"ns.str": "book", "ref1.ref1.str1": "John", "refs1.ref1.str1": "Marc"}
        |]""".stripMargin

    ok
  }

}