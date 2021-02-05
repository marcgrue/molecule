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
import molecule.core.dsl.api._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.dsl.dummyTypes._
import molecule.core.generic.Datom._
import scala.language.higherKinds

trait Datom_1_15[o0[_], p0, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends Datom_[p0] with Datom

trait Datom_1_15_L0[o0[_], p0, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L1[o0[_], p0, o1[_], p1, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L2[o0[_], p0, o1[_], p1, o2[_], p2, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,o2[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,o2[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,o2[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,o2[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1 with o2[p2]], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1, o2, p2 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1, o2, p2 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1, o2, p2 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1, o2, p2          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1, o2, p2          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1, o2, p2          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1, o2, p2, o3, p3          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}


trait Datom_1_15_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Ns_1_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_1_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_15[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns_2_16[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] extends Datom_1_15[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe], Ns_2_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]] with Ns_1_16[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ], Ns_2_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , I1, Tpe, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]] with Ns_1_15[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ]

  final lazy val e          : Next[e         , Datom_e        , Long   ] = ???
  final lazy val a          : Next[a         , Datom_a        , String ] = ???
  final lazy val v          : Next[v         , Datom_v        , Any    ] = ???
  final lazy val t          : Next[t         , Datom_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Datom_op       , Boolean] = ???
  
  
  final lazy val e_         : Stay[e         , Datom_e        , Long   ] = ???
  final lazy val a_         : Stay[a         , Datom_a        , String ] = ???
  final lazy val v_         : Stay[v         , Datom_v        , Any    ] = ???
  final lazy val t_         : Stay[t         , Datom_t        , Long   ] = ???
  final lazy val tx_        : Stay[tx        , Datom_tx       , Long   ] = ???
  final lazy val txInstant_ : Stay[txInstant , Datom_txInstant, Date   ] = ???
  final lazy val op_        : Stay[op        , Datom_op       , Boolean] = ???
}

     
