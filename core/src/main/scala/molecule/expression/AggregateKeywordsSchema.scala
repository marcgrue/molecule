package molecule.expression

import scala.language.higherKinds


/** Count expression on generic attributes.
  * <br><br>
  * Apply `count` or `countDistinct` keywords to generic attributes.
  * {{{
  *   Schema.e(count).get.head === 27                  // count of all attributes in schema
  *   Schema.a(countDistinct).get.head === 23          // count of all distinct attribute names in schema
  *   Schema.ns("myNamepace").a(count).get.head === 3  // count of attributes in MyNamespace
  * }}}
  *
  * @see [[http://www.scalamolecule.org/manual/attributes/aggregates/ Manual]]
  *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/expression/Aggregates.scala#L1 Tests]]
  * @groupname aggregates Aggregate keywords
  * @groupdesc aggregates Keywords applied to attributes that return aggregated value(s).
  * @groupprio aggregates 30
  * @groupname aggrNumber Number aggregation keywords
  * @groupdesc aggrNumber Keywords applied to number attributes that return aggregated value(s).
  * @groupprio aggrNumber 31
  * */

/** Apply methods of arity 1-22 taking aggregate keywords. */
object AggregateKeywordsSchema extends AggregateKeywords {

  import molecule.api.core

  trait AggregateSchema00[Ns0]

  trait AggregateSchema01[Ns1[_], A] {
    def apply(v: core.count)        : Ns1[Int] = ???
    def apply(v: core.countDistinct): Ns1[Int] = ???
  }

  trait AggregateSchema02[Ns2[_,_], A, B] {
    def apply(v: core.count)        : Ns2[A, Int] = ???
    def apply(v: core.countDistinct): Ns2[A, Int] = ???
  }

  trait AggregateSchema03[Ns3[_,_,_], A, B, C] {
    def apply(v: core.count)        : Ns3[A, B, Int] = ???
    def apply(v: core.countDistinct): Ns3[A, B, Int] = ???
  }

  trait AggregateSchema04[Ns4[_,_,_,_], A, B, C, D] {
    def apply(v: core.count)        : Ns4[A, B, C, Int] = ???
    def apply(v: core.countDistinct): Ns4[A, B, C, Int] = ???
  }

  trait AggregateSchema05[Ns5[_,_,_,_,_], A, B, C, D, E] {
    def apply(v: core.count)        : Ns5[A, B, C, D, Int] = ???
    def apply(v: core.countDistinct): Ns5[A, B, C, D, Int] = ???
  }

  trait AggregateSchema06[Ns6[_,_,_,_,_,_], A, B, C, D, E, F] {
    def apply(v: core.count)        : Ns6[A, B, C, D, E, Int] = ???
    def apply(v: core.countDistinct): Ns6[A, B, C, D, E, Int] = ???
  }

  trait AggregateSchema07[Ns7[_,_,_,_,_,_,_], A, B, C, D, E, F, G] {
    def apply(v: core.count)        : Ns7[A, B, C, D, E, F, Int] = ???
    def apply(v: core.countDistinct): Ns7[A, B, C, D, E, F, Int] = ???
  }

  trait AggregateSchema08[Ns8[_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H] {
    def apply(v: core.count)        : Ns8[A, B, C, D, E, F, G, Int] = ???
    def apply(v: core.countDistinct): Ns8[A, B, C, D, E, F, G, Int] = ???
  }

  trait AggregateSchema09[Ns9[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I] {
    def apply(v: core.count)        : Ns9[A, B, C, D, E, F, G, H, Int] = ???
    def apply(v: core.countDistinct): Ns9[A, B, C, D, E, F, G, H, Int] = ???
  }

  trait AggregateSchema10[Ns10[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J] {
    def apply(v: core.count)        : Ns10[A, B, C, D, E, F, G, H, I, Int] = ???
    def apply(v: core.countDistinct): Ns10[A, B, C, D, E, F, G, H, I, Int] = ???
  }

  trait AggregateSchema11[Ns11[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K] {
    def apply(v: core.count)        : Ns11[A, B, C, D, E, F, G, H, I, J, Int] = ???
    def apply(v: core.countDistinct): Ns11[A, B, C, D, E, F, G, H, I, J, Int] = ???
  }

  trait AggregateSchema12[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L] {
    def apply(v: core.count)        : Ns12[A, B, C, D, E, F, G, H, I, J, K, Int] = ???
    def apply(v: core.countDistinct): Ns12[A, B, C, D, E, F, G, H, I, J, K, Int] = ???
  }

  trait AggregateSchema13[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M] {
    def apply(v: core.count)        : Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int] = ???
    def apply(v: core.countDistinct): Ns13[A, B, C, D, E, F, G, H, I, J, K, L, Int] = ???
  }

  trait AggregateSchema14[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N] {
    def apply(v: core.count)        : Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int] = ???
    def apply(v: core.countDistinct): Ns14[A, B, C, D, E, F, G, H, I, J, K, L, M, Int] = ???
  }

  trait AggregateSchema15[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] {
    def apply(v: core.count)        : Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int] = ???
    def apply(v: core.countDistinct): Ns15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, Int] = ???
  }

  trait AggregateSchema16[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {
    def apply(v: core.count)        : Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int] = ???
    def apply(v: core.countDistinct): Ns16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Int] = ???
  }

  trait AggregateSchema17[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] {
    def apply(v: core.count)        : Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int] = ???
    def apply(v: core.countDistinct): Ns17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Int] = ???
  }

  trait AggregateSchema18[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] {
    def apply(v: core.count)        : Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int] = ???
    def apply(v: core.countDistinct): Ns18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Int] = ???
  }

  trait AggregateSchema19[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] {
    def apply(v: core.count)        : Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int] = ???
    def apply(v: core.countDistinct): Ns19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Int] = ???
  }

  trait AggregateSchema20[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] {
    def apply(v: core.count)        : Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int] = ???
    def apply(v: core.countDistinct): Ns20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Int] = ???
  }

  trait AggregateSchema21[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {
    def apply(v: core.count)        : Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
    def apply(v: core.countDistinct): Ns21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
  }

  trait AggregateSchema22[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {
    def apply(v: core.count)        : Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int] = ???
    def apply(v: core.countDistinct): Ns22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int] = ???
  }
}

