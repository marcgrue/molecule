package molecule.core.composition.nested_3

import scala.language.higherKinds


/** Add nested molecule to input molecule awaiting 3 inputs.
  * {{{
  *   m(Order.no(?).total.>(?).att(?) * LineItem.product.price.quantity)
  *     .apply(23, 120, "Ben Smith").get === List(
  *       (23, 124, "Ben Smith", List(("Chocolate", 48.00, 1), ("Whisky", 38.00, 2)))
  *     )
  * }}}
  */
trait Nested_3
