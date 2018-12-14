package molecule.generic.dsl.datom
import java.util.Date
import scala.language.higherKinds


trait Datom_2[Ns2[_,_], Ns3[_,_,_], In2[_,_,_], In3[_,_,_,_], A, B] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns3[A, B, Type], In3[_,_,_,_]] with Ns3[A, B, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns2[A, B], In2[_,_,_]] with Ns2[A, B]

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
