package molecule.core.generic.index

import java.util.Date
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.outIndex._
import molecule.core.generic.GenericNs
import scala.language.higherKinds


/** Container for AVET Index object. */
trait GenericAVET {

  /** AVET Index object to start AVET Index molecule. */
  object AVET extends AVET_0 with FirstNS {

    /** Instantiate AVET Index filtered by namespace-prefixed attribute name (":part_Ns/attr"). */
    final def apply(a: String)                          : AVET_0 = ???

    /** Instantiate AVET Index filtered by attribute name and value. */
    final def apply(a: String, v: Any)                  : AVET_0 = ???

    /** Instantiate AVET Index filtered by attribute name, value and entity id. */
    final def apply(a: String, v: Any, e: Long)         : AVET_0 = ???

    /** Instantiate AVET Index filtered by attribute name, value, entity id and
      * transaction entity id (`tx`) or point in time (`t`).*/
    final def apply(a: String, v: Any, e: Long, t: Long): AVET_0 = ???


    /** Range of values (using Datomic's Database.indexRange API)
      *
      * Values between `from` (inclusive) and `until` (exclusive)
      *
      *  - From beginning if `from` is None
      *  - To end if `until` is None
      *
      * Molecule doesn't allow both to be None since it would return the whole database.
      * (Use Datomic raw access if that is needed)
      * */
    final def range(a: String, from: Option[Any], until: Option[Any]): AVET_0 = ???
  }
}

/** AVET Index.
  *
  * "The AVET index provides efficient access to particular combinations of attribute and value."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the AVET Index in Molecule by instantiating an AVET object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Create AVET Index molecule with 1 entity id argument
  *   AVET(":Person/age").e.v.t.get === List(
  *     (e1, 42, t2),
  *     (e2, 37, t5)
  *     (e3, 14, t7),
  *   )
  *
  *   // Narrow search with multiple arguments
  *   AVET(":Person/age", 42).e.t.get === List( (e1, t2) )
  *   AVET(":Person/age", 42, e1).e.v.get === List( (e1, t2) )
  *   AVET(":Person/age", 42, e1, t2).e.v.get === List( (e1, t2) )
  * }}}
  *
  * The AVET Index can be filtered by a range of values between `from` (inclusive) and
  * `until` (exclusive) for an attribute:
  * {{{
  *   AVET.range(":Person/age", Some(14), Some(37)).v.e.t.get === List(
  *     (14, e4, t7) // 14 is included in value range
  *                  // 37 not included in value range
  *                  // 42 outside value range
  *   )
  *
  *   // If `from` is None, the range starts from the beginning
  *   AVET.range(":Person/age", None, Some(40)).v.e.t.get === List(
  *     (14, e3, t7),
  *     (37, e2, t5),
  *   )
  *
  *   // If `until` is None, the range goes to the end
  *   AVET.range(":Person/age", Some(20), None).v.e.t.get === List(
  *     (37, e2, t5),
  *     (42, e1, t2)
  *   )
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
  *       So omitting arguments constructing the Index object (like `AVET.a.v.e.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.AVET)`
  *       <br><br>
  *       `from` and `until` cannot both be None since Molecule doesn't allow returning all datoms.
  * */
trait AVET extends GenericNs {

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

/** AVET interface to add a first generic attribute to molecule. */
trait AVET_0 extends AVET with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[AVET_1[Type], P2[_,_]] with AVET_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

/** AVET interface to add a second generic attribute to molecule. */
trait AVET_1[A] extends AVET with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[AVET_2[A, Type], P3[_,_,_]] with AVET_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AVET_2[A, B] extends AVET with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[AVET_3[A, B, Type], P4[_,_,_,_]] with AVET_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AVET_3[A, B, C] extends AVET with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[AVET_4[A, B, C, Type], P5[_,_,_,_,_]] with AVET_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AVET_4[A, B, C, D] extends AVET with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[AVET_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with AVET_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AVET_5[A, B, C, D, E] extends AVET with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[AVET_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with AVET_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AVET_6[A, B, C, D, E, F] extends AVET with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[AVET_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with AVET_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AVET_7[A, B, C, D, E, F, G] extends AVET with OutIndex_7[A, B, C, D, E, F, G]

