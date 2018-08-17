package molecule.generic

import java.util.Date
import molecule.boilerplate.attributes._
import scala.language.higherKinds


/** Meta attributes of Datoms.
  * {{{
  *   // Get entity id with `e`
  *   Person.e.name.get.head === (17592186045894L, "Ben")
  *
  *   // Attributes of all Person entities having `age` asserted
  *   Person.a.age_.get.sorted === List(
  *     ":person/name",
  *     ":person/age",
  *     ":tag/score",
  *     ":tag/flags",
  *     ":cat/name"
  *   )
  *
  *   // Namespace names of all Person entities having `age` asserted
  *   Person.ns.age_.get.sorted === List(
  *     "person",
  *     "tag",
  *     "cat"
  *   )
  *
  *   // Transaction time t, operation and value
  *   Person.t.op.v.getHistory === List(
  *     (1031, true, "Benny"),
  *     (1033, false, "Benny"),
  *     (1033, true, "Ben")
  *   )
  *
  *   // Transaction time as date
  *   val date = java.util.Date("2018-08-04")
  *   Person.t.op.v.txInstant_(date).getHistory === List(
  *     (1033, true, "Ben")
  *   )
  *
  *   // Transaction entity id
  *   val txEntity = 17592186045908L
  *   Person.t.op.v.tx_(txEntity).getHistory === List(
  *     (1033, true, "Ben")
  *   )
  * }}}
  * @see [[http://www.scalamolecule.org/manual/time/history/ Manual]]
  *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/GetHistory.scala#L1 Tests]]
  */
trait Datom {

  /** Entity id. */
  def e: Any = ???

  /** Attribute name. */
  def a: Any = ???

  /** Attribute value. */
  def v: Any = ???

  /** Namespace name. */
  def ns: Any = ???

  /** Transaction entity id. */
  def tx: Any = ???

  /** Transaction time t. */
  def t: Any = ???

  /** Transaction time as java.util.Date. */
  def txInstant: Any = ???

  /** Transaction operation (add: True or retract: False). */
  def op: Any = ???


  // Tacit

  /** Tacit entity id. */
  def e_ : Any = ???

  /** Tacit attribute name. */
  def a_ : Any = ???

  /** Tacit attribute value. */
  def v_ : Any = ???

  /** Tacit namespace name. */
  def ns_ : Any = ???

  /** Tacit transaction entity id. */
  def tx_ : Any = ???

  /** Tacit transaction time t. */
  def t_ : Any = ???

  /** Tacit transaction time as java.util.Date. */
  def txInstant_ : Any = ???

  /** Tacit transaction operation (add: True or retract: False). */
  def op_ : Any = ???
}


object Datom {

  trait Datom00[Ns0, Ns1[_], In0[_], In1[_, _]] extends Datom {
    override def e          : Ns1[Long   ] with OneLong   [Ns1[Long   ], In1[Long   , Long   ]] = ???
    override def a          : Ns1[String ] with OneString [Ns1[String ], In1[String , String ]] = ???
    override def v          : Ns1[Any    ] with OneAny    [Ns1[Any    ], In1[Any    , Any    ]] = ???
    override def ns         : Ns1[String ] with OneString [Ns1[String ], In1[String , String ]] = ???
    override def tx         : Ns1[Long   ] with OneLong   [Ns1[Long   ], In1[Long   , Long   ]] = ???
    override def t          : Ns1[Long   ] with OneLong   [Ns1[Long   ], In1[Long   , Long   ]] = ???
    override def txInstant  : Ns1[Date   ] with OneDate   [Ns1[Date   ], In1[Date   , Date   ]] = ???
    override def op         : Ns1[Boolean] with OneBoolean[Ns1[Boolean], In1[Boolean, Boolean]] = ???

    override def e_         : Ns0 with OneLong   [Ns0, In0[Long   ]] = ???
    override def a_         : Ns0 with OneString [Ns0, In0[String ]] = ???
    override def v_         : Ns0 with OneAny    [Ns0, In0[Any    ]] = ???
    override def ns_        : Ns0 with OneString [Ns0, In0[String ]] = ???
    override def tx_        : Ns0 with OneLong   [Ns0, In0[Long   ]] = ???
    override def t_         : Ns0 with OneLong   [Ns0, In0[Long   ]] = ???
    override def txInstant_ : Ns0 with OneDate   [Ns0, In0[Date   ]] = ???
    override def op_        : Ns0 with OneBoolean[Ns0, In0[Boolean]] = ???
  }


