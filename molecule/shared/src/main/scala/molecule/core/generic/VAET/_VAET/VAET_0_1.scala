/*
* AUTO-GENERATED Molecule DSL for namespace `VAET`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/VAETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.VAET

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait VAET_0_1[o0[_], p0, A] extends VAET_[p0] with NS_0_01[o0, p0, A]

trait VAET_0_1_L0[o0[_], p0, A] extends VAET_0_1[o0, p0, A] {

  final lazy val e          : OneLong    [VAET_0_2_L0[o0, p0 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L0[o0, p0 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L0[o0, p0 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L0[o0, p0 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L0[o0, p0 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L0[o0, p0 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L0[o0, p0 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L0[o0, p0 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L0[o0, p0 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L0[o0, p0 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L0[o0, p0 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L0[o0, p0 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L0[o0, p0 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L0[o0, p0 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L0[o0, p0 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L0[o0, p0 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L0[o0, p0 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L0[o0, p0 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L0[o0, p0 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L0[o0, p0 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L0[o0, p0 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L0[o0, p0 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L0[o0, p0, A], D01[o0,_,_]] with VAET_0_1_L0[o0, p0, A] with Indexed = ???
  
  final def Self: VAET_0_1_L0[o0, p0, A] with SelfJoin = ???
}


trait VAET_0_1_L1[o0[_], p0, o1[_], p1, A] extends VAET_0_1[o0, p0 with o1[p1], A] {

  final lazy val e          : OneLong    [VAET_0_2_L1[o0, p0, o1, p1 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L1[o0, p0, o1, p1 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L1[o0, p0, o1, p1 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L1[o0, p0, o1, p1 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L1[o0, p0, o1, p1 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L1[o0, p0, o1, p1 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L1[o0, p0, o1, p1 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L1[o0, p0, o1, p1 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L1[o0, p0, o1, p1 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L1[o0, p0, o1, p1 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L1[o0, p0, o1, p1 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L1[o0, p0, o1, p1 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L1[o0, p0, o1, p1 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L1[o0, p0, o1, p1 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L1[o0, p0, o1, p1 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L1[o0, p0, o1, p1, A], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  
  final def Self: VAET_0_1_L1[o0, p0, o1, p1, A] with SelfJoin = ???
}


trait VAET_0_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, A] extends VAET_0_1[o0, p0 with o1[p1 with o2[p2]], A] {

  final lazy val e          : OneLong    [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L2[o0, p0, o1, p1, o2, p2 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  
  final def Self: VAET_0_1_L2[o0, p0, o1, p1, o2, p2, A] with SelfJoin = ???
}


trait VAET_0_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A] extends VAET_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A] {

  final lazy val e          : OneLong    [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  
  final def Self: VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with SelfJoin = ???
}


trait VAET_0_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A] extends VAET_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A] {

  final lazy val e          : OneLong    [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  
  final def Self: VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with SelfJoin = ???
}


trait VAET_0_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A] extends VAET_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A] {

  final lazy val e          : OneLong    [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  
  final def Self: VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with SelfJoin = ???
}


trait VAET_0_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A] extends VAET_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A] {

  final lazy val e          : OneLong    [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  
  final def Self: VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with SelfJoin = ???
}


trait VAET_0_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A] extends VAET_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A] {

  final lazy val e          : OneLong    [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a        , A, String ], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v        , A, Any    ], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t        , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx       , A, Long   ], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant, A, Date   ], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op       , A, Boolean], D02[o0,_,_,_]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e$        , A, Option[Long   ]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a$        , A, Option[String ]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v$        , A, Option[Any    ]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t$        , A, Option[Long   ]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx$       , A, Option[Long   ]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant$, A, Option[Date   ]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op$       , A, Option[Boolean]]] with VAET_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  
  final def Self: VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with SelfJoin = ???
}

     
