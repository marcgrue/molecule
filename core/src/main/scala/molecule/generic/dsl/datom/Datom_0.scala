package molecule.generic.dsl.datom
import java.util.Date
import scala.language.higherKinds


trait Datom_0[Ns0, Ns1[_], In0[_], In1[_, _]] extends Datom {
  type Next_[Attr[_, _], Type] = Attr[Ns1[Type], In1[_,_]] with Ns1[Type]
  type Stay_[Attr[_, _], Type] = Attr[Ns0, In0[_]] with Ns0

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
