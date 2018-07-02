package molecule.examples.seattle.schema
import molecule.schema.definition._

@InOut(2, 8)
object SeattleDefinition {

  trait Community {
    val name         = oneString.fulltextSearch.doc("A community's name")
    val url          = oneString.doc("A community's url")
    val category     = manyString.fulltextSearch.doc("All community categories")
    val orgtype      = oneEnum('community, 'commercial, 'nonprofit, 'personal).doc("A community orgtype enum value")
    val `type`       = oneEnum('email_list, 'twitter, 'facebook_page, 'blog, 'website, 'wiki, 'myspace, 'ning).doc("Community type enum values")
    val neighborhood = one[Neighborhood].doc("A community's neighborhood")
  }

  trait Neighborhood {
    val name     = oneString.doc("A unique neighborhood name (upsertable)")
    val district = one[District].doc("A neighborhood's district")
  }

  trait District {
    val name   = oneString.doc("A unique district name (upsertable)")
    val region = oneEnum('n, 'ne, 'e, 'se, 's, 'sw, 'w, 'nw).doc("A district region enum value")
  }
}