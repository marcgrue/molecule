package molecule.examples
import molecule.datomic.peer.api._
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.util.MoleculeSpec


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {


    ok
  }
}
