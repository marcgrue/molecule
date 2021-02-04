/*
* AUTO-GENERATED Molecule DSL for namespace `Datom`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/DatomDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Datom._Datom
import molecule.core.generic.Datom._

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

trait Datom_0_1[o0[_], p0, A] extends Datom_[p0]

trait Datom_0_1_L0[o0[_], p0, A] extends Datom_0_1[o0, p0, A] {

  final lazy val e          : OneLong    [Datom_0_2_L0[o0, p0 with Datom_e        , A, Long   ], Datom_1_2_L0[o0, p0 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L0[o0, p0 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L0[o0, p0 with Datom_a        , A, String ], Datom_1_2_L0[o0, p0 with Datom_a        , String , A, String ]] with Datom_0_2_L0[o0, p0 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L0[o0, p0 with Datom_v        , A, Any    ], Datom_1_2_L0[o0, p0 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L0[o0, p0 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L0[o0, p0 with Datom_t        , A, Long   ], Datom_1_2_L0[o0, p0 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L0[o0, p0 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L0[o0, p0 with Datom_tx       , A, Long   ], Datom_1_2_L0[o0, p0 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L0[o0, p0 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L0[o0, p0 with Datom_txInstant, A, Date   ], Datom_1_2_L0[o0, p0 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L0[o0, p0 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L0[o0, p0 with Datom_op       , A, Boolean], Datom_1_2_L0[o0, p0 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L0[o0, p0 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L0[o0, p0 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L0[o0, p0 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L0[o0, p0 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L0[o0, p0 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L0[o0, p0 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L0[o0, p0 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L0[o0, p0 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L0[o0, p0 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L0[o0, p0 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L0[o0, p0 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L0[o0, p0 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L0[o0, p0 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L0[o0, p0 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L0[o0, p0 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_e        , Long   , A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_a        , String , A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_v        , Any    , A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_t        , Long   , A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_tx       , Long   , A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_txInstant, Date   , A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L0[o0, p0, A], Datom_1_1_L0[o0, p0 with Datom_op       , Boolean, A]] with Datom_0_1_L0[o0, p0, A] with Indexed = ???
  
  final def Self: Datom_0_1_L0[o0, p0, A] with SelfJoin = ???
}


trait Datom_0_1_L1[o0[_], p0, o1[_], p1, A] extends Datom_0_1[o0, p0 with o1[p1], A] {

  final lazy val e          : OneLong    [Datom_0_2_L1[o0, p0, o1, p1 with Datom_e        , A, Long   ], Datom_1_2_L1[o0, p0, o1, p1 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L1[o0, p0, o1, p1 with Datom_a        , A, String ], Datom_1_2_L1[o0, p0, o1, p1 with Datom_a        , String , A, String ]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L1[o0, p0, o1, p1 with Datom_v        , A, Any    ], Datom_1_2_L1[o0, p0, o1, p1 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L1[o0, p0, o1, p1 with Datom_t        , A, Long   ], Datom_1_2_L1[o0, p0, o1, p1 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L1[o0, p0, o1, p1 with Datom_tx       , A, Long   ], Datom_1_2_L1[o0, p0, o1, p1 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L1[o0, p0, o1, p1 with Datom_txInstant, A, Date   ], Datom_1_2_L1[o0, p0, o1, p1 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L1[o0, p0, o1, p1 with Datom_op       , A, Boolean], Datom_1_2_L1[o0, p0, o1, p1 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L1[o0, p0, o1, p1 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L1[o0, p0, o1, p1 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L1[o0, p0, o1, p1 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L1[o0, p0, o1, p1 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L1[o0, p0, o1, p1 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L1[o0, p0, o1, p1 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L1[o0, p0, o1, p1 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L1[o0, p0, o1, p1 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_e        , Long   , A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_a        , String , A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_v        , Any    , A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_t        , Long   , A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_tx       , Long   , A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_txInstant, Date   , A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L1[o0, p0, o1, p1, A], Datom_1_1_L1[o0, p0, o1, p1 with Datom_op       , Boolean, A]] with Datom_0_1_L1[o0, p0, o1, p1, A] with Indexed = ???
  
  final def Self: Datom_0_1_L1[o0, p0, o1, p1, A] with SelfJoin = ???
}


trait Datom_0_1_L2[o0[_], p0, o1[_], p1, o2[_], p2, A] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2]], A] {

  final lazy val e          : OneLong    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , A, Long   ], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , A, String ], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String , A, String ]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , A, Any    ], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , A, Long   ], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , A, Long   ], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, A, Date   ], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , A, Boolean], Datom_1_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   , A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String , A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    , A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   , A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   , A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   , A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean, A]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with Indexed = ???
  
  final def Self: Datom_0_1_L2[o0, p0, o1, p1, o2, p2, A] with SelfJoin = ???
}


trait Datom_0_1_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], A] {

  final lazy val e          : OneLong    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , A, Long   ], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , A, String ], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String , A, String ]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , A, Any    ], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , A, Long   ], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , A, Long   ], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, A, Date   ], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , A, Boolean], Datom_1_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   , A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String , A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    , A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   , A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   , A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   , A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean, A]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with Indexed = ???
  
  final def Self: Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3, A] with SelfJoin = ???
}


trait Datom_0_1_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], A] {

  final lazy val e          : OneLong    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , A, Long   ], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , A, String ], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String , A, String ]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , A, Any    ], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , A, Long   ], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , A, Long   ], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, A, Date   ], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , A, Boolean], Datom_1_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   , A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String , A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    , A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   , A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   , A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   , A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean, A]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with Indexed = ???
  
  final def Self: Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A] with SelfJoin = ???
}


trait Datom_0_1_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], A] {

  final lazy val e          : OneLong    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , A, Long   ], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , A, String ], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String , A, String ]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , A, Any    ], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , A, Long   ], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , A, Long   ], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, A, Date   ], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , A, Boolean], Datom_1_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   , A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String , A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    , A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   , A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   , A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   , A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean, A]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with Indexed = ???
  
  final def Self: Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A] with SelfJoin = ???
}


trait Datom_0_1_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], A] {

  final lazy val e          : OneLong    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , A, Long   ], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , A, String ], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String , A, String ]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , A, Any    ], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , A, Long   ], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , A, Long   ], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, A, Date   ], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , A, Boolean], Datom_1_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   , A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String , A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    , A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   , A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   , A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   , A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean, A]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with Indexed = ???
  
  final def Self: Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A] with SelfJoin = ???
}


trait Datom_0_1_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A] extends Datom_0_1[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], A] {

  final lazy val e          : OneLong    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , A, Long   ], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   , A, Long   ]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , A, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , A, String ], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String , A, String ]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , A, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , A, Any    ], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    , A, Any    ]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , A, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , A, Long   ], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   , A, Long   ]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , A, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , A, Long   ], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   , A, Long   ]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , A, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, A, Date   ], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   , A, Date   ]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, A, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , A, Boolean], Datom_1_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean, A, Boolean]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , A, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , A, Option[Long   ]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , A, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , A, Option[String ]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , A, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , A, Option[Any    ]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , A, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , A, Option[Long   ]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , A, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , A, Option[Long   ]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , A, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, A, Option[Date   ]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, A, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , A, Option[Boolean]]] with Datom_0_2_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , A, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   , A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String , A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    , A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   , A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   , A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   , A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean, A]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with Indexed = ???
  
  final def Self: Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A] with SelfJoin = ???
}

     
