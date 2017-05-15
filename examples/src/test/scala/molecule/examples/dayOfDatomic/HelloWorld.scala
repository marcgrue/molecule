//package molecule.examples.dayOfDatomic
//import datomic.Peer
//import datomic.Util._
//import molecule.util.MoleculeSpec
//import scala.collection.JavaConverters._
//
//
//class HelloWorld extends MoleculeSpec {
//
//  "Hello World" >> {
//
//    // Transaction input is data
//    val tempid = Peer.tempid(":db.part/user")
//    val txData = list(list(
//      ":db/add", tempid,
//      ":db/doc", "Hello world"))
//    val conn = loadList(txData, "hello-world")
//
//    // Transaction result is data
//    val txresult = conn.transact(txData)
//    txresult.get().toString.take(26) === "{:db-before datomic.db.Db@" // etc...
//
//    // Database is a value
//    val dbValue = conn.db()
//
//    // Query input is data
//    val qresult = Peer.q( """[:find ?e :where [?e :db/doc "Hello world"]]""", dbValue)
//
//    // Query result is data
////    val id1: Object  = qresult.asScala.toList.head .head
////    val a: String = qresult.asScala.head.asScala.head
//    val id: Object = qresult.asScala.toList.head.asScala.head
//
//    // Entity is a navigable view over data
//    dbValue.entity(id).get(":db/id") === 17592186045417L
//
//    // Schema itself is data
//    dbValue.entity(":db/doc").get(":db/id") === 62
//  }
//}