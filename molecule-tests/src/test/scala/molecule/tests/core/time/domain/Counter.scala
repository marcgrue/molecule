package molecule.tests.core.time.domain

import molecule.tests.core.base.dsl.CoreTest.Ns
import molecule.datomic.api.out1._
import molecule.datomic.base.facade.Conn

// Example domain class getting and manipulating the db with molecules
// Molecules need an implicit conn object at runtime
case class Counter(eid: Long) {

  // Live value
  def value(implicit conn: Conn): Int = {
    Ns(eid).int.get.head
  }

  // Mutate domain data
  def incr(implicit conn: Conn): Int = {
    val next = value + 10
    Ns(eid).int(next).update
    next
  }
}