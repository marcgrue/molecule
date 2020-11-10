package molecule.core.transform

import java.lang.{Double => jDouble, Long => jLong}
import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
import java.net.URI
import java.util.{Date, UUID, List => jList, Map => jMap}
import clojure.lang.{Keyword, LazySeq, PersistentHashSet, PersistentVector}

/** Core molecule interface defining actions that can be called on molecules.
  *
  * Generally we could often have made higher-order methods taking a casting function for
  * variating cases. But we prioritize minimizing the macro-generated code as much as possible
  * to lower compile time overhead.
  */
trait CastHelpersAggr[Tpl] extends CastHelpersOptNested[Tpl] {

  // card one

  protected def castAggrOneListInt(row: jList[_], i: Int): List[Int] = {
    val it   = row.get(i).asInstanceOf[PersistentVector].iterator
    var list = List.empty[Int]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[jLong].toInt
    list
  }

  protected def castAggrOneListFloat(row: jList[_], i: Int): List[Float] = {
    val it   = row.get(i).asInstanceOf[PersistentVector].iterator
    var list = List.empty[Float]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[jDouble].toFloat
    list
  }

  protected def castAggrOneListBigInt(row: jList[_], i: Int): List[BigInt] = {
    val it   = row.get(i).asInstanceOf[PersistentVector].iterator
    var list = List.empty[BigInt]
    while (it.hasNext)
      list = list :+ BigInt(it.next.toString)
    list
  }

  protected def castAggrOneListBigDecimal(row: jList[_], i: Int): List[BigDecimal] = {
    val it   = row.get(i).asInstanceOf[PersistentVector].iterator
    var list = List.empty[BigDecimal]
    while (it.hasNext)
      list = list :+ BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    list
  }

  protected def castAggrOneList[T](row: jList[_], i: Int): List[T] = {
    val it   = row.get(i).asInstanceOf[PersistentVector].iterator
    var list = List.empty[T]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[T]
    list
  }

  // card many

  protected def castAggrManyListInt(row: jList[_], i: Int): List[Set[Int]] = {
    val it  = row.get(i).asInstanceOf[PersistentVector].iterator
    var set = Set.empty[Int]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toInt
    List(set)
  }

  protected def castAggrManyListFloat(row: jList[_], i: Int): List[Set[Float]] = {
    val it  = row.get(i).asInstanceOf[PersistentVector].iterator
    var set = Set.empty[Float]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toFloat
    List(set)
  }

  protected def castAggrManyListBigInt(row: jList[_], i: Int): List[Set[BigInt]] = {
    val it  = row.get(i).asInstanceOf[PersistentVector].iterator
    var set = Set.empty[BigInt]
    while (it.hasNext)
      set += BigInt(it.next.toString)
    List(set)
  }

  protected def castAggrManyListBigDecimal(row: jList[_], i: Int): List[Set[BigDecimal]] = {
    val it  = row.get(i).asInstanceOf[PersistentVector].iterator
    var set = Set.empty[BigDecimal]
    while (it.hasNext)
      set += BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    List(set)
  }

  protected def castAggrManyList[T](row: jList[_], i: Int): List[Set[T]] = {
    val it  = row.get(i).asInstanceOf[PersistentVector].iterator
    var set = Set.empty[T]
    while (it.hasNext)
      set += it.next.asInstanceOf[T]
    List(set)
  }

  // ------------------------------------

  protected def castAggrOneListDistinctInt(row: jList[_], i: Int): List[Int] = {
    val it   = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var list = List.empty[Int]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[jLong].toInt
    list
  }

  protected def castAggrOneListDistinctFloat(row: jList[_], i: Int): List[Float] = {
    val it   = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var list = List.empty[Float]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[jDouble].toFloat
    list
  }

  protected def castAggrOneListDistinctBigInt(row: jList[_], i: Int): List[BigInt] = {
    val it   = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var list = List.empty[BigInt]
    while (it.hasNext)
      list = list :+ BigInt(it.next.toString)
    list
  }

  protected def castAggrOneListDistinctBigDecimal(row: jList[_], i: Int): List[BigDecimal] = {
    val it   = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var list = List.empty[BigDecimal]
    while (it.hasNext)
      list = list :+ BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    list
  }

  protected def castAggrOneListDistinct[T](row: jList[_], i: Int): List[T] = {
    val it   = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var list = List.empty[T]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[T]
    list
  }

  // card many

