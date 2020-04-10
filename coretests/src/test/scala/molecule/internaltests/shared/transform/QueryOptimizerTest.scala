package molecule.internaltests.shared.transform

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneOffset}
import molecule.api.out5._
import molecule.ast.query.{Query, _}
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.{Model2Query, QueryOptimizer}


class QueryOptimizerTest extends CoreSpec {
  sequential

  var time1 = System.currentTimeMillis()

  def time(i: Int): Unit = {
    val time2     = System.currentTimeMillis()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    val d         = LocalDateTime.ofInstant(Instant.ofEpochMilli(time2 - time1), ZoneOffset.UTC)
    println(s"$i: " + formatter.format(d))
    time1 = System.currentTimeMillis()
  }

  class Setup extends CoreSetup {
    println("====================")

    def test(i: Int, q: String): Unit = {
      (1 to 10).foreach(_ => conn.q(q))
      time(i)
    }

    val max  = 10000
    val data = (1 to max).map(i => (i, i, i))
    Ns.int.Ref1.int1.Ref2.int2 insert data

    time(0)
    println("-----")

    val middle = max / 2
  }


    "1" in new Setup {

      //      Ns.int.>(middle).Ref1.int1.Ref2.int2.debugGet

      QueryOptimizer(
        Query(
          Find(List(
            Var("b"),
            Var("d"),
            Var("f"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("b"), Val(5000)), ScalarBinding(Var("b2"))),
            Funct(">", Seq(Var("b2"), Val(0)), NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
            DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
            DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
            DataClause(ImplDS, Var("e"), KW("Ref2", "int2", ""), Var("f"), Empty, NoBinding))))
      ) === Query(
        Find(List(
          Var("b"),
          Var("d"),
          Var("f"))),
        Where(List(
          Funct(".compareTo ^Long", Seq(Var("b"), Val(5000)), ScalarBinding(Var("b2"))),
          Funct(">", Seq(Var("b2"), Val(0)), NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("Ref2", "int2", ""), Var("f"), Empty, NoBinding))))

      val q =
        s"""[:find  ?b ?d ?f
           | :where
           |        [?a :Ns/int ?b]
           |        [(.compareTo ^Long ?b $middle) ?b2]
           |        [(> ?b2 0)]
           |        [?a :Ns/ref1 ?c]
           |        [?c :Ref1/int1 ?d]
           |        [?c :Ref1/ref2 ?e]
           |        [?e :Ref2/int2 ?f]
           |        ]""".stripMargin

      val q1 =
        s"""[:find  ?b ?d ?f
           | :where [(.compareTo ^Long ?b $middle) ?b2]
           |        [(> ?b2 0)]
           |        [?a :Ns/int ?b]
           |        [?a :Ns/ref1 ?c]
           |        [?c :Ref1/int1 ?d]
           |        [?c :Ref1/ref2 ?e]
           |        [?e :Ref2/int2 ?f]]""".stripMargin

      test(2, q)
      test(2, q)
      test(2, q)
      println("---------")
      test(3, q1)
      test(3, q1)
      test(3, q1)

      ok
    }


