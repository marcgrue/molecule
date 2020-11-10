package molecule.coretests.equality

import molecule.datomic.peer.api.out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class ApplyFloat extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.float$ insert List(
        (1, Some(1.0f)),
        (2, Some(2.0f)),
        (3, Some(3.0f)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.float.apply(1.0f).get === List(1.0f)
      Ns.float.apply(2.0f).get === List(2.0f)
      Ns.float.apply(1.0f, 2.0f).get === List(1.0f, 2.0f)

      // `or`
      Ns.float.apply(1.0f or 2.0f).get === List(1.0f, 2.0f)
      Ns.float.apply(1.0f or 2.0f or 3.0f).get === List(3.0f, 1.0f, 2.0f)

      // Seq
      Ns.float.apply().get === Nil
      Ns.float.apply(Nil).get === Nil
      Ns.float.apply(List(1.0f)).get === List(1.0f)
      Ns.float.apply(List(2.0f)).get === List(2.0f)
      Ns.float.apply(List(1.0f, 2.0f)).get === List(1.0f, 2.0f)
      Ns.float.apply(List(1.0f), List(2.0f)).get === List(1.0f, 2.0f)
      Ns.float.apply(List(1.0f, 2.0f), List(3.0f)).get === List(3.0f, 1.0f, 2.0f)
      Ns.float.apply(List(1.0f), List(2.0f, 3.0f)).get === List(3.0f, 1.0f, 2.0f)
      Ns.float.apply(List(1.0f, 2.0f, 3.0f)).get === List(3.0f, 1.0f, 2.0f)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.float_.apply(1.0f).get === List(1)
      Ns.int.float_.apply(2.0f).get === List(2)
      Ns.int.float_.apply(1.0f, 2.0f).get === List(1, 2)

      // `or`
      Ns.int.float_.apply(1.0f or 2.0f).get === List(1, 2)
      Ns.int.float_.apply(1.0f or 2.0f or 3.0f).get === List(1, 2, 3)

      // Seq
      Ns.int.float_.apply().get === List(4)
      Ns.int.float_.apply(Nil).get === List(4)
      Ns.int.float_.apply(List(1.0f)).get === List(1)
      Ns.int.float_.apply(List(2.0f)).get === List(2)
      Ns.int.float_.apply(List(1.0f, 2.0f)).get === List(1, 2)
      Ns.int.float_.apply(List(1.0f), List(2.0f)).get === List(1, 2)
      Ns.int.float_.apply(List(1.0f, 2.0f), List(3.0f)).get === List(1, 2, 3)
      Ns.int.float_.apply(List(1.0f), List(2.0f, 3.0f)).get === List(1, 2, 3)
      Ns.int.float_.apply(List(1.0f, 2.0f, 3.0f)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.floats$ insert List(
        (1, Some(Set(1.0f, 2.0f))),
        (2, Some(Set(2.0f, 3.0f))),
        (3, Some(Set(3.0f, 4.0f))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.floats.apply(1.0f).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(2.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(1.0f, 2.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))

      // `or`
      Ns.int.floats.apply(1.0f or 2.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(1.0f or 2.0f or 3.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))

      // Seq
      Ns.int.floats.apply().get === Nil
      Ns.int.floats.apply(Nil).get === Nil
      Ns.int.floats.apply(List(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(List(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(List(1.0f, 2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(List(1.0f), List(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(List(1.0f, 2.0f), List(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      Ns.int.floats.apply(List(1.0f), List(2.0f, 3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      Ns.int.floats.apply(List(1.0f, 2.0f, 3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))


      // AND semantics

      // Set
      Ns.int.floats.apply(Set[Float]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.floats.apply(Set(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(Set(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(Set(1.0f, 3.0f)).get === Nil
      Ns.int.floats.apply(Set(2.0f, 3.0f)).get === List((2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f, 3.0f)).get === Nil

      Ns.int.floats.apply(Set(1.0f, 2.0f), Set[Float]()).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(4.0f)).get === List((1, Set(1.0f, 2.0f)), (3, Set(3.0f, 4.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(2.0f), Set(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))

      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(2.0f, 3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(2.0f, 4.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f), Set(3.0f, 4.0f)).get === List((1, Set(1.0f, 2.0f)), (3, Set(3.0f, 4.0f)))

      // `and`
      Ns.int.floats.apply(1.0f and 2.0f).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(1.0f and 3.0f).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.floats.apply(1.0f).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(2.0f).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(1.0f, 2.0f).get === List(Set(1.0f, 3.0f, 2.0f))

      // `or`
      Ns.floats.apply(1.0f or 2.0f).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(1.0f or 2.0f or 3.0f).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))

      // Seq
      Ns.floats.apply().get === Nil
      Ns.floats.apply(Nil).get === Nil
      Ns.floats.apply(List(1.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(List(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f, 2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f), List(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f, 2.0f), List(3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f), List(2.0f, 3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f, 2.0f, 3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))


      // AND semantics

      // Set
      Ns.floats.apply(Set[Float]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.floats.apply(Set(1.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(Set(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(Set(1.0f, 2.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(Set(1.0f, 3.0f)).get === Nil
      Ns.floats.apply(Set(2.0f, 3.0f)).get === List(Set(2.0f, 3.0f))
      Ns.floats.apply(Set(1.0f, 2.0f, 3.0f)).get === Nil

      Ns.floats.apply(Set(1.0f, 2.0f), Set(2.0f)).get === List(Set(1.0f, 2.0f, 3.0f))
      Ns.floats.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List(Set(1.0f, 2.0f, 3.0f, 4.0f))
      Ns.floats.apply(Set(1.0f, 2.0f), Set(4.0f)).get === List(Set(1.0f, 2.0f, 3.0f, 4.0f))
      Ns.floats.apply(Set(1.0f, 2.0f), Set(2.0f), Set(3.0f)).get === List(Set(1.0f, 2.0f, 3.0f, 4.0f))

      Ns.floats.apply(Set(1.0f, 2.0f), Set(2.0f, 3.0f)).get === List(Set(1.0f, 2.0f, 3.0f))
      Ns.floats.apply(Set(1.0f, 2.0f), Set(2.0f, 4.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(Set(1.0f, 2.0f), Set(3.0f, 4.0f)).get === List(Set(1.0f, 2.0f, 3.0f, 4.0f))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.floats.apply(1.0f and 2.0f).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(1.0f and 3.0f).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.floats_.apply(1.0f).get === List(1)
      Ns.int.floats_.apply(2.0f).get === List(1, 2)
      Ns.int.floats_.apply(1.0f, 2.0f).get === List(1, 2)

      // `or`
      Ns.int.floats_.apply(1.0f or 2.0f).get === List(1, 2)
      Ns.int.floats_.apply(1.0f or 2.0f or 3.0f).get === List(1, 2, 3)

      // Seq
      Ns.int.floats_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.floats_.apply(Nil).get === List(4)
      Ns.int.floats_.apply(List(1.0f)).get === List(1)
      Ns.int.floats_.apply(List(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(List(1.0f, 2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(List(1.0f), List(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(List(1.0f, 2.0f), List(3.0f)).get === List(1, 2, 3)
      Ns.int.floats_.apply(List(1.0f), List(2.0f, 3.0f)).get === List(1, 2, 3)
      Ns.int.floats_.apply(List(1.0f, 2.0f, 3.0f)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.floats_.apply(Set[Float]()).get === List(4)
      Ns.int.floats_.apply(Set(1.0f)).get === List(1)
      Ns.int.floats_.apply(Set(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(Set(1.0f, 2.0f)).get === List(1)
      Ns.int.floats_.apply(Set(1.0f, 3.0f)).get === Nil
      Ns.int.floats_.apply(Set(2.0f, 3.0f)).get === List(2)
      Ns.int.floats_.apply(Set(1.0f, 2.0f, 3.0f)).get === Nil

      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List(1, 2, 3)
      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(4.0f)).get === List(1, 3)
      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(2.0f), Set(3.0f)).get === List(1, 2, 3)

      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(2.0f, 3.0f)).get === List(1, 2)
      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(2.0f, 4.0f)).get === List(1)
      Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(3.0f, 4.0f)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.floats_.apply(1.0f and 2.0f).get === List(1)
      Ns.int.floats_.apply(1.0f and 3.0f).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[Float]()

      val l1 = List(float1)
      val l2 = List(float2)

      val s1 = Set(float1)
      val s2 = Set(float2)

      val l12 = List(float1, float2)
      val l23 = List(float2, float3)

      val s12 = Set(float1, float2)
      val s23 = Set(float2, float3)


      // OR semantics

      // Vararg
      Ns.int.floats_.apply(float1, float2).get === List(1, 2)

      // `or`
      Ns.int.floats_.apply(float1 or float2).get === List(1, 2)

      // Seq
      Ns.int.floats_.apply(seq0).get === List(4)
      Ns.int.floats_.apply(List(float1), List(float2)).get === List(1, 2)
      Ns.int.floats_.apply(l1, l2).get === List(1, 2)
      Ns.int.floats_.apply(List(float1, float2)).get === List(1, 2)
      Ns.int.floats_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.floats_.apply(set0).get === List(4)

      Ns.int.floats_.apply(Set(float1)).get === List(1)
      Ns.int.floats_.apply(s1).get === List(1)

      Ns.int.floats_.apply(Set(float2)).get === List(1, 2)
      Ns.int.floats_.apply(s2).get === List(1, 2)

      Ns.int.floats_.apply(Set(float1, float2)).get === List(1)
      Ns.int.floats_.apply(s12).get === List(1)

      Ns.int.floats_.apply(Set(float2, float3)).get === List(2)
      Ns.int.floats_.apply(s23).get === List(2)

      Ns.int.floats_.apply(Set(float1, float2), Set(float2, float3)).get === List(1, 2)
      Ns.int.floats_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.floats_.apply(float1 and float2).get === List(1)
    }
  }


  "Implicit widening conversions, card one" in new CoreSetup {

    Ns.int.float insert List(
      (1, 1),
      (2, 1L),
      (3, 1f),
      (4, float2),
    )

    val res1 = List(
      (1, 1f),
      (2, 1f),
      (3, 1f),
    )
    val res2 = List((4, 2f))

    val res1t = List(1, 2, 3)
    val res2t = List(4)

    // Mandatory

    Ns.int.float(1).get.sortBy(_._1) === res1
    Ns.int.float.not(2).get.sortBy(_._1) === res1
    Ns.int.float.<(2).get.sortBy(_._1) === res1
    Ns.int.float.>(1).get === res2
    Ns.int.float.>=(2).get === res2
    Ns.int.float.<=(1).get.sortBy(_._1) === res1

    Ns.int.float(1L).get.sortBy(_._1) === res1
    Ns.int.float.not(2L).get.sortBy(_._1) === res1
    Ns.int.float.<(2L).get.sortBy(_._1) === res1
    Ns.int.float.>(1L).get === res2
    Ns.int.float.>=(2L).get === res2
    Ns.int.float.<=(1L).get.sortBy(_._1) === res1


    Ns.int.float(int1).get.sortBy(_._1) === res1
    Ns.int.float.not(int2).get.sortBy(_._1) === res1
    Ns.int.float.<(int2).get.sortBy(_._1) === res1
    Ns.int.float.>(int1).get === res2
    Ns.int.float.>=(int2).get === res2
    Ns.int.float.<=(int1).get.sortBy(_._1) === res1

    Ns.int.float(long1).get.sortBy(_._1) === res1
    Ns.int.float.not(long2).get.sortBy(_._1) === res1
    Ns.int.float.<(long2).get.sortBy(_._1) === res1
    Ns.int.float.>(long1).get === res2
    Ns.int.float.>=(long2).get === res2
    Ns.int.float.<=(long1).get.sortBy(_._1) === res1

    // Tacit

    Ns.int.float_(1).get.sorted === res1t
    Ns.int.float_.not(2).get.sorted === res1t
    Ns.int.float_.<(2).get.sorted === res1t
    Ns.int.float_.>(1).get === res2t
    Ns.int.float_.>=(2).get === res2t
    Ns.int.float_.<=(1).get.sorted === res1t

    Ns.int.float_(1L).get.sorted === res1t
    Ns.int.float_.not(2L).get.sorted === res1t
    Ns.int.float_.<(2L).get.sorted === res1t
    Ns.int.float_.>(1L).get === res2t
    Ns.int.float_.>=(2L).get === res2t
    Ns.int.float_.<=(1L).get.sorted === res1t


    Ns.int.float_(int1).get.sorted === res1t
    Ns.int.float_.not(int2).get.sorted === res1t
    Ns.int.float_.<(int2).get.sorted === res1t
    Ns.int.float_.>(int1).get === res2t
    Ns.int.float_.>=(int2).get === res2t
    Ns.int.float_.<=(int1).get.sorted === res1t

    Ns.int.float_(long1).get.sorted === res1t
    Ns.int.float_.not(long2).get.sorted === res1t
    Ns.int.float_.<(long2).get.sorted === res1t
    Ns.int.float_.>(long1).get === res2t
    Ns.int.float_.>=(long2).get === res2t
    Ns.int.float_.<=(long1).get.sorted === res1t
  }


  "Implicit widening conversions, card many" in new CoreSetup {

    Ns.int.floats insert List(
      (1, Set(1)),
      (2, Set(1L)),
      (3, Set(1f)),
      (4, Set(float2)),
    )

    val res1 = List(
      (1, Set(1f)),
      (2, Set(1f)),
      (3, Set(1f)),
    )
    val res2 = List((4, Set(2f)))

    val res1t = List(1, 2, 3)
    val res2t = List(4)

    // Mandatory

    Ns.int.floats(1).get.sortBy(_._1) === res1
    Ns.int.floats.not(2).get.sortBy(_._1) === res1
    Ns.int.floats.<(2).get.sortBy(_._1) === res1
    Ns.int.floats.>(1).get === res2
    Ns.int.floats.>=(2).get === res2
    Ns.int.floats.<=(1).get.sortBy(_._1) === res1

    Ns.int.floats(1L).get.sortBy(_._1) === res1
    Ns.int.floats.not(2L).get.sortBy(_._1) === res1
    Ns.int.floats.<(2L).get.sortBy(_._1) === res1
    Ns.int.floats.>(1L).get === res2
    Ns.int.floats.>=(2L).get === res2
    Ns.int.floats.<=(1L).get.sortBy(_._1) === res1


    Ns.int.floats(int1).get.sortBy(_._1) === res1
    Ns.int.floats.not(int2).get.sortBy(_._1) === res1
    Ns.int.floats.<(int2).get.sortBy(_._1) === res1
    Ns.int.floats.>(int1).get === res2
    Ns.int.floats.>=(int2).get === res2
    Ns.int.floats.<=(int1).get.sortBy(_._1) === res1

    Ns.int.floats(long1).get.sortBy(_._1) === res1
    Ns.int.floats.not(long2).get.sortBy(_._1) === res1
    Ns.int.floats.<(long2).get.sortBy(_._1) === res1
    Ns.int.floats.>(long1).get === res2
    Ns.int.floats.>=(long2).get === res2
    Ns.int.floats.<=(long1).get.sortBy(_._1) === res1

    // Tacit

    Ns.int.floats_(1).get.sorted === res1t
    Ns.int.floats_.not(2).get.sorted === res1t
    Ns.int.floats_.<(2).get.sorted === res1t
    Ns.int.floats_.>(1).get === res2t
    Ns.int.floats_.>=(2).get === res2t
    Ns.int.floats_.<=(1).get.sorted === res1t

    Ns.int.floats_(1L).get.sorted === res1t
    Ns.int.floats_.not(2L).get.sorted === res1t
    Ns.int.floats_.<(2L).get.sorted === res1t
    Ns.int.floats_.>(1L).get === res2t
    Ns.int.floats_.>=(2L).get === res2t
    Ns.int.floats_.<=(1L).get.sorted === res1t


    Ns.int.floats_(int1).get.sorted === res1t
    Ns.int.floats_.not(int2).get.sorted === res1t
    Ns.int.floats_.<(int2).get.sorted === res1t
    Ns.int.floats_.>(int1).get === res2t
    Ns.int.floats_.>=(int2).get === res2t
    Ns.int.floats_.<=(int1).get.sorted === res1t

    Ns.int.floats_(long1).get.sorted === res1t
    Ns.int.floats_.not(long2).get.sorted === res1t
    Ns.int.floats_.<(long2).get.sorted === res1t
    Ns.int.floats_.>(long1).get === res2t
    Ns.int.floats_.>=(long2).get === res2t
    Ns.int.floats_.<=(long1).get.sorted === res1t
  }
}