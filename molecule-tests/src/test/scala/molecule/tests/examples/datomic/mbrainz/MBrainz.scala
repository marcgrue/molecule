package molecule.tests.examples.datomic.mbrainz

import molecule.datomic.api.in1_out4._
import molecule.tests.examples.datomic.mbrainz.dsl.mBrainz._
import molecule.TestSpec
import molecule.datomic.util.SystemPeer
import scala.language.postfixOps


// See instructions in examples/README_pro.md to setup testing mbrainz

class MBrainz extends TestSpec {


  "Data" in new MBrainzSetup {

    // What are the titles of all the tracks John Lennon played on? (showing 5)
    Track.name.Artists.name_("John Lennon").get.sorted.take(5) === List(
      "Aisumasen (I'm Sorry)",
      "Amsterdam",
      "Angela",
      "Attica State",
      "Au"
      // etc...
    )

    // What are the titles, album names, and release years of John Lennon's tracks?
    Release.year.name.Media.Tracks.name.Artists.name_("John Lennon")
      .get(5).sortBy(t => (t._1, t._2, t._3)) === List(
      (1969, "Live Peace in Toronto 1969", "Cold Turkey"),
      (1969, "Unfinished Music No. 3: Wedding Album", "Amsterdam"),
      (1971, "Power to the People", "Open Your Box"),
      (1973, "Some Time in New York City", "Sunday Bloody Sunday"),
      (1973, "Some Time in New York City", "The Luck of the Irish"),
    )

    // What are the titles, album names, and release years of the John Lennon tracks released before or during 1970?
    Release.year.<=(1970).name.Media.Tracks.name.Artists.name_("John Lennon")
      .get(5).sortBy(t => (t._1, t._2, t._3)) === List(
      (1969, "Live Peace in Toronto 1969", "Cold Turkey"),
      (1969, "Unfinished Music No. 2: Life With the Lions", "Baby's Heartbeat"),
      (1969, "Unfinished Music No. 2: Life With the Lions", "Two Minutes Silence"),
      (1969, "Unfinished Music No. 3: Wedding Album", "Amsterdam"),
      (1970, "Instant Karma! / Who Has Seen the Wind?", "Instant Karma!"),
    )

    if (system == SystemPeer) {
      // What are the titles, artists, album names, and release years of all tracks having the word "always" in their titles?
      Release.year.name.Media.Tracks.name.contains("always").Artists.name
        .get(5).sortBy(t => (t._1, t._2, t._3)) === List(
        (1968, "Signed, Sealed and Delivered", "I Want to Be With You Always", "Lefty Frizzell"),
        (1970, "Check Out Your Mind!", "You'll Always Be Mine", "The Impressions"),
        (1971, "Hot Rocks 1964-1971", "You Can’t Always Get What You Want", "The Rolling Stones"),
        (1972, "Always on My Mind / That Ain't Right", "Always on My Mind", "Brenda Lee"),
        (1972, "You'll Always Be a Friend", "You'll Always Be a Friend", "Hot Chocolate"),
      )
    }

    // Gender distribution
    Artist.gender_("male").e(count).get === List(1325)
    Artist.gender_("female").e(count).get === List(309)
    Artist.gender_("other").e(count).get === List(1)
  }


  //  // Todo: model as graph with bidirectional relationships
  //  "Collaboration" in new MBrainzSetup {
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


  "2-step querying" in new MBrainzSetup {
    // Which artists have songs that might be covers of The Who (or vice versa)?

    // 2-step querying:
    // First get songs of The Who
    val whoSongs = Track.name.not("Outro", "[outro]", "Intro", "[intro]").Artists.name_("The Who").get
    // Then get songs with same titles by other artists (using output from first query)
    // Note that we use the long list of whoSongs as input instead of applying
    // it directly to the track name attribute which would explode the query.
    m(Track.name(?).Artists.name.not("The Who"))(whoSongs).get(5) === List(
      ("The Last Time", "The Rolling Stones"),
      ("Overture", "Lionel Bart"),
      ("Sensation", "London Symphony Orchestra"),
      ("Miracle Cure", "London Symphony Orchestra"),
      ("Sensation", "Neon Rose")
    )
  }
}