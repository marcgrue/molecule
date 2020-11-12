package molecule.coretests.bidirectionals.edgeOther

import molecule.core.util._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.datomic.api.in1_out3._

class EdgeOneOtherSave extends MoleculeSpec {

  class setup extends Setup {
    val favoriteAnimalOf = m(Person.name_(?).Favorite.weight.Animal.name)
    val favoritePersonOf = m(Animal.name_(?).Favorite.weight.Person.name)
  }


  "base/edge/target" >> {

    "new target" in new setup {

      Person.name("Ann").Favorite.weight(7).Animal.name("Rex").save.eids

      // Bidirectional property edges have been saved
      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }


    "existing target" in new setup {

      val rex = Animal.name.insert("Rex").eid

      // Save Ann with weighed relationship to existing Rex
      Person.name("Ann").Favorite.weight(7).animal(rex).save.eids

      // Ann and Rex each others favorite with a weight of 7
      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }
  }


  "base + edge/target" >> {

    "new target" in new setup {

      val favoriteRex = Favorite.weight(7).Animal.name("Rex").save.eid

      Person.name("Ann").favorite(favoriteRex).save.eid

      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }

    "existing target" in new setup {

      val rex = Animal.name.insert("Rex").eid

      val favoriteRex = Favorite.weight(7).animal(rex).save.eid

      Person.name("Ann").favorite(favoriteRex).save

      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }
  }
}
