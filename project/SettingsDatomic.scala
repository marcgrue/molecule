import sbt._


trait SettingsDatomic {

  // Replace with path to your Datomic downloads directory
  val datomicDownloadsDir = "/Users/mg/lib/datomic"


  val testDatomicDir = new File(datomicDownloadsDir)
  if (!new File(datomicDownloadsDir).isDirectory)
    throw new IllegalArgumentException(
      "Please set your datomic downloads directory path in project.SettingsDatomic")

  val datomicProVersions      = datomicVersions("datomic-pro")
  val datomicDevLocalVersions = datomicVersions("dev-local")


  // Force Datomic free version to be used with `sbt <cmd> -Dfree=true` where
  // <cmd> can be `compile`, `publish` etc.
  val useFree: Boolean = sys.props.get("free") match {
    case Some("true") => true
    case _            => false
  }

  val datomicProtocol = if (useFree || datomicProVersions.isEmpty) "free" else "dev"

  if (!useFree && !testDatomicDir.listFiles().exists(_.getName.startsWith("datomic-pro-")))
    throw new IllegalArgumentException(
      s"Please download Datomic starter/pro to `$datomicDownloadsDir` or " +
        s"switch to free version (see README_free and README_pro)")

  if (!testDatomicDir.listFiles().exists(_.getName =="datomic-free-0.9.5697"))
    throw new IllegalArgumentException(
      s"Please download datomic-free-0.9.5697 to `$datomicDownloadsDir` " +
        s"and run `bin/maven-install`.")


  // Force specific version with `sbt compile -Ddatomic.pro=1.0.6202`
  val datomicProVersion = sys.props.get("datomic.pro").getOrElse(
    if (datomicProVersions.nonEmpty) datomicProVersions.max else ""
  )

  // Force specific version with `sbt compile -Ddatomic.dev-local=0.9.225`
  val datomicDevLocalVersion = sys.props.get("datomic.dev-local").getOrElse(
    if (datomicDevLocalVersions.nonEmpty) datomicDevLocalVersions.max else ""
  )

  val datomicHome = datomicProtocol match {
    case "dev"  => datomicDownloadsDir + "/datomic-pro-" + datomicProVersion
    case "free" => datomicDownloadsDir + "/datomic-free-0.9.5697"
  }
  def datomicVersions(id: String): Seq[String] =
    Option(new File(Path.userHome + "/.m2/repository/com/datomic/" + id))
      .fold(Seq.empty[String])(f => f.listFiles.filter(_.isDirectory).map(_.getName))

  // print current datomic setup to console when running sbt commands from terminal
  println(
    s"""---- Datomic settings --------------------------------------------------
       |  -Dfree                 : ${sys.props.get("free").getOrElse("<not set>")}
       |  -Ddatomic.pro          : ${sys.props.get("datomic.pro").getOrElse("<not set>")}
       |  -Ddatomic.free         : ${sys.props.get("datomic.free").getOrElse("<not set>")}
       |
       |  datomicProtocol        : $datomicProtocol
       |  datomicDownloadsDir    : $datomicDownloadsDir
       |  datomicHome            : $datomicHome
       |  datomicProVersions     : $datomicProVersions
       |  datomicProVersion      : $datomicProVersion
       |  datomicDevLocalVersions: $datomicDevLocalVersions
       |  datomicDevLocalVersion : $datomicDevLocalVersion
       |------------------------------------------------------------------------""".stripMargin
  )
}
