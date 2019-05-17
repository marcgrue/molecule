import sbt.compilerPlugin
lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamolecule",
  organizationName := "ScalaMolecule",
  organizationHomepage := Some(url("http://www.scalamolecule.org")),
  version := "0.18.5",
  scalaVersion := "2.12.8",
  scalacOptions := Seq(
    "-feature",
    "-language:implicitConversions",
    "-deprecation",
    //    "-Yrangepos",
    //    "-Ystatistics",
    //    "-Ymacro-debug-lite",
    //    "-Xprint",
    //    "-Ymacro-debug-verbose",
    //    "-Yshow-trees-stringified",
    //    "-Yshow-trees"
    //    "-Yquasiquote-debug"
    //    ,"-Ydebug"
  ),
  resolvers ++= Seq(
    "datomic" at "http://files.datomic.com/maven",
    "clojars" at "http://clojars.org/repo",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
  ),
  //  autoAPIMappings := true,
  apiURL := Some(url("https://example.org/api/")),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.datomic" % "datomic-free" % "0.9.5697",
    "org.specs2" %% "specs2-core" % "4.2.0" % "test",
    compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch)
  ),
  incOptions := incOptions.value.withLogRecompileOnMacro(false)
)


lazy val molecule = project.in(file("."))
  .settings(moduleName := "molecule-root")
  .settings(commonSettings ++ noPublishSettings)
  .aggregate(moleculeCore, moleculeCoretests, moleculeExamples)


lazy val moleculeCore = project.in(file("core"))
  .settings(moduleName := "molecule")
  .settings(commonSettings ++ publishSettings)
  .settings(scalacOptions in Compile in doc ++= Seq(
    "-diagrams",
    "-groups",
    "-doc-version", version.value,
    "-doc-title", "Molecule",
    "-sourcepath", (baseDirectory in ThisBuild).value.toString,
    "-doc-source-url", s"https://github.com/scalamolecule/molecule/tree/master€{FILE_PATH}.scala#L1"
  ))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "moleculeBuildInfo"
  )

lazy val moleculeCoretests = project.in(file("coretests"))
  .dependsOn(moleculeCore)
  .settings(commonSettings ++ noPublishSettings)
  .enablePlugins(MoleculePlugin)
  .settings(
    moduleName := "molecule-coretests",
    moleculeSchemas := Seq(
      "molecule/coretests/bidirectionals",
      "molecule/coretests/nested",
      "molecule/coretests/schemaDef",
      "molecule/coretests/util"
    )
  )

lazy val moleculeExamples = project.in(file("examples"))
  .dependsOn(moleculeCore)
  .settings(commonSettings ++ noPublishSettings)
  .settings(Seq(autoAPIMappings := true))
  .enablePlugins(MoleculePlugin)
  .settings(
    moduleName := "molecule-examples",
    moleculeSchemas := Seq(
      "molecule/examples/dayOfDatomic",
      "molecule/examples/gremlin",
      "molecule/examples/mbrainz",
      "molecule/examples/seattle"
    )
  )


lazy val snapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
lazy val releases = "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

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

lazy val noPublishSettings = Seq(
  skip in publish := true,
  publish := ((): Unit),
  publishLocal := ((): Unit),
  publishArtifact in(Compile, packageDoc) := false,
  publishArtifact in packageDoc := false,
  sources in(Compile, doc) := Seq.empty
)