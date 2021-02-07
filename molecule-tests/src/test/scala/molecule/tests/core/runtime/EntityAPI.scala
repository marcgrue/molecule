package molecule.tests.core.runtime

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out3._
import molecule.TestSpec

class EntityAPI extends TestSpec {

   // See also molecule.examples.datomic.dayOfDatomic.ProductsAndOrders

  "touch Map" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid.touch === Map(
      ":db/id" -> eid,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> Map(
        ":db/id" -> refId,
        ":Ref1/str1" -> "Hollywood Rd"),
      ":Ns/str" -> "Ben"
    )

    eid.touchQuoted ===
      s"""Map(
         |  ":db/id" -> ${eid}L,
         |  ":Ns/int" -> 42,
         |  ":Ns/ref1" -> Map(
         |    ":db/id" -> ${refId}L,
         |    ":Ref1/str1" -> "Hollywood Rd"),
         |  ":Ns/str" -> "Ben")""".stripMargin

    // Level

    eid.touchMax(2) === Map(
      ":db/id" -> eid,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> Map(":db/id" -> refId, ":Ref1/str1" -> "Hollywood Rd"),
      ":Ns/str" -> "Ben"
    )
    eid.touchMax(1) === Map(
      ":db/id" -> eid,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> refId,
      ":Ns/str" -> "Ben"
    )

