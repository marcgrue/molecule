package molecule.schema

import molecule.schema.definition.many
import scala.annotation.StaticAnnotation

/** Schema definition DSL.
  * <br><br>
  * Define Datomic database schema in a Schema Definition file.
  * <br><br>
  * For small projects, the schema can be defined without partition definitions where
  * all namespaces reside in a default tacit partition:
  * {{{
  *   package path.to.your.project
  *   import molecule.schema.definition._       // import schema definition DSL
  *
  *   @InOut(1, 8)                              // Set input/output arity
  *   object SeattleDefinition {                // Schema definition object
  *
  *     trait Person {                          // Namespace
  *       val name = oneString.fulltext   // String attribute definition with fulltext search
  *       val age  = oneInt                     // Int attribute definition
  *     }
  *
  *     // Additional namespaces...
  *   }
  * }}}
  * For larger projects, it is recommended to group namespaces in partitions:
  * {{{
  *   package path.to.your.project
  *   import molecule.schema.definition._
  *
  *   @InOut(3, 15)
  *   object SeattleDefinition {
  *
  *     object customer {
  *       trait Person {
  *         val name    = oneString.fulltext
  *         val age     = oneInt
  *         val address = one[Address]
  *         val bought  = many[products.Item]
  *       }
  *       trait Address {
  *         val street = oneString.fulltext
  *         val city   = oneInt
  *       }
  *       // ..more namespaces in the `customer` partition
  *     }
  *
  *     object products {
  *       trait Item {
  *         val title   = oneString
  *         val inStock = oneInt
  *       }
  *       // ..more namespaces in the `products` partition
  *     }
  *
  *     // Additional partitions...
  *   }
  * }}}
  *
  * @see [[http://www.scalamolecule.org/manual/schema/ Manual]]
  *      | Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/util/schema/CoreTestDefinition.scala Schema without partitions]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/schemaDef/schema/PartitionTestDefinition.scala Schema with partitions]],
  *      [[https://github.com/scalamolecule/molecule/tree/master/coretests/src/test/scala/molecule/coretests/bidirectionals Bidirectionals]]
  * @groupname setup Setup
  * @groupprio setup 1
  * @groupname opt Options
  * @groupprio opt 2
  * @groupname one Cardinality-one attributes
  * @groupprio one 3
  * @groupname many Cardinality-many attributes
  * @groupprio many 4
  * @groupname map Map attributes
  * @groupprio map 5
  * @groupname ref References
  * @groupprio ref 6
  * @groupname bi Bidirectional references
  * @groupprio bi 7
  * @groupname edge Bidirectional edge references
  * @groupprio edge 8
  * */
object definition {

  /** Arity annotation for number of molecule input/output attributes.
    *
    * @group setup
    */
  class InOut(inputArity: Int = 3, outputArity: Int = 8) extends StaticAnnotation


  // Options ---------------------------------------------------------

  /** Attribute options.
    *
    * @group opt
    */
  sealed trait optionBuilder[Self] {

    /** Attribute description. */
    def doc(s: String) = ??? // can only be last

    /** Indexed option (defaults to true).
      * <br><br>
      * Generated index for this attribute. By default all attributes are set with
      * the indexed option automatically by Molecule, so you don't need to set this.
      * */
    lazy val indexed: Self = ???

    /** No history option. */
    lazy val noHistory: Self = ???

    /** Unique identity option.
      * <br><br>
      * Attribute value is unique to each entity and "upsert" is enabled.
      * <br><br>
      * Attempts to insert a duplicate value for a temporary entity id will cause all
      * attributes associated with that temporary id to be merged with the entity
      * already in the database.
      * */
    lazy val uniqueIdentity: Self = ???

    /** Unique value option.
      * <br><br>
      * Attribute value is unique to each entity.
      * <br><br>
      * Attempts to insert a duplicate value for a different entity id will fail.
      * */
    lazy val uniqueValue: Self = ???
  }


  trait refOptionBuilder[RefType] extends optionBuilder[RefType] {

    /** Apply namespace type to reference.
      *
      * @tparam RefNs Ref namespace type
      */
    def apply[RefNs]: RefType = ???

    /** Is-component option.
      * <br><br>
      * Specifies that an attribute whose type is :db.type/ref is a component.
      * <br><br>
      * Referenced entities become subcomponents of the entity to which the attribute is applied.
      * <br><br>
      * When you retract an entity with :db.fn/retractEntity, all subcomponents are also retracted.
      * <br><br>
      * When you touch an entity, all its subcomponent entities are touched recursively.
      */
    lazy val isComponent: RefType = ???
  }


  // String ---------------------------------------------------------

  /** Card-one String attribute.
    *
    * @group one
    */
  trait oneString extends optionBuilder[oneString] {

