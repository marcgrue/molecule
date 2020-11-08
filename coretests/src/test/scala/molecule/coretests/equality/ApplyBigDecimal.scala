package molecule.coretests.equality

import molecule.datomic.peer.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class ApplyBigDecimal extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.bigDec$ insert List(
        (1, Some(bigDec1)),
        (2, Some(bigDec2)),
        (3, Some(bigDec3)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.bigDec.apply(bigDec1).get === List(bigDec1)
      Ns.bigDec.apply(bigDec2).get === List(bigDec2)
      Ns.bigDec.apply(bigDec1, bigDec2).get === List(bigDec1, bigDec2)

      // `or`
      Ns.bigDec.apply(bigDec1 or bigDec2).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(bigDec1 or bigDec2 or bigDec3).get === List(bigDec3, bigDec1, bigDec2)

      // Seq
      Ns.bigDec.apply().get === Nil
      Ns.bigDec.apply(Nil).get === Nil
      Ns.bigDec.apply(List(bigDec1)).get === List(bigDec1)
      Ns.bigDec.apply(List(bigDec2)).get === List(bigDec2)
      Ns.bigDec.apply(List(bigDec1, bigDec2)).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1), List(bigDec2)).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(bigDec3, bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(bigDec3, bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1, bigDec2, bigDec3)).get === List(bigDec3, bigDec1, bigDec2)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.bigDec_.apply(bigDec1).get === List(1)
      Ns.int.bigDec_.apply(bigDec2).get === List(2)
      Ns.int.bigDec_.apply(bigDec1, bigDec2).get === List(1, 2)

      // `or`
      Ns.int.bigDec_.apply(bigDec1 or bigDec2).get === List(1, 2)
      Ns.int.bigDec_.apply(bigDec1 or bigDec2 or bigDec3).get === List(1, 2, 3)

      // Seq
      Ns.int.bigDec_.apply().get === List(4)
      Ns.int.bigDec_.apply(Nil).get === List(4)
      Ns.int.bigDec_.apply(List(bigDec1)).get === List(1)
      Ns.int.bigDec_.apply(List(bigDec2)).get === List(2)
      Ns.int.bigDec_.apply(List(bigDec1, bigDec2)).get === List(1, 2)
      Ns.int.bigDec_.apply(List(bigDec1), List(bigDec2)).get === List(1, 2)
      Ns.int.bigDec_.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDec_.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDec_.apply(List(bigDec1, bigDec2, bigDec3)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.bigDecs$ insert List(
        (1, Some(Set(bigDec1, bigDec2))),
        (2, Some(Set(bigDec2, bigDec3))),
        (3, Some(Set(bigDec3, bigDec4))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.bigDecs.apply(bigDec1).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(bigDec2).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(bigDec1, bigDec2).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))

      // `or`
      Ns.int.bigDecs.apply(bigDec1 or bigDec2).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(bigDec1 or bigDec2 or bigDec3).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))

      // Seq
      Ns.int.bigDecs.apply().get === Nil
      Ns.int.bigDecs.apply(Nil).get === Nil
      Ns.int.bigDecs.apply(List(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(List(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(List(bigDec1, bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(List(bigDec1), List(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      Ns.int.bigDecs.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      Ns.int.bigDecs.apply(List(bigDec1, bigDec2, bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))


      // AND semantics

      // Set
      Ns.int.bigDecs.apply(Set[BigDecimal]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.bigDecs.apply(Set(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec3)).get === Nil
      Ns.int.bigDecs.apply(Set(bigDec2, bigDec3)).get === List((2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2, bigDec3)).get === Nil

      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set[BigDecimal]()).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec4)).get === List((1, Set(bigDec1, bigDec2)), (3, Set(bigDec3, bigDec4)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2), Set(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))

      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec4)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec3, bigDec4)).get === List((1, Set(bigDec1, bigDec2)), (3, Set(bigDec3, bigDec4)))

      // `and`
      Ns.int.bigDecs.apply(bigDec1 and bigDec2).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(bigDec1 and bigDec3).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.bigDecs.apply(bigDec1).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(bigDec2).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(bigDec1, bigDec2).get === List(Set(bigDec1, bigDec3, bigDec2))

      // `or`
      Ns.bigDecs.apply(bigDec1 or bigDec2).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(bigDec1 or bigDec2 or bigDec3).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))

      // Seq
      Ns.bigDecs.apply().get === Nil
      Ns.bigDecs.apply(Nil).get === Nil
      Ns.bigDecs.apply(List(bigDec1)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(List(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1, bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1), List(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1, bigDec2, bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))


      // AND semantics

      // Set
      Ns.bigDecs.apply(Set[BigDecimal]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.bigDecs.apply(Set(bigDec1)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(Set(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(Set(bigDec1, bigDec3)).get === Nil
      Ns.bigDecs.apply(Set(bigDec2, bigDec3)).get === List(Set(bigDec2, bigDec3))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2, bigDec3)).get === Nil

      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2)).get === List(Set(bigDec1, bigDec2, bigDec3))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec4)).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2), Set(bigDec3)).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4))

      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List(Set(bigDec1, bigDec2, bigDec3))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec4)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec3, bigDec4)).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.bigDecs.apply(bigDec1 and bigDec2).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(bigDec1 and bigDec3).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.bigDecs_.apply(bigDec1).get === List(1)
      Ns.int.bigDecs_.apply(bigDec2).get === List(1, 2)
      Ns.int.bigDecs_.apply(bigDec1, bigDec2).get === List(1, 2)

      // `or`
      Ns.int.bigDecs_.apply(bigDec1 or bigDec2).get === List(1, 2)
      Ns.int.bigDecs_.apply(bigDec1 or bigDec2 or bigDec3).get === List(1, 2, 3)

      // Seq
      Ns.int.bigDecs_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.bigDecs_.apply(Nil).get === List(4)
      Ns.int.bigDecs_.apply(List(bigDec1)).get === List(1)
      Ns.int.bigDecs_.apply(List(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1, bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1), List(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDecs_.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDecs_.apply(List(bigDec1, bigDec2, bigDec3)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.bigDecs_.apply(Set[BigDecimal]()).get === List(4)
      Ns.int.bigDecs_.apply(Set(bigDec1)).get === List(1)
      Ns.int.bigDecs_.apply(Set(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2)).get === List(1)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec3)).get === Nil
      Ns.int.bigDecs_.apply(Set(bigDec2, bigDec3)).get === List(2)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2, bigDec3)).get === Nil

      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec4)).get === List(1, 3)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec2), Set(bigDec3)).get === List(1, 2, 3)

      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List(1, 2)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec4)).get === List(1)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec3, bigDec4)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bigDecs_.apply(bigDec1 and bigDec2).get === List(1)
      Ns.int.bigDecs_.apply(bigDec1 and bigDec3).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[BigDecimal]()

      val l1 = List(bigDec1)
      val l2 = List(bigDec2)

      val s1 = Set(bigDec1)
      val s2 = Set(bigDec2)

      val l12 = List(bigDec1, bigDec2)
      val l23 = List(bigDec2, bigDec3)

      val s12 = Set(bigDec1, bigDec2)
      val s23 = Set(bigDec2, bigDec3)


      // OR semantics

      // Vararg
      Ns.int.bigDecs_.apply(bigDec1, bigDec2).get === List(1, 2)

      // `or`
      Ns.int.bigDecs_.apply(bigDec1 or bigDec2).get === List(1, 2)

      // Seq
      Ns.int.bigDecs_.apply(seq0).get === List(4)
      Ns.int.bigDecs_.apply(List(bigDec1), List(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(l1, l2).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1, bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.bigDecs_.apply(set0).get === List(4)

      Ns.int.bigDecs_.apply(Set(bigDec1)).get === List(1)
      Ns.int.bigDecs_.apply(s1).get === List(1)

      Ns.int.bigDecs_.apply(Set(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(s2).get === List(1, 2)

      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2)).get === List(1)
      Ns.int.bigDecs_.apply(s12).get === List(1)

      Ns.int.bigDecs_.apply(Set(bigDec2, bigDec3)).get === List(2)
      Ns.int.bigDecs_.apply(s23).get === List(2)

      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List(1, 2)
      Ns.int.bigDecs_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.bigDecs_.apply(bigDec1 and bigDec2).get === List(1)
    }
  }


  "Implicit widening conversions, card one" in new CoreSetup {

    Ns.int.bigDec insert List(
      (1, 1),
      (2, 1L),
      (3, 1f),
      (4, 1.0),
      (5, BigDecimal(1)),
      (6, BigDecimal(1.0)),
      (7, bigDec2),
    )

    val res1 = List(
      (1, BigDecimal(1)),
      (2, BigDecimal(1)),
      (3, BigDecimal(1)),
      (4, BigDecimal(1)),
      (5, BigDecimal(1)),
      (6, BigDecimal(1)),
    )
    val res2 = List((7, BigDecimal(2)))

    val i1 = 1
    val i2 = 2
    val d1 = 1.0
    val x1 = BigDecimal(1)
    val y1 = BigDecimal(1.0)

    Ns.int.bigDec(1).get.sortBy(_._1) === res1
    Ns.int.bigDec(1L).get.sortBy(_._1) === res1
    Ns.int.bigDec(1f).get.sortBy(_._1) === res1
    Ns.int.bigDec(1.0).get.sortBy(_._1) === res1

    Ns.int.bigDec(i1).get.sortBy(_._1) === res1
    Ns.int.bigDec(d1).get.sortBy(_._1) === res1
    Ns.int.bigDec(x1).get.sortBy(_._1) === res1
    Ns.int.bigDec(y1).get.sortBy(_._1) === res1

    Ns.int.bigDec.not(i2).get.sortBy(_._1) === res1
    Ns.int.bigDec.<(i2).get.sortBy(_._1) === res1
    Ns.int.bigDec.>(i1).get === res2
    Ns.int.bigDec.>=(i2).get === res2
    Ns.int.bigDec.<=(i1).get.sortBy(_._1) === res1

    Ns.int.bigDec.not(2).get.sortBy(_._1) === res1
    Ns.int.bigDec.<(2).get.sortBy(_._1) === res1
    Ns.int.bigDec.>(1).get === res2
    Ns.int.bigDec.>=(2).get === res2
    Ns.int.bigDec.<=(1).get.sortBy(_._1) === res1


    val res1t = List(1, 2, 3, 4, 5, 6)
    val res2t = List(7)

    Ns.int.bigDec_(1).get.sorted === res1t
    Ns.int.bigDec_(1L).get.sorted === res1t
    Ns.int.bigDec_(1f).get.sorted === res1t
    Ns.int.bigDec_(1.0).get.sorted === res1t

    Ns.int.bigDec_(i1).get.sorted === res1t
    Ns.int.bigDec_(d1).get.sorted === res1t
    Ns.int.bigDec_(x1).get.sorted === res1t
    Ns.int.bigDec_(y1).get.sorted === res1t

    Ns.int.bigDec_.not(i2).get.sorted === res1t
    Ns.int.bigDec_.<(i2).get.sorted === res1t
    Ns.int.bigDec_.>(i1).get === res2t
    Ns.int.bigDec_.>=(i2).get === res2t
    Ns.int.bigDec_.<=(i1).get.sorted === res1t

    Ns.int.bigDec_.not(2).get.sorted === res1t
    Ns.int.bigDec_.<(2).get.sorted === res1t
    Ns.int.bigDec_.>(1).get === res2t
    Ns.int.bigDec_.>=(2).get === res2t
    Ns.int.bigDec_.<=(1).get.sorted === res1t
  }


  "Implicit widening conversions, card many" in new CoreSetup {

    Ns.int.bigDecs insert List(
      (1, Set(1)),
      (2, Set(1L)),
      (3, Set(1f)),
      (4, Set(1.0)),
      (5, Set(BigDecimal(1))),
      (6, Set(BigDecimal(1.0))),
      (7, Set(bigDec2)),
    )

    val res1 = List(
      (1, Set(BigDecimal(1))),
      (2, Set(BigDecimal(1))),
      (3, Set(BigDecimal(1))),
      (4, Set(BigDecimal(1))),
      (5, Set(BigDecimal(1))),
      (6, Set(BigDecimal(1))),
    )
    val res2 = List((7, Set(BigDecimal(2))))

    val i1 = 1
    val i2 = 2
    val d1 = 1.0
    val x1 = BigDecimal(1)
    val y1 = BigDecimal(1.0)

    Ns.int.bigDecs(1).get.sortBy(_._1) === res1
    Ns.int.bigDecs(1L).get.sortBy(_._1) === res1
    Ns.int.bigDecs(1f).get.sortBy(_._1) === res1
    Ns.int.bigDecs(1.0).get.sortBy(_._1) === res1

    Ns.int.bigDecs(i1).get.sortBy(_._1) === res1
    Ns.int.bigDecs(d1).get.sortBy(_._1) === res1
    Ns.int.bigDecs(x1).get.sortBy(_._1) === res1
    Ns.int.bigDecs(y1).get.sortBy(_._1) === res1

    Ns.int.bigDecs.not(i2).get.sortBy(_._1) === res1
    Ns.int.bigDecs.<(i2).get.sortBy(_._1) === res1
    Ns.int.bigDecs.>(i1).get === res2
    Ns.int.bigDecs.>=(i2).get === res2
    Ns.int.bigDecs.<=(i1).get.sortBy(_._1) === res1

    Ns.int.bigDecs.not(2).get.sortBy(_._1) === res1
    Ns.int.bigDecs.<(2).get.sortBy(_._1) === res1
    Ns.int.bigDecs.>(1).get === res2
    Ns.int.bigDecs.>=(2).get === res2
    Ns.int.bigDecs.<=(1).get.sortBy(_._1) === res1


    val res1t = List(1, 2, 3, 4, 5, 6)
    val res2t = List(7)

    Ns.int.bigDecs_(1).get.sorted === res1t
    Ns.int.bigDecs_(1L).get.sorted === res1t
    Ns.int.bigDecs_(1f).get.sorted === res1t
    Ns.int.bigDecs_(1.0).get.sorted === res1t

    Ns.int.bigDecs_(i1).get.sorted === res1t
    Ns.int.bigDecs_(d1).get.sorted === res1t
    Ns.int.bigDecs_(x1).get.sorted === res1t
    Ns.int.bigDecs_(y1).get.sorted === res1t

    Ns.int.bigDecs_.not(i2).get.sorted === res1t
    Ns.int.bigDecs_.<(i2).get.sorted === res1t
    Ns.int.bigDecs_.>(i1).get === res2t
    Ns.int.bigDecs_.>=(i2).get === res2t
    Ns.int.bigDecs_.<=(i1).get.sorted === res1t

    Ns.int.bigDecs_.not(2).get.sorted === res1t
    Ns.int.bigDecs_.<(2).get.sorted === res1t
    Ns.int.bigDecs_.>(1).get === res2t
    Ns.int.bigDecs_.>=(2).get === res2t
    Ns.int.bigDecs_.<=(1).get.sorted === res1t
  }
}