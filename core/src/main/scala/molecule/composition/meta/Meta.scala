package molecule.composition.meta

import java.util.Date
import molecule.boilerplate.attributes._
import molecule.composition.branch.Tx._
//import molecule.composition.branch.Branch.{Branch01, Branch03}
import scala.language.higherKinds

trait Meta

object Meta {

  trait Meta00[Ns0, Ns1[_], In0[_], In1[_, _]] extends Meta {

  }


  trait Meta01[Ns1[_], Ns2[_,_], In1[_,_], In2[_,_,_], A] extends Meta {
    def e          : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
    def a          : Ns2[A, String ] with OneString [Ns2[A, String ], In2[String , A, String ]] = ???
    def v          : Ns2[A, Any    ] with OneAny    [Ns2[A, Any    ], In2[Any    , A, Any    ]] = ???
    def ns         : Ns2[A, String ] with OneString [Ns2[A, String ], In2[String , A, String ]] = ???
    def tx         : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
    def t          : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
    def txInstant  : Ns2[A, Date   ] with OneDate   [Ns2[A, Date   ], In2[Date   , A, Date   ]] = ???
    def op         : Ns2[A, Boolean] with OneBoolean[Ns2[A, Boolean], In2[Boolean, A, Boolean]] = ???

    def e_         : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
    def a_         : Ns1[A] with OneString [Ns1[A], In1[String , A]] = ???
    def v_         : Ns1[A] with OneAny    [Ns1[A], In1[Any    , A]] = ???
    def ns_        : Ns1[A] with OneString [Ns1[A], In1[String , A]] = ???
    def tx_        : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
    def t_         : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
    def txInstant_ : Ns1[A] with OneDate   [Ns1[A], In1[Date   , A]] = ???
    def op_        : Ns1[A] with OneBoolean[Ns1[A], In1[Boolean, A]] = ???

//    object Tx extends Tx02[A]
//    object Tx_ extends Tx01[A]
  }


  trait Meta02[Ns2[_,_], Ns3[_,_,_], In2[_,_,_], In3[_,_,_,_], A, B] extends Meta {
    def e          : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
    def a          : Ns3[A, B, String ] with OneString [Ns3[A, B, String ], In3[String , A, B, String ]] = ???
    def v          : Ns3[A, B, Any    ] with OneAny    [Ns3[A, B, Any    ], In3[Any    , A, B, Any    ]] = ???
    def ns         : Ns3[A, B, String ] with OneString [Ns3[A, B, String ], In3[String , A, B, String ]] = ???
    def tx         : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
    def t          : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
    def txInstant  : Ns3[A, B, Date   ] with OneDate   [Ns3[A, B, Date   ], In3[Date   , A, B, Date   ]] = ???
    def op         : Ns3[A, B, Boolean] with OneBoolean[Ns3[A, B, Boolean], In3[Boolean, A, B, Boolean]] = ???

    def e_         : Ns2[A, B] with OneLong   [Ns2[A, B], In2[Long   , A, B]] = ???
    def a_         : Ns2[A, B] with OneString [Ns2[A, B], In2[String , A, B]] = ???
    def v_         : Ns2[A, B] with OneAny    [Ns2[A, B], In2[Any    , A, B]] = ???
    def ns_        : Ns2[A, B] with OneString [Ns2[A, B], In2[String , A, B]] = ???
    def tx_        : Ns2[A, B] with OneLong   [Ns2[A, B], In2[Long   , A, B]] = ???
    def t_         : Ns2[A, B] with OneLong   [Ns2[A, B], In2[Long   , A, B]] = ???
    def txInstant_ : Ns2[A, B] with OneDate   [Ns2[A, B], In2[Date   , A, B]] = ???
    def op_        : Ns2[A, B] with OneBoolean[Ns2[A, B], In2[Boolean, A, B]] = ???

//    object Tx extends Tx02[Ns1, Ns2, A]
  }


