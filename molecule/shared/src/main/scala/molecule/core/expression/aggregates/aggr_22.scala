package molecule.core.expression.aggregates

import molecule.core.api.Keywords

object aggr_22 {

  trait Aggr_22_L0[o0[_], p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L1[o0[_], p0, o1[_], p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L2[o0[_], p0, o1[_], p1, o2[_], p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, o2, p2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }

  trait Aggr_22_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Ns[_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_[_],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] {
    def apply(v: Keywords.min)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.max)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.rand)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sample)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.sum)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.median)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V      ] = ???
    def apply(v: Keywords.mins)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.maxs)         : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.distinct)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.rands)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.samples)      : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, List[V]] = ???
    def apply(v: Keywords.count)        : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.countDistinct): Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Int    ] = ???
    def apply(v: Keywords.avg)          : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.variance)     : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
    def apply(v: Keywords.stddev)       : Ns[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Double ] = ???
  }
}