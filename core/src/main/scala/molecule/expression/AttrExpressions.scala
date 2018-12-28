package molecule.expression
import molecule.ast.model._
import molecule.boilerplate.attributes.Attr


/** Attribute expression markers and methods.
  * {{{
  *   Person.age(42)                           // equality
  *   Person.name.contains("John")             // fulltext search
  *   Person.age.!=(42)                        // negation (or `not`)
  *   Person.age.<(42)                         // comparison (< > <= >=)
  *   Person.age().get                         // match non-asserted datoms (null) in query
  *   Person(benId).age().update               // apply empty value to retract value(s) in updates
  *   Person.hobbies.assert("golf")            // assert card-many value(s)
  *   Person.hobbies.replace("golf", "diving") // replace card-many attribute value(s)
  *   Person.hobbies.retract("golf")           // retract card-many attribute value(s)
  *   Person.tags.k("en")                      // match values of map attributes by key
  *   Person.name(?)                           // initiate input molecules awaiting input at runtime
  *   Person.name(unify)                       // Unify attributes in self-joins
  * }}}
  * @see Manual: [[http://www.scalamolecule.org/manual/attributes/expressions/ expressions]],
  *      [[http://www.scalamolecule.org/manual/attributes/aggregates/ aggregates]],
  *      [[http://www.scalamolecule.org/manual/attributes/parameterized/ input molecules]],
  *      [[http://www.scalamolecule.org/manual/relationships/self-join/ self-join]]
  *     | Tests: [[https://github.com/scalamolecule/molecule/tree/master/coretests/src/test/scala/molecule/coretests/expression expressions]],
  *     [[https://github.com/scalamolecule/molecule/tree/master/coretests/src/test/scala/molecule/coretests/input/Input1String.scala#L1 input1]],
  *     [[https://github.com/scalamolecule/molecule/tree/master/coretests/src/test/scala/molecule/coretests/input/Input2.scala#L1 input2]],
  *     [[https://github.com/scalamolecule/molecule/tree/master/coretests/src/test/scala/molecule/coretests/input/Input3.scala#L1 input3]]
  * @groupname attrMarker Attribute markers
  * @groupdesc attrMarker Markers applied to attributes that change the semantics of the attribute/molecule.
  * @groupprio attrMarker 20
  * @groupname attrExprImplicits Expression implicits
  * @groupdesc attrExprImplicits Turns basic types into `TermValue`'s that can be used in [[molecule.ast.model.Expression Expression]]
  * @groupprio attrExprImplicits 21
  */
trait AttrExpressions {

  /** Turn molecule into input molecule awaiting input.
    * <br><br>
    * Apply input marker `?` to attribute to turn molecule into an 'input molecule'.
    * <br><br>
    * At runtime the input molecule expects input for the attribute in place of the `?` marker.
    * {{{
    *   // Input molecule created at compile time.
    *   val ageOfPersons = m(Person.name_(?).age) // awaiting name of type String
    *
    *   // At runtime, "Ben" is applied as input replacing the `?` placeholder and we can get the age.
    *   ageOfPersons("Ben").get === List(42)
    * }}}
    *
    * @note Data can only be retrieved from input molecules once they have been resolved with input.<br>
    *       Input molecule queries are cached and optimized by Datomic.
    * @group attrMarker
    **/
  trait ?

  // Avoiding overload via core.? import
  type ?? = molecule.expression.AttrExpressions.?

  /** Unify attribute value in self-join.
    * <br><br>
    * Apply `unify` marker to attribute to unify its value with previous values of the same attribute in the molecule in a self-join.
    * {{{
    *   m(Person.age.name.Beverages * Beverage.name.rating) insert List(
    *       (23, "Joe", List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
    *       (25, "Ben", List(("Coffee", 2), ("Tea", 3))),
    *       (23, "Liz", List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))
    *
    *   // What beverages do pairs of 23- AND 25-year-olds like in common?
    *   // Drink name is unified - Joe and Ben both drink coffee, etc..
    *   Person.age_(23).name.Beverages.name._Ns.Self
    *         .age_(25).name.Beverages.name_(unify).get.sorted === List(
    *     ("Joe", "Coffee", "Ben"),
    *     ("Liz", "Coffee", "Ben"),
    *     ("Liz", "Tea", "Ben")
    *   )
    * }}}
    *
    * @group attrMarker
    **/
  trait unify


  /** Expression methods common for all attributes. */
  trait AttrExpr[Ns, T] {

    /** Apply empty value to retract datom in an update.
      * {{{
      *   val benId = Person.name("Ben").age(42).save.eid
      *   Person.name.age$ === List(("Ben", Some(42)))
      *
      *   // Retract Ben's age
      *   Person(benId).age().update
      *   Person.name.age$ === List(("Ben", None))
      * }}}
      * For cardinality-many attributes, ''all'' values of the attribute are retracted.
      */
    def apply(): Ns with Attr = ???

