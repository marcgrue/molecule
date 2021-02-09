/*
* AUTO-GENERATED Molecule DSL for namespace `TxCount`
*
* To change:
* 1. Edit data model in app.dsl.dataModel/TxCountDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.util.testing.TxCount

import molecule.core.composition.CompositeInit_0.CompositeInit_0_01
import molecule.core.composition.Tx.Tx01
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression.aggregates.aggr_01._
import molecule.core.generic.Datom._
import scala.language.higherKinds

trait TxCount_0_1[o0[_], p0, A] extends TxCount_[p0] with _TxCount_
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

     
