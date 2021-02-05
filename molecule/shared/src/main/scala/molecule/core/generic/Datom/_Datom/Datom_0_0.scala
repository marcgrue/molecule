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

trait Datom_0_0[o0[_], p0] extends Datom_[p0] with Datom

trait Datom_0_0_L0[o0[_], p0, Ns_0_0[o0[_],_], Ns_0_1[o0[_],_,_], Ns_1_0[o0[_],_,_], Ns_1_1[o0[_],_,_,_]] extends Datom_0_0[o0, p0] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0 with Prop, Tpe], Ns_1_1[o0, p0 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0               ], Ns_1_0[o0, p0          , Tpe     ]] with Ns_0_0[o0, p0               ]

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


trait Datom_0_0_L1[o0[_], p0, o1[_], p1, Ns_0_0[o0[_],_,o1[_],_], Ns_0_1[o0[_],_,o1[_],_,_], Ns_1_0[o0[_],_,o1[_],_,_], Ns_1_1[o0[_],_,o1[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1               ], Ns_1_0[o0, p0, o1, p1          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1               ]

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


trait Datom_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2, Ns_0_0[o0[_],_,o1[_],_,o2[_],_], Ns_0_1[o0[_],_,o1[_],_,o2[_],_,_], Ns_1_0[o0[_],_,o1[_],_,o2[_],_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2               ], Ns_1_0[o0, p0, o1, p1, o2, p2          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2               ]

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


trait Datom_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, Ns_0_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_], Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_], Ns_1_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3               ]

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


trait Datom_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, Ns_0_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_], Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_], Ns_1_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

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


trait Datom_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, Ns_0_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_], Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_], Ns_1_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

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


trait Datom_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, Ns_0_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_], Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_], Ns_1_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

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


trait Datom_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, Ns_0_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_], Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_], Ns_1_0[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_]] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe, Tpe]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Ns_1_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , Tpe     ]] with Ns_0_0[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

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

     