    /** Match one or more attribute values.
      * <br><br>
      * Applying value(s) to an attribute has different semantics depending on what operation is performed:
      * {{{
      *   // Querying with `get` - Ben is 42
      *   Person.name_("Ben").age.get === List(42)
      *
      *   // OR-semantics when multiple values are queried
      *   Person.name_("Ben", "Liz").age.get === List(42, 37)
      *
      *   // Saving new value (any old value is retracted)
      *   Person.name("Joe").save
      *
      *   // Saving multiple new card-many attribute values (all old values are retracted).
      *   // (Saving multiple new values not allowed for card-one attributes)
      *   Person.hobbies("golf", "diving").save
      *
      *   // Replacing value when updating (old value is retracted).
      *   Person(benId).age(43).update
      *
      *   // Replacing multiple values for card-many attributes (all old values are retracted).
      *   // (Replacing multiple values not allowed for card-one attributes)
      *   Person(benId).hobbies("reading", "walking").update
      * }}}
      *
      * @param value      Attribute values to be matched
      * @param moreValues Optional additional attribute values to be matched
      * @return Filtered molecule
      */
    def  apply(value: T, moreValues: T*): Ns with Attr = ???

    /** Match one or more Iterables of attribute values.
      * <br><br>
      * Multiple Iterables are concatenated into one Iterable of values to be matched.
      * <br><br>
      * Applying value(s) to an attribute has different semantics depending on what operation is performed:
      * {{{
      *   // Querying with `get` - Ben is 42
      *   Person.name_(Set("Ben")).age.get === List(42)
      *
      *   val members = List("Ben", "Liz")
      *   val associates = List("Don", "Ann")
      *
      *   // OR-semantics when multiple values are queried
      *   Person.name_(members).age.get === List(42, 37)
      *   // Multiple Iterables concatenated
      *   Person.name_(members, associates).age.get === List(42, 37, 71, 28)
      *
      *   // Single value in Iterable can be added when saving
      *   // (although easier to apply the value directly)
      *   Person.name(List("Joe")).save
      *
      *   // Saving multiple new card-many attribute values (all old values are retracted).
      *   // (Saving multiple new values not allowed for card-one attributes)
      *   val sports = Set("golf", "diving")
      *   Person.hobbies(sports).save
      *
      *   // Replacing value when updating (old value is retracted).
      *   Person(benId).age(List(43)).update
      *
      *   // Replacing multiple values for card-many attributes (all old values are retracted).
      *   // (Replacing multiple values not allowed for card-one attributes)
      *   Person(benId).hobbies(Seq("reading", "walking")).update
      *
      *   // Multiple Iterables can be applied
      *   Person(benId).hobbies(Seq("reading", "walking"), Set("stamps")).update
      * }}}
      *
      * @param values     Iterable of attribute values to be matched
      * @param moreValues Optional additional Iterables of attribute values to be matched
      * @return Filtered molecule
      */
    def apply(values: Seq[T], moreValues: Seq[T]*): Ns with Attr = ???


    /** Mark tacit attribute to be unified in self-join.
      * <br><br>
      * Attributes before '''`Self`''' are joined with attributes added after '''`Self`''' by values that can unify:
      * <br><br>
      * Find 23-year olds liking the same beverage as 25-year olds (unifying by beverage):
      * {{{
      *   Person.name.age(23).Drinks.beverage._Person.Self // create self join
      *         .name.age(25).Drinks.beverage_(unify)      // unify by beverage
      *         .get === List(
      *           ("Joe", 23, "Coffee", "Ben", 25),  // Joe (23) and Ben(25) both like coffee
      *           ("Liz", 23, "Coffee", "Ben", 25),  // Liz (23) and Ben(25) both like coffee
      *           ("Liz", 23, "Tea", "Ben", 25)      // Liz (23) and Ben(25) both like tea
      *         )
      * }}}
      * `unify` marker can only be applied to tacit attribute (with underscore).
      *
      * @param unifyer `unify` marker to unify self-join by this attribute values
      * @return Self-join molecule
      */
    def apply(unifyer: unify): Ns with Attr = ???


    /** Match attribute values different from one or more applied values.
      * {{{
      *   Person.name.get === List("Ben", "Liz", "Joe")
      *
      *   // Negate one value
      *   Person.name.not("Ben").get === List("Liz", "Joe")
      *
      *   // Negate multiple values
      *   Person.name.not("Ben", "Liz").get === List("Joe")
      *
      *   // same as
      *   Person.name.!=("Ben", "Liz").get === List("Joe")
      * }}}
      *
      * @param value      Negated attribute value
      * @param moreValues Optional additional negated attribute values
      * @return Filtered molecule
      */
    def not(value: T, moreValues: T*): Ns with Attr = ???


    /** Match attribute values different from applied Iterable of values.
      * {{{
      *   Person.name.get === List("Ben", "Liz", "Joe")
      *
      *   // Negate Iterable of values
      *   Person.name.not(List("Ben", "Joe")).get === List("Liz")
      *
      *   // same as
      *   Person.name.!=(List("Ben", "Joe")).get === List("Liz")
      * }}}
      *
      * @param values Iterable of negated attribute values
      * @return Filtered molecule
      */
    def not(values: Seq[T]): Ns with Attr = ???


