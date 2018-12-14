package molecule.coretests

import molecule.api.in3_out22._
import molecule.ast.model._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.transform.Model2Query

class AdHocTest extends CoreSpec {


  //  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)

  //    // First entity - 3 transactions
  //      val tx1 = Ns.str("a").int(1).save
  //      val e1  = tx1.eid
  //      val t1  = tx1.t
  //
  //      val tx2 = Ns(e1).str("b").update
  //      val t2  = tx2.t
  //
  //      val tx3 = Ns(e1).int(2).update
  //      val t3  = tx3.t
  //
  //
  //      // Second entity - 2 transactions
  //
  //      val tx4 = Ns.str("x").int(4).save
  //      val e2  = tx4.eid
  //      val t4  = tx4.t
  //
  //      val tx5 = Ns(e2).int(5).update
  //      val t5  = tx5.t


  //  "Applied eid to `e`" in new CoreSetup {
  //
  ////    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids
  //
  ////    Ns.int.get === List(1, 2, 3)
  //
  //    val e4 = 42L
  //
  //
  ////    Ns.e(e4).int.get
  ////    Ns.e_(e1).int.get === List(1)
  ////
  ////    Ns.e(e1, e2).int.get === List((e2, 2), (e1, 1))
  ////    Ns.e_(e1, e2).int.get === List(1, 2)
  ////
  ////    val e23 = Seq(e2, e3)
  ////    Ns.e(e23).int.get === List((e2, 2), (e3, 3))
  ////    Ns.e_(e23).int.get === List(2, 3)
  //
  //    ok
  //  }

