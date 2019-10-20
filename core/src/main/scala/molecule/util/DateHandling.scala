package molecule.util
import java.time.format.DateTimeFormatter
import java.time._
import java.util.{Calendar, Date, TimeZone}

trait DateHandling extends RegexMatching {

  private lazy val localZoneOffset: ZoneOffset = OffsetDateTime.now().getOffset
  private lazy val localOffset    : String     = localZoneOffset.toString

  private lazy val tzoffset: Int = {
    val calendar = Calendar.getInstance();
    calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
  }

  private def error(err: String): Nothing = {
    val err1 = "[DateHandling]  " + err
    println(err1)
    throw new IllegalArgumentException(err1)
  }

  private def mkMs(s: String): Int = s match {
    case r"(00\d|0\d\d|\d\d\d)$n" => n.toInt
    case r"(0\d|\d\d)$n"          => n.toInt * 10
    case r"(\d)$n"                => n.toInt * 100
  }

  private def local(sign: String, zh: String, zm: String): Boolean = {
    s"$sign$zh:$zm" == localOffset || zh == "Z"
  }

  private def p(s: String, i: Int = 2): String = i match {
    case 2 => "%02d".format(s.toInt)
    case 3 => "%03d".format(s.toInt)
    case 4 => "%04d".format(s.toInt)
  }

