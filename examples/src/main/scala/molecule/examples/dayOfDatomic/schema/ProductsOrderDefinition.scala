package molecule.examples.dayOfDatomic.schema
import molecule.schema.definition._

// See: http://blog.datomic.com/2013/06/component-entities.html

@InOut(0, 6)
object ProductsOrderDefinition {

  trait Order {
    val orderid   = oneInt
    val lineItems = many[LineItem].subComponents
    val lineItem  = one[LineItem].subComponent // dummy for corner case
  }


  trait LineItem {
    val product  = one[Product]
    val price    = oneDouble
    val quantity = oneInt
    val text     = oneString // dummy for corner case
    val comments = many[Comment].subComponents
  }

  trait Product {
    val description = oneString.indexed
  }

  // Some extra namespaces to explore insertion of multiple levels of nested data

  trait Comment {
    val text    = oneString
    val descr   = oneString
    val authors = many[Person].subComponents
  }

  trait Person {
    val name = oneString
  }
}
