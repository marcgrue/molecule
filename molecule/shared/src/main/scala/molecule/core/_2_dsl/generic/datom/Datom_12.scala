package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_12[o0[_], p0, Ns12[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns13[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_], In12[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_], In13[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] extends Datom {

  final lazy val e          : OneLong   [Ns13[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns13[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns13[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, Any    ], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns13[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns13[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns13[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, Date   ], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns13[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, Boolean], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
  final lazy val a_         : OneString [Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
  final lazy val v_         : OneAny    [Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
  final lazy val t_         : OneLong   [Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L] with Indexed = ???
}
