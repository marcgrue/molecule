package molecule.tests.examples.datomic.seattle

import molecule.datomic.api.in2_out8._
import molecule.tests.examples.datomic.seattle.dsl.Seattle._
import molecule.TestSpec
import scala.language.reflectiveCalls


class SeattleQueryTests extends TestSpec {


  "A first query" in new SeattleSetup {

    // Query of molecule
    m(Community.name) --> {
      """[:find  ?b
        | :where [?a :Community/name ?b]]""".stripMargin
    }
  }


  "Querying _for_ attribute values" in new SeattleSetup {

    // Multiple attributes
    m(Community.name.url.category) -->
      """[:find  ?b ?c (distinct ?d)
        | :where [?a :Community/name ?b]
        |        [?a :Community/url ?c]
        |        [?a :Community/category ?d]]""".stripMargin
  }


  "Querying _by_ attribute values" in new SeattleSetup {

    // Names of twitter communities
    m(Community.name.type_("twitter")) -->
      """[:find  ?b
        | :where [?a :Community/type :Community.type/twitter]
        |        [?a :Community/name ?b]]""".stripMargin


    // Categories (many-cardinality) of the Belltown community
    m(Community.name_("belltown").category) -->
      """[:find  (distinct ?c)
        | :where [(ground "belltown") ?b]
        |        [?a :Community/name ?b]
        |        [?a :Community/category ?c]]""".stripMargin


    // Names of news or arts communities - transforms to a query using Rules
    m(Community.name.category_("news" or "arts")) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :Community/name ?b]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [?a :Community/category "news"]]
        |     [(rule1 ?a) [?a :Community/category "arts"]]]
        |)""".stripMargin
  }


  "Querying across references" in new SeattleSetup {

    // Communities in north eastern region
    // Ref's are modelled as "Bond"'s (between Atoms)
    m(Community.name.Neighborhood.District.region_("ne")) -->
      """[:find  ?b
        | :where [?d :District/region :District.region/ne]
        |        [?c :Neighborhood/district ?d]
        |        [?a :Community/neighborhood ?c]
        |        [?a :Community/name ?b]]""".stripMargin


    // Communities and their region
    m(Community.name.Neighborhood.District.region) -->
      """[:find  ?b ?e2
        | :where [?a :Community/name ?b]
        |        [?a :Community/neighborhood ?c]
        |        [?c :Neighborhood/district ?d]
        |        [?d :District/region ?e]
        |        [?e :db/ident ?e1]
        |        [(name ?e1) ?e2]]""".stripMargin
  }


  "Advanced queries - parameterizing queries" in new SeattleSetup {

    /** ******* Single input parameter **************************/

    // Community input molecule awaiting some type value
    m(Community.name.type_.apply(?)) -->
      """[:find  ?b
        | :in    $ ?c2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]""".stripMargin

    // Applying a value completes the query
    m(Community.name.type_(?))("twitter") -->
      """[:find  ?b
        | :in    $ ?c
        | :where [?a :Community/type ?c]
        |        [?a :Community/name ?b]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 :Community.type/twitter
        |)""".stripMargin


    // Skip underscore to return the input value too
    m(Community.name.`type`(?)) -->
      """[:find  ?b ?c2
        | :in    $ ?c2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]""".stripMargin


    m(Community.name.`type`(?)).apply("twitter") -->
      """[:find  ?b ?c2
        | :in    $ ?c2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 twitter
        |)""".stripMargin


    // Multiple input values - logical OR ------------------------

    m(Community.name.`type`(?)).apply("facebook_page" or "twitter") -->
      """[:find  ?b ?c2
        | :in    $ [?c2 ...]
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [facebook_page, twitter]
        |)""".stripMargin


    // Finding communities of type "facebook_page" OR "twitter"
    // The following 3 notation variations transform in the same way
    m(Community.name.`type`(?))("facebook_page" or "twitter") -->
      //    m(Community.name.`type`(?))("facebook_page", "twitter") -->
      //    m(Community.name.`type`(?))(List("facebook_page", "twitter")) -->
      """[:find  ?b ?c2
        | :in    $ [?c2 ...]
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [facebook_page, twitter]
        |)""".stripMargin


    /** ******* Multiple input parameters **************************/

    // Single tuple of input values - logical AND ------------------------

    m(Community.name.type_(?).orgtype_(?)) -->
      """[:find  ?b
        | :in    $ ?c2 ?d2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]
        |        [?a :Community/orgtype ?d]
        |        [?d :db/ident ?d1]
        |        [(name ?d1) ?d2]]""".stripMargin


    // The following 3 notation variations transform in the same way

    m(Community.name.type_(?).orgtype_(?))("email_list" and "community") -->
      //    m(Community.name.type_(?).orgtype_(?)).apply("email_list", "community") -->
      //    m(Community.name.type_(?).orgtype_(?)).apply(List(("email_list", "community"))) -->
      """[:find  ?b
        | :in    $ ?c ?d
        | :where [?a :Community/type ?c]
        |        [?a :Community/name ?b]
        |        [?a :Community/orgtype ?d]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 :Community.type/email_list
        |  3 :Community.orgtype/community
        |)""".stripMargin


    // Multiple tuples of input values ------------------------

    // Communities of some `type` AND some `orgtype` (include input values!)
    m(Community.name.`type`(?).orgtype(?)) -->
      """[:find  ?b ?c2 ?d2
        | :in    $ ?c2 ?d2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]
        |        [?a :Community/orgtype ?d]
        |        [?d :db/ident ?d1]
        |        [(name ?d1) ?d2]]""".stripMargin


    // The following 3 notation variations transform in the same way

    //    m(Community.name.`type`(?!).orgtype(?!)).apply(("email_list" and "community") or ("website" and "commercial")) -->
    //    m(Community.name.`type`(?!).orgtype(?!)).apply(("email_list", "community"), ("website", "commercial")) -->
    m(Community.name.`type`(?).orgtype(?)).apply(Seq(("email_list", "community"), ("website", "commercial"))) -->
      """[:find  ?b ?c2 ?d2
        | :in    $ %
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]
        |        (rule1 ?a)
        |        [?a :Community/orgtype ?d]
        |        [?d :db/ident ?d1]
        |        [(name ?d1) ?d2]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a)
        |   [?a :Community/type :Community.type/email_list]
        |   [?a :Community/orgtype :Community.orgtype/community]]
        |     [(rule1 ?a)
        |   [?a :Community/type :Community.type/website]
        |   [?a :Community/orgtype :Community.orgtype/commercial]]]
        |)""".stripMargin
  }


  "Invoking functions in queries" in new SeattleSetup {

    m(Community.name < "C") -->
      """[:find  ?b
        | :where [(.compareTo ^String ?b "C") ?b2]
        |        [(< ?b2 0)]
        |        [?a :Community/name ?b]]""".stripMargin

    m(Community.name < ?) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(.compareTo ^String ?b ?b1) ?b2]
        |        [(< ?b2 0)]
        |        [?a :Community/name ?b]]""".stripMargin

    m(Community.name < ?).apply("C") -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(.compareTo ^String ?b ?b1) ?b2]
        |        [(< ?b2 0)]
        |        [?a :Community/name ?b]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 C
        |)""".stripMargin
  }


  "Querying with fulltext search" in new SeattleSetup {

    m(Community.name contains "Wallingford") -->
      """[:find  ?b
        | :where [(fulltext $ :Community/name "Wallingford") [[ ?a ?b ]]]]""".stripMargin


    m(Community.name contains ?) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(fulltext $ :Community/name ?b1) [[ ?a ?b ]]]]""".stripMargin


    m(Community.name contains ?).apply("Wallingford") -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(fulltext $ :Community/name ?b1) [[ ?a ?b ]]]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 Wallingford
        |)""".stripMargin


    // Fulltext search on many-attribute (`category`)

    m(Community.name.type_("website").category contains "food") -->
      """[:find  ?b (distinct ?d)
        | :in    $ %
        | :where [?a :Community/type :Community.type/website]
        |        [?a :Community/name ?b]
        |        [?a :Community/category ?d]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [(fulltext $ :Community/category "food") [[ ?a ?a_1 ]]]]]
        |)""".stripMargin


    m(Community.name.type_(?).category contains ?) -->
      """[:find  ?b (distinct ?d)
        | :in    $ ?c2 ?d1
        | :where [(fulltext $ :Community/category ?d1) [[ ?a ?d ]]]
        |        [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]""".stripMargin


    m(Community.name.type_(?).category contains ?).apply("website", Set("food")) -->
      """[:find  ?b (distinct ?d)
        | :in    $ % ?c
        | :where [?a :Community/type ?c]
        |        [?a :Community/name ?b]
        |        [?a :Community/category ?d]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [(fulltext $ :Community/category "food") [[ ?a ?d1_1 ]]]]]
        |  3 :Community.type/website
        |)""".stripMargin
  }


  "Querying with rules (logical OR)" in new SeattleSetup {

    m(Community.name.type_("twitter" or "facebook_page")) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :Community/name ?b]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [?a :Community/type :Community.type/twitter]]
        |     [(rule1 ?a) [?a :Community/type :Community.type/facebook_page]]]
        |)""".stripMargin


    m(Community.name.Neighborhood.District.region_("ne" or "sw")) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :Community/name ?b]
        |        [?a :Community/neighborhood ?c]
        |        [?c :Neighborhood/district ?d]
        |        (rule1 ?d)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?d) [?d :District/region :District.region/ne]]
        |     [(rule1 ?d) [?d :District/region :District.region/sw]]]
        |)""".stripMargin


    // Social media in southern regions
    m(Community.name.type_("twitter" or "facebook_page").Neighborhood.District.region_("sw" or "s" or "se")) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :Community/name ?b]
        |        (rule1 ?a)
        |        [?a :Community/neighborhood ?d]
        |        [?d :Neighborhood/district ?e]
        |        (rule2 ?e)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [?a :Community/type :Community.type/twitter]]
        |     [(rule1 ?a) [?a :Community/type :Community.type/facebook_page]]
        |     [(rule2 ?e) [?e :District/region :District.region/sw]]
        |     [(rule2 ?e) [?e :District/region :District.region/s]]
        |     [(rule2 ?e) [?e :District/region :District.region/se]]]
        |)""".stripMargin


    m(Community.name.type_(?).Neighborhood.District.region_(?)) -->
      """[:find  ?b
        | :in    $ ?c2 ?f2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]
        |        [?a :Community/neighborhood ?d]
        |        [?d :Neighborhood/district ?e]
        |        [?e :District/region ?f]
        |        [?f :db/ident ?f1]
        |        [(name ?f1) ?f2]]""".stripMargin


    m(Community.name.type_(?).Neighborhood.District.region_(?)).apply(
      ("twitter" or "facebook_page") and ("sw" or "s" or "se")
      // or
      // Seq("twitter", "facebook_page"), Seq("sw", "s", "se")
    ) -->
      """[:find  ?b
        | :in    $ [?c ...] [?f ...]
        | :where [?a :Community/type ?c]
        |        [?a :Community/name ?b]
        |        [?e :District/region ?f]
        |        [?d :Neighborhood/district ?e]
        |        [?a :Community/neighborhood ?d]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [:Community.type/twitter, :Community.type/facebook_page]
        |  3 [:District.region/sw, :District.region/s, :District.region/se]
        |)""".stripMargin
  }


  "Working with time" in new SeattleSetup {

    m(Schema.txInstant) -->
      """[:find  ?txInstant
        | :where [(= ?sys false)]
        |        [_ :db.install/attribute ?id ?tx]
        |        [?id :db/ident ?idIdent]
        |        [(namespace ?idIdent) ?nsFull]
        |        [(.matches ^String ?nsFull "^(db|db.alter|db.excise|db.install|db.part|db.sys|fressian|db.entity|db.attr|:?-.*)") ?sys]
        |        [(.contains ^String ?nsFull "_") ?isPart]
        |        [(.split ^String ?nsFull "_") ?nsParts]
        |        [(first ?nsParts) ?part0]
        |        [(if ?isPart ?part0 "db.part/user") ?part]
        |        [(last ?nsParts) ?ns]
        |        [?tx :db/txInstant ?txInstant]]""".stripMargin
  }


  "Manipulating data - insert" in new SeattleSetup {

    /** Insert data into molecule and save ***********************************************/

    testInsertMolecule(
      Community
        .name("AAA")
        .url("myUrl")
        .`type`("twitter")
        .orgtype("personal")
        .category("my", "favorites")
        .Neighborhood.name("myNeighborhood")
        .District.name("myDistrict").region("nw")
    ) -->
      //  Some things to notice:
      //  - Enum values are prefixed with their namespace ("nw" becomes ":District.region/nw")
      //  - Multiple values of many-cardinality attributes each get their own statement ("my" + "favorites")
      //
      //           action             temp id                   attribute                 value
      """List(
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/name        ,  AAA                           ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/url         ,  myUrl                         ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/type        ,  :Community.type/twitter       ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/orgtype     ,  :Community.orgtype/personal   ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/category    ,  my                            ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/category    ,  favorites                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/neighborhood,  #db/id[:db.part/user -1000002]),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :Neighborhood/name     ,  myNeighborhood                ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :Neighborhood/district ,  #db/id[:db.part/user -1000003]),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :District/name         ,  myDistrict                    ),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :District/region       ,  :District.region/nw           )
        |)""".stripMargin


    /** Use molecule as template to insert matching data sets ********************************************/

    //    Community.name.url.`type`.orgtype.category.Neighborhood.name.District.name.region insert List(
    //      ("DDD Blogging Georgetown", "http://www.blogginggeorgetown.com/", "blog", "commercial", Set("DD cat 1", "DD cat 2"), "DD Georgetown", "Greater Duwamish", "s"),
    //      ("DDD Interbay District Blog", "http://interbayneighborhood.neighborlogs.com/", "blog", "community", Set("DD cat 3"), "DD Interbay", "Magnolia/Queen Anne", "w")
    //    )

    m(Community.name.url.`type`.orgtype.category.Neighborhood.name.District.name.region) -->
      List(
        List("DDD Blogging Georgetown", "http://www.blogginggeorgetown.com/", "blog", "commercial", Set("DD cat 1", "DD cat 2"), "DD Georgetown", "Greater Duwamish", "s"),
        List("DDD Interbay District Blog", "http://interbayneighborhood.neighborlogs.com/", "blog", "community", Set("DD cat 3"), "DD Interbay", "Magnolia/Queen Anne", "w")
      ) -->
      // Semantically identical to the previous transaction
      """List(
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/name        ,  DDD Blogging Georgetown                      ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/url         ,  http://www.blogginggeorgetown.com/           ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/type        ,  :Community.type/blog                         ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/orgtype     ,  :Community.orgtype/commercial                ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/category    ,  DD cat 1                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/category    ,  DD cat 2                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :Community/neighborhood,  #db/id[:db.part/user -1000002]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :Neighborhood/name     ,  DD Georgetown                                ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :Neighborhood/district ,  #db/id[:db.part/user -1000003]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :District/name         ,  Greater Duwamish                             ),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :District/region       ,  :District.region/s                           ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :Community/name        ,  DDD Interbay District Blog                   ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :Community/url         ,  http://interbayneighborhood.neighborlogs.com/),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :Community/type        ,  :Community.type/blog                         ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :Community/orgtype     ,  :Community.orgtype/community                 ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :Community/category    ,  DD cat 3                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :Community/neighborhood,  #db/id[:db.part/user -1000005]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000005],  :Neighborhood/name     ,  DD Interbay                                  ),
        |  List(:db/add,  #db/id[:db.part/user -1000005],  :Neighborhood/district ,  #db/id[:db.part/user -1000006]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000006],  :District/name         ,  Magnolia/Queen Anne                          ),
        |  List(:db/add,  #db/id[:db.part/user -1000006],  :District/region       ,  :District.region/w                           )
        |)""".stripMargin
  }


  "Manipulating data - update/retract" in new SeattleSetup {

    val belltownId: Long = Community.e.name_("belltown").get.head


    // One-cardinality attributes ..............................

    // Assert new value
    testUpdateMolecule(
      Community(belltownId).name("belltown 2").url("url 2")
    ) -->
      s"""List(
        |  List(:db/add,  $belltownId                ,  :Community/name,  belltown 2),
        |  List(:db/add,  $belltownId                ,  :Community/url ,  url 2     )
        |)""".stripMargin


    // Many-cardinality attributes ............................

    // Replace category
    // Retracts current value an asserts new value
    testUpdateMolecule(
      Community(belltownId).category.replace("news" -> "Cool news")
    ) -->
      s"""List(
        |  List(:db/retract,  $belltownId                ,  :Community/category,  news     ),
        |  List(:db/add    ,  $belltownId                ,  :Community/category,  Cool news)
        |)""".stripMargin


    // Replace multiple categories
    testUpdateMolecule(
      Community(belltownId).category.replace(
        "Cool news" -> "Super cool news",
        "events" -> "Super cool events"
      )
    ) -->
      s"""List(
        |  List(:db/retract,  $belltownId                ,  :Community/category,  Cool news        ),
        |  List(:db/add    ,  $belltownId                ,  :Community/category,  Super cool news  ),
        |  List(:db/retract,  $belltownId                ,  :Community/category,  events           ),
        |  List(:db/add    ,  $belltownId                ,  :Community/category,  Super cool events)
        |)""".stripMargin


    // Add a category
    testUpdateMolecule(
      Community(belltownId).category.assert("extra category")
    ) -->
      s"""List(
        |  List(:db/add,  $belltownId                ,  :Community/category,  extra category)
        |)""".stripMargin


    // Remove a category
    testUpdateMolecule(
      Community(belltownId).category.retract("Super cool events")
    ) -->
      s"""List(
        |  List(:db/retract,  $belltownId                ,  :Community/category,  Super cool events)
        |)""".stripMargin


    // Mixing updates and deletes..........................

    // Applying nothing (empty parenthesises) finds and retract all values of an attribute
    // Note how the name is updated at the same time
    testUpdateMolecule(
      Community(belltownId).name("belltown 3").url().category()
    ) -->
      s"""List(
        |  List(:db/add    ,  $belltownId                ,  :Community/name    ,  belltown 3                    ),
        |  List(:db/retract,  $belltownId                ,  :Community/url     ,  http://www.belltownpeople.com/),
        |  List(:db/retract,  $belltownId                ,  :Community/category,  news                          ),
        |  List(:db/retract,  $belltownId                ,  :Community/category,  events                        )
        |)""".stripMargin
  }
}