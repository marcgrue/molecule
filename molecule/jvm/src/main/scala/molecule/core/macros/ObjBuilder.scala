package molecule.core.macros

import scala.reflect.macros.blackbox


class ObjBuilder(val c: blackbox.Context) extends Base {

  import c.universe._

  val z = InspectMacro("MacroTest", 1, 900)


  def obj[T: W]: Tree = {
    val ObjType = weakTypeOf[T]
    //    val t       = q"new A with B"

    val a  = tq"A"
    val b  = tq"B"
    val ab = Seq(a, b)


    //        val t1 = "molecule.tests.core.base.dsl.CoreTest.Ns_int"
    val t1 = q"Ns_intx"
    val t2 = tq"Ns_str"

    val t4 = TypeName("DummyProp with Ref1_int1 with Ref1_str1")
    val t3 = tq"Ns_Ref1_[$t4]"


    val ts  = Seq(
      tq"molecule.tests.core.base.dsl.CoreTest.Ns_int",
      tq"molecule.tests.core.base.dsl.CoreTest.Ns_str"
    )
//    val obj = Obj("import molecule.tests.core.base.dsl.CoreTest._", 0, Seq(
//      Prop(tq"Ns_int", "int", tq"Int", castOneAttr("Int")),
//      Prop(tq"Ns_str", "str", tq"String", castOneAttr("String"))
//    ))
    //
    //        val t0 = tq""

    //    val awb = q"${ab}"

    val t = q"new A with B"
    //    val t       = q"new A with B with C[A with B]"
    //    val t       = q"new ${ObjType.widen}"

    z(1
      , showRaw(ObjType)
      , showRaw(tq"A with B")
      , showRaw(tq"A with B with C[A with B]")
      ,
      q"""{
           import molecule.tests.core.base.dsl.CoreTest._
           new $t1 with $t2 with $t3 {

           }
         }
         """
      , t
    )


    t
  }

}
