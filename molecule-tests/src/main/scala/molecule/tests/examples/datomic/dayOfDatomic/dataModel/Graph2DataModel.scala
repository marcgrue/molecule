package molecule.tests.examples.datomic.dayOfDatomic.dataModel

import molecule.core.data.model._

@InOut(2, 4)
object Graph2DataModel {

  trait User {
    val name        = oneString.uniqueIdentity
    val roleInGroup = many[RoleInGroup]
  }

  trait RoleInGroup {
    val name  = oneString
    val roles = many[Role]
    val group = one[Group]
  }

  trait Group {
    val name  = oneString.uniqueIdentity
    val roles = many[Role]
  }

  trait Role {
    val name = oneString.uniqueIdentity
  }
}