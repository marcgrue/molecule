package molecule.expression

import molecule.api._
import scala.language.higherKinds


/** Aggregate keywords to mark aggregate expressions on attributes.
  * {{{
  *   // Aggregates on any attribute type
  *   Person.age(count).get.head         === 3   // count of asserted `age` attribute values
  *   Person.age(countDistinct).get.head === 3   // count of asserted distinct `age` attribute values
  *   Person.age(max).get.head           === 38  // maximum `age` value (using `compare`)
  *   Person.age(min).get.head           === 5   // maximum `age` value (using `compare`)
  *   Person.age(rand).get.head          === 25  // single random `age` value
  *   Person.age(sample).get.head        === 27  // single sample `age` value (when single value, same as random)
  *
  *   // Aggregates on any attribute type, returning multiple values
  *   Person.age(distinct).get.head  === Vector(5, 7, 38)  // distinct `age` values
  *   Person.age(max(2)).get.head    === Vector(38, 7)     // 2 maximum `age` values
  *   Person.age(min(2)).get.head    === Vector(5, 7)      // 2 minimum `age` values
  *   Person.age(rand(2)).get.head   === Stream(5, ?)      // 2 random `age` values (values can re-occur)
  *   Person.age(sample(2)).get.head === Vector(7, 38)     // 2 sample `age` values
  *
  *   // Aggregates on number attributes
  *   Person.age(sum).get.head      === 50               // sum of all `age` numbers
  *   Person.age(avg).get.head      === 16.66666667      // average of all `age` numbers
  *   Person.age(median).get.head   === 7                // median of all `age` numbers
  *   Person.age(stddev).get.head   === 15.107025591499  // standard deviation of all `age` numbers
  *   Person.age(variance).get.head === 228.2222222222   // variance of all `age` numbers
  * }}}
  *
  * @see [[http://www.scalamolecule.org/manual/attributes/aggregates/ Manual]]
  *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/expression/Aggregates.scala#L1 Tests]]
  * @groupname aggregates Aggregate keywords
  * @groupdesc aggregates Keywords applied to attributes that return aggregated value(s).
  * @groupprio aggregates 30
  * @groupname aggrNumber Number aggregation keywords
  * @groupdesc aggrNumber Keywords applied to number attributes that return aggregated value(s).
  * @groupprio aggrNumber 31
  * */
trait AggregateKeywords {

  // Aggregate keywords --------------------------------------------------------------------------------------------------------

  /** Count of attribute values.
    * <br><br>
    * Apply `count` keyword to attribute to return count of attribute values of entities matching the molecule.
    * {{{
    *   Person.firstName.lastName.age insert List(
    *     ("Ben", "Hayday", 42),
    *     ("Liz", "Taylor", 34),
    *     ("Liz", "Swifty", 34),
    *     ("Liz", "Mooray", 25)
    *   )
    *   Person.firstName.age(count).get === List(
    *     ("Ben", 1),
    *     ("Liz", 3) // 34, 34, 25
    *   )
    * }}}
    *
    * @return Int
    * @group aggregates
    **/
  object count extends count
  trait count


  /** Count of distinct attribute values.
    * <br><br>
    * Apply `countDistinct` keyword to attribute to return count of distinct attribute values of entities matching the molecule.
    * {{{
    *   Person.firstName.lastName.age insert List(
    *     ("Ben", "Hayday", 42),
    *     ("Liz", "Taylor", 34),
    *     ("Liz", "Swifty", 34),
    *     ("Liz", "Mooray", 25)
    *   )
    *   Person.firstName.age(countDistinct).get === List(
    *     ("Ben", 1),
    *     ("Liz", 2) // 34, 25
    *   )
    * }}}
    *
    * @return Int
    * @group aggregates
    **/
  object countDistinct extends countDistinct
  trait countDistinct


  /** Distinct attribute values.
    * <br><br>
    * Apply `distinct` keyword to attribute to return Vector of distinct attribute values of entities matching the molecule.
    * {{{
    *   Person.firstName.lastName.age insert List(
    *     ("Ben", "Hayday", 42),
    *     ("Liz", "Taylor", 34),
    *     ("Liz", "Swifty", 34),
    *     ("Liz", "Mooray", 25)
    *   )
    *   Person.firstName.age(distinct) insert List(
    *     ("Ben", 42),
    *     ("Liz", Vector(34, 25)) // only single 34 returned
    *   )
    * }}}
    *
    * @return Vector[attribute-type]
    * @group aggregates
    **/
  object distinct extends distinct
  trait distinct


