package molecule.examples

import java.util
import java.util.{List => jList, Map => jMap}
import clojure.lang.Keyword
import molecule.datomic.api.out3._
import molecule.examples.dayOfDatomic.dsl.aggregates.{Data, Obj}
import molecule.examples.dayOfDatomic.dsl.graph.User
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema}
import molecule.datomic.peer.facade.Datomic_Peer._
import datomic.Util._
import molecule.datomic.api.in2_out8.m
import molecule.examples.ExampleSpec
import molecule.datomic.client.facade.Datomic_Client
import molecule.examples.seattle.dsl.seattle.Community
import scala.jdk.CollectionConverters._


class ExampleAdHoc extends ExampleSpec {

  devLocalOnly = true


  "example adhoc" in new SeattleSetup {


    val typeAndOrgtype2 = m(Community.name.`type`(?).orgtype(?))


    // Logic AND pairs separated by OR
    typeAndOrgtype2.apply(("email_list" and "community") or ("website" and "commercial")).get(5)

    ok
  }
}
