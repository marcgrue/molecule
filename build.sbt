import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

lazy val molecule = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    Settings.base ++ Settings.shared ++
      //      Publish.withoutDocs // save time without doc creation for publishLocal
      Publish.withDocs // make docs for publishSigned
  )
  .jsSettings(Settings.js)
  .jvmSettings(Settings.jvm)

lazy val moleculeJS  = molecule.js.enablePlugins(TzdbPlugin)
lazy val moleculeJVM = molecule.jvm


lazy val moleculeCoretests = project.in(file("coretests"))
  .dependsOn(moleculeJVM)
  .settings(Settings.base ++ Settings.jvm ++ Publish.not)
// Un-comment to re-create molecule lib jars if schemas change
//  .enablePlugins(MoleculePlugin).settings(Settings.moleculeCoretests)


lazy val moleculeExamples = project.in(file("examples"))
  .dependsOn(moleculeJVM)
  .settings(Settings.base ++ Settings.jvm ++ Publish.not)
// Un-comment to re-create molecule lib jars if schemas change
//  .enablePlugins(MoleculePlugin).settings(Settings.moleculeExamples)
