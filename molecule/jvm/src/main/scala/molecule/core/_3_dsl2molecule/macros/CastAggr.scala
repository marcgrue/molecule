package molecule.core._3_dsl2molecule.macros

import molecule.core._3_dsl2molecule.ops.TreeOps
import scala.reflect.macros.blackbox

private[molecule] trait CastAggr extends CastOptNested with TreeOps {
  val c: blackbox.Context
  import c.universe._


  def castAggrInt: Int => Tree = (i: Int) => q"row.get($i).toString.toInt"
  def castAggrDouble: Int => Tree = (i: Int) => q"row.get($i).asInstanceOf[Double]"

  def castAggrOneList(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrOneListInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrOneList[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrOneListFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrOneList[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrOneList[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrOneListBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrOneListBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrOneList[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrOneList[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrOneListURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrOneList[java.util.UUID](row, $i)"
  }
  def castAggrManyList(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrManyListInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrManyList[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrManyListFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrManyList[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrManyList[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrManyListBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrManyListBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrManyList[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrManyList[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrManyListURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrManyList[java.util.UUID](row, $i)"
  }

  def castAggrOneListDistinct(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrOneListDistinctInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrOneListDistinct[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrOneListDistinctFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrOneListDistinct[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrOneListDistinct[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrOneListDistinctBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrOneListDistinctBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrOneListDistinct[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrOneListDistinct[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrOneListDistinctURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrOneListDistinct[java.util.UUID](row, $i)"
  }
  def castAggrManyListDistinct(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrManyListDistinctInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrManyListDistinct[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrManyListDistinctFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrManyListDistinct[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrManyListDistinct[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrManyListDistinctBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrManyListDistinctBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrManyListDistinct[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrManyListDistinct[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrManyListDistinctURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrManyListDistinct[java.util.UUID](row, $i)"
  }

  def castAggrOneListRand(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrOneListRandInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrOneListRand[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrOneListRandFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrOneListRand[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrOneListRand[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrOneListRandBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrOneListRandBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrOneListRand[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrOneListRand[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrOneListRandURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrOneListRand[java.util.UUID](row, $i)"
  }
  def castAggrManyListRand(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrManyListRandInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrManyListRand[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrManyListRandFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrManyListRand[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrManyListRand[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrManyListRandBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrManyListRandBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrManyListRand[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrManyListRand[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrManyListRandURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrManyListRand[java.util.UUID](row, $i)"
  }


  def castAggrSingleSample(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrSingleSampleInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrSingleSample[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrSingleSampleFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrSingleSample[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrSingleSample[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrSingleSampleBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrSingleSampleBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrSingleSample[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrSingleSample[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrSingleSampleURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrSingleSample[java.util.UUID](row, $i)"
  }

  def castAggrOneSingle(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castOneInt(row, $i)"
    case "Long"           => (i: Int) => q"castOne[Long](row, $i)"
    case "Float"          => (i: Int) => q"castOneFloat(row, $i)"
    case "Double"         => (i: Int) => q"castOne[Double](row, $i)"
    case "String"         => (i: Int) => q"castOne[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castOneBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castOneBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castOne[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castOne[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castOneURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castOne[java.util.UUID](row, $i)"
  }
  def castAggrManySingle(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrManySingleInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrManySingle[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrManySingleFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrManySingle[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrManySingle[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrManySingleBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrManySingleBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrManySingle[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrManySingle[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrManySingleURI(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrManySingle[java.util.UUID](row, $i)"
  }
}
