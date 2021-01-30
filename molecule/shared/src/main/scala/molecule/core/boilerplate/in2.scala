package molecule.core.boilerplate
import molecule.core.boilerplate.base._
import molecule.core.composition.Composite_In_2._
import molecule.core.expression.AggregateKeywords._
import molecule.core.generic.datom._
import scala.language.higherKinds


/** Type distribution to groups of functionality for input molecules expecting 2 inputs. */
object in2 {


  trait In_2_0[obj[_], props, In2_0[o[_], _, _, _], In2_1[o[_], _, _, _, _], In3_0[o[_], _, _, _, _], In3_1[o[_], _, _, _, _, _], I1, I2]
    extends IN2_00[obj, props, I1, I2]
      with Datom_2[obj, props, In2_0, In2_1, In3_0, In3_1, I1, I2]
      with Composite_In_2_00[obj, props, I1, I2] {

    // Build on from entity id
    def apply(e: Long): In2_0[obj, props, I1, I2] = ???
  }

  trait In_2_1[obj[_], props, In2_1[o[_], _, _, _, _], In2_2[o[_], _, _, _, _, _], In3_1[o[_], _, _, _, _, _], In3_2[o[_], _, _, _, _, _, _], I1, I2, A]
    extends IN2_01[obj, props, I1, I2, A]
      with Datom_3[obj, props, In2_1, In2_2, In3_1, In3_2, I1, I2, A]
      with Aggregate03[obj, props, In2_1, I1, I2, A]
      with Composite_In_2_01[obj, props, I1, I2, A]

  trait In_2_2[obj[_], props, In2_2[o[_], _, _, _, _, _], In2_3[o[_], _, _, _, _, _, _], In3_2[o[_], _, _, _, _, _, _], In3_3[o[_], _, _, _, _, _, _, _], I1, I2, A, B]
    extends IN2_02[obj, props, I1, I2, A, B]
      with Datom_4[obj, props, In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]
      with Aggregate04[obj, props, In2_2, I1, I2, A, B]
      with Composite_In_2_02[obj, props, I1, I2, A, B]

  trait In_2_3[obj[_], props, In2_3[o[_], _, _, _, _, _, _], In2_4[o[_], _, _, _, _, _, _, _], In3_3[o[_], _, _, _, _, _, _, _], In3_4[o[_], _, _, _, _, _, _, _, _], I1, I2, A, B, C]
    extends IN2_03[obj, props, I1, I2, A, B, C]
      with Datom_5[obj, props, In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]
      with Aggregate05[obj, props, In2_3, I1, I2, A, B, C]
      with Composite_In_2_03[obj, props, I1, I2, A, B, C]

  trait In_2_4[obj[_], props, In2_4[o[_], _, _, _, _, _, _, _], In2_5[o[_], _, _, _, _, _, _, _, _], In3_4[o[_], _, _, _, _, _, _, _, _], In3_5[o[_], _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D]
    extends IN2_04[obj, props, I1, I2, A, B, C, D]
      with Datom_6[obj, props, In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]
      with Aggregate06[obj, props, In2_4, I1, I2, A, B, C, D]
      with Composite_In_2_04[obj, props, I1, I2, A, B, C, D]

  trait In_2_5[obj[_], props, In2_5[o[_], _, _, _, _, _, _, _, _], In2_6[o[_], _, _, _, _, _, _, _, _, _], In3_5[o[_], _, _, _, _, _, _, _, _, _], In3_6[o[_], _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E]
    extends IN2_05[obj, props, I1, I2, A, B, C, D, E]
      with Datom_7[obj, props, In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]
      with Aggregate07[obj, props, In2_5, I1, I2, A, B, C, D, E]
      with Composite_In_2_05[obj, props, I1, I2, A, B, C, D, E]

  trait In_2_6[obj[_], props, In2_6[o[_], _, _, _, _, _, _, _, _, _], In2_7[o[_], _, _, _, _, _, _, _, _, _, _], In3_6[o[_], _, _, _, _, _, _, _, _, _, _], In3_7[o[_], _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F]
    extends IN2_06[obj, props, I1, I2, A, B, C, D, E, F]
      with Datom_8[obj, props, In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]
      with Aggregate08[obj, props, In2_6, I1, I2, A, B, C, D, E, F]
      with Composite_In_2_06[obj, props, I1, I2, A, B, C, D, E, F]

  trait In_2_7[obj[_], props, In2_7[o[_], _, _, _, _, _, _, _, _, _, _], In2_8[o[_], _, _, _, _, _, _, _, _, _, _, _], In3_7[o[_], _, _, _, _, _, _, _, _, _, _, _], In3_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G]
    extends IN2_07[obj, props, I1, I2, A, B, C, D, E, F, G]
      with Datom_9[obj, props, In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]
      with Aggregate09[obj, props, In2_7, I1, I2, A, B, C, D, E, F, G]
      with Composite_In_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]

  trait In_2_8[obj[_], props, In2_8[o[_], _, _, _, _, _, _, _, _, _, _, _], In2_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In3_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In3_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H]
    extends IN2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]
      with Datom_10[obj, props, In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]
      with Aggregate10[obj, props, In2_8, I1, I2, A, B, C, D, E, F, G, H]
      with Composite_In_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]

  trait In_2_9[obj[_], props, In2_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In2_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In3_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I]
    extends IN2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]
      with Datom_11[obj, props, In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]
      with Aggregate11[obj, props, In2_9, I1, I2, A, B, C, D, E, F, G, H, I]
      with Composite_In_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]

  trait In_2_10[obj[_], props, In2_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J]
    extends IN2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]
      with Datom_12[obj, props, In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]
      with Aggregate12[obj, props, In2_10, I1, I2, A, B, C, D, E, F, G, H, I, J]
      with Composite_In_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]

  trait In_2_11[obj[_], props, In2_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K]
    extends IN2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      with Datom_13[obj, props, In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate13[obj, props, In2_11, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      with Composite_In_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]

  trait In_2_12[obj[_], props, In2_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
    extends IN2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      with Datom_14[obj, props, In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate14[obj, props, In2_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      with Composite_In_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]

  trait In_2_13[obj[_], props, In2_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends IN2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Datom_15[obj, props, In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate15[obj, props, In2_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Composite_In_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait In_2_14[obj[_], props, In2_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends IN2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Datom_16[obj, props, In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate16[obj, props, In2_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Composite_In_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait In_2_15[obj[_], props, In2_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends IN2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Datom_17[obj, props, In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate17[obj, props, In2_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Composite_In_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait In_2_16[obj[_], props, In2_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends IN2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Datom_18[obj, props, In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate18[obj, props, In2_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Composite_In_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait In_2_17[obj[_], props, In2_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends IN2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Datom_19[obj, props, In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate19[obj, props, In2_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Composite_In_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait In_2_18[obj[_], props, In2_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends IN2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Datom_20[obj, props, In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate20[obj, props, In2_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Composite_In_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait In_2_19[obj[_], props, In2_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends IN2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Datom_21[obj, props, In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate21[obj, props, In2_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Composite_In_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait In_2_20[obj[_], props, In2_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends IN2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Datom_22[obj, props, In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Aggregate22[obj, props, In2_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Composite_In_2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait In_2_21[obj[_], props, In2_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends IN2_21[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Composite_In_2_21[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait In_2_22[obj[_], props, In2_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends IN2_22[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Composite_In_2_22[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}