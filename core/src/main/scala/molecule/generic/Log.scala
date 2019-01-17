package molecule.generic

import java.util.Date
import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outIndex._
import scala.language.higherKinds


/** Container for Log object. */
trait GenericLog {

  /** Log object to start Log molecule. */
  object Log extends Log_0 with FirstNS {

    /** Range of transactions (using Datomic's Log.txRange API)
      *
      * Returns transactions between transaction points in time `from` (inclusive)
      * and `until` (exclusive).
      *
      * Option args can be
      *
      * - Transaction point in time (`t`) of type Long/Int
      * - Transaction entity id (`tx`) of type Long
      * - Transaction wall clock time (`txInstant`) of type java.util.Date
      *
      * If `from` is None, transactions from beginning are fetched.<br>
      * If `until` is None, transaction until end are fetched.<br>
      *
      * Molecule doesn't allow both arguments to be None since it would return the
      * whole database. (Use Datomic raw access if that is needed)
      *
      * @param from First tx (inclusive). Beginning if None
      * @param until Last tx (exclusive). End if None
      * @return Log builder object to add generic datom attributes
      */
    final def apply(from: Option[Any], until: Option[Any]): Log_0 = ???
  }
}

/** Log interface.
  *<br><br>
  * Datomic's database log is a recording of all transaction data in historic order,
  * organized for efficient access by transaction.
  *<br><br>
  * Instantiate Log object with tx range arguments between `from` (inclusive) and `until` (exclusive),
  * and add Log attributes to be returned as tuples of data:
  * {{{
  *   // Data from transaction t1 until t4 (exclusive)
  *   Log(Some(t1), Some(t4)).t.e.a.v.op.get === List(
  *     (t1, e1, ":person/name", "Ben", true),
  *     (t1, e1, ":person/age", 41, true),
  *
  *     (t2, e2, ":person/name", "Liz", true),
  *     (t2, e2, ":person/age", 37, true),
  *
  *     (t3, e1, ":person/age", 41, false),
  *     (t3, e1, ":person/age", 42, true)
  *   )
  *
  *   // If `from` is None, the range starts from the beginning
  *   Log(None, Some(t3)).v.e.t.get === List(
  *     (t1, e1, ":person/name", "Ben", true),
  *     (t1, e1, ":person/age", 41, true),
  *
  *     (t2, e2, ":person/name", "Liz", true),
  *     (t2, e2, ":person/age", 37, true)
  *
  *     // t3 not included
  *   )
  *
  *   // If `until` is None, the range goes to the end
  *   Log(Some(t2), None).v.e.t.get === List(
  *     // t1 not included
  *
  *     (t2, e2, ":person/name", "Liz", true),
  *     (t2, e2, ":person/age", 37, true),
  *
  *     (t3, e1, ":person/age", 41, false),
  *     (t3, e1, ":person/age", 42, true)
  *   )
  * }}}
  *
  * Log attributes available:
  *
  *  - '''`e`''' - Entity id (Long)
  *  - '''`a`''' - Full attribute name like ":person/name" (String)
  *  - '''`v`''' - Value of Datoms (Any)
  *  - '''`t`''' - Transaction pointer (Long/Int)
  *  - '''`tx`''' - Transaction entity id (Long)
  *  - '''`txInstant`''' - Transaction wall clock time (java.util.Date)
  *  - '''`op`''' - Operation status: assertion (true) / retraction (false)
  *
  * @note Contrary to the Datomic Log which is map of individual transactions
  * the Molecule Log implementation is flattened to be one continuous list
  * of transaction data. This is to have a transparent unified return type
  * as all other molecules returning data. Data can always be grouped if needed.
  */
trait Log extends GenericNs {

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


/** Log interface to add a first generic attribute to molecule. */
trait Log_0 extends Log with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[Log_1[Type], P2[_,_]] with Log_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

/** Log interface to add a second generic attribute to molecule. */
trait Log_1[A] extends Log with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[Log_2[A, Type], P3[_,_,_]] with Log_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_2[A, B] extends Log with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[Log_3[A, B, Type], P4[_,_,_,_]] with Log_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_3[A, B, C] extends Log with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[Log_4[A, B, C, Type], P5[_,_,_,_,_]] with Log_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_4[A, B, C, D] extends Log with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[Log_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with Log_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_5[A, B, C, D, E] extends Log with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[Log_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with Log_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_6[A, B, C, D, E, F] extends Log with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[Log_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with Log_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_7[A, B, C, D, E, F, G] extends Log with OutIndex_7[A, B, C, D, E, F, G]

