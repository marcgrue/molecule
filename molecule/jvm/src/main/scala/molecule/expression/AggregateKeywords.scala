package molecule.expression

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
    * @return List[attribute-type]
    * @group aggregates
    **/
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
  trait max {
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
      * @return List[attribute-type]
      **/
    def apply(i: Int): maxs = ???
  }
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
  trait min {
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
      * @return List[attribute-type]
      **/
    def apply(i: Int): mins = ???
  }
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
  trait rand {
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
      * @return List[attribute-type]
      * @group aggregates
      **/
    def apply(i: Int): rands = ???
  }
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
  trait sample {
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
      * @return List[attribute-type]
      * @group aggregates
      **/
    def apply(i: Int): samples = ???
  }
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
  trait variance
}

/** Apply methods of arity 1-22 taking aggregate keywords. */
object AggregateKeywords extends AggregateKeywords {

  import molecule.api.core

  trait Aggregate00[Ns0]

  trait Aggregate01[Ns1[_], A] {

    def apply(v: core.min)     : Ns1[A] = ???
    def apply(v: core.max)     : Ns1[A] = ???
    def apply(v: core.rand)    : Ns1[A] = ???
    def apply(v: core.sample)  : Ns1[A] = ???

    def apply(v: core.mins)    : Ns1[List[A]] = ???
    def apply(v: core.maxs)    : Ns1[List[A]] = ???
    def apply(v: core.distinct): Ns1[List[A]] = ???
    def apply(v: core.rands)   : Ns1[List[A]] = ???
    def apply(v: core.samples) : Ns1[List[A]] = ???

    def apply(v: core.count)        : Ns1[Int   ] = ???
    def apply(v: core.countDistinct): Ns1[Int   ] = ???
    def apply(v: core.sum)          : Ns1[A     ] = ???
    def apply(v: core.avg)          : Ns1[Double] = ???
    def apply(v: core.median)       : Ns1[A     ] = ???
    def apply(v: core.variance)     : Ns1[Double] = ???
    def apply(v: core.stddev)       : Ns1[Double] = ???
  }


  trait Aggregate02[Ns2[_,_], A, B] {

    def apply(v: core.min)     : Ns2[A, B] = ???
    def apply(v: core.max)     : Ns2[A, B] = ???
    def apply(v: core.rand)    : Ns2[A, B] = ???
    def apply(v: core.sample)  : Ns2[A, B] = ???

    def apply(v: core.mins)    : Ns2[A, List[B]] = ???
    def apply(v: core.maxs)    : Ns2[A, List[B]] = ???
    def apply(v: core.distinct): Ns2[A, List[B]] = ???
    def apply(v: core.rands)   : Ns2[A, List[B]] = ???
    def apply(v: core.samples) : Ns2[A, List[B]] = ???

    def apply(v: core.count)        : Ns2[A, Int   ] = ???
    def apply(v: core.countDistinct): Ns2[A, Int   ] = ???
    def apply(v: core.sum)          : Ns2[A, B     ] = ???
    def apply(v: core.avg)          : Ns2[A, Double] = ???
    def apply(v: core.median)       : Ns2[A, B     ] = ???
    def apply(v: core.variance)     : Ns2[A, Double] = ???
    def apply(v: core.stddev)       : Ns2[A, Double] = ???
  }


  trait Aggregate03[Ns3[_,_,_], A, B, C] {

    def apply(v: core.min)     : Ns3[A, B, C] = ???
    def apply(v: core.max)     : Ns3[A, B, C] = ???
    def apply(v: core.rand)    : Ns3[A, B, C] = ???
    def apply(v: core.sample)  : Ns3[A, B, C] = ???

    def apply(v: core.mins)    : Ns3[A, B, List[C]] = ???
    def apply(v: core.maxs)    : Ns3[A, B, List[C]] = ???
    def apply(v: core.distinct): Ns3[A, B, List[C]] = ???
    def apply(v: core.rands)   : Ns3[A, B, List[C]] = ???
    def apply(v: core.samples) : Ns3[A, B, List[C]] = ???

