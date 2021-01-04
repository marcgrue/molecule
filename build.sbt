import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

lazy val molecule = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    Settings.base ++ Settings.shared ++
      Publish.withoutDocs // save time without doc creation for publishLocal
//          Publish.withDocs // make docs for publishSigned
  )
  .jsSettings(Settings.js)
  .jvmSettings(Settings.jvm)

lazy val js  = molecule.js.enablePlugins(TzdbPlugin)
lazy val jvm = molecule.jvm


lazy val tests = project.in(file("molecule-tests"))
  .dependsOn(jvm)
  .settings(Settings.base ++ Settings.jvm ++ Settings.tests ++ Publish.not)
// Un-comment to re-create molecule lib jars if schemas change
//  .enablePlugins(MoleculePlugin).settings(Settings.moleculeTests)
