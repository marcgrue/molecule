//package molecule.coretests.generic
//
//import molecule.api.out4._
//import molecule.coretests.util.CoreSpec
//import molecule.coretests.util.dsl.coreTest.Ns
//import molecule.coretests.util.schema.CoreTestSchema
//
//
//class GenericNsQuery extends CoreSpec {
//
//  sequential
//
//  // Create new db from schema
//  implicit val conn = recreateDbFrom(CoreTestSchema)
//
//  // First entity - 3 transactions
//  val tx1 = Ns.str("a").int(1).save
//  val e1  = tx1.eid
//  val t1  = tx1.t
//
//  val tx2 = Ns(e1).str("b").update
//  val t2  = tx2.t
//
//  val tx3 = Ns(e1).int(2).update
//  val t3  = tx3.t
//
//
//  // Second entity - 2 transactions
//
//  val tx4 = Ns.str("x").int(4).save
//  val e2  = tx4.eid
//  val t4  = tx4.t
//
//  val tx5 = Ns(e2).int(5).update
//  val t5  = tx5.t
//
//
//  "ns" >> {
//
//    "mandatory" >> {
//
//      Ns.ns._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]]""".stripMargin
//
//      // eq
//      Ns.ns("db")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(= ?b2 "db")]]""".stripMargin
//
//      // eq multiple
//      Ns.ns("db", "fressian")._query.debug ===
//        """[:find  ?b2
//          | :in    $ %
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        (rule1 ?b2)]
//          |
//          |RULES: [
//          | [(rule1 ?b2) [(= ?b2 "db")]]
//          | [(rule1 ?b2) [(= ?b2 "fressian")]]
//          |]
//          |""".stripMargin
//
//      // neq
//      Ns.ns.not("db")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(.compareTo ^String ?b2 "db") ?b2_1]
//          |        [(!= ?b2_1 0)]]""".stripMargin
//
//      // neq multiple
//      Ns.ns.not("db", "fressian")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(.compareTo ^String ?b2 "db") ?b2_1]
//          |        [(!= ?b2_1 0)]
//          |        [(.compareTo ^String ?b2 "fressian") ?b2_2]
//          |        [(!= ?b2_2 0)]]""".stripMargin
//    }
//
//
//    "tacit" >> {
//
//      // eq
//      Ns.ns_("db.install").a._query.datalog ===
//        """[:find  ?c2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(= ?b2 "db.install")]
//          |        [(name ?b1) ?c2]]""".stripMargin
//
//      // + count
//      Ns.ns_("db.install").a(count)._query.datalog ===
//        """[:find  (count ?c2)
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(= ?b2 "db.install")]
//          |        [(name ?b1) ?c2]]""".stripMargin
//
//      // eq multiple
//      Ns.ns("db.install", "fressian")._query.debug ===
//        """[:find  ?b2
//          | :in    $ %
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        (rule1 ?b2)]
//          |
//          |RULES: [
//          | [(rule1 ?b2) [(= ?b2 "db.install")]]
//          | [(rule1 ?b2) [(= ?b2 "fressian")]]
//          |]
//          |""".stripMargin
//
//      // neq
//      Ns.ns_.not("db").a._query.datalog ===
//        """[:find  ?c2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(.compareTo ^String ?b2 "db") ?b2_1]
//          |        [(!= ?b2_1 0)]
//          |        [(name ?b1) ?c2]]""".stripMargin
//
//      // neq multiple
//      Ns.ns_.not("db", "ns").a._query.datalog ===
//        """[:find  ?c2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(.compareTo ^String ?b2 "db") ?b2_1]
//          |        [(!= ?b2_1 0)]
//          |        [(.compareTo ^String ?b2 "ns") ?b2_2]
//          |        [(!= ?b2_2 0)]
//          |        [(name ?b1) ?c2]]""".stripMargin
//    }
//  }
//
//
//  "a" >> {
//
//    "mandatory" >> {
//
//      Ns.a._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(name ?b1) ?b2]]""".stripMargin
//
//      Ns.a(count)._query.datalog ===
//        """[:find  (count ?b2)
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(name ?b1) ?b2]]""".stripMargin
//
//      Ns.ns.a._query.datalog ===
//        """[:find  ?b2 ?c2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]]""".stripMargin
//
//      // eq
//      Ns.a("lang")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(name ?b1) ?b2]
//          |        [(= ?b2 "lang")]]""".stripMargin
//
//      Ns.ns.a("lang")._query.datalog ===
//        """[:find  ?b2 ?c2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]
//          |        [(= ?c2 "lang")]]""".stripMargin
//
//      // eq multiple
//      Ns.a("lang", "txInstant")._query.debug ===
//        """[:find  ?b2
//          | :in    $ %
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(name ?b1) ?b2]
//          |        (rule1 ?b2)]
//          |
//          |RULES: [
//          | [(rule1 ?b2) [(= ?b2 "lang")]]
//          | [(rule1 ?b2) [(= ?b2 "txInstant")]]
//          |]
//          |""".stripMargin
//
//      Ns.ns.a("lang", "txInstant")._query.debug ===
//        """[:find  ?b2 ?c2
//          | :in    $ %
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]
//          |        (rule1 ?c2)]
//          |
//          |RULES: [
//          | [(rule1 ?c2) [(= ?c2 "lang")]]
//          | [(rule1 ?c2) [(= ?c2 "txInstant")]]
//          |]
//          |""".stripMargin
//
//      // neq
//      Ns.a.not("attribute")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(name ?b1) ?b2]
//          |        [(.compareTo ^String ?b2 "attribute") ?b2_1]
//          |        [(!= ?b2_1 0)]]""".stripMargin
//
//      // neq multiple
//      Ns.a.not("attribute", "attrs")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(name ?b1) ?b2]
//          |        [(.compareTo ^String ?b2 "attribute") ?b2_1]
//          |        [(!= ?b2_1 0)]
//          |        [(.compareTo ^String ?b2 "attrs") ?b2_2]
//          |        [(!= ?b2_2 0)]]""".stripMargin
//    }
//
//
//    "tacit" >> {
//
//      // eq
//      Ns.ns.a_("lang")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]
//          |        [(= ?c2 "lang")]]""".stripMargin
//
//      // eq multiple
//      Ns.ns.a_("lang", "tag")._query.debug ===
//        """[:find  ?b2
//          | :in    $ %
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]
//          |        (rule1 ?c2)]
//          |
//          |RULES: [
//          | [(rule1 ?c2) [(= ?c2 "lang")]]
//          | [(rule1 ?c2) [(= ?c2 "tag")]]
//          |]
//          |""".stripMargin
//
//      // neq
//      Ns.ns.a_.not("tag")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]
//          |        [(.compareTo ^String ?c2 "tag") ?c2_1]
//          |        [(!= ?c2_1 0)]]""".stripMargin
//
//      // neq multiple
//      Ns.ns.a_.not("lang", "tag")._query.datalog ===
//        """[:find  ?b2
//          | :where [:db.part/db :db.install/attribute ?b]
//          |        [?b :db/ident ?b1]
//          |        [(.getNamespace ^clojure.lang.Keyword ?b1) ?b2]
//          |        [(name ?b1) ?c2]
//          |        [(.compareTo ^String ?c2 "lang") ?c2_1]
//          |        [(!= ?c2_1 0)]
//          |        [(.compareTo ^String ?c2 "tag") ?c2_2]
//          |        [(!= ?c2_2 0)]]""".stripMargin
//    }
//  }
//
//
//  "v" >> {
//
//    Ns.ns.v.debugGet
//    Ns.ns.v._query.datalog ===
//      """[:find  ?a ?c2 ?c
//        | :where [?a ?attr ?c]
//        |        [?attr :db/ident ?c1]
//        |        [(name ?c1) ?c2]]""".stripMargin
//
//    Ns.e.a.v._query.datalog ===
//      """[:find  ?a ?c2 ?c
//        | :where [?a ?attr ?c]
//        |        [?attr :db/ident ?c1]
//        |        [(name ?c1) ?c2]]""".stripMargin
//
//    Ns.e.ns.a.v._query.datalog ===
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
//    Ns.e_(22L).a.v.debugGet
//    Ns.e_(22L).ns.a.v.debugGet
//    Ns.e_(22L).a.v.get.map(clean2) === List(
//      ("doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
//      ("tag", ":int"),
//      ("ident", ":db.type/long"),
//    )
//
//    Ns.e_(22L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
//      ("db", "doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
//      ("db", "ident", ":db.type/long"),
//      ("fressian", "tag", ":int")
//    )
//
//    // :db.type/string
//    Ns.e_(23L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
//      ("db", "doc", "Value type for strings."),
//      ("db", "ident", ":db.type/string"),
//      ("fressian", "tag", ":string")
//    )
//
//    // :db.cardinality/one
//    Ns.e_(35L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
//      ("db", "doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
//      ("db", "ident", ":db.cardinality/one")
//    )
//
//    // :db.cardinality/many
//    Ns.e_(36L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
//      ("db", "doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
//      ("db", "ident", ":db.cardinality/many")
//    )
//
//    // :ns/str
//    Ns.e_(63L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
//      ("db", "cardinality", 35),
//      ("db", "fulltext", true),
//      ("db", "ident", ":ns/str"),
//      ("db", "index", true),
//      ("db", "valueType", 23)
//    )
//
//    // :ns/ints
//    Ns.e_(80L).ns.a.v.get.map(clean).sortBy(t => (t._1, t._2)) === List(
//      ("db", "cardinality", 36),
//      ("db", "ident", ":ns/ints"),
//      ("db", "index", true),
//      ("db", "valueType", 22) // Uses Datomic long internally
//    )
//  }
//}