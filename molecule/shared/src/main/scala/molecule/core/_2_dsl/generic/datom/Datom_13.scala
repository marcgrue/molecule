package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_13[o0[_], p0, Ns13[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns14[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In13[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] extends Datom {

  final lazy val e          : OneLong   [Ns14[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns14[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns14[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns14[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns14[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns14[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns14[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
  final lazy val a_         : OneString [Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
  final lazy val v_         : OneAny    [Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
  final lazy val t_         : OneLong   [Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M], In13[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns13[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M] with Indexed = ???
}
