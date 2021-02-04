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

trait Datom_0_0[o0[_], p0] extends Datom_[p0]

trait Datom_0_0_L0[o0[_], p0] extends Datom_0_0[o0, p0] {

  final lazy val e          : OneLong    [Datom_0_1_L0[o0, p0 with Datom_e        , Long   ], Datom_1_1_L0[o0, p0 with Datom_e        , Long   , Long   ]] with Datom_0_1_L0[o0, p0 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L0[o0, p0 with Datom_a        , String ], Datom_1_1_L0[o0, p0 with Datom_a        , String , String ]] with Datom_0_1_L0[o0, p0 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L0[o0, p0 with Datom_v        , Any    ], Datom_1_1_L0[o0, p0 with Datom_v        , Any    , Any    ]] with Datom_0_1_L0[o0, p0 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L0[o0, p0 with Datom_t        , Long   ], Datom_1_1_L0[o0, p0 with Datom_t        , Long   , Long   ]] with Datom_0_1_L0[o0, p0 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L0[o0, p0 with Datom_tx       , Long   ], Datom_1_1_L0[o0, p0 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L0[o0, p0 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L0[o0, p0 with Datom_txInstant, Date   ], Datom_1_1_L0[o0, p0 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L0[o0, p0 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L0[o0, p0 with Datom_op       , Boolean], Datom_1_1_L0[o0, p0 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L0[o0, p0 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L0[o0, p0 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L0[o0, p0 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L0[o0, p0 with Datom_a$        , Option[String ]]] with Datom_0_1_L0[o0, p0 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L0[o0, p0 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L0[o0, p0 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L0[o0, p0 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L0[o0, p0 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L0[o0, p0 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L0[o0, p0 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L0[o0, p0 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L0[o0, p0 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L0[o0, p0 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L0[o0, p0 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_e        , Long   ]] with Datom_0_0_L0[o0, p0] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_a        , String ]] with Datom_0_0_L0[o0, p0] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_v        , Any    ]] with Datom_0_0_L0[o0, p0] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_t        , Long   ]] with Datom_0_0_L0[o0, p0] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_tx       , Long   ]] with Datom_0_0_L0[o0, p0] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_txInstant, Date   ]] with Datom_0_0_L0[o0, p0] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L0[o0, p0], Datom_1_0_L0[o0, p0 with Datom_op       , Boolean]] with Datom_0_0_L0[o0, p0] with Indexed = ???
}


trait Datom_0_0_L1[o0[_], p0, o1[_], p1] extends Datom_0_0[o0, p0 with o1[p1]] {

  final lazy val e          : OneLong    [Datom_0_1_L1[o0, p0, o1, p1 with Datom_e        , Long   ], Datom_1_1_L1[o0, p0, o1, p1 with Datom_e        , Long   , Long   ]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L1[o0, p0, o1, p1 with Datom_a        , String ], Datom_1_1_L1[o0, p0, o1, p1 with Datom_a        , String , String ]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L1[o0, p0, o1, p1 with Datom_v        , Any    ], Datom_1_1_L1[o0, p0, o1, p1 with Datom_v        , Any    , Any    ]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L1[o0, p0, o1, p1 with Datom_t        , Long   ], Datom_1_1_L1[o0, p0, o1, p1 with Datom_t        , Long   , Long   ]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L1[o0, p0, o1, p1 with Datom_tx       , Long   ], Datom_1_1_L1[o0, p0, o1, p1 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L1[o0, p0, o1, p1 with Datom_txInstant, Date   ], Datom_1_1_L1[o0, p0, o1, p1 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L1[o0, p0, o1, p1 with Datom_op       , Boolean], Datom_1_1_L1[o0, p0, o1, p1 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L1[o0, p0, o1, p1 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L1[o0, p0, o1, p1 with Datom_a$        , Option[String ]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L1[o0, p0, o1, p1 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L1[o0, p0, o1, p1 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L1[o0, p0, o1, p1 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L1[o0, p0, o1, p1 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L1[o0, p0, o1, p1 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L1[o0, p0, o1, p1 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_e        , Long   ]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_a        , String ]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_v        , Any    ]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_t        , Long   ]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_tx       , Long   ]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_txInstant, Date   ]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L1[o0, p0, o1, p1], Datom_1_0_L1[o0, p0, o1, p1 with Datom_op       , Boolean]] with Datom_0_0_L1[o0, p0, o1, p1] with Indexed = ???
}


trait Datom_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2]]] {

  final lazy val e          : OneLong    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   ], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   , Long   ]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String ], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String , String ]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    ], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    , Any    ]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   ], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   , Long   ]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   ], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   ], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean], Datom_1_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , Option[String ]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , Long   ]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , String ]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , Any    ]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , Long   ]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , Long   ]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, Date   ]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L2[o0, p0, o1, p1, o2, p2], Datom_1_0_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , Boolean]] with Datom_0_0_L2[o0, p0, o1, p1, o2, p2] with Indexed = ???
}


trait Datom_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {

  final lazy val e          : OneLong    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   ], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   , Long   ]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String ], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String , String ]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    ], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    , Any    ]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   ], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   , Long   ]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   ], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   ], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean], Datom_1_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , Option[String ]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , Long   ]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , String ]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , Any    ]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , Long   ]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , Long   ]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, Date   ]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3], Datom_1_0_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , Boolean]] with Datom_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3] with Indexed = ???
}


trait Datom_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {

  final lazy val e          : OneLong    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   ], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   , Long   ]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String ], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String , String ]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    ], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    , Any    ]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   ], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   , Long   ]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   ], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   ], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean], Datom_1_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , Option[String ]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , Long   ]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , String ]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , Any    ]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , Long   ]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , Long   ]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, Date   ]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4], Datom_1_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , Boolean]] with Datom_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4] with Indexed = ???
}


trait Datom_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {

  final lazy val e          : OneLong    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   ], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   , Long   ]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String ], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String , String ]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    ], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    , Any    ]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   ], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   , Long   ]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   ], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   ], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean], Datom_1_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , Option[String ]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , Long   ]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , String ]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , Any    ]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , Long   ]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , Long   ]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, Date   ]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5], Datom_1_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , Boolean]] with Datom_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5] with Indexed = ???
}


trait Datom_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {

  final lazy val e          : OneLong    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   ], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   , Long   ]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String ], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String , String ]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    ], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    , Any    ]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   ], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   , Long   ]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   ], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   ], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean], Datom_1_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , Option[String ]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , Long   ]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , String ]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , Any    ]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , Long   ]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , Long   ]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, Date   ]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6], Datom_1_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , Boolean]] with Datom_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6] with Indexed = ???
}


trait Datom_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends Datom_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {

  final lazy val e          : OneLong    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   ], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   , Long   ]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String ], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String , String ]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    ], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    , Any    ]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   ], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   , Long   ]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   ], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   , Long   ]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   ], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   , Date   ]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean], Datom_1_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean, Boolean]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , Option[Long   ]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , Option[String ]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , Option[Any    ]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , Option[Long   ]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , Option[Long   ]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, Option[Date   ]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , Option[Boolean]]] with Datom_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , Long   ]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val a_         : OneString  [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , String ]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , Any    ]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , Long   ]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , Long   ]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, Date   ]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7], Datom_1_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , Boolean]] with Datom_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7] with Indexed = ???
}

     
