package molecule.boilerplate

import java.net.URI
import java.util.{Date, UUID}
import molecule.dsl._
//import molecule.dsl.expr.?
import molecule._


object attributes {




  trait SelfJoin

  trait Ref[This, Next]
  trait OneRef[This, Next] extends Ref[This, Next]
  trait ManyRef[This, Next] extends Ref[This, Next]

  trait Attr

  trait RefAttr[Ns, T] extends RefAttrOps[Ns, T] with Attr

  trait OneRefAttr[Ns, In] extends OneRefAttrOps[Ns, In] with RefAttr[Ns,  Long]

  trait ManyRefAttr[Ns, In] extends ManyRefAttrOps[Ns, In] with RefAttr[Ns,  Long]

  trait BackRefAttr[Ns, In] extends BackRefAttrOps[Ns, In] with RefAttr[Ns,  Long]


  sealed trait ValueAttr[Ns, In, T, U] extends ValueAttrOps[Ns, In, T, U] with Attr

  // Cardinality one attributes

  trait One[Ns, In, T] extends OneOps[Ns, In, T] with ValueAttr[Ns, In, T, T]
  trait OneString [Ns, In] extends One[Ns, In, String    ]
  trait OneInt    [Ns, In] extends One[Ns, In, Int       ]
  trait OneLong   [Ns, In] extends One[Ns, In, Long      ]
  trait OneFloat  [Ns, In] extends One[Ns, In, Float     ]
  trait OneDouble [Ns, In] extends One[Ns, In, Double    ]
  trait OneBoolean[Ns, In] extends One[Ns, In, Boolean   ]
  trait OneBigInt [Ns, In] extends One[Ns, In, BigInt    ]
  trait OneBigDec [Ns, In] extends One[Ns, In, BigDecimal]
  trait OneDate   [Ns, In] extends One[Ns, In, Date      ]
  trait OneUUID   [Ns, In] extends One[Ns, In, UUID      ]
  trait OneURI    [Ns, In] extends One[Ns, In, URI       ]
  trait OneByte   [Ns, In] extends One[Ns, In, Byte      ]
  trait OneAny    [Ns, In] extends One[Ns, In, Any       ]


  // Cardinality many attributes

  trait Many[Ns, In, S, T] extends ManyOps[Ns, In, S, T] with ValueAttr[Ns, In, T, S]
  trait ManyString [Ns, In] extends Many[Ns, In, Set[String    ], String    ]
  trait ManyInt    [Ns, In] extends Many[Ns, In, Set[Int       ], Int       ]
  trait ManyLong   [Ns, In] extends Many[Ns, In, Set[Long      ], Long      ]
  trait ManyFloat  [Ns, In] extends Many[Ns, In, Set[Float     ], Float     ]
  trait ManyDouble [Ns, In] extends Many[Ns, In, Set[Double    ], Double    ]
  trait ManyBoolean[Ns, In] extends Many[Ns, In, Set[Boolean   ], Boolean   ]
  trait ManyBigInt [Ns, In] extends Many[Ns, In, Set[BigInt    ], BigInt    ]
  trait ManyBigDec [Ns, In] extends Many[Ns, In, Set[BigDecimal], BigDecimal]
  trait ManyDate   [Ns, In] extends Many[Ns, In, Set[Date      ], Date      ]
  trait ManyUUID   [Ns, In] extends Many[Ns, In, Set[UUID      ], UUID      ]
  trait ManyURI    [Ns, In] extends Many[Ns, In, Set[URI       ], URI       ]
  trait ManyByte   [Ns, In] extends Many[Ns, In, Set[Byte      ], Byte      ]


  // Map attributes

  trait MapAttrK

  trait MapAttr[Ns, In, M, T] extends MapAttrOps[Ns, In, M, T] with ValueAttr[Ns, In, T, M]

  trait MapString [Ns, In] extends MapAttr[Ns, In, Map[String, String    ], String    ]
  trait MapInt    [Ns, In] extends MapAttr[Ns, In, Map[String, Int       ], Int       ]
  trait MapLong   [Ns, In] extends MapAttr[Ns, In, Map[String, Long      ], Long      ]
  trait MapFloat  [Ns, In] extends MapAttr[Ns, In, Map[String, Float     ], Float     ]
  trait MapDouble [Ns, In] extends MapAttr[Ns, In, Map[String, Double    ], Double    ]
  trait MapBoolean[Ns, In] extends MapAttr[Ns, In, Map[String, Boolean   ], Boolean   ]
  trait MapBigInt [Ns, In] extends MapAttr[Ns, In, Map[String, BigInt    ], BigInt    ]
  trait MapBigDec [Ns, In] extends MapAttr[Ns, In, Map[String, BigDecimal], BigDecimal]
  trait MapDate   [Ns, In] extends MapAttr[Ns, In, Map[String, Date      ], Date      ]
  trait MapUUID   [Ns, In] extends MapAttr[Ns, In, Map[String, UUID      ], UUID      ]
  trait MapURI    [Ns, In] extends MapAttr[Ns, In, Map[String, URI       ], URI       ]
  trait MapByte   [Ns, In] extends MapAttr[Ns, In, Map[String, Byte      ], Byte      ]


