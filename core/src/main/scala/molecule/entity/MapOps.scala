package molecule.entity

trait MapOps {

  implicit class MapAttr2MapOps[T](mapAttr: Option[Map[String, T]]) {

    def at(key: String): Option[T] = mapAttr.flatMap(_.get(key))

    def atOrElse(key: String, default: T): T = at(key).getOrElse(default)

    def mapAt(key1: String, keyN: String*): Map[String, T] = {
      val keys = key1 +: keyN
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => m.toList.foldLeft(Map[String, T]()) {
          case (acc, (k, v)) if keys.contains(k) => acc + (k -> v)
          case (acc, _)                          => acc
        }
      }
    }

    def values(needle1: T, needleN: T*): Map[String, T] = {
      val needles = needle1 +: needleN
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => needle1 match {
          case _: String => {
            val pattern = s"(?i).*(${needles.mkString("|")}).*".r.pattern
            m.toList.foldLeft(Map[String, T]()) {
              case (acc, (k, v)) if pattern.matcher(v.toString).matches => acc + (k -> v)
              case (acc, _)                                             => acc
            }
          }
          case _         => m.toList.foldLeft(Map[String, T]()) {
            case (acc, (k, v)) if needles.contains(v) => acc + (k -> v)
            case (acc, _)                             => acc
          }
        }
      }
    }

    def keyValue(keyNeedles: (String, T)*): Map[String, T] = {
      val subpatterns = keyNeedles.map { case (k, v) => s"$k.*$v.*" }.mkString("|")
      val pattern = s"(?i)($subpatterns)".r.pattern
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => m.toList.foldLeft(Map[String, T]()) {
          case (acc, (k, v)) if pattern.matcher(k + v).matches => acc + (k -> v)
          case (acc, _)                                        => acc
        }
      }
    }

    def keyValues(keyNeedle1: (String, Seq[T]), keyNeedleN: (String, Seq[T])*): Map[String, T] = {
      val keyNeedles = keyNeedle1 +: keyNeedleN
      val subpatterns = keyNeedles.map { case (k, vs) => s"$k.*(${vs.mkString("|")}).*" }.mkString("|")
      val pattern = s"(?i)($subpatterns)".r.pattern
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => m.toList.foldLeft(Map[String, T]()) {
          case (acc, (k, v)) if pattern.matcher(k + v).matches => acc + (k -> v)
          case (acc, _)                                        => acc
        }
      }
    }
  }
}
