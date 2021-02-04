import sbt._
import sbt.Keys._
import sbtmolecule.MoleculePlugin.autoImport._

trait SettingsMolecule {


  // Paths to folders where `dataModel/<..>DataModel` files reside for your domain.

  lazy val moleculeTests: Seq[Def.Setting[_]] = Seq(
    moduleName := "molecule-tests",

    // Generate Molecule boilerplate code with `sbt clean compile -Dmolecule=true`
    moleculePluginActive := sys.props.get("molecule") == Some("true"),
//    moleculeMakeJars := true,
    moleculeMakeJars := false,
    moleculeGenericPkg := "molecule.core.generic",
    moleculeDataModelPaths := Seq(
//      "molecule/tests/core/base",
//      "molecule/tests/core/bidirectionals",
//      "molecule/tests/core/ref",
//      "molecule/tests/core/schemaDef",

//      "molecule/tests/examples/datomic/dayOfDatomic",
//      "molecule/tests/examples/datomic/mbrainz",
//      "molecule/tests/examples/datomic/seattle",
//      "molecule/tests/examples/gremlin/gettingStarted",

      // To generate these generic models, un-comment line 82-84 in sbt-plugin.DatModelParser
      // and publish snapshot first.
      "molecule/tests/core/generic"
    )
  )
}
