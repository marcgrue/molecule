package molecule.core.macros

import molecule.core.ops.TreeOps
import scala.reflect.macros.blackbox

private[molecule] trait CastAggr extends CastOptNested {
  val c: blackbox.Context

  import c.universe._


  val castAggrInt   : Int => Tree = (i: Int) => q"row.get($i).toString.toInt"
  val castAggrDouble: Int => Tree = (i: Int) => q"row.get($i).asInstanceOf[Double]"

  val castAggrOneList : String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrOneListInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrOneList[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrOneListFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrOneList[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrOneList[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrOneListBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrOneListBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrOneList[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrOneList[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrOneListURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrOneList[UUID](row, $i)"
  }
  val castAggrManyList: String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrManyListInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrManyList[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrManyListFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrManyList[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrManyList[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrManyListBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrManyListBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrManyList[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrManyList[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrManyListURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrManyList[UUID](row, $i)"
  }

  val castAggrOneListDistinct : String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrOneListDistinctInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrOneListDistinct[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrOneListDistinctFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrOneListDistinct[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrOneListDistinct[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrOneListDistinctBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrOneListDistinctBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrOneListDistinct[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrOneListDistinct[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrOneListDistinctURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrOneListDistinct[UUID](row, $i)"
  }
  val castAggrManyListDistinct: String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrManyListDistinctInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrManyListDistinct[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrManyListDistinctFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrManyListDistinct[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrManyListDistinct[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrManyListDistinctBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrManyListDistinctBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrManyListDistinct[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrManyListDistinct[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrManyListDistinctURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrManyListDistinct[UUID](row, $i)"
  }

  val castAggrOneListRand : String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrOneListRandInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrOneListRand[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrOneListRandFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrOneListRand[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrOneListRand[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrOneListRandBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrOneListRandBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrOneListRand[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrOneListRand[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrOneListRandURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrOneListRand[UUID](row, $i)"
  }
  val castAggrManyListRand: String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrManyListRandInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrManyListRand[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrManyListRandFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrManyListRand[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrManyListRand[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrManyListRandBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrManyListRandBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrManyListRand[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrManyListRand[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrManyListRandURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrManyListRand[UUID](row, $i)"
  }


  val castAggrSingleSample: String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrSingleSampleInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrSingleSample[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrSingleSampleFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrSingleSample[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrSingleSample[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrSingleSampleBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrSingleSampleBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrSingleSample[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrSingleSample[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrSingleSampleURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrSingleSample[UUID](row, $i)"
  }

  val castAggrOneSingle : String => Int => Tree = {
    case "Int"        => (i: Int) => q"castOneInt(row, $i)"
    case "Long"       => (i: Int) => q"castOne[Long](row, $i)"
    case "Float"      => (i: Int) => q"castOneFloat(row, $i)"
    case "Double"     => (i: Int) => q"castOne[Double](row, $i)"
    case "String"     => (i: Int) => q"castOne[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castOneBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castOneBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castOne[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castOne[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castOneURI(row, $i)"
    case "UUID"       => (i: Int) => q"castOne[UUID](row, $i)"
  }
  val castAggrManySingle: String => Int => Tree = {
    case "Int"        => (i: Int) => q"castAggrManySingleInt(row, $i)"
    case "Long"       => (i: Int) => q"castAggrManySingle[Long](row, $i)"
    case "Float"      => (i: Int) => q"castAggrManySingleFloat(row, $i)"
    case "Double"     => (i: Int) => q"castAggrManySingle[Double](row, $i)"
    case "String"     => (i: Int) => q"castAggrManySingle[String](row, $i)"
    case "BigInt"     => (i: Int) => q"castAggrManySingleBigInt(row, $i)"
    case "BigDecimal" => (i: Int) => q"castAggrManySingleBigDecimal(row, $i)"
    case "Date"       => (i: Int) => q"castAggrManySingle[Date](row, $i)"
    case "Boolean"    => (i: Int) => q"castAggrManySingle[Boolean](row, $i)"
    case "URI"        => (i: Int) => q"castAggrManySingleURI(row, $i)"
    case "UUID"       => (i: Int) => q"castAggrManySingle[UUID](row, $i)"
  }
}
