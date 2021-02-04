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

trait VAET_0_0[o0[_], p0] extends VAET_[p0] with NS_0_00[o0, p0]

trait VAET_0_0_L0[o0[_], p0] extends VAET_0_0[o0, p0] {

  final lazy val e          : OneLong    [VAET_0_1_L0[o0, p0 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L0[o0, p0 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L0[o0, p0 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L0[o0, p0 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L0[o0, p0 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L0[o0, p0 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L0[o0, p0 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L0[o0, p0 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L0[o0, p0 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L0[o0, p0 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L0[o0, p0 with VAET_a$        , Option[String ]]] with VAET_0_1_L0[o0, p0 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L0[o0, p0 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L0[o0, p0 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L0[o0, p0 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L0[o0, p0 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L0[o0, p0 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L0[o0, p0 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L0[o0, p0 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L0[o0, p0 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L0[o0, p0 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L0[o0, p0 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L0[o0, p0], D00[o0,_]] with VAET_0_0_L0[o0, p0] with Indexed = ???
}


trait VAET_0_0_L1[o0[_], p0, o1[_], p1] extends VAET_0_0[o0, p0 with o1[p1]] {

  final lazy val e          : OneLong    [VAET_0_1_L1[o0, p0, o1, p1 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L1[o0, p0, o1, p1 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L1[o0, p0, o1, p1 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L1[o0, p0, o1, p1 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L1[o0, p0, o1, p1 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L1[o0, p0, o1, p1 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L1[o0, p0, o1, p1 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L1[o0, p0, o1, p1 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L1[o0, p0, o1, p1 with VAET_a$        , Option[String ]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L1[o0, p0, o1, p1 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L1[o0, p0, o1, p1 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L1[o0, p0, o1, p1 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L1[o0, p0, o1, p1 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L1[o0, p0, o1, p1 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L1[o0, p0, o1, p1 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L1[o0, p0, o1, p1], D00[o0,_]] with VAET_0_0_L1[o0, p0, o1, p1] with Indexed = ???
}


trait VAET_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends VAET_0_0[o0, p0 with o1[p1 with o2[p2]]] {

  final lazy val e          : OneLong    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_a$        , Option[String ]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L2[o0, p0, o1, p1, o2, p2 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L2[o0, p0, o1, p1, o2, p2], D00[o0,_]] with VAET_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
}


trait VAET_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends VAET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {

  final lazy val e          : OneLong    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a$        , Option[String ]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], D00[o0,_]] with VAET_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
}


trait VAET_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends VAET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {

  final lazy val e          : OneLong    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a$        , Option[String ]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], D00[o0,_]] with VAET_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
}


trait VAET_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends VAET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {

  final lazy val e          : OneLong    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a$        , Option[String ]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], D00[o0,_]] with VAET_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
}


trait VAET_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends VAET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {

  final lazy val e          : OneLong    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a$        , Option[String ]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], D00[o0,_]] with VAET_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
}


trait VAET_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends VAET_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {

  final lazy val e          : OneLong    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e        , Long   ], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a        , String ], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v        , Any    ], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t        , Long   ], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx       , Long   ], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant, Date   ], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op       , Boolean], D01[o0,_,_]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e$        , Option[Long   ]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a$        , Option[String ]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v$        , Option[Any    ]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t$        , Option[Long   ]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx$       , Option[Long   ]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant$, Option[Date   ]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op$       , Option[Boolean]]] with VAET_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with VAET_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val a_         : OneString  [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val v_         : OneAny     [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val t_         : OneLong    [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val tx_        : OneLong    [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val txInstant_ : OneDate    [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val op_        : OneBoolean [VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], D00[o0,_]] with VAET_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
}

     