    /** Match attribute values different from one or more applied values.
      * {{{
      *   Person.name.get === List("Ben", "Liz", "Joe")
      *
      *   // Negate one value
      *   Person.name.!=("Ben").get === List("Liz", "Joe")
      *
      *   // Negate multiple values
      *   Person.name.!=("Ben", "Liz").get === List("Joe")
      *
      *   // same as
      *   Person.name.not("Ben", "Liz").get === List("Joe")
      * }}}
      *
      * @param value      Negated attribute value
      * @param moreValues Optional additional negated attribute values
      * @return Filtered molecule
      */
    def !=(value: T, value2: T, moreValues: T*): Ns with Attr = ???

    /** Match attribute values different from applied value.
      * {{{
      *   Person.name.get === List("Ben", "Liz", "Joe")
      *
      *   // Negate value
      *   Person.name.!=("Ben").get === List("Liz", "Joe")
      *
      *   // same as
      *   Person.name.not("Ben").get === List("Liz", "Joe")
      * }}}
      *
      * @param value Negated attribute value
      * @return Filtered molecule
      */
    def !=(value: T): Ns with Attr = ??? // Hack to satisfy intellij inference


    /** Match attribute values different from applied Iterable of values.
      * {{{
      *   Person.name.get === List("Ben", "Liz", "Joe")
      *
      *   // Negate Iterable of values
      *   Person.name.!=(List("Ben", "Joe")).get === List("Liz")
      *
      *   // same as
      *   Person.name.not(List("Ben", "Joe")).get === List("Liz")
      * }}}
      *
      * @param values Iterable of negated attribute values
      * @return Filtered molecule
      */
    def !=(values: Seq[T]): Ns with Attr = ???
  }


  /** Value update methods for card-many attributes. */
  trait ManyAttrExpr[Ns, Add, OldNew, Rem] {

    /** Assert one or more card-many attribute values.
      * {{{
      *   Person.hobbies.get === List(Set("golf", "diving"))
      *
      *   // Assert/add value
      *   Person(benId).hobbies.assert("stamps").update
      *
      *   // Assert multiple values
      *   Person(benId).hobbies.assert("walking", "theater").update
      *
      *   Person.hobbies.get === List(Set("golf", "diving", "stamps", "walking", "theater"))
      * }}}
      *
      * @param value      New attribute value
      * @param moreValues Optional additional new attribute values
      * @return Molecule to be updated
      */
    def assert(value: Add, moreValues: Add*): Ns with Attr = ???


    /** Assert Iterable of card-many attribute values.
      * {{{
      *   Person.hobbies.get === List(Set("golf", "diving"))
      *
      *   // Assert/add values of Iterable
      *   Person(benId).hobbies.assert(Seq("stamps", "walking", "theater")).update
      *
      *   Person.hobbies.get === List(Set("golf", "diving", "stamps", "walking", "theater"))
      * }}}
      *
      * @param values Iterable of attribute values
      * @return Molecule to be updated
      */
    def assert(values: Iterable[Add]): Ns with Attr = ???


    /** Replace one or more card-many attribute values.
      * <br><br>
      * Retracts old value and asserts new value.
      * {{{
      *   Person.hobbies.get === List(Set("golf", "diving"))
      *
      *   // Replace value by applying old/new value pair
      *   Person(benId).hobbies.replace("golf" -> "theater").update
      *
      *   // Replace multiple values by applying multiple old/new value pairs
      *   Person(benId).hobbies.replace("theater" -> "concerts", "diving" -> "football").update
      *
      *   Person.hobbies.get === List(Set("concerts", "football"))
      * }}}
      *
      * @param oldNew  Pair of old/new value
      * @param oldNews Optional additional pairs of old/new value
      * @return Molecule to be updated
      */
    def replace(oldNew: OldNew, oldNews: OldNew*): Ns with Attr = ???


    /** Replace Iterable of card-many attribute values.
      * <br><br>
      * Retracts old value and asserts new value.
      * {{{
      *   Person.hobbies.get === List(Set("golf", "diving"))
      *
      *   // Replace values by applying Iterable of old/new value pairs
      *   Person(benId).hobbies.replace("theater" -> "concerts", "diving" -> "football").update
      *
      *   Person.hobbies.get === List(Set("concerts", "football"))
      * }}}
      *
      * @param oldNews Iterable of old/new attribute values. For map attributes it's key/value pairs.
      * @return Molecule to be updated
      */
    def replace(oldNews: Iterable[OldNew]): Ns with Attr = ???


    /** Retract one or more card-many attribute values.
      * {{{
      *   Person.hobbies.get === List(Set("golf", "diving", "stamps", "walking", "theater"))
      *
      *   // Retract value
      *   Person(benId).hobbies.retract("theater").update
      *
      *   // Retract multiple values
      *   Person(benId).hobbies.retract("stamps", "walking").update
      *
      *   Person.hobbies.get === List(Set("golf", "diving"))
      * }}}
      *
      * @param value      Attribute value to be retracted
      * @param moreValues Optional additional attribute values to be retracted
      * @return Molecule to be updated
      */
    def retract(value: Rem, moreValues: Rem*): Ns with Attr = ???


