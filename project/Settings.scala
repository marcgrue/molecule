import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin.autoImport.{BuildInfoKey, buildInfoKeys, buildInfoPackage}
import sbtmolecule.MoleculePlugin.autoImport.{moleculeMakeJars, moleculeSchemas}

object Settings {

  // Used by `coretests` and `examples` too that are not ScalaJS transpiled
  val base: Seq[Def.Setting[_]] = Seq(
    organization := "org.scalamolecule",
    organizationName := "ScalaMolecule",
    organizationHomepage := Some(url("http://www.scalamolecule.org")),
    version := "0.22.4",
    crossScalaVersions := Seq("2.12.11", "2.13.2"),
    scalaVersion in ThisBuild := "2.13.2",

    scalacOptions := List(
      "-feature",
      "-language:implicitConversions",
      "-deprecation"
    ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => Seq("-Ymacro-annotations")
      case _             => Nil
    }) ++ (
      // See https://github.com/suzaku-io/boopickle/commit/4d92604ee3e83c018e87a76a561468635634afac
      if (scala.util.Properties.javaVersion.startsWith("1.8")) Nil else Seq("-release", "8")),

    resolvers ++= Seq(
      ("datomic" at "http://files.datomic.com/maven").withAllowInsecureProtocol(true),
      ("clojars" at "http://clojars.org/repo").withAllowInsecureProtocol(true)
    ),

    unmanagedSourceDirectories in Compile ++= {
      (unmanagedSourceDirectories in Compile).value.map { dir =>
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, 13)) => file(dir.getPath ++ "-2.13+")
          case _             => file(dir.getPath ++ "-2.13-")
        }
      }
    }
  )

  val shared: Seq[Def.Setting[_]] = Seq(
    name := "molecule",
    moduleName := "molecule",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "moleculeBuildInfo"
  )

  val js: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0"
    )
  )

  val jvm: Seq[Def.Setting[_]] = Seq(
    name := "molecule",
    libraryDependencies ++= Seq(
      "com.datomic" % "datomic-free" % "0.9.5697",
      "org.specs2" %% "specs2-core" % "4.9.4"
    )
  )


  // Molecule boilerplate code generation settings -----------------

  lazy val moleculeCoretests: Seq[Def.Setting[_]] = Seq(
    moduleName := "molecule-coretests",
    moleculeMakeJars := true,
    moleculeSchemas := Seq(
      "molecule/coretests/bidirectionals",
      "molecule/coretests/nested",
      "molecule/coretests/schemaDef",
      "molecule/coretests/util"
    )
  )

  lazy val moleculeExamples: Seq[Def.Setting[_]] = Seq(
    moduleName := "molecule-examples",
    moleculeMakeJars := true,
    moleculeSchemas := Seq(
      "molecule/examples/dayOfDatomic",
      "molecule/examples/gremlin",
      "molecule/examples/mbrainz",
      "molecule/examples/seattle"
    )
  )
}
