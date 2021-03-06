/*
* AUTO-GENERATED Molecule DSL for namespace `AEVT`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/AEVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AEVT

import java.util.Date
import molecule.core.dsl.base._
import scala.language.higherKinds

trait AEVT_0_3[o0[_], p0, A, B, C] extends AEVT with NS_0_03[o0, p0, A, B, C]

trait AEVT_0_3_L0[o0[_], p0, A, B, C] extends AEVT_0_3[o0, p0, A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L0[o0, p0 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L0[o0, p0 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L0[o0, p0          , A, B, C     ], Nothing] with AEVT_0_3_L0[o0, p0          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L1[o0[_], p0, o1[_], p1, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L1[o0, p0, o1, p1 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L1[o0, p0, o1, p1 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L1[o0, p0, o1, p1          , A, B, C     ], Nothing] with AEVT_0_3_L1[o0, p0, o1, p1          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1 with o2[p2]], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L2[o0, p0, o1, p1, o2, p2 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L2[o0, p0, o1, p1, o2, p2          , A, B, C     ], Nothing] with AEVT_0_3_L2[o0, p0, o1, p1, o2, p2          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A, B, C     ], Nothing] with AEVT_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A, B, C     ], Nothing] with AEVT_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A, B, C     ], Nothing] with AEVT_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A, B, C     ], Nothing] with AEVT_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}


trait AEVT_0_3_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B, C] extends AEVT_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, B, C, Tpe], Nothing] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, B, C, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AEVT_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A, B, C     ], Nothing] with AEVT_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A, B, C     ]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

     
