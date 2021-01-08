package molecule.core.composition.nested

/** Add nested molecule.
  * <br><br>
  * Related data of cardinality-many referenced entities can be queried in a "flat" way:
  * {{{
  *   m(Order.no.LineItem.product.price.quantity).get === List(
  *     (23, "Chocolate", 48.00, 1),
  *     (23, "Whisky", 38.00, 2)
  *   )
  * }}}
  * For convenience, Molecule offers to automatically nest the same data so that
  * redundancy is avoided and we can work straight on the hierarchical data:
  * {{{
  *   m(Order.no * LineItem.product.price.quantity).get === List(
  *     (23, List(("Chocolate", 48.00, 1), ("Whisky", 38.00, 2)))
  *   )
  * }}}
  * Nested molecules can nest up to 7 levels deep.
  * <br><br>
  * Internally, Molecule adds entity ids to each level in the query to be able to group data on each level by a unique entity id.
  */
trait Nested
