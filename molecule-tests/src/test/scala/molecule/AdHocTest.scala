package molecule

import java.util.Date
import molecule.core.ast.elements._
import molecule.core.composition.Tx
//import molecule.datomic.api.in1_out2.m
import molecule.tests.core.bidirectionals.schema.BidirectionalSchema
//import molecule.core.dsl.base.{Init, NS_0_01}
import molecule.core.dsl.{attributes, base}
import molecule.core.macros.ObjBuilder
import molecule.core.util.Helpers
import molecule.core.util.testing.TxCount.schema.TxCountSchema
import molecule.datomic.api.in3_out10._
import molecule.datomic.base.facade.Conn
import molecule.datomic.base.transform.Model2Query
import molecule.datomic.peer.facade.Datomic_Peer
import scala.reflect.runtime.universe._
//import molecule.tests.core.base.dsl.CoreTest._
import molecule.tests.core.base.schema.CoreTestSchema
//import molecule.tests.examples.gremlin.gettingStarted.dsl.ModernGraph2.Person
import org.specs2.mutable.Specification


//class AdHocTest extends molecule.setup.TestSpec with Helpers {
class AdHocTest extends Specification {


  //  "Simple hyperedge" in new GraphSetup {
  //
  //    import molecule.tests.examples.datomic.dayOfDatomic.dsl.Graph._
  //
  //    // User 1 Roles in Group 2
  //    User.name_("User1")
  //      .RoleInGroup.Group.name_("Group2")
  //      ._RoleInGroup.Role.name.inspectGet
  //
  //
  //    ok
  //  }
  //
  //    "adhoc" in new BidirectionalSetup {
  //      import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //      Person.name("Ann").Buddies.e(gus)
  //    }

  //    "adhoc" >> {
  //      import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //      implicit val conn: Conn = Datomic_Peer.recreateDbFrom(BidirectionalSchema)
  //
  //      val o = Person.name("Ann").Buddies.e(42L).getObj
  //
  //      o.name
  //      o.Buddies.e
  //
  //      ok
  //    }
  //
  //
  //
  //
  //  "adhoc" in new CoreSetup {
  //    import molecule.tests.core.base.dsl.CoreTest._
  //

  //  }
  //
  //  "Insert resolves to correct partitions" in new PartitionSetup {
  //    import molecule.tests.core.schemaDef.dsl.PartitionTest._
  //    m(lit_Book.title.Author.name._lit_Book.Reviewers * gen_Person.name).inspectGet
  //  }
  //
  //
  //  "Insert resolves to correct partitions" in new ModernGraph2Setup {
  //
  //    import molecule.tests.examples.gremlin.gettingStarted.dsl.ModernGraph2._
  //
  //    m(Person.name.Knows * Person.name).inspectGet
  //  }
  //
  //
  //    "txCount" >> {
  //
  //      import molecule.core.util.testing.TxCount._
  //      implicit val conn: Conn = Datomic_Peer.recreateDbFrom(TxCountSchema)
  //      TxCount.db("x").basisT(42L).save
  //
  //      ok
  //    }

  "adhoc" >> {
    import molecule.tests.core.base.dsl.CoreTest._
//    import molecule.tests.core.base.dsl.CoreTest.Ns
    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(CoreTestSchema)

//    Schema.part.get
//    Ns.str.getObj

//    Ns.int.ints$ insert List(
//      (10, Some(Set(1, 2))),
//      (20, Some(Set(2, 3))),
//      (30, Some(Set(3, 4))),
//      (40, None)
//    )
//
//    // Apply empty value to match entities with non-asserted attributes (null)
//    Ns.int.ints_().get === List(40)
//
//    // Same as applying empty Iterables
//    Ns.int.ints_(Nil).get === List(40)
//    Ns.int.ints_(List()).get === List(40)
//
//    // Applying empty value to mandatory attribute is contradictive and never matches entities.
//    Ns.int.ints().get === Nil
//
//    // Applying possibly empty list as variable simply yields empty result set
//    val emptyList = Nil
//    Ns.ints(emptyList).get === Nil
//
//    // Apply Nil to tacit attribute of input molecule
//    m(Ns.int.ints_(?)).apply(Nil).get === List(40)
//
//    // Apply Nil to mandatory attribute of input molecule never matches any entities
//    m(Ns.int.ints(?)).apply(Nil).get === Nil

    ok
  }
  //
  //
  //  "A first query" in new SeattleSetup {
  //    import molecule.tests.examples.datomic.seattle.dsl.Seattle._
  //
  //    // A Community-name molecule
  //    val communities = m(Community. e.name_)
  //
  //    // We have 150 communities
  //    communities.get.size === 150
  //  }

  //  "adhoc" in new BidirectionalSetup {
  //import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //
  //  }

  //  "adhoc" in new PartitionSetup {

  //  }


  //  "example adhoc" in new SeattleSetup {
  //
  //
  //    //    ok
  //  }


  //  "example adhoc" in new SocialNewsSetup {
  //
  //
  //
  //    ok
  //  }
}
//