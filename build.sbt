lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamolecule",
  version := "0.6.3",
  scalaVersion := "2.11.8",
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
    "com.datomic" % "datomic-pro" % "0.9.5359",
    "org.specs2" %% "specs2" % "2.4.11"
  ),
  // Remove Java directories
  unmanagedSourceDirectories in Compile <<= (scalaSource in Compile) (Seq(_)),
  unmanagedSourceDirectories in Test <<= (scalaSource in Test) (Seq(_))
)

lazy val defDirsCore = Seq(
  "molecule/part",
  "molecule/util"
)
lazy val defDirsExamples = Seq(
  "molecule/examples/dayOfDatomic",
  "molecule/examples/seattle",
  "molecule/examples/mbrainz",
  "molecule/examples/graph"
)


lazy val molecule = project.in(file("."))
  .settings(moduleName := "molecule-root")
  .aggregate(moleculeCore, moleculeCoretest, moleculeExamples)
  .settings(commonSettings ++ noPublishSettings)


lazy val moleculeCore = project.in(file("core"))
  .settings(moduleName := "molecule")
  .settings(commonSettings ++ publishSettings)


lazy val moleculeCoretest = project.in(file("coretest"))
  .settings(moduleName := "molecule-coretest")
  .dependsOn(moleculeCore)
  .settings(commonSettings ++ noPublishSettings)
  // Generate boilerplate
  .settings(definitionDirectories(defDirsCore))
  .settings(taskKey[Unit]("Create jar") <<= makeJar(defDirsCore))


lazy val moleculeExamples = project.in(file("examples"))
  .settings(moduleName := "molecule-examples")
  .dependsOn(moleculeCore)
  .settings(commonSettings ++ noPublishSettings)
  // Generate boilerplate
  .settings(definitionDirectories(defDirsExamples))
  .settings(taskKey[Unit]("Create jar") <<= makeJar(defDirsExamples))



def definitionDirectories(defDirs: Seq[String]) = sourceGenerators in Compile += Def.task[Seq[File]] {
  val codeDir = (scalaSource in Compile).value
  val managedDir = (sourceManaged in Compile).value
  val srcFiles = MoleculeBoilerplate.generate(codeDir, managedDir, defDirs)
  val cache = FileFunction.cached(
    streams.value.cacheDirectory / "moleculeBoilerplate",
    inStyle = FilesInfo.hash,
    outStyle = FilesInfo.hash
  ) {
    in: Set[File] => srcFiles.toSet
  }
  cache(srcFiles.toSet).toSeq
}.taskValue


def makeJar(domainDirs: Seq[String]) = Def.task {
  val sourceDir = (sourceManaged in Compile).value
  val targetDir = (classDirectory in Compile).value
  val moduleDirName = baseDirectory.value.toString.split("/").last

  // Create jar from generated source files
  val srcJar = new File(baseDirectory.value + "/lib/" + moduleDirName + "-sources.jar/")
  val srcFilesData = files2TupleRec("", sourceDir, ".scala")
  sbt.IO.jar(srcFilesData, srcJar, new java.util.jar.Manifest)

  // Create jar from class files compiled from generated source files
  val targetJar = new File(baseDirectory.value + "/lib/" + moduleDirName + ".jar/")
  val targetFilesData = files2TupleRec("", targetDir, ".class")
  sbt.IO.jar(targetFilesData, targetJar, new java.util.jar.Manifest)

  // Cleanup now obsolete generated code
  domainDirs.foreach { dir =>
    sbt.IO.delete(sourceDir / dir)
    sbt.IO.delete(targetDir / dir)
  }
}.triggeredBy(compile in Compile)


def files2TupleRec(pathPrefix: String, dir: File, tpe: String): Seq[Tuple2[File, String]] = {
  sbt.IO.listFiles(dir) flatMap { f =>
    if (f.isFile && f.name.endsWith(tpe))
      Seq((f, s"${pathPrefix}${f.getName}"))
    else
      files2TupleRec(s"${pathPrefix}${f.getName}/", f, tpe)
  }
}

lazy val snapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
lazy val releases = "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo <<= version((v: String) => Some(if (v.trim endsWith "SNAPSHOT") snapshots else releases)),
  publishArtifact in Test := false,
  pomIncludeRepository := (_ => false),
  homepage := Some(url("https://github.com/marcgrue/molecule")),
  licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(ScmInfo(url("https://github.com/scalamolecule/molecule"), "scm:git:git@github.com:scalamolecule/molecule.git")),
  pomExtra := (
    <developers>
      <developer>
        <id>marcgrue</id>
        <name>Marc Grue</name>
        <url>http://marcgrue.com</url>
      </developer>
    </developers>
    )
)

lazy val noPublishSettings = Seq(
  publish :=(),
  publishLocal :=(),
  publishArtifact := false
)