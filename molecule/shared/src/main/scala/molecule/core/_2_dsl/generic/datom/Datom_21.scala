package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_21[o0[_], p0, Ns21[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns22[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends Datom {

  final lazy val e          : OneLong   [Ns22[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns22[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns22[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns22[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns22[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns22[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns22[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean], In22[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns22[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
  final lazy val a_         : OneString [Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
  final lazy val v_         : OneAny    [Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
  final lazy val t_         : OneLong   [Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with Indexed = ???
}
