package molecule.core._2_dsl.boilerplate
import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.composition.CompositeInit_0._
import molecule.core._2_dsl.composition.CompositeInit_1._
import molecule.core._2_dsl.composition.CompositeInit_2._
import molecule.core._2_dsl.composition.CompositeInit_3._
import molecule.core._2_dsl.composition.Tx._
import molecule.core._2_dsl.expression.AggregateKeywords._
import molecule.core._2_dsl.generic.datom._
import scala.language.higherKinds


/** Type distribution to groups of functionality. */
object api {

  trait Api_0_00[obj[_], props, Ns0[o[_], _], Ns1[o[_], _, _], In1_0[o[_], _, _], In1_1[o[_], _, _, _]]
    extends NS_0_00[obj, props]
      with Datom_0[obj, props, Ns0, Ns1, In1_0, In1_1]
      with Aggregate0[obj, props, Ns0]
      with CompositeInit_0_00[obj, props]
      with Tx00[obj, props]

  trait Api_0_01[obj[_], props, Ns1[o[_], _, _], Ns2[o[_], _, _, _], In1_1[o[_], _, _, _], In1_2[o[_], _, _, _, _], A]
    extends NS_0_01[obj, props, A]
      with Datom_1[obj, props, Ns1, Ns2, In1_1, In1_2, A]
      with Aggregate1[obj, props, Ns1, A]
      with CompositeInit_0_01[obj, props, A]
      with Tx01[obj, props, A]

  trait Api_0_02[obj[_], props, Ns2[o[_], _, _, _], Ns3[o[_], _, _, _, _], In1_2[o[_], _, _, _, _], In1_3[o[_], _, _, _, _, _], A, B]
    extends NS_0_02[obj, props, A, B]
      with Datom_2[obj, props, Ns2, Ns3, In1_2, In1_3, A, B]
      with Aggregate2[obj, props, Ns2, A, B]
      with CompositeInit_0_02[obj, props, A, B]
      with Tx02[obj, props, A, B]

  trait Api_0_03[obj[_], props, Ns3[o[_], _, _, _, _], Ns4[o[_], _, _, _, _, _], In1_3[o[_], _, _, _, _, _], In1_4[o[_], _, _, _, _, _, _], A, B, C]
    extends NS_0_03[obj, props, A, B, C]
      with Datom_3[obj, props, Ns3, Ns4, In1_3, In1_4, A, B, C]
      with Aggregate3[obj, props, Ns3, A, B, C]
      with CompositeInit_0_03[obj, props, A, B, C]
      with Tx03[obj, props, A, B, C]

  trait Api_0_04[obj[_], props, Ns4[o[_], _, _, _, _, _], Ns5[o[_], _, _, _, _, _, _], In1_4[o[_], _, _, _, _, _, _], In1_5[o[_], _, _, _, _, _, _, _], A, B, C, D]
    extends NS_0_04[obj, props, A, B, C, D]
      with Datom_4[obj, props, Ns4, Ns5, In1_4, In1_5, A, B, C, D]
      with Aggregate4[obj, props, Ns4, A, B, C, D]
      with CompositeInit_0_04[obj, props, A, B, C, D]
      with Tx04[obj, props, A, B, C, D]

  trait Api_0_05[obj[_], props, Ns5[o[_], _, _, _, _, _, _], Ns6[o[_], _, _, _, _, _, _, _], In1_5[o[_], _, _, _, _, _, _, _], In1_6[o[_], _, _, _, _, _, _, _, _], A, B, C, D, E]
    extends NS_0_05[obj, props, A, B, C, D, E]
      with Datom_5[obj, props, Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]
      with Aggregate5[obj, props, Ns5, A, B, C, D, E]
      with CompositeInit_0_05[obj, props, A, B, C, D, E]
      with Tx05[obj, props, A, B, C, D, E]

  trait Api_0_06[obj[_], props, Ns6[o[_], _, _, _, _, _, _, _], Ns7[o[_], _, _, _, _, _, _, _, _], In1_6[o[_], _, _, _, _, _, _, _, _], In1_7[o[_], _, _, _, _, _, _, _, _, _], A, B, C, D, E, F]
    extends NS_0_06[obj, props, A, B, C, D, E, F]
      with Datom_6[obj, props, Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]
      with Aggregate6[obj, props, Ns6, A, B, C, D, E, F]
      with CompositeInit_0_06[obj, props, A, B, C, D, E, F]
      with Tx06[obj, props, A, B, C, D, E, F]

