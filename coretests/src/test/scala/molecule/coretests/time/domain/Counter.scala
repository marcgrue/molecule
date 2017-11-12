package molecule.coretests.time.domain

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest.Ns
import molecule.facade.Conn

// Example domain class getting and manipulating the db with molecules
// Molecules need an implicit conn object at runtime
case class Counter(eid: Long) {

  // Live value
  def value(implicit conn: Conn): Int = {
    Ns(eid).int.get.head
  }

  // Mutate domain data
  def incr(implicit conn: Conn): Int = {
    val next = value + 1
    Ns(eid).int(next).update
    next
  }
}