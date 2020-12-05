package molecule.core.generic.index

import java.util.Date
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.outIndex._
import molecule.core.generic.GenericNs
import scala.language.higherKinds


/** Container for AEVT Index object. */
trait GenericAEVT {

  /** AEVT Index object to start AEVT Index molecule. */
  object AEVT extends AEVT_0 with FirstNS {

    /** Unfiltered AEVT Index fetching ALL datoms (!) */
    final def apply                                     : AEVT_0 = ???

    /** Instantiate AEVT Index filtered by namespace-prefixed attribute name (":part_Ns/attr"). */
    final def apply(a: String)                          : AEVT_0 = ???

    /** Instantiate AEVT Index filtered by attribute name and entity id. */
    final def apply(a: String, e: Long)                 : AEVT_0 = ???

    /** Instantiate AEVT Index filtered by attribute name, entity id and value. */
    final def apply(a: String, e: Long, v: Any)         : AEVT_0 = ???

    /** Instantiate AEVT Index filtered by attribute name, entity id, value and
      * transaction entity id (`tx`) or point in time (`t`).*/
    final def apply(a: String, e: Long, v: Any, t: Long): AEVT_0 = ???
  }
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
  * @see [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/generic/Index.scala#L1 Tests]]
  *     for more Index query examples.
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
}

/** AEVT interface to add a first generic attribute to molecule. */
trait AEVT_0 extends AEVT with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[AEVT_1[Type], P2[_,_]] with AEVT_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

/** AEVT interface to add a second generic attribute to molecule. */
trait AEVT_1[A] extends AEVT with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_2[A, Type], P3[_,_,_]] with AEVT_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_2[A, B] extends AEVT with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_3[A, B, Type], P4[_,_,_,_]] with AEVT_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_3[A, B, C] extends AEVT with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_4[A, B, C, Type], P5[_,_,_,_,_]] with AEVT_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_4[A, B, C, D] extends AEVT with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with AEVT_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_5[A, B, C, D, E] extends AEVT with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with AEVT_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_6[A, B, C, D, E, F] extends AEVT with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with AEVT_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_7[A, B, C, D, E, F, G] extends AEVT with OutIndex_7[A, B, C, D, E, F, G]