  trait Api_0_07[obj[_], props, Ns7[o[_], _, _, _, _, _, _, _, _], Ns8[o[_], _, _, _, _, _, _, _, _, _], In1_7[o[_], _, _, _, _, _, _, _, _, _], In1_8[o[_], _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G]
    extends NS_0_07[obj, props, A, B, C, D, E, F, G]
      with Datom_7[obj, props, Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]
      with Aggregate7[obj, props, Ns7, A, B, C, D, E, F, G]
      with CompositeInit_0_07[obj, props, A, B, C, D, E, F, G]
      with Tx07[obj, props, A, B, C, D, E, F, G]

  trait Api_0_08[obj[_], props, Ns8[o[_], _, _, _, _, _, _, _, _, _], Ns9[o[_], _, _, _, _, _, _, _, _, _, _], In1_8[o[_], _, _, _, _, _, _, _, _, _, _], In1_9[o[_], _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
    extends NS_0_08[obj, props, A, B, C, D, E, F, G, H]
      with Datom_8[obj, props, Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]
      with Aggregate8[obj, props, Ns8, A, B, C, D, E, F, G, H]
      with CompositeInit_0_08[obj, props, A, B, C, D, E, F, G, H]
      with Tx08[obj, props, A, B, C, D, E, F, G, H]

  trait Api_0_09[obj[_], props, Ns9[o[_], _, _, _, _, _, _, _, _, _, _], Ns10[o[_], _, _, _, _, _, _, _, _, _, _, _], In1_9[o[_], _, _, _, _, _, _, _, _, _, _, _], In1_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
    extends NS_0_09[obj, props, A, B, C, D, E, F, G, H, I]
      with Datom_9[obj, props, Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]
      with Aggregate9[obj, props, Ns9, A, B, C, D, E, F, G, H, I]
      with CompositeInit_0_09[obj, props, A, B, C, D, E, F, G, H, I]
      with Tx09[obj, props, A, B, C, D, E, F, G, H, I]

  trait Api_0_10[obj[_], props, Ns10[o[_], _, _, _, _, _, _, _, _, _, _, _], Ns11[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In1_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In1_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
    extends NS_0_10[obj, props, A, B, C, D, E, F, G, H, I, J]
      with Datom_10[obj, props, Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]
      with Aggregate10[obj, props, Ns10, A, B, C, D, E, F, G, H, I, J]
      with CompositeInit_0_10[obj, props, A, B, C, D, E, F, G, H, I, J]
      with Tx10[obj, props, A, B, C, D, E, F, G, H, I, J]

  trait Api_0_11[obj[_], props, Ns11[o[_], _, _, _, _, _, _, _, _, _, _, _, _], Ns12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In1_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
    extends NS_0_11[obj, props, A, B, C, D, E, F, G, H, I, J, K]
      with Datom_11[obj, props, Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]
      with Aggregate11[obj, props, Ns11, A, B, C, D, E, F, G, H, I, J, K]
      with CompositeInit_0_11[obj, props, A, B, C, D, E, F, G, H, I, J, K]
      with Tx11[obj, props, A, B, C, D, E, F, G, H, I, J, K]

  trait Api_0_12[obj[_], props, Ns12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], Ns13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
    extends NS_0_12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]
      with Datom_12[obj, props, Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]
      with Aggregate12[obj, props, Ns12, A, B, C, D, E, F, G, H, I, J, K, L]
      with CompositeInit_0_12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]
      with Tx12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]

  trait Api_0_13[obj[_], props, Ns13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends NS_0_13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Datom_13[obj, props, Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Aggregate13[obj, props, Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with CompositeInit_0_13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with Tx13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait Api_0_14[obj[_], props, Ns14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends NS_0_14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Datom_14[obj, props, Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Aggregate14[obj, props, Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with CompositeInit_0_14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with Tx14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait Api_0_15[obj[_], props, Ns15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends NS_0_15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Datom_15[obj, props, Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Aggregate15[obj, props, Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with CompositeInit_0_15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with Tx15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait Api_0_16[obj[_], props, Ns16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends NS_0_16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Datom_16[obj, props, Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Aggregate16[obj, props, Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with CompositeInit_0_16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with Tx16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait Api_0_17[obj[_], props, Ns17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends NS_0_17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Datom_17[obj, props, Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Aggregate17[obj, props, Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with CompositeInit_0_17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with Tx17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait Api_0_18[obj[_], props, Ns18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends NS_0_18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Datom_18[obj, props, Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Aggregate18[obj, props, Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with CompositeInit_0_18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with Tx18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait Api_0_19[obj[_], props, Ns19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends NS_0_19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Datom_19[obj, props, Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Aggregate19[obj, props, Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with CompositeInit_0_19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with Tx19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait Api_0_20[obj[_], props, Ns20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends NS_0_20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Datom_20[obj, props, Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Aggregate20[obj, props, Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with CompositeInit_0_20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with Tx20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait Api_0_21[obj[_], props, Ns21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends NS_0_21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Datom_21[obj, props, Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Aggregate21[obj, props, Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with CompositeInit_0_21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with Tx21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait Api_0_22[obj[_], props, Ns22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends NS_0_22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Datom_22[obj, props, Ns22, P24, In1_22, P25, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with CompositeInit_0_22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      with Tx22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]


  

    // For starting a molecule build with a tacit attribute
    trait Api_1_00[obj[_], props, In1_0[o[_], _, _], In1_1[o[_], _, _, _], In2_0[o[_], _, _, _], In2_1[o[_], _, _, _, _], I1]
      extends NS_1_00[obj, props, I1]
        with Datom_1[obj, props, In1_0, In1_1, In2_0, In2_1, I1]
        with CompositeInit_1_00[obj, props, I1] {

      // Build on from entity id
      def apply(e: Long): In1_0[obj, props, I1] = ???
    }

    trait Api_1_01[obj[_], props, In1_1[o[_], _, _, _], In1_2[o[_], _, _, _, _], In2_1[o[_], _, _, _, _], In2_2[o[_], _, _, _, _, _], I1, A]
      extends NS_1_01[obj, props, I1, A]
        with Datom_2[obj, props, In1_1, In1_2, In2_1, In2_2, I1, A]
        with Aggregate2[obj, props, In1_1, I1, A]
        with CompositeInit_1_01[obj, props, I1, A]


    trait Api_1_02[obj[_], props, In1_2[o[_], _, _, _, _], In1_3[o[_], _, _, _, _, _], In2_2[o[_], _, _, _, _, _], In2_3[o[_], _, _, _, _, _, _], I1, A, B]
      extends NS_1_02[obj, props, I1, A, B]
        with Datom_3[obj, props, In1_2, In1_3, In2_2, In2_3, I1, A, B]
        with Aggregate3[obj, props, In1_2, I1, A, B]
        with CompositeInit_1_02[obj, props, I1, A, B]

    trait Api_1_03[obj[_], props, In1_3[o[_], _, _, _, _, _], In1_4[o[_], _, _, _, _, _, _], In2_3[o[_], _, _, _, _, _, _], In2_4[o[_], _, _, _, _, _, _, _], I1, A, B, C]
      extends NS_1_03[obj, props, I1, A, B, C]
        with Datom_4[obj, props, In1_3, In1_4, In2_3, In2_4, I1, A, B, C]
        with Aggregate4[obj, props, In1_3, I1, A, B, C]
        with CompositeInit_1_03[obj, props, I1, A, B, C]

    trait Api_1_04[obj[_], props, In1_4[o[_], _, _, _, _, _, _], In1_5[o[_], _, _, _, _, _, _, _], In2_4[o[_], _, _, _, _, _, _, _], In2_5[o[_], _, _, _, _, _, _, _, _], I1, A, B, C, D]
      extends NS_1_04[obj, props, I1, A, B, C, D]
        with Datom_5[obj, props, In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]
        with Aggregate5[obj, props, In1_4, I1, A, B, C, D]
        with CompositeInit_1_04[obj, props, I1, A, B, C, D]

    trait Api_1_05[obj[_], props, In1_5[o[_], _, _, _, _, _, _, _], In1_6[o[_], _, _, _, _, _, _, _, _], In2_5[o[_], _, _, _, _, _, _, _, _], In2_6[o[_], _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E]
      extends NS_1_05[obj, props, I1, A, B, C, D, E]
        with Datom_6[obj, props, In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]
        with Aggregate6[obj, props, In1_5, I1, A, B, C, D, E]
        with CompositeInit_1_05[obj, props, I1, A, B, C, D, E]

    trait Api_1_06[obj[_], props, In1_6[o[_], _, _, _, _, _, _, _, _], In1_7[o[_], _, _, _, _, _, _, _, _, _], In2_6[o[_], _, _, _, _, _, _, _, _, _], In2_7[o[_], _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F]
      extends NS_1_06[obj, props, I1, A, B, C, D, E, F]
        with Datom_7[obj, props, In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]
        with Aggregate7[obj, props, In1_6, I1, A, B, C, D, E, F]
        with CompositeInit_1_06[obj, props, I1, A, B, C, D, E, F]

    trait Api_1_07[obj[_], props, In1_7[o[_], _, _, _, _, _, _, _, _, _], In1_8[o[_], _, _, _, _, _, _, _, _, _, _], In2_7[o[_], _, _, _, _, _, _, _, _, _, _], In2_8[o[_], _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G]
      extends NS_1_07[obj, props, I1, A, B, C, D, E, F, G]
        with Datom_8[obj, props, In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]
        with Aggregate8[obj, props, In1_7, I1, A, B, C, D, E, F, G]
        with CompositeInit_1_07[obj, props, I1, A, B, C, D, E, F, G]

    trait Api_1_08[obj[_], props, In1_8[o[_], _, _, _, _, _, _, _, _, _, _], In1_9[o[_], _, _, _, _, _, _, _, _, _, _, _], In2_8[o[_], _, _, _, _, _, _, _, _, _, _, _], In2_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H]
      extends NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]
        with Datom_9[obj, props, In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]
        with Aggregate9[obj, props, In1_8, I1, A, B, C, D, E, F, G, H]
        with CompositeInit_1_08[obj, props, I1, A, B, C, D, E, F, G, H]

    trait Api_1_09[obj[_], props, In1_9[o[_], _, _, _, _, _, _, _, _, _, _, _], In1_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In2_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In2_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I]
      extends NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]
        with Datom_10[obj, props, In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]
        with Aggregate10[obj, props, In1_9, I1, A, B, C, D, E, F, G, H, I]
        with CompositeInit_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]

    trait Api_1_10[obj[_], props, In1_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In1_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In2_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J]
      extends NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]
        with Datom_11[obj, props, In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]
        with Aggregate11[obj, props, In1_10, I1, A, B, C, D, E, F, G, H, I, J]
        with CompositeInit_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]

    trait Api_1_11[obj[_], props, In1_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K]
      extends NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]
        with Datom_12[obj, props, In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]
        with Aggregate12[obj, props, In1_11, I1, A, B, C, D, E, F, G, H, I, J, K]
        with CompositeInit_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]

    trait Api_1_12[obj[_], props, In1_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L]
      extends NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
        with Datom_13[obj, props, In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]
        with Aggregate13[obj, props, In1_12, I1, A, B, C, D, E, F, G, H, I, J, K, L]
        with CompositeInit_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]

    trait Api_1_13[obj[_], props, In1_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
      extends NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with Datom_14[obj, props, In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with Aggregate14[obj, props, In1_13, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with CompositeInit_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]

    trait Api_1_14[obj[_], props, In1_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      extends NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with Datom_15[obj, props, In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with Aggregate15[obj, props, In1_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with CompositeInit_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    trait Api_1_15[obj[_], props, In1_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      extends NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with Datom_16[obj, props, In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with Aggregate16[obj, props, In1_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with CompositeInit_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    trait Api_1_16[obj[_], props, In1_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      extends NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with Datom_17[obj, props, In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with Aggregate17[obj, props, In1_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with CompositeInit_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    trait Api_1_17[obj[_], props, In1_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      extends NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with Datom_18[obj, props, In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with Aggregate18[obj, props, In1_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with CompositeInit_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    trait Api_1_18[obj[_], props, In1_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      extends NS_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with Datom_19[obj, props, In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with Aggregate19[obj, props, In1_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with CompositeInit_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    trait Api_1_19[obj[_], props, In1_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      extends NS_1_19[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with Datom_20[obj, props, In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with Aggregate20[obj, props, In1_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with CompositeInit_1_19[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    trait Api_1_20[obj[_], props, In1_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      extends NS_1_20[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with Datom_21[obj, props, In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with Aggregate21[obj, props, In1_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with CompositeInit_1_20[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    trait Api_1_21[obj[_], props, In1_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      extends NS_1_21[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
        with Datom_22[obj, props, In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
        with Aggregate22[obj, props, In1_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
        with CompositeInit_1_21[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    trait Api_1_22[obj[_], props, In1_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      extends NS_1_22[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
        with CompositeInit_1_22[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]



    trait Api_2_00[obj[_], props, In2_0[o[_], _, _, _], In2_1[o[_], _, _, _, _], In3_0[o[_], _, _, _, _], In3_1[o[_], _, _, _, _, _], I1, I2]
      extends NS_2_00[obj, props, I1, I2]
        with Datom_2[obj, props, In2_0, In2_1, In3_0, In3_1, I1, I2]
        with CompositeInit_2_00[obj, props, I1, I2] {

      // Build on from entity id
      def apply(e: Long): In2_0[obj, props, I1, I2] = ???
    }

    trait Api_2_01[obj[_], props, In2_1[o[_], _, _, _, _], In2_2[o[_], _, _, _, _, _], In3_1[o[_], _, _, _, _, _], In3_2[o[_], _, _, _, _, _, _], I1, I2, A]
      extends NS_2_01[obj, props, I1, I2, A]
        with Datom_3[obj, props, In2_1, In2_2, In3_1, In3_2, I1, I2, A]
        with Aggregate3[obj, props, In2_1, I1, I2, A]
        with CompositeInit_2_01[obj, props, I1, I2, A]

    trait Api_2_02[obj[_], props, In2_2[o[_], _, _, _, _, _], In2_3[o[_], _, _, _, _, _, _], In3_2[o[_], _, _, _, _, _, _], In3_3[o[_], _, _, _, _, _, _, _], I1, I2, A, B]
      extends NS_2_02[obj, props, I1, I2, A, B]
        with Datom_4[obj, props, In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]
        with Aggregate4[obj, props, In2_2, I1, I2, A, B]
        with CompositeInit_2_02[obj, props, I1, I2, A, B]

    trait Api_2_03[obj[_], props, In2_3[o[_], _, _, _, _, _, _], In2_4[o[_], _, _, _, _, _, _, _], In3_3[o[_], _, _, _, _, _, _, _], In3_4[o[_], _, _, _, _, _, _, _, _], I1, I2, A, B, C]
      extends NS_2_03[obj, props, I1, I2, A, B, C]
        with Datom_5[obj, props, In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]
        with Aggregate5[obj, props, In2_3, I1, I2, A, B, C]
        with CompositeInit_2_03[obj, props, I1, I2, A, B, C]

    trait Api_2_04[obj[_], props, In2_4[o[_], _, _, _, _, _, _, _], In2_5[o[_], _, _, _, _, _, _, _, _], In3_4[o[_], _, _, _, _, _, _, _, _], In3_5[o[_], _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D]
      extends NS_2_04[obj, props, I1, I2, A, B, C, D]
        with Datom_6[obj, props, In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]
        with Aggregate6[obj, props, In2_4, I1, I2, A, B, C, D]
        with CompositeInit_2_04[obj, props, I1, I2, A, B, C, D]

    trait Api_2_05[obj[_], props, In2_5[o[_], _, _, _, _, _, _, _, _], In2_6[o[_], _, _, _, _, _, _, _, _, _], In3_5[o[_], _, _, _, _, _, _, _, _, _], In3_6[o[_], _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E]
      extends NS_2_05[obj, props, I1, I2, A, B, C, D, E]
        with Datom_7[obj, props, In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]
        with Aggregate7[obj, props, In2_5, I1, I2, A, B, C, D, E]
        with CompositeInit_2_05[obj, props, I1, I2, A, B, C, D, E]

    trait Api_2_06[obj[_], props, In2_6[o[_], _, _, _, _, _, _, _, _, _], In2_7[o[_], _, _, _, _, _, _, _, _, _, _], In3_6[o[_], _, _, _, _, _, _, _, _, _, _], In3_7[o[_], _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F]
      extends NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]
        with Datom_8[obj, props, In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]
        with Aggregate8[obj, props, In2_6, I1, I2, A, B, C, D, E, F]
        with CompositeInit_2_06[obj, props, I1, I2, A, B, C, D, E, F]

    trait Api_2_07[obj[_], props, In2_7[o[_], _, _, _, _, _, _, _, _, _, _], In2_8[o[_], _, _, _, _, _, _, _, _, _, _, _], In3_7[o[_], _, _, _, _, _, _, _, _, _, _, _], In3_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G]
      extends NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]
        with Datom_9[obj, props, In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]
        with Aggregate9[obj, props, In2_7, I1, I2, A, B, C, D, E, F, G]
        with CompositeInit_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]

    trait Api_2_08[obj[_], props, In2_8[o[_], _, _, _, _, _, _, _, _, _, _, _], In2_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In3_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In3_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H]
      extends NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]
        with Datom_10[obj, props, In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]
        with Aggregate10[obj, props, In2_8, I1, I2, A, B, C, D, E, F, G, H]
        with CompositeInit_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]

    trait Api_2_09[obj[_], props, In2_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In2_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In3_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I]
      extends NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]
        with Datom_11[obj, props, In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]
        with Aggregate11[obj, props, In2_9, I1, I2, A, B, C, D, E, F, G, H, I]
        with CompositeInit_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]

    trait Api_2_10[obj[_], props, In2_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J]
      extends NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]
        with Datom_12[obj, props, In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]
        with Aggregate12[obj, props, In2_10, I1, I2, A, B, C, D, E, F, G, H, I, J]
        with CompositeInit_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]

    trait Api_2_11[obj[_], props, In2_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K]
      extends NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
        with Datom_13[obj, props, In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
        with Aggregate13[obj, props, In2_11, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
        with CompositeInit_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]

    trait Api_2_12[obj[_], props, In2_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
      extends NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
        with Datom_14[obj, props, In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
        with Aggregate14[obj, props, In2_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
        with CompositeInit_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]

    trait Api_2_13[obj[_], props, In2_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
      extends NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with Datom_15[obj, props, In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with Aggregate15[obj, props, In2_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with CompositeInit_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]

    trait Api_2_14[obj[_], props, In2_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      extends NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with Datom_16[obj, props, In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with Aggregate16[obj, props, In2_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with CompositeInit_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    trait Api_2_15[obj[_], props, In2_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      extends NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with Datom_17[obj, props, In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with Aggregate17[obj, props, In2_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with CompositeInit_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    trait Api_2_16[obj[_], props, In2_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      extends NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with Datom_18[obj, props, In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with Aggregate18[obj, props, In2_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with CompositeInit_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    trait Api_2_17[obj[_], props, In2_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      extends NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with Datom_19[obj, props, In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with Aggregate19[obj, props, In2_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with CompositeInit_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    trait Api_2_18[obj[_], props, In2_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      extends NS_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with Datom_20[obj, props, In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with Aggregate20[obj, props, In2_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with CompositeInit_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    trait Api_2_19[obj[_], props, In2_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      extends NS_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with Datom_21[obj, props, In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with Aggregate21[obj, props, In2_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with CompositeInit_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    trait Api_2_20[obj[_], props, In2_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      extends NS_2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with Datom_22[obj, props, In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with Aggregate22[obj, props, In2_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with CompositeInit_2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    trait Api_2_21[obj[_], props, In2_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      extends NS_2_21[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
        with CompositeInit_2_21[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    trait Api_2_22[obj[_], props, In2_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      extends NS_2_22[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
        with CompositeInit_2_22[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]




    trait Api_3_00[obj[_], props, In3_0[o[_], _, _, _, _], In3_1[o[_], _, _, _, _, _], In4_0[o[_], _, _, _, _, _], In4_1[o[_], _, _, _, _, _, _], I1, I2, I3]
      extends NS_3_00[obj, props, I1, I2, I3]
        with Datom_3[obj, props, In3_0, In3_1, In4_0, In4_1, I1, I2, I3]
        with CompositeInit_3_00[obj, props, I1, I2, I3] {

      // Build on from entity id
      def apply(e: Long): In3_0[obj, props, I1, I2, I3] = ???
    }

    trait Api_3_01[obj[_], props, In3_1[o[_], _, _, _, _, _], In3_2[o[_], _, _, _, _, _, _], In4_1[o[_], _, _, _, _, _, _], In4_2[o[_], _, _, _, _, _, _, _], I1, I2, I3, A]
      extends NS_3_01[obj, props, I1, I2, I3, A]
        with Datom_4[obj, props, In3_1, In3_2, In4_1, In4_2, I1, I2, I3, A]
        with Aggregate4[obj, props, In3_1, I1, I2, I3, A]
        with CompositeInit_3_01[obj, props, I1, I2, I3, A]

    trait Api_3_02[obj[_], props, In3_2[o[_], _, _, _, _, _, _], In3_3[o[_], _, _, _, _, _, _, _], In4_2[o[_], _, _, _, _, _, _, _], In4_3[o[_], _, _, _, _, _, _, _, _], I1, I2, I3, A, B]
      extends NS_3_02[obj, props, I1, I2, I3, A, B]
        with Datom_5[obj, props, In3_2, In3_3, In4_2, In4_3, I1, I2, I3, A, B]
        with Aggregate5[obj, props, In3_2, I1, I2, I3, A, B]
        with CompositeInit_3_02[obj, props, I1, I2, I3, A, B]

    trait Api_3_03[obj[_], props, In3_3[o[_], _, _, _, _, _, _, _], In3_4[o[_], _, _, _, _, _, _, _, _], In4_3[o[_], _, _, _, _, _, _, _, _], In4_4[o[_], _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C]
      extends NS_3_03[obj, props, I1, I2, I3, A, B, C]
        with Datom_6[obj, props, In3_3, In3_4, In4_3, In4_4, I1, I2, I3, A, B, C]
        with Aggregate6[obj, props, In3_3, I1, I2, I3, A, B, C]
        with CompositeInit_3_03[obj, props, I1, I2, I3, A, B, C]

    trait Api_3_04[obj[_], props, In3_4[o[_], _, _, _, _, _, _, _, _], In3_5[o[_], _, _, _, _, _, _, _, _, _], In4_4[o[_], _, _, _, _, _, _, _, _, _], In4_5[o[_], _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D]
      extends NS_3_04[obj, props, I1, I2, I3, A, B, C, D]
        with Datom_7[obj, props, In3_4, In3_5, In4_4, In4_5, I1, I2, I3, A, B, C, D]
        with Aggregate7[obj, props, In3_4, I1, I2, I3, A, B, C, D]
        with CompositeInit_3_04[obj, props, I1, I2, I3, A, B, C, D]

    trait Api_3_05[obj[_], props, In3_5[o[_], _, _, _, _, _, _, _, _, _], In3_6[o[_], _, _, _, _, _, _, _, _, _, _], In4_5[o[_], _, _, _, _, _, _, _, _, _, _], In4_6[o[_], _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E]
      extends NS_3_05[obj, props, I1, I2, I3, A, B, C, D, E]
        with Datom_8[obj, props, In3_5, In3_6, In4_5, In4_6, I1, I2, I3, A, B, C, D, E]
        with Aggregate8[obj, props, In3_5, I1, I2, I3, A, B, C, D, E]
        with CompositeInit_3_05[obj, props, I1, I2, I3, A, B, C, D, E]

    trait Api_3_06[obj[_], props, In3_6[o[_], _, _, _, _, _, _, _, _, _, _], In3_7[o[_], _, _, _, _, _, _, _, _, _, _, _], In4_6[o[_], _, _, _, _, _, _, _, _, _, _, _], In4_7[o[_], _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F]
      extends NS_3_06[obj, props, I1, I2, I3, A, B, C, D, E, F]
        with Datom_9[obj, props, In3_6, In3_7, In4_6, In4_7, I1, I2, I3, A, B, C, D, E, F]
        with Aggregate9[obj, props, In3_6, I1, I2, I3, A, B, C, D, E, F]
        with CompositeInit_3_06[obj, props, I1, I2, I3, A, B, C, D, E, F]

    trait Api_3_07[obj[_], props, In3_7[o[_], _, _, _, _, _, _, _, _, _, _, _], In3_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In4_7[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In4_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G]
      extends NS_3_07[obj, props, I1, I2, I3, A, B, C, D, E, F, G]
        with Datom_10[obj, props, In3_7, In3_8, In4_7, In4_8, I1, I2, I3, A, B, C, D, E, F, G]
        with Aggregate10[obj, props, In3_7, I1, I2, I3, A, B, C, D, E, F, G]
        with CompositeInit_3_07[obj, props, I1, I2, I3, A, B, C, D, E, F, G]

    trait Api_3_08[obj[_], props, In3_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _], In3_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In4_8[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In4_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H]
      extends NS_3_08[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H]
        with Datom_11[obj, props, In3_8, In3_9, In4_8, In4_9, I1, I2, I3, A, B, C, D, E, F, G, H]
        with Aggregate11[obj, props, In3_8, I1, I2, I3, A, B, C, D, E, F, G, H]
        with CompositeInit_3_08[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H]

    trait Api_3_09[obj[_], props, In3_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_9[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I]
      extends NS_3_09[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I]
        with Datom_12[obj, props, In3_9, In3_10, In4_9, In4_10, I1, I2, I3, A, B, C, D, E, F, G, H, I]
        with Aggregate12[obj, props, In3_9, I1, I2, I3, A, B, C, D, E, F, G, H, I]
        with CompositeInit_3_09[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I]

    trait Api_3_10[obj[_], props, In3_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_10[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
      extends NS_3_10[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
        with Datom_13[obj, props, In3_10, In3_11, In4_10, In4_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
        with Aggregate13[obj, props, In3_10, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
        with CompositeInit_3_10[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]

    trait Api_3_11[obj[_], props, In3_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_11[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
      extends NS_3_11[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
        with Datom_14[obj, props, In3_11, In3_12, In4_11, In4_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
        with Aggregate14[obj, props, In3_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
        with CompositeInit_3_11[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]

    trait Api_3_12[obj[_], props, In3_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_12[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
      extends NS_3_12[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
        with Datom_15[obj, props, In3_12, In3_13, In4_12, In4_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
        with Aggregate15[obj, props, In3_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
        with CompositeInit_3_12[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]

    trait Api_3_13[obj[_], props, In3_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_13[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
      extends NS_3_13[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with Datom_16[obj, props, In3_13, In3_14, In4_13, In4_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with Aggregate16[obj, props, In3_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
        with CompositeInit_3_13[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]

    trait Api_3_14[obj[_], props, In3_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_14[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      extends NS_3_14[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with Datom_17[obj, props, In3_14, In3_15, In4_14, In4_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with Aggregate17[obj, props, In3_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
        with CompositeInit_3_14[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

    trait Api_3_15[obj[_], props, In3_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_15[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      extends NS_3_15[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with Datom_18[obj, props, In3_15, In3_16, In4_15, In4_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with Aggregate18[obj, props, In3_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
        with CompositeInit_3_15[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

    trait Api_3_16[obj[_], props, In3_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_16[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      extends NS_3_16[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with Datom_19[obj, props, In3_16, In3_17, In4_16, In4_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with Aggregate19[obj, props, In3_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
        with CompositeInit_3_16[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

    trait Api_3_17[obj[_], props, In3_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_17[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      extends NS_3_17[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with Datom_20[obj, props, In3_17, In3_18, In4_17, In4_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with Aggregate20[obj, props, In3_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
        with CompositeInit_3_17[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

    trait Api_3_18[obj[_], props, In3_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_18[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      extends NS_3_18[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with Datom_21[obj, props, In3_18, In3_19, In4_18, In4_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with Aggregate21[obj, props, In3_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
        with CompositeInit_3_18[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

    trait Api_3_19[obj[_], props, In3_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_19[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      extends NS_3_19[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with Datom_22[obj, props, In3_19, In3_20, In4_19, In4_20, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with Aggregate22[obj, props, In3_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
        with CompositeInit_3_19[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

    trait Api_3_20[obj[_], props, In3_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_20[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      extends NS_3_20[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
        with CompositeInit_3_20[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

    trait Api_3_21[obj[_], props, In3_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_21[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      extends NS_3_21[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
        with CompositeInit_3_21[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

    trait Api_3_22[obj[_], props, In3_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_22[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P25[o[_], _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
      extends NS_3_22[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
        with CompositeInit_3_22[obj, props, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
   
}