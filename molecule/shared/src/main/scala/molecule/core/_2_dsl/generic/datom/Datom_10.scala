package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_10[o0[_], p0, Ns10[o[_],_,_,_,_,_,_,_,_,_,_,_], Ns11[o[_],_,_,_,_,_,_,_,_,_,_,_,_], In10[o[_],_,_,_,_,_,_,_,_,_,_,_,_], In11[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] extends Datom {

  final lazy val e          : OneLong   [Ns11[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, Long   ], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, J, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns11[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, String ], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, J, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns11[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, Any    ], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, J, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns11[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, Long   ], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, J, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns11[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, Long   ], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, J, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns11[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, Date   ], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, J, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns11[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, Boolean], In11[o0,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, J, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
  final lazy val a_         : OneString [Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
  final lazy val v_         : OneAny    [Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
  final lazy val t_         : OneLong   [Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0, A, B, C, D, E, F, G, H, I, J] with Indexed = ???
}
