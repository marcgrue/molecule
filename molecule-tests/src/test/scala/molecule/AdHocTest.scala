package molecule

import java.util.Date
import molecule.core.api.Molecule_1
import molecule.core.ast.elements._
import molecule.core.composition.Tx
import molecule.core.dsl.base.NS_0_01
import molecule.core.dsl.{attributes, base}
import molecule.core.macros.ObjBuilder
import molecule.core.util.Helpers
import molecule.core.util.testing.TxCount.schema.TxCountSchema
import molecule.datomic.api.in3_out10._
import molecule.datomic.base.facade.Conn
import molecule.datomic.base.transform.Model2Query
import molecule.datomic.peer.facade.Datomic_Peer
import scala.reflect.runtime.universe._
//import molecule.tests.core.base.dsl.CoreTest._
import molecule.tests.core.base.schema.CoreTestSchema
//import molecule.tests.examples.gremlin.gettingStarted.dsl.ModernGraph2.Person
import org.specs2.mutable.Specification
import scala.language.experimental.macros


//class AdHocTest extends molecule.setup.TestSpec with Helpers {
class AdHocTest extends Specification {


  //  "Simple hyperedge" in new GraphSetup {
  //
  //    import molecule.tests.examples.datomic.dayOfDatomic.dsl.Graph._
  //
  //    // User 1 Roles in Group 2
  //    User.name_("User1")
  //      .RoleInGroup.Group.name_("Group2")
  //      ._RoleInGroup.Role.name.inspectGet
  //
  //
  //    ok
  //  }

  //  "adhoc" in new BidirectionalSetup {
  //
  //    import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //
  //
  //
  //  }
  //
  //
  //
  //
  //  "adhoc" in new CoreSetup {
  //    import molecule.tests.core.base.dsl.CoreTest._
  //

  //  }
  //
  //  "Insert resolves to correct partitions" in new PartitionSetup {
  //    import molecule.tests.core.schemaDef.dsl.PartitionTest._
  //    m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name).inspectGet
  //  }
  //
  //
  //  "Insert resolves to correct partitions" in new ModernGraph2Setup {
  //
  //    import molecule.tests.examples.gremlin.gettingStarted.dsl.ModernGraph2._
  //
  //    m(Person.name.Knows * Person.name).inspectGet
  //  }


  //  "adhoc" >> {
  //
  //    import molecule.core.util.testing.TxCount._
  //    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(TxCountSchema)
  //    TxCount.db.Tx.apply(TxCount.db)
  //
  //  }

  "adhoc" >> {

    import molecule.tests.core.base.dsl.CoreTest._
    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(CoreTestSchema)

    Ns.int(0).str("x").Ref1.int1(1).save

    val o = m(Ns.int.str.Ref1.int1).getObj

    o.int === 0
    o.str === "x"
    o.Ref1.int1 === 1


////    val o1 = m(Ns.int.str).getObj
////    val o1 = m(Ns.int).getObj
//
//    val o2 = new Ns_int with Ns_str with Ns_Ref1_[Ref1_int1] {
//      final override lazy val int = 0
//      final override lazy val str = "x"
//      final override def Ref1: Ref1_int1 = new Ref1_int1 {
//        final override lazy val int1: Int = 1
//      }
//    }
//
//    val o3 = new Ns_int with Ns_str {
//      final override lazy val int = 0
//      final override lazy val str = "x"
//    }
//
//    trait Ns_str        { lazy val str       : String                  = ??? }
//    trait Ns_int        { lazy val int       : Int                     = ??? }
//
//
//    showRaw(reify(new Ns_int with Ns_str {
//      final override lazy val int = 0
//      final override lazy val str = "x"
//    }))

//    import scala.reflect.runtime.universe._
//    trait A { val a = 7 }
//    trait B
//    val AB = tq"A with B"
//    val ab = q"new $AB"
//
//
//    def build[T]: T = macro ObjBuilder.obj[T]
//
////    type tt = A with B
////    trait X extends A with B
//
////    build[A with B]
//    val o  = build[A with B]




//    build[A]




//    RefinedType(
//      List(
//        RefinedType(
//          List(
//            RefinedType(
//              List(
//                TypeRef(SingleType(SingleType(SingleType(SingleType(ThisType("<root>"), molecule), molecule.core), molecule.core.dsl), molecule.core.dsl.base), molecule.core.dsl.base.DummyProp, List()),
//                TypeRef(ThisType(molecule.tests.core.base.dsl.CoreTest), molecule.tests.core.base.dsl.CoreTest.Ns_int, List())
//              ),
//              Scope()
//            ),
//            TypeRef(ThisType(molecule.tests.core.base.dsl.CoreTest), molecule.tests.core.base.dsl.CoreTest.Ns_str, List())),
//          Scope()
//        ),
//        TypeRef(
//          ThisType(molecule.tests.core.base.dsl.CoreTest), molecule.tests.core.base.dsl.CoreTest.Ns_Ref1_,
//          List(
//            RefinedType(
//              List(
//                TypeRef(
//                  SingleType(SingleType(SingleType(SingleType(ThisType("<root>"), molecule), molecule.core), molecule.core.dsl), molecule.core.dsl.base), molecule.core.dsl.base.DummyProp, List()),
//                TypeRef(ThisType(molecule.tests.core.base.dsl.CoreTest), molecule.tests.core.base.dsl.CoreTest.Ref1_int1, List())),
//              Scope()
//            )
//          )
//        )
//      ),
//      Scope()
//    )

//    class obj() extends Nothing with molecule.tests.core.base.dsl.CoreTest.Ns_int with molecule.tests.core.base.dsl.CoreTest.Ns_str with molecule.tests.core.base.dsl.CoreTest.Ns_Ref1_[Nothing with molecule.tests.core.base.dsl.CoreTest.Ref1_int1] {
//      override val int: Int = castOneInt(row, 0)
//      override val str: String = castOne[String](row, 1)
//    }
//
//    class obj()
//
//    val x = new obj()

//    class obj extends Nothing with molecule.tests.core.base.dsl.CoreTest.Ns_int {
//      final override lazy val int: Int = 0
//    }
//    val x = new obj()
//
//    x.int === 0




//    o2.int === 0
//    o2.str === "x"
//    o2.Ref1.int1 === 1


    //    {
    //      val o = m(Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1).getObjList.head
    //      o.float
    //      o.Refs1.head.int1
    //      o.RefsSub1.str1
    //    }
    //
    //        val o = m(Ns.float.Ref1.int1.Refs2.*(Ref2.int2)).getObjList.head
    //        o.float
    //        o.Ref1.int1
    //        o.Ref1.Refs2.head.int2
    //
    //    Ns.str.Ref1.str1._Ns.Refs1.int1.inspectInsert(
    //      List(
    //        ("a", "a1", 1),
    //        ("b", "b1", 2)
    //      )
    //    )

    ok
  }
  //
  //
  //  "A first query" in new SeattleSetup {
  //    import molecule.tests.examples.datomic.seattle.dsl.Seattle._
  //
  //    // A Community-name molecule
  //    val communities = m(Community. e.name_)
  //
  //    // We have 150 communities
  //    communities.get.size === 150
  //  }

  //  "adhoc" in new BidirectionalSetup {
  //import molecule.tests.core.bidirectionals.dsl.Bidirectional._
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
//