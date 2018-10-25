
lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamolecule",
  version := "0.15.0",
  scalaVersion := "2.12.7",
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
    Resolver.sonatypeRepo("snapshots")
  ),
//  autoAPIMappings := true,
  apiURL := Some(url("https://example.org/api/")),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.datomic" % "datomic-free" % "0.9.5697",
    "org.specs2" %% "specs2-core" % "4.2.0" % "test"
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
//  .settings(Seq(definitionDirs(false,
//  "molecule/coretests/bidirectionals",
//  "molecule/coretests/nested",
//  "molecule/coretests/schemaDef",
//  "molecule/coretests/util"
//)))

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
//  .settings(Seq(definitionDirs(true,
//  "molecule/examples/dayOfDatomic",
//  "molecule/examples/gremlin",
//  "molecule/examples/mbrainz",
//  "molecule/examples/seattle"
//)))

//def definitionDirs(docs: Boolean, domainDirs: String*): Def.Setting[Seq[Task[Seq[File]]]] = sourceGenerators in Compile += Def.task[Seq[File]] {
//  val codeDir = (scalaSource in Compile).value
//  val sourceDir = (sourceManaged in Compile).value
//
//  // generate source files
//  val sourceFiles = FileBuilder(codeDir, sourceDir, domainDirs.toSeq, docs)
//
//  // Avoid re-generating boilerplate if nothing has changed when running `sbt compile`
//  val cache = FileFunction.cached(
//    streams.value.cacheDirectory / "moleculeBoilerplateTesting",
//    inStyle = FilesInfo.lastModified,
//    outStyle = FilesInfo.hash
//  ) {
//    in: Set[File] => sourceFiles.toSet
//  }
//  cache(sourceFiles.toSet).toSeq
//}.taskValue


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
  credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
  pomExtra :=
    <developers>
      <developer>
        <id>marcgrue</id>
        <name>Marc Grue</name>
        <url>http://marcgrue.com</url>
      </developer>
    </developers>
)

lazy val noPublishSettings = Seq(
  publish := ((): Unit),
  publishLocal := ((): Unit),
  publishArtifact in(Compile, packageDoc) := false,
  publishArtifact in packageDoc := false,
  sources in(Compile, doc) := Seq.empty
)