package molecule.core.generic.datom

import java.util.Date
import scala.language.higherKinds


/** Generic attribute interface to add second generic attribute */
trait Datom_1[Ns1[_], Ns2[_,_], In1[_,_], In2[_,_,_], A] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns2[A, Type], In2[_,_,_]] with Ns2[A, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns1[A], In1[_,_]] with Ns1[A]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???

  final lazy val e_         : Stay_[e         , Long   ] = ???
  final lazy val a_         : Stay_[a         , String ] = ???
  final lazy val v_         : Stay_[v         , Any    ] = ???
  final lazy val t_         : Stay_[t         , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Date   ] = ???
  final lazy val op_        : Stay_[op        , Boolean] = ???
}
