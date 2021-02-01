package molecule.core._2_dsl.boilerplate

import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.expression.AggregateKeywordsSchema._
import scala.language.higherKinds


/** Type distribution to groups of functionality for 'output'/standard molecules. */
object outSchema {

  trait OutSchema_0[obj[_], props, Ns0[_, _]]
    extends NS_0_00[obj, props]
      with AggregateSchema00[obj, props, Ns0]

  trait OutSchema_1[obj[_], props, Ns1[_,_,_], A]
    extends NS_0_01[obj, props, A]
      with AggregateSchema01[obj, props, Ns1, A]
//      with AggregateSchema01x[obj, props, A]

  trait OutSchema_2[obj[_], props, Ns2[_, _, _, _], A, B]
    extends NS_0_02[obj, props, A, B]
      with AggregateSchema02[obj, props, Ns2, A, B]

  trait OutSchema_3[obj[_], props, Ns3[_, _, _, _, _], A, B, C]
    extends NS_0_03[obj, props, A, B, C]
      with AggregateSchema03[obj, props, Ns3, A, B, C]

  trait OutSchema_4[obj[_], props, Ns4[_, _, _, _, _, _], A, B, C, D]
    extends NS_0_04[obj, props, A, B, C, D]
      with AggregateSchema04[obj, props, Ns4, A, B, C, D]

  trait OutSchema_5[obj[_], props, Ns5[_, _, _, _, _, _, _], A, B, C, D, E]
    extends NS_0_05[obj, props, A, B, C, D, E]
      with AggregateSchema05[obj, props, Ns5, A, B, C, D, E]

  trait OutSchema_6[obj[_], props, Ns6[_, _, _, _, _, _, _, _], A, B, C, D, E, F]
    extends NS_0_06[obj, props, A, B, C, D, E, F]
      with AggregateSchema06[obj, props, Ns6, A, B, C, D, E, F]

  trait OutSchema_7[obj[_], props, Ns7[_, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G]
    extends NS_0_07[obj, props, A, B, C, D, E, F, G]
      with AggregateSchema07[obj, props, Ns7, A, B, C, D, E, F, G]

  trait OutSchema_8[obj[_], props, Ns8[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
    extends NS_0_08[obj, props, A, B, C, D, E, F, G, H]
      with AggregateSchema08[obj, props, Ns8, A, B, C, D, E, F, G, H]

  trait OutSchema_9[obj[_], props, Ns9[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
    extends NS_0_09[obj, props, A, B, C, D, E, F, G, H, I]
      with AggregateSchema09[obj, props, Ns9, A, B, C, D, E, F, G, H, I]

  trait OutSchema_10[obj[_], props, Ns10[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
    extends NS_0_10[obj, props, A, B, C, D, E, F, G, H, I, J]
      with AggregateSchema10[obj, props, Ns10, A, B, C, D, E, F, G, H, I, J]

  trait OutSchema_11[obj[_], props, Ns11[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
    extends NS_0_11[obj, props, A, B, C, D, E, F, G, H, I, J, K]
      with AggregateSchema11[obj, props, Ns11, A, B, C, D, E, F, G, H, I, J, K]

  trait OutSchema_12[obj[_], props, Ns12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
    extends NS_0_12[obj, props, A, B, C, D, E, F, G, H, I, J, K, L]
      with AggregateSchema12[obj, props, Ns12, A, B, C, D, E, F, G, H, I, J, K, L]

  trait OutSchema_13[obj[_], props, Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends NS_0_13[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with AggregateSchema13[obj, props, Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait OutSchema_14[obj[_], props, Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends NS_0_14[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with AggregateSchema14[obj, props, Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait OutSchema_15[obj[_], props, Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends NS_0_15[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with AggregateSchema15[obj, props, Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait OutSchema_16[obj[_], props, Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends NS_0_16[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with AggregateSchema16[obj, props, Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait OutSchema_17[obj[_], props, Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends NS_0_17[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with AggregateSchema17[obj, props, Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait OutSchema_18[obj[_], props, Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends NS_0_18[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with AggregateSchema18[obj, props, Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait OutSchema_19[obj[_], props, Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends NS_0_19[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with AggregateSchema19[obj, props, Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait OutSchema_20[obj[_], props, Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends NS_0_20[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with AggregateSchema20[obj, props, Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait OutSchema_21[obj[_], props, Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends NS_0_21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with AggregateSchema21[obj, props, Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait OutSchema_22[obj[_], props, Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends NS_0_22[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}