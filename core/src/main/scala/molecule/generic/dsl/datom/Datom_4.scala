package molecule.generic.dsl.datom
import java.util.Date
import scala.language.higherKinds


trait Datom_4[Ns4[_,_,_,_], Ns5[_,_,_,_,_], In4[_,_,_,_,_], In5[_,_,_,_,_,_], A, B, C, D] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns5[A, B, C, D, Type], In5[_,_,_,_,_,_]] with Ns5[A, B, C, D, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns4[A, B, C, D], In4[_,_,_,_,_]] with Ns4[A, B, C, D]

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
