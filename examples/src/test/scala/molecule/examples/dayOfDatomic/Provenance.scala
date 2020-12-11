package molecule.examples.dayOfDatomic
import molecule.core.ast.model._
import molecule.core.util.testing.MoleculeSpec
import molecule.datomic.api.out7._
import molecule.examples.dayOfDatomic.dsl.socialNews._
import scala.language.reflectiveCalls


class Provenance extends MoleculeSpec {

  "Transaction meta data" in new SocialNewsSetup {

    val ecURL = "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html"

    // Stu adds two new stories with AddStories use case
    val stuTx = Story.title.url.Tx(MetaData.user_(stu).usecase_("AddStories")) insert List(
      ("ElastiCache in 6 minutes", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html"),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html")
    )

    // Transaction meta data (stu id and use case name) is transacted as datoms in the db.part/tx partition
    m(Story.title.url.Tx(MetaData.user_(stu).usecase_("AddStories"))) -->
      Model(List(
        Atom("Story", "title", "String", 1, VarValue, None, List(), List()),
        Atom("Story", "url", "String", 1, VarValue, None, List(), List()),
        TxMetaData(List(
          Atom("MetaData", "user_", "ref", 1, Eq(List(stu)), None, List(), List()),
          Atom("MetaData", "usecase_", "String", 1, Eq(List("AddStories")), None, List(), List())))
      )) -->
      List(
        List("ElastiCache in 6 minutes", ecURL),
        List("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html")
      ) -->
      //       operation     temp id (dummy values)         attribute          value
      s"""List(
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Story/title     ,  ElastiCache in 6 minutes                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Story/url       ,  http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :Story/title     ,  Keep Chocolate Love Atomic                                   ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :Story/url       ,  http://blog.datomic.com/2012/08/atomic-chocolate.html        ),
        |  List(:db/add,  #db/id[:db.part/tx   -1000003],  :MetaData/user   ,  $stu                                               ),
        |  List(:db/add,  #db/id[:db.part/tx   -1000003],  :MetaData/usecase,  AddStories                                                   )
        |)""".stripMargin

    // Two story entities and one transaction entity is created
    val List(elasticacheStory, chocolateStory, stuTxId) = stuTx.eids

    // Now we have 5 stories - the two last from the transaction above
    Story.title.url.tx.get.sortBy(_._3) === List(
      ("Clojure Rationale", "http://clojure.org/rationale", tx1),
      ("Teach Yourself Programming in Ten Years", "http://norvig.com/21-days.html", tx1),
      ("Beating the Averages", "http://www.paulgraham.com/avg.html", tx1),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html", stuTxId),
      ("ElastiCache in 6 minutes", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html", stuTxId)
    )

    // We can also traverse the generated entity ids to see what is saved
    elasticacheStory.touch === Map(
      ":db/id" -> elasticacheStory,
      ":Story/title" -> "ElastiCache in 6 minutes",
      ":Story/url" -> "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html")

    chocolateStory.touch === Map(
      ":db/id" -> chocolateStory,
      ":Story/title" -> "Keep Chocolate Love Atomic",
      ":Story/url" -> "http://blog.datomic.com/2012/08/atomic-chocolate.html")

    // Time of transaction
    val stuTxInstant = stuTxId.get[java.util.Date](":db/txInstant").get

    // Limit entity traversal 1 level deep
    stuTxId.touchMax(1) === Map(
      ":db/id" -> stuTxId,
      ":db/txInstant" -> stuTxInstant,
      ":MetaData/usecase" -> "AddStories",
      ":MetaData/user" -> stu
    )

    // Or full traversal to Stu's data
    stuTxId.touch === Map(
      ":db/id" -> stuTxId,
      ":db/txInstant" -> stuTxInstant,
      ":MetaData/usecase" -> "AddStories",
      ":MetaData/user" -> Map(
        ":db/id" -> stu,
        ":User/email" -> "stuarthalloway@datomic.com",
        ":User/firstName" -> "Stu",
        ":User/lastName" -> "Halloway"
      )
    )

    // Find data via transaction meta data

    // Stories that Stu added (first meta information used)
    Story.title.url.Tx(MetaData.user_(stu)).get.sortBy(_._1) === List(
      ("ElastiCache in 6 minutes", ecURL),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html"),
    )

    // Stories that were added with the AddStories use case (second meta information used)
    Story.title.url.Tx(MetaData.usecase_("AddStories")).get.sortBy(_._1) === List(
      ("ElastiCache in 6 minutes", ecURL),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html"),
    )

    // Stories that Stu added with the AddStories use case (both meta data used)
    Story.title.url.Tx(MetaData.user_(stu).usecase_("AddStories")).get.sortBy(_._1) === List(
      ("ElastiCache in 6 minutes", ecURL),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html"),
    )

    // Stories and transactions where Stu added stories (`tx` is returned)
    Story.title.tx.Tx(MetaData.user_(stu).usecase_("AddStories")).get.sortBy(_._1) === List(
      ("ElastiCache in 6 minutes", stuTxId),
      ("Keep Chocolate Love Atomic", stuTxId),
    )

    // Stories and names of who added them (Note that we can have referenced meta data!)
    Story.title.Tx(MetaData.User.firstName.lastName).get.sortBy(_._1) === List(
      ("ElastiCache in 6 minutes", "Stu", "Halloway"),
      ("Keep Chocolate Love Atomic", "Stu", "Halloway")
    )

    // Stories added by a user named "Stu"
    Story.title.Tx(MetaData.User.firstName_("Stu")).get.sorted === List(
      "ElastiCache in 6 minutes",
      "Keep Chocolate Love Atomic"
    )

    // Stories added by a user with email "stuarthalloway@datomic.com"
    Story.title.Tx(MetaData.User.email_("stuarthalloway@datomic.com")).get .sorted=== List(
      "ElastiCache in 6 minutes",
      "Keep Chocolate Love Atomic"
    )

    // Count of stories added by a user with email "stuarthalloway@datomic.com"
    Story.title(count).Tx(MetaData.User.email_("stuarthalloway@datomic.com")).get.head === 2

    // Emails of users who added stories
    Story.title_.Tx(MetaData.usecase_("AddStories").User.email).get === List(
      "stuarthalloway@datomic.com"
    )


    // Updating data with additional transaction meta data...

    // Ed fixes the spelling error
    val edTx   = Story(elasticacheStory).title("ElastiCache in 5 minutes").Tx(MetaData.user(ed).usecase_("UpdateStory")).update
    val edTxId = edTx.tx

    // Title now
    Story.url_(ecURL).title.get.head === "ElastiCache in 5 minutes"

    // Title before (using database as of the first transaction)
    Story.url_(ecURL).title.getAsOf(stuTx.inst).head === "ElastiCache in 6 minutes"

    // Who changed the title and when? Using the history database
    Story.url_(ecURL).title.op.tx.Tx(MetaData.usecase.User.firstName).getHistory.sortBy(r => (r._3, r._2)) === List(
      ("ElastiCache in 6 minutes", true, stuTxId, "AddStories", "Stu"), // Stu adds the story
      ("ElastiCache in 6 minutes", false, edTxId, "UpdateStory", "Ed"), // retraction automatically added by Datomic
      ("ElastiCache in 5 minutes", true, edTxId, "UpdateStory", "Ed")   // Ed's update of the title
    )

    // Entire attributes history of ElastiCache story _entity_
    Story(elasticacheStory).a.v.op.tx.Tx(MetaData.usecase.User.firstName).getHistory.sortBy(r => (r._4, r._3)) === List(
      (":Story/title", "ElastiCache in 6 minutes", true, stuTxId, "AddStories", "Stu"),
      (":Story/url", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html", true, stuTxId, "AddStories", "Stu"),
      (":Story/title", "ElastiCache in 6 minutes", false, edTxId, "UpdateStory", "Ed"),
      (":Story/title", "ElastiCache in 5 minutes", true, edTxId, "UpdateStory", "Ed"),
    )

    // Stories with latest use case meta date
    Story.title.Tx(MetaData.usecase).get.sortBy(_._1) === List(
      ("ElastiCache in 5 minutes", "UpdateStory"),
      ("Keep Chocolate Love Atomic", "AddStories"),
    )

    // Stories without use case meta data
    Story.title.Tx(MetaData.usecase_(Nil)).get.sorted === List(
      "Beating the Averages",
      "Clojure Rationale",
      "Teach Yourself Programming in Ten Years"
    )
  }
}
