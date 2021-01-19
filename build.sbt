import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}


lazy val molecule = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .settings(
    Settings.base ++
      Publish.withoutDocs // save time without doc creation for publishLocal
    //      Publish.withDocs // make docs for publishSigned
  )
  .jsSettings(Settings.js)
  .jsConfigure(_.enablePlugins(TzdbPlugin))
  .jvmSettings(Settings.jvm)

lazy val js  = molecule.js
lazy val jvm = molecule.jvm

lazy val tests = project.in(file("molecule-tests"))
  .enablePlugins(BuildInfoPlugin, MoleculePlugin)
  .dependsOn(jvm)
  .settings(
    Settings.base ++
      Settings.jvm ++
      Settings.moleculeTests ++
      Settings.tests ++
      Publish.not
  )
