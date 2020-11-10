package molecule.coretests.attrMap

import java.net.URI
import java.util.{Date, UUID}
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.peer.api.out2._

class Base extends CoreSpec {

  class Setup extends CoreSetup {

    val en    = "en"
    val da    = "da"
    val fr    = "fr"
    val it    = "it"
    val Hi    = "Hi"
    val He    = "He"
    val Bo    = "Bo"
    val regEx = "Hi|He"

    val i10 = 10
    val i20 = 20
    val i30 = 30

    Ns.int.strMap insert List(
      (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno")),
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej")),
      (5, Map[String, String]())
    )
    Ns.int.intMap insert List(
      (1, Map("en" -> 10, "da" -> 30)),
      (2, Map("en" -> 10, "da" -> 10, "fr" -> 20, "it" -> 30)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30)),
      (5, Map[String, Int]())
    )
    Ns.int.longMap insert List(
      (1, Map("en" -> 10L, "da" -> 30L)),
      (2, Map("en" -> 10L, "da" -> 10L, "fr" -> 20L, "it" -> 30L)),
      (3, Map("en" -> 30L, "da" -> 30L)),
      (4, Map("da" -> 30L)),
      (5, Map[String, Long]())
    )
    Ns.int.floatMap insert List(
      (1, Map("en" -> 10f, "da" -> 30f)),
      (2, Map("en" -> 10f, "da" -> 10f, "fr" -> 20f, "it" -> 30f)),
      (3, Map("en" -> 30f, "da" -> 30f)),
      (4, Map("da" -> 30f)),
      (5, Map[String, Float]())
    )
    Ns.int.doubleMap insert List(
      (1, Map("en" -> 10.0, "da" -> 30.0)),
      (2, Map("en" -> 10.0, "da" -> 10.0, "fr" -> 20.0, "it" -> 30.0)),
      (3, Map("en" -> 30.0, "da" -> 30.0)),
      (4, Map("da" -> 30.0)),
      (5, Map[String, Double]())
    )
    Ns.int.boolMap insert List(
      (1, Map("en" -> true, "da" -> false)),
      (2, Map("en" -> true, "da" -> true, "fr" -> false, "it" -> false)),
      (3, Map("en" -> false, "en" -> false)),
      (4, Map("da" -> false)),
      (5, Map[String, Boolean]())
    )
    Ns.int.dateMap insert List(
      (1, Map("en" -> date1, "da" -> date3)),
      (2, Map("en" -> date1, "da" -> date1, "fr" -> date2, "it" -> date3)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3)),
      (5, Map[String, Date]())
    )
    Ns.int.uuidMap insert List(
      (1, Map("en" -> uuid1, "da" -> uuid3)),
      (2, Map("en" -> uuid1, "da" -> uuid1, "fr" -> uuid2, "it" -> uuid3)),
      (3, Map("en" -> uuid3, "da" -> uuid3)),
      (4, Map("da" -> uuid3)),
      (5, Map[String, UUID]())
    )
    Ns.int.uriMap insert List(
      (1, Map("en" -> uri1, "da" -> uri3)),
      (2, Map("en" -> uri1, "da" -> uri1, "fr" -> uri2, "it" -> uri3)),
      (3, Map("en" -> uri3, "da" -> uri3)),
      (4, Map("da" -> uri3)),
      (5, Map[String, URI]())
    )
  }
}