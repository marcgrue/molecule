package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_2[o0[_], p0, Ns2[o[_],_,_,_], Ns3[o[_],_,_,_,_], In2[o[_],_,_,_,_], In3[o[_],_,_,_,_,_], A, B] extends Datom {

  final lazy val e          : OneLong   [Ns3[o0, p0 with Datom_e        , A, B, Long   ], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns3[o0, p0 with Datom_a        , A, B, String ], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns3[o0, p0 with Datom_v        , A, B, Any    ], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns3[o0, p0 with Datom_t        , A, B, Long   ], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns3[o0, p0 with Datom_tx       , A, B, Long   ], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns3[o0, p0 with Datom_txInstant, A, B, Date   ], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns3[o0, p0 with Datom_op       , A, B, Boolean], In3[o0,_,_,_,_,_]] with Ns3[o0, p0 with Datom_op       , A, B, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
  final lazy val a_         : OneString [Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
  final lazy val v_         : OneAny    [Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
  final lazy val t_         : OneLong   [Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns2[o0, p0, A, B], In2[o0,_,_,_,_]] with Ns2[o0, p0, A, B] with Indexed = ???
}
