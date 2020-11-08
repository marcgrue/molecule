package molecule.factory

import molecule.api.Molecule._
import molecule.boilerplate.base._
import molecule.macros.MakeMolecule
import scala.language.experimental.macros


/** Implicit molecule factory methods of arity 1-22.
  * <br><br>
  * Molecules are type-safe custom Scala models of data structures in a Datomic database.
  * <br><br>
  * Molecules are build with your custom meta-DSL that is auto-generated from your Schema Definition file.
  * Calling `m` on your modelled DSL structure lets Molecule macros create a custom molecule,
  * ready for retrieving or manipulating data in the Datomic database.
  * <br><br>
  * Each molecule consists of one or more attributes that can have values or expressions applied.
  * The arity of a molecule is determined by the number of attributes that will return data when the
  * molecule is queried against the Datomic database. Attributes returning data are called "output attributes".
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @see [[http://www.scalamolecule.org/manual/quick-start/introduction/ Manual]]
  * @groupname molecule Implicit factory methods to create molecules.
  * @groupprio molecule 50
  */
trait Molecule_Factory {

  /** Macro creation of molecule from user-defined DSL structure with 1 output attribute.
    * <br><br>
    * Molecules can be created explicitly or implicitly by building a DSL structure using boilerplate
    * code generated from the schema definition file.
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Once the molecule models the desired data structure
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Explicitly calling `m` to create Person molecule with 1 output attribute
    *   m(Person.name).get === List("Ben")
    *
    *   // Molecule implicitly created so we can call `get`
    *   Person.name.get.head === "Ben"
    * }}}
    * For arity-many molecules, data structures are returned as tuples. But for arity-1
    * molecules (like the example having only 1 output attribute, `name`) there's no need for
    * a tuple, so values type-safely matching the attribute are returned directly in the list.
    *
    * @group molecule
    * @param dsl User-defined DSL structure modelling the molecule
    * @tparam A Type of output attribute 1 (`name`: String)
    * @return Molecule of arity-1 typed to first attribute (Molecule01[A])
    */
  implicit final def m[A](dsl: NS01[A]): Molecule01[A] = macro MakeMolecule.from1attr[A]


  /** Macro creation of molecule from user-defined DSL structure with 2 output attributes.
    * <br><br>
    * Molecules can be created explicitly or implicitly by building a DSL structure using boilerplate
    * code generated from the schema definition file.
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Once the molecule models the desired data structure
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * <br><br>
    * Data structures are returned as tuples of values type-safely matching the molecule attribute types
    * {{{
    *   // Explicitly calling `m` to create Person molecule with 2 attributes
    *   m(Person.name.age).get.head === ("Ben", 42)
    *
    *   // Molecule implicitly created so we can call `get`
    *   Person.name.age.get.head === ("Ben", 42)
    * }}}
    *
    * @group molecule
    * @param dsl User-defined DSL structure modelling the molecule
    * @tparam A Type of output attribute 1 (`name`: String)
    * @tparam B Type of output attribute 2 (`age`: Int)
    * @return Molecule of arity-2 typed to two attributes (Molecule02[A, B])
    */
  implicit final def m[A, B](dsl: NS02[A, B]): Molecule02[A, B] = macro MakeMolecule.from2attr[A, B]

  implicit final def m[A, B, C](dsl: NS03[A, B, C]): Molecule03[A, B, C] = macro MakeMolecule.from3attr[A, B, C]
  implicit final def m[A, B, C, D](dsl: NS04[A, B, C, D]): Molecule04[A, B, C, D] = macro MakeMolecule.from4attr[A, B, C, D]
  implicit final def m[A, B, C, D, E](dsl: NS05[A, B, C, D, E]): Molecule05[A, B, C, D, E] = macro MakeMolecule.from5attr[A, B, C, D, E]
  implicit final def m[A, B, C, D, E, F](dsl: NS06[A, B, C, D, E, F]): Molecule06[A, B, C, D, E, F] = macro MakeMolecule.from6attr[A, B, C, D, E, F]
  implicit final def m[A, B, C, D, E, F, G](dsl: NS07[A, B, C, D, E, F, G]): Molecule07[A, B, C, D, E, F, G] = macro MakeMolecule.from7attr[A, B, C, D, E, F, G]
  implicit final def m[A, B, C, D, E, F, G, H](dsl: NS08[A, B, C, D, E, F, G, H]): Molecule08[A, B, C, D, E, F, G, H] = macro MakeMolecule.from8attr[A, B, C, D, E, F, G, H]
  implicit final def m[A, B, C, D, E, F, G, H, I](dsl: NS09[A, B, C, D, E, F, G, H, I]): Molecule09[A, B, C, D, E, F, G, H, I] = macro MakeMolecule.from9attr[A, B, C, D, E, F, G, H, I]
  implicit final def m[A, B, C, D, E, F, G, H, I, J](dsl: NS10[A, B, C, D, E, F, G, H, I, J]): Molecule10[A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule.from10attr[A, B, C, D, E, F, G, H, I, J]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K](dsl: NS11[A, B, C, D, E, F, G, H, I, J, K]): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule.from11attr[A, B, C, D, E, F, G, H, I, J, K]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS12[A, B, C, D, E, F, G, H, I, J, K, L]): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule.from12attr[A, B, C, D, E, F, G, H, I, J, K, L]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS13[A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule.from13attr[A, B, C, D, E, F, G, H, I, J, K, L, M]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule.from14attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule.from15attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule.from16attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule.from17attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule.from18attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule.from19attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule.from20attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: NS21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule.from21attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  implicit final def m[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](dsl: NS22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeMolecule.from22attr[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}