  trait Datom01[Ns1[_], Ns2[_,_], In1[_,_], In2[_,_,_], A] extends Datom {
    override def e          : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
    override def a          : Ns2[A, String ] with OneString [Ns2[A, String ], In2[String , A, String ]] = ???
    override def v          : Ns2[A, Any    ] with OneAny    [Ns2[A, Any    ], In2[Any    , A, Any    ]] = ???
    override def ns         : Ns2[A, String ] with OneString [Ns2[A, String ], In2[String , A, String ]] = ???
    override def tx         : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
    override def t          : Ns2[A, Long   ] with OneLong   [Ns2[A, Long   ], In2[Long   , A, Long   ]] = ???
    override def txInstant  : Ns2[A, Date   ] with OneDate   [Ns2[A, Date   ], In2[Date   , A, Date   ]] = ???
    override def op         : Ns2[A, Boolean] with OneBoolean[Ns2[A, Boolean], In2[Boolean, A, Boolean]] = ???

    override def e_         : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
    override def a_         : Ns1[A] with OneString [Ns1[A], In1[String , A]] = ???
    override def v_         : Ns1[A] with OneAny    [Ns1[A], In1[Any    , A]] = ???
    override def ns_        : Ns1[A] with OneString [Ns1[A], In1[String , A]] = ???
    override def tx_        : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
    override def t_         : Ns1[A] with OneLong   [Ns1[A], In1[Long   , A]] = ???
    override def txInstant_ : Ns1[A] with OneDate   [Ns1[A], In1[Date   , A]] = ???
    override def op_        : Ns1[A] with OneBoolean[Ns1[A], In1[Boolean, A]] = ???
  }


  trait Datom02[Ns2[_,_], Ns3[_,_,_], In2[_,_,_], In3[_,_,_,_], A, B] extends Datom {
    override def e          : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
    override def a          : Ns3[A, B, String ] with OneString [Ns3[A, B, String ], In3[String , A, B, String ]] = ???
    override def v          : Ns3[A, B, Any    ] with OneAny    [Ns3[A, B, Any    ], In3[Any    , A, B, Any    ]] = ???
    override def ns         : Ns3[A, B, String ] with OneString [Ns3[A, B, String ], In3[String , A, B, String ]] = ???
    override def tx         : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
    override def t          : Ns3[A, B, Long   ] with OneLong   [Ns3[A, B, Long   ], In3[Long   , A, B, Long   ]] = ???
    override def txInstant  : Ns3[A, B, Date   ] with OneDate   [Ns3[A, B, Date   ], In3[Date   , A, B, Date   ]] = ???
    override def op         : Ns3[A, B, Boolean] with OneBoolean[Ns3[A, B, Boolean], In3[Boolean, A, B, Boolean]] = ???

    override def e_         : Ns2[A, B] with OneLong   [Ns2[A, B], In2[Long   , A, B]] = ???
    override def a_         : Ns2[A, B] with OneString [Ns2[A, B], In2[String , A, B]] = ???
    override def v_         : Ns2[A, B] with OneAny    [Ns2[A, B], In2[Any    , A, B]] = ???
    override def ns_        : Ns2[A, B] with OneString [Ns2[A, B], In2[String , A, B]] = ???
    override def tx_        : Ns2[A, B] with OneLong   [Ns2[A, B], In2[Long   , A, B]] = ???
    override def t_         : Ns2[A, B] with OneLong   [Ns2[A, B], In2[Long   , A, B]] = ???
    override def txInstant_ : Ns2[A, B] with OneDate   [Ns2[A, B], In2[Date   , A, B]] = ???
    override def op_        : Ns2[A, B] with OneBoolean[Ns2[A, B], In2[Boolean, A, B]] = ???
  }


  trait Datom03[Ns3[_,_,_], Ns4[_,_,_,_], In3[_,_,_,_], In4[_,_,_,_,_], A, B, C] extends Datom {
    override def e          : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
    override def a          : Ns4[A, B, C, String ] with OneString [Ns4[A, B, C, String ], In4[String , A, B, C, String ]] = ???
    override def v          : Ns4[A, B, C, Any    ] with OneAny    [Ns4[A, B, C, Any    ], In4[Any    , A, B, C, Any    ]] = ???
    override def ns         : Ns4[A, B, C, String ] with OneString [Ns4[A, B, C, String ], In4[String , A, B, C, String ]] = ???
    override def tx         : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
    override def t          : Ns4[A, B, C, Long   ] with OneLong   [Ns4[A, B, C, Long   ], In4[Long   , A, B, C, Long   ]] = ???
    override def txInstant  : Ns4[A, B, C, Date   ] with OneDate   [Ns4[A, B, C, Date   ], In4[Date   , A, B, C, Date   ]] = ???
    override def op         : Ns4[A, B, C, Boolean] with OneBoolean[Ns4[A, B, C, Boolean], In4[Boolean, A, B, C, Boolean]] = ???

