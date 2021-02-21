package molecule.core.composition

import molecule.core.dsl.base.Init
import scala.language.higherKinds

/** Transaction meta data on molecule.
  * <br><br>
  * `Tx` takes a transaction meta data molecule with attributes having the transaction id as their entity id.
  * {{{
  *   // Save molecule with transaction data
  *   Person.name("Ben").Tx(MyMetaData.action("add member")).save.eid
  *
  *   // Query for data with transaction meta data - "which persons became members"
  *   Person.name.Tx(MyMetaData.action_("add member")).get === List("Ben")
  * }}}
  */
trait Tx

trait Tx_[props] {
  def Tx: props = ???
}