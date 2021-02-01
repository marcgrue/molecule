package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_15[o0[_], p0, Ns15[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns16[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends Datom {

  final lazy val e          : OneLong   [Ns16[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns16[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns16[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns16[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns16[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns16[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns16[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean], In16[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns16[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
  final lazy val a_         : OneString [Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
  final lazy val v_         : OneAny    [Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
  final lazy val t_         : OneLong   [Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with Indexed = ???
}
