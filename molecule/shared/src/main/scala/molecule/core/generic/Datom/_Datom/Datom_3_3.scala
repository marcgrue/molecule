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

trait Datom_3_3[o0[_], p0, I1, I2, I3, A, B, C] extends Datom_[p0]

trait Datom_3_3_L0[o0[_], p0, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0, I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L0[o0, p0 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L0[o0, p0 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L0[o0, p0 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L0[o0, p0 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L0[o0, p0 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L0[o0, p0 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L0[o0, p0 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L0[o0, p0 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L0[o0, p0 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L0[o0, p0 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L0[o0, p0 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L0[o0, p0 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L0[o0, p0 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L0[o0, p0 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L0[o0, p0 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L0[o0, p0 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L0[o0, p0 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L0[o0, p0 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L0[o0, p0 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L0[o0, p0 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L0[o0, p0 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L0[o0, p0 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L0[o0, p0, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L1[o0[_], p0, o1[_], p1, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L1[o0, p0, o1, p1 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L1[o0, p0, o1, p1 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L1[o0, p0, o1, p1 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L1[o0, p0, o1, p1 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L1[o0, p0, o1, p1 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L1[o0, p0, o1, p1 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L1[o0, p0, o1, p1 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L1[o0, p0, o1, p1 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L1[o0, p0, o1, p1 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L1[o0, p0, o1, p1 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L1[o0, p0, o1, p1 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L1[o0, p0, o1, p1 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L1[o0, p0, o1, p1 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L1[o0, p0, o1, p1 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L1[o0, p0, o1, p1 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L1[o0, p0, o1, p1, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L2[o0[_], p0, o1[_], p1, o2[_], p2, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1 with o2[p2]], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L2[o0, p0, o1, p1, o2, p2 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L2[o0, p0, o1, p1, o2, p2, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1 with o2[p2 with o3[p3]]], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L3[o0, p0, o1, p1, o2, p2, o3, p3, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, I1, I2, I3, A, B, C] with SelfJoin = ???
}


trait Datom_3_3_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, I1, I2, I3, A, B, C] extends Datom_3_3[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]], I1, I2, I3, A, B, C] {

  final lazy val e          : OneLong    [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val a          : OneString  [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, I2, I3, A, B, C, String ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a        , I1, I2, I3, A, B, C, String ] with Indexed = ???
  final lazy val v          : OneAny     [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, I2, I3, A, B, C, Any    ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v        , I1, I2, I3, A, B, C, Any    ] with Indexed = ???
  final lazy val t          : OneLong    [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t        , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val tx         : OneLong    [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, I2, I3, A, B, C, Long   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx       , I1, I2, I3, A, B, C, Long   ] with Indexed = ???
  final lazy val txInstant  : OneDate    [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant, I1, I2, I3, A, B, C, Date   ] with Indexed = ???
  final lazy val op         : OneBoolean [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, I2, I3, A, B, C, Boolean], D07[o0,_,_,_,_,_,_,_,_]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op       , I1, I2, I3, A, B, C, Boolean] with Indexed = ???
  
  final lazy val e$         : OneLong$   [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_e$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val a$         : OneString$ [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_a$        , I1, I2, I3, A, B, C, Option[String ]] with Indexed = ???
  final lazy val v$         : OneAny$    [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_v$        , I1, I2, I3, A, B, C, Option[Any    ]] with Indexed = ???
  final lazy val t$         : OneLong$   [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_t$        , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val tx$        : OneLong$   [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_tx$       , I1, I2, I3, A, B, C, Option[Long   ]] with Indexed = ???
  final lazy val txInstant$ : OneDate$   [Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_txInstant$, I1, I2, I3, A, B, C, Option[Date   ]] with Indexed = ???
  final lazy val op$        : OneBoolean$[Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]]] with Datom_3_4_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Datom_op$       , I1, I2, I3, A, B, C, Option[Boolean]] with Indexed = ???
  
  final lazy val e_         : OneLong    [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val a_         : OneString  [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val v_         : OneAny     [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val t_         : OneLong    [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val tx_        : OneLong    [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val txInstant_ : OneDate    [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  final lazy val op_        : OneBoolean [Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C], D06[o0,_,_,_,_,_,_,_]] with Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with Indexed = ???
  
  final def Self: Datom_3_3_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, I1, I2, I3, A, B, C] with SelfJoin = ???
}

     
