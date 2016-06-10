package molecule.dsl
import molecule.dsl.schemaDSL._

import scala.language.higherKinds

trait Out_0[Ns0, Ns1[_], In1_0[_], In1_1[_, _]]
  extends NS0[Nothing]
    with Generic0[Ns0, Ns1, In1_0, In1_1]
    with Aggregate0[Ns0]
    with Branch0[Ns0, Ns1] {
}

trait Out_1[Ns1[_], Ns2[_, _], In1_1[_, _], In1_2[_, _, _], A]
  extends NS1[A]
    with Generic1[Ns1, Ns2, In1_1, In1_2, A]
    with Aggregate1[Ns1, A]
    with Branch1[Ns1, Ns2, A]
//    with Free1[A]

trait Out_2[Ns2[_, _], Ns3[_, _, _], In1_2[_, _, _], In1_3[_, _, _, _], A, B]
  extends NS2[A, B]
    with Generic2[Ns2, Ns3, In1_2, In1_3, A, B]
    with Aggregate2[Ns2, A, B]
    with Branch2[Ns2, Ns3, A, B] {

  def ~[a      ] (molecule: NS1 [a      ]): Free2[(A, B), a        ] = ???
  def ~[a, b   ] (molecule: NS2 [a, b   ]): Free2[(A, B), (a, b   )] = ???
  def ~[a, b, c] (molecule: NS3 [a, b, c]): Free2[(A, B), (a, b, c)] = ???
}



trait Out_3[Ns3[_, _, _], Ns4[_, _, _, _], In1_3[_, _, _, _], In1_4[_, _, _, _, _], A, B, C]
  extends NS3[A, B, C]
    with Generic3[Ns3, Ns4, In1_3, In1_4, A, B, C]
    with Aggregate3[Ns3, A, B, C]
    with Branch3[Ns3, Ns4, A, B, C]{

      def ~[a      ] (nested: NS1 [a      ]): Free2[(A, B, C), a] = ???
      def ~[a, b   ] (nested: NS2 [a, b   ]): Free2[(A, B, C), (a, b   )] = ???
      def ~[a, b, c] (nested: NS3 [a, b, c]): Free2[(A, B, C), (a, b, c)] = ???
    }

trait Out_4[Ns4[_, _, _, _], Ns5[_, _, _, _, _], In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], A, B, C, D]
  extends NS4[A, B, C, D]
    with Generic4[Ns4, Ns5, In1_4, In1_5, A, B, C, D]
    with Aggregate4[Ns4, A, B, C, D]
    with Branch4[Ns4, Ns5, A, B, C, D]

trait Out_5[Ns5[_, _, _, _, _], Ns6[_, _, _, _, _, _], In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], A, B, C, D, E]
  extends NS5[A, B, C, D, E]
    with Generic5[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]
    with Aggregate5[Ns5, A, B, C, D, E]
    with Branch5[Ns5, Ns6, A, B, C, D, E]

trait Out_6[Ns6[_, _, _, _, _, _], Ns7[_, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], A, B, C, D, E, F]
  extends NS6[A, B, C, D, E, F]
    with Generic6[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]
    with Aggregate6[Ns6, A, B, C, D, E, F]
    with Branch6[Ns6, Ns7, A, B, C, D, E, F]

trait Out_7[Ns7[_, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G]
  extends NS7[A, B, C, D, E, F, G]
    with Generic7[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]
    with Aggregate7[Ns7, A, B, C, D, E, F, G]
    with Branch7[Ns7, Ns8, A, B, C, D, E, F, G]

trait Out_8[Ns8[_, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H]
  extends NS8[A, B, C, D, E, F, G, H]
    with Generic8[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]
    with Aggregate8[Ns8, A, B, C, D, E, F, G, H]
    with Branch8[Ns8, Ns9, A, B, C, D, E, F, G, H]

trait Out_9[Ns9[_, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I]
  extends NS9[A, B, C, D, E, F, G, H, I]
    with Generic9[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]
    with Aggregate9[Ns9, A, B, C, D, E, F, G, H, I]
    with Branch9[Ns9, Ns10, A, B, C, D, E, F, G, H, I]

trait Out_10[Ns10[_, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J]
  extends NS10[A, B, C, D, E, F, G, H, I, J]
    with Generic10[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]
    with Aggregate10[Ns10, A, B, C, D, E, F, G, H, I, J]
    with Branch10[Ns10, Ns11, A, B, C, D, E, F, G, H, I, J]

trait Out_11[Ns11[_, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K]
  extends NS11[A, B, C, D, E, F, G, H, I, J, K]
    with Generic11[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]
    with Aggregate11[Ns11, A, B, C, D, E, F, G, H, I, J, K]
    with Branch11[Ns11, Ns12, A, B, C, D, E, F, G, H, I, J, K]

trait Out_12[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L]
  extends NS12[A, B, C, D, E, F, G, H, I, J, K, L]
    with Generic12[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]
    with Aggregate12[Ns12, A, B, C, D, E, F, G, H, I, J, K, L]
    with Branch12[Ns12, Ns13, A, B, C, D, E, F, G, H, I, J, K, L]

trait Out_13[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M]
  extends NS13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    with Generic13[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]
    with Aggregate13[Ns13, A, B, C, D, E, F, G, H, I, J, K, L, M]
    with Branch13[Ns13, Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M]

trait Out_14[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  extends NS14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    with Generic14[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    with Aggregate14[Ns14, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    with Branch14[Ns14, Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]

trait Out_15[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  extends NS15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    with Generic15[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    with Aggregate15[Ns15, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    with Branch15[Ns15, Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]

trait Out_16[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  extends NS16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    with Generic16[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    with Aggregate16[Ns16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    with Branch16[Ns16, Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]

trait Out_17[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  extends NS17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    with Generic17[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    with Aggregate17[Ns17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    with Branch17[Ns17, Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]

trait Out_18[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  extends NS18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    with Generic18[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    with Aggregate18[Ns18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    with Branch18[Ns18, Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]

trait Out_19[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  extends NS19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    with Generic19[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    with Aggregate19[Ns19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    with Branch19[Ns19, Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]

trait Out_20[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  extends NS20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    with Generic20[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    with Aggregate20[Ns20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    with Branch20[Ns20, Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]

trait Out_21[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  extends NS21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    with Generic21[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    with Aggregate21[Ns21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    with Branch21[Ns21, Ns22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]

trait Out_22[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], P24[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
  extends NS22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    with Generic22[Ns22, P23, In1_22, P24, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    with Branch22[Ns22, P23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
