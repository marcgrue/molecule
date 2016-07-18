package molecule
package attrMap
import molecule.util.CoreSetup
import molecule.util.dsl.coreTest._

class Manipulation extends Base {


  "Manipulate" in new CoreSetup {

    // Insert

    val eid: Long = Ns.strMap.insert(Map("en" -> "Hi")).eid
    Ns.strMap.one === Map("en" -> "Hi")


    // Update + Add

    // When a previous populated key is encountered the old fact is
    // retracted and the new one asserted (like an update).
    Ns(eid).strMap.add("en" -> "Hi there", "fr" -> "Bonjour").update
    Ns.strMap.one === Map("en" -> "Hi there", "fr" -> "Bonjour")


    // Remove pair (by key)

    Ns(eid).strMap.remove("en").update
    Ns.strMap.one === Map("fr" -> "Bonjour")


    // Applying nothing (empty parenthesises)
    // finds and retract all values of an attribute

    Ns(eid).strMap().update
    Ns.strMap.get === List()
  }

}