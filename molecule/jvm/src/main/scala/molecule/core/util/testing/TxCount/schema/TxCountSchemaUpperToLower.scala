/*
* AUTO-GENERATED Datomic Schema exchange boilerplate code
*
* To change:
* 1. edit schema definition file in `app.schema/`
* 2. `sbt compile` in terminal
* 3. Refresh and re-compile project in IDE
*/
package molecule.core.util.testing.TxCount.schema

import datomic.Util._

object TxCountSchemaUpperToLower {

  lazy val namespaces = list(

    // TxCount ----------------------------------------------------------

    map(read(":db/id"), read(":TxCount/db")    , read(":db/ident"), read(":txCount/db")),
    map(read(":db/id"), read(":TxCount/basisT"), read(":db/ident"), read(":txCount/basisT"))
  )
}