//package molecule.core.generic.datom
//
//import java.util.Date
//import scala.language.higherKinds
//
//
//trait Datom_17[Obj, Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends Datom {
//  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns18[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Tpe], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns18[Obj with Prop, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Tpe]
//  type Stay_[Attr[_, _]           ] = Attr[Ns17[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Ns17[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
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
