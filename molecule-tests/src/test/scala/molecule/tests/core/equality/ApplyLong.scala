package molecule.tests.core.equality

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out4._
import molecule.setup.TestSpec

class ApplyLong extends TestSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.long$ insert List(
        (1, Some(1L)),
        (2, Some(2L)),
        (3, Some(3L)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.long.apply(1L).get === List(1L)
      Ns.long.apply(2L).get === List(2L)
      Ns.long.apply(1L, 2L).get === List(1L, 2L)

      // `or`
      Ns.long.apply(1L or 2L).get === List(1L, 2L)
      Ns.long.apply(1L or 2L or 3L).get === List(1L, 2L, 3L)

      // Seq
      Ns.long.apply().get === Nil
      Ns.long.apply(Nil).get === Nil
      Ns.long.apply(List(1L)).get === List(1L)
      Ns.long.apply(List(2L)).get === List(2L)
      Ns.long.apply(List(1L, 2L)).get === List(1L, 2L)
      Ns.long.apply(List(1L), List(2L)).get === List(1L, 2L)
      Ns.long.apply(List(1L, 2L), List(3L)).get === List(1L, 2L, 3L)
      Ns.long.apply(List(1L), List(2L, 3L)).get === List(1L, 2L, 3L)
      Ns.long.apply(List(1L, 2L, 3L)).get === List(1L, 2L, 3L)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.long_.apply(1L).get === List(1)
      Ns.int.long_.apply(2L).get === List(2)
      Ns.int.long_.apply(1L, 2L).get === List(1, 2)

      // `or`
      Ns.int.long_.apply(1L or 2L).get === List(1, 2)
      Ns.int.long_.apply(1L or 2L or 3L).get === List(1, 2, 3)

      // Seq
      Ns.int.long_.apply().get === List(4)
      Ns.int.long_.apply(Nil).get === List(4)
      Ns.int.long_.apply(List(1L)).get === List(1)
      Ns.int.long_.apply(List(2L)).get === List(2)
      Ns.int.long_.apply(List(1L, 2L)).get === List(1, 2)
      Ns.int.long_.apply(List(1L), List(2L)).get === List(1, 2)
      Ns.int.long_.apply(List(1L, 2L), List(3L)).get === List(1, 2, 3)
      Ns.int.long_.apply(List(1L), List(2L, 3L)).get === List(1, 2, 3)
      Ns.int.long_.apply(List(1L, 2L, 3L)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.longs$ insert List(
        (1, Some(Set(1L, 2L))),
        (2, Some(Set(2L, 3L))),
        (3, Some(Set(3L, 4L))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.longs.apply(1L).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(2L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(1L, 2L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))

      // `or`
      Ns.int.longs.apply(1L or 2L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(1L or 2L or 3L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

      // Seq
      Ns.int.longs.apply().get === Nil
      Ns.int.longs.apply(Nil).get === Nil
      Ns.int.longs.apply(List(1L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(List(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(List(1L, 2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(List(1L), List(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(List(1L, 2L), List(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      Ns.int.longs.apply(List(1L), List(2L, 3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      Ns.int.longs.apply(List(1L, 2L, 3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))


      // AND semantics

      // Set
      Ns.int.longs.apply(Set[Long]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.longs.apply(Set(1L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(Set(1L, 2L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(Set(1L, 3L)).get === Nil
      Ns.int.longs.apply(Set(2L, 3L)).get === List((2, Set(2L, 3L)))
      Ns.int.longs.apply(Set(1L, 2L, 3L)).get === Nil

      Ns.int.longs.apply(Set(1L, 2L), Set[Long]()).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(Set(1L, 2L), Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(Set(1L, 2L), Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      Ns.int.longs.apply(Set(1L, 2L), Set(4L)).get === List((1, Set(1L, 2L)), (3, Set(3L, 4L)))
      Ns.int.longs.apply(Set(1L, 2L), Set(2L), Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

      Ns.int.longs.apply(Set(1L, 2L), Set(2L, 3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(Set(1L, 2L), Set(2L, 4L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(Set(1L, 2L), Set(3L, 4L)).get === List((1, Set(1L, 2L)), (3, Set(3L, 4L)))

      // `and`
      Ns.int.longs.apply(1L and 2L).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(1L and 3L).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.longs.apply(1L).get === List(Set(1L, 2L))
      Ns.longs.apply(2L).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(1L, 2L).get === List(Set(1L, 3L, 2L))

      // `or`
      Ns.longs.apply(1L or 2L).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(1L or 2L or 3L).get === List(Set(1L, 4L, 3L, 2L))

      // Seq
      Ns.longs.apply().get === Nil
      Ns.longs.apply(Nil).get === Nil
      Ns.longs.apply(List(1L)).get === List(Set(1L, 2L))
      Ns.longs.apply(List(2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(List(1L, 2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(List(1L), List(2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(List(1L, 2L), List(3L)).get === List(Set(1L, 4L, 3L, 2L))
      Ns.longs.apply(List(1L), List(2L, 3L)).get === List(Set(1L, 4L, 3L, 2L))
      Ns.longs.apply(List(1L, 2L, 3L)).get === List(Set(1L, 4L, 3L, 2L))


      // AND semantics

      // Set
      Ns.longs.apply(Set[Long]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.longs.apply(Set(1L)).get === List(Set(1L, 2L))
      Ns.longs.apply(Set(2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(Set(1L, 2L)).get === List(Set(1L, 2L))
      Ns.longs.apply(Set(1L, 3L)).get === Nil
      Ns.longs.apply(Set(2L, 3L)).get === List(Set(2L, 3L))
      Ns.longs.apply(Set(1L, 2L, 3L)).get === Nil

      Ns.longs.apply(Set(1L, 2L), Set(2L)).get === List(Set(1L, 2L, 3L))
      Ns.longs.apply(Set(1L, 2L), Set(3L)).get === List(Set(1L, 2L, 3L, 4L))
      Ns.longs.apply(Set(1L, 2L), Set(4L)).get === List(Set(1L, 2L, 3L, 4L))
      Ns.longs.apply(Set(1L, 2L), Set(2L), Set(3L)).get === List(Set(1L, 2L, 3L, 4L))

      Ns.longs.apply(Set(1L, 2L), Set(2L, 3L)).get === List(Set(1L, 2L, 3L))
      Ns.longs.apply(Set(1L, 2L), Set(2L, 4L)).get === List(Set(1L, 2L))
      Ns.longs.apply(Set(1L, 2L), Set(3L, 4L)).get === List(Set(1L, 2L, 3L, 4L))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.longs.apply(1L and 2L).get === List(Set(1L, 2L))
      Ns.longs.apply(1L and 3L).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.longs_.apply(1L).get === List(1)
      Ns.int.longs_.apply(2L).get === List(1, 2)
      Ns.int.longs_.apply(1L, 2L).get === List(1, 2)

      // `or`
      Ns.int.longs_.apply(1L or 2L).get === List(1, 2)
      Ns.int.longs_.apply(1L or 2L or 3L).get === List(1, 2, 3)

      // Seq
      Ns.int.longs_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.longs_.apply(Nil).get === List(4)
      Ns.int.longs_.apply(List(1L)).get === List(1)
      Ns.int.longs_.apply(List(2L)).get === List(1, 2)
      Ns.int.longs_.apply(List(1L, 2L)).get === List(1, 2)
      Ns.int.longs_.apply(List(1L), List(2L)).get === List(1, 2)
      Ns.int.longs_.apply(List(1L, 2L), List(3L)).get === List(1, 2, 3)
      Ns.int.longs_.apply(List(1L), List(2L, 3L)).get === List(1, 2, 3)
      Ns.int.longs_.apply(List(1L, 2L, 3L)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.longs_.apply(Set[Long]()).get === List(4)
      Ns.int.longs_.apply(Set(1L)).get === List(1)
      Ns.int.longs_.apply(Set(2L)).get === List(1, 2)
      Ns.int.longs_.apply(Set(1L, 2L)).get === List(1)
      Ns.int.longs_.apply(Set(1L, 3L)).get === Nil
      Ns.int.longs_.apply(Set(2L, 3L)).get === List(2)
      Ns.int.longs_.apply(Set(1L, 2L, 3L)).get === Nil

      Ns.int.longs_.apply(Set(1L, 2L), Set(2L)).get === List(1, 2)
      Ns.int.longs_.apply(Set(1L, 2L), Set(3L)).get === List(1, 2, 3)
      Ns.int.longs_.apply(Set(1L, 2L), Set(4L)).get === List(1, 3)
      Ns.int.longs_.apply(Set(1L, 2L), Set(2L), Set(3L)).get === List(1, 2, 3)

      Ns.int.longs_.apply(Set(1L, 2L), Set(2L, 3L)).get === List(1, 2)
      Ns.int.longs_.apply(Set(1L, 2L), Set(2L, 4L)).get === List(1)
      Ns.int.longs_.apply(Set(1L, 2L), Set(3L, 4L)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.longs_.apply(1L and 2L).get === List(1)
      Ns.int.longs_.apply(1L and 3L).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[Long]()

      val l1 = List(long1)
      val l2 = List(long2)

      val s1 = Set(long1)
      val s2 = Set(long2)

      val l12 = List(long1, long2)
      val l23 = List(long2, long3)

      val s12 = Set(long1, long2)
      val s23 = Set(long2, long3)


      // OR semantics

      // Vararg
      Ns.int.longs_.apply(long1, long2).get === List(1, 2)

      // `or`
      Ns.int.longs_.apply(long1 or long2).get === List(1, 2)

      // Seq
      Ns.int.longs_.apply(seq0).get === List(4)
      Ns.int.longs_.apply(List(long1), List(long2)).get === List(1, 2)
      Ns.int.longs_.apply(l1, l2).get === List(1, 2)
      Ns.int.longs_.apply(List(long1, long2)).get === List(1, 2)
      Ns.int.longs_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.longs_.apply(set0).get === List(4)

      Ns.int.longs_.apply(Set(long1)).get === List(1)
      Ns.int.longs_.apply(s1).get === List(1)

      Ns.int.longs_.apply(Set(long2)).get === List(1, 2)
      Ns.int.longs_.apply(s2).get === List(1, 2)

      Ns.int.longs_.apply(Set(long1, long2)).get === List(1)
      Ns.int.longs_.apply(s12).get === List(1)

      Ns.int.longs_.apply(Set(long2, long3)).get === List(2)
      Ns.int.longs_.apply(s23).get === List(2)

      Ns.int.longs_.apply(Set(long1, long2), Set(long2, long3)).get === List(1, 2)
      Ns.int.longs_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.longs_.apply(long1 and long2).get === List(1)
    }
  }


  "Implicit conversions Int to Long, card one" in new CoreSetup {

    Ns.int.long insert List(
      (1, 1),
      (2, 1L),
      (3, long2),
    )

    val res1 = List(
      (1, 1L),
      (2, 1L),
    )
    val res2 = List((3, 2L))

    val res1t = List(1, 2)
    val res2t = List(3)

    // Mandatory

    Ns.int.long(1).get.sortBy(_._1) === res1
    Ns.int.long.not(2).get.sortBy(_._1) === res1
    Ns.int.long.<(2).get.sortBy(_._1) === res1
    Ns.int.long.>(1).get === res2
    Ns.int.long.>=(2).get === res2
    Ns.int.long.<=(1).get.sortBy(_._1) === res1


    Ns.int.long(int1).get.sortBy(_._1) === res1
    Ns.int.long.not(int2).get.sortBy(_._1) === res1
    Ns.int.long.<(int2).get.sortBy(_._1) === res1
    Ns.int.long.>(int1).get === res2
    Ns.int.long.>=(int2).get === res2
    Ns.int.long.<=(int1).get.sortBy(_._1) === res1

    // Tacit

    Ns.int.long_(1).get.sorted === res1t
    Ns.int.long_.not(2).get.sorted === res1t
    Ns.int.long_.<(2).get.sorted === res1t
    Ns.int.long_.>(1).get === res2t
    Ns.int.long_.>=(2).get === res2t
    Ns.int.long_.<=(1).get.sorted === res1t


    Ns.int.long_(int1).get.sorted === res1t
    Ns.int.long_.not(int2).get.sorted === res1t
    Ns.int.long_.<(int2).get.sorted === res1t
    Ns.int.long_.>(int1).get === res2t
    Ns.int.long_.>=(int2).get === res2t
    Ns.int.long_.<=(int1).get.sorted === res1t
  }


  "Implicit conversions Int to Long, card many" in new CoreSetup {

    Ns.int.longs insert List(
      (1, Set(1)),
      (2, Set(1L)),
      (3, Set(long2)),
    )

    val res1 = List(
      (1, Set(1L)),
      (2, Set(1L)),
    )
    val res2 = List((3, Set(2L)))

    val res1t = List(1, 2)
    val res2t = List(3)

    // Mandatory

    Ns.int.longs(1).get.sortBy(_._1) === res1
    Ns.int.longs.not(2).get.sortBy(_._1) === res1
    Ns.int.longs.<(2).get.sortBy(_._1) === res1
    Ns.int.longs.>(1).get === res2
    Ns.int.longs.>=(2).get === res2
    Ns.int.longs.<=(1).get.sortBy(_._1) === res1


    Ns.int.longs(int1).get.sortBy(_._1) === res1
    Ns.int.longs.not(int2).get.sortBy(_._1) === res1
    Ns.int.longs.<(int2).get.sortBy(_._1) === res1
    Ns.int.longs.>(int1).get === res2
    Ns.int.longs.>=(int2).get === res2
    Ns.int.longs.<=(int1).get.sortBy(_._1) === res1

    // Tacit

    Ns.int.longs_(1).get.sorted === res1t
    Ns.int.longs_.not(2).get.sorted === res1t
    Ns.int.longs_.<(2).get.sorted === res1t
    Ns.int.longs_.>(1).get === res2t
    Ns.int.longs_.>=(2).get === res2t
    Ns.int.longs_.<=(1).get.sorted === res1t


    Ns.int.longs_(int1).get.sorted === res1t
    Ns.int.longs_.not(int2).get.sorted === res1t
    Ns.int.longs_.<(int2).get.sorted === res1t
    Ns.int.longs_.>(int1).get === res2t
    Ns.int.longs_.>=(int2).get === res2t
    Ns.int.longs_.<=(int1).get.sorted === res1t
  }
}