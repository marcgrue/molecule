package molecule.core.boilerplate

import molecule.core.boilerplate.base._
import molecule.core.expression.AggregateKeywordsSchema._
import scala.language.higherKinds


/** Type distribution to groups of functionality for 'output'/standard molecules. */
object outSchema {

  trait OutSchema_0[Ns0]
    extends NS00[Nothing]
      with AggregateSchema00[Ns0]

  trait OutSchema_1[Ns1[_], A]
    extends NS01[A]
      with AggregateSchema01[Ns1, A]

  trait OutSchema_2[Ns2[_, _], A, B]
    extends NS02[A, B]
      with AggregateSchema02[Ns2, A, B]

  trait OutSchema_3[Ns3[_, _, _], A, B, C]
    extends NS03[A, B, C]
      with AggregateSchema03[Ns3, A, B, C]

  trait OutSchema_4[Ns4[_, _, _, _], A, B, C, D]
    extends NS04[A, B, C, D]
      with AggregateSchema04[Ns4, A, B, C, D]

  trait OutSchema_5[Ns5[_, _, _, _, _], A, B, C, D, E]
    extends NS05[A, B, C, D, E]
      with AggregateSchema05[Ns5, A, B, C, D, E]

  trait OutSchema_6[Ns6[_, _, _, _, _, _], A, B, C, D, E, F]
    extends NS06[A, B, C, D, E, F]
      with AggregateSchema06[Ns6, A, B, C, D, E, F]

  trait OutSchema_7[Ns7[_, _, _, _, _, _, _], A, B, C, D, E, F, G]
    extends NS07[A, B, C, D, E, F, G]
      with AggregateSchema07[Ns7, A, B, C, D, E, F, G]

  trait OutSchema_8[Ns8[_, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
    extends NS08[A, B, C, D, E, F, G, H]
      with AggregateSchema08[Ns8, A, B, C, D, E, F, G, H]

  trait OutSchema_9[Ns9[_, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
    extends NS09[A, B, C, D, E, F, G, H, I]
      with AggregateSchema09[Ns9, A, B, C, D, E, F, G, H, I]

  trait OutSchema_10[Ns10[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
    extends NS10[A, B, C, D, E, F, G, H, I, J]
      with AggregateSchema10[Ns10, A, B, C, D, E, F, G, H, I, J]

  trait OutSchema_11[Ns11[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
    extends NS11[A, B, C, D, E, F, G, H, I, J, K]
      with AggregateSchema11[Ns11, A, B, C, D, E, F, G, H, I, J, K]

  trait OutSchema_12[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
    extends NS12[A, B, C, D, E, F, G, H, I, J, K, L]
      with AggregateSchema12[Ns12, A, B, C, D, E, F, G, H, I, J, K, L]

  trait OutSchema_13[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends NS13[A, B, C, D, E, F, G, H, I, J, K, L, M]
      with AggregateSchema13[Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait OutSchema_14[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends NS14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with AggregateSchema14[Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait OutSchema_15[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends NS15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with AggregateSchema15[Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait OutSchema_16[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends NS16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with AggregateSchema16[Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait OutSchema_17[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends NS17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with AggregateSchema17[Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait OutSchema_18[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends NS18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with AggregateSchema18[Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait OutSchema_19[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends NS19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with AggregateSchema19[Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait OutSchema_20[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends NS20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with AggregateSchema20[Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait OutSchema_21[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends NS21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with AggregateSchema21[Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait OutSchema_22[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends NS22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}