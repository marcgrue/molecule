package molecule.core.boilerplate
import molecule.core.boilerplate.base._
import molecule.core.composition.CompositeInit_In_2._
import molecule.core.expression.AggregateKeywords._
import molecule.core.generic.datom._
import scala.language.higherKinds


/** Type distribution to groups of functionality for input molecules expecting 2 inputs. */
object in2 {

  trait In_2_0[In2_0[_, _], In2_1[_, _, _], In3_0[_, _, _], In3_1[_, _, _, _], I1, I2]
    extends IN2_00[I1, I2]
      with Datom_2[In2_0, In2_1, In3_0, In3_1, I1, I2]
      with CompositeInit_In_2_00[I1, I2] {

    // Build on from entity id
    def apply(e: Long): In2_0[I1, I2] = ???
  }

  trait In_2_1[In2_1[_, _, _], In2_2[_, _, _, _], In3_1[_, _, _, _], In3_2[_, _, _, _, _], I1, I2, A]
    extends IN2_01[I1, I2, A]
      with Datom_3[In2_1, In2_2, In3_1, In3_2, I1, I2, A]
      with Aggregate03[In2_1, I1, I2, A]
      with CompositeInit_In_2_01[I1, I2, A]

  trait In_2_2[In2_2[_, _, _, _], In2_3[_, _, _, _, _], In3_2[_, _, _, _, _], In3_3[_, _, _, _, _, _], I1, I2, A, B]
    extends IN2_02[I1, I2, A, B]
      with Datom_4[In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]
      with Aggregate04[In2_2, I1, I2, A, B]
      with CompositeInit_In_2_02[I1, I2, A, B]

  trait In_2_3[In2_3[_, _, _, _, _], In2_4[_, _, _, _, _, _], In3_3[_, _, _, _, _, _], In3_4[_, _, _, _, _, _, _], I1, I2, A, B, C]
    extends IN2_03[I1, I2, A, B, C]
      with Datom_5[In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]
      with Aggregate05[In2_3, I1, I2, A, B, C]
      with CompositeInit_In_2_03[I1, I2, A, B, C]

  trait In_2_4[In2_4[_, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], In3_4[_, _, _, _, _, _, _], In3_5[_, _, _, _, _, _, _, _], I1, I2, A, B, C, D]
    extends IN2_04[I1, I2, A, B, C, D]
      with Datom_6[In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]
      with Aggregate06[In2_4, I1, I2, A, B, C, D]
      with CompositeInit_In_2_04[I1, I2, A, B, C, D]

  trait In_2_5[In2_5[_, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], In3_5[_, _, _, _, _, _, _, _], In3_6[_, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E]
    extends IN2_05[I1, I2, A, B, C, D, E]
      with Datom_7[In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]
      with Aggregate07[In2_5, I1, I2, A, B, C, D, E]
      with CompositeInit_In_2_05[I1, I2, A, B, C, D, E]

  trait In_2_6[In2_6[_, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], In3_6[_, _, _, _, _, _, _, _, _], In3_7[_, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F]
    extends IN2_06[I1, I2, A, B, C, D, E, F]
      with Datom_8[In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]
      with Aggregate08[In2_6, I1, I2, A, B, C, D, E, F]
      with CompositeInit_In_2_06[I1, I2, A, B, C, D, E, F]

  trait In_2_7[In2_7[_, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], In3_7[_, _, _, _, _, _, _, _, _, _], In3_8[_, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G]
    extends IN2_07[I1, I2, A, B, C, D, E, F, G]
      with Datom_9[In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]
      with Aggregate09[In2_7, I1, I2, A, B, C, D, E, F, G]
      with CompositeInit_In_2_07[I1, I2, A, B, C, D, E, F, G]

  trait In_2_8[In2_8[_, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], In3_8[_, _, _, _, _, _, _, _, _, _, _], In3_9[_, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H]
    extends IN2_08[I1, I2, A, B, C, D, E, F, G, H]
      with Datom_10[In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]
      with Aggregate10[In2_8, I1, I2, A, B, C, D, E, F, G, H]
      with CompositeInit_In_2_08[I1, I2, A, B, C, D, E, F, G, H]

  trait In_2_9[In2_9[_, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In3_9[_, _, _, _, _, _, _, _, _, _, _, _], In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I]
    extends IN2_09[I1, I2, A, B, C, D, E, F, G, H, I]
      with Datom_11[In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]
      with Aggregate11[In2_9, I1, I2, A, B, C, D, E, F, G, H, I]
      with CompositeInit_In_2_09[I1, I2, A, B, C, D, E, F, G, H, I]

  trait In_2_10[In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J]
    extends IN2_10[I1, I2, A, B, C, D, E, F, G, H, I, J]
      with Datom_12[In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]
      with Aggregate12[In2_10, I1, I2, A, B, C, D, E, F, G, H, I, J]
      with CompositeInit_In_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J]

  trait In_2_11[In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K]
    extends IN2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      with Datom_13[In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate13[In2_11, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      with CompositeInit_In_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K]

  trait In_2_12[In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
    extends IN2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      with Datom_14[In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate14[In2_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      with CompositeInit_In_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]

  trait In_2_13[In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends IN2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Datom_15[In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate15[In2_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with CompositeInit_In_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait In_2_14[In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends IN2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Datom_16[In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate16[In2_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with CompositeInit_In_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait In_2_15[In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends IN2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Datom_17[In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate17[In2_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with CompositeInit_In_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait In_2_16[In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends IN2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Datom_18[In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate18[In2_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with CompositeInit_In_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait In_2_17[In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends IN2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Datom_19[In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate19[In2_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with CompositeInit_In_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait In_2_18[In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends IN2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Datom_20[In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate20[In2_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with CompositeInit_In_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait In_2_19[In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends IN2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Datom_21[In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate21[In2_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with CompositeInit_In_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait In_2_20[In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends IN2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Datom_22[In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Aggregate22[In2_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with CompositeInit_In_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait In_2_21[In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends IN2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with CompositeInit_In_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait In_2_22[In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends IN2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with CompositeInit_In_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}