package molecule.boilerplate
import scala.language.higherKinds


trait In_1_0[In1_0[_], In1_1[_,_], In2_0[_,_], In2_1[_, _,_], I1]
  extends NS1[I1]
  with Generic1[In1_0, In1_1, In2_0, In2_1, I1] {

  // Build on from entity id
  def apply(e: Long): In1_0[I1] = ???
}

trait In_1_1[In1_1[_,_], In1_2[_,_,_], In2_1[_,_,_], In2_2[_,_,_,_], I1, A]
  extends NS2[I1, A]
  with Generic2[In1_1, In1_2, In2_1, In2_2, I1, A]
  with Aggregate2[In1_1, I1, A]

trait In_1_2[In1_2[_,_,_], In1_3[_,_,_,_], In2_2[_,_,_,_], In2_3[_,_,_,_,_], I1, A, B]
  extends NS3[I1, A, B]
  with Generic3[In1_2, In1_3, In2_2, In2_3, I1, A, B]
  with Aggregate3[In1_2, I1, A, B]

trait In_1_3[In1_3[_,_,_,_], In1_4[_,_,_,_,_], In2_3[_,_,_,_,_], In2_4[_,_,_,_,_,_], I1, A, B, C]
  extends NS4[I1, A, B, C]
  with Generic4[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]
  with Aggregate4[In1_3, I1, A, B, C]

trait In_1_4[In1_4[_,_,_,_,_], In1_5[_,_,_,_,_,_], In2_4[_,_,_,_,_,_], In2_5[_,_,_,_,_,_,_], I1, A, B, C, D]
  extends NS5[I1, A, B, C, D]
  with Generic5[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]
  with Aggregate5[In1_4, I1, A, B, C, D]

trait In_1_5[In1_5[_,_,_,_,_,_], In1_6[_,_,_,_,_,_,_], In2_5[_,_,_,_,_,_,_], In2_6[_,_,_,_,_,_,_,_], I1, A, B, C, D, E]
  extends NS6[I1, A, B, C, D, E]
  with Generic6[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]
  with Aggregate6[In1_5, I1, A, B, C, D, E]

trait In_1_6[In1_6[_,_,_,_,_,_,_], In1_7[_,_,_,_,_,_,_,_], In2_6[_,_,_,_,_,_,_,_], In2_7[_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F]
  extends NS7[I1, A, B, C, D, E, F]
  with Generic7[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]
  with Aggregate7[In1_6, I1, A, B, C, D, E, F]

trait In_1_7[In1_7[_,_,_,_,_,_,_,_], In1_8[_,_,_,_,_,_,_,_,_], In2_7[_,_,_,_,_,_,_,_,_], In2_8[_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G]
  extends NS8[I1, A, B, C, D, E, F, G]
  with Generic8[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]
  with Aggregate8[In1_7, I1, A, B, C, D, E, F, G]

trait In_1_8[In1_8[_,_,_,_,_,_,_,_,_], In1_9[_,_,_,_,_,_,_,_,_,_], In2_8[_,_,_,_,_,_,_,_,_,_], In2_9[_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H]
  extends NS9[I1, A, B, C, D, E, F, G, H]
  with Generic9[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]
  with Aggregate9[In1_8, I1, A, B, C, D, E, F, G, H]

trait In_1_9[In1_9[_,_,_,_,_,_,_,_,_,_], In1_10[_,_,_,_,_,_,_,_,_,_,_], In2_9[_,_,_,_,_,_,_,_,_,_,_], In2_10[_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I]
  extends NS10[I1, A, B, C, D, E, F, G, H, I]
  with Generic10[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]
  with Aggregate10[In1_9, I1, A, B, C, D, E, F, G, H, I]

trait In_1_10[In1_10[_,_,_,_,_,_,_,_,_,_,_], In1_11[_,_,_,_,_,_,_,_,_,_,_,_], In2_10[_,_,_,_,_,_,_,_,_,_,_,_], In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J]
  extends NS11[I1, A, B, C, D, E, F, G, H, I, J]
  with Generic11[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]
  with Aggregate11[In1_10, I1, A, B, C, D, E, F, G, H, I, J]

trait In_1_11[In1_11[_,_,_,_,_,_,_,_,_,_,_,_], In1_12[_,_,_,_,_,_,_,_,_,_,_,_,_], In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K]
  extends NS12[I1, A, B, C, D, E, F, G, H, I, J, K]
  with Generic12[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]
  with Aggregate12[In1_11, I1, A, B, C, D, E, F, G, H, I, J, K]

trait In_1_12[In1_12[_,_,_,_,_,_,_,_,_,_,_,_,_], In1_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L]
  extends NS13[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  with Generic13[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  with Aggregate13[In1_12, I1, A, B, C, D, E, F, G, H, I, J, K, L]

trait In_1_13[In1_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  extends NS14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  with Generic14[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  with Aggregate14[In1_13, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]

trait In_1_14[In1_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  extends NS15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  with Generic15[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  with Aggregate15[In1_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

trait In_1_15[In1_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  extends NS16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  with Generic16[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  with Aggregate16[In1_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

trait In_1_16[In1_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  extends NS17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  with Generic17[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  with Aggregate17[In1_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

trait In_1_17[In1_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  extends NS18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  with Generic18[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  with Aggregate18[In1_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

trait In_1_18[In1_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  extends NS19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  with Generic19[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  with Aggregate19[In1_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

trait In_1_19[In1_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  extends NS20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  with Generic20[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  with Aggregate20[In1_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

trait In_1_20[In1_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  extends NS21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  with Generic21[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  with Aggregate21[In1_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

trait In_1_21[In1_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  extends NS22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  with Aggregate22[In1_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  with Generic22[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

trait In_1_22[In1_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  extends NS23[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
