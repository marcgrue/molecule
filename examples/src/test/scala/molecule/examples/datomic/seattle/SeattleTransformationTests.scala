package molecule.examples.datomic.seattle

import molecule.core.ast.model._
import molecule.core.ast.query._
import molecule.datomic.api.in2_out8._
import molecule.examples.datomic.seattle.dsl.seattle._
import molecule.ExampleSpec
import scala.language.reflectiveCalls


class SeattleTransformationTests extends ExampleSpec {


  "A first query" in new SeattleSetup {

    // Testing that a molecule goes correctly through 3 transformations:

    m(Community.name) --> {

      // Dsl --> Model
      // Each attribute is modelled as an "Atom"

      Model(List(
        Atom("Community", "name", "String", 1, VarValue)))

    } --> {

      // Model --> Query
      // Each atom transforms to one or more query clauses and we
      // introduce value markers (like 'a) to connect elements

      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause("a", KW("Community", "name"), "b"))))

    } --> {

      // Query --> Query string
      // The Query object is transformed to a query string

      """[:find  ?b
        | :where [?a :Community/name ?b]]""".stripMargin
    }
  }


  "Querying _for_ attribute values" in new SeattleSetup {

    // Multiple attributes
    m(Community.name.url.category) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue),
        Atom("Community", "url", "String", 1, VarValue),
        Atom("Community", "category", "String", 2, VarValue))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c"),
          AggrExpr("distinct", List(), Var("d")))),
        Where(List(
          DataClause("a", KW("Community", "name"), "b"),
          DataClause("a", KW("Community", "url"), "c"),
          DataClause("a", KW("Community", "category"), "d")))
      ) -->
      """[:find  ?b ?c (distinct ?d)
        | :where [?a :Community/name ?b]
        |        [?a :Community/url ?c]
        |        [?a :Community/category ?d]]""".stripMargin
  }

  "Querying _by_ attribute values" in new SeattleSetup {

    // Names of twitter communities
    m(Community.name.type_("twitter")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue),
        Atom("Community", "type_", "String", 1, Eq(List("twitter")), Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Val("__enum__:Community.type/twitter"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :where [?a :Community/type :Community.type/twitter]
        |        [?a :Community/name ?b]]""".stripMargin


    // Categories (many-cardinality) of the Belltown Community
    m(Community.name_("belltown").category) -->
      Model(List(
        Atom("Community", "name_", "String", 1, Eq(List("belltown"))),
        Atom("Community", "category", "String", 2, VarValue))
      ) -->
      Query(
        Find(List(
          AggrExpr("distinct", List(), Var("c")))),
        Where(List(
          Funct("""ground "belltown"""", Seq(Empty), ScalarBinding(Var("b"))),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "category"), Var("c"), Empty)))
      ) -->
      """[:find  (distinct ?c)
        | :where [(ground "belltown") ?b]
        |        [?a :Community/name ?b]
        |        [?a :Community/category ?c]]""".stripMargin


    // Names of news or arts communities - transforms to a query using Rules
    m(Community.name.category_("news", "arts")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue),
        Atom("Community", "category_", "String", 2, Eq(List("news", "arts")), None))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(List(), List(
          Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("Community", "category"), Val("news"), Empty))),
          Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("Community", "category"), Val("arts"), Empty)))), List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty),
          RuleInvocation("rule1", List(Var("a")))))
      ) -->
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
    // Ref's are modelelled as "Bond"'s (between Atoms)
    m(Community.name.Neighborhood.District.region_("ne")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region_", "String", 1, Eq(List("ne")), Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("d"), KW("District", "region", ""), Val("__enum__:District.region/ne"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Neighborhood", "district", "District"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding)))) -->
      """[:find  ?b
        | :where [?d :District/region :District.region/ne]
        |        [?c :Neighborhood/district ?d]
        |        [?a :Community/neighborhood ?c]
        |        [?a :Community/name ?b]]""".stripMargin


    // Communities and their region
    m(Community.name.Neighborhood.District.region) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region", "String", 1, EnumVal, Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("e2"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Neighborhood", "district", "District"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("District", "region"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("db", "ident"), Var("e1"), Empty, NoBinding),
          Funct("name", List(Var("e1")), ScalarBinding(Var("e2")))))
      ) -->
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
    m(Community.name.type_(?)) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "type"), Var("c2"), Some(":Community.type/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b
        | :in    $ ?c2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]""".stripMargin

    // Applying a value as input
    m(Community.name.type_(?)).apply("twitter") -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq("__enum__:Community.type/twitter")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding)))
      ) -->
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


    // Omit underscore to return a `type` value
    m(Community.name.`type`(?)) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, Qm, Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "type"), Var("c2"), Some(":Community.type/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b ?c2
        | :in    $ ?c2
        | :where [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]""".stripMargin


    m(Community.name.`type`(?)).apply("twitter") -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, Qm, Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"))),
        In(
          List(
            InVar(ScalarBinding(Var("c2")), Seq(Seq("twitter")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, Qm, Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"))),
        In(
          List(
            InVar(CollectionBinding(Var("c2")), Seq(Seq("facebook_page", "twitter")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
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
    m(Community.name.`type`(?)).apply("facebook_page" or "twitter") -->
      // or
      //    m(Community.name.`type`(?))("facebook_page", "twitter") -->
      //    m(Community.name.`type`(?))(List("facebook_page", "twitter")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, Qm, Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"))),
        In(
          List(
            InVar(CollectionBinding(Var("c2")), Seq(Seq("facebook_page", "twitter")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
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

    // Single tuple of input values - AND-semantics ------------------------

    m(Community.name.type_(?).orgtype_(?)) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Atom("Community", "orgtype_", "String", 1, Qm, Some(":Community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "type"), Var("c2"), Some(":Community.type/")),
            Placeholder(Var("a"), KW("Community", "orgtype"), Var("d2"), Some(":Community.orgtype/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          DataClause(ImplDS, Var("a"), KW("Community", "orgtype"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("db", "ident"), Var("d1"), Empty, NoBinding),
          Funct("name", Seq(Var("d1")), ScalarBinding(Var("d2")))))
      ) -->
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

    m(Community.name.type_(?).orgtype_(?)).apply("email_list" and "community") -->
      // or
      //    m(Community.name.type_(?).orgtype_(?)).apply("email_list", "community") -->
      //    m(Community.name.type_(?).orgtype_(?)).apply(List(("email_list", "community"))) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Atom("Community", "orgtype_", "String", 1, Qm, Some(":Community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq("__enum__:Community.type/email_list"))),
            InVar(ScalarBinding(Var("d")), Seq(Seq("__enum__:Community.orgtype/community")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "orgtype", ""), Var("d"), Empty, NoBinding)))
      ) -->
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

    // Communities of some `type` AND some `orgtype` (include input values)
    m(Community.name.`type`(?).orgtype(?)) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, Qm, Some(":Community.type/")),
        Atom("Community", "orgtype", "String", 1, Qm, Some(":Community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"),
          Var("d2"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "type"), Var("c2"), Some(":Community.type/")),
            Placeholder(Var("a"), KW("Community", "orgtype"), Var("d2"), Some(":Community.orgtype/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          DataClause(ImplDS, Var("a"), KW("Community", "orgtype"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("db", "ident"), Var("d1"), Empty, NoBinding),
          Funct("name", Seq(Var("d1")), ScalarBinding(Var("d2")))))
      ) -->
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
    m(Community.name.`type`(?).orgtype(?))(Seq(("email_list", "community"), ("website", "commercial"))) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, Qm, Some(":Community.type/")),
        Atom("Community", "orgtype", "String", 1, Qm, Some(":Community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"),
          Var("d2"))),
        In(
          List(),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("Community", "type", ""), KW("Community.type", "email_list", ""), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("Community", "orgtype", ""), KW("Community.orgtype", "community", ""), Empty, NoBinding))),
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("Community", "type", ""), KW("Community.type", "website", ""), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("Community", "orgtype", ""), KW("Community.orgtype", "commercial", ""), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          RuleInvocation("rule1", Seq(Var("a"))),
          DataClause(ImplDS, Var("a"), KW("Community", "orgtype", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("db", "ident", ""), Var("d1"), Empty, NoBinding),
          Funct("name", Seq(Var("d1")), ScalarBinding(Var("d2")))))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, Lt("C")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          Funct(".compareTo ^String", Seq(Var("b"), Val("C")), ScalarBinding(Var("b2"))),
          Funct("<", Seq(Var("b2"), Val(0)), NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :where [(.compareTo ^String ?b "C") ?b2]
        |        [(< ?b2 0)]
        |        [?a :Community/name ?b]]""".stripMargin

    m(Community.name < ?) -->
      Model(List(
        Atom("Community", "name", "String", 1, Lt(Qm)))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "name", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          Funct(".compareTo ^String", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<", Seq(Var("b2"), Val(0)), NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(.compareTo ^String ?b ?b1) ?b2]
        |        [(< ?b2 0)]
        |        [?a :Community/name ?b]]""".stripMargin

    m(Community.name < ?).apply("C") -->
      Model(List(
        Atom("Community", "name", "String", 1, Lt(Qm)))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq("C")))),
          List(),
          List(DS)),
        Where(List(
          Funct(".compareTo ^String", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<", Seq(Var("b2"), Val(0)), NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding)))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, Fulltext(List("Wallingford"))))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          Funct("fulltext", Seq(DS, KW("Community", "name"), Val("Wallingford")), RelationBinding(List(Var("a"), Var("b"))))))
      ) -->
      """[:find  ?b
        | :where [(fulltext $ :Community/name "Wallingford") [[ ?a ?b ]]]]""".stripMargin


    m(Community.name contains ?) -->
      Model(List(
        Atom("Community", "name", "String", 1, Fulltext(Seq(Qm))))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "name"), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          Funct("fulltext", Seq(DS, KW("Community", "name"), Var("b1")), RelationBinding(List(Var("a"), Var("b"))))))
      ) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(fulltext $ :Community/name ?b1) [[ ?a ?b ]]]]""".stripMargin


    m(Community.name contains ?).apply("Wallingford") -->
      Model(List(
        Atom("Community", "name", "String", 1, Fulltext(List(Qm))))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b1")), Seq(Seq("Wallingford")))),
          List(),
          List(DS)),
        Where(List(
          Funct("fulltext", Seq(DS, KW("Community", "name"), Var("b1")), RelationBinding(List(Var("a"), Var("b"))))))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Eq(List("website")), Some(":Community.type/")),
        Atom("Community", "category", "String", 2, Fulltext(List("food")), None))
      ) -->
      Query(
        Find(List(
          Var("b"),
          AggrExpr("distinct", Seq(), Var("d")))),
        In(
          List(),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              Funct("fulltext", Seq(DS(""), KW("Community", "category", ""), Val("food")), RelationBinding(List(Var("a"), Var("a_1"))))))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Val("__enum__:Community.type/website"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "category", ""), Var("d"), Empty, NoBinding),
          RuleInvocation("rule1", Seq(Var("a")))))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Atom("Community", "category", "String", 2, Fulltext(List(Qm)), None))
      ) -->
      Query(
        Find(List(
          Var("b"),
          AggrExpr("distinct", Seq(), Var("d")))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "type", ""), Var("c2"), Some(":Community.type/")),
            Placeholder(Var("a"), KW("Community", "category", ""), Var("d1"), None)),
          List(),
          List(DS)),
        Where(List(
          Funct("fulltext", Seq(DS, KW("Community", "category", ""), Var("d1")), RelationBinding(List(Var("a"), Var("d")))),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b (distinct ?d)
        | :in    $ ?c2 ?d1
        | :where [(fulltext $ :Community/category ?d1) [[ ?a ?d ]]]
        |        [?a :Community/name ?b]
        |        [?a :Community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(name ?c1) ?c2]]""".stripMargin


    m(Community.name.type_(?).category contains ?).apply("website", Set("food")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Atom("Community", "category", "String", 2, Fulltext(List(Qm)), None))
      ) -->
      Query(
        Find(List(
          Var("b"),
          AggrExpr("distinct", Seq(), Var("d")))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq("__enum__:Community.type/website")))),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              Funct("fulltext", Seq(DS(""), KW("Community", "category", ""), Val("food")), RelationBinding(List(Var("a"), Var("d1_1"))))))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "category", ""), Var("d"), Empty, NoBinding),
          RuleInvocation("rule1", Seq(Var("a")))))
      ) -->
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

    // Social media

    m(Community.name.type_("twitter" or "facebook_page")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Eq(List("twitter", "facebook_page")), Some(":Community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Val("__enum__:Community.type/twitter"), Empty, NoBinding))),
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Val("__enum__:Community.type/facebook_page"), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          RuleInvocation("rule1", Seq(Var("a")))))
      ) -->
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


    m(Community.name.Neighborhood.District.region("ne" or "sw")) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region", "String", 1, Eq(List("ne", "sw")), Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("e2"))),
        In(
          List(),
          List(
            Rule("rule1", Seq(Var("d")), Seq(
              DataClause(ImplDS, Var("d"), KW("District", "region", ""), Val("__enum__:District.region/ne"), Empty, NoBinding))),
            Rule("rule1", Seq(Var("d")), Seq(
              DataClause(ImplDS, Var("d"), KW("District", "region", ""), Val("__enum__:District.region/sw"), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Neighborhood", "district", "District"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("District", "region", ""), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("db", "ident", ""), Var("e1"), Empty, NoBinding),
          Funct("name", Seq(Var("e1")), ScalarBinding(Var("e2"))),
          RuleInvocation("rule1", Seq(Var("d")))))
      ) -->
      """[:find  ?b ?e2
        | :in    $ %
        | :where [?a :Community/name ?b]
        |        [?a :Community/neighborhood ?c]
        |        [?c :Neighborhood/district ?d]
        |        [?d :District/region ?e]
        |        [?e :db/ident ?e1]
        |        [(name ?e1) ?e2]
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
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Eq(List("twitter", "facebook_page")), Some(":Community.type/")),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region_", "String", 1, Eq(List("sw", "s", "se")), Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Val("__enum__:Community.type/twitter"), Empty, NoBinding))),
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Val("__enum__:Community.type/facebook_page"), Empty, NoBinding))),
            Rule("rule2", Seq(Var("e")), Seq(
              DataClause(ImplDS, Var("e"), KW("District", "region", ""), Val("__enum__:District.region/sw"), Empty, NoBinding))),
            Rule("rule2", Seq(Var("e")), Seq(
              DataClause(ImplDS, Var("e"), KW("District", "region", ""), Val("__enum__:District.region/s"), Empty, NoBinding))),
            Rule("rule2", Seq(Var("e")), Seq(
              DataClause(ImplDS, Var("e"), KW("District", "region", ""), Val("__enum__:District.region/se"), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          RuleInvocation("rule1", Seq(Var("a"))),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("Neighborhood", "district", "District"), Var("e"), Empty, NoBinding),
          RuleInvocation("rule2", Seq(Var("e")))))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region_", "String", 1, Qm, Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("Community", "type"), Var("c2"), Some(":Community.type/")),
            Placeholder(Var("e"), KW("District", "region"), Var("f2"), Some(":District.region/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "type"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident"), Var("c1"), Empty, NoBinding),
          Funct("name", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("Neighborhood", "district", "District"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("District", "region"), Var("f"), Empty, NoBinding),
          DataClause(ImplDS, Var("f"), KW("db", "ident"), Var("f1"), Empty, NoBinding),
          Funct("name", Seq(Var("f1")), ScalarBinding(Var("f2")))))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region_", "String", 1, Qm, Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("c")), Seq(Seq("__enum__:Community.type/twitter", "__enum__:Community.type/facebook_page"))),
            InVar(CollectionBinding(Var("f")), Seq(Seq("__enum__:District.region/sw", "__enum__:District.region/s", "__enum__:District.region/se")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("District", "region", ""), Var("f"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("Neighborhood", "district", "District"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("d"), Empty, NoBinding)))
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


    m(Community.name.type_(?).Neighborhood.District.region_(?)).apply(
      ("twitter" or "facebook_page") and ("sw" or "s" or "se")
      // or
      // Seq("twitter", "facebook_page"), Seq("sw", "s", "se")
    ) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "type_", "String", 1, Qm, Some(":Community.type/")),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "region_", "String", 1, Qm, Some(":District.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("c")), Seq(Seq("__enum__:Community.type/twitter", "__enum__:Community.type/facebook_page"))),
            InVar(CollectionBinding(Var("f")), Seq(Seq("__enum__:District.region/sw", "__enum__:District.region/s", "__enum__:District.region/se")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("District", "region", ""), Var("f"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("Neighborhood", "district", "District"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Community", "neighborhood", "Neighborhood"), Var("d"), Empty, NoBinding)))
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
      Model(List(
        Generic("Schema", "txInstant", "schema", NoValue))
      ) -->
      Query(
        Find(List(
          Var("txInstant"))),
        Where(List(
          Funct("=", Seq(Var("sys"), Val(false)), NoBinding),
          DataClause(ImplDS, Var("_"), KW("db.install", "attribute", ""), Var("id"), Var("tx"), NoBinding),
          DataClause(ImplDS, Var("id"), KW("db", "ident", ""), Var("idIdent"), NoBinding, NoBinding),
          Funct("namespace", Seq(Var("idIdent")), ScalarBinding(Var("nsFull"))),
          Funct(".matches ^String", Seq(Var("nsFull"), Val("^(db|db.alter|db.excise|db.install|db.part|db.sys|fressian|db.entity|db.attr|:?-.*)")), ScalarBinding(Var("sys"))),
          Funct(".contains ^String", Seq(Var("nsFull"), Val("_")), ScalarBinding(Var("isPart"))),
          Funct(".split ^String", Seq(Var("nsFull"), Val("_")), ScalarBinding(Var("nsParts"))),
          Funct("first", Seq(Var("nsParts")), ScalarBinding(Var("part0"))),
          Funct("if", Seq(Var("isPart"), Var("part0"), Val("db.part/user")), ScalarBinding(Var("part"))),
          Funct("last", Seq(Var("nsParts")), ScalarBinding(Var("ns"))),
          DataClause(ImplDS, Var("tx"), KW("db", "txInstant", ""), Var("txInstant"), Empty, NoBinding)))
      ) -->
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
      Model(List(
        Atom("Community", "name", "String", 1, Eq(List("AAA")), None),
        Atom("Community", "url", "String", 1, Eq(List("myUrl")), None),
        Atom("Community", "type", "String", 1, Eq(List("twitter")), Some(":Community.type/")),
        Atom("Community", "orgtype", "String", 1, Eq(List("personal")), Some(":Community.orgtype/")),
        Atom("Community", "category", "String", 2, Eq(List("my", "favorites")), None),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Atom("Neighborhood", "name", "String", 1, Eq(List("myNeighborhood")), None, List()),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "name", "String", 1, Eq(List("myDistrict")), None),
        Atom("District", "region", "String", 1, Eq(List("nw")), Some(":District.region/")))
      ) -->
      //  Some things to notice:
      //  - Enum values are prefixed with their namespace ("nw" becomes ":District.region/nw")
      //  - Multiple values of many-cardinality attributes each get their own statement ("my" + "favorites")
      //
      //       operation            temp id                 attribute                value
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

    m(Community.name.url.`type`.orgtype.category.Neighborhood.name.District.name.region) -->
      Model(List(
        Atom("Community", "name", "String", 1, VarValue, None),
        Atom("Community", "url", "String", 1, VarValue, None),
        Atom("Community", "type", "String", 1, EnumVal, Some(":Community.type/")),
        Atom("Community", "orgtype", "String", 1, EnumVal, Some(":Community.orgtype/")),
        Atom("Community", "category", "String", 2, VarValue, None),
        Bond("Community", "neighborhood", "Neighborhood", 1),
        Atom("Neighborhood", "name", "String", 1, VarValue, None),
        Bond("Neighborhood", "district", "District", 1),
        Atom("District", "name", "String", 1, VarValue, None),
        Atom("District", "region", "String", 1, EnumVal, Some(":District.region/")))
      ) -->
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

    val belltownId = Community.e.name_("belltown").get.head


    // One-cardinality attributes ..............................

    // Assert new value
    testUpdateMolecule(
      Community(belltownId).name("belltown 2").url("url 2")
    ) -->
      Model(List(
        Generic("Community", "e_", "datom", Eq(List(belltownId))),
        Atom("Community", "name", "String", 1, Eq(List("belltown 2")), None),
        Atom("Community", "url", "String", 1, Eq(List("url 2")), None))
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
      Model(List(
        Generic("Community", "e_", "datom", Eq(List(belltownId))),
        Atom("Community", "category", "String", 2, ReplaceValue(Seq("news" -> "Cool news")), None))
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
      Model(List(
        Generic("Community", "e_", "datom", Eq(List(belltownId))),
        Atom("Community", "category", "String", 2, ReplaceValue(Seq(
          "Cool news" -> "Super cool news",
          "events" -> "Super cool events")), None))
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
      Model(List(
        Generic("Community", "e_", "datom", Eq(List(belltownId))),
        Atom("Community", "category", "String", 2, AssertValue(List("extra category")), None))
      ) -->
      s"""List(
         |  List(:db/add,  $belltownId                ,  :Community/category,  extra category)
         |)""".stripMargin


    // Remove a category
    testUpdateMolecule(
      Community(belltownId).category.retract("Super cool events")
    ) -->
      Model(List(
        Generic("Community", "e_", "datom", Eq(List(belltownId))),
        Atom("Community", "category", "String", 2, RetractValue(List("Super cool events")), None))
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
      Model(List(
        Generic("Community", "e_", "datom", Eq(List(belltownId))),
        Atom("Community", "name", "String", 1, Eq(List("belltown 3")), None),
        Atom("Community", "url", "String", 1, Eq(List()), None),
        Atom("Community", "category", "String", 2, Eq(List()), None))
      ) -->
      s"""List(
         |  List(:db/add    ,  $belltownId                ,  :Community/name    ,  belltown 3                    ),
         |  List(:db/retract,  $belltownId                ,  :Community/url     ,  http://www.belltownpeople.com/),
         |  List(:db/retract,  $belltownId                ,  :Community/category,  news                          ),
         |  List(:db/retract,  $belltownId                ,  :Community/category,  events                        )
         |)""".stripMargin
  }
}