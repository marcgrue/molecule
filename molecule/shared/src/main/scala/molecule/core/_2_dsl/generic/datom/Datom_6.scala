package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

trait Datom_6[o0[_], p0, Ns6[o[_],_,_,_,_,_,_,_], Ns7[o[_],_,_,_,_,_,_,_,_], In6[o[_],_,_,_,_,_,_,_,_], In7[o[_],_,_,_,_,_,_,_,_,_], A, B, C, D, E, F] extends Datom {

  final lazy val e          : OneLong   [Ns7[o0, p0 with Datom_e        , A, B, C, D, E, F, Long   ], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_e        , A, B, C, D, E, F, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns7[o0, p0 with Datom_a        , A, B, C, D, E, F, String ], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_a        , A, B, C, D, E, F, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns7[o0, p0 with Datom_v        , A, B, C, D, E, F, Any    ], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_v        , A, B, C, D, E, F, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns7[o0, p0 with Datom_t        , A, B, C, D, E, F, Long   ], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_t        , A, B, C, D, E, F, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns7[o0, p0 with Datom_tx       , A, B, C, D, E, F, Long   ], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_tx       , A, B, C, D, E, F, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns7[o0, p0 with Datom_txInstant, A, B, C, D, E, F, Date   ], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_txInstant, A, B, C, D, E, F, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns7[o0, p0 with Datom_op       , A, B, C, D, E, F, Boolean], In7[o0,_,_,_,_,_,_,_,_,_]] with Ns7[o0, p0 with Datom_op       , A, B, C, D, E, F, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
  final lazy val a_         : OneString [Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
  final lazy val v_         : OneAny    [Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
  final lazy val t_         : OneLong   [Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns6[o0, p0, A, B, C, D, E, F], In6[o0,_,_,_,_,_,_,_,_]] with Ns6[o0, p0, A, B, C, D, E, F] with Indexed = ???
}
