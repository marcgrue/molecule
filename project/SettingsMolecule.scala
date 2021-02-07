import sbt._
import sbt.Keys._
import sbtmolecule.MoleculePlugin.autoImport._

trait SettingsMolecule {


  // Paths to folders where `dataModel/<..>DataModel` files reside for your domain.

  lazy val moleculeTests: Seq[Def.Setting[_]] = Seq(
    moduleName := "molecule-tests",

    // Generate Molecule boilerplate code with `sbt clean compile -Dmolecule=true`
    moleculePluginActive := sys.props.get("molecule") == Some("true"),
    moleculeDataModelPaths := Seq(
      "molecule/tests/core/base",
      "molecule/tests/core/bidirectionals",
      "molecule/tests/core/ref",
      "molecule/tests/core/schemaDef",

      "molecule/tests/examples/datomic/dayOfDatomic",
      "molecule/tests/examples/datomic/mbrainz",
      "molecule/tests/examples/datomic/seattle",
      "molecule/tests/examples/gremlin/gettingStarted",

      //      "molecule/tests/core/generic"
    ),
        moleculeMakeJars := false,
    //    moleculeGenericPkg := "molecule.core.generic",

    // Let IDE detect created jars in unmanaged lib directory
    exportJars := true
  )

  /*
To re-generate generic dsl code:

1. In SettingsMolecule, set:

    moleculeMakeJars := false,
    moleculeGenericPkg := "molecule.core.generic",
    moleculeDataModelPaths := Seq(
      // ...uncomment others to save compile time
      "molecule/tests/core/generic"
    )

2. `sbt clean compile -Dmolecule=true`
3. Copy 7 folders in src_managed.main.molecule.tests.core.generic.dsl to shared.src.main.scala.molecule.core.generic
   */
}
