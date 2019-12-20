package molecule.transform
import java.lang.{Double => jDouble, Long => jLong}
import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
import java.net.URI
import java.util.{Date, UUID, List => jList, Map => jMap}
import clojure.lang.{Keyword, PersistentHashSet, PersistentVector}

/** Core molecule interface defining actions that can be called on molecules.
  *
  * Generally we could often have made higher-order methods taking a casting function for
  * variating cases. But we prioritize minimizing the macro-generated code as much as possible
  * to lower compile time overhead.
  */
trait CastHelpers[Tpl] extends CastHelpersAggr[Tpl] {


  // Standard row cast interface to be materialized by macro
  protected def castRow(row: jList[AnyRef]): Tpl = ???


  // One ===========================================================================================

  protected def castOneInt(row: jList[_], i: Int): Int =
    row.get(i).asInstanceOf[jLong].toInt

  protected def castOneInt2(row: jList[_], i: Int): Int =
    row.get(i).asInstanceOf[java.lang.Integer].toInt

  protected def castOneFloat(row: jList[_], i: Int): Float =
    row.get(i).asInstanceOf[jDouble].toFloat

  protected def castOneBigInt(row: jList[_], i: Int): BigInt =
    BigInt(row.get(i).toString)

  protected def castOneBigDecimal(row: jList[_], i: Int): BigDecimal =
    BigDecimal(row.get(i).asInstanceOf[jBigDec].toString)

  protected def castOne[T](row: jList[_], i: Int): T =
    row.get(i).asInstanceOf[T]


  // Many ===========================================================================================

