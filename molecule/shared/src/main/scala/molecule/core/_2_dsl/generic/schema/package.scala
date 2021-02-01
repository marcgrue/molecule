package molecule.core._2_dsl.generic


/** Generic Schema attribute interfaces of all arities.
  * <br><br>
  * The generic Schema interface provides attributes to build molecules that
  * query the Schema structure of the current database.
  * {{{
  *   // List of attribute entity ids
  *   val attrIds: Seq[Long] = Schema.id.get
  *
  *   // Attribute name elements
  *   Schema.a.part.ns.nsFull.attr.get === List (
  *     (":sales_Customer/name", "sales", "Customer", "sales_Customer", "name"),
  *     (":sales_Customer/name", "sales", "Customer", "sales_Customer", "name"),
  *     // etc..
  *   )
  *
  *   // Datomic type and cardinality of attributes
  *   Schema.a.tpe.card.get === List (
  *     (":sales_Customer/name", "string", "one"),
  *     (":accounting_Invoice/invoiceLine", "ref", "many"),
  *   )
  *
  *   // Optional docs and attribute options
  *   // These can be retrieved as mandatory or optional values
  *   Schema.a
  *         .index
  *         .doc$
  *         .unique$
  *         .fulltext$
  *         .isComponent$
  *         .noHistory$
  *         .get === List(
  *     (":sales_Customer/name",
  *       true,            // indexed
  *       "Customer name", // doc
  *       None,            // Uniqueness not set
  *       Some(true),      // Fulltext search set so that we can search for names
  *       None,            // Not a component
  *       None             // History is preserved (noHistory not set)
  *       ),
  *     (":accounting_Invoice/invoiceLine",
  *       true,                   // indexed
  *       "Ref to Invoice lines", // doc
  *       None,                   // Uniqueness not set
  *       None,                   // Fulltext search not set
  *       Some(true),             // Invoice is a component - owns invoice lines
  *       None                    // History is preserved (noHistory not set)
  *       ),
  *   )
  *
  *   // Defined enum values
  *   Schema.a.enum.get.groupBy(_._1).map(g => g._1 -> g._2) === Map(
  *     ":Person/gender" -> List("female", "male"),
  *     ":Interests/sports" -> List("golf", "basket", "badminton")
  *   )
  *
  *   // Schema transaction times
  *   Schema.t.tx.txInstant.get === List(
  *     (t1, tx1, <Date: 2018-11-07 09:28:10>), // Initial schema transaction
  *     (t2, tx2, <Date: 2019-01-12 12:43:27>), // Additional schema attribute definitions...
  *   )
  * }}}
  *
  * Apply expressions to narrow the returned selection of Schema data:
  * {{{
  *   // Namespaces in the "gen" partition (partition name tacit)
  *   Schema.part_("location").ns.get === List("Country", "Region", etc...)
  *
  *   // Attributes in the "Person" namespace
  *   Schema.ns_("Person").attr.get === List("name", "age", "hobbies", etc...)
  *
  *   // How many enum attributes?
  *   Schema.enum_.a(count).get === List(2)
  * }}}
  *
  * @note Schema attributes defined in Datomic's bootstrap process that are not related
  *       to the current database are transparently filtered out from all Schema queries.
  */
package object schema
