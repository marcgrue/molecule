package molecule.core.expression

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
