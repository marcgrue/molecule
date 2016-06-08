package molecule.dsl

import java.util.Date
import molecule.dsl.schemaDSL._
import scala.language.higherKinds


trait Generic0[Ns0, Ns1[_], In0[_], In1[_, _]] {
  lazy val e          : Ns1[Long   ] with OneLong   [Ns1[Long   ], In1[Long   , Long   ]] = ???
  lazy val a          : Ns1[String ] with OneString [Ns1[String ], In1[String , String ]] = ???
  lazy val v          : Ns1[Any    ] with OneAny    [Ns1[Any    ], In1[Any    , Any    ]] = ???
  lazy val ns         : Ns1[String ] with OneString [Ns1[String ], In1[String , String ]] = ???
  lazy val tx         : Ns1[Long   ] with OneLong   [Ns1[Long   ], In1[Long   , Long   ]] = ???
  lazy val txT        : Ns1[Long   ] with OneLong   [Ns1[Long   ], In1[Long   , Long   ]] = ???
  lazy val txInstant  : Ns1[Date   ] with OneDate   [Ns1[Date   ], In1[Date   , Date   ]] = ???
  lazy val op         : Ns1[Boolean] with OneBoolean[Ns1[Boolean], In1[Boolean, Boolean]] = ???

  lazy val e_         : Ns0 with OneLong   [Ns0, In0[Long   ]] = ???
  lazy val a_         : Ns0 with OneString [Ns0, In0[String ]] = ???
  lazy val v_         : Ns0 with OneAny    [Ns0, In0[Any    ]] = ???
  lazy val ns_        : Ns0 with OneString [Ns0, In0[String ]] = ???
  lazy val tx_        : Ns0 with OneLong   [Ns0, In0[Long   ]] = ???
  lazy val txT_       : Ns0 with OneLong   [Ns0, In0[Long   ]] = ???
  lazy val txInstant_ : Ns0 with OneDate   [Ns0, In0[Date   ]] = ???
  lazy val op_        : Ns0 with OneBoolean[Ns0, In0[Boolean]] = ???
}


trait Generic1[Ns1[_], Ns2[_,_], In1[_,_], In2[_,_,_], A] {
  lazy val e         : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
  lazy val a         : Ns2[A, String ] with OneString [Ns2[A, String ], In2[String , A, String ]] = ???
  lazy val v         : Ns2[A, Any    ] with OneAny    [Ns2[A, Any    ], In2[Any    , A, Any    ]] = ???
  lazy val ns        : Ns2[A, String ] with OneString [Ns2[A, String ], In2[String , A, String ]] = ???
  lazy val tx        : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
  lazy val txT       : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
  lazy val txInstant : Ns2[A, Date   ] with OneDate   [Ns2[A, Date   ], In2[Date   , A, Date   ]] = ???
  lazy val op        : Ns2[A, Boolean] with OneBoolean[Ns2[A, Boolean], In2[Boolean, A, Boolean]] = ???

  lazy val e_          : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
  lazy val a_          : Ns1[A] with OneString [Ns1[A], In1[String , A]] = ???
  lazy val v_          : Ns1[A] with OneAny    [Ns1[A], In1[Any    , A]] = ???
  lazy val ns_         : Ns1[A] with OneString [Ns1[A], In1[String , A]] = ???
  lazy val tx_         : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
  lazy val txT_        : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
  lazy val txInstant_  : Ns1[A] with OneDate   [Ns1[A], In1[Date   , A]] = ???
  lazy val op_         : Ns1[A] with OneBoolean[Ns1[A], In1[Boolean, A]] = ???
}


