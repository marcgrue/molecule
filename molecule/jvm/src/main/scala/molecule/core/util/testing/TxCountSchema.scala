/*
* AUTO-GENERATED Molecule DSL schema boilerplate code
*
* To change:
* 1. edit schema definition file in `molecule.tests.core.base.schema/`
* 2. `sbt compile` in terminal
* 3. Refresh and re-compile project in IDE
*/
package molecule.core.util.testing

import datomic.Util._
import molecule.core.data.SchemaTransaction

object TxCountSchema extends SchemaTransaction {

  lazy val partitions = list()


  lazy val namespaces = list(

    // TxCount ----------------------------------------------------------

    map(read(":db/ident")             , read(":TxCount/db"),
        read(":db/valueType")         , read(":db.type/string"),
        read(":db/cardinality")       , read(":db.cardinality/one"),
        read(":db/doc")               , "Database observed.",
        read(":db/index")             , true.asInstanceOf[Object]),

    map(read(":db/ident")             , read(":TxCount/basisT"),
        read(":db/valueType")         , read(":db.type/long"),
        read(":db/cardinality")       , read(":db.cardinality/one"),
        read(":db/doc")               , "Holder of current db basis t for peer-server tests",
        read(":db/index")             , true.asInstanceOf[Object])
  )
}