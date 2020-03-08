package molecule.examples
import molecule.api.in1_out4._
import molecule.api.out3.recreateDbFrom
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.util.MoleculeSpec


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {


    ok
  }
}
