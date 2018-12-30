
package molecule.coretests.generic

import molecule.api.out10._
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.ops.exception.VerifyModelException
import molecule.util.expectCompileError


class SchemaTest extends CoreSpec {

  sequential


  "Partition schema values" >> {

    implicit val conn = recreateDbFrom(PartitionTestSchema)

    "part" >> {

      Schema.part.get === List("lit", "gen")

      Schema.part("gen").get === List("gen")
      Schema.part("gen", "lit").get === List("lit", "gen")

      Schema.part.not("gen").get === List("lit")
      Schema.part.not("gen", "lit").get === Nil

      Schema.part(count).get === List(2)


      // Since all attributes have an attribute name, a tacit `part_` makes no difference
      Schema.ns.get.sorted === List("book", "person", "profession")
      Schema.part_.ns.get.sorted === List("book", "person", "profession")

      // We can though filter by one or more tacit attribute names
      Schema.part_("gen").ns.get.sorted === List("person", "profession")
      Schema.part_("gen").ns.attr.get === List(
        ("profession", "name"),
        ("person", "name"),
        ("person", "gender"),
        ("person", "professions")
      )

      // Namespaces of partitions named "gen" or "lit"
      Schema.part_("gen", "lit").ns.get.sorted === List("book", "person", "profession")

      // Negate tacit partition
      Schema.part_.not("lit").ns.get.sorted === List("person", "profession")
      Schema.part_.not("gen", "lit").ns.get === Nil
    }


    "nsFull" >> {

      // Partition-prefixed namespaces
      Schema.nsFull.get.sorted === List("gen_Person", "gen_Profession", "lit_Book")
      // Namespaces without partition prefix
      Schema.ns.get.sorted === List("book", "person", "profession")

      Schema.nsFull("gen_Profession").get === List("gen_Profession")
      Schema.nsFull("gen_Profession", "lit_Book").get.sorted === List("gen_Profession", "lit_Book")

      Schema.nsFull.not("gen_Profession").get.sorted === List("gen_Person", "lit_Book")
      Schema.nsFull.not("gen_Profession", "lit_Book").get === List("gen_Person")

      Schema.nsFull(count).get === List(3)


      // Since all attributes have a namespace, a tacit `nsFull_` makes no difference
      Schema.a.get.size === 9
      Schema.nsFull_.a.get.size === 9

      // We can though filter by one or more tacit namespace names
      Schema.nsFull_("gen_Profession").attr.get.sorted === List("name")
      Schema.nsFull_("gen_Person").attr.get.sorted.sorted === List("gender", "name", "professions")

      // Attributes in namespace "ref1" or "gen_Person"
      Schema.nsFull_("gen_Profession", "gen_Person").attr.get.sorted === List(
        // Note that duplicate attribute `name`s have coalesced in the result Set
        "gender", "name", "professions"
      )

      // Negate tacit namespace name
      Schema.nsFull_.not("lit_Book").attr.get.sorted === List(
        "gender", "name", "professions"
      )
      Schema.nsFull_.not("lit_Book", "gen_Person").attr.get.sorted === List(
        "name"
      )

      // Enum
      Schema.part_("gen").ns_("person").attr_("gender").enum.get === List("female", "male")

      // All enums grouped by attribute
      Schema.a.enum.get.groupBy(_._1).map(g => g._1 -> g._2.map(_._2).sorted) === Map(
        ":gen_Person/gender" -> List("female", "male"),
        ":lit_Book/cat" -> List("bad", "good")
      )
    }
  }


  implicit val conn = recreateDbFrom(CoreTestSchema)

