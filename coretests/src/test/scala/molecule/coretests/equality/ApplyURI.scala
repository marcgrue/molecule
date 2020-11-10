package molecule.coretests.equality

import java.net.URI
import molecule.datomic.peer.api.out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class ApplyURI extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.uri$ insert List(
        (1, Some(uri1)),
        (2, Some(uri2)),
        (3, Some(uri3)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.uri.apply(uri1).get === List(uri1)
      Ns.uri.apply(uri2).get === List(uri2)
      Ns.uri.apply(uri1, uri2).get === List(uri1, uri2)

      // `or`
      Ns.uri.apply(uri1 or uri2).get === List(uri1, uri2)
      Ns.uri.apply(uri1 or uri2 or uri3).get === List(uri3, uri1, uri2)

      // Seq
      Ns.uri.apply().get === Nil
      Ns.uri.apply(Nil).get === Nil
      Ns.uri.apply(List(uri1)).get === List(uri1)
      Ns.uri.apply(List(uri2)).get === List(uri2)
      Ns.uri.apply(List(uri1, uri2)).get === List(uri1, uri2)
      Ns.uri.apply(List(uri1), List(uri2)).get === List(uri1, uri2)
      Ns.uri.apply(List(uri1, uri2), List(uri3)).get === List(uri3, uri1, uri2)
      Ns.uri.apply(List(uri1), List(uri2, uri3)).get === List(uri3, uri1, uri2)
      Ns.uri.apply(List(uri1, uri2, uri3)).get === List(uri3, uri1, uri2)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.uri_.apply(uri1).get === List(1)
      Ns.int.uri_.apply(uri2).get === List(2)
      Ns.int.uri_.apply(uri1, uri2).get === List(1, 2)

      // `or`
      Ns.int.uri_.apply(uri1 or uri2).get === List(1, 2)
      Ns.int.uri_.apply(uri1 or uri2 or uri3).get === List(1, 2, 3)

      // Seq
      Ns.int.uri_.apply().get === List(4)
      Ns.int.uri_.apply(Nil).get === List(4)
      Ns.int.uri_.apply(List(uri1)).get === List(1)
      Ns.int.uri_.apply(List(uri2)).get === List(2)
      Ns.int.uri_.apply(List(uri1, uri2)).get === List(1, 2)
      Ns.int.uri_.apply(List(uri1), List(uri2)).get === List(1, 2)
      Ns.int.uri_.apply(List(uri1, uri2), List(uri3)).get === List(1, 2, 3)
      Ns.int.uri_.apply(List(uri1), List(uri2, uri3)).get === List(1, 2, 3)
      Ns.int.uri_.apply(List(uri1, uri2, uri3)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.uris$ insert List(
        (1, Some(Set(uri1, uri2))),
        (2, Some(Set(uri2, uri3))),
        (3, Some(Set(uri3, uri4))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.uris.apply(uri1).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(uri2).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(uri1, uri2).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))

      // `or`
      Ns.int.uris.apply(uri1 or uri2).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(uri1 or uri2 or uri3).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))

      // Seq
      Ns.int.uris.apply().get === Nil
      Ns.int.uris.apply(Nil).get === Nil
      Ns.int.uris.apply(List(uri1)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(List(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(List(uri1, uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(List(uri1), List(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(List(uri1, uri2), List(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      Ns.int.uris.apply(List(uri1), List(uri2, uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      Ns.int.uris.apply(List(uri1, uri2, uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))


      // AND semantics

      // Set
      Ns.int.uris.apply(Set[URI]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.uris.apply(Set(uri1)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(Set(uri1, uri2)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(Set(uri1, uri3)).get === Nil
      Ns.int.uris.apply(Set(uri2, uri3)).get === List((2, Set(uri2, uri3)))
      Ns.int.uris.apply(Set(uri1, uri2, uri3)).get === Nil

      Ns.int.uris.apply(Set(uri1, uri2), Set[URI]()).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(Set(uri1, uri2), Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(Set(uri1, uri2), Set(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      Ns.int.uris.apply(Set(uri1, uri2), Set(uri4)).get === List((1, Set(uri1, uri2)), (3, Set(uri3, uri4)))
      Ns.int.uris.apply(Set(uri1, uri2), Set(uri2), Set(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))

      Ns.int.uris.apply(Set(uri1, uri2), Set(uri2, uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(Set(uri1, uri2), Set(uri2, uri4)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(Set(uri1, uri2), Set(uri3, uri4)).get === List((1, Set(uri1, uri2)), (3, Set(uri3, uri4)))

      // `and`
      Ns.int.uris.apply(uri1 and uri2).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(uri1 and uri3).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.uris.apply(uri1).get === List(Set(uri1, uri2))
      Ns.uris.apply(uri2).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(uri1, uri2).get === List(Set(uri1, uri3, uri2))

      // `or`
      Ns.uris.apply(uri1 or uri2).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(uri1 or uri2 or uri3).get === List(Set(uri1, uri4, uri3, uri2))

      // Seq
      Ns.uris.apply().get === Nil
      Ns.uris.apply(Nil).get === Nil
      Ns.uris.apply(List(uri1)).get === List(Set(uri1, uri2))
      Ns.uris.apply(List(uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(List(uri1, uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(List(uri1), List(uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(List(uri1, uri2), List(uri3)).get === List(Set(uri1, uri4, uri3, uri2))
      Ns.uris.apply(List(uri1), List(uri2, uri3)).get === List(Set(uri1, uri4, uri3, uri2))
      Ns.uris.apply(List(uri1, uri2, uri3)).get === List(Set(uri1, uri4, uri3, uri2))


      // AND semantics

      // Set
      Ns.uris.apply(Set[URI]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.uris.apply(Set(uri1)).get === List(Set(uri1, uri2))
      Ns.uris.apply(Set(uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(Set(uri1, uri2)).get === List(Set(uri1, uri2))
      Ns.uris.apply(Set(uri1, uri3)).get === Nil
      Ns.uris.apply(Set(uri2, uri3)).get === List(Set(uri2, uri3))
      Ns.uris.apply(Set(uri1, uri2, uri3)).get === Nil

      Ns.uris.apply(Set(uri1, uri2), Set(uri2)).get === List(Set(uri1, uri2, uri3))
      Ns.uris.apply(Set(uri1, uri2), Set(uri3)).get === List(Set(uri1, uri2, uri3, uri4))
      Ns.uris.apply(Set(uri1, uri2), Set(uri4)).get === List(Set(uri1, uri2, uri3, uri4))
      Ns.uris.apply(Set(uri1, uri2), Set(uri2), Set(uri3)).get === List(Set(uri1, uri2, uri3, uri4))

      Ns.uris.apply(Set(uri1, uri2), Set(uri2, uri3)).get === List(Set(uri1, uri2, uri3))
      Ns.uris.apply(Set(uri1, uri2), Set(uri2, uri4)).get === List(Set(uri1, uri2))
      Ns.uris.apply(Set(uri1, uri2), Set(uri3, uri4)).get === List(Set(uri1, uri2, uri3, uri4))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.uris.apply(uri1 and uri2).get === List(Set(uri1, uri2))
      Ns.uris.apply(uri1 and uri3).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.uris_.apply(uri1).get === List(1)
      Ns.int.uris_.apply(uri2).get === List(1, 2)
      Ns.int.uris_.apply(uri1, uri2).get === List(1, 2)

      // `or`
      Ns.int.uris_.apply(uri1 or uri2).get === List(1, 2)
      Ns.int.uris_.apply(uri1 or uri2 or uri3).get === List(1, 2, 3)

      // Seq
      Ns.int.uris_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.uris_.apply(Nil).get === List(4)
      Ns.int.uris_.apply(List(uri1)).get === List(1)
      Ns.int.uris_.apply(List(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1, uri2)).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1), List(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1, uri2), List(uri3)).get === List(1, 2, 3)
      Ns.int.uris_.apply(List(uri1), List(uri2, uri3)).get === List(1, 2, 3)
      Ns.int.uris_.apply(List(uri1, uri2, uri3)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.uris_.apply(Set[URI]()).get === List(4)
      Ns.int.uris_.apply(Set(uri1)).get === List(1)
      Ns.int.uris_.apply(Set(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(Set(uri1, uri2)).get === List(1)
      Ns.int.uris_.apply(Set(uri1, uri3)).get === Nil
      Ns.int.uris_.apply(Set(uri2, uri3)).get === List(2)
      Ns.int.uris_.apply(Set(uri1, uri2, uri3)).get === Nil

      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri3)).get === List(1, 2, 3)
      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri4)).get === List(1, 3)
      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri2), Set(uri3)).get === List(1, 2, 3)

      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri2, uri3)).get === List(1, 2)
      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri2, uri4)).get === List(1)
      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri3, uri4)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.uris_.apply(uri1 and uri2).get === List(1)
      Ns.int.uris_.apply(uri1 and uri3).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[URI]()

      val l1 = List(uri1)
      val l2 = List(uri2)

      val s1 = Set(uri1)
      val s2 = Set(uri2)

      val l12 = List(uri1, uri2)
      val l23 = List(uri2, uri3)

      val s12 = Set(uri1, uri2)
      val s23 = Set(uri2, uri3)


      // OR semantics

      // Vararg
      Ns.int.uris_.apply(uri1, uri2).get === List(1, 2)

      // `or`
      Ns.int.uris_.apply(uri1 or uri2).get === List(1, 2)

      // Seq
      Ns.int.uris_.apply(seq0).get === List(4)
      Ns.int.uris_.apply(List(uri1), List(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(l1, l2).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1, uri2)).get === List(1, 2)
      Ns.int.uris_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.uris_.apply(set0).get === List(4)

      Ns.int.uris_.apply(Set(uri1)).get === List(1)
      Ns.int.uris_.apply(s1).get === List(1)

      Ns.int.uris_.apply(Set(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(s2).get === List(1, 2)

      Ns.int.uris_.apply(Set(uri1, uri2)).get === List(1)
      Ns.int.uris_.apply(s12).get === List(1)

      Ns.int.uris_.apply(Set(uri2, uri3)).get === List(2)
      Ns.int.uris_.apply(s23).get === List(2)

      Ns.int.uris_.apply(Set(uri1, uri2), Set(uri2, uri3)).get === List(1, 2)
      Ns.int.uris_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.uris_.apply(uri1 and uri2).get === List(1)
    }
  }
}