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
    version := "0.23.0-SNAPSHOT",
    crossScalaVersions := Seq("2.12.12", "2.13.3"),
    scalaVersion in ThisBuild := "2.13.3",

    scalacOptions := List(
      "-feature",
      "-language:implicitConversions",
      "-deprecation",
      "-language:postfixOps"
    ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => Seq("-Ymacro-annotations")
      case _             => Nil
    }) ++ (
      // See https://github.com/suzaku-io/boopickle/commit/4d92604ee3e83c018e87a76a561468635634afac
      if (scala.util.Properties.javaVersion.startsWith("1.8")) Nil else Seq("-release", "8")),

    resolvers ++= Seq(
      ("datomic" at "http://files.datomic.com/maven").withAllowInsecureProtocol(true),
      ("clojars" at "http://clojars.org/repo").withAllowInsecureProtocol(true),
      // If using datomic-pro/starter
      "my.datomic.com" at "https://my.datomic.com/repo"
    ),

    /*
    If using datomic-pro/starter, create a ~/.sbt/.credentials file with the following content:
      realm=Datomic Maven Repo
      host=my.datomic.com
      id=my.datomic.com
      user=<your-username>
      pass=<your-password>
    * */
//    credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),

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
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.specs2" %% "specs2-core" % "4.10.0",
      "org.scalamolecule" % "datomic-client-api-java-scala" % "0.4.5-SNAPSHOT",

//            "com.datomic" % "datomic-free" % "0.9.5697",
      "com.datomic" % "datomic-pro" % "1.0.6202",
    ),
    // Add this exclusion if datomic-pro is used to avoid conflicts
    excludeDependencies ++= Seq(ExclusionRule("com.datomic", "datomic-free"))
  )

  // Proprietary Client dev-local dependency needed for tests
  // Please download from https://cognitect.com/dev-tools and install locally per included instructions
  val tests: Seq[Def.Setting[_]] = Seq(
    resolvers ++= Seq(Resolver.mavenLocal),
    libraryDependencies ++= Seq(
      "com.datomic" % "dev-local" % "0.9.225"
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
