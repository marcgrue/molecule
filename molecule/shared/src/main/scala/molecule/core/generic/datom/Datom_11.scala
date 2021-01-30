package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_11[o0[_], p0, Ns11[o[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns12[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_], In11[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_], In12[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] extends Datom {

  final lazy val e          : OneLong   [Ns12[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, K, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns12[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, String ], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, K, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns12[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, Any    ], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, K, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns12[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, K, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns12[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, K, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns12[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, Date   ], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, K, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns12[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, Boolean], In12[o0,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, K, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
  final lazy val a_         : OneString [Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
  final lazy val v_         : OneAny    [Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
  final lazy val t_         : OneLong   [Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0, A, B, C, D, E, F, G, H, I, J, K] with Indexed = ???
}
