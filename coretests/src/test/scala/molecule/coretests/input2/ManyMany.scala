package molecule.coretests.input2

import java.net.URI
import molecule.api.in2_out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.input.exception.InputMolecule_2_Exception
import molecule.input.{InputMolecule_1, InputMolecule_2}
import scala.reflect.ClassTag


class ManyMany extends CoreSpec {
  sequential

  class Setup extends CoreSetup {

    def pairsTacit[I1, I2, A](test: String,
                              im: InputMolecule_2.InputMolecule_2_01[I1, I2, A],
                              im1: InputMolecule_1.InputMolecule_1_01[I1, A],
                              im2: InputMolecule_1.InputMolecule_1_01[I2, A],
                              inOut1: Seq[(I1, List[A])],
                              inOut2: Seq[(I2, List[A])]
                             )(implicit ev: ClassTag[A]) = {
      println("------------------------")
      println(s"""$test   pairs tacit""")
      var i = 0
      for {
        (in1, out1) <- inOut1
        (in2, out2) <- inOut2
      } yield {
        try {
          i += 1
          im.apply(in1, in2).get === out1.intersect(out2)
        } catch {
          case e: Throwable =>
            val expected =
              s"""
                 |In/out 1: $in1   -->   $out1
                 |In/out 2: $in2   -->   $out2
                 |Expected: ${out1.intersect(out2)}""".stripMargin
            println(expected)
            val result = "Result  : " + im(in1, in2).get
            println(result)
            println("Result 1: " + im1(in1).get)
            println("Result 2: " + im2(in2).get)
            throw new RuntimeException(expected + "\n" + result)
        }
      }
      println(s"$test   $i tests passed")
    }

    def pairsTacit22[I1, I2](test: String,
                             im: InputMolecule_2.InputMolecule_2_01[I1, I2, Int],
                             im1: InputMolecule_1.InputMolecule_1_01[I1, Int],
                             im2: InputMolecule_1.InputMolecule_1_01[I2, Int],
                             inOut1: Seq[(I1, List[Int])],
                             inOut2: Seq[(I2, List[Int])]
                            ) = {
      println("------------------------")
      println(s"""$test   pairs tacit 2+2""")
      var i = 0
      for {
        (in1a, out1a) <- inOut1
        (in1b, out1b) <- inOut1
        (in2a, out2a) <- inOut2
        (in2b, out2b) <- inOut2
      } yield {
        try {
          i += 1
          //          im((in1a, in2a), (in1b, in2b)).debugGet
          im((in1a, in2a), (in1b, in2b)).get.sorted === (out1a.intersect(out2a) ++ out1b.intersect(out2b)).distinct.sorted
        } catch {
          case e: Throwable =>
            val expected =
              s"""
                 |In/out 1a: $in1a   -->   $out1a
                 |In/out 1b: $in1b   -->   $out1b
                 |In/out 2a: $in2a   -->   $out2a
                 |In/out 2b: $in2b   -->   $out2b
                 |Expected : ${out1a.intersect(out2a)}   ++   ${out1b.intersect(out2b)}   -->   ${(out1a.intersect(out2a) ++ out1b.intersect(out2b)).distinct.sorted}""".stripMargin
            println(expected)
            val result = "Result   : " + im((in1a, in2a), (in1b, in2b)).get
            println(result)
            println("Result 1 : " + im1(in1a, in1b).get)
            println("Result 2 : " + im2(in2a, in2b).get)
            im((in1a, in2a), (in1b, in2b)).debugGet
            throw new RuntimeException(expected + "\n" + result)
        }
      }
      println(s"Eq Eq   $i tests passed")
    }

    def pairsMandatory[I1, I2, A, B, C](test: String,
                                        im: InputMolecule_2.InputMolecule_2_03[I1, I2, A, B, C],
                                        im1: InputMolecule_1.InputMolecule_1_02[I1, A, B],
                                        im2: InputMolecule_1.InputMolecule_1_02[I2, A, C],
                                        inOut1: Seq[(I1, Seq[(A, B)])],
                                        inOut2: Seq[(I2, Seq[(A, C)])]
                                       )(implicit ev: ClassTag[(A, B, C)]) = {
      println("------------------------")
      println(s"""$test   pairs mandatory""")
      var i = 0
      for {
        (in1, out1) <- inOut1
        (in2, out2) <- inOut2
      } yield {
        try {
          i += 1
          val intersection: Seq[A] = out1.map(_._1).intersect(out2.map(_._1))
          val output1: Map[A, B] = out1.toMap
          val output2: Map[A, C] = out2.toMap
          val out: Seq[(A, B, C)] = intersection.map(i => (i, output1(i), output2(i)))
          im.apply(in1, in2).get === out
        } catch {
          case e: Throwable =>
            val intersection: Seq[A] = out1.map(_._1).intersect(out2.map(_._1))
            val output1: Map[A, B] = out1.toMap
            val output2: Map[A, C] = out2.toMap
            val out: Seq[(A, B, C)] = intersection.map(i => (i, output1(i), output2(i)))
            val expected =
              s"""
                 |In/out 1: $in1   -->   $out1
                 |In/out 2: $in2   -->   $out2
                 |Expected: $out""".stripMargin
            println(expected)
            val result = "Result  : " + im(in1, in2).get
            println(result)
            println("Result 1: " + im1(in1).get)
            println("Result 2: " + im2(in2).get)
            throw new RuntimeException(expected + "\n" + result)
        }
      }
      println(s"$test   $i tests passed")
    }


