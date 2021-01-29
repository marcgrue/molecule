package molecule.core.boilerplate

import molecule.core.boilerplate.base._
import molecule.core.composition.Composite_In_3._
import molecule.core.expression.AggregateKeywords._
import molecule.core.generic.datom._
import scala.language.higherKinds

/** Type distribution to groups of functionality for input molecules expecting 3 inputs. */
object in3 {

  
  trait In_3_0[obj[_], props, In3_0[_, _, _, _, _], In3_1[_, _, _, _, _, _], In4_0[_, _, _, _, _, _], In4_1[_, _, _, _, _, _, _], I1, I2, I3]
    extends IN3_00[obj, props, I1, I2, I3]
//      with Datom_3[obj, props, In3_0, In3_1, In4_0, In4_1, I1, I2, I3]
      with Composite_In_3_00[obj, props, I1, I2, I3] {

    // Build on from entity id
    def apply(e: Long): In3_0[obj[_], props, I1, I2, I3] = ???
  }

  trait In_3_1[obj[_], props, In3_1[_, _, _, _, _, _], In3_2[_, _, _, _, _, _, _], In4_1[_, _, _, _, _, _, _], In4_2[_, _, _, _, _, _, _, _], I1, I2, I3, A]
    extends IN3_01[obj, props, I1, I2, I3, A]
//      with Datom_4[obj, props, In3_1, In3_2, In4_1, In4_2, I1, I2, I3, A]
      with Aggregate04[obj, props, In3_1, I1, I2, I3, A]
      with Composite_In_3_01[obj, props, I1, I2, I3, A]

  trait In_3_2[obj[_], props, In3_2[_, _, _, _, _, _, _], In3_3[_, _, _, _, _, _, _, _], In4_2[_, _, _, _, _, _, _, _], In4_3[_, _, _, _, _, _, _, _, _], I1, I2, I3, A, B]
    extends IN3_02[obj, props, I1, I2, I3, A, B]
//      with Datom_5[obj, props, In3_2, In3_3, In4_2, In4_3, I1, I2, I3, A, B]
      with Aggregate05[obj, props, In3_2, I1, I2, I3, A, B]
      with Composite_In_3_02[obj, props, I1, I2, I3, A, B]

