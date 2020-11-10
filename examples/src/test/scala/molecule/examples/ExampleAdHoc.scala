package molecule.examples
import molecule.datomic.peer.api.out3._
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.core.util.MoleculeSpec


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {


    ok
  }
}
