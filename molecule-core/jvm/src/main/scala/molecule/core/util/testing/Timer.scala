package molecule.core.util.testing

case class Timer(txt: String = "time", on: Boolean = true) {
  val time0 = System.currentTimeMillis
  var time1 = time0
  //    println("timer start ---------------------------------")

  def delta: Long = {
    val time2 = System.currentTimeMillis - time1
    time1 = System.currentTimeMillis()
    time2
  }

  def ms: String = thousands(delta) + " ms"
  def msTotal: String = thousands(System.currentTimeMillis() - time0) + " ms"

  def log(n: Int): Unit = {
    val time2 = System.currentTimeMillis - time1
    if (on) println(s"$txt $n: " + "%10d".format(time2))
    time1 = System.currentTimeMillis()
  }
  def total: Unit = if (on) println(
    s"$txt saving time: ${thousands(System.currentTimeMillis - time0)} ms"
  )


  def thousands(i: Long): String =
    i.toString.reverse.grouped(3).mkString(" ").reverse


  def prettyMillisDelta(millisDelta: Long): String = {
    val second = 1000L
    val minute = second * 60
    val hour   = minute * 60
    val day    = hour * 24
    val month  = day * 30
    val year   = day * 365

    if (millisDelta / year > 1) s"${millisDelta / year} years ago"
    else if (millisDelta / year == 1) "1 year"
    else if (millisDelta / month > 1) s"${millisDelta / month} months ago"
    else "xxx"
  }
}


