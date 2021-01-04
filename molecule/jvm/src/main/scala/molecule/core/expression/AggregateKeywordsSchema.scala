package molecule.core.expression


/** Count expression on generic attributes.
  * <br><br>
  * Apply `count` or `countDistinct` keywords to generic attributes.
  * {{{
  *   Schema.e(count).get.head === 27                  // count of all attributes in schema
  *   Schema.a(countDistinct).get.head === 23          // count of all distinct attribute names in schema
  *   Schema.ns("myNamepace").a(count).get.head === 3  // count of attributes in MyNamespace
  * }}}
  *
  * @groupname aggregates Aggregate keywords
  * @groupdesc aggregates Keywords applied to attributes that return aggregated value(s).
  * @groupprio aggregates 30
  * @groupname aggrNumber Number aggregation keywords
  * @groupdesc aggrNumber Keywords applied to number attributes that return aggregated value(s).
  * @groupprio aggrNumber 31
  * */

/** Apply methods of arity 1-22 taking aggregate keywords. */
object AggregateKeywordsSchema extends AggregateKeywords {

  import molecule.core.api.Keywords

  trait AggregateSchema00[Ns0]

  trait AggregateSchema01[Ns1[_], A] {
    def apply(v: Keywords.count): Ns1[Int] = ???
  }

  trait AggregateSchema02[Ns2[_,_], A, B] {
    def apply(v: Keywords.count): Ns2[A, Int] = ???
  }

  trait AggregateSchema03[Ns3[_,_,_], A, B, C] {
    def apply(v: Keywords.count): Ns3[A, B, Int] = ???
  }

  trait AggregateSchema04[Ns4[_,_,_,_], A, B, C, D] {
    def apply(v: Keywords.count): Ns4[A, B, C, Int] = ???
  }

  trait AggregateSchema05[Ns5[_,_,_,_,_], A, B, C, D, E] {
    def apply(v: Keywords.count): Ns5[A, B, C, D, Int] = ???
  }

  trait AggregateSchema06[Ns6[_,_,_,_,_,_], A, B, C, D, E, F] {
    def apply(v: Keywords.count): Ns6[A, B, C, D, E, Int] = ???
  }

  trait AggregateSchema07[Ns7[_,_,_,_,_,_,_], A, B, C, D, E, F, G] {
    def apply(v: Keywords.count): Ns7[A, B, C, D, E, F, Int] = ???
  }

  trait AggregateSchema08[Ns8[_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {
    def apply(v: Keywords.count): Ns8[A, B, C, D, E, F, G, Int] = ???
  }

  trait AggregateSchema09[Ns9[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {
    def apply(v: Keywords.count): Ns9[A, B, C, D, E, F, G, H, Int] = ???
  }

  trait AggregateSchema10[Ns10[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {
    def apply(v: Keywords.count): Ns10[A, B, C, D, E, F, G, H, I, Int] = ???
  }

  trait AggregateSchema11[Ns11[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {
    def apply(v: Keywords.count): Ns11[A, B, C, D, E, F, G, H, I, J, Int] = ???
  }

  trait AggregateSchema12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {
    def apply(v: Keywords.count): Ns12[A, B, C, D, E, F, G, H, I, J, K, Int] = ???
  }

  trait AggregateSchema13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {
    def apply(v: Keywords.count): Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int] = ???
  }

  trait AggregateSchema14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {
    def apply(v: Keywords.count): Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int] = ???
  }

  trait AggregateSchema15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
    def apply(v: Keywords.count): Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int] = ???
  }

  trait AggregateSchema16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {
    def apply(v: Keywords.count): Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int] = ???
  }

  trait AggregateSchema17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {
    def apply(v: Keywords.count): Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int] = ???
  }

  trait AggregateSchema18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {
    def apply(v: Keywords.count): Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int] = ???
  }

  trait AggregateSchema19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {
    def apply(v: Keywords.count): Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int] = ???
  }

  trait AggregateSchema20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {
    def apply(v: Keywords.count): Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int] = ???
  }

  trait AggregateSchema21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {
    def apply(v: Keywords.count): Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
  }

  trait AggregateSchema22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {
    def apply(v: Keywords.count): Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int] = ???
  }
}

