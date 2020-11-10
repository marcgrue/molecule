package molecule.core.boilerplate
import molecule.core.boilerplate.base._
import molecule.core.composition.CompositeInit_In_1._
import molecule.core.expression.AggregateKeywords._
import molecule.core.generic.datom._
import scala.language.higherKinds


/** Type distribution to groups of functionality for input molecules expecting 1 input. */
object in1 {

  // For starting a molecule build with a tacit attribute
  trait In_1_0[In1_0[_], In1_1[_, _], In2_0[_, _], In2_1[_, _, _], I1]
    extends IN1_00[I1]
      with Datom_1[In1_0, In1_1, In2_0, In2_1, I1]
      with CompositeInit_In_1_00[I1] {

    // Build on from entity id
    def apply(e: Long): In1_0[I1] = ???
  }

  trait In_1_1[In1_1[_, _], In1_2[_, _, _], In2_1[_, _, _], In2_2[_, _, _, _], I1, A]
    extends IN1_01[I1, A]
      with Datom_2[In1_1, In1_2, In2_1, In2_2, I1, A]
      with Aggregate02[In1_1, I1, A]
      with CompositeInit_In_1_01[I1, A]


  trait In_1_2[In1_2[_, _, _], In1_3[_, _, _, _], In2_2[_, _, _, _], In2_3[_, _, _, _, _], I1, A, B]
    extends IN1_02[I1, A, B]
      with Datom_3[In1_2, In1_3, In2_2, In2_3, I1, A, B]
      with Aggregate03[In1_2, I1, A, B]
      with CompositeInit_In_1_02[I1, A, B]

  trait In_1_3[In1_3[_, _, _, _], In1_4[_, _, _, _, _], In2_3[_, _, _, _, _], In2_4[_, _, _, _, _, _], I1, A, B, C]
    extends IN1_03[I1, A, B, C]
      with Datom_4[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]
      with Aggregate04[In1_3, I1, A, B, C]
      with CompositeInit_In_1_03[I1, A, B, C]

  trait In_1_4[In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], In2_4[_, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], I1, A, B, C, D]
    extends IN1_04[I1, A, B, C, D]
      with Datom_5[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]
      with Aggregate05[In1_4, I1, A, B, C, D]
      with CompositeInit_In_1_04[I1, A, B, C, D]

  trait In_1_5[In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], I1, A, B, C, D, E]
    extends IN1_05[I1, A, B, C, D, E]
      with Datom_6[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]
      with Aggregate06[In1_5, I1, A, B, C, D, E]
      with CompositeInit_In_1_05[I1, A, B, C, D, E]

  trait In_1_6[In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F]
    extends IN1_06[I1, A, B, C, D, E, F]
      with Datom_7[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]
      with Aggregate07[In1_6, I1, A, B, C, D, E, F]
      with CompositeInit_In_1_06[I1, A, B, C, D, E, F]

  trait In_1_7[In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G]
    extends IN1_07[I1, A, B, C, D, E, F, G]
      with Datom_8[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]
      with Aggregate08[In1_7, I1, A, B, C, D, E, F, G]
      with CompositeInit_In_1_07[I1, A, B, C, D, E, F, G]

  trait In_1_8[In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H]
    extends IN1_08[I1, A, B, C, D, E, F, G, H]
      with Datom_9[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]
      with Aggregate09[In1_8, I1, A, B, C, D, E, F, G, H]
      with CompositeInit_In_1_08[I1, A, B, C, D, E, F, G, H]

  trait In_1_9[In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I]
    extends IN1_09[I1, A, B, C, D, E, F, G, H, I]
      with Datom_10[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]
      with Aggregate10[In1_9, I1, A, B, C, D, E, F, G, H, I]
      with CompositeInit_In_1_09[I1, A, B, C, D, E, F, G, H, I]

  trait In_1_10[In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J]
    extends IN1_10[I1, A, B, C, D, E, F, G, H, I, J]
      with Datom_11[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]
      with Aggregate11[In1_10, I1, A, B, C, D, E, F, G, H, I, J]
      with CompositeInit_In_1_10[I1, A, B, C, D, E, F, G, H, I, J]

  trait In_1_11[In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K]
    extends IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
      with Datom_12[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate12[In1_11, I1, A, B, C, D, E, F, G, H, I, J, K]
      with CompositeInit_In_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]

  trait In_1_12[In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L]
    extends IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
      with Datom_13[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate13[In1_12, I1, A, B, C, D, E, F, G, H, I, J, K, L]
      with CompositeInit_In_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]

  trait In_1_13[In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Datom_14[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate14[In1_13, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with CompositeInit_In_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait In_1_14[In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Datom_15[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate15[In1_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with CompositeInit_In_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait In_1_15[In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Datom_16[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate16[In1_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with CompositeInit_In_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait In_1_16[In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Datom_17[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate17[In1_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with CompositeInit_In_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait In_1_17[In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Datom_18[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate18[In1_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with CompositeInit_In_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait In_1_18[In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Datom_19[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate19[In1_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with CompositeInit_In_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait In_1_19[In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends IN1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Datom_20[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate20[In1_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with CompositeInit_In_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait In_1_20[In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends IN1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Datom_21[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Aggregate21[In1_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with CompositeInit_In_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait In_1_21[In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends IN1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Datom_22[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Aggregate22[In1_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with CompositeInit_In_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait In_1_22[In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends IN1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with CompositeInit_In_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}