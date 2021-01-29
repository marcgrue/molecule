//package molecule.core.generic.datom
//
//import java.util.Date
//import scala.language.higherKinds
//
//
//trait Datom_11[Obj, Ns11[_,_,_,_,_,_,_,_,_,_,_,_,_], Ns12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] extends Datom {
//  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns12[Obj, A, B, C, D, E, F, G, H, I, J, K, Tpe], In12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns12[Obj with Prop, A, B, C, D, E, F, G, H, I, J, K, Tpe]
//  type Stay_[Attr[_, _]           ] = Attr[Ns11[Obj, A, B, C, D, E, F, G, H, I, J, K], In11[_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns11[Obj, A, B, C, D, E, F, G, H, I, J, K]
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
