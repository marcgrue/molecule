package molecule.tests.core.bidirectionals.edgeOther

import molecule.tests.core.bidirectionals.dsl.Bidirectional._
import molecule.datomic.api.in1_out3._
import molecule.setup.TestSpec

class EdgeOneOtherSave extends TestSpec {

  class Setup extends BidirectionalSetup {
    val favoriteAnimalOf = m(Person.name_(?).Favorite.weight.Animal.name)
    val favoritePersonOf = m(Animal.name_(?).Favorite.weight.Person.name)
  }


  "base/edge/target" >> {

    "new target" in new Setup {

      Person.name("Ann").Favorite.weight(7).Animal.name("Rex").save.eids

      // Bidirectional property edges have been saved
      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }


    "existing target" in new Setup {

      val rex = Animal.name.insert("Rex").eid

      // Save Ann with weighed relationship to existing Rex
      Person.name("Ann").Favorite.weight(7).animal(rex).save.eids

      // Ann and Rex each others favorite with a weight of 7
      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }
  }


  "base + edge/target" >> {

    "new target" in new Setup {

      val favoriteRex = Favorite.weight(7).Animal.name("Rex").save.eid

      Person.name("Ann").favorite(favoriteRex).save.eid

      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }

    "existing target" in new Setup {

      val rex = Animal.name.insert("Rex").eid

      val favoriteRex = Favorite.weight(7).animal(rex).save.eid

      Person.name("Ann").favorite(favoriteRex).save

      favoriteAnimalOf("Ann").get === List((7, "Rex"))
      favoritePersonOf("Rex").get === List((7, "Ann"))
    }
  }
}
