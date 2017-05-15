package molecule.boilerplate
import scala.language.higherKinds


trait In_2_0[In2_0[_,_], In2_1[_,_,_], In3_0[_,_,_], In3_1[_, _,_,_], I1, I2]
  extends NS2[I1, I2]
  with Generic2[In2_0, In2_1, In3_0, In3_1, I1, I2] {

  // Build on from entity id
  def apply(e: Long): In2_0[I1, I2] = ???
}

trait In_2_1[In2_1[_,_,_], In2_2[_,_,_,_], In3_1[_,_,_,_], In3_2[_,_,_,_,_], I1, I2, A]
  extends NS3[I1, I2, A]
  with Generic3[In2_1, In2_2, In3_1, In3_2, I1, I2, A]
  with Aggregate3[In2_1, I1, I2, A]

trait In_2_2[In2_2[_,_,_,_], In2_3[_,_,_,_,_], In3_2[_,_,_,_,_], In3_3[_,_,_,_,_,_], I1, I2, A, B]
  extends NS4[I1, I2, A, B]
  with Generic4[In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]
  with Aggregate4[In2_2, I1, I2, A, B]

trait In_2_3[In2_3[_,_,_,_,_], In2_4[_,_,_,_,_,_], In3_3[_,_,_,_,_,_], In3_4[_,_,_,_,_,_,_], I1, I2, A, B, C]
  extends NS5[I1, I2, A, B, C]
  with Generic5[In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]
  with Aggregate5[In2_3, I1, I2, A, B, C]

trait In_2_4[In2_4[_,_,_,_,_,_], In2_5[_,_,_,_,_,_,_], In3_4[_,_,_,_,_,_,_], In3_5[_,_,_,_,_,_,_,_], I1, I2, A, B, C, D]
  extends NS6[I1, I2, A, B, C, D]
  with Generic6[In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]
  with Aggregate6[In2_4, I1, I2, A, B, C, D]

trait In_2_5[In2_5[_,_,_,_,_,_,_], In2_6[_,_,_,_,_,_,_,_], In3_5[_,_,_,_,_,_,_,_], In3_6[_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E]
  extends NS7[I1, I2, A, B, C, D, E]
  with Generic7[In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]
  with Aggregate7[In2_5, I1, I2, A, B, C, D, E]

trait In_2_6[In2_6[_,_,_,_,_,_,_,_], In2_7[_,_,_,_,_,_,_,_,_], In3_6[_,_,_,_,_,_,_,_,_], In3_7[_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F]
  extends NS8[I1, I2, A, B, C, D, E, F]
  with Generic8[In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]
  with Aggregate8[In2_6, I1, I2, A, B, C, D, E, F]

trait In_2_7[In2_7[_,_,_,_,_,_,_,_,_], In2_8[_,_,_,_,_,_,_,_,_,_], In3_7[_,_,_,_,_,_,_,_,_,_], In3_8[_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G]
  extends NS9[I1, I2, A, B, C, D, E, F, G]
  with Generic9[In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]
  with Aggregate9[In2_7, I1, I2, A, B, C, D, E, F, G]

trait In_2_8[In2_8[_,_,_,_,_,_,_,_,_,_], In2_9[_,_,_,_,_,_,_,_,_,_,_], In3_8[_,_,_,_,_,_,_,_,_,_,_], In3_9[_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H]
  extends NS10[I1, I2, A, B, C, D, E, F, G, H]
  with Generic10[In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]
  with Aggregate10[In2_8, I1, I2, A, B, C, D, E, F, G, H]

trait In_2_9[In2_9[_,_,_,_,_,_,_,_,_,_,_], In2_10[_,_,_,_,_,_,_,_,_,_,_,_], In3_9[_,_,_,_,_,_,_,_,_,_,_,_], In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I]
  extends NS11[I1, I2, A, B, C, D, E, F, G, H, I]
  with Generic11[In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]
  with Aggregate11[In2_9, I1, I2, A, B, C, D, E, F, G, H, I]

trait In_2_10[In2_10[_,_,_,_,_,_,_,_,_,_,_,_], In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J]
  extends NS12[I1, I2, A, B, C, D, E, F, G, H, I, J]
  with Generic12[In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]
  with Aggregate12[In2_10, I1, I2, A, B, C, D, E, F, G, H, I, J]

trait In_2_11[In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  extends NS13[I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  with Generic13[In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  with Aggregate13[In2_11, I1, I2, A, B, C, D, E, F, G, H, I, J, K]

trait In_2_12[In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  extends NS14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  with Generic14[In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  with Aggregate14[In2_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]

trait In_2_13[In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  extends NS15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  with Generic15[In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  with Aggregate15[In2_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]

trait In_2_14[In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  extends NS16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  with Generic16[In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  with Aggregate16[In2_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

trait In_2_15[In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  extends NS17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  with Generic17[In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  with Aggregate17[In2_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

trait In_2_16[In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  extends NS18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  with Generic18[In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  with Aggregate18[In2_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

trait In_2_17[In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  extends NS19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  with Generic19[In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  with Aggregate19[In2_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

trait In_2_18[In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  extends NS20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  with Generic20[In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  with Aggregate20[In2_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

trait In_2_19[In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  extends NS21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  with Generic21[In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  with Aggregate21[In2_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

trait In_2_20[In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  extends NS22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  with Generic22[In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  with Aggregate22[In2_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

trait In_2_21[In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  extends NS23[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

trait In_2_22[In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  extends NS24[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
