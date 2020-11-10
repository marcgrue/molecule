package molecule.core

/** Internal transformers from DSL to Model/Query/Transaction/Datomic.
  * <br><br>
  * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
  * <br><br>
  * Custom DSL molecule --> Model --> Query --> Datomic query string
  * @see [[http://www.scalamolecule.org/dev/transformation/]]
  * */
package object transform
