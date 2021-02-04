package molecule.core.boilerplate


/** Base namespace markers to carry attribute types from one arity to the next. */
object base {

  trait NS

  trait FirstNS extends NS {

    /** Filter molecule by applying one or more entity ids of type `Long`.
      *
      * @param eid  First entity id
      * @param eids Further entity ids (varargs)
      * @return molecule to be further expanded with more attributes.
      */
    def apply(eid: Long, eids: Long*): AnyRef = ???

    /** Filter molecule by applying one or more entity ids of type `Long`.
      *
      * @param eids Iterable of entity ids, typically List, Seq or Set of ids.
      * @return molecule to be further expanded with more attributes.
      */
    def apply(eids: Iterable[Long]): AnyRef = ???

    /** Add entity id(s) input placeholder to the molecule.
      * <br>At runtime, entity id(s) are applied as vararg(s) or list/sets.
      *
      * @param eids Iterable of entity ids, typically List, Seq or Set of ids.
      * @return molecule to be further expanded with more attributes.
      */
    def apply(eids: molecule.core._2_dsl.expression.AttrExpressions.?): AnyRef = ???
  }

  // Using dummy type parameter to simplify parsing DSL
  trait NS_0_00[obj[_], props] extends NS
  trait NS_0_01[obj[_], props, A] extends NS
  trait NS_0_02[obj[_], props, A, B] extends NS
  trait NS_0_03[obj[_], props, A, B, C] extends NS
  trait NS_0_04[obj[_], props, A, B, C, D] extends NS
  trait NS_0_05[obj[_], props, A, B, C, D, E] extends NS
  trait NS_0_06[obj[_], props, A, B, C, D, E, F] extends NS
  trait NS_0_07[obj[_], props, A, B, C, D, E, F, G] extends NS
  trait NS_0_08[obj[_], props, A, B, C, D, E, F, G, H] extends NS
  trait NS_0_09[obj[_], props, A, B, C, D, E, F, G, H, I] extends NS
  trait NS_0_10[obj[_], props, A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS_0_11[obj[_], props, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS_0_12[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS_0_13[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS_0_14[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS_0_15[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS_0_16[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS_0_17[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS_0_18[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS_0_19[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS_0_20[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS_0_21[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS_0_22[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS_0_23[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS_0_24[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS_0_25[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait NS_1_00[obj[_], props, I1] extends NS
  trait NS_1_01[obj[_], props, I1, A] extends NS
  trait NS_1_02[obj[_], props, I1, A, B] extends NS
  trait NS_1_03[obj[_], props, I1, A, B, C] extends NS
  trait NS_1_04[obj[_], props, I1, A, B, C, D] extends NS
  trait NS_1_05[obj[_], props, I1, A, B, C, D, E] extends NS
  trait NS_1_06[obj[_], props, I1, A, B, C, D, E, F] extends NS
  trait NS_1_07[obj[_], props, I1, A, B, C, D, E, F, G] extends NS
  trait NS_1_08[obj[_], props, I1, A, B, C, D, E, F, G, H] extends NS
  trait NS_1_09[obj[_], props, I1, A, B, C, D, E, F, G, H, I] extends NS
  trait NS_1_10[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS_1_11[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS_1_12[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS_1_13[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS_1_14[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS_1_15[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS_1_16[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS_1_17[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS_1_18[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS_1_19[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS_1_20[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS_1_21[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS_1_22[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS_1_23[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS_1_24[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS_1_25[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait NS_2_00[obj[_], props, I1, I2] extends NS
  trait NS_2_01[obj[_], props, I1, I2, A] extends NS
  trait NS_2_02[obj[_], props, I1, I2, A, B] extends NS
  trait NS_2_03[obj[_], props, I1, I2, A, B, C] extends NS
  trait NS_2_04[obj[_], props, I1, I2, A, B, C, D] extends NS
  trait NS_2_05[obj[_], props, I1, I2, A, B, C, D, E] extends NS
  trait NS_2_06[obj[_], props, I1, I2, A, B, C, D, E, F] extends NS
  trait NS_2_07[obj[_], props, I1, I2, A, B, C, D, E, F, G] extends NS
  trait NS_2_08[obj[_], props, I1, I2, A, B, C, D, E, F, G, H] extends NS
  trait NS_2_09[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I] extends NS
  trait NS_2_10[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS_2_11[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS_2_12[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS_2_13[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS_2_14[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS_2_15[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS_2_16[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS_2_17[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS_2_18[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS_2_19[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS_2_20[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS_2_21[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS_2_22[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS_2_23[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS_2_24[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS_2_25[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait NS_3_00[obj[_], props, I1, I2, I3] extends NS
  trait NS_3_01[obj[_], props, I1, I2, I3, A] extends NS
  trait NS_3_02[obj[_], props, I1, I2, I3, A, B] extends NS
  trait NS_3_03[obj[_], props, I1, I2, I3, A, B, C] extends NS
  trait NS_3_04[obj[_], props, I1, I2, I3, A, B, C, D] extends NS
  trait NS_3_05[obj[_], props, I1, I2, I3, A, B, C, D, E] extends NS
  trait NS_3_06[obj[_], props, I1, I2, I3, A, B, C, D, E, F] extends NS
  trait NS_3_07[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G] extends NS
  trait NS_3_08[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H] extends NS
  trait NS_3_09[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I] extends NS
  trait NS_3_10[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS_3_11[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS_3_12[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS_3_13[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS_3_14[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS_3_15[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS_3_16[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS_3_17[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS_3_18[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS_3_19[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS_3_20[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS_3_21[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS_3_22[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS_3_23[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS_3_24[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS_3_25[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS
}