    def apply(v: core.count)        : Ns3[A, B, Int   ] = ???
    def apply(v: core.countDistinct): Ns3[A, B, Int   ] = ???
    def apply(v: core.sum)          : Ns3[A, B, C     ] = ???
    def apply(v: core.avg)          : Ns3[A, B, Double] = ???
    def apply(v: core.median)       : Ns3[A, B, C     ] = ???
    def apply(v: core.variance)     : Ns3[A, B, Double] = ???
    def apply(v: core.stddev)       : Ns3[A, B, Double] = ???
  }


  trait Aggregate04[Ns4[_,_,_,_], A, B, C, D] {

    def apply(v: core.min)     : Ns4[A, B, C, D] = ???
    def apply(v: core.max)     : Ns4[A, B, C, D] = ???
    def apply(v: core.rand)    : Ns4[A, B, C, D] = ???
    def apply(v: core.sample)  : Ns4[A, B, C, D] = ???

    def apply(v: core.mins)    : Ns4[A, B, C, List[D]] = ???
    def apply(v: core.maxs)    : Ns4[A, B, C, List[D]] = ???
    def apply(v: core.distinct): Ns4[A, B, C, List[D]] = ???
    def apply(v: core.rands)   : Ns4[A, B, C, List[D]] = ???
    def apply(v: core.samples) : Ns4[A, B, C, List[D]] = ???

    def apply(v: core.count)        : Ns4[A, B, C, Int   ] = ???
    def apply(v: core.countDistinct): Ns4[A, B, C, Int   ] = ???
    def apply(v: core.sum)          : Ns4[A, B, C, D     ] = ???
    def apply(v: core.avg)          : Ns4[A, B, C, Double] = ???
    def apply(v: core.median)       : Ns4[A, B, C, D     ] = ???
    def apply(v: core.variance)     : Ns4[A, B, C, Double] = ???
    def apply(v: core.stddev)       : Ns4[A, B, C, Double] = ???
  }


  trait Aggregate05[Ns5[_,_,_,_,_], A, B, C, D, E] {

    def apply(v: core.min)     : Ns5[A, B, C, D, E] = ???
    def apply(v: core.max)     : Ns5[A, B, C, D, E] = ???
    def apply(v: core.rand)    : Ns5[A, B, C, D, E] = ???
    def apply(v: core.sample)  : Ns5[A, B, C, D, E] = ???

    def apply(v: core.mins)    : Ns5[A, B, C, D, List[E]] = ???
    def apply(v: core.maxs)    : Ns5[A, B, C, D, List[E]] = ???
    def apply(v: core.distinct): Ns5[A, B, C, D, List[E]] = ???
    def apply(v: core.rands)   : Ns5[A, B, C, D, List[E]] = ???
    def apply(v: core.samples) : Ns5[A, B, C, D, List[E]] = ???

    def apply(v: core.count)        : Ns5[A, B, C, D, Int   ] = ???
    def apply(v: core.countDistinct): Ns5[A, B, C, D, Int   ] = ???
    def apply(v: core.sum)          : Ns5[A, B, C, D, E     ] = ???
    def apply(v: core.avg)          : Ns5[A, B, C, D, Double] = ???
    def apply(v: core.median)       : Ns5[A, B, C, D, E     ] = ???
    def apply(v: core.variance)     : Ns5[A, B, C, D, Double] = ???
    def apply(v: core.stddev)       : Ns5[A, B, C, D, Double] = ???
  }


  trait Aggregate06[Ns6[_,_,_,_,_,_], A, B, C, D, E, F] {

    def apply(v: core.min)     : Ns6[A, B, C, D, E, F] = ???
    def apply(v: core.max)     : Ns6[A, B, C, D, E, F] = ???
    def apply(v: core.rand)    : Ns6[A, B, C, D, E, F] = ???
    def apply(v: core.sample)  : Ns6[A, B, C, D, E, F] = ???

