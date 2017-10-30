package molecule.examples.seattle

import molecule._
import molecule.examples.seattle.dsl.seattle._


import scala.language.reflectiveCalls

class TakeTests extends SeattleSpec {


  "take" >> {

    Community.name.url.category.get(5) foreach println
//    println(Community.name.url.json)


    ok
  }
}