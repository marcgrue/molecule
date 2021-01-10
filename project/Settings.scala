import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

object Settings extends SettingsDatomic with SettingsMolecule {

  val base: Seq[Def.Setting[_]] = Seq(
    organization := "org.scalamolecule",
    organizationName := "ScalaMolecule",
    organizationHomepage := Some(url("http://www.scalamolecule.org")),
    version in ThisBuild := "0.23.2",
    crossScalaVersions := Seq("2.12.12", "2.13.4"),
    scalaVersion in ThisBuild := "2.13.4",

    scalacOptions := List(
      "-feature",
      "-language:implicitConversions",
      "-deprecation",
      "-language:postfixOps",
      "-language:higherKinds"
    ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => Seq("-Ymacro-annotations")
      case _             => Nil
    }),

    resolvers ++= Seq(
      "clojars" at "https://clojars.org/repo",
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

  val js: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0"
    )
  )

  val jvm: Seq[Def.Setting[_]] = {
    Seq(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "org.specs2" %% "specs2-core" % "4.10.5",
        "org.scalamolecule" %% "datomic-client-api-java-scala" % "0.6.1"
      )
    ) ++ (if (datomicProtocol == "free") {
      Seq(libraryDependencies += "com.datomic" % "datomic-free" % "0.9.5697")
    } else {
      Seq(
        // Please download from https://cognitect.com/dev-tools and install locally per included instructions
        libraryDependencies += "com.datomic" % "datomic-pro" % datomicProVersion,
        excludeDependencies += ExclusionRule("com.datomic", "datomic-free"),
        credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
      )
    })
  }

  val tests: Seq[Def.Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      // Proprietary Client dev-local dependency needed for tests against dev-local
      // Please download from https://cognitect.com/dev-tools and install locally per included instructions
      "com.datomic" % "dev-local" % datomicDevLocalVersion
    ),

    // Find scala version specific jars in respective libs
    unmanagedBase := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => file(unmanagedBase.value.getPath ++ "/2.13")
        case _             => file(unmanagedBase.value.getPath ++ "/2.12")
      }
    },

    // Let IntelliJ detect created jars in unmanaged lib directory
    exportJars := true,

    // Run sbt tests for all systems sequentially to avoid data locks with db
    parallelExecution in Test := false,

    buildInfoKeys := Seq[BuildInfoKey](
      name, version, scalaVersion, sbtVersion,
      "datomicProtocol" -> datomicProtocol,
      "datomicHome" -> datomicHome,
      "datomicProVersions" -> datomicProVersions,
      "datomicProVersion" -> datomicProVersion,
      "datomicDevLocalVersions" -> datomicDevLocalVersions,
      "datomicDevLocalVersion" -> datomicDevLocalVersion
    ),
    buildInfoPackage := "moleculeBuildInfo"
  )
}
