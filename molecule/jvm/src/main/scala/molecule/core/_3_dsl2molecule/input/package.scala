package molecule

/** Input molecules awaiting input.
  * <br><br>
  * Input molecules are molecules that awaits one or more inputs at runtime. When input value is applied,
  * the input molecule is resolved and a standard molecule is returned that we can then call actions on.
  * <br><br>
  * Input molecule queries are cached by Datomic. So there is a runtime performance gain in using input molecules. Furthermore,
  * input molecules are a good fit for re-use for queries where only a few parameters change.
  * <br><br>
  * Input molecules can await 1, 2 or 3 inputs and are constructed by applying the [[molecule.expression.AttrExpressions.? ?]] marker
  * to attributes. If one marker is applied, we get a [[molecule.core._3_dsl2molecule.input.InputMolecule_1 InputMolecule_1]], 2 inputs creates
  * an [[molecule.core._3_dsl2molecule.input.InputMolecule_2 InputMolecule_2]] and 3 an [[molecule.core._3_dsl2molecule.input.InputMolecule_3 InputMolecule_3]].
  * <br><br>
  * The three input molecule interfaces come in arity-versions corresponding to the number of non-?-marked attributes
  * in the input molecule. Let's see a simple example:
  * {{{
  *   // Sample data
  *   Person.name.age insert List(("Ben", 42), ("Liz", 34))
  *
  *   // Input molecule created at compile time. Awaits a name of type String
  *   val ageOfPersons: InputMolecule_1.InputMolecule_1_01[String, Int] = m(Person.name_(?).age)
  *
  *   // Resolved molecule. "Ben" input is matched against name attribute
  *   val ageOfPersonsNamedBen: Molecule.Molecule01[Int] = ageOfPersons.apply("Ben")
  *
  *   // Calling action on resolved molecule.
  *   // (Only age is returned since name was marked as tacit with the underscore notation)
  *   ageOfPersonsNamedBen.get === List(42)
  *
  *   // Or we can re-use the input molecule straight away
  *   ageOfPersons("Liz").get === List(34)
  * }}}
  *
  */
package object input
