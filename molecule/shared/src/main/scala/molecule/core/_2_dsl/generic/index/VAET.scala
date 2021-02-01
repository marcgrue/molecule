package molecule.core._2_dsl.generic.index

import java.util.Date
import molecule.core._2_dsl.boilerplate.attributes._
import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.boilerplate.dummyTypes._
import molecule.core._2_dsl.boilerplate.outIndex._
import molecule.core._2_dsl.generic.GenericNs
import scala.language.higherKinds


/** Container for VAET reverse Index object. */
trait GenericVAET {

  /** VAET Index object to start VAET reverse Index molecule. */
  object VAET extends VAET_0[VAET_, Nothing] with FirstNS {

    /** Unfiltered VAET Index fetching ALL datoms (!) */
    final def apply                                          : VAET_0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value. */
    final def apply(refId: Long)                             : VAET_0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value and attribute name. */
    final def apply(refId: Long, a: String)                  : VAET_0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value, attribute name and entity id. */
    final def apply(refId: Long, a: String, e: Long)         : VAET_0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value, attribute name, entity id and
      * transaction entity id (`tx`) or point in time (`t`). */
    final def apply(refId: Long, a: String, e: Long, t: Long): VAET_0[VAET_, Nothing] = ???
  }
}

trait VAET_[props] {
  def VAET: props = ???
}

/** VAET reverse Index.
  *
  * "The VAET index contains all and only datoms whose attribute has a :db/valueType of :db.type/ref.
  * This is also known as the reverse index, since it allows efficient navigation of relationships in reverse."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the VAET Index in Molecule by instantiating a VAET object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Say we have 3 entities pointing to one entity:
  *   Release.e.name.Artists.e.name.get === List(
  *     (r1, "Abbey Road", a1, "The Beatles"),
  *     (r2, "Magical Mystery Tour", a1, "The Beatles"),
  *     (r3, "Let it be", a1, "The Beatles"),
  *   )
  *
  *   // .. then we can get the reverse relationships with the VAET Index:
  *   VAET(a1).v.a.e.get === List(
  *     (a1, ":Release/artists", r1),
  *     (a1, ":Release/artists", r2),
  *     (a1, ":Release/artists", r3),
  *   )
  *
  *   // Narrow search with multiple arguments
  *   VAET(a1, ":Release/artist").e.get === List(r1, r2, r3)
  *   VAET(a1, ":Release/artist", r2).e.get === List(r2)
  *   VAET(a1, ":Release/artist", r2, t7).e.get === List(r2)
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
  *       So omitting arguments constructing the Index object (like `VAET.v.a.e.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.VAET)`
  * */
trait VAET extends GenericNs {

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


  trait VAET_e         { val e        : Long    }
  trait VAET_a         { val a        : String  }
  trait VAET_v         { val v        : Any     }
  trait VAET_t         { val t        : Long    }
  trait VAET_tx        { val tx       : Long    }
  trait VAET_txInstant { val txInstant: Date    }
  trait VAET_op        { val op       : Boolean }
}

/** VAET interface to add a first generic attribute to molecule. */
trait VAET_0[obj[_], props] extends VAET with OutIndex_0[obj, props] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_1[obj, Prop, Tpe], _] with VAET_1[obj, Prop, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

/** VAET interface to add a second generic attribute to molecule. */
trait VAET_1[obj[_], props, A] extends VAET with OutIndex_1[obj, props, A] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_2[obj, props, A, Tpe], _] with VAET_2[obj, props with Prop, A, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

trait VAET_2[obj[_], props, A, B] extends VAET with OutIndex_2[obj, props, A, B] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_3[obj, props, A, B, Tpe], _] with VAET_3[obj, props with Prop, A, B, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

trait VAET_3[obj[_], props, A, B, C] extends VAET with OutIndex_3[obj, props, A, B, C] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_4[obj, props, A, B, C, Tpe], _] with VAET_4[obj, props with Prop, A, B, C, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

trait VAET_4[obj[_], props, A, B, C, D] extends VAET with OutIndex_4[obj, props, A, B, C, D] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_5[obj, props, A, B, C, D, Tpe], _] with VAET_5[obj, props with Prop, A, B, C, D, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

trait VAET_5[obj[_], props, A, B, C, D, E] extends VAET with OutIndex_5[obj, props, A, B, C, D, E] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_6[obj, props, A, B, C, D, E, Tpe], _] with VAET_6[obj, props with Prop, A, B, C, D, E, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

trait VAET_6[obj[_], props, A, B, C, D, E, F] extends VAET with OutIndex_6[obj, props, A, B, C, D, E, F] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[VAET_7[obj, props, A, B, C, D, E, F, Tpe], _] with VAET_7[obj, props with Prop, A, B, C, D, E, F, Tpe]

  final lazy val e          : Next[e         , VAET_e        , Long   ] = ???
  final lazy val a          : Next[a         , VAET_a        , String ] = ???
  final lazy val v          : Next[v         , VAET_v        , Any    ] = ???
  final lazy val t          : Next[t         , VAET_t        , Long   ] = ???
  final lazy val tx         : Next[tx        , VAET_tx       , Long   ] = ???
  final lazy val txInstant  : Next[txInstant , VAET_txInstant, Date   ] = ???
  final lazy val op         : Next[op        , VAET_op       , Boolean] = ???
}

trait VAET_7[obj[_], props, A, B, C, D, E, F, G] extends VAET with OutIndex_7[obj, props, A, B, C, D, E, F, G]
