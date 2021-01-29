package molecule.core.boilerplate

import molecule.core.boilerplate.base._
import molecule.core.expression.AggregateKeywordsSchema._
import scala.language.higherKinds


/** Type distribution to groups of functionality for 'output'/standard molecules. */
object outSchema {

  trait OutSchema_0[Ns0]
    extends NS00[Nothing]
      with AggregateSchema00[Ns0]

  trait OutSchema_1[Obj, Ns1[_, _], A]
    extends NS01[Obj, A]
      with AggregateSchema01[Obj, Ns1, A]

  trait OutSchema_2[Obj, Ns2[_, _, _], A, B]
    extends NS02[Obj, A, B]
      with AggregateSchema02[Obj, Ns2, A, B]

  trait OutSchema_3[Obj, Ns3[_, _, _, _], A, B, C]
    extends NS03[Obj, A, B, C]
      with AggregateSchema03[Obj, Ns3, A, B, C]

  trait OutSchema_4[Obj, Ns4[_, _, _, _, _], A, B, C, D]
    extends NS04[Obj, A, B, C, D]
      with AggregateSchema04[Obj, Ns4, A, B, C, D]

  trait OutSchema_5[Obj, Ns5[_, _, _, _, _, _], A, B, C, D, E]
    extends NS05[Obj, A, B, C, D, E]
      with AggregateSchema05[Obj, Ns5, A, B, C, D, E]

  trait OutSchema_6[Obj, Ns6[_, _, _, _, _, _, _], A, B, C, D, E, F]
    extends NS06[Obj, A, B, C, D, E, F]
      with AggregateSchema06[Obj, Ns6, A, B, C, D, E, F]

  trait OutSchema_7[Obj, Ns7[_, _, _, _, _, _, _, _], A, B, C, D, E, F, G]
    extends NS07[Obj, A, B, C, D, E, F, G]
      with AggregateSchema07[Obj, Ns7, A, B, C, D, E, F, G]

  trait OutSchema_8[Obj, Ns8[_, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
    extends NS08[Obj, A, B, C, D, E, F, G, H]
      with AggregateSchema08[Obj, Ns8, A, B, C, D, E, F, G, H]

  trait OutSchema_9[Obj, Ns9[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
    extends NS09[Obj, A, B, C, D, E, F, G, H, I]
      with AggregateSchema09[Obj, Ns9, A, B, C, D, E, F, G, H, I]

  trait OutSchema_10[Obj, Ns10[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
    extends NS10[Obj, A, B, C, D, E, F, G, H, I, J]
      with AggregateSchema10[Obj, Ns10, A, B, C, D, E, F, G, H, I, J]

  trait OutSchema_11[Obj, Ns11[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
    extends NS11[Obj, A, B, C, D, E, F, G, H, I, J, K]
      with AggregateSchema11[Obj, Ns11, A, B, C, D, E, F, G, H, I, J, K]

  trait OutSchema_12[Obj, Ns12[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
    extends NS12[Obj, A, B, C, D, E, F, G, H, I, J, K, L]
      with AggregateSchema12[Obj, Ns12, A, B, C, D, E, F, G, H, I, J, K, L]

  trait OutSchema_13[Obj, Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
    extends NS13[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M]
      with AggregateSchema13[Obj, Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]

  trait OutSchema_14[Obj, Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    extends NS14[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
      with AggregateSchema14[Obj, Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

  trait OutSchema_15[Obj, Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    extends NS15[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
      with AggregateSchema15[Obj, Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

  trait OutSchema_16[Obj, Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    extends NS16[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
      with AggregateSchema16[Obj, Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

  trait OutSchema_17[Obj, Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    extends NS17[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
      with AggregateSchema17[Obj, Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

  trait OutSchema_18[Obj, Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    extends NS18[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
      with AggregateSchema18[Obj, Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

  trait OutSchema_19[Obj, Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    extends NS19[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
      with AggregateSchema19[Obj, Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

  trait OutSchema_20[Obj, Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    extends NS20[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
      with AggregateSchema20[Obj, Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

  trait OutSchema_21[Obj, Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    extends NS21[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
      with AggregateSchema21[Obj, Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

  trait OutSchema_22[Obj, Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    extends NS22[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}