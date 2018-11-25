package molecule.coretests.generic

import molecule.api.out4._
import molecule.coretests.util.CoreSpec
import molecule.generic.Db

// Todo: expand on generic/meta queries...

class GenericDb extends CoreSpec {


  "Datomic a" in new CoreSetup {

    Db.a._query.datalog ===
      """[:find  ?b2
        | :where [?a ?attr ?b]
        |        [?attr :db/ident ?b1]
        |        [(.toString ^clojure.lang.Keyword ?b1) ?b2]]""".stripMargin

    Db.a.get.sorted === List(
      ":db.install/attribute",
      ":db.install/function",
      ":db.install/partition",
      ":db.install/valueType",
      ":db/cardinality",
      ":db/code",
      ":db/doc",
      ":db/fulltext",
      ":db/ident",
      ":db/index",
      ":db/isComponent",
      ":db/lang",
      ":db/txInstant",
      ":db/unique",
      ":db/valueType",
      ":fressian/tag",
    )

    Db.a(count).get.head === 16
  }


  "Ns v" in new CoreSetup {

    Db.e.a.v._query.datalog ===
      """[:find  ?a ?c2 ?c
        | :where [?a ?attr ?c]
        |        [?attr :db/ident ?c1]
        |        [(.toString ^clojure.lang.Keyword ?c1) ?c2]]""".stripMargin

    def clean(tpl: (String, Any)): (String, Any) = tpl match {
      case (a, v: clojure.lang.Keyword) => (a, v.toString)
      case (a, v)                       => (a, v)
    }

    // :db.type/long
    Db.e_(22L).a.v.get.map(clean).sortBy(_._1) === List(
      (":db/doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
      (":db/ident", ":db.type/long"),
      (":fressian/tag", ":int")
    )

    // :db.type/string
    Db.e_(23L).a.v.get.map(clean).sortBy(_._1) === List(
      (":db/doc", "Value type for strings."),
      (":db/ident", ":db.type/string"),
      (":fressian/tag", ":string")
    )

    // :db.cardinality/one
    Db.e_(35L).a.v.get.map(clean).sortBy(_._1) === List(
      (":db/doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
      (":db/ident", ":db.cardinality/one")
    )

    // :db.cardinality/many
    Db.e_(36L).a.v.get.map(clean).sortBy(_._1) === List(
      (":db/doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
      (":db/ident", ":db.cardinality/many")
    )

    // :ns/str
    Db.e_(63L).a.v.get.map(clean).sortBy(_._1) === List(
      (":db/cardinality", 35),
      (":db/fulltext", true),
      (":db/ident", ":ns/str"),
//      (":db/index", true),
      (":db/valueType", 23)
    )

    // :ns/ints
    Db.e_(80L).a.v.get.map(clean).sortBy(_._1) === List(
      (":db/cardinality", 36),
      (":db/ident", ":ns/ints"),
//      (":db/index", true),
      (":db/valueType", 22) // Uses Datomic long internally
    )

    // All attributes (generic and custom)
    //    Db.e.a.v.get.sortBy(r => (r._1, r._2, r._3.toString)).map { case (e, a, v) =>
    //      val e1 = if (e < 10000) e else e + "L"
    //      val v1 = v match {
    //        case n: Long if n < 10000 => n
    //        case n: Long              => n + "L"
    //        case n: Int               => n
    //        case b: Boolean           => b
    //        case other                => "\"" + other.toString + "\""
    //      }
    //      (e1, "\"" + a + "\"", v1)
    //    }.mkString(",\n")
    List(
      (0, ":db.install/attribute", 10),
      (0, ":db.install/attribute", 100),
      (0, ":db.install/attribute", 101),
      (0, ":db.install/attribute", 102),
      (0, ":db.install/attribute", 103),
      (0, ":db.install/attribute", 104),
      (0, ":db.install/attribute", 105),
      (0, ":db.install/attribute", 106),
      (0, ":db.install/attribute", 107),
      (0, ":db.install/attribute", 108),
      (0, ":db.install/attribute", 109),
      (0, ":db.install/attribute", 11),
      (0, ":db.install/attribute", 110),
      (0, ":db.install/attribute", 111),
      (0, ":db.install/attribute", 112),
      (0, ":db.install/attribute", 113),
      (0, ":db.install/attribute", 114),
      (0, ":db.install/attribute", 115),
      (0, ":db.install/attribute", 116),
      (0, ":db.install/attribute", 117),
      (0, ":db.install/attribute", 118),
      (0, ":db.install/attribute", 119),
      (0, ":db.install/attribute", 12),
      (0, ":db.install/attribute", 120),
      (0, ":db.install/attribute", 121),
      (0, ":db.install/attribute", 122),
      (0, ":db.install/attribute", 123),
      (0, ":db.install/attribute", 124),
      (0, ":db.install/attribute", 125),
      (0, ":db.install/attribute", 126),
      (0, ":db.install/attribute", 127),
      (0, ":db.install/attribute", 128),
      (0, ":db.install/attribute", 129),
      (0, ":db.install/attribute", 13),
      (0, ":db.install/attribute", 130),
      (0, ":db.install/attribute", 131),
      (0, ":db.install/attribute", 132),
      (0, ":db.install/attribute", 14),
      (0, ":db.install/attribute", 15),
      (0, ":db.install/attribute", 16),
      (0, ":db.install/attribute", 17),
      (0, ":db.install/attribute", 18),
      (0, ":db.install/attribute", 19),
      (0, ":db.install/attribute", 39),
      (0, ":db.install/attribute", 40),
      (0, ":db.install/attribute", 41),
      (0, ":db.install/attribute", 42),
      (0, ":db.install/attribute", 43),
      (0, ":db.install/attribute", 44),
      (0, ":db.install/attribute", 45),
      (0, ":db.install/attribute", 46),
      (0, ":db.install/attribute", 47),
      (0, ":db.install/attribute", 50),
      (0, ":db.install/attribute", 51),
      (0, ":db.install/attribute", 52),
      (0, ":db.install/attribute", 62),
      (0, ":db.install/attribute", 63),
      (0, ":db.install/attribute", 64),
      (0, ":db.install/attribute", 65),
      (0, ":db.install/attribute", 66),
      (0, ":db.install/attribute", 67),
      (0, ":db.install/attribute", 68),
      (0, ":db.install/attribute", 69),
      (0, ":db.install/attribute", 70),
      (0, ":db.install/attribute", 71),
      (0, ":db.install/attribute", 72),
      (0, ":db.install/attribute", 73),
      (0, ":db.install/attribute", 74),
      (0, ":db.install/attribute", 75),
      (0, ":db.install/attribute", 76),
      (0, ":db.install/attribute", 77),
      (0, ":db.install/attribute", 78),
      (0, ":db.install/attribute", 79),
      (0, ":db.install/attribute", 8),
      (0, ":db.install/attribute", 80),
      (0, ":db.install/attribute", 81),
      (0, ":db.install/attribute", 82),
      (0, ":db.install/attribute", 83),
      (0, ":db.install/attribute", 84),
      (0, ":db.install/attribute", 85),
      (0, ":db.install/attribute", 86),
      (0, ":db.install/attribute", 87),
      (0, ":db.install/attribute", 88),
      (0, ":db.install/attribute", 89),
      (0, ":db.install/attribute", 9),
      (0, ":db.install/attribute", 90),
      (0, ":db.install/attribute", 91),
      (0, ":db.install/attribute", 92),
      (0, ":db.install/attribute", 93),
      (0, ":db.install/attribute", 94),
      (0, ":db.install/attribute", 95),
      (0, ":db.install/attribute", 96),
      (0, ":db.install/attribute", 97),
      (0, ":db.install/attribute", 98),
      (0, ":db.install/attribute", 99),
      (0, ":db.install/function", 54),
      (0, ":db.install/function", 55),
      (0, ":db.install/partition", 0),
      (0, ":db.install/partition", 3),
      (0, ":db.install/partition", 4),
      (0, ":db.install/valueType", 20),
      (0, ":db.install/valueType", 21),
      (0, ":db.install/valueType", 22),
      (0, ":db.install/valueType", 23),
      (0, ":db.install/valueType", 24),
      (0, ":db.install/valueType", 25),
      (0, ":db.install/valueType", 26),
      (0, ":db.install/valueType", 27),
      (0, ":db.install/valueType", 56),
      (0, ":db.install/valueType", 57),
      (0, ":db.install/valueType", 58),
      (0, ":db.install/valueType", 59),
      (0, ":db.install/valueType", 60),
      (0, ":db.install/valueType", 61),
      (0, ":db/doc", "Name of the system partition. The system partition includes the core of datomic, as well as user schemas: type definitions, attribute definitions, partition definitions, and data function definitions."),
      (0, ":db/ident", ":db.part/db"),
      (1, ":db/doc", "Primitive assertion. All transactions eventually reduce to a collection of primitive assertions and retractions of facts, e.g. [:db/add fred :age 42]."),
      (1, ":db/ident", ":db/add"),
      (2, ":db/doc", "Primitive retraction. All transactions eventually reduce to a collection of assertions and retractions of facts, e.g. [:db/retract fred :age 42]."),
      (2, ":db/ident", ":db/retract"),
      (3, ":db/doc", "Partition used to store data about transactions. Transaction data always includes a :db/txInstant which is the transaction's timestamp, and can be extended to store other information at transaction granularity."),
      (3, ":db/ident", ":db.part/tx"),
      (4, ":db/doc", "Name of the user partition. The user partition is analogous to the default namespace in a programming language, and should be used as a temporary home for data during interactive development."),
      (4, ":db/ident", ":db.part/user"),
      (8, ":db/cardinality", 35),
      (8, ":db/doc", "System-assigned attribute set to true for transactions not fully incorporated into the index"),
      (8, ":db/ident", ":db.sys/partiallyIndexed"),
      (8, ":db/valueType", 24),
      (9, ":db/cardinality", 35),
      (9, ":db/doc", "System-assigned attribute for an id e in the log that has been changed to id v in the index"),
      (9, ":db/ident", ":db.sys/reId"),
      (9, ":db/valueType", 20),
      (10, ":db/cardinality", 35),
      (10, ":db/doc", "Attribute used to uniquely name an entity."),
      (10, ":db/ident", ":db/ident"),
      (10, ":db/unique", 38),
      (10, ":db/valueType", 21),
      (11, ":db/cardinality", 36),
      (11, ":db/doc", "System attribute with type :db.type/ref. Asserting this attribute on :db.part/db with value v will install v as a partition."),
      (11, ":db/ident", ":db.install/partition"),
      (11, ":db/valueType", 20),
      (12, ":db/cardinality", 36),
      (12, ":db/doc", "System attribute with type :db.type/ref. Asserting this attribute on :db.part/db with value v will install v as a value type."),
      (12, ":db/ident", ":db.install/valueType"),
      (12, ":db/valueType", 20),
      (13, ":db/cardinality", 36),
      (13, ":db/doc", "System attribute with type :db.type/ref. Asserting this attribute on :db.part/db with value v will install v as an attribute."),
      (13, ":db/ident", ":db.install/attribute"),
      (13, ":db/valueType", 20),
      (14, ":db/cardinality", 36),
      (14, ":db/doc", "System attribute with type :db.type/ref. Asserting this attribute on :db.part/db with value v will install v as a data function."),
      (14, ":db/ident", ":db.install/function"),
      (14, ":db/valueType", 20),
      (15, ":db/cardinality", 35),
      (15, ":db/ident", ":db/excise"),
      (15, ":db/valueType", 20),
      (16, ":db/cardinality", 36),
      (16, ":db/ident", ":db.excise/attrs"),
      (16, ":db/valueType", 20),
      (17, ":db/cardinality", 35),
      (17, ":db/ident", ":db.excise/beforeT"),
      (17, ":db/valueType", 22),
      (18, ":db/cardinality", 35),
      (18, ":db/ident", ":db.excise/before"),
      (18, ":db/valueType", 25),
      (19, ":db/cardinality", 36),
      (19, ":db/doc", "System attribute with type :db.type/ref. Asserting this attribute on :db.part/db with value v will alter the definition of existing attribute v."),
      (19, ":db/ident", ":db.alter/attribute"),
      (19, ":db/valueType", 20),
      (20, ":db/doc", "Value type for references. All references from one entity to another are through attributes with this value type."),
      (20, ":db/ident", ":db.type/ref"),
      (20, ":fressian/tag", ":ref"),
      (21, ":db/doc", "Value type for keywords. Keywords are used as names, and are interned for efficiency. Keywords map to the native interned-name type in languages that support them."),
      (21, ":db/ident", ":db.type/keyword"),
      (21, ":fressian/tag", ":key"),
      (22, ":db/doc", "Fixed integer value type. Same semantics as a Java long: 64 bits wide, two's complement binary representation."),
      (22, ":db/ident", ":db.type/long"),
      (22, ":fressian/tag", ":int"),

      (23, ":db/doc", "Value type for strings."),
      (23, ":db/ident", ":db.type/string"),
      (23, ":fressian/tag", ":string"),

      (24, ":db/doc", "Boolean value type."),
      (24, ":db/ident", ":db.type/boolean"),
      (24, ":fressian/tag", ":bool"),
      (25, ":db/doc", "Value type for instants in time. Stored internally as a number of milliseconds since midnight, January 1, 1970 UTC. Representation type will vary depending on the language you are using."),
      (25, ":db/ident", ":db.type/instant"),
      (25, ":fressian/tag", ":inst"),
      (26, ":db/doc", "Value type for database functions. See Javadoc for Peer.function."),
      (26, ":db/ident", ":db.type/fn"),
      (26, ":fressian/tag", ":datomic/fn"),
      (27, ":db/doc", "Value type for small binaries. Maps to byte array on the JVM."),
      (27, ":db/ident", ":db.type/bytes"),
      (27, ":fressian/tag", ":bytes"),

      (35, ":db/doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
      (35, ":db/ident", ":db.cardinality/one"),

      (36, ":db/doc", "One of two legal values for the :db/cardinality attribute. Specify :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes."),
      (36, ":db/ident", ":db.cardinality/many"),
      (37, ":db/doc", "Specifies that an attribute's value is unique. Attempts to create a new entity with a colliding value for a :db.unique/value will fail."),
      (37, ":db/ident", ":db.unique/value"),
      (38, ":db/doc", "Specifies that an attribute's value is unique. Attempts to create a new entity with a colliding value for a :db.unique/value will become upserts."),
      (38, ":db/ident", ":db.unique/identity"),
      (39, ":db/cardinality", 35),
      (39, ":db/doc", "Keyword-valued attribute of a value type that specifies the underlying fressian type used for serialization."),
      (39, ":db/ident", ":fressian/tag"),
      (39, ":db/index", true),
      (39, ":db/valueType", 21),
      (40, ":db/cardinality", 35),
      (40, ":db/doc", "Property of an attribute that specifies the attribute's value type. Built-in value types include, :db.type/keyword, :db.type/string, :db.type/ref, :db.type/instant, :db.type/long, :db.type/bigdec, :db.type/boolean, :db.type/float, :db.type/uuid, :db.type/double, :db.type/bigint,  :db.type/uri."),
      (40, ":db/ident", ":db/valueType"),
      (40, ":db/valueType", 20),
      (41, ":db/cardinality", 35),
      (41, ":db/doc", "Property of an attribute. Two possible values: :db.cardinality/one for single-valued attributes, and :db.cardinality/many for many-valued attributes. Defaults to :db.cardinality/one."),
      (41, ":db/ident", ":db/cardinality"),
      (41, ":db/valueType", 20),
      (42, ":db/cardinality", 35),
      (42, ":db/doc", "Property of an attribute. If value is :db.unique/value, then attribute value is unique to each entity. Attempts to insert a duplicate value for a temporary entity id will fail. If value is :db.unique/identity, then attribute value is unique, and upsert is enabled. Attempting to insert a duplicate value for a temporary entity id will cause all attributes associated with that temporary id to be merged with the entity already in the database. Defaults to nil."),
      (42, ":db/ident", ":db/unique"),
      (42, ":db/valueType", 20),
      (43, ":db/cardinality", 35),
      (43, ":db/doc", "Property of attribute whose vtype is :db.type/ref. If true, then the attribute is a component of the entity referencing it. When you query for an entire entity, components are fetched automatically. Defaults to nil."),
      (43, ":db/ident", ":db/isComponent"),
      (43, ":db/valueType", 24),
      (44, ":db/cardinality", 35),
      (44, ":db/doc", "Property of an attribute. If true, create an AVET index for the attribute. Defaults to false."),
      (44, ":db/ident", ":db/index"),
      (44, ":db/valueType", 24),
      (45, ":db/cardinality", 35),
      (45, ":db/doc", "Property of an attribute. If true, past values of the attribute are not retained after indexing. Defaults to false."),
      (45, ":db/ident", ":db/noHistory"),
      (45, ":db/valueType", 24),
      (46, ":db/cardinality", 35),
      (46, ":db/doc", "Attribute of a data function. Value is a keyword naming the implementation language of the function. Legal values are :db.lang/java and :db.lang/clojure"),
      (46, ":db/ident", ":db/lang"),
      (46, ":db/valueType", 20),
      (47, ":db/cardinality", 35),
      (47, ":db/doc", "String-valued attribute of a data function that contains the function's source code."),
      (47, ":db/fulltext", true),
      (47, ":db/ident", ":db/code"),
      (47, ":db/valueType", 23),
      (48, ":db/doc", "Value of :db/lang attribute, specifying that a data function is implemented in Clojure."),
      (48, ":db/ident", ":db.lang/clojure"),
      (49, ":db/doc", "Value of :db/lang attribute, specifying that a data function is implemented in Java."),
      (49, ":db/ident", ":db.lang/java"),
      (50, ":db/cardinality", 35),
      (50, ":db/doc", "Attribute whose value is a :db.type/instant. A :db/txInstant is recorded automatically with every transaction."),
      (50, ":db/ident", ":db/txInstant"),
      (50, ":db/index", true),
      (50, ":db/valueType", 25),
      (51, ":db/cardinality", 35),
      (51, ":db/doc", "Property of an attribute. If true, create a fulltext search index for the attribute. Defaults to false."),
      (51, ":db/ident", ":db/fulltext"),
      (51, ":db/valueType", 24),
      (52, ":db/cardinality", 35),
      (52, ":db/doc", "A function-valued attribute for direct use by transactions and queries."),
      (52, ":db/ident", ":db/fn"),
      (52, ":db/valueType", 26),
      (53, ":db/ident", ":db.bootstrap/part"),
      (54, ":db/code", "(clojure.core/fn [db e] (datomic.builtins/build-retract-args db e))"),
      (54, ":db/doc", "Retract all facts about an entity, including references from other entities and component attributes recursively."),
      (54, ":db/ident", ":db.fn/retractEntity"),
      (54, ":db/ident", ":db/retractEntity"),
      (54, ":db/lang", 48),
      (55, ":db/code", "(clojure.core/fn [db e a ov nv] (datomic.builtins/compare-and-swap db e a ov nv))"),
      (55, ":db/doc", "Compare and swap the value of an entity's attribute."),
      (55, ":db/ident", ":db.fn/cas"),
      (55, ":db/ident", ":db/cas"),
      (55, ":db/lang", 48),
      (56, ":db/doc", "Value type for UUIDs. Maps to java.util.UUID on the JVM."),
      (56, ":db/ident", ":db.type/uuid"),
      (56, ":fressian/tag", ":uuid"),
      (57, ":db/doc", "Floating point value type. Same semantics as a Java double: double-precision 64-bit IEEE 754 floating point."),
      (57, ":db/ident", ":db.type/double"),
      (57, ":fressian/tag", ":double"),
      (58, ":db/doc", "Floating point value type. Same semantics as a Java float: single-precision 32-bit IEEE 754 floating point."),
      (58, ":db/ident", ":db.type/float"),
      (58, ":fressian/tag", ":float"),
      (59, ":db/doc", "Value type for URIs. Maps to java.net.URI on the JVM."),
      (59, ":db/ident", ":db.type/uri"),
      (59, ":fressian/tag", ":uri"),
      (60, ":db/doc", "Value type for arbitrary precision integers. Maps to java.math.BigInteger on the JVM."),
      (60, ":db/ident", ":db.type/bigint"),
      (60, ":fressian/tag", ":bigint"),
      (61, ":db/doc", "Value type for arbitrary precision floating point numbers. Maps to java.math.BigDecimal on the JVM."),
      (61, ":db/ident", ":db.type/bigdec"),
      (61, ":fressian/tag", ":bigdec"),
      (62, ":db/cardinality", 35),
      (62, ":db/doc", "Documentation string for an entity."),
      (62, ":db/fulltext", true),
      (62, ":db/ident", ":db/doc"),
      (62, ":db/valueType", 23),

      (63, ":db/cardinality", 35),
      (63, ":db/fulltext", true),
      (63, ":db/ident", ":ns/str"),
      (63, ":db/index", true),
      (63, ":db/valueType", 23),

      (64, ":db/cardinality", 35),
      (64, ":db/ident", ":ns/int"),
      (64, ":db/index", true),
      (64, ":db/valueType", 22),

      (65, ":db/cardinality", 35),
      (65, ":db/ident", ":ns/long"),
      (65, ":db/index", true),
      (65, ":db/valueType", 22),

      (66, ":db/cardinality", 35),
      (66, ":db/ident", ":ns/float"),
      (66, ":db/index", true),
      (66, ":db/valueType", 57),
      (67, ":db/cardinality", 35),
      (67, ":db/ident", ":ns/double"),
      (67, ":db/index", true),
      (67, ":db/valueType", 57),
      (68, ":db/cardinality", 35),
      (68, ":db/ident", ":ns/bool"),
      (68, ":db/index", true),
      (68, ":db/valueType", 24),
      (69, ":db/cardinality", 35),
      (69, ":db/ident", ":ns/bigInt"),
      (69, ":db/index", true),
      (69, ":db/valueType", 60),
      (70, ":db/cardinality", 35),
      (70, ":db/ident", ":ns/bigDec"),
      (70, ":db/index", true),
      (70, ":db/valueType", 61),
      (71, ":db/cardinality", 35),
      (71, ":db/ident", ":ns/date"),
      (71, ":db/index", true),
      (71, ":db/valueType", 25),
      (72, ":db/cardinality", 35),
      (72, ":db/ident", ":ns/uuid"),
      (72, ":db/index", true),
      (72, ":db/valueType", 56),
      (73, ":db/cardinality", 35),
      (73, ":db/ident", ":ns/uri"),
      (73, ":db/index", true),
      (73, ":db/valueType", 59),
      (74, ":db/cardinality", 35),
      (74, ":db/ident", ":ns/enum"),
      (74, ":db/index", true),
      (74, ":db/valueType", 20),
      (75, ":db/cardinality", 35),
      (75, ":db/ident", ":ns/byte"),
      (75, ":db/index", true),
      (75, ":db/valueType", 27),
      (76, ":db/cardinality", 35),
      (76, ":db/ident", ":ns/parent"),
      (76, ":db/index", true),
      (76, ":db/valueType", 20),
      (77, ":db/cardinality", 35),
      (77, ":db/ident", ":ns/ref1"),
      (77, ":db/index", true),
      (77, ":db/valueType", 20),
      (78, ":db/cardinality", 35),
      (78, ":db/ident", ":ns/refSub1"),
      (78, ":db/index", true),
      (78, ":db/isComponent", true),
      (78, ":db/valueType", 20),
      (79, ":db/cardinality", 36),
      (79, ":db/fulltext", true),
      (79, ":db/ident", ":ns/strs"),
      (79, ":db/index", true),
      (79, ":db/valueType", 23),
      (80, ":db/cardinality", 36),
      (80, ":db/ident", ":ns/ints"),
      (80, ":db/index", true),
      (80, ":db/valueType", 22),
      (81, ":db/cardinality", 36),
      (81, ":db/ident", ":ns/longs"),
      (81, ":db/index", true),
      (81, ":db/valueType", 22),
      (82, ":db/cardinality", 36),
      (82, ":db/ident", ":ns/floats"),
      (82, ":db/index", true),
      (82, ":db/valueType", 57),
      (83, ":db/cardinality", 36),
      (83, ":db/ident", ":ns/doubles"),
      (83, ":db/index", true),
      (83, ":db/valueType", 57),
      (84, ":db/cardinality", 36),
      (84, ":db/ident", ":ns/bools"),
      (84, ":db/index", true),
      (84, ":db/valueType", 24),
      (85, ":db/cardinality", 36),
      (85, ":db/ident", ":ns/bigInts"),
      (85, ":db/index", true),
      (85, ":db/valueType", 60),
      (86, ":db/cardinality", 36),
      (86, ":db/ident", ":ns/bigDecs"),
      (86, ":db/index", true),
      (86, ":db/valueType", 61),
      (87, ":db/cardinality", 36),
      (87, ":db/ident", ":ns/dates"),
      (87, ":db/index", true),
      (87, ":db/valueType", 25),
      (88, ":db/cardinality", 36),
      (88, ":db/ident", ":ns/uuids"),
      (88, ":db/index", true),
      (88, ":db/valueType", 56),
      (89, ":db/cardinality", 36),
      (89, ":db/ident", ":ns/uris"),
      (89, ":db/index", true),
      (89, ":db/valueType", 59),
      (90, ":db/cardinality", 36),
      (90, ":db/ident", ":ns/enums"),
      (90, ":db/index", true),
      (90, ":db/valueType", 20),
      (91, ":db/cardinality", 36),
      (91, ":db/ident", ":ns/bytes"),
      (91, ":db/index", true),
      (91, ":db/valueType", 27),
      (92, ":db/cardinality", 36),
      (92, ":db/ident", ":ns/parents"),
      (92, ":db/index", true),
      (92, ":db/valueType", 20),
      (93, ":db/cardinality", 36),
      (93, ":db/ident", ":ns/refs1"),
      (93, ":db/index", true),
      (93, ":db/valueType", 20),
      (94, ":db/cardinality", 36),
      (94, ":db/ident", ":ns/refsSub1"),
      (94, ":db/index", true),
      (94, ":db/isComponent", true),
      (94, ":db/valueType", 20),
      (95, ":db/cardinality", 36),
      (95, ":db/fulltext", true),
      (95, ":db/ident", ":ns/strMap"),
      (95, ":db/index", true),
      (95, ":db/valueType", 23),
      (96, ":db/cardinality", 35),
      (96, ":db/fulltext", true),
      (96, ":db/ident", ":ns/strMapK"),
      (96, ":db/index", true),
      (96, ":db/valueType", 23),
      (97, ":db/cardinality", 36),
      (97, ":db/ident", ":ns/intMap"),
      (97, ":db/index", true),
      (97, ":db/valueType", 23),
      (98, ":db/cardinality", 35),
      (98, ":db/ident", ":ns/intMapK"),
      (98, ":db/index", true),
      (98, ":db/valueType", 22),
      (99, ":db/cardinality", 36),
      (99, ":db/ident", ":ns/longMap"),
      (99, ":db/index", true),
      (99, ":db/valueType", 23),
      (100, ":db/cardinality", 35),
      (100, ":db/ident", ":ns/longMapK"),
      (100, ":db/index", true),
      (100, ":db/valueType", 22),
      (101, ":db/cardinality", 36),
      (101, ":db/ident", ":ns/floatMap"),
      (101, ":db/index", true),
      (101, ":db/valueType", 23),
      (102, ":db/cardinality", 35),
      (102, ":db/ident", ":ns/floatMapK"),
      (102, ":db/index", true),
      (102, ":db/valueType", 57),
      (103, ":db/cardinality", 36),
      (103, ":db/ident", ":ns/doubleMap"),
      (103, ":db/index", true),
      (103, ":db/valueType", 23),
      (104, ":db/cardinality", 35),
      (104, ":db/ident", ":ns/doubleMapK"),
      (104, ":db/index", true),
      (104, ":db/valueType", 57),
      (105, ":db/cardinality", 36),
      (105, ":db/ident", ":ns/boolMap"),
      (105, ":db/index", true),
      (105, ":db/valueType", 23),
      (106, ":db/cardinality", 35),
      (106, ":db/ident", ":ns/boolMapK"),
      (106, ":db/index", true),
      (106, ":db/valueType", 24),
      (107, ":db/cardinality", 36),
      (107, ":db/ident", ":ns/bigIntMap"),
      (107, ":db/index", true),
      (107, ":db/valueType", 23),
      (108, ":db/cardinality", 35),
      (108, ":db/ident", ":ns/bigIntMapK"),
      (108, ":db/index", true),
      (108, ":db/valueType", 60),
      (109, ":db/cardinality", 36),
      (109, ":db/ident", ":ns/bigDecMap"),
      (109, ":db/index", true),
      (109, ":db/valueType", 23),
      (110, ":db/cardinality", 35),
      (110, ":db/ident", ":ns/bigDecMapK"),
      (110, ":db/index", true),
      (110, ":db/valueType", 61),
      (111, ":db/cardinality", 36),
      (111, ":db/ident", ":ns/dateMap"),
      (111, ":db/index", true),
      (111, ":db/valueType", 23),
      (112, ":db/cardinality", 35),
      (112, ":db/ident", ":ns/dateMapK"),
      (112, ":db/index", true),
      (112, ":db/valueType", 25),
      (113, ":db/cardinality", 36),
      (113, ":db/ident", ":ns/uuidMap"),
      (113, ":db/index", true),
      (113, ":db/valueType", 23),
      (114, ":db/cardinality", 35),
      (114, ":db/ident", ":ns/uuidMapK"),
      (114, ":db/index", true),
      (114, ":db/valueType", 56),
      (115, ":db/cardinality", 36),
      (115, ":db/ident", ":ns/uriMap"),
      (115, ":db/index", true),
      (115, ":db/valueType", 23),
      (116, ":db/cardinality", 35),
      (116, ":db/ident", ":ns/uriMapK"),
      (116, ":db/index", true),
      (116, ":db/valueType", 59),
      (117, ":db/cardinality", 36),
      (117, ":db/ident", ":ns/byteMap"),
      (117, ":db/index", true),
      (117, ":db/valueType", 27),
      (118, ":db/cardinality", 35),
      (118, ":db/ident", ":ns/byteMapK"),
      (118, ":db/index", true),
      (118, ":db/valueType", 27),
      (119, ":db/cardinality", 35),
      (119, ":db/ident", ":ref1/str1"),
      (119, ":db/index", true),
      (119, ":db/valueType", 23),
      (120, ":db/cardinality", 35),
      (120, ":db/ident", ":ref1/int1"),
      (120, ":db/index", true),
      (120, ":db/valueType", 22),
      (121, ":db/cardinality", 35),
      (121, ":db/ident", ":ref1/enum1"),
      (121, ":db/index", true),
      (121, ":db/valueType", 20),
      (122, ":db/cardinality", 35),
      (122, ":db/ident", ":ref1/ref2"),
      (122, ":db/index", true),
      (122, ":db/valueType", 20),
      (123, ":db/cardinality", 35),
      (123, ":db/ident", ":ref1/refSub2"),
      (123, ":db/index", true),
      (123, ":db/isComponent", true),
      (123, ":db/valueType", 20),
      (124, ":db/cardinality", 36),
      (124, ":db/ident", ":ref1/strs1"),
      (124, ":db/index", true),
      (124, ":db/valueType", 23),
      (125, ":db/cardinality", 36),
      (125, ":db/ident", ":ref1/ints1"),
      (125, ":db/index", true),
      (125, ":db/valueType", 22),
      (126, ":db/cardinality", 36),
      (126, ":db/ident", ":ref1/refs2"),
      (126, ":db/index", true),
      (126, ":db/valueType", 20),
      (127, ":db/cardinality", 36),
      (127, ":db/ident", ":ref1/refsSub2"),
      (127, ":db/index", true),
      (127, ":db/isComponent", true),
      (127, ":db/valueType", 20),
      (128, ":db/cardinality", 35),
      (128, ":db/ident", ":ref2/str2"),
      (128, ":db/index", true),
      (128, ":db/valueType", 23),
      (129, ":db/cardinality", 35),
      (129, ":db/ident", ":ref2/int2"),
      (129, ":db/index", true),
      (129, ":db/valueType", 22),
      (130, ":db/cardinality", 35),
      (130, ":db/ident", ":ref2/enum2"),
      (130, ":db/index", true),
      (130, ":db/valueType", 20),
      (131, ":db/cardinality", 36),
      (131, ":db/ident", ":ref2/strs2"),
      (131, ":db/index", true),
      (131, ":db/valueType", 23),
      (132, ":db/cardinality", 36),
      (132, ":db/ident", ":ref2/ints2"),
      (132, ":db/index", true),
      (132, ":db/valueType", 22),
      (13194139533366L, ":db/txInstant", "Thu Jan 01 01:00:00 CET 1970"),
      (13194139533368L, ":db/txInstant", "Thu Jan 01 01:00:00 CET 1970"),
      (13194139533375L, ":db/txInstant", "Thu Jan 01 01:00:00 CET 1970"),
      (13194139534312L, ":db/txInstant", "Mon Jul 09 09:23:49 CEST 2018"),
      (13194139534313L, ":db/txInstant", "Mon Jul 09 09:23:50 CEST 2018"),
      (17592186045418L, ":db/ident", ":ns.enum/enum0"),
      (17592186045419L, ":db/ident", ":ns.enum/enum1"),
      (17592186045420L, ":db/ident", ":ns.enum/enum2"),
      (17592186045421L, ":db/ident", ":ns.enum/enum3"),
      (17592186045422L, ":db/ident", ":ns.enum/enum4"),
      (17592186045423L, ":db/ident", ":ns.enum/enum5"),
      (17592186045424L, ":db/ident", ":ns.enum/enum6"),
      (17592186045425L, ":db/ident", ":ns.enum/enum7"),
      (17592186045426L, ":db/ident", ":ns.enum/enum8"),
      (17592186045427L, ":db/ident", ":ns.enum/enum9"),
      (17592186045428L, ":db/ident", ":ns.enums/enum0"),
      (17592186045429L, ":db/ident", ":ns.enums/enum1"),
      (17592186045430L, ":db/ident", ":ns.enums/enum2"),
      (17592186045431L, ":db/ident", ":ns.enums/enum3"),
      (17592186045432L, ":db/ident", ":ns.enums/enum4"),
      (17592186045433L, ":db/ident", ":ns.enums/enum5"),
      (17592186045434L, ":db/ident", ":ns.enums/enum6"),
      (17592186045435L, ":db/ident", ":ns.enums/enum7"),
      (17592186045436L, ":db/ident", ":ns.enums/enum8"),
      (17592186045437L, ":db/ident", ":ns.enums/enum9"),
      (17592186045438L, ":db/ident", ":ref1.enum1/enum10"),
      (17592186045439L, ":db/ident", ":ref1.enum1/enum11"),
      (17592186045440L, ":db/ident", ":ref1.enum1/enum12"),
      (17592186045441L, ":db/ident", ":ref2.enum2/enum20"),
      (17592186045442L, ":db/ident", ":ref2.enum2/enum21"),
      (17592186045443L, ":db/ident", ":ref2.enum2/enum22")
    )

    ok
    //    Db.e.a.v.apply(count").get.head === 16
  }

}