//package molecule.core.generic.datom
//
//import java.util.Date
//import scala.language.higherKinds
//
//
//trait Datom_14[Obj, Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends Datom {
//  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns15[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Tpe], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns15[Obj with Prop, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Tpe]
//  type Stay_[Attr[_, _]           ] = Attr[Ns14[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns14[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
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
