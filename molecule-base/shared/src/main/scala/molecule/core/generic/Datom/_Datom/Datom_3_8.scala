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

trait Datom_3_8[o0[_], p0, I1, I2, I3, A, B, C, D, E, F, G, H] extends Datom with NS_3_08[o0, p0, I1, I2, I3, A, B, C, D, E, F, G, H]

trait Datom_3_8_L0[o0[_], p0, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0, I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L1[o0[_], p0, o1[_], p1, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L2[o0[_], p0, o1[_], p1, o2[_], p2, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1 with o2[p2]], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1, o2, p2 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1, o2, p2 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1, o2, p2          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1, o2, p2          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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


trait Datom_3_8_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, I1, I2, I3, A, B, C, D, E, F, G, H, Ns_3_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_], Ns_3_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_8[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_4_9[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_3_8[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], I1, I2, I3, A, B, C, D, E, F, G, H] {
  type Next_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe], Nothing] with Ns_3_9[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, I1, I2, I3, A, B, C, D, E, F, G, H, Tpe]
  type Stay_[Attr[_, _], Prop, Tpe] = Attr[Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , I1, I2, I3, A, B, C, D, E, F, G, H     ], Nothing] with Ns_3_8[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , I1, I2, I3, A, B, C, D, E, F, G, H     ]

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

     
