import sbt._
import sbt.Keys._
import sbtmolecule.MoleculePlugin.autoImport._

trait SettingsMolecule {


  // Paths to folders where `schema/<SchemaDefinitionFiles>` reside for your
  // molecule databases

  lazy val moleculeTests: Seq[Def.Setting[_]] = Seq(
    moduleName := "molecule-tests",
    moleculeMakeJars := true,
    moleculeDataModelPaths := Seq(
      "molecule/tests/core/base",
      "molecule/tests/core/bidirectionals",
      "molecule/tests/core/ref",
      "molecule/tests/core/schemaDef",

      "molecule/tests/examples/datomic/dayOfDatomic",
      "molecule/tests/examples/datomic/mbrainz",
      "molecule/tests/examples/datomic/seattle",
      "molecule/tests/examples/gremlin/gettingStarted"
    )
  )
}
