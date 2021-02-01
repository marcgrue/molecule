package molecule.core._2_dsl.generic.index

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes._
import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.boilerplate.dummyTypes._
import molecule.core._2_dsl.boilerplate.outIndex._
import molecule.core._2_dsl.generic.GenericNs
import scala.language.higherKinds


/** Container for AEVT Index object. */
trait GenericAEVT {

  /** AEVT Index object to start AEVT Index molecule. */
  object AEVT extends AEVT_0[AEVT_, Nothing] with FirstNS {

    /** Unfiltered AEVT Index fetching ALL datoms (!) */
    final def apply                                     : AEVT_0[AEVT_, Nothing] = ???

    /** Instantiate AEVT Index filtered by namespace-prefixed attribute name (":part_Ns/attr"). */
    final def apply(a: String)                          : AEVT_0[AEVT_, Nothing] = ???

    /** Instantiate AEVT Index filtered by attribute name and entity id. */
    final def apply(a: String, e: Long)                 : AEVT_0[AEVT_, Nothing] = ???

    /** Instantiate AEVT Index filtered by attribute name, entity id and value. */
    final def apply(a: String, e: Long, v: Any)         : AEVT_0[AEVT_, Nothing] = ???

    /** Instantiate AEVT Index filtered by attribute name, entity id, value and
      * transaction entity id (`tx`) or point in time (`t`).*/
    final def apply(a: String, e: Long, v: Any, t: Long): AEVT_0[AEVT_, Nothing] = ???
  }
}

trait AEVT_[props] {
  def AEVT: props = ???
}

/** AEVT Index.
  *
  * "The AEVT index provides efficient access to all values for a given attribute,
  * comparable to traditional column access style."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the AEVT Index in Molecule by instantiating an AEVT object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Create AEVT Index molecule with 1 entity id argument
  *   AEVT(":Person/name").e.v.t.get === List(
  *     (e1, "Ben", t2),
  *     (e2, "Liz", t5)
  *   )
  *
  *   // Narrow search with multiple arguments
  *   AEVT(":Person/name", e1).e.v.get === List( (e1, "Ben") )
  *   AEVT(":Person/name", e1, "Ben").e.v.get === List( (e1, "Ben") )
  *   AEVT(":Person/name", e1, "Ben", t2).e.v.get === List( (e1, "Ben") )
  * }}}
  *
  * Index attributes available:
  *
  *  - '''`e`''' - Entity id (Long)
  *  - '''`a`''' - Full attribute name like ":Person/name" (String)
  *  - '''`v`''' - Value of Datoms (Any)
  *  - '''`t`''' - Transaction pointer (Long/Int)
  *  - '''`tx`''' - Transaction entity id (Long)
  *  - '''`txInstant`''' - Transaction wall clock time (java.util.Date)
  *  - '''`op`''' - Operation status: assertion (true) / retraction (false)
  *
  * @note The Molecule Index API's don't allow returning the whole Index/the whole database.
  *       So omitting arguments constructing the Index object (like `AEVT.a.e.v.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.AEVT)`
  * */
trait AEVT extends GenericNs {

  /** Entity id (Long) */
  final class e        [Ns, In] extends OneLong   [Ns, In] with Indexed

  /** (Partition)-Namespace-prefixed attribute name (":part_Ns/attr") */
  final class a        [Ns, In] extends OneString [Ns, In] with Indexed

  /** Datom value (Any)*/
  final class v        [Ns, In] extends OneAny    [Ns, In] with Indexed

  /** Transaction point in time `t` (Long/Int) */
  final class t        [Ns, In] extends OneLong   [Ns, In] with Indexed

  /** Transaction entity id (Long) */
  final class tx       [Ns, In] extends OneLong   [Ns, In] with Indexed

  /** Transaction wall-clock time (Date) */
  final class txInstant[Ns, In] extends OneDate   [Ns, In] with Indexed

  /** Transaction operation: assertion (true) or retraction (false) */
  final class op       [Ns, In] extends OneBoolean[Ns, In] with Indexed

  trait AEVT_e         { val e        : Long    }
  trait AEVT_a         { val a        : String  }
  trait AEVT_v         { val v        : Any     }
  trait AEVT_t         { val t        : Long    }
  trait AEVT_tx        { val tx       : Long    }
  trait AEVT_txInstant { val txInstant: Date    }
  trait AEVT_op        { val op       : Boolean }
}

/** AEVT interface to add a first generic attribute to molecule. */
trait AEVT_0[obj[_], props] extends AEVT with OutIndex_0[obj, props] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_1[obj, Prop, Tpe], _] with AEVT_1[obj, Prop, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

/** AEVT interface to add a second generic attribute to molecule. */
trait AEVT_1[obj[_], props, A] extends AEVT with OutIndex_1[obj, props, A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_2[obj, props, A, Tpe], _] with AEVT_2[obj, props with Prop, A, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

trait AEVT_2[obj[_], props, A, B] extends AEVT with OutIndex_2[obj, props, A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_3[obj, props, A, B, Tpe], _] with AEVT_3[obj, props with Prop, A, B, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

trait AEVT_3[obj[_], props, A, B, C] extends AEVT with OutIndex_3[obj, props, A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_4[obj, props, A, B, C, Tpe], _] with AEVT_4[obj, props with Prop, A, B, C, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

trait AEVT_4[obj[_], props, A, B, C, D] extends AEVT with OutIndex_4[obj, props, A, B, C, D] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_5[obj, props, A, B, C, D, Tpe], _] with AEVT_5[obj, props with Prop, A, B, C, D, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

trait AEVT_5[obj[_], props, A, B, C, D, E] extends AEVT with OutIndex_5[obj, props, A, B, C, D, E] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_6[obj, props, A, B, C, D, E, Tpe], _] with AEVT_6[obj, props with Prop, A, B, C, D, E, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

trait AEVT_6[obj[_], props, A, B, C, D, E, F] extends AEVT with OutIndex_6[obj, props, A, B, C, D, E, F] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[AEVT_7[obj, props, A, B, C, D, E, F, Tpe], _] with AEVT_7[obj, props with Prop, A, B, C, D, E, F, Tpe]

  final lazy val e          : Next[e         , AEVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , AEVT_a        , String ] = ???
  final lazy val v          : Next[v         , AEVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , AEVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , AEVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , AEVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , AEVT_op       , Boolean] = ???
}

trait AEVT_7[obj[_], props, A, B, C, D, E, F, G] extends AEVT with OutIndex_7[obj, props, A, B, C, D, E, F, G]

