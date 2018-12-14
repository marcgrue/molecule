package molecule.generic.dsl.datom
import java.util.Date
import scala.language.higherKinds


trait Datom_10[Ns10[_,_,_,_,_,_,_,_,_,_], Ns11[_,_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns11[A, B, C, D, E, F, G, H, I, J, Type], In11[_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[A, B, C, D, E, F, G, H, I, J, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns10[A, B, C, D, E, F, G, H, I, J], In10[_,_,_,_,_,_,_,_,_,_,_]] with Ns10[A, B, C, D, E, F, G, H, I, J]

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
