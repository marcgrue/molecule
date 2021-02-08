package molecule

import molecule.core.dsl.attributes
import molecule.datomic.api.out3._
import molecule.datomic.base.facade.Conn
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.tests.core.base.dsl.CoreTest.{Ns, _}
import molecule.tests.core.base.schema.CoreTestSchema
import org.specs2.mutable.Specification


//class AdHocTest extends TestSpec with Helpers with ClojureBridge {
class AdHocTest extends Specification {


  //  "adhoc" in new CoreSetup {
  "adhoc" >> {

    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(CoreTestSchema)


    //    Ns.float.Refs1.*(Ref1.int1)._Ns.str.get.head === (1f, Seq(11, 12), "a")


//    val a: Ref1_0_2_L1[Ns_, Nothing with Ns_float, Ns_Refs1_, Nothing with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]]                                                                                                                                                        = Ns.float.Refs1.*(Ref1.int1)
//    val b: Ref1_0_2_L1[Ns_, Nothing with Ns_float, Ns_Refs1_, Nothing with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]]#Next[Ref1_0_2_L1[Ns_, Nothing with Ns_float, Ns_Refs1_, Nothing with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]]#str1, Ref1_str1, String] = Ns.float.Refs1.*(Ref1.int1).str1

//    val c: Ns_0_2_L0[Ns_, Nothing with Ns_float with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]]                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      = Ns.float.Refs1.*(Ref1.int1)
//    val d: Ref1_0_3_L1[Ns_, Nothing with Ns_float with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Ns_RefsSub1_, Nothing with Ref1_str1, Float, Seq[Int], String] with attributes.Attr = Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1
//    val c0: Ns_0_2_L0[Ns_Refs1_, Nothing with Ns_float with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]]     = Ns.float.Refs1.*(Ref1.int1)
//    val c1: Molecule_0.Molecule_0_02[Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]]                        = m(Ns.float.Refs1.*(Ref1.int1))
//    val c1: Molecule_0.Molecule_0_02[Nothing with Ns_float with Ns_Refs1_[Seq[Nothing with Ref1_int1]], Float, Seq[Int]] = m(Ns.float.Refs1.*(Ref1.int1))
//    val d = Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1
//    val e: Seq[(Float, Seq[Int], String)]
    //    = m(Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1).get

    val a : Nothing with Ns_float with Ns_Refs1_[Seq[Nothing with Ref1_int1]]                                            = Ns.float.Refs1.*(Ref1.int1).getObjList.head
    val a1: Nothing with Ns_float with Ns_Refs1_[Seq[Nothing with Ref1_int1]] with Ns_RefsSub1_[Nothing with Ref1_str1] = Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1.getObjList.head

//    {
//      val o = m(Ns.float.Refs1.*(Ref1.int1).str1).getObjList.head
//      o.float
//      o.Refs1.Refs1.head.int1
//      o.Refs1.str1
//    }
    {
      val o = m(Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1).getObjList.head
      o.float
      o.Refs1.head.int1
//      o.str
      o.RefsSub1.str1
    }
//    {

//    val a: Nothing with Ns_float with Ref1_Refs2_[Nothing with Ref1_int1] = m(Ns.float.Ref1.int1.Refs2).getObjList.head
//    val a: Nothing with Ns_float with Ns_Ref1_[Nothing with Ref1_int1 with Ref1_Refs2_[Nothing with Ref2_str2]]                      = m(Ns.float.Ref1.int1.Refs2.str2).getObjList.head
//    val c: Ref2_0_3_L1[Ns_, Nothing with Ns_float, Ref1_Refs2_, Nothing with Ref1_Refs2_[Seq[Nothing with Ref2_str2]], Float, Int, Seq[String]] = Ns.float.Ref1.int1.Refs2.*(Ref2.str2)
//    val b: Nothing with Ns_float with Ref1_Refs2_[Nothing with Ref1_Refs2_[Seq[Nothing with Ref2_str2]]]                                        = m(Ns.float.Ref1.int1.Refs2.*(Ref2.str2)).getObjList.head
//    val b: Nothing with Ns_float with Ns_Ref1_[Nothing with Ref1_int1 with Ref1_Refs2_[Seq[Nothing with Ref2_str2]]]                            = m(Ns.float.Ref1.int1.Refs2.*(Ref2.str2)).getObjList.head

//      val o = m(Ns.float.Ref1.int1).getObjList.head
//      o.float
//      o.Ref1.int1
//    }

      val o = m(Ns.float.Ref1.int1.Refs2.*(Ref2.int2)).getObjList.head
    o.float
    o.Ref1.int1
    o.Ref1.Refs2.head.int2


    ok
  }



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