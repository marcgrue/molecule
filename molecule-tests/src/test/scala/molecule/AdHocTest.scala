package molecule

import datomicClient.ClojureBridge
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.tests.core.base.dsl.coreTest._


class AdHocTest extends TestSpec with Helpers with ClojureBridge {


  import scala.language.dynamics

  object DynImpl extends Dynamic {
    def selectDynamic(name: String): String = name
  }

  trait Ns_str {
    val str: String
  }

  trait Ns_str$ {
    val str$: Option[String]
  }

  trait Ns_int {
    val int: Int
  }

  trait Ns_refs {
    val refs: Set[Long]
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


  case class obj1(
    str: String,
    int: Int,
    Refs: List[obj2]
  ) extends Ns_str with Ns_int with Ns_Refs[obj2]

  case class obj2(
    str1: String,
    int1: Int,
  ) extends Ref1_str1 with Ref1_int1


  "adhoc" in new CoreSetup {

    val x0: DynImpl.type = DynImpl
    val x1: String       = DynImpl.foo
    val x2: String       = DynImpl.foo2

    val z = Ns.str.int.get.head

    z._1
    z._2

    val a1: List[(String, Seq[Int])] = Ns.str.Refs1.*(Ref1.int1).get


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


    ok
  }

  //  "adhoc" in new BidirectionalSetup {
  //import molecule.tests.core.bidirectionals.dsl.bidirectional._
  //
  //  }

  //  "adhoc" in new PartitionSetup {

  //  }


  //  "example adhoc" in new SeattleSetup {
  //
  //
  //    //    ok
  //  }


  //  "example adhoc" in new SocialNewsSetup {
  //
  //
  //
  //    ok
  //  }
}
