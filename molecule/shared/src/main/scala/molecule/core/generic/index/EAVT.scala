package molecule.core.generic.index

import java.util.Date
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.outIndex._
import molecule.core.generic.GenericNs
import scala.language.higherKinds


/** Container for EAVT Index object. */
trait GenericEAVT {

  /** EAVT Index object to instantiate EAVT Index molecule. */
  object EAVT extends EAVT_0[EAVT_, Nothing] with FirstNS {

    /** Unfiltered EAVT Index fetching ALL datoms (!) */
    final def apply                                     : EAVT_0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id. */
    final def apply(e: Long)                            : EAVT_0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id and namespace-prefixed
      * attribute name (":part_Ns/attr"). */
    final def apply(e: Long, a: String)                 : EAVT_0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id, attribute name and value. */
    final def apply(e: Long, a: String, v: Any)         : EAVT_0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id, attribute name, value and
      * transaction entity id (`tx`) or point in time (`t`). */
    final def apply(e: Long, a: String, v: Any, t: Long): EAVT_0[EAVT_, Nothing] = ???
  }
}

trait EAVT_[props] {
  def EAVT: props = ???
}

/** EAVT Index.
  *
  * "The EAVT index provides efficient access to everything about a given entity.
  * Conceptually this is very similar to row access style in a SQL database,
  * except that entities can possess arbitrary attributes rather then being limited
  * to a predefined set of columns."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the EAVT Index in Molecule by instantiating an EAVT object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Create EAVT Index molecule with 1 entity id argument
  *   EAVT(e1).e.a.v.t.get === List(
  *     (e1, ":Person/name", "Ben", t1),
  *     (e1, ":Person/age", 42, t2),
  *     (e1, ":Golf/score", 5.7, t2)
  *   )
  *
  *   // Narrow search with multiple arguments
  *   EAVT(e1, ":Person/age").a.v.get === List( (":Person/age", 42) )
  *   EAVT(e1, ":Person/age", 42).a.v.get === List( (":Person/age", 42) )
  *   EAVT(e1, ":Person/age", 42, t1).a.v.get === List( (":Person/age", 42) )
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
  *       So omitting arguments constructing the Index object (like `EAVT.e.a.v.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.EAVT)`
  * */
trait EAVT extends GenericNs {

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


  trait EAVT_e         { val e        : Long    }
  trait EAVT_a         { val a        : String  }
  trait EAVT_v         { val v        : Any     }
  trait EAVT_t         { val t        : Long    }
  trait EAVT_tx        { val tx       : Long    }
  trait EAVT_txInstant { val txInstant: Date    }
  trait EAVT_op        { val op       : Boolean }
}

/** EAVT interface to add a first generic attribute to molecule. */
trait EAVT_0[obj[_], props] extends EAVT with OutIndex_0[obj, props] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_1[obj, Prop, Tpe], _] with EAVT_1[obj, Prop, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

/** EAVT interface to add a second generic attribute to molecule. */
trait EAVT_1[obj[_], props, A] extends EAVT with OutIndex_1[obj, props, A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_2[obj, props, A, Tpe], _] with EAVT_2[obj, props with Prop, A, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

trait EAVT_2[obj[_], props, A, B] extends EAVT with OutIndex_2[obj, props, A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_3[obj, props, A, B, Tpe], _] with EAVT_3[obj, props with Prop, A, B, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

trait EAVT_3[obj[_], props, A, B, C] extends EAVT with OutIndex_3[obj, props, A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_4[obj, props, A, B, C, Tpe], _] with EAVT_4[obj, props with Prop, A, B, C, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

trait EAVT_4[obj[_], props, A, B, C, D] extends EAVT with OutIndex_4[obj, props, A, B, C, D] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_5[obj, props, A, B, C, D, Tpe], _] with EAVT_5[obj, props with Prop, A, B, C, D, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

trait EAVT_5[obj[_], props, A, B, C, D, E] extends EAVT with OutIndex_5[obj, props, A, B, C, D, E] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_6[obj, props, A, B, C, D, E, Tpe], _] with EAVT_6[obj, props with Prop, A, B, C, D, E, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

trait EAVT_6[obj[_], props, A, B, C, D, E, F] extends EAVT with OutIndex_6[obj, props, A, B, C, D, E, F] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[EAVT_7[obj, props, A, B, C, D, E, F, Tpe], _] with EAVT_7[obj, props with Prop, A, B, C, D, E, F, Tpe]

  final lazy val e          : Next[e         , EAVT_e        , Long   ] = ???
  final lazy val a          : Next[a         , EAVT_a        , String ] = ???
  final lazy val v          : Next[v         , EAVT_v        , Any    ] = ???
  final lazy val t          : Next[t         , EAVT_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , EAVT_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , EAVT_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , EAVT_op       , Boolean] = ???
}

trait EAVT_7[obj[_], props, A, B, C, D, E, F, G] extends EAVT with OutIndex_7[obj, props, A, B, C, D, E, F, G]
