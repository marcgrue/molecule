package molecule.dsl
import scala.annotation.StaticAnnotation


object schemaDefinition {

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

  // Number .....................
  trait number[Builder] extends scalarAttr[Builder] {
    // One by one
    lazy val count   : Builder = ???
    lazy val avg     : Builder = ???
    lazy val median  : Builder = ???
    lazy val variance: Builder = ???
    lazy val stddev  : Builder = ???

    // All
    lazy val aggregates: Builder = ???
  }

  // Int
  object oneInt extends oneInt
  trait oneInt extends number[oneInt] with scalarAttr[oneInt]

  object manyInt extends manyInt
  trait manyInt extends number[manyInt] with scalarAttr[manyInt]

  object mapInt extends mapInt
  trait mapInt extends number[mapInt] with scalarAttr[mapInt]

  // Long
  object oneLong extends oneLong
  trait oneLong extends number[oneLong] with scalarAttr[oneLong]

  object manyLong extends manyLong
  trait manyLong extends number[manyLong] with scalarAttr[manyLong]

  object mapLong extends mapLong
  trait mapLong extends number[mapLong] with scalarAttr[mapLong]

  // Float
  object oneFloat extends oneFloat
  trait oneFloat extends number[oneFloat] with scalarAttr[oneFloat]

  object manyFloat extends manyFloat
  trait manyFloat extends number[manyFloat] with scalarAttr[manyFloat]

  object mapFloat extends mapFloat
  trait mapFloat extends number[mapFloat] with scalarAttr[mapFloat]

  // Double
  object oneDouble extends oneDouble
  trait oneDouble extends number[oneDouble] with scalarAttr[oneDouble]

  object manyDouble extends manyDouble
  trait manyDouble extends number[manyDouble] with scalarAttr[manyDouble]

  object mapDouble extends mapDouble
  trait mapDouble extends number[mapDouble] with scalarAttr[mapDouble]

  // Boolean
  object oneBoolean extends oneBoolean
  trait oneBoolean extends scalarAttr[oneBoolean]

  object manyBoolean extends manyBoolean
  trait manyBoolean extends scalarAttr[manyBoolean]

  object mapBoolean extends mapBoolean
  trait mapBoolean extends scalarAttr[mapBoolean]

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

  // todo?
  // BigInt
  // BigDec
  // Bytes


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


  // Bidirectional references

  object oneBi {
    def apply[ThisNsOrRevRefAttr] = this
    def doc(s: String) = ???
  }
  object manyBi {
    def apply[ThisNsOrRevRefAttr] = this
    def doc(s: String) = ???
  }

  // Ref from edge to target ns
  object target {
    def apply[biRefAttr] = this
    def doc(s: String) = ???
  }
}