    /** Retract Iterable of card-many attribute values.
      * {{{
      *   Person.hobbies.get === List(Set("golf", "diving", "stamps", "walking", "theater"))
      *
      *   // Retract multiple values
      *   Person(benId).hobbies.retract(List("walking", "theater")).update
      *
      *   Person.hobbies.get === List(Set("golf", "diving", "stamps"))
      * }}}
      *
      * @param values Iterable of attribute values to be retracted
      * @return Molecule to be updated
      */
    def retract(values: Iterable[Rem]): Ns with Attr = ???
  }

  /** Expression methods of card-one ref attributes. */
  trait OneRefAttrExpr[Ns, In] extends AttrExpr[Ns, Long]

  /** Expression methods of card-many ref attributes. */
  trait ManyRefAttrExpr[Ns, In] extends AttrExpr[Ns, Long] with ManyAttrExpr[Ns, Long, (Long, Long), Long]

  /** Expression methods of value attributes. */
  trait ValueAttrExpr[Ns, In, T] extends AttrExpr[Ns, T] {

    /** Match attribute values less than upper value.
      * {{{
      *   Person.age.get === List(5, 12, 28)
      *   Person.age.<(12).get === List(5)
      * }}}
      *
      * @param upper Upper value
      * @return Molecule
      */
    def <(upper: T): Ns with Attr = ???


    /** Match attribute values less than or equal to upper value.
      * {{{
      *   Person.age.get === List(5, 12, 28)
      *   Person.age.<=(12).get === List(5, 12)
      * }}}
      *
      * @param upper Upper value
      * @return Molecule
      */
    def <=(upper: T): Ns with Attr = ???


    /** Match attribute values bigger than lower value
      * {{{
      *   Person.age.get === List(5, 12, 28)
      *   Person.age.>(12).get === List(28)
      * }}}
      *
      * @param lower Lower value
      * @return Molecule
      */
    def >(lower: T): Ns with Attr = ???


    /** Match attribute values bigger than or equal to lower value.
      * {{{
      *   Person.age.get === List(5, 12, 28)
      *   Person.age.>=(12).get === List(12, 28)
      * }}}
      *
      * @param lower Lower value
      * @return Molecule
      */
    def >=(lower: T): Ns with Attr = ???

    // Input

    /** Mark molecule as input molecule awaiting attribute value(s) to be matched.
      * {{{
      *   Person.name.age.get === List(("Ben", 42), ("Liz", 37))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val ageOfPerson = m(Person.name_(?).age)
      *
      *   // Apply `name` attribute value at runtime to get age
      *   ageOfPerson("Ben").get === List(42)
      * }}}
      *
      * @param value Input marker `?` for equality match
      * @return Input molecule
      */
//    def apply(value: molecule.api.core.?): In with Attr = ???
    def apply(value: ??): In with Attr = ???


    /** Mark molecule as input molecule awaiting attribute negation value(s).
      * {{{
      *   Person.name.age.get === List(("Ben", 42), ("Liz", 37))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val ageOfPersonsOtherThan = m(Person.name_.not(?).age)
      *
      *   // Apply `name` attribute value at runtime to get ages of all other than Ben
      *   ageOfPersonsOtherThan("Ben").get === List(37) // Liz' age
      * }}}
      *
      * @param value Input marker `?` for negation value
      * @return Input molecule
      */
    def not(value: ??): In with Attr = ???


    /** Mark molecule as input molecule awaiting attribute negation value(s).
      * {{{
      *   Person.name.age.get === List(("Ben", 42), ("Liz", 37))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val ageOfPersonsOtherThan = m(Person.name_.!=(?).age)
      *
      *   // Apply `name` attribute value at runtime to get ages of all other than Ben
      *   ageOfPersonsOtherThan("Ben").get === List(37) // Liz' age
      * }}}
      *
      * @param value Input marker `?` for negation value
      * @return Input molecule
      */
    def !=(value: ??): In with Attr = ???


    /** Mark molecule as input molecule awaiting attribute upper value.
      * {{{
      *   Person.name.age.get === List(("Liz", 37), ("Ben", 42), ("Don", 71))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val personsUnder = m(Person.name.age_.<(?))
      *
      *   // Apply upper value at runtime to get names of all under 42
      *   personsUnder(42).get === List("Liz")
      * }}}
      *
      * @param upper Input marker `?` for upper value
      * @return Input molecule
      */
    def <(upper: ??): In with Attr = ???


    /** Mark molecule as input molecule awaiting attribute upper value.
      * {{{
      *   Person.name.age.get === List(("Liz", 37), ("Ben", 42), ("Don", 71))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val personsUnderOrExactly = m(Person.name.age_.<=(?))
      *
      *   // Apply upper value at runtime to get names of all under or exactly 42
      *   personsUnderOrExactly(42).get === List("Liz", "Ben")
      * }}}
      *
      * @param upper Input marker `?` for upper value
      * @return Input molecule
      */
    def <=(upper: ??): In with Attr = ???


    /** Mark molecule as input molecule awaiting attribute lower value.
      * {{{
      *   Person.name.age.get === List(("Liz", 37), ("Ben", 42), ("Don", 71))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val personsOver = m(Person.name.age_.>(?))
      *
      *   // Apply lower value at runtime to get names of all over 42
      *   personsOver(42).get === List("Don")
      * }}}
      *
      * @param lower Input marker `?` for lower value
      * @return Input molecule
      */
    def >(lower: ??): In with Attr = ???


