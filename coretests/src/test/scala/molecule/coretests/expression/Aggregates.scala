package molecule.coretests.expression

import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out3._

class Aggregates extends CoreSpec {

  class AggregateSetup extends CoreSetup {

    // We pair cardinality many attribute values with card-one's
    // too to be able to group by cardinality one values
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
      (1.1f, Set(1.1f, 2.2f)),
      (2.2f, Set(2.2f, 3.3f)),
      (3.3f, Set(3.3f, 4.4f)))

    Ns.double.doubles insert List(
      (1.1, Set(1.1, 2.2)),
      (2.2, Set(2.2, 3.3)),
      (3.3, Set(3.3, 4.4)))

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
      (enum1, Set(enum1, enum2)),
      (enum2, Set(enum2, enum3)),
      (enum3, Set(enum2, enum4)))

    Ns.bigInt.bigInts insert List(
      (bigInt1, Set(bigInt1, bigInt2)),
      (bigInt2, Set(bigInt2, bigInt3)),
      (bigInt3, Set(bigInt2, bigInt4)))

    Ns.bigDec.bigDecs insert List(
      (bigDec1, Set(bigDec1, bigDec2)),
      (bigDec2, Set(bigDec2, bigDec3)),
      (bigDec3, Set(bigDec2, bigDec4)))
  }


  "sum" in new AggregateSetup {
    Ns.int(sum).get.head === (1 + 2 + 3)
    Ns.long(sum).get.head === 6L
    Ns.float(sum).get.head === 6.6f
    Ns.double(sum).get.head === 6.6
  }


  "median" in new AggregateSetup {
    Ns.int(median).get.head === 2
    Ns.long(median).get.head === 2L
    Ns.float(median).get.head === 2.2f
    Ns.double(median).get.head === 2.2
  }


  "avg" in new AggregateSetup {
    Ns.int(avg).get.head === (1 + 2 + 3) / 3
    Ns.long(avg).get.head === 2
    Ns.float(avg).get.head === 2.1999999999999997 // datomic precision
    Ns.double(avg).get.head === 2.1999999999999997 // datomic precision
  }


  "variance" in new AggregateSetup {
    Ns.int(variance).get.head === 0.6666666666666666
    Ns.long(variance).get.head === 0.6666666666666666
    Ns.float(variance).get.head === 0.8066666666666665
    Ns.double(variance).get.head === 0.8066666666666665
  }


  "stddev" in new AggregateSetup {
    Ns.int(stddev).get.head === 0.816496580927726
    Ns.long(stddev).get.head === 0.816496580927726
    Ns.float(stddev).get.head === 0.8981462390204986
    Ns.double(stddev).get.head === 0.8981462390204986
  }


  "min" in new AggregateSetup {

    // card one

    Ns.str(min).get.head === "str1"
    Ns.int(min).get.head === 1
    Ns.long(min).get.head === 1L
    Ns.float(min).get.head === 1.1f
    Ns.double(min).get.head === 1.1
    Ns.bool(min).get.head === false
    Ns.date(min).get.head === date1
    Ns.uuid(min).get.head === uuid1
    Ns.uri(min).get.head === uri1
    Ns.enum(min).get.head === enum1
    Ns.bigInt(min).get.head === bigInt1
    Ns.bigDec(min).get.head === bigDec1

    Ns.str(min(2)).get.head === List("str1", "str2")
    Ns.int(min(2)).get.head === List(1, 2)
    Ns.long(min(2)).get.head === List(1L, 2L)
    Ns.float(min(2)).get.head === List(1.1f, 2.2f)
    Ns.double(min(2)).get.head === List(1.1, 2.2)
    Ns.bool(min(2)).get.head === List(false, true)
    Ns.date(min(2)).get.head === List(date1, date2)
    Ns.uuid(min(2)).get.head === List(uuid1, uuid2)
    Ns.uri(min(2)).get.head === List(uri1, uri2)
    Ns.enum(min(2)).get.head === List(enum1, enum2)
    Ns.bigInt(min(2)).get.head === List(bigInt1, bigInt2)
    Ns.bigDec(min(2)).get.head === List(bigDec1, bigDec2)


    // card many

    Ns.strs(min).get.head === Set("a")
    Ns.ints(min).get.head === Set(1)
    Ns.longs(min).get.head === Set(1L)
    Ns.floats(min).get.head === Set(1.1f)
    Ns.doubles(min).get.head === Set(1.1)
    Ns.bools(min).get.head === Set(false)
    Ns.dates(min).get.head === Set(date1)
    Ns.uuids(min).get.head === Set(uuid1)
    Ns.uris(min).get.head === Set(uri1)
    Ns.enums(min).get.head === Set(enum1)
    Ns.bigInts(min).get.head === Set(bigInt1)
    Ns.bigDecs(min).get.head === Set(bigDec1)

    Ns.strs(min(2)).get.head === List(Set("a", "b"))
    Ns.ints(min(2)).get.head === List(Set(1, 2))
    Ns.longs(min(2)).get.head === List(Set(1L, 2L))
    Ns.floats(min(2)).get.head === List(Set(1.1f, 2.2f))
    Ns.doubles(min(2)).get.head === List(Set(1.1, 2.2))
    Ns.bools(min(2)).get.head === List(Set(false, true))
    Ns.dates(min(2)).get.head === List(Set(date1, date2))
    Ns.uuids(min(2)).get.head === List(Set(uuid1, uuid2))
    Ns.uris(min(2)).get.head === List(Set(uri1, uri2))
    Ns.enums(min(2)).get.head === List(Set(enum1, enum2))
    Ns.bigInts(min(2)).get.head === List(Set(bigInt1, bigInt2))
    Ns.bigDecs(min(2)).get.head === List(Set(bigDec1, bigDec2))


    // combinations

    Ns.int(min).ints.get === List(
      (1, Set(1, 4, 3, 2))
    )

    Ns.int(min(2)).ints.get === List(
      (List(1, 1), Set(1, 4, 3, 2))
    )


    Ns.int.ints(min).get === List(
      (1, Set(1)),
      (2, Set(2)),
      (3, Set(2))
    )

    Ns.int.ints(min(2)).get === List(
      (1, List(Set(1, 2))),
      (2, List(Set(2, 3))),
      (3, List(Set(2, 4)))
    )


    Ns.int(min).ints(min).get === List(
      (1, Set(1))
    )

    Ns.int(min(2)).ints(min).get === List(
      (List(1, 1), Set(1))
    )

    Ns.int(min).ints(min(2)).get === List(
      (1, List(Set(1, 2)))
    )

    Ns.int(min(2)).ints(min(2)).get === List(
      (List(1, 1), List(Set(1, 2)))
    )
  }


  "max" in new AggregateSetup {

    // card one

    Ns.str(max).get.head === "str4"
    Ns.int(max).get.head === 3
    Ns.long(max).get.head === 3L
    Ns.float(max).get.head === 3.3f
    Ns.double(max).get.head === 3.3
    Ns.bool(max).get.head === true
    Ns.date(max).get.head === date3
    Ns.uuid(max).get.head === uuid3
    Ns.uri(max).get.head === uri3
    Ns.enum(max).get.head === enum3
    Ns.bigInt(max).get.head === bigInt3
    Ns.bigDec(max).get.head === bigDec3

    Ns.str(max(2)).get.head === List("str4", "str3")
    Ns.int(max(2)).get.head === List(3, 2)
    Ns.long(max(2)).get.head === List(3L, 2L)
    Ns.float(max(2)).get.head === List(3.3f, 2.2f)
    Ns.double(max(2)).get.head === List(3.3, 2.2)
    Ns.bool(max(2)).get.head === List(true, false)
    Ns.date(max(2)).get.head === List(date3, date2)
    Ns.uuid(max(2)).get.head === List(uuid3, uuid2)
    Ns.uri(max(2)).get.head === List(uri3, uri2)
    Ns.enum(max(2)).get.head === List(enum3, enum2)
    Ns.bigInt(max(2)).get.head === List(bigInt3, bigInt2)
    Ns.bigDec(max(2)).get.head === List(bigDec3, bigDec2)


    // card many

    Ns.strs(max).get.head === Set("d")
    Ns.ints(max).get.head === Set(4)
    Ns.longs(max).get.head === Set(4L)
    Ns.floats(max).get.head === Set(4.4f)
    Ns.doubles(max).get.head === Set(4.4)
    Ns.bools(max).get.head === Set(true)
    Ns.dates(max).get.head === Set(date4)
    Ns.uuids(max).get.head === Set(uuid4)
    Ns.uris(max).get.head === Set(uri4)
    Ns.enums(max).get.head === Set(enum4)
    Ns.bigInts(max).get.head === Set(bigInt4)
    Ns.bigDecs(max).get.head === Set(bigDec4)

    Ns.strs(max(2)).get.head === List(Set("d", "c"))
    Ns.ints(max(2)).get.head === List(Set(4, 3))
    Ns.longs(max(2)).get.head === List(Set(4L, 3L))
    Ns.floats(max(2)).get.head === List(Set(4.4f, 3.3f))
    Ns.doubles(max(2)).get.head === List(Set(4.4, 3.3))
    Ns.bools(max(2)).get.head === List(Set(true, false))
    Ns.dates(max(2)).get.head === List(Set(date4, date3))
    Ns.uuids(max(2)).get.head === List(Set(uuid4, uuid3))
    Ns.uris(max(2)).get.head === List(Set(uri4, uri3))
    Ns.enums(max(2)).get.head === List(Set(enum4, enum3))
    Ns.bigInts(max(2)).get.head === List(Set(bigInt4, bigInt3))
    Ns.bigDecs(max(2)).get.head === List(Set(bigDec4, bigDec3))


    // combinations

    Ns.int(max).ints.get === List(
      (3, Set(1, 4, 3, 2))
    )

    Ns.int(max(2)).ints.get === List(
      (List(3, 3), Set(1, 4, 3, 2))
    )


    Ns.int.ints(max).get === List(
      (1, Set(2)),
      (2, Set(3)),
      (3, Set(4))
    )

    Ns.int.ints(max(2)).get === List(
      (1, List(Set(1, 2))),
      (2, List(Set(2, 3))),
      (3, List(Set(2, 4)))
    )


    Ns.int(max).ints(max).get === List(
      (3, Set(4))
    )

    Ns.int(max(2)).ints(max).get === List(
      (List(3, 3), Set(4))
    )

    Ns.int(max).ints(max(2)).get === List(
      (3, List(Set(4, 3)))
    )

    Ns.int(max(2)).ints(max(2)).get === List(
      (List(3, 3), List(Set(4, 3)))
    )
  }

  val strs    = Seq("str1", "str2", "str3", "str4")
  val ints    = Seq(1, 2, 3)
  val longs   = Seq(1L, 2L, 3L)
  val floats  = Seq(1.1f, 2.2f, 3.3f)
  val doubles = Seq(1.1, 2.2, 3.3)
  val bools   = Seq(false, true)
  val dates   = Seq(date1, date2, date3)
  val uuids   = Seq(uuid1, uuid2, uuid3)
  val uris    = Seq(uri1, uri2, uri3)
  val enums   = Seq(enum1, enum2, enum3)
  val bigInts = Seq(bigInt1, bigInt2, bigInt3)
  val bigDecs = Seq(bigDec1, bigDec2, bigDec3)


  "rand" in new AggregateSetup {
    strs.contains(Ns.str(rand).get.head) === true
    ints.contains(Ns.int(rand).get.head) === true
    longs.contains(Ns.long(rand).get.head) === true
    floats.contains(Ns.float(rand).get.head) === true
    doubles.contains(Ns.double(rand).get.head) === true
    bools.contains(Ns.bool(rand).get.head) === true
    dates.contains(Ns.date(rand).get.head) === true
    uuids.contains(Ns.uuid(rand).get.head) === true
    uris.contains(Ns.uri(rand).get.head) === true
    enums.contains(Ns.enum(rand).get.head) === true
    bigInts.contains(Ns.bigInt(rand).get.head) === true
    bigDecs.contains(Ns.bigDec(rand).get.head) === true

    strs.intersect(Ns.str(rand(2)).get.head).nonEmpty === true
    ints.intersect(Ns.int(rand(2)).get.head).nonEmpty === true
    longs.intersect(Ns.long(rand(2)).get.head).nonEmpty === true
    floats.intersect(Ns.float(rand(2)).get.head).nonEmpty === true
    doubles.intersect(Ns.double(rand(2)).get.head).nonEmpty === true
    bools.intersect(Ns.bool(rand(2)).get.head).nonEmpty === true
    dates.intersect(Ns.date(rand(2)).get.head).nonEmpty === true
    uuids.intersect(Ns.uuid(rand(2)).get.head).nonEmpty === true
    uris.intersect(Ns.uri(rand(2)).get.head).nonEmpty === true
    enums.intersect(Ns.enum(rand(2)).get.head).nonEmpty === true
    bigInts.intersect(Ns.bigInt(rand(2)).get.head).nonEmpty === true
    bigDecs.intersect(Ns.bigDec(rand(2)).get.head).nonEmpty === true
  }


  "sample" in new AggregateSetup {
    strs.contains(Ns.str(sample).get.head) === true
    ints.contains(Ns.int(sample).get.head) === true
    longs.contains(Ns.long(sample).get.head) === true
    floats.contains(Ns.float(sample).get.head) === true
    doubles.contains(Ns.double(sample).get.head) === true
    bools.contains(Ns.bool(sample).get.head) === true
    dates.contains(Ns.date(sample).get.head) === true
    uuids.contains(Ns.uuid(sample).get.head) === true
    uris.contains(Ns.uri(sample).get.head) === true
    enums.contains(Ns.enum(sample).get.head) === true
    bigInts.contains(Ns.bigInt(sample).get.head) === true
    bigDecs.contains(Ns.bigDec(sample).get.head) === true

    strs.intersect(Ns.str(sample(2)).get.head).size === 2
    ints.intersect(Ns.int(sample(2)).get.head).size === 2
    longs.intersect(Ns.long(sample(2)).get.head).size === 2
    floats.intersect(Ns.float(sample(2)).get.head).size === 2
    doubles.intersect(Ns.double(sample(2)).get.head).size === 2
    bools.intersect(Ns.bool(sample(2)).get.head).size === 2
    dates.intersect(Ns.date(sample(2)).get.head).size === 2
    uuids.intersect(Ns.uuid(sample(2)).get.head).size === 2
    uris.intersect(Ns.uri(sample(2)).get.head).size === 2
    enums.intersect(Ns.enum(sample(2)).get.head).size === 2
    bigInts.intersect(Ns.bigInt(sample(2)).get.head).size === 2
    bigDecs.intersect(Ns.bigDec(sample(2)).get.head).size === 2
  }


  "distinct" in new CoreSetup {

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
      ("a", List(1)),
      ("b", List(3, 2)),
    )

    Ns.int.str(distinct).get.sortBy(_._1) === List(
      (1, List("a")),
      (2, List("b")),
      (3, List("b"))
    )

    Ns.str(distinct).int(distinct).get === List(
      (List("a", "b"), List(1, 3, 2)),
    )
  }


  "count, countDistinct" in new CoreSetup {

    Ns.str.int insert List(
      ("a", 1),
      ("b", 2),
      ("b", 2),
      ("b", 3)
    )
    Ns.int(4).save

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


  "count, countDistinct with entity id" in new CoreSetup {

    // card-one

    val e1 = Ns.int(1).save.eid
    val e2 = Ns.int(2).save.eid
    val e3 = Ns.int(3).save.eid

    Ns.e.int(count).get === List(
      (e1, 1),
      (e2, 1),
      (e3, 1)
    )


    // card-many

    val e4 = Ns.ints(Seq(1, 2)).save.eid
    val e5 = Ns.ints(3).save.eid

    Ns.e.ints(count).get === List(
      (e4, 2),
      (e5, 1)
    )
  }

  "Map attributes can't use aggregates" in new AggregateSetup {
    expectCompileError(
      "m(Ns.intMap(min))",
      "molecule.core.ops.exception.VerifyRawModelException: Only expression keywords `not` and `unify` can be applied to Map attributes."
    )

    expectCompileError(
      """m(Ns.intMapK("a")(min))""",
      "molecule.core.ops.exception.VerifyRawModelException: Only expression keywords `not` and `unify` can be applied to Map attributes."
    )
  }

  "Multiple aggregates of same attr" in new CoreSetup {

    Ns.int.insert(1, 2, 4)

    Ns.int(sum).int(avg).int(median).get.head === (7, 2.3333333333333335, 2)
  }
}