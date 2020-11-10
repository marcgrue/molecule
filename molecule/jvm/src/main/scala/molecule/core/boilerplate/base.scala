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
    def apply(eids: molecule.core.expression.AttrExpressions.?): AnyRef = ???
  }

  // Using dummy type parameter to simplify parsing DSL
  trait NS00[Dummy] extends NS
  trait NS01[A] extends NS
  trait NS02[A, B] extends NS
  trait NS03[A, B, C] extends NS
  trait NS04[A, B, C, D] extends NS
  trait NS05[A, B, C, D, E] extends NS
  trait NS06[A, B, C, D, E, F] extends NS
  trait NS07[A, B, C, D, E, F, G] extends NS
  trait NS08[A, B, C, D, E, F, G, H] extends NS
  trait NS09[A, B, C, D, E, F, G, H, I] extends NS
  trait NS10[A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS11[A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS12[A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS13[A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS23[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS24[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS25[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait IN1
  trait IN1_00[I1] extends NS
  trait IN1_01[I1, A] extends NS
  trait IN1_02[I1, A, B] extends NS
  trait IN1_03[I1, A, B, C] extends NS
  trait IN1_04[I1, A, B, C, D] extends NS
  trait IN1_05[I1, A, B, C, D, E] extends NS
  trait IN1_06[I1, A, B, C, D, E, F] extends NS
  trait IN1_07[I1, A, B, C, D, E, F, G] extends NS
  trait IN1_08[I1, A, B, C, D, E, F, G, H] extends NS
  trait IN1_09[I1, A, B, C, D, E, F, G, H, I] extends NS
  trait IN1_10[I1, A, B, C, D, E, F, G, H, I, J] extends NS
  trait IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait IN1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait IN1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait IN1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait IN1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait IN1_23[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait IN1_24[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait IN1_25[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait IN2
  trait IN2_00[I1, I2] extends NS
  trait IN2_01[I1, I2, A] extends NS
  trait IN2_02[I1, I2, A, B] extends NS
  trait IN2_03[I1, I2, A, B, C] extends NS
  trait IN2_04[I1, I2, A, B, C, D] extends NS
  trait IN2_05[I1, I2, A, B, C, D, E] extends NS
  trait IN2_06[I1, I2, A, B, C, D, E, F] extends NS
  trait IN2_07[I1, I2, A, B, C, D, E, F, G] extends NS
  trait IN2_08[I1, I2, A, B, C, D, E, F, G, H] extends NS
  trait IN2_09[I1, I2, A, B, C, D, E, F, G, H, I] extends NS
  trait IN2_10[I1, I2, A, B, C, D, E, F, G, H, I, J] extends NS
  trait IN2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait IN2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait IN2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait IN2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait IN2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait IN2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait IN2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait IN2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait IN2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait IN2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait IN2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait IN2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait IN2_23[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait IN2_24[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait IN2_25[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait IN3
  trait IN3_00[I1, I2, I3] extends NS
  trait IN3_01[I1, I2, I3, A] extends NS
  trait IN3_02[I1, I2, I3, A, B] extends NS
  trait IN3_03[I1, I2, I3, A, B, C] extends NS
  trait IN3_04[I1, I2, I3, A, B, C, D] extends NS
  trait IN3_05[I1, I2, I3, A, B, C, D, E] extends NS
  trait IN3_06[I1, I2, I3, A, B, C, D, E, F] extends NS
  trait IN3_07[I1, I2, I3, A, B, C, D, E, F, G] extends NS
  trait IN3_08[I1, I2, I3, A, B, C, D, E, F, G, H] extends NS
  trait IN3_09[I1, I2, I3, A, B, C, D, E, F, G, H, I] extends NS
  trait IN3_10[I1, I2, I3, A, B, C, D, E, F, G, H, I, J] extends NS
  trait IN3_11[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait IN3_12[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait IN3_13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait IN3_14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait IN3_15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait IN3_16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait IN3_17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait IN3_18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait IN3_19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait IN3_20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait IN3_21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait IN3_22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait IN3_23[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait IN3_24[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait IN3_25[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS
}