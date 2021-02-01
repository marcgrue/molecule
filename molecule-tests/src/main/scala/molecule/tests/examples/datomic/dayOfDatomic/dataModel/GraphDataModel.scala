package molecule.tests.examples.datomic.dayOfDatomic.dataModel

import molecule.core._1_dataModel.data.model._

@InOut(0, 4)
object GraphDataModel {

  trait User {
    val name        = oneString.uniqueIdentity
    val roleInGroup = many[RoleInGroup]
  }

  trait RoleInGroup {
    val name  = oneString
    val role  = one[Role]
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