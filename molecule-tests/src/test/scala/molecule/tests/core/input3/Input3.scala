package molecule.tests.core.input3

import java.net.URI
import molecule.core.api._
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in3_out4._
import molecule.core.api.exception.Molecule_3_Exception
import molecule.setup.TestSpec
import scala.reflect.ClassTag


class Input3 extends TestSpec {

  if (heavyInputTesting) {

    class Setup extends CoreSetup {

      def triplesTacit[I1, I2, I3, A](test: String,
                                      im: Molecule_3.Molecule_3_01[I1, I2, I3, A],
                                      im1: Molecule_1.Molecule_1_01[I1, A],
                                      im2: Molecule_1.Molecule_1_01[I2, A],
                                      im3: Molecule_1.Molecule_1_01[I3, A],
                                      inOut1: Seq[(I1, List[A])],
                                      inOut2: Seq[(I2, List[A])],
                                      inOut3: Seq[(I3, List[A])]
                                     )(implicit ev: ClassTag[A]) = {
        println("------------------------")
        println(s"""$test   pairs tacit""")
        var i = 0
        for {
          (in1, out1) <- inOut1
          (in2, out2) <- inOut2
          (in3, out3) <- inOut3
        } yield {
          try {
            i += 1
            //          im.apply(in1, in2, in3).inspectGet
            im.apply(in1, in2, in3).get === out1.intersect(out2).intersect(out3)
          } catch {
            case e: Throwable =>
              val expected =
                s"""
                   |In/out 1: $in1   -->   $out1
                   |In/out 2: $in2   -->   $out2
                   |In/out 2: $in3   -->   $out3
                   |Expected: ${out1.intersect(out2).intersect(out3)}""".stripMargin
              println(expected)
              val result = "Result  : " + im(in1, in2, in3).get
              println(result)
              println("Result 1: " + im1(in1).get)
              println("Result 2: " + im2(in2).get)
              println("Result 2: " + im3(in3).get)
              im(in1, in2, in3).inspectGet
              throw new RuntimeException(expected + "\n" + result)
          }
        }
        println(s"$test   $i tests passed")
      }


      def groupsTacit111[I1, I2, I3](test: String, im: Molecule_3.Molecule_3_01[I1, I2, I3, Int],
                                     im1: Molecule_1.Molecule_1_01[I1, Int],
                                     im2: Molecule_1.Molecule_1_01[I2, Int],
                                     im3: Molecule_1.Molecule_1_01[I3, Int],
                                     inOut1: Seq[(Seq[I1], List[Int])],
                                     inOut2: Seq[(Seq[I2], List[Int])],
                                     inOut3: Seq[(Seq[I3], List[Int])]
                                    ) = {
        println("------------------------")
        println(s"""$test   tacit 1+1+1""")
        val combos = (for {
          (in1, out1) <- inOut1
          (in2, out2) <- inOut2
          (in3, out3) <- inOut3
        } yield {
          (in1.distinct, in2.distinct, in3.distinct, out1, out2, out3)
        }).distinct

        var i = 0
        //              combos.take(500) foreach { case (in1, in2, in3, out1, out2, out3) =>
        combos foreach { case (in1, in2, in3, out1, out2, out3) =>
          i += 1
          //          println(s"$in1   ++   $in2     -->     $out1   ++   $out2")
          try {
            im.apply(in1, in2, in3).get.sorted === out1.intersect(out2).intersect(out3).sorted
          } catch {
            case e: Throwable =>
              val expected =
                s"""
                   |In/out 1: $in1   -->   $out1
                   |In/out 2: $in2   -->   $out2
                   |In/out 3: $in3   -->   $out3
                   |Expected: ${out1.intersect(out2).intersect(out3)}""".stripMargin
              println(expected)
              val result = "Result  : " + im(in1, in2, in3).get
              im(in1, in2, in3).inspectGet
              println(result)
              println("Result 1: " + im1(in1).get)
              println("Result 2: " + im2(in2).get)
              println("Result 3: " + im3(in3).get)
              throw new RuntimeException(expected + "\n" + result)
          }
        }
        println(s"$test   $i tests passed")
      }


      Ns.int.longs$.enums$.uris$ insert List(
        (1, Some(Set(long1, long2, long3)), Some(Set(enum1, enum2, enum3)), Some(Set(uri1, uri2, uri3))),
        (2, Some(Set(long2, long3, long4)), Some(Set(enum2, enum3, enum4)), Some(Set(uri2, uri3, uri4))),
        (3, Some(Set(long3, long4, long5)), Some(Set(enum3, enum4, enum5)), Some(Set(uri3, uri4, uri5))),

        (4, Some(Set(long1, long2, long3)), None, None),
        (5, None, Some(Set(enum1, enum2, enum3)), None),
        (6, None, None, Some(Set(uri1, uri2, uri3))),
        (7, None, None, None)
      )

    }


    "Pairs" in new Setup {

      val m1Eq = m(Ns.int.longs_(?))
      val d1Eq = List(
        (Set[Long](), List(5, 6, 7)),
        (Set(long0), Nil),
        (Set(long1), List(1, 4)),
        (Set(long2), List(1, 2, 4)),
        (Set(long3), List(1, 2, 3, 4)),
        (Set(long4), List(2, 3)),
        (Set(long5), List(3)),
        (Set(long0, long1), Nil),
        (Set(long1, long2), List(1, 4)),
        (Set(long2, long3), List(1, 2, 4)),
        (Set(long3, long4), List(2, 3)),
        (Set(long4, long5), List(3))
      )

      val m1Not = m(Ns.int.longs_.not(?))
      val d1Not = List(
        (Set[Long](), List(1, 2, 3, 4)),
        (Set(long0), List(1, 2, 3, 4)),
        (Set(long1), List(2, 3)),
        (Set(long2), List(3)),
        (Set(long3), Nil),
        (Set(long4), List(1, 4)),
        (Set(long5), List(1, 2, 4)),
        (Set(long0, long1), List(1, 2, 3, 4)),
        (Set(long1, long2), List(2, 3)),
        (Set(long2, long3), List(3)),
        (Set(long3, long4), List(1, 4)),
        (Set(long4, long5), List(1, 2, 4))
      )

      val m1Ge = m(Ns.int.longs_.>=(?))
      val d1Ge = List(
        (Set[Long](), List(1, 2, 3, 4)),
        (Set(long0), List(1, 2, 3, 4)),
        (Set(long1), List(1, 2, 3, 4)),
        (Set(long2), List(1, 2, 3, 4)),
        (Set(long3), List(1, 2, 3, 4)),
        (Set(long4), List(2, 3)),
        (Set(long5), List(3))
      )

      val m2Eq = m(Ns.int.enums_(?))
      val d2Eq = List(
        (Set[String](), List(4, 6, 7)),
        (Set(enum0), Nil),
        (Set(enum1), List(1, 5)),
        (Set(enum2), List(1, 2, 5)),
        (Set(enum3), List(1, 2, 3, 5)),
        (Set(enum4), List(2, 3)),
        (Set(enum5), List(3)),
        (Set(enum0, enum1), Nil),
        (Set(enum1, enum2), List(1, 5)),
        (Set(enum2, enum3), List(1, 2, 5)),
        (Set(enum3, enum4), List(2, 3)),
        (Set(enum4, enum5), List(3))
      )

      val m2Not = m(Ns.int.enums_.not(?))
      val d2Not = List(
        (Set[String](), List(1, 2, 3, 5)),
        (Set(enum0), List(1, 2, 3, 5)),
        (Set(enum1), List(2, 3)),
        (Set(enum2), List(3)),
        (Set(enum3), Nil),
        (Set(enum4), List(1, 5)),
        (Set(enum5), List(1, 2, 5)),
        (Set(enum0, enum1), List(1, 2, 3, 5)),
        (Set(enum1, enum2), List(2, 3)),
        (Set(enum2, enum3), List(3)),
        (Set(enum3, enum4), List(1, 5)),
        (Set(enum4, enum5), List(1, 2, 5))
      )

      val m2Lt = m(Ns.int.enums_.<(?))
      val d2Lt = List(
        (Set[String](), List(1, 2, 3, 5)),
        (Set(enum0), Nil),
        (Set(enum1), Nil),
        (Set(enum2), List(1, 5)),
        (Set(enum3), List(1, 2, 5)),
        (Set(enum4), List(1, 2, 3, 5)),
        (Set(enum5), List(1, 2, 3, 5))
      )

      val m3Eq = m(Ns.int.uris_(?))
      val d3Eq = List(
        (Set[URI](), List(4, 5, 7)),
        (Set(uri0), Nil),
        (Set(uri1), List(1, 6)),
        (Set(uri2), List(1, 2, 6)),
        (Set(uri3), List(1, 2, 3, 6)),
        (Set(uri4), List(2, 3)),
        (Set(uri5), List(3)),
        (Set(uri0, uri1), Nil),
        (Set(uri1, uri2), List(1, 6)),
        (Set(uri2, uri3), List(1, 2, 6)),
        (Set(uri3, uri4), List(2, 3)),
        (Set(uri4, uri5), List(3))
      )

      val m3Not = m(Ns.int.uris_.not(?))
      val d3Not = List(
        (Set[URI](), List(1, 2, 3, 6)),
        (Set(uri0), List(1, 2, 3, 6)),
        (Set(uri1), List(2, 3)),
        (Set(uri2), List(3)),
        (Set(uri3), Nil),
        (Set(uri4), List(1, 6)),
        (Set(uri5), List(1, 2, 6)),
        (Set(uri0, uri1), List(1, 2, 3, 6)),
        (Set(uri1, uri2), List(2, 3)),
        (Set(uri2, uri3), List(3)),
        (Set(uri3, uri4), List(1, 6)),
        (Set(uri4, uri5), List(1, 2, 6))
      )

      val m3Gt = m(Ns.int.uris_.>(?))
      val d3Gt = List(
        (Set[URI](), List(1, 2, 3, 6)),
        (Set(uri0), List(1, 2, 3, 6)),
        (Set(uri1), List(1, 2, 3, 6)),
        (Set(uri2), List(1, 2, 3, 6)),
        (Set(uri3), List(2, 3)),
        (Set(uri4), List(3)),
        (Set(uri5), Nil)
      )

      val combinations = List(
        ("Eq Eq Eq", m(Ns.int.longs_(?).enums_(?).uris_(?)), m1Eq, m2Eq, m3Eq, d1Eq, d2Eq, d3Eq),
        ("Eq Eq !=", m(Ns.int.longs_(?).enums_(?).uris_.not(?)), m1Eq, m2Eq, m3Not, d1Eq, d2Eq, d3Not),
        ("Eq Eq Gt", m(Ns.int.longs_(?).enums_(?).uris_.>(?)), m1Eq, m2Eq, m3Gt, d1Eq, d2Eq, d3Gt),
        ("Eq != Eq", m(Ns.int.longs_(?).enums_.not(?).uris_(?)), m1Eq, m2Not, m3Eq, d1Eq, d2Not, d3Eq),
        ("Eq != !=", m(Ns.int.longs_(?).enums_.not(?).uris_.not(?)), m1Eq, m2Not, m3Not, d1Eq, d2Not, d3Not),
        ("Eq != Gt", m(Ns.int.longs_(?).enums_.not(?).uris_.>(?)), m1Eq, m2Not, m3Gt, d1Eq, d2Not, d3Gt),
        ("Eq Lt Eq", m(Ns.int.longs_(?).enums_.<(?).uris_(?)), m1Eq, m2Lt, m3Eq, d1Eq, d2Lt, d3Eq),
        ("Eq Lt !=", m(Ns.int.longs_(?).enums_.<(?).uris_.not(?)), m1Eq, m2Lt, m3Not, d1Eq, d2Lt, d3Not),
        ("Eq Lt Gt", m(Ns.int.longs_(?).enums_.<(?).uris_.>(?)), m1Eq, m2Lt, m3Gt, d1Eq, d2Lt, d3Gt),

        ("!= Eq Eq", m(Ns.int.longs_.not(?).enums_(?).uris_(?)), m1Not, m2Eq, m3Eq, d1Not, d2Eq, d3Eq),
        ("!= Eq !=", m(Ns.int.longs_.not(?).enums_(?).uris_.not(?)), m1Not, m2Eq, m3Not, d1Not, d2Eq, d3Not),
        ("!= Eq Gt", m(Ns.int.longs_.not(?).enums_(?).uris_.>(?)), m1Not, m2Eq, m3Gt, d1Not, d2Eq, d3Gt),
        ("!= != Eq", m(Ns.int.longs_.not(?).enums_.not(?).uris_(?)), m1Not, m2Not, m3Eq, d1Not, d2Not, d3Eq),
        ("!= != !=", m(Ns.int.longs_.not(?).enums_.not(?).uris_.not(?)), m1Not, m2Not, m3Not, d1Not, d2Not, d3Not),
        ("!= != Gt", m(Ns.int.longs_.not(?).enums_.not(?).uris_.>(?)), m1Not, m2Not, m3Gt, d1Not, d2Not, d3Gt),
        ("!= Lt Eq", m(Ns.int.longs_.not(?).enums_.<(?).uris_(?)), m1Not, m2Lt, m3Eq, d1Not, d2Lt, d3Eq),
        ("!= Lt !=", m(Ns.int.longs_.not(?).enums_.<(?).uris_.not(?)), m1Not, m2Lt, m3Not, d1Not, d2Lt, d3Not),
        ("!= Lt Gt", m(Ns.int.longs_.not(?).enums_.<(?).uris_.>(?)), m1Not, m2Lt, m3Gt, d1Not, d2Lt, d3Gt),

        ("Ge Eq Eq", m(Ns.int.longs_.>=(?).enums_(?).uris_(?)), m1Ge, m2Eq, m3Eq, d1Ge, d2Eq, d3Eq),
        ("Ge Eq !=", m(Ns.int.longs_.>=(?).enums_(?).uris_.not(?)), m1Ge, m2Eq, m3Not, d1Ge, d2Eq, d3Not),
        ("Ge Eq Gt", m(Ns.int.longs_.>=(?).enums_(?).uris_.>(?)), m1Ge, m2Eq, m3Gt, d1Ge, d2Eq, d3Gt),
        ("Ge != Eq", m(Ns.int.longs_.>=(?).enums_.not(?).uris_(?)), m1Ge, m2Not, m3Eq, d1Ge, d2Not, d3Eq),
        ("Ge != !=", m(Ns.int.longs_.>=(?).enums_.not(?).uris_.not(?)), m1Ge, m2Not, m3Not, d1Ge, d2Not, d3Not),
        ("Ge != Gt", m(Ns.int.longs_.>=(?).enums_.not(?).uris_.>(?)), m1Ge, m2Not, m3Gt, d1Ge, d2Not, d3Gt),
        ("Ge Lt Eq", m(Ns.int.longs_.>=(?).enums_.<(?).uris_(?)), m1Ge, m2Lt, m3Eq, d1Ge, d2Lt, d3Eq),
        ("Ge Lt !=", m(Ns.int.longs_.>=(?).enums_.<(?).uris_.not(?)), m1Ge, m2Lt, m3Not, d1Ge, d2Lt, d3Not),
        ("Ge Lt Gt", m(Ns.int.longs_.>=(?).enums_.<(?).uris_.>(?)), m1Ge, m2Lt, m3Gt, d1Ge, d2Lt, d3Gt)
      )
      combinations.foreach { case (test, im, im1, im2, im3, inOut1, inOut2, inOut3) => triplesTacit(test, im, im1, im2, im3, inOut1, inOut2, inOut3) }
    }


    "Groups" >> {

      "1 input each" in new Setup {

        val m1Eq = m(Ns.int.longs_(?))
        val d1Eq = List(
          (List(Set[Long]()), List(5, 6, 7)),
          (List(Set(long0)), Nil),
          (List(Set(long1)), List(1, 4)),
          (List(Set(long2)), List(1, 2, 4)),
          (List(Set(long3)), List(1, 2, 3, 4)),
          (List(Set(long4)), List(2, 3)),
          (List(Set(long5)), List(3)),
          (List(Set(long0, long1)), Nil),
          (List(Set(long1, long2)), List(1, 4)),
          (List(Set(long2, long3)), List(1, 2, 4)),
          (List(Set(long3, long4)), List(2, 3)),
          (List(Set(long4, long5)), List(3))
        )

        val m1Not = m(Ns.int.longs_.not(?))
        val d1Not = List(
          (List(Set[Long]()), List(1, 2, 3, 4)),
          (List(Set(long0)), List(1, 2, 3, 4)),
          (List(Set(long1)), List(2, 3)),
          (List(Set(long2)), List(3)),
          (List(Set(long3)), Nil),
          (List(Set(long4)), List(1, 4)),
          (List(Set(long5)), List(1, 2, 4)),
          (List(Set(long0, long1)), List(1, 2, 3, 4)),
          (List(Set(long1, long2)), List(2, 3)),
          (List(Set(long2, long3)), List(3)),
          (List(Set(long3, long4)), List(1, 4)),
          (List(Set(long4, long5)), List(1, 2, 4))
        )

        val m1Ge = m(Ns.int.longs_.>=(?))
        val d1Ge = List(
          (List(Set[Long]()), List(1, 2, 3, 4)),
          (List(Set(long0)), List(1, 2, 3, 4)),
          (List(Set(long1)), List(1, 2, 3, 4)),
          (List(Set(long2)), List(1, 2, 3, 4)),
          (List(Set(long3)), List(1, 2, 3, 4)),
          (List(Set(long4)), List(2, 3)),
          (List(Set(long5)), List(3))
        )

        val m2Eq = m(Ns.int.enums_(?))
        val d2Eq = List(
          (List(Set[String]()), List(4, 6, 7)),
          (List(Set(enum0)), Nil),
          (List(Set(enum1)), List(1, 5)),
          (List(Set(enum2)), List(1, 2, 5)),
          (List(Set(enum3)), List(1, 2, 3, 5)),
          (List(Set(enum4)), List(2, 3)),
          (List(Set(enum5)), List(3)),
          (List(Set(enum0, enum1)), Nil),
          (List(Set(enum1, enum2)), List(1, 5)),
          (List(Set(enum2, enum3)), List(1, 2, 5)),
          (List(Set(enum3, enum4)), List(2, 3)),
          (List(Set(enum4, enum5)), List(3))
        )

        val m2Not = m(Ns.int.enums_.not(?))
        val d2Not = List(
          (List(Set[String]()), List(1, 2, 3, 5)),
          (List(Set(enum0)), List(1, 2, 3, 5)),
          (List(Set(enum1)), List(2, 3)),
          (List(Set(enum2)), List(3)),
          (List(Set(enum3)), Nil),
          (List(Set(enum4)), List(1, 5)),
          (List(Set(enum5)), List(1, 2, 5)),
          (List(Set(enum0, enum1)), List(1, 2, 3, 5)),
          (List(Set(enum1, enum2)), List(2, 3)),
          (List(Set(enum2, enum3)), List(3)),
          (List(Set(enum3, enum4)), List(1, 5)),
          (List(Set(enum4, enum5)), List(1, 2, 5))
        )

        val m2Lt = m(Ns.int.enums_.<(?))
        val d2Lt = List(
          (List(Set[String]()), List(1, 2, 3, 5)),
          (List(Set(enum0)), Nil),
          (List(Set(enum1)), Nil),
          (List(Set(enum2)), List(1, 5)),
          (List(Set(enum3)), List(1, 2, 5)),
          (List(Set(enum4)), List(1, 2, 3, 5)),
          (List(Set(enum5)), List(1, 2, 3, 5))
        )

        val m3Eq = m(Ns.int.uris_(?))
        val d3Eq = List(
          (List(Set[URI]()), List(4, 5, 7)),
          (List(Set(uri0)), Nil),
          (List(Set(uri1)), List(1, 6)),
          (List(Set(uri2)), List(1, 2, 6)),
          (List(Set(uri3)), List(1, 2, 3, 6)),
          (List(Set(uri4)), List(2, 3)),
          (List(Set(uri5)), List(3)),
          (List(Set(uri0, uri1)), Nil),
          (List(Set(uri1, uri2)), List(1, 6)),
          (List(Set(uri2, uri3)), List(1, 2, 6)),
          (List(Set(uri3, uri4)), List(2, 3)),
          (List(Set(uri4, uri5)), List(3))
        )

        val m3Not = m(Ns.int.uris_.not(?))
        val d3Not = List(
          (List(Set[URI]()), List(1, 2, 3, 6)),
          (List(Set(uri0)), List(1, 2, 3, 6)),
          (List(Set(uri1)), List(2, 3)),
          (List(Set(uri2)), List(3)),
          (List(Set(uri3)), Nil),
          (List(Set(uri4)), List(1, 6)),
          (List(Set(uri5)), List(1, 2, 6)),
          (List(Set(uri0, uri1)), List(1, 2, 3, 6)),
          (List(Set(uri1, uri2)), List(2, 3)),
          (List(Set(uri2, uri3)), List(3)),
          (List(Set(uri3, uri4)), List(1, 6)),
          (List(Set(uri4, uri5)), List(1, 2, 6))
        )

        val m3Gt = m(Ns.int.uris_.>(?))
        val d3Gt = List(
          (List(Set[URI]()), List(1, 2, 3, 6)),
          (List(Set(uri0)), List(1, 2, 3, 6)),
          (List(Set(uri1)), List(1, 2, 3, 6)),
          (List(Set(uri2)), List(1, 2, 3, 6)),
          (List(Set(uri3)), List(2, 3)),
          (List(Set(uri4)), List(3)),
          (List(Set(uri5)), Nil)
        )


        val combinations = List(
          ("Eq Eq Eq", m(Ns.int.longs_(?).enums_(?).uris_(?)), m1Eq, m2Eq, m3Eq, d1Eq, d2Eq, d3Eq),
          ("Eq Eq !=", m(Ns.int.longs_(?).enums_(?).uris_.not(?)), m1Eq, m2Eq, m3Not, d1Eq, d2Eq, d3Not),
          ("Eq Eq Gt", m(Ns.int.longs_(?).enums_(?).uris_.>(?)), m1Eq, m2Eq, m3Gt, d1Eq, d2Eq, d3Gt),
          ("Eq != Eq", m(Ns.int.longs_(?).enums_.not(?).uris_(?)), m1Eq, m2Not, m3Eq, d1Eq, d2Not, d3Eq),
          ("Eq != !=", m(Ns.int.longs_(?).enums_.not(?).uris_.not(?)), m1Eq, m2Not, m3Not, d1Eq, d2Not, d3Not),
          ("Eq != Gt", m(Ns.int.longs_(?).enums_.not(?).uris_.>(?)), m1Eq, m2Not, m3Gt, d1Eq, d2Not, d3Gt),
          ("Eq Lt Eq", m(Ns.int.longs_(?).enums_.<(?).uris_(?)), m1Eq, m2Lt, m3Eq, d1Eq, d2Lt, d3Eq),
          ("Eq Lt !=", m(Ns.int.longs_(?).enums_.<(?).uris_.not(?)), m1Eq, m2Lt, m3Not, d1Eq, d2Lt, d3Not),
          ("Eq Lt Gt", m(Ns.int.longs_(?).enums_.<(?).uris_.>(?)), m1Eq, m2Lt, m3Gt, d1Eq, d2Lt, d3Gt),

          ("!= Eq Eq", m(Ns.int.longs_.not(?).enums_(?).uris_(?)), m1Not, m2Eq, m3Eq, d1Not, d2Eq, d3Eq),
          ("!= Eq !=", m(Ns.int.longs_.not(?).enums_(?).uris_.not(?)), m1Not, m2Eq, m3Not, d1Not, d2Eq, d3Not),
          ("!= Eq Gt", m(Ns.int.longs_.not(?).enums_(?).uris_.>(?)), m1Not, m2Eq, m3Gt, d1Not, d2Eq, d3Gt),
          ("!= != Eq", m(Ns.int.longs_.not(?).enums_.not(?).uris_(?)), m1Not, m2Not, m3Eq, d1Not, d2Not, d3Eq),
          ("!= != !=", m(Ns.int.longs_.not(?).enums_.not(?).uris_.not(?)), m1Not, m2Not, m3Not, d1Not, d2Not, d3Not),
          ("!= != Gt", m(Ns.int.longs_.not(?).enums_.not(?).uris_.>(?)), m1Not, m2Not, m3Gt, d1Not, d2Not, d3Gt),
          ("!= Lt Eq", m(Ns.int.longs_.not(?).enums_.<(?).uris_(?)), m1Not, m2Lt, m3Eq, d1Not, d2Lt, d3Eq),
          ("!= Lt !=", m(Ns.int.longs_.not(?).enums_.<(?).uris_.not(?)), m1Not, m2Lt, m3Not, d1Not, d2Lt, d3Not),
          ("!= Lt Gt", m(Ns.int.longs_.not(?).enums_.<(?).uris_.>(?)), m1Not, m2Lt, m3Gt, d1Not, d2Lt, d3Gt),

          ("Ge Eq Eq", m(Ns.int.longs_.>=(?).enums_(?).uris_(?)), m1Ge, m2Eq, m3Eq, d1Ge, d2Eq, d3Eq),
          ("Ge Eq !=", m(Ns.int.longs_.>=(?).enums_(?).uris_.not(?)), m1Ge, m2Eq, m3Not, d1Ge, d2Eq, d3Not),
          ("Ge Eq Gt", m(Ns.int.longs_.>=(?).enums_(?).uris_.>(?)), m1Ge, m2Eq, m3Gt, d1Ge, d2Eq, d3Gt),
          ("Ge != Eq", m(Ns.int.longs_.>=(?).enums_.not(?).uris_(?)), m1Ge, m2Not, m3Eq, d1Ge, d2Not, d3Eq),
          ("Ge != !=", m(Ns.int.longs_.>=(?).enums_.not(?).uris_.not(?)), m1Ge, m2Not, m3Not, d1Ge, d2Not, d3Not),
          ("Ge != Gt", m(Ns.int.longs_.>=(?).enums_.not(?).uris_.>(?)), m1Ge, m2Not, m3Gt, d1Ge, d2Not, d3Gt),
          ("Ge Lt Eq", m(Ns.int.longs_.>=(?).enums_.<(?).uris_(?)), m1Ge, m2Lt, m3Eq, d1Ge, d2Lt, d3Eq),
          ("Ge Lt !=", m(Ns.int.longs_.>=(?).enums_.<(?).uris_.not(?)), m1Ge, m2Lt, m3Not, d1Ge, d2Lt, d3Not),
          ("Ge Lt Gt", m(Ns.int.longs_.>=(?).enums_.<(?).uris_.>(?)), m1Ge, m2Lt, m3Gt, d1Ge, d2Lt, d3Gt)
        )
        combinations.foreach { case (test, im, im1, im2, im3, inOut1, inOut2, inOut3) => groupsTacit111(test, im, im1, im2, im3, inOut1, inOut2, inOut3) }
      }


      "2 inputs" in new Setup {

        m(Ns.int.longs_(?).enums_(?).uris_(?)).apply(
          List(Set(1L, 2L), Set(5L)), // 1, 3, 4
          List(Set("enum1")), // 1
          List(Set(uri2)) // 1, 2
        ).get === List(1)

        m(Ns.int.longs_(?).enums_(?).uris_(?)).apply(
          List(Set(1L, 2L), Set(5L)), // 1, 3, 4
          List(Set("enum4")), // 2, 3
          List(Set(uri3)) // 1, 2, 3, 6
        ).get === List(3)


        (m(Ns.int.ints(?).longs(?).strs(?)).apply(Nil, List(Set(1L)), List(Set("a"))).get must throwA[Molecule_3_Exception])
          .message === "Got the exception molecule.core.input.exception.Molecule_3_Exception: " +
          "Can only apply empty list (Nil) to a tacit input attribute. Please make input attr tacit: `ints` --> `ints_`"

        (m(Ns.int.ints_.<=(?).longs_(?).strs_(?)).apply(List(Set(1), Set(2)), List(Set(1L)), List(Set("a"))).get must throwA[Molecule_3_Exception])
          .message === "Got the exception molecule.core.input.exception.Molecule_3_Exception: " +
          s"Can't apply multiple values to input attribute `:Ns/ints` having expression (<, >, <=, >=, !=)"
      }
    }
  }
}