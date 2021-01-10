package molecule

import datomicClient.ClojureBridge
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.tests.core.base.dsl.coreTest._


class AdHocTest extends TestSpec with Helpers with ClojureBridge {



  //  import scala.language.dynamics
  //
  //  object DynImpl extends Dynamic {
  //    def selectDynamic(name: String): String = name
  //  }
  //
  //  trait Ns_str {
  //    val str: String
  //  }
  //
  //  trait Ns_str$ {
  //    val str$: Option[String]
  //  }
  //
  //  trait Ns_int {
  //    val int: Int
  //  }
  //
  //  trait Ns_refs {
  //    val refs: Set[Long]
  //  }
  //
  //  trait Ns_Refs[Sub] {
  //    def Refs: List[Sub]
  //  }
  //
  //  trait Ref1_str1 {
  //    val str1: String
  //  }
  //
  //  trait Ref1_int1 {
  //    val int1: Int
  //  }
  //
  //
  //  trait tpe1 extends Ns_str with Ns_int with Ns_Refs[tpe2]  with dyn
  //
  //  case class obj1(
  //    str: String,
  //    int: Int,
  //    Refs: List[tpe2]
  //  ) extends tpe1 {
  //
  //    def apply(body: tpe1 => Any): tpe1 = ???
  //    def mutable(body: tpe1 => Any): tpe1 with saver = ???
  //  }
  //
  //  trait saver {
  //    def update: Unit = ???
  //    def delete: Unit = ???
  //  }
  //  trait withSelf {
  //
  //  }
  //  trait dyn extends Dynamic {
  //    def selectDynamic(name: String): String = name
  //    def applyDynamic(name: String)(args: Any): String = name
  //  }
  //
  //  trait tpe2 extends Ref1_str1 with Ref1_int1
  //  case class obj2(
  //    str1: String,
  //    int1: Int,
  //  ) extends tpe2
  //
  //
  //  "adhoc" in new CoreSetup {
  //
  //
  //    val objs = List(
  //      obj1("a", 1, List(obj2("a1", 11), obj2("a2", 12))),
  //      obj1("b", 2, List(obj2("b1", 21), obj2("b2", 22)))
  //    )
  //
  //    objs.map { o1 =>
  //      o1.int
  //      o1.str
  //      o1.Refs.map { o2 =>
  //        o2.int1
  //        o2.str1
  //      }
  //    }
  //
  //
  //    //    val rolePlayer = Ns(42).str.int.Refs1.*.str1.int1 { self =>
  //    val rolePlayer = obj1("a", 1, List(obj2("a1", 11), obj2("a2", 12))) { self =>
  //      def roleMethod: Int = self.int * 7
  //      val xx = 5
  //    }
  //
  //    val rolePlayerMut = obj1("a", 1, List(obj2("a1", 11), obj2("a2", 12))).mutable { self =>
  //      def roleMethod: Int = self.int * 7
  //      val xx = 5
  //      self.int = 8
  //    }
  //
  //    rolePlayer.int
  //    //    rolePlayer.int = 6
  //    rolePlayer.Refs.head.int1
  //
  //    //    rolePlayer.i = 7
  //
  //    rolePlayer.update
  //    rolePlayer.delete
  //
  //    rolePlayer.str
  //
  //
  //    rolePlayer.xxx
  //    //    rolePlayer.roleMethod(3)
  //    //    rolePlayer.xx
  //
  //
  //    Ns.str.int.inspectGet
  //
  //
  //    ok
  //  }

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