  private def getDateStr(date: Date, zoneOffset: ZoneOffset, pattern: String) = {
    val ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime), zoneOffset)
    val zdt = ZonedDateTime.of(ldt, zoneOffset)
    zdt.format(DateTimeFormatter.ofPattern(pattern))
  }

  def date2datomicStr(date: Date, zoneOffset: ZoneOffset = localZoneOffset): String =
    getDateStr(date, zoneOffset, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")


  def date2str(date: Date,
               zoneOffset: ZoneOffset = localZoneOffset): String = {
    val epochMs0 = date.getTime
    val epochMs  = epochMs0 - daylight(epochMs0)
    val zdt      = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMs), zoneOffset)
    val ms       = (zdt.getNano / 1000000) > 0
    if (zoneOffset == localZoneOffset) {
      if (ms) {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
      } else if (zdt.getSecond != 0) {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      } else if (zdt.getHour != 0 || zdt.getMinute != 0) {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
      } else {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      }
    } else {
      if (ms) {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS XXX"))
      } else if (zdt.getSecond != 0) {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"))
      } else {
        zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm XXX"))
      }
    }
  }

  private def daylight(ms: Long): Int = {
    tzoffset - TimeZone.getDefault.getOffset(new Date(ms).getTime)
  }

  def str2date(s: String,
               zoneOffset: ZoneOffset = localZoneOffset,
              ): Date = {
    val inst = str2zdt(s, zoneOffset).toInstant
    val ms   = inst.getEpochSecond * 1000 + inst.getNano / 1000000
    new Date(ms + daylight(ms))
  }

  def str2zdt(s: String,
              zoneOffset: ZoneOffset = localZoneOffset,
             ): ZonedDateTime = {

    def mkZh(zh0: String): Int =
      if (zh0.contains("Z"))
      // todo: not sure about this...
        0
      else
        zh0.toInt
    def mkZm(zh0: String, zm0: String): Int =
      if (zh0.contains("Z")) 0 else zm0.toInt

    def da(y0: String,
           m0: String = "1",
           d0: String = "1",
           hh0: String = "0",
           mm0: String = "0",
           ss0: String = "0",
           ms0: String = "0",
           zh0: String = "99",
           zm0: String = "99"
          ): ZonedDateTime = {
      val (y, m, d, hh, mm, ss, nanoS, zh, zm) = (
        y0.toInt, m0.toInt, d0.toInt,
        hh0.toInt, mm0.toInt, ss0.toInt, mkMs(ms0) * 1000000,
        mkZh(zh0), mkZm(zh0, zm0)
      )
      if (zh0.contains("Z")) { // todo: is this the right interpretation of Z?
        ZonedDateTime.of(y, m, d, hh, mm, ss, nanoS, localZoneOffset)
      } else if (zm != 99) {
        ZonedDateTime.of(y, m, d, hh, mm, ss, nanoS, ZoneOffset.ofHoursMinutes(zh, zm))
      } else if (zh != 99) {
        ZonedDateTime.of(y, m, d, hh, mm, ss, nanoS, ZoneOffset.ofHours(zh))
      } else {
        ZonedDateTime.of(y, m, d, hh, mm, ss, nanoS, zoneOffset)
      }
    }

    s.trim match {
      case r"^(\d{1,4})$y$$"                                                                                                                                                                                                         => da(y)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m$$"                                                                                                                                                                                      => da(y, m)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d$$"                                                                                                                                                          => da(y, m, d)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm$$"                                                                                                  => da(y, m, d, hh, mm)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss$$"                                                                          => da(y, m, d, hh, mm, ss)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms$$"                                                            => da(y, m, d, hh, mm, ss, ms)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms *([\+\-]?)$sign(1[0-2]|0?[0-9]|Z)$zh$$"                       => da(y, m, d, hh, mm, ss, ms, sign + zh)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$" => da(y, m, d, hh, mm, ss, ms, sign + zh, sign + zm)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss *([\+\-]?)$sign(1[0-2]|0?[0-9]|Z)$zh$$"                                     => da(y, m, d, hh, mm, ss, "0", sign + zh)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"               => da(y, m, d, hh, mm, ss, "0", sign + zh, sign + zm)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm *([\+\-]?)$sign(1[0-2]|0?[0-9]|Z)$zh$$"                                                             => da(y, m, d, hh, mm, "0", "0", sign + zh)
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                       => da(y, m, d, hh, mm, "0", "0", sign + zh, sign + zm)
      case other                                                                                                                                                                                                                     =>
        error(s"Unrecognized date pattern: `$other`")
    }
  }

  def truncateDateStr(dateStr: String): String = {
    dateStr.trim match {
      case r"^(\d{1,4})$y$$"                                                                                                                                                                                                         => s"${p(y, 4)}-01-01"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m$$"                                                                                                                                                                                      => s"${p(y, 4)}-${p(m)}-01"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d$$"                                                                                                                                                          => s"${p(y, 4)}-${p(m)}-${p(d)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+00:00$$"                                                                                                                                               => s"${p(y, 4)}-${p(m)}-${p(d)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+00:00:00$$"                                                                                                                                            => s"${p(y, 4)}-${p(m)}-${p(d)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+00:00:00\.0{1,3}$$"                                                                                                                                    => s"${p(y, 4)}-${p(m)}-${p(d)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm$$"                                                                                                  => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:00$$"                                                                                               => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:00\.0{1,3}$$"                                                                                       => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss$$"                                                                          => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.0{1,3}$$"                                                                  => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms$$"                                                            => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}.$ms"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+00:00 *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                                                                    => s"${p(y, 4)}-${p(m)}-${p(d)}" + (if (local(sign, zh, zm)) "" else s" 00:00 $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+00:00:00 *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                                                                 => s"${p(y, 4)}-${p(m)}-${p(d)}" + (if (local(sign, zh, zm)) "" else s" 00:00 $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+00:00:00\.0{1,3} *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                                                         => s"${p(y, 4)}-${p(m)}-${p(d)}" + (if (local(sign, zh, zm)) "" else s" 00:00 $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                       => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}" + (if (local(sign, zh, zm)) "" else s" $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:00 *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                    => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}" + (if (local(sign, zh, zm)) "" else s" $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:00\.0{1,3} *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                            => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}" + (if (local(sign, zh, zm)) "" else s" $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"               => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}" + (if (local(sign, zh, zm)) "" else s" $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.0{1,3} *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"       => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}" + (if (local(sign, zh, zm)) "" else s" $sign$zh:$zm")
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$" => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}.$ms" + (if (local(sign, zh, zm)) "" else s" $sign$zh:$zm")
      case other                                                                                                                                                                                                                     =>
        error(s"Can't truncate unrecognized zoned date pattern: `$other`")
    }
  }

  def expandDateStr(dateStr: String): String = {
    dateStr.trim match {
      case r"^(\d{1,4})$y$$"                                                                                                                                                                                                         => s"${p(y, 4)}-01-01 00:00:00.000 $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m$$"                                                                                                                                                                                      => s"${p(y, 4)}-${p(m)}-01 00:00:00.000 $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d$$"                                                                                                                                                          => s"${p(y, 4)}-${p(m)}-${p(d)} 00:00:00.000 $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh$$"                                                                                                                          => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:00:00.000 $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm$$"                                                                                                  => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:00.000 $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss$$"                                                                          => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}.000 $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms$$"                                                            => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}.${p(ms, 3)} $localOffset"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss\.(\d{1,3})$ms *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$" => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}.${p(ms, 3)} $sign$zh:$zm"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm:([1-5][0-9]|0?[0-9])$ss *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"               => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:${p(ss)}.000 $sign$zh:$zm"
      case r"^(\d{1,4})$y-(1[0-2]|0?[0-9])$m-(3[01]|[12][0-9]|0?[0-9])$d['T ]+(2[0-3]|1[0-9]|0?[0-9])$hh:([1-5][0-9]|0?[0-9])$mm *([\+\-]?)$sign(1[0-2]|0?[0-9])$zh:([1-5][0-9]|0?[0-9])$zm$$"                                       => s"${p(y, 4)}-${p(m)}-${p(d)} ${p(hh)}:${p(mm)}:00.000 $sign$zh:$zm"
      case other                                                                                                                                                                                                                     =>
        error(s"Can't expand unrecognized date pattern: `$other`")
    }
  }
}
