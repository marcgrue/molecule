/*
* AUTO-GENERATED Molecule DSL for namespace `VAET`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/VAETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.VAET._VAET
import molecule.core.generic.VAET._

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait VAET_0_3[o0[_], p0, A, B, C] extends VAET_[p0] with NS_0_03[o0, p0, A, B, C]

trait VAET_0_3_L0[o0[_], p0, A, B, C] extends VAET_0_3[o0, p0, A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L0[o0, p0 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L0[o0, p0 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L0[o0, p0 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L0[o0, p0 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L0[o0, p0 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L0[o0, p0 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L0[o0, p0 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L0[o0, p0 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L0[o0, p0 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L0[o0, p0 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L0[o0, p0 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L0[o0, p0 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L0[o0, p0 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L0[o0, p0 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L0[o0, p0 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L0[o0, p0 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L0[o0, p0 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L0[o0, p0 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L0[o0, p0 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L0[o0, p0 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L0[o0, p0 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L0[o0, p0 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L0[o0, p0, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L0[o0, p0, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L0[o0, p0, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L1[o0[_], p0, o1[_], p1, A, B, C] extends VAET_0_3[o0, p0 with o1[p1], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L1[o0, p0, o1, p1 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L1[o0, p0, o1, p1 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L1[o0, p0, o1, p1 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L1[o0, p0, o1, p1 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L1[o0, p0, o1, p1 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L1[o0, p0, o1, p1 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L1[o0, p0, o1, p1 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L1[o0, p0, o1, p1 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L1[o0, p0, o1, p1 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L1[o0, p0, o1, p1 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L1[o0, p0, o1, p1 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L1[o0, p0, o1, p1 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L1[o0, p0, o1, p1 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L1[o0, p0, o1, p1 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L1[o0, p0, o1, p1 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L1[o0, p0, o1, p1, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L1[o0, p0, o1, p1, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B, C] extends VAET_0_3[o0, p0 with o1[p1 with o2[p2]], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L2[o0, p0, o1, p1, o2, p2 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L2[o0, p0, o1, p1, o2, p2, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B, C] extends VAET_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B, C] extends VAET_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B, C] extends VAET_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B, C] extends VAET_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C] with SelfJoin = ???
}


trait VAET_0_3_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B, C] extends VAET_0_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B, C] {

  final lazy val e          : OneLong    [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e        , A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a        , A, B, C, String ], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a        , A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v        , A, B, C, Any    ], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v        , A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t        , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t        , A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx       , A, B, C, Long   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx       , A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant, A, B, C, Date   ], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op       , A, B, C, Boolean], D04[o0,_,_,_,_,_]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op       , A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e$        , A, B, C, Option[Long   ]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a$        , A, B, C, Option[String ]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a$        , A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v$        , A, B, C, Option[Any    ]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v$        , A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t$        , A, B, C, Option[Long   ]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t$        , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx$       , A, B, C, Option[Long   ]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx$       , A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant$, A, B, C, Option[Date   ]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant$, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op$       , A, B, C, Option[Boolean]]] with VAET_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op$       , A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C], D03[o0,_,_,_,_]] with VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with Indexed = ???
  
  final def Self: VAET_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C] with SelfJoin = ???
}

     
