package molecule.coretests.generic.schema
import molecule.schema.definition._

@InOut(0, 16)
object SchemaDefinition {

  trait Schema {

    val id    = oneLong.doc("Attribute entity id")
    val ident = oneString.doc("Attribute ident ':ns/attr' (clojure KeyWord as String)")
    val ns    = oneString.doc("Namespace name 'ns'")
    val a     = oneString.doc("Attribute name 'attr'")

    // Required
    val tpe  = oneString.doc("Datomic value type: string, long, double, boolean, bigint, bigdec, instant, uuid, uri, bytes, ref")
    val card = oneString.doc("Cardinality: one, many")


    // Optional
    val doc            = oneString.fulltextSearch.doc("Attribute description")
    val indexed        = oneBoolean.doc("Are attribute values indexed?")
    val unique         = oneString.doc("Attribute value unique constraints: identity, value")
    val fulltextSearch = oneBoolean.doc("Does attribute allow fulltext search?")
    val isComponent    = oneBoolean.doc("Is attribute a component?")
    val noHistory      = oneBoolean.doc("Is attribute prohibiting history?")
    val enums          = manyString.doc("Enum values of attribute")

    // Meta information about schema transactions
    val t         = oneLong.doc("Schema transaction time t of when Attribute was created")
    val tx        = oneLong.doc("Schema transaction entity id of when Attribute was created")
    val txInstant = oneDate.doc("Schema transaction Date of when Attribute was created")
  }

}
