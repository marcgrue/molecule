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

trait Datom_0_2[o0[_], p0, A, B] extends Datom_[p0]

trait Datom_0_2_L0[o0[_], p0, A, B] extends Datom_0_2[o0, p0, A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L0[o0, p0 with Datom_e        , A, B, Long   ], Datom_1_3_L0[o0, p0 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L0[o0, p0 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L0[o0, p0 with Datom_a        , A, B, String ], Datom_1_3_L0[o0, p0 with Datom_a        , String , A, B, String ]] with Datom_0_3_L0[o0, p0 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L0[o0, p0 with Datom_v        , A, B, Any    ], Datom_1_3_L0[o0, p0 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L0[o0, p0 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L0[o0, p0 with Datom_t        , A, B, Long   ], Datom_1_3_L0[o0, p0 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L0[o0, p0 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L0[o0, p0 with Datom_tx       , A, B, Long   ], Datom_1_3_L0[o0, p0 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L0[o0, p0 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L0[o0, p0 with Datom_txInstant, A, B, Date   ], Datom_1_3_L0[o0, p0 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L0[o0, p0 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L0[o0, p0 with Datom_op       , A, B, Boolean], Datom_1_3_L0[o0, p0 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L0[o0, p0 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L0[o0, p0 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L0[o0, p0 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L0[o0, p0 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L0[o0, p0 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L0[o0, p0 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L0[o0, p0 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L0[o0, p0 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L0[o0, p0 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L0[o0, p0 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L0[o0, p0 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L0[o0, p0 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L0[o0, p0 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L0[o0, p0 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L0[o0, p0 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_e        , Long   , A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_a        , String , A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_v        , Any    , A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_t        , Long   , A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_tx       , Long   , A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L0[o0, p0, A, B], Datom_1_2_L0[o0, p0 with Datom_op       , Boolean, A, B]] with Datom_0_2_L0[o0, p0, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L0[o0, p0, A, B] with SelfJoin = ???
}


trait Datom_0_2_L1[o0[_], p0, o1[_], p1, A, B] extends Datom_0_2[o0, p0 with o1[p1], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L1[o0, p0, o1, p1 with Datom_e        , A, B, Long   ], Datom_1_3_L1[o0, p0, o1, p1 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L1[o0, p0, o1, p1 with Datom_a        , A, B, String ], Datom_1_3_L1[o0, p0, o1, p1 with Datom_a        , String , A, B, String ]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L1[o0, p0, o1, p1 with Datom_v        , A, B, Any    ], Datom_1_3_L1[o0, p0, o1, p1 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L1[o0, p0, o1, p1 with Datom_t        , A, B, Long   ], Datom_1_3_L1[o0, p0, o1, p1 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L1[o0, p0, o1, p1 with Datom_tx       , A, B, Long   ], Datom_1_3_L1[o0, p0, o1, p1 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L1[o0, p0, o1, p1 with Datom_txInstant, A, B, Date   ], Datom_1_3_L1[o0, p0, o1, p1 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L1[o0, p0, o1, p1 with Datom_op       , A, B, Boolean], Datom_1_3_L1[o0, p0, o1, p1 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L1[o0, p0, o1, p1 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L1[o0, p0, o1, p1 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L1[o0, p0, o1, p1 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L1[o0, p0, o1, p1 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L1[o0, p0, o1, p1 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L1[o0, p0, o1, p1 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L1[o0, p0, o1, p1 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L1[o0, p0, o1, p1 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_e        , Long   , A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_a        , String , A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_v        , Any    , A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_t        , Long   , A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_tx       , Long   , A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L1[o0, p0, o1, p1, A, B], Datom_1_2_L1[o0, p0, o1, p1 with Datom_op       , Boolean, A, B]] with Datom_0_2_L1[o0, p0, o1, p1, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L1[o0, p0, o1, p1, A, B] with SelfJoin = ???
}


trait Datom_0_2_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B] extends Datom_0_2[o0, p0 with o1[p1 with o2[p2]], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , A, B, Long   ], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , A, B, String ], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String , A, B, String ]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , A, B, Any    ], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , A, B, Long   ], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , A, B, Long   ], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, A, B, Date   ], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , A, B, Boolean], Datom_1_3_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   , A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String , A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    , A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   , A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   , A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean, A, B]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L2[o0, p0, o1, p1, o2, p2, A, B] with SelfJoin = ???
}


trait Datom_0_2_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B] extends Datom_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , A, B, Long   ], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , A, B, String ], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String , A, B, String ]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , A, B, Any    ], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , A, B, Long   ], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , A, B, Long   ], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, A, B, Date   ], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , A, B, Boolean], Datom_1_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   , A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String , A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    , A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   , A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   , A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean, A, B]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3, A, B] with SelfJoin = ???
}


trait Datom_0_2_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B] extends Datom_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , A, B, Long   ], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , A, B, String ], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String , A, B, String ]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , A, B, Any    ], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , A, B, Long   ], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , A, B, Long   ], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, A, B, Date   ], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , A, B, Boolean], Datom_1_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   , A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String , A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    , A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   , A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   , A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean, A, B]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B] with SelfJoin = ???
}


trait Datom_0_2_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B] extends Datom_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , A, B, Long   ], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , A, B, String ], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String , A, B, String ]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , A, B, Any    ], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , A, B, Long   ], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , A, B, Long   ], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, A, B, Date   ], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , A, B, Boolean], Datom_1_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   , A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String , A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    , A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   , A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   , A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean, A, B]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B] with SelfJoin = ???
}


trait Datom_0_2_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B] extends Datom_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , A, B, Long   ], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , A, B, String ], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String , A, B, String ]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , A, B, Any    ], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , A, B, Long   ], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , A, B, Long   ], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, A, B, Date   ], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , A, B, Boolean], Datom_1_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   , A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String , A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    , A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   , A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   , A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean, A, B]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B] with SelfJoin = ???
}


trait Datom_0_2_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B] extends Datom_0_2[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A, B] {

  final lazy val e          : OneLong    [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , A, B, Long   ], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   , A, B, Long   ]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , A, B, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , A, B, String ], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String , A, B, String ]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , A, B, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , A, B, Any    ], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    , A, B, Any    ]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , A, B, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , A, B, Long   ], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   , A, B, Long   ]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , A, B, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , A, B, Long   ], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   , A, B, Long   ]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , A, B, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, A, B, Date   ], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   , A, B, Date   ]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, A, B, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , A, B, Boolean], Datom_1_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean, A, B, Boolean]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , A, B, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , A, B, Option[Long   ]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , A, B, Option[String ]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , A, B, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , A, B, Option[Any    ]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , A, B, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , A, B, Option[Long   ]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , A, B, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , A, B, Option[Long   ]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , A, B, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, A, B, Option[Date   ]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, A, B, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , A, B, Option[Boolean]]] with Datom_0_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , A, B, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   , A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String , A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    , A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   , A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   , A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   , A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean, A, B]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with Indexed = ???
  
  final def Self: Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B] with SelfJoin = ???
}

     
