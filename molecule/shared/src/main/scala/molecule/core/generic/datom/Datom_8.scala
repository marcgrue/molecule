package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_8[o0[_], p0, Ns8[o[_],_,_,_,_,_,_,_,_,_], Ns9[o[_],_,_,_,_,_,_,_,_,_,_], In8[o[_],_,_,_,_,_,_,_,_,_,_], In9[o[_],_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] extends Datom {

  final lazy val e          : OneLong   [Ns9[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, Long   ], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_e        , A, B, C, D, E, F, G, H, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns9[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, String ], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_a        , A, B, C, D, E, F, G, H, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns9[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, Any    ], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_v        , A, B, C, D, E, F, G, H, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns9[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, Long   ], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_t        , A, B, C, D, E, F, G, H, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns9[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, Long   ], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, H, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns9[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, Date   ], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, H, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns9[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, Boolean], In9[o0,_,_,_,_,_,_,_,_,_,_,_]] with Ns9[o0, p0 with Datom_op       , A, B, C, D, E, F, G, H, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
  final lazy val a_         : OneString [Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
  final lazy val v_         : OneAny    [Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
  final lazy val t_         : OneLong   [Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns8[o0, p0, A, B, C, D, E, F, G, H], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0, A, B, C, D, E, F, G, H] with Indexed = ???
}