  trait Meta03[Ns3[_,_,_], Ns4[_,_,_,_], In3[_,_,_,_], In4[_,_,_,_,_], A, B, C] extends Meta {
    def e          : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
    def a          : Ns4[A, B, C, String ] with OneString [Ns4[A, B, C, String ], In4[String , A, B, C, String ]] = ???
    def v          : Ns4[A, B, C, Any    ] with OneAny    [Ns4[A, B, C, Any    ], In4[Any    , A, B, C, Any    ]] = ???
    def ns         : Ns4[A, B, C, String ] with OneString [Ns4[A, B, C, String ], In4[String , A, B, C, String ]] = ???
    def tx         : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
    def t          : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
    def txInstant  : Ns4[A, B, C, Date   ] with OneDate   [Ns4[A, B, C, Date   ], In4[Date   , A, B, C, Date   ]] = ???
    def op         : Ns4[A, B, C, Boolean] with OneBoolean[Ns4[A, B, C, Boolean], In4[Boolean, A, B, C, Boolean]] = ???

    def e_         : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
    def a_         : Ns3[A, B, C] with OneString [Ns3[A, B, C], In3[String , A, B, C]] = ???
    def v_         : Ns3[A, B, C] with OneAny    [Ns3[A, B, C], In3[Any    , A, B, C]] = ???
    def ns_        : Ns3[A, B, C] with OneString [Ns3[A, B, C], In3[String , A, B, C]] = ???
    def tx_        : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
    def t_         : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
    def txInstant_ : Ns3[A, B, C] with OneDate   [Ns3[A, B, C], In3[Date   , A, B, C]] = ???
    def op_        : Ns3[A, B, C] with OneBoolean[Ns3[A, B, C], In3[Boolean, A, B, C]] = ???
  }


  trait Meta04[Ns4[_,_,_,_], Ns5[_,_,_,_,_], In4[_,_,_,_,_], In5[_,_,_,_,_,_], A, B, C, D] extends Meta {
    def e          : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
    def a          : Ns5[A, B, C, D, String ] with OneString [Ns5[A, B, C, D, String ], In5[String , A, B, C, D, String ]] = ???
    def v          : Ns5[A, B, C, D, Any    ] with OneAny    [Ns5[A, B, C, D, Any    ], In5[Any    , A, B, C, D, Any    ]] = ???
    def ns         : Ns5[A, B, C, D, String ] with OneString [Ns5[A, B, C, D, String ], In5[String , A, B, C, D, String ]] = ???
    def tx         : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
    def t          : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
    def txInstant  : Ns5[A, B, C, D, Date   ] with OneDate   [Ns5[A, B, C, D, Date   ], In5[Date   , A, B, C, D, Date   ]] = ???
    def op         : Ns5[A, B, C, D, Boolean] with OneBoolean[Ns5[A, B, C, D, Boolean], In5[Boolean, A, B, C, D, Boolean]] = ???

    def e_         : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
    def a_         : Ns4[A, B, C, D] with OneString [Ns4[A, B, C, D], In4[String , A, B, C, D]] = ???
    def v_         : Ns4[A, B, C, D] with OneAny    [Ns4[A, B, C, D], In4[Any    , A, B, C, D]] = ???
    def ns_        : Ns4[A, B, C, D] with OneString [Ns4[A, B, C, D], In4[String , A, B, C, D]] = ???
    def tx_        : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
    def t_         : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
    def txInstant_ : Ns4[A, B, C, D] with OneDate   [Ns4[A, B, C, D], In4[Date   , A, B, C, D]] = ???
    def op_        : Ns4[A, B, C, D] with OneBoolean[Ns4[A, B, C, D], In4[Boolean, A, B, C, D]] = ???
  }


  trait Meta05[Ns5[_,_,_,_,_], Ns6[_,_,_,_,_,_], In5[_,_,_,_,_,_], In6[_,_,_,_,_,_,_], A, B, C, D, E] extends Meta {