  /** Maximum attribute value(s).
    * <br><br>
    * Apply `max` keyword to attribute to return the maximum attribute value of entities matching the molecule.
    * {{{
    *   Person.age.insert(25, 34, 37, 42, 70)
    *   Person.age(max).get.head === 70
    * }}}
    * Apply `max(n)` to return Vector of the n biggest values.
    * {{{
    *   Person.age(max(3)).get.head === Vector(37, 42, 70)
    * }}}
    *
    * @note `max`/`max(n)` supports all value types (via comparators).
    *       <br>`max(n)` Can at most return the number of values that match.
    * @group aggregates
    **/
  object max extends max {
    /** Maximum n values of attribute.
      * <br><br>
      * Apply `max(n)` to attribute to return Vector of the n biggest values of entities matching the molecule.
      * {{{
      *   Person.age.insert(25, 34, 37, 42, 70)
      *   Person.age(max(3)).get.head === List(37, 42, 70)
      * }}}
      *
      * @note `max`/`max(n)` supports all value types (via comparators).<br>
      *       Can at most return the number of values that match.
      * @return Vector[attribute-type]
      **/
    def apply(i: Int): maxs = ???
  }
  trait max
  trait maxs


  /** Minimum attribute value(s).
    * <br><br>
    * Apply `min` keyword to attribute to return the minimum attribute value of entities matching the molecule.
    * {{{
    *   Person.age.insert(25, 34, 37, 42, 70)
    *   Person.age(min).get.head === 25
    * }}}
    * Apply `min(n)` to return Vector of the n smallest values.
    * {{{
    *   Person.age(min(3)).get.head === Vector(25, 34, 37)
    * }}}
    *
    * @note `min`/`min(n)` supports all value types (via comparators).
    *       <br>`min(n)` Can at most return the number of values that match.
    * @group aggregates
    **/
  object min extends min {
    /** Minimum n values of attribute.
      * <br><br>
      * Apply `min(n)` to attribute to return Vector of the n smallest values of entities matching the molecule.
      * {{{
      *   Person.age.insert(25, 34, 37, 42, 70)
      *   Person.age(min(2)).get.head === List(25, 34)
      * }}}
      *
      * @note `min`/`min(n)` supports all value types (via comparators).<br>
      *       Can at most return the number of values that match.
      * @return Vector[attribute-type]
      **/
    def apply(i: Int): mins = ???
  }
  trait min
  trait mins


  /** Random attribute value(s).
    * <br><br>
    * Apply `random` keyword to attribute to return a single random attribute of entities matching the molecule.
    * {{{
    *   Person.age.insert(25, 34, 37, 42, 70)
    *   Person.age(random).get.head === 34 // or other..
    * }}}
    * Apply `random(n)` to return Vector of n random values. Observe though that duplicate random values can re-occur.
    * {{{
    *   Person.age(random(3)).get.head === Vector(42, 25, 42) // or other..
    * }}}
    * To get distinct values only, use the `sample(n)` keyword instead.
    *
    * @group aggregates
    **/
  object rand extends rand {
    /** Random values of attribute.
      * <br><br>
      * Apply a number n to `random` to return Stream of n random attribute values of from entities matching the molecule.
      * <br>Observe that duplicate random values can re-occur.
      * {{{
      *   Person.age.insert(25, 34, 37, 42, 70)
      *   Person.age(random(3)).get.head === Stream(42, 25, 42) // or other..
      * }}}
      * To get distinct values only, use the `sample(n)` keyword instead.
      *
      * @return Stream[attribute-type]
      * @group aggregates
      **/
    def apply(i: Int): rands = ???
  }
  trait rand
  trait rands


  /** Sample attribute value(s).
    * <br><br>
    * Apply `sample` keyword to attribute to return a single sample (random) attribute value of entities matching the molecule.
    * {{{
    *   Person.age.insert(25, 34, 37, 42, 70)
    *   Person.age(sample).get.head === 42 // or other..
    * }}}
    * Apply `sample(n)` to return Vector of up to n distinct sample values.
    * {{{
    *   Person.age(sample(3)).get.head === Vector(70, 25, 37) // or other..
    * }}}
    * If values don't need to be distinct, `random(n)` can be used also.
    *
    * @note Can at most return the number of values that match.
    * @group aggregates
    **/
  object sample extends sample {
    /** Distinct sample values of attribute.
      * <br><br>
      * Apply `sample(n)` to an attribute to return a Vector of up to n distinct sample values (can at most return the number of values that match).
      * {{{
      *   Person.age.insert(25, 34, 37, 42, 70)
      *   Person.age(sample(3)).get.head === Vector(42, 25, 42) // or other..
      * }}}
      * If values don't need to be distinct, `random(n)` can be used also.
      *
      * @note Can at most return the number of values that match.
      * @return Vector[attribute-type]
      * @group aggregates
      **/
    def apply(i: Int): samples = ???
  }
  trait sample
  trait samples


  // Aggregate number keywords ---------------------------------------------------------------------------------------------------

