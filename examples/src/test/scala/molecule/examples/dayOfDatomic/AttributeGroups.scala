package molecule.examples.dayOfDatomic
import molecule.core.util.MoleculeSpec
import molecule.datomic.api.out2._
import molecule.datomic.peer.facade.Datomic_Peer._

class AttributeGroups extends MoleculeSpec {

  "Attribute groups" in new SocialNewsSetup {

    // Find all attributes in the story namespace
    // todo
//    Schema.ns_("story").a.get === List(":Story/title", ":Story/url")
//    Schema.a.ns_("story").get === List(":Story/title", ":Story/url")

//    // Create a reusable rule
//    val attrInNs = m(Schema.a.ns_(?))
//
//    // Find all attributes in story namespace, using the rule
//    attrInNs("story").get === List(":Story/title", ":Story/url")

    // Find all entities possessing *any* story attribute (the 3 stories)
//    Schema.e.ns_("Story").get.sorted === List(s1, s2, s3)

    ok
  }
}