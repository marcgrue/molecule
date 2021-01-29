//package molecule.core.generic.datom
//
//import java.util.Date
//import scala.language.higherKinds
//
//
///** Generic attribute interface to add first generic attribute */
//trait Datom_0[Obj, Ns0, Ns1[_,_], In0[_,_], In1[_,_,_,_]] extends Datom {
//  // Adding underscore to avoid collusion with custom boilerplate that extends these classes
//  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns1[Prop, Tpe], In1[_,_,_,_]] with Ns1[Prop, Tpe]
//  type Stay_[Attr[_, _]           ] = Attr[Ns0, In0[_,_]] with Ns0
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
