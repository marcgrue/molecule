/*
* AUTO-GENERATED Molecule DSL for namespace `TxCount`
*
* To change:
* 1. Edit data model in app.dsl.dataModel/TxCountDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.util.testing.TxCount

import molecule.core.composition.CompositeInit_0._
import molecule.core.composition.Tx._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression._
import molecule.core.generic.Datom._

trait TxCount_0_2[o0[_], p0, A, B] extends TxCount_[p0] with _TxCount_
  with NS_0_02           [o0, p0, A, B]
  with CompositeInit_0_02[o0, p0, A, B]
  with Tx02              [o0, p0, A, B]

trait TxCount_0_2_L0[o0[_], p0, A, B] extends TxCount_0_2[o0, p0, A, B]
  with Datom_0_2_L0[o0, p0, A, B, TxCount_0_2_L0, Nothing, Nothing, Nothing]
  with Aggr_02_L0  [o0, p0, A, B, TxCount_0_2_L0]
{
  
  override type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L0[o0, p0          , A, B     ], Nothing] with TxCount_0_2_L0[o0, p0          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L0[o0, p0, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L1[o0[_], p0, o1[_], p1, A, B] extends TxCount_0_2[o0, p0 with o1[p1], A, B]
  with Datom_0_2_L1[o0, p0, o1, p1, A, B, TxCount_0_2_L1, Nothing, Nothing, Nothing]
  with Aggr_02_L1  [o0, p0, o1, p1, A, B, TxCount_0_2_L1]
{
  
  override type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L1[o0, p0, o1, p1          , A, B     ], Nothing] with TxCount_0_2_L1[o0, p0, o1, p1          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L1[o0, p0, o1, p1, A, B] with SelfJoin = ???
}


trait TxCount_0_2_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B] extends TxCount_0_2[o0, p0 with o1[p1 with o2[p2]], A, B]
  with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B, TxCount_0_2_L2, Nothing, Nothing, Nothing]
  with Aggr_02_L2  [o0, p0, o1, p1, o2, p2, A, B, TxCount_0_2_L2]
{
  
  override type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0_2_L2[o0, p0, o1, p1, o2, p2          , A, B     ], Nothing] with TxCount_0_2_L2[o0, p0, o1, p1, o2, p2          , A, B     ]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
  
  final def Self: TxCount_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with SelfJoin = ???
}

     
