package molecule.examples.dayOfDatomic
import molecule._
import molecule.ast.model._
import molecule.examples.dayOfDatomic.dsl.socialNews.{Story, _}
import molecule.util.MoleculeSpec

import scala.language.reflectiveCalls


class Provenance extends MoleculeSpec {


  "Transaction annotation data" in new SocialNewsSetup {

    val ecURL = "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html"

    // Stu adds two new stories with AddStories use case
    val stuTx = Story.title.url.tx_(Source.user_(stu).usecase_("AddStories")) insert List(
      ("ElastiCache in 6 minutes", ecURL),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html")
    )

    // Transaction meta data (stu id and use case name) is transacted as datoms in the db.part/tx partition
    m(Story.title.url.tx_(Source.user_(stu).usecase_("AddStories"))) -->
      Model(List(
        Atom("story", "title", "String", 1, VarValue, None, List(), List()),
        Atom("story", "url", "String", 1, VarValue, None, List(TxValue_), List()),
        TxModel(List(
          Atom("source", "user_", "Long", 1, Eq(List(17592186045423L)), None, List(), List()),
          Atom("source", "usecase_", "String", 1, Eq(List("AddStories")), None, List(), List())))
      )) -->
      List(
        List("ElastiCache in 6 minutes", ecURL),
        List("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html")
      ) -->
      //           action          temp id             attribute            value
      """List(
        |  List(  :db/add,   #db/id[:db.part/user -1000001],   :story/title   ,   ElastiCache in 6 minutes                                       )
        |  List(  :db/add,   #db/id[:db.part/user -1000001],   :story/url     ,   http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html  )
        |  List(  :db/add,   #db/id[:db.part/user -1000002],   :story/title   ,   Keep Chocolate Love Atomic                                     )
        |  List(  :db/add,   #db/id[:db.part/user -1000002],   :story/url     ,   http://blog.datomic.com/2012/08/atomic-chocolate.html          )
        |  List(  :db/add,   #db/id[:db.part/tx -1000049],   :source/user   ,   17592186045423                                                 )
        |  List(  :db/add,   #db/id[:db.part/tx -1000049],   :source/usecase,   AddStories                                                     )
        |)""".stripMargin

    // Two story entities and one transaction entity is created
    stuTx.eids === List(17592186045450L, 17592186045451L, 13194139534345L)
    val List(elasticacheStory, chocolateStory, stuTxId) = stuTx.eids

    // Now we have 5 stories - the two last from the transaction above
    //    Story.title.url.tx.debug
    Story.title.url.tx.get.sortBy(_._3) === List(
      ("Clojure Rationale", "http://clojure.org/rationale", 13194139534314L),
      ("Teach Yourself Programming in Ten Years", "http://norvig.com/21-days.html", 13194139534314L),
      ("Beating the Averages", "http://www.paulgraham.com/avg.html", 13194139534314L),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html", stuTxId),
      ("ElastiCache in 6 minutes", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html", stuTxId)
    )

    // We can also traverse the generated entity ids to see what is saved
    elasticacheStory.touch === Map(
      ":db/id" -> elasticacheStory,
      ":story/title" -> "ElastiCache in 6 minutes",
      ":story/url" -> "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html")

    chocolateStory.touch === Map(
      ":db/id" -> chocolateStory,
      ":story/title" -> "Keep Chocolate Love Atomic",
      ":story/url" -> "http://blog.datomic.com/2012/08/atomic-chocolate.html")

    // Time of transaction
    val stuTxInstant = stuTxId(":db/txInstant").get

    stuTxId.touch(1) === Map(
      ":db/id" -> stuTxId,
      ":db/txInstant" -> stuTxInstant,
      ":source/usecase" -> "AddStories",
      ":source/user" -> stu)

    // Or full traversal to Stu's data
    stuTxId.touch === Map(
      ":db/id" -> stuTxId,
      ":db/txInstant" -> stuTxInstant,
      ":source/usecase" -> "AddStories",
      ":source/user" -> Map(
        ":db/id" -> stu,
        ":user/email" -> "stuarthalloway@datomic.com",
        ":user/firstName" -> "Stu",
        ":user/lastName" -> "Halloway")
    )

    // Find data via transaction annotation data

    // Stories that Stu added (first annotation information used)
    Story.title.url.tx_(Source.user_(stu)).get === List(
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html"),
      ("ElastiCache in 6 minutes", ecURL)
    )

    // Stories that were added with the AddStories use case (second annotation information used)
    Story.title.url.tx_(Source.usecase_("AddStories")).get === List(
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html"),
      ("ElastiCache in 6 minutes", ecURL)
    )

    // Stories that Stu added with the AddStories use case (both annotations used)
    Story.title.url.tx_(Source.user_(stu).usecase_("AddStories")).get === List(
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html"),
      ("ElastiCache in 6 minutes", ecURL)
    )

    // Stories and transactions where Stu added stories (`tx` is returned)
    Story.title.tx(Source.user_(stu).usecase_("AddStories")).get === List(
      ("ElastiCache in 6 minutes", stuTxId),
      ("Keep Chocolate Love Atomic", stuTxId)
    )

    // Stories and names of who added them (Note that we can have nested annotation data!)
    Story.title.tx_(Source.User.firstName.lastName).get === List(
      ("ElastiCache in 6 minutes", "Stu", "Halloway"),
      ("Keep Chocolate Love Atomic", "Stu", "Halloway")
    )

    // Stories added by a user named "Stu"
    Story.title.tx_(Source.User.firstName_("Stu")).get === List(
      "ElastiCache in 6 minutes",
      "Keep Chocolate Love Atomic"
    )

    // Stories added by a user with email "stuarthalloway@datomic.com"
    Story.title.tx_(Source.User.email_("stuarthalloway@datomic.com")).get === List(
      "ElastiCache in 6 minutes",
      "Keep Chocolate Love Atomic"
    )

    // Count of stories added by a user with email "stuarthalloway@datomic.com"
    Story.title(count).tx_(Source.User.email_("stuarthalloway@datomic.com")).one === 2

    // Emails of users who added stories
    Story.title_.tx_(Source.usecase_("AddStories").User.email).get === List(
      "stuarthalloway@datomic.com"
    )


    // Updating data with additional transaction annotation data...

    // Ed fixes the spelling error
    val edTx   = Story(elasticacheStory).title("ElastiCache in 5 minutes").tx_(Source.user(ed).usecase_("UpdateStory")).update
    val edTxId = edTx.eids.last

    // This is what happens in the background:
    // Since we are "updating" a cardinality-one attribute (title) we only need to _add_ the new value.
    // Datomic automatically retracts the old value first (see further below).
    testUpdateMolecule(
      m(Story(elasticacheStory).title("ElastiCache in 5 minutes").tx_(Source.user(ed).usecase_("UpdateStory")))
    ) -->
      Model(List(
        Meta("story", "", "e", NoValue, Eq(List(17592186045450L))),
        Atom("story", "title", "String", 1, Eq(List("ElastiCache in 5 minutes")), None, List(TxValue_), List()),
        TxModel(List(
          Atom("source", "user", "Long", 1, Eq(List(17592186045424L)), None, List(), List()),
          Atom("source", "usecase_", "String", 1, Eq(List("UpdateStory")), None, List(), List()))))
      ) -->
      """List(
        |  List(  :db/add,   17592186045450,   :story/title   ,   ElastiCache in 5 minutes  )
        |  List(  :db/add,   #db/id[:db.part/tx -1000051],   :source/user   ,   17592186045424            )
        |  List(  :db/add,   #db/id[:db.part/tx -1000051],   :source/usecase,   UpdateStory               )
        |)""".stripMargin


    // Title now
    Story.url_(ecURL).title.get.head === "ElastiCache in 5 minutes"

    // Title before (using database as of the first transaction)
    Story.url_(ecURL).title.asOf(stuTx.inst).get.head === "ElastiCache in 6 minutes"

    // Who changed the title and when? Using the history database containing all datoms ever
    Story.url_(ecURL).title.op.tx(Source.User.firstName).history.get.reverse === List(
      ("ElastiCache in 6 minutes", true, stuTxId, "Stu"),
      ("ElastiCache in 6 minutes", false, edTxId, "Ed"), // retraction automatically added by Datomic
      ("ElastiCache in 5 minutes", true, edTxId, "Ed"))


    // Entire attributes history of ElastiCache story _entity_
    Story(elasticacheStory).a.v.op.tx(Source.usecase.User.firstName).history.get === List(
      (":story/url", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html", true, stuTxId, "AddStories", "Stu"),
      (":story/title", "ElastiCache in 6 minutes", true, stuTxId, "AddStories", "Stu"),
      (":story/title", "ElastiCache in 6 minutes", false, edTxId, "UpdateStory", "Ed"),
      (":story/title", "ElastiCache in 5 minutes", true, edTxId, "UpdateStory", "Ed")
    )


    // Stories with latest use case annotation
    Story.title.tx_(Source.usecase).get === List(
      ("Keep Chocolate Love Atomic", "AddStories"),
      ("ElastiCache in 5 minutes", "UpdateStory")
    )

    // Stories without use case annotations
    Story.title.tx_(Source.usecase_(nil)).get === List(
      "Clojure Rationale",
      "Beating the Averages",
      "Teach Yourself Programming in Ten Years"
    )
  }
}
