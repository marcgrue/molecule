package molecule.core.composition.nested_1

import scala.language.higherKinds


/** Add nested molecule to input molecule awaiting 1 input.
  * {{{
  *   m(Order.no(?) * LineItem.product.price.quantity)
  *     .apply(23).get === List(
  *       (23, List(("Chocolate", 48.00, 1), ("Whisky", 38.00, 2)))
  *     )
  * }}}
  */
trait Nested_1
