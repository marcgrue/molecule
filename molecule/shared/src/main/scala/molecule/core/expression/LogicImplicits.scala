package molecule.core.expression

import java.net.URI
import java.util.{Date, UUID}
import molecule.core.ast.elements._

/** Logic expression implicits to build OR/AND logic.
  * {{{
  *   Person.name("John" or "Jonas")           // OR-logic
  * }}}
  *
  * @groupname attrLogicImplicits Expression implicits
  * @groupdesc attrLogicImplicits Turns basic types into `TermValue`'s that can be used in [[molecule.core.ast.elements.Expression Expression]]
  * @groupprio attrLogicImplicits 21
  */
trait LogicImplicits {

  /** @group attrLogicImplicits */
  implicit final def string2Model(v: String): TermValue[String] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def int2Model(v: Int): TermValue[Int] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def long2Model(v: Long): TermValue[Long] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def float2Model(v: Float): TermValue[Float] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def double2Model(v: Double): TermValue[Double] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def bigInt2Model(v: BigInt): TermValue[BigInt] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def bigDec2Model(v: BigDecimal): TermValue[BigDecimal] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def boolean2Model(v: Boolean): TermValue[Boolean] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def date2Model(v: Date): TermValue[Date] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def uuid2Model(v: UUID): TermValue[UUID] = TermValue(v)
  /** @group attrLogicImplicits */
  implicit final def uri2Model(v: URI): TermValue[URI] = TermValue(v)

  /** @group attrLogicImplicits */
  implicit final def stringSet2Model(set: Set[String]): TermValue[Set[String]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def intSet2Model(set: Set[Int]): TermValue[Set[Int]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def longSet2Model(set: Set[Long]): TermValue[Set[Long]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def floatSet2Model(set: Set[Float]): TermValue[Set[Float]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def doubleSet2Model(set: Set[Double]): TermValue[Set[Double]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def bigIntSet2Model(set: Set[BigInt]): TermValue[Set[BigInt]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def bigDecSet2Model(set: Set[BigDecimal]): TermValue[Set[BigDecimal]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def booleanSet2Model(set: Set[Boolean]): TermValue[Set[Boolean]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def dateSet2Model(set: Set[Date]): TermValue[Set[Date]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def uuidSet2Model(set: Set[UUID]): TermValue[Set[UUID]] = TermValue(set)
  /** @group attrLogicImplicits */
  implicit final def uriSet2Model(set: Set[URI]): TermValue[Set[URI]] = TermValue(set)

  /** @group attrLogicImplicits */
  implicit final def tuple2Model[A, B](tpl: (A, B)): TermValue[(A, B)] = TermValue(tpl)
}

