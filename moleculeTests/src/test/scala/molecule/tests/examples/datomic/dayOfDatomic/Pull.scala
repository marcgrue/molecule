package molecule.tests.examples.datomic.dayOfDatomic

import java.util.UUID
import datomic.Util
import molecule.core.util.{DatomicDevLocal, DatomicPeer}
import molecule.datomic.api.out8._
import molecule.tests.examples.datomic.mbrainz.dsl.mBrainz._
import molecule.TestSpec
import scala.language.postfixOps

/*
  https://github.com/Datomic/day-of-datomic/blob/master/tutorial/pull.clj
  https://docs.datomic.com/on-prem/pull.html
*/

class Pull extends TestSpec {

  class Setup extends MBrainzSetup {
    lazy val ledZeppelinUUID       = UUID.fromString("678d88b2-87b0-403b-b63d-5da7465aecc3")
    lazy val ledZeppelin           = Artist.e.gid_(ledZeppelinUUID).get.head
    lazy val mccartney             = Artist.e.gid_(UUID.fromString("ba550d0e-adac-4864-b88b-407cab5e76af")).get.head
    lazy val darkSideOfTheMoon     = Release.e.gid_(UUID.fromString("24824319-9bb8-3d1e-a2c5-b8b864dafd1b")).get.head
    lazy val dylanHarrisonSessions = Release.e.gid_(UUID.fromString("67bbc160-ac45-4caf-baae-a7e9f5180429")).get.head
    lazy val concertForBangladesh  = Release.e.gid_(UUID.fromString("f3bdff34-9a85-4adc-a014-922eef9cdaa5")).get.head
    lazy val dylanHarrisonCd       = Release(dylanHarrisonSessions).media.get.head
    lazy val ghostRiders           = Release(dylanHarrisonSessions).Media.Tracks.e.position_(11).get.head
    lazy val gb                    = Country.e.name_("United Kingdom").get.head
    lazy val georgeHarrison        = Artist.e.name_("George Harrison").get.head
    lazy val bobDylan              = Artist.e.name_("Bob Dylan").get.head
  }

  "raw pull" in new Setup {

    // Pull raw java.util.Map's with clojure.lang.Keyword -> <data> pairs
    conn.db.pull(
      "[:Artist/name :Artist/gid]",
      Util.list(Util.read(":Artist/gid"), ledZeppelinUUID)
    ) === Util.map(
      Util.read(":Artist/name"), "Led Zeppelin",
      Util.read(":Artist/gid"), ledZeppelinUUID
    )
  }