    def groupsTacit22[I1, I2](test: String,
                              im: InputMolecule_2.InputMolecule_2_01[I1, I2, Int],
                              im1: InputMolecule_1.InputMolecule_1_01[I1, Int],
                              im2: InputMolecule_1.InputMolecule_1_01[I2, Int],
                              inOut1: Seq[(Seq[I1], List[Int])],
                              inOut2: Seq[(Seq[I2], List[Int])]) = {
      println("------------------------")
      println(s"""$test   groups tacit 2+2""")
      val combos = (for {
        (in1a, out1a) <- inOut1
        (in1b, out1b) <- inOut1
        (in2a, out2a) <- inOut2
        (in2b, out2b) <- inOut2
      } yield {

        val in1 = (in1a ++ in1b).distinct
        val in2: Seq[I2] = (in2a ++ in2b).distinct
        val out1: Seq[Int] = (in1a, in1b) match {
          case (Nil, Nil)                                                     => inOut1.head._2
          case (Seq(_: Set[_]), Nil)                                          => out1a
          case (Nil, Seq(_: Set[_]))                                          => out1b
          case (Seq(s1: Set[_]), Seq(s2: Set[_])) if s1.isEmpty && s2.isEmpty => out1a
          case (_, Seq(s2: Set[_])) if s2.isEmpty                             => out1a
          case (Seq(s1: Set[_]), _) if s1.isEmpty                             => out1b
          case _                                                              => (out1a ++ out1b).distinct
        }

        val out2: Seq[Int] = (in2a, in2b) match {
          case (Nil, Nil)                                                     => inOut2.head._2
          case (Seq(_: Set[_]), Nil)                                          => out2a
          case (Nil, Seq(_: Set[_]))                                          => out2b
          case (Seq(s1: Set[_]), Seq(s2: Set[_])) if s1.isEmpty && s2.isEmpty => out2a
          case (_, Seq(s2: Set[_])) if s2.isEmpty                             => out2a
          case (Seq(s1: Set[_]), _) if s1.isEmpty                             => out2b
          case _                                                              => (out2a ++ out2b).distinct
        }
        //          println("----------------")
        //          println(s"    $in2a   ++   $in2b     -->     $in2")
        //          println(s"    $out2a   ++   $out2b     -->     $out2")
        (in1, in2, out1, out2)
      }).distinct

      var i = 0
      //        combos.take(500) foreach { case (in1, in2, out1, out2) =>
      combos foreach { case (in1, in2, out1, out2) =>
        i += 1
        //          println(s"$in1   ++   $in2     -->     $out1   ++   $out2")
        try {
          im.apply(in1, in2).get.sorted === out1.intersect(out2).sorted
        } catch {
          case e: Throwable =>
            val expected =
              s"""
                 |In/out 1: $in1   -->   $out1
                 |In/out 2: $in2   -->   $out2
                 |Expected: ${out1.intersect(out2)}""".stripMargin
            println(expected)
            val result = "Result  : " + im(in1, in2).get
            im(in1, in2).debugGet
            println(result)
            println("Result 1: " + im1(in1).get)
            println("Result 2: " + im2(in2).get)
            throw new RuntimeException(expected + "\n" + result)
        }
      }
      println(s"$test   $i tests passed")
    }


    def groupsTacit21[I1, I2](test: String,
                              im: InputMolecule_2.InputMolecule_2_01[I1, I2, Int],
                              im1: InputMolecule_1.InputMolecule_1_01[I1, Int],
                              im2: InputMolecule_1.InputMolecule_1_01[I2, Int],
                              inOut1: Seq[(Seq[I1], List[Int])],
                              inOut2: Seq[(Seq[I2], List[Int])]) = {
      println("------------------------")
      println(s"""$test   groups tacit 2+1""")
      val combos = (for {
        (in1a, out1a) <- inOut1
        (in1b, out1b) <- inOut1
        (in2a, out2a) <- inOut2
      } yield {

        val in1 = (in1a ++ in1b).distinct
        val in2: Seq[I2] = in2a.distinct
        val out1: Seq[Int] = (in1a, in1b) match {
          case (Nil, Nil)                                                     => inOut1.head._2
          case (Seq(_: Set[_]), Nil)                                          => out1a
          case (Nil, Seq(_: Set[_]))                                          => out1b
          case (Seq(s1: Set[_]), Seq(s2: Set[_])) if s1.isEmpty && s2.isEmpty => out1a
          case (_, Seq(s2: Set[_])) if s2.isEmpty                             => out1a
          case (Seq(s1: Set[_]), _) if s1.isEmpty                             => out1b
          case _                                                              => (out1a ++ out1b).distinct
        }

        val out2: Seq[Int] = out2a

        //          println("----------------")
        //          println(s"    $in1a   ++   $in1b     -->     $in1")
        //          println(s"    $out1a   ++   $out1b     -->     $out1")
        (in1, in2, out1, out2)
      }).distinct

      var i = 0
      //        combos.take(500) foreach { case (in1, in2, out1, out2) =>
      combos foreach { case (in1, in2, out1, out2) =>
        i += 1
        //          println(s"$in1   ++   $in2     -->     $out1   ++   $out2")
        try {
          im.apply(in1, in2).get.sorted === out1.intersect(out2).sorted
        } catch {
          case e: Throwable =>
            val expected =
              s"""
                 |In/out 1: $in1   -->   $out1
                 |In/out 2: $in2   -->   $out2
                 |Expected: ${out1.intersect(out2)}""".stripMargin
            println(expected)
            val result = "Result  : " + im(in1, in2).get
            im(in1, in2).debugGet
            println(result)
            println("Result 1: " + im1(in1).get)
            println("Result 2: " + im2(in2).get)
            throw new RuntimeException(expected + "\n" + result)
        }
      }
      println(s"$test   $i tests passed")
    }

