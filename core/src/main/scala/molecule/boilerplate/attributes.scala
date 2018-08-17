package molecule.boilerplate

import java.net.URI
import java.util.{Date, UUID}
import molecule.expression.AttrExpressions._

/** Boilerplate interfaces for custom DSL generated from schema definition file.
  * <br><br>
  * Encodes attribute cardinality, type and mode (mandatory, tacit, optional).
  * */
object attributes {

  trait SelfJoin

  trait Ref[This, Next]
  trait OneRef[This, Next] extends Ref[This, Next]
  trait ManyRef[This, Next] extends Ref[This, Next]

  trait Attr

  trait RefAttr[Ns, T] extends Attr

  trait OneRefAttr[Ns, In] extends OneRefAttrExpr[Ns, In] with RefAttr[Ns,  Long]

  trait ManyRefAttr[Ns, In] extends ManyRefAttrExpr[Ns, In] with RefAttr[Ns,  Long]

  sealed trait ValueAttr[Ns, In, T, U] extends Attr

  // Cardinality one attributes

  trait One[Ns, In, T] extends ValueAttr[Ns, In, T, T] with OneExpr[Ns, In, T]

  trait OneString    [Ns, In] extends One[Ns, In, String    ]
  trait OneInt       [Ns, In] extends One[Ns, In, Int       ]
  trait OneLong      [Ns, In] extends One[Ns, In, Long      ]
  trait OneFloat     [Ns, In] extends One[Ns, In, Float     ]
  trait OneDouble    [Ns, In] extends One[Ns, In, Double    ]
  trait OneBoolean   [Ns, In] extends One[Ns, In, Boolean   ]
  trait OneBigInt    [Ns, In] extends One[Ns, In, BigInt    ]
  trait OneBigDecimal[Ns, In] extends One[Ns, In, BigDecimal]
  trait OneDate      [Ns, In] extends One[Ns, In, Date      ]
  trait OneUUID      [Ns, In] extends One[Ns, In, UUID      ]
  trait OneURI       [Ns, In] extends One[Ns, In, URI       ]
  trait OneByte      [Ns, In] extends One[Ns, In, Byte      ]
  trait OneAny       [Ns, In] extends One[Ns, In, Any       ]


  // Cardinality many attributes

  trait Many[Ns, In, S, T] extends ValueAttr[Ns, In, T, S] with ManyExpr[Ns, In, T]

  trait ManyString    [Ns, In] extends Many[Ns, In, Set[String    ], String    ]
  trait ManyInt       [Ns, In] extends Many[Ns, In, Set[Int       ], Int       ]
  trait ManyLong      [Ns, In] extends Many[Ns, In, Set[Long      ], Long      ]
  trait ManyFloat     [Ns, In] extends Many[Ns, In, Set[Float     ], Float     ]
  trait ManyDouble    [Ns, In] extends Many[Ns, In, Set[Double    ], Double    ]
  trait ManyBoolean   [Ns, In] extends Many[Ns, In, Set[Boolean   ], Boolean   ]
  trait ManyBigInt    [Ns, In] extends Many[Ns, In, Set[BigInt    ], BigInt    ]
  trait ManyBigDecimal[Ns, In] extends Many[Ns, In, Set[BigDecimal], BigDecimal]
  trait ManyDate      [Ns, In] extends Many[Ns, In, Set[Date      ], Date      ]
  trait ManyUUID      [Ns, In] extends Many[Ns, In, Set[UUID      ], UUID      ]
  trait ManyURI       [Ns, In] extends Many[Ns, In, Set[URI       ], URI       ]
  trait ManyByte      [Ns, In] extends Many[Ns, In, Set[Byte      ], Byte      ]


  // Map attributes

  trait MapAttrK

  trait MapAttr[Ns, In, M, T] extends ValueAttr[Ns, In, T, M] with MapAttrExpr[Ns, In, T]

  trait MapString    [Ns, In] extends MapAttr[Ns, In, Map[String, String    ], String    ]
  trait MapInt       [Ns, In] extends MapAttr[Ns, In, Map[String, Int       ], Int       ]
  trait MapLong      [Ns, In] extends MapAttr[Ns, In, Map[String, Long      ], Long      ]
  trait MapFloat     [Ns, In] extends MapAttr[Ns, In, Map[String, Float     ], Float     ]
  trait MapDouble    [Ns, In] extends MapAttr[Ns, In, Map[String, Double    ], Double    ]
  trait MapBoolean   [Ns, In] extends MapAttr[Ns, In, Map[String, Boolean   ], Boolean   ]
  trait MapBigInt    [Ns, In] extends MapAttr[Ns, In, Map[String, BigInt    ], BigInt    ]
  trait MapBigDecimal[Ns, In] extends MapAttr[Ns, In, Map[String, BigDecimal], BigDecimal]
  trait MapDate      [Ns, In] extends MapAttr[Ns, In, Map[String, Date      ], Date      ]
  trait MapUUID      [Ns, In] extends MapAttr[Ns, In, Map[String, UUID      ], UUID      ]
  trait MapURI       [Ns, In] extends MapAttr[Ns, In, Map[String, URI       ], URI       ]
  trait MapByte      [Ns, In] extends MapAttr[Ns, In, Map[String, Byte      ], Byte      ]