  // Enums

  object EnumValue
  trait Enum
  trait OneEnum  [Ns, In] extends One [Ns, In, String]              with Enum
  trait ManyEnums[Ns, In] extends Many[Ns, In, Set[String], String] with Enum


  // Optional attributes

  trait RefAttr$ extends Attr
  trait OneRefAttr$  extends RefAttr$
  trait ManyRefAttr$ extends RefAttr$

  trait ValueAttr$[T] extends Attr

  trait OneValueAttr$[T]  extends ValueAttr$[T]
  trait OneString$  extends OneValueAttr$[String    ]
  trait OneInt$     extends OneValueAttr$[Int       ]
  trait OneLong$    extends OneValueAttr$[Long      ]
  trait OneFloat$   extends OneValueAttr$[Float     ]
  trait OneDouble$  extends OneValueAttr$[Double    ]
  trait OneBoolean$ extends OneValueAttr$[Boolean   ]
  trait OneBigInt$  extends OneValueAttr$[BigInt    ]
  trait OneBigDec$  extends OneValueAttr$[BigDecimal]
  trait OneDate$    extends OneValueAttr$[Date      ]
  trait OneUUID$    extends OneValueAttr$[UUID      ]
  trait OneURI$     extends OneValueAttr$[URI       ]
  trait OneByte$    extends OneValueAttr$[Byte      ]

  trait ManyValueAttr$[T] extends ValueAttr$[T]
  trait ManyString$  extends ManyValueAttr$[Set[String    ]]
  trait ManyInt$     extends ManyValueAttr$[Set[Int       ]]
  trait ManyLong$    extends ManyValueAttr$[Set[Long      ]]
  trait ManyFloat$   extends ManyValueAttr$[Set[Float     ]]
  trait ManyDouble$  extends ManyValueAttr$[Set[Double    ]]
  trait ManyBoolean$ extends ManyValueAttr$[Set[Boolean   ]]
  trait ManyBigInt$  extends ManyValueAttr$[Set[BigInt    ]]
  trait ManyBigDec$  extends ManyValueAttr$[Set[BigDecimal]]
  trait ManyDate$    extends ManyValueAttr$[Set[Date      ]]
  trait ManyUUID$    extends ManyValueAttr$[Set[UUID      ]]
  trait ManyURI$     extends ManyValueAttr$[Set[URI       ]]
  trait ManyByte$    extends ManyValueAttr$[Set[Byte      ]]

  trait MapAttr$[T] extends Attr
  trait MapString$  extends MapAttr$[Map[String, String    ]]
  trait MapInt$     extends MapAttr$[Map[String, Int       ]]
  trait MapLong$    extends MapAttr$[Map[String, Long      ]]
  trait MapFloat$   extends MapAttr$[Map[String, Float     ]]
  trait MapDouble$  extends MapAttr$[Map[String, Double    ]]
  trait MapBoolean$ extends MapAttr$[Map[String, Boolean   ]]
  trait MapBigInt$  extends MapAttr$[Map[String, BigInt    ]]
  trait MapBigDec$  extends MapAttr$[Map[String, BigDecimal]]
  trait MapDate$    extends MapAttr$[Map[String, Date      ]]
  trait MapUUID$    extends MapAttr$[Map[String, UUID      ]]
  trait MapURI$     extends MapAttr$[Map[String, URI       ]]
  trait MapByte$    extends MapAttr$[Map[String, Byte      ]]

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


  // Bidirectional markers

  // Bidirectional ref to defining ns (self-reference)
  trait BiSelfRef_
  trait BiSelfRefAttr_

  // Bidirectional ref to other ns
  trait BiOtherRef_[revRefAttr]
  trait BiOtherRefAttr_[revRefAttr]

  // Property edge namespace
  trait BiEdge_

  // Ref from defining ns to edge
  trait BiEdgeRef_[revRefAttr]
  trait BiEdgeRefAttr_[revRefAttr]

  // Edge properties
  trait BiEdgePropAttr_
  trait BiEdgePropRef_
  trait BiEdgePropRefAttr_

  // Ref from edge to target ns
  trait BiTargetRef_[biRefAttr]
  trait BiTargetRefAttr_[biRefAttr]
}
