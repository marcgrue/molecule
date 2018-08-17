package molecule.factory

import molecule.boilerplate.in1._
  import molecule.input.InputMolecule_1._
import molecule.macros.MakeMolecule_In._
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Factory methods to create input molecules of arity 1-22 awaiting 1 input.
  * == Molecules ==
  * Molecules are type-safe custom Scala models of data structures in a Datomic database.
  * <br><br>
  * Molecules are build with your custom meta-DSL that is auto-generated from your Schema Definition file.
  * Calling `m` on your modelled DSL structure lets Molecule macros create a custom molecule,
  * ready for retrieving or manipulating data in the Datomic database.
  * <br><br>
  * Each molecule consists of one or more attributes that can have values or expressions applied.
  * The arity of a molecule is determined by the number of attributes that will return data when the
  * molecule is queried against the Datomic database. Attributes returning data are called "output attributes".
  *
  * == Input molecules ==
  * Input molecules awaiting 1 input have one attribute with `?` applied to mark that
  * it awaits an input at runtime for that attribute. Once the input molecule has been resolved
  * with input, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @see [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  * @groupname input1 Factory methods to create input molecules awaiting 1 input.
  * @groupprio input1 51
  */
trait Molecule_In_1_Factory {

  /** Macro creation of input molecule awaiting 1 input from user-defined DSL structure with 1 output attribute (arity 1).
    * <br><br>
    * Molecules are build by adding one or more attributes to an initial namespace
    * like `Person` from the example below.
    * <br><br>
    * Applying the `?` marker to an attribute changes the semantics of a molecule to become
    * an "input molecule" that awaits input at runtime for the attribute marked with `?`.
    * <br><br>
    * Once the input molecule has been resolved with input, we can call various
    * actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` attribute to create input molecule
    *   val personOfAge = m(Person.name.age_(?))
    *
    *   // At runtime, an `age` value is applied to get the Person's name
    *   personOfAge(42).get.head === "Ben"
    * }}}
    * For arity-many molecules, data structures are returned as tuples. But for arity-1
    * molecules (like the example having only 1 output attribute, `name`) there's no need for
    * a tuple, so values type-safely matching the attribute are returned directly in the list.
    *
    * @group input1
    * @param inputDsl User-defined DSL structure modelling the input molecule
    * @tparam In1_1 Internal builder pattern type
    * @tparam In1_2 Internal builder pattern type
    * @tparam In2_1 Internal builder pattern type
    * @tparam In2_2 Internal builder pattern type
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @return Input molecule ready to be resolved
    */
  def m[In1_1[_, _], In1_2[_, _, _], In2_1[_, _, _], In2_2[_, _, _, _], I1, A](inputDsl: In_1_1[In1_1, In1_2, In2_1, In2_2, I1, A]): InputMolecule_1_01[I1, A] = macro await_1_1[In1_1, In1_2, In2_1, In2_2, I1, A]


  /** Macro creation of input molecule awaiting 1 input from user-defined DSL structure with 2 output attributes (arity 2).
    * <br><br>
    * Molecules are build by adding one or more attributes to an initial namespace
    * like `Person` from the example below.
    * <br><br>
    * Applying the `?` marker to an attribute changes the semantics of a molecule to become
    * an "input molecule" that awaits input at runtime for the attribute marked with `?`.
    * <br><br>
    * Once the input molecule has been resolved with input, we can call various
    * actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` attribute to create input molecule
    *   val personOfAge = m(Person.name.age_(?).score)
    *
    *   // At runtime, an `age` value is applied to get the Person's name and score
    *   personOfAge(42).get.head === ("Ben", 7)
    * }}}
    * @group input1
    * @param inputDsl User-defined DSL structure modelling the input molecule
    * @tparam In1_2 Internal builder pattern type
    * @tparam In1_3 Internal builder pattern type
    * @tparam In2_2 Internal builder pattern type
    * @tparam In2_3 Internal builder pattern type
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @tparam B Type of output attribute 2 (`score`: Int)
    * @return Input molecule ready to be resolved
    */
  def m[In1_2[_, _, _], In1_3[_, _, _, _], In2_2[_, _, _, _], In2_3[_, _, _, _, _], I1, A, B](inputDsl: In_1_2[In1_2, In1_3, In2_2, In2_3, I1, A, B]): InputMolecule_1_02[I1, A, B] = macro await_1_2[In1_2, In1_3, In2_2, In2_3, I1, A, B]


  def m[In1_3[_, _, _, _], In1_4[_, _, _, _, _], In2_3[_, _, _, _, _], In2_4[_, _, _, _, _, _], I1, A, B, C](inputDsl: In_1_3[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro await_1_3[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]
  def m[In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], In2_4[_, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], I1, A, B, C, D](inputDsl: In_1_4[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro await_1_4[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]
  def m[In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], I1, A, B, C, D, E](inputDsl: In_1_5[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro await_1_5[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]
  def m[In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F](inputDsl: In_1_6[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro await_1_6[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]
  def m[In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G](inputDsl: In_1_7[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro await_1_7[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]
  def m[In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H](inputDsl: In_1_8[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro await_1_8[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]
  def m[In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I](inputDsl: In_1_9[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro await_1_9[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]
  def m[In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J](inputDsl: In_1_10[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro await_1_10[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]
  def m[In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K](inputDsl: In_1_11[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro await_1_11[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L](inputDsl: In_1_12[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro await_1_12[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M](inputDsl: In_1_13[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro await_1_13[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](inputDsl: In_1_14[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro await_1_14[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](inputDsl: In_1_15[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro await_1_15[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](inputDsl: In_1_16[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro await_1_16[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](inputDsl: In_1_17[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro await_1_17[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](inputDsl: In_1_18[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro await_1_18[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](inputDsl: In_1_19[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro await_1_19[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](inputDsl: In_1_20[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro await_1_20[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](inputDsl: In_1_21[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro await_1_21[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  def m[In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](inputDsl: In_1_22[In1_22, In1_23, In2_22, In2_23, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro await_1_22[In1_22, In1_23, In2_22, In2_23, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}