    def e          : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
    def a          : Ns6[A, B, C, D, E, String ] with OneString [Ns6[A, B, C, D, E, String ], In6[String , A, B, C, D, E, String ]] = ???
    def v          : Ns6[A, B, C, D, E, Any    ] with OneAny    [Ns6[A, B, C, D, E, Any    ], In6[Any    , A, B, C, D, E, Any    ]] = ???
    def ns         : Ns6[A, B, C, D, E, String ] with OneString [Ns6[A, B, C, D, E, String ], In6[String , A, B, C, D, E, String ]] = ???
    def tx         : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
    def t          : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
    def txInstant  : Ns6[A, B, C, D, E, Date   ] with OneDate   [Ns6[A, B, C, D, E, Date   ], In6[Date   , A, B, C, D, E, Date   ]] = ???
    def op         : Ns6[A, B, C, D, E, Boolean] with OneBoolean[Ns6[A, B, C, D, E, Boolean], In6[Boolean, A, B, C, D, E, Boolean]] = ???

    def e_         : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
    def a_         : Ns5[A, B, C, D, E] with OneString [Ns5[A, B, C, D, E], In5[String , A, B, C, D, E]] = ???
    def v_         : Ns5[A, B, C, D, E] with OneAny    [Ns5[A, B, C, D, E], In5[Any    , A, B, C, D, E]] = ???
    def ns_        : Ns5[A, B, C, D, E] with OneString [Ns5[A, B, C, D, E], In5[String , A, B, C, D, E]] = ???
    def tx_        : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
    def t_         : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
    def txInstant_ : Ns5[A, B, C, D, E] with OneDate   [Ns5[A, B, C, D, E], In5[Date   , A, B, C, D, E]] = ???
    def op_        : Ns5[A, B, C, D, E] with OneBoolean[Ns5[A, B, C, D, E], In5[Boolean, A, B, C, D, E]] = ???
  }


  trait Meta06[Ns6[_,_,_,_,_,_], Ns7[_,_,_,_,_,_,_], In6[_,_,_,_,_,_,_], In7[_,_,_,_,_,_,_,_], A, B, C, D, E, F] extends Meta {
    def e          : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
    def a          : Ns7[A, B, C, D, E, F, String ] with OneString [Ns7[A, B, C, D, E, F, String ], In7[String , A, B, C, D, E, F, String ]] = ???
    def v          : Ns7[A, B, C, D, E, F, Any    ] with OneAny    [Ns7[A, B, C, D, E, F, Any    ], In7[Any    , A, B, C, D, E, F, Any    ]] = ???
    def ns         : Ns7[A, B, C, D, E, F, String ] with OneString [Ns7[A, B, C, D, E, F, String ], In7[String , A, B, C, D, E, F, String ]] = ???
    def tx         : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
    def t          : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
    def txInstant  : Ns7[A, B, C, D, E, F, Date   ] with OneDate   [Ns7[A, B, C, D, E, F, Date   ], In7[Date   , A, B, C, D, E, F, Date   ]] = ???
    def op         : Ns7[A, B, C, D, E, F, Boolean] with OneBoolean[Ns7[A, B, C, D, E, F, Boolean], In7[Boolean, A, B, C, D, E, F, Boolean]] = ???

    def e_         : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
    def a_         : Ns6[A, B, C, D, E, F] with OneString [Ns6[A, B, C, D, E, F], In6[String , A, B, C, D, E, F]] = ???
    def v_         : Ns6[A, B, C, D, E, F] with OneAny    [Ns6[A, B, C, D, E, F], In6[Any    , A, B, C, D, E, F]] = ???
    def ns_        : Ns6[A, B, C, D, E, F] with OneString [Ns6[A, B, C, D, E, F], In6[String , A, B, C, D, E, F]] = ???
    def tx_        : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
    def t_         : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
    def txInstant_ : Ns6[A, B, C, D, E, F] with OneDate   [Ns6[A, B, C, D, E, F], In6[Date   , A, B, C, D, E, F]] = ???
    def op_        : Ns6[A, B, C, D, E, F] with OneBoolean[Ns6[A, B, C, D, E, F], In6[Boolean, A, B, C, D, E, F]] = ???
  }