  //  "adhoc" in new CoreSetup {
  "adhoc" >> {


    // Create ref
    val List(e1, r1) = Ns.int(1).Ref1.int1(10).save.eids
    r1.retract
    // Ref entity with attribute values is gone - no ref orphan exist
    Ns.int(1).ref1$.get.head === (1, None)


    // Create another ref
    val List(e2, r2) = Ns.int(2).Ref1.int1(20).save.eids

    println(e1)
    println(r1)
    println("----------")
    println(e2)
    println(r2)

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
    Ns.int.Ref1.e.int1.debugGet
    Ns.int(2).Ref1.e.int1.get.head === (2, r2, 21)

    //        val rows = conn.q("""[:find  ?b ?b2
    //                            | :where [?a :ns/str ?b]
    //                            |        [?a ?attr _]
    //                            |        [?attr :db/ident ?b1]
    //                            |        [(.toString ^clojure.lang.Keyword ?b1) ?b2]]""".stripMargin)
    //
    //    val rows = conn.q("""[:find  ?id ?ident ?ident2 ?ns ?attr
    //             | :where [:db.part/db :db.install/attribute ?id]
    //             |        [?id :db/ident ?ident]
    //             |        [(str ?ident) ?ident2]
    //             |        [(namespace ?ident) ?ns]
    //             |        [(name ?ident) ?attr]]""".stripMargin)
    //
    //
    //    println(rows.head(2).asInstanceOf[String])
    //    println(rows.head(2).isInstanceOf[String])
    //
    //    println(rows.head(1).isInstanceOf[clojure.lang.Keyword])
    //    println(rows.head(1).isInstanceOf[Object])
    //
    //        rows foreach println
    //    Ns.ns.debugGet


//    val List(_, e2) = Ns.strs("a").Ref1.int1(1).save.eids

//    Ns.int.Ref1.e(count).debugGet
//    Ns.e(count).Ref1.e(count).debugGet

//    Ns.e(count).Ref1.int1_(1).debugGet
//    val two = 2
//    m(Ns.strs.Ref1.e.int1.<(two)).debugGet
//    Ns.strs.Ref1.e(count).int1.<(two).debugGet
//    Ns.strs.Ref1.int1.<(two).debugGet

//    Schema.tpe.apply(count).get
//    Schema.tpe.apply(max).get

    //    Ns.e.str.debugGet
//    //    Ns.ns.str.debugGet
//    //    Ns.a.str.debugGet
//    //    Ns.v.str.debugGet
//    //    Ns.t.str.debugGet
//
//    Ns.str.e.debugGet
//    Ns.str.ns.debugGet
//    Ns.str.a.debugGet
//    //    Ns.str.v.debugGet
//    Ns.str.t.debugGet
//    Ns.str.op.debugGet
//
//    Ns.str.ns.a.v.debugGet

    //    Schema.ns.a.debugGet
    //    m(Schema.ns + Ns.str.t).debugGet

    //    Schema.ns.a.v.debugGet
    //    Schema.ns.v._query.datalog ===
    //      """[:find  ?a ?c2 ?c
    //        | :where [?a ?attr ?c]
    //        |        [?attr :db/ident ?c1]
    //        |        [(name ?c1) ?c2]]""".stripMargin
    //
    //    Schema.e.a.v._query.datalog ===
    //      """[:find  ?a ?c2 ?c
    //        | :where [?a ?attr ?c]
    //        |        [?attr :db/ident ?c1]
    //        |        [(name ?c1) ?c2]]""".stripMargin
    //
    //    Schema.e.ns.a.v._query.datalog ===
    //      """[:find  ?a ?c2 ?d2 ?c
    //        | :where [:db.part/db :db.install/attribute ?c]
    //        |        [?ns :db/ident ?c1]
    //        |        [(.getNamespace ^clojure.lang.Keyword ?c1) ?c2]
    //        |        [(name ?c1) ?d2]]""".stripMargin
    //
    //    def clean2(tpl: (String, Any)): (String, Any) = tpl match {
    //      case (a, v: clojure.lang.Keyword) => (a, v.toString)
    //      case (a, v)                       => (a, v)
    //    }
    //    def clean(tpl: (String, String, Any)): (String, String, Any) = tpl match {
    //      case (ns, a, v: clojure.lang.Keyword) => (ns, a, v.toString)
    //      case (ns, a, v)                       => (ns, a, v)
    //    }
    //
    //
    //    // :db.type/long
    //    Schema.e_(22L).a.v.debugGet
    //    Schema.e_(22L).ns.a.v.debugGet
    //    Schema.e_(22L).a.v.get.map(clean2) === List(
    //      ("doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
    //      ("tag", ":int"),
    //      ("ident", ":db.type/long"),
    //    )
    //
    //    Schema.e_(22L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
    //      ("db", "ident", ":db.type/long"),
    //      ("fressian", "tag", ":int")
    //    )
    //
    //    // :db.type/string
    //    Schema.e_(23L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "Value type for strings."),
    //      ("db", "ident", ":db.type/string"),
    //      ("fressian", "tag", ":string")
    //    )
    //
    //    // :db.cardinality/one
    //    Schema.e_(35L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
    //      ("db", "ident", ":db.cardinality/one")
    //    )
    //
    //    // :db.cardinality/many
    //    Schema.e_(36L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
    //      ("db", "ident", ":db.cardinality/many")
    //    )
    //
    //    // :ns/str
    //    Schema.e_(63L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "cardinality", 35),
    //      ("db", "fulltext", true),
    //      ("db", "ident", ":ns/str"),
    //      ("db", "index", true),
    //      ("db", "valueType", 23)
    //    )
    //
    //    // :ns/ints
    //    Schema.e_(80L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
    //      ("db", "cardinality", 36),
    //      ("db", "ident", ":ns/ints"),
    //      ("db", "index", true),
    //      ("db", "valueType", 22) // Uses Datomic long internally
    //    )


    //    Schema.a.debugGet

    //    Schema.a.get.sorted.take(10) === List(
    //      "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec",
    //      "bigDecMap",
    //      "bigDecMapK",
    //      "bigDecs",
    //      "bigInt",
    //      "bigIntMap"
    //    )

    //    Schema.ns(count).debugGet
    //    Schema.a(count).debugGet
    //    Schema.ns(count).get.head === 9
    //    Schema.a(count).get.head === 93

    //    Schema.ns.a.debugGet
    //    Schema.ns.a.get.sorted.take(10) === List(
    //      ("db", "cardinality"),
    //      ("db", "code"),
    //      ("db", "doc"),
    //      ("db", "excise"),
    //      ("db", "fn"),
    //      ("db", "fulltext"),
    //      ("db", "ident"),
    //      ("db", "index"),
    //      ("db", "isComponent"),
    //      ("db", "lang"),
    //    )
    //
    //
    //    // eq
    //    Schema.ns_("db.install").a.get.sorted === List(
    //      "attribute",
    //      "function",
    //      "partition",
    //      "valueType"
    //    )
    //
    //    // + count
    //    Schema.ns_("db.install").a(count).get.head === 4
    //
    //    // eq multiple
    //    Schema.ns_("db.install", "fressian").a.get.sorted === List(
    //      "attribute",
    //      "function",
    //      "partition",
    //      "tag", // fressian
    //      "valueType"
    //    )
    //
    //    // neq
    //    Schema.ns_.not("db").a.get.sorted.take(5) === List(
    //      "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec"
    //    )
    //
    //    // neq multiple
    //    Schema.ns_.not("db", "ns").a.get.sorted.take(4) === List(
    //      "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //    )
    //
    //    // eq
    //    Schema.a("lang").get === List("lang")
    //    Schema.ns.a("lang").get === List(("db", "lang"))
    //
    //    Schema.a("valueType").get === List("valueType")
    //    // `valueType` attribute is defined in 2 namespaces
    //    Schema.ns.a("valueType").get === List(("db", "valueType"), ("db.install", "valueType"))
    //
    //    // eq multiple
    //    Schema.a("lang", "txInstant").get.sorted === List("lang", "txInstant")
    //    Schema.ns.a("lang", "txInstant").get.sorted === List(("db", "lang"), ("db", "txInstant"))
    //    Schema.ns.a("lang", "valueType").get.sorted === List(("db", "lang"), ("db", "valueType"), ("db.install", "valueType"))
    //
    //    // neq
    //    Schema.a.not("attribute").get.sorted.take(9) === List(
    //      // "attribute",
    //      "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec",
    //      "bigDecMap",
    //      "bigDecMapK",
    //      "bigDecs",
    //      "bigInt",
    //      "bigIntMap"
    //    )
    //
    //    // neq multiple
    //    Schema.a.not("attribute", "attrs").get.sorted.take(8) === List(
    //      // "attribute",
    //      // "attrs",
    //      "before",
    //      "beforeT",
    //      "bigDec",
    //      "bigDecMap",
    //      "bigDecMapK",
    //      "bigDecs",
    //      "bigInt",
    //      "bigIntMap"
    //    )
    //
    //
    //    // eq
    //    Schema.ns.a_("lang").get === List("db")
    //
    //    // `valueType` is in two namespaces
    //    Schema.ns.a_("valueType").get.sorted === List("db", "db.install")
    //
    //    // eq multiple
    //    Schema.ns.a_("lang", "tag").get.sorted === List("db", "fressian")
    //
    //    // neq
    //    Schema.ns.a_.not("tag").get.sorted.take(13) === List(
    //      "db",
    //      "db.alter",
    //      "db.excise",
    //      "db.install",
    //      "db.sys",
    //      // "fressian", - only attribute named `tag` is in `not` clause
    //      "ns",
    //      "ref1",
    //      "ref2"
    //    )
    //
    //    // neq multiple
    //    Schema.ns.a_.not("lang", "tag").get.sorted.take(11) === List(
    //      "db", // `db` namespace has `lang` but also has other attributes and is therefore not excluded
    //      "db.alter",
    //      "db.excise",
    //      "db.install",
    //      "db.sys",
    //      // "fressian", - only attribute named `tag` is in `not` clause
    //      "ns",
    //      "ref1",
    //      "ref2"
    //    )

//        val model1 = Model(List(
//          Atom("ns", "int", "Int", 1, VarValue, None, Seq(NoValue), Seq()),
//          Bond("ns", "ref1", "ref1", 1, Seq()),
//          Meta("ref1", "ref1", "e", NoValue, Fn("count", None)),
//          Atom("ref1", "int1", "Int", 1, Lt(2), None, Seq(), Seq())))
//        val model1 = Model(List(
//          Atom("ns", "int", "Int", 1, VarValue, None, Seq(NoValue), Seq()),
//          Bond("ns", "ref1", "ref1", 1, Seq()),
//          Meta("?", "e", "e", NoValue, Fn("count", None)),
//          Atom("ref1", "int1", "Int", 1, Lt(2), None, Seq(), Seq())))
//        val query1 = Model2Query(model1)
//
//        val model2 = Model(List(
//          Atom("ns", "int", "Int", 1, VarValue, None, Seq(NoValue), Seq()),
//          Bond("ns", "ref1", "ref1", 1, Seq()),
//          Meta("?", "e", "e", NoValue, Fn("count", None)),
//          Atom("ref1", "int1", "Int", 1, Lt(2), None, Seq(), Seq())))
//        val query2 = Model2Query(model2)
//        println(model)
//        println(query)

    //        Schema.a.debugGet
    //        Schema.a("attribute").debugGet
    //        Schema.a("attribute", "attrs").debugGet
    //        Schema.a.not("attribute").debugGet
    //        Schema.a.not("attribute", "attrs").debugGet

    //        Schema.ns.debugGet
    //        Schema.ns("ref1").debugGet
    //        Schema.ns("ref1", "ref2").debugGet
    //        Schema.ns.not("ref1").debugGet
    //        Schema.ns.not("ref1", "ref2").debugGet
    //
    //    Schema.ns.debugGet
    //    Schema.ns(count).debugGet
    //
    //
    //    Ns.str.apply(count).debugGet
    //    Schema.a.apply(count).debugGet
    //    Schema.a.apply(countDistinct).debugGet
    //    Schema.a.apply("lang").debugGet
    //    Schema.a(count).valueType(countDistinct).get.head === (124, 8)
    //
    //
    //    Ns.ns.Ref1.a.debugGet
    //    Ns.ns(count).a("aa").v.debugGet
    //    Schema.ns(count).a("aa").v.debugGet
    //    Log.ns(count).a("aa").v.debugGet
    //
    //    println(Schema.ns._model)
    //

    //    Schema.ns.debugGet

    //    Ns.ns.a.v.debugGet
    //    Ns.ns.Ref1.a.debugGet
    //    Schema.ns.a.v.debugGet
    //    Log.ns.a.v.debugGet

    //    Ns.e(count).int_.>(1).get.head === 2
    //    Ns.int.e(count).get.head === 2
    //    Ns.int_.>(1).e(count).get.head === 2

    //    Ns.e.int.debugGet
    //    Ns.int.e.debugGet

    //        val e1 = 42L
    //        Ns.long(e1).debugGet
    //        Ns(e1).str.get === List((e1, "a"))
    //        Ns.e(e1).int.get === List((e1, 1))
    //        Ns(e1).int.get === List((e1, 1))
    //    Schema.e(e1).get === List((e1, 1))


    //    Schema.e.ns.a.v.tx.t.txInstant.op.debugGet

    //    EAVT.e.a.

    //    Ref1.e(42L).str1("xx").get === 7


    //    Schema.ns.a.debugGet
    //    Schema.22L).a.v.debugGet
    //    Ns(e1).a.v.debugGet
    //
    //    Ns(e1).a.v.t.op.debugGetHistory
    //
    //    Schema.e_(22L).a.v.debugGet
    //
    //    Ns(e1).a.v.t.op.getHistory.sortBy(t => (t._3, t._4)) === List(
    //      (":ns/str", "a", t1, true),
    //      (":ns/int", 1, t1, true),
    //      (":ns/str", "a", t2, false),
    //      (":ns/str", "b", t2, true),
    //      (":ns/int", 1, t3, false),
    //      (":ns/int", 2, t3, true)
    //    )

    //    val e = Ns.int(1).save.eid
    //
    //    Ns.e.int.get === List((e, 1))
    //
    //    e.retract
    //
    //    Ns.e.int.get === Nil
    //
    //
    //
    //    Ns.int_.tx.debugGet
    //    Ns.int_.tx.debugGetHistory
    ////    Ns.e.int.t.op.debugGetHistory
    //
    ////    Ns.e.int.t.op.debugGetHistory
    ////
    ////    Ns.e.int.t.op.getHistory.sortBy(t => (t1, t._3, t._4)) === List(
    ////      // e1
    ////      (e1, 1, t1, true),
    ////      (e1, 1, t3, false),
    ////      (e1, 2, t3, true),
    ////      //e2
    ////      (e2, 4, t4, true),
    ////      (e2, 4, t5, false),
    ////      (e2, 5, t5, true)
    ////    )


    //    Schema.ns(count).get === List("db.install")


    ok
  }

}