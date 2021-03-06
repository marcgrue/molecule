/*
* AUTO-GENERATED Molecule DSL for namespace `VAET`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/VAETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.VAET

import java.util.Date
import molecule.core.dsl.base._
import scala.language.higherKinds

trait VAET_0_2[o0[_], p0, A, B] extends VAET with NS_0_02[o0, p0, A, B]

trait VAET_0_2_L0[o0[_], p0, A, B] extends VAET_0_2[o0, p0, A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L0[o0, p0 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L0[o0, p0 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L0[o0, p0          , A, B     ], Nothing] with VAET_0_2_L0[o0, p0          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L1[o0[_], p0, o1[_], p1, A, B] extends VAET_0_2[o0, p0 with o1[p1], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L1[o0, p0, o1, p1 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L1[o0, p0, o1, p1 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L1[o0, p0, o1, p1          , A, B     ], Nothing] with VAET_0_2_L1[o0, p0, o1, p1          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B] extends VAET_0_2[o0, p0 with o1[p1 with o2[p2]], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L2[o0, p0, o1, p1, o2, p2 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L2[o0, p0, o1, p1, o2, p2          , A, B     ], Nothing] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B] extends VAET_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A, B     ], Nothing] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B] extends VAET_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A, B     ], Nothing] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B] extends VAET_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A, B     ], Nothing] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B] extends VAET_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A, B     ], Nothing] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}


trait VAET_0_2_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B] extends VAET_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, B, Tpe], Nothing] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, B, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A, B     ], Nothing] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A, B     ]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

     
