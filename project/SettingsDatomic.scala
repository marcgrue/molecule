import sbt._

/* Internal safeguards for Molecule development.
 * Doesn't affect the end user.
 */
trait SettingsDatomic {

  // Replace with path to your Datomic downloads directory
  val datomicDistributionsDir = "/Users/mg/lib/datomic"


  val testDatomicDir = new File(datomicDistributionsDir)
  if (!new File(datomicDistributionsDir).isDirectory)
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

  if (!useFree && datomicProVersions.isEmpty)
    throw new IllegalArgumentException(
      s"Please download Datomic starter/pro or " +
        s"switch to free version (see README_free and README_pro)")

  if (!testDatomicDir.listFiles().exists(_.getName == "datomic-free-0.9.5697"))
    throw new IllegalArgumentException(
      s"Please download datomic-free-0.9.5697 to `$datomicDistributionsDir` " +
        s"and run `bin/maven-install`.")


  // Force specific free version with `sbt compile -Ddatomic.free=0.9.9999`
  val datomicFreeVersion = sys.props.get("datomic.free").getOrElse(
    if (datomicProVersions.nonEmpty) datomicProVersions.max else ""
  )

  // Force specific pro version with `sbt compile -Ddatomic.pro=1.0.6202`
  val datomicProVersion = sys.props.get("datomic.pro").getOrElse(
    if (datomicProVersions.nonEmpty) datomicProVersions.max else ""
  )

  // Force specific dev-local version with `sbt compile -Ddatomic.dev-local=0.9.225`
  val datomicDevLocalVersion = sys.props.get("datomic.dev-local").getOrElse(
    if (datomicDevLocalVersions.nonEmpty) datomicDevLocalVersions.max else ""
  )

  val datomicHome = datomicProtocol match {
    case "dev"  => datomicDistributionsDir + "/datomic-pro-" + datomicProVersion
    case "free" => datomicDistributionsDir + "/datomic-free-0.9.5697"
  }

  def datomicVersions(system: String): Seq[String] = {
    val datomicPath = Path.userHome + (System.getProperty("os.name").toLowerCase match {
      case os if os.contains("win")                        => "\\.m2\\repository\\com\\datomic\\"
      case os if os.contains("mac") | os.contains("linux") => "/.m2/repository/com/datomic/"
      case osName                                          =>
        throw new RuntimeException(s"Unknown operating system $osName")
    })
    val dir         = new File(datomicPath + system)
    if (!dir.isDirectory || dir.listFiles() == null) {
      val cmd     = system match {
        case "datomic-pro" => "bin/maven-install"
        case "dev-local"   => "./install"
      }
      val distDir = new File(datomicDistributionsDir)
      if (distDir.listFiles() == null
        || !distDir.listFiles.filter(_.isDirectory).exists(_.getName.contains(system))
      ) {
        // Need to download Datomic distribution
        throw new RuntimeException(
          s"Couldn't find any $system Datomic distribution in $datomicDistributionsDir" +
            s"\nPlease download a $system distribution." +
            s"\nThen run `$cmd` in the distribution directory to install libraries to local .m2 repository.")
      }
      // Need to install Datomic distribution
      throw new RuntimeException(
          s"Please run `$cmd` in the $system distribution in $distDir to install " +
            s"the system libraries to local .m2 repository.")
    }
    // Get list of Datomic system version names
    dir.listFiles.filter(_.isDirectory).map(_.getName)
  }

  // print current datomic setup to console when running sbt commands from terminal
  println(
    s"""---- Datomic settings --------------------------------------------------
       |  -Dfree                 : ${sys.props.get("free").getOrElse("<not set>")}
       |  -Ddatomic.pro          : ${sys.props.get("datomic.pro").getOrElse("<not set>")}
       |  -Ddatomic.free         : ${sys.props.get("datomic.free").getOrElse("<not set>")}
       |
       |  datomicProtocol        : $datomicProtocol
       |  datomicDownloadsDir    : $datomicDistributionsDir
       |  datomicHome            : $datomicHome
       |
       |  datomicProVersions     : $datomicProVersions
       |  datomicProVersion      : $datomicProVersion
       |
       |  datomicDevLocalVersions: $datomicDevLocalVersions
       |  datomicDevLocalVersion : $datomicDevLocalVersion
       |------------------------------------------------------------------------""".stripMargin
  )
}
