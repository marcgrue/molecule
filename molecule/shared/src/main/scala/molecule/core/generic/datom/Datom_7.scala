package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_7[o0[_], p0, Ns7[o[_],_,_,_,_,_,_,_,_], Ns8[o[_],_,_,_,_,_,_,_,_,_], In7[o[_],_,_,_,_,_,_,_,_,_], In8[o[_],_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G] extends Datom {

  final lazy val e          : OneLong   [Ns8[o0, p0 with Datom_e        , A, B, C, D, E, F, G, Long   ], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_e        , A, B, C, D, E, F, G, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns8[o0, p0 with Datom_a        , A, B, C, D, E, F, G, String ], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_a        , A, B, C, D, E, F, G, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns8[o0, p0 with Datom_v        , A, B, C, D, E, F, G, Any    ], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_v        , A, B, C, D, E, F, G, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns8[o0, p0 with Datom_t        , A, B, C, D, E, F, G, Long   ], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_t        , A, B, C, D, E, F, G, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns8[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, Long   ], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_tx       , A, B, C, D, E, F, G, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns8[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, Date   ], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_txInstant, A, B, C, D, E, F, G, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns8[o0, p0 with Datom_op       , A, B, C, D, E, F, G, Boolean], In8[o0,_,_,_,_,_,_,_,_,_,_]] with Ns8[o0, p0 with Datom_op       , A, B, C, D, E, F, G, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
  final lazy val a_         : OneString [Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
  final lazy val v_         : OneAny    [Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
  final lazy val t_         : OneLong   [Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns7[o0, p0, A, B, C, D, E, F, G], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0, A, B, C, D, E, F, G] with Indexed = ???
}
