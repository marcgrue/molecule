package molecule.examples
import molecule.datomic.api.out3._
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.core.util.MoleculeSpec
import molecule.datomic.peer.facade.Datomic_Peer._


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {


    ok
  }
}
