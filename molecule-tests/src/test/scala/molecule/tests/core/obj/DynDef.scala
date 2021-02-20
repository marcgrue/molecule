//package molecule.tests.core.obj
//
//import datomicClient.ClojureBridge
//import molecule.TestSpec
//import molecule.core.util.Helpers
//import molecule.datomic.api.out3._
//import molecule.tests.core.base.dsl.CoreTest._
//import scala.language.dynamics
//
//
//class DynDef extends TestSpec with Helpers with ClojureBridge {
//
//  trait Ns_str {
//    val str: String
//  }
//  trait Ns_strm {
//    var str: String
//  }
//  trait Ns_str$ {
//    val str$: Option[String]
//  }
//  trait Ns_int {
//    val int: Int
//  }
//  trait Ns_intm {
//    var int: Int
//  }
//  trait Ns_refs {
//    val refs: Set[Long]
//  }
//  trait Ns_Refs[Sub] {
//    def Refs: List[Sub]
//  }
//  trait Ref1_str1 {
//    val str1: String
//  }
//  trait Ref1_int1 {
//    val int1: Int
//  }
//
//  trait mutations {
//    def update: Unit = {}
//    def delete: Unit = {}
//  }
//  trait dyn extends Dynamic {
//    def selectDynamic(name: String): Any = name
//    def applyDynamic(name: String)(args: Any): String = name
//  }
//
//  trait tpe1 extends Ns_strm with Ns_intm with Ns_Refs[tpe2] with dyn with mutations
//  case class obj1(
//    private val str0: String,
//    private val int0: Int,
//    private val Refs0: List[tpe2]
//  ) extends tpe1 {
//
//    var str  = str0
//    var int  = int0
//    var Refs = Refs0
//
//    // todo: macro
//    def apply(body: tpe1 => Unit): tpe1 = {
//      body(this)
//      this
//    }
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
//    //    val rolePlayer = Ns(42).str.int.Refs1.*.str1.int1 { self =>
//    val o: tpe1 = obj1("a", 1, List(obj2("a1", 11), obj2("a2", 12))) { self =>
//      self.int = self.int + 2
//      def roleMethod: Int = self.int * 7
//      //      self.int
//
//      i
//    }
//
//    int3
//
//
//    o.int === 3
//    o.Refs.head.int1 === 11
//
//
//    o.int === 3
//    o.roleMethod === 21
//
////    o.update
////    o.delete
//
//
//    Ns.str.int.get
//
//
//    ok
//  }
//
//
//      object rp {
//        val p = obj1("a", 2, List(obj2("a1", 11), obj2("a2", 12)))
//        p.int = 3
//        def roleMethod: Int = p.int * 7
//        val xx = 5
//      }
//
//      rp.roleMethod
//      rp.xx
//
//    trait tpe1 extends Ns_strm with Ns_intm with Ns_Refs[tpe2] with saver with dyn
//    trait tpe1 extends Ns_strm with Ns_intm with saver with dyn
//
//    case class Account(
//      private val name0: String,
//      private val amount0: Int,
//      //    private val Refs0: List[tpe2]
//    ) extends tpe1 {
//      var name    = name0
//      var balance = amount0
//      //    var Refs    = Refs0
//
//      // todo: macro
//
//      def apply(body: tpe1 => Unit): tpe1 = {
//        //      new Account(this.str, this.balance, this.Refs) {
//        new Account(this.name, this.balance) {
//          val self: Account = this
//
//          self.balance = 3
//          val xx = 5
//          def roleMethod: Int = self.balance * 7 + xx
//
//          override def selectDynamic(name: String): Any = name match {
//            case "roleMethod" => roleMethod
//            case other        => throw new IllegalArgumentException(s"Please implement role method `$other`.")
//          }
//        }
//      }
//    }
//
//      object role1 {
//        val o = new obj1("a", 2, List(obj2("a1", 11), obj2("a2", 12)))
//        o.int = 3
//        val xx = 5
//        def roleMethod: Int = o.balance * 7 + xx
//      }
//
//      role1.o.balance
//      role1.roleMethod
//
//
//  //  "adhoc" in new BidirectionalSetup {
//  //import molecule.tests.core.bidirectionals.dsl.Bidirectional._
//  //
//  //  }
//
//  //  "adhoc" in new PartitionSetup {
//
//  //  }
//
//
//  //  "example adhoc" in new SeattleSetup {
//  //
//  //
//  //    //    ok
//  //  }
//
//
//  //  "example adhoc" in new SocialNewsSetup {
//  //
//  //
//  //
//  //    ok
//  //  }
//}