    override def e_         : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
    override def a_         : Ns3[A, B, C] with OneString [Ns3[A, B, C], In3[String , A, B, C]] = ???
    override def v_         : Ns3[A, B, C] with OneAny    [Ns3[A, B, C], In3[Any    , A, B, C]] = ???
    override def ns_        : Ns3[A, B, C] with OneString [Ns3[A, B, C], In3[String , A, B, C]] = ???
    override def tx_        : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
    override def t_         : Ns3[A, B, C] with OneLong   [Ns3[A, B, C], In3[Long   , A, B, C]] = ???
    override def txInstant_ : Ns3[A, B, C] with OneDate   [Ns3[A, B, C], In3[Date   , A, B, C]] = ???
    override def op_        : Ns3[A, B, C] with OneBoolean[Ns3[A, B, C], In3[Boolean, A, B, C]] = ???
  }


  trait Datom04[Ns4[_,_,_,_], Ns5[_,_,_,_,_], In4[_,_,_,_,_], In5[_,_,_,_,_,_], A, B, C, D] extends Datom {
    override def e          : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
    override def a          : Ns5[A, B, C, D, String ] with OneString [Ns5[A, B, C, D, String ], In5[String , A, B, C, D, String ]] = ???
    override def v          : Ns5[A, B, C, D, Any    ] with OneAny    [Ns5[A, B, C, D, Any    ], In5[Any    , A, B, C, D, Any    ]] = ???
    override def ns         : Ns5[A, B, C, D, String ] with OneString [Ns5[A, B, C, D, String ], In5[String , A, B, C, D, String ]] = ???
    override def tx         : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
    override def t          : Ns5[A, B, C, D, Long   ] with OneLong   [Ns5[A, B, C, D, Long   ], In5[Long   , A, B, C, D, Long   ]] = ???
    override def txInstant  : Ns5[A, B, C, D, Date   ] with OneDate   [Ns5[A, B, C, D, Date   ], In5[Date   , A, B, C, D, Date   ]] = ???
    override def op         : Ns5[A, B, C, D, Boolean] with OneBoolean[Ns5[A, B, C, D, Boolean], In5[Boolean, A, B, C, D, Boolean]] = ???

    override def e_         : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
    override def a_         : Ns4[A, B, C, D] with OneString [Ns4[A, B, C, D], In4[String , A, B, C, D]] = ???
    override def v_         : Ns4[A, B, C, D] with OneAny    [Ns4[A, B, C, D], In4[Any    , A, B, C, D]] = ???
    override def ns_        : Ns4[A, B, C, D] with OneString [Ns4[A, B, C, D], In4[String , A, B, C, D]] = ???
    override def tx_        : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
    override def t_         : Ns4[A, B, C, D] with OneLong   [Ns4[A, B, C, D], In4[Long   , A, B, C, D]] = ???
    override def txInstant_ : Ns4[A, B, C, D] with OneDate   [Ns4[A, B, C, D], In4[Date   , A, B, C, D]] = ???
    override def op_        : Ns4[A, B, C, D] with OneBoolean[Ns4[A, B, C, D], In4[Boolean, A, B, C, D]] = ???
  }


  trait Datom05[Ns5[_,_,_,_,_], Ns6[_,_,_,_,_,_], In5[_,_,_,_,_,_], In6[_,_,_,_,_,_,_], A, B, C, D, E] extends Datom {

    override def e          : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
    override def a          : Ns6[A, B, C, D, E, String ] with OneString [Ns6[A, B, C, D, E, String ], In6[String , A, B, C, D, E, String ]] = ???
    override def v          : Ns6[A, B, C, D, E, Any    ] with OneAny    [Ns6[A, B, C, D, E, Any    ], In6[Any    , A, B, C, D, E, Any    ]] = ???
    override def ns         : Ns6[A, B, C, D, E, String ] with OneString [Ns6[A, B, C, D, E, String ], In6[String , A, B, C, D, E, String ]] = ???
    override def tx         : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
    override def t          : Ns6[A, B, C, D, E, Long   ] with OneLong   [Ns6[A, B, C, D, E, Long   ], In6[Long   , A, B, C, D, E, Long   ]] = ???
    override def txInstant  : Ns6[A, B, C, D, E, Date   ] with OneDate   [Ns6[A, B, C, D, E, Date   ], In6[Date   , A, B, C, D, E, Date   ]] = ???
    override def op         : Ns6[A, B, C, D, E, Boolean] with OneBoolean[Ns6[A, B, C, D, E, Boolean], In6[Boolean, A, B, C, D, E, Boolean]] = ???

    override def e_         : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
    override def a_         : Ns5[A, B, C, D, E] with OneString [Ns5[A, B, C, D, E], In5[String , A, B, C, D, E]] = ???
    override def v_         : Ns5[A, B, C, D, E] with OneAny    [Ns5[A, B, C, D, E], In5[Any    , A, B, C, D, E]] = ???
    override def ns_        : Ns5[A, B, C, D, E] with OneString [Ns5[A, B, C, D, E], In5[String , A, B, C, D, E]] = ???
    override def tx_        : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
    override def t_         : Ns5[A, B, C, D, E] with OneLong   [Ns5[A, B, C, D, E], In5[Long   , A, B, C, D, E]] = ???
    override def txInstant_ : Ns5[A, B, C, D, E] with OneDate   [Ns5[A, B, C, D, E], In5[Date   , A, B, C, D, E]] = ???
    override def op_        : Ns5[A, B, C, D, E] with OneBoolean[Ns5[A, B, C, D, E], In5[Boolean, A, B, C, D, E]] = ???
  }


