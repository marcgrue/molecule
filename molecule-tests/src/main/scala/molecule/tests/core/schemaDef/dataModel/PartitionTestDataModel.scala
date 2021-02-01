package molecule.tests.core.schemaDef.dataModel

import molecule.core._1_dataModel.data.model._

@InOut(0, 4)
object PartitionTestDataModel {

  object gen {
    trait Profession {
      val name = oneString
    }

    trait Person {
      val name        = oneString
      val gender      = oneEnum("male", "female")
      val professions = many[Profession]
    }
  }

  object lit {
    trait Book {
      val title     = oneString
      val author    = one[gen.Person]
      // To avoid attr/partition name clashes we can prepend the definition object name
      // (in case we would have needed an attribute named `gen` for instance)
      val editor    = one[gen.Person]
      val cat       = oneEnum("good", "bad")
      val reviewers = many[gen.Person]
    }
  }
}