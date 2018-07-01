package molecule.util
import java.text.SimpleDateFormat
import java.time.{LocalDate, ZoneId}
import java.util.Date

private[molecule] trait Helpers {

  protected object mkDate {
    def apply(year: Int, month: Int = 1, day: Int = 1): Date = {
      val localDate = LocalDate.of(year, month, day)
      val instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant
      Date.from(instant)
    }
  }

  // Todo: need advice on handling time zones!

  protected def format(date: Date): String = {
    val f = new SimpleDateFormat("'#inst \"'yyyy-MM-dd'T'HH:mm:ss.SSSXXX'\"'")
    //    f.setTimeZone(TimeZone.getTimeZone("UTC"))
    f.format(date)
  }

  protected def format2(date: Date): String = {
    val f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    //    f.setTimeZone(TimeZone.getTimeZone("UTC"))
    f.format(date)
  }

  protected def f(a: Any) = a match {
    case date: Date => format2(date).replace("+", "\\\\+")
    case other      => other
  }
  protected def f2(a: Any) = a match {
    case date: Date => format2(date)
    case other      => other
  }


  protected def tupleToSeq(arg: Any) = arg match {
    case t: (_, _)                                                             => Seq(t._1, t._2)
    case t: (_, _, _)                                                          => Seq(t._1, t._2, t._3)
    case t: (_, _, _, _)                                                       => Seq(t._1, t._2, t._3, t._4)
    case t: (_, _, _, _, _)                                                    => Seq(t._1, t._2, t._3, t._4, t._5)
    case t: (_, _, _, _, _, _)                                                 => Seq(t._1, t._2, t._3, t._4, t._5, t._6)
    case t: (_, _, _, _, _, _, _)                                              => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7)
    case t: (_, _, _, _, _, _, _, _)                                           => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8)
    case t: (_, _, _, _, _, _, _, _, _)                                        => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
    case t: (_, _, _, _, _, _, _, _, _, _)                                     => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10)
    case t: (_, _, _, _, _, _, _, _, _, _, _)                                  => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _)                               => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _)                            => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _)                         => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _)                      => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)                   => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)                => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)             => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)          => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)       => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)    => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21)
    case t: (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _) => Seq(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21, t._22)
    case l: Seq[_]                                                             => l
    case a                                                                     => Seq(a)
  }
}