package molecule.examples.dayOfDatomic
import molecule.datomic.peer.api._


import molecule.examples.dayOfDatomic.dsl.productsOrder._
import molecule.examples.dayOfDatomic.schema._
import molecule.util.MoleculeSpec


class ProductsAndOrders extends MoleculeSpec {


  "Nested data, 1 level without initial namespace asserts" >> {

    // See: http://blog.datomic.com/2013/06/component-entities.html

    // Make db
    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert 2 products
    val List(chocolateId, whiskyId) = Product.description.insert("Expensive Chocolate", "Cheap Whisky").eids

    // Insert nested data .................................

    // We don't necessarily have to assert a fact of the initial namespace
    val List(order, l1, l2) = m(Order.LineItems * LineItem.product.price.quantity) insert List((chocolateId, 48.00, 1), (whiskyId, 38.00, 2)) eids

    order.touch === Map(
      ":db/id" -> order,
      ":Order/lineItems" -> List(Map(
        ":db/id" -> l1,
        ":LineItem/price" -> 48.0,
        ":LineItem/product" -> Map(
          ":db/id" -> chocolateId,
          ":Product/description" -> "Expensive Chocolate"),
        ":LineItem/quantity" -> 1), Map(
        ":db/id" -> l2,
        ":LineItem/price" -> 38.0,
        ":LineItem/product" -> Map(
          ":db/id" -> whiskyId,
          ":Product/description" -> "Cheap Whisky"),
        ":LineItem/quantity" -> 2)))

    // We can get the created order entity id and its lineItem's data
    m(Order.e.LineItems * LineItem.product.price.quantity).get === Seq(
      (order, Seq((chocolateId, 48.00, 1), (whiskyId, 38.00, 2)))
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
    val List(orderId, l1, l2) = order.insert(23, List((chocolateId, 48.00, 1), (whiskyId, 38.00, 2))).eids

    // Find id of order with chocolate
    Order.e.LineItems.Product.description_("Expensive Chocolate").get.head === orderId


    // Touch entity ................................

    // Get all attributes/values of this entity. Sub-component values are recursively retrieved
    orderId.touch === Map(
      ":db/id" -> orderId,
      ":Order/lineItems" -> List(
        Map(":db/id" -> l1, ":LineItem/price" -> 48.0, ":LineItem/product" ->
          Map(":db/id" -> chocolateId, ":Product/description" -> "Expensive Chocolate"), ":LineItem/quantity" -> 1),
        Map(":db/id" -> l2, ":LineItem/price" -> 38.0, ":LineItem/product" ->
          Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"), ":LineItem/quantity" -> 2)),
      ":Order/orderid" -> 23)

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
      ":db/id" -> order23,
      ":Order/lineItems" -> List(
        Map(":db/id" -> l1, ":LineItem/price" -> 48.0, ":LineItem/product" ->
          Map(":db/id" -> chocolateId, ":Product/description" -> "Expensive Chocolate"), ":LineItem/quantity" -> 1),
        Map(":db/id" -> l2, ":LineItem/price" -> 38.0, ":LineItem/product" ->
          // Whisky _is_ touched although it has no quantity asserted!
          Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky")),
        Map(":db/id" -> l3, ":LineItem/price" -> 77.0, ":LineItem/product" ->
          Map(":db/id" -> licoriceId, ":Product/description" -> "Licorice"), ":LineItem/quantity" -> 2)),
      ":Order/orderid" -> 23)

    order24.touch === Map(
      ":db/id" -> order24,
      ":Order/lineItems" -> List(
        Map(":db/id" -> ll1, ":LineItem/price" -> 38.0, ":LineItem/product" ->
          Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"), ":LineItem/quantity" -> 3),
        Map(":db/id" -> ll2, ":LineItem/price" -> 77.0, ":LineItem/product" ->
          Map(":db/id" -> licoriceId, ":Product/description" -> "Licorice"), ":LineItem/quantity" -> 4)),
      ":Order/orderid" -> 24)


    // Get ................................

    // Get adjacent facts
    m(Order.orderid.LineItems.quantity.price.Product.e.description).get.sortBy(_._1) === List(
      (23, 2, 77.0, licoriceId, "Licorice"),
      // whisky for order 23 is _not_ fetched since it has no quantity asserted!
      (23, 1, 48.0, chocolateId, "Expensive Chocolate"),
      (24, 3, 38.0, whiskyId, "Cheap Whisky"),
      (24, 4, 77.0, licoriceId, "Licorice")
    )

    // Make `quantity` optional (by appending `$`) and get all facts wether quantity is asserted or not.
    // Quantities are then returned as Option[Int]
    m(Order.orderid.LineItems.quantity$.price.Product.e.description).get.sortBy(t => (t._1, t._5)) === List(
      (23, None, 38.0, whiskyId, "Cheap Whisky"),
      (23, Some(1), 48.0, chocolateId, "Expensive Chocolate"),
      (23, Some(2), 77.0, licoriceId, "Licorice"),
      (24, Some(3), 38.0, whiskyId, "Cheap Whisky"),
      (24, Some(4), 77.0, licoriceId, "Licorice")
    )

    // Get nested data
    m(Order.e.orderid.LineItems * LineItem.quantity.price).get === Seq(
      (order23, 23, Seq((1, 48.0), (2, 77.0))),
      (order24, 24, Seq((3, 38.0), (4, 77.0))))

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
    val List(order23, l1, p1, l2, p2, l3, p3, order24, ll1, pp1, ll2, pp2) = order insert List(
      (23, List(
        (48.00, Some(1), "Expensive Chocolate"),
        (38.00, None, "Cheap Whisky"),
        (77.00, Some(2), "Licorice"))),
      (24, List(
        (38.00, Some(3), "Cheap Whisky"),
        (77.00, Some(4), "Licorice")))) eids

    //    val List(order23, order24) = Order.e.orderid.get.sortBy(_._2).map(_._1)

    // Find id of orders containing various products
    Order.e.LineItems.Product.description_("Expensive Chocolate").get === List(order23)
    Order.e.LineItems.Product.description_("Licorice").get === List(order24, order23)


    // Touch ................................

    // Get all attributes/values of this entity. Sub-component values are recursively retrieved
    order23.touch === Map(
      ":db/id" -> order23,
      ":Order/lineItems" -> List(
        Map(":db/id" -> l1, ":LineItem/price" -> 48.0, ":LineItem/product" ->
          Map(":db/id" -> p1, ":Product/description" -> "Expensive Chocolate"), ":LineItem/quantity" -> 1),
        Map(":db/id" -> l2, ":LineItem/price" -> 38.0, ":LineItem/product" ->
          Map(":db/id" -> p2, ":Product/description" -> "Cheap Whisky")),
        Map(":db/id" -> l3, ":LineItem/price" -> 77.0, ":LineItem/product" ->
          Map(":db/id" -> p3, ":Product/description" -> "Licorice"), ":LineItem/quantity" -> 2)),
      ":Order/orderid" -> 23)

    order24.touch === Map(
      ":db/id" -> order24,
      ":Order/lineItems" -> List(
        Map(":db/id" -> ll1, ":LineItem/price" -> 38.0, ":LineItem/product" ->
          Map(":db/id" -> pp1, ":Product/description" -> "Cheap Whisky"), ":LineItem/quantity" -> 3),
        Map(":db/id" -> ll2, ":LineItem/price" -> 77.0, ":LineItem/product" ->
          Map(":db/id" -> pp2, ":Product/description" -> "Licorice"), ":LineItem/quantity" -> 4)),
      ":Order/orderid" -> 24)

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
    val List(o1, l1, c1, c2, l2, c3, c4, c5) = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * Comment.text) insert List(
      (23, List(
        (chocolateId, 48.00, 1, List("first", "product")),
        (whiskyId, 38.00, 2, List("second", "is", "best"))
      ))
    ) eids

    // Order lines with correct products added
    Order(o1).LineItems.product.get === List(chocolateId, whiskyId)

    // Order line comments are correct
    LineItem.product_(chocolateId).Comments.text.get === List("first", "product")

    // 2 levels of nested data entered
    o1.touch === Map(
      ":db/id" -> o1,
      ":Order/lineItems" -> List(
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c1, ":Comment/text" -> "first"),
          Map(":db/id" -> c2, ":Comment/text" -> "product")),
          ":LineItem/price" -> 48.0, ":LineItem/quantity" -> 1, ":LineItem/product" ->
            Map(":db/id" -> chocolateId, ":Product/description" -> "Expensive Chocolate"),
          ":db/id" -> l1),
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c3, ":Comment/text" -> "second"),
          Map(":db/id" -> c4, ":Comment/text" -> "is"),
          Map(":db/id" -> c5, ":Comment/text" -> "best")),
          ":LineItem/price" -> 38.0,
          ":LineItem/quantity" -> 2,
          ":LineItem/product" ->
            Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"),
          ":db/id" -> l2)),
      ":Order/orderid" -> 23)

    //    eid.touch === Map(
    //      ":db/id" -> 17592186045422L,
    //      ":Order/lineItems" -> List(
    //        Map(":LineItem/comments" -> List(
    //          Map(":db/id" -> 17592186045424L, ":Comment/text" -> "first"),
    //          Map(":db/id" -> 17592186045425L, ":Comment/text" -> "product")),
    //          ":LineItem/price" -> 48.0, ":LineItem/quantity" -> 1, ":LineItem/product" ->
    //            Map(":db/id" -> 17592186045419L, ":Product/description" -> "Expensive Chocolate"),
    //          ":db/id" -> 17592186045423L),
    //        Map(":LineItem/comments" -> List(
    //          Map(":db/id" -> 17592186045427L, ":Comment/text" -> "second"),
    //          Map(":db/id" -> 17592186045428L, ":Comment/text" -> "is"),
    //          Map(":db/id" -> 17592186045429L, ":Comment/text" -> "best")),
    //          ":LineItem/price" -> 38.0,
    //          ":LineItem/quantity" -> 2,
    //          ":LineItem/product" ->
    //            Map(":db/id" -> 17592186045420L, ":Product/description" -> "Cheap Whisky"),
    //          ":db/id" -> 17592186045426L)),
    //      ":Order/orderid" -> 23)


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
    val List(o1, l1, c1, c2, l2, c3, c4, c5) = Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * Comment.text.descr) insert List(
      (23, List(
        (chocolateId, 48.00, 1, List(
          ("first", "1a"),
          ("product", "1b"))),
        (whiskyId, 38.00, 2, List(
          ("second", "2b"),
          ("is", "2b"),
          ("best", "2c")))
      ))
    ) eids

    // 2 levels of nested data entered
    o1.touch === Map(
      ":db/id" -> o1,
      ":Order/lineItems" -> List(
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c1, ":Comment/descr" -> "1a", ":Comment/text" -> "first"),
          Map(":db/id" -> c2, ":Comment/descr" -> "1b", ":Comment/text" -> "product")),
          ":LineItem/price" -> 48.0,
          ":LineItem/quantity" -> 1,
          ":LineItem/product" ->
            Map(":db/id" -> chocolateId, ":Product/description" -> "Expensive Chocolate"),
          ":db/id" -> l1),
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c3, ":Comment/descr" -> "2b", ":Comment/text" -> "second"),
          Map(":db/id" -> c4, ":Comment/descr" -> "2b", ":Comment/text" -> "is"),
          Map(":db/id" -> c5, ":Comment/descr" -> "2c", ":Comment/text" -> "best")),
          ":LineItem/price" -> 38.0,
          ":LineItem/quantity" -> 2,
          ":LineItem/product" ->
            Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"),
          ":db/id" -> l2)),
      ":Order/orderid" -> 23)

    //    orderId.touch === Map(
    //      ":db/id" -> 17592186045422L,
    //      ":Order/lineItems" -> List(
    //        Map(":LineItem/comments" -> List(
    //          Map(":db/id" -> 17592186045424L, ":Comment/descr" -> "1a", ":Comment/text" -> "first"),
    //          Map(":db/id" -> 17592186045425L, ":Comment/descr" -> "1b", ":Comment/text" -> "product")),
    //          ":LineItem/price" -> 48.0,
    //          ":LineItem/quantity" -> 1,
    //          ":LineItem/product" ->
    //            Map(":db/id" -> 17592186045419L, ":Product/description" -> "Expensive Chocolate"),
    //          ":db/id" -> 17592186045423L),
    //        Map(":LineItem/comments" -> List(
    //          Map(":db/id" -> 17592186045427L, ":Comment/descr" -> "2b", ":Comment/text" -> "second"),
    //          Map(":db/id" -> 17592186045428L, ":Comment/descr" -> "2b", ":Comment/text" -> "is"),
    //          Map(":db/id" -> 17592186045429L, ":Comment/descr" -> "2c", ":Comment/text" -> "best")),
    //          ":LineItem/price" -> 38.0,
    //          ":LineItem/quantity" -> 2,
    //          ":LineItem/product" ->
    //            Map(":db/id" -> 17592186045420L, ":Product/description" -> "Cheap Whisky"),
    //          ":db/id" -> 17592186045426L)),
    //      ":Order/orderid" -> 23)


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
    val List(o1, l1, c1, a1, c2, a2, l2, c3, a3, a4, c4, a5, c5, a6) =
      Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name)) insert List(
        (23, List(
          (chocolateId, 48.00, 1, List(
            ("first", "1a", List("Marc Grue")),
            ("product", "1b", List("Marc Grue")))),
          (whiskyId, 38.00, 2, List(
            ("second", "2b", List("Don Juan", "Stuart Halloway")),
            ("is", "2b", List("Nick Smith")),
            ("best", "2c", List("test"))))
        ))
      ) eids

    /* 3 levels of nested data entered*/
    o1.touch === Map(
      ":db/id" -> o1,
      ":Order/lineItems" -> List(
        Map(
          ":LineItem/comments" -> List(
            Map(
              ":db/id" -> c1, ":Comment/authors" -> List(
                Map(":db/id" -> a1, ":Person/name" -> "Marc Grue")),
              ":Comment/descr" -> "1a",
              ":Comment/text" -> "first"),
            Map(":db/id" -> c2, ":Comment/authors" -> List(
              Map(":db/id" -> a2, ":Person/name" -> "Marc Grue")),
              ":Comment/descr" -> "1b",
              ":Comment/text" -> "product")),
          ":LineItem/price" -> 48.0,
          ":LineItem/quantity" -> 1,
          ":LineItem/product" ->
            Map(":db/id" -> chocolateId, ":Product/description" -> "Expensive Chocolate"),
          ":db/id" -> l1),
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c3, ":Comment/authors" -> List(
            Map(":db/id" -> a3, ":Person/name" -> "Don Juan"),
            Map(":db/id" -> a4, ":Person/name" -> "Stuart Halloway")),
            ":Comment/descr" -> "2b",
            ":Comment/text" -> "second"),
          Map(":db/id" -> c4, ":Comment/authors" -> List(
            Map(":db/id" -> a5, ":Person/name" -> "Nick Smith")),
            ":Comment/descr" -> "2b",
            ":Comment/text" -> "is"),
          Map(":db/id" -> c5, ":Comment/authors" -> List(
            Map(":db/id" -> a6, ":Person/name" -> "test")),
            ":Comment/descr" -> "2c",
            ":Comment/text" -> "best")),
          ":LineItem/price" -> 38.0,
          ":LineItem/quantity" -> 2,
          ":LineItem/product" ->
            Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"),
          ":db/id" -> l2)),
      ":Order/orderid" -> 23)

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
    val List(o1, l1, c1, a1, c2, c3) =
      Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr.Authors * Person.name)) insert List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("second", "2b", List("Don Juan")),
          ("is", "2b", Nil),
          ("best", "2c", List())))
      ))
    ) eids

    // 3 levels of nested data entered, some with missing values that are then not asserted
    o1.touch === Map(
      ":db/id" -> o1,
      ":Order/lineItems" -> List(
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c1, ":Comment/authors" -> List(
            Map(":db/id" -> a1, ":Person/name" -> "Don Juan")),
            ":Comment/descr" -> "2b",
            ":Comment/text" -> "second"),
          Map(":db/id" -> c2, ":Comment/descr" -> "2b", ":Comment/text" -> "is"),
          Map(":db/id" -> c3, ":Comment/descr" -> "2c", ":Comment/text" -> "best")),
          ":LineItem/price" -> 38.0,
          ":LineItem/quantity" -> 2,
          ":LineItem/product" ->
            Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"),
          ":db/id" -> l1)),
      ":Order/orderid" -> 23)

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
    val List(o1, l1, c1, a1, c2, a2) =
      Order.orderid.LineItems * (LineItem.product.price.quantity.Comments * (Comment.text.descr$.Authors * Person.name)) insert List(
      (23, List(
        (whiskyId, 38.00, 2, List(
          ("second", None, List("Don Juan")),
          ("chance", Some("foo"), List("Marc"))
        ))
      ))
    ) eids

    // 3 levels of nested data entered, some with missing values that are then not asserted
    o1.touch === Map(
      ":db/id" -> o1,
      ":Order/lineItems" -> List(
        Map(":LineItem/comments" -> List(
          Map(":db/id" -> c1, ":Comment/authors" -> List(
            Map(":db/id" -> a1, ":Person/name" -> "Don Juan")),
            ":Comment/text" -> "second"),
          Map(":db/id" -> c2, ":Comment/authors" -> List(
            Map(":db/id" -> a2, ":Person/name" -> "Marc")),
            ":Comment/descr" -> "foo",
            ":Comment/text" -> "chance")),
          ":LineItem/price" -> 38.0,
          ":LineItem/quantity" -> 2,
          ":LineItem/product" ->
            Map(":db/id" -> whiskyId, ":Product/description" -> "Cheap Whisky"),
          ":db/id" -> l1)),
      ":Order/orderid" -> 23)

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


  "Corner case with flat card-many + nested" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)

    // Insert product
    val whiskyId = Product.description.insert("Cheap Whisky").eid

    // We can use an optional attribute (`Comment.descr$`) for missing values
    val orderId = Order.orderid.LineItems * (LineItem.product.price.quantity.text.Comments * (Comment.text.descr$.Authors * Person.name)) insert List(
      (23, List(
        (whiskyId, 38.00, 2, "in stock", List(
          ("second", None, List("Don Juan")),
          ("chance", Some("foo"), List("Marc"))
        ))
      ))
    ) eid

    // Combining flat card-many (LineItems.text) + nested
    Order.orderid_(23).LineItems.text
      .Comments.*(Comment.text
      .Authors.*(Person.name
    )).get === List(
      ("in stock", List(
        ("second", List("Don Juan")),
        ("chance", List("Marc"))))
    )

    Order.orderid_(23).LineItems.text
      .Comments.*(Comment.text
      .Authors.*(Person.name
    )).getJson ===
      """[
        |{"lineItems.LineItem.text": "in stock", "LineItem.comments": [
        |   {"Comment.text": "second", "Comment.authors": [
        |      {"Person.name": "Don Juan"}]},
        |   {"Comment.text": "chance", "Comment.authors": [
        |      {"Person.name": "Marc"}]}]}
        |]""".stripMargin
  }
}
