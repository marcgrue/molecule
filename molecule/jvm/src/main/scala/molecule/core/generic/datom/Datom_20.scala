package molecule.core.generic.datom

import java.util.Date
import scala.language.higherKinds


trait Datom_20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Type], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

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
