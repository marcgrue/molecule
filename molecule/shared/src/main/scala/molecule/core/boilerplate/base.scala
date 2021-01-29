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
  trait NS00[obj[_], props] extends NS
  trait NS01[obj[_], props, A] extends NS
  trait NS02[obj[_], props, A, B] extends NS
  trait NS03[obj[_], props, A, B, C] extends NS
  trait NS04[obj[_], props, A, B, C, D] extends NS
  trait NS05[obj[_], props, A, B, C, D, E] extends NS
  trait NS06[obj[_], props, A, B, C, D, E, F] extends NS
  trait NS07[obj[_], props, A, B, C, D, E, F, G] extends NS
  trait NS08[obj[_], props, A, B, C, D, E, F, G, H] extends NS
  trait NS09[obj[_], props, A, B, C, D, E, F, G, H, I] extends NS
  trait NS10[obj[_], props, A, B, C, D, E, F, G, H, I, J] extends NS
  trait NS11[obj[_], props, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait NS12[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait NS13[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait NS14[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait NS15[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait NS16[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait NS17[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait NS18[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait NS19[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait NS20[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait NS21[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait NS22[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait NS23[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait NS24[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait NS25[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait IN1
  trait IN1_00[obj[_], props, I1] extends NS
  trait IN1_01[obj[_], props, I1, A] extends NS
  trait IN1_02[obj[_], props, I1, A, B] extends NS
  trait IN1_03[obj[_], props, I1, A, B, C] extends NS
  trait IN1_04[obj[_], props, I1, A, B, C, D] extends NS
  trait IN1_05[obj[_], props, I1, A, B, C, D, E] extends NS
  trait IN1_06[obj[_], props, I1, A, B, C, D, E, F] extends NS
  trait IN1_07[obj[_], props, I1, A, B, C, D, E, F, G] extends NS
  trait IN1_08[obj[_], props, I1, A, B, C, D, E, F, G, H] extends NS
  trait IN1_09[obj[_], props, I1, A, B, C, D, E, F, G, H, I] extends NS
  trait IN1_10[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J] extends NS
  trait IN1_11[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait IN1_12[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait IN1_13[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait IN1_14[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait IN1_15[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait IN1_16[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait IN1_17[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait IN1_18[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait IN1_19[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait IN1_20[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait IN1_21[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait IN1_22[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait IN1_23[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait IN1_24[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait IN1_25[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait IN2
  trait IN2_00[obj[_], props, I1, I2] extends NS
  trait IN2_01[obj[_], props, I1, I2, A] extends NS
  trait IN2_02[obj[_], props, I1, I2, A, B] extends NS
  trait IN2_03[obj[_], props, I1, I2, A, B, C] extends NS
  trait IN2_04[obj[_], props, I1, I2, A, B, C, D] extends NS
  trait IN2_05[obj[_], props, I1, I2, A, B, C, D, E] extends NS
  trait IN2_06[obj[_], props, I1, I2, A, B, C, D, E, F] extends NS
  trait IN2_07[obj[_], props, I1, I2, A, B, C, D, E, F, G] extends NS
  trait IN2_08[obj[_], props, I1, I2, A, B, C, D, E, F, G, H] extends NS
  trait IN2_09[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I] extends NS
  trait IN2_10[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J] extends NS
  trait IN2_11[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait IN2_12[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait IN2_13[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait IN2_14[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait IN2_15[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait IN2_16[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait IN2_17[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait IN2_18[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait IN2_19[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait IN2_20[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait IN2_21[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait IN2_22[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait IN2_23[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait IN2_24[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait IN2_25[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS


  trait IN3
  trait IN3_00[obj[_], props, I1, I2, I3] extends NS
  trait IN3_01[obj[_], props, I1, I2, I3, A] extends NS
  trait IN3_02[obj[_], props, I1, I2, I3, A, B] extends NS
  trait IN3_03[obj[_], props, I1, I2, I3, A, B, C] extends NS
  trait IN3_04[obj[_], props, I1, I2, I3, A, B, C, D] extends NS
  trait IN3_05[obj[_], props, I1, I2, I3, A, B, C, D, E] extends NS
  trait IN3_06[obj[_], props, I1, I2, I3, A, B, C, D, E, F] extends NS
  trait IN3_07[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G] extends NS
  trait IN3_08[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H] extends NS
  trait IN3_09[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I] extends NS
  trait IN3_10[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J] extends NS
  trait IN3_11[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K] extends NS
  trait IN3_12[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L] extends NS
  trait IN3_13[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M] extends NS
  trait IN3_14[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N] extends NS
  trait IN3_15[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] extends NS
  trait IN3_16[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends NS
  trait IN3_17[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] extends NS
  trait IN3_18[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] extends NS
  trait IN3_19[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] extends NS
  trait IN3_20[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] extends NS
  trait IN3_21[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends NS
  trait IN3_22[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends NS
  trait IN3_23[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X] extends NS
  trait IN3_24[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y] extends NS
  trait IN3_25[obj[_], props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, X, Y, Z] extends NS
}