    /** Mark molecule as input molecule awaiting attribute lower value.
      * {{{
      *   Person.name.age.get === List(("Liz", 37), ("Ben", 42), ("Don", 71))
      *
      *   // Create input molecule at compile time by applying `?` marker to attribute
      *   val personsOverOrExactly = m(Person.name.age_.>=(?))
      *
      *   // Apply lower value at runtime to get names of all over or exactly 42
      *   personsOverOrExactly(42).get === List("Ben", "Don")
      * }}}
      *
      * @param lower Input marker `?` for lower value
      * @return Input molecule
      */
    def >=(lower: ??): In with Attr = ???


    /** Filter attribute values with logical expression.
      * {{{
      *   Person.name.age.get === List(
      *     ("Liz", 37),
      *     ("Ben", 42),
      *     ("Don", 71)
      *   )
      *
      *   // Apply OR expression
      *   // Match all entities with `name` attribute having value "Liz" or "Ben"
      *   Person.name_("Liz" or "Ben").age.get === List(37, 42)
      * }}}
      *
      * @param expr1 OR expression
      * @return Molecule
      */
    def apply(expr1: Exp1[T]): Ns with Attr = ???


    /** Filter attribute values with logical expression.
      * {{{
      *   Person.name.age.hobbies.get === List(
      *     ("Joe", 42, Set("golf", "reading")),
      *     ("Ben", 42, Set("golf", "diving", "reading")),
      *     ("Liz", 37, Set("golf", "diving"))
      *   )
      *
      *   // Apply AND expression for card-many attributes
      *   Person.name.hobbies_("golf" and "diving").get === List("Ben", "Liz")
      *
      *   // Given an input molecule awaiting 2 inputs, we can apply AND-pairs to OR expression:
      *   val persons = m(Person.name_(?).age(?))
      *   persons(("Ben" and 42) or ("Liz" and 37)).get === List(42, 37)
      * }}}
      *
      * @param expr2 OR/AND expression
      * @return Molecule
      */
    def apply(expr2: Exp2[T, T]): Ns with Attr = ???


    /** Expression AST for building OR/AND expressions.
      * {{{
      *   Person.name.age.hobbies.get === List(
      *     ("Liz", 37, Set("golf", "diving")),
      *     ("Ben", 42, Set("golf", "diving", "reading")),
      *     ("Joe", 42, Set("golf", "reading"))
      *   )
      *
      *   // Apply two AND expression for card-many attributes
      *   Person.name.hobbies_("golf" and "diving" and "reading").get === List("Ben")
      * }}}
      * With input molecules we can apply logic to multiple attributes at once.
      * {{{
      *   Person.name.age.noOfCars.noOfKids.get === List(
      *     ("Joe", 42, 1, 2),
      *     ("Ben", 42, 1, 1),
      *     ("Liz", 37, 2, 3)
      *   )
      *   // Apply AND-triples to OR expression:
      *   val persons = m(Person.name.age_(?).noOfCars(?).noOfKids_(?))
      *   persons((42 and 1 and 1) or (37 and 2 and 3)).get === List("Ben", "Liz")
      * }}}
      *
      * @param expr3 OR/AND expression
      * @return Molecule
      */
    def apply(expr3: Exp3[T, T, T]): Ns with Attr = ???
  }


  /** Expression methods of card-one attributes. */
  trait OneExpr[Ns, In, T] extends ValueAttrExpr[Ns, In, T]


  /** Expression methods of card-many attributes. */
  trait ManyExpr[Ns, In, T] extends ValueAttrExpr[Ns, In, T] with ManyAttrExpr[Ns, T, (T, T), T] {

    def apply(set: Set[T], moreSets: Set[T]*): Ns with Attr = ???

    def not(set: Set[T]): Ns with Attr = ???
    def not(set: Set[T], set2: Set[T], moreSets: Set[T]*): Ns with Attr = ???
    def not(sets: Seq[Set[T]])             : Ns with Attr = ???

    def !=(set: Set[T]): Ns with Attr = ???
    def !=(set: Set[T], set2: Set[T], moreSets: Set[T]*): Ns with Attr = ???
    def !=(sets: Seq[Set[T]])             : Ns with Attr = ???
  }


  /** Expression methods of map attributes. */
  trait MapAttrExpr[Ns, In, T] extends ValueAttrExpr[Ns, In, T] with ManyAttrExpr[Ns, (String, T), (String, T), String] {

    /** Match map attribute key/value pair(s).
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply key/value pair
      *   Greeting.id.strMap_("da" -> "Hej").get === List(3, 4)
      *
      *   // Apply multiple key/value pairs (OR semantics)
      *   Greeting.id.strMap_("da" -> "Hej", "en" -> "Hi there").get === List(1, 3, 4)
      * }}}
      *
      * @param keyValue      Key/value pair
      * @param moreKeyValues Optional further key/value pairs
      * @return Filtered molecule
      */
    def apply(keyValue: (String, T), moreKeyValues: (String, T)*): Ns with Attr = ???


