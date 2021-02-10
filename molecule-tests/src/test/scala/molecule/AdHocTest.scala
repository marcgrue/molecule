package molecule

import java.util.Date
import molecule.core.ast.elements._
import molecule.core.util.Helpers
import molecule.datomic.api.in1_out5._
import molecule.datomic.base.facade.Conn
import molecule.datomic.base.transform.Model2Query
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.tests.core.base.dsl.CoreTest._
import molecule.tests.core.base.schema.CoreTestSchema
import org.specs2.mutable.Specification


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
  //    import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //
  //        val animalsCloseTo = m(Person.name_(?).CloseTo.*(CloseTo.weight.Animal.name))
  //  }
  //
  //
  //
  //
  //    "adhoc" in new CoreSetup {
  //
  //      //    val a: Molecule_0.Molecule_0_02[Ns_[Nothing with Ns_str] with Ref1_[Nothing with Ref1_int1], String, Int] = m(Ns.str("a") + Ref1.int1(10))
  //      //
  //      //    val b: Ns_[Nothing with Ns_str] with Ref1_[Nothing with Ref1_int1] = a.getObjList.head
  //      //
  //      //    b.Ns.str
  //      //    b.Ref1.int1
  //      //
  //      //
  //      //    val c: Molecule_0.Molecule_0_03[Nothing with Ns_int with Tx_[Ns_[Nothing with Ns_str] with Ref1_[Nothing with Ref1_int1]], Int, String, Int] = m(Ns.int(1).Tx.apply(Ns.str("a") + Ref1.int1(10)))
  //
  //      //      m(Ns.int.str)
  //
  //
  //
  //    }



  "adhoc" >> {

    import molecule.tests.core.base.dsl.CoreTest._
    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(CoreTestSchema)
    //    m(Ns.str.Ref1.str1._Ns.Refs1.int1)

    //      m(Ns.int.str)

    //      m(Ns.str("a") + Ref1.int1(10)).inspectGet

    //    m(Ns.int(1).Tx.apply(Ref2.str2("a") + Ref1.int1(10))) //.inspectSave

    /*
    1          List(
      1          Atom("Ns", "int", "Int", 1, Eq(Seq(1)), None, Seq(), Seq())
      2          TxMetaData(
        1          Composite(
          1          Atom("Ref2", "str2", "String", 1, Eq(Seq("a")), None, Seq(), Seq()))
        2          Composite(
          1          Atom("Ref1", "int1", "Int", 1, Eq(Seq(10)), None, Seq(), Seq()))))
     */


    //    m(Ns.int(1).Tx(Ref2.str2("a"))).inspectSave
    //    m(Ns.int(1).Tx(Ref2.str2("a"))).save

    val date1 = new Date()

//    Model2Query(
//      Model(List(
//        Atom("Ns", "date", "java.util.Date", 1, Eq(Seq("__ident__date1")), None, Seq(), Seq())))
//    )

    m(Ns.date(date1)).inspectGet



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