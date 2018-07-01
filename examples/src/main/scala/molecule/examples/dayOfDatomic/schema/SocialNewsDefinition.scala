package molecule.examples.dayOfDatomic.schema
import molecule.schema.definition._

// http://blog.datomic.com/2013/05/a-whirlwind-tour-of-datomic-query_16.html


@InOut(2, 5)
object SocialNewsDefinition {

  trait Story {
    val title = oneString.fulltextSearch.indexed
    val url   = oneString.uniqueIdentity
  }

  trait Parent {
    val comment = one[Comment].isComponent
  }

  trait Comment {
    val author = one[User]
    val text   = oneString
  }

  trait User {
    val firstName    = oneString.indexed
    val lastName     = oneString.indexed
    val email        = oneString.uniqueIdentity
    val passwordHash = oneString
    val upVotes      = many[Story]
  }

  trait Publish {
    val at = oneDate.indexed
  }

  trait MetaData {
    val user    = one[User]
    val usecase = oneString
  }
}