    def groupsTacit11[I1, I2](test: String, im: InputMolecule_2.InputMolecule_2_01[I1, I2, Int],
                              im1: InputMolecule_1.InputMolecule_1_01[I1, Int],
                              im2: InputMolecule_1.InputMolecule_1_01[I2, Int],
                              inOut1: Seq[(Seq[I1], List[Int])],
                              inOut2: Seq[(Seq[I2], List[Int])]) = {
      println("------------------------")
      println(s"""$test   tacit 1 + 1""")
      val combos = (for {
        (in1, out1) <- inOut1
        (in2, out2) <- inOut2
      } yield {
        (in1.distinct, in2.distinct, out1, out2)
      }).distinct

      var i = 0
      //        combos.take(500) foreach { case (in1, in2, out1, out2) =>
      combos foreach { case (in1, in2, out1, out2) =>
        i += 1
        //          println(s"$in1   ++   $in2     -->     $out1   ++   $out2")
        try {
          im.apply(in1, in2).get.sorted === out1.intersect(out2).sorted
        } catch {
          case e: Throwable =>
            val expected =
              s"""
                 |In/out 1: $in1   -->   $out1
                 |In/out 2: $in2   -->   $out2
                 |Expected: ${out1.intersect(out2)}""".stripMargin
            println(expected)
            val result = "Result  : " + im(in1, in2).get
            im(in1, in2).debugGet
            println(result)
            println("Result 1: " + im1(in1).get)
            println("Result 2: " + im2(in2).get)
            throw new RuntimeException(expected + "\n" + result)
        }
      }
      println(s"$test   $i tests passed")
    }

  }

  class IntLongSetup extends Setup {
    Ns.int.ints$.longs$ insert List(
      (1, Some(Set(1, 2, 3)), Some(Set(1L, 2L, 3L))),
      (2, Some(Set(2, 3, 4)), Some(Set(2L, 3L, 4L))),
      (3, Some(Set(3, 4, 5)), Some(Set(3L, 4L, 5L))),

      (4, Some(Set(1, 2, 3)), None),
      (5, None, Some(Set(1L, 2L, 3L))),
      (6, None, None)
    )
  }

  class EnumUriSetup extends Setup {
    Ns.int.enums$.uris$ insert List(
      (1, Some(Set(enum1, enum2, enum3)), Some(Set(uri1, uri2, uri3))),
      (2, Some(Set(enum2, enum3, enum4)), Some(Set(uri2, uri3, uri4))),
      (3, Some(Set(enum3, enum4, enum5)), Some(Set(uri3, uri4, uri5))),

      (4, Some(Set(enum1, enum2, enum3)), None),
      (5, None, Some(Set(uri1, uri2, uri3))),
      (6, None, None)
    )
  }


