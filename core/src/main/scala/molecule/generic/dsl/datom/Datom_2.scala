package molecule.generic.dsl.datom
import java.util.Date
import scala.language.higherKinds


trait Datom_2[Ns2[_,_], Ns3[_,_,_], In2[_,_,_], In3[_,_,_,_], A, B] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns3[A, B, Type], In3[_,_,_,_]] with Ns3[A, B, Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns2[A, B], In2[_,_,_]] with Ns2[A, B]

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