  "Schema attributes" >> {

    "id" >> {

      Schema.id.get.size === 70

      Schema.id.get(5) === List(97, 98, 99, 100, 101)

      Schema.id(97).get(5) === List(97)
      Schema.id(97, 98).get(5) === List(97, 98)

      Schema.id.not(97).get.size === 69
      Schema.id.not(97, 98).get.size === 68

      Schema.id(count).get === List(70)


      // Since all attributes have an id, a tacit `id_` makes no difference
      Schema.id_.attr.get.size === 70
      Schema.id_.attr.get(3) === List("floats", "double", "byte")

      // We can though filter by one or more tacit attribute ids
      Schema.id_(97).attr.get === List("intMap")
      Schema.id_(99, 100).attr.get === List("longMap", "longMapK")

      Schema.id_.not(97).attr.get.size === 69
      Schema.id_.not(97, 98).attr.get.size === 68
    }


    "a" >> {

      Schema.a.get.size === 70
      Schema.a.get(3) === List(":ns/bigInts", ":ns/enums", ":ns/dates")

      Schema.a(":ns/str").get === List(":ns/str")
      Schema.a(":ns/str", ":ns/int").get === List(":ns/int", ":ns/str")

      Schema.a.not(":ns/str").get.size === 69
      Schema.a.not(":ns/str", ":ns/int").get.size === 68

      Schema.a(count).get === List(70)


      // Since all attributes have an `ident`, a tacit `ident_` makes no difference
      Schema.a_.ns.get.size === 3

      // We can though filter by one or more tacit ident names
      Schema.a_(":ns/str").ns.get.sorted === List("ns")

      // Namespaces with attributes ident ":ns/str" or ":ref1/str1"
      Schema.a_(":ns/str", ":ref1/str1").ns.get.sorted === List("ns", "ref1")

      // Negate tacit ident value
      // Note though that since other attributes than `str` exist, the namespace is still returned
      Schema.a_.not(":ref2/int2").ns.get.sorted === List("ns", "ref1", "ref2")

      // If we exclude all attributes in a namespace, it won't be returned
      Schema.a_.not(":ref2/int2", ":ref2/enum2", ":ref2/strs2", ":ref2/str2", ":ref2/ints2")
        .ns.get.sorted === List("ns", "ref1")
    }


    "part when not defined" >> {

      // Empty String returned when no custom partitions are defined
      Schema.part.get === List("")
    }


    "ns" >> {

      Schema.ns.get === List("ns", "ref1", "ref2")

      Schema.ns("ref1").get === List("ref1")
      Schema.ns("ref1", "ref2").get === List("ref1", "ref2")

      Schema.ns.not("ref1").get === List("ns", "ref2")
      Schema.ns.not("ref1", "ref2").get === List("ns")

      Schema.ns(count).get === List(3)


      // Since all attributes have a namespace, a tacit `ns_` makes no difference
      Schema.ns_.attr.get.size === 70

      // We can though filter by one or more tacit namespace names
      Schema.ns_("ref1").attr.get.sorted === List(
        "enum1", "int1", "ints1", "ref2", "refSub2", "refs2", "refsSub2", "str1", "strs1"
      )

      // Attributes in namespace "ref1" or "ref2"
      Schema.ns_("ref1", "ref2").attr.get.sorted === List(
        "enum1", "enum2", "int1", "int2", "ints1", "ints2", "ref2", "refSub2", "refs2", "refsSub2", "str1", "str2", "strs1", "strs2"
      )

      // Negate tacit namespace name
      Schema.ns_.not("ns").attr.get.sorted === List(
        "enum1", "enum2", "int1", "int2", "ints1", "ints2", "ref2", "refSub2", "refs2", "refsSub2", "str1", "str2", "strs1", "strs2"
      )
      Schema.ns_.not("ns", "ref2").attr.get.sorted === List(
        "enum1", "int1", "ints1", "ref2", "refSub2", "refs2", "refsSub2", "str1", "strs1"
      )
    }


    "attr" >> {

      Schema.attr.get.size === 70
      Schema.attr.get(5) === List("floats", "double", "byte", "str1", "uri")

      Schema.attr("str").get === List("str")
      Schema.attr("str", "int").get === List("str", "int")

      Schema.attr.not("str").get.size === 69
      Schema.attr.not("str", "int").get.size === 68

      Schema.attr(count).get === List(70)


      // Since all attributes have an attribute name, a tacit `a_` makes no difference
      Schema.attr_.ns.get.size === 3

      // We can though filter by one or more tacit attribute names
      Schema.attr_("str").ns.get.sorted === List("ns")

      // Namespaces with attributes named "str" or "str1"
      Schema.attr_("str", "str1").ns.get.sorted === List("ns", "ref1")

      // Negate tacit attribute name
      // Note though that since other attributes than `str` exist, the namespace is still returned
      Schema.attr_.not("int2").ns.get.sorted === List("ns", "ref1", "ref2")

      // If we exclude all attributes in a namespace, it won't be returned
      Schema.attr_.not("int2", "enum2", "strs2", "str2", "ints2").ns.get.sorted === List("ns", "ref1")
    }


    "tpe" >> {

      // Datomic types of schema attributes
      // Note that attributes defined being of Scala type
      // - `Integer` are internally saved as type `long` in Datomic
      // - `Float` are internally saved as type `double` in Datomic
      // Molecule transparently converts back and forth so that application code only have to consider the Scala type.

      Schema.tpe.get === List(
        "ref", "bigdec", "string", "bytes", "double", "long", "uri", "uuid", "bigint", "boolean", "instant"
      )

      Schema.tpe("string").get === List("string")
      Schema.tpe("string", "long").get === List("string", "long")

      Schema.tpe.not("instant").get === List(
        "ref", "bigdec", "string", "bytes", "double", "long", "uri", "uuid", "bigint", "boolean"
      )
      Schema.tpe.not("instant", "boolean").get === List(
        "ref", "bigdec", "string", "bytes", "double", "long", "uri", "uuid", "bigint"
      )

      Schema.tpe(count).get === List(11)


      // Since all attributes have a value type, a tacit `tpe_` makes no difference
      Schema.ns.get.size === 3
      Schema.tpe_.ns.get.size === 3

      // We can though filter by one or more tacit value types
      Schema.tpe_("string").ns.get.sorted === List("ns", "ref1", "ref2")
      // Only namespace `ns` has attributes of type Boolean
      Schema.tpe_("boolean").ns.get.sorted === List("ns")

      // Namespaces with attributes of type string or long
      Schema.tpe_("string", "long").ns.get.sorted === List("ns", "ref1", "ref2")

      // Negate tacit attribute type
      // Note though that since other attributes have other types, the namespace is still returned
      Schema.tpe_.not("string").ns.get.sorted === List("ns", "ref1", "ref2")

      // If we exclude all attribute types in a namespace, it won't be returned
      Schema.tpe_.not("string", "long", "ref").ns.get.sorted === List("ns")
    }


    "card" >> {

      Schema.card.get === List("one", "many")

      Schema.card("one").get === List("one")
      Schema.card("one", "many").get === List("one", "many")

      Schema.card.not("one").get === List("many")
      Schema.card.not("one", "many").get === Nil

      Schema.card(count).get === List(2)


      // Since all attributes have a cardinality, a tacit `card_` makes no difference
      Schema.a.get.size === 70
      Schema.card_.a.get.size === 70

      // We can though filter by cardinality
      Schema.card_("one").a.get.size === 36
      Schema.card_("many").a.get.size === 34

      // Attributes of cardinality one or many, well that's all
      Schema.card_("one", "many").a.get.size === 70

      // Negate tacit namespace name
      Schema.card_.not("one").a.get.size === 34 // many
      Schema.card_.not("many").a.get.size === 36 // one
      Schema.card_.not("one", "many").a.get.size === 0
    }
  }


