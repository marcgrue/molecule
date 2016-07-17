package molecule.schema

import scala.annotation.StaticAnnotation


object definition {

  // Annotation for arities of molecule inputs and outputs
  class InOut(inputArity: Int = 3, outputArity: Int = 8) extends StaticAnnotation

  trait Bidirectional

  private[molecule] sealed trait scalarAttr[Builder] {
    lazy val indexed       : Builder   = ???
    lazy val noHistory     : Builder   = ???
    lazy val uniqueValue   : oneString = ???
    lazy val uniqueIdentity: oneString = ???
    def doc(s: String) = ??? // can only be last
  }


  // String
  object oneString extends oneString
  trait oneString extends scalarAttr[oneString] {
    lazy val fullTextSearch: oneString = ???
  }
  object manyString extends manyString
  trait manyString extends scalarAttr[manyString] {
    lazy val fullTextSearch: manyString = ???
  }
  object mapString extends mapString
  trait mapString extends scalarAttr[mapString] {
    val fullTextSearch: mapString = ???
  }

  // Todo: Specialized attributes with constraints
  object oneEmail extends oneEmail
  trait oneEmail extends oneString

  trait number[Builder, T] extends scalarAttr[Builder] {
    // Todo: schema level constraints
    def min(n: T) : this.type = ???
    def max(n: T) : this.type = ???
  }

  // Int
  object oneInt extends oneInt
  trait oneInt extends number[oneInt, Int]

  object manyInt extends manyInt
  trait manyInt extends number[manyInt, Int]

  object mapInt extends mapInt
  trait mapInt extends number[mapInt, Int]

  // Long
  object oneLong extends oneLong
  trait oneLong extends number[oneLong, Long]

  object manyLong extends manyLong
  trait manyLong extends number[manyLong, Long]

  object mapLong extends mapLong
  trait mapLong extends number[mapLong, Long]

  // Float
  object oneFloat extends oneFloat
  trait oneFloat extends number[oneFloat, Float]

  object manyFloat extends manyFloat
  trait manyFloat extends number[manyFloat, Float]

  object mapFloat extends mapFloat
  trait mapFloat extends number[mapFloat, Float]

  // Double
  object oneDouble extends oneDouble
  trait oneDouble extends number[oneDouble, Double]

  object manyDouble extends manyDouble
  trait manyDouble extends number[manyDouble, Double]

  object mapDouble extends mapDouble
  trait mapDouble extends number[mapDouble, Double]

  // Boolean
  object oneBoolean extends oneBoolean
  trait oneBoolean extends scalarAttr[oneBoolean]

  object manyBoolean extends manyBoolean
  trait manyBoolean extends scalarAttr[manyBoolean]

  object mapBoolean extends mapBoolean
  trait mapBoolean extends scalarAttr[mapBoolean]


  // BigInt
  object oneBigInt extends oneBigInt
  trait oneBigInt extends number[oneBigInt, BigInt]

  object manyBigInt extends manyBigInt
  trait manyBigInt extends number[manyBigInt, BigInt]

  object mapBigInt extends mapBigInt
  trait mapBigInt extends number[mapBigInt, BigInt]

  // BigDecimal
  object oneBigDec extends oneBigDec
  trait oneBigDec extends number[oneBigDec, BigDecimal]

  object manyBigDec extends manyBigDec
  trait manyBigDec extends number[manyBigDec, BigDecimal]

  object mapBigDec extends mapBigDec
  trait mapBigDec extends number[mapBigDec, BigDecimal]

  // Date
  object oneDate extends oneDate
  trait oneDate extends scalarAttr[oneDate]

  object manyDate extends manyDate
  trait manyDate extends scalarAttr[manyDate]

  object mapDate extends mapDate
  trait mapDate extends scalarAttr[mapDate]

  // UUID
  object oneUUID extends oneUUID
  trait oneUUID extends scalarAttr[oneUUID]

  object manyUUID extends manyUUID
  trait manyUUID extends scalarAttr[manyUUID]

  object mapUUID extends mapUUID
  trait mapUUID extends scalarAttr[mapUUID]

  // URI
  object oneURI extends oneURI
  trait oneURI extends scalarAttr[oneURI]

  object manyURI extends manyURI
  trait manyURI extends scalarAttr[manyURI]

  object mapURI extends mapURI
  trait mapURI extends scalarAttr[mapURI]

  // Bytes
  object oneByte extends oneByte
  trait oneByte extends scalarAttr[oneByte]

  object manyByte extends manyByte
  trait manyByte extends scalarAttr[manyByte]

  object mapByte extends mapByte
  trait mapByte extends scalarAttr[mapByte]

  // Enum
  private[molecule] trait enum extends scalarAttr[enum] {
    // Require at least 2 enum values (any use case for only 1 enum??)
    def apply(e1: Symbol, e2: Symbol, es: Symbol*) = this
  }
  object oneEnum extends enum
  object manyEnum extends enum


  // References

  object one {
    def apply[Ns] = this
    lazy val subComponent = this
    def doc(s: String) = ??? // can only be last
  }
  object many {
    def apply[Ns] = this
    lazy val subComponents = this
    def doc(s: String) = ???
  }


  // Bidirectional ref

  object oneBi {
    def apply[ThisNsOrRevRefAttr] = this
    def doc(s: String) = ???
  }
  object manyBi {
    def apply[ThisNsOrRevRefAttr] = this
    def doc(s: String) = ???
  }

  // Bidirectional edge

  object oneBiEdge {
    def apply[ThisNsOrRevRefAttr] = this
    def doc(s: String) = ???
  }
  object manyBiEdge {
    def apply[ThisNsOrRevRefAttr] = this
    def doc(s: String) = ???
  }

  // Ref from edge to target ns
  object target {
    def apply[biRefAttr] = this
    def doc(s: String) = ???
  }
}