    "2" in new Setup {

      //      Ns.int.Ref1.int1.>(middle).Ref2.int2.debugGet

      QueryOptimizer(
        Query(
          Find(List(
            Var("b"),
            Var("d"),
            Var("f"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
            DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("d"), Val(5000)), ScalarBinding(Var("d2"))),
            Funct(">", Seq(Var("d2"), Val(0)), NoBinding),
            DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
            DataClause(ImplDS, Var("e"), KW("Ref2", "int2", ""), Var("f"), Empty, NoBinding))))
      ) === Query(
        Find(List(
          Var("b"),
          Var("d"),
          Var("f"))),
        Where(List(
          Funct(".compareTo ^Long", Seq(Var("d"), Val(5000)), ScalarBinding(Var("d2"))),
          Funct(">", Seq(Var("d2"), Val(0)), NoBinding),

          DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),

          DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("e"), KW("Ref2", "int2", ""), Var("f"), Empty, NoBinding))))

      val q =
        s"""[:find  ?b ?d ?f
           | :where
           |        [?a :Ns/int ?b]
           |        [?a :Ns/ref1 ?c]
           |        [?c :Ref1/int1 ?d]
           |        [(.compareTo ^Long ?d $middle) ?d2]
           |        [(> ?d2 0)]
           |        [?c :Ref1/ref2 ?e]
           |        [?e :Ref2/int2 ?f]
           |        ]""".stripMargin

      val q1 =
        s"""[:find  ?b ?d ?f
           | :where
           |        [(.compareTo ^Long ?d $middle) ?d2]
           |        [(> ?d2 0)]
           |        [?c :Ref1/int1 ?d]
           |        [?a :Ns/ref1 ?c]
           |        [?a :Ns/int ?b]
           |        [?c :Ref1/ref2 ?e]
           |        [?e :Ref2/int2 ?f]
           |        ]""".stripMargin

      test(2, q)
      test(2, q)
      test(2, q)
      println("---------")
      test(3, q1)
      test(3, q1)
      test(3, q1)

      ok
    }


    "3" in new Setup {

      //      Ns.int.Ref1.int1.Ref2.int2.>(middle).debugGet

      QueryOptimizer(
        Query(
          Find(List(
            Var("b"),
            Var("d"),
            Var("f"))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
            DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
            DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
            DataClause(ImplDS, Var("e"), KW("Ref2", "int2", ""), Var("f"), Empty, NoBinding),
            Funct(".compareTo ^Long", Seq(Var("f"), Val(5000)), ScalarBinding(Var("f2"))),
            Funct(">", Seq(Var("f2"), Val(0)), NoBinding))))
      ) === Query(
        Find(List(
          Var("b"),
          Var("d"),
          Var("f"))),
        Where(List(
          Funct(".compareTo ^Long", Seq(Var("f"), Val(5000)), ScalarBinding(Var("f2"))),
          Funct(">", Seq(Var("f2"), Val(0)), NoBinding),
          DataClause(ImplDS, Var("e"), KW("Ref2", "int2", ""), Var("f"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
        )))

      val q =
        s"""[:find  ?b ?d ?f
           | :where [?a :Ns/int ?b]
           |        [?a :Ns/ref1 ?c]
           |        [?c :Ref1/int1 ?d]
           |        [?c :Ref1/ref2 ?e]
           |        [?e :Ref2/int2 ?f]
           |        [(.compareTo ^Long ?f $middle) ?f2]
           |        [(> ?f2 0)]]""".stripMargin

      val q1 =
        s"""[:find  ?b ?d ?f
           | :where
           |        [(> ?f2 0)]
           |        [(.compareTo ^Long ?f $middle) ?f2]
           |        [?e :Ref2/int2 ?f]
           |        [?c :Ref1/ref2 ?e]
           |        [?a :Ns/ref1 ?c]
           |        [?a :Ns/int ?b]
           |        [?c :Ref1/int1 ?d]
           |        ]""".stripMargin

      test(2, q)
      test(2, q)
      test(2, q)
      println("---------")
      test(3, q1)
      test(3, q1)
      test(3, q1)

      ok
    }


    "4" in new Setup {

      QueryOptimizer(
        Query(
          Find(List(
            Var("a"),
            Var("c"),
            Var("d"),
            Pull("a__3", "music_WorkTitle", "short", None))),
          Where(List(
            DataClause(ImplDS, Var("a"), KW("music_WorkTitle", "title", ""), Var("c"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("music_WorkTitle", "short", ""), Val("x"), Empty, NoBinding),
            DataClause(ImplDS, Var("a"), KW("music_WorkTitle", "short", ""), Var("d"), Empty, NoBinding),
            Funct("molecule.util.fns/bind", Seq(Var("a")), ScalarBinding(Var("a__3"))))))
      ) === Query(
        Find(List(
          Var("a"),
          Var("c"),
          Var("d"),
          Pull("a__3", "music_WorkTitle", "short", None))),
        Where(List(
          Funct("molecule.util.fns/bind", Seq(Var("a")), ScalarBinding(Var("a__3"))),
          DataClause(ImplDS, Var("a"), KW("music_WorkTitle", "short", ""), Val("x"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("music_WorkTitle", "title", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("music_WorkTitle", "short", ""), Var("d"), Empty, NoBinding),
        )))
    }


    "Using un-optimized raw query" in new Setup {

      Ns.str("a").int(42).save

      // Binding 42 is promoted as first clause in optimized query
      m(Ns.str.int(42))._query === Query(
        Find(List(
          Var("b"),
          Var("c"))),
        Where(List(
          Funct("ground 42", Seq(Empty), ScalarBinding(Var("c"))),
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "str", ""), Var("b"), Empty, NoBinding)
        )))

      Ns.str.int(42).get.head === ("a", 42)

      m(Ns.str.int(42))._rawQuery === Query(
        Find(List(
          Var("b"),
          Var("c"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Ns", "str", ""), Var("b"), Empty, NoBinding),
          Funct("ground 42", Seq(Empty), ScalarBinding(Var("c"))),
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("c"), Empty, NoBinding),
        )))

      conn.q(Ns.str.int(42)._rawQuery.toMap).head === List("a", 42)
    }


  "Fulltext" in new Setup {

    //    val m1 = m(Ns.int.Ref1.str1.contains("hello"))
    //    println(m1._rawQuery)
    //    println(m1._query)

    QueryOptimizer(
      Query(
        Find(List(
          Var("b"),
          Var("d"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
          Funct("fulltext", Seq(DS, KW("Ref1", "str1", ""), Val("hello")), RelationBinding(List(Var("c"), Var("d")))))))
    ) === Query(
      Find(List(
        Var("b"),
        Var("d"))),
      Where(List(
        Funct("fulltext", Seq(DS, KW("Ref1", "str1", ""), Val("hello")), RelationBinding(List(Var("c"), Var("d")))),
        DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
      )))


    //    val m2 = m(Ns.int.Ref1.str1.contains("hello"))
    //    println(m2._rawQuery)
    //    println(m2._query)

    QueryOptimizer(
      Query(
        Find(List(
          Var("b"),
          Var("d"),
          Var("f"))),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
          DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
          DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
          Funct("fulltext", Seq(DS, KW("Ref2", "str2", ""), Val("hello")), RelationBinding(List(Var("e"), Var("f")))))))
    ) === Query(
      Find(List(
        Var("b"),
        Var("d"),
        Var("f"))),
      Where(List(
        Funct("fulltext", Seq(DS, KW("Ref2", "str2", ""), Val("hello")), RelationBinding(List(Var("e"), Var("f")))),
        DataClause(ImplDS, Var("c"), KW("Ref1", "ref2", "Ref2"), Var("e"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "ref1", "Ref1"), Var("c"), Empty, NoBinding),
        DataClause(ImplDS, Var("a"), KW("Ns", "int", ""), Var("b"), Empty, NoBinding),
        DataClause(ImplDS, Var("c"), KW("Ref1", "int1", ""), Var("d"), Empty, NoBinding),
      )))
  }
}