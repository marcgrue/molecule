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

  trait AggregateSchema01[Obj, Ns1[_, _], A] {
    def apply(v: Keywords.count): Ns1[Obj, Int] = ???
  }

  trait AggregateSchema02[Obj, Ns2[_, _,_], A, B] {
    def apply(v: Keywords.count): Ns2[Obj, A, Int] = ???
  }

  trait AggregateSchema03[Obj, Ns3[_, _,_,_], A, B, C] {
    def apply(v: Keywords.count): Ns3[Obj, A, B, Int] = ???
  }

  trait AggregateSchema04[Obj, Ns4[_, _,_,_,_], A, B, C, D] {
    def apply(v: Keywords.count): Ns4[Obj, A, B, C, Int] = ???
  }

  trait AggregateSchema05[Obj, Ns5[_, _,_,_,_,_], A, B, C, D, E] {
    def apply(v: Keywords.count): Ns5[Obj, A, B, C, D, Int] = ???
  }

  trait AggregateSchema06[Obj, Ns6[_, _,_,_,_,_,_], A, B, C, D, E, F] {
    def apply(v: Keywords.count): Ns6[Obj, A, B, C, D, E, Int] = ???
  }

  trait AggregateSchema07[Obj, Ns7[_, _,_,_,_,_,_,_], A, B, C, D, E, F, G] {
    def apply(v: Keywords.count): Ns7[Obj, A, B, C, D, E, F, Int] = ???
  }

  trait AggregateSchema08[Obj, Ns8[_, _,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {
    def apply(v: Keywords.count): Ns8[Obj, A, B, C, D, E, F, G, Int] = ???
  }

  trait AggregateSchema09[Obj, Ns9[_, _,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {
    def apply(v: Keywords.count): Ns9[Obj, A, B, C, D, E, F, G, H, Int] = ???
  }

  trait AggregateSchema10[Obj, Ns10[_, _,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {
    def apply(v: Keywords.count): Ns10[Obj, A, B, C, D, E, F, G, H, I, Int] = ???
  }

  trait AggregateSchema11[Obj, Ns11[_, _,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {
    def apply(v: Keywords.count): Ns11[Obj, A, B, C, D, E, F, G, H, I, J, Int] = ???
  }

  trait AggregateSchema12[Obj, Ns12[_, _,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {
    def apply(v: Keywords.count): Ns12[Obj, A, B, C, D, E, F, G, H, I, J, K, Int] = ???
  }

  trait AggregateSchema13[Obj, Ns13[_, _,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {
    def apply(v: Keywords.count): Ns13[Obj, A, B, C, D, E, F, G, H, I, J, K, L, Int] = ???
  }

  trait AggregateSchema14[Obj, Ns14[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {
    def apply(v: Keywords.count): Ns14[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, Int] = ???
  }

  trait AggregateSchema15[Obj, Ns15[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
    def apply(v: Keywords.count): Ns15[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int] = ???
  }

  trait AggregateSchema16[Obj, Ns16[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {
    def apply(v: Keywords.count): Ns16[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int] = ???
  }

  trait AggregateSchema17[Obj, Ns17[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {
    def apply(v: Keywords.count): Ns17[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int] = ???
  }

  trait AggregateSchema18[Obj, Ns18[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {
    def apply(v: Keywords.count): Ns18[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int] = ???
  }

  trait AggregateSchema19[Obj, Ns19[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {
    def apply(v: Keywords.count): Ns19[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int] = ???
  }

  trait AggregateSchema20[Obj, Ns20[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {
    def apply(v: Keywords.count): Ns20[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int] = ???
  }

  trait AggregateSchema21[Obj, Ns21[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {
    def apply(v: Keywords.count): Ns21[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
  }

  trait AggregateSchema22[Obj, Ns22[_, _,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {
    def apply(v: Keywords.count): Ns22[Obj, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int] = ???
  }
}

