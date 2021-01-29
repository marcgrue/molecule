package molecule.core.boilerplate
import molecule.core.boilerplate.base._
import molecule.core.composition.Composite._
//import molecule.core.composition.Composite._
import molecule.core.composition.Tx._
import molecule.core.expression.AggregateKeywords._
import molecule.core.generic.datom._
import scala.language.higherKinds


/** Type distribution to groups of functionality for 'output'/standard molecules. */
object out {

  trait Out_00[obj[_], props, Ns0[_, _], Ns1[_, _, _], In1_0[_, _, _], In1_1[_, _, _, _]]
    extends NS00[obj, props]
//      with Datom_0[obj, props, Ns0, Ns1, In1_0, In1_1]
      with Aggregate00[obj, props, Ns0]
      with Composite00[obj, props]
      with Tx00[obj, props]

  trait Out_01[obj[_], props, Ns1[_, _, _], Ns2[_, _, _, _], In1_1[_, _, _, _], In1_2[_, _, _, _, _], A]
    extends NS01[obj, props, A]
//      with Datom_1[obj, props, Ns1, Ns2, In1_1, In1_2, A]
      with Aggregate01[obj, props, Ns1, A]
      with Composite01[obj, props, A]
      with Tx01[obj, props, A]

  trait Out_02[obj[_], props, Ns2[_, _, _, _], Ns3[_, _, _, _, _], In1_2[_, _, _, _, _], In1_3[_, _, _, _, _, _], A, B]
    extends NS02[obj, props, A, B]
//      with Datom_2[obj, props, Ns2, Ns3, In1_2, In1_3, A, B]
      with Aggregate02[obj, props, Ns2, A, B]
      with Composite02[obj, props, A, B]
      with Tx02[obj, props, A, B]

  trait Out_03[obj[_], props, Ns3[_, _, _, _, _], Ns4[_, _, _, _, _, _], In1_3[_, _, _, _, _, _], In1_4[_, _, _, _, _, _, _], A, B, C]
    extends NS03[obj, props, A, B, C]
//      with Datom_3[obj, props, Ns3, Ns4, In1_3, In1_4, A, B, C]
      with Aggregate03[obj, props, Ns3, A, B, C]
      with Composite03[obj, props, A, B, C]
      with Tx03[obj, props, A, B, C]

  trait Out_04[obj[_], props, Ns4[_, _, _, _, _, _], Ns5[_, _, _, _, _, _, _], In1_4[_, _, _, _, _, _, _], In1_5[_, _, _, _, _, _, _, _], A, B, C, D]
    extends NS04[obj, props, A, B, C, D]
//      with Datom_4[obj, props, Ns4, Ns5, In1_4, In1_5, A, B, C, D]
      with Aggregate04[obj, props, Ns4, A, B, C, D]
      with Composite04[obj, props, A, B, C, D]
      with Tx04[obj, props, A, B, C, D]

  trait Out_05[obj[_], props, Ns5[_, _, _, _, _, _, _], Ns6[_, _, _, _, _, _, _, _], In1_5[_, _, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _, _, _], A, B, C, D, E]
    extends NS05[obj, props, A, B, C, D, E]
//      with Datom_5[obj, props, Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]
      with Aggregate05[obj, props, Ns5, A, B, C, D, E]
      with Composite05[obj, props, A, B, C, D, E]
      with Tx05[obj, props, A, B, C, D, E]