  // Enums

  object EnumValue
  trait Enum
  trait OneEnum  [Ns, In] extends One [Ns, In, String]              with Enum
  trait ManyEnums[Ns, In] extends Many[Ns, In, Set[String], String] with Enum


  // Optional attributes

  trait RefAttr$[Ns, T] extends Attr with OptionalExpr[Ns, T]
  trait OneRefAttr$ [Ns] extends RefAttr$[Ns, Long]
  trait ManyRefAttr$[Ns] extends RefAttr$[Ns, Set[Long]]

  trait ValueAttr$[T] extends Attr

  trait OneValueAttr$[Ns, T] extends ValueAttr$[T] with OptionalExpr[Ns, T]

  trait OneString$    [Ns] extends OneValueAttr$[Ns, String    ]
  trait OneInt$       [Ns] extends OneValueAttr$[Ns, Int       ]
  trait OneLong$      [Ns] extends OneValueAttr$[Ns, Long      ]
  trait OneFloat$     [Ns] extends OneValueAttr$[Ns, Float     ]
  trait OneDouble$    [Ns] extends OneValueAttr$[Ns, Double    ]
  trait OneBoolean$   [Ns] extends OneValueAttr$[Ns, Boolean   ]
  trait OneBigInt$    [Ns] extends OneValueAttr$[Ns, BigInt    ]
  trait OneBigDecimal$[Ns] extends OneValueAttr$[Ns, BigDecimal]
  trait OneDate$      [Ns] extends OneValueAttr$[Ns, Date      ]
  trait OneUUID$      [Ns] extends OneValueAttr$[Ns, UUID      ]
  trait OneURI$       [Ns] extends OneValueAttr$[Ns, URI       ]
  trait OneByte$      [Ns] extends OneValueAttr$[Ns, Byte      ]


  trait ManyValueAttr$[Ns, T] extends ValueAttr$[T] with OptionalExpr[Ns, T]

  trait ManyString$    [Ns] extends ManyValueAttr$[Ns, Set[String    ]]
  trait ManyInt$       [Ns] extends ManyValueAttr$[Ns, Set[Int       ]]
  trait ManyLong$      [Ns] extends ManyValueAttr$[Ns, Set[Long      ]]
  trait ManyFloat$     [Ns] extends ManyValueAttr$[Ns, Set[Float     ]]
  trait ManyDouble$    [Ns] extends ManyValueAttr$[Ns, Set[Double    ]]
  trait ManyBoolean$   [Ns] extends ManyValueAttr$[Ns, Set[Boolean   ]]
  trait ManyBigInt$    [Ns] extends ManyValueAttr$[Ns, Set[BigInt    ]]
  trait ManyBigDecimal$[Ns] extends ManyValueAttr$[Ns, Set[BigDecimal]]
  trait ManyDate$      [Ns] extends ManyValueAttr$[Ns, Set[Date      ]]
  trait ManyUUID$      [Ns] extends ManyValueAttr$[Ns, Set[UUID      ]]
  trait ManyURI$       [Ns] extends ManyValueAttr$[Ns, Set[URI       ]]
  trait ManyByte$      [Ns] extends ManyValueAttr$[Ns, Set[Byte      ]]


  trait MapAttr$[Ns, T] extends Attr with OptionalExpr[Ns, T]

  trait MapString$    [Ns] extends MapAttr$[Ns, Map[String, String    ]]
  trait MapInt$       [Ns] extends MapAttr$[Ns, Map[String, Int       ]]
  trait MapLong$      [Ns] extends MapAttr$[Ns, Map[String, Long      ]]
  trait MapFloat$     [Ns] extends MapAttr$[Ns, Map[String, Float     ]]
  trait MapDouble$    [Ns] extends MapAttr$[Ns, Map[String, Double    ]]
  trait MapBoolean$   [Ns] extends MapAttr$[Ns, Map[String, Boolean   ]]
  trait MapBigInt$    [Ns] extends MapAttr$[Ns, Map[String, BigInt    ]]
  trait MapBigDecimal$[Ns] extends MapAttr$[Ns, Map[String, BigDecimal]]
  trait MapDate$      [Ns] extends MapAttr$[Ns, Map[String, Date      ]]
  trait MapUUID$      [Ns] extends MapAttr$[Ns, Map[String, UUID      ]]
  trait MapURI$       [Ns] extends MapAttr$[Ns, Map[String, URI       ]]
  trait MapByte$      [Ns] extends MapAttr$[Ns, Map[String, Byte      ]]


  trait Enum$[Ns, T] extends Attr with OptionalExpr[Ns, T]

  trait OneEnum$   [Ns] extends Enum$[Ns, String]
  trait ManyEnums$ [Ns] extends Enum$[Ns, Set[String]]


  // Attribute options

  case class Doc(msg: String)
  trait UniqueValue
  trait UniqueIdentity
  trait Indexed
  trait FulltextSearch[Ns, In] extends FulltextSearchExpr[Ns, In]
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
