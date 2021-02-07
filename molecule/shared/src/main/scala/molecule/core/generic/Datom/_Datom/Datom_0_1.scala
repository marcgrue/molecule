/*
* AUTO-GENERATED Molecule DSL for namespace `Datom`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/DatomDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Datom

import java.net.URI
import java.util.Date
import java.util.UUID
import molecule.core.composition.CompositeInit_0._
import molecule.core.composition.CompositeInit_1._
import molecule.core.composition.CompositeInit_2._
import molecule.core.composition.CompositeInit_3._
import molecule.core.composition.nested._
import molecule.core.composition.Nested_1._
import molecule.core.composition.Nested_2._
import molecule.core.composition.Nested_3._
import molecule.core.composition.Tx._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression._
import molecule.core.expression.AttrExpressions.?
import molecule.core.generic.Datom._
import molecule.core.generic.GenericNs
import scala.language.higherKinds

trait Datom_0_1[o0[_], p0, A] extends Datom_[p0] with Datom with NS_0_01[o0, p0, A]

trait Datom_0_1_L0[o0[_], p0, A, Ns_0_1[o0[_],_,_], Ns_0_2[o0[_],_,_,_], Ns_1_1[o0[_],_,_,_], Ns_1_2[o0[_],_,_,_,_]] extends Datom_0_1[o0, p0, A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0 with Prop, A, Tpe], Ns_1_2[o0, p0 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0          , A     ], Ns_1_1[o0, p0          , Tpe, A     ]] with Ns_0_1[o0, p0          , A     ]

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


trait Datom_0_1_L1[o0[_], p0, o1[_], p1, A, Ns_0_1[o0[_],_,o1[_],_,_], Ns_0_2[o0[_],_,o1[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1          , A     ], Ns_1_1[o0, p0, o1, p1          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1          , A     ]

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


trait Datom_0_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, Ns_0_1[o0[_],_,o1[_],_,o2[_],_,_], Ns_0_2[o0[_],_,o1[_],_,o2[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,o2[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2]], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1, o2, p2 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1, o2, p2 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1, o2, p2 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2          , A     ], Ns_1_1[o0, p0, o1, p1, o2, p2          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1, o2, p2          , A     ]

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


trait Datom_0_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_], Ns_0_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3          , A     ], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3          , A     ]

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


trait Datom_0_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_], Ns_0_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A     ], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A     ]

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


trait Datom_0_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_], Ns_0_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A     ], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A     ]

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


trait Datom_0_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_], Ns_0_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A     ], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A     ]

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


trait Datom_0_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, Ns_0_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_], Ns_0_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_], Ns_1_1[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_], Ns_1_2[o0[_],_,o1[_],_,o2[_],_,o3[_],_,o4[_],_,o5[_],_,o6[_],_,o7[_],_,_,_,_]] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, Tpe], Ns_1_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe, A, Tpe]] with Ns_0_2[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A     ], Ns_1_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , Tpe, A     ]] with Ns_0_1[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A     ]

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

     
