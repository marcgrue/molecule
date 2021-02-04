package molecule.core.transform

import java.lang.{Double => jDouble, Long => jLong}
import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
import java.net.URI
import java.util.{Date, UUID, Iterator => jIterator, Map => jMap}
import clojure.lang.{Keyword, PersistentArrayMap, PersistentVector}
import molecule.core.util.Helpers

/** Core molecule interface defining actions that can be called on molecules.
  *
  * Generally we could often have made higher-order methods taking a casting function for
  * variating cases. But we prioritize minimizing the macro-generated code as much as possible
  * to lower compile time overhead.
  */
trait CastHelpersOptNested extends Helpers {

  // card one

  protected def castOptNestedOneInt(it: jIterator[_]): Int =
    it.next.asInstanceOf[jLong].toInt

  protected def castOptNestedOneInt2(it: jIterator[_]): Int =
    it.next.toString.toInt
//    it.next.asInstanceOf[java.lang.Integer].toInt

  protected def castOptNestedOneFloat(it: jIterator[_]): Float =
    it.next.asInstanceOf[jDouble].toFloat

  protected def castOptNestedOneBigInt(it: jIterator[_]): BigInt =
    BigInt(it.next.toString)

  protected def castOptNestedOneBigDecimal(it: jIterator[_]): BigDecimal =
    BigDecimal(it.next.asInstanceOf[jBigDec].toString)

  protected def castOptNestedOne[T](it: jIterator[_]): T =
    it.next.asInstanceOf[T]

  protected def castOptNestedOneEnum(it: jIterator[_]): String =
    it.next
      .asInstanceOf[PersistentArrayMap].valIterator.next
      .asInstanceOf[Keyword].getName

  protected def castOptNestedOneRefAttr(it: jIterator[_]): Long = {
    it.next
      .asInstanceOf[PersistentArrayMap].valIterator.next
      .asInstanceOf[jLong].toLong
  }


  // Many ===========================================================================================

