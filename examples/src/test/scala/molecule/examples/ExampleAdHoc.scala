package molecule.examples
import java.util
import java.util.{List => jList, Map => jMap}
import clojure.lang.Keyword
import molecule.datomic.api.out3._
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.core.util.MoleculeSpec
import molecule.datomic.peer.facade.Datomic_Peer._
import datomic.Util._
import molecule.datomic.client.devLocal.facade.Datomic_DevLocal
import scala.jdk.CollectionConverters._


class ExampleAdHoc extends MoleculeSpec {


  "example adhoc" >> {



    println(AggregatesSchema.namespaces.getClass)

    val nss = AggregatesSchema.namespaces.asInstanceOf[jList[jMap[Any, Any]]]

    println(nss)

    val nss2 = new util.ArrayList[jMap[Any, Any]]()

    nss.forEach { map =>
      nss2.add(
        map.asScala.filterNot(_._1 == read(":db/index")).asJava
      )
    }

    println(nss2)



      ok
  }
}