    /** Fulltext search option.
      * <br><br>
      * Generate eventually consistent fulltext search index for this attribute.
      */
    lazy val fulltext: oneString = ???
  }
  object oneString extends oneString


  /** Card-many String attribute.
    *
    * @group many
    */
  trait manyString extends optionBuilder[manyString] {

    /** Fulltext search option.
      * <br><br>
      * Generate eventually consistent fulltext search index for this attribute.
      */
    lazy val fulltext: manyString = ???
  }
  object manyString extends manyString


  /** String map attribute.
    *
    * @group map
    */
  trait mapString extends optionBuilder[mapString] {

    /** Fulltext search option.
      * <br><br>
      * Generate eventually consistent fulltext search index for this attribute.
      */
    val fulltext: mapString = ???
  }
  object mapString extends mapString

  // Todo: Specialized attributes with constraints?
  private[molecule] object oneEmail extends oneEmail
  private[molecule] trait oneEmail extends oneString

  private[molecule] trait number[Self, T] extends optionBuilder[Self] {
    // Todo: schema level constraints?
    def min(n: T): Self = ???
    def max(n: T): Self = ???
  }


  // Int ---------------------------------------------------------

  /** Card-one Int attribute.
    *
    * @group one
    */
  trait oneInt extends number[oneInt, Int]
  object oneInt extends oneInt


  /** Card-many Int attribute.
    *
    * @group many
    */
  trait manyInt extends number[manyInt, Int]
  object manyInt extends manyInt


  /** Int map attribute.
    *
    * @group map
    */
  trait mapInt extends number[mapInt, Int]
  object mapInt extends mapInt


  // Long ---------------------------------------------------------

  /** Card-one Long attribute.
    *
    * @group one
    */
  trait oneLong extends number[oneLong, Long]
  object oneLong extends oneLong


  /** Card-many Long attribute.
    *
    * @group many
    */
  trait manyLong extends number[manyLong, Long]
  object manyLong extends manyLong


  /** Long map attribute.
    *
    * @group map
    */
  trait mapLong extends number[mapLong, Long]
  object mapLong extends mapLong


  // Float ---------------------------------------------------------

  /** Card-one Float attribute.
    *
    * @group one
    */
  trait oneFloat extends number[oneFloat, Float]
  object oneFloat extends oneFloat


  /** Card-many Float attribute.
    *
    * @group many
    */
  trait manyFloat extends number[manyFloat, Float]
  object manyFloat extends manyFloat


  /** Float map attribute.
    *
    * @group map
    */
  trait mapFloat extends number[mapFloat, Float]
  object mapFloat extends mapFloat


  // Double ---------------------------------------------------------

  /** Card-one Double attribute.
    *
    * @group one
    */
  trait oneDouble extends number[oneDouble, Double]
  object oneDouble extends oneDouble


  /** Card-many Double attribute.
    *
    * @group many
    */
  trait manyDouble extends number[manyDouble, Double]
  object manyDouble extends manyDouble


  /** Double map attribute.
    *
    * @group map
    */
  trait mapDouble extends number[mapDouble, Double]
  object mapDouble extends mapDouble


  // Boolean ---------------------------------------------------------

  /** Card-one Boolean attribute.
    *
    * @group one
    */
  trait oneBoolean extends optionBuilder[oneBoolean]
  object oneBoolean extends oneBoolean


  /** Card-many Boolean attribute.
    *
    * @group many
    */
  trait manyBoolean extends optionBuilder[manyBoolean]
  object manyBoolean extends manyBoolean


  /** Boolean map attribute.
    *
    * @group map
    */
  trait mapBoolean extends optionBuilder[mapBoolean]
  object mapBoolean extends mapBoolean


  // BigInt ---------------------------------------------------------

  /** Card-one BigInt attribute.
    *
    * @group one
    */
  trait oneBigInt extends number[oneBigInt, BigInt]
  object oneBigInt extends oneBigInt


  /** Card-many BigInt attribute.
    *
    * @group many
    */
  trait manyBigInt extends number[manyBigInt, BigInt]
  object manyBigInt extends manyBigInt


  /** BigInt map attribute.
    *
    * @group map
    */
  trait mapBigInt extends number[mapBigInt, BigInt]
  object mapBigInt extends mapBigInt


  // BigDecimal ---------------------------------------------------------

  /** Card-one BigDecimal attribute.
    *
    * @group one
    */
  trait oneBigDecimal extends number[oneBigDecimal, BigDecimal]
  object oneBigDecimal extends oneBigDecimal


  /** Card-many BigDecimal attribute.
    *
    * @group many
    */
  trait manyBigDecimal extends number[manyBigDecimal, BigDecimal]
  object manyBigDecimal extends manyBigDecimal


  /** BigDecimal map attribute.
    *
    * @group map
    */
  trait mapBigDecimal extends number[mapBigDecimal, BigDecimal]
  object mapBigDecimal extends mapBigDecimal