trait Generic2[Ns2[_,_], Ns3[_,_,_], In2[_,_,_], In3[_,_,_,_], A, B] {
  lazy val e         : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
  lazy val a         : Ns3[A, B, String ] with OneString [Ns3[A, B, String ], In3[String , A, B, String ]] = ???
  lazy val v         : Ns3[A, B, Any    ] with OneAny    [Ns3[A, B, Any    ], In3[Any    , A, B, Any    ]] = ???
  lazy val ns        : Ns3[A, B, String ] with OneString [Ns3[A, B, String ], In3[String , A, B, String ]] = ???
  lazy val tx        : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
  lazy val txT       : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
  lazy val txInstant : Ns3[A, B, Date   ] with OneDate   [Ns3[A, B, Date   ], In3[Date   , A, B, Date   ]] = ???
  lazy val op        : Ns3[A, B, Boolean] with OneBoolean[Ns3[A, B, Boolean], In3[Boolean, A, B, Boolean]] = ???

  lazy val e_         : Ns2[A, B] with OneLong   [Ns2[A, B], In2[B, A, B]] = ???
  lazy val a_         : Ns2[A, B] with OneString [Ns2[A, B], In2[B, A, B]] = ???
  lazy val v_         : Ns2[A, B] with OneAny    [Ns2[A, B], In2[B, A, B]] = ???
  lazy val ns_        : Ns2[A, B] with OneString [Ns2[A, B], In2[B, A, B]] = ???
  lazy val tx_        : Ns2[A, B] with OneLong   [Ns2[A, B], In2[B, A, B]] = ???
  lazy val txT_       : Ns2[A, B] with OneLong   [Ns2[A, B], In2[B, A, B]] = ???
  lazy val txInstant_ : Ns2[A, B] with OneDate   [Ns2[A, B], In2[B, A, B]] = ???
  lazy val op_        : Ns2[A, B] with OneBoolean[Ns2[A, B], In2[B, A, B]] = ???
}


trait Generic3[Ns3[_,_,_], Ns4[_,_,_,_], In3[_,_,_,_], In4[_,_,_,_,_], A, B, C] {
  lazy val e         : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
  lazy val a         : Ns4[A, B, C, String ] with OneString [Ns4[A, B, C, String ], In4[String , A, B, C, String ]] = ???
  lazy val v         : Ns4[A, B, C, Any    ] with OneAny    [Ns4[A, B, C, Any    ], In4[Any    , A, B, C, Any    ]] = ???
  lazy val ns        : Ns4[A, B, C, String ] with OneString [Ns4[A, B, C, String ], In4[String , A, B, C, String ]] = ???
  lazy val tx        : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
  lazy val txT       : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
  lazy val txInstant : Ns4[A, B, C, Date   ] with OneDate   [Ns4[A, B, C, Date   ], In4[Date   , A, B, C, Date   ]] = ???
  lazy val op        : Ns4[A, B, C, Boolean] with OneBoolean[Ns4[A, B, C, Boolean], In4[Boolean, A, B, C, Boolean]] = ???

  lazy val e_         : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
  lazy val a_         : Ns3[A, B, C] with OneString [Ns3[A, B, C], In3[String , A, B, C]] = ???
  lazy val v_         : Ns3[A, B, C] with OneAny    [Ns3[A, B, C], In3[Any    , A, B, C]] = ???
  lazy val ns_        : Ns3[A, B, C] with OneString [Ns3[A, B, C], In3[String , A, B, C]] = ???
  lazy val tx_        : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
  lazy val txT_       : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
  lazy val txInstant_ : Ns3[A, B, C] with OneDate   [Ns3[A, B, C], In3[Date   , A, B, C]] = ???
  lazy val op_        : Ns3[A, B, C] with OneBoolean[Ns3[A, B, C], In3[Boolean, A, B, C]] = ???
}


trait Generic4[Ns4[_,_,_,_], Ns5[_,_,_,_,_], In4[_,_,_,_,_], In5[_,_,_,_,_,_], A, B, C, D] {
  lazy val e         : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
  lazy val a         : Ns5[A, B, C, D, String ] with OneString [Ns5[A, B, C, D, String ], In5[String , A, B, C, D, String ]] = ???
  lazy val v         : Ns5[A, B, C, D, Any    ] with OneAny    [Ns5[A, B, C, D, Any    ], In5[Any    , A, B, C, D, Any    ]] = ???
  lazy val ns        : Ns5[A, B, C, D, String ] with OneString [Ns5[A, B, C, D, String ], In5[String , A, B, C, D, String ]] = ???
  lazy val tx        : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
  lazy val txT       : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
  lazy val txInstant : Ns5[A, B, C, D, Date   ] with OneDate   [Ns5[A, B, C, D, Date   ], In5[Date   , A, B, C, D, Date   ]] = ???
  lazy val op        : Ns5[A, B, C, D, Boolean] with OneBoolean[Ns5[A, B, C, D, Boolean], In5[Boolean, A, B, C, D, Boolean]] = ???

