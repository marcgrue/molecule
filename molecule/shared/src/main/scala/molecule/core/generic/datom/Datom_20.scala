package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_20[o0[_], p0, Ns20[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns21[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends Datom {

  final lazy val e          : OneLong   [Ns21[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns21[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns21[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns21[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns21[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns21[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns21[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
  final lazy val a_         : OneString [Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
  final lazy val v_         : OneAny    [Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
  final lazy val t_         : OneLong   [Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with Indexed = ???
}
