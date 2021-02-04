/*
* AUTO-GENERATED Molecule DSL for namespace `AEVT`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/AEVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AEVT._AEVT
import molecule.core.generic.AEVT._

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait AEVT_0_4[o0[_], p0, A, B, C, D] extends AEVT_[p0] with NS_0_04[o0, p0, A, B, C, D]

trait AEVT_0_4_L0[o0[_], p0, A, B, C, D] extends AEVT_0_4[o0, p0, A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L0[o0, p0 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L0[o0, p0 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L0[o0, p0 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L0[o0, p0 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L0[o0, p0 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L0[o0, p0 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L0[o0, p0 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L0[o0, p0 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L0[o0, p0 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L0[o0, p0 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L0[o0, p0 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L0[o0, p0 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L0[o0, p0 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L0[o0, p0 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L0[o0, p0 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L0[o0, p0 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L0[o0, p0 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L0[o0, p0 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L0[o0, p0 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L0[o0, p0 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L0[o0, p0 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L0[o0, p0 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L0[o0, p0, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L0[o0, p0, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L0[o0, p0, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L1[o0[_], p0, o1[_], p1, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L1[o0, p0, o1, p1 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L1[o0, p0, o1, p1, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1 with o2[p2]], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L2[o0, p0, o1, p1, o2, p2 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L2[o0, p0, o1, p1, o2, p2, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L3[o0, p0, o1, p1, o2, p2, o3, p3 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D] with SelfJoin = ???
}


trait AEVT_0_4_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B, C, D] extends AEVT_0_4[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B, C, D] {

  final lazy val e          : OneLong    [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val a          : OneString  [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a        , A, B, C, D, String ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a        , A, B, C, D, String ] with Indexed = ???
  final lazy val v          : OneAny     [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v        , A, B, C, D, Any    ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v        , A, B, C, D, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t        , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t        , A, B, C, D, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx       , A, B, C, D, Long   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx       , A, B, C, D, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant, A, B, C, D, Date   ], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant, A, B, C, D, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op       , A, B, C, D, Boolean], D05[o0,_,_,_,_,_,_]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op       , A, B, C, D, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_e$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a$        , A, B, C, D, Option[String ]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_a$        , A, B, C, D, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v$        , A, B, C, D, Option[Any    ]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_v$        , A, B, C, D, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t$        , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_t$        , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx$       , A, B, C, D, Option[Long   ]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_tx$       , A, B, C, D, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant$, A, B, C, D, Option[Date   ]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_txInstant$, A, B, C, D, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op$       , A, B, C, D, Option[Boolean]]] with AEVT_0_5_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with AEVT_op$       , A, B, C, D, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  final lazy val a_         : OneString  [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  final lazy val v_         : OneAny     [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  final lazy val t_         : OneLong    [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  final lazy val tx_        : OneLong    [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  final lazy val txInstant_ : OneDate    [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  final lazy val op_        : OneBoolean [AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D], D04[o0,_,_,_,_,_]] with AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with Indexed = ???
  
  final def Self: AEVT_0_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D] with SelfJoin = ???
}

     