  trait In_3_3[obj[_], props, In3_3[_, _, _, _, _, _, _, _], In3_4[_, _, _, _, _, _, _, _, _], In4_3[_, _, _, _, _, _, _, _, _], In4_4[_, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C]
    extends IN3_03[obj, props, I1, I2, I3, A, B, C]
//      with Datom_6[obj, props, In3_3, In3_4, In4_3, In4_4, I1, I2, I3, A, B, C]
      with Aggregate06[obj, props, In3_3, I1, I2, I3, A, B, C]
      with Composite_In_3_03[obj, props, I1, I2, I3, A, B, C]

  trait In_3_4[obj[_], props, In3_4[_, _, _, _, _, _, _, _, _], In3_5[_, _, _, _, _, _, _, _, _, _], In4_4[_, _, _, _, _, _, _, _, _, _], In4_5[_, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D]
    extends IN3_04[obj, props, I1, I2, I3, A, B, C, D]
//      with Datom_7[obj, props, In3_4, In3_5, In4_4, In4_5, I1, I2, I3, A, B, C, D]
      with Aggregate07[obj, props, In3_4, I1, I2, I3, A, B, C, D]
      with Composite_In_3_04[obj, props, I1, I2, I3, A, B, C, D]

  trait In_3_5[obj[_], props, In3_5[_, _, _, _, _, _, _, _, _, _], In3_6[_, _, _, _, _, _, _, _, _, _, _], In4_5[_, _, _, _, _, _, _, _, _, _, _], In4_6[_, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E]
    extends IN3_05[obj, props, I1, I2, I3, A, B, C, D, E]
//      with Datom_8[obj, props, In3_5, In3_6, In4_5, In4_6, I1, I2, I3, A, B, C, D, E]
      with Aggregate08[obj, props, In3_5, I1, I2, I3, A, B, C, D, E]
      with Composite_In_3_05[obj, props, I1, I2, I3, A, B, C, D, E]

  trait In_3_6[obj[_], props, In3_6[_, _, _, _, _, _, _, _, _, _, _], In3_7[_, _, _, _, _, _, _, _, _, _, _, _], In4_6[_, _, _, _, _, _, _, _, _, _, _, _], In4_7[_, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F]
    extends IN3_06[obj, props, I1, I2, I3, A, B, C, D, E, F]
//      with Datom_9[obj, props, In3_6, In3_7, In4_6, In4_7, I1, I2, I3, A, B, C, D, E, F]
      with Aggregate09[obj, props, In3_6, I1, I2, I3, A, B, C, D, E, F]
      with Composite_In_3_06[obj, props, I1, I2, I3, A, B, C, D, E, F]

  trait In_3_7[obj[_], props, In3_7[_, _, _, _, _, _, _, _, _, _, _, _], In3_8[_, _, _, _, _, _, _, _, _, _, _, _, _], In4_7[_, _, _, _, _, _, _, _, _, _, _, _, _], In4_8[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G]
    extends IN3_07[obj, props, I1, I2, I3, A, B, C, D, E, F, G]
//      with Datom_10[obj, props, In3_7, In3_8, In4_7, In4_8, I1, I2, I3, A, B, C, D, E, F, G]
      with Aggregate10[obj, props, In3_7, I1, I2, I3, A, B, C, D, E, F, G]
      with Composite_In_3_07[obj, props, I1, I2, I3, A, B, C, D, E, F, G]

  trait In_3_8[obj[_], props, In3_8[_, _, _, _, _, _, _, _, _, _, _, _, _], In3_9[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_8[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_9[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H]
    extends IN3_08[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H]
//      with Datom_11[obj, props, In3_8, In3_9, In4_8, In4_9, I1, I2, I3, A, B, C, D, E, F, G, H]
      with Aggregate11[obj, props, In3_8, I1, I2, I3, A, B, C, D, E, F, G, H]
      with Composite_In_3_08[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H]

  trait In_3_9[obj[_], props, In3_9[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_9[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_10[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I]
    extends IN3_09[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I]
//      with Datom_12[obj, props, In3_9, In3_10, In4_9, In4_10, I1, I2, I3, A, B, C, D, E, F, G, H, I]
      with Aggregate12[obj, props, In3_9, I1, I2, I3, A, B, C, D, E, F, G, H, I]
      with Composite_In_3_09[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I]

  trait In_3_10[obj[_], props, In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_10[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
    extends IN3_10[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
//      with Datom_13[obj, props, In3_10, In3_11, In4_10, In4_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
      with Aggregate13[obj, props, In3_10, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
      with Composite_In_3_10[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]

  trait In_3_11[obj[_], props, In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
    extends IN3_11[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
//      with Datom_14[obj, props, In3_11, In3_12, In4_11, In4_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate14[obj, props, In3_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
      with Composite_In_3_11[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]

  trait In_3_12[obj[_], props, In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
    extends IN3_12[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
//      with Datom_15[obj, props, In3_12, In3_13, In4_12, In4_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate15[obj, props, In3_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
      with Composite_In_3_12[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]

  trait In_3_13[obj[_], props, In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends IN3_13[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
//      with Datom_16[obj, props, In3_13, In3_14, In4_13, In4_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate16[obj, props, In3_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Composite_In_3_13[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait In_3_14[obj[_], props, In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends IN3_14[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
//      with Datom_17[obj, props, In3_14, In3_15, In4_14, In4_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate17[obj, props, In3_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Composite_In_3_14[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait In_3_15[obj[_], props, In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends IN3_15[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
//      with Datom_18[obj, props, In3_15, In3_16, In4_15, In4_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate18[obj, props, In3_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Composite_In_3_15[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait In_3_16[obj[_], props, In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends IN3_16[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
//      with Datom_19[obj, props, In3_16, In3_17, In4_16, In4_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate19[obj, props, In3_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Composite_In_3_16[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait In_3_17[obj[_], props, In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends IN3_17[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
//      with Datom_20[obj, props, In3_17, In3_18, In4_17, In4_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate20[obj, props, In3_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Composite_In_3_17[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait In_3_18[obj[_], props, In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends IN3_18[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
//      with Datom_21[obj, props, In3_18, In3_19, In4_18, In4_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate21[obj, props, In3_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Composite_In_3_18[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait In_3_19[obj[_], props, In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends IN3_19[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
//      with Datom_22[obj, props, In3_19, In3_20, In4_19, In4_20, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate22[obj, props, In3_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Composite_In_3_19[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait In_3_20[obj[_], props, In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends IN3_20[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Composite_In_3_20[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait In_3_21[obj[_], props, In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends IN3_21[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Composite_In_3_21[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait In_3_22[obj[_], props, In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends IN3_22[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Composite_In_3_22[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}