  trait Datom06[Ns6[_,_,_,_,_,_], Ns7[_,_,_,_,_,_,_], In6[_,_,_,_,_,_,_], In7[_,_,_,_,_,_,_,_], A, B, C, D, E, F] extends Datom {
    override def e          : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
    override def a          : Ns7[A, B, C, D, E, F, String ] with OneString [Ns7[A, B, C, D, E, F, String ], In7[String , A, B, C, D, E, F, String ]] = ???
    override def v          : Ns7[A, B, C, D, E, F, Any    ] with OneAny    [Ns7[A, B, C, D, E, F, Any    ], In7[Any    , A, B, C, D, E, F, Any    ]] = ???
    override def ns         : Ns7[A, B, C, D, E, F, String ] with OneString [Ns7[A, B, C, D, E, F, String ], In7[String , A, B, C, D, E, F, String ]] = ???
    override def tx         : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
    override def t          : Ns7[A, B, C, D, E, F, Long   ] with OneLong   [Ns7[A, B, C, D, E, F, Long   ], In7[Long   , A, B, C, D, E, F, Long   ]] = ???
    override def txInstant  : Ns7[A, B, C, D, E, F, Date   ] with OneDate   [Ns7[A, B, C, D, E, F, Date   ], In7[Date   , A, B, C, D, E, F, Date   ]] = ???
    override def op         : Ns7[A, B, C, D, E, F, Boolean] with OneBoolean[Ns7[A, B, C, D, E, F, Boolean], In7[Boolean, A, B, C, D, E, F, Boolean]] = ???

    override def e_         : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
    override def a_         : Ns6[A, B, C, D, E, F] with OneString [Ns6[A, B, C, D, E, F], In6[String , A, B, C, D, E, F]] = ???
    override def v_         : Ns6[A, B, C, D, E, F] with OneAny    [Ns6[A, B, C, D, E, F], In6[Any    , A, B, C, D, E, F]] = ???
    override def ns_        : Ns6[A, B, C, D, E, F] with OneString [Ns6[A, B, C, D, E, F], In6[String , A, B, C, D, E, F]] = ???
    override def tx_        : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
    override def t_         : Ns6[A, B, C, D, E, F] with OneLong   [Ns6[A, B, C, D, E, F], In6[Long   , A, B, C, D, E, F]] = ???
    override def txInstant_ : Ns6[A, B, C, D, E, F] with OneDate   [Ns6[A, B, C, D, E, F], In6[Date   , A, B, C, D, E, F]] = ???
    override def op_        : Ns6[A, B, C, D, E, F] with OneBoolean[Ns6[A, B, C, D, E, F], In6[Boolean, A, B, C, D, E, F]] = ???
  }


