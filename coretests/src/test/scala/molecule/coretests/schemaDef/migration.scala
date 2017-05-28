//package molecule.coretests.schemaDef
//
//import molecule._
//import molecule.ast.model.{Atom, Bond, Eq, Model}
//import molecule.coretests.schemaDef.dsl.partitionTest._
//import molecule.coretests.schemaDef.schema.PartitionTestSchema
//import molecule.util.MoleculeSpec
//import org.specs2.specification.Scope
//
//import scala.language.reflectiveCalls
//
//class MigrationSetup1 extends Scope {
//  implicit val conn = recreateDbFrom(PartitionTestSchema)
//}
//
//class Migration extends MoleculeSpec {
//
//
//  "Nested 2 levels" in new MigrationSetup1 {
//    m(lit_Book.title.Reviewers.name.Professions * gen_Profession.name) insert List(("book", "Jan", List("Musician")))
//
//    m(lit_Book.title.Reviewers.name.Professions * gen_Profession.name).get === List(("book", "Jan", List("Musician")))
//    m(lit_Book.title.Reviewers.Professions * gen_Profession.name).get === List(("book", List("Musician")))
//  }
//}