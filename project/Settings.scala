import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport.{buildInfoKeys, buildInfoPackage, BuildInfoKey}

object Settings extends SettingsDatomic with SettingsMolecule {


  val base: Seq[Def.Setting[_]] = Seq(
    organization := "org.scalamolecule",
    organizationName := "ScalaMolecule",
    organizationHomepage := Some(url("http://www.scalamolecule.org")),
    version in ThisBuild := "0.23.0-SNAPSHOT",
    crossScalaVersions := Seq("2.12.12", "2.13.4"),
    scalaVersion in ThisBuild := "2.13.4",

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
      "my.datomic.com" at "https://my.datomic.com/repo",
      Resolver.mavenLocal
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
    name := "shared",
    moduleName := "shared",
    buildInfoKeys := Seq[BuildInfoKey](
      name, version, scalaVersion, sbtVersion,
      "datomicProtocol" -> datomicProtocol,
      "datomicHome" -> datomicHome,
      "datomicProVersions" -> datomicProVersions,
      "datomicProVersion" -> datomicProVersion,
      "datomicDevLocalVersions" -> datomicDevLocalVersions,
      "datomicDevLocalVersion" -> datomicDevLocalVersion,
    ),
    buildInfoPackage := "moleculeBuildInfo"
  )

  val js: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0"
    )
  )


  val jvm: Seq[Def.Setting[_]] = {
    Seq(
      name := "jvm",
      moduleName := "jvm",
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "org.specs2" %% "specs2-core" % "4.10.0",
        "org.scalamolecule" % "datomic-client-api-java-scala" % "0.5.4-SNAPSHOT"
      )
    ) ++ (if (datomicProtocol == "free") {
      Seq(libraryDependencies += "com.datomic" % "datomic-free" % "0.9.5697")
    } else {
      Seq(
        libraryDependencies += "com.datomic" % "datomic-pro" % datomicProVersion,
        excludeDependencies += ExclusionRule("com.datomic", "datomic-free")
      )
    })
  }

  // Proprietary Client dev-local dependency needed for tests against dev-local
  // Please download from https://cognitect.com/dev-tools and install locally per included instructions
  val tests: Seq[Def.Setting[_]] = Seq(
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "com.datomic" % "dev-local" % datomicDevLocalVersion
    )
  )
}
