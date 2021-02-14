/*
* AUTO-GENERATED Molecule DSL for namespace `TxCount`
*
* To change:
* 1. Edit data model in app.dsl.dataModel/TxCountDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.util.testing.TxCount

import java.net.URI
import java.util.Date
import java.util.UUID
import molecule.core.composition.CompositeInit_0.CompositeInit_0_02
import molecule.core.composition.Tx_0_02
import molecule.core.composition.nested_0.nested_0_02._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression.aggregates.aggr_02._
import molecule.core.generic.Datom._
import scala.language.higherKinds

trait TxCount_0_2[o0[_], p0, A, B] extends _TxCount_
  with NS_0_02           [o0, p0, A, B]
  with CompositeInit_0_02[o0, p0, A, B]
  with Tx_0_02           [o0, p0, A, B]

trait TxCount_0_2_L0[o0[_], p0, A, B] extends TxCount_0_2[o0, p0, A, B]
  with Datom_0_2_L0[o0, p0, A, B, TxCount_0_2_L0, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L0[o0, p0          , A, B     ], Nothing] with TxCount_0_2_L0[o0, p0          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L0[o0, p0, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L1[o0[_], p0, o1[_], p1, A, B] extends TxCount_0_2[o0, p0 with o1[p1], A, B]
  with Datom_0_2_L1[o0, p0, o1, p1, A, B, TxCount_0_2_L1, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L1[o0, p0, o1, p1          , A, B     ], Nothing] with TxCount_0_2_L1[o0, p0, o1, p1          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L1[o0, p0, o1, p1, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2]], A, B]
  with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B, TxCount_0_2_L2, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L2[o0, p0, o1, p1, o2, p2          , A, B     ], Nothing] with TxCount_0_2_L2[o0, p0, o1, p1, o2, p2          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B]
  with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, TxCount_0_2_L3, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A, B     ], Nothing] with TxCount_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B]
  with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, TxCount_0_2_L4, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A, B     ], Nothing] with TxCount_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B]
  with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, TxCount_0_2_L5, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A, B     ], Nothing] with TxCount_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B]
  with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, TxCount_0_2_L6, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A, B     ], Nothing] with TxCount_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B]
  with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, TxCount_0_2_L7, Nothing, Nothing, Nothing]
{
  
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A, B     ], Nothing] with TxCount_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with SelfJoin = ???
}

     