  protected def castManyInt(row: jList[_], i: Int): Set[Int] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Int]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toInt
    set
  }

  protected def castManyFloat(row: jList[_], i: Int): Set[Float] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Float]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toFloat
    set
  }

  protected def castManyBigInt(row: jList[_], i: Int): Set[BigInt] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[BigInt]
    while (it.hasNext)
      set += BigInt(it.next.toString)
    set
  }

  protected def castManyBigDecimal(row: jList[_], i: Int): Set[BigDecimal] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[BigDecimal]
    while (it.hasNext)
      set += BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    set
  }

  protected def castManyEnum(row: jList[_], i: Int): Set[String] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[String]
    while (it.hasNext)
      set += it.next.toString
    set
  }

  protected def castMany[T](row: jList[_], i: Int): Set[T] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[T]
    while (it.hasNext)
      set += it.next.asInstanceOf[T]
    set
  }


  // Optional card one ===========================================================================================

  // Datomic pull requests for optional value either returns `null` or a `jMap[<ident>, <value>]`

  protected def castOptOneInt(row: jList[_], i: Int): Option[Int] = if (row.get(i) == null) {
    Option.empty[Int]
  } else {
    Some(row.get(i).asInstanceOf[jMap[_, _]].values.iterator.next.asInstanceOf[jLong].toInt)
  }

  protected def castOptOneFloat(row: jList[_], i: Int): Option[Float] = if (row.get(i) == null) {
    Option.empty[Float]
  } else {
    Some(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jDouble].toFloat)
  }

  protected def castOptOneLong(row: jList[_], i: Int): Option[Long] = if (row.get(i) == null) {
    Option.empty[Long]
  } else {
    Some(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jLong].toLong)
  }

  protected def castOptOneDouble(row: jList[_], i: Int): Option[Double] = if (row.get(i) == null) {
    Option.empty[Double]
  } else {
    Some(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jDouble].toDouble)
  }

  protected def castOptOneBigInt(row: jList[_], i: Int): Option[BigInt] = if (row.get(i) == null) {
    Option.empty[BigInt]
  } else {
    Some(BigInt(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jBigInt].toString))
  }

  protected def castOptOneBigDecimal(row: jList[_], i: Int): Option[BigDecimal] = if (row.get(i) == null) {
    Option.empty[BigDecimal]
  } else {
    Some(BigDecimal(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jBigDec].toString))
  }

  protected def castOptOne[T](row: jList[_], i: Int): Option[T] = if (row.get(i) == null) {
    Option.empty[T]
  } else {
    Some(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[T])
  }

  // ----------------------------------------------

  protected def castOptOneApplyInt(row: jList[_], i: Int): Option[Int] = if (row.get(i) == null) {
    Option.empty[Int]
  } else {
    Some(row.get(i).asInstanceOf[jLong].toInt)
  }

  protected def castOptOneApplyFloat(row: jList[_], i: Int): Option[Float] = if (row.get(i) == null) {
    Option.empty[Float]
  } else {
    Some(row.get(i).asInstanceOf[jDouble].toFloat)
  }

  protected def castOptOneApplyLong(row: jList[_], i: Int): Option[Long] = if (row.get(i) == null) {
    Option.empty[Long]
  } else {
    Some(row.get(i).asInstanceOf[jLong].toLong)
  }

  protected def castOptOneApplyDouble(row: jList[_], i: Int): Option[Double] = if (row.get(i) == null) {
    Option.empty[Double]
  } else {
    Some(row.get(i).asInstanceOf[jDouble].toDouble)
  }

  protected def castOptOneApplyBigInt(row: jList[_], i: Int): Option[BigInt] = if (row.get(i) == null) {
    Option.empty[BigInt]
  } else {
    Some(BigInt(row.get(i).asInstanceOf[jBigInt].toString))
  }

  protected def castOptOneApplyBigDecimal(row: jList[_], i: Int): Option[BigDecimal] = if (row.get(i) == null) {
    Option.empty[BigDecimal]
  } else {
    Some(BigDecimal(row.get(i).asInstanceOf[jBigDec].toString))
  }

  // Datomic pull requests for optional value either returns `null` or a `jMap[<ident>, <value>]`
  protected def castOptOneApply[T](row: jList[_], i: Int): Option[T] = if (row.get(i) == null) {
    Option.empty[T]
  } else {
    Some(row.get(i).asInstanceOf[T])
  }


  // Optional card many ===========================================================================================

  protected def castOptManyInt(row: jList[_], i: Int): Option[Set[Int]] = if (row.get(i) == null) {
    Option.empty[Set[Int]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[Int]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toInt
    Some(set)
  }

  protected def castOptManyFloat(row: jList[_], i: Int): Option[Set[Float]] = if (row.get(i) == null) {
    Option.empty[Set[Float]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[Float]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toFloat
    Some(set)
  }

  protected def castOptManyLong(row: jList[_], i: Int): Option[Set[Long]] = if (row.get(i) == null) {
    Option.empty[Set[Long]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[Long]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toLong
    Some(set)
  }

  protected def castOptManyDouble(row: jList[_], i: Int): Option[Set[Double]] = if (row.get(i) == null) {
    Option.empty[Set[Double]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[Double]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toDouble
    Some(set)
  }

  protected def castOptManyBigInt(row: jList[_], i: Int): Option[Set[BigInt]] = if (row.get(i) == null) {
    Option.empty[Set[BigInt]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[BigInt]
    while (it.hasNext)
      set += BigInt(it.next.asInstanceOf[jBigInt].toString)
    Some(set)
  }

  protected def castOptManyBigDecimal(row: jList[_], i: Int): Option[Set[BigDecimal]] = if (row.get(i) == null) {
    Option.empty[Set[BigDecimal]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[BigDecimal]
    while (it.hasNext)
      set += BigDecimal(it.next.asInstanceOf[jBigDec].toString)
    Some(set)
  }

  protected def castOptMany[T](row: jList[_], i: Int): Option[Set[T]] = if (row.get(i) == null) {
    Option.empty[Set[T]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[T]
    while (it.hasNext)
      set += it.next.asInstanceOf[T]
    Some(set)
  }

  // ------------------------------

  protected def castOptManyApplyInt(row: jList[_], i: Int): Option[Set[Int]] = if (row.get(i) == null) {
    Option.empty[Set[Int]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Int]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toInt
    Some(set)
  }

  protected def castOptManyApplyFloat(row: jList[_], i: Int): Option[Set[Float]] = if (row.get(i) == null) {
    Option.empty[Set[Float]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Float]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toFloat
    Some(set)
  }

  protected def castOptManyApplyLong(row: jList[_], i: Int): Option[Set[Long]] = if (row.get(i) == null) {
    Option.empty[Set[Long]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Long]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toLong
    Some(set)
  }

  protected def castOptManyApplyDouble(row: jList[_], i: Int): Option[Set[Double]] = if (row.get(i) == null) {
    Option.empty[Set[Double]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Double]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toDouble
    Some(set)
  }

  protected def castOptManyApplyBigInt(row: jList[_], i: Int): Option[Set[BigInt]] = if (row.get(i) == null) {
    Option.empty[Set[BigInt]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[BigInt]
    while (it.hasNext)
      set += BigInt(it.next.asInstanceOf[jBigInt].toString)
    Some(set)
  }

  protected def castOptManyApplyBigDecimal(row: jList[_], i: Int): Option[Set[BigDecimal]] = if (row.get(i) == null) {
    Option.empty[Set[BigDecimal]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[BigDecimal]
    while (it.hasNext)
      set += BigDecimal(it.next.asInstanceOf[jBigDec].toString)
    Some(set)
  }

  protected def castOptManyApply[T](row: jList[_], i: Int): Option[Set[T]] = if (row.get(i) == null) {
    Option.empty[Set[T]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[T]
    while (it.hasNext)
      set += it.next.asInstanceOf[T]
    Some(set)
  }


  // Optional ref attr ===========================================================================================

  protected def castOptOneRefAttr(row: jList[_], i: Int): Option[Long] = if (row.get(i) == null) {
    Option.empty[Long]
  } else {
    Some(row.get(i)
      .asInstanceOf[jMap[String, PersistentVector]].values.iterator.next
      .asInstanceOf[jMap[_, _]].values.iterator.next
      .asInstanceOf[jLong].toLong)
  }

  protected def castOptManyRefAttr(row: jList[_], i: Int): Option[Set[Long]] = if (row.get(i) == null) {
    Option.empty[Set[Long]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[Long]
    while (it.hasNext)
      set += it.next.asInstanceOf[jMap[_, _]].values.iterator.next.asInstanceOf[jLong].toLong
    Some(set)
  }


  // Enum ===========================================================================================

  protected def castOptOneEnum(row: jList[_], i: Int): Option[String] = if (row.get(i) == null) {
    Option.empty[String]
  } else {
    Some(row.get(i).asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName)
  }

  protected def castOptManyEnum(row: jList[_], i: Int): Option[Set[String]] = if (row.get(i) == null) {
    Option.empty[Set[String]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var set = Set.empty[String]
    while (it.hasNext)
      set += it.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName
    Some(set)
  }


  // Map ===========================================================================================

  protected def castMapString(row: jList[_], i: Int): Map[String, String] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, String]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1))
    }
    map
  }

  protected def castMapInt(row: jList[_], i: Int): Map[String, Int] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Int]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toInt)
    }
    map
  }

  protected def castMapFloat(row: jList[_], i: Int): Map[String, Float] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Float]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toFloat)
    }
    map
  }

  protected def castMapBoolean(row: jList[_], i: Int): Map[String, Boolean] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Boolean]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toBoolean)
    }
    map
  }

  protected def castMapLong(row: jList[_], i: Int): Map[String, Long] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Long]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toLong)
    }
    map
  }

  protected def castMapDouble(row: jList[_], i: Int): Map[String, Double] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Double]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toDouble)
    }
    map
  }

  protected def castMapDate(row: jList[_], i: Int): Map[String, Date] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Date]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> str2date(vs(1)))
    }
    map
  }

  protected def castMapUUID(row: jList[_], i: Int): Map[String, UUID] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, UUID]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> UUID.fromString(vs(1)))
    }
    map
  }

  protected def castMapURI(row: jList[_], i: Int): Map[String, URI] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, URI]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> new URI(vs(1)))
    }
    map
  }

  protected def castMapBigInt(row: jList[_], i: Int): Map[String, BigInt] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, BigInt]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> BigInt(vs(1).toString))
    }
    map
  }

  protected def castMapBigDecimal(row: jList[_], i: Int): Map[String, BigDecimal] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, BigDecimal]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> BigDecimal(vs(1).toString))
    }
    map
  }


  // Optional Map ===========================================================================================

  protected def castOptMapString(row: jList[_], i: Int): Option[Map[String, String]] = if (row.get(i) == null) {
    Option.empty[Map[String, String]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, String]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1))
    }
    Some(map)
  }

  protected def castOptMapInt(row: jList[_], i: Int): Option[Map[String, Int]] = if (row.get(i) == null) {
    Option.empty[Map[String, Int]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, Int]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toInt)
    }
    Some(map)
  }

  protected def castOptMapFloat(row: jList[_], i: Int): Option[Map[String, Float]] = if (row.get(i) == null) {
    Option.empty[Map[String, Float]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, Float]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toFloat)
    }
    Some(map)
  }

  protected def castOptMapBoolean(row: jList[_], i: Int): Option[Map[String, Boolean]] = if (row.get(i) == null) {
    Option.empty[Map[String, Boolean]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, Boolean]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toBoolean)
    }
    Some(map)
  }

  protected def castOptMapLong(row: jList[_], i: Int): Option[Map[String, Long]] = if (row.get(i) == null) {
    Option.empty[Map[String, Long]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, Long]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toLong)
    }
    Some(map)
  }

  protected def castOptMapDouble(row: jList[_], i: Int): Option[Map[String, Double]] = if (row.get(i) == null) {
    Option.empty[Map[String, Double]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, Double]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toDouble)
    }
    Some(map)
  }

  protected def castOptMapDate(row: jList[_], i: Int): Option[Map[String, Date]] = if (row.get(i) == null) {
    Option.empty[Map[String, Date]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, Date]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> str2date(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapUUID(row: jList[_], i: Int): Option[Map[String, UUID]] = if (row.get(i) == null) {
    Option.empty[Map[String, UUID]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, UUID]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> UUID.fromString(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapURI(row: jList[_], i: Int): Option[Map[String, URI]] = if (row.get(i) == null) {
    Option.empty[Map[String, URI]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, URI]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> new URI(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapBigInt(row: jList[_], i: Int): Option[Map[String, BigInt]] = if (row.get(i) == null) {
    Option.empty[Map[String, BigInt]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, BigInt]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> BigInt(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapBigDecimal(row: jList[_], i: Int): Option[Map[String, BigDecimal]] = if (row.get(i) == null) {
    Option.empty[Map[String, BigDecimal]]
  } else {
    val it  = row.get(i).asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
    var map = Map.empty[String, BigDecimal]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> BigDecimal(vs(1)))
    }
    Some(map)
  }


  // Optional Map apply ===========================================================================================

  protected def castOptMapApplyString(row: jList[_], i: Int): Option[Map[String, String]] = if (row.get(i) == null) {
    Option.empty[Map[String, String]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, String]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1))
    }
    Some(map)
  }

  protected def castOptMapApplyInt(row: jList[_], i: Int): Option[Map[String, Int]] = if (row.get(i) == null) {
    Option.empty[Map[String, Int]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Int]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toInt)
    }
    Some(map)
  }

  protected def castOptMapApplyFloat(row: jList[_], i: Int): Option[Map[String, Float]] = if (row.get(i) == null) {
    Option.empty[Map[String, Float]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Float]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toFloat)
    }
    Some(map)
  }

  protected def castOptMapApplyBoolean(row: jList[_], i: Int): Option[Map[String, Boolean]] = if (row.get(i) == null) {
    Option.empty[Map[String, Boolean]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Boolean]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toBoolean)
    }
    Some(map)
  }

  protected def castOptMapApplyLong(row: jList[_], i: Int): Option[Map[String, Long]] = if (row.get(i) == null) {
    Option.empty[Map[String, Long]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Long]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toLong)
    }
    Some(map)
  }

  protected def castOptMapApplyDouble(row: jList[_], i: Int): Option[Map[String, Double]] = if (row.get(i) == null) {
    Option.empty[Map[String, Double]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Double]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> vs(1).toDouble)
    }
    Some(map)
  }

  protected def castOptMapApplyDate(row: jList[_], i: Int): Option[Map[String, Date]] = if (row.get(i) == null) {
    Option.empty[Map[String, Date]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, Date]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> str2date(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapApplyUUID(row: jList[_], i: Int): Option[Map[String, UUID]] = if (row.get(i) == null) {
    Option.empty[Map[String, UUID]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, UUID]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> UUID.fromString(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapApplyURI(row: jList[_], i: Int): Option[Map[String, URI]] = if (row.get(i) == null) {
    Option.empty[Map[String, URI]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, URI]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> new URI(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapApplyBigInt(row: jList[_], i: Int): Option[Map[String, BigInt]] = if (row.get(i) == null) {
    Option.empty[Map[String, BigInt]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, BigInt]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> BigInt(vs(1)))
    }
    Some(map)
  }

  protected def castOptMapApplyBigDecimal(row: jList[_], i: Int): Option[Map[String, BigDecimal]] = if (row.get(i) == null) {
    Option.empty[Map[String, BigDecimal]]
  } else {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var map = Map.empty[String, BigDecimal]
    var vs  = new Array[String](2)
    while (it.hasNext) {
      vs = it.next.toString.split("@", 2)
      map += (vs(0) -> BigDecimal(vs(1)))
    }
    Some(map)
  }
}
