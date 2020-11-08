package molecule.examples.mbrainz.dayOfDatomic

import java.util.UUID
import datomic.Peer
import molecule.datomic.peer.api._
import molecule.examples.mbrainz.dsl.mBrainz._
import molecule.facade.Conn
import molecule.util.MoleculeSpec
import scala.language.postfixOps

/*
  https://github.com/Datomic/day-of-datomic/blob/master/tutorial/pull.clj
*/

class Pull extends MoleculeSpec {
  sequential

  implicit val conn = Conn(Peer.connect("datomic:free://localhost:4334/mbrainz-1968-1973"))

  val ledZeppelin = Artist.e.name_("Led Zeppelin").get.head
  val mccartney = Artist.e.name_("Paul McCartney").get.head
  val darkSideOfTheMoon = Release.e.name_("The Dark Side of the Moon").get.head

  val dylanHarrisonSessions = Release.e.name_("Dylan–Harrison Sessions").get.head
  val dylanHarrisonCd = Release(dylanHarrisonSessions).media.get.head.head
  val ghostRiders = Release(dylanHarrisonSessions).Media.Tracks.e.position_(11).get.head


  "attribute name" >> {
    conn.db.pull("[:artist/name :artist/gid]", ledZeppelin).toString ===
      """{:artist/name "Led Zeppelin", :artist/gid #uuid "678d88b2-87b0-403b-b63d-5da7465aecc3"}"""


    Artist(ledZeppelin).name.gid.get.head ===
      ("Led Zeppelin", UUID.fromString("678d88b2-87b0-403b-b63d-5da7465aecc3"))

    conn.db.pull("[:artist/country]", ledZeppelin).toString ===
      """{:artist/country {:db/id 17592186045580}}"""

    Artist(ledZeppelin).country.get.head === 17592186045580L
  }


  "reverse lookup" >> {
    // (show first 3 country entities...)
    conn.db.pull("[:artist/_country]", ":country/GB").toString.take(93) ===
      """{:artist/_country [{:db/id 527765581342226} {:db/id 527765581342413} {:db/id 527765581343048}"""

    // (same entity ids come in another order)
    Artist.e.Country.name_("United Kingdom").get.take(3) ===
      List(606930418543592L, 747667906897894L, 628920651098089L)
  }


  "component defaults" >> {
    conn.db.pull("[:release/media]", darkSideOfTheMoon).toString.take(255) ===
      """{:release/media [{:db/id 927987813884021, :Medium/tracks [{:db/id 927987813884022, :Track/artists [{:db/id 646512837142669}], :Track/artistCredit "Pink Floyd", :Track/position 7, :Track/name "Us and Them", :Track/duration 469853} {:db/id 927987813884023, """

    // (same entity ids come in another order)
    Artist.e.Country.name_("United Kingdom").get.take(3) ===
      List(606930418543592L, 747667906897894L, 628920651098089L)
  }


  "reverse component lookup" >> {
    conn.db.pull("[:release/_media]", dylanHarrisonCd).toString ===
      """{:release/_media {:db/id 17592186070949}}"""

    Release.e.Media.e_(dylanHarrisonCd).get.head === 17592186070949L
  }


  "map specification" >> {
    conn.db.pull(
      "[:track/name {:track/artists [:db/id :artist/name]}]",
      ghostRiders
    ).toString ===
      """{:track/name "Ghost Riders in the Sky", :track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}"""


//    Release.name_("Dylan–Harrison Sessions").Media.Tracks.position_(11).name.Artists.e.name.debugGet

    Release.name_("Dylan–Harrison Sessions")
      .Media.Tracks.position_(11).name.Artists.*(Artist.e.name).get === List(
      ("Ghost Riders in the Sky", List(
        (646512837145512L,  "George Harrison"),
        (721279627832670L,  "Bob Dylan")
      ))
    )


//    Release.name_("Dylan–Harrison Sessions")
//      .Media.Tracks.position_(11).name.artists$.debugGet


//    Artist.e.name_("Led Zeppelin").gid$.debugGet

    conn.q(
      """[:find  ?a (pull ?a_gid1 [:Artist/gid])
        | :where [?a :Artist/name "Led Zeppelin"]
        |        [(molecule.util.fns/bind ?a) ?a_gid1]]""".stripMargin
    )
    conn.q(
      """[:find  ?f ?g ?j
        | :where [?a :Release/name "Dylan–Harrison Sessions"]
        |        [?a :Release/media ?c]
        |        [?c :Medium/tracks ?d]
        |        [?d :Track/position 11]
        |        [?d :Track/name ?f]
        |        [?d :Track/artists ?g]
        |        [?g :Artist/name ?j]]""".stripMargin
    )

    conn.q(
      """[:find  ?f (pull ?d [:track/name {:track/artists [:db/id :artist/name]}])
        | :where [?a :Release/name "Dylan–Harrison Sessions"]
        |        [?a :Release/media ?c]
        |        [?c :Medium/tracks ?d]
        |        [?d :Track/position 11]
        |        [?d :Track/name ?f]
        |        ]""".stripMargin
    ).toString === """List(List(Ghost Riders in the Sky, {:track/name "Ghost Riders in the Sky", :track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}))"""

    conn.q(
      """[:find ?c (pull ?d [:track/name {:track/artists [:db/id :artist/name]}])
        | :where [?a :Release/name "Dylan–Harrison Sessions"]
        |        [?a :Release/media ?c]
        |        [?c :Medium/tracks ?d]
        |        [?d :Track/position 11]
        |        [?d :Track/name ?f]
        |        ]""".stripMargin
    ).toString === """List(List(1002754604675937, {:track/name "Ghost Riders in the Sky", :track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}))"""

    conn.q(
      """[:find  ?f (pull ?d_artists1 [:Track/artists])
        | :where [?a :Release/name "Dylan–Harrison Sessions"]
        |        [?a :Release/media ?c]
        |        [?c :Medium/tracks ?d]
        |        [?d :Track/position 11]
        |        [?d :Track/name ?f]
        |        [(molecule.util.fns/bind ?d) ?d_artists1]]""".stripMargin
    ).toString === """List(List(Ghost Riders in the Sky, {:Track/artists [{:db/id 646512837145512} {:db/id 721279627832670}]}))"""

    conn.q(
      """[:find  ?f (pull ?d_track [{:track/artists [:db/id :artist/name]}])
        | :where [?a :Release/name "Dylan–Harrison Sessions"]
        |        [?a :Release/media ?c]
        |        [?c :Medium/tracks ?d]
        |        [?d :Track/position 11]
        |        [?d :Track/name ?f]
        |        [(molecule.util.fns/bind ?d) ?d_track]]""".stripMargin
    ).toString === """List(List(Ghost Riders in the Sky, {:track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}))"""

    conn.q(
      """[:find  ?f (pull ?d_track [{:track/artists [:db/id :artist/name]}])
        | :in $ [?pos ...]
        | :where [?a :Release/name "Dylan–Harrison Sessions"]
        |        [?a :Release/media ?c]
        |        [?c :Medium/tracks ?d]
        |        [?d :Track/position ?pos]
        |        [?d :Track/name ?f]
        |        [(molecule.util.fns/bind ?d) ?d_track]]""".stripMargin,
      datomic.Util.list(
        10.asInstanceOf[Object],
        11.asInstanceOf[Object],
        42.asInstanceOf[Object]
      )
    ).toString ===
    """List(List(One Too Many Mornings, {:track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}), """ +
    """List(Ghost Riders in the Sky, {:track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}))"""


//    println(dylanHarrisonSessions.touchQuoted)

    ok

  }

  }