package molecule.generic.dsl.datom
import java.util.Date
import scala.language.higherKinds


trait Datom_1[Ns1[_], Ns2[_,_], In1[_,_], In2[_,_,_], A] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns2[A, Type], In2[_,_,_]] with Ns2[A, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns1[A], In1[_,_]] with Ns1[A]

  final def e          : Next_[e         , Long   ] = ???
  final def ns         : Next_[ns        , String ] = ???
  final def a          : Next_[a         , String ] = ???
  final def v          : Next_[v         , Any    ] = ???
  final def t          : Next_[t         , Long   ] = ???
  final def tx         : Next_[tx        , Long   ] = ???
  final def txInstant  : Next_[txInstant , Date   ] = ???
  final def op         : Next_[op        , Boolean] = ???

  final def e_         : Stay_[e         , Long   ] = ???
  final def ns_        : Stay_[ns        , String ] = ???
  final def a_         : Stay_[a         , String ] = ???
  final def v_         : Stay_[v         , Any    ] = ???
  final def t_         : Stay_[t         , Long   ] = ???
  final def tx_        : Stay_[tx        , Long   ] = ???
  final def txInstant_ : Stay_[txInstant , Date   ] = ???
  final def op_        : Stay_[op        , Boolean] = ???
}
