package molecule.coretests.manipulation

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class Retract extends CoreSpec {


  "Entity" in new CoreSetup {

    val e1 = Ns.int.str insert List(
      (1, "a"),
      (2, "b")
    ) eid

    Ns.int.str.get.sorted === List(
      (1, "a"),
      (2, "b")
    )

    e1.retract

    Ns.int.str.get === List(
      (2, "b")
    )
  }


  "Card-one component" in new CoreSetup {

    val e1 = Ns.int.RefSub1.int1 insert List(
      (1, 10),
      (2, 20)
    ) eid

    Ns.int.RefSub1.int1.get.sorted === List(
      (1, 10),
      (2, 20)
    )
    Ref1.int1.get.sorted === List(10, 20)

    e1.retract

    Ns.int.RefSub1.int1.get === List(
      (2, 20)
    )
    // Card-one sub-entity was retracted
    Ref1.int1.get === List(20)
  }


  "Card-many components" in new CoreSetup {

    val e1 = m(Ns.int.RefsSub1 * Ref1.int1).insert(List(
      (1, Seq(10, 11)),
      (2, Seq(20, 21))
    )).eid

    m(Ns.int.RefsSub1 * Ref1.int1).get.sortBy(_._1) === List(
      (1, Seq(10, 11)),
      (2, Seq(20, 21))
    )
    Ref1.int1.get.sorted === List(10, 11, 20, 21)

    e1.retract

    m(Ns.int.RefsSub1 * Ref1.int1).get === List(
      (2, Seq(20, 21))
    )
    // Card-many sub-entitities were retracted
    Ref1.int1.get === List(20, 21)
  }


  "Card-many components nested" in new CoreSetup {

    val e1 = Ns.int.RefsSub1.*(Ref1.int1.RefsSub2.*(Ref2.int2)).insert(List(
      (1, Seq(
        (10, Seq(100, 101)),
        (11, Seq(110, 111)))),
      (2, Seq(
        (20, Seq(200, 201)),
        (21, Seq(210, 211))))
    )).eid

    Ns.int.RefsSub1.*(Ref1.int1.RefsSub2.*(Ref2.int2)).get === List(
      (1, Seq(
        (10, Seq(100, 101)),
        (11, Seq(110, 111)))),
      (2, Seq(
        (20, Seq(200, 201)),
        (21, Seq(210, 211))))
    )
    Ref1.int1.get.sorted === List(10, 11, 20, 21)
    Ref2.int2.get.sorted === List(100, 101, 110, 111, 200, 201, 210, 211)

    e1.retract

    Ns.int.RefsSub1.*(Ref1.int1.RefsSub2.*(Ref2.int2)).get === List(
      (2, Seq(
        (20, Seq(200, 201)),
        (21, Seq(210, 211))))
    )
    Ref1.int1.get.sorted === List(20, 21)
    Ref2.int2.get.sorted === List(200, 201, 210, 211)
  }


  "Orphan references" in new CoreSetup {

    // Create ref
    val List(e1,r1) = Ns.int(1).Ref1.int1(10).save.eids
    r1.retract
    // Ref entity with attribute values is gone - no ref orphan exist
    Ns.int(1).ref1$.get.head === (1, None)


    // Create another ref
    val List(e2,r2) = Ns.int(2).Ref1.int1(20).save.eids

    // Retract attribute value from ref entity - ref entity still exist
    Ref1(r2).int1().update

    // Ref entity r2 is now an orphan
    // Entity e2 still has a reference to r2
    Ns.int(2).ref1$.get.head === (2, Some(r2))
    // r2 has no attribute values
    Ns.int(2).Ref1.int1$.get.head === (2, None)

    // Add attribute value to ref entity again
    Ref1(r2).int1(21).update

    // Ref entity is no longer an orphan
    Ns.int(2).Ref1.e.int1.get.head === (2, r2, 21)
  }
}
