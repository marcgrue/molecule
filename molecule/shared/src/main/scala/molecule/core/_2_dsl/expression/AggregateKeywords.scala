package molecule.core._2_dsl.expression

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
    *   Match.sKeywords.insert(1, 2, 4)
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
    *   Match.sKeywords.insert(1, 2, 4)
    *   Match.score(median).get.head === 2
    * }}}
    * OBS: When it comes to an even number of values, Datomic has a special implementation of median that is different from the one described on the
    * [[https://en.wikipedia.org/wiki/Median Wiki entry on the median function]].
    * <br><br>
    * Datomic calculates the median of even number of values as the average of the two middle numbers rounded down to nearest whole number
    * {{{
    *   Match.sKeywords.insert(1, 2, 3, 4)
    *   Match.score(median).get.head === 2 // (2 + 3) / 2 = 2.5 rounded down to 2
    * }}}
    * With decimal numbers this can go wrong:
    * {{{
    *   Match.sKeywords.insert(1.0, 2.5, 2.5, 3.0)
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
    *   Match.sKeywords.insert(1, 2, 4)
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
    *   Match.sKeywords.insert(1, 2, 4)
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
    *   Match.sKeywords.insert(1, 2, 4)
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

  import molecule.core._4_api.api.Keywords

  trait Aggregate0[obj[_], props, Ns0[o[_],_]]

  trait Aggregate1[obj[_], props, Ns1[o[_],_,_], A] {

    def apply(v: Keywords.min)     : Ns1[obj, props, A] = ???
    def apply(v: Keywords.max)     : Ns1[obj, props, A] = ???
    def apply(v: Keywords.rand)    : Ns1[obj, props, A] = ???
    def apply(v: Keywords.sample)  : Ns1[obj, props, A] = ???

    def apply(v: Keywords.mins)    : Ns1[obj, props, List[A]] = ???
    def apply(v: Keywords.maxs)    : Ns1[obj, props, List[A]] = ???
    def apply(v: Keywords.distinct): Ns1[obj, props, List[A]] = ???
    def apply(v: Keywords.rands)   : Ns1[obj, props, List[A]] = ???
    def apply(v: Keywords.samples) : Ns1[obj, props, List[A]] = ???

    def apply(v: Keywords.count)        : Ns1[obj, props, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns1[obj, props, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns1[obj, props, A     ] = ???
    def apply(v: Keywords.avg)          : Ns1[obj, props, Double] = ???
    def apply(v: Keywords.median)       : Ns1[obj, props, A     ] = ???
    def apply(v: Keywords.variance)     : Ns1[obj, props, Double] = ???
    def apply(v: Keywords.stddev)       : Ns1[obj, props, Double] = ???
  }


  trait Aggregate2[obj[_], props, Ns2[o[_],_,_,_], A, B] {

    def apply(v: Keywords.min)     : Ns2[obj, props, A, B] = ???
    def apply(v: Keywords.max)     : Ns2[obj, props, A, B] = ???
    def apply(v: Keywords.rand)    : Ns2[obj, props, A, B] = ???
    def apply(v: Keywords.sample)  : Ns2[obj, props, A, B] = ???

    def apply(v: Keywords.mins)    : Ns2[obj, props, A, List[B]] = ???
    def apply(v: Keywords.maxs)    : Ns2[obj, props, A, List[B]] = ???
    def apply(v: Keywords.distinct): Ns2[obj, props, A, List[B]] = ???
    def apply(v: Keywords.rands)   : Ns2[obj, props, A, List[B]] = ???
    def apply(v: Keywords.samples) : Ns2[obj, props, A, List[B]] = ???

    def apply(v: Keywords.count)        : Ns2[obj, props, A, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns2[obj, props, A, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns2[obj, props, A, B     ] = ???
    def apply(v: Keywords.avg)          : Ns2[obj, props, A, Double] = ???
    def apply(v: Keywords.median)       : Ns2[obj, props, A, B     ] = ???
    def apply(v: Keywords.variance)     : Ns2[obj, props, A, Double] = ???
    def apply(v: Keywords.stddev)       : Ns2[obj, props, A, Double] = ???
  }


  trait Aggregate3[obj[_], props, Ns3[o[_],_,_,_,_], A, B, C] {

    def apply(v: Keywords.min)     : Ns3[obj, props, A, B, C] = ???
    def apply(v: Keywords.max)     : Ns3[obj, props, A, B, C] = ???
    def apply(v: Keywords.rand)    : Ns3[obj, props, A, B, C] = ???
    def apply(v: Keywords.sample)  : Ns3[obj, props, A, B, C] = ???

    def apply(v: Keywords.mins)    : Ns3[obj, props, A, B, List[C]] = ???
    def apply(v: Keywords.maxs)    : Ns3[obj, props, A, B, List[C]] = ???
    def apply(v: Keywords.distinct): Ns3[obj, props, A, B, List[C]] = ???
    def apply(v: Keywords.rands)   : Ns3[obj, props, A, B, List[C]] = ???
    def apply(v: Keywords.samples) : Ns3[obj, props, A, B, List[C]] = ???

    def apply(v: Keywords.count)        : Ns3[obj, props, A, B, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns3[obj, props, A, B, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns3[obj, props, A, B, C     ] = ???
    def apply(v: Keywords.avg)          : Ns3[obj, props, A, B, Double] = ???
    def apply(v: Keywords.median)       : Ns3[obj, props, A, B, C     ] = ???
    def apply(v: Keywords.variance)     : Ns3[obj, props, A, B, Double] = ???
    def apply(v: Keywords.stddev)       : Ns3[obj, props, A, B, Double] = ???
  }


  trait Aggregate4[obj[_], props, Ns4[o[_],_,_,_,_,_], A, B, C, D] {

    def apply(v: Keywords.min)     : Ns4[obj, props, A, B, C, D] = ???
    def apply(v: Keywords.max)     : Ns4[obj, props, A, B, C, D] = ???
    def apply(v: Keywords.rand)    : Ns4[obj, props, A, B, C, D] = ???
    def apply(v: Keywords.sample)  : Ns4[obj, props, A, B, C, D] = ???

    def apply(v: Keywords.mins)    : Ns4[obj, props, A, B, C, List[D]] = ???
    def apply(v: Keywords.maxs)    : Ns4[obj, props, A, B, C, List[D]] = ???
    def apply(v: Keywords.distinct): Ns4[obj, props, A, B, C, List[D]] = ???
    def apply(v: Keywords.rands)   : Ns4[obj, props, A, B, C, List[D]] = ???
    def apply(v: Keywords.samples) : Ns4[obj, props, A, B, C, List[D]] = ???

    def apply(v: Keywords.count)        : Ns4[obj, props, A, B, C, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns4[obj, props, A, B, C, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns4[obj, props, A, B, C, D     ] = ???
    def apply(v: Keywords.avg)          : Ns4[obj, props, A, B, C, Double] = ???
    def apply(v: Keywords.median)       : Ns4[obj, props, A, B, C, D     ] = ???
    def apply(v: Keywords.variance)     : Ns4[obj, props, A, B, C, Double] = ???
    def apply(v: Keywords.stddev)       : Ns4[obj, props, A, B, C, Double] = ???
  }


  trait Aggregate5[obj[_], props, Ns5[o[_],_,_,_,_,_,_], A, B, C, D, E] {

    def apply(v: Keywords.min)     : Ns5[obj, props, A, B, C, D, E] = ???
    def apply(v: Keywords.max)     : Ns5[obj, props, A, B, C, D, E] = ???
    def apply(v: Keywords.rand)    : Ns5[obj, props, A, B, C, D, E] = ???
    def apply(v: Keywords.sample)  : Ns5[obj, props, A, B, C, D, E] = ???

    def apply(v: Keywords.mins)    : Ns5[obj, props, A, B, C, D, List[E]] = ???
    def apply(v: Keywords.maxs)    : Ns5[obj, props, A, B, C, D, List[E]] = ???
    def apply(v: Keywords.distinct): Ns5[obj, props, A, B, C, D, List[E]] = ???
    def apply(v: Keywords.rands)   : Ns5[obj, props, A, B, C, D, List[E]] = ???
    def apply(v: Keywords.samples) : Ns5[obj, props, A, B, C, D, List[E]] = ???

    def apply(v: Keywords.count)        : Ns5[obj, props, A, B, C, D, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns5[obj, props, A, B, C, D, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns5[obj, props, A, B, C, D, E     ] = ???
    def apply(v: Keywords.avg)          : Ns5[obj, props, A, B, C, D, Double] = ???
    def apply(v: Keywords.median)       : Ns5[obj, props, A, B, C, D, E     ] = ???
    def apply(v: Keywords.variance)     : Ns5[obj, props, A, B, C, D, Double] = ???
    def apply(v: Keywords.stddev)       : Ns5[obj, props, A, B, C, D, Double] = ???
  }


  trait Aggregate6[obj[_], props, Ns6[o[_],_,_,_,_,_,_,_], A, B, C, D, E, F] {

    def apply(v: Keywords.min)     : Ns6[obj, props, A, B, C, D, E, F] = ???
    def apply(v: Keywords.max)     : Ns6[obj, props, A, B, C, D, E, F] = ???
    def apply(v: Keywords.rand)    : Ns6[obj, props, A, B, C, D, E, F] = ???
    def apply(v: Keywords.sample)  : Ns6[obj, props, A, B, C, D, E, F] = ???

    def apply(v: Keywords.mins)    : Ns6[obj, props, A, B, C, D, E, List[F]] = ???
    def apply(v: Keywords.maxs)    : Ns6[obj, props, A, B, C, D, E, List[F]] = ???
    def apply(v: Keywords.distinct): Ns6[obj, props, A, B, C, D, E, List[F]] = ???
    def apply(v: Keywords.rands)   : Ns6[obj, props, A, B, C, D, E, List[F]] = ???
    def apply(v: Keywords.samples) : Ns6[obj, props, A, B, C, D, E, List[F]] = ???

    def apply(v: Keywords.count)        : Ns6[obj, props, A, B, C, D, E, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns6[obj, props, A, B, C, D, E, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns6[obj, props, A, B, C, D, E, F     ] = ???
    def apply(v: Keywords.avg)          : Ns6[obj, props, A, B, C, D, E, Double] = ???
    def apply(v: Keywords.median)       : Ns6[obj, props, A, B, C, D, E, F     ] = ???
    def apply(v: Keywords.variance)     : Ns6[obj, props, A, B, C, D, E, Double] = ???
    def apply(v: Keywords.stddev)       : Ns6[obj, props, A, B, C, D, E, Double] = ???
  }


  trait Aggregate7[obj[_], props, Ns7[o[_],_,_,_,_,_,_,_,_], A, B, C, D, E, F, G] {

    def apply(v: Keywords.min)     : Ns7[obj, props, A, B, C, D, E, F, G] = ???
    def apply(v: Keywords.max)     : Ns7[obj, props, A, B, C, D, E, F, G] = ???
    def apply(v: Keywords.rand)    : Ns7[obj, props, A, B, C, D, E, F, G] = ???
    def apply(v: Keywords.sample)  : Ns7[obj, props, A, B, C, D, E, F, G] = ???

    def apply(v: Keywords.mins)    : Ns7[obj, props, A, B, C, D, E, F, List[G]] = ???
    def apply(v: Keywords.maxs)    : Ns7[obj, props, A, B, C, D, E, F, List[G]] = ???
    def apply(v: Keywords.distinct): Ns7[obj, props, A, B, C, D, E, F, List[G]] = ???
    def apply(v: Keywords.rands)   : Ns7[obj, props, A, B, C, D, E, F, List[G]] = ???
    def apply(v: Keywords.samples) : Ns7[obj, props, A, B, C, D, E, F, List[G]] = ???

    def apply(v: Keywords.count)        : Ns7[obj, props, A, B, C, D, E, F, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns7[obj, props, A, B, C, D, E, F, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns7[obj, props, A, B, C, D, E, F, G     ] = ???
    def apply(v: Keywords.avg)          : Ns7[obj, props, A, B, C, D, E, F, Double] = ???
    def apply(v: Keywords.median)       : Ns7[obj, props, A, B, C, D, E, F, G     ] = ???
    def apply(v: Keywords.variance)     : Ns7[obj, props, A, B, C, D, E, F, Double] = ???
    def apply(v: Keywords.stddev)       : Ns7[obj, props, A, B, C, D, E, F, Double] = ???
  }


  trait Aggregate8[obj[_], props, Ns8[o[_],_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {

    def apply(v: Keywords.min)     : Ns8[obj, props, A, B, C, D, E, F, G, H] = ???
    def apply(v: Keywords.max)     : Ns8[obj, props, A, B, C, D, E, F, G, H] = ???
    def apply(v: Keywords.rand)    : Ns8[obj, props, A, B, C, D, E, F, G, H] = ???
    def apply(v: Keywords.sample)  : Ns8[obj, props, A, B, C, D, E, F, G, H] = ???

    def apply(v: Keywords.mins)    : Ns8[obj, props, A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: Keywords.maxs)    : Ns8[obj, props, A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: Keywords.distinct): Ns8[obj, props, A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: Keywords.rands)   : Ns8[obj, props, A, B, C, D, E, F, G, List[H]] = ???
    def apply(v: Keywords.samples) : Ns8[obj, props, A, B, C, D, E, F, G, List[H]] = ???

    def apply(v: Keywords.count)        : Ns8[obj, props, A, B, C, D, E, F, G, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns8[obj, props, A, B, C, D, E, F, G, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns8[obj, props, A, B, C, D, E, F, G, H     ] = ???
    def apply(v: Keywords.avg)          : Ns8[obj, props, A, B, C, D, E, F, G, Double] = ???
    def apply(v: Keywords.median)       : Ns8[obj, props, A, B, C, D, E, F, G, H     ] = ???
    def apply(v: Keywords.variance)     : Ns8[obj, props, A, B, C, D, E, F, G, Double] = ???
    def apply(v: Keywords.stddev)       : Ns8[obj, props, A, B, C, D, E, F, G, Double] = ???
  }


  trait Aggregate9[obj[_], props, Ns9[o[_],_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {

    def apply(v: Keywords.min)     : Ns9[obj, props, A, B, C, D, E, F, G, H, I] = ???
    def apply(v: Keywords.max)     : Ns9[obj, props, A, B, C, D, E, F, G, H, I] = ???
    def apply(v: Keywords.rand)    : Ns9[obj, props, A, B, C, D, E, F, G, H, I] = ???
    def apply(v: Keywords.sample)  : Ns9[obj, props, A, B, C, D, E, F, G, H, I] = ???

    def apply(v: Keywords.mins)    : Ns9[obj, props, A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: Keywords.maxs)    : Ns9[obj, props, A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: Keywords.distinct): Ns9[obj, props, A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: Keywords.rands)   : Ns9[obj, props, A, B, C, D, E, F, G, H, List[I]] = ???
    def apply(v: Keywords.samples) : Ns9[obj, props, A, B, C, D, E, F, G, H, List[I]] = ???

    def apply(v: Keywords.count)        : Ns9[obj, props, A, B, C, D, E, F, G, H, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns9[obj, props, A, B, C, D, E, F, G, H, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns9[obj, props, A, B, C, D, E, F, G, H, I     ] = ???
    def apply(v: Keywords.avg)          : Ns9[obj, props, A, B, C, D, E, F, G, H, Double] = ???
    def apply(v: Keywords.median)       : Ns9[obj, props, A, B, C, D, E, F, G, H, I     ] = ???
    def apply(v: Keywords.variance)     : Ns9[obj, props, A, B, C, D, E, F, G, H, Double] = ???
    def apply(v: Keywords.stddev)       : Ns9[obj, props, A, B, C, D, E, F, G, H, Double] = ???
  }


  trait Aggregate10[obj[_], props, Ns10[o[_],_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {

    def apply(v: Keywords.min)     : Ns10[obj, props, A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: Keywords.max)     : Ns10[obj, props, A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: Keywords.rand)    : Ns10[obj, props, A, B, C, D, E, F, G, H, I, J] = ???
    def apply(v: Keywords.sample)  : Ns10[obj, props, A, B, C, D, E, F, G, H, I, J] = ???

    def apply(v: Keywords.mins)    : Ns10[obj, props, A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: Keywords.maxs)    : Ns10[obj, props, A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: Keywords.distinct): Ns10[obj, props, A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: Keywords.rands)   : Ns10[obj, props, A, B, C, D, E, F, G, H, I, List[J]] = ???
    def apply(v: Keywords.samples) : Ns10[obj, props, A, B, C, D, E, F, G, H, I, List[J]] = ???

    def apply(v: Keywords.count)        : Ns10[obj, props, A, B, C, D, E, F, G, H, I, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns10[obj, props, A, B, C, D, E, F, G, H, I, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns10[obj, props, A, B, C, D, E, F, G, H, I, J     ] = ???
    def apply(v: Keywords.avg)          : Ns10[obj, props, A, B, C, D, E, F, G, H, I, Double] = ???
    def apply(v: Keywords.median)       : Ns10[obj, props, A, B, C, D, E, F, G, H, I, J     ] = ???
    def apply(v: Keywords.variance)     : Ns10[obj, props, A, B, C, D, E, F, G, H, I, Double] = ???
    def apply(v: Keywords.stddev)       : Ns10[obj, props, A, B, C, D, E, F, G, H, I, Double] = ???
  }


  trait Aggregate11[obj[_], props, Ns11[o[_],_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {

    def apply(v: Keywords.min)     : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: Keywords.max)     : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: Keywords.rand)    : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, K] = ???
    def apply(v: Keywords.sample)  : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, K] = ???

    def apply(v: Keywords.mins)    : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: Keywords.maxs)    : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: Keywords.distinct): Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: Keywords.rands)   : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, List[K]] = ???
    def apply(v: Keywords.samples) : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, List[K]] = ???

    def apply(v: Keywords.count)        : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, K     ] = ???
    def apply(v: Keywords.avg)          : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, Double] = ???
    def apply(v: Keywords.median)       : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, K     ] = ???
    def apply(v: Keywords.variance)     : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, Double] = ???
    def apply(v: Keywords.stddev)       : Ns11[obj, props, A, B, C, D, E, F, G, H, I, J, Double] = ???
  }


  trait Aggregate12[obj[_], props, Ns12[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {

    def apply(v: Keywords.min)     : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: Keywords.max)     : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: Keywords.rand)    : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L] = ???
    def apply(v: Keywords.sample)  : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L] = ???

    def apply(v: Keywords.mins)    : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: Keywords.maxs)    : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: Keywords.distinct): Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: Keywords.rands)   : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???
    def apply(v: Keywords.samples) : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, List[L]] = ???

    def apply(v: Keywords.count)        : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L     ] = ???
    def apply(v: Keywords.avg)          : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, Double] = ???
    def apply(v: Keywords.median)       : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L     ] = ???
    def apply(v: Keywords.variance)     : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, Double] = ???
    def apply(v: Keywords.stddev)       : Ns12[obj, props, A, B, C, D, E, F, G, H, I, J, K, Double] = ???
  }


  trait Aggregate13[obj[_], props, Ns13[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {

    def apply(v: Keywords.min)     : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: Keywords.max)     : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: Keywords.rand)    : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M] = ???
    def apply(v: Keywords.sample)  : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M] = ???

    def apply(v: Keywords.mins)    : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: Keywords.maxs)    : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: Keywords.distinct): Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: Keywords.rands)   : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???
    def apply(v: Keywords.samples) : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, List[M]] = ???

    def apply(v: Keywords.count)        : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M     ] = ???
    def apply(v: Keywords.avg)          : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
    def apply(v: Keywords.median)       : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M     ] = ???
    def apply(v: Keywords.variance)     : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
    def apply(v: Keywords.stddev)       : Ns13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, Double] = ???
  }


  trait Aggregate14[obj[_], props, Ns14[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {

    def apply(v: Keywords.min)     : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: Keywords.max)     : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: Keywords.rand)    : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???
    def apply(v: Keywords.sample)  : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = ???

    def apply(v: Keywords.mins)    : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: Keywords.maxs)    : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: Keywords.distinct): Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: Keywords.rands)   : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???
    def apply(v: Keywords.samples) : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, List[N]] = ???

    def apply(v: Keywords.count)        : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N     ] = ???
    def apply(v: Keywords.avg)          : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
    def apply(v: Keywords.median)       : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N     ] = ???
    def apply(v: Keywords.variance)     : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
    def apply(v: Keywords.stddev)       : Ns14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, Double] = ???
  }


  trait Aggregate15[obj[_], props, Ns15[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {

    def apply(v: Keywords.min)     : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: Keywords.max)     : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: Keywords.rand)    : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???
    def apply(v: Keywords.sample)  : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = ???

    def apply(v: Keywords.mins)    : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: Keywords.maxs)    : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: Keywords.distinct): Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: Keywords.rands)   : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???
    def apply(v: Keywords.samples) : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, List[O]] = ???

    def apply(v: Keywords.count)        : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ] = ???
    def apply(v: Keywords.avg)          : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
    def apply(v: Keywords.median)       : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O     ] = ???
    def apply(v: Keywords.variance)     : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
    def apply(v: Keywords.stddev)       : Ns15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Double] = ???
  }


  trait Aggregate16[obj[_], props, Ns16[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {

    def apply(v: Keywords.min)     : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: Keywords.max)     : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: Keywords.rand)    : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???
    def apply(v: Keywords.sample)  : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = ???

    def apply(v: Keywords.mins)    : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: Keywords.maxs)    : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: Keywords.distinct): Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: Keywords.rands)   : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???
    def apply(v: Keywords.samples) : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, List[P]] = ???

    def apply(v: Keywords.count)        : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P     ] = ???
    def apply(v: Keywords.avg)          : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
    def apply(v: Keywords.median)       : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P     ] = ???
    def apply(v: Keywords.variance)     : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
    def apply(v: Keywords.stddev)       : Ns16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Double] = ???
  }


  trait Aggregate17[obj[_], props, Ns17[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {

    def apply(v: Keywords.min)     : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: Keywords.max)     : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: Keywords.rand)    : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???
    def apply(v: Keywords.sample)  : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = ???

    def apply(v: Keywords.mins)    : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: Keywords.maxs)    : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: Keywords.distinct): Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: Keywords.rands)   : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???
    def apply(v: Keywords.samples) : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, List[Q]] = ???

    def apply(v: Keywords.count)        : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q     ] = ???
    def apply(v: Keywords.avg)          : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
    def apply(v: Keywords.median)       : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q     ] = ???
    def apply(v: Keywords.variance)     : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
    def apply(v: Keywords.stddev)       : Ns17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Double] = ???
  }


  trait Aggregate18[obj[_], props, Ns18[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {

    def apply(v: Keywords.min)     : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: Keywords.max)     : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: Keywords.rand)    : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???
    def apply(v: Keywords.sample)  : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = ???

    def apply(v: Keywords.mins)    : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: Keywords.maxs)    : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: Keywords.distinct): Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: Keywords.rands)   : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???
    def apply(v: Keywords.samples) : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, List[R]] = ???

    def apply(v: Keywords.count)        : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R     ] = ???
    def apply(v: Keywords.avg)          : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
    def apply(v: Keywords.median)       : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R     ] = ???
    def apply(v: Keywords.variance)     : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
    def apply(v: Keywords.stddev)       : Ns18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Double] = ???
  }


  trait Aggregate19[obj[_], props, Ns19[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {

    def apply(v: Keywords.min)     : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: Keywords.max)     : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: Keywords.rand)    : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???
    def apply(v: Keywords.sample)  : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = ???

    def apply(v: Keywords.mins)    : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: Keywords.maxs)    : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: Keywords.distinct): Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: Keywords.rands)   : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???
    def apply(v: Keywords.samples) : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, List[S]] = ???

    def apply(v: Keywords.count)        : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S     ] = ???
    def apply(v: Keywords.avg)          : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
    def apply(v: Keywords.median)       : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S     ] = ???
    def apply(v: Keywords.variance)     : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
    def apply(v: Keywords.stddev)       : Ns19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Double] = ???
  }


  trait Aggregate20[obj[_], props, Ns20[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {

    def apply(v: Keywords.min)     : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: Keywords.max)     : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: Keywords.rand)    : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???
    def apply(v: Keywords.sample)  : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = ???

    def apply(v: Keywords.mins)    : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: Keywords.maxs)    : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: Keywords.distinct): Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: Keywords.rands)   : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???
    def apply(v: Keywords.samples) : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, List[T]] = ???

    def apply(v: Keywords.count)        : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T     ] = ???
    def apply(v: Keywords.avg)          : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
    def apply(v: Keywords.median)       : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T     ] = ???
    def apply(v: Keywords.variance)     : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
    def apply(v: Keywords.stddev)       : Ns20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Double] = ???
  }


  trait Aggregate21[obj[_], props, Ns21[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {

    def apply(v: Keywords.min)     : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: Keywords.max)     : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: Keywords.rand)    : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???
    def apply(v: Keywords.sample)  : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = ???

    def apply(v: Keywords.mins)    : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: Keywords.maxs)    : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: Keywords.distinct): Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: Keywords.rands)   : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???
    def apply(v: Keywords.samples) : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, List[U]] = ???

    def apply(v: Keywords.count)        : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ] = ???
    def apply(v: Keywords.avg)          : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
    def apply(v: Keywords.median)       : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ] = ???
    def apply(v: Keywords.variance)     : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
    def apply(v: Keywords.stddev)       : Ns21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Double] = ???
  }


  trait Aggregate22[obj[_], props, Ns22[o[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {

    def apply(v: Keywords.min)     : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: Keywords.max)     : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: Keywords.rand)    : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???
    def apply(v: Keywords.sample)  : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = ???

    def apply(v: Keywords.mins)    : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: Keywords.maxs)    : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: Keywords.distinct): Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: Keywords.rands)   : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???
    def apply(v: Keywords.samples) : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[U]] = ???

    def apply(v: Keywords.count)        : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int   ] = ???
    def apply(v: Keywords.countDistinct): Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int   ] = ???
    def apply(v: Keywords.sum)          : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V     ] = ???
    def apply(v: Keywords.avg)          : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
    def apply(v: Keywords.median)       : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V     ] = ???
    def apply(v: Keywords.variance)     : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
    def apply(v: Keywords.stddev)       : Ns22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double] = ???
  }
}
