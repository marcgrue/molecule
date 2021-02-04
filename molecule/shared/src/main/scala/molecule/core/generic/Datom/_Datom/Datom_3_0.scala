/*
* AUTO-GENERATED Molecule DSL for namespace `Datom`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/DatomDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Datom

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait Datom_3_0[o0[_], p0, I1, I2, I3] extends Datom_[p0]

trait Datom_3_0_L0[o0[_], p0, I1, I2, I3] extends Datom_3_0[o0, p0, I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L0[o0, p0 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L0[o0, p0 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L0[o0, p0 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L0[o0, p0 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L0[o0, p0 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L0[o0, p0 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L0[o0, p0 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L0[o0, p0 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L0[o0, p0 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L0[o0, p0 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L0[o0, p0 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L0[o0, p0 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L0[o0, p0 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L0[o0, p0 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L0[o0, p0 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L0[o0, p0 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L0[o0, p0 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L0[o0, p0 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L0[o0, p0 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L0[o0, p0 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L0[o0, p0 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L0[o0, p0 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L0[o0, p0, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L0[o0, p0, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L1[o0[_], p0, o1[_], p1, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L1[o0, p0, o1, p1 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L1[o0, p0, o1, p1 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L1[o0, p0, o1, p1 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L1[o0, p0, o1, p1 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L1[o0, p0, o1, p1 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L1[o0, p0, o1, p1 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L1[o0, p0, o1, p1 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L1[o0, p0, o1, p1 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L1[o0, p0, o1, p1 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L1[o0, p0, o1, p1 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L1[o0, p0, o1, p1 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L1[o0, p0, o1, p1 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L1[o0, p0, o1, p1 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L1[o0, p0, o1, p1 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L1[o0, p0, o1, p1 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L1[o0, p0, o1, p1, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L2[o0[_], p0, o1[_], p1, o2[_], p2, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1 with o2[p2]], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3] with Indexed = ???
}


trait Datom_3_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, I1, I2, I3] extends Datom_3_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], I1, I2, I3] {

  final lazy val e          : OneLong    [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, I2, I3, String ], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, I2, I3, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, I2, I3, Any    ], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, I2, I3, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, I2, I3, Long   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, I2, I3, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, I2, I3, Date   ], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, I2, I3, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, I2, I3, Boolean], D04[o0,_,_,_,_,_]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, I2, I3, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , I1, I2, I3, Option[String ]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , I1, I2, I3, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , I1, I2, I3, Option[Any    ]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , I1, I2, I3, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , I1, I2, I3, Option[Long   ]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , I1, I2, I3, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, I1, I2, I3, Option[Date   ]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, I1, I2, I3, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , I1, I2, I3, Option[Boolean]]] with Datom_3_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , I1, I2, I3, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3], D03[o0,_,_,_,_]] with Datom_3_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3] with Indexed = ???
}

     
