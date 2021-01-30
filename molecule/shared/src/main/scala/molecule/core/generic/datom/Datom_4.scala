package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import scala.language.higherKinds

trait Datom_4[o0[_], p0, Ns4[o[_],_,_,_,_,_], Ns5[o[_],_,_,_,_,_,_], In4[o[_],_,_,_,_,_,_], In5[o[_],_,_,_,_,_,_,_], A, B, C, D] extends Datom {

  final lazy val e          : OneLong   [Ns5[o0, p0 with Datom_e        , A, B, C, D, Long   ], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns5[o0, p0 with Datom_a        , A, B, C, D, String ], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns5[o0, p0 with Datom_v        , A, B, C, D, Any    ], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns5[o0, p0 with Datom_t        , A, B, C, D, Long   ], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns5[o0, p0 with Datom_tx       , A, B, C, D, Long   ], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns5[o0, p0 with Datom_txInstant, A, B, C, D, Date   ], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns5[o0, p0 with Datom_op       , A, B, C, D, Boolean], In5[o0,_,_,_,_,_,_,_]] with Ns5[o0, p0 with Datom_op       , A, B, C, D, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString [Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny    [Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong   [Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns4[o0, p0, A, B, C, D], In4[o0,_,_,_,_,_,_]] with Ns4[o0, p0, A, B, C, D] with Indexed = ???
}
