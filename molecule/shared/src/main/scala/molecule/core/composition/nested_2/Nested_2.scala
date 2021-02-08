package molecule.core.composition.nested_2

import scala.language.higherKinds


/** Add nested molecule to input molecule awaiting 2 inputs.
  * {{{
  *   m(Order.no(?).total.>(?) * LineItem.product.price.quantity)
  *     .apply(23, 120).get === List(
  *       (23, 124, List(("Chocolate", 48.00, 1), ("Whisky", 38.00, 2)))
  *     )
  * }}}
  */
trait Nested_2
