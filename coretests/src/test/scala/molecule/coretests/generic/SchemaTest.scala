package molecule.coretests.generic

import molecule.core.util.{expectCompileError, DatomicPeer}
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out3._


class SchemaTest extends CoreSpec {


  "Partition schema values" >> {

    "part" in new PartitionSetup {

      Schema.part.get === List("lit", "gen")

      Schema.part("gen").get === List("gen")
      Schema.part("gen", "lit").get === List("lit", "gen")

      Schema.part.not("gen").get === List("lit")
      Schema.part.not("gen", "lit").get === Nil

      // All Schema attributes can be compared. But maybe not that useful,
      // so this is only tested here:
      Schema.part.>("gen").get === List("lit")
      Schema.part.>=("gen").get === List("lit", "gen")
      Schema.part.<=("gen").get === List("gen")
      Schema.part.<("gen").get === Nil

      Schema.part(count).get === List(2)


      // Since all attributes have an attribute name, a tacit `part_` makes no difference
      Schema.ns.get.sorted === List("Book", "Person", "Profession")
      Schema.part_.ns.get.sorted === List("Book", "Person", "Profession")

      // We can though filter by one or more tacit attribute names
      Schema.part_("gen").ns.get.sorted === List("Person", "Profession")
      Schema.part_("gen").ns.attr.get.sorted === List(
        ("Person", "gender"),
        ("Person", "name"),
        ("Person", "professions"),
        ("Profession", "name"),
      )

      // Namespaces of partitions named "gen" or "lit"
      Schema.part_("gen", "lit").ns.get.sorted === List("Book", "Person", "Profession")

      // Negate tacit partition
      Schema.part_.not("lit").ns.get.sorted === List("Person", "Profession")
      Schema.part_.not("gen", "lit").ns.get === Nil
    }


    "nsFull" in new PartitionSetup {

      // Partition-prefixed namespaces
      Schema.nsFull.get.sorted === List("gen_Person", "gen_Profession", "lit_Book")
      // Namespaces without partition prefix
      Schema.ns.get.sorted === List("Book", "Person", "Profession")

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

      // Attributes in namespace "Ref1" or "gen_Person"
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
      Schema.part_("gen").ns_("Person").attr_("gender").enum.get === List("female", "male")

      // All enums grouped by attribute
      Schema.a.enum.get.groupBy(_._1).map(g => g._1 -> g._2.map(_._2).sorted) === Map(
        ":gen_Person/gender" -> List("female", "male"),
        ":lit_Book/cat" -> List("bad", "good")
      )
    }
  }

  class SchemaSetup extends CoreSetup {
    // Differing counts and ids for different systems
    val List(attrCount, a1, a2, a3, card1count, card2count) = system match {
      case DatomicPeer => List(74, 97, 99, 100, 32, 42)
      case _           => List(71, 105, 107, 108, 31, 40)
    }
  }

  "Schema attributes" >> {

    "id" in new SchemaSetup {

      Schema.id.get.size === attrCount

      Schema.id.get(5) === List(97, 98, 99, 100, 101)

      Schema.id(97).get(5) === List(97)
      Schema.id(97, 98).get(5) === List(97, 98)

      Schema.id.not(97).get.size === attrCount - 1
      Schema.id.not(97, 98).get.size === attrCount - 2

      Schema.id(count).get === List(attrCount)


      // Since all attributes have an id, a tacit `id_` makes no difference
      Schema.id_.attr.get.size === attrCount
      Schema.id_.attr.get(3) === List("floats", "double", "str1")

      // We can though filter by one or more tacit attribute ids
      Schema.id_(a1).attr.get === List("longMap")
      Schema.id_(a2, a3).attr.get === List("doubleMap", "boolMap")

      Schema.id_.not(a1).attr.get.size === attrCount - 1
      Schema.id_.not(a2, a3).attr.get.size === attrCount - 2
    }


    "a" in new SchemaSetup {

      Schema.a.get.size === attrCount
      Schema.a.get(3) === List(":Ns/double", ":Ns/doubleMap", ":Ref2/ints2")

      Schema.a(":Ns/str").get === List(":Ns/str")
      Schema.a(":Ns/str", ":Ns/int").get === List(":Ns/int", ":Ns/str")

      Schema.a.not(":Ns/str").get.size === attrCount - 1
      Schema.a.not(":Ns/str", ":Ns/int").get.size === attrCount - 2

      Schema.a(count).get === List(attrCount)


      // Since all attributes have an `ident`, a tacit `ident_` makes no difference
      Schema.a_.ns.get.size === 5

      // We can though filter by one or more tacit ident names
      Schema.a_(":Ns/str").ns.get.sorted === List("Ns")

      // Namespaces with attributes ident ":Ns/str" or ":Ref1/str1"
      Schema.a_(":Ns/str", ":Ref1/str1").ns.get.sorted === List("Ns", "Ref1")

      // Negate tacit ident value
      // Note though that since other attributes than `str` exist, the namespace is still returned
      Schema.a_.not(":Ref2/int2").ns.get.sorted === List("Ns", "Ref1", "Ref2", "Ref3", "Ref4")

      // If we exclude all attributes in a namespace, it won't be returned
      Schema.a_.not(
        ":Ref2/str2",
        ":Ref2/int2",
        ":Ref2/enum2",
        ":Ref2/strs2",
        ":Ref2/ints2",
        ":Ref2/ref3",
        ":Ref2/refs3",
      ).ns.get.sorted === List("Ns", "Ref1", "Ref3", "Ref4")
    }


    "part when not defined" in new SchemaSetup {

      // Default `db.part/user` partition name returned when no custom partitions are defined
      Schema.part.get === List("db.part/user")

      // Note that when no custom partitions are defined, namespaces are not prefixed with any partition name
    }


    "nsFull" in new SchemaSetup {

      // `nsfull` always starts with lowercase letter as used in Datomic queries
      // - when partitions are defined: concatenates `part` + `ns`
      // - when partitions are not defined: `ns` starting with lower case letter

      Schema.nsFull.get === List("Ns", "Ref4", "Ref2", "Ref3", "Ref1")

      Schema.nsFull("Ref1").get === List("Ref1")
      Schema.nsFull("Ref1", "Ref2").get === List("Ref2", "Ref1")

      Schema.nsFull.not("Ref1").get.sorted === List("Ns", "Ref2", "Ref3", "Ref4")
      Schema.nsFull.not("Ref1", "Ref2").get.sorted === List("Ns", "Ref3", "Ref4")

      Schema.nsFull(count).get === List(5)


      // Since all attributes have a namespace, a tacit `nsFull_` makes no difference
      Schema.nsFull_.attr.get.size === attrCount

      // We can though filter by one or more tacit namespace names
      Schema.nsFull_("Ref1").attr.get.sorted === List(
        "enum1", "enums1", "int1", "intMap1", "ints1",
        "ref2", "refSub2", "refs2", "refsSub2", "str1", "strs1"
      )

      // Attributes in namespace "Ref1" or "Ref2"
      Schema.nsFull_("Ref1", "Ref2").attr.get.sorted === List(
        "enum1", "enum2", "enums1", "int1", "int2", "intMap1", "ints1",
        "ints2", "ref2", "ref3", "refSub2", "refs2", "refs3", "refsSub2",
        "str1", "str2", "strs1", "strs2"
      )

      // Negate tacit namespace name
      Schema.nsFull_.not("Ns").attr.get.sorted === List(
        "enum1", "enum2", "enum3", "enum4", "enums1",
        "int1", "int2", "int3", "int4", "intMap1",
        "ints1", "ints2", "ints3", "ints4",
        "ref2", "ref3", "ref4", "refSub2",
        "refs2", "refs3", "refs4", "refsSub2",
        "str1", "str2", "str3", "str4",
        "strs1", "strs2", "strs3", "strs4"
      )
      Schema.nsFull_.not("Ns", "Ref2").attr.get.sorted === List(
        "enum1", "enum3", "enum4", "enums1",
        "int1", "int3", "int4", "intMap1",
        "ints1", "ints3", "ints4",
        "ref2", "ref4", "refSub2",
        "refs2", "refs4", "refsSub2",
        "str1", "str3", "str4",
        "strs1", "strs3", "strs4"
      )
    }


    "ns" in new SchemaSetup {

      Schema.ns.get === List("Ns", "Ref4", "Ref2", "Ref3", "Ref1")

      Schema.nsFull.get === List("Ns", "Ref4", "Ref2", "Ref3", "Ref1")

      Schema.ns("Ref1").get === List("Ref1")
      Schema.ns("Ref1", "Ref2").get === List("Ref2", "Ref1")

      Schema.ns.not("Ref1").get.sorted === List("Ns", "Ref2", "Ref3", "Ref4")
      Schema.ns.not("Ref1", "Ref2").get.sorted === List("Ns", "Ref3", "Ref4")

      Schema.ns(count).get === List(5)


      // Since all attributes have a namespace, a tacit `ns_` makes no difference
      Schema.ns_.attr.get.size === attrCount

      // We can though filter by one or more tacit namespace names
      Schema.ns_("Ref1").attr.get.sorted === List(
        "enum1", "enums1", "int1", "intMap1", "ints1",
        "ref2", "refSub2", "refs2", "refsSub2", "str1", "strs1"
      )

      // Attributes in namespace "Ref1" or "Ref2"
      Schema.ns_("Ref1", "Ref2").attr.get.sorted === List(
        "enum1", "enum2", "enums1",
        "int1", "int2", "intMap1",
        "ints1", "ints2",
        "ref2", "ref3", "refSub2",
        "refs2", "refs3", "refsSub2",
        "str1", "str2",
        "strs1", "strs2"
      )

      // Negate tacit namespace name
      Schema.ns_.not("Ns").attr.get.sorted === List(
        "enum1", "enum2", "enum3", "enum4", "enums1",
        "int1", "int2", "int3", "int4", "intMap1",
        "ints1", "ints2", "ints3", "ints4",
        "ref2", "ref3", "ref4", "refSub2",
        "refs2", "refs3", "refs4", "refsSub2",
        "str1", "str2", "str3", "str4",
        "strs1", "strs2", "strs3", "strs4"
      )
      Schema.ns_.not("Ns", "Ref2").attr.get.sorted === List(
        "enum1", "enum3", "enum4", "enums1",
        "int1", "int3", "int4", "intMap1",
        "ints1", "ints3", "ints4",
        "ref2", "ref4", "refSub2",
        "refs2", "refs4", "refsSub2",
        "str1", "str3", "str4",
        "strs1", "strs3", "strs4"
      )
    }


    "attr" in new SchemaSetup {

      Schema.attr.get.size === attrCount
      if (system == DatomicPeer)
        Schema.attr.get(5) === List("floats", "double", "str1", "byte", "uri")
      else
        Schema.attr.get(5) === List("floats", "double", "str1", "uri", "dates")

      Schema.attr("str").get === List("str")
      Schema.attr("str", "int").get === List("str", "int")

      Schema.attr.not("str").get.size === attrCount - 1
      Schema.attr.not("str", "int").get.size === attrCount - 2

      Schema.attr(count).get === List(attrCount)


      // Since all attributes have an attribute name, a tacit `a_` makes no difference
      Schema.attr_.ns.get.size === 5

      // We can though filter by one or more tacit attribute names
      Schema.attr_("str").ns.get.sorted === List("Ns")

      // Namespaces with attributes named "str" or "str1"
      Schema.attr_("str", "str1").ns.get.sorted === List("Ns", "Ref1")

      // Negate tacit attribute name
      // Note though that since other attributes than `str` exist, the namespace is still returned
      Schema.attr_.not("int2").ns.get.sorted === List("Ns", "Ref1", "Ref2", "Ref3", "Ref4")

      // If we exclude all attributes in a namespace, it won't be returned
      Schema.attr_.not(
        "str2",
        "int2",
        "enum2",
        "strs2",
        "ints2",
        "ref3",
        "refs3",
      ).ns.get.sorted === List("Ns", "Ref1", "Ref3", "Ref4")
    }


    "tpe" in new SchemaSetup {

      // Datomic types of schema attributes
      // Note that attributes defined being of Scala type
      // - `Integer` are internally saved as type `long` in Datomic
      // - `Float` are internally saved as type `double` in Datomic
      // Molecule transparently converts back and forth so that application code only have to consider the Scala type.

      if (system == DatomicPeer)
        Schema.tpe.get.sorted === List(
          "bigdec",
          "bigint",
          "boolean",
          "bytes", // not in Client
          "double",
          "instant",
          "long",
          "ref",
          "string",
          "uri",
          "uuid",
        )
      else
        Schema.tpe.get.sorted === List(
          "bigdec",
          "bigint",
          "boolean",
          "double",
          "instant",
          "long",
          "ref",
          "string",
          "uri",
          "uuid",
        )

      Schema.tpe("string").get === List("string")
      Schema.tpe("string", "long").get === List("string", "long")

      if (system == DatomicPeer) {
        Schema.tpe.not("instant").get.sorted === List(
          "bigdec",
          "bigint",
          "boolean",
          "bytes",
          "double",
          "long",
          "ref",
          "string",
          "uri",
          "uuid",
        )
        Schema.tpe.not("instant", "boolean").get.sorted === List(
          "bigdec",
          "bigint",
          "bytes",
          "double",
          "long",
          "ref",
          "string",
          "uri",
          "uuid",
        )
        Schema.tpe(count).get === List(11)

      } else {

        // Client without bytes type
        Schema.tpe.not("instant").get.sorted === List(
          "bigdec",
          "bigint",
          "boolean",
          "double",
          "long",
          "ref",
          "string",
          "uri",
          "uuid",
        )
        Schema.tpe.not("instant", "boolean").get.sorted === List(
          "bigdec",
          "bigint",
          "double",
          "long",
          "ref",
          "string",
          "uri",
          "uuid",
        )
        Schema.tpe(count).get === List(10)
      }


      // Since all attributes have a value type, a tacit `tpe_` makes no difference
      Schema.ns.get.size === 5
      Schema.tpe_.ns.get.size === 5

      // We can though filter by one or more tacit value types
      Schema.tpe_("string").ns.get.sorted === List(
        "Ns", "Ref1", "Ref2", "Ref3", "Ref4")
      // Only namespace `ns` has attributes of type Boolean
      Schema.tpe_("boolean").ns.get.sorted === List("Ns")

      // Namespaces with attributes of type string or long
      Schema.tpe_("string", "long").ns.get.sorted === List(
        "Ns", "Ref1", "Ref2", "Ref3", "Ref4")

      // Negate tacit attribute type
      // Note though that since other attributes have other types, the namespace is still returned
      Schema.tpe_.not("string").ns.get.sorted === List(
        "Ns", "Ref1", "Ref2", "Ref3", "Ref4")

      // If we exclude all attribute types in a namespace, it won't be returned
      Schema.tpe_.not("string", "long", "ref").ns.get.sorted === List("Ns")
    }


    "card" in new SchemaSetup {

      Schema.card.get === List("one", "many")

      Schema.card("one").get === List("one")
      Schema.card("one", "many").get === List("one", "many")

      Schema.card.not("one").get === List("many")
      Schema.card.not("one", "many").get === Nil

      Schema.card(count).get === List(2)


      // Since all attributes have a cardinality, a tacit `card_` makes no difference
      Schema.a.get.size === attrCount
      Schema.card_.a.get.size === attrCount

      // We can though filter by cardinality
      Schema.card_("one").a.get.size === card1count
      Schema.card_("many").a.get.size === card2count

      // Attributes of cardinality one or many, well that's all
      Schema.card_("one", "many").a.get.size === attrCount

      // Negate tacit namespace name
      Schema.card_.not("one").a.get.size === card2count // many
      Schema.card_.not("many").a.get.size === card1count // one
      Schema.card_.not("one", "many").a.get.size === 0
    }
  }


  "Schema attribute options" >> {

    "doc" in new SchemaSetup {

      // 2 core attributes are documented
      Schema.doc.get === List(
        "Card one String attribute",
        "Card one Int attribute"
      )
      // See what attributes is
      Schema.a.doc.get === List(
        (":Ns/str", "Card one String attribute"),
        (":Ns/int", "Card one Int attribute")
      )

      // Filtering by a complete `doc_` is probably not that useful
      Schema.doc("Card one Int attribute").get === List(
        "Card one Int attribute")
      // .. likely the same for negation
      Schema.doc.not("Card one String attribute").get === List(
        "Card one Int attribute")

      // Docs only searchable with Peer (requires fulltext search)
      if (system == DatomicPeer) {
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
          "molecule.core.transform.exception.Model2QueryException: " +
            "Fulltext search can only be performed with 1 search phrase.")
      }

      // Count documented attributes
      Schema.doc(count).get === List(2)


      // Use tacit `doc_` to filter documented attributes
      // All attributes
      Schema.a.get.size === attrCount
      // Documented attributes
      Schema.doc_.a.get.size === 2

      // Filtering by a complete tacit `doc_` text is probably not that useful
      Schema.doc_("Card one Int attribute").a.get === List(":Ns/int")
      // .. likely the same for negation
      Schema.doc_.not("Card one Int attribute").a.get === List(":Ns/str")


      // Docs only searchable with Peer (requires fulltext search)
      if (system == DatomicPeer) {
        // Tacit fulltext search in doc texts
        Schema.doc_.contains("Int").a.get === List(":Ns/int")
        Schema.doc_.contains("one").a.get === List(":Ns/int", ":Ns/str")
      }

      // Get optional attribute doc text with `doc$`
      Schema.attr_("bool", "str").a.doc$.get === List(
        (":Ns/str", Some("Card one String attribute")),
        (":Ns/bool", None),
      )

      // Filter by applying optional attribute doc text string
      val someDocText1 = Some("Card one String attribute")
      val someDocText2 = None
      Schema.attr_("bool", "str").a.doc$(someDocText1).get === List(
        (":Ns/str", Some("Card one String attribute"))
      )
      Schema.attr_("bool", "str").a.doc$(someDocText2).get === List(
        (":Ns/bool", None),
      )
    }


    "index" in new SchemaSetup {

      // Index option only available in Peer
      if (system == DatomicPeer) {

        // All attributes are indexed
        Schema.index.get === List(true) // no false
        Schema.a.index.get.size === attrCount

        Schema.a.index(true).get.size === attrCount
        Schema.a.index(false).get.size === 0

        Schema.a.index.not(true).get.size === 0
        Schema.a.index.not(false).get.size === attrCount

        // Count attribute indexing statuses (only true)
        Schema.index(count).get === List(1)


        // Using tacit `index_` is not that useful since all attributes are indexed by default
        Schema.a.get.size === attrCount
        Schema.index_.a.get.size === attrCount

        Schema.index_(true).a.get.size === attrCount
        Schema.index_.not(false).a.get.size === attrCount


        // Get optional attribute indexing status with `index$`
        Schema.attr_("bool", "str").a.index$.get === List(
          (":Ns/str", Some(true)),
          (":Ns/bool", Some(true)),
        )

        // Filter by applying optional attribute indexing status
        val some = Some(true)
        Schema.attr_("bool", "str").a.index$(some).get === List(
          (":Ns/bool", Some(true)),
          (":Ns/str", Some(true)),
        )
        Schema.attr_("bool", "str").a.index$(Some(true)).get === List(
          (":Ns/bool", Some(true)),
          (":Ns/str", Some(true)),
        )

        val none = None
        Schema.attr_("bool", "str").a.index$(none).get === Nil
        Schema.attr_("bool", "str").a.index$(None).get === Nil
      }
    }

    "unique" in new SchemaSetup {

      // Unique options
      Schema.unique.get === List("identity", "value")

      // Unique options
      Schema.a.unique.get === List(
        (":Ref2/str2", "identity"),
        (":Ref2/int2", "value"),
      )

      // Count attribute indexing statuses
      Schema.unique(count).get === List(2)

      Schema.a.unique("identity").get === List((":Ref2/str2", "identity"))
      Schema.a.unique("value").get === List((":Ref2/int2", "value"))

      Schema.a.unique.not("identity").get === List((":Ref2/int2", "value"))
      Schema.a.unique.not("value").get === List((":Ref2/str2", "identity"))


      // Filter attributes by tacit `unique_` option
      Schema.unique_.a.get === List(":Ref2/int2", ":Ref2/str2")

      Schema.unique_("identity").a.get === List(":Ref2/str2")
      Schema.unique_.not("value").a.get === List(":Ref2/str2")


      // Get optional attribute indexing status with `index$`
      Schema.attr_("str", "str2", "int2").a.unique$.get.sorted === List(
        (":Ns/str", None),
        (":Ref2/int2", Some("value")),
        (":Ref2/str2", Some("identity")),
      )

      // Filter by applying optional attribute uniqueness status

      val some1 = Some("identity")
      Schema.attr_("str", "str2", "int2").a.unique$(some1).get === List(
        (":Ref2/str2", Some("identity"))
      )

      val some2 = Some("value")
      Schema.attr_("str", "str2", "int2").a.unique$(some2).get === List(
        (":Ref2/int2", Some("value"))
      )

      val none = None
      Schema.attr_("str", "str2", "int2").a.unique$(none).get === List(
        (":Ns/str", None)
      )

      // Number of non-unique attributes
      Schema.a.unique$(None).get.size === attrCount - 2
    }


    "fulltext" in new SchemaSetup {

      // Fulltext option only available in Peer
      if (system == DatomicPeer) {

        // Fulltext options
        Schema.fulltext.get === List(true) // no false

        // Count attribute fulltext statuses (only true)
        Schema.fulltext(count).get === List(1)

        // Attributes with fulltext search
        Schema.a.fulltext.get.sortBy(_._1) === List(
          (":Ns/str", true),
          (":Ns/strMap", true),
          (":Ns/strs", true),
          (":Ref1/str1", true),
          (":Ref2/str2", true)
        )

        Schema.a.fulltext(true).get.size === 5
        // Option is either true or non-asserted (nil/None), never false
        Schema.a.fulltext(false).get.size === 0

        Schema.a.fulltext.not(true).get.size === 0
        Schema.a.fulltext.not(false).get.size === 5


        // Filter attributes with tacit `fulltext_` option
        Schema.fulltext_.a.get.sorted === List(
          ":Ns/str",
          ":Ns/strMap",
          ":Ns/strs",
          ":Ref1/str1",
          ":Ref2/str2"
        )
        Schema.fulltext_(true).a.get.sorted === List(
          ":Ns/str",
          ":Ns/strMap",
          ":Ns/strs",
          ":Ref1/str1",
          ":Ref2/str2"
        )
        Schema.fulltext_.not(false).a.get.sorted === List(
          ":Ns/str",
          ":Ns/strMap",
          ":Ns/strs",
          ":Ref1/str1",
          ":Ref2/str2"
        )


        // Get optional attribute fulltext status with `fulltext$`
        Schema.attr_("bool", "str").a.fulltext$.get === List(
          (":Ns/str", Some(true)),
          (":Ns/bool", None),
        )

        // Filter by applying optional attribute fulltext search status
        val some = Some(true)
        Schema.attr_("bool", "str").a.fulltext$(some).get === List(
          (":Ns/str", Some(true)))
        Schema.attr_("bool", "str").a.fulltext$(Some(true)).get === List(
          (":Ns/str", Some(true)))

        val none = None
        Schema.attr_("bool", "str").a.fulltext$(none).get === List(
          (":Ns/bool", None))
        Schema.attr_("bool", "str").a.fulltext$(None).get === List(
          (":Ns/bool", None))

        // Number of attributes without fulltext search
        Schema.a.fulltext$(None).get.size === attrCount - 5
      }
    }


    "isComponent" in new SchemaSetup {

      // Component status options - either true or non-asserted
      Schema.isComponent.get === List(true) // no false
      Schema.isComponent(count).get === List(1)

      // Component attributes
      Schema.a.isComponent.get === List(
        (":Ns/refSub1", true),
        (":Ns/refsSub1", true),
        (":Ref1/refsSub2", true),
        (":Ref1/refSub2", true),
      )

      Schema.a.isComponent(true).get.size === 4
      // Option is either true or non-asserted (nil/None), never false
      Schema.a.isComponent(false).get.size === 0

      Schema.a.isComponent.not(true).get.size === 0
      Schema.a.isComponent.not(false).get.size === 4


      // Filter attributes with tacit `isComponent_` option
      Schema.isComponent_.a.get === List(
        ":Ns/refsSub1",
        ":Ref1/refSub2",
        ":Ref1/refsSub2",
        ":Ns/refSub1",
      )
      Schema.isComponent_(true).a.get === List(
        ":Ns/refsSub1",
        ":Ref1/refSub2",
        ":Ref1/refsSub2",
        ":Ns/refSub1",
      )
      Schema.isComponent_.not(false).a.get === List(
        ":Ns/refsSub1",
        ":Ref1/refSub2",
        ":Ref1/refsSub2",
        ":Ns/refSub1",
      )


      // Get optional attribute component status with `isComponent$`
      Schema.attr_("bool", "refSub1").a.isComponent$.get.sorted === List(
        (":Ns/bool", None),
        (":Ns/refSub1", Some(true)),
      )

      // Filter by applying optional attribute component status
      val some = Some(true)
      Schema.attr_("bool", "refSub1").a.isComponent$(some).get === List(
        (":Ns/refSub1", Some(true)))

      Schema.attr_("bool", "refSub1").a.isComponent$(Some(true)).get === List(
        (":Ns/refSub1", Some(true)))

      val none = None
      Schema.attr_("bool", "refSub1").a.isComponent$(none).get === List(
        (":Ns/bool", None))

      Schema.attr_("bool", "refSub1").a.isComponent$(None).get === List(
        (":Ns/bool", None))

      // Number of non-component attributes
      Schema.a.isComponent$(None).get.size === attrCount - 4
    }


    "noHistory" in new SchemaSetup {

      // No-history status options - either true or non-asserted
      Schema.noHistory.get === List(true) // no false
      Schema.noHistory(count).get === List(1)

      // No-history attributes
      Schema.a.noHistory.get === List(
        (":Ref2/ints2", true)
      )

      Schema.a.noHistory(true).get.size === 1
      // Option is either true or non-asserted (nil/None), never false
      Schema.a.noHistory(false).get.size === 0

      Schema.a.noHistory.not(true).get.size === 0
      Schema.a.noHistory.not(false).get.size === 1


      // Filter attributes with tacit `noHistory_` option
      Schema.noHistory_.a.get === List(":Ref2/ints2")
      Schema.noHistory_(true).a.get === List(":Ref2/ints2")
      Schema.noHistory_.not(false).a.get === List(":Ref2/ints2")


      // Get optional attribute no-history status with `noHistory$`
      Schema.attr_("bool", "ints2").a.noHistory$.get.sorted === List(
        (":Ns/bool", None),
        (":Ref2/ints2", Some(true)),
      )

      // Filter by applying optional attribute no-history status
      val some = Some(true)
      Schema.attr_("bool", "ints2").a.noHistory$(some).get === List(
        (":Ref2/ints2", Some(true)))

      Schema.attr_("bool", "ints2").a.noHistory$(Some(true)).get === List(
        (":Ref2/ints2", Some(true)))

      val none = None
      Schema.attr_("bool", "ints2").a.noHistory$(none).get === List(
        (":Ns/bool", None))

      Schema.attr_("bool", "ints2").a.noHistory$(None).get === List(
        (":Ns/bool", None))

      // Number of non-component attributes
      Schema.a.noHistory$(None).get.size === attrCount - 1
    }
  }


  "enum" in new SchemaSetup {

    // Attribute/enum values in namespace `ref2`
    Schema.ns_("Ref2").attr.enum.get.sorted === List(
      ("enum2", "enum20"),
      ("enum2", "enum21"),
      ("enum2", "enum22"),
    )

    // All enums grouped by ident
    Schema.a.enum.get.groupBy(_._1).map(g => g._1 -> g._2.map(_._2).sorted)
      .toList.sortBy(_._1) === List(
      ":Ns/enum" -> List("enum0", "enum1", "enum2", "enum3", "enum4",
        "enum5", "enum6", "enum7", "enum8", "enum9"),
      ":Ns/enums" -> List("enum0", "enum1", "enum2", "enum3", "enum4",
        "enum5", "enum6", "enum7", "enum8", "enum9"),
      ":Ref1/enum1" -> List("enum10", "enum11", "enum12"),
      ":Ref1/enums1" -> List("enum10", "enum11", "enum12"),
      ":Ref2/enum2" -> List("enum20", "enum21", "enum22"),
      ":Ref3/enum3" -> List("enum30", "enum31", "enum32"),
      ":Ref4/enum4" -> List("enum40", "enum41", "enum42"),
    )

    // Enums of a specific attribute
    Schema.a_(":Ns/enum").enum.get.sorted === List(
      "enum0", "enum1", "enum2", "enum3", "enum4",
      "enum5", "enum6", "enum7", "enum8", "enum9"
    )


    Schema.a.enum("enum0").get === List(
      (":Ns/enums", "enum0"),
      (":Ns/enum", "enum0")
    )

    Schema.a.enum("enum0", "enum1").get.sortBy(r => (r._1, r._2)) === List(
      (":Ns/enum", "enum0"),
      (":Ns/enum", "enum1"),
      (":Ns/enums", "enum0"),
      (":Ns/enums", "enum1")
    )

    // How many enums in total (duplicate enum values coalesce)
    Schema.enum(count).get === List(22)

    // Enums per namespace
    Schema.ns.enum(count).get === List(
      ("Ns", 10),
      ("Ref1", 3),
      ("Ref2", 3),
      ("Ref3", 3),
      ("Ref4", 3),
    )

    // Enums per namespace per attribute
    Schema.ns.attr.enum(count).get === List(
      ("Ns", "enum", 10),
      ("Ns", "enums", 10),
      ("Ref1", "enum1", 3),
      ("Ref1", "enums1", 3),
      ("Ref2", "enum2", 3),
      ("Ref3", "enum3", 3),
      ("Ref4", "enum4", 3),
    )


    // Attributes with some enum value
    Schema.a.enum_("enum0").get.sorted === List(
      ":Ns/enum", ":Ns/enums")
    Schema.a.enum_("enum0", "enum1").get.sorted === List(
      ":Ns/enum", ":Ns/enums")


    // Excluding one enum value will still match the other values
    Schema.a.enum_.not("enum0").get.sorted === List(
      ":Ns/enum",
      ":Ns/enums",
      ":Ref1/enum1",
      ":Ref1/enums1",
      ":Ref2/enum2",
      ":Ref3/enum3",
      ":Ref4/enum4",
    )

    // If we exclude all enum values of an attribute it won't be returned
    Schema.a.enum_.not("enum10", "enum11", "enum12").get.sorted === List(
      ":Ns/enum",
      ":Ns/enums",
      ":Ref2/enum2",
      ":Ref3/enum3",
      ":Ref4/enum4",
    )
  }


  "t, tx, txInstant" in new SchemaSetup {

    if (system == DatomicPeer) {
      // OBS time t only implemented for Peer (dev-local can't convert from tx to t)
      // Schema transaction time t
      Schema.t.get === List(1000)

      // Schema transaction entity id
      Schema.tx.get === List(13194139534312L)

      // Get tx wall clock time from Log for comparison with time from Schema query
      Schema.txInstant.get === List(Log().txInstant.get.head)

    } else {

      // Schema transaction entity id
      Schema.tx.get === List(13194139533318L)
      // Get tx wall clock time from Log for comparison with time from Schema query
      Schema.txInstant.get === List(Log().txInstant.get.head)
    }
  }
}