  // Date ---------------------------------------------------------

  /** Card-one Date attribute.
    *
    * @group one
    */
  trait oneDate extends optionBuilder[oneDate]
  object oneDate extends oneDate


  /** Card-many Date attribute.
    *
    * @group many
    */
  trait manyDate extends optionBuilder[manyDate]
  object manyDate extends manyDate


  /** Date map attribute.
    *
    * @group map
    */
  trait mapDate extends optionBuilder[mapDate]
  object mapDate extends mapDate


  // UUID ---------------------------------------------------------

  /** Card-one UUID attribute.
    *
    * @group one
    */
  trait oneUUID extends optionBuilder[oneUUID]
  object oneUUID extends oneUUID


  /** Card-many UUID attribute.
    *
    * @group many
    */
  trait manyUUID extends optionBuilder[manyUUID]
  object manyUUID extends manyUUID


  /** UUID map attribute.
    *
    * @group map
    */
  trait mapUUID extends optionBuilder[mapUUID]
  object mapUUID extends mapUUID


  // URI ---------------------------------------------------------

  /** Card-one URI attribute.
    *
    * @group one
    */
  trait oneURI extends optionBuilder[oneURI]
  object oneURI extends oneURI


  /** Card-many URI attribute.
    *
    * @group many
    */
  trait manyURI extends optionBuilder[manyURI]
  object manyURI extends manyURI


  /** URI map attribute.
    *
    * @group map
    */
  trait mapURI extends optionBuilder[mapURI]
  object mapURI extends mapURI


  // Bytes ---------------------------------------------------------

  /** Card-one Bytes attribute.
    *
    * @group one
    */
  trait oneByte extends optionBuilder[oneByte]
  object oneByte extends oneByte


  /** Card-many Bytes attribute.
    *
    * @group many
    */
  trait manyByte extends optionBuilder[manyByte]
  object manyByte extends manyByte


  /** Bytes map attribute.
    *
    * @group map
    */
  trait mapByte extends optionBuilder[mapByte]
  object mapByte extends mapByte


  // Enum ---------------------------------------------------------

  private[molecule] trait enum extends optionBuilder[enum] {
    // Require at least 2 enum values (any use case for only 1 enum??)
    def apply(e1: String, e2: String, es: String*) = this
  }


  /** Card-one Enum attribute.
    *
    * @group one
    */
  trait oneEnum extends enum
  object oneEnum extends oneEnum


  /** Card-many Enum attribute.
    *
    * @group many
    */
  trait manyEnum extends enum
  object manyEnum extends manyEnum


  // Any ---------------------------------------------------------

  /** Internal card-one Any attribute for multi-typed values in log and indexes.
    *
    * Do _not_ use in custom schema definitions.
    *
    * It is only implemented internally for generic log and indexes.
    *
    * @group one
    */
  private[molecule] object oneAny extends optionBuilder[oneString]


  // References ---------------------------------------------------------

  /** Card-one reference.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/card-one/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/util/schema/CoreTestDefinition.scala Example]]
    * @group ref
    */
  trait one extends refOptionBuilder[one]
  object one extends one


  /** Card-many reference.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/card-many/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/util/schema/CoreTestDefinition.scala Example]]
    * @group ref
    */
  trait many extends refOptionBuilder[many]
  object many extends many


  // Bidirectional ref ---------------------------------------------------------

  /** Card-one bi-directional reference.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/bidirectional/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/bidirectionals/schema/BidirectionalDefinition.scala Example]]
    * @group bi
    */
  trait oneBi extends refOptionBuilder[oneBi]
  object oneBi extends oneBi


  /** Card-many bi-directional reference.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/bidirectional/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/bidirectionals/schema/BidirectionalDefinition.scala Example]]
    * @group bi
    */
  trait manyBi extends refOptionBuilder[manyBi]
  object manyBi extends manyBi


  // Bidirectional edge ---------------------------------------------------------

  /** Card-one bi-directional edge reference.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/bidirectional/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/bidirectionals/schema/BidirectionalDefinition.scala Example]]
    * @group edge
    */
  trait oneBiEdge extends refOptionBuilder[oneBiEdge]
  object oneBiEdge extends oneBiEdge


  /** Card-many bi-directional edge reference.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/bidirectional/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/bidirectionals/schema/BidirectionalDefinition.scala Example]]
    * @group edge
    */
  trait manyBiEdge extends refOptionBuilder[manyBiEdge]
  object manyBiEdge extends manyBiEdge


  /** Bi-directional edge target attribute.
    *
    * @see [[http://www.scalamolecule.org/manual/relationships/bidirectional/ Manual]] |
    *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/main/scala/molecule/coretests/bidirectionals/schema/BidirectionalDefinition.scala Example]]
    * @group edge
    */
  trait target extends refOptionBuilder[target]
  object target extends target
}

