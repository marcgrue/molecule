/*
* AUTO-GENERATED Molecule DSL for namespace `AVET`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/AVETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AVET

import java.util.Date
import molecule.core.dsl.base._
import scala.language.higherKinds

trait AVET_0_0[o0[_], p0] extends AVET with NS_0_00[o0, p0]

trait AVET_0_0_L0[o0[_], p0] extends AVET_0_0[o0, p0] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L0[o0, p0 with Prop, Tpe], Nothing] with AVET_0_1_L0[o0, p0 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L0[o0, p0               ], Nothing] with AVET_0_0_L0[o0, p0               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L1[o0[_], p0, o1[_], p1] extends AVET_0_0[o0, p0 with o1[p1]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L1[o0, p0, o1, p1 with Prop, Tpe], Nothing] with AVET_0_1_L1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L1[o0, p0, o1, p1               ], Nothing] with AVET_0_0_L1[o0, p0, o1, p1               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends AVET_0_0[o0, p0 with o1[p1 with o2[p2]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Nothing] with AVET_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L2[o0, p0, o1, p1, o2, p2               ], Nothing] with AVET_0_0_L2[o0, p0, o1, p1, o2, p2               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends AVET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Nothing] with AVET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ], Nothing] with AVET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends AVET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Nothing] with AVET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Nothing] with AVET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends AVET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Nothing] with AVET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Nothing] with AVET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends AVET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Nothing] with AVET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Nothing] with AVET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}


trait AVET_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends AVET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AVET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Nothing] with AVET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[AVET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Nothing] with AVET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

  final lazy val e          : Next[e         , AVET_e        , Long   ] = ???
  final lazy val a          : Next[a         , AVET_a        , String ] = ???
  final lazy val v          : Next[v         , AVET_v        , Any    ] = ???
  final lazy val t          : Next[t         , AVET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AVET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AVET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AVET_op       , Boolean] = ???
}

     
