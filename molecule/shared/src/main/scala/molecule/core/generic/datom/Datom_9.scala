package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_9[o0[_], p0, Ns9[o[_],_,_,_,_,_,_,_,_,_,_], Ns10[o[_],_,_,_,_,_,_,_,_,_,_,_], In9[o[_],_,_,_,_,_,_,_,_,_,_,_], In10[o[_],_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] extends Datom {

  final lazy val e          : OneLong   [Ns10[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, Long   ], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, I, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns10[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, String ], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, I, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns10[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, Any    ], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, I, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns10[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, Long   ], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, I, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns10[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, Long   ], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, I, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns10[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, Date   ], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, I, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns10[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, Boolean], In10[o0,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns10[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, I, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
  final lazy val a_         : OneString [Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
  final lazy val v_         : OneAny    [Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
  final lazy val t_         : OneLong   [Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns9[o0, p0, A, B, C, D, E, F, G, H, I], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0, A, B, C, D, E, F, G, H, I] with Indexed = ???
}
