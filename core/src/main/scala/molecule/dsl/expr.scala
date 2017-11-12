package molecule.dsl
import java.net.URI
import java.util.{Date, UUID}

import molecule.ast.model.TermValue


trait expr {

  // Attribute expression implicits

  implicit def string2Model (v: String ): TermValue[String]  = TermValue(v)
  implicit def int2Model    (v: Int    ): TermValue[Int]     = TermValue(v)
  implicit def long2Model   (v: Long   ): TermValue[Long]    = TermValue(v)
  implicit def float2Model  (v: Float  ): TermValue[Float]   = TermValue(v)
  implicit def double2Model (v: Double ): TermValue[Double]  = TermValue(v)
  implicit def boolean2Model(v: Boolean): TermValue[Boolean] = TermValue(v)
  implicit def date2Model   (v: Date   ): TermValue[Date]    = TermValue(v)
  implicit def uuid2Model   (v: UUID   ): TermValue[UUID]    = TermValue(v)
  implicit def uri2Model    (v: URI    ): TermValue[URI]     = TermValue(v)

  implicit def stringSet2Model (set: Set[String] ): TermValue[Set[String]]  = TermValue(set)
  implicit def intSet2Model    (set: Set[Int]    ): TermValue[Set[Int]]     = TermValue(set)
  implicit def longSet2Model   (set: Set[Long]   ): TermValue[Set[Long]]    = TermValue(set)
  implicit def floatSet2Model  (set: Set[Float]  ): TermValue[Set[Float]]   = TermValue(set)
  implicit def doubleSet2Model (set: Set[Double] ): TermValue[Set[Double]]  = TermValue(set)
  implicit def booleanSet2Model(set: Set[Boolean]): TermValue[Set[Boolean]] = TermValue(set)
  implicit def dateSet2Model   (set: Set[Date]   ): TermValue[Set[Date]]    = TermValue(set)
  implicit def uuidSet2Model   (set: Set[UUID]   ): TermValue[Set[UUID]]    = TermValue(set)
  implicit def uriSet2Model    (set: Set[URI]    ): TermValue[Set[URI]]     = TermValue(set)

  implicit def tuple2Model[A, B](tpl: (A, B)): TermValue[(A, B)] = TermValue(tpl)


  // Input marker
  trait ?
  object ? extends ?


  // Null marker (for non-asserted facts)
  trait nil
  object nil extends nil

  // Unifying marker for attributes to be unified in self-joins
  trait unify
  object unify extends unify


  // Aggregates

  trait distinct
  object distinct extends distinct

  trait max
  private[molecule] trait maxs
  object max extends max {
    def apply(i: Int): maxs = ???
  }

  /**
    * Minimum value
    * `val smallestSize = Car.size(min).get.head`
    * */
  trait min
  private[molecule] trait mins
  object min extends min {
    /**
      * Minimum n values
      * `val threeSmallestSizes = Car.size(min(3)).get.head`
      * */
    def apply(i: Int): mins = ???
  }

  trait rand
  private[molecule] trait rands
  object rand extends rand {
    def apply(i: Int): rands = ???
  }

  trait sample
  private[molecule] trait samples
  object sample extends sample {
    def apply(i: Int): samples = ???
  }

  trait count
  object count extends count

  trait countDistinct
  object countDistinct extends countDistinct

  trait sum
  object sum extends sum

  trait avg
  object avg extends avg

  trait median
  object median extends median

  trait variance
  object variance extends variance

  trait stddev
  object stddev extends stddev
}