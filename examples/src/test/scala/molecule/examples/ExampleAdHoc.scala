package molecule.examples
import molecule.api.in1_out4._
import molecule.api.out3.recreateDbFrom
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.util.MoleculeSpec


class ExampleAdHoc extends MoleculeSpec {



  implicit val conn = recreateDbFrom(AggregatesSchema, "Aggregates")

  val planets  = Seq("Sun", "Jupiter", "Saturn", "Uranus", "Neptune", "Earth", "Venus", "Mars", "Ganymede", "Titan", "Mercury", "Callisto", "Io", "Moon", "Europa", "Triton", "Eris")
  val radiuses = Seq(696000.0, 69911.0, 58232.0, 25362.0, 24622.0, 6371.0, 6051.8, 3390.0, 2631.2, 2576.0, 2439.7, 2410.3, 1821.5, 1737.1, 1561.0, 1353.4, 1163.0)
  val url      = "http://en.wikipedia.org/wiki/List_of_Solar_System_objects_by_size"

  // Insert data with tx meta data
  Obj.name.meanRadius.Tx(Data.source_(url)) insert (planets zip radiuses)


  "example adhoc" >> {

//    implicit val conn = recreateDbFrom(ProductsOrderSchema)
    //    implicit val conn = recreateDbFrom(GraphSchema)

//    Obj.meanRadius(sum).debugGet
    Obj.meanRadius(sum).meanRadius(avg).debugGet
//    Obj.meanRadius(sum).meanRadius(median).debugGet
    Obj.meanRadius(sum).meanRadius(avg).meanRadius(median).debugGet

//    Obj.meanRadius(sum).meanRadius(avg).meanRadius(median).get.head ===
//      (907632.9999999999,53390.17647058823,2631.2)

    conn.q(
      """[:find  (sum ?b) (avg ?c)
        | :with  ?a
        | :where [?a :Obj/meanRadius ?b]
        |        [?a :Obj/meanRadius ?c]]""".stripMargin
    ) foreach println

    println("---------")
    conn.q(
      """[:find  (sum ?b) (avg ?c) (median ?d)
        | :with  ?a
        | :where [?a :Obj/meanRadius ?b]
        |        [?a :Obj/meanRadius ?c]
        |        [?a :Obj/meanRadius ?d]]""".stripMargin
    ) foreach println
    println("---------")
    conn.q(
      """[:find  (sum ?b) (avg ?b) (median ?b)
        | :with  ?a
        | :where [?a :Obj/meanRadius ?b]]""".stripMargin
    ) foreach println

    ok
  }



  "example adhoc" >> {

    //    implicit val conn = recreateDbFrom(ProductsOrderSchema)
    //    implicit val conn = recreateDbFrom(GraphSchema)


    ok
  }

}