  trait Datom07[Ns7[_,_,_,_,_,_,_], Ns8[_,_,_,_,_,_,_,_], In7[_,_,_,_,_,_,_,_], In8[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G] extends Datom {
    override def e          : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
    override def a          : Ns8[A, B, C, D, E, F, G, String ] with OneString [Ns8[A, B, C, D, E, F, G, String ], In8[String , A, B, C, D, E, F, G, String ]] = ???
    override def v          : Ns8[A, B, C, D, E, F, G, Any    ] with OneAny    [Ns8[A, B, C, D, E, F, G, Any    ], In8[Any    , A, B, C, D, E, F, G, Any    ]] = ???
    override def ns         : Ns8[A, B, C, D, E, F, G, String ] with OneString [Ns8[A, B, C, D, E, F, G, String ], In8[String , A, B, C, D, E, F, G, String ]] = ???
    override def tx         : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
    override def t          : Ns8[A, B, C, D, E, F, G, Long   ] with OneLong   [Ns8[A, B, C, D, E, F, G, Long   ], In8[Long   , A, B, C, D, E, F, G, Long   ]] = ???
    override def txInstant  : Ns8[A, B, C, D, E, F, G, Date   ] with OneDate   [Ns8[A, B, C, D, E, F, G, Date   ], In8[Date   , A, B, C, D, E, F, G, Date   ]] = ???
    override def op         : Ns8[A, B, C, D, E, F, G, Boolean] with OneBoolean[Ns8[A, B, C, D, E, F, G, Boolean], In8[Boolean, A, B, C, D, E, F, G, Boolean]] = ???

    override def e_         : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
    override def a_         : Ns7[A, B, C, D, E, F, G] with OneString [Ns7[A, B, C, D, E, F, G], In7[String , A, B, C, D, E, F, G]] = ???
    override def v_         : Ns7[A, B, C, D, E, F, G] with OneAny    [Ns7[A, B, C, D, E, F, G], In7[Any    , A, B, C, D, E, F, G]] = ???
    override def ns_        : Ns7[A, B, C, D, E, F, G] with OneString [Ns7[A, B, C, D, E, F, G], In7[String , A, B, C, D, E, F, G]] = ???
    override def tx_        : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
    override def t_         : Ns7[A, B, C, D, E, F, G] with OneLong   [Ns7[A, B, C, D, E, F, G], In7[Long   , A, B, C, D, E, F, G]] = ???
    override def txInstant_ : Ns7[A, B, C, D, E, F, G] with OneDate   [Ns7[A, B, C, D, E, F, G], In7[Date   , A, B, C, D, E, F, G]] = ???
    override def op_        : Ns7[A, B, C, D, E, F, G] with OneBoolean[Ns7[A, B, C, D, E, F, G], In7[Boolean, A, B, C, D, E, F, G]] = ???
  }


  trait Datom08[Ns8[_,_,_,_,_,_,_,_], Ns9[_,_,_,_,_,_,_,_,_], In8[_,_,_,_,_,_,_,_,_], In9[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] extends Datom {
    override def e          : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
    override def a          : Ns9[A, B, C, D, E, F, G, H, String ] with OneString [Ns9[A, B, C, D, E, F, G, H, String ], In9[String , A, B, C, D, E, F, G, H, String ]] = ???
    override def v          : Ns9[A, B, C, D, E, F, G, H, Any    ] with OneAny    [Ns9[A, B, C, D, E, F, G, H, Any    ], In9[Any    , A, B, C, D, E, F, G, H, Any    ]] = ???
    override def ns         : Ns9[A, B, C, D, E, F, G, H, String ] with OneString [Ns9[A, B, C, D, E, F, G, H, String ], In9[String , A, B, C, D, E, F, G, H, String ]] = ???
    override def tx         : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
    override def t          : Ns9[A, B, C, D, E, F, G, H, Long   ] with OneLong   [Ns9[A, B, C, D, E, F, G, H, Long   ], In9[Long   , A, B, C, D, E, F, G, H, Long   ]] = ???
    override def txInstant  : Ns9[A, B, C, D, E, F, G, H, Date   ] with OneDate   [Ns9[A, B, C, D, E, F, G, H, Date   ], In9[Date   , A, B, C, D, E, F, G, H, Date   ]] = ???
    override def op         : Ns9[A, B, C, D, E, F, G, H, Boolean] with OneBoolean[Ns9[A, B, C, D, E, F, G, H, Boolean], In9[Boolean, A, B, C, D, E, F, G, H, Boolean]] = ???

    override def e_         : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
    override def a_         : Ns8[A, B, C, D, E, F, G, H] with OneString [Ns8[A, B, C, D, E, F, G, H], In8[String , A, B, C, D, E, F, G, H]] = ???
    override def v_         : Ns8[A, B, C, D, E, F, G, H] with OneAny    [Ns8[A, B, C, D, E, F, G, H], In8[Any    , A, B, C, D, E, F, G, H]] = ???
    override def ns_        : Ns8[A, B, C, D, E, F, G, H] with OneString [Ns8[A, B, C, D, E, F, G, H], In8[String , A, B, C, D, E, F, G, H]] = ???
    override def tx_        : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
    override def t_         : Ns8[A, B, C, D, E, F, G, H] with OneLong   [Ns8[A, B, C, D, E, F, G, H], In8[Long   , A, B, C, D, E, F, G, H]] = ???
    override def txInstant_ : Ns8[A, B, C, D, E, F, G, H] with OneDate   [Ns8[A, B, C, D, E, F, G, H], In8[Date   , A, B, C, D, E, F, G, H]] = ???
    override def op_        : Ns8[A, B, C, D, E, F, G, H] with OneBoolean[Ns8[A, B, C, D, E, F, G, H], In8[Boolean, A, B, C, D, E, F, G, H]] = ???
  }


  trait Datom09[Ns9[_,_,_,_,_,_,_,_,_], Ns10[_,_,_,_,_,_,_,_,_,_], In9[_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] extends Datom {
    override def e          : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
    override def a          : Ns10[A, B, C, D, E, F, G, H, I, String ] with OneString [Ns10[A, B, C, D, E, F, G, H, I, String ], In10[String , A, B, C, D, E, F, G, H, I, String ]] = ???
    override def v          : Ns10[A, B, C, D, E, F, G, H, I, Any    ] with OneAny    [Ns10[A, B, C, D, E, F, G, H, I, Any    ], In10[Any    , A, B, C, D, E, F, G, H, I, Any    ]] = ???
    override def ns         : Ns10[A, B, C, D, E, F, G, H, I, String ] with OneString [Ns10[A, B, C, D, E, F, G, H, I, String ], In10[String , A, B, C, D, E, F, G, H, I, String ]] = ???
    override def tx         : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
    override def t          : Ns10[A, B, C, D, E, F, G, H, I, Long   ] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, Long   ], In10[Long   , A, B, C, D, E, F, G, H, I, Long   ]] = ???
    override def txInstant  : Ns10[A, B, C, D, E, F, G, H, I, Date   ] with OneDate   [Ns10[A, B, C, D, E, F, G, H, I, Date   ], In10[Date   , A, B, C, D, E, F, G, H, I, Date   ]] = ???
    override def op         : Ns10[A, B, C, D, E, F, G, H, I, Boolean] with OneBoolean[Ns10[A, B, C, D, E, F, G, H, I, Boolean], In10[Boolean, A, B, C, D, E, F, G, H, I, Boolean]] = ???

    override def e_         : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
    override def a_         : Ns9[A, B, C, D, E, F, G, H, I] with OneString [Ns9[A, B, C, D, E, F, G, H, I], In9[String , A, B, C, D, E, F, G, H, I]] = ???
    override def v_         : Ns9[A, B, C, D, E, F, G, H, I] with OneAny    [Ns9[A, B, C, D, E, F, G, H, I], In9[Any    , A, B, C, D, E, F, G, H, I]] = ???
    override def ns_        : Ns9[A, B, C, D, E, F, G, H, I] with OneString [Ns9[A, B, C, D, E, F, G, H, I], In9[String , A, B, C, D, E, F, G, H, I]] = ???
    override def tx_        : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
    override def t_         : Ns9[A, B, C, D, E, F, G, H, I] with OneLong   [Ns9[A, B, C, D, E, F, G, H, I], In9[Long   , A, B, C, D, E, F, G, H, I]] = ???
    override def txInstant_ : Ns9[A, B, C, D, E, F, G, H, I] with OneDate   [Ns9[A, B, C, D, E, F, G, H, I], In9[Date   , A, B, C, D, E, F, G, H, I]] = ???
    override def op_        : Ns9[A, B, C, D, E, F, G, H, I] with OneBoolean[Ns9[A, B, C, D, E, F, G, H, I], In9[Boolean, A, B, C, D, E, F, G, H, I]] = ???
  }


  trait Datom10[Ns10[_,_,_,_,_,_,_,_,_,_], Ns11[_,_,_,_,_,_,_,_,_,_,_], In10[_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] extends Datom {
    override def e          : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
    override def a          : Ns11[A, B, C, D, E, F, G, H, I, J, String ] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, String ], In11[String , A, B, C, D, E, F, G, H, I, J, String ]] = ???
    override def v          : Ns11[A, B, C, D, E, F, G, H, I, J, Any    ] with OneAny    [Ns11[A, B, C, D, E, F, G, H, I, J, Any    ], In11[Any    , A, B, C, D, E, F, G, H, I, J, Any    ]] = ???
    override def ns         : Ns11[A, B, C, D, E, F, G, H, I, J, String ] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, String ], In11[String , A, B, C, D, E, F, G, H, I, J, String ]] = ???
    override def tx         : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
    override def t          : Ns11[A, B, C, D, E, F, G, H, I, J, Long   ] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, Long   ], In11[Long   , A, B, C, D, E, F, G, H, I, J, Long   ]] = ???
    override def txInstant  : Ns11[A, B, C, D, E, F, G, H, I, J, Date   ] with OneDate   [Ns11[A, B, C, D, E, F, G, H, I, J, Date   ], In11[Date   , A, B, C, D, E, F, G, H, I, J, Date   ]] = ???
    override def op         : Ns11[A, B, C, D, E, F, G, H, I, J, Boolean] with OneBoolean[Ns11[A, B, C, D, E, F, G, H, I, J, Boolean], In11[Boolean, A, B, C, D, E, F, G, H, I, J, Boolean]] = ???

    override def e_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
    override def a_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneString [Ns10[A, B, C, D, E, F, G, H, I, J], In10[String , A, B, C, D, E, F, G, H, I, J]] = ???
    override def v_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneAny    [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Any    , A, B, C, D, E, F, G, H, I, J]] = ???
    override def ns_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneString [Ns10[A, B, C, D, E, F, G, H, I, J], In10[String , A, B, C, D, E, F, G, H, I, J]] = ???
    override def tx_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
    override def t_         : Ns10[A, B, C, D, E, F, G, H, I, J] with OneLong   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Long   , A, B, C, D, E, F, G, H, I, J]] = ???
    override def txInstant_ : Ns10[A, B, C, D, E, F, G, H, I, J] with OneDate   [Ns10[A, B, C, D, E, F, G, H, I, J], In10[Date   , A, B, C, D, E, F, G, H, I, J]] = ???
    override def op_        : Ns10[A, B, C, D, E, F, G, H, I, J] with OneBoolean[Ns10[A, B, C, D, E, F, G, H, I, J], In10[Boolean, A, B, C, D, E, F, G, H, I, J]] = ???
  }


  trait Datom11[Ns11[_,_,_,_,_,_,_,_,_,_,_], Ns12[_,_,_,_,_,_,_,_,_,_,_,_], In11[_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] extends Datom {
    override def e          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
    override def a          : Ns12[A, B, C, D, E, F, G, H, I, J, K, String ] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, String ], In12[String , A, B, C, D, E, F, G, H, I, J, K, String ]] = ???
    override def v          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Any    ] with OneAny    [Ns12[A, B, C, D, E, F, G, H, I, J, K, Any    ], In12[Any    , A, B, C, D, E, F, G, H, I, J, K, Any    ]] = ???
    override def ns         : Ns12[A, B, C, D, E, F, G, H, I, J, K, String ] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, String ], In12[String , A, B, C, D, E, F, G, H, I, J, K, String ]] = ???
    override def tx         : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
    override def t          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Long   ], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, Long   ]] = ???
    override def txInstant  : Ns12[A, B, C, D, E, F, G, H, I, J, K, Date   ] with OneDate   [Ns12[A, B, C, D, E, F, G, H, I, J, K, Date   ], In12[Date   , A, B, C, D, E, F, G, H, I, J, K, Date   ]] = ???
    override def op         : Ns12[A, B, C, D, E, F, G, H, I, J, K, Boolean] with OneBoolean[Ns12[A, B, C, D, E, F, G, H, I, J, K, Boolean], In12[Boolean, A, B, C, D, E, F, G, H, I, J, K, Boolean]] = ???

    override def e_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def a_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[String , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def v_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneAny    [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Any    , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def ns_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneString [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[String , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def tx_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def t_         : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneLong   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Long   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def txInstant_ : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneDate   [Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Date   , A, B, C, D, E, F, G, H, I, J, K]] = ???
    override def op_        : Ns11[A, B, C, D, E, F, G, H, I, J, K] with OneBoolean[Ns11[A, B, C, D, E, F, G, H, I, J, K], In11[Boolean, A, B, C, D, E, F, G, H, I, J, K]] = ???
  }


  trait Datom12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], In12[_,_,_,_,_,_,_,_,_,_,_,_,_], In13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] extends Datom {
    override def e          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
    override def a          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, String ]] = ???
    override def v          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Any    ] with OneAny    [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Any    ], In13[Any    , A, B, C, D, E, F, G, H, I, J, K, L, Any    ]] = ???
    override def ns         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, String ], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, String ]] = ???
    override def tx         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
    override def t          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Long   ], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, Long   ]] = ???
    override def txInstant  : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Date   ] with OneDate   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Date   ], In13[Date   , A, B, C, D, E, F, G, H, I, J, K, L, Date   ]] = ???
    override def op         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Boolean] with OneBoolean[Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Boolean], In13[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, Boolean]] = ???

    override def e_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def a_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[String , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def v_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneAny    [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Any    , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def ns_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneString [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[String , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def tx_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def t_         : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneLong   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Long   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def txInstant_ : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneDate   [Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Date   , A, B, C, D, E, F, G, H, I, J, K, L]] = ???
    override def op_        : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] with OneBoolean[Ns12[A, B, C, D, E, F, G, H, I, J, K, L], In12[Boolean, A, B, C, D, E, F, G, H, I, J, K, L]] = ???
  }


  trait Datom13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] extends Datom {
    override def e          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
    override def a          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, String ]] = ???
    override def v          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ] with OneAny    [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ], In14[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, Any    ]] = ???
    override def ns         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, String ], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, String ]] = ???
    override def tx         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
    override def t          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, Long   ]] = ???
    override def txInstant  : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ] with OneDate   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ], In14[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, Date   ]] = ???
    override def op         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean] with OneBoolean[Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean], In14[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, Boolean]] = ???

    override def e_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def a_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def v_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneAny    [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def ns_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneString [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[String , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def tx_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def t_         : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneLong   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def txInstant_ : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneDate   [Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
    override def op_        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] with OneBoolean[Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M], In13[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M]] = ???
  }


  trait Datom14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends Datom {
    override def e          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
    override def a          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ]] = ???
    override def v          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ] with OneAny    [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ], In15[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Any    ]] = ???
    override def ns         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, String ]] = ???
    override def tx         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
    override def t          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Long   ]] = ???
    override def txInstant  : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ] with OneDate   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ], In15[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, Date   ]] = ???
    override def op         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean] with OneBoolean[Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean], In15[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Boolean]] = ???

    override def e_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def a_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def v_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneAny    [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def ns_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneString [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def tx_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def t_         : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneLong   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def txInstant_ : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneDate   [Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
    override def op_        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] with OneBoolean[Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N], In14[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N]] = ???
  }


  trait Datom15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends Datom {
    override def e          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
    override def a          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ]] = ???
    override def v          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ] with OneAny    [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ], In16[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Any    ]] = ???
    override def ns         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, String ]] = ???
    override def tx         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
    override def t          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Long   ]] = ???
    override def txInstant  : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ] with OneDate   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ], In16[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Date   ]] = ???
    override def op         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean] with OneBoolean[Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean], In16[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Boolean]] = ???

    override def e_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def a_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def v_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneAny    [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def ns_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneString [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def tx_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def t_         : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneLong   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def txInstant_ : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneDate   [Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
    override def op_        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] with OneBoolean[Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O], In15[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] = ???
  }


  trait Datom16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends Datom {
    override def e          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
    override def a          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ]] = ???
    override def v          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ] with OneAny    [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ], In17[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Any    ]] = ???
    override def ns         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, String ]] = ???
    override def tx         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
    override def t          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Long   ]] = ???
    override def txInstant  : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ] with OneDate   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ], In17[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Date   ]] = ???
    override def op         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean] with OneBoolean[Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean], In17[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Boolean]] = ???

    override def e_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def a_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def v_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneAny    [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def ns_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneString [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def tx_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def t_         : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneLong   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def txInstant_ : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneDate   [Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
    override def op_        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] with OneBoolean[Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], In16[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] = ???
  }


  trait Datom17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends Datom {
    override def e          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
    override def a          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ]] = ???
    override def v          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ] with OneAny    [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ], In18[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Any    ]] = ???
    override def ns         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, String ]] = ???
    override def tx         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
    override def t          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Long   ]] = ???
    override def txInstant  : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ] with OneDate   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ], In18[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Date   ]] = ???
    override def op         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean] with OneBoolean[Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean], In18[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Boolean]] = ???

    override def e_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def a_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def v_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneAny    [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def ns_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneString [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def tx_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def t_         : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneLong   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def txInstant_ : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneDate   [Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
    override def op_        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] with OneBoolean[Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q], In17[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] = ???
  }


  trait Datom18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends Datom {
    override def e          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
    override def a          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ]] = ???
    override def v          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ] with OneAny    [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ], In19[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Any    ]] = ???
    override def ns         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, String ]] = ???
    override def tx         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
    override def t          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Long   ]] = ???
    override def txInstant  : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ] with OneDate   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ], In19[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Date   ]] = ???
    override def op         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean] with OneBoolean[Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean], In19[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Boolean]] = ???

    override def e_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def a_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def v_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneAny    [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def ns_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneString [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def tx_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def t_         : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneLong   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def txInstant_ : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneDate   [Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
    override def op_        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] with OneBoolean[Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R], In18[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] = ???
  }


  trait Datom19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends Datom {
    override def e          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
    override def a          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ]] = ???
    override def v          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ] with OneAny    [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ], In20[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Any    ]] = ???
    override def ns         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, String ]] = ???
    override def tx         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
    override def t          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Long   ]] = ???
    override def txInstant  : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ] with OneDate   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ], In20[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Date   ]] = ???
    override def op         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean] with OneBoolean[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean], In20[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Boolean]] = ???

    override def e_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def a_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def v_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneAny    [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def ns_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneString [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def tx_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def t_         : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneLong   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def txInstant_ : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneDate   [Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
    override def op_        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] with OneBoolean[Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S], In19[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] = ???
  }


  trait Datom20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends Datom {
    override def e          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
    override def a          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ]] = ???
    override def v          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ] with OneAny    [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ], In21[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Any    ]] = ???
    override def ns         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, String ]] = ???
    override def tx         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
    override def t          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Long   ]] = ???
    override def txInstant  : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ] with OneDate   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ], In21[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Date   ]] = ???
    override def op         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean] with OneBoolean[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean], In21[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Boolean]] = ???

    override def e_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def a_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def v_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneAny    [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def ns_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneString [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def tx_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def t_         : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneLong   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def txInstant_ : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneDate   [Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
    override def op_        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] with OneBoolean[Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T], In20[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] = ???
  }


  trait Datom21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends Datom {
    override def e          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
    override def a          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ]] = ???
    override def v          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ] with OneAny    [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ], In22[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Any    ]] = ???
    override def ns         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ], In22[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, String ]] = ???
    override def tx         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
    override def t          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ], In22[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Long   ]] = ???
    override def txInstant  : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ] with OneDate   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ], In22[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Date   ]] = ???
    override def op         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean] with OneBoolean[Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean], In22[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Boolean]] = ???

    override def e_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def a_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def v_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneAny    [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Any    , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def ns_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneString [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[String , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def tx_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def t_         : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneLong   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Long   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def txInstant_ : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneDate   [Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Date   , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
    override def op_        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] with OneBoolean[Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U], In21[Boolean, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] = ???
  }


  trait Datom22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends Datom {
    override def e_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def a_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def v_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneAny    [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def ns_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneString [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def tx_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def t_         : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneLong   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def txInstant_ : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneDate   [Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
    override def op_        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] with OneBoolean[Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], Nothing] = ???
  }
}