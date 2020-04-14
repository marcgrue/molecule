
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

lazy val supportedScalaVersions = List("2.13.1", "2.12.10")
lazy val baseFlags              = List(
  "-feature",
  "-language:implicitConversions",
  "-deprecation"
)

lazy val baseSettings = Defaults.coreDefaultSettings ++ Seq(
  version := "0.22.2-SNAPSHOT",
  organization := "org.scalamolecule",
  organizationName := "ScalaMolecule",
  organizationHomepage := Some(url("http://www.scalamolecule.org"))
)

lazy val jvmSettings = baseSettings ++ Seq(
  scalacOptions := (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 13)) => baseFlags :+ "-Ymacro-annotations"
    case _             => baseFlags
  }),
  resolvers ++= Seq(
    ("datomic" at "http://files.datomic.com/maven").withAllowInsecureProtocol(true),
    ("clojars" at "http://clojars.org/repo").withAllowInsecureProtocol(true),
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.datomic" % "datomic-free" % "0.9.5697",
    "org.specs2" %% "specs2-core" % "4.7.1" % "test"

  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 13)) => Nil
    case _             => sbt.compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full) :: Nil
  }),
  unmanagedSourceDirectories in Compile ++= {
    (unmanagedSourceDirectories in Compile).value.map { dir =>
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => file(dir.getPath ++ "-2.13+")
        case _             => file(dir.getPath ++ "-2.13-")
      }
    }
  },
  incOptions := incOptions.value.withLogRecompileOnMacro(false)
)


lazy val molecule = project.in(file("."))
  .settings(noPublishSettings ++ Seq(
    moduleName := "molecule-root",
    crossScalaVersions := Nil
  ))
  .aggregate(moleculeCoreJVM, moleculeCoretests, moleculeExamples)


lazy val moleculeCore = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(baseSettings ++
    //    publishSettingsWithoutDoc ++ // save time without doc creation for publishLocal
    publishSettings ++ // make docs for publishSigned
    Seq(
      moduleName := "molecule",
      crossScalaVersions := supportedScalaVersions,
      scalacOptions in Compile in doc ++= Seq(
        "-diagrams",
        "-groups",
        "-doc-version", version.value,
        "-doc-title", "Molecule",
        "-sourcepath", (baseDirectory in ThisBuild).value.toString,
        "-doc-source-url", s"https://github.com/scalamolecule/molecule/tree/master€{FILE_PATH}.scala#L1"
      ),
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
      buildInfoPackage := "moleculeBuildInfo",
    )
  )
  .enablePlugins(BuildInfoPlugin)
  .jvmSettings(jvmSettings)

  /*
    Publishing instructions:

    Delete previous ivy cached build files before publishing locally
    > del ~/.ivy2/local/org.scalamolecule/molecule*

    `.enablePlugins(ScalaJSPlugin)` un-commented (active):
    > sbt +publishLocal // produces sjs build files (for %%% refs) 2.12/2.13
    > sbt publishLocal  // produces sjs build files (for %%% refs) 2.12

    `.enablePlugins(ScalaJSPlugin)` commented (inactive):
    > sbt +publishLocal // produces scala build files (for %% refs) 2.12/2.13
    > sbt publishLocal  // produces scala build files (for %% refs) 2.12

    > sbt [+]publishSigned for publishing to nexus/maven
   */
  .enablePlugins(ScalaJSPlugin)


lazy val moleculeCoreJVM = moleculeCore.jvm

lazy val moleculeCoreJS = moleculeCore.js


lazy val moleculeCoretests = project.in(file("coretests"))
  .dependsOn(moleculeCoreJVM)
  .settings(jvmSettings ++ noPublishSettings ++ Seq(
    crossScalaVersions := supportedScalaVersions
  ))
// Un-comment to re-create molecule lib jars if schemas change
//  .enablePlugins(MoleculePlugin)
//  .settings(
//    moduleName := "molecule-coretests",
//    moleculeMakeJars := true,
//    moleculeSchemas := Seq(
//      "molecule/coretests/bidirectionals",
//      "molecule/coretests/nested",
//      "molecule/coretests/schemaDef",
//      "molecule/coretests/util"
//    )
//  )


lazy val moleculeExamples = project.in(file("examples"))
  .dependsOn(moleculeCoreJVM)
  .settings(jvmSettings ++ noPublishSettings ++ Seq(
    crossScalaVersions := supportedScalaVersions,
    autoAPIMappings := true
  ))
// Un-comment to re-create molecule lib jars if schemas change
//  .enablePlugins(MoleculePlugin)
//  .settings(
//    moduleName := "molecule-examples",
//    moleculeMakeJars := true,
//    moleculeSchemas := Seq(
//      "molecule/examples/dayOfDatomic",
//      "molecule/examples/gremlin",
//      "molecule/examples/mbrainz",
//      "molecule/examples/seattle"
//    )
//  )


lazy val snapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
lazy val releases  = "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := (if (isSnapshot.value) Some(snapshots) else Some(releases)),
  publishArtifact in Test := false,
  scalacOptions in(Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value + "/src/main/scaladoc/rootdoc.txt"),
  pomIncludeRepository := (_ => false),
  homepage := Some(url("http://scalamolecule.org")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://github.com/scalamolecule/molecule"), "scm:git:git@github.com:scalamolecule/molecule.git")),
  developers := List(
    Developer(
      id = "marcgrue",
      name = "Marc Grue",
      email = "marcgrue@gmail.com",
      url = url("http://marcgrue.com")
    )
  )
)

lazy val publishSettingsWithoutDoc = Seq(
  publishMavenStyle := true,
  publishTo := (if (isSnapshot.value) Some(snapshots) else Some(releases)),
  publishArtifact in Test := false,
  sources in doc := Seq.empty,
  publishArtifact in packageDoc := false,
  pomIncludeRepository := (_ => false),
  homepage := Some(url("http://scalamolecule.org")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://github.com/scalamolecule/molecule"), "scm:git:git@github.com:scalamolecule/molecule.git")),
  developers := List(
    Developer(
      id = "marcgrue",
      name = "Marc Grue",
      email = "marcgrue@gmail.com",
      url = url("http://marcgrue.com")
    )
  )
)

lazy val noPublishSettings = Seq(
  skip in publish := true,
  publish := ((): Unit),
  publishLocal := ((): Unit),
  publishArtifact in(Compile, packageDoc) := false,
  sources in(Compile, doc) := Seq.empty
)