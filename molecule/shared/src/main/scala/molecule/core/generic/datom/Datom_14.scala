package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_14[o0[_], p0, Ns14[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns15[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends Datom {

  final lazy val e          : OneLong   [Ns15[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns15[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns15[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns15[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns15[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns15[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns15[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
  final lazy val a_         : OneString [Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
  final lazy val v_         : OneAny    [Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
  final lazy val t_         : OneLong   [Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N] with Indexed = ???
}
