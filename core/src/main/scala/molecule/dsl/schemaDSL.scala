package molecule.dsl
import java.net.URI
import java.util.{UUID, Date}
import molecule._
import molecule.ast.model._


object schemaDSL {

  trait NS

  trait FirstNS extends NS

  // Using dummy type parameter to simplify parsing DSL
  trait NS0[Dummy] extends NS
  trait NS1[A] extends NS
  trait NS2[A, B] extends NS
  trait NS3[A, B, C] extends NS
  trait NS4[A, B, C, D] extends NS
  trait NS5[A, B, C, D, E] extends NS
  trait NS6[A, B, C, D, E, F] extends NS
  trait NS7[A, B, C, D, E, F, G] extends NS
  trait NS8[A, B, C, D, E, F, G, H] extends NS
  trait NS9[A, B, C, D, E, F, G, H, I] extends NS
  trait NS10[A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS11[A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS12[A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS13[A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS23[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS24[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS25[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  // Dummy types used with generated trait types
  type P0 = Nothing
  type P1[A] = Nothing
  type P2[A, B] = Nothing
  type P3[A, B, C] = Nothing
  type P4[A, B, C, D] = Nothing
  type P5[A, B, C, D, E] = Nothing
  type P6[A, B, C, D, E, F] = Nothing
  type P7[A, B, C, D, E, F, G] = Nothing
  type P8[A, B, C, D, E, F, G, H] = Nothing
  type P9[A, B, C, D, E, F, G, H, I] = Nothing
  type P10[A, B, C, D, E, F, G, H, I, J] = Nothing
  type P11[A, B, C, D, E, F, G, H, I, J, K] = Nothing
  type P12[A, B, C, D, E, F, G, H, I, J, K, L] = Nothing
  type P13[A, B, C, D, E, F, G, H, I, J, K, L, M] = Nothing
  type P14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = Nothing
  type P15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = Nothing
  type P16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = Nothing
  type P17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = Nothing
  type P18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = Nothing
  type P19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = Nothing
  type P20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = Nothing
  type P21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = Nothing
  type P22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = Nothing
  type P23[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] = Nothing
  type P24[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] = Nothing
  type P25[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] = Nothing
  type P26[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z, AA] = Nothing
  type P27[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z, AA, AB] = Nothing

  trait SelfJoin

  trait Ref[This, Next]
  trait OneRef[This, Next] extends Ref[This, Next]
  trait ManyRef[This, Next] extends Ref[This, Next]

  //  import scalaz._
  //  trait ManyRefSelf2[A, B]
  //  {
  //    def tree(levels: Int): Seq[Tree[(A, B)]] = ???
  //  }
  //  trait ManyRefAttrSelf[Ns, In] extends ManyRefAttr[Ns, In] {
  //    def recurse(levels: Int): Ns with Attr = ???
  //  }

  trait Attr


  trait RefAttr[Ns, T] extends Attr {
    // Null reference (ref datom not asserted)
    def apply(noValue: nil): Ns with Attr = ???

    // Unifying marker for attributes to be unified in self-joins
    def apply(unifyThis: unify): Ns with Attr = ???
  }

  trait OneRefAttr[Ns, In] extends RefAttr[Ns,  Long] {
    def apply(ref: Long)      : Ns with Attr = ???
    def apply(refs: Seq[Long]): Ns with Attr = ???
  }

  trait ManyRefAttr[Ns, In] extends RefAttr[Ns,  Long] {
    def apply(values: Long*)                          : Ns with Attr = ???
    def apply(oneSet: Set[Long], moreSets: Set[Long]*): Ns with Attr = ??? // Todo: not implemented yet

    def add(value: Long)                              : Ns with Attr = ???
    def remove(values: Long*)                         : Ns with Attr = ???
  }

  trait BackRefAttr[Ns, In] extends RefAttr[Ns,  Long] {
    def apply(value: Long): Ns with Attr = ???
  }


  sealed trait ValueAttr[Ns, In, T, U] extends Attr {

    // Keyword for entity api
    val _kw: String = ""

    // Null (datom not asserted)
    def apply(noValue: nil): Ns with Attr = ???

    // Unifying marker for attributes to be unified in self-joins
    def apply(unifyThis: unify): Ns with Attr = ???

    // Negation
    def not(one: T, more: T*) : Ns with Attr = ???
    def not(many: Seq[T])     : Ns with Attr = ???
    // Same as
    def != (one: T, more: T*) : Ns with Attr = ???
    // Todo: remove this method when Intellij can infer from the above method alone...
    def != (one: T)           : Ns with Attr = ???
    def != (many: Seq[T])     : Ns with Attr = ???

    // Comparison
    def <  (value: T) : Ns with Attr = ???
    def >  (value: T) : Ns with Attr = ???
    def <= (value: T) : Ns with Attr = ???
    def >= (value: T) : Ns with Attr = ???

    // Input
    def apply(in: ?) : In with Attr = ???
    def not  (in: ?) : In with Attr = ???
    def !=   (in: ?) : In with Attr = ???
    def <    (in: ?) : In with Attr = ???
    def >    (in: ?) : In with Attr = ???
    def <=   (in: ?) : In with Attr = ???
    def >=   (in: ?) : In with Attr = ???

    def apply(expr1: Exp1[T])       : Ns with Attr = ???
    def apply(expr2: Exp2[T, T])    : Ns with Attr = ???
    def apply(expr3: Exp3[T, T, T]) : Ns with Attr = ???
  }

  // Cardinality one attributes

  trait One[Ns, In, T] extends ValueAttr[Ns, In, T, T] {
    // Empty `apply` is a request in insert molecules to delete values!
    def apply()                 : Ns with Attr = ???
    def apply(one: T, more: T*) : Ns with Attr = ???
    def apply(many: Seq[T])   : Ns with Attr = ???
  }
  trait OneString [Ns, In] extends One[Ns, In, String ]
  trait OneInt    [Ns, In] extends One[Ns, In, Int    ]
  trait OneLong   [Ns, In] extends One[Ns, In, Long   ]
  trait OneFloat  [Ns, In] extends One[Ns, In, Float  ]
  trait OneDouble [Ns, In] extends One[Ns, In, Double ]
  trait OneDate   [Ns, In] extends One[Ns, In, Date   ]
  trait OneBoolean[Ns, In] extends One[Ns, In, Boolean]
  trait OneUUID   [Ns, In] extends One[Ns, In, UUID   ]
  trait OneURI    [Ns, In] extends One[Ns, In, URI    ]
  trait OneAny    [Ns, In] extends One[Ns, In, Any    ]


  // Cardinality many attributes

  trait Many[Ns, In, S, T] extends ValueAttr[Ns, In, T, S] {
    def apply(values: T*)                          : Ns with Attr = ???
    def apply(set: S, moreSets: S*)                : Ns with Attr = ???
    def apply(oldNew: (T, T), oldNewMore: (T, T)*) : Ns with Attr = ???
    def add(value: T)                              : Ns with Attr = ???
    def remove(values: T*)                         : Ns with Attr = ???
  }
  trait ManyString [Ns, In] extends Many[Ns, In, Set[String ], String ]
  trait ManyInt    [Ns, In] extends Many[Ns, In, Set[Int    ], Int    ]
  trait ManyLong   [Ns, In] extends Many[Ns, In, Set[Long   ], Long   ]
  trait ManyFloat  [Ns, In] extends Many[Ns, In, Set[Float  ], Float  ]
  trait ManyDouble [Ns, In] extends Many[Ns, In, Set[Double ], Double ]
  trait ManyBoolean[Ns, In] extends Many[Ns, In, Set[Boolean], Boolean]
  trait ManyDate   [Ns, In] extends Many[Ns, In, Set[Date   ], Date   ]
  trait ManyUUID   [Ns, In] extends Many[Ns, In, Set[UUID   ], UUID   ]
  trait ManyURI    [Ns, In] extends Many[Ns, In, Set[URI    ], URI    ]


  // Map attributes

  trait MapAttrK

  trait MapAttr[Ns, In, M, T] extends ValueAttr[Ns, In, T, M]  {

    // Manipulation
    // Empty `apply` is a request to delete all key/values!
    def apply()                                         : Ns with Attr = ???
    def add(pair: (String, T), morePairs: (String, T)*) : Ns with Attr = ???
    def remove(key: String, moreKeys: String*)          : Ns with Attr = ???

    // Values
    def apply(value: T, more: T*)                        : Ns with Attr = ???
    def apply(set: Set[T], moreSets: Set[T]*)            : Ns with Attr = ???
    def apply(pair: (String, T), morePairs: (String, T)*): Ns with Attr = ???
    def apply(pairs: Seq[(String, T)])                   : Ns with Attr = ???
    def apply(pairs: Or[(String, T)])                    : Ns with Attr = ???

    // Keys
    def k(value: String)                : Values with Ns with Attr = ???
    def k(value: String, more: String*) : Values with Ns with Attr = ???
    def k(values: Seq[String])          : Values with Ns with Attr = ???
    def k(or: Or[String])               : Values with Ns with Attr = ???

    // Keyed attribute value methods
    trait Values {
      def apply(value: T, more: T*): Ns with Attr = ???
      def apply(values: Seq[T])    : Ns with Attr = ???
      def apply(or: Or[T])         : Ns with Attr = ???

      // Negation
      def not(one: T, more: T*)         : Ns with Attr = ???
      def != (one: T, more: T*) : Ns with Attr = ???

      // Comparison
      def <  (value: T) : Ns with Attr = ???
      def >  (value: T) : Ns with Attr = ???
      def <= (value: T) : Ns with Attr = ???
      def >= (value: T) : Ns with Attr = ???

      // Todo: How can we make those available to String type only?
      def contains(that: T): Ns with Attr = ???
      def contains(in: ?)  : In with Attr = ???
    }
  }

  trait MapAttr2[Ns, In, Ns1, In1, M, T] extends ValueAttr[Ns, In, T, M]  {

    // Manipulation
    // Empty `apply` is a request to delete all key/values!
    def apply()                                         : Ns with Attr = ???
    def add(pair: (String, T), morePairs: (String, T)*) : Ns with Attr = ???
    def remove(key: String, moreKeys: String*)          : Ns with Attr = ???

    // Values
    def apply(value: T, more: T*)                        : Ns with Attr = ???
    def apply(set: Set[T], moreSets: Set[T]*)            : Ns with Attr = ???
    def apply(pair: (String, T), morePairs: (String, T)*): Ns with Attr = ???
    def apply(pairs: Seq[(String, T)])                   : Ns with Attr = ???
    def apply(pairs: Or[(String, T)])                    : Ns with Attr = ???

    // Keys
    def k(value: String)                : Values with Ns with Attr = ???
    def k(value: String, more: String*) : Values with Ns with Attr = ???
    def k(values: Seq[String])          : Values with Ns with Attr = ???
    def k(or: Or[String])               : Values with Ns with Attr = ???

    // Keyed attribute value methods
    trait Values {
      def apply(value: T, more: T*): Ns with Attr = ???
      def apply(values: Seq[T])    : Ns with Attr = ???
      def apply(or: Or[T])         : Ns with Attr = ???

      // Negation
      def not(one: T, more: T*)         : Ns with Attr = ???
      def != (one: T, more: T*) : Ns with Attr = ???

      // Comparison
      def <  (value: T) : Ns with Attr = ???
      def >  (value: T) : Ns with Attr = ???
      def <= (value: T) : Ns with Attr = ???
      def >= (value: T) : Ns with Attr = ???

      // Todo: How can we make those available to String type only?
      def contains(that: T): Ns with Attr = ???
      def contains(in: ?)  : In with Attr = ???
    }
  }

  trait MapString [Ns, In] extends MapAttr[Ns, In, Map[String, String ], String ]
  trait MapInt    [Ns, In] extends MapAttr[Ns, In, Map[String, Int    ], Int    ]
  trait MapLong   [Ns, In] extends MapAttr[Ns, In, Map[String, Long   ], Long   ]
  trait MapFloat  [Ns, In] extends MapAttr[Ns, In, Map[String, Float  ], Float  ]
  trait MapDouble [Ns, In] extends MapAttr[Ns, In, Map[String, Double ], Double ]
  trait MapBoolean[Ns, In] extends MapAttr[Ns, In, Map[String, Boolean], Boolean]
  trait MapDate   [Ns, In] extends MapAttr[Ns, In, Map[String, Date   ], Date   ]
  trait MapUUID   [Ns, In] extends MapAttr[Ns, In, Map[String, UUID   ], UUID   ]
  trait MapURI    [Ns, In] extends MapAttr[Ns, In, Map[String, URI    ], URI    ]


  // Enums

  object EnumValue
  trait Enum
  trait OneEnum  [Ns, In] extends One [Ns, In, String]              with Enum
  trait ManyEnums[Ns, In] extends Many[Ns, In, Set[String], String] with Enum


  // Optionals

  trait RefAttr$ extends Attr
  trait OneRefAttr$  extends RefAttr$
  trait ManyRefAttr$ extends RefAttr$

  trait ValueAttr$[T] extends Attr

  trait OneValueAttr$[T]  extends ValueAttr$[T]
  trait OneString$  extends OneValueAttr$[String ]
  trait OneInt$     extends OneValueAttr$[Int    ]
  trait OneLong$    extends OneValueAttr$[Long   ]
  trait OneFloat$   extends OneValueAttr$[Float  ]
  trait OneDouble$  extends OneValueAttr$[Double ]
  trait OneBoolean$ extends OneValueAttr$[Boolean]
  trait OneDate$    extends OneValueAttr$[Date   ]
  trait OneUUID$    extends OneValueAttr$[UUID   ]
  trait OneURI$     extends OneValueAttr$[URI    ]

  trait ManyValueAttr$[T] extends ValueAttr$[T]
  trait ManyString$  extends ManyValueAttr$[Set[String ]]
  trait ManyInt$     extends ManyValueAttr$[Set[Int    ]]
  trait ManyLong$    extends ManyValueAttr$[Set[Long   ]]
  trait ManyFloat$   extends ManyValueAttr$[Set[Float  ]]
  trait ManyDouble$  extends ManyValueAttr$[Set[Double ]]
  trait ManyBoolean$ extends ManyValueAttr$[Set[Boolean]]
  trait ManyDate$    extends ManyValueAttr$[Set[Date   ]]
  trait ManyUUID$    extends ManyValueAttr$[Set[UUID   ]]
  trait ManyURI$     extends ManyValueAttr$[Set[URI    ]]

  trait MapAttr$[T] extends Attr
  trait MapString$  extends MapAttr$[Map[String, String        ]]
  trait MapInt$     extends MapAttr$[Map[String, Int           ]]
  trait MapLong$    extends MapAttr$[Map[String, Long          ]]
  trait MapFloat$   extends MapAttr$[Map[String, Float         ]]
  trait MapDouble$  extends MapAttr$[Map[String, Double        ]]
  trait MapBoolean$ extends MapAttr$[Map[String, Boolean       ]]
  trait MapDate$    extends MapAttr$[Map[String, java.util.Date]]
  trait MapUUID$    extends MapAttr$[Map[String, java.util.UUID]]
  trait MapURI$     extends MapAttr$[Map[String, java.net.URI  ]]

  trait Enum$
  trait OneEnum$   extends Enum$
  trait ManyEnums$ extends Enum$


  // Attribute options

  case class Doc(msg: String)
  trait UniqueValue
  trait UniqueIdentity
  trait Indexed
  trait FulltextSearch[Ns, In] {
    def contains(s: String, more: String*): Ns with Attr = ???
    def contains(in: ?)                   : In with Attr = ???
  }
  trait IsComponent
  trait NoHistory

  // Molecule-related options
  trait Bidirectional[revRef]
  trait ReverseRef[bidirectRef]
}