  "Schema attribute options" >> {

    "doc" >> {

      // 2 core attributes are documented
      Schema.doc.get === List(
        "Card one String attribute",
        "Card one Int attribute"
      )
      // See what attributes is
      Schema.a.doc.get === List(
        (":ns/str", "Card one String attribute"),
        (":ns/int", "Card one Int attribute")
      )

      // Filtering by a complete `doc_` is probably not that useful
      Schema.doc("Card one Int attribute").get === List("Card one Int attribute")
      // .. likely the same for negation
      Schema.doc.not("Card one String attribute").get === List("Card one Int attribute")

      // Instead, use fulltext search for a whole word in doc texts
      Schema.doc.contains("Int").get === List(
        "Card one Int attribute"
      )
      Schema.doc.contains("attribute").get === List(
        "Card one String attribute",
        "Card one Int attribute"
      )
      // Fulltext search for multiple words not allowed
      expectCompileError(
        """m(Schema.doc.contains("Int", "String"))""",
        "molecule.transform.exception.Model2QueryException: " +
          "Fulltext search can only be performed with 1 search phrase.")

      // Count documented attributes
      Schema.doc(count).get === List(2)


      // Use tacit `doc_` to filter documented attributes
      // All attributes
      Schema.a.get.size === 70
      // Documented attributes
      Schema.doc_.a.get.size === 2

      // Filtering by a complete tacit `doc_` text is probably not that useful
      Schema.doc_("Card one Int attribute").a.get === List(":ns/int")
      // .. likely the same for negation
      Schema.doc_.not("Card one Int attribute").a.get === List(":ns/str")


      // Tacit fulltext search in doc texts
      Schema.doc_.contains("Int").a.get === List(":ns/int")
      Schema.doc_.contains("one").a.get === List(":ns/int", ":ns/str")

      // Get optional attribute doc text with `doc$`
      Schema.attr_("bool", "str").a.doc$.get === List(
        (":ns/bool", None),
        (":ns/str", Some("Card one String attribute"))
      )

      // Filter by applying optional attribute doc text string
      val someDocText1 = Some("Card one String attribute")
      val someDocText2 = None
      Schema.attr_("bool", "str").a.doc$(someDocText1).get === List(
        (":ns/str", Some("Card one String attribute"))
      )
      Schema.attr_("bool", "str").a.doc$(someDocText2).get === List(
        (":ns/bool", None),
      )
    }


    "index" >> {

      // All attributes are indexed
      Schema.index.get === List(true) // no false
      Schema.a.index.get.size === 70

      Schema.a.index(true).get.size === 70
      Schema.a.index(false).get.size === 0

      Schema.a.index.not(true).get.size === 0
      Schema.a.index.not(false).get.size === 70

      // Count attribute indexing statuses (only true)
      Schema.index(count).get === List(1)


      // Using tacit `index_` is not that useful since all attributes are indexed by default
      Schema.a.get.size === 70
      Schema.index_.a.get.size === 70

      Schema.index_(true).a.get.size === 70
      Schema.index_.not(false).a.get.size === 70


      // Get optional attribute indexing status with `index$`
      Schema.attr_("bool", "str").a.index$.get === List(
        (":ns/bool", Some(true)),
        (":ns/str", Some(true)),
      )

      // Filter by applying optional attribute indexing status
      val some = Some(true)
      Schema.attr_("bool", "str").a.index$(some).get === List(
        (":ns/str", Some(true)),
        (":ns/bool", Some(true))
      )
      Schema.attr_("bool", "str").a.index$(Some(true)).get === List(
        (":ns/str", Some(true)),
        (":ns/bool", Some(true))
      )

      val none = None
      Schema.attr_("bool", "str").a.index$(none).get === Nil
      Schema.attr_("bool", "str").a.index$(None).get === Nil
    }


    "unique" >> {

      // Unique options
      Schema.unique.get === List("identity", "value")

      // Unique options
      Schema.a.unique.get === List(
        (":ref2/str2", "identity"),
        (":ref2/int2", "value"),
      )

      // Count attribute indexing statuses
      Schema.unique(count).get === List(2)

      Schema.a.unique("identity").get === List((":ref2/str2", "identity"))
      Schema.a.unique("value").get === List((":ref2/int2", "value"))

      Schema.a.unique.not("identity").get === List((":ref2/int2", "value"))
      Schema.a.unique.not("value").get === List((":ref2/str2", "identity"))


      // Filter attributes by tacit `unique_` option
      Schema.unique_.a.get === List(":ref2/int2", ":ref2/str2")

      Schema.unique_("identity").a.get === List(":ref2/str2")
      Schema.unique_.not("value").a.get === List(":ref2/str2")


      // Get optional attribute indexing status with `index$`
      Schema.attr_("str", "str2", "int2").a.unique$.get === List(
        (":ns/str", None),
        (":ref2/str2", Some("identity")),
        (":ref2/int2", Some("value")),
      )

      // Filter by applying optional attribute uniqueness status

      val some1 = Some("identity")
      Schema.attr_("str", "str2", "int2").a.unique$(some1).get === List(
        (":ref2/str2", Some("identity"))
      )

      val some2 = Some("value")
      Schema.attr_("str", "str2", "int2").a.unique$(some2).get === List(
        (":ref2/int2", Some("value"))
      )

      val none = None
      Schema.attr_("str", "str2", "int2").a.unique$(none).get === List(
        (":ns/str", None)
      )

      // Number of non-unique attributes
      Schema.a.unique$(None).get.size === 70 - 2
    }


    "fulltext" >> {

      // Fulltext options
      Schema.fulltext.get === List(true) // no false

      // Count attribute fulltext statuses (only true)
      Schema.fulltext(count).get === List(1)

      // Attributes with fulltext search
      Schema.a.fulltext.get === List(
        (":ns/strs", true),
        (":ns/str", true),
        (":ns/strMap", true),
        (":ns/strMapK", true)
      )

      Schema.a.fulltext(true).get.size === 4
      // Option is either true or non-asserted (nil/None), never false
      Schema.a.fulltext(false).get.size === 0

      Schema.a.fulltext.not(true).get.size === 0
      Schema.a.fulltext.not(false).get.size === 4


      // Filter attributes with tacit `fulltext_` option
      Schema.fulltext_.a.get === List(":ns/strMapK", ":ns/strMap", ":ns/strs", ":ns/str")

      Schema.fulltext_(true).a.get === List(":ns/strMapK", ":ns/strMap", ":ns/strs", ":ns/str")
      Schema.fulltext_.not(false).a.get === List(":ns/strMapK", ":ns/strMap", ":ns/strs", ":ns/str")


      // Get optional attribute fulltext status with `fulltext$`
      Schema.attr_("bool", "str").a.fulltext$.get === List(
        (":ns/bool", None),
        (":ns/str", Some(true))
      )

      // Filter by applying optional attribute fulltext search status
      val some = Some(true)
      Schema.attr_("bool", "str").a.fulltext$(some).get === List((":ns/str", Some(true)))
      Schema.attr_("bool", "str").a.fulltext$(Some(true)).get === List((":ns/str", Some(true)))

      val none = None
      Schema.attr_("bool", "str").a.fulltext$(none).get === List((":ns/bool", None))
      Schema.attr_("bool", "str").a.fulltext$(None).get === List((":ns/bool", None))

      // Number of attributes without fulltext search
      Schema.a.fulltext$(None).get.size === 70 - 4
    }


    "isComponent" >> {

      // Component status options - either true or non-asserted
      Schema.isComponent.get === List(true) // no false
      Schema.isComponent(count).get === List(1)

      // Component attributes
      Schema.a.isComponent.get === List(
        (":ref1/refsSub2", true),
        (":ns/refsSub1", true),
        (":ns/refSub1", true),
        (":ref1/refSub2", true)
      )

      Schema.a.isComponent(true).get.size === 4
      // Option is either true or non-asserted (nil/None), never false
      Schema.a.isComponent(false).get.size === 0

      Schema.a.isComponent.not(true).get.size === 0
      Schema.a.isComponent.not(false).get.size === 4


      // Filter attributes with tacit `isComponent_` option
      Schema.isComponent_.a.get === List(
        ":ref1/refSub2",
        ":ns/refsSub1",
        ":ref1/refsSub2",
        ":ns/refSub1"
      )
      Schema.isComponent_(true).a.get === List(
        ":ref1/refSub2",
        ":ns/refsSub1",
        ":ref1/refsSub2",
        ":ns/refSub1"
      )
      Schema.isComponent_.not(false).a.get === List(
        ":ref1/refSub2",
        ":ns/refsSub1",
        ":ref1/refsSub2",
        ":ns/refSub1"
      )


      // Get optional attribute component status with `isComponent$`
      Schema.attr_("bool", "refSub1").a.isComponent$.get === List(
        (":ns/bool", None),
        (":ns/refSub1", Some(true)),
      )

      // Filter by applying optional attribute component status
      val some = Some(true)
      Schema.attr_("bool", "refSub1").a.isComponent$(some).get === List((":ns/refSub1", Some(true)))
      Schema.attr_("bool", "refSub1").a.isComponent$(Some(true)).get === List((":ns/refSub1", Some(true)))

      val none = None
      Schema.attr_("bool", "refSub1").a.isComponent$(none).get === List((":ns/bool", None))
      Schema.attr_("bool", "refSub1").a.isComponent$(None).get === List((":ns/bool", None))

      // Number of non-component attributes
      Schema.a.isComponent$(None).get.size === 70 - 4
    }


    "noHistory" >> {

      // No-history status options - either true or non-asserted
      Schema.noHistory.get === List(true) // no false
      Schema.noHistory(count).get === List(1)

      // No-history attributes
      Schema.a.noHistory.get === List(
        (":ref2/ints2", true)
      )

      Schema.a.noHistory(true).get.size === 1
      // Option is either true or non-asserted (nil/None), never false
      Schema.a.noHistory(false).get.size === 0

      Schema.a.noHistory.not(true).get.size === 0
      Schema.a.noHistory.not(false).get.size === 1


      // Filter attributes with tacit `noHistory_` option
      Schema.noHistory_.a.get === List(":ref2/ints2")
      Schema.noHistory_(true).a.get === List(":ref2/ints2")
      Schema.noHistory_.not(false).a.get === List(":ref2/ints2")


      // Get optional attribute no-history status with `noHistory$`
      Schema.attr_("bool", "ints2").a.noHistory$.get === List(
        (":ns/bool", None),
        (":ref2/ints2", Some(true)),
      )

      // Filter by applying optional attribute no-history status
      val some = Some(true)
      Schema.attr_("bool", "ints2").a.noHistory$(some).get === List((":ref2/ints2", Some(true)))
      Schema.attr_("bool", "ints2").a.noHistory$(Some(true)).get === List((":ref2/ints2", Some(true)))

      val none = None
      Schema.attr_("bool", "ints2").a.noHistory$(none).get === List((":ns/bool", None))
      Schema.attr_("bool", "ints2").a.noHistory$(None).get === List((":ns/bool", None))

      // Number of non-component attributes
      Schema.a.noHistory$(None).get.size === 70 - 1
    }
  }


