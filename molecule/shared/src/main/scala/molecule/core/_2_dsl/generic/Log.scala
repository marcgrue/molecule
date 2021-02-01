package molecule.core._2_dsl.generic

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes._
import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.boilerplate.dummyTypes._
import molecule.core._2_dsl.boilerplate.outIndex._
import scala.language.higherKinds


/** Container for Log object. */
trait GenericLog {

  /** Log object to start Log molecule. */
  object Log extends Log_0[Log_, Nothing] with FirstNS {

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
    final def apply(from: Option[Any], until: Option[Any]): Log_0[Log_, Nothing] = ???

    final def apply(from: Option[Any]): Log_0[Log_, Nothing] = ???

    final def apply(): Log_0[Log_, Nothing] = ???
  }
}

trait Log_[props] {
  def Log: props = ???
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
  *     (t1, e1, ":Person/name", "Ben", true),
  *     (t1, e1, ":Person/age", 41, true),
  *
  *     (t2, e2, ":Person/name", "Liz", true),
  *     (t2, e2, ":Person/age", 37, true),
  *
  *     (t3, e1, ":Person/age", 41, false),
  *     (t3, e1, ":Person/age", 42, true)
  *   )
  *
  *   // If `from` is None, the range starts from the beginning
  *   Log(None, Some(t3)).v.e.t.get === List(
  *     (t1, e1, ":Person/name", "Ben", true),
  *     (t1, e1, ":Person/age", 41, true),
  *
  *     (t2, e2, ":Person/name", "Liz", true),
  *     (t2, e2, ":Person/age", 37, true)
  *
  *     // t3 not included
  *   )
  *
  *   // If `until` is None, the range goes to the end
  *   Log(Some(t2), None).v.e.t.get === List(
  *     // t1 not included
  *
  *     (t2, e2, ":Person/name", "Liz", true),
  *     (t2, e2, ":Person/age", 37, true),
  *
  *     (t3, e1, ":Person/age", 41, false),
  *     (t3, e1, ":Person/age", 42, true)
  *   )
  * }}}
  *
  * Log attributes available:
  *
  *  - '''`e`''' - Entity id (Long)
  *  - '''`a`''' - Full attribute name like ":Person/name" (String)
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

  trait Log_e         { val e: Long         }
  trait Log_a         { val a: String       }
  trait Log_v         { val v: Any          }
  trait Log_t         { val t: Long         }
  trait Log_tx        { val tx: Long        }
  trait Log_txInstant { val txInstant: Date }
  trait Log_op        { val op: Boolean     }
}


/** Log interface to add a first generic attribute to molecule. */
trait Log_0[obj[_], props] extends Log with OutIndex_0[obj, props] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_1[obj, Prop, Tpe], _] with Log_1[obj, Prop, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

/** Log interface to add a second generic attribute to molecule. */
trait Log_1[obj[_], props, A] extends Log with OutIndex_1[obj, props, A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_2[obj, props with Prop, A, Tpe], _] with Log_2[obj, props with Prop, A, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

trait Log_2[obj[_], props, A, B] extends Log with OutIndex_2[obj, props, A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_3[obj, props with Prop, A, B, Tpe], _] with Log_3[obj, props with Prop, A, B, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

trait Log_3[obj[_], props, A, B, C] extends Log with OutIndex_3[obj, props, A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_4[obj, props with Prop, A, B, C, Tpe], _] with Log_4[obj, props with Prop, A, B, C, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

trait Log_4[obj[_], props, A, B, C, D] extends Log with OutIndex_4[obj, props, A, B, C, D] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_5[obj, props with Prop, A, B, C, D, Tpe], _] with Log_5[obj, props with Prop, A, B, C, D, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

trait Log_5[obj[_], props, A, B, C, D, E] extends Log with OutIndex_5[obj, props, A, B, C, D, E] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_6[obj, props with Prop, A, B, C, D, E, Tpe], _] with Log_6[obj, props with Prop, A, B, C, D, E, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

trait Log_6[obj[_], props, A, B, C, D, E, F] extends Log with OutIndex_6[obj, props, A, B, C, D, E, F] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Log_7[obj, props with Prop, A, B, C, D, E, F, Tpe], _] with Log_7[obj, props with Prop, A, B, C, D, E, F, Tpe]

  final lazy val e          : Next[e         , Log_e        , Long   ] = ???
  final lazy val a          : Next[a         , Log_a        , String ] = ???
  final lazy val v          : Next[v         , Log_v        , Any    ] = ???
  final lazy val t          : Next[t         , Log_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , Log_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , Log_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , Log_op       , Boolean] = ???
}

trait Log_7[obj[_], props, A, B, C, D, E, F, G] extends Log with OutIndex_7[obj, props, A, B, C, D, E, F, G]