    def apply(v: core.mins)    : Ns6[A, B, C, D, E, List[F]] = ???
    def apply(v: core.maxs)    : Ns6[A, B, C, D, E, List[F]] = ???
    def apply(v: core.distinct): Ns6[A, B, C, D, E, List[F]] = ???
    def apply(v: core.rands)   : Ns6[A, B, C, D, E, List[F]] = ???
    def apply(v: core.samples) : Ns6[A, B, C, D, E, List[F]] = ???

    def apply(v: core.count)        : Ns6[A, B, C, D, E, Int   ] = ???
    def apply(v: core.countDistinct): Ns6[A, B, C, D, E, Int   ] = ???
    def apply(v: core.sum)          : Ns6[A, B, C, D, E, F     ] = ???
    def apply(v: core.avg)          : Ns6[A, B, C, D, E, Double] = ???
    def apply(v: core.median)       : Ns6[A, B, C, D, E, F     ] = ???
    def apply(v: core.variance)     : Ns6[A, B, C, D, E, Double] = ???
    def apply(v: core.stddev)       : Ns6[A, B, C, D, E, Double] = ???
  }


  trait Aggregate07[Ns7[_,_,_,_,_,_,_], A, B, C, D, E, F, G] {

    def apply(v: core.min)     : Ns7[A, B, C, D, E, F, G] = ???
    def apply(v: core.max)     : Ns7[A, B, C, D, E, F, G] = ???
    def apply(v: core.rand)    : Ns7[A, B, C, D, E, F, G] = ???
    def apply(v: core.sample)  : Ns7[A, B, C, D, E, F, G] = ???

    def apply(v: core.mins)    : Ns7[A, B, C, D, E, F, List[G]] = ???
    def apply(v: core.maxs)    : Ns7[A, B, C, D, E, F, List[G]] = ???
    def apply(v: core.distinct): Ns7[A, B, C, D, E, F, List[G]] = ???
    def apply(v: core.rands)   : Ns7[A, B, C, D, E, F, List[G]] = ???
    def apply(v: core.samples) : Ns7[A, B, C, D, E, F, List[G]] = ???

    def apply(v: core.count)        : Ns7[A, B, C, D, E, F, Int   ] = ???
    def apply(v: core.countDistinct): Ns7[A, B, C, D, E, F, Int   ] = ???
    def apply(v: core.sum)          : Ns7[A, B, C, D, E, F, G     ] = ???
    def apply(v: core.avg)          : Ns7[A, B, C, D, E, F, Double] = ???
    def apply(v: core.median)       : Ns7[A, B, C, D, E, F, G     ] = ???
    def apply(v: core.variance)     : Ns7[A, B, C, D, E, F, Double] = ???
    def apply(v: core.stddev)       : Ns7[A, B, C, D, E, F, Double] = ???
  }