    eid.touchQuotedMax(1) ===
      s"""Map(
         |  ":db/id" -> ${eid}L,
         |  ":Ns/int" -> 42,
         |  ":Ns/ref1" -> ${refId}L,
         |  ":Ns/str" -> "Ben")""".stripMargin
  }


  "touch List" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid.touchList === List(
      ":db/id" -> eid,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> List((":db/id", refId), (":Ref1/str1", "Hollywood Rd")),
      ":Ns/str" -> "Ben"
    )

    eid.touchListQuoted ===
      s"""List(
         |  ":db/id" -> ${eid}L,
         |  ":Ns/int" -> 42,
         |  ":Ns/ref1" -> List(
         |    ":db/id" -> ${refId}L,
         |    ":Ref1/str1" -> "Hollywood Rd"),
         |  ":Ns/str" -> "Ben")""".stripMargin

    eid.touchListMax(3) === List(
      ":db/id" -> eid,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> List((":db/id", refId), (":Ref1/str1", "Hollywood Rd")),
      ":Ns/str" -> "Ben"
    )
    eid.touchListMax(1) === List(
      ":db/id" -> eid,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> refId,
      ":Ns/str" -> "Ben"
    )


    eid.touchListQuotedMax(1) ===
      s"""List(
         |  ":db/id" -> ${eid}L,
         |  ":Ns/int" -> 42,
         |  ":Ns/ref1" -> ${refId}L,
         |  ":Ns/str" -> "Ben")""".stripMargin
  }


  "one" in new CoreSetup {

    val e1 = Ns.str("a").save.eid
    e1.touchList === List(":db/id" -> e1, ":Ns/str" -> "a")

    val e2 = Ns.int(1).save.eid
    e2.touchList === List(":db/id" -> e2, ":Ns/int" -> 1)

    val e3 = Ns.long(1L).save.eid
    e3.touchList === List(":db/id" -> e3, ":Ns/long" -> 1L)

    val e4 = Ns.float(1.1f).save.eid
    // Since Datomic uses Double for floating numbers we get a Double back
    e4.touchList === List(":db/id" -> e4, ":Ns/float" -> 1.1)

    val e5 = Ns.double(1.1).save.eid
    e5.touchList === List(":db/id" -> e5, ":Ns/double" -> 1.1)

    val e6 = Ns.bool(true).save.eid
    e6.touchList === List(":db/id" -> e6, ":Ns/bool" -> true)

    val e7 = Ns.bigInt(bigInt1).save.eid
    e7.touchList === List(":db/id" -> e7, ":Ns/bigInt" -> bigInt1)

    val e8 = Ns.bigDec(bigDec1).save.eid
    e8.touchList === List(":db/id" -> e8, ":Ns/bigDec" -> bigDec1)

    val e9 = Ns.date(date1).save.eid
    e9.touchList === List(":db/id" -> e9, ":Ns/date" -> date1)

    val e10 = Ns.uuid(uuid1).save.eid
    e10.touchList === List(":db/id" -> e10, ":Ns/uuid" -> uuid1)

    val e11 = Ns.uri(uri1).save.eid
    e11.touchList === List(":db/id" -> e11, ":Ns/uri" -> uri1)

    val e12 = Ns.enum("enum1").save.eid
    e12.touchList === List(":db/id" -> e12, ":Ns/enum" -> ":Ns.enum/enum1")
  }


  "many" in new CoreSetup {
    val e1 = Ns.strs("a", "b").save.eid
    e1.touchList === List(":db/id" -> e1, ":Ns/strs" -> List("a", "b"))

    val e2 = Ns.ints(1, 2).save.eid
    e2.touchList === List(":db/id" -> e2, ":Ns/ints" -> List(1, 2))

    val e3 = Ns.longs(1L, 2L).save.eid
    e3.touchList === List(":db/id" -> e3, ":Ns/longs" -> List(1, 2))

    val e4 = Ns.floats(1.1f, 2.2f).save.eid
    // Since Datomic uses Double for floating numbers we get a Double back
    e4.touchList === List(":db/id" -> e4, ":Ns/floats" -> List(1.1, 2.2))

    val e5 = Ns.doubles(1.1, 2.2).save.eid
    e5.touchList === List(":db/id" -> e5, ":Ns/doubles" -> List(1.1, 2.2))

    val e6 = Ns.bools(true, false).save.eid
    e6.touchList === List(":db/id" -> e6, ":Ns/bools" -> List(false, true))

    val e7 = Ns.dates(date1, date2).save.eid
    e7.touchList === List(":db/id" -> e7, ":Ns/dates" -> List(date1, date2))

    val e8 = Ns.uuids(uuid1, uuid2).save.eid
    e8.touchList === List(":db/id" -> e8, ":Ns/uuids" -> List(uuid1, uuid2))

    val e9 = Ns.uris(uri1, uri2).save.eid
    e9.touchList === List(":db/id" -> e9, ":Ns/uris" -> List(uri1, uri2))

    val e10 = Ns.bigInts(bigInt1, bigInt2).save.eid
    e10.touchList === List(":db/id" -> e10, ":Ns/bigInts" -> List(bigInt1, bigInt2))

    val e11 = Ns.bigDecs(bigDec1, bigDec2).save.eid
    e11.touchList === List(":db/id" -> e11, ":Ns/bigDecs" -> List(bigDec1, bigDec2))

    val e12 = Ns.enums(enum1, enum2).save.eid
    e12.touchList === List(":db/id" -> e12, ":Ns/enums" -> List(":Ns.enums/enum1", ":Ns.enums/enum2"))
  }


  "map" in new CoreSetup {

    val e1 = Ns.strMap("a" -> "aa", "b" -> "bb").save.eid
    e1.touchList === List(":db/id" -> e1, ":Ns/strMap" -> List("a@aa", "b@bb"))

    val e2 = Ns.intMap("a" -> 1, "b" -> 2).save.eid
    e2.touchList === List(":db/id" -> e2, ":Ns/intMap" -> List("a@1", "b@2"))

    val e3 = Ns.longMap("a" -> 1L, "b" -> 2L).save.eid
    e3.touchList === List(":db/id" -> e3, ":Ns/longMap" -> List("a@1", "b@2"))

    val e4 = Ns.floatMap("a" -> 1.1f, "b" -> 2.2f).save.eid
    e4.touchList === List(":db/id" -> e4, ":Ns/floatMap" -> List("a@1.1", "b@2.2"))

    val e5 = Ns.doubleMap("a" -> 1.1, "b" -> 2.2).save.eid
    e5.touchList === List(":db/id" -> e5, ":Ns/doubleMap" -> List("a@1.1", "b@2.2"))

    val e6 = Ns.boolMap("a" -> true, "b" -> false).save.eid
    e6.touchList === List(":db/id" -> e6, ":Ns/boolMap" -> List("a@true", "b@false"))

    val e7 = Ns.dateMap("a" -> date1, "b" -> date2).save.eid
    e7.touchList === List(":db/id" -> e7, ":Ns/dateMap" -> List("a@2001-07-01", "b@2002-01-01"))

    val e8 = Ns.uuidMap("a" -> uuid1, "b" -> uuid2).save.eid
    e8.touchList === List(":db/id" -> e8, ":Ns/uuidMap" -> List(s"a@$uuid1", s"b@$uuid2"))

    val e9 = Ns.uriMap("a" -> uri1, "b" -> uri2).save.eid
    e9.touchList === List(":db/id" -> e9, ":Ns/uriMap" -> List(s"a@$uri1", s"b@$uri2"))

    val e10 = Ns.bigIntMap("a" -> bigInt1, "b" -> bigInt2).save.eid
    e10.touchList === List(":db/id" -> e10, ":Ns/bigIntMap" -> List(s"a@$bigInt1", s"b@$bigInt2"))

    val e11 = Ns.bigDecMap("a" -> bigDec1, "b" -> bigDec2).save.eid
    e11.touchList === List(":db/id" -> e11, ":Ns/bigDecMap" -> List(s"a@$bigDec1", s"b@$bigDec2"))
  }


  "apply typed" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    val typed  : Option[String] = eid[String](":Ns/str")
    val untyped: Option[Any]    = eid(":Ns/str")

    // Level 1
    eid[String](":Ns/str") === Some("Ben")
    eid[Int](":Ns/int") === Some(42)

    // Level 2
    refId[Long](":Ref1/str1") === Some("Hollywood Rd")

    // Non-existing attribute returns None
    eid.apply[Any](":Ns/non-existing-attribute") === None
  }


  "apply untyped" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid(":Ns/str", ":Ns/int", ":Ns/ref1") === List(
      Some("Ben"),
      Some(42),
      Some(
        Map(
          ":db/id" -> refId,
          ":Ref1/str1" -> "Hollywood Rd"
        )
      )
    )

    // Type ascription is still unchecked since it is eliminated by erasure
    // so we suppress compile warnings emitted
    val List(
    optName: Option[String]@unchecked,
    optAge: Option[Int]@unchecked,
    optAddress: Option[Map[String, Any]]@unchecked
    ) = eid(
      ":Ns/str",
      ":Ns/int",
      ":Ns/ref1"
    )

    val name: String = optName.getOrElse("no name")

    val address: Map[String, Any] = optAddress.getOrElse(Map.empty[String, Any])
    val street : String           = address.getOrElse(":Ref1/str1", "no street").asInstanceOf[String]

    name === "Ben"
    street === "Hollywood Rd"
  }


  "Reverse lookup" in new CoreSetup {

    val r = Ref1.int1(10).save.eid

    // 3 entities pointing to r
    val eids = Ns.int.ref1.insert(List((1, r), (2, r), (3, r))).eids.sorted

    // get entities pointing to r
    r(":Ns/_ref1") === Some(eids)

    // Alternatively we can get the entities type safely with a query
    Ns.e.ref1_(r).get.sorted === eids

  }
}