  /** Average of attribute values.
    * <br><br>
    * Apply `avg` keyword to attribute to return average of attribute values of entities matching the molecule.
    * {{{
    *   Match.score.insert(1, 2, 4)
    *   Match.score(avg).get.head === 2.3333333333333335 // (1 + 2 + 4) / 3
    * }}}
    *
    * @return Double
    * @group aggrNumber
    **/
  object avg extends avg
  trait avg


  /** Median of attribute values.
    * <br><br>
    * Apply `median` keyword to attribute to return median of attribute values of entities matching the molecule.
    * {{{
    *   Match.score.insert(1, 2, 4)
    *   Match.score(median).get.head === 2
    * }}}
    * OBS: When it comes to an even number of values, Datomic has a special implementation of median that is different from the one described on the
    * [[https://en.wikipedia.org/wiki/Median Wiki entry on the median function]].
    * <br><br>
    * Datomic calculates the median of even number of values as the average of the two middle numbers rounded down to nearest whole number
    * {{{
    *   Match.score.insert(1, 2, 3, 4)
    *   Match.score(median).get.head === 2 // (2 + 3) / 2 = 2.5 rounded down to 2
    * }}}
    * With decimal numbers this can go wrong:
    * {{{
    *   Match.score.insert(1.0, 2.5, 2.5, 3.0)
    *   Match.score(median).get.head === 2 // (2.5 + 2.5) / 2 = 2.5 rounded down to 2 (This is wrong and bug report has been filed)
    * }}}
    *
    * @return Value of Attribute type
    * @group aggrNumber
    **/
  object median extends median
  trait median


  /** Variance of attribute values.
    * <br><br>
    * Apply `stddev` keyword to attribute to return variance of attribute values of entities matching the molecule.
    * {{{
    *   Match.score.insert(1, 2, 4)
    *   Match.score(stddev).get.head === 1.247219128924647
    * }}}
    *
    * @return Double
    * @group aggrNumber
    **/
  object stddev extends stddev
  trait stddev


  /** Sum of attribute values.
    * <br><br>
    * Apply `sum` keyword to attribute to return sum of attribute values of entities matching the molecule.
    * {{{
    *   Match.score.insert(1, 2, 4)
    *   Match.score(sum).get.head === 7
    * }}}
    *
    * @return Value of Attribute type
    * @group aggrNumber
    **/
  object sum extends sum
  trait sum


  /** Variance of attribute values.
    * <br><br>
    * Apply `variance` keyword to attribute to return variance of attribute values of entities matching the molecule.
    * {{{
    *   Match.score.insert(1, 2, 4)
    *   Match.score(variance).get.head === 1.5555555555555556
    * }}}
    *
    * @return Double
    * @group aggrNumber
    **/
  object variance extends variance
  trait variance
}

/** Apply methods of arity 1-22 taking aggregate keywords. */
object AggregateKeywords {

  trait Aggregate00[Ns0]

  trait Aggregate01[Ns1[_], A] {

    def apply(v: min)     : Ns1[A] = ???
    def apply(v: max)     : Ns1[A] = ???
    def apply(v: rand)    : Ns1[A] = ???
    def apply(v: sample)  : Ns1[A] = ???

    def apply(v: mins)    : Ns1[Vector[A]] = ???
    def apply(v: maxs)    : Ns1[Vector[A]] = ???
    def apply(v: distinct): Ns1[Vector[A]] = ???
    def apply(v: rands)   : Ns1[Stream[A]] = ???
    def apply(v: samples) : Ns1[Vector[A]] = ???

    def apply(v: count)        : Ns1[Int   ] = ???
    def apply(v: countDistinct): Ns1[Int   ] = ???
    def apply(v: sum)          : Ns1[A     ] = ???
    def apply(v: avg)          : Ns1[Double] = ???
    def apply(v: median)       : Ns1[A     ] = ???
    def apply(v: variance)     : Ns1[Double] = ???
    def apply(v: stddev)       : Ns1[Double] = ???

    lazy val length: Ns1[Int] = ???
  }


  trait Aggregate02[Ns2[_,_], A, B] {

    def apply(v: min)     : Ns2[A, B] = ???
    def apply(v: max)     : Ns2[A, B] = ???
    def apply(v: rand)    : Ns2[A, B] = ???
    def apply(v: sample)  : Ns2[A, B] = ???

    def apply(v: mins)    : Ns2[A, Vector[B]] = ???
    def apply(v: maxs)    : Ns2[A, Vector[B]] = ???
    def apply(v: distinct): Ns2[A, Vector[B]] = ???
    def apply(v: rands)   : Ns2[A, Stream[B]] = ???
    def apply(v: samples) : Ns2[A, Vector[B]] = ???

