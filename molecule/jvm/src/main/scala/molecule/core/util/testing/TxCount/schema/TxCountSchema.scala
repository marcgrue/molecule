/*
* AUTO-GENERATED Datomic Schema generation boilerplate code
*
* To change:
* 1. edit data model file in `app.dataModel/`
* 2. `sbt compile` in terminal
* 3. Refresh and re-compile project in IDE
*/
package molecule.core.util.testing.TxCount.schema

import molecule.core.data.SchemaTransaction
import datomic.Util._
import datomic.Peer._

object TxCountSchema extends SchemaTransaction {

  lazy val partitions = list()


  lazy val namespaces = list(

    // TxCount ----------------------------------------------------------

    map(read(":db/ident")             , read(":TxCount/db"),
        read(":db/valueType")         , read(":db.type/string"),
        read(":db/cardinality")       , read(":db.cardinality/one"),
        read(":db/doc")               , "Database name",
        read(":db/index")             , true.asInstanceOf[Object]),

    map(read(":db/ident")             , read(":TxCount/basisT"),
        read(":db/valueType")         , read(":db.type/long"),
        read(":db/cardinality")       , read(":db.cardinality/one"),
        read(":db/doc")               , "Datomic basis T",
        read(":db/index")             , true.asInstanceOf[Object])
  )
}