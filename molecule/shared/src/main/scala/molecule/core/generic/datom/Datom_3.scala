package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_3[o0[_], p0, Ns3[o[_],_,_,_,_], Ns4[o[_],_,_,_,_,_], In3[o[_],_,_,_,_,_], In4[o[_],_,_,_,_,_,_], A, B, C] extends Datom {

  final lazy val e          : OneLong   [Ns4[o0, p0 with Datom_e        , A, B, C, Long   ], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns4[o0, p0 with Datom_a        , A, B, C, String ], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns4[o0, p0 with Datom_v        , A, B, C, Any    ], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns4[o0, p0 with Datom_t        , A, B, C, Long   ], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns4[o0, p0 with Datom_tx       , A, B, C, Long   ], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns4[o0, p0 with Datom_txInstant, A, B, C, Date   ], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns4[o0, p0 with Datom_op       , A, B, C, Boolean], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0 with Datom_op       , A, B, C, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
  final lazy val a_         : OneString [Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny    [Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong   [Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns3[o0, p0, A, B, C], In3[o0,_,_,_,_,_]] with Ns3[o0, p0, A, B, C] with Indexed = ???
}
