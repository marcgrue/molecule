lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamolecule",
  version := "0.10.3",
  scalaVersion := "2.12.1",
  scalacOptions := Seq("-feature", "-language:implicitConversions", "-Yrangepos"),
  resolvers ++= Seq(
    "datomic" at "http://files.datomic.com/maven",
    "clojars" at "http://clojars.org/repo",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.datomic" % "datomic-free" % "0.9.5561",
    "org.specs2" %% "specs2" % "2.4.17"
  ),
  // Remove Java directories
  excludeFilter in unmanagedSources := HiddenFileFilter || "*.java"
)


lazy val molecule = project.in(file("."))
  .settings(moduleName := "molecule-root")
  .settings(commonSettings ++ noPublishSettings)
  .aggregate(moleculeCore, moleculeCoretest, moleculeExamples)


lazy val moleculeCore = project.in(file("core"))
  .settings(moduleName := "molecule")
  .settings(commonSettings ++ publishSettings)


lazy val moleculeCoretest = project.in(file("coretest"))
  .dependsOn(moleculeCore)
  .enablePlugins(MoleculePlugin)
  .settings(commonSettings ++ noPublishSettings)
  .settings(
    moduleName := "molecule-coretest",
    moleculeSchemas := Seq(
      "molecule/partition",
      "molecule/bidirectional",
      "molecule/util"
    )
  )
// Manually add schema definition directories for boilerplate generation testing
//  .settings(Seq(definitionDirsSeparate(
//  "molecule/util",
//  "molecule/partition",
//  "molecule/bidirectional"
//)))

lazy val moleculeExamples = project.in(file("examples"))
  .dependsOn(moleculeCore)
  .enablePlugins(MoleculePlugin)
  .settings(commonSettings ++ noPublishSettings)
  .settings(
    moduleName := "molecule-examples",
    moleculeSchemas := Seq(
      "molecule/examples/dayOfDatomic",
      "molecule/examples/gremlin",
      "molecule/examples/mbrainz",
      "molecule/examples/seattle"
    )
  )
// Manually add schema definition directories for boilerplate generation testing
//  .settings(Seq(definitionDirs(
//  "molecule/examples/dayOfDatomic",
//  "molecule/examples/gremlin",
//  "molecule/examples/mbrainz",
//  "molecule/examples/seattle"
//)))


//def definitionDirsSeparate(domainDirs: String*) = definitionDirs0(true, domainDirs: _*)
//def definitionDirs(domainDirs: String*) = definitionDirs0(false, domainDirs: _*)
//def definitionDirs0(separateInFiles: Boolean, domainDirs: String*) = sourceGenerators in Compile += Def.task[Seq[File]] {
//  val codeDir = (scalaSource in Compile).value
//  val sourceDir = (sourceManaged in Compile).value
//
//  // generate source files
//  val sourceFiles = MoleculeBoilerplate(codeDir, sourceDir, domainDirs.toSeq, separateInFiles)
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
  pomIncludeRepository := (_ => false),
  homepage := Some(url("http://scalamolecule.org")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://github.com/scalamolecule/molecule"), "scm:git:git@github.com:scalamolecule/molecule.git")),
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
  publish :=(),
  publishLocal :=(),
  publishArtifact := false
)