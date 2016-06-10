package molecule.examples.dayOfDatomic
import molecule._
import molecule.examples.dayOfDatomic.dsl.productsOrder._
import molecule.examples.dayOfDatomic.schema._
import molecule.util.MoleculeSpec


//object testNestedOutsideSpedcs2 extends DatomicFacade with App {
//  implicit val conn = load(ProductsOrderSchema, "Orders")
//
//  val order = m(Order.LineItems * LineItem.product.price.quantity) insert List((3L, 48.00, 1), (4L, 38.00, 2)) eid
//
//  println(m(Order.LineItems * LineItem.product.price.quantity).get)
//}

class ProductsAndOrders extends MoleculeSpec {

  "Nested data, 1 level without initial namespace asserts" >> {

    // See: http://blog.datomic.com/2013/06/component-entities.html

    // Make db
    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky").eids

    // Insert nested data .................................

    // We don't necessarily have to assert a fact of the initial namespace
    val order = m(Order.LineItems * LineItem.product.price.quantity) insert List((chocolateId, 48.00, 1), (whiskyId, 38.00, 2)) eid

    order.touch === Map(
      ":db/id" -> 17592186045422L,
      ":order/lineItems" -> List(
        Map(":db/id" -> 17592186045423L, ":lineItem/price" -> 48.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045419L, ":product/description" -> "Expensive Chocolate"), ":lineItem/quantity" -> 1),
        Map(":db/id" -> 17592186045424L, ":lineItem/price" -> 38.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky"), ":lineItem/quantity" -> 2)))

    // We can get the created order entity id and its lineItem's data
    m(Order.e.LineItems * LineItem.product.price.quantity).get === Seq(
      (17592186045422L, Seq((chocolateId, 48.00, 1), (whiskyId, 38.00, 2)))
    )

    // Or we can omit the order entity id and get the lineItem data in groups for each Order found (only 1 here)
    m(Order.LineItems * LineItem.product.price.quantity).get === Seq(
      Seq((chocolateId, 48.00, 1), (whiskyId, 38.00, 2))
    )

    // If we query for only 1 attribute we get a list of values instead of tuples of values
    m(Order.LineItems * LineItem.product).get === Seq(
      Seq(chocolateId, whiskyId)
    )
  }


  "Nested Data, 1 level" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky").eids

    // Template for Order with multiple LineItems
    val order = m(Order.orderid.LineItems * LineItem.product.price.quantity)

    // Insert nested data .................................

    // Make order with two line items and return created entity id
    val orderId = order.insert(23, List((chocolateId, 48.00, 1), (whiskyId, 38.00, 2))).eid

    // Find id of order with chocolate
    Order.e.LineItems.Product.description_("Expensive Chocolate").get.head === orderId


    // Touch entity ................................

    // Get all attributes/values of this entity. Sub-component values are recursively retrieved
    orderId.touch === Map(
      ":db/id" -> 17592186045422L,
      ":order/lineItems" -> List(
        Map(":db/id" -> 17592186045423L, ":lineItem/price" -> 48.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045419L, ":product/description" -> "Expensive Chocolate"), ":lineItem/quantity" -> 1),
        Map(":db/id" -> 17592186045424L, ":lineItem/price" -> 38.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky"), ":lineItem/quantity" -> 2)),
      ":order/orderid" -> 23)

    // We can re-use the `order` molecule
    order.get === List(
      (23, List((chocolateId, 48.00, 1), (whiskyId, 38.00, 2)))
    )

    // Retract nested data ............................

    // Retract entity - all subcomponents/lineItems are retracted
    orderId.retract

    // The products are still there
    Product.e.description_("Expensive Chocolate" or "Cheap Whisky").get === List(chocolateId, whiskyId)
  }


  "Nested Data, 1 level, with non-asserted values" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId, licoriceId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky", "Licorice").eids

    // Template for Order with multiple LineItems and optional quantity
    val order = m(Order.orderid.LineItems * LineItem.product.price.quantity$)


    // Insert .................................

    // Make order with two line items and return created entity id
    val List(order23, l1, l2, l3, order24, ll1, ll2) = order insert List(
      (23, List(
        (chocolateId, 48.00, Some(1)),
        (whiskyId, 38.00, None),
        (licoriceId, 77.00, Some(2)))),
      (24, List(
        (whiskyId, 38.00, Some(3)),
        (licoriceId, 77.00, Some(4))))) eids


    // Find id of orders containing various products
    Order.e.LineItems.Product.description_("Expensive Chocolate").get === List(order23)
    Order.e.LineItems.Product.description_("Licorice").get === List(order24, order23)


    // Touch ................................

    // Get all attributes/values of this entity. Sub-component values are recursively retrieved
    order23.touch === Map(
      ":db/id" -> 17592186045423L,
      ":order/lineItems" -> List(
        Map(":db/id" -> 17592186045424L, ":lineItem/price" -> 48.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045419L, ":product/description" -> "Expensive Chocolate"), ":lineItem/quantity" -> 1),
        Map(":db/id" -> 17592186045425L, ":lineItem/price" -> 38.0, ":lineItem/product" ->
          // Whisky _is_ touched although it has no quantity asserted!
          Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky")),
        Map(":db/id" -> 17592186045426L, ":lineItem/price" -> 77.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045421L, ":product/description" -> "Licorice"), ":lineItem/quantity" -> 2)),
      ":order/orderid" -> 23)

    order24.touch === Map(
      ":db/id" -> 17592186045427L,
      ":order/lineItems" -> List(
        Map(":db/id" -> 17592186045428L, ":lineItem/price" -> 38.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky"), ":lineItem/quantity" -> 3),
        Map(":db/id" -> 17592186045429L, ":lineItem/price" -> 77.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045421L, ":product/description" -> "Licorice"), ":lineItem/quantity" -> 4)),
      ":order/orderid" -> 24)


    // Get ................................

    // Get adjacent facts
    m(Order.orderid.LineItems.quantity.price.product.Product.description).get.sortBy(_._1) === List(
      (23, 2, 77.0, licoriceId, "Licorice"),
      // whisky for order 23 is _not_ fetched since it has no quantity asserted!
      (23, 1, 48.0, chocolateId, "Expensive Chocolate"),
      (24, 3, 38.0, whiskyId, "Cheap Whisky"),
      (24, 4, 77.0, licoriceId, "Licorice")
    )

    // Make `quantity` optional (by appending `$`) and get all facts wether quantity is asserted or not.
    // Quantities are then returned as Option[Int]
    m(Order.orderid.LineItems.quantity$.price.product.Product.description).get.sortBy(_._1) === List(
      (23, Some(2), 77.0, licoriceId, "Licorice"),
      (23, Some(1), 48.0, chocolateId, "Expensive Chocolate"),
      (23, None, 38.0, whiskyId, "Cheap Whisky"),
      (24, Some(3), 38.0, whiskyId, "Cheap Whisky"),
      (24, Some(4), 77.0, licoriceId, "Licorice")
    )

    // Get nested data
    m(Order.e.orderid.LineItems * LineItem.quantity.price).get === Seq(
      (17592186045423L, 23, Seq((1, 48.0), (2, 77.0))),
      (17592186045427L, 24, Seq((3, 38.0), (4, 77.0))))

    m(Order.orderid.LineItems * LineItem.quantity.price).get === Seq(
      (23, Seq((1, 48.0), (2, 77.0))),
      (24, Seq((3, 38.0), (4, 77.0))))

    m(Order.orderid.LineItems * LineItem.quantity).get === Seq(
      (23, Seq(1, 2)),
      (24, Seq(3, 4)))


    // Retract ............................

    // Retract entity - all subcomponents/lineItems are retracted
    order23.retract

    // The products are still there
    Product.e.description_("Expensive Chocolate" or "Cheap Whisky").get === List(chocolateId, whiskyId)
  }


