package molecule.meta.index

import java.util.Date
import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outIndex._
import molecule.meta.MetaNs
import scala.language.higherKinds


/** Container for EAVT Index object. */
trait GenericEAVT {

  /** EAVT Index object to instantiate EAVT Index molecule. */
  object EAVT extends EAVT_0 with FirstNS {

    /** Instantiate EAVT Index filtered by entity id. */
    final def apply(e: Long)                            : EAVT_0 = ???

    /** Instantiate EAVT Index filtered by entity id and namespace-prefixed
      * attribute name (":part_Ns/attr"). */
    final def apply(e: Long, a: String)                 : EAVT_0 = ???

    /** Instantiate EAVT Index filtered by entity id, attribute name and value. */
    final def apply(e: Long, a: String, v: Any)         : EAVT_0 = ???

    /** Instantiate EAVT Index filtered by entity id, attribute name, value and
      * transaction entity id (`tx`) or point in time (`t`). */
    final def apply(e: Long, a: String, v: Any, t: Long): EAVT_0 = ???
  }
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
  * or more arguments and then add meta attributes:
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
  * @see [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/generic/Index.scala#L1 Tests]]
  *     for more Index query examples.
  *
  * @note The Molecule Index API's don't allow returning the whole Index/the whole database.
  *       So omitting arguments constructing the Index object (like `EAVT.e.a.v.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.EAVT)`
  * */
trait EAVT extends MetaNs {

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
}

/** EAVT interface to add a first generic attribute to molecule. */
trait EAVT_0 extends EAVT with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[EAVT_1[Type], P2[_,_]] with EAVT_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

/** EAVT interface to add a second generic attribute to molecule. */
trait EAVT_1[A] extends EAVT with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[EAVT_2[A, Type], P3[_,_,_]] with EAVT_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait EAVT_2[A, B] extends EAVT with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[EAVT_3[A, B, Type], P4[_,_,_,_]] with EAVT_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait EAVT_3[A, B, C] extends EAVT with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[EAVT_4[A, B, C, Type], P5[_,_,_,_,_]] with EAVT_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait EAVT_4[A, B, C, D] extends EAVT with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[EAVT_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with EAVT_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait EAVT_5[A, B, C, D, E] extends EAVT with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[EAVT_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with EAVT_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait EAVT_6[A, B, C, D, E, F] extends EAVT with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[EAVT_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with EAVT_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait EAVT_7[A, B, C, D, E, F, G] extends EAVT with OutIndex_7[A, B, C, D, E, F, G]

