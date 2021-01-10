package molecule.tests.examples.datomic.dayOfDatomic.dataModel

import molecule.core.data.model._

// See: http://blog.datomic.com/2013/06/component-entities.html

@InOut(0, 6)
object ProductsOrderDataModel {

  trait Order {
    val orderid   = oneInt
    val lineItems = many[LineItem].isComponent
    val lineItem  = one[LineItem].isComponent
  }


  trait LineItem {
    val product  = one[Product]
    val price    = oneDouble
    val quantity = oneInt
    val text     = oneString
    val comments = many[Comment].isComponent
  }

  trait Product {
    val description = oneString.indexed
  }

  // Some extra namespaces to explore insertion of multiple levels of nested data

  trait Comment {
    val text    = oneString
    val descr   = oneString
    val authors = many[Person].isComponent
  }

  trait Person {
    val name = oneString
  }
}
