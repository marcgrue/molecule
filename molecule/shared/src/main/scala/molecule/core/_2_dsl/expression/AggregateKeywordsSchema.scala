package molecule.core._2_dsl.expression

import molecule.core._2_dsl.generic.schema.Schema_1


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

  import molecule.core._4_api.api.Keywords

  trait AggregateSchema00[obj[_], props, Ns0[_,_]]

  trait AggregateSchema01[obj[_], props, Ns1[_,_,_], A] {
    def apply(v: Keywords.count): Ns1[obj[_], props, Int] = ???
  }

  trait AggregateSchema02[obj[_], props, Ns2[_,_,_,_], A, B] {
    def apply(v: Keywords.count): Ns2[obj[_], props, A, Int] = ???
  }

  trait AggregateSchema03[obj[_], props, Ns3[_,_,_,_,_], A, B, C] {
    def apply(v: Keywords.count): Ns3[obj[_], props, A, B, Int] = ???
  }

  trait AggregateSchema04[obj[_], props, Ns4[_,_,_,_,_,_], A, B, C, D] {
    def apply(v: Keywords.count): Ns4[obj[_], props, A, B, C, Int] = ???
  }

  trait AggregateSchema05[obj[_], props, Ns5[_,_,_,_,_,_,_], A, B, C, D, E] {
    def apply(v: Keywords.count): Ns5[obj[_], props, A, B, C, D, Int] = ???
  }

  trait AggregateSchema06[obj[_], props, Ns6[_,_,_,_,_,_,_,_], A, B, C, D, E, F] {
    def apply(v: Keywords.count): Ns6[obj[_], props, A, B, C, D, E, Int] = ???
  }

  trait AggregateSchema07[obj[_], props, Ns7[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G] {
    def apply(v: Keywords.count): Ns7[obj[_], props, A, B, C, D, E, F, Int] = ???
  }

  trait AggregateSchema08[obj[_], props, Ns8[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {
    def apply(v: Keywords.count): Ns8[obj[_], props, A, B, C, D, E, F, G, Int] = ???
  }

  trait AggregateSchema09[obj[_], props, Ns9[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {
    def apply(v: Keywords.count): Ns9[obj[_], props, A, B, C, D, E, F, G, H, Int] = ???
  }

  trait AggregateSchema10[obj[_], props, Ns10[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {
    def apply(v: Keywords.count): Ns10[obj[_], props, A, B, C, D, E, F, G, H, I, Int] = ???
  }

  trait AggregateSchema11[obj[_], props, Ns11[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {
    def apply(v: Keywords.count): Ns11[obj[_], props, A, B, C, D, E, F, G, H, I, J, Int] = ???
  }

  trait AggregateSchema12[obj[_], props, Ns12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {
    def apply(v: Keywords.count): Ns12[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, Int] = ???
  }

  trait AggregateSchema13[obj[_], props, Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {
    def apply(v: Keywords.count): Ns13[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, Int] = ???
  }

  trait AggregateSchema14[obj[_], props, Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {
    def apply(v: Keywords.count): Ns14[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, Int] = ???
  }

  trait AggregateSchema15[obj[_], props, Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
    def apply(v: Keywords.count): Ns15[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int] = ???
  }

  trait AggregateSchema16[obj[_], props, Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {
    def apply(v: Keywords.count): Ns16[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int] = ???
  }

  trait AggregateSchema17[obj[_], props, Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {
    def apply(v: Keywords.count): Ns17[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int] = ???
  }

  trait AggregateSchema18[obj[_], props, Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {
    def apply(v: Keywords.count): Ns18[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int] = ???
  }

  trait AggregateSchema19[obj[_], props, Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {
    def apply(v: Keywords.count): Ns19[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int] = ???
  }

  trait AggregateSchema20[obj[_], props, Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {
    def apply(v: Keywords.count): Ns20[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int] = ???
  }

  trait AggregateSchema21[obj[_], props, Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {
    def apply(v: Keywords.count): Ns21[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
  }

  trait AggregateSchema22[obj[_], props, Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {
    def apply(v: Keywords.count): Ns22[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int] = ???
  }
}