  protected def castOptNestedManyInt(it: jIterator[_]): Set[Int] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[Int]
    while (it1.hasNext)
      set += it1.next.asInstanceOf[jLong].toInt
    set
  }

  protected def castOptNestedManyFloat(it: jIterator[_]): Set[Float] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[Float]
    while (it1.hasNext)
      set += it1.next.asInstanceOf[jDouble].toFloat
    set
  }

  protected def castOptNestedManyBigInt(it: jIterator[_]): Set[BigInt] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[BigInt]
    while (it1.hasNext)
      set += BigInt(it1.next.toString)
    set
  }

  protected def castOptNestedManyBigDecimal(it: jIterator[_]): Set[BigDecimal] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[BigDecimal]
    while (it1.hasNext)
      set += BigDecimal(it1.next.asInstanceOf[java.math.BigDecimal].toString)
    set
  }

  protected def castOptNestedMany[T](it: jIterator[_]): Set[T] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[T]
    while (it1.hasNext)
      set += it1.next.asInstanceOf[T]
    set
  }

  protected def castOptNestedManyEnum(it: jIterator[_]): Set[String] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[String]
    while (it1.hasNext)
      set += it1.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName
    set
  }

  protected def castOptNestedManyRefAttr(it: jIterator[_]): Set[Long] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var set = Set.empty[Long]
    while (it1.hasNext)
      set += it1.next
        .asInstanceOf[PersistentArrayMap].valIterator.next
        .asInstanceOf[jLong].toLong
    set
  }


  // Optional card one ===========================================================================================

  protected def castOptNestedOptOneInt(it: jIterator[_]): Option[Int] =
    it.next match {
      case "__none__" => Option.empty[Int]
      case v          => Some(v.asInstanceOf[jLong].toInt)
    }

  protected def castOptNestedOptOneFloat(it: jIterator[_]): Option[Float] =
    it.next match {
      case "__none__" => Option.empty[Float]
      case v          => Some(v.asInstanceOf[jDouble].toFloat)
    }

  protected def castOptNestedOptOneLong(it: jIterator[_]): Option[Long] =
    it.next match {
      case "__none__" => Option.empty[Long]
      case v          => Some(v.asInstanceOf[jLong].toLong)
    }

  protected def castOptNestedOptOneDouble(it: jIterator[_]): Option[Double] =
    it.next match {
      case "__none__" => Option.empty[Double]
      case v          => Some(v.asInstanceOf[jDouble].toDouble)
    }

  protected def castOptNestedOptOneBigInt(it: jIterator[_]): Option[BigInt] =
    it.next match {
      case "__none__" => Option.empty[BigInt]
      case v          => Some(BigInt(v.asInstanceOf[jBigInt].toString))
    }

  protected def castOptNestedOptOneBigDecimal(it: jIterator[_]): Option[BigDecimal] =
    it.next match {
      case "__none__" => Option.empty[BigDecimal]
      case v          => Some(BigDecimal(v.asInstanceOf[jBigDec].toString))
    }

  protected def castOptNestedOptOne[T](it: jIterator[_]): Option[T] =
    it.next match {
      case "__none__" => Option.empty[T]
      case v          => Some(v.asInstanceOf[T])
    }

  protected def castOptNestedOptOneEnum(it: jIterator[_]): Option[String] =
    it.next match {
      case "__none__" => Option.empty[String]
      case v          =>
        Some(
          v.asInstanceOf[PersistentArrayMap].valIterator.next
            .asInstanceOf[Keyword].getName
        )
    }

  protected def castOptNestedOptOneRefAttr(it: jIterator[_]): Option[Long] =
    it.next match {
      case "__none__" => Option.empty[Long]
      case v          =>
        Some(v
          .asInstanceOf[jMap[_, _]].values.iterator.next
          .asInstanceOf[jLong].toLong
        )
    }

  // Optional card many ===========================================================================================


  protected def castOptNestedOptManyInt(it: jIterator[_]): Option[Set[Int]] =
    it.next match {
      case "__none__" => Option.empty[Set[Int]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[Int]
        while (it1.hasNext)
          set += it1.next.asInstanceOf[jLong].toInt
        Some(set)
    }

  protected def castOptNestedOptManyFloat(it: jIterator[_]): Option[Set[Float]] =
    it.next match {
      case "__none__" => Option.empty[Set[Float]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[Float]
        while (it1.hasNext)
          set += it1.next.asInstanceOf[jDouble].toFloat
        Some(set)
    }

  protected def castOptNestedOptManyLong(it: jIterator[_]): Option[Set[Long]] =
    it.next match {
      case "__none__" => Option.empty[Set[Long]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[Long]
        while (it1.hasNext)
          set += it1.next.asInstanceOf[jLong].toLong
        Some(set)
    }

  protected def castOptNestedOptManyDouble(it: jIterator[_]): Option[Set[Double]] =
    it.next match {
      case "__none__" => Option.empty[Set[Double]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[Double]
        while (it1.hasNext)
          set += it1.next.asInstanceOf[jDouble].toDouble
        Some(set)
    }

  protected def castOptNestedOptManyBigInt(it: jIterator[_]): Option[Set[BigInt]] =
    it.next match {
      case "__none__" => Option.empty[Set[BigInt]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[BigInt]
        while (it1.hasNext)
          set += BigInt(it1.next.asInstanceOf[jBigInt].toString)
        Some(set)
    }

  protected def castOptNestedOptManyBigDecimal(it: jIterator[_]): Option[Set[BigDecimal]] =
    it.next match {
      case "__none__" => Option.empty[Set[BigDecimal]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[BigDecimal]
        while (it1.hasNext)
          set += BigDecimal(it1.next.asInstanceOf[jBigDec].toString)
        Some(set)
    }

  protected def castOptNestedOptMany[T](it: jIterator[_]): Option[Set[T]] =
    it.next match {
      case "__none__" => Option.empty[Set[T]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[T]
        while (it1.hasNext)
          set += it1.next.asInstanceOf[T]
        Some(set)
    }

  protected def castOptNestedOptManyEnum(it: jIterator[_]): Option[Set[String]] =
    it.next match {
      case "__none__" => Option.empty[Set[String]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[String]
        while (it1.hasNext)
          set += it1.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName
        Some(set)
    }

  protected def castOptNestedOptManyRefAttr(it: jIterator[_]): Option[Set[Long]] =
    it.next match {
      case "__none__" => Option.empty[Set[Long]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var set = Set.empty[Long]
        while (it1.hasNext)
          set += it1.next
            .asInstanceOf[PersistentArrayMap].valIterator.next
            .asInstanceOf[jLong].toLong
        Some(set)
    }


  // Map ===========================================================================================

  protected def castOptNestedMapString(it: jIterator[_]): Map[String, String] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, String]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> vs(1))
    }
    map
  }

  protected def castOptNestedMapInt(it: jIterator[_]): Map[String, Int] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, Int]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toInt)
    }
    map
  }

  protected def castOptNestedMapFloat(it: jIterator[_]): Map[String, Float] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, Float]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toFloat)
    }
    map
  }

  protected def castOptNestedMapBoolean(it: jIterator[_]): Map[String, Boolean] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, Boolean]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toBoolean)
    }
    map
  }

  protected def castOptNestedMapLong(it: jIterator[_]): Map[String, Long] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, Long]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toLong)
    }
    map
  }

  protected def castOptNestedMapDouble(it: jIterator[_]): Map[String, Double] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, Double]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toDouble)
    }
    map
  }

  protected def castOptNestedMapDate(it: jIterator[_]): Map[String, Date] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, Date]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> str2date(vs(1)))
    }
    map
  }

  protected def castOptNestedMapUUID(it: jIterator[_]): Map[String, UUID] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, UUID]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> UUID.fromString(vs(1)))
    }
    map
  }

  protected def castOptNestedMapURI(it: jIterator[_]): Map[String, URI] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, URI]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> new URI(vs(1)))
    }
    map
  }

  protected def castOptNestedMapBigInt(it: jIterator[_]): Map[String, BigInt] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, BigInt]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> BigInt(vs(1).toString))
    }
    map
  }

  protected def castOptNestedMapBigDecimal(it: jIterator[_]): Map[String, BigDecimal] = {
    val it1 = it.next.asInstanceOf[PersistentVector].iterator
    var map = Map.empty[String, BigDecimal]
    var vs  = new Array[String](2)
    while (it1.hasNext) {
      vs = it1.next.toString.split("@", 2)
      map += (vs(0) -> BigDecimal(vs(1).toString))
    }
    map
  }


  // Optional Map ===========================================================================================

  protected def castOptNestedOptMapString(it: jIterator[_]): Option[Map[String, String]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, String]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, String]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> vs(1))
        }
        Some(map)
    }

  protected def castOptNestedOptMapInt(it: jIterator[_]): Option[Map[String, Int]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, Int]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, Int]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> vs(1).toInt)
        }
        Some(map)
    }

  protected def castOptNestedOptMapFloat(it: jIterator[_]): Option[Map[String, Float]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, Float]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, Float]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> vs(1).toFloat)
        }
        Some(map)
    }

  protected def castOptNestedOptMapBoolean(it: jIterator[_]): Option[Map[String, Boolean]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, Boolean]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, Boolean]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> vs(1).toBoolean)
        }
        Some(map)
    }

  protected def castOptNestedOptMapLong(it: jIterator[_]): Option[Map[String, Long]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, Long]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, Long]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> vs(1).toLong)
        }
        Some(map)
    }

  protected def castOptNestedOptMapDouble(it: jIterator[_]): Option[Map[String, Double]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, Double]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, Double]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> vs(1).toDouble)
        }
        Some(map)
    }

  protected def castOptNestedOptMapDate(it: jIterator[_]): Option[Map[String, Date]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, Date]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, Date]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> str2date(vs(1)))
        }
        Some(map)
    }

  protected def castOptNestedOptMapUUID(it: jIterator[_]): Option[Map[String, UUID]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, UUID]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, UUID]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> UUID.fromString(vs(1)))
        }
        Some(map)
    }

  protected def castOptNestedOptMapURI(it: jIterator[_]): Option[Map[String, URI]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, URI]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, URI]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> new URI(vs(1)))
        }
        Some(map)
    }

  protected def castOptNestedOptMapBigInt(it: jIterator[_]): Option[Map[String, BigInt]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, BigInt]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, BigInt]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> BigInt(vs(1)))
        }
        Some(map)
    }

  protected def castOptNestedOptMapBigDecimal(it: jIterator[_]): Option[Map[String, BigDecimal]] =
    it.next match {
      case "__none__" => Option.empty[Map[String, BigDecimal]]
      case v          =>
        val it1 = v.asInstanceOf[PersistentVector].iterator
        var map = Map.empty[String, BigDecimal]
        var vs  = new Array[String](2)
        while (it1.hasNext) {
          vs = it1.next.toString.split("@", 2)
          map += (vs(0) -> BigDecimal(vs(1)))
        }
        Some(map)
    }
}