    def apply(v: count)        : Ns2[A, Int   ] = ???
    def apply(v: countDistinct): Ns2[A, Int   ] = ???
    def apply(v: sum)          : Ns2[A, B     ] = ???
    def apply(v: avg)          : Ns2[A, Double] = ???
    def apply(v: median)       : Ns2[A, B     ] = ???
    def apply(v: variance)     : Ns2[A, Double] = ???
    def apply(v: stddev)       : Ns2[A, Double] = ???

    lazy val length: Ns2[A, Int] = ???
  }


  trait Aggregate03[Ns3[_,_,_], A, B, C] {

    def apply(v: min)     : Ns3[A, B, C] = ???
    def apply(v: max)     : Ns3[A, B, C] = ???
    def apply(v: rand)    : Ns3[A, B, C] = ???
    def apply(v: sample)  : Ns3[A, B, C] = ???

    def apply(v: mins)    : Ns3[A, B, Vector[C]] = ???
    def apply(v: maxs)    : Ns3[A, B, Vector[C]] = ???
    def apply(v: distinct): Ns3[A, B, Vector[C]] = ???
    def apply(v: rands)   : Ns3[A, B, Stream[C]] = ???
    def apply(v: samples) : Ns3[A, B, Vector[C]] = ???

    def apply(v: count)        : Ns3[A, B, Int   ] = ???
    def apply(v: countDistinct): Ns3[A, B, Int   ] = ???
    def apply(v: sum)          : Ns3[A, B, C     ] = ???
    def apply(v: avg)          : Ns3[A, B, Double] = ???
    def apply(v: median)       : Ns3[A, B, C     ] = ???
    def apply(v: variance)     : Ns3[A, B, Double] = ???
    def apply(v: stddev)       : Ns3[A, B, Double] = ???

    lazy val length: Ns3[A, B, Int] = ???
  }


  trait Aggregate04[Ns4[_,_,_,_], A, B, C, D] {

    def apply(v: min)     : Ns4[A, B, C, D] = ???
    def apply(v: max)     : Ns4[A, B, C, D] = ???
    def apply(v: rand)    : Ns4[A, B, C, D] = ???
    def apply(v: sample)  : Ns4[A, B, C, D] = ???

    def apply(v: mins)    : Ns4[A, B, C, Vector[D]] = ???
    def apply(v: maxs)    : Ns4[A, B, C, Vector[D]] = ???
    def apply(v: distinct): Ns4[A, B, C, Vector[D]] = ???
    def apply(v: rands)   : Ns4[A, B, C, Stream[D]] = ???
    def apply(v: samples) : Ns4[A, B, C, Vector[D]] = ???

    def apply(v: count)        : Ns4[A, B, C, Int   ] = ???
    def apply(v: countDistinct): Ns4[A, B, C, Int   ] = ???
    def apply(v: sum)          : Ns4[A, B, C, D     ] = ???
    def apply(v: avg)          : Ns4[A, B, C, Double] = ???
    def apply(v: median)       : Ns4[A, B, C, D     ] = ???
    def apply(v: variance)     : Ns4[A, B, C, Double] = ???
    def apply(v: stddev)       : Ns4[A, B, C, Double] = ???

    lazy val length: Ns4[A, B, C, Int] = ???
  }


  trait Aggregate05[Ns5[_,_,_,_,_], A, B, C, D, E] {

    def apply(v: min)     : Ns5[A, B, C, D, E] = ???
    def apply(v: max)     : Ns5[A, B, C, D, E] = ???
    def apply(v: rand)    : Ns5[A, B, C, D, E] = ???
    def apply(v: sample)  : Ns5[A, B, C, D, E] = ???

    def apply(v: mins)    : Ns5[A, B, C, D, Vector[E]] = ???
    def apply(v: maxs)    : Ns5[A, B, C, D, Vector[E]] = ???
    def apply(v: distinct): Ns5[A, B, C, D, Vector[E]] = ???
    def apply(v: rands)   : Ns5[A, B, C, D, Stream[E]] = ???
    def apply(v: samples) : Ns5[A, B, C, D, Vector[E]] = ???

    def apply(v: count)        : Ns5[A, B, C, D, Int   ] = ???
    def apply(v: countDistinct): Ns5[A, B, C, D, Int   ] = ???
    def apply(v: sum)          : Ns5[A, B, C, D, E     ] = ???
    def apply(v: avg)          : Ns5[A, B, C, D, Double] = ???
    def apply(v: median)       : Ns5[A, B, C, D, E     ] = ???
    def apply(v: variance)     : Ns5[A, B, C, D, Double] = ???
    def apply(v: stddev)       : Ns5[A, B, C, D, Double] = ???

    lazy val length: Ns5[A, B, C, D, Int] = ???
  }


  trait Aggregate06[Ns6[_,_,_,_,_,_], A, B, C, D, E, F] {

