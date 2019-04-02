package molecule.examples.mbrainz
import datomic.Peer
import molecule.api.in1_out4._
import molecule.examples.mbrainz.dsl.mBrainz._
import molecule.examples.mbrainz.schema.MBrainzSchemaLowerToUpper
import molecule.facade.Conn
import molecule.util.MoleculeSpec
import scala.language.postfixOps

/*
  Download free Datomic verson to your machine and download the mbrainz-sample data set (see links below)

  Start Datomic transactor first (replace first part of path to where you have the datomic download):

  cd [datomic-download]
  bin/transactor config/samples/dev-transactor-template.properties

  [first time (replace full path):]
  bin/datomic restore-db file:///Users/mg/lib/datomic/datomic-free-0.9.5697/mbrainz-1968-1973 datomic:free://localhost:4334/mbrainz-1968-1973

  Remember to add -Xmx2g -server to IDE compiler settings ("Additional build process VM options)
  Also, ensure the same java version is used in IDE

  See:
  http://blog.datomic.com/2013/07/datomic-musicbrainz-sample-database.html
  https://github.com/Datomic/mbrainz-sample
  https://github.com/Datomic/mbrainz-sample/wiki/Queries
*/

class MBrainz extends MoleculeSpec {
  sequential

  implicit val conn = Conn(Peer.connect("datomic:free://localhost:4334/mbrainz-1968-1973"))

  if (Schema.a(":Artist/name").get.isEmpty) {
    // Add uppercase-namespaced attribute names so that we can access the externally
    // transacted lowercase names with uppercase names of the molecule code.
    conn.datomicConn.transact(MBrainzSchemaLowerToUpper.namespaces)
  }


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
      (1972, "Always on My Mind / That Ain't Right", "Always on My Mind", "Brenda Lee"),
      (1972, "You'll Always Be a Friend", "You'll Always Be a Friend", "Hot Chocolate"),
      (1971, "Hot Rocks 1964-1971", "You Can’t Always Get What You Want", "The Rolling Stones"),
      (1970, "Check Out Your Mind!", "You'll Always Be Mine", "The Impressions"),
      (1968, "Signed, Sealed and Delivered", "I Want to Be With You Always", "Lefty Frizzell"))


    // Gender distribution
    Artist.gender_("male").e(count).get === List(1325)
    Artist.gender_("female").e(count).get === List(309)
    Artist.gender_("other").e(count).get === List(1)
  }


  //  // Todo: model as graph with bidirectional relationships
  //  "Collaboration" >> {
  //
  //    // Who collaborated with one of the Beatles?
  //    // Repeated attributes was translated to transitive lookups - model graph instead... todo
  //
  //    Track.Artists.name("John Lennon", "Paul McCartney", "George Harrison", "Ringo Starr").name.get === List(
  //      ("John Lennon", "The Plastic Ono Band"),
  //      ("George Harrison", "Bob Dylan"),
  //      ("John Lennon", "Yoko Ono"),
  //      ("George Harrison", "Ravi Shankar"),
  //      ("Paul McCartney", "Linda McCartney"))
  //
  //    // Who directly collaborated with George Harrison,
  //    Track.Artists.name_("George Harrison").name.get === List("Bob Dylan", "Ravi Shankar")
  //    // .. or collaborated with one of his collaborators?
  //    Track.Artists.name_("George Harrison").name_.name.get === List("Ali Akbar Khan")
  //
  //    // Parameterized input molecule for direct collaborators
  //    val collaborators = m(Track.Artists.name_(?).name)
  //
  //    // George Harrison's collaborators
  //    val collabs1 = collaborators("George Harrison").get.toSeq
  //    collabs1 === List("Bob Dylan", "Ravi Shankar")
  //
  //    // George Harrison's collaborators collaborators (includes George...)
  //    collaborators(collabs1).get === List("George Harrison", "Ali Akbar Khan")
  //  }


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
}