  lazy val e_         : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
  lazy val a_         : Ns4[A, B, C, D] with OneString [Ns4[A, B, C, D], In4[String , A, B, C, D]] = ???
  lazy val v_         : Ns4[A, B, C, D] with OneAny    [Ns4[A, B, C, D], In4[Any    , A, B, C, D]] = ???
  lazy val ns_        : Ns4[A, B, C, D] with OneString [Ns4[A, B, C, D], In4[String , A, B, C, D]] = ???
  lazy val tx_        : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
  lazy val txT_       : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
  lazy val txInstant_ : Ns4[A, B, C, D] with OneDate   [Ns4[A, B, C, D], In4[Date   , A, B, C, D]] = ???
  lazy val op_        : Ns4[A, B, C, D] with OneBoolean[Ns4[A, B, C, D], In4[Boolean, A, B, C, D]] = ???
}


trait Generic5[Ns5[_,_,_,_,_], Ns6[_,_,_,_,_,_], In5[_,_,_,_,_,_], In6[_,_,_,_,_,_,_], A, B, C, D, E] {

  lazy val e         : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
  lazy val a         : Ns6[A, B, C, D, E, String ] with OneString [Ns6[A, B, C, D, E, String ], In6[String , A, B, C, D, E, String ]] = ???
  lazy val v         : Ns6[A, B, C, D, E, Any    ] with OneAny    [Ns6[A, B, C, D, E, Any    ], In6[Any    , A, B, C, D, E, Any    ]] = ???
  lazy val ns        : Ns6[A, B, C, D, E, String ] with OneString [Ns6[A, B, C, D, E, String ], In6[String , A, B, C, D, E, String ]] = ???
  lazy val tx        : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
  lazy val txT       : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
  lazy val txInstant : Ns6[A, B, C, D, E, Date   ] with OneDate   [Ns6[A, B, C, D, E, Date   ], In6[Date   , A, B, C, D, E, Date   ]] = ???
  lazy val op        : Ns6[A, B, C, D, E, Boolean] with OneBoolean[Ns6[A, B, C, D, E, Boolean], In6[Boolean, A, B, C, D, E, Boolean]] = ???

  lazy val e_         : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
  lazy val a_         : Ns5[A, B, C, D, E] with OneString [Ns5[A, B, C, D, E], In5[String , A, B, C, D, E]] = ???
  lazy val v_         : Ns5[A, B, C, D, E] with OneAny    [Ns5[A, B, C, D, E], In5[Any    , A, B, C, D, E]] = ???
  lazy val ns_        : Ns5[A, B, C, D, E] with OneString [Ns5[A, B, C, D, E], In5[String , A, B, C, D, E]] = ???
  lazy val tx_        : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
  lazy val txT_       : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
  lazy val txInstant_ : Ns5[A, B, C, D, E] with OneDate   [Ns5[A, B, C, D, E], In5[Date   , A, B, C, D, E]] = ???
  lazy val op_        : Ns5[A, B, C, D, E] with OneBoolean[Ns5[A, B, C, D, E], In5[Boolean, A, B, C, D, E]] = ???
}


