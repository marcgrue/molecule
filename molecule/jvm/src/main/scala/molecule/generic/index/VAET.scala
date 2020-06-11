package molecule.generic.index

import java.util.Date
import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outIndex._
import molecule.generic.GenericNs
import scala.language.higherKinds


/** Container for VAET reverse Index object. */
trait GenericVAET {

  /** VAET Index object to start VAET reverse Index molecule. */
  object VAET extends VAET_0 with FirstNS {

    /** Instantiate VAET reverse Index filtered by ref entity id value. */
    final def apply(refId: Long)                             : VAET_0 = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value and attribute name. */
    final def apply(refId: Long, a: String)                  : VAET_0 = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value, attribute name and entity id. */
    final def apply(refId: Long, a: String, e: Long)         : VAET_0 = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value, attribute name, entity id and
      * transaction entity id (`tx`) or point in time (`t`). */
    final def apply(refId: Long, a: String, e: Long, t: Long): VAET_0 = ???
  }
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
  * @see [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/generic/Index.scala#L1 Tests]]
  *     for more Index query examples.
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
}

/** VAET interface to add a first generic attribute to molecule. */
trait VAET_0 extends VAET with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[VAET_1[Type], P2[_,_]] with VAET_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

/** VAET interface to add a second generic attribute to molecule. */
trait VAET_1[A] extends VAET with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[VAET_2[A, Type], P3[_,_,_]] with VAET_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait VAET_2[A, B] extends VAET with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[VAET_3[A, B, Type], P4[_,_,_,_]] with VAET_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait VAET_3[A, B, C] extends VAET with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[VAET_4[A, B, C, Type], P5[_,_,_,_,_]] with VAET_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait VAET_4[A, B, C, D] extends VAET with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[VAET_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with VAET_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait VAET_5[A, B, C, D, E] extends VAET with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[VAET_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with VAET_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait VAET_6[A, B, C, D, E, F] extends VAET with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[VAET_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with VAET_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Long   ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait VAET_7[A, B, C, D, E, F, G] extends VAET with OutIndex_7[A, B, C, D, E, F, G]