    /** Match Iterable of map attribute key/value pair(s).
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply Iterable of key/value pairs (OR semantics)
      *   Greeting.id.strMap_(List("da" -> "Hej", "en" -> "Hi there")).get === List(1, 3, 4)
      * }}}
      *
      * @param keyValues Iterable of key/value pairs
      * @return Filtered molecule
      */
    def apply(keyValues: Iterable[(String, T)]): Ns with Attr = ???


    /** Match OR expression of map attribute key/value pairs.
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply OR expression(s) of key/value pairs
      *   Greeting.id.strMap_("en" -> "Hi there" or "fr" -> "Bonjour").get === List(1, 2)
      *   Greeting.id.strMap_("en" -> "Hi there" or "fr" -> "Bonjour" or "en" -> "Hello").get === List(1, 2, 3)
      * }}}
      *
      * @param keyValues One or more OR expressions of key/value pairs
      * @return Filtered molecule
      */
    def apply(keyValues: Or[(String, T)]): Ns with Attr = ???


    /** Match Map of map attribute key/value pair(s).
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply Map of key/value pairs (OR semantics)
      *   Greeting.id.strMap_(Map("da" -> "Hej", "en" -> "Hi there")).get === List(1, 3, 4)
      * }}}
      *
      * @param map Map of key/value pairs
      * @return Filtered molecule
      */
    def apply(map: Map[String, T]): Ns with Attr = ???


    /** Match map attribute key to retrieve corresponding key/value pairs.
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply key
      *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
      *   Greeting.id.strMap.k("en").get === List(
      *     (1, Map("en" -> "Hi there")),
      *     (2, Map("en" -> "Oh, Hi")),
      *     (3, Map("en" -> "Hello"))
      *   )
      * }}}
      *
      * @param key Map attribute key (String)
      * @return Filtered molecule
      */
    def k(key: String): Values with Ns with Attr = ??? // Hack to satisfy intellij inference


    /** Match map attribute keys to retrieve corresponding key/value pairs.
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply keys
      *   Greeting.id.strMap_.k("en", "fr").get === List(1, 2, 3)
      *   Greeting.id.strMap.k("en", "fr").get === List(
      *     (1, Map("en" -> "Hi there")),
      *     (2, Map("en" -> "Oh, Hi", "fr" -> "Bonjour")),
      *     (3, Map("en" -> "Hello"))
      *   )
      * }}}
      *
      * @param key      Map attribute key (String)
      * @param moreKeys Optional additional map attribute keys
      * @return Filtered molecule
      */
    def k(key: String, moreKeys: String*): Values with Ns with Attr = ???


    /** Match Iterable of map attribute keys to retrieve corresponding key/value pairs.
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply Iterable of keys
      *   Greeting.id.strMap_.k(List("en", "fr")).get === List(1, 2, 3)
      *   Greeting.id.strMap.k(List("en", "fr")).get === List(
      *     (1, Map("en" -> "Hi there")),
      *     (2, Map("en" -> "Oh, Hi", "fr" -> "Bonjour")),
      *     (3, Map("en" -> "Hello"))
      *   )
      * }}}
      *
      * @param keys Iterable of attribute keys
      * @return Filtered molecule
      */
    def k(keys: Iterable[String]): Values with Ns with Attr = ???


    /** Match OR expression(s) of map attribute keys to retrieve corresponding key/value pairs.
      * {{{
      *   Greeting.id.strMap insert List(
      *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      *     (3, Map("en" -> "Hello", "da" -> "Hej")),
      *     (4, Map("da" -> "Hej"))
      *   )
      *
      *   // Apply OR expression of keys
      *   Greeting.id.strMap_.k("en" or "fr").get === List(1, 2, 3)
      *   Greeting.id.strMap.k("en" or "fr").get === List(
      *     (1, Map("en" -> "Hi there")),
      *     (2, Map("en" -> "Oh, Hi", "fr" -> "Bonjour")),
      *     (3, Map("en" -> "Hello"))
      *   )
      *
      *   // Apply multiple OR expressions of keys
      *   Greeting.id.strMap_.k("en" or "fr" or "da").get === List(1, 2, 3, 4)
      * }}}
      *
      * @param or OR expression(s) of attribute keys
      * @return Filtered molecule
      */
    def k(or: Or[String]): Values with Ns with Attr = ???


    // Keyed attribute value methods
    trait Values {

      /** Match value(s) of filtered map attribute.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("da").get === List(1, 2, 3, 4)
        *
        *   // Apply additional value filter
        *   Greeting.id.strMap_.k("da")("Hej").get === List(3, 4)
        *
        *   // Apply additional value filters as OR expression
        *   Greeting.id.strMap_.k("da")("Hej", "Hejsa").get === List(1, 3, 4)
        * }}}
        *
        * @param value      Filtering value
        * @param moreValues Optional additional filtering values
        * @return Filtered molecule
        */
      def apply(value: T, moreValues: T*): Ns with Attr = ???


