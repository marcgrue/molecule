/*
* AUTO-GENERATED Molecule DSL for namespace `EAVT`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/EAVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.EAVT

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait EAVT_0_1[o0[_], p0, A] extends EAVT_[p0] with NS_0_01[o0, p0, A]

trait EAVT_0_1_L0[o0[_], p0, A] extends EAVT_0_1[o0, p0, A] {

  final lazy val e          : OneLong    [EAVT_0_2_L0[o0, p0 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L0[o0, p0 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L0[o0, p0 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L0[o0, p0 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L0[o0, p0 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L0[o0, p0 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L0[o0, p0 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L0[o0, p0 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L0[o0, p0 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L0[o0, p0 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L0[o0, p0 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L0[o0, p0 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L0[o0, p0 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L0[o0, p0 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L0[o0, p0 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L0[o0, p0 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L0[o0, p0 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L0[o0, p0 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L0[o0, p0 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L0[o0, p0 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L0[o0, p0 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L0[o0, p0 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L0[o0, p0, A], D01[o0,_,_]] with EAVT_0_1_L0[o0, p0, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L0[o0, p0, A] with SelfJoin = ???
}


trait EAVT_0_1_L1[o0[_], p0, o1[_], p1, A] extends EAVT_0_1[o0, p0 with o1[p1], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L1[o0, p0, o1, p1 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with EAVT_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L1[o0, p0, o1, p1, A] with SelfJoin = ???
}


trait EAVT_0_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, A] extends EAVT_0_1[o0, p0 with o1[p1 with o2[p2]], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L2[o0, p0, o1, p1, o2, p2 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L2[o0, p0, o1, p1, o2, p2, A] with SelfJoin = ???
}


trait EAVT_0_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A] extends EAVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with SelfJoin = ???
}


trait EAVT_0_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A] extends EAVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with SelfJoin = ???
}


trait EAVT_0_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A] extends EAVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with SelfJoin = ???
}


trait EAVT_0_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A] extends EAVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with SelfJoin = ???
}


trait EAVT_0_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A] extends EAVT_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A] {

  final lazy val e          : OneLong    [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_e        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_a        , A, String ], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_v        , A, Any    ], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_t        , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_tx       , A, Long   ], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_txInstant, A, Date   ], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_op       , A, Boolean], D02[o0,_,_,_]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_e$        , A, Option[Long   ]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_a$        , A, Option[String ]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_v$        , A, Option[Any    ]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_t$        , A, Option[Long   ]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_tx$       , A, Option[Long   ]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_txInstant$, A, Option[Date   ]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_op$       , A, Option[Boolean]]] with EAVT_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with EAVT_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val a_         : OneString  [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val v_         : OneAny     [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val t_         : OneLong    [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val tx_        : OneLong    [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val op_        : OneBoolean [EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  
  final def Self: EAVT_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with SelfJoin = ???
}

     