  trait Aggregate08[Ns8[_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {

    def apply(v: core.min)     : Ns8[A, B, C, D, E, F, G, H] = ???
    def apply(v: core.max)     : Ns8[A, B, C, D, E, F, G, H] = ???
    def apply(v: core.rand)    : Ns8[A, B, C, D, E, F, G, H] = ???
    def apply(v: core.sample)  : Ns8[A, B, C, D, E, F, G, H] = ???

    def apply(v: core.mins)    : Ns8[A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: core.maxs)    : Ns8[A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: core.distinct): Ns8[A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: core.rands)   : Ns8[A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: core.samples) : Ns8[A, B, C, D, E, F, G, List[H]] = ???

    def apply(v: core.count)        : Ns8[A, B, C, D, E, F, G, Int   ] = ???
    def apply(v: core.countDistinct): Ns8[A, B, C, D, E, F, G, Int   ] = ???
    def apply(v: core.sum)          : Ns8[A, B, C, D, E, F, G, H     ] = ???
    def apply(v: core.avg)          : Ns8[A, B, C, D, E, F, G, Double] = ???
    def apply(v: core.median)       : Ns8[A, B, C, D, E, F, G, H     ] = ???
    def apply(v: core.variance)     : Ns8[A, B, C, D, E, F, G, Double] = ???
    def apply(v: core.stddev)       : Ns8[A, B, C, D, E, F, G, Double] = ???
  }


  trait Aggregate09[Ns9[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {

    def apply(v: core.min)     : Ns9[A, B, C, D, E, F, G, H, I] = ???
    def apply(v: core.max)     : Ns9[A, B, C, D, E, F, G, H, I] = ???
    def apply(v: core.rand)    : Ns9[A, B, C, D, E, F, G, H, I] = ???
    def apply(v: core.sample)  : Ns9[A, B, C, D, E, F, G, H, I] = ???

    def apply(v: core.mins)    : Ns9[A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: core.maxs)    : Ns9[A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: core.distinct): Ns9[A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: core.rands)   : Ns9[A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: core.samples) : Ns9[A, B, C, D, E, F, G, H, List[I]] = ???

    def apply(v: core.count)        : Ns9[A, B, C, D, E, F, G, H, Int   ] = ???
    def apply(v: core.countDistinct): Ns9[A, B, C, D, E, F, G, H, Int   ] = ???
    def apply(v: core.sum)          : Ns9[A, B, C, D, E, F, G, H, I     ] = ???
    def apply(v: core.avg)          : Ns9[A, B, C, D, E, F, G, H, Double] = ???
    def apply(v: core.median)       : Ns9[A, B, C, D, E, F, G, H, I     ] = ???
    def apply(v: core.variance)     : Ns9[A, B, C, D, E, F, G, H, Double] = ???
    def apply(v: core.stddev)       : Ns9[A, B, C, D, E, F, G, H, Double] = ???
  }


  trait Aggregate10[Ns10[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {

    def apply(v: core.min)     : Ns10[A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: core.max)     : Ns10[A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: core.rand)    : Ns10[A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: core.sample)  : Ns10[A, B, C, D, E, F, G, H, I, J] = ???

    def apply(v: core.mins)    : Ns10[A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: core.maxs)    : Ns10[A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: core.distinct): Ns10[A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: core.rands)   : Ns10[A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: core.samples) : Ns10[A, B, C, D, E, F, G, H, I, List[J]] = ???

    def apply(v: core.count)        : Ns10[A, B, C, D, E, F, G, H, I, Int   ] = ???
    def apply(v: core.countDistinct): Ns10[A, B, C, D, E, F, G, H, I, Int   ] = ???
    def apply(v: core.sum)          : Ns10[A, B, C, D, E, F, G, H, I, J     ] = ???
    def apply(v: core.avg)          : Ns10[A, B, C, D, E, F, G, H, I, Double] = ???
    def apply(v: core.median)       : Ns10[A, B, C, D, E, F, G, H, I, J     ] = ???
    def apply(v: core.variance)     : Ns10[A, B, C, D, E, F, G, H, I, Double] = ???
    def apply(v: core.stddev)       : Ns10[A, B, C, D, E, F, G, H, I, Double] = ???
  }


  trait Aggregate11[Ns11[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {

    def apply(v: core.min)     : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: core.max)     : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: core.rand)    : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: core.sample)  : Ns11[A, B, C, D, E, F, G, H, I, J, K] = ???

    def apply(v: core.mins)    : Ns11[A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: core.maxs)    : Ns11[A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: core.distinct): Ns11[A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: core.rands)   : Ns11[A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: core.samples) : Ns11[A, B, C, D, E, F, G, H, I, J, List[K]] = ???

    def apply(v: core.count)        : Ns11[A, B, C, D, E, F, G, H, I, J, Int   ] = ???
    def apply(v: core.countDistinct): Ns11[A, B, C, D, E, F, G, H, I, J, Int   ] = ???
    def apply(v: core.sum)          : Ns11[A, B, C, D, E, F, G, H, I, J, K     ] = ???
    def apply(v: core.avg)          : Ns11[A, B, C, D, E, F, G, H, I, J, Double] = ???
    def apply(v: core.median)       : Ns11[A, B, C, D, E, F, G, H, I, J, K     ] = ???
    def apply(v: core.variance)     : Ns11[A, B, C, D, E, F, G, H, I, J, Double] = ???
    def apply(v: core.stddev)       : Ns11[A, B, C, D, E, F, G, H, I, J, Double] = ???
  }


  trait Aggregate12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {

    def apply(v: core.min)     : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: core.max)     : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: core.rand)    : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: core.sample)  : Ns12[A, B, C, D, E, F, G, H, I, J, K, L] = ???

    def apply(v: core.mins)    : Ns12[A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: core.maxs)    : Ns12[A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: core.distinct): Ns12[A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: core.rands)   : Ns12[A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: core.samples) : Ns12[A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???

    def apply(v: core.count)        : Ns12[A, B, C, D, E, F, G, H, I, J, K, Int   ] = ???
    def apply(v: core.countDistinct): Ns12[A, B, C, D, E, F, G, H, I, J, K, Int   ] = ???
    def apply(v: core.sum)          : Ns12[A, B, C, D, E, F, G, H, I, J, K, L     ] = ???
    def apply(v: core.avg)          : Ns12[A, B, C, D, E, F, G, H, I, J, K, Double] = ???
    def apply(v: core.median)       : Ns12[A, B, C, D, E, F, G, H, I, J, K, L     ] = ???
    def apply(v: core.variance)     : Ns12[A, B, C, D, E, F, G, H, I, J, K, Double] = ???
    def apply(v: core.stddev)       : Ns12[A, B, C, D, E, F, G, H, I, J, K, Double] = ???
  }


  trait Aggregate13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {

    def apply(v: core.min)     : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: core.max)     : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: core.rand)    : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: core.sample)  : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M] = ???

