/*
* AUTO-GENERATED Molecule DSL for namespace `EAVT`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/EAVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.EAVT

import java.util.Date
import molecule.core.dsl.base._
import scala.language.higherKinds

trait EAVT_0_0[o0[_], p0] extends EAVT with NS_0_00[o0, p0]

trait EAVT_0_0_L0[o0[_], p0] extends EAVT_0_0[o0, p0] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L0[o0, p0 with Prop, Tpe], Nothing] with EAVT_0_1_L0[o0, p0 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L0[o0, p0               ], Nothing] with EAVT_0_0_L0[o0, p0               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L1[o0[_], p0, o1[_], p1] extends EAVT_0_0[o0, p0 with o1[p1]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L1[o0, p0, o1, p1 with Prop, Tpe], Nothing] with EAVT_0_1_L1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L1[o0, p0, o1, p1               ], Nothing] with EAVT_0_0_L1[o0, p0, o1, p1               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends EAVT_0_0[o0, p0 with o1[p1 with o2[p2]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Nothing] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L2[o0, p0, o1, p1, o2, p2               ], Nothing] with EAVT_0_0_L2[o0, p0, o1, p1, o2, p2               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends EAVT_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Nothing] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ], Nothing] with EAVT_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends EAVT_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Nothing] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Nothing] with EAVT_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends EAVT_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Nothing] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Nothing] with EAVT_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends EAVT_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Nothing] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Nothing] with EAVT_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}


trait EAVT_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends EAVT_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Nothing] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[EAVT_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Nothing] with EAVT_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

     
