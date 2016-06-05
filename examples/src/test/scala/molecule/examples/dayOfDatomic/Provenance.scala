package molecule.examples.dayOfDatomic
import molecule._
import molecule.ast.model._
import molecule.examples.dayOfDatomic.dsl.socialNews.{Story, _}
import molecule.util.MoleculeSpec

import scala.language.reflectiveCalls


class Provenance extends MoleculeSpec {


  "Transaction meta data" in new SocialNewsSetup {

    val ecURL = "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html"

    // Stu adds two new stories with AddStories use case
    val tx1 = Story.title.url.tx_(Source.user_(stu).usecase_("AddStories")) insert List(
      ("ElastiCache in 6 minutes", ecURL),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html")
    )

    // Transaction meta data (stu id and use case name) is transacted as datoms in the db.part/tx partition
    m(Story.title.url.tx_(Source.user_(stu).usecase_("AddStories"))) -->
      Model(List(
        Atom("story", "title", "String", 1, VarValue, None, List(), List()),
        Atom("story", "url", "String", 1, VarValue, None, List(), List()),
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
    tx1.eids === List(17592186045450L, 17592186045451L, 13194139534345L)
    val List(storyId1, storyId2, txId) = tx1.eids

    // Now we have 5 stories - the two last from the transaction above
    Story.title.url.tx.get.sortBy(_._3) === List(
      ("Clojure Rationale", "http://clojure.org/rationale", 13194139534314L),
      ("Teach Yourself Programming in Ten Years", "http://norvig.com/21-days.html", 13194139534314L),
      ("Beating the Averages", "http://www.paulgraham.com/avg.html", 13194139534314L),
      ("Keep Chocolate Love Atomic", "http://blog.datomic.com/2012/08/atomic-chocolate.html", txId),
      ("ElastiCache in 6 minutes", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html", txId)
    )

    // We can also traverse the generated entity ids to see what is saved
    storyId1.touch === Map(
      ":db/id" -> storyId1,
      ":story/title" -> "ElastiCache in 6 minutes",
      ":story/url" -> "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html")

    storyId2.touch === Map(
      ":db/id" -> storyId2,
      ":story/title" -> "Keep Chocolate Love Atomic",
      ":story/url" -> "http://blog.datomic.com/2012/08/atomic-chocolate.html")

    // Time of transaction
    val txInstant = txId(":db/txInstant").get

    txId.touch(1) === Map(
      ":db/id" -> txId,
      ":db/txInstant" -> txInstant,
      ":source/usecase" -> "AddStories",
      ":source/user" -> stu)

    // Or full traversal to Stu's data
    txId.touch === Map(
      ":db/id" -> txId,
      ":db/txInstant" -> txInstant,
      ":source/usecase" -> "AddStories",
      ":source/user" -> Map(
        ":db/id" -> stu,
        ":user/email" -> "stuarthalloway@datomic.com",
        ":user/firstName" -> "Stu",
        ":user/lastName" -> "Halloway")
    )



    // Ed fixes the spelling error
    Story(storyId1).title("ElastiCache in 5 minutes").tx_(Source.user(ed)).update

    // Title now
    Story.url_(ecURL).title.get.head === "ElastiCache in 5 minutes"

    // Title before
    Story.url_(ecURL).title.asOf(tx1.inst).get.head === "ElastiCache in 6 minutes"

    // Who changed the title and when?
    Story.url_(ecURL).title.tx.op.tx_(Source.User.email_).history.get.reverse === List(
      ("ElastiCache in 6 minutes", 13194139534345L, true),
      ("ElastiCache in 6 minutes", 13194139534348L, false),
      ("ElastiCache in 5 minutes", 13194139534348L, true))

    // (un-comment and run test to see data with current date...)
    // Story.e.url_(ecURL).title.tx.txInstant.txAdded.tx(Source.User.email_).history.get.reverse === List(
    //   (17592186045449,ElastiCache in 6 minutes,13194139534344,Sun Nov 30 01:21:55 CET 2014,true),
    //   (17592186045449,ElastiCache in 6 minutes,13194139534347,Sun Nov 30 01:21:55 CET 2014,false),
    //   (17592186045449,ElastiCache in 5 minutes,13194139534347,Sun Nov 30 01:21:55 CET 2014,true))


    // Entire history of Story entity (sort by transactions)
    Story(storyId1).a.v.tx.op.history.get.reverse.sortBy(_._3) === List(
      (":story/title", "ElastiCache in 6 minutes", 13194139534345L, true),
      (":story/url", "http://blog.datomic.com/2012/09/elasticache-in-5-minutes.html", 13194139534345L, true),
      (":story/title", "ElastiCache in 6 minutes", 13194139534348L, false),
      (":story/title", "ElastiCache in 5 minutes", 13194139534348L, true)
    )



  }
}
