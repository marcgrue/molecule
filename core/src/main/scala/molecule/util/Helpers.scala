package molecule.util
import java.net.URI
import java.text.SimpleDateFormat
import java.time.{LocalDate, ZoneId}
import java.util.{Date, TimeZone, UUID}
import datomic.ListenableFuture
import molecule.ast.query.date2datomicStr
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.control.NonFatal

private[molecule] trait Helpers {

  final protected object mkDate {
    def apply(year: Int, month: Int = 1, day: Int = 1): Date = {
      val localDate = LocalDate.of(year, month, day)
      val instant   = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant
      Date.from(instant)
    }
  }

  final protected lazy val sdfDatomic = new SimpleDateFormat("'#inst \"'yyyy-MM-dd'T'HH:mm:ss.SSSXXX'\"'")
  final protected lazy val sdf        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

  protected def date2datomicStr(date: Date): String = sdfDatomic.format(date)
  protected def date2str(date: Date): String = sdf.format(date)

  protected def date(s: String): Date = sdf.parse(s)

  final protected def f(a: Any) = a match {
    case date: Date => date2str(date).replace("+", "\\\\+")
    case other      => other
  }
  final protected def f2(a: Any) = a match {
    case date: Date => date2str(date)
    case other      => other
  }

  protected def cast(value: Any): String = value match {
    case (a, b)     => s"(${cast(a)}, ${cast(b)})"
    case v: Long    => v.toString + "L"
    case v: Float   => v.toString + "f"
    case date: Date => "\"" + date2datomicStr(date) + "\""
    case v: String  => "\"" + v + "\""
    case v: UUID    => "\"" + v + "\""
    case v: URI     => "\"" + v + "\""
    case v          => v.toString
  }

  final protected def o(opt: Option[Any]): String = if (opt.isDefined) s"""Some(${cast(opt.get)})""" else "None"

  final protected def seq[T](values: Seq[T]): String = values.map {
    case set: Set[_] => set.map(cast).mkString("Set(", ", ", ")")
    case seq: Seq[_] => seq.map(cast).mkString("Seq(", ", ", ")")
    case (a, b)      => s"${cast(a)} -> ${cast(b)}"
    case v           => cast(v)
  }.mkString("Seq(", ", ", ")")


  final protected def tupleToSeq(arg: Any): Seq[Any] = arg match {
    case l: Seq[_]  => l
    case p: Product => p match {
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
    }
    case a          => Seq(a)
  }


  private var time0 = System.currentTimeMillis()
  private val times = collection.mutable.Map.empty[Int, Long]
  protected final def time(n: Int, prev: Int = 0) = {
    if (n < 1 || prev < 0)
      throw new IllegalArgumentException(s"Identifiers have to be positive numbers")

    if (times.nonEmpty && n <= times.keys.max)
      throw new IllegalArgumentException(s"Identifier have to be incremental. `$n` is smaller than or equal to previous `${times.keys.max}`")

    if (times.keys.toSeq.contains(n))
      throw new IllegalArgumentException(s"Can't use same time identifier `$n` multiple times")

    val time1   = if (prev > 0) times(prev) else time0
    val time2   = System.currentTimeMillis()
    val elapsed = time2 - time1
    times += n -> time2
    val formatter = new java.text.SimpleDateFormat("HH:mm:ss.SSS", new java.util.Locale("en", "UK"))
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
    println(s"TIME $n: " + formatter.format(new java.util.Date(elapsed)))
    time0 = System.currentTimeMillis()
  }


  private[molecule] def bridgeDatomicFuture[T](listenF: ListenableFuture[T])(implicit ec: ExecutionContext): Future[T] = {
    val p = Promise[T]
    listenF.addListener(
      new java.lang.Runnable {
        override def run: Unit = {
          try {
            p.success(listenF.get())
          } catch {
            case e: java.util.concurrent.ExecutionException =>
              p.failure(e.getCause)
            case NonFatal(e)                                =>
              p.failure(e)
          }
          ()
        }
      },
      (arg0: Runnable) => ec.execute(arg0)
    )
    p.future
  }
}