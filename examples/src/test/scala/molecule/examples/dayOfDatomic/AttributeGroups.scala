package molecule.examples.dayOfDatomic
import molecule.imports._
import molecule.schema.Db
import molecule.util.MoleculeSpec


class AttributeGroups extends MoleculeSpec {

  "Attribute groups" in new SocialNewsSetup {

    // Find all attributes in the story namespace
    Db.a.ns_("story").get === List(":story/title", ":story/url")

    // Create a reusable rule
    val attrInNs = m(Db.a.ns_(?))

    // Find all attributes in story namespace, using the rule
    attrInNs("story").get === List(":story/url", ":story/title")

    // Find all entities possessing *any* story attribute (the 3 stories)
    Db.e.ns_("story").get.toSeq.sorted === List(s1, s2, s3)
  }
}