package molecule.tests.core.obj

import datomicClient.ClojureBridge
import molecule.TestSpec
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.tests.core.base.dsl.CoreTest._

class Mutable extends TestSpec with Helpers {

  trait Ns_str_mut {
    var str: String
  }
  trait Ns_int_mut {
    var int: Int
  }
  trait Ns_Refs[Sub] {
    def Refs: List[Sub]
  }
  trait Ref1_str1 {
    val str1: String
  }
  trait Ref1_int1 {
    val int1: Int
  }

  trait mutations {
    def update: Unit = {}
    def delete: Unit = {}
  }

  trait tpe1 extends Ns_str_mut with Ns_int_mut with Ns_Refs[tpe2] with mutations
  case class obj1(
    private val str0: String,
    private val int0: Int,
    private val Refs0: List[tpe2]
  ) extends tpe1 {

    var str  = str0
    var int  = int0
    var Refs = Refs0

  }

  trait tpe2 extends Ref1_str1 with Ref1_int1
  case class obj2(
    str1: String,
    int1: Int,
  ) extends tpe2


  "adhoc" in new CoreSetup {

    val objs = List(
      obj1("a", 1, List(obj2("a1", 11), obj2("a2", 12))),
      obj1("b", 2, List(obj2("b1", 21), obj2("b2", 22)))
    )

    objs.map { o1 =>
      o1.int
      o1.str
      o1.Refs.map { o2 =>
        o2.int1
        o2.str1
      }
    }

    val o: tpe1 = obj1("a", 1, List(obj2("a1", 11), obj2("a2", 12)))


    val o2: Ns_str_mut
      with Ns_int_mut
      with Ns_Refs[
      Ref1_str1
        with Ref1_int1
    ] = new Ns_str_mut
      with Ns_int_mut
      with Ns_Refs[
      Ref1_str1
        with Ref1_int1
    ] {
      var str: String = "a"
      var int: Int    = 1
      def Refs = List(
        new Ref1_str1 with Ref1_int1 {
          val str1: String = "aa"
          val int1: Int    = 11
        }
      )
    }


    o.int === 3
    o.Refs.head.int1 === 11


    o.int = 4
    o.int === 4

    o.update
    o.delete


    Ns.str.int.get
    Ns.str.int.getObjList


    ok
  }
}