  "Pairs" >> {

    "Ints/Longs" >> {

      "Tacit" in new IntLongSetup {

        val m1Eq = m(Ns.int.ints_(?))
        val d1Eq = List(
          (Set[Int](), List(5, 6)),
          (Set(int0), Nil),
          (Set(int1), List(1, 4)),
          (Set(int2), List(1, 2, 4)),
          (Set(int3), List(1, 2, 3, 4)),
          (Set(int4), List(2, 3)),
          (Set(int5), List(3)),
          (Set(int0, int1), Nil),
          (Set(int1, int2), List(1, 4)),
          (Set(int2, int3), List(1, 2, 4)),
          (Set(int3, int4), List(2, 3)),
          (Set(int4, int5), List(3))
        )

        val m1Not = m(Ns.int.ints_.not(?))
        val d1Not = List(
          (Set[Int](), List(1, 2, 3, 4)),
          (Set(int0), List(1, 2, 3, 4)),
          (Set(int1), List(2, 3)),
          (Set(int2), List(3)),
          (Set(int3), Nil),
          (Set(int4), List(1, 4)),
          (Set(int5), List(1, 2, 4)),
          (Set(int0, int1), List(1, 2, 3, 4)),
          (Set(int1, int2), List(2, 3)),
          (Set(int2, int3), List(3)),
          (Set(int3, int4), List(1, 4)),
          (Set(int4, int5), List(1, 2, 4))
        )

        val m1Lt = m(Ns.int.ints_.<(?))
        val d1Lt = List(
          (Set[Int](), List(1, 2, 3, 4)),
          (Set(int0), Nil),
          (Set(int1), Nil),
          (Set(int2), List(1, 4)),
          (Set(int3), List(1, 2, 4)),
          (Set(int4), List(1, 2, 3, 4)),
          (Set(int5), List(1, 2, 3, 4))
        )

        val m2Eq = m(Ns.int.longs_(?))
        val d2Eq = List(
          (Set[Long](), List(4, 6)),
          (Set(long0), Nil),
          (Set(long1), List(1, 5)),
          (Set(long2), List(1, 2, 5)),
          (Set(long3), List(1, 2, 3, 5)),
          (Set(long4), List(2, 3)),
          (Set(long5), List(3)),
          (Set(long0, long1), Nil),
          (Set(long1, long2), List(1, 5)),
          (Set(long2, long3), List(1, 2, 5)),
          (Set(long3, long4), List(2, 3)),
          (Set(long4, long5), List(3))
        )

        val m2Not = m(Ns.int.longs_.not(?))
        val d2Not = List(
          (Set[Long](), List(1, 2, 3, 5)),
          (Set(long0), List(1, 2, 3, 5)),
          (Set(long1), List(2, 3)),
          (Set(long2), List(3)),
          (Set(long3), Nil),
          (Set(long4), List(1, 5)),
          (Set(long5), List(1, 2, 5)),
          (Set(long0, long1), List(1, 2, 3, 5)),
          (Set(long1, long2), List(2, 3)),
          (Set(long2, long3), List(3)),
          (Set(long3, long4), List(1, 5)),
          (Set(long4, long5), List(1, 2, 5))
        )

        val m2Gt = m(Ns.int.longs_.>(?))
        val d2Gt = List(
          (Set[Long](), List(1, 2, 3, 5)),
          (Set(long0), List(1, 2, 3, 5)),
          (Set(long1), List(1, 2, 3, 5)),
          (Set(long2), List(1, 2, 3, 5)),
          (Set(long3), List(2, 3)),
          (Set(long4), List(3)),
          (Set(long5), Nil)
        )

        val combinations = List(
          ("Eq Eq", m(Ns.int.ints_(?).longs_(?)), m1Eq, m2Eq, d1Eq, d2Eq),
          ("Eq !=", m(Ns.int.ints_(?).longs_.not(?)), m1Eq, m2Not, d1Eq, d2Not),
          ("Eq Gt", m(Ns.int.ints_(?).longs_.>(?)), m1Eq, m2Gt, d1Eq, d2Gt),
          ("!= Eq", m(Ns.int.ints_.not(?).longs_(?)), m1Not, m2Eq, d1Not, d2Eq),
          ("!= !=", m(Ns.int.ints_.not(?).longs_.not(?)), m1Not, m2Not, d1Not, d2Not),
          ("!= Gt", m(Ns.int.ints_.not(?).longs_.>(?)), m1Not, m2Gt, d1Not, d2Gt),
          ("Lt Eq", m(Ns.int.ints_.<(?).longs_(?)), m1Lt, m2Eq, d1Lt, d2Eq),
          ("Lt !=", m(Ns.int.ints_.<(?).longs_.not(?)), m1Lt, m2Not, d1Lt, d2Not),
          ("Lt Gt", m(Ns.int.ints_.<(?).longs_.>(?)), m1Lt, m2Gt, d1Lt, d2Gt)
        )
        // 1 pair
        combinations.foreach { case (test, im, im1, im2, inOut1, inOut2) => pairsTacit(test, im, im1, im2, inOut1, inOut2) }

        // 2 pairs (only for equals
        pairsTacit22("Eq Eq", m(Ns.int.ints_(?).longs_(?)), m1Eq, m2Eq, d1Eq, d2Eq)


        m(Ns.int.ints_(?).longs_.not(?)).apply(Nil)


        // Can't apply 0 pairs to molecule with tacit input attribute

        (m(Ns.int.ints(?).longs(?)).apply(Nil).get must throwA[InputMolecule_2_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
          "Can only apply empty list of pairs (Nil) to two tacit attributes"

        (m(Ns.int.ints(?).longs_(?)).apply(Nil).get must throwA[InputMolecule_2_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
          "Can only apply empty list of pairs (Nil) to two tacit attributes"

        (m(Ns.int.ints_(?).longs(?)).apply(Nil).get must throwA[InputMolecule_2_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
          "Can only apply empty list of pairs (Nil) to two tacit attributes"


        // Can't apply multiple pairs to input molecule with an expression input attribute

        (m(Ns.int.ints_(?).longs_.not(?)).apply(List((Set(1), Set(1L)), (Set(2), Set(2L)))).get must throwA[InputMolecule_2_Exception])
          .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
          "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)"

      }


      "Mandatory" in new IntLongSetup {

        val m1Eq = m(Ns.int.ints(?))

        val d1Eq = List(
          (Set[Int](), Nil),
          (Set(int0), Nil),
          (Set(int1), List((1, Set(int1, int2, int3)), (4, Set(int1, int2, int3)))),
          (Set(int2), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (4, Set(int1, int2, int3)))),
          (Set(int3), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)), (4, Set(int1, int2, int3)))),
          (Set(int4), List((2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)))),
          (Set(int5), List((3, Set(int3, int4, int5)))),
          (Set(int0, int1), Nil),
          (Set(int1, int2), List((1, Set(int1, int2, int3)), (4, Set(int1, int2, int3)))),
          (Set(int2, int3), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (4, Set(int1, int2, int3)))),
          (Set(int3, int4), List((2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)))),
          (Set(int4, int5), List((3, Set(int3, int4, int5))))
        )

        val m1Not = m(Ns.int.ints.not(?))
        val d1Not = List(
          (Set[Int](), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)), (4, Set(int1, int2, int3)))),
          (Set(int0), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)), (4, Set(int1, int2, int3)))),
          (Set(int1), List((2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)))),
          (Set(int2), List((3, Set(int3, int4, int5)))),
          (Set(int3), Nil),
          (Set(int4), List((1, Set(int1, int2, int3)), (4, Set(int1, int2, int3)))),
          (Set(int5), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (4, Set(int1, int2, int3)))),
          (Set(int0, int1), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)), (4, Set(int1, int2, int3)))),
          (Set(int1, int2), List((2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)))),
          (Set(int2, int3), List((3, Set(int3, int4, int5)))),
          (Set(int3, int4), List((1, Set(int1, int2, int3)), (4, Set(int1, int2, int3)))),
          (Set(int4, int5), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (4, Set(int1, int2, int3))))
        )

        val m1Lt = m(Ns.int.ints.<(?))
        val d1Lt = List(
          (Set[Int](), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (3, Set(int3, int4, int5)), (4, Set(int1, int2, int3)))),
          (Set(int0), Nil),
          (Set(int1), Nil),
          (Set(int2), List((1, Set(int1)), (4, Set(int1)))),
          (Set(int3), List((1, Set(int1, int2)), (2, Set(int2)), (4, Set(int1, int2)))),
          (Set(int4), List((1, Set(int1, int2, int3)), (2, Set(int2, int3)), (3, Set(int3)), (4, Set(int1, int2, int3)))),
          (Set(int5), List((1, Set(int1, int2, int3)), (2, Set(int2, int3, int4)), (3, Set(int3, int4)), (4, Set(int1, int2, int3))))
        )

        val m2Eq = m(Ns.int.longs(?))
        val d2Eq = List(
          (Set[Long](), Nil),
          (Set(long0), Nil),
          (Set(long1), List((1, Set(long1, long2, long3)), (5, Set(long1, long2, long3)))),
          (Set(long2), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (5, Set(long1, long2, long3)))),
          (Set(long3), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long1, long2, long3)))),
          (Set(long4), List((2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)))),
          (Set(long5), List((3, Set(long3, long4, long5)))),
          (Set(long0, long1), Nil),
          (Set(long1, long2), List((1, Set(long1, long2, long3)), (5, Set(long1, long2, long3)))),
          (Set(long2, long3), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (5, Set(long1, long2, long3)))),
          (Set(long3, long4), List((2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)))),
          (Set(long4, long5), List((3, Set(long3, long4, long5))))
        )

        val m2Not = m(Ns.int.longs.not(?))
        val d2Not = List(
          (Set[Long](), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long1, long2, long3)))),
          (Set(long0), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long1, long2, long3)))),
          (Set(long1), List((2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)))),
          (Set(long2), List((3, Set(long3, long4, long5)))),
          (Set(long3), Nil),
          (Set(long4), List((1, Set(long1, long2, long3)), (5, Set(long1, long2, long3)))),
          (Set(long5), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (5, Set(long1, long2, long3)))),
          (Set(long0, long1), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long1, long2, long3)))),
          (Set(long1, long2), List((2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)))),
          (Set(long2, long3), List((3, Set(long3, long4, long5)))),
          (Set(long3, long4), List((1, Set(long1, long2, long3)), (5, Set(long1, long2, long3)))),
          (Set(long4, long5), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (5, Set(long1, long2, long3))))
        )

        val m2Gt = m(Ns.int.longs.>(?))
        val d2Gt = List(
          (Set[Long](), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long1, long2, long3)))),
          (Set(long0), List((1, Set(long1, long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long1, long2, long3)))),
          (Set(long1), List((1, Set(long2, long3)), (2, Set(long2, long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long2, long3)))),
          (Set(long2), List((1, Set(long3)), (2, Set(long3, long4)), (3, Set(long3, long4, long5)), (5, Set(long3)))),
          (Set(long3), List((2, Set(long4)), (3, Set(long4, long5)))),
          (Set(long4), List((3, Set(long5)))),
          (Set(long5), Nil)
        )

        val combinations = List(
          ("Eq Eq", m(Ns.int.ints(?).longs(?)), m1Eq, m2Eq, d1Eq, d2Eq),
          ("Eq !=", m(Ns.int.ints(?).longs.not(?)), m1Eq, m2Not, d1Eq, d2Not),
          ("Eq Gt", m(Ns.int.ints(?).longs.>(?)), m1Eq, m2Gt, d1Eq, d2Gt),
          ("!= Eq", m(Ns.int.ints.not(?).longs(?)), m1Not, m2Eq, d1Not, d2Eq),
          ("!= !=", m(Ns.int.ints.not(?).longs.not(?)), m1Not, m2Not, d1Not, d2Not),
          ("!= Gt", m(Ns.int.ints.not(?).longs.>(?)), m1Not, m2Gt, d1Not, d2Gt),
          ("Lt Eq", m(Ns.int.ints.<(?).longs(?)), m1Lt, m2Eq, d1Lt, d2Eq),
          ("Lt !=", m(Ns.int.ints.<(?).longs.not(?)), m1Lt, m2Not, d1Lt, d2Not),
          ("Lt Gt", m(Ns.int.ints.<(?).longs.>(?)), m1Lt, m2Gt, d1Lt, d2Gt)
        )
        combinations.foreach { case (test, im, im1, im2, inOut1, inOut2) => pairsMandatory(test, im, im1, im2, inOut1, inOut2) }
      }
    }


    "1 pair, Enums/URIs" >> {

      "Tacit" in new EnumUriSetup {

        val m1Eq = m(Ns.int.enums_(?))
        val d1Eq = List(
          (Set[String](), List(5, 6)),
          (Set(enum0), Nil),
          (Set(enum1), List(1, 4)),
          (Set(enum2), List(1, 2, 4)),
          (Set(enum3), List(1, 2, 3, 4)),
          (Set(enum4), List(2, 3)),
          (Set(enum5), List(3)),
          (Set(enum0, enum1), Nil),
          (Set(enum1, enum2), List(1, 4)),
          (Set(enum2, enum3), List(1, 2, 4)),
          (Set(enum3, enum4), List(2, 3)),
          (Set(enum4, enum5), List(3))
        )

        val m1Not = m(Ns.int.enums_.not(?))
        val d1Not = List(
          (Set[String](), List(1, 2, 3, 4)),
          (Set(enum0), List(1, 2, 3, 4)),
          (Set(enum1), List(2, 3)),
          (Set(enum2), List(3)),
          (Set(enum3), Nil),
          (Set(enum4), List(1, 4)),
          (Set(enum5), List(1, 2, 4)),
          (Set(enum0, enum1), List(1, 2, 3, 4)),
          (Set(enum1, enum2), List(2, 3)),
          (Set(enum2, enum3), List(3)),
          (Set(enum3, enum4), List(1, 4)),
          (Set(enum4, enum5), List(1, 2, 4))
        )

        val m1Lt = m(Ns.int.enums_.<(?))
        val d1Lt = List(
          (Set[String](), List(1, 2, 3, 4)),
          (Set(enum0), Nil),
          (Set(enum1), Nil),
          (Set(enum2), List(1, 4)),
          (Set(enum3), List(1, 2, 4)),
          (Set(enum4), List(1, 2, 3, 4)),
          (Set(enum5), List(1, 2, 3, 4))
        )

        val m2Eq = m(Ns.int.uris_(?))
        val d2Eq = List(
          (Set[URI](), List(4, 6)),
          (Set(uri0), Nil),
          (Set(uri1), List(1, 5)),
          (Set(uri2), List(1, 2, 5)),
          (Set(uri3), List(1, 2, 3, 5)),
          (Set(uri4), List(2, 3)),
          (Set(uri5), List(3)),
          (Set(uri0, uri1), Nil),
          (Set(uri1, uri2), List(1, 5)),
          (Set(uri2, uri3), List(1, 2, 5)),
          (Set(uri3, uri4), List(2, 3)),
          (Set(uri4, uri5), List(3))
        )

        val m2Not = m(Ns.int.uris_.not(?))
        val d2Not = List(
          (Set[URI](), List(1, 2, 3, 5)),
          (Set(uri0), List(1, 2, 3, 5)),
          (Set(uri1), List(2, 3)),
          (Set(uri2), List(3)),
          (Set(uri3), Nil),
          (Set(uri4), List(1, 5)),
          (Set(uri5), List(1, 2, 5)),
          (Set(uri0, uri1), List(1, 2, 3, 5)),
          (Set(uri1, uri2), List(2, 3)),
          (Set(uri2, uri3), List(3)),
          (Set(uri3, uri4), List(1, 5)),
          (Set(uri4, uri5), List(1, 2, 5))
        )

        val m2Gt = m(Ns.int.uris_.>(?))
        val d2Gt = List(
          (Set[URI](), List(1, 2, 3, 5)),
          (Set(uri0), List(1, 2, 3, 5)),
          (Set(uri1), List(1, 2, 3, 5)),
          (Set(uri2), List(1, 2, 3, 5)),
          (Set(uri3), List(2, 3)),
          (Set(uri4), List(3)),
          (Set(uri5), Nil)
        )

        val combinations = List(
          ("Eq Eq", m(Ns.int.enums_(?).uris_(?)), m1Eq, m2Eq, d1Eq, d2Eq),
          ("Eq !=", m(Ns.int.enums_(?).uris_.not(?)), m1Eq, m2Not, d1Eq, d2Not),
          ("Eq Gt", m(Ns.int.enums_(?).uris_.>(?)), m1Eq, m2Gt, d1Eq, d2Gt),
          ("!= Eq", m(Ns.int.enums_.not(?).uris_(?)), m1Not, m2Eq, d1Not, d2Eq),
          ("!= !=", m(Ns.int.enums_.not(?).uris_.not(?)), m1Not, m2Not, d1Not, d2Not),
          ("!= Gt", m(Ns.int.enums_.not(?).uris_.>(?)), m1Not, m2Gt, d1Not, d2Gt),
          ("Lt Eq", m(Ns.int.enums_.<(?).uris_(?)), m1Lt, m2Eq, d1Lt, d2Eq),
          ("Lt !=", m(Ns.int.enums_.<(?).uris_.not(?)), m1Lt, m2Not, d1Lt, d2Not),
          ("Lt Gt", m(Ns.int.enums_.<(?).uris_.>(?)), m1Lt, m2Gt, d1Lt, d2Gt)
        )
        combinations.foreach { case (test, im, im1, im2, inOut1, inOut2) => pairsTacit(test, im, im1, im2, inOut1, inOut2) }
      }


      "Mandatory" in new EnumUriSetup {

        val m1Eq = m(Ns.int.enums(?))
        val d1Eq = List(
          (Set[String](), Nil),
          (Set(enum0), Nil),
          (Set(enum1), List((1, Set(enum1, enum2, enum3)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum2), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum3), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum4), List((2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)))),
          (Set(enum5), List((3, Set(enum3, enum4, enum5)))),
          (Set(enum0, enum1), Nil),
          (Set(enum1, enum2), List((1, Set(enum1, enum2, enum3)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum2, enum3), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum3, enum4), List((2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)))),
          (Set(enum4, enum5), List((3, Set(enum3, enum4, enum5))))
        )

        val m1Not = m(Ns.int.enums.not(?))
        val d1Not = List(
          (Set[String](), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum0), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum1), List((2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)))),
          (Set(enum2), List((3, Set(enum3, enum4, enum5)))),
          (Set(enum3), Nil),
          (Set(enum4), List((1, Set(enum1, enum2, enum3)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum5), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum0, enum1), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum1, enum2), List((2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)))),
          (Set(enum2, enum3), List((3, Set(enum3, enum4, enum5)))),
          (Set(enum3, enum4), List((1, Set(enum1, enum2, enum3)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum4, enum5), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (4, Set(enum1, enum2, enum3))))
        )

        val m1Lt = m(Ns.int.enums.<(?))
        val d1Lt = List(
          (Set[String](), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4, enum5)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum0), Nil),
          (Set(enum1), Nil),
          (Set(enum2), List((1, Set(enum1)), (4, Set(enum1)))),
          (Set(enum3), List((1, Set(enum1, enum2)), (2, Set(enum2)), (4, Set(enum1, enum2)))),
          (Set(enum4), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3)), (3, Set(enum3)), (4, Set(enum1, enum2, enum3)))),
          (Set(enum5), List((1, Set(enum1, enum2, enum3)), (2, Set(enum2, enum3, enum4)), (3, Set(enum3, enum4)), (4, Set(enum1, enum2, enum3))))
        )

        val m2Eq = m(Ns.int.uris(?))
        val d2Eq = List(
          (Set[URI](), Nil),
          (Set(uri0), Nil),
          (Set(uri1), List((1, Set(uri1, uri2, uri3)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri2), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri3), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri4), List((2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)))),
          (Set(uri5), List((3, Set(uri3, uri4, uri5)))),
          (Set(uri0, uri1), Nil),
          (Set(uri1, uri2), List((1, Set(uri1, uri2, uri3)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri2, uri3), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri3, uri4), List((2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)))),
          (Set(uri4, uri5), List((3, Set(uri3, uri4, uri5))))
        )

        val m2Not = m(Ns.int.uris.not(?))
        val d2Not = List(
          (Set[URI](), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri0), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri1), List((2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)))),
          (Set(uri2), List((3, Set(uri3, uri4, uri5)))),
          (Set(uri3), Nil),
          (Set(uri4), List((1, Set(uri1, uri2, uri3)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri5), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri0, uri1), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri1, uri2), List((2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)))),
          (Set(uri2, uri3), List((3, Set(uri3, uri4, uri5)))),
          (Set(uri3, uri4), List((1, Set(uri1, uri2, uri3)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri4, uri5), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (5, Set(uri1, uri2, uri3))))
        )

        val m2Gt = m(Ns.int.uris.>(?))
        val d2Gt = List(
          (Set[URI](), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri0), List((1, Set(uri1, uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri1, uri2, uri3)))),
          (Set(uri1), List((1, Set(uri2, uri3)), (2, Set(uri2, uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri2, uri3)))),
          (Set(uri2), List((1, Set(uri3)), (2, Set(uri3, uri4)), (3, Set(uri3, uri4, uri5)), (5, Set(uri3)))),
          (Set(uri3), List((2, Set(uri4)), (3, Set(uri4, uri5)))),
          (Set(uri4), List((3, Set(uri5)))),
          (Set(uri5), Nil)
        )

        val combinations = List(
          ("Eq Eq", m(Ns.int.enums(?).uris(?)), m1Eq, m2Eq, d1Eq, d2Eq),
          ("Eq !=", m(Ns.int.enums(?).uris.not(?)), m1Eq, m2Not, d1Eq, d2Not),
          ("Eq Gt", m(Ns.int.enums(?).uris.>(?)), m1Eq, m2Gt, d1Eq, d2Gt),
          ("!= Eq", m(Ns.int.enums.not(?).uris(?)), m1Not, m2Eq, d1Not, d2Eq),
          ("!= !=", m(Ns.int.enums.not(?).uris.not(?)), m1Not, m2Not, d1Not, d2Not),
          ("!= Gt", m(Ns.int.enums.not(?).uris.>(?)), m1Not, m2Gt, d1Not, d2Gt),
          ("Lt Eq", m(Ns.int.enums.<(?).uris(?)), m1Lt, m2Eq, d1Lt, d2Eq),
          ("Lt !=", m(Ns.int.enums.<(?).uris.not(?)), m1Lt, m2Not, d1Lt, d2Not),
          ("Lt Gt", m(Ns.int.enums.<(?).uris.>(?)), m1Lt, m2Gt, d1Lt, d2Gt)
        )

        combinations.foreach { case (test, im, im1, im2, inOut1, inOut2) => pairsMandatory(test, im, im1, im2, inOut1, inOut2) }
      }
    }
  }


  "Groups" >> {

    "Ints/Longs" in new IntLongSetup {

      val m1Eq = m(Ns.int.ints_(?))
      val d1Eq = List(
        (Nil, List(5, 6)),
        (List(Set[Int]()), List(5, 6)),
        (List(Set(int0)), Nil),
        (List(Set(int1)), List(1, 4)),
        (List(Set(int2)), List(1, 2, 4)),
        (List(Set(int3)), List(1, 2, 3, 4)),
        (List(Set(int4)), List(2, 3)),
        (List(Set(int5)), List(3)),
        (List(Set(int0, int1)), Nil),
        (List(Set(int1, int2)), List(1, 4)),
        (List(Set(int2, int3)), List(1, 2, 4)),
        (List(Set(int3, int4)), List(2, 3)),
        (List(Set(int4, int5)), List(3))
      )

      val m1Not = m(Ns.int.ints_.not(?))
      val d1Not = List(
        (Nil, List(1, 2, 3, 4)),
        (List(Set[Int]()), List(1, 2, 3, 4)),
        (List(Set(int0)), List(1, 2, 3, 4)),
        (List(Set(int1)), List(2, 3)),
        (List(Set(int2)), List(3)),
        (List(Set(int3)), Nil),
        (List(Set(int4)), List(1, 4)),
        (List(Set(int5)), List(1, 2, 4)),
        (List(Set(int0, int1)), List(1, 2, 3, 4)),
        (List(Set(int1, int2)), List(2, 3)),
        (List(Set(int2, int3)), List(3)),
        (List(Set(int3, int4)), List(1, 4)),
        (List(Set(int4, int5)), List(1, 2, 4))
      )

      val m1Lt = m(Ns.int.ints_.<(?))
      val d1Lt = List(
        (Nil, List(1, 2, 3, 4)),
        (List(Set[Int]()), List(1, 2, 3, 4)),
        (List(Set(int0)), Nil),
        (List(Set(int1)), Nil),
        (List(Set(int2)), List(1, 4)),
        (List(Set(int3)), List(1, 2, 4)),
        (List(Set(int4)), List(1, 2, 3, 4)),
        (List(Set(int5)), List(1, 2, 3, 4))
      )

      val m2Eq = m(Ns.int.longs_(?))
      val d2Eq = List(
        (Nil, List(4, 6)),
        (List(Set[Long]()), List(4, 6)),
        (List(Set(long0)), Nil),
        (List(Set(long1)), List(1, 5)),
        (List(Set(long2)), List(1, 2, 5)),
        (List(Set(long3)), List(1, 2, 3, 5)),
        (List(Set(long4)), List(2, 3)),
        (List(Set(long5)), List(3)),
        (List(Set(long0, long1)), Nil),
        (List(Set(long1, long2)), List(1, 5)),
        (List(Set(long2, long3)), List(1, 2, 5)),
        (List(Set(long3, long4)), List(2, 3)),
        (List(Set(long4, long5)), List(3))
      )

      val m2Not = m(Ns.int.longs_.not(?))
      val d2Not = List(
        (Nil, List(1, 2, 3, 5)),
        (List(Set[Long]()), List(1, 2, 3, 5)),
        (List(Set(long0)), List(1, 2, 3, 5)),
        (List(Set(long1)), List(2, 3)),
        (List(Set(long2)), List(3)),
        (List(Set(long3)), Nil),
        (List(Set(long4)), List(1, 5)),
        (List(Set(long5)), List(1, 2, 5)),
        (List(Set(long0, long1)), List(1, 2, 3, 5)),
        (List(Set(long1, long2)), List(2, 3)),
        (List(Set(long2, long3)), List(3)),
        (List(Set(long3, long4)), List(1, 5)),
        (List(Set(long4, long5)), List(1, 2, 5))
      )

      val m2Gt = m(Ns.int.longs_.>(?))
      val d2Gt = List(
        (Nil, List(1, 2, 3, 5)),
        (List(Set[Long]()), List(1, 2, 3, 5)),
        (List(Set(long0)), List(1, 2, 3, 5)),
        (List(Set(long1)), List(1, 2, 3, 5)),
        (List(Set(long2)), List(1, 2, 3, 5)),
        (List(Set(long3)), List(2, 3)),
        (List(Set(long4)), List(3)),
        (List(Set(long5)), Nil)
      )

      // 2 inputs in each group
      groupsTacit22("Eq Eq", m(Ns.int.ints_(?).longs_(?)), m1Eq, m2Eq, d1Eq, d2Eq)

      val combinations1 = List(
        ("Eq Eq", m(Ns.int.ints_(?).longs_(?)), m1Eq, m2Eq, d1Eq, d2Eq),
        ("Eq !=", m(Ns.int.ints_(?).longs_.not(?)), m1Eq, m2Not, d1Eq, d2Not),
        ("Eq Gt", m(Ns.int.ints_(?).longs_.>(?)), m1Eq, m2Gt, d1Eq, d2Gt)
      )
      // 2 inputs in 1st group, 1 input in 2nd group
      combinations1.foreach { case (test, im, im1, im2, inOut1, inOut2) => groupsTacit21(test, im, im1, im2, inOut1, inOut2) }


      val combinations2 = List(
        ("Eq Eq", m(Ns.int.ints_(?).longs_(?)), m1Eq, m2Eq, d1Eq, d2Eq),
        ("Eq !=", m(Ns.int.ints_(?).longs_.not(?)), m1Eq, m2Not, d1Eq, d2Not),
        ("Eq Gt", m(Ns.int.ints_(?).longs_.>(?)), m1Eq, m2Gt, d1Eq, d2Gt),
        ("!= Eq", m(Ns.int.ints_.not(?).longs_(?)), m1Not, m2Eq, d1Not, d2Eq),
        ("!= !=", m(Ns.int.ints_.not(?).longs_.not(?)), m1Not, m2Not, d1Not, d2Not),
        ("!= Gt", m(Ns.int.ints_.not(?).longs_.>(?)), m1Not, m2Gt, d1Not, d2Gt),
        ("Lt Eq", m(Ns.int.ints_.<(?).longs_(?)), m1Lt, m2Eq, d1Lt, d2Eq),
        ("Lt !=", m(Ns.int.ints_.<(?).longs_.not(?)), m1Lt, m2Not, d1Lt, d2Not),
        ("Lt Gt", m(Ns.int.ints_.<(?).longs_.>(?)), m1Lt, m2Gt, d1Lt, d2Gt)
      )
      // 1 input in each group
      combinations2.foreach { case (test, im, im1, im2, inOut1, inOut2) => groupsTacit11(test, im, im1, im2, inOut1, inOut2) }


      (m(Ns.int.ints(?).longs(?)).apply(Nil, List(Set(1L))).get must throwA[InputMolecule_2_Exception])
        .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
        "Can only apply empty list (Nil) to a tacit input attribute. Please make input attr tacit: `ints` --> `ints_`"


      (m(Ns.int.ints_.<=(?).longs_(?)).apply(List(Set(1), Set(2)), List(Set(1L))).get must throwA[InputMolecule_2_Exception])
        .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
        s"Can't apply multiple values to input attribute `:ns/ints` having expression (<, >, <=, >=, !=)"
    }


    "Enum/URI" in new EnumUriSetup {
      m(Ns.int.enums_(?).uris_(?)).apply(List(Set("enum2")), List(Set(uri3))).get === List(1, 2)
      m(Ns.int.enums_(?).uris_.not(?)).apply(List(Set("enum2")), List(Set(uri3))).get === Nil
      m(Ns.int.enums_(?).uris_.>(?)).apply(List(Set("enum2")), List(Set(uri3))).get === List(2)
      m(Ns.int.enums_.not(?).uris_(?)).apply(List(Set("enum2")), List(Set(uri3))).get === List(3)
      m(Ns.int.enums_.not(?).uris_.not(?)).apply(List(Set("enum2")), List(Set(uri3))).get === Nil
      m(Ns.int.enums_.not(?).uris_.>(?)).apply(List(Set("enum2")), List(Set(uri3))).get === List(3)
      m(Ns.int.enums_.<(?).uris_(?)).apply(List(Set("enum2")), List(Set(uri3))).get === List(1)
      m(Ns.int.enums_.<(?).uris_.not(?)).apply(List(Set("enum2")), List(Set(uri3))).get === Nil
      m(Ns.int.enums_.<(?).uris_.>(?)).apply(List(Set("enum2")), List(Set(uri3))).get === Nil
    }


    "`and` syntax" in new IntLongSetup {

      val im = m(Ns.int.ints_(?).longs_(?))

      im(Set(1) and Set(2L)).get === List(1)
      im(List(Set(1)), List(Set(2L))).get === List(1)

      im(Set(1) and Set(2L, 3L)).get === List(1)
      im(List(Set(1)), List(Set(2L, 3L))).get === List(1)

      im(Set(1) and (Set(2L) or Set(3L))).get === List(1)
      im(List(Set(1)), List(Set(2L), Set(3L))).get === List(1)

      im(Set(1) and (Set(1L, 2L) or Set(3L))).get === List(1)
      im(List(Set(1)), List(Set(1L, 2L), Set(3L))).get === List(1)


      im(Set(2, 3) and Set(2L)).get === List(1, 2)
      im(List(Set(2, 3)), List(Set(2L))).get === List(1, 2)

      im(Set(2, 3) and Set(2L, 3L)).get === List(1, 2)
      im(List(Set(2, 3)), List(Set(2L, 3L))).get === List(1, 2)

      im(Set(2, 3) and (Set(2L) or Set(3L))).get === List(1, 2)
      im(List(Set(2, 3)), List(Set(2L), Set(3L))).get === List(1, 2)

      im(Set(2, 3) and (Set(1L, 2L) or Set(3L))).get === List(1, 2)
      im(List(Set(2, 3)), List(Set(1L, 2L), Set(3L))).get === List(1, 2)


      im((Set(2) or Set(3)) and Set(2L)).get === List(1, 2)
      im(List(Set(2), Set(3)), List(Set(2L))).get === List(1, 2)

      im((Set(2) or Set(3)) and Set(2L, 3L)).get === List(1, 2)
      im(List(Set(2), Set(3)), List(Set(2L, 3L))).get === List(1, 2)

      im((Set(2) or Set(3)) and (Set(2L) or Set(3L))).get === List(1, 2, 3)
      im(List(Set(2), Set(3)), List(Set(2L), Set(3L))).get === List(1, 2, 3)

      im((Set(2) or Set(3)) and (Set(1L, 2L) or Set(3L))).get === List(1, 2, 3)
      im(List(Set(2), Set(3)), List(Set(1L, 2L), Set(3L))).get === List(1, 2, 3)
    }
  }
}