trait Generic6[Ns6[_,_,_,_,_,_], Ns7[_,_,_,_,_,_,_], In6[_,_,_,_,_,_,_], In7[_,_,_,_,_,_,_,_], A, B, C, D, E, F] {
  lazy val e         : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
  lazy val a         : Ns7[A, B, C, D, E, F, String ] with OneString [Ns7[A, B, C, D, E, F, String ], In7[String , A, B, C, D, E, F, String ]] = ???
  lazy val v         : Ns7[A, B, C, D, E, F, Any    ] with OneAny    [Ns7[A, B, C, D, E, F, Any    ], In7[Any    , A, B, C, D, E, F, Any    ]] = ???
  lazy val ns        : Ns7[A, B, C, D, E, F, String ] with OneString [Ns7[A, B, C, D, E, F, String ], In7[String , A, B, C, D, E, F, String ]] = ???
  lazy val tx        : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
  lazy val txT       : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
  lazy val txInstant : Ns7[A, B, C, D, E, F, Date   ] with OneDate   [Ns7[A, B, C, D, E, F, Date   ], In7[Date   , A, B, C, D, E, F, Date   ]] = ???
  lazy val op        : Ns7[A, B, C, D, E, F, Boolean] with OneBoolean[Ns7[A, B, C, D, E, F, Boolean], In7[Boolean, A, B, C, D, E, F, Boolean]] = ???

  lazy val e_         : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
  lazy val a_         : Ns6[A, B, C, D, E, F] with OneString [Ns6[A, B, C, D, E, F], In6[String , A, B, C, D, E, F]] = ???
  lazy val v_         : Ns6[A, B, C, D, E, F] with OneAny    [Ns6[A, B, C, D, E, F], In6[Any    , A, B, C, D, E, F]] = ???
  lazy val ns_        : Ns6[A, B, C, D, E, F] with OneString [Ns6[A, B, C, D, E, F], In6[String , A, B, C, D, E, F]] = ???
  lazy val tx_        : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
  lazy val txT_       : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
  lazy val txInstant_ : Ns6[A, B, C, D, E, F] with OneDate   [Ns6[A, B, C, D, E, F], In6[Date   , A, B, C, D, E, F]] = ???
  lazy val op_        : Ns6[A, B, C, D, E, F] with OneBoolean[Ns6[A, B, C, D, E, F], In6[Boolean, A, B, C, D, E, F]] = ???
}


trait Generic7[Ns7[_,_,_,_,_,_,_], Ns8[_,_,_,_,_,_,_,_], In7[_,_,_,_,_,_,_,_], In8[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G] {
  lazy val e         : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
  lazy val a         : Ns8[A, B, C, D, E, F, G, String ] with OneString [Ns8[A, B, C, D, E, F, G, String ], In8[String , A, B, C, D, E, F, G, String ]] = ???
  lazy val v         : Ns8[A, B, C, D, E, F, G, Any    ] with OneAny    [Ns8[A, B, C, D, E, F, G, Any    ], In8[Any    , A, B, C, D, E, F, G, Any    ]] = ???
  lazy val ns        : Ns8[A, B, C, D, E, F, G, String ] with OneString [Ns8[A, B, C, D, E, F, G, String ], In8[String , A, B, C, D, E, F, G, String ]] = ???
  lazy val tx        : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
  lazy val txT       : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
  lazy val txInstant : Ns8[A, B, C, D, E, F, G, Date   ] with OneDate   [Ns8[A, B, C, D, E, F, G, Date   ], In8[Date   , A, B, C, D, E, F, G, Date   ]] = ???
  lazy val op        : Ns8[A, B, C, D, E, F, G, Boolean] with OneBoolean[Ns8[A, B, C, D, E, F, G, Boolean], In8[Boolean, A, B, C, D, E, F, G, Boolean]] = ???

  lazy val e_         : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
  lazy val a_         : Ns7[A, B, C, D, E, F, G] with OneString [Ns7[A, B, C, D, E, F, G], In7[String , A, B, C, D, E, F, G]] = ???
  lazy val v_         : Ns7[A, B, C, D, E, F, G] with OneAny    [Ns7[A, B, C, D, E, F, G], In7[Any    , A, B, C, D, E, F, G]] = ???
  lazy val ns_        : Ns7[A, B, C, D, E, F, G] with OneString [Ns7[A, B, C, D, E, F, G], In7[String , A, B, C, D, E, F, G]] = ???
  lazy val tx_        : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
  lazy val txT_       : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
  lazy val txInstant_ : Ns7[A, B, C, D, E, F, G] with OneDate   [Ns7[A, B, C, D, E, F, G], In7[Date   , A, B, C, D, E, F, G]] = ???
  lazy val op_        : Ns7[A, B, C, D, E, F, G] with OneBoolean[Ns7[A, B, C, D, E, F, G], In7[Boolean, A, B, C, D, E, F, G]] = ???
}


trait Generic8[Ns8[_,_,_,_,_,_,_,_], Ns9[_,_,_,_,_,_,_,_,_], In8[_,_,_,_,_,_,_,_,_], In9[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {
  lazy val e         : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
  lazy val a         : Ns9[A, B, C, D, E, F, G, H, String ] with OneString [Ns9[A, B, C, D, E, F, G, H, String ], In9[String , A, B, C, D, E, F, G, H, String ]] = ???
  lazy val v         : Ns9[A, B, C, D, E, F, G, H, Any    ] with OneAny    [Ns9[A, B, C, D, E, F, G, H, Any    ], In9[Any    , A, B, C, D, E, F, G, H, Any    ]] = ???
  lazy val ns        : Ns9[A, B, C, D, E, F, G, H, String ] with OneString [Ns9[A, B, C, D, E, F, G, H, String ], In9[String , A, B, C, D, E, F, G, H, String ]] = ???
  lazy val tx        : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
  lazy val txT       : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
  lazy val txInstant : Ns9[A, B, C, D, E, F, G, H, Date   ] with OneDate   [Ns9[A, B, C, D, E, F, G, H, Date   ], In9[Date   , A, B, C, D, E, F, G, H, Date   ]] = ???
  lazy val op        : Ns9[A, B, C, D, E, F, G, H, Boolean] with OneBoolean[Ns9[A, B, C, D, E, F, G, H, Boolean], In9[Boolean, A, B, C, D, E, F, G, H, Boolean]] = ???

  lazy val e_         : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
  lazy val a_         : Ns8[A, B, C, D, E, F, G, H] with OneString [Ns8[A, B, C, D, E, F, G, H], In8[String , A, B, C, D, E, F, G, H]] = ???
  lazy val v_         : Ns8[A, B, C, D, E, F, G, H] with OneAny    [Ns8[A, B, C, D, E, F, G, H], In8[Any    , A, B, C, D, E, F, G, H]] = ???
  lazy val ns_        : Ns8[A, B, C, D, E, F, G, H] with OneString [Ns8[A, B, C, D, E, F, G, H], In8[String , A, B, C, D, E, F, G, H]] = ???
  lazy val tx_        : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
  lazy val txT_       : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
  lazy val txInstant_ : Ns8[A, B, C, D, E, F, G, H] with OneDate   [Ns8[A, B, C, D, E, F, G, H], In8[Date   , A, B, C, D, E, F, G, H]] = ???
  lazy val op_        : Ns8[A, B, C, D, E, F, G, H] with OneBoolean[Ns8[A, B, C, D, E, F, G, H], In8[Boolean, A, B, C, D, E, F, G, H]] = ???
}


trait Generic9[Ns9[_,_,_,_,_,_,_,_,_], Ns10[_,_,_,_,_,_,_,_,_,_], In9[_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {
  lazy val e         : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
  lazy val a         : Ns10[A, B, C, D, E, F, G, H, I, String ] with OneString [Ns10[A, B, C, D, E, F, G, H, I, String ], In10[String , A, B, C, D, E, F, G, H, I, String ]] = ???
  lazy val v         : Ns10[A, B, C, D, E, F, G, H, I, Any    ] with OneAny    [Ns10[A, B, C, D, E, F, G, H, I, Any    ], In10[Any    , A, B, C, D, E, F, G, H, I, Any    ]] = ???
  lazy val ns        : Ns10[A, B, C, D, E, F, G, H, I, String ] with OneString [Ns10[A, B, C, D, E, F, G, H, I, String ], In10[String , A, B, C, D, E, F, G, H, I, String ]] = ???
  lazy val tx        : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
  lazy val txT       : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
  lazy val txInstant : Ns10[A, B, C, D, E, F, G, H, I, Date   ] with OneDate   [Ns10[A, B, C, D, E, F, G, H, I, Date   ], In10[Date   , A, B, C, D, E, F, G, H, I, Date   ]] = ???
  lazy val op        : Ns10[A, B, C, D, E, F, G, H, I, Boolean] with OneBoolean[Ns10[A, B, C, D, E, F, G, H, I, Boolean], In10[Boolean, A, B, C, D, E, F, G, H, I, Boolean]] = ???

  lazy val e_         : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
  lazy val a_         : Ns9[A, B, C, D, E, F, G, H, I] with OneString [Ns9[A, B, C, D, E, F, G, H, I], In9[String , A, B, C, D, E, F, G, H, I]] = ???
  lazy val v_         : Ns9[A, B, C, D, E, F, G, H, I] with OneAny    [Ns9[A, B, C, D, E, F, G, H, I], In9[Any    , A, B, C, D, E, F, G, H, I]] = ???
  lazy val ns_        : Ns9[A, B, C, D, E, F, G, H, I] with OneString [Ns9[A, B, C, D, E, F, G, H, I], In9[String , A, B, C, D, E, F, G, H, I]] = ???
  lazy val tx_        : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
  lazy val txT_       : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
  lazy val txInstant_ : Ns9[A, B, C, D, E, F, G, H, I] with OneDate   [Ns9[A, B, C, D, E, F, G, H, I], In9[Date   , A, B, C, D, E, F, G, H, I]] = ???
  lazy val op_        : Ns9[A, B, C, D, E, F, G, H, I] with OneBoolean[Ns9[A, B, C, D, E, F, G, H, I], In9[Boolean, A, B, C, D, E, F, G, H, I]] = ???
}


trait Generic10[Ns10[_,_,_,_,_,_,_,_,_,_], Ns11[_,_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {
  lazy val e         : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
  lazy val a         : Ns11[A, B, C, D, E, F, G, H, I, J, String ] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, String ], In11[String , A, B, C, D, E, F, G, H, I, J, String ]] = ???
  lazy val v         : Ns11[A, B, C, D, E, F, G, H, I, J, Any    ] with OneAny    [Ns11[A, B, C, D, E, F, G, H, I, J, Any    ], In11[Any    , A, B, C, D, E, F, G, H, I, J, Any    ]] = ???
  lazy val ns        : Ns11[A, B, C, D, E, F, G, H, I, J, String ] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, String ], In11[String , A, B, C, D, E, F, G, H, I, J, String ]] = ???
  lazy val tx        : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
  lazy val txT       : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
  lazy val txInstant : Ns11[A, B, C, D, E, F, G, H, I, J, Date   ] with OneDate   [Ns11[A, B, C, D, E, F, G, H, I, J, Date   ], In11[Date   , A, B, C, D, E, F, G, H, I, J, Date   ]] = ???
  lazy val op        : Ns11[A, B, C, D, E, F, G, H, I, J, Boolean] with OneBoolean[Ns11[A, B, C, D, E, F, G, H, I, J, Boolean], In11[Boolean, A, B, C, D, E, F, G, H, I, J, Boolean]] = ???

  lazy val e_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val a_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneString [Ns10[A, B, C, D, E, F, G, H, I, J], In10[String , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val v_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneAny    [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Any    , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val ns_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneString [Ns10[A, B, C, D, E, F, G, H, I, J], In10[String , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val tx_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val txT_       : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val txInstant_ : Ns10[A, B, C, D, E, F, G, H, I, J] with OneDate   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Date   , A, B, C, D, E, F, G, H, I, J]] = ???
  lazy val op_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneBoolean[Ns10[A, B, C, D, E, F, G, H, I, J], In10[Boolean, A, B, C, D, E, F, G, H, I, J]] = ???
}


trait Generic11[Ns11[_,_,_,_,_,_,_,_,_,_,_], Ns12[_,_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {
  lazy val e         : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
  lazy val a         : Ns12[A, B, C, D, E, F, G, H, I, J, K, String ] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, String ], In12[String , A, B, C, D, E, F, G, H, I, J, K, String ]] = ???
  lazy val v         : Ns12[A, B, C, D, E, F, G, H, I, J, K, Any    ] with OneAny    [Ns12[A, B, C, D, E, F, G, H, I, J, K, Any    ], In12[Any    , A, B, C, D, E, F, G, H, I, J, K, Any    ]] = ???
  lazy val ns        : Ns12[A, B, C, D, E, F, G, H, I, J, K, String ] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, String ], In12[String , A, B, C, D, E, F, G, H, I, J, K, String ]] = ???
  lazy val tx        : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
  lazy val txT       : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
  lazy val txInstant : Ns12[A, B, C, D, E, F, G, H, I, J, K, Date   ] with OneDate   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Date   ], In12[Date   , A, B, C, D, E, F, G, H, I, J, K, Date   ]] = ???
  lazy val op        : Ns12[A, B, C, D, E, F, G, H, I, J, K, Boolean] with OneBoolean[Ns12[A, B, C, D, E, F, G, H, I, J, K, Boolean], In12[Boolean, A, B, C, D, E, F, G, H, I, J, K, Boolean]] = ???

  lazy val e_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val a_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[String , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val v_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneAny    [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Any    , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val ns_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[String , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val tx_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val txT_       : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val txInstant_ : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneDate   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Date   , A, B, C, D, E, F, G, H, I, J, K]] = ???
  lazy val op_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneBoolean[Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Boolean, A, B, C, D, E, F, G, H, I, J, K]] = ???
}


trait Generic12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_], In13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {
  lazy val e         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
  lazy val a         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, String ]] = ???
  lazy val v         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Any    ] with OneAny    [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Any    ], In13[Any    , A, B, C, D, E, F, G, H, I, J, K, L, Any    ]] = ???
  lazy val ns        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, String ]] = ???
  lazy val tx        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
  lazy val txT       : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
  lazy val txInstant : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Date   ] with OneDate   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Date   ], In13[Date   , A, B, C, D, E, F, G, H, I, J, K, L, Date   ]] = ???
  lazy val op        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Boolean] with OneBoolean[Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Boolean], In13[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, Boolean]] = ???

  lazy val e_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val a_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[String , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val v_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneAny    [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Any    , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val ns_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[String , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val tx_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val txT_       : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val txInstant_ : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneDate   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Date   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  lazy val op_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneBoolean[Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Boolean, A, B, C, D, E, F, G, H, I, J, K, L]] = ???
}


trait Generic13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {
  lazy val e         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
  lazy val a         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, String ]] = ???
  lazy val v         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ] with OneAny    [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ], In14[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ]] = ???
  lazy val ns        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, String ]] = ???
  lazy val tx        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
  lazy val txT       : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
  lazy val txInstant : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ] with OneDate   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ], In14[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ]] = ???
  lazy val op        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean] with OneBoolean[Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean], In14[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean]] = ???

  lazy val e_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val a_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val v_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneAny    [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val ns_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val tx_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val txT_       : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val txInstant_ : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneDate   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  lazy val op_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneBoolean[Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
}


trait Generic14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {
  lazy val e         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
  lazy val a         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ]] = ???
  lazy val v         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ] with OneAny    [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ], In15[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ]] = ???
  lazy val ns        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ]] = ???
  lazy val tx        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
  lazy val txT       : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
  lazy val txInstant : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ] with OneDate   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ], In15[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ]] = ???
  lazy val op        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean] with OneBoolean[Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean], In15[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean]] = ???

  lazy val e_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val a_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val v_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneAny    [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val ns_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val tx_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val txT_       : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val txInstant_ : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneDate   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  lazy val op_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneBoolean[Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
}


trait Generic15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  lazy val e         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
  lazy val a         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ]] = ???
  lazy val v         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ] with OneAny    [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ], In16[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ]] = ???
  lazy val ns        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ]] = ???
  lazy val tx        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
  lazy val txT       : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
  lazy val txInstant : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ] with OneDate   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ], In16[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ]] = ???
  lazy val op        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean] with OneBoolean[Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean], In16[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean]] = ???

  lazy val e_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val a_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val v_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneAny    [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val ns_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val tx_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val txT_       : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val txInstant_ : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneDate   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  lazy val op_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneBoolean[Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
}


trait Generic16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {
  lazy val e         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
  lazy val a         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ]] = ???
  lazy val v         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ] with OneAny    [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ], In17[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ]] = ???
  lazy val ns        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ]] = ???
  lazy val tx        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
  lazy val txT       : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
  lazy val txInstant : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ] with OneDate   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ], In17[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ]] = ???
  lazy val op        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean] with OneBoolean[Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean], In17[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean]] = ???

  lazy val e_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val a_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val v_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneAny    [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val ns_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val tx_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val txT_       : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val txInstant_ : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneDate   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  lazy val op_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneBoolean[Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
}


trait Generic17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {
  lazy val e         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
  lazy val a         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ]] = ???
  lazy val v         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ] with OneAny    [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ], In18[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ]] = ???
  lazy val ns        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ]] = ???
  lazy val tx        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
  lazy val txT       : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
  lazy val txInstant : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ] with OneDate   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ], In18[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ]] = ???
  lazy val op        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean] with OneBoolean[Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean], In18[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean]] = ???

  lazy val e_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val a_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val v_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneAny    [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val ns_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val tx_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val txT_       : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val txInstant_ : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneDate   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  lazy val op_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneBoolean[Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
}


trait Generic18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {
  lazy val e         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
  lazy val a         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ]] = ???
  lazy val v         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ] with OneAny    [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ], In19[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ]] = ???
  lazy val ns        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ]] = ???
  lazy val tx        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
  lazy val txT       : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
  lazy val txInstant : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ] with OneDate   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ], In19[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ]] = ???
  lazy val op        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean] with OneBoolean[Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean], In19[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean]] = ???

  lazy val e_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val a_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val v_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneAny    [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val ns_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val tx_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val txT_       : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val txInstant_ : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneDate   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  lazy val op_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneBoolean[Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
}


trait Generic19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {
  lazy val e         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
  lazy val a         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ]] = ???
  lazy val v         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ] with OneAny    [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ], In20[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ]] = ???
  lazy val ns        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ]] = ???
  lazy val tx        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
  lazy val txT       : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
  lazy val txInstant : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ] with OneDate   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ], In20[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ]] = ???
  lazy val op        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean] with OneBoolean[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean], In20[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean]] = ???

  lazy val e_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val a_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val v_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneAny    [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val ns_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val tx_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val txT_       : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val txInstant_ : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneDate   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  lazy val op_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneBoolean[Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
}


trait Generic20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {
  lazy val e         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
  lazy val a         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ]] = ???
  lazy val v         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ] with OneAny    [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ], In21[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ]] = ???
  lazy val ns        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ]] = ???
  lazy val tx        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
  lazy val txT       : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
  lazy val txInstant : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ] with OneDate   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ], In21[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ]] = ???
  lazy val op        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean] with OneBoolean[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean], In21[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean]] = ???

  lazy val e_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val a_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val v_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneAny    [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val ns_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val tx_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val txT_       : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val txInstant_ : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneDate   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  lazy val op_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneBoolean[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
}


trait Generic21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {
  lazy val e         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
  lazy val a         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ]] = ???
  lazy val v         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ] with OneAny    [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ], In22[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ]] = ???
  lazy val ns        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ]] = ???
  lazy val tx        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
  lazy val txT       : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
  lazy val txInstant : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ] with OneDate   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ], In22[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ]] = ???
  lazy val op        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean] with OneBoolean[Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean], In22[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean]] = ???

  lazy val e_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val a_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val v_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneAny    [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val ns_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val tx_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val txT_       : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val txInstant_ : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneDate   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  lazy val op_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneBoolean[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
}


trait Generic22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {
  lazy val e_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val a_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val v_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneAny    [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val ns_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val tx_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val txT_       : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val txInstant_ : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneDate   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  lazy val op_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneBoolean[Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
}