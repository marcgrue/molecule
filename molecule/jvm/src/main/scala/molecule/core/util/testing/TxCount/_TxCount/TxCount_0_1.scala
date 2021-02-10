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
import molecule.core.composition.CompositeInit_0.CompositeInit_0_01
import molecule.core.composition.Tx.Tx01
import molecule.core.composition.nested_0.nested_0_01._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression.aggregates.aggr_01._
import molecule.core.generic.Datom._
import scala.language.higherKinds

trait TxCount_0_1[o0[_], p0, A] extends _TxCount_
  with NS_0_01           [o0, p0, A]
  with CompositeInit_0_01[o0, p0, A]
  with Tx01              [o0, p0, A]

trait TxCount_0_1_L0[o0[_], p0, A] extends TxCount_0_1[o0, p0, A]
  with Datom_0_1_L0[o0, p0, A, TxCount_0_1_L0, TxCount_0_2_L0, Nothing, Nothing]
  with Aggr_01_L0  [o0, p0, A, TxCount_0_1_L0]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L0[o0, p0 with Prop, A, Tpe], Nothing] with TxCount_0_2_L0[o0, p0 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L0[o0, p0          , A     ], Nothing] with TxCount_0_1_L0[o0, p0          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L0[o0, p0, A] with SelfJoin = ???
}


trait TxCount_0_1_L1[o0[_], p0, o1[_], p1, A] extends TxCount_0_1[o0, p0 with o1[p1], A]
  with Datom_0_1_L1[o0, p0, o1, p1, A, TxCount_0_1_L1, TxCount_0_2_L1, Nothing, Nothing]
  with Aggr_01_L1  [o0, p0, o1, p1, A, TxCount_0_1_L1]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L1[o0, p0, o1, p1 with Prop, A, Tpe], Nothing] with TxCount_0_2_L1[o0, p0, o1, p1 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L1[o0, p0, o1, p1          , A     ], Nothing] with TxCount_0_1_L1[o0, p0, o1, p1          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L1[o0, p0, o1, p1, A] with SelfJoin = ???
}


trait TxCount_0_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, A] extends TxCount_0_1[o0, p0 with o1[p1 with o2[p2]], A]
  with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A, TxCount_0_1_L2, TxCount_0_2_L2, Nothing, Nothing]
  with Aggr_01_L2  [o0, p0, o1, p1, o2, p2, A, TxCount_0_1_L2]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L2[o0, p0, o1, p1, o2, p2 with Prop, A, Tpe], Nothing] with TxCount_0_2_L2[o0, p0, o1, p1, o2, p2 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L2[o0, p0, o1, p1, o2, p2          , A     ], Nothing] with TxCount_0_1_L2[o0, p0, o1, p1, o2, p2          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L2[o0, p0, o1, p1, o2, p2, A] with SelfJoin = ???
}


trait TxCount_0_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A] extends TxCount_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A]
  with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, TxCount_0_1_L3, TxCount_0_2_L3, Nothing, Nothing]
  with Aggr_01_L3  [o0, p0, o1, p1, o2, p2, o3, p3, A, TxCount_0_1_L3]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, Tpe], Nothing] with TxCount_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A     ], Nothing] with TxCount_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with SelfJoin = ???
}


trait TxCount_0_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A] extends TxCount_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A]
  with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, TxCount_0_1_L4, TxCount_0_2_L4, Nothing, Nothing]
  with Aggr_01_L4  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, TxCount_0_1_L4]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, Tpe], Nothing] with TxCount_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A     ], Nothing] with TxCount_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with SelfJoin = ???
}


trait TxCount_0_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A] extends TxCount_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A]
  with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, TxCount_0_1_L5, TxCount_0_2_L5, Nothing, Nothing]
  with Aggr_01_L5  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, TxCount_0_1_L5]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, Tpe], Nothing] with TxCount_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A     ], Nothing] with TxCount_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with SelfJoin = ???
}


trait TxCount_0_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A] extends TxCount_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A]
  with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, TxCount_0_1_L6, TxCount_0_2_L6, Nothing, Nothing]
  with Aggr_01_L6  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, TxCount_0_1_L6]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, Tpe], Nothing] with TxCount_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A     ], Nothing] with TxCount_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with SelfJoin = ???
}


trait TxCount_0_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A] extends TxCount_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A]
  with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, TxCount_0_1_L7, TxCount_0_2_L7, Nothing, Nothing]
  with Aggr_01_L7  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, TxCount_0_1_L7]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, Tpe], Nothing] with TxCount_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, A, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A     ], Nothing] with TxCount_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7          , A     ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with SelfJoin = ???
}

     
