package molecule.examples
import molecule.api.in1_out4._
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{GraphSchema, ProductsOrderSchema}
import molecule.util.MoleculeSpec


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {

//    implicit val conn = recreateDbFrom(ProductsOrderSchema)
    //    implicit val conn = recreateDbFrom(GraphSchema)


    ok
  }



}