  "enum" >> {

    // Attribute/enum values in namespace `ref2`
    Schema.ns_("ref2").attr.enum.get.sorted === List(
      ("enum2", "enum20"),
      ("enum2", "enum21"),
      ("enum2", "enum22"),
    )

    // All enums grouped by ident
    Schema.a.enum.get.groupBy(_._1).map(g => g._1 -> g._2.map(_._2).sorted) === Map(
      ":ns/enum" -> List("enum0", "enum1", "enum2", "enum3", "enum4", "enum5", "enum6", "enum7", "enum8", "enum9"),
      ":ns/enums" -> List("enum0", "enum1", "enum2", "enum3", "enum4", "enum5", "enum6", "enum7", "enum8", "enum9"),
      ":ref1/enum1" -> List("enum10", "enum11", "enum12"),
      ":ref2/enum2" -> List("enum20", "enum21", "enum22"),
    )

    // Enums of a specific attribute
    Schema.a_(":ns/enum").enum.get.sorted === List(
      "enum0", "enum1", "enum2", "enum3", "enum4", "enum5", "enum6", "enum7", "enum8", "enum9"
    )


    Schema.a.enum("enum0").get === List(
      (":ns/enums", "enum0"),
      (":ns/enum", "enum0")
    )

    Schema.a.enum("enum0", "enum1").get.sortBy(r => (r._1, r._2)) === List(
      (":ns/enum", "enum0"),
      (":ns/enum", "enum1"),
      (":ns/enums", "enum0"),
      (":ns/enums", "enum1")
    )

    // How many enums in total (duplicate enum values coalesce)
    Schema.enum(count).get === List(16)

    // Enums per namespace
    Schema.ns.enum(count).get === List(
      ("ns", 10),
      ("ref1", 3),
      ("ref2", 3),
    )

    // Enums per namespace per attribute
    Schema.ns.attr.enum(count).get === List(
      ("ns", "enum", 10),
      ("ns", "enums", 10),
      ("ref1", "enum1", 3),
      ("ref2", "enum2", 3),
    )


    // Attributes with some enum value
    Schema.a.enum_("enum0").get.sorted === List(":ns/enum", ":ns/enums")
    Schema.a.enum_("enum0", "enum1").get.sorted === List(":ns/enum", ":ns/enums")


    // Excluding one enum value will still match the other values
    Schema.a.enum_.not("enum0").get === List(
      ":ref2/enum2",
      ":ns/enum",
      ":ns/enums",
      ":ref1/enum1"
    )

    // If we exclude all enum values of an attribute it won't be returned
    Schema.a.enum_.not("enum10", "enum11", "enum12").get === List(
      ":ref2/enum2",
      ":ns/enum",
      ":ns/enums"
    )
  }


  "t, tx, txInstant" >> {

    // Schema transaction time t
    Schema.t.get === List(1001)

    // Schema transaction entity id
    Schema.tx.get === List(13194139534313L)

    // todo
    // Schema transaction wall clock time (compare Schema tx Date with Log tx Date)
    //        val txInstant = Log.t(1001).txInstant.get
    //        Schema.txInstant.get === List(txInstant)
  }
}