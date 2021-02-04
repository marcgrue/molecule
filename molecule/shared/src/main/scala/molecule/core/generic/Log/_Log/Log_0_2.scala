/*
* AUTO-GENERATED Molecule DSL for namespace `Log`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/LogDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Log._Log
import molecule.core.generic.Log._

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait Log_0_2[o0[_], p0, A, B] extends Log_[p0] with NS_0_02[o0, p0, A, B]

trait Log_0_2_L0[o0[_], p0, A, B] extends Log_0_2[o0, p0, A, B] {

  final lazy val e          : OneLong    [Log_0_3_L0[o0, p0 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L0[o0, p0 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L0[o0, p0 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L0[o0, p0 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L0[o0, p0 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L0[o0, p0 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L0[o0, p0 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L0[o0, p0 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L0[o0, p0 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L0[o0, p0 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L0[o0, p0 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L0[o0, p0 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L0[o0, p0 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L0[o0, p0 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L0[o0, p0 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L0[o0, p0 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L0[o0, p0 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L0[o0, p0 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L0[o0, p0 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L0[o0, p0 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L0[o0, p0 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L0[o0, p0 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L0[o0, p0, A, B], D02[o0,_,_,_]] with Log_0_2_L0[o0, p0, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L0[o0, p0, A, B] with SelfJoin = ???
}


trait Log_0_2_L1[o0[_], p0, o1[_], p1, A, B] extends Log_0_2[o0, p0 with o1[p1], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L1[o0, p0, o1, p1 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L1[o0, p0, o1, p1 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L1[o0, p0, o1, p1 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L1[o0, p0, o1, p1 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L1[o0, p0, o1, p1 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L1[o0, p0, o1, p1 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L1[o0, p0, o1, p1 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L1[o0, p0, o1, p1 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L1[o0, p0, o1, p1 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L1[o0, p0, o1, p1 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L1[o0, p0, o1, p1 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L1[o0, p0, o1, p1 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L1[o0, p0, o1, p1 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L1[o0, p0, o1, p1 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L1[o0, p0, o1, p1 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L1[o0, p0, o1, p1 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L1[o0, p0, o1, p1, A, B], D02[o0,_,_,_]] with Log_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L1[o0, p0, o1, p1, A, B] with SelfJoin = ???
}


trait Log_0_2_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B] extends Log_0_2[o0, p0 with o1[p1 with o2[p2]], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L2[o0, p0, o1, p1, o2, p2 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], D02[o0,_,_,_]] with Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with SelfJoin = ???
}


trait Log_0_2_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B] extends Log_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], D02[o0,_,_,_]] with Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with SelfJoin = ???
}


trait Log_0_2_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B] extends Log_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], D02[o0,_,_,_]] with Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with SelfJoin = ???
}


trait Log_0_2_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B] extends Log_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], D02[o0,_,_,_]] with Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with SelfJoin = ???
}


trait Log_0_2_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B] extends Log_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], D02[o0,_,_,_]] with Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with SelfJoin = ???
}


trait Log_0_2_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B] extends Log_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B] {

  final lazy val e          : OneLong    [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_e        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_a        , A, B, String ], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_v        , A, B, Any    ], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_t        , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_tx       , A, B, Long   ], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_txInstant, A, B, Date   ], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_op       , A, B, Boolean], D03[o0,_,_,_,_]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_e$        , A, B, Option[Long   ]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_a$        , A, B, Option[String ]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_v$        , A, B, Option[Any    ]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_t$        , A, B, Option[Long   ]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_tx$       , A, B, Option[Long   ]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_txInstant$, A, B, Option[Date   ]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_op$       , A, B, Option[Boolean]]] with Log_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Log_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], D02[o0,_,_,_]] with Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  
  final def Self: Log_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with SelfJoin = ???
}

     
