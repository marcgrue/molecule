import sbt.Keys.{baseDirectory, description, developers, doc, homepage, isSnapshot, licenses, packageDoc, pomIncludeRepository, publish, publishArtifact, publishLocal, publishMavenStyle, publishTo, scalacOptions, scmInfo, skip, sources, version}
import sbt._


object Publish {

  lazy private val snapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  lazy private val releases  = "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"


  lazy val withDocs = Seq(
    publishMavenStyle := true,
    publishTo := (if (isSnapshot.value) Some(snapshots) else Some(releases)),
    publishArtifact in Test := false,
    scalacOptions in Compile in doc ++= Seq(
      "-doc-root-content", baseDirectory.value + "/src/main/scaladoc/rootdoc.txt",
      "-diagrams", "-groups",
      "-doc-version", version.value,
      "-doc-title", "Molecule",
      "-sourcepath", (baseDirectory in ThisBuild).value.toString,
      "-doc-source-url", s"https://github.com/scalamolecule/molecule/tree/master€{FILE_PATH}.scala#L1"
    ),
    pomIncludeRepository := (_ => false),
    homepage := Some(url("http://scalamolecule.org")),
    licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scmInfo := Some(ScmInfo(url("https://github.com/scalamolecule/molecule"), "scm:git:git@github.com:scalamolecule/molecule.git")),
    description := "molecule",
    developers := List(
      Developer(
        id = "marcgrue",
        name = "Marc Grue",
        email = "marcgrue@gmail.com",
        url = url("http://marcgrue.com")
      )
    )
  )

  lazy val withoutDocs = Seq(
    publishMavenStyle := true,
    publishTo := (if (isSnapshot.value) Some(snapshots) else Some(releases)),
    publishArtifact in Test := false,
    sources in doc := Seq.empty,
    publishArtifact in packageDoc := false,
    pomIncludeRepository := (_ => false),
    homepage := Some(url("http://scalamolecule.org")),
    description := "molecule",
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

  lazy val not = Seq(
    skip in publish := true,
    publish := ((): Unit),
    publishLocal := ((): Unit),
    publishArtifact in(Compile, packageDoc) := false,
    sources in(Compile, doc) := Seq.empty
  )
}