      /** Match Iterable of value(s) of filtered map attribute.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("da").get === List(1, 2, 3, 4)
        *
        *   // Apply Seq of additional value filters (OR semantics)
        *   Greeting.id.strMap_.k("da")(Seq("Hej", "Hejsa")).get === List(1, 3, 4)
        * }}}
        *
        * @param values Iterable of filtering value(s)
        * @return Filtered molecule
        */
      def apply(values: Iterable[T]): Ns with Attr = ???


      /** Match OR expression of value(s) of filtered map attribute.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("da").get === List(1, 2, 3, 4)
        *
        *   // Apply additional value filters as OR expression
        *   Greeting.id.strMap_.k("da")("Hej" or "Hejsa").get === List(1, 3, 4)
        *   Greeting.id.strMap_.k("da")("Hej" or "Hejsa" or "Hilser").get === List(1, 2, 3, 4)
        * }}}
        *
        * @param or OR expression of filtering values
        * @return Filtered molecule
        */
      def apply(or: Or[T]): Ns with Attr = ???


      /** Match negated value(s) of filtered map attribute.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional negation value filter
        *   Greeting.id.strMap_.k("en").not("Hello").get === List(1, 2)
        *   Greeting.id.strMap.k("en").not("Hello").get === List(
        *     (1, Map("en" -> "Hi there")),
        *     (2, Map("en" -> "Oh, Hi"))
        *   )
        *
        *   // Apply multiple negation value filters (OR semantics)
        *   Greeting.id.strMap_.k("en").not("Hello", "Hi there").get === List(2)
        *
        *   // Same as
        *   Greeting.id.strMap_.k("en").!=("Hello", "Hi there").get === List(2)
        * }}}
        *
        * @param value      Filter value
        * @param moreValues Optional additional filter values
        * @return Filtered molecule
        */
      def not(value: T, moreValues: T*): Ns with Attr = ???


      /** Match negated value(s) of filtered map attribute.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional negation value filter
        *   Greeting.id.strMap_.k("en").!=("Hello").get === List(1, 2)
        *   Greeting.id.strMap.k("en").!=("Hello").get === List(
        *     (1, Map("en" -> "Hi there")),
        *     (2, Map("en" -> "Oh, Hi"))
        *   )
        *
        *   // Apply multiple negation value filters (OR semantics)
        *   Greeting.id.strMap_.k("en").!=("Hello", "Hi there").get === List(2)
        *
        *   // Same as
        *   Greeting.id.strMap_.k("en").not("Hello", "Hi there").get === List(2)
        * }}}
        *
        * @param value      Filter value
        * @param moreValues Optional additional filter values
        * @return Filtered molecule
        */
      def !=(value: T, moreValues: T*): Ns with Attr = ???


      /** Match values of filtered map attribute less than upper value.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional less-than value filter
        *   Greeting.id.strMap_.k("en").<("Hi").get === List(3)
        *   Greeting.id.strMap.k("en").<("Hi").get === List(
        *     (3, Map("en" -> "Hello"))
        *   )
        * }}}
        *
        * @param upper Upper value
        * @return Filtered molecule
        */
      def <(upper: T): Ns with Attr = ???


      /** Match values of filtered map attribute less than or equal to upper value.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional less-than-or-equal-to value filter
        *   Greeting.id.strMap_.k("en").<=("Hi").get === List(1, 3)
        *   Greeting.id.strMap.k("en").<=("Hi").get === List(
        *     (1, Map("en" -> "Hi there")),
        *     (3, Map("en" -> "Hello"))
        *   )
        * }}}
        *
        * @param upper Upper value
        * @return Filtered molecule
        */
      def <=(upper: T): Ns with Attr = ???


      /** Match values of filtered map attribute bigger than lower value.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional bigger-than value filter
        *   Greeting.id.strMap_.k("en").>("Hi").get === List(2)
        *   Greeting.id.strMap.k("en").>("Hi").get === List(
        *     (2, Map("en" -> "Oh, Hi"))
        *   )
        * }}}
        *
        * @param lower Lower value
        * @return Filtered molecule
        */
      def >(lower: T): Ns with Attr = ???


      /** Match values of filtered map attribute bigger than or equal to lower value.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional bigger-than-or-equal-to value filter
        *   Greeting.id.strMap_.k("en").>=("Hi").get === List(1, 2)
        *   Greeting.id.strMap.k("en").>=("Hi").get === List(
        *     (1, Map("en" -> "Hi there")),
        *     (2, Map("en" -> "Oh, Hi"))
        *   )
        * }}}
        *
        * @param lower Lower value
        * @return Filtered molecule
        */
      def >=(lower: T): Ns with Attr = ???


