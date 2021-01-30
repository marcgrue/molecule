package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_5[o0[_], p0, Ns5[o[_],_,_,_,_,_,_], Ns6[o[_],_,_,_,_,_,_,_], In5[o[_],_,_,_,_,_,_,_], In6[o[_],_,_,_,_,_,_,_,_], A, B, C, D, E] extends Datom {

  final lazy val e          : OneLong   [Ns6[o0, p0 with Datom_e        , A, B, C, D, E, Long   ], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_e        , A, B, C, D, E, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns6[o0, p0 with Datom_a        , A, B, C, D, E, String ], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_a        , A, B, C, D, E, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns6[o0, p0 with Datom_v        , A, B, C, D, E, Any    ], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_v        , A, B, C, D, E, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns6[o0, p0 with Datom_t        , A, B, C, D, E, Long   ], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_t        , A, B, C, D, E, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns6[o0, p0 with Datom_tx       , A, B, C, D, E, Long   ], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_tx       , A, B, C, D, E, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns6[o0, p0 with Datom_txInstant, A, B, C, D, E, Date   ], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_txInstant, A, B, C, D, E, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns6[o0, p0 with Datom_op       , A, B, C, D, E, Boolean], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0 with Datom_op       , A, B, C, D, E, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
  final lazy val a_         : OneString [Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
  final lazy val v_         : OneAny    [Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
  final lazy val t_         : OneLong   [Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns5[o0, p0, A, B, C, D, E], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0, A, B, C, D, E] with Indexed = ???
}
