package molecule.factory

import molecule.action.Molecule._
import molecule.boilerplate.out._
import molecule.macros.MakeMolecule._
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


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
    * @param dsl     User-defined DSL structure modelling the molecule
    * @tparam Ns1    Internal builder pattern type
    * @tparam Ns2    Internal builder pattern type
    * @tparam In1_1  Internal builder pattern type
    * @tparam In1_2  Internal builder pattern type
    * @tparam A      Type of output attribute 1 (`name`: String)
    * @return        Molecule of arity-1 typed to first attribute (Molecule01[A])
    */
  implicit def m[Ns1[_], Ns2[_, _], In1_1[_, _], In1_2[_, _, _], A](dsl: Out_1[Ns1, Ns2, In1_1, In1_2, A]): Molecule01[A] = macro from1attr[Ns1, Ns2, In1_1, In1_2, A]


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
    * @group molecule
    * @param dsl    User-defined DSL structure modelling the molecule
    * @tparam Ns2   Internal builder pattern type
    * @tparam Ns3   Internal builder pattern type
    * @tparam In1_2 Internal builder pattern type
    * @tparam In1_3 Internal builder pattern type
    * @tparam A     Type of output attribute 1 (`name`: String)
    * @tparam B     Type of output attribute 2 (`age`: Int)
    * @return       Molecule of arity-2 typed to two attributes (Molecule02[A, B])
    */
  implicit def m[Ns2[_, _], Ns3[_, _, _], In1_2[_, _, _], In1_3[_, _, _, _], A, B](dsl: Out_2[Ns2, Ns3, In1_2, In1_3, A, B]): Molecule02[A, B] = macro from2attr[Ns2, Ns3, In1_2, In1_3, A, B]


  implicit def m[Ns3[_, _, _], Ns4[_, _, _, _], In1_3[_, _, _, _], In1_4[_, _, _, _, _], A, B, C](dsl: Out_3[Ns3, Ns4, In1_3, In1_4, A, B, C]): Molecule03[A, B, C] = macro from3attr[Ns3, Ns4, In1_3, In1_4, A, B, C]
  implicit def m[Ns4[_, _, _, _], Ns5[_, _, _, _, _], In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], A, B, C, D](dsl: Out_4[Ns4, Ns5, In1_4, In1_5, A, B, C, D]): Molecule04[A, B, C, D] = macro from4attr[Ns4, Ns5, In1_4, In1_5, A, B, C, D]
  implicit def m[Ns5[_, _, _, _, _], Ns6[_, _, _, _, _, _], In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], A, B, C, D, E](dsl: Out_5[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]): Molecule05[A, B, C, D, E] = macro from5attr[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]
  implicit def m[Ns6[_, _, _, _, _, _], Ns7[_, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], A, B, C, D, E, F](dsl: Out_6[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]): Molecule06[A, B, C, D, E, F] = macro from6attr[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]
  implicit def m[Ns7[_, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G](dsl: Out_7[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]): Molecule07[A, B, C, D, E, F, G] = macro from7attr[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]
  implicit def m[Ns8[_, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H](dsl: Out_8[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]): Molecule08[A, B, C, D, E, F, G, H] = macro from8attr[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]
  implicit def m[Ns9[_, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I](dsl: Out_9[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]): Molecule09[A, B, C, D, E, F, G, H, I] = macro from9attr[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]
  implicit def m[Ns10[_, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J](dsl: Out_10[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]): Molecule10[A, B, C, D, E, F, G, H, I, J] = macro from10attr[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]
  implicit def m[Ns11[_, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K](dsl: Out_11[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = macro from11attr[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]
  implicit def m[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L](dsl: Out_12[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = macro from12attr[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]
  implicit def m[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: Out_13[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = macro from13attr[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]
  implicit def m[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: Out_14[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro from14attr[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  implicit def m[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: Out_15[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro from15attr[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  implicit def m[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: Out_16[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro from16attr[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  implicit def m[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: Out_17[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro from17attr[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  implicit def m[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: Out_18[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro from18attr[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  implicit def m[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: Out_19[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro from19attr[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  implicit def m[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: Out_20[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro from20attr[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  implicit def m[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: Out_21[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro from21attr[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  implicit def m[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](dsl: Out_22[Ns22, Ns23, In1_22, In1_23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro from22attr[Ns22, Ns23, In1_22, In1_23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}