    def apply(v: min)     : Ns6[A, B, C, D, E, F] = ???
    def apply(v: max)     : Ns6[A, B, C, D, E, F] = ???
    def apply(v: rand)    : Ns6[A, B, C, D, E, F] = ???
    def apply(v: sample)  : Ns6[A, B, C, D, E, F] = ???

    def apply(v: mins)    : Ns6[A, B, C, D, E, Vector[F]] = ???
    def apply(v: maxs)    : Ns6[A, B, C, D, E, Vector[F]] = ???
    def apply(v: distinct): Ns6[A, B, C, D, E, Vector[F]] = ???
    def apply(v: rands)   : Ns6[A, B, C, D, E, Stream[F]] = ???
    def apply(v: samples) : Ns6[A, B, C, D, E, Vector[F]] = ???

    def apply(v: count)        : Ns6[A, B, C, D, E, Int   ] = ???
    def apply(v: countDistinct): Ns6[A, B, C, D, E, Int   ] = ???
    def apply(v: sum)          : Ns6[A, B, C, D, E, F     ] = ???
    def apply(v: avg)          : Ns6[A, B, C, D, E, Double] = ???
    def apply(v: median)       : Ns6[A, B, C, D, E, F     ] = ???
    def apply(v: variance)     : Ns6[A, B, C, D, E, Double] = ???
    def apply(v: stddev)       : Ns6[A, B, C, D, E, Double] = ???

    lazy val length: Ns6[A, B, C, D, E, Int] = ???
  }


  trait Aggregate07[Ns7[_,_,_,_,_,_,_], A, B, C, D, E, F, G] {

    def apply(v: min)     : Ns7[A, B, C, D, E, F, G] = ???
    def apply(v: max)     : Ns7[A, B, C, D, E, F, G] = ???
    def apply(v: rand)    : Ns7[A, B, C, D, E, F, G] = ???
    def apply(v: sample)  : Ns7[A, B, C, D, E, F, G] = ???

    def apply(v: mins)    : Ns7[A, B, C, D, E, F, Vector[G]] = ???
    def apply(v: maxs)    : Ns7[A, B, C, D, E, F, Vector[G]] = ???
    def apply(v: distinct): Ns7[A, B, C, D, E, F, Vector[G]] = ???
    def apply(v: rands)   : Ns7[A, B, C, D, E, F, Stream[G]] = ???
    def apply(v: samples) : Ns7[A, B, C, D, E, F, Vector[G]] = ???

    def apply(v: count)        : Ns7[A, B, C, D, E, F, Int   ] = ???
    def apply(v: countDistinct): Ns7[A, B, C, D, E, F, Int   ] = ???
    def apply(v: sum)          : Ns7[A, B, C, D, E, F, G     ] = ???
    def apply(v: avg)          : Ns7[A, B, C, D, E, F, Double] = ???
    def apply(v: median)       : Ns7[A, B, C, D, E, F, G     ] = ???
    def apply(v: variance)     : Ns7[A, B, C, D, E, F, Double] = ???
    def apply(v: stddev)       : Ns7[A, B, C, D, E, F, Double] = ???

    lazy val length: Ns7[A, B, C, D, E, F, Int] = ???
  }


