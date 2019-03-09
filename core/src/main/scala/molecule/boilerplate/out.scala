package molecule.boilerplate
import molecule.boilerplate.base._
import molecule.composition.CompositeInit._
import molecule.composition.Tx._
import molecule.expression.AggregateKeywords._
import molecule.meta.datom._
import scala.language.higherKinds


/** Type distribution to groups of functionality for 'output'/standard molecules. */
object out {

  trait Out_0[Ns0, Ns1[_], In1_0[_], In1_1[_, _]]
    extends NS00[Nothing]
      with Datom_0[Ns0, Ns1, In1_0, In1_1]
      with Aggregate00[Ns0]
      with CompositeInit00
      with Tx00

  trait Out_1[Ns1[_], Ns2[_, _], In1_1[_, _], In1_2[_, _, _], A]
    extends NS01[A]
      with Datom_1[Ns1, Ns2, In1_1, In1_2, A]
      with Aggregate01[Ns1, A]
      with CompositeInit01[A]
      with Tx01[A]

  trait Out_2[Ns2[_, _], Ns3[_, _, _], In1_2[_, _, _], In1_3[_, _, _, _], A, B]
    extends NS02[A, B]
      with Datom_2[Ns2, Ns3, In1_2, In1_3, A, B]
      with Aggregate02[Ns2, A, B]
      with CompositeInit02[A, B]
      with Tx02[A, B]

  trait Out_3[Ns3[_, _, _], Ns4[_, _, _, _], In1_3[_, _, _, _], In1_4[_, _, _, _, _], A, B, C]
    extends NS03[A, B, C]
      with Datom_3[Ns3, Ns4, In1_3, In1_4, A, B, C]
      with Aggregate03[Ns3, A, B, C]
      with CompositeInit03[A, B, C]
      with Tx03[A, B, C]

  trait Out_4[Ns4[_, _, _, _], Ns5[_, _, _, _, _], In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], A, B, C, D]
    extends NS04[A, B, C, D]
      with Datom_4[Ns4, Ns5, In1_4, In1_5, A, B, C, D]
      with Aggregate04[Ns4, A, B, C, D]
      with CompositeInit04[A, B, C, D]
      with Tx04[A, B, C, D]

  trait Out_5[Ns5[_, _, _, _, _], Ns6[_, _, _, _, _, _], In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], A, B, C, D, E]
    extends NS05[A, B, C, D, E]
      with Datom_5[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]
      with Aggregate05[Ns5, A, B, C, D, E]
      with CompositeInit05[A, B, C, D, E]
      with Tx05[A, B, C, D, E]

  trait Out_6[Ns6[_, _, _, _, _, _], Ns7[_, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], A, B, C, D, E, F]
    extends NS06[A, B, C, D, E, F]
      with Datom_6[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]
      with Aggregate06[Ns6, A, B, C, D, E, F]
      with CompositeInit06[A, B, C, D, E, F]
      with Tx06[A, B, C, D, E, F]

  trait Out_7[Ns7[_, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G]
    extends NS07[A, B, C, D, E, F, G]
      with Datom_7[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]
      with Aggregate07[Ns7, A, B, C, D, E, F, G]
      with CompositeInit07[A, B, C, D, E, F, G]
      with Tx07[A, B, C, D, E, F, G]

  trait Out_8[Ns8[_, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
    extends NS08[A, B, C, D, E, F, G, H]
      with Datom_8[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]
      with Aggregate08[Ns8, A, B, C, D, E, F, G, H]
      with CompositeInit08[A, B, C, D, E, F, G, H]
      with Tx08[A, B, C, D, E, F, G, H]

  trait Out_9[Ns9[_, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
    extends NS09[A, B, C, D, E, F, G, H, I]
      with Datom_9[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]
      with Aggregate09[Ns9, A, B, C, D, E, F, G, H, I]
      with CompositeInit09[A, B, C, D, E, F, G, H, I]
      with Tx09[A, B, C, D, E, F, G, H, I]

  trait Out_10[Ns10[_, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
    extends NS10[A, B, C, D, E, F, G, H, I, J]
      with Datom_10[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]
      with Aggregate10[Ns10, A, B, C, D, E, F, G, H, I, J]
      with CompositeInit10[A, B, C, D, E, F, G, H, I, J]
      with Tx10[A, B, C, D, E, F, G, H, I, J]

  trait Out_11[Ns11[_, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
    extends NS11[A, B, C, D, E, F, G, H, I, J, K]
      with Datom_11[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate11[Ns11, A, B, C, D, E, F, G, H, I, J, K]
      with CompositeInit11[A, B, C, D, E, F, G, H, I, J, K]
      with Tx11[A, B, C, D, E, F, G, H, I, J, K]

  trait Out_12[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
    extends NS12[A, B, C, D, E, F, G, H, I, J, K, L]
      with Datom_12[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate12[Ns12, A, B, C, D, E, F, G, H, I, J, K, L]
      with CompositeInit12[A, B, C, D, E, F, G, H, I, J, K, L]
      with Tx12[A, B, C, D, E, F, G, H, I, J, K, L]

  trait Out_13[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends NS13[A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Datom_13[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate13[Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with CompositeInit13[A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Tx13[A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait Out_14[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends NS14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Datom_14[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate14[Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with CompositeInit14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Tx14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait Out_15[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends NS15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Datom_15[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate15[Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with CompositeInit15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Tx15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait Out_16[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends NS16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Datom_16[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate16[Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with CompositeInit16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Tx16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait Out_17[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends NS17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Datom_17[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate17[Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with CompositeInit17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Tx17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait Out_18[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends NS18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Datom_18[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate18[Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with CompositeInit18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Tx18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait Out_19[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends NS19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Datom_19[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate19[Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with CompositeInit19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Tx19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait Out_20[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends NS20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Datom_20[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Aggregate20[Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with CompositeInit20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Tx20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait Out_21[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends NS21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Datom_21[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Aggregate21[Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with CompositeInit21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Tx21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait Out_22[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends NS22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Datom_22[Ns22, P23, In1_22, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with CompositeInit22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Tx22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}