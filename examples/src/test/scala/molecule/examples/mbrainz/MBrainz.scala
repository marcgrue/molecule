package molecule.examples.mbrainz
import datomic.Peer
import molecule.Imports._
import molecule.examples.mbrainz.dsl.mBrainz._
import molecule.facade.Conn
import molecule.util.MoleculeSpec
import scala.language.postfixOps

/*
  Download free Datomic verson to your machine and download the mbrainz-sample data set (see links below)

  Start Datomic transactor first (replace first part of path to where you have the datomic download):

  cd [datomic-download]
  bin/transactor config/samples/dev-transactor-template.properties

  [first time (replace full path):]
  bin/datomic restore-db file:///Users/mg/lib/datomic/datomic-free-0.9.5561.62/mbrainz-1968-1973 datomic:free://localhost:4334/mbrainz-1968-1973

  Remember to add -Xmx2g -server to IDE compiler settings ("Additional build process VM options)
  Also, ensure the same java version is used in IDE

  See:
  http://blog.datomic.com/2013/07/datomic-musicbrainz-sample-database.html
  https://github.com/Datomic/mbrainz-sample
  https://github.com/Datomic/mbrainz-sample/wiki/Queries
*/

class MBrainz extends MoleculeSpec {

  implicit val conn = Conn(Peer.connect("datomic:free://localhost:4334/mbrainz-1968-1973"))


  "Data" >> {

    // What are the titles of all the tracks John Lennon played on?
    Track.name.Artists.name_("John Lennon").get(3) === List("Baby's Heartbeat", "John & Yoko", "Nutopian International Anthem")

    // What are the titles, album names, and release years of John Lennon's tracks?
    Release.year.name.Media.Tracks.name.Artists.name_("John Lennon").get(5) === List(
      (1969, "Unfinished Music No. 3: Wedding Album", "Amsterdam"),
      (1973, "Some Time in New York City", "Sunday Bloody Sunday"),
      (1973, "Some Time in New York City", "The Luck of the Irish"),
      (1971, "Power to the People", "Open Your Box"),
      (1969, "Live Peace in Toronto 1969", "Cold Turkey")
    )

    // What are the titles, album names, and release years of the John Lennon tracks released before or during 1970?
    Release.year.<=(1970).name.Media.Tracks.name.Artists.name_("John Lennon").get(5) === List(
      (1969, "Unfinished Music No. 3: Wedding Album", "Amsterdam"),
      (1969, "Live Peace in Toronto 1969", "Cold Turkey"),
      (1970, "Instant Karma! / Who Has Seen the Wind?", "Instant Karma!"),
      (1969, "Unfinished Music No. 2: Life With the Lions", "Two Minutes Silence"),
      (1969, "Unfinished Music No. 2: Life With the Lions", "Baby's Heartbeat")
    )

    // What are the titles, artists, album names, and release years of all tracks having the word "always" in their titles?
    Release.year.name.Media.Tracks.name.contains("always").Artists.name.get(5) === List(
      (1972, "You'll Always Be a Friend", "You'll Always Be a Friend", "Hot Chocolate"),
      (1971, "Hot Rocks 1964-1971", "You Can’t Always Get What You Want", "The Rolling Stones"),
      (1972, "Always on My Mind / That Ain't Right", "Always on My Mind", "Brenda Lee"),
      (1970, "Check Out Your Mind!", "You'll Always Be Mine", "The Impressions"),
      (1968, "Signed, Sealed and Delivered", "I Want to Be With You Always", "Lefty Frizzell"))
  }

  "Collaboration" >> {

    // Who collaborated with one of the Beatles?
    // Repeated attributes are translated to transitive lookups
    Track.Artists.name("John Lennon", "Paul McCartney", "George Harrison", "Ringo Starr").name.get === List(
      ("John Lennon", "The Plastic Ono Band"),
      ("George Harrison", "Bob Dylan"),
      ("John Lennon", "Yoko Ono"),
      ("George Harrison", "Ravi Shankar"),
      ("Paul McCartney", "Linda McCartney"))

    // Who directly collaborated with George Harrison,
    Track.Artists.name_("George Harrison").name.get === List("Bob Dylan", "Ravi Shankar")
    // .. or collaborated with one of his collaborators?
    Track.Artists.name_("George Harrison").name_.name.get === List("Ali Akbar Khan")

    // Parameterized input molecule for direct collaborators
    val collaborators = m(Track.Artists.name_(?).name)

    // George Harrison's collaborators
    val collabs1 = collaborators("George Harrison").get.toSeq
    collabs1 === List("Bob Dylan", "Ravi Shankar")

    // George Harrison's collaborators collaborators (includes George...)
    collaborators(collabs1).get === List("George Harrison", "Ali Akbar Khan")
  }

  "2-step querying" >> {
    // Which artists have songs that might be covers of The Who (or vice versa)?

    // 2-step querying:
    // First get songs of The Who
    val whoSongs = Track.name.!=("Outro", "[outro]", "Intro", "[intro]").Artists.name_("The Who").get.toSeq
    // Then get songs with same titles by other artists (using output from first query)
    Track.name(whoSongs).Artists.name.!=("The Who").get(5) === List(
      ("The Last Time", "The Rolling Stones"),
      ("Overture", "Lionel Bart"),
      ("Sensation", "London Symphony Orchestra"),
      ("Miracle Cure", "London Symphony Orchestra"),
      ("Sensation", "Neon Rose")
    )
  }

//  "Distinct" >> {
//
//    println(conn.q("""[:find (distinct ?sortName)
//             | :with ?artist
//             | :where [?artist :artist/name "Fire"]
//             |        [?artist :artist/sortName ?sortName]]""".stripMargin))
//
//    println(conn.q("""[:find ?artist (distinct ?sortName)
//             | :where [?artist :artist/name "Fire"]
//             |        [?artist :artist/sortName ?sortName]]""".stripMargin))
//
//    Artist.e.name_("Fire").sortName.getD
//    Artist.name_("Fire").sortName.getD
//    Artist.name_("Fire").sortName(distinct).getD
//
//    ok
//  }
  //  "Transitive tests" >> {
  //    "No Bonds" >> {
  //      // Todo...
  //      Artist.name.name.debug === 7
  //
  //      Artist.sortName.name.name.debug === 7
  //      Artist.name.sortName.name.debug === 7
  //      Artist.name.name.sortName.debug === 7
  //    }
  //
  //    "One Bond" >> {
  //      Track.Artists.name.name.debug === 7
  //
  //      Track.Artists.sortName.name.name.debug === 7
  //      Track.Artists.name.sortName.name.debug === 7
  //      Track.Artists.name.name.sortName.debug === 7
  //    }
  //
  //    "Two Bond" >> {
  //      Track.Artists.sortName.name._Track.Artists.name.debug === 7
  //      Track.Artists.name.sortName._Track.Artists.name.debug === 7
  //      Track.Artists.name._Track.Artists.sortName.name.debug === 7
  //      Track.Artists.name._Track.Artists.name.sortName.debug === 7
  //
  //      Track.Artists.sortName.name._Track.Artists.name.debug === 7
  //      Track.Artists.name.sortName._Track.Artists.name.debug === 7
  //      Track.Artists.name._Track.Artists.sortName.name.debug === 7
  //      Track.Artists.name._Track.Artists.name.sortName.debug === 7
  //
  //      //    Track.Artists.name.name.debug === 7
  //
  //      Track.Artists.name("John Lennon", "Paul McCartney", "George Harrison", "Ringo Starr").name.debug === 7
  //    }
  //  }
}