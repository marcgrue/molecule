package molecule.transform
import java.lang.{Double => jDouble, Long => jLong}
import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
import java.util.{Iterator => jIterator}
import molecule.util.Helpers

/** Core molecule interface defining actions that can be called on molecules.
  *
  * Generally we could often have made higher-order methods taking a casting function for
  * variating cases. But we prioritize minimizing the macro-generated code as much as possible
  * to lower compile time overhead.
  */
trait CastHelpersNested[Tpl] extends Helpers {

  // card one

  protected def castNestedOneInt(it: jIterator[_]): Int =
    it.next.asInstanceOf[jLong].toInt

  protected def castNestedOneInt2(it: jIterator[_]): Int =
    it.next.asInstanceOf[java.lang.Integer].toInt

  protected def castNestedOneFloat(it: jIterator[_]): Float =
    it.next.asInstanceOf[jDouble].toFloat

  protected def castNestedOneBigInt(it: jIterator[_]): BigInt =
    BigInt(it.next.toString)

  protected def castNestedOneBigDecimal(it: jIterator[_]): BigDecimal =
    BigDecimal(it.next.asInstanceOf[jBigDec].toString)

  protected def castNestedOne[T](it: jIterator[_]): T =
    it.next.asInstanceOf[T]


  // Many ===========================================================================================

  //  protected def castManyInt(row: jList[_], i: Int): Set[Int] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[Int]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jLong].toInt
  //    set
  //  }
  //
  //  protected def castManyFloat(row: jList[_], i: Int): Set[Float] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[Float]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jDouble].toFloat
  //    set
  //  }
  //
  //  protected def castManyBigInt(row: jList[_], i: Int): Set[BigInt] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[BigInt]
  //    while (it.hasNext)
  //      set += BigInt(it.next.toString)
  //    set
  //  }
  //
  //  protected def castManyBigDecimal(row: jList[_], i: Int): Set[BigDecimal] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[BigDecimal]
  //    while (it.hasNext)
  //      set += BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
  //    set
  //  }
  //
  //  protected def castManyEnum(row: jList[_], i: Int): Set[String] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[String]
  //    while (it.hasNext)
  //      set += it.next.toString
  //    set
  //  }
  //
  //  protected def castMany[T](row: jList[_], i: Int): Set[T] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[T]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[T]
  //    set
  //  }


  // Optional card one nested pull ===========================================================================================

  protected def castNestedOptOneInt(it: jIterator[_]): Option[Int] =
    it.next match {
      case "__none__" => Option.empty[Int]
      case v          => Some(v.asInstanceOf[jLong].toInt)
    }

  protected def castNestedOptOneFloat(it: jIterator[_]): Option[Float] =
    it.next match {
      case "__none__" => Option.empty[Float]
      case v          => Some(v.asInstanceOf[jDouble].toFloat)
    }

  protected def castNestedOptOneLong(it: jIterator[_]): Option[Long] =
    it.next match {
      case "__none__" => Option.empty[Long]
      case v          => Some(v.asInstanceOf[jLong].toLong)
    }

  protected def castNestedOptOneDouble(it: jIterator[_]): Option[Double] =
    it.next match {
      case "__none__" => Option.empty[Double]
      case v          => Some(v.asInstanceOf[jDouble].toDouble)
    }

  protected def castNestedOptOneBigInt(it: jIterator[_]): Option[BigInt] =
    it.next match {
      case "__none__" => Option.empty[BigInt]
      case v          => Some(BigInt(v.asInstanceOf[jBigInt].toString))
    }

  protected def castNestedOptOneBigDecimal(it: jIterator[_]): Option[BigDecimal] =
    it.next match {
      case "__none__" => Option.empty[BigDecimal]
      case v          => Some(BigDecimal(v.asInstanceOf[jBigDec].toString))
    }

  protected def castNestedOptOne[T](it: jIterator[_]): Option[T] =
    it.next match {
      case "__none__" => Option.empty[T]
      case v          => Some(v.asInstanceOf[T])
    }

  //
  //  // Optional card many ===========================================================================================
  //
  //
  //  protected def castOptManyInt(row: jList[_], i: Int): Option[Set[Int]] = if (row.get(i) == null) {
  //    Option.empty[Set[Int]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[Int]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jLong].toInt
  //    Some(set)
  //  }
  //
  //  protected def castOptManyFloat(row: jList[_], i: Int): Option[Set[Float]] = if (row.get(i) == null) {
  //    Option.empty[Set[Float]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[Float]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jDouble].toFloat
  //    Some(set)
  //  }
  //
  //  protected def castOptManyLong(row: jList[_], i: Int): Option[Set[Long]] = if (row.get(i) == null) {
  //    Option.empty[Set[Long]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[Long]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jLong].toLong
  //    Some(set)
  //  }
  //
  //  protected def castOptManyDouble(row: jList[_], i: Int): Option[Set[Double]] = if (row.get(i) == null) {
  //    Option.empty[Set[Double]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[Double]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jDouble].toDouble
  //    Some(set)
  //  }
  //
  //  protected def castOptManyBigInt(row: jList[_], i: Int): Option[Set[BigInt]] = if (row.get(i) == null) {
  //    Option.empty[Set[BigInt]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[BigInt]
  //    while (it.hasNext)
  //      set += BigInt(it.next.asInstanceOf[jBigInt].toString)
  //    Some(set)
  //  }
  //
  //  protected def castOptManyBigDecimal(row: jList[_], i: Int): Option[Set[BigDecimal]] = if (row.get(i) == null) {
  //    Option.empty[Set[BigDecimal]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[BigDecimal]
  //    while (it.hasNext)
  //      set += BigDecimal(it.next.asInstanceOf[jBigDec].toString)
  //    Some(set)
  //  }
  //
  //  protected def castOptMany[T](row: jList[_], i: Int): Option[Set[T]] = if (row.get(i) == null) {
  //    Option.empty[Set[T]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[T]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[T]
  //    Some(set)
  //  }
  //
  //  // ------------------------------
  //
  //  protected def castOptManyApplyInt(row: jList[_], i: Int): Option[Set[Int]] = if (row.get(i) == null) {
  //    Option.empty[Set[Int]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[Int]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jLong].toInt
  //    Some(set)
  //  }
  //
  //  protected def castOptManyApplyFloat(row: jList[_], i: Int): Option[Set[Float]] = if (row.get(i) == null) {
  //    Option.empty[Set[Float]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[Float]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jDouble].toFloat
  //    Some(set)
  //  }
  //
  //  protected def castOptManyApplyLong(row: jList[_], i: Int): Option[Set[Long]] = if (row.get(i) == null) {
  //    Option.empty[Set[Long]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[Long]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jLong].toLong
  //    Some(set)
  //  }
  //
  //  protected def castOptManyApplyDouble(row: jList[_], i: Int): Option[Set[Double]] = if (row.get(i) == null) {
  //    Option.empty[Set[Double]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[Double]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jDouble].toDouble
  //    Some(set)
  //  }
  //
  //  protected def castOptManyApplyBigInt(row: jList[_], i: Int): Option[Set[BigInt]] = if (row.get(i) == null) {
  //    Option.empty[Set[BigInt]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[BigInt]
  //    while (it.hasNext)
  //      set += BigInt(it.next.asInstanceOf[jBigInt].toString)
  //    Some(set)
  //  }
  //
  //  protected def castOptManyApplyBigDecimal(row: jList[_], i: Int): Option[Set[BigDecimal]] = if (row.get(i) == null) {
  //    Option.empty[Set[BigDecimal]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[BigDecimal]
  //    while (it.hasNext)
  //      set += BigDecimal(it.next.asInstanceOf[jBigDec].toString)
  //    Some(set)
  //  }
  //
  //  protected def castOptManyApply[T](row: jList[_], i: Int): Option[Set[T]] = if (row.get(i) == null) {
  //    Option.empty[Set[T]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var set = Set.empty[T]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[T]
  //    Some(set)
  //  }
  //
  //
  //  // Optional ref attr ===========================================================================================
  //
  //  protected def castOptOneRefAttr(row: jList[_], i: Int): Option[Long] = if (row.get(i) == null) {
  //    Option.empty[Long]
  //  } else {
  //    Some(row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.asInstanceOf[jMap[_, _]].values.iterator.next.asInstanceOf[jLong].toLong)
  //  }
  //
  //  protected def castOptManyRefAttr(row: jList[_], i: Int): Option[Set[Long]] = if (row.get(i) == null) {
  //    Option.empty[Set[Long]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[Long]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jMap[_, _]].values.iterator.next.asInstanceOf[jLong].toLong
  //    Some(set)
  //  }
  //
  //
  //  // Enum ===========================================================================================
  //
  //  protected def castOptOneEnum(row: jList[_], i: Int): Option[String] = if (row.get(i) == null) {
  //    Option.empty[String]
  //  } else {
  //    Some(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName)
  //  }
  //
  //  protected def castOptManyEnum(row: jList[_], i: Int): Option[Set[String]] = if (row.get(i) == null) {
  //    Option.empty[Set[String]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var set = Set.empty[String]
  //    while (it.hasNext)
  //      set += it.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName
  //    Some(set)
  //  }
  //
  //
  //  // Map ===========================================================================================
  //
  //  protected def castMapString(row: jList[_], i: Int): Map[String, String] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, String]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1))
  //    }
  //    map
  //  }
  //
  //  protected def castMapInt(row: jList[_], i: Int): Map[String, Int] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Int]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toInt)
  //    }
  //    map
  //  }
  //
  //  protected def castMapFloat(row: jList[_], i: Int): Map[String, Float] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Float]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toFloat)
  //    }
  //    map
  //  }
  //
  //  protected def castMapBoolean(row: jList[_], i: Int): Map[String, Boolean] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Boolean]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toBoolean)
  //    }
  //    map
  //  }
  //
  //  protected def castMapLong(row: jList[_], i: Int): Map[String, Long] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Long]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toLong)
  //    }
  //    map
  //  }
  //
  //  protected def castMapDouble(row: jList[_], i: Int): Map[String, Double] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Double]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toDouble)
  //    }
  //    map
  //  }
  //
  //  protected def castMapDate(row: jList[_], i: Int): Map[String, Date] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Date]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> str2date(vs(1)))
  //    }
  //    map
  //  }
  //
  //  protected def castMapUUID(row: jList[_], i: Int): Map[String, UUID] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, UUID]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> UUID.fromString(vs(1)))
  //    }
  //    map
  //  }
  //
  //  protected def castMapURI(row: jList[_], i: Int): Map[String, URI] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, URI]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> new URI(vs(1)))
  //    }
  //    map
  //  }
  //
  //  protected def castMapBigInt(row: jList[_], i: Int): Map[String, BigInt] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, BigInt]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> BigInt(vs(1).toString))
  //    }
  //    map
  //  }
  //
  //  protected def castMapBigDecimal(row: jList[_], i: Int): Map[String, BigDecimal] = {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, BigDecimal]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> BigDecimal(vs(1).toString))
  //    }
  //    map
  //  }
  //
  //
  //  // Optional Map ===========================================================================================
  //
  //  protected def castOptMapString(row: jList[_], i: Int): Option[Map[String, String]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, String]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, String]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapInt(row: jList[_], i: Int): Option[Map[String, Int]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Int]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, Int]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toInt)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapFloat(row: jList[_], i: Int): Option[Map[String, Float]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Float]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, Float]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toFloat)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapBoolean(row: jList[_], i: Int): Option[Map[String, Boolean]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Boolean]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, Boolean]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toBoolean)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapLong(row: jList[_], i: Int): Option[Map[String, Long]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Long]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, Long]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toLong)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapDouble(row: jList[_], i: Int): Option[Map[String, Double]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Double]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, Double]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toDouble)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapDate(row: jList[_], i: Int): Option[Map[String, Date]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Date]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, Date]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> str2date(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapUUID(row: jList[_], i: Int): Option[Map[String, UUID]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, UUID]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, UUID]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> UUID.fromString(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapURI(row: jList[_], i: Int): Option[Map[String, URI]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, URI]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, URI]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> new URI(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapBigInt(row: jList[_], i: Int): Option[Map[String, BigInt]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, BigInt]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, BigInt]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> BigInt(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapBigDecimal(row: jList[_], i: Int): Option[Map[String, BigDecimal]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, BigDecimal]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
  //    var map = Map.empty[String, BigDecimal]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> BigDecimal(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //
  //  // Optional Map apply ===========================================================================================
  //
  //  protected def castOptMapApplyString(row: jList[_], i: Int): Option[Map[String, String]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, String]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, String]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyInt(row: jList[_], i: Int): Option[Map[String, Int]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Int]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Int]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toInt)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyFloat(row: jList[_], i: Int): Option[Map[String, Float]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Float]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Float]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toFloat)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyBoolean(row: jList[_], i: Int): Option[Map[String, Boolean]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Boolean]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Boolean]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toBoolean)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyLong(row: jList[_], i: Int): Option[Map[String, Long]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Long]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Long]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toLong)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyDouble(row: jList[_], i: Int): Option[Map[String, Double]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Double]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Double]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> vs(1).toDouble)
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyDate(row: jList[_], i: Int): Option[Map[String, Date]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, Date]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, Date]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> str2date(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyUUID(row: jList[_], i: Int): Option[Map[String, UUID]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, UUID]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, UUID]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> UUID.fromString(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyURI(row: jList[_], i: Int): Option[Map[String, URI]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, URI]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, URI]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> new URI(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyBigInt(row: jList[_], i: Int): Option[Map[String, BigInt]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, BigInt]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, BigInt]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> BigInt(vs(1)))
  //    }
  //    Some(map)
  //  }
  //
  //  protected def castOptMapApplyBigDecimal(row: jList[_], i: Int): Option[Map[String, BigDecimal]] = if (row.get(i) == null) {
  //    Option.empty[Map[String, BigDecimal]]
  //  } else {
  //    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
  //    var map = Map.empty[String, BigDecimal]
  //    var vs  = new Array[String](2)
  //    while (it.hasNext) {
  //      vs = it.next.toString.split("@", 2)
  //      map += (vs(0) -> BigDecimal(vs(1)))
  //    }
  //    Some(map)
  //  }
}