  protected def castAggrManyListDistinctInt(row: jList[_], i: Int): List[Set[Int]] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Int]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toInt
    List(set)
  }

  protected def castAggrManyListDistinctFloat(row: jList[_], i: Int): List[Set[Float]] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[Float]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toFloat
    List(set)
  }

  protected def castAggrManyListDistinctBigInt(row: jList[_], i: Int): List[Set[BigInt]] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[BigInt]
    while (it.hasNext)
      set += BigInt(it.next.toString)
    List(set)
  }

  protected def castAggrManyListDistinctBigDecimal(row: jList[_], i: Int): List[Set[BigDecimal]] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[BigDecimal]
    while (it.hasNext)
      set += BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    List(set)
  }

  protected def castAggrManyListDistinct[T](row: jList[_], i: Int): List[Set[T]] = {
    val it  = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var set = Set.empty[T]
    while (it.hasNext)
      set += it.next.asInstanceOf[T]
    List(set)
  }

  // ------------------------------------

  protected def castAggrOneListRandInt(row: jList[_], i: Int): List[Int] = {
    val it   = row.get(i).asInstanceOf[LazySeq].iterator
    var list = List.empty[Int]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[jLong].toInt
    list
  }

  protected def castAggrOneListRandFloat(row: jList[_], i: Int): List[Float] = {
    val it   = row.get(i).asInstanceOf[LazySeq].iterator
    var list = List.empty[Float]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[jDouble].toFloat
    list
  }

  protected def castAggrOneListRandBigInt(row: jList[_], i: Int): List[BigInt] = {
    val it   = row.get(i).asInstanceOf[LazySeq].iterator
    var list = List.empty[BigInt]
    while (it.hasNext)
      list = list :+ BigInt(it.next.toString)
    list
  }

  protected def castAggrOneListRandBigDecimal(row: jList[_], i: Int): List[BigDecimal] = {
    val it   = row.get(i).asInstanceOf[LazySeq].iterator
    var list = List.empty[BigDecimal]
    while (it.hasNext)
      list = list :+ BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    list
  }

  protected def castAggrOneListRand[T](row: jList[_], i: Int): List[T] = {
    val it   = row.get(i).asInstanceOf[LazySeq].iterator
    var list = List.empty[T]
    while (it.hasNext)
      list = list :+ it.next.asInstanceOf[T]
    list
  }

  // card many

  protected def castAggrManyListRandInt(row: jList[_], i: Int): List[Set[Int]] = {
    val it  = row.get(i).asInstanceOf[LazySeq].iterator
    var set = Set.empty[Int]
    while (it.hasNext)
      set += it.next.asInstanceOf[jLong].toInt
    List(set)
  }

  protected def castAggrManyListRandFloat(row: jList[_], i: Int): List[Set[Float]] = {
    val it  = row.get(i).asInstanceOf[LazySeq].iterator
    var set = Set.empty[Float]
    while (it.hasNext)
      set += it.next.asInstanceOf[jDouble].toFloat
    List(set)
  }

  protected def castAggrManyListRandBigInt(row: jList[_], i: Int): List[Set[BigInt]] = {
    val it  = row.get(i).asInstanceOf[LazySeq].iterator
    var set = Set.empty[BigInt]
    while (it.hasNext)
      set += BigInt(it.next.toString)
    List(set)
  }

  protected def castAggrManyListRandBigDecimal(row: jList[_], i: Int): List[Set[BigDecimal]] = {
    val it  = row.get(i).asInstanceOf[LazySeq].iterator
    var set = Set.empty[BigDecimal]
    while (it.hasNext)
      set += BigDecimal(it.next.asInstanceOf[java.math.BigDecimal].toString)
    List(set)
  }

  protected def castAggrManyListRand[T](row: jList[_], i: Int): List[Set[T]] = {
    val it  = row.get(i).asInstanceOf[LazySeq].iterator
    var set = Set.empty[T]
    while (it.hasNext)
      set += it.next.asInstanceOf[T]
    List(set)
  }

  // ------------------------------------

  protected def castAggrSingleSampleInt(row: jList[_], i: Int): Int =
    row.get(i).asInstanceOf[PersistentVector].iterator.next.asInstanceOf[jLong].toInt

  protected def castAggrSingleSampleFloat(row: jList[_], i: Int): Float =
    row.get(i).asInstanceOf[PersistentVector].iterator.next.asInstanceOf[jDouble].toFloat

  protected def castAggrSingleSampleBigInt(row: jList[_], i: Int): BigInt =
    BigInt(row.get(i).asInstanceOf[PersistentVector].iterator.next.toString)

  protected def castAggrSingleSampleBigDecimal(row: jList[_], i: Int): BigDecimal =
    BigDecimal(row.get(i).asInstanceOf[PersistentVector].iterator.next.asInstanceOf[java.math.BigDecimal].toString)

  protected def castAggrSingleSample[T](row: jList[_], i: Int): T =
    row.get(i).asInstanceOf[PersistentVector].iterator.next.asInstanceOf[T]

  // ------------------------------------

  protected def castAggrManySingleInt(row: jList[_], i: Int): Set[Int] =
    Set(row.get(i).asInstanceOf[jLong].toInt)

  protected def castAggrManySingleFloat(row: jList[_], i: Int): Set[Float] =
    Set(row.get(i).asInstanceOf[jDouble].toFloat)

  protected def castAggrManySingleBigInt(row: jList[_], i: Int): Set[BigInt] =
    Set(BigInt(row.get(i).toString))

  protected def castAggrManySingleBigDecimal(row: jList[_], i: Int): Set[BigDecimal] =
    Set(BigDecimal(row.get(i).asInstanceOf[java.math.BigDecimal].toString))

  protected def castAggrManySingle[T](row: jList[_], i: Int): Set[T] =
    Set(row.get(i).asInstanceOf[T])


}
