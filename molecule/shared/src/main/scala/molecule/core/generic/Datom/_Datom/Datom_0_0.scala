/*
* AUTO-GENERATED Molecule DSL for namespace `Datom`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/DatomDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Datom

import java.util.Date
import molecule.core.dsl.base._
import scala.language.higherKinds

trait Datom_0_0[o0[_], p0] extends Datom with NS_0_00[o0, p0]

trait Datom_0_0_L0[o0[_], p0, Ns_0_0[_[_],_], Ns_0_1[_[_],_,_], Ns_1_0[_[_],_,_], Ns_1_1[_[_],_,_,_]] extends Datom_0_0[o0, p0] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0 with Prop, Tpe], Ns_1_1[o0, p0 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0               ], Ns_1_0[o0, p0          , Tpe     ]] with Ns_0_0[o0, p0               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L1[o0[_], p0, o1[_], p1, Ns_0_0[_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1               ], Ns_1_0[o0, p0, o1, p1          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2, Ns_0_0[_[_],_,_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2]]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2               ], Ns_1_0[o0, p0, o1, p1, o2, p2          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, Ns_0_0[_[_],_,_[_],_,_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, Ns_0_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, Ns_0_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, Ns_0_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}


trait Datom_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, Ns_0_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_], Ns_0_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_0[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_], Ns_1_1[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

  final lazy val e          : Next_[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next_[a         , Datom_a        , String ] = ???
  final lazy val v          : Next_[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next_[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next_[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay_[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay_[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay_[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay_[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay_[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay_[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay_[op        , Datom_op       , Boolean] = ???
}

     
