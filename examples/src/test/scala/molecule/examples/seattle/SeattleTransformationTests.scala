package molecule.examples.seattle
import molecule.ast.model._
import molecule.ast.query._
import molecule.generic.Db
import molecule.examples.seattle.dsl.seattle._
import molecule.api._
import scala.language.reflectiveCalls


class SeattleTransformationTests extends SeattleSpec {


  "A first query" >> {

    // Testing that a molecule goes correctly through 3 transformations:

    m(Community.name) --> {

      // Dsl --> Model
      // Each attribute is modelled as an "Atom"

      Model(List(
        Atom("community", "name", "String", 1, VarValue)))

    } --> {

      // Model --> Query
      // Each atom transforms to one or more query clauses and we
      // introduce value markers (like 'a) to connect elements

      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause("a", KW("community", "name"), "b"))))

    } --> {

      // Query --> Query string
      // The Query object is transformed to a query string

      """[:find  ?b
        | :where [?a :community/name ?b]]""".stripMargin
    }
  }


  "Querying _for_ attribute values" >> {

    // Multiple attributes
    m(Community.name.url.category) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue),
        Atom("community", "url", "String", 1, VarValue),
        Atom("community", "category", "String", 2, VarValue))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c"),
          AggrExpr("distinct", List(), Var("d")))),
        Where(List(
          DataClause("a", KW("community", "name"), "b"),
          DataClause("a", KW("community", "url"), "c"),
          DataClause("a", KW("community", "category"), "d")))
      ) -->
      """[:find  ?b ?c (distinct ?d)
        | :where [?a :community/name ?b]
        |        [?a :community/url ?c]
        |        [?a :community/category ?d]]""".stripMargin
  }

  "Querying _by_ attribute values" >> {

    // Names of twitter communities
    m(Community.name.type_("twitter")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue),
        Atom("community", "type_", "String", 1, Eq(List("twitter")), Some(":community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause("a", KW("community", "name"), "b"),
          DataClause("a", KW("community", "type"), Val(":community.type/twitter"))))
      ) -->
      """[:find  ?b
        | :where [?a :community/name ?b]
        |        [?a :community/type ":community.type/twitter"]]""".stripMargin


    // Categories (many-cardinality) of the Belltown Community
    m(Community.name_("belltown").category) -->
      Model(List(
        Atom("community", "name_", "String", 1, Eq(List("belltown"))),
        Atom("community", "category", "String", 2, VarValue))
      ) -->
      Query(
        Find(List(
          AggrExpr("distinct", List(), Var("c")))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Val("belltown"), Empty),
          DataClause(ImplDS, Var("a"), KW("community", "category"), Var("c"), Empty)))
      ) -->
      """[:find  (distinct ?c)
        | :where [?a :community/name "belltown"]
        |        [?a :community/category ?c]]""".stripMargin


    // Names of news or arts communities - transforms to a query using Rules
    m(Community.name.category_("news", "arts")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue),
        Atom("community", "category_", "String", 2, Eq(List("news", "arts")), None))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(List(), List(
          Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("community", "category"), Val("news"), Empty))),
          Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("community", "category"), Val("arts"), Empty)))), List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty),
          RuleInvocation("rule1", List(Var("a")))))
      ) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :community/name ?b]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [?a :community/category "news"]]
        |     [(rule1 ?a) [?a :community/category "arts"]]]
        |)""".stripMargin
  }


  "Querying across references" >> {

    // Communities in north eastern region
    // Ref's are modelelled as "Bond"'s (between Atoms)
    m(Community.name.Neighborhood.District.region_("ne")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region_", "String", 1, Eq(List("ne")), Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("neighborhood", "district", "district"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("district", "region"), Val(":district.region/ne"), Empty)))
      ) -->
      """[:find  ?b
        | :where [?a :community/name ?b]
        |        [?a :community/neighborhood ?c]
        |        [?c :neighborhood/district ?d]
        |        [?d :district/region ":district.region/ne"]]""".stripMargin


    // Communities and their region
    m(Community.name.Neighborhood.District.region) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region", "String", 1, EnumVal, Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("e2"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("neighborhood", "district", "district"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("district", "region"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("db", "ident"), Var("e1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", List(Var("e1")), ScalarBinding(Var("e2")))))
      ) -->
      """[:find  ?b ?e2
        | :where [?a :community/name ?b]
        |        [?a :community/neighborhood ?c]
        |        [?c :neighborhood/district ?d]
        |        [?d :district/region ?e]
        |        [?e :db/ident ?e1]
        |        [(.getName ^clojure.lang.Keyword ?e1) ?e2]]""".stripMargin
  }


  "Advanced queries - parameterizing queries" >> {

    /** ******* Single input parameter **************************/

    // Community input molecule awaiting some type value
    m(Community.name.type_(?)) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "type", ""), Var("c2"), Some(":community.type/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b
        | :in    $ ?c2
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]]""".stripMargin

    // Applying a value as input
    m(Community.name.type_(?)).apply("twitter") -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq(":community.type/twitter")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ ?c
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 :community.type/twitter
        |)""".stripMargin


    // Omit underscore to return a `type` value
    m(Community.name.`type`(?)) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, Qm, Some(":community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "type", ""), Var("c2"), Some(":community.type/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b ?c2
        | :in    $ ?c2
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]]""".stripMargin


    m(Community.name.`type`(?)).apply("twitter") -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, Qm, Some(":community.type/")))
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
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b ?c2
        | :in    $ ?c2
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 twitter
        |)""".stripMargin



    // Multiple input values - logical OR ------------------------

    m(Community.name.`type`(?)).apply("facebook_page" or "twitter") -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, Qm, Some(":community.type/")))
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
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b ?c2
        | :in    $ [?c2 ...]
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]]
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
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, Qm, Some(":community.type/")))
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
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2")))))
      ) -->
      """[:find  ?b ?c2
        | :in    $ [?c2 ...]
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]]
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
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Atom("community", "orgtype_", "String", 1, Qm, Some(":community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "type", ""), Var("c2"), Some(":community.type/")),
            Placeholder(Var("a"), KW("community", "orgtype", ""), Var("d2"), Some(":community.orgtype/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          DataClause(ImplDS, Var("a"), KW("community", "orgtype", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("db", "ident", ""), Var("d1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("d1")), ScalarBinding(Var("d2")))))
      ) -->
      """[:find  ?b
        | :in    $ ?c2 ?d2
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]
        |        [?a :community/orgtype ?d]
        |        [?d :db/ident ?d1]
        |        [(.getName ^clojure.lang.Keyword ?d1) ?d2]]""".stripMargin


    // The following 3 notation variations transform in the same way

    m(Community.name.type_(?).orgtype_(?)).apply("email_list" and "community") -->
      // or
      //    m(Community.name.type_(?).orgtype_(?)).apply("email_list", "community") -->
      //    m(Community.name.type_(?).orgtype_(?)).apply(List(("email_list", "community"))) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Atom("community", "orgtype_", "String", 1, Qm, Some(":community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq(":community.type/email_list"))),
            InVar(ScalarBinding(Var("d")), Seq(Seq(":community.orgtype/community")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "orgtype", ""), Var("d"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ ?c ?d
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?a :community/orgtype ?d]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 :community.type/email_list
        |  3 :community.orgtype/community
        |)""".stripMargin


    // Multiple tuples of input values ------------------------

    // Communities of some `type` AND some `orgtype` (include input values)
    m(Community.name.`type`(?).orgtype(?)) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, Qm, Some(":community.type/")),
        Atom("community", "orgtype", "String", 1, Qm, Some(":community.orgtype/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("c2"),
          Var("d2"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "type", ""), Var("c2"), Some(":community.type/")),
            Placeholder(Var("a"), KW("community", "orgtype", ""), Var("d2"), Some(":community.orgtype/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          DataClause(ImplDS, Var("a"), KW("community", "orgtype", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("db", "ident", ""), Var("d1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("d1")), ScalarBinding(Var("d2")))))
      ) -->
      """[:find  ?b ?c2 ?d2
        | :in    $ ?c2 ?d2
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]
        |        [?a :community/orgtype ?d]
        |        [?d :db/ident ?d1]
        |        [(.getName ^clojure.lang.Keyword ?d1) ?d2]]""".stripMargin


    // The following 3 notation variations transform in the same way

    //    m(Community.name.`type`(?!).orgtype(?!)).apply(("email_list" and "community") or ("website" and "commercial")) -->
    //    m(Community.name.`type`(?!).orgtype(?!)).apply(("email_list", "community"), ("website", "commercial")) -->
    m(Community.name.`type`(?).orgtype(?))(Seq(("email_list", "community"), ("website", "commercial"))) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, Qm, Some(":community.type/")),
        Atom("community", "orgtype", "String", 1, Qm, Some(":community.orgtype/")))
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
              DataClause(ImplDS, Var("a"), KW("community", "type", ""), Val(":community.type/email_list"), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("community", "orgtype", ""), Val(":community.orgtype/community"), Empty, NoBinding))),
            Rule("rule1", Seq(Var("a")), Seq(
              DataClause(ImplDS, Var("a"), KW("community", "type", ""), Val(":community.type/website"), Empty, NoBinding),
              DataClause(ImplDS, Var("a"), KW("community", "orgtype", ""), Val(":community.orgtype/commercial"), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          RuleInvocation("rule1", Seq(Var("a"))),
          DataClause(ImplDS, Var("a"), KW("community", "orgtype", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("db", "ident", ""), Var("d1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("d1")), ScalarBinding(Var("d2")))))
      ) -->
      """[:find  ?b ?c2 ?d2
        | :in    $ %
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]
        |        (rule1 ?a)
        |        [?a :community/orgtype ?d]
        |        [?d :db/ident ?d1]
        |        [(.getName ^clojure.lang.Keyword ?d1) ?d2]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a)
        |   [?a :community/type ":community.type/email_list"]
        |   [?a :community/orgtype ":community.orgtype/community"]]
        |     [(rule1 ?a)
        |   [?a :community/type ":community.type/website"]
        |   [?a :community/orgtype ":community.orgtype/commercial"]]]
        |)""".stripMargin
  }


  "Invoking functions in queries" >> {

    m(Community.name < "C") -->
      Model(List(
        Atom("community", "name", "String", 1, Lt("C")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty),
          Funct(".compareTo ^String", List(Var("b"), Val("C")), ScalarBinding(Var("b2"))),
          Funct("<", List(Var("b2"), Val(0)), NoBinding)))
      ) -->
      """[:find  ?b
        | :where [?a :community/name ?b]
        |        [(.compareTo ^String ?b "C") ?b2]
        |        [(< ?b2 0)]]""".stripMargin

    m(Community.name < ?) -->
      Model(List(
        Atom("community", "name", "String", 1, Lt(Qm)))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "name", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^String", Seq(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<", Seq(Var("b2"), Val(0)), NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [?a :community/name ?b]
        |        [(.compareTo ^String ?b ?b1) ?b2]
        |        [(< ?b2 0)]]""".stripMargin

    m(Community.name < ?).apply("C") -->
      Model(List(
        Atom("community", "name", "String", 1, Lt(Qm)))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(List(
          InVar(ScalarBinding(Var("b1")), List(List("C"))))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty, NoBinding),
          Funct(".compareTo ^String", List(Var("b"), Var("b1")), ScalarBinding(Var("b2"))),
          Funct("<", List(Var("b2"), Val(0)), NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [?a :community/name ?b]
        |        [(.compareTo ^String ?b ?b1) ?b2]
        |        [(< ?b2 0)]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 C
        |)""".stripMargin
  }


  "Querying with fulltext search" >> {

    m(Community.name contains "Wallingford") -->
      Model(List(
        Atom("community", "name", "String", 1, Fulltext(List("Wallingford"))))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          Funct("fulltext", Seq(DS, KW("community", "name", ""), Val("Wallingford")), RelationBinding(List(Var("a"), Var("b"))))))
      ) -->
      """[:find  ?b
        | :where [(fulltext $ :community/name "Wallingford") [[ ?a ?b ]]]]""".stripMargin


    m(Community.name contains ?) -->
      Model(List(
        Atom("community", "name", "String", 1, Fulltext(Seq(Qm))))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "name", ""), Var("b1"), None)),
          List(),
          List(DS)),
        Where(List(
          Funct("fulltext", Seq(DS, KW("community", "name", ""), Var("b1")), RelationBinding(List(Var("a"), Var("b"))))))
      ) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(fulltext $ :community/name ?b1) [[ ?a ?b ]]]]""".stripMargin


    m(Community.name contains ?).apply("Wallingford") -->
      Model(List(
        Atom("community", "name", "String", 1, Fulltext(List(Qm))))
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
          Funct("fulltext", Seq(DS, KW("community", "name", ""), Var("b1")), RelationBinding(List(Var("a"), Var("b"))))))
      ) -->
      """[:find  ?b
        | :in    $ ?b1
        | :where [(fulltext $ :community/name ?b1) [[ ?a ?b ]]]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 Wallingford
        |)""".stripMargin


    // Fulltext search on many-attribute (`category`)

    m(Community.name.type_("website").category contains "food") -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Eq(List("website")), Some(":community.type/")),
        Atom("community", "category", "String", 2, Fulltext(List("food")), None))
      ) -->
      Query(
        Find(List(
          Var("b"),
          AggrExpr("distinct", Seq(), Var("d")))),
        In(
          List(),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              Funct("fulltext", Seq(DS(""), KW("community", "category", ""), Val("food")), RelationBinding(List(Var("a"), Var("a_1"))))))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Val(":community.type/website"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "category", ""), Var("d"), Empty, NoBinding),
          RuleInvocation("rule1", Seq(Var("a")))))
      ) -->
      """[:find  ?b (distinct ?d)
        | :in    $ %
        | :where [?a :community/name ?b]
        |        [?a :community/type ":community.type/website"]
        |        [?a :community/category ?d]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [(fulltext $ :community/category "food") [[ ?a ?a_1 ]]]]]
        |)""".stripMargin


    m(Community.name.type_(?).category contains ?) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Atom("community", "category", "String", 2, Fulltext(List(Qm)), None))
      ) -->
      Query(
        Find(List(
          Var("b"),
          AggrExpr("distinct", Seq(), Var("d")))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "type", ""), Var("c2"), Some(":community.type/")),
            Placeholder(Var("a"), KW("community", "category", ""), Var("d1"), None)),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          Funct("fulltext", Seq(DS, KW("community", "category", ""), Var("d1")), RelationBinding(List(Var("a"), Var("d"))))))
      ) -->
      """[:find  ?b (distinct ?d)
        | :in    $ ?c2 ?d1
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]
        |        [(fulltext $ :community/category ?d1) [[ ?a ?d ]]]]""".stripMargin


    m(Community.name.type_(?).category contains ?).apply("website", Set("food")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Atom("community", "category", "String", 2, Fulltext(List(Qm)), None))
      ) -->
      Query(
        Find(List(
          Var("b"),
          AggrExpr("distinct", Seq(), Var("d")))),
        In(
          List(
            InVar(ScalarBinding(Var("c")), Seq(Seq(":community.type/website")))),
          List(
            Rule("rule1", Seq(Var("a")), Seq(
              Funct("fulltext", Seq(DS(""), KW("community", "category", ""), Val("food")), RelationBinding(List(Var("a"), Var("d1_1"))))))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "category", ""), Var("d"), Empty, NoBinding),
          RuleInvocation("rule1", Seq(Var("a")))))
      ) -->
      """[:find  ?b (distinct ?d)
        | :in    $ % ?c
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?a :community/category ?d]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [(fulltext $ :community/category "food") [[ ?a ?d1_1 ]]]]]
        |  3 :community.type/website
        |)""".stripMargin
  }


  "Querying with rules (logical OR)" >> {

    // Social media

    m(Community.name.type_("twitter" or "facebook_page")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Eq(List("twitter", "facebook_page")), Some(":community.type/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(List(), List(
          Rule("rule1", List(Var("a")), List(
            DataClause(ImplDS, Var("a"), KW("community", "type"), Val(":community.type/twitter"), Empty))),
          Rule("rule1", List(Var("a")), List(
            DataClause(ImplDS, Var("a"), KW("community", "type"), Val(":community.type/facebook_page"), Empty)))), List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty),
          RuleInvocation("rule1", List(Var("a")))))
      ) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :community/name ?b]
        |        (rule1 ?a)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [?a :community/type ":community.type/twitter"]]
        |     [(rule1 ?a) [?a :community/type ":community.type/facebook_page"]]]
        |)""".stripMargin


    m(Community.name.Neighborhood.District.region("ne" or "sw")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region", "String", 1, Eq(List("ne", "sw")), Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"),
          Var("e2"))),
        In(List(), List(
          Rule("rule1", List(Var("d")), List(
            DataClause(ImplDS, Var("d"), KW("district", "region"), Val(":district.region/ne"), Empty))),
          Rule("rule1", List(Var("d")), List(
            DataClause(ImplDS, Var("d"), KW("district", "region"), Val(":district.region/sw"), Empty)))), List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("neighborhood", "district", "district"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("district", "region"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("db", "ident"), Var("e1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", List(Var("e1")), ScalarBinding(Var("e2"))),
          RuleInvocation("rule1", List(Var("d")))))
      ) -->
      """[:find  ?b ?e2
        | :in    $ %
        | :where [?a :community/name ?b]
        |        [?a :community/neighborhood ?c]
        |        [?c :neighborhood/district ?d]
        |        [?d :district/region ?e]
        |        [?e :db/ident ?e1]
        |        [(.getName ^clojure.lang.Keyword ?e1) ?e2]
        |        (rule1 ?d)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?d) [?d :district/region ":district.region/ne"]]
        |     [(rule1 ?d) [?d :district/region ":district.region/sw"]]]
        |)""".stripMargin


    // Social media in southern regions
    m(Community.name.type_("twitter" or "facebook_page").Neighborhood.District.region_("sw" or "s" or "se")) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Eq(List("twitter", "facebook_page")), Some(":community.type/")),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region_", "String", 1, Eq(List("sw", "s", "se")), Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(List(), List(
          Rule("rule1", List(Var("a")), List(
            DataClause(ImplDS, Var("a"), KW("community", "type"), Val(":community.type/twitter"), Empty))),
          Rule("rule1", List(Var("a")), List(
            DataClause(ImplDS, Var("a"), KW("community", "type"), Val(":community.type/facebook_page"), Empty))),

          Rule("rule2", List(Var("e")), List(
            DataClause(ImplDS, Var("e"), KW("district", "region"), Val(":district.region/sw"), Empty))),
          Rule("rule2", List(Var("e")), List(
            DataClause(ImplDS, Var("e"), KW("district", "region"), Val(":district.region/s"), Empty))),
          Rule("rule2", List(Var("e")), List(
            DataClause(ImplDS, Var("e"), KW("district", "region"), Val(":district.region/se"), Empty)))), List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name"), Var("b"), Empty),
          RuleInvocation("rule1", List(Var("a"))),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("d"), Empty),
          DataClause(ImplDS, Var("d"), KW("neighborhood", "district", "district"), Var("e"), Empty),
          RuleInvocation("rule2", List(Var("e")))))
      ) -->
      """[:find  ?b
        | :in    $ %
        | :where [?a :community/name ?b]
        |        (rule1 ?a)
        |        [?a :community/neighborhood ?d]
        |        [?d :neighborhood/district ?e]
        |        (rule2 ?e)]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [[(rule1 ?a) [?a :community/type ":community.type/twitter"]]
        |     [(rule1 ?a) [?a :community/type ":community.type/facebook_page"]]
        |     [(rule2 ?e) [?e :district/region ":district.region/sw"]]
        |     [(rule2 ?e) [?e :district/region ":district.region/s"]]
        |     [(rule2 ?e) [?e :district/region ":district.region/se"]]]
        |)""".stripMargin


    m(Community.name.type_(?).Neighborhood.District.region_(?)) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region_", "String", 1, Qm, Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            Placeholder(Var("a"), KW("community", "type", ""), Var("c2"), Some(":community.type/")),
            Placeholder(Var("e"), KW("district", "region", ""), Var("f2"), Some(":district.region/"))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("db", "ident", ""), Var("c1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("c1")), ScalarBinding(Var("c2"))),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("neighborhood", "district", "district"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("district", "region", ""), Var("f"), Empty, NoBinding),
          DataClause(ImplDS, Var("f"), KW("db", "ident", ""), Var("f1"), Empty, NoBinding),
          Funct(".getName ^clojure.lang.Keyword", Seq(Var("f1")), ScalarBinding(Var("f2")))))
      ) -->
      """[:find  ?b
        | :in    $ ?c2 ?f2
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?c :db/ident ?c1]
        |        [(.getName ^clojure.lang.Keyword ?c1) ?c2]
        |        [?a :community/neighborhood ?d]
        |        [?d :neighborhood/district ?e]
        |        [?e :district/region ?f]
        |        [?f :db/ident ?f1]
        |        [(.getName ^clojure.lang.Keyword ?f1) ?f2]]""".stripMargin


    m(Community.name.type_(?).Neighborhood.District.region_(?)).apply(
      ("twitter" or "facebook_page") and ("sw" or "s" or "se")
      // or
      // Seq("twitter", "facebook_page"), Seq("sw", "s", "se")
    ) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region_", "String", 1, Qm, Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("c")), Seq(Seq(":community.type/twitter", ":community.type/facebook_page"))),
            InVar(CollectionBinding(Var("f")), Seq(Seq(":district.region/sw", ":district.region/s", ":district.region/se")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("neighborhood", "district", "district"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("district", "region", ""), Var("f"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ [?c ...] [?f ...]
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?a :community/neighborhood ?d]
        |        [?d :neighborhood/district ?e]
        |        [?e :district/region ?f]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [:community.type/twitter, :community.type/facebook_page]
        |  3 [:district.region/sw, :district.region/s, :district.region/se]
        |)""".stripMargin


    m(Community.name.type_(?).Neighborhood.District.region_(?)).apply(
      ("twitter" or "facebook_page") and ("sw" or "s" or "se")
      // or
      // Seq("twitter", "facebook_page"), Seq("sw", "s", "se")
    ) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "type_", "String", 1, Qm, Some(":community.type/")),
        Bond("community", "neighborhood", "neighborhood", 1),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "region_", "String", 1, Qm, Some(":district.region/")))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("c")), Seq(Seq(":community.type/twitter", ":community.type/facebook_page"))),
            InVar(CollectionBinding(Var("f")), Seq(Seq(":district.region/sw", ":district.region/s", ":district.region/se")))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("community", "name", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "type", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("community", "neighborhood", "neighborhood"), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("d"), KW("neighborhood", "district", "district"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("district", "region", ""), Var("f"), Empty, NoBinding)))
      ) -->
      """[:find  ?b
        | :in    $ [?c ...] [?f ...]
        | :where [?a :community/name ?b]
        |        [?a :community/type ?c]
        |        [?a :community/neighborhood ?d]
        |        [?d :neighborhood/district ?e]
        |        [?e :district/region ?f]]
        |
        |INPUTS:
        |List(
        |  1 datomic.db.Db@xxx
        |  2 [:community.type/twitter, :community.type/facebook_page]
        |  3 [:district.region/sw, :district.region/s, :district.region/se]
        |)""".stripMargin
  }


  "Working with time" >> {

    //    implicit val conn = loadFromFiles("seattle-schema1a.dtm", "seattle-data0a.dtm", 2)

    m(Db.txInstant) -->
      Model(List(
        Atom("db", "txInstant", "Long", 1, VarValue))
      ) -->
      Query(
        Find(List(
          Var("b"))),
        Where(List(
          DataClause("a", KW("db", "txInstant"), "b")))
      ) -->
      """[:find  ?b
        | :where [?a :db/txInstant ?b]]""".stripMargin
  }


  "Manipulating data - insert" >> {

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
        Atom("community", "name", "String", 1, Eq(List("AAA")), None),
        Atom("community", "url", "String", 1, Eq(List("myUrl")), None),
        Atom("community", "type", "String", 1, Eq(List("twitter")), Some(":community.type/")),
        Atom("community", "orgtype", "String", 1, Eq(List("personal")), Some(":community.orgtype/")),
        Atom("community", "category", "String", 2, Eq(List("my", "favorites")), None),
        Bond("community", "neighborhood", "neighborhood", 1),
        Atom("neighborhood", "name", "String", 1, Eq(List("myNeighborhood")), None, List()),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "name", "String", 1, Eq(List("myDistrict")), None),
        Atom("district", "region", "String", 1, Eq(List("nw")), Some(":district.region/")))
      ) -->
      //  Some things to notice:
      //  - Enum values are prefixed with their namespace ("nw" becomes ":district.region/nw")
      //  - Multiple values of many-cardinality attributes each get their own statement ("my" + "favorites")
      //
      //       operation            temp id                 attribute                value
      """List(
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/name        ,  AAA                           ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/url         ,  myUrl                         ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/type        ,  :community.type/twitter       ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/orgtype     ,  :community.orgtype/personal   ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/category    ,  my                            ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/category    ,  favorites                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/neighborhood,  #db/id[:db.part/user -1000002]),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :neighborhood/name     ,  myNeighborhood                ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :neighborhood/district ,  #db/id[:db.part/user -1000003]),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :district/name         ,  myDistrict                    ),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :district/region       ,  :district.region/nw           )
        |)""".stripMargin


    /** Use molecule as template to insert matching data sets ********************************************/

    m(Community.name.url.`type`.orgtype.category.Neighborhood.name.District.name.region) -->
      Model(List(
        Atom("community", "name", "String", 1, VarValue, None),
        Atom("community", "url", "String", 1, VarValue, None),
        Atom("community", "type", "String", 1, EnumVal, Some(":community.type/")),
        Atom("community", "orgtype", "String", 1, EnumVal, Some(":community.orgtype/")),
        Atom("community", "category", "String", 2, VarValue, None),
        Bond("community", "neighborhood", "neighborhood", 1),
        Atom("neighborhood", "name", "String", 1, VarValue, None),
        Bond("neighborhood", "district", "district", 1),
        Atom("district", "name", "String", 1, VarValue, None),
        Atom("district", "region", "String", 1, EnumVal, Some(":district.region/")))
      ) -->
      List(
        List("DDD Blogging Georgetown", "http://www.blogginggeorgetown.com/", "blog", "commercial", Set("DD cat 1", "DD cat 2"), "DD Georgetown", "Greater Duwamish", "s"),
        List("DDD Interbay District Blog", "http://interbayneighborhood.neighborlogs.com/", "blog", "community", Set("DD cat 3"), "DD Interbay", "Magnolia/Queen Anne", "w")
      ) -->
      // Semantically identical to the previous transaction
      """List(
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/name        ,  DDD Blogging Georgetown                      ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/url         ,  http://www.blogginggeorgetown.com/           ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/type        ,  :community.type/blog                         ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/orgtype     ,  :community.orgtype/commercial                ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/category    ,  DD cat 1                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/category    ,  DD cat 2                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000001],  :community/neighborhood,  #db/id[:db.part/user -1000002]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :neighborhood/name     ,  DD Georgetown                                ),
        |  List(:db/add,  #db/id[:db.part/user -1000002],  :neighborhood/district ,  #db/id[:db.part/user -1000003]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :district/name         ,  Greater Duwamish                             ),
        |  List(:db/add,  #db/id[:db.part/user -1000003],  :district/region       ,  :district.region/s                           ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :community/name        ,  DDD Interbay District Blog                   ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :community/url         ,  http://interbayneighborhood.neighborlogs.com/),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :community/type        ,  :community.type/blog                         ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :community/orgtype     ,  :community.orgtype/community                 ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :community/category    ,  DD cat 3                                     ),
        |  List(:db/add,  #db/id[:db.part/user -1000004],  :community/neighborhood,  #db/id[:db.part/user -1000005]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000005],  :neighborhood/name     ,  DD Interbay                                  ),
        |  List(:db/add,  #db/id[:db.part/user -1000005],  :neighborhood/district ,  #db/id[:db.part/user -1000006]               ),
        |  List(:db/add,  #db/id[:db.part/user -1000006],  :district/name         ,  Magnolia/Queen Anne                          ),
        |  List(:db/add,  #db/id[:db.part/user -1000006],  :district/region       ,  :district.region/w                           )
        |)""".stripMargin
  }


  "Manipulating data - update/retract" >> {

    val belltownId = Community.e.name_("belltown").get.head


    // One-cardinality attributes ..............................

    // Assert new value
    testUpdateMolecule(
      Community(belltownId).name("belltown 2").url("url 2")
    ) -->
      Model(List(
        Meta("community", "eid_", "e", NoValue, Eq(List(17592186045886L))),
        Atom("community", "name", "String", 1, Eq(List("belltown 2")), None),
        Atom("community", "url", "String", 1, Eq(List("url 2")), None))
      ) -->
      """List(
        |  List(:db/add,  17592186045886                ,  :community/name,  belltown 2),
        |  List(:db/add,  17592186045886                ,  :community/url ,  url 2     )
        |)""".stripMargin


    // Many-cardinality attributes ............................

    // Replace category
    // Retracts current value an asserts new value
    testUpdateMolecule(
      Community(belltownId).category.replace("news" -> "Cool news")
    ) -->
      Model(List(
        Meta("community", "eid_", "e", NoValue, Eq(List(17592186045886L))),
        Atom("community", "category", "String", 2, ReplaceValue(Seq("news" -> "Cool news")), None))
      ) -->
      """List(
        |  List(:db/retract,  17592186045886                ,  :community/category,  news     ),
        |  List(:db/add    ,  17592186045886                ,  :community/category,  Cool news)
        |)""".stripMargin


    // Replace multiple categories
    testUpdateMolecule(
      Community(belltownId).category.replace(
        "Cool news" -> "Super cool news",
        "events" -> "Super cool events"
      )
    ) -->
      Model(List(
        Meta("community", "eid_", "e", NoValue, Eq(List(17592186045886L))),
        Atom("community", "category", "String", 2, ReplaceValue(Seq(
          "Cool news" -> "Super cool news",
          "events" -> "Super cool events")), None))
      ) -->
      """List(
        |  List(:db/retract,  17592186045886                ,  :community/category,  Cool news        ),
        |  List(:db/add    ,  17592186045886                ,  :community/category,  Super cool news  ),
        |  List(:db/retract,  17592186045886                ,  :community/category,  events           ),
        |  List(:db/add    ,  17592186045886                ,  :community/category,  Super cool events)
        |)""".stripMargin


    // Add a category
    testUpdateMolecule(
      Community(belltownId).category.assert("extra category")
    ) -->
      Model(List(
        Meta("community", "eid_", "e", NoValue, Eq(List(17592186045886L))),
        Atom("community", "category", "String", 2, AssertValue(List("extra category")), None))
      ) -->
      """List(
        |  List(:db/add,  17592186045886                ,  :community/category,  extra category)
        |)""".stripMargin


    // Remove a category
    testUpdateMolecule(
      Community(belltownId).category.retract("Super cool events")
    ) -->
      Model(List(
        Meta("community", "eid_", "e", NoValue, Eq(List(17592186045886L))),
        Atom("community", "category", "String", 2, RetractValue(List("Super cool events")), None))
      ) -->
      """List(
        |  List(:db/retract,  17592186045886                ,  :community/category,  Super cool events)
        |)""".stripMargin


    // Mixing updates and deletes..........................

    // Applying nothing (empty parenthesises) finds and retract all values of an attribute
    // Note how the name is updated at the same time
    testUpdateMolecule(
      Community(belltownId).name("belltown 3").url().category()
    ) -->
      Model(List(
        Meta("community", "eid_", "e", NoValue, Eq(List(17592186045886L))),
        Atom("community", "name", "String", 1, Eq(List("belltown 3")), None),
        Atom("community", "url", "String", 1, Eq(List()), None),
        Atom("community", "category", "String", 2, Eq(List()), None))
      ) -->
      """List(
        |  List(:db/add    ,  17592186045886                ,  :community/name    ,  belltown 3                    ),
        |  List(:db/retract,  17592186045886                ,  :community/url     ,  http://www.belltownpeople.com/),
        |  List(:db/retract,  17592186045886                ,  :community/category,  news                          ),
        |  List(:db/retract,  17592186045886                ,  :community/category,  events                        )
        |)""".stripMargin
  }
}