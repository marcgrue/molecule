package molecule.core._2_dsl.generic.datom

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes.{Indexed, OneAny, OneBoolean, OneDate, OneLong, OneString}
import molecule.core._2_dsl.generic.Datom
import scala.language.higherKinds

/** Generic attribute interface to add first generic attribute */
trait Datom_0[o0[_], p0, Ns0[o[_],_], Ns1[o[_],_,_], In0[o[_],_,_], In1[o[_],_,_,_]] extends Datom {

  final lazy val e          : OneLong   [Ns1[o0, p0 with Datom_e        , Long   ], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString [Ns1[o0, p0 with Datom_a        , String ], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny    [Ns1[o0, p0 with Datom_v        , Any    ], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong   [Ns1[o0, p0 with Datom_t        , Long   ], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong   [Ns1[o0, p0 with Datom_tx       , Long   ], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate   [Ns1[o0, p0 with Datom_txInstant, Date   ], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean[Ns1[o0, p0 with Datom_op       , Boolean], In1[o0,_,_,_]] with Ns1[o0, p0 with Datom_op       , Boolean] with Indexed = ???

  final lazy val e_         : OneLong   [Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
  final lazy val a_         : OneString [Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
  final lazy val v_         : OneAny    [Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
  final lazy val t_         : OneLong   [Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
  final lazy val tx_        : OneLong   [Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
  final lazy val txInstant_ : OneDate   [Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
  final lazy val op_        : OneBoolean[Ns0[o0, p0], In0[o0,_,_]] with Ns0[o0, p0] with Indexed = ???
}
