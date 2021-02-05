/*
* AUTO-GENERATED Molecule DSL for namespace `Log`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/LogDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Log

import java.util.Date
//import molecule.core.dsl.api._
//import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
//import molecule.core.dsl.dummyTypes._
//import molecule.core.generic.Log._
import scala.language.higherKinds

trait Log_0_0[o0[_], p0] extends Log_[p0] with Log with NS_0_00[o0, p0]

trait Log_0_0_L0[o0[_], p0] extends Log_0_0[o0, p0] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L0[o0, p0 with Prop, Tpe], Nothing] with Log_0_1_L0[o0, p0 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L0[o0, p0               ], Nothing] with Log_0_0_L0[o0, p0               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L1[o0[_], p0, o1[_], p1] extends Log_0_0[o0, p0 with o1[p1]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L1[o0, p0, o1, p1 with Prop, Tpe], Nothing] with Log_0_1_L1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L1[o0, p0, o1, p1               ], Nothing] with Log_0_0_L1[o0, p0, o1, p1               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends Log_0_0[o0, p0 with o1[p1 with o2[p2]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Nothing] with Log_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L2[o0, p0, o1, p1, o2, p2               ], Nothing] with Log_0_0_L2[o0, p0, o1, p1, o2, p2               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends Log_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Nothing] with Log_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ], Nothing] with Log_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends Log_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Nothing] with Log_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Nothing] with Log_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends Log_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Nothing] with Log_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Nothing] with Log_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends Log_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Nothing] with Log_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Nothing] with Log_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}


trait Log_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends Log_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Nothing] with Log_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Log_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Nothing] with Log_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

     
