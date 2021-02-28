package molecule.tests.core.obj

import datomicClient.ClojureBridge
import molecule.core.dsl.base.Init
//import molecule.core.transform.DynamicProp
import molecule.setup.TestSpec
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.tests.core.base.dsl.CoreTest._
import scala.language.dynamics
import scala.language.implicitConversions

class Mutable extends TestSpec with Helpers {

  trait Ns_name_mut {
    var name: String = _
  }
  trait Ns_age_mut {
    var age: Int = _
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

  trait DynamicProp2 extends Dynamic {
    def selectDynamic(name: String): Any = 7
  }


  "adhoc" in new CoreSetup {

    type Obj = DynamicProp2 with Ns_age_mut with Ns_name_mut
    //      with DynamicProp
    //      with mutations

    //    val person = new DynamicProp with Ns_age_mut with Ns_name_mut {
    val person = new DynamicProp2 with Ns_age_mut with Ns_name_mut {
      age = 1
      name = "a"

      // todo: macro extracts role methods from `body` and generates the following code:
      //      def apply(body: Obj => Unit): DynamicProp with Ns_age_mut with Ns_name_mut = new DynamicProp with Ns_age_mut with Ns_name_mut {
      def apply(body: Obj => Unit): Obj = new DynamicProp2 with Ns_age_mut with Ns_name_mut {
        age = 1
        name = "a"
        def roleMethod: Int = {
          age = 4
          age
        }

        override def selectDynamic(name: String): Any = name match {
          case "roleMethod" => roleMethod
          case other        => throw new IllegalArgumentException(s"Please implement role method `$other`.")
        }
      }
    }

    //    object Person extends DynamicProp2 with Ns_age_mut with Ns_name_mut {
    object Person extends Ns_age_mut with Ns_name_mut {

      // todo: macro extracts role methods from `body` and generates the following code:
      //      def apply(body: Obj => Unit): DynamicProp with Ns_age_mut with Ns_name_mut = new DynamicProp with Ns_age_mut with Ns_name_mut {
      def apply(e: Long)(body: Ns_age_mut with Ns_name_mut => Unit): DynamicProp2 with Ns_age_mut with Ns_name_mut = new DynamicProp2 with Ns_age_mut with Ns_name_mut {
        age = 1
        name = "a"

        def roleMethod: Int = {
          age = 4 + xx
          age
        }

        override def selectDynamic(name: String): Any = name match {
          case "roleMethod" => roleMethod
          case other        => throw new IllegalArgumentException(s"Please implement role method `$other`.")
        }

      }
    }

    person.age === 1
    //    person.name === "a"

    def xx = 5

    //    val p2: Obj = person.apply { self =>
    val p2: DynamicProp2 with Ns_age_mut with Ns_name_mut = Person(42L) { self =>

      def roleMethod: Int = {
        self.age = 4 + xx
        self.age
      }
    }

    // Role player mutation
    person.roleMethod === 7
    person.age === 1

    p2.age === 1
    p2.roleMethod === 9
    p2.age === 9

    val p3 = Person
    // p3.roleMethod // correctly no dynamism when roleMethods haven't been defined

    // Direct mutation
    person.age = 5
    person.age === 5

    //    person.update
    //    person.delete


//    Ns.int.apply { self: Init with Ns_int => }
//
//    val z = Ns.int { self => self.int }


    Ns.int(1).save
    val x = m(Ns.int) { self =>

      def hej = 5

    }

    x.int === 7
    x.hej === 5


    //          object rp {
    //            val p = obj1("a", 2, List(obj2("a1", 11), obj2("a2", 12)))
    //            p.int = 3
    //            def roleMethod: Int = p.int * 7
    //            val xx = 5
    //          }
    //
    //          rp.roleMethod
    //          rp.xx
    //
    //        trait tpe1 extends Ns_strm with Ns_intm with Ns_Refs[tpe2] with saver with dyn
    //        trait tpe1 extends Ns_strm with Ns_intm with saver with dyn
    //
    //        case class Account(
    //          private val name0: String,
    //          private val amount0: Int,
    //          //    private val Refs0: List[tpe2]
    //        ) extends tpe1 {
    //          var name    = name0
    //          var balance = amount0
    //          //    var Refs    = Refs0
    //
    //          // todo: macro
    //
    //          def apply(body: tpe1 => Unit): tpe1 = {
    //            //      new Account(this.str, this.balance, this.Refs) {
    //            new Account(this.name, this.balance) {
    //              val self: Account = this
    //
    //              self.balance = 3
    //              val xx = 5
    //              def roleMethod: Int = self.balance * 7 + xx
    //
    //              override def selectDynamic(name: String): Any = name match {
    //                case "roleMethod" => roleMethod
    //                case other        => throw new IllegalArgumentException(s"Please implement role method `$other`.")
    //              }
    //            }
    //          }
    //        }
    //
    //          object role1 {
    //            val o = new obj1("a", 2, List(obj2("a1", 11), obj2("a2", 12)))
    //            o.int = 3
    //            val xx = 5
    //            def roleMethod: Int = o.balance * 7 + xx
    //          }
    //
    //          role1.o.balance
    //          role1.roleMethod



    ok
  }
}
