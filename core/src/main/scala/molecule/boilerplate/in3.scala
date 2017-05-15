package molecule.boilerplate
import scala.language.higherKinds


trait In_3_0[In3_0[_,_,_], In3_1[_,_,_,_], In4_0[_,_,_,_], In4_1[_, _,_,_,_], I1, I2, I3]
  extends NS3[I1, I2, I3]
  with Generic3[In3_0, In3_1, In4_0, In4_1, I1, I2, I3] {

  // Build on from entity id
  def apply(e: Long): In3_0[I1, I2, I3] = ???
}

trait In_3_1[In3_1[_,_,_,_], In3_2[_,_,_,_,_], In4_1[_,_,_,_,_], In4_2[_,_,_,_,_,_], I1, I2, I3, A]
  extends NS4[I1, I2, I3, A]
  with Generic4[In3_1, In3_2, In4_1, In4_2, I1, I2, I3, A]
  with Aggregate4[In3_1, I1, I2, I3, A]

trait In_3_2[In3_2[_,_,_,_,_], In3_3[_,_,_,_,_,_], In4_2[_,_,_,_,_,_], In4_3[_,_,_,_,_,_,_], I1, I2, I3, A, B]
  extends NS5[I1, I2, I3, A, B]
  with Generic5[In3_2, In3_3, In4_2, In4_3, I1, I2, I3, A, B]
  with Aggregate5[In3_2, I1, I2, I3, A, B]

trait In_3_3[In3_3[_,_,_,_,_,_], In3_4[_,_,_,_,_,_,_], In4_3[_,_,_,_,_,_,_], In4_4[_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C]
  extends NS6[I1, I2, I3, A, B, C]
  with Generic6[In3_3, In3_4, In4_3, In4_4, I1, I2, I3, A, B, C]
  with Aggregate6[In3_3, I1, I2, I3, A, B, C]

trait In_3_4[In3_4[_,_,_,_,_,_,_], In3_5[_,_,_,_,_,_,_,_], In4_4[_,_,_,_,_,_,_,_], In4_5[_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D]
  extends NS7[I1, I2, I3, A, B, C, D]
  with Generic7[In3_4, In3_5, In4_4, In4_5, I1, I2, I3, A, B, C, D]
  with Aggregate7[In3_4, I1, I2, I3, A, B, C, D]

trait In_3_5[In3_5[_,_,_,_,_,_,_,_], In3_6[_,_,_,_,_,_,_,_,_], In4_5[_,_,_,_,_,_,_,_,_], In4_6[_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E]
  extends NS8[I1, I2, I3, A, B, C, D, E]
  with Generic8[In3_5, In3_6, In4_5, In4_6, I1, I2, I3, A, B, C, D, E]
  with Aggregate8[In3_5, I1, I2, I3, A, B, C, D, E]

trait In_3_6[In3_6[_,_,_,_,_,_,_,_,_], In3_7[_,_,_,_,_,_,_,_,_,_], In4_6[_,_,_,_,_,_,_,_,_,_], In4_7[_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F]
  extends NS9[I1, I2, I3, A, B, C, D, E, F]
  with Generic9[In3_6, In3_7, In4_6, In4_7, I1, I2, I3, A, B, C, D, E, F]
  with Aggregate9[In3_6, I1, I2, I3, A, B, C, D, E, F]

trait In_3_7[In3_7[_,_,_,_,_,_,_,_,_,_], In3_8[_,_,_,_,_,_,_,_,_,_,_], In4_7[_,_,_,_,_,_,_,_,_,_,_], In4_8[_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G]
  extends NS10[I1, I2, I3, A, B, C, D, E, F, G]
  with Generic10[In3_7, In3_8, In4_7, In4_8, I1, I2, I3, A, B, C, D, E, F, G]
  with Aggregate10[In3_7, I1, I2, I3, A, B, C, D, E, F, G]

trait In_3_8[In3_8[_,_,_,_,_,_,_,_,_,_,_], In3_9[_,_,_,_,_,_,_,_,_,_,_,_], In4_8[_,_,_,_,_,_,_,_,_,_,_,_], In4_9[_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H]
  extends NS11[I1, I2, I3, A, B, C, D, E, F, G, H]
  with Generic11[In3_8, In3_9, In4_8, In4_9, I1, I2, I3, A, B, C, D, E, F, G, H]
  with Aggregate11[In3_8, I1, I2, I3, A, B, C, D, E, F, G, H]

trait In_3_9[In3_9[_,_,_,_,_,_,_,_,_,_,_,_], In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], In4_9[_,_,_,_,_,_,_,_,_,_,_,_,_], In4_10[_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I]
  extends NS12[I1, I2, I3, A, B, C, D, E, F, G, H, I]
  with Generic12[In3_9, In3_10, In4_9, In4_10, I1, I2, I3, A, B, C, D, E, F, G, H, I]
  with Aggregate12[In3_9, I1, I2, I3, A, B, C, D, E, F, G, H, I]

trait In_3_10[In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_10[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
  extends NS13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
  with Generic13[In3_10, In3_11, In4_10, In4_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
  with Aggregate13[In3_10, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]

trait In_3_11[In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
  extends NS14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
  with Generic14[In3_11, In3_12, In4_11, In4_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
  with Aggregate14[In3_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]

trait In_3_12[In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
  extends NS15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
  with Generic15[In3_12, In3_13, In4_12, In4_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
  with Aggregate15[In3_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]

trait In_3_13[In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
  extends NS16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
  with Generic16[In3_13, In3_14, In4_13, In4_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
  with Aggregate16[In3_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]

trait In_3_14[In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  extends NS17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  with Generic17[In3_14, In3_15, In4_14, In4_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  with Aggregate17[In3_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

trait In_3_15[In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  extends NS18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  with Generic18[In3_15, In3_16, In4_15, In4_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  with Aggregate18[In3_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

trait In_3_16[In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  extends NS19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  with Generic19[In3_16, In3_17, In4_16, In4_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  with Aggregate19[In3_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

trait In_3_17[In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  extends NS20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  with Generic20[In3_17, In3_18, In4_17, In4_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  with Aggregate20[In3_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

trait In_3_18[In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  extends NS21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  with Generic21[In3_18, In3_19, In4_18, In4_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  with Aggregate21[In3_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

trait In_3_19[In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  extends NS22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  with Generic22[In3_19, In3_20, In4_19, In4_20, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  with Aggregate22[In3_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

trait In_3_20[In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  extends NS23[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

trait In_3_21[In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  extends NS24[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

trait In_3_22[In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  extends NS25[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