    def apply(v: core.mins)    : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: core.maxs)    : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: core.distinct): Ns13[A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: core.rands)   : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: core.samples) : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???

    def apply(v: core.count)        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int   ] = ???
    def apply(v: core.countDistinct): Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int   ] = ???
    def apply(v: core.sum)          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M     ] = ???
    def apply(v: core.avg)          : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
    def apply(v: core.median)       : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, M     ] = ???
    def apply(v: core.variance)     : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
    def apply(v: core.stddev)       : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
  }


  trait Aggregate14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {

    def apply(v: core.min)     : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: core.max)     : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: core.rand)    : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: core.sample)  : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???

    def apply(v: core.mins)    : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: core.maxs)    : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: core.distinct): Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: core.rands)   : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: core.samples) : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???

    def apply(v: core.count)        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
    def apply(v: core.countDistinct): Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
    def apply(v: core.sum)          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N     ] = ???
    def apply(v: core.avg)          : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
    def apply(v: core.median)       : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, N     ] = ???
    def apply(v: core.variance)     : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
    def apply(v: core.stddev)       : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
  }


  trait Aggregate15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {

    def apply(v: core.min)     : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: core.max)     : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: core.rand)    : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: core.sample)  : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???

    def apply(v: core.mins)    : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: core.maxs)    : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: core.distinct): Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: core.rands)   : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: core.samples) : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???

    def apply(v: core.count)        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int   ] = ???
    def apply(v: core.countDistinct): Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int   ] = ???
    def apply(v: core.sum)          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ] = ???
    def apply(v: core.avg)          : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
    def apply(v: core.median)       : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ] = ???
    def apply(v: core.variance)     : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
    def apply(v: core.stddev)       : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
  }


  trait Aggregate16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {

    def apply(v: core.min)     : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: core.max)     : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: core.rand)    : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: core.sample)  : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???

    def apply(v: core.mins)    : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: core.maxs)    : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: core.distinct): Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: core.rands)   : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: core.samples) : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???

    def apply(v: core.count)        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int   ] = ???
    def apply(v: core.countDistinct): Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int   ] = ???
    def apply(v: core.sum)          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P     ] = ???
    def apply(v: core.avg)          : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
    def apply(v: core.median)       : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P     ] = ???
    def apply(v: core.variance)     : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
    def apply(v: core.stddev)       : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
  }


  trait Aggregate17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {

    def apply(v: core.min)     : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: core.max)     : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: core.rand)    : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: core.sample)  : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???

    def apply(v: core.mins)    : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: core.maxs)    : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: core.distinct): Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: core.rands)   : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: core.samples) : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???

    def apply(v: core.count)        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int   ] = ???
    def apply(v: core.countDistinct): Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int   ] = ???
    def apply(v: core.sum)          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q     ] = ???
    def apply(v: core.avg)          : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
    def apply(v: core.median)       : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q     ] = ???
    def apply(v: core.variance)     : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
    def apply(v: core.stddev)       : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
  }


  trait Aggregate18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {

    def apply(v: core.min)     : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: core.max)     : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: core.rand)    : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: core.sample)  : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???

    def apply(v: core.mins)    : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: core.maxs)    : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: core.distinct): Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: core.rands)   : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: core.samples) : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???

    def apply(v: core.count)        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int   ] = ???
    def apply(v: core.countDistinct): Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int   ] = ???
    def apply(v: core.sum)          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R     ] = ???
    def apply(v: core.avg)          : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
    def apply(v: core.median)       : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R     ] = ???
    def apply(v: core.variance)     : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
    def apply(v: core.stddev)       : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
  }


  trait Aggregate19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {

    def apply(v: core.min)     : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: core.max)     : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: core.rand)    : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: core.sample)  : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???

    def apply(v: core.mins)    : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: core.maxs)    : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: core.distinct): Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: core.rands)   : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: core.samples) : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???

    def apply(v: core.count)        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int   ] = ???
    def apply(v: core.countDistinct): Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int   ] = ???
    def apply(v: core.sum)          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S     ] = ???
    def apply(v: core.avg)          : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
    def apply(v: core.median)       : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S     ] = ???
    def apply(v: core.variance)     : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
    def apply(v: core.stddev)       : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
  }


  trait Aggregate20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {

    def apply(v: core.min)     : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: core.max)     : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: core.rand)    : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: core.sample)  : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???

    def apply(v: core.mins)    : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: core.maxs)    : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: core.distinct): Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: core.rands)   : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: core.samples) : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???

    def apply(v: core.count)        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int   ] = ???
    def apply(v: core.countDistinct): Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int   ] = ???
    def apply(v: core.sum)          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T     ] = ???
    def apply(v: core.avg)          : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
    def apply(v: core.median)       : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T     ] = ???
    def apply(v: core.variance)     : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
    def apply(v: core.stddev)       : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
  }


  trait Aggregate21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {

    def apply(v: core.min)     : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: core.max)     : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: core.rand)    : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: core.sample)  : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???

    def apply(v: core.mins)    : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: core.maxs)    : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: core.distinct): Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: core.rands)   : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: core.samples) : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???

    def apply(v: core.count)        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int   ] = ???
    def apply(v: core.countDistinct): Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int   ] = ???
    def apply(v: core.sum)          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ] = ???
    def apply(v: core.avg)          : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
    def apply(v: core.median)       : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ] = ???
    def apply(v: core.variance)     : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
    def apply(v: core.stddev)       : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
  }


  trait Aggregate22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {

    def apply(v: core.min)     : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: core.max)     : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: core.rand)    : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: core.sample)  : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???

    def apply(v: core.mins)    : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: core.maxs)    : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: core.distinct): Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: core.rands)   : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: core.samples) : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???

    def apply(v: core.count)        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int   ] = ???
    def apply(v: core.countDistinct): Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int   ] = ???
    def apply(v: core.sum)          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V     ] = ???
    def apply(v: core.avg)          : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
    def apply(v: core.median)       : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V     ] = ???
    def apply(v: core.variance)     : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
    def apply(v: core.stddev)       : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
  }
}