  "query" in new Setup {

    // Get typed results with queries

    // Attribute name
    Artist(ledZeppelin).name.startYear.get.head === ("Led Zeppelin", 1968)
    Artist(ledZeppelin).country.get.head === gb

    // Reverse lookup (with country id/name)
    Artist.e.country(gb).get.size === 482
    Artist.e.Country.name_("United Kingdom").get.size === 482

    // Component defaults
    val darkSideMedia = Release.e_(darkSideOfTheMoon).media.get.head.head
    /*
    // Touching the darkSideMedia entity pulls related tracks since
    // :medium/tracks is a component attribute: (commented out for brevity)

    darkSideMedia.touch === Map(
      ":db/id" -> 927987813949629L,
      ":Medium/format" -> ":Medium.format/vinyl12",
      ":Medium/position" -> 1,
      ":Medium/trackCount" -> 10,
      ":Medium/tracks" -> List(
        Map(
          ":Track/name" -> "Us and Them",
          ":track/artistCredit" -> "Pink Floyd",
          ":Track/artists" -> List(Map(
            ":Artist/gid" -> "83d91898-7763-47d7-b03b-b92132375c47",
            ":Artist/country" -> ":country/GB",
            ":Artist/type" -> ":Artist.type/group",
            ":Artist/sortName" -> "Pink Floyd",
            ":Artist/startYear" -> 1964,
            ":db/id" -> 646512837142669L,
            ":Artist/name" -> "Pink Floyd")),
          ":Track/position" -> 7,
          ":db/id" -> 927987813949630L,
          ":Track/duration" -> 469853),
        Map(
          ":Track/name" -> "Speak to Me",
          ":track/artistCredit" -> "Pink Floyd",
          ":Track/artists" -> List(Map(
            ":Artist/gid" -> "83d91898-7763-47d7-b03b-b92132375c47",
            ":Artist/country" -> ":country/GB",
            ":Artist/type" -> ":Artist.type/group",
            ":Artist/sortName" -> "Pink Floyd",
            ":Artist/startYear" -> 1964,
            ":db/id" -> 646512837142669L,
            ":Artist/name" -> "Pink Floyd")),
          ":Track/position" -> 1,
          ":db/id" -> 927987813949631L,
          ":Track/duration" -> 68346),
        Map(
          ":Track/name" -> "Time",

          etc...
     */

    // Reverse component lookup
    Release.e.media_(dylanHarrisonCd).get.head === dylanHarrisonSessions
    Release.e.Media.e_(dylanHarrisonCd).get.head === dylanHarrisonSessions

    // Map specification
    Track(ghostRiders).name.Artists.*(Artist.e.name).get.map(t => (t._1, t._2.sortBy(_._2))) === List(
      ("Ghost Riders in the Sky", List(
        (bobDylan, "Bob Dylan"),
        (georgeHarrison, "George Harrison"),
      ))
    )

    // Nested map specification
    Release(concertForBangladesh).Media.*(
      Medium.position.Tracks.*(
        Track.name.Artists.*(
          Artist.name))).get
      // sort output
      .map(medium =>
        medium.sortBy(_._1).map(tracks =>
          tracks._2.sortBy(_._1))) ===
      List(
        List(
          List(
            ("Bangla Dhun", List("Ravi Shankar")),
            ("George Harrison / Ravi Shankar Introduction", List("Ravi Shankar", "George Harrison"))),
          List(
            ("Bangla Desh", List("George Harrison")),
            ("Something", List("George Harrison"))),
          List(
            ("Awaiting on You All", List("George Harrison")),
            ("My Sweet Lord", List("George Harrison")),
            ("That's the Way God Planned It", List("Billy Preston")),
            ("Wah-Wah", List("George Harrison"))),
          List(
            ("A Hard Rain's Gonna Fall", List("Bob Dylan")),
            ("Blowin' in the Wind", List("Bob Dylan")),
            ("It Takes a Lot to Laugh / It Takes a Train to Cry", List("Bob Dylan")),
            ("Just Like a Woman", List("Bob Dylan")),
            ("Mr. Tambourine Man", List("Bob Dylan"))),
          List(
            ("Beware of Darkness", List("George Harrison")),
            ("Introduction of the Band", List("George Harrison")),
            ("It Don't Come Easy", List("Ringo Starr")),
            ("While My Guitar Gently Weeps", List("George Harrison"))),
          List(
            ("Here Comes the Sun", List("George Harrison")),
            ("Jumpin' Jack Flash / Youngblood", List("Leon Russell")))))


    // Wildcard specification
    //     concertForBangladeshEid.touch === Map(<big data graph for Bangladesh concert>)

    // Wildcard + map specification
    // Using optional attributes
    Track(ghostRiders).name.position$.duration$.artistCredit$.Artists.*(Artist.name).get === (
      if (system == DatomicDevLocal) {
        // dev-local sample db set has no artistCredit data
        List(("Ghost Riders in the Sky", Some(11), Some(218506), None,
          List("Bob Dylan", "George Harrison")))
      } else {
        List(("Ghost Riders in the Sky", Some(11), Some(218506), Some("Bob Dylan & George Harrison"),
          List("George Harrison", "Bob Dylan")))
      })

    // Default option (applying to None)
    Artist(mccartney).name.endYear$.get
      .map(t => (t._1, t._2.getOrElse(0))).head === ("Paul McCartney", 0)


    // Default option with different type
    Artist(mccartney).name.endYear$.get
      .map(t => (t._1, t._2.getOrElse("N/A"))).head === ("Paul McCartney", "N/A")


    // Absent attributes can't compile
    // Artist.name.diedIn1996.get


    // Explicit limit (applied on result)
    if (system == DatomicDevLocal)
      Track.e.Artists.e_(ledZeppelin).get(10) === List(
        27505382880529729L, 71402285107811038L, 27505382880529730L, 71402285107811037L, 27505382880529731L,
        71402285107811040L, 27505382880529732L, 71402285107811039L, 27505382880529733L, 27505382880529734L)
    else
      Track.e.Artists.e_(ledZeppelin).get(10) === List(
        1029142883684579L, 1029142883684580L, 1029142883684577L, 1029142883684578L, 1029142883688203L,
        1029142883688204L, 1029142883688202L, 1029142883688207L, 1029142883688208L, 1029142883688205L)


    // Limit + subspec
    Track.name.Artists.e_(ledZeppelin).get.sorted.take(10) === List(
      "Babe I'm Gonna Leave You",
      "Black Dog",
      "Black Mountain Side",
      "Bring It On Home",
      "Bring It on Home",
      "Bron-Yr-Aur Stomp",
      "Celebration Day",
      "Communication Breakdown",
      "D'yer Mak'er",
      "Dancing Days"
    )


    // Limit + subspec + :as option
    // Since attribute names are not returned, this is not relevant in this context


    // No limit
    Track.e.Artists.e_(ledZeppelin).get.size === 128


    // Empty results
    // Non-existing attributes not compilable in molecule


    // Pull expression in query (here, only distinct results are returned)
    Release.name.Artists.e_(ledZeppelin).get.sorted === List(
      "Heartbreaker / Bring It On Home",
      "Houses of the Holy",
      "Immigrant Song / Hey Hey What Can I Do",
      "Led Zeppelin",
      "Led Zeppelin II",
      "Led Zeppelin III",
      "Led Zeppelin IV",
      "Whole Lotta Love / Living Loving Maid",
    )


    // Dynamic pattern input

    // Note that only find-rel elements are allowed in client :find
    // :find (pull ?e [*])
    // :find [(pull ?e [*]) ...]  // not allowed in client
    // See question and answer: https://clojurians-log.clojureverse.org/datomic/2018-04-04

    if (system == DatomicPeer) {
      // Using a dynamic query
      conn.q(
        """[:find [(pull ?e pattern) ...]
          |       :in $ ?artist pattern
          |       :where [?e :release/artists ?artist]]""".stripMargin,
        ledZeppelin,
        "[:Release/name]"
      ) === List(
        List(":Release/name" -> "Led Zeppelin II"),
        List(":Release/name" -> "Led Zeppelin II"),
        List(":Release/name" -> "Led Zeppelin II"),
        List(":Release/name" -> "Led Zeppelin II"),
        List(":Release/name" -> "Immigrant Song / Hey Hey What Can I Do"),
        List(":Release/name" -> "Immigrant Song / Hey Hey What Can I Do"),
        List(":Release/name" -> "Houses of the Holy"),
        List(":Release/name" -> "Heartbreaker / Bring It On Home"),
        List(":Release/name" -> "Led Zeppelin III"),
        List(":Release/name" -> "Led Zeppelin III"),
        List(":Release/name" -> "Led Zeppelin III"),
        List(":Release/name" -> "Whole Lotta Love / Living Loving Maid"),
        List(":Release/name" -> "Led Zeppelin IV"),
        List(":Release/name" -> "Led Zeppelin IV"),
        List(":Release/name" -> "Led Zeppelin"),
        List(":Release/name" -> "Led Zeppelin"),
        List(":Release/name" -> "Led Zeppelin"),
      )
    }
  }
}