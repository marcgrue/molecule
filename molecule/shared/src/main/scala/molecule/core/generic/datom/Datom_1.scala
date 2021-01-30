package molecule.core.generic.datom

import java.util.Date
import molecule.core.boilerplate.attributes._
import scala.language.higherKinds

/** Generic attribute interface to add second generic attribute */
trait Datom_1[o0[_], p0, Ns1[o[_],_,_], Ns2[o[_],_,_,_], In1[o[_],_,_,_], In2[o[_],_,_,_,_], A] extends Datom {

  final lazy val e          : OneLong   [Ns2[o0, p0 with Datom_e        , A, Long   ], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns2[o0, p0 with Datom_a        , A, String ], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns2[o0, p0 with Datom_v        , A, Any    ], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns2[o0, p0 with Datom_t        , A, Long   ], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns2[o0, p0 with Datom_tx       , A, Long   ], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns2[o0, p0 with Datom_txInstant, A, Date   ], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns2[o0, p0 with Datom_op       , A, Boolean], In2[o0,_,_,_,_]] with Ns2[o0, p0 with Datom_op       , A, Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
  final lazy val a_         : OneString [Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
  final lazy val v_         : OneAny    [Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
  final lazy val t_         : OneLong   [Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns1[o0, p0, A], In1[o0,_,_,_]] with Ns1[o0, p0, A] with Indexed = ???
}
