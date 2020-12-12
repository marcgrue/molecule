package molecule.examples.dayOfDatomic
import molecule.examples.ExampleSpec
import molecule.datomic.api.out3._
import molecule.examples.dayOfDatomic.dsl.socialNews._
import scala.language.postfixOps

// http://blog.datomic.com/2013/05/a-whirlwind-tour-of-datomic-query_16.html

class QueryTour extends ExampleSpec {

  "Queries and joins" in new SocialNewsSetup {

    // 3. Finding All Users with a first name
    User.e.firstName_.get === List(stu, ed)


    // 4. Finding a specific user
    User.e.email_("editor@example.com").get.head === ed


    // 5. Finding a User's Comments
    Comment.e.Author.email_("editor@example.com").get.sorted === List(c2, c4, c5, c7, c11)
    Comment.e.Author.email_("stuarthalloway@datomic.com").get.sorted === List(c1, c3, c6, c8, c9, c10, c12)

    Comment.e.text.Author.email_("editor@example.com").firstName.get.sorted === List(
      (c2, "blah 2", "Ed"),
      (c4, "blah 4", "Ed"),
      (c5, "blah 5", "Ed"),
      (c7, "blah 7", "Ed"),
      (c11, "blah 11", "Ed")
    )


    // 6. Returning an Aggregate of Comments of some Author
    Comment.e(count).Author.email_("editor@example.com").get.head === 5

    // Or we could simply read the size of the (un-aggregated) result (todo: measure performance differences)
    Comment.e.Author.email("editor@example.com").get.size === 5


    // 7. Have people commented on other people?
    // Composite molecule with a User.email (a User) sharing entity id with Parent that points to a Comment author
    m(User.email_ + Parent.Comment.author).get.size === 0

    // Sanity check: 2 people are commenting on stories
    // Composite molecule with a Story.title (a Story) sharing entity id with Parent that points to a Comment author
    m(Story.title_ + Parent.Comment.author).get.size === 2
  }


  "Schema-aware joins" in new SocialNewsSetup {

    // 8. A Schema Query

    // Attributes of all entities having comments
    val commentIds = Parent.e.comment_.get
    Parent(commentIds).a.get.sorted === List(
      ":Comment/author",
      ":Comment/text",
      ":Parent/comment",
      ":Story/title",
      ":Story/url"
    )

    // Attributes of stories having comments
    val storiesWithComments = m(Story.e.title_ + Parent.comment_).get
    Story(storiesWithComments).a.get === List(
      ":Story/url",
      ":Story/title",
      ":Parent/comment",
    )

    // Attributes of comments having a sub-comment
    val commentsWithSubComments = m(Comment.e.text_ + Parent.comment_).get
    Comment(commentsWithSubComments).a.get.sorted === List(
      ":Comment/author",
      ":Comment/text",
      ":Parent/comment"
    )
  }


  "Entities" in new SocialNewsSetup {

    // 9-11. Finding an entity ID - An implicit Entity
    // Since we can implicitly convert an entity ID to an entity we'll call the id `editor`
    val editor: Long = User.e.email_("editor@example.com").get.head


    // 12. Requesting an Attribute value
    editor.apply(":User/firstName") === Some("Ed")
    editor.apply(":User/firstName") === "Ed"
    editor(":unrecognizedKey") === "Ed"

    // Or as query
    User(editor).firstName.get.head === "Ed"


    // 13. Touching an entity
    // Get all attributes/values of this entity. Sub-component values are recursively retrieved
    editor.touch === Map(
      ":db/id" -> ed,
      ":User/email" -> "editor@example.com",
      ":User/firstName" -> "Ed",
      ":User/lastName" -> "Itor"
    )


    // 14. Navigating backwards

    // The editors comments (Comments pointing to the Editor entity)
    editor(":Comment/_author") === List(c2, c4, c5, c7, c11)

    // .. almost same as: (here, only matching data is returned)
    // Comments of editor
    Comment.e.author_(editor).get.sorted === List(c2, c4, c5, c7, c11)


    // 15. Navigating Deeper with entity api

    // Todo: using the entity api like this is clumsy. Find a better way...

    // Comments to the editors comments (with mapping)
    editor(":Comment/_author").asInstanceOf[List[Long]]
      .map(_(":Parent/comment")).asInstanceOf[List[Map[String, Any]]]
      .map(_.head._2) === List(c3, c6, c8, c12)

    // Comments to the editors comments (with `for` comprehension)
    (for {
      editorComment <- editor(":Comment/_author").asInstanceOf[List[Long]]
      editorCommentComment <- editorComment(":Parent/comment").asInstanceOf[Option[Map[String, Any]]]
    } yield editorCommentComment.head._2) === List(c3, c6, c8, c12)

    // todo - make some nice entity traversal dsl instead of this...
//    (for {
//      comments <- editor.apply[Seq[Long]](":Comment/_author").get
//      subComments <- comments.apply[Map[String, Any]](":Parent/comment").get
//    } yield subComments._2) === List(c3, c6, c8, c12)

    // Comments to the editors comments (with query)
    m(Comment.author_(editor) + Parent.comment).get.sorted === List(c3, c6, c8, c12)

    // Editors comments and responses (note that c4 wasn't commented on)
    m(Comment.author_(editor) + Parent.e.comment).get.sorted === List(
      (c2, c3),
      (c5, c6),
      (c7, c8),
      (c11, c12)
    )

    // Editors comments having responses (c4 not commented on)
    m(Comment.author_(editor) + Parent.e.comment_).get.sorted === List(c2, c5, c7, c11)
  }


  "Time travel" in new SocialNewsSetup {

    // Initial creation of Ed
    val tx2 = txR2.tx

    // Update Ed's first name
   val txR3 = m(User(ed).firstName("Edward")).update


    // 16. Querying for a Transaction
    val tx3 = User(ed).firstName_.tx.get.head
    tx3 === txR3.tx


    // 17. Converting Transaction to T
    val t3 = datomic.Peer.toT(tx3)
    t3 === txR3.t

    // Or query for relative system time directly
    User(ed).firstName_.t.get.head === t3


    // 18. Getting a Tx Instant
    val txInstant = User(ed).firstName_.txInstant.get.last


    // 19. Going back in Time

    // Current name
    User(ed).firstName.getAsOf(t3).head === "Edward"

    // Name before change (from previous transaction)
    User(ed).firstName.getAsOf(t3 - 1).head === "Ed"

    // We can use the transaction value also
    User(ed).firstName.getAsOf(tx3).head === "Edward"


    // Auditing ................................................................

    // 20. Querying Across All time (sort by transactions)
    User(ed).firstName.tx.op.getHistory.sortBy(_._2) === List(
      ("Ed", tx2, true),
      ("Ed", tx3, false),
      ("Edward", tx3, true)
    )

    // 21. Querying Plain Java Data - not supported by Molecule
  }
}