  trait Aggregate08[Ns8[_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {

    def apply(v: min)     : Ns8[A, B, C, D, E, F, G, H] = ???
    def apply(v: max)     : Ns8[A, B, C, D, E, F, G, H] = ???
    def apply(v: rand)    : Ns8[A, B, C, D, E, F, G, H] = ???
    def apply(v: sample)  : Ns8[A, B, C, D, E, F, G, H] = ???

    def apply(v: mins)    : Ns8[A, B, C, D, E, F, G, Vector[H]] = ???
    def apply(v: maxs)    : Ns8[A, B, C, D, E, F, G, Vector[H]] = ???
    def apply(v: distinct): Ns8[A, B, C, D, E, F, G, Vector[H]] = ???
    def apply(v: rands)   : Ns8[A, B, C, D, E, F, G, Stream[H]] = ???
    def apply(v: samples) : Ns8[A, B, C, D, E, F, G, Vector[H]] = ???

    def apply(v: count)        : Ns8[A, B, C, D, E, F, G, Int   ] = ???
    def apply(v: countDistinct): Ns8[A, B, C, D, E, F, G, Int   ] = ???
    def apply(v: sum)          : Ns8[A, B, C, D, E, F, G, H     ] = ???
    def apply(v: avg)          : Ns8[A, B, C, D, E, F, G, Double] = ???
    def apply(v: median)       : Ns8[A, B, C, D, E, F, G, H     ] = ???
    def apply(v: variance)     : Ns8[A, B, C, D, E, F, G, Double] = ???
    def apply(v: stddev)       : Ns8[A, B, C, D, E, F, G, Double] = ???

    lazy val length: Ns8[A, B, C, D, E, F, G, Int] = ???
  }


  trait Aggregate09[Ns9[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {

    def apply(v: min)     : Ns9[A, B, C, D, E, F, G, H, I] = ???
    def apply(v: max)     : Ns9[A, B, C, D, E, F, G, H, I] = ???
    def apply(v: rand)    : Ns9[A, B, C, D, E, F, G, H, I] = ???
    def apply(v: sample)  : Ns9[A, B, C, D, E, F, G, H, I] = ???

    def apply(v: mins)    : Ns9[A, B, C, D, E, F, G, H, Vector[I]] = ???
    def apply(v: maxs)    : Ns9[A, B, C, D, E, F, G, H, Vector[I]] = ???
    def apply(v: distinct): Ns9[A, B, C, D, E, F, G, H, Vector[I]] = ???
    def apply(v: rands)   : Ns9[A, B, C, D, E, F, G, H, Stream[I]] = ???
    def apply(v: samples) : Ns9[A, B, C, D, E, F, G, H, Vector[I]] = ???

    def apply(v: count)        : Ns9[A, B, C, D, E, F, G, H, Int   ] = ???
    def apply(v: countDistinct): Ns9[A, B, C, D, E, F, G, H, Int   ] = ???
    def apply(v: sum)          : Ns9[A, B, C, D, E, F, G, H, I     ] = ???
    def apply(v: avg)          : Ns9[A, B, C, D, E, F, G, H, Double] = ???
    def apply(v: median)       : Ns9[A, B, C, D, E, F, G, H, I     ] = ???
    def apply(v: variance)     : Ns9[A, B, C, D, E, F, G, H, Double] = ???
    def apply(v: stddev)       : Ns9[A, B, C, D, E, F, G, H, Double] = ???

    lazy val length: Ns9[A, B, C, D, E, F, G, H, Int] = ???
  }


  trait Aggregate10[Ns10[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {

    def apply(v: min)     : Ns10[A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: max)     : Ns10[A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: rand)    : Ns10[A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: sample)  : Ns10[A, B, C, D, E, F, G, H, I, J] = ???

    def apply(v: mins)    : Ns10[A, B, C, D, E, F, G, H, I, Vector[J]] = ???
    def apply(v: maxs)    : Ns10[A, B, C, D, E, F, G, H, I, Vector[J]] = ???
    def apply(v: distinct): Ns10[A, B, C, D, E, F, G, H, I, Vector[J]] = ???
    def apply(v: rands)   : Ns10[A, B, C, D, E, F, G, H, I, Stream[J]] = ???
    def apply(v: samples) : Ns10[A, B, C, D, E, F, G, H, I, Vector[J]] = ???

    def apply(v: count)        : Ns10[A, B, C, D, E, F, G, H, I, Int   ] = ???
    def apply(v: countDistinct): Ns10[A, B, C, D, E, F, G, H, I, Int   ] = ???
    def apply(v: sum)          : Ns10[A, B, C, D, E, F, G, H, I, J     ] = ???
    def apply(v: avg)          : Ns10[A, B, C, D, E, F, G, H, I, Double] = ???
    def apply(v: median)       : Ns10[A, B, C, D, E, F, G, H, I, J     ] = ???
    def apply(v: variance)     : Ns10[A, B, C, D, E, F, G, H, I, Double] = ???
    def apply(v: stddev)       : Ns10[A, B, C, D, E, F, G, H, I, Double] = ???

    lazy val length: Ns10[A, B, C, D, E, F, G, H, I, Int] = ???
  }


  trait Aggregate11[Ns11[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {

    def apply(v: min)     : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: max)     : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: rand)    : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: sample)  : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???

    def apply(v: mins)    : Ns11[A, B, C, D, E, F, G, H, I, J, Vector[K]] = ???
    def apply(v: maxs)    : Ns11[A, B, C, D, E, F, G, H, I, J, Vector[K]] = ???
    def apply(v: distinct): Ns11[A, B, C, D, E, F, G, H, I, J, Vector[K]] = ???
    def apply(v: rands)   : Ns11[A, B, C, D, E, F, G, H, I, J, Stream[K]] = ???
    def apply(v: samples) : Ns11[A, B, C, D, E, F, G, H, I, J, Vector[K]] = ???

    def apply(v: count)        : Ns11[A, B, C, D, E, F, G, H, I, J, Int   ] = ???
    def apply(v: countDistinct): Ns11[A, B, C, D, E, F, G, H, I, J, Int   ] = ???
    def apply(v: sum)          : Ns11[A, B, C, D, E, F, G, H, I, J, K     ] = ???
    def apply(v: avg)          : Ns11[A, B, C, D, E, F, G, H, I, J, Double] = ???
    def apply(v: median)       : Ns11[A, B, C, D, E, F, G, H, I, J, K     ] = ???
    def apply(v: variance)     : Ns11[A, B, C, D, E, F, G, H, I, J, Double] = ???
    def apply(v: stddev)       : Ns11[A, B, C, D, E, F, G, H, I, J, Double] = ???

    lazy val length: Ns11[A, B, C, D, E, F, G, H, I, J, Int] = ???
  }


  trait Aggregate12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {

    def apply(v: min)     : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: max)     : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: rand)    : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: sample)  : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???

    def apply(v: mins)    : Ns12[A, B, C, D, E, F, G, H, I, J, K, Vector[L]] = ???
    def apply(v: maxs)    : Ns12[A, B, C, D, E, F, G, H, I, J, K, Vector[L]] = ???
    def apply(v: distinct): Ns12[A, B, C, D, E, F, G, H, I, J, K, Vector[L]] = ???
    def apply(v: rands)   : Ns12[A, B, C, D, E, F, G, H, I, J, K, Stream[L]] = ???
    def apply(v: samples) : Ns12[A, B, C, D, E, F, G, H, I, J, K, Vector[L]] = ???

    def apply(v: count)        : Ns12[A, B, C, D, E, F, G, H, I, J, K, Int   ] = ???
    def apply(v: countDistinct): Ns12[A, B, C, D, E, F, G, H, I, J, K, Int   ] = ???
    def apply(v: sum)          : Ns12[A, B, C, D, E, F, G, H, I, J, K, L     ] = ???
    def apply(v: avg)          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Double] = ???
    def apply(v: median)       : Ns12[A, B, C, D, E, F, G, H, I, J, K, L     ] = ???
    def apply(v: variance)     : Ns12[A, B, C, D, E, F, G, H, I, J, K, Double] = ???
    def apply(v: stddev)       : Ns12[A, B, C, D, E, F, G, H, I, J, K, Double] = ???

    lazy val length: Ns12[A, B, C, D, E, F, G, H, I, J, K, Int] = ???
  }


  trait Aggregate13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {

    def apply(v: min)     : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: max)     : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: rand)    : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: sample)  : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???

    def apply(v: mins)    : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Vector[M]] = ???
    def apply(v: maxs)    : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Vector[M]] = ???
    def apply(v: distinct): Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Vector[M]] = ???
    def apply(v: rands)   : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Stream[M]] = ???
    def apply(v: samples) : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Vector[M]] = ???

    def apply(v: count)        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int   ] = ???
    def apply(v: countDistinct): Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int   ] = ???
    def apply(v: sum)          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M     ] = ???
    def apply(v: avg)          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
    def apply(v: median)       : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M     ] = ???
    def apply(v: variance)     : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
    def apply(v: stddev)       : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???

    lazy val length: Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int] = ???
  }


  trait Aggregate14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {

    def apply(v: min)     : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: max)     : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: rand)    : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: sample)  : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???

    def apply(v: mins)    : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Vector[N]] = ???
    def apply(v: maxs)    : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Vector[N]] = ???
    def apply(v: distinct): Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Vector[N]] = ???
    def apply(v: rands)   : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Stream[N]] = ???
    def apply(v: samples) : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Vector[N]] = ???

    def apply(v: count)        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
    def apply(v: countDistinct): Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
    def apply(v: sum)          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N     ] = ???
    def apply(v: avg)          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
    def apply(v: median)       : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N     ] = ???
    def apply(v: variance)     : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
    def apply(v: stddev)       : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???

    lazy val length:Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
  }


  trait Aggregate15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {

    def apply(v: min)     : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: max)     : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: rand)    : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: sample)  : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???

    def apply(v: mins)    : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Vector[O]] = ???
    def apply(v: maxs)    : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Vector[O]] = ???
    def apply(v: distinct): Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Vector[O]] = ???
    def apply(v: rands)   : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Stream[O]] = ???
    def apply(v: samples) : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Vector[O]] = ???

    def apply(v: count)        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int   ] = ???
    def apply(v: countDistinct): Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int   ] = ???
    def apply(v: sum)          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ] = ???
    def apply(v: avg)          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
    def apply(v: median)       : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ] = ???
    def apply(v: variance)     : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
    def apply(v: stddev)       : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???

    lazy val length: Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int] = ???
  }


  trait Aggregate16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {

    def apply(v: min)     : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: max)     : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: rand)    : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: sample)  : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???

    def apply(v: mins)    : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Vector[P]] = ???
    def apply(v: maxs)    : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Vector[P]] = ???
    def apply(v: distinct): Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Vector[P]] = ???
    def apply(v: rands)   : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Stream[P]] = ???
    def apply(v: samples) : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Vector[P]] = ???

    def apply(v: count)        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int   ] = ???
    def apply(v: countDistinct): Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int   ] = ???
    def apply(v: sum)          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P     ] = ???
    def apply(v: avg)          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
    def apply(v: median)       : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P     ] = ???
    def apply(v: variance)     : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
    def apply(v: stddev)       : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???

    lazy val length: Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int] = ???
  }


  trait Aggregate17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {

    def apply(v: min)     : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: max)     : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: rand)    : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: sample)  : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???

    def apply(v: mins)    : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Vector[Q]] = ???
    def apply(v: maxs)    : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Vector[Q]] = ???
    def apply(v: distinct): Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Vector[Q]] = ???
    def apply(v: rands)   : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Stream[Q]] = ???
    def apply(v: samples) : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Vector[Q]] = ???

    def apply(v: count)        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int   ] = ???
    def apply(v: countDistinct): Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int   ] = ???
    def apply(v: sum)          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q     ] = ???
    def apply(v: avg)          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
    def apply(v: median)       : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q     ] = ???
    def apply(v: variance)     : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
    def apply(v: stddev)       : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???

    lazy val length: Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int] = ???
  }


  trait Aggregate18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {

    def apply(v: min)     : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: max)     : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: rand)    : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: sample)  : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???

    def apply(v: mins)    : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Vector[R]] = ???
    def apply(v: maxs)    : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Vector[R]] = ???
    def apply(v: distinct): Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Vector[R]] = ???
    def apply(v: rands)   : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Stream[R]] = ???
    def apply(v: samples) : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Vector[R]] = ???

    def apply(v: count)        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int   ] = ???
    def apply(v: countDistinct): Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int   ] = ???
    def apply(v: sum)          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R     ] = ???
    def apply(v: avg)          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
    def apply(v: median)       : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R     ] = ???
    def apply(v: variance)     : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
    def apply(v: stddev)       : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???

    lazy val length: Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int] = ???
  }


  trait Aggregate19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {

    def apply(v: min)     : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: max)     : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: rand)    : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: sample)  : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???

    def apply(v: mins)    : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Vector[S]] = ???
    def apply(v: maxs)    : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Vector[S]] = ???
    def apply(v: distinct): Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Vector[S]] = ???
    def apply(v: rands)   : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Stream[S]] = ???
    def apply(v: samples) : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Vector[S]] = ???

    def apply(v: count)        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int   ] = ???
    def apply(v: countDistinct): Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int   ] = ???
    def apply(v: sum)          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S     ] = ???
    def apply(v: avg)          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
    def apply(v: median)       : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S     ] = ???
    def apply(v: variance)     : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
    def apply(v: stddev)       : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???

    lazy val length: Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int] = ???
  }


  trait Aggregate20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {

    def apply(v: min)     : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: max)     : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: rand)    : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: sample)  : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???

    def apply(v: mins)    : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Vector[T]] = ???
    def apply(v: maxs)    : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Vector[T]] = ???
    def apply(v: distinct): Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Vector[T]] = ???
    def apply(v: rands)   : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Stream[T]] = ???
    def apply(v: samples) : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Vector[T]] = ???

    def apply(v: count)        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int   ] = ???
    def apply(v: countDistinct): Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int   ] = ???
    def apply(v: sum)          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T     ] = ???
    def apply(v: avg)          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
    def apply(v: median)       : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T     ] = ???
    def apply(v: variance)     : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
    def apply(v: stddev)       : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???

    lazy val length: Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int] = ???
  }


  trait Aggregate21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {

    def apply(v: min)     : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: max)     : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: rand)    : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: sample)  : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???

    def apply(v: mins)    : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Vector[U]] = ???
    def apply(v: maxs)    : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Vector[U]] = ???
    def apply(v: distinct): Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Vector[U]] = ???
    def apply(v: rands)   : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Stream[U]] = ???
    def apply(v: samples) : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Vector[U]] = ???

    def apply(v: count)        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int   ] = ???
    def apply(v: countDistinct): Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int   ] = ???
    def apply(v: sum)          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ] = ???
    def apply(v: avg)          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
    def apply(v: median)       : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ] = ???
    def apply(v: variance)     : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
    def apply(v: stddev)       : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???

    lazy val length: Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
  }


  trait Aggregate22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {

    def apply(v: min)     : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: max)     : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: rand)    : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: sample)  : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???

    def apply(v: mins)    : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Vector[U]] = ???
    def apply(v: maxs)    : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Vector[U]] = ???
    def apply(v: distinct): Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Vector[U]] = ???
    def apply(v: rands)   : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Stream[U]] = ???
    def apply(v: samples) : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Vector[U]] = ???

    def apply(v: count)        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int   ] = ???
    def apply(v: countDistinct): Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int   ] = ???
    def apply(v: sum)          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V     ] = ???
    def apply(v: avg)          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
    def apply(v: median)       : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V     ] = ???
    def apply(v: variance)     : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
    def apply(v: stddev)       : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???

    lazy val length: Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int] = ???
  }
}