      /** Match word values of filtered String map attribute.
        * <br><br>
        * Fulltext searches are case-insensitive and only searches for whole words.
        * {{{
        *   Greeting.id.strMap insert List(
        *     (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
        *     (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
        *     (3, Map("en" -> "Hello", "da" -> "Hej")),
        *     (4, Map("da" -> "Hej"))
        *   )
        *
        *   // Apply key filter only
        *   Greeting.id.strMap_.k("en").get === List(1, 2, 3)
        *
        *   // Apply additional less-than-or-equal-to value filter
        *   Greeting.id.strMap_.k("en").contains("Hi").get === List(1, 2)
        *   Greeting.id.strMap.k("en").contains("Hi").get === List(
        *     (1, Map("en" -> "Hi there")),
        *     (2, Map("en" -> "Oh, Hi"))
        *   )
        *
        *   // Regex can be used
        *   Greeting.id.strMap_.k("en").contains("Hi|Hello").get === List(1, 2, 3)
        * }}}
        *
        * @note Fulltext search is constrained by several defaults (which cannot be altered):
        *       searches are case insensitive, remove apostrophe or apostrophe and s sequences,
        *       and filter out the following common English stop words:
        *       "a", "an", "and", "are", "as", "at", "be", "but", "by",
        *       "for", "if", "in", "into", "is", "it",
        *       "no", "not", "of", "on", "or", "such",
        *       "that", "the", "their", "then", "there", "these",
        *       "they", "this", "to", "was", "will", "with"
        * @param word Search word or regex
        * @return Filtered molecule
        */
      def contains(word: T): Ns with Attr = ???
      // Todo: How can we make `contains` available to String type only?
    }
  }

  /** Expression methods of optional attributes. */
  trait OptionalExpr[Ns, T] {

    /** Apply optional attribute value in save molecule.
      * {{{
      *   val benAge = Some(42)
      *   val lizAge = None
      *
      *   // Save optional `age` values
      *   Person.name("Ben").age$(benAge).save
      *   Person.name("Liz").age$(lizAge).save
      *
      *   Person.name.age$.get === List(
      *     ("Ben", Some(42)),
      *     ("Liz", None),
      *   )
      * }}}
      *
      * @param some Optional attribute value to be saved
      * @return Save-molecule
      */
    def apply(some: Option[T]): Ns with Attr = ???
  }

  /** Expression methods of String attributes with fulltext search. */
  trait FulltextExpr[Ns, In] {

    /** Match words of String attribute.
      * <br><br>
      * Fulltext searches are case-insensitive and only searches for whole words.
      * {{{
      *   Phrase.id.txt.get === List(
      *     (1, "The quick fox jumps"),
      *     (2, "Ten slow monkeys")
      *   )
      *
      *   Phrase.id.txt_.contains("jumps").get === List(1)
      *
      *   // Only whole words matched
      *   Phrase.id.txt_.contains("jump").get === Nil
      *
      *   // Searches are case-insensitive
      *   Phrase.id.txt_.contains("JuMpS").get === List(1)
      *
      *   // Empty spaces ignored
      *   Phrase.id.txt_.contains("   jumps   ").get === List(1)
      *
      *   // Multiple search words have OR semantics
      *   Phrase.id.txt_.contains("jumps", "slow").get === List(1, 2)
      *
      *   // Common words ignored
      *   Phrase.id.txt_.contains("The").get === Nil
      * }}}
      *
      * @note Fulltext search is constrained by several defaults (which cannot be altered):
      *       searches are case insensitive, remove apostrophe or apostrophe and s sequences,
      *       and filter out the following common English stop words:
      *       "a", "an", "and", "are", "as", "at", "be", "but", "by",
      *       "for", "if", "in", "into", "is", "it",
      *       "no", "not", "of", "on", "or", "such",
      *       "that", "the", "their", "then", "there", "these",
      *       "they", "this", "to", "was", "will", "with"
      * @param word     Search word
      * @param moreWords Optional additional search words
      * @return Filtered molecule
      */
    def contains(word: String, moreWords: String*): Ns with Attr = ???


    /** Mark as input molecule with String attribute word search.
      * <br><br>
      * Fulltext searches are case-insensitive and only searches for whole words.
      * {{{
      *   Phrase.id.txt.get === List(
      *     (1, "The quick fox jumps"),
      *     (2, "Ten slow monkeys")
      *   )
      *
      *   // Mark as input molecule awaiting word(s) to search for
      *   val phraseFinder = m(Phrase.id.txt_.contains(?))
      *
      *   // Then we can apply words to the input molecule at runtime:
      *
      *   phraseFinder("jumps").get === List(1)
      *
      *   // Only whole words matched
      *   phraseFinder("jump").get === Nil
      *
      *   // Searches are case-insensitive
      *   phraseFinder("JuMpS").get === List(1)
      *
      *   // Empty spaces ignored
      *   phraseFinder("   jumps   ").get === List(1)
      *
      *   // Multiple search words have OR semantics
      *   phraseFinder("jumps", "slow").get === List(1, 2)
      *
      *   // Common words ignored
      *   phraseFinder("The").get === Nil
      * }}}
      *
      * @note Fulltext search is constrained by several defaults (which cannot be altered):
      *       searches are case insensitive, remove apostrophe or apostrophe and s sequences,
      *       and filter out the following common English stop words:
      *       "a", "an", "and", "are", "as", "at", "be", "but", "by",
      *       "for", "if", "in", "into", "is", "it",
      *       "no", "not", "of", "on", "or", "such",
      *       "that", "the", "their", "then", "there", "these",
      *       "they", "this", "to", "was", "will", "with"
      * @param words Search words
      * @return Input molecule awaiting search word(s)
      */
    def contains(words: ??): In with Attr = ???
  }
}

object AttrExpressions extends AttrExpressions