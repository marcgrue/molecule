/*
* AUTO-GENERATED Molecule DSL for namespace `AEVT`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/AEVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AEVT

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait AEVT_0_1[o0[_], p0, A] extends AEVT_[p0] with NS_0_01[o0, p0, A]

trait AEVT_0_1_L0[o0[_], p0, A] extends AEVT_0_1[o0, p0, A] {

  final lazy val e          : OneLong    [AEVT_0_2_L0[o0, p0 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L0[o0, p0 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L0[o0, p0 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L0[o0, p0 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L0[o0, p0 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L0[o0, p0 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L0[o0, p0 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L0[o0, p0 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L0[o0, p0 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L0[o0, p0 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L0[o0, p0 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L0[o0, p0 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L0[o0, p0 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L0[o0, p0 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L0[o0, p0 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L0[o0, p0 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L0[o0, p0 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L0[o0, p0 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L0[o0, p0 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L0[o0, p0 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L0[o0, p0 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L0[o0, p0 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with AEVT_0_1_L0[o0, p0, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L0[o0, p0, A] with SelfJoin = ???
}


trait AEVT_0_1_L1[o0[_], p0, o1[_], p1, A] extends AEVT_0_1[o0, p0 with o1[p1], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L1[o0, p0, o1, p1 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with AEVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L1[o0, p0, o1, p1, A] with SelfJoin = ???
}


trait AEVT_0_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, A] extends AEVT_0_1[o0, p0 with o1[p1 with o2[p2]], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L2[o0, p0, o1, p1, o2, p2 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with SelfJoin = ???
}


trait AEVT_0_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A] extends AEVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with SelfJoin = ???
}


trait AEVT_0_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A] extends AEVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with SelfJoin = ???
}


trait AEVT_0_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A] extends AEVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with SelfJoin = ???
}


trait AEVT_0_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A] extends AEVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with SelfJoin = ???
}


trait AEVT_0_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A] extends AEVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A] {

  final lazy val e          : OneLong    [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a        , A, String ], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v        , A, Any    ], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t        , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx       , A, Long   ], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant, A, Date   ], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op       , A, Boolean], D02[o0,_,_,_]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e$        , A, Option[Long   ]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a$        , A, Option[String ]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v$        , A, Option[Any    ]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t$        , A, Option[Long   ]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx$       , A, Option[Long   ]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant$, A, Option[Date   ]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op$       , A, Option[Boolean]]] with AEVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  
  final def Self: AEVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with SelfJoin = ???
}

     
