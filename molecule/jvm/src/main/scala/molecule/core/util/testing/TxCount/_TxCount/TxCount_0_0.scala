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
import molecule.core.composition.CompositeInit_0.CompositeInit_0_00
import molecule.core.composition.Tx_0.Tx_0_00
import molecule.core.composition.nested_0.nested_0_00._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression.aggregates.aggr_00._
import molecule.core.generic.Datom._
import scala.language.higherKinds

trait TxCount_0_0[o0[_], p0] extends _TxCount_
  with NS_0_00           [o0, p0]
  with CompositeInit_0_00[o0, p0]
  with Tx_0_00           [o0, p0]

trait TxCount_0_0_L0[o0[_], p0] extends TxCount_0_0[o0, p0]
  with Datom_0_0_L0[o0, p0, TxCount_0_0_L0, TxCount_0_1_L0, Nothing, Nothing]
  with Aggr_00_L0  [o0, p0, TxCount_0_0_L0]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L0[o0, p0 with Prop, Tpe], Nothing] with TxCount_0_1_L0[o0, p0 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L0[o0, p0               ], Nothing] with TxCount_0_0_L0[o0, p0               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L1[o0[_], p0, o1[_], p1] extends TxCount_0_0[o0, p0 with o1[p1]]
  with Datom_0_0_L1[o0, p0, o1, p1, TxCount_0_0_L1, TxCount_0_1_L1, Nothing, Nothing]
  with Aggr_00_L1  [o0, p0, o1, p1, TxCount_0_0_L1]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L1[o0, p0, o1, p1 with Prop, Tpe], Nothing] with TxCount_0_1_L1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L1[o0, p0, o1, p1               ], Nothing] with TxCount_0_0_L1[o0, p0, o1, p1               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends TxCount_0_0[o0, p0 with o1[p1 with o2[p2]]]
  with Datom_0_0_L2[o0, p0, o1, p1, o2, p2, TxCount_0_0_L2, TxCount_0_1_L2, Nothing, Nothing]
  with Aggr_00_L2  [o0, p0, o1, p1, o2, p2, TxCount_0_0_L2]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Nothing] with TxCount_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L2[o0, p0, o1, p1, o2, p2               ], Nothing] with TxCount_0_0_L2[o0, p0, o1, p1, o2, p2               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends TxCount_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]]
  with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, TxCount_0_0_L3, TxCount_0_1_L3, Nothing, Nothing]
  with Aggr_00_L3  [o0, p0, o1, p1, o2, p2, o3, p3, TxCount_0_0_L3]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Nothing] with TxCount_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ], Nothing] with TxCount_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends TxCount_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]]
  with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, TxCount_0_0_L4, TxCount_0_1_L4, Nothing, Nothing]
  with Aggr_00_L4  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, TxCount_0_0_L4]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Nothing] with TxCount_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Nothing] with TxCount_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends TxCount_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]]
  with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, TxCount_0_0_L5, TxCount_0_1_L5, Nothing, Nothing]
  with Aggr_00_L5  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, TxCount_0_0_L5]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Nothing] with TxCount_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Nothing] with TxCount_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends TxCount_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]]
  with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, TxCount_0_0_L6, TxCount_0_1_L6, Nothing, Nothing]
  with Aggr_00_L6  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, TxCount_0_0_L6]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Nothing] with TxCount_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Nothing] with TxCount_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}


trait TxCount_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends TxCount_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]]
  with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, TxCount_0_0_L7, TxCount_0_1_L7, Nothing, Nothing]
  with Aggr_00_L7  [o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, TxCount_0_0_L7]
{
  type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Nothing] with TxCount_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Nothing] with TxCount_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???
  
  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long  ]] = ???
  
  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}

     
