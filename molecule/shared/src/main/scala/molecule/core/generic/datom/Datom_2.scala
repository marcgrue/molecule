//package molecule.core.generic.datom
//
//import java.util.Date
//import scala.language.higherKinds
//
//
//trait Datom_2[Obj, Ns2[_,_,_,_], Ns3[_,_,_,_,_], In2[_,_,_,_,_], In3[_,_,_,_,_,_], A, B] extends Datom {
//  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns3[Obj, A, B, Tpe], In3[_,_,_,_,_,_]] with Ns3[Obj with Prop, A, B, Tpe]
//  type Stay_[Attr[_, _]           ] = Attr[Ns2[Obj, A, B], In2[_,_,_,_,_]] with Ns2[Obj, A, B]
//
//  final lazy val e          : Next_[e        , Datom_e        , Long   ] = ???
//  final lazy val a          : Next_[a        , Datom_a        , String ] = ???
//  final lazy val v          : Next_[v        , Datom_v        , Any    ] = ???
//  final lazy val t          : Next_[t        , Datom_t        , Long   ] = ???
//  final lazy val tx         : Next_[tx       , Datom_tx       , Long   ] = ???
//  final lazy val txInstant  : Next_[txInstant, Datom_txInstant, Date   ] = ???
//  final lazy val op         : Next_[op       , Datom_op       , Boolean] = ???
//
//  final lazy val e_         : Stay_[e        ] = ???
//  final lazy val a_         : Stay_[a        ] = ???
//  final lazy val v_         : Stay_[v        ] = ???
//  final lazy val t_         : Stay_[t        ] = ???
//  final lazy val tx_        : Stay_[tx       ] = ???
//  final lazy val txInstant_ : Stay_[txInstant] = ???
//  final lazy val op_        : Stay_[op       ] = ???
//}