  trait Meta07[Ns7[_,_,_,_,_,_,_], Ns8[_,_,_,_,_,_,_,_], In7[_,_,_,_,_,_,_,_], In8[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G] extends Meta {
    def e          : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
    def a          : Ns8[A, B, C, D, E, F, G, String ] with OneString [Ns8[A, B, C, D, E, F, G, String ], In8[String , A, B, C, D, E, F, G, String ]] = ???
    def v          : Ns8[A, B, C, D, E, F, G, Any    ] with OneAny    [Ns8[A, B, C, D, E, F, G, Any    ], In8[Any    , A, B, C, D, E, F, G, Any    ]] = ???
    def ns         : Ns8[A, B, C, D, E, F, G, String ] with OneString [Ns8[A, B, C, D, E, F, G, String ], In8[String , A, B, C, D, E, F, G, String ]] = ???
    def tx         : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
    def t          : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
    def txInstant  : Ns8[A, B, C, D, E, F, G, Date   ] with OneDate   [Ns8[A, B, C, D, E, F, G, Date   ], In8[Date   , A, B, C, D, E, F, G, Date   ]] = ???
    def op         : Ns8[A, B, C, D, E, F, G, Boolean] with OneBoolean[Ns8[A, B, C, D, E, F, G, Boolean], In8[Boolean, A, B, C, D, E, F, G, Boolean]] = ???

    def e_         : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
    def a_         : Ns7[A, B, C, D, E, F, G] with OneString [Ns7[A, B, C, D, E, F, G], In7[String , A, B, C, D, E, F, G]] = ???
    def v_         : Ns7[A, B, C, D, E, F, G] with OneAny    [Ns7[A, B, C, D, E, F, G], In7[Any    , A, B, C, D, E, F, G]] = ???
    def ns_        : Ns7[A, B, C, D, E, F, G] with OneString [Ns7[A, B, C, D, E, F, G], In7[String , A, B, C, D, E, F, G]] = ???
    def tx_        : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
    def t_         : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
    def txInstant_ : Ns7[A, B, C, D, E, F, G] with OneDate   [Ns7[A, B, C, D, E, F, G], In7[Date   , A, B, C, D, E, F, G]] = ???
    def op_        : Ns7[A, B, C, D, E, F, G] with OneBoolean[Ns7[A, B, C, D, E, F, G], In7[Boolean, A, B, C, D, E, F, G]] = ???
  }


  trait Meta08[Ns8[_,_,_,_,_,_,_,_], Ns9[_,_,_,_,_,_,_,_,_], In8[_,_,_,_,_,_,_,_,_], In9[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] extends Meta {
    def e          : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
    def a          : Ns9[A, B, C, D, E, F, G, H, String ] with OneString [Ns9[A, B, C, D, E, F, G, H, String ], In9[String , A, B, C, D, E, F, G, H, String ]] = ???
    def v          : Ns9[A, B, C, D, E, F, G, H, Any    ] with OneAny    [Ns9[A, B, C, D, E, F, G, H, Any    ], In9[Any    , A, B, C, D, E, F, G, H, Any    ]] = ???
    def ns         : Ns9[A, B, C, D, E, F, G, H, String ] with OneString [Ns9[A, B, C, D, E, F, G, H, String ], In9[String , A, B, C, D, E, F, G, H, String ]] = ???
    def tx         : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
    def t          : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
    def txInstant  : Ns9[A, B, C, D, E, F, G, H, Date   ] with OneDate   [Ns9[A, B, C, D, E, F, G, H, Date   ], In9[Date   , A, B, C, D, E, F, G, H, Date   ]] = ???
    def op         : Ns9[A, B, C, D, E, F, G, H, Boolean] with OneBoolean[Ns9[A, B, C, D, E, F, G, H, Boolean], In9[Boolean, A, B, C, D, E, F, G, H, Boolean]] = ???

    def e_         : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
    def a_         : Ns8[A, B, C, D, E, F, G, H] with OneString [Ns8[A, B, C, D, E, F, G, H], In8[String , A, B, C, D, E, F, G, H]] = ???
    def v_         : Ns8[A, B, C, D, E, F, G, H] with OneAny    [Ns8[A, B, C, D, E, F, G, H], In8[Any    , A, B, C, D, E, F, G, H]] = ???
    def ns_        : Ns8[A, B, C, D, E, F, G, H] with OneString [Ns8[A, B, C, D, E, F, G, H], In8[String , A, B, C, D, E, F, G, H]] = ???
    def tx_        : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
    def t_         : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
    def txInstant_ : Ns8[A, B, C, D, E, F, G, H] with OneDate   [Ns8[A, B, C, D, E, F, G, H], In8[Date   , A, B, C, D, E, F, G, H]] = ???
    def op_        : Ns8[A, B, C, D, E, F, G, H] with OneBoolean[Ns8[A, B, C, D, E, F, G, H], In8[Boolean, A, B, C, D, E, F, G, H]] = ???
  }


  trait Meta09[Ns9[_,_,_,_,_,_,_,_,_], Ns10[_,_,_,_,_,_,_,_,_,_], In9[_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] extends Meta {
    def e          : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
    def a          : Ns10[A, B, C, D, E, F, G, H, I, String ] with OneString [Ns10[A, B, C, D, E, F, G, H, I, String ], In10[String , A, B, C, D, E, F, G, H, I, String ]] = ???
    def v          : Ns10[A, B, C, D, E, F, G, H, I, Any    ] with OneAny    [Ns10[A, B, C, D, E, F, G, H, I, Any    ], In10[Any    , A, B, C, D, E, F, G, H, I, Any    ]] = ???
    def ns         : Ns10[A, B, C, D, E, F, G, H, I, String ] with OneString [Ns10[A, B, C, D, E, F, G, H, I, String ], In10[String , A, B, C, D, E, F, G, H, I, String ]] = ???
    def tx         : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
    def t          : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
    def txInstant  : Ns10[A, B, C, D, E, F, G, H, I, Date   ] with OneDate   [Ns10[A, B, C, D, E, F, G, H, I, Date   ], In10[Date   , A, B, C, D, E, F, G, H, I, Date   ]] = ???
    def op         : Ns10[A, B, C, D, E, F, G, H, I, Boolean] with OneBoolean[Ns10[A, B, C, D, E, F, G, H, I, Boolean], In10[Boolean, A, B, C, D, E, F, G, H, I, Boolean]] = ???

    def e_         : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
    def a_         : Ns9[A, B, C, D, E, F, G, H, I] with OneString [Ns9[A, B, C, D, E, F, G, H, I], In9[String , A, B, C, D, E, F, G, H, I]] = ???
    def v_         : Ns9[A, B, C, D, E, F, G, H, I] with OneAny    [Ns9[A, B, C, D, E, F, G, H, I], In9[Any    , A, B, C, D, E, F, G, H, I]] = ???
    def ns_        : Ns9[A, B, C, D, E, F, G, H, I] with OneString [Ns9[A, B, C, D, E, F, G, H, I], In9[String , A, B, C, D, E, F, G, H, I]] = ???
    def tx_        : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
    def t_         : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
    def txInstant_ : Ns9[A, B, C, D, E, F, G, H, I] with OneDate   [Ns9[A, B, C, D, E, F, G, H, I], In9[Date   , A, B, C, D, E, F, G, H, I]] = ???
    def op_        : Ns9[A, B, C, D, E, F, G, H, I] with OneBoolean[Ns9[A, B, C, D, E, F, G, H, I], In9[Boolean, A, B, C, D, E, F, G, H, I]] = ???
  }


  trait Meta10[Ns10[_,_,_,_,_,_,_,_,_,_], Ns11[_,_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] extends Meta {
    def e          : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
    def a          : Ns11[A, B, C, D, E, F, G, H, I, J, String ] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, String ], In11[String , A, B, C, D, E, F, G, H, I, J, String ]] = ???
    def v          : Ns11[A, B, C, D, E, F, G, H, I, J, Any    ] with OneAny    [Ns11[A, B, C, D, E, F, G, H, I, J, Any    ], In11[Any    , A, B, C, D, E, F, G, H, I, J, Any    ]] = ???
    def ns         : Ns11[A, B, C, D, E, F, G, H, I, J, String ] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, String ], In11[String , A, B, C, D, E, F, G, H, I, J, String ]] = ???
    def tx         : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
    def t          : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
    def txInstant  : Ns11[A, B, C, D, E, F, G, H, I, J, Date   ] with OneDate   [Ns11[A, B, C, D, E, F, G, H, I, J, Date   ], In11[Date   , A, B, C, D, E, F, G, H, I, J, Date   ]] = ???
    def op         : Ns11[A, B, C, D, E, F, G, H, I, J, Boolean] with OneBoolean[Ns11[A, B, C, D, E, F, G, H, I, J, Boolean], In11[Boolean, A, B, C, D, E, F, G, H, I, J, Boolean]] = ???

    def e_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
    def a_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneString [Ns10[A, B, C, D, E, F, G, H, I, J], In10[String , A, B, C, D, E, F, G, H, I, J]] = ???
    def v_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneAny    [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Any    , A, B, C, D, E, F, G, H, I, J]] = ???
    def ns_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneString [Ns10[A, B, C, D, E, F, G, H, I, J], In10[String , A, B, C, D, E, F, G, H, I, J]] = ???
    def tx_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
    def t_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
    def txInstant_ : Ns10[A, B, C, D, E, F, G, H, I, J] with OneDate   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Date   , A, B, C, D, E, F, G, H, I, J]] = ???
    def op_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneBoolean[Ns10[A, B, C, D, E, F, G, H, I, J], In10[Boolean, A, B, C, D, E, F, G, H, I, J]] = ???
  }


  trait Meta11[Ns11[_,_,_,_,_,_,_,_,_,_,_], Ns12[_,_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] extends Meta {
    def e          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
    def a          : Ns12[A, B, C, D, E, F, G, H, I, J, K, String ] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, String ], In12[String , A, B, C, D, E, F, G, H, I, J, K, String ]] = ???
    def v          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Any    ] with OneAny    [Ns12[A, B, C, D, E, F, G, H, I, J, K, Any    ], In12[Any    , A, B, C, D, E, F, G, H, I, J, K, Any    ]] = ???
    def ns         : Ns12[A, B, C, D, E, F, G, H, I, J, K, String ] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, String ], In12[String , A, B, C, D, E, F, G, H, I, J, K, String ]] = ???
    def tx         : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
    def t          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
    def txInstant  : Ns12[A, B, C, D, E, F, G, H, I, J, K, Date   ] with OneDate   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Date   ], In12[Date   , A, B, C, D, E, F, G, H, I, J, K, Date   ]] = ???
    def op         : Ns12[A, B, C, D, E, F, G, H, I, J, K, Boolean] with OneBoolean[Ns12[A, B, C, D, E, F, G, H, I, J, K, Boolean], In12[Boolean, A, B, C, D, E, F, G, H, I, J, K, Boolean]] = ???

    def e_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def a_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[String , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def v_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneAny    [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Any    , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def ns_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[String , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def tx_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def t_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def txInstant_ : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneDate   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Date   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    def op_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneBoolean[Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Boolean, A, B, C, D, E, F, G, H, I, J, K]] = ???
  }


  trait Meta12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_], In13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] extends Meta {
    def e          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
    def a          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, String ]] = ???
    def v          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Any    ] with OneAny    [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Any    ], In13[Any    , A, B, C, D, E, F, G, H, I, J, K, L, Any    ]] = ???
    def ns         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, String ]] = ???
    def tx         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
    def t          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
    def txInstant  : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Date   ] with OneDate   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Date   ], In13[Date   , A, B, C, D, E, F, G, H, I, J, K, L, Date   ]] = ???
    def op         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Boolean] with OneBoolean[Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Boolean], In13[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, Boolean]] = ???

    def e_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def a_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[String , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def v_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneAny    [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Any    , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def ns_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[String , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def tx_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def t_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def txInstant_ : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneDate   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Date   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    def op_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneBoolean[Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Boolean, A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  }


  trait Meta13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] extends Meta {
    def e          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
    def a          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, String ]] = ???
    def v          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ] with OneAny    [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ], In14[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ]] = ???
    def ns         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, String ]] = ???
    def tx         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
    def t          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
    def txInstant  : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ] with OneDate   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ], In14[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ]] = ???
    def op         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean] with OneBoolean[Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean], In14[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean]] = ???

    def e_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def a_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def v_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneAny    [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def ns_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def tx_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def t_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def txInstant_ : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneDate   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    def op_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneBoolean[Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  }


  trait Meta14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends Meta {
    def e          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
    def a          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ]] = ???
    def v          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ] with OneAny    [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ], In15[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ]] = ???
    def ns         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ]] = ???
    def tx         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
    def t          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
    def txInstant  : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ] with OneDate   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ], In15[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ]] = ???
    def op         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean] with OneBoolean[Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean], In15[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean]] = ???

    def e_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def a_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def v_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneAny    [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def ns_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def tx_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def t_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def txInstant_ : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneDate   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    def op_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneBoolean[Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  }


  trait Meta15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends Meta {
    def e          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
    def a          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ]] = ???
    def v          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ] with OneAny    [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ], In16[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ]] = ???
    def ns         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ]] = ???
    def tx         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
    def t          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
    def txInstant  : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ] with OneDate   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ], In16[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ]] = ???
    def op         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean] with OneBoolean[Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean], In16[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean]] = ???

    def e_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def a_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def v_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneAny    [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def ns_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def tx_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def t_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def txInstant_ : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneDate   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    def op_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneBoolean[Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  }


  trait Meta16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends Meta {
    def e          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
    def a          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ]] = ???
    def v          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ] with OneAny    [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ], In17[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ]] = ???
    def ns         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ]] = ???
    def tx         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
    def t          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
    def txInstant  : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ] with OneDate   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ], In17[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ]] = ???
    def op         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean] with OneBoolean[Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean], In17[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean]] = ???

    def e_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def a_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def v_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneAny    [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def ns_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def tx_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def t_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def txInstant_ : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneDate   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    def op_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneBoolean[Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  }


  trait Meta17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends Meta {
    def e          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
    def a          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ]] = ???
    def v          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ] with OneAny    [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ], In18[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ]] = ???
    def ns         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ]] = ???
    def tx         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
    def t          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
    def txInstant  : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ] with OneDate   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ], In18[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ]] = ???
    def op         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean] with OneBoolean[Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean], In18[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean]] = ???

    def e_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def a_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def v_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneAny    [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def ns_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def tx_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def t_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def txInstant_ : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneDate   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    def op_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneBoolean[Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  }


  trait Meta18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends Meta {
    def e          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
    def a          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ]] = ???
    def v          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ] with OneAny    [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ], In19[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ]] = ???
    def ns         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ]] = ???
    def tx         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
    def t          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
    def txInstant  : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ] with OneDate   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ], In19[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ]] = ???
    def op         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean] with OneBoolean[Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean], In19[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean]] = ???

    def e_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def a_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def v_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneAny    [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def ns_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def tx_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def t_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def txInstant_ : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneDate   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    def op_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneBoolean[Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  }


  trait Meta19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends Meta {
    def e          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
    def a          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ]] = ???
    def v          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ] with OneAny    [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ], In20[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ]] = ???
    def ns         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ]] = ???
    def tx         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
    def t          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
    def txInstant  : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ] with OneDate   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ], In20[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ]] = ???
    def op         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean] with OneBoolean[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean], In20[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean]] = ???

    def e_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def a_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def v_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneAny    [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def ns_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def tx_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def t_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def txInstant_ : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneDate   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    def op_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneBoolean[Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  }


  trait Meta20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends Meta {
    def e          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
    def a          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ]] = ???
    def v          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ] with OneAny    [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ], In21[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ]] = ???
    def ns         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ]] = ???
    def tx         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
    def t          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
    def txInstant  : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ] with OneDate   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ], In21[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ]] = ???
    def op         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean] with OneBoolean[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean], In21[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean]] = ???

    def e_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def a_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def v_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneAny    [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def ns_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def tx_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def t_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def txInstant_ : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneDate   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    def op_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneBoolean[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  }


  trait Meta21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends Meta {
    def e          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
    def a          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ]] = ???
    def v          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ] with OneAny    [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ], In22[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ]] = ???
    def ns         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ]] = ???
    def tx         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
    def t          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
    def txInstant  : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ] with OneDate   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ], In22[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ]] = ???
    def op         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean] with OneBoolean[Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean], In22[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean]] = ???

    def e_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def a_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def v_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneAny    [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def ns_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def tx_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def t_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def txInstant_ : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneDate   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    def op_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneBoolean[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  }


  trait Meta22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends Meta {
    def e_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def a_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def v_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneAny    [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def ns_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def tx_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def t_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def txInstant_ : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneDate   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    def op_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneBoolean[Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  }
}