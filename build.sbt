import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}


lazy val base = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("molecule-base"))
  .settings(Settings.base ++
    //    Publish.withoutDocs // save time without doc creation for publishLocal
    Publish.withDocs // make docs for publishSigned
  )
  .jsSettings(Settings.js)
  .jvmSettings(Settings.jvm)

lazy val baseJS  = base.js
lazy val baseJVM = base.jvm


lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("molecule-core"))
  .dependsOn(base)
  .settings(Settings.base ++
    //    Publish.withoutDocs // save time without doc creation for publishLocal
    Publish.withDocs // make docs for publishSigned
  )
  .jsSettings(Settings.js)
  //  .jsConfigure(_.enablePlugins(TzdbPlugin))
  .jvmSettings(Settings.jvm)

lazy val coreJS  = core.js
lazy val coreJVM = core.jvm


lazy val tests = project.in(file("molecule-tests"))
  .enablePlugins(BuildInfoPlugin, MoleculePlugin)
  .dependsOn(coreJVM, baseJVM)
  .settings(Settings.base ++ Settings.jvm ++ Settings.moleculeTests ++ Settings.tests ++ Publish.not)
