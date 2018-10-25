package molecule.examples
import molecule.api.in1_out4._
import molecule.examples.dayOfDatomic.schema.ProductsOrderSchema
import molecule.util.MoleculeSpec


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {

    implicit val conn = recreateDbFrom(ProductsOrderSchema)




    ok
  }
}
