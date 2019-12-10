package molecule.coretests

import molecule.api.in1_out3._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.Helpers

class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {

    val List(r20, r21, r22) = Ref2.int2.insert(20, 21, 22).eids

    val refs1 = Ref1.str1.int1$.refs2$ insert List(
      ("aa", Some(10), Some(Set(r20, r21))),
      ("bb", None, Some(Set(r22))),
      ("cc", Some(11), None),
    ) eidSet

    Ns.str.int.refs1$ insert List(
      ("a", 1, Some(refs1)),
      ("b", 2, None),
    )

    Ns.str.Refs1.*?(Ref1.str1.int1$).get === List(
      ("a", List(("aa", Some(10)), ("bb", None), ("cc", Some(11)))),
      ("b", List())
    )

    Ns.str.Refs1.*?(Ref1.str1.int1$.Refs2.*?(Ref2.int2)).get === List(
      ("a", List(
        ("aa", Some(10), List(20, 21)),
        ("bb", None, List(22)),
        ("cc", Some(11), List()))),
      ("b", List())
    )


    //    conn.q(
    //      """[:find  ?f (pull ?d_track [{:track/artists [:db/id :artist/name]}])
    //        | :in $ [?pos ...]
    //        | :where [?a :Release/name "Dylan–Harrison Sessions"]
    //        |        [?a :Release/media ?c]
    //        |        [?c :Medium/tracks ?d]
    //        |        [?d :Track/position ?pos]
    //        |        [?d :Track/name ?f]
    //        |        [(molecule.util.fns/bind ?d) ?d_track]]""".stripMargin,
    //      datomic.Util.list(
    //        10.asInstanceOf[Object],
    //        11.asInstanceOf[Object],
    //        42.asInstanceOf[Object]
    //      )
    //    ).toString ===
    //      """List(List(One Too Many Mornings, {:track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}), """ +
    //        """List(Ghost Riders in the Sky, {:track/artists [{:db/id 646512837145512, :artist/name "George Harrison"} {:db/id 721279627832670, :artist/name "Bob Dylan"}]}))"""

    ok
  }
}