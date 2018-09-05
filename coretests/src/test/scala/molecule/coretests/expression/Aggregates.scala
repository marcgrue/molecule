package molecule.coretests.expression

import molecule.api._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class Aggregates extends CoreSpec {

  class ManySetup extends CoreSetup {

    val (a, b, c, d) = ("a", "b", "c", "d")

    // We pair cardinality many attribute values with card-one's too to be able to group by cardinality one values
    Ns.str.strs insert List(
      ("str1", Set("a", "b")),
      ("str2", Set("b", "c")),
      ("str3", Set("ba", "d")),
      ("str4", Set.empty[String])
    )

    Ns.int.ints insert List(
      (1, Set(1, 2)),
      (2, Set(2, 3)),
      (3, Set(2, 4)))

    Ns.long.longs insert List(
      (1L, Set(1L, 2L)),
      (2L, Set(2L, 3L)),
      (3L, Set(2L, 4L)))

    Ns.float.floats insert List(
      (1.0f, Set(1.0f, 2.0f)),
      (2.0f, Set(2.0f, 3.0f)),
      (3.0f, Set(2.5f, 4.0f)))

    Ns.double.doubles insert List(
      (1.0, Set(1.0, 2.0)),
      (2.0, Set(2.0, 3.0)),
      (3.0, Set(2.5, 4.0)))

    // Set of boolean values maybe not so useful
    Ns.bool.bools insert List(
      (false, Set(false)),
      (true, Set(false, true)))

    Ns.date.dates insert List(
      (date1, Set(date1, date2)),
      (date2, Set(date2, date3)),
      (date3, Set(date2, date4)))

    Ns.uuid.uuids insert List(
      (uuid1, Set(uuid1, uuid2)),
      (uuid2, Set(uuid2, uuid3)),
      (uuid3, Set(uuid2, uuid4)))

    Ns.uri.uris insert List(
      (uri1, Set(uri1, uri2)),
      (uri2, Set(uri2, uri3)),
      (uri3, Set(uri2, uri4)))

    Ns.enum.enums insert List(
      ("enum1", Set("enum1", "enum2")),
      ("enum2", Set("enum2", "enum3")),
      ("enum3", Set("enum2", "enum4")))
  }


  "Distinct" in new CoreSetup {

    Ns.str.int insert List(
      ("a", 1),
      ("b", 2),
      ("b", 2),
      ("b", 3)
    )
    Ns.int(4).save


    Ns.str.int.get.sortBy(r => (r._1, r._2)) === List(
      ("a", 1),
      ("b", 2),
      ("b", 3)
    )

    Ns.e.str.int.get.map(r => (r._2, r._3)).sortBy(r => (r._1, r._2)) === List(
      ("a", 1),
      ("b", 2),
      ("b", 2),
      ("b", 3)
    )

    Ns.str.int(distinct).get.sortBy(_._1) === List(
      ("a", Vector(1)),
      ("b", Vector(3, 2)),
    )

    Ns.int.str(distinct).get.sortBy(_._1) === List(
      (1, Vector("a")),
      (2, Vector("b")),
      (3, Vector("b"))
    )

    Ns.str.int(count).get.sortBy(_._1) === List(
      ("a", 1),
      ("b", 3)
    )
    Ns.str.int(countDistinct).get.sortBy(_._1) === List(
      ("a", 1),
      ("b", 2)
    )

    Ns.int.str(count).get.sortBy(_._1) === List(
      (1, 1),
      (2, 2),
      (3, 1)
    )
    Ns.int.str(countDistinct).get.sortBy(_._1) === List(
      (1, 1),
      (2, 1),
      (3, 1)
    )

    Ns.str(distinct).int(distinct).get === List(
      (Vector("a", "b"), Vector(1, 3, 2)),
    )

    Ns.str(count).str(countDistinct).get === List(
      (4, 2),
    )

    // extra int without a str value was saved
    Ns.int(count).int(countDistinct).get === List(
      (5, 4),
    )

    Ns.int(count).get === List(5)
    Ns.int(countDistinct).get === List(4)


    Ns.str_(Nil).int.get === List(4)
  }
}