  trait Out_06[obj[_], props, Ns6[_, _, _, _, _, _, _, _], Ns7[_, _, _, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F]
    extends NS06[obj, props, A, B, C, D, E, F]
//      with Datom_6[obj, props, Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]
      with Aggregate06[obj, props, Ns6, A, B, C, D, E, F]
      with Composite06[obj, props, A, B, C, D, E, F]
      with Tx06[obj, props, A, B, C, D, E, F]

  trait Out_07[obj[_], props, Ns7[_, _, _, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G]
    extends NS07[obj, props, A, B, C, D, E, F, G]
//      with Datom_7[obj, props, Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]
      with Aggregate07[obj, props, Ns7, A, B, C, D, E, F, G]
      with Composite07[obj, props, A, B, C, D, E, F, G]
      with Tx07[obj, props, A, B, C, D, E, F, G]

  trait Out_08[obj[_], props, Ns8[_, _, _, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
    extends NS08[obj, props, A, B, C, D, E, F, G, H]
//      with Datom_8[obj, props, Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]
      with Aggregate08[obj, props, Ns8, A, B, C, D, E, F, G, H]
      with Composite08[obj, props, A, B, C, D, E, F, G, H]
      with Tx08[obj, props, A, B, C, D, E, F, G, H]

  trait Out_09[obj[_], props, Ns9[_, _, _, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
    extends NS09[obj, props, A, B, C, D, E, F, G, H, I]
//      with Datom_9[obj, props, Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]
      with Aggregate09[obj, props, Ns9, A, B, C, D, E, F, G, H, I]
      with Composite09[obj, props, A, B, C, D, E, F, G, H, I]
      with Tx09[obj, props, A, B, C, D, E, F, G, H, I]

  trait Out_10[obj[_], props, Ns10[_, _, _, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
    extends NS10[obj, props, A, B, C, D, E, F, G, H, I, J]
//      with Datom_10[obj, props, Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]
      with Aggregate10[obj, props, Ns10, A, B, C, D, E, F, G, H, I, J]
      with Composite10[obj, props, A, B, C, D, E, F, G, H, I, J]
      with Tx10[obj, props, A, B, C, D, E, F, G, H, I, J]

  trait Out_11[obj[_], props, Ns11[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
    extends NS11[obj, props, A, B, C, D, E, F, G, H, I, J, K]
//      with Datom_11[obj, props, Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate11[obj, props, Ns11, A, B, C, D, E, F, G, H, I, J, K]
      with Composite11[obj, props, A, B, C, D, E, F, G, H, I, J, K]
      with Tx11[obj, props, A, B, C, D, E, F, G, H, I, J, K]

  trait Out_12[obj[_], props, Ns12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
    extends NS12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]
//      with Datom_12[obj, props, Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate12[obj, props, Ns12, A, B, C, D, E, F, G, H, I, J, K, L]
      with Composite12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]
      with Tx12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]

  trait Out_13[obj[_], props, Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends NS13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]
//      with Datom_13[obj, props, Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate13[obj, props, Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Composite13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Tx13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait Out_14[obj[_], props, Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends NS14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
//      with Datom_14[obj, props, Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate14[obj, props, Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Composite14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Tx14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait Out_15[obj[_], props, Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends NS15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
//      with Datom_15[obj, props, Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate15[obj, props, Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Composite15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Tx15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait Out_16[obj[_], props, Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends NS16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
//      with Datom_16[obj, props, Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate16[obj, props, Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Composite16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Tx16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait Out_17[obj[_], props, Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends NS17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
//      with Datom_17[obj, props, Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate17[obj, props, Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Composite17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Tx17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait Out_18[obj[_], props, Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends NS18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
//      with Datom_18[obj, props, Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate18[obj, props, Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Composite18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Tx18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait Out_19[obj[_], props, Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends NS19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
//      with Datom_19[obj, props, Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate19[obj, props, Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Composite19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Tx19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait Out_20[obj[_], props, Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends NS20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
//      with Datom_20[obj, props, Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Aggregate20[obj, props, Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Composite20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Tx20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait Out_21[obj[_], props, Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends NS21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
//      with Datom_21[obj, props, Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Aggregate21[obj, props, Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Composite21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Tx21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait Out_22[obj[_], props, Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends NS22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
//      with Datom_22[obj, props, Ns22, P24, In1_22, P25, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Composite22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Tx22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}