package molecule.coretests.meta.schema
import molecule.schema.definition._

@InOut(0, 22)
object SchemaDefinition {

  trait Schema {

    val id     = oneLong.doc("Attribute entity id")
    val ident  = oneString.doc("Attribute ident ':part_Ns/attr' or :Ns/attr (clojure KeyWord as String)")
    val part   = oneString.doc("Partition name 'part'")
    val nsFull = oneString.doc("Namespace name with partition prefix 'part_ns' or 'ns' if no partitions defined")
    val ns     = oneString.doc("Namespace name 'ns'")
    val a      = oneString.doc("Attribute name 'attr'")

    // Required
    val tpe  = oneString.doc("Datomic value type: string, long, double, boolean, bigint, bigdec, instant, uuid, uri, bytes, ref")
    val card = oneString.doc("Cardinality: one, many")


    // Optional
    val doc         = oneString.fulltext.doc("Attribute description")
    val index       = oneBoolean.doc("Are attribute values indexed?")
    val unique      = oneString.doc("Attribute value unique constraints: identity, value")
    val fulltext    = oneBoolean.doc("Does attribute allow fulltext search?")
    val isComponent = oneBoolean.doc("Is attribute a component?")
    val noHistory   = oneBoolean.doc("Is attribute prohibiting history?")
    val enum        = oneString.doc("Enum value(s) of attribute")

    // Meta information about schema transactions
    val t         = oneLong.doc("Schema transaction time t of when Attribute was created")
    val tx        = oneLong.doc("Schema transaction entity id of when Attribute was created")
    val txInstant = oneDate.doc("Schema transaction Date of when Attribute was created")
  }

}