  "Mixing nested and adjacent data" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Template for Order with multiple nested LineItems, optional quantity and adjacent `Product.description`
    val order = m(Order.orderid.LineItems * LineItem.price.quantity$.Product.description)


    // Insert .................................

    // Although we wouldn't like to create redundant products, we'll make some here anyway
    // just to demonstrate that the nested part of a molecule can contain an adjacent card-one reference
    order insert List(
      (23, List(
        (48.00, Some(1), "Expensive Chocolate"),
        (38.00, None, "Cheap Whisky"),
        (77.00, Some(2), "Licorice"))),
      (24, List(
        (38.00, Some(3), "Cheap Whisky"),
        (77.00, Some(4), "Licorice")))) eids

    val List(order23, order24) = Order.e.orderid.get.sortBy(_._2).map(_._1)

    // Find id of orders containing various products
    Order.e.LineItems.Product.description_("Expensive Chocolate").get === List(order23)
    Order.e.LineItems.Product.description_("Licorice").get === List(order24, order23)


    // Touch ................................

    // Get all attributes/values of this entity. Sub-component values are recursively retrieved
    order23.touch === Map(
      ":db/id" -> 17592186045419L,
      ":order/lineItems" -> List(
        Map(":db/id" -> 17592186045420L, ":lineItem/price" -> 48.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045421L, ":product/description" -> "Expensive Chocolate"), ":lineItem/quantity" -> 1),
        Map(":db/id" -> 17592186045422L, ":lineItem/price" -> 38.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045423L, ":product/description" -> "Cheap Whisky")),
        Map(":db/id" -> 17592186045424L, ":lineItem/price" -> 77.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045425L, ":product/description" -> "Licorice"), ":lineItem/quantity" -> 2)),
      ":order/orderid" -> 23)

    order24.touch === Map(
      ":db/id" -> 17592186045426L,
      ":order/lineItems" -> List(
        Map(":db/id" -> 17592186045427L, ":lineItem/price" -> 38.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045428L, ":product/description" -> "Cheap Whisky"), ":lineItem/quantity" -> 3),
        Map(":db/id" -> 17592186045429L, ":lineItem/price" -> 77.0, ":lineItem/product" ->
          Map(":db/id" -> 17592186045430L, ":product/description" -> "Licorice"), ":lineItem/quantity" -> 4)),
      ":order/orderid" -> 24)


    // Get ................................

    // Get nested data
    m(Order.orderid.LineItems * LineItem.price.quantity$.Product.description).get === List(
      (23, List(
        (48.00, Some(1), "Expensive Chocolate"),
        (38.00, None, "Cheap Whisky"),
        (77.00, Some(2), "Licorice"))),
      (24, List(
        (38.00, Some(3), "Cheap Whisky"),
        (77.00, Some(4), "Licorice"))))
  }


  "Nested Data, 2 levels" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky").eids

    // Insert nested data
    val eid = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * Comment.text) insert List(
      (23, List(
        (chocolateId, 48.00, 1, List("first", "product")),
        (whiskyId, 38.00, 2, List("second", "is", "best"))
      ))
    ) eid

    // Order lines with correct products added
    Order(eid).LineItems.product.get === List(chocolateId, whiskyId)

    // Order line comments are correct
    LineItem.product_(chocolateId).Comments.text.get === List("first", "product")

    // 2 levels of nested data entered
    eid.touch === Map(
      ":db/id" -> 17592186045422L,
      ":order/lineItems" -> List(
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045424L, ":comment/text" -> "first"),
          Map(":db/id" -> 17592186045425L, ":comment/text" -> "product")),
          ":lineItem/price" -> 48.0, ":lineItem/quantity" -> 1, ":lineItem/product" ->
            Map(":db/id" -> 17592186045419L, ":product/description" -> "Expensive Chocolate"),
          ":db/id" -> 17592186045423L),
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045427L, ":comment/text" -> "second"),
          Map(":db/id" -> 17592186045428L, ":comment/text" -> "is"),
          Map(":db/id" -> 17592186045429L, ":comment/text" -> "best")),
          ":lineItem/price" -> 38.0,
          ":lineItem/quantity" -> 2,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky"),
          ":db/id" -> 17592186045426L)),
      ":order/orderid" -> 23)


    m(Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * Comment.text)).get === List(
      (23, List(
        (chocolateId, 48.00, 1, List("first", "product")),
        (whiskyId, 38.00, 2, List("second", "is", "best"))
      ))
    )
  }


  "Nested Data, 2 levels, multiple attrs" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky").eids

    // Insert nested data with multiple 2nd level args
    val orderId = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * Comment.text.descr) insert List(
      (23, List(
        (chocolateId, 48.00, 1, List(
          ("first", "1a"),
          ("product", "1b"))),
        (whiskyId, 38.00, 2, List(
          ("second", "2b"),
          ("is", "2b"),
          ("best", "2c")))
      ))
    ) eid

    // 2 levels of nested data entered
    orderId.touch === Map(
      ":db/id" -> 17592186045422L,
      ":order/lineItems" -> List(
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045424L, ":comment/descr" -> "1a", ":comment/text" -> "first"),
          Map(":db/id" -> 17592186045425L, ":comment/descr" -> "1b", ":comment/text" -> "product")),
          ":lineItem/price" -> 48.0,
          ":lineItem/quantity" -> 1,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045419L, ":product/description" -> "Expensive Chocolate"),
          ":db/id" -> 17592186045423L),
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045427L, ":comment/descr" -> "2b", ":comment/text" -> "second"),
          Map(":db/id" -> 17592186045428L, ":comment/descr" -> "2b", ":comment/text" -> "is"),
          Map(":db/id" -> 17592186045429L, ":comment/descr" -> "2c", ":comment/text" -> "best")),
          ":lineItem/price" -> 38.0,
          ":lineItem/quantity" -> 2,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky"),
          ":db/id" -> 17592186045426L)),
      ":order/orderid" -> 23)


    m(Order.orderid.LineItems * (LineItem.quantity.price.Comments * Comment.text.descr)).get === List(
      (23, List(
        (1, 48.00, List(
          ("first", "1a"),
          ("product", "1b"))),
        (2, 38.00, List(
          ("second", "2b"),
          ("is", "2b"),
          ("best", "2c")))
      ))
    )
  }


  "Nested Data, 3 levels" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky").eids

    // Insert nested data in 3 levels
    val orderId = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name)) insert List(
      (23, List(
        (chocolateId, 48.00, 1, List(
          ("first", "1a", List("Marc Grue")),
          ("product", "1b", List("Marc Grue")))),
        (whiskyId, 38.00, 2, List(
          ("second", "2b", List("Don Juan", "Stuart Halloway")),
          ("is", "2b", List("Nick Smith")),
          ("best", "2c", List("test"))))
      ))
    ) eid

    /* 3 levels of nested data entered*/
    orderId.touch === Map(
      ":db/id" -> 17592186045422L,
      ":order/lineItems" -> List(
        Map(
          ":lineItem/comments" -> List(
            Map(
              ":db/id" -> 17592186045424L, ":comment/authors" -> List(
                Map(":db/id" -> 17592186045425L, ":person/name" -> "Marc Grue")),
              ":comment/descr" -> "1a",
              ":comment/text" -> "first"),
            Map(":db/id" -> 17592186045426L, ":comment/authors" -> List(
              Map(":db/id" -> 17592186045427L, ":person/name" -> "Marc Grue")),
              ":comment/descr" -> "1b",
              ":comment/text" -> "product")),
          ":lineItem/price" -> 48.0,
          ":lineItem/quantity" -> 1,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045419L, ":product/description" -> "Expensive Chocolate"),
          ":db/id" -> 17592186045423L),
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045429L, ":comment/authors" -> List(
            Map(":db/id" -> 17592186045430L, ":person/name" -> "Don Juan"),
            Map(":db/id" -> 17592186045431L, ":person/name" -> "Stuart Halloway")),
            ":comment/descr" -> "2b",
            ":comment/text" -> "second"),
          Map(":db/id" -> 17592186045432L, ":comment/authors" -> List(
            Map(":db/id" -> 17592186045433L, ":person/name" -> "Nick Smith")),
            ":comment/descr" -> "2b",
            ":comment/text" -> "is"),
          Map(":db/id" -> 17592186045434L, ":comment/authors" -> List(
            Map(":db/id" -> 17592186045435L, ":person/name" -> "test")),
            ":comment/descr" -> "2c",
            ":comment/text" -> "best")),
          ":lineItem/price" -> 38.0,
          ":lineItem/quantity" -> 2,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045420L, ":product/description" -> "Cheap Whisky"),
          ":db/id" -> 17592186045428L)),
      ":order/orderid" -> 23)

    m(Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name))).get === List(
      (23, List(
        (chocolateId, 48.00, 1, List(
          ("first", "1a", List("Marc Grue")),
          ("product", "1b", List("Marc Grue")))),
        (whiskyId, 38.00, 2, List(
          ("second", "2b", List("Don Juan", "Stuart Halloway")),
          ("is", "2b", List("Nick Smith")),
          ("best", "2c", List("test"))))
      ))
    )
  }


  "Nested Data, empty data sets" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val whiskyId = Product.description.insert("Cheap Whisky").eid

    // Insert nested data in 3 levels - passing empty lists of nested date prevents it to be saved
    val orderId = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name)) insert List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("second", "2b", List("Don Juan")),
          ("is", "2b", Nil),
          ("best", "2c", List())))
      ))
    ) eid

    // 3 levels of nested data entered, some with missing values that are then not asserted
    orderId.touch === Map(
      ":db/id" -> 17592186045421L,
      ":order/lineItems" -> List(
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045423L, ":comment/authors" -> List(
            Map(":db/id" -> 17592186045424L, ":person/name" -> "Don Juan")),
            ":comment/descr" -> "2b",
            ":comment/text" -> "second"),
          Map(":db/id" -> 17592186045425L, ":comment/descr" -> "2b", ":comment/text" -> "is"),
          Map(":db/id" -> 17592186045426L, ":comment/descr" -> "2c", ":comment/text" -> "best")),
          ":lineItem/price" -> 38.0,
          ":lineItem/quantity" -> 2,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045419L, ":product/description" -> "Cheap Whisky"),
          ":db/id" -> 17592186045422L)),
      ":order/orderid" -> 23)

    // Comments with no author are not fetched
    m(Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name))).get === List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("second", "2b", List("Don Juan"))))
      ))
    )
  }


  "Nested Data, non-asserted values" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert product
    val whiskyId = Product.description.insert("Cheap Whisky").eid

    // We can use an optional attribute (`Comment.descr$`) for missing values
    val orderId = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr$.Authors * Person.name)) insert List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("second", None, List("Don Juan")),
          ("chance", Some("foo"), List("Marc"))
        ))
      ))
    ) eid

    // 3 levels of nested data entered, some with missing values that are then not asserted
    orderId.touch === Map(
      ":db/id" -> 17592186045421L,
      ":order/lineItems" -> List(
        Map(":lineItem/comments" -> List(
          Map(":db/id" -> 17592186045423L, ":comment/authors" -> List(
            Map(":db/id" -> 17592186045424L, ":person/name" -> "Don Juan")),
            ":comment/text" -> "second"),
          Map(":db/id" -> 17592186045425L, ":comment/authors" -> List(
            Map(":db/id" -> 17592186045426L, ":person/name" -> "Marc")),
            ":comment/descr" -> "foo",
            ":comment/text" -> "chance")),
          ":lineItem/price" -> 38.0,
          ":lineItem/quantity" -> 2,
          ":lineItem/product" ->
            Map(":db/id" -> 17592186045419L, ":product/description" -> "Cheap Whisky"),
          ":db/id" -> 17592186045422L)),
      ":order/orderid" -> 23)

    // Comment with no `description` is not fetched
    m(Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name))).get === List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("chance", "foo", List("Marc"))
        ))
      ))
    )

    // Comment with optional `description` is fetched
    m(Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr$.Authors * Person.name))).get === List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("second", None, List("Don Juan")),
          ("chance", Some("foo"), List("Marc"))
        ))
      ))
    )
  }
}
