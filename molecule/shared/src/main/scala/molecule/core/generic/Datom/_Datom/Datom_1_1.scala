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

trait Datom_1_1[o0[_], p0, I1, A] extends Datom_[p0]

trait Datom_1_1_L0[o0[_], p0, I1, A] extends Datom_1_1[o0, p0, I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L0[o0, p0 with Datom_e        , I1, A, Long   ], Datom_2_2_L0[o0, p0 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L0[o0, p0 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L0[o0, p0 with Datom_a        , I1, A, String ], Datom_2_2_L0[o0, p0 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L0[o0, p0 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L0[o0, p0 with Datom_v        , I1, A, Any    ], Datom_2_2_L0[o0, p0 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L0[o0, p0 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L0[o0, p0 with Datom_t        , I1, A, Long   ], Datom_2_2_L0[o0, p0 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L0[o0, p0 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L0[o0, p0 with Datom_tx       , I1, A, Long   ], Datom_2_2_L0[o0, p0 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L0[o0, p0 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L0[o0, p0 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L0[o0, p0 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L0[o0, p0 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L0[o0, p0 with Datom_op       , I1, A, Boolean], Datom_2_2_L0[o0, p0 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L0[o0, p0 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L0[o0, p0 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L0[o0, p0 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L0[o0, p0 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L0[o0, p0 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L0[o0, p0 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L0[o0, p0 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L0[o0, p0 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L0[o0, p0 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L0[o0, p0 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L0[o0, p0 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L0[o0, p0 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L0[o0, p0 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L0[o0, p0 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L0[o0, p0 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_e        , I1, Long   , A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_a        , I1, String , A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_v        , I1, Any    , A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_t        , I1, Long   , A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L0[o0, p0, I1, A], Datom_2_1_L0[o0, p0 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L0[o0, p0, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L0[o0, p0, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L1[o0[_], p0, o1[_], p1, I1, A] extends Datom_1_1[o0, p0 with o1[p1], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L1[o0, p0, o1, p1 with Datom_e        , I1, A, Long   ], Datom_2_2_L1[o0, p0, o1, p1 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L1[o0, p0, o1, p1 with Datom_a        , I1, A, String ], Datom_2_2_L1[o0, p0, o1, p1 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L1[o0, p0, o1, p1 with Datom_v        , I1, A, Any    ], Datom_2_2_L1[o0, p0, o1, p1 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L1[o0, p0, o1, p1 with Datom_t        , I1, A, Long   ], Datom_2_2_L1[o0, p0, o1, p1 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L1[o0, p0, o1, p1 with Datom_tx       , I1, A, Long   ], Datom_2_2_L1[o0, p0, o1, p1 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L1[o0, p0, o1, p1 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L1[o0, p0, o1, p1 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L1[o0, p0, o1, p1 with Datom_op       , I1, A, Boolean], Datom_2_2_L1[o0, p0, o1, p1 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L1[o0, p0, o1, p1 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L1[o0, p0, o1, p1 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L1[o0, p0, o1, p1 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L1[o0, p0, o1, p1 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L1[o0, p0, o1, p1 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L1[o0, p0, o1, p1 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L1[o0, p0, o1, p1 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L1[o0, p0, o1, p1 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_e        , I1, Long   , A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_a        , I1, String , A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_v        , I1, Any    , A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_t        , I1, Long   , A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L1[o0, p0, o1, p1, I1, A], Datom_2_1_L1[o0, p0, o1, p1 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L1[o0, p0, o1, p1, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L1[o0, p0, o1, p1, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, I1, A] extends Datom_1_1[o0, p0 with o1[p1 with o2[p2]], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, A, Long   ], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, A, String ], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, A, Any    ], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, A, Long   ], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, A, Long   ], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, A, Boolean], Datom_2_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, Long   , A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, String , A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, Any    , A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, Long   , A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A], Datom_2_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L2[o0, p0, o1, p1, o2, p2, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, I1, A] extends Datom_1_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, A, Long   ], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, A, String ], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, A, Any    ], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, A, Long   ], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, A, Long   ], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, A, Boolean], Datom_2_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, Long   , A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, String , A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, Any    , A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, Long   , A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A], Datom_2_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, I1, A] extends Datom_1_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, A, Long   ], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, A, String ], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, A, Any    ], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, A, Long   ], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, A, Long   ], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, A, Boolean], Datom_2_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, Long   , A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, String , A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, Any    , A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, Long   , A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A], Datom_2_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, I1, A] extends Datom_1_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, A, Long   ], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, A, String ], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, A, Any    ], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, A, Long   ], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, A, Long   ], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, A, Boolean], Datom_2_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, Long   , A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, String , A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, Any    , A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, Long   , A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A], Datom_2_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, I1, A] extends Datom_1_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, A, Long   ], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, A, String ], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, A, Any    ], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, A, Long   ], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, A, Long   ], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, A, Boolean], Datom_2_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, Long   , A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, String , A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, Any    , A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, Long   , A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A], Datom_2_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, A] with SelfJoin = ???
}


trait Datom_1_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, I1, A] extends Datom_1_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], I1, A] {

  final lazy val e          : OneLong    [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, A, Long   ], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, Long   , A, Long   ]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, A, String ], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, String , A, String ]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, A, Any    ], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, Any    , A, Any    ]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, A, Long   ], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, Long   , A, Long   ]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, A, Long   ], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, Long   , A, Long   ]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, A, Date   ], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, Date   , A, Date   ]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, A, Boolean], Datom_2_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, Boolean, A, Boolean]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , I1, A, Option[Long   ]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , I1, A, Option[String ]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , I1, A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , I1, A, Option[Any    ]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , I1, A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , I1, A, Option[Long   ]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , I1, A, Option[Long   ]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , I1, A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, I1, A, Option[Date   ]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, I1, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , I1, A, Option[Boolean]]] with Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , I1, A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, Long   , A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, String , A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, Any    , A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, Long   , A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, Long   , A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, Date   , A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A], Datom_2_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, Boolean, A]] with Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with Indexed = ???
  
  final def Self: Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, A] with SelfJoin = ???
}

     
