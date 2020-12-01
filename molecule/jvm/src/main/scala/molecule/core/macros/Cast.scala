package molecule.core.macros

import molecule.core.ops.TreeOps
import scala.reflect.macros.blackbox

private[molecule] trait Cast extends CastAggr with TreeOps {
  val c: blackbox.Context

  import c.universe._

  val y = DebugMacro("Cast", 1)


  def castOneAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castOne[String](row, $i)"
    case "Int"            => (i: Int) => q"castOneInt(row, $i)"
    case "Int2"           => (i: Int) => q"castOneInt2(row, $i)"
    case "Float"          => (i: Int) => q"castOneFloat(row, $i)"
    case "Boolean"        => (i: Int) => q"castOne[Boolean](row, $i)"
    case "Long"           => (i: Int) => q"castOne[Long](row, $i)"
    case "Double"         => (i: Int) => q"castOne[Double](row, $i)"
    case "java.util.Date" => (i: Int) => q"castOne[java.util.Date](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castOne[java.util.UUID](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castOneURI(row, $i)"
    case "BigInt"         => (i: Int) => q"castOneBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castOneBigDecimal(row, $i)"
    case "Any"            => (i: Int) => q"row.get($i)"
  }

  def castManyAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castMany[String](row, $i)"
    case "Int"            => (i: Int) => q"castManyInt(row, $i)"
    case "Float"          => (i: Int) => q"castManyFloat(row, $i)"
    case "Boolean"        => (i: Int) => q"castMany[Boolean](row, $i)"
    case "Long"           => (i: Int) => q"castMany[Long](row, $i)"
    case "Double"         => (i: Int) => q"castMany[Double](row, $i)"
    case "java.util.Date" => (i: Int) => q"castMany[java.util.Date](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castMany[java.util.UUID](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castManyURI(row, $i)"
    case "BigInt"         => (i: Int) => q"castManyBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castManyBigDecimal(row, $i)"
  }

  val castMandatoryAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) castOneAttr(t.tpeS) else castManyAttr(t.tpeS)


  val castOptionalAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) {
      // Optional, card one
      t.tpeS match {
        case "String"         => (i: Int) => q"castOptOne[String](row, $i)"
        case "Int"            => (i: Int) => q"castOptOneInt(row, $i)"
        case "Float"          => (i: Int) => q"castOptOneFloat(row, $i)"
        case "Boolean"        => (i: Int) => q"castOptOne[Boolean](row, $i)"
        case "Long"           => (i: Int) => q"castOptOneLong(row, $i)"
        case "Double"         => (i: Int) => q"castOptOneDouble(row, $i)"
        case "java.util.Date" => (i: Int) => q"castOptOne[java.util.Date](row, $i)"
        case "java.util.UUID" => (i: Int) => q"castOptOne[java.util.UUID](row, $i)"
        case "java.net.URI"   => (i: Int) => q"castOptOneURI(row, $i)"
        case "BigInt"         => (i: Int) => q"castOptOneBigInt(row, $i)"
        case "BigDecimal"     => (i: Int) => q"castOptOneBigDecimal(row, $i)"
      }
    } else {
      // Optional, card many
      t.tpeS match {
        case "String"         => (i: Int) => q"castOptMany[String](row, $i)"
        case "Int"            => (i: Int) => q"castOptManyInt(row, $i)"
        case "Float"          => (i: Int) => q"castOptManyFloat(row, $i)"
        case "Boolean"        => (i: Int) => q"castOptMany[Boolean](row, $i)"
        case "Long"           => (i: Int) => q"castOptManyLong(row, $i)"
        case "Double"         => (i: Int) => q"castOptManyDouble(row, $i)"
        case "java.util.Date" => (i: Int) => q"castOptMany[java.util.Date](row, $i)"
        case "java.util.UUID" => (i: Int) => q"castOptMany[java.util.UUID](row, $i)"
        case "java.net.URI"   => (i: Int) => q"castOptManyURI(row, $i)"
        case "BigInt"         => (i: Int) => q"castOptManyBigInt(row, $i)"
        case "BigDecimal"     => (i: Int) => q"castOptManyBigDecimal(row, $i)"
      }
    }

  val castOptionalApplyAttr: richTree => Int => Tree = (t: richTree) => if (t.card == 1) {
    // Optional, card one
    t.tpeS match {
      case "String"         => (i: Int) => q"castOptOneApply[String](row, $i)"
      case "Int"            => (i: Int) => q"castOptOneApplyInt(row, $i)"
      case "Float"          => (i: Int) => q"castOptOneApplyFloat(row, $i)"
      case "Boolean"        => (i: Int) => q"castOptOneApply[Boolean](row, $i)"
      case "Long"           => (i: Int) => q"castOptOneApplyLong(row, $i)"
      case "Double"         => (i: Int) => q"castOptOneApplyDouble(row, $i)"
      case "java.util.Date" => (i: Int) => q"castOptOneApply[java.util.Date](row, $i)"
      case "java.util.UUID" => (i: Int) => q"castOptOneApply[java.util.UUID](row, $i)"
      case "java.net.URI"   => (i: Int) => q"castOptOneApplyURI(row, $i)"
      case "BigInt"         => (i: Int) => q"castOptOneApplyBigInt(row, $i)"
      case "BigDecimal"     => (i: Int) => q"castOptOneApplyBigDecimal(row, $i)"
    }
  } else {
    // Optional, card many
    t.tpeS match {
      case "String"         => (i: Int) => q"castOptManyApply[String](row, $i)"
      case "Int"            => (i: Int) => q"castOptManyApplyInt(row, $i)"
      case "Float"          => (i: Int) => q"castOptManyApplyFloat(row, $i)"
      case "Boolean"        => (i: Int) => q"castOptManyApply[Boolean](row, $i)"
      case "Long"           => (i: Int) => q"castOptManyApplyLong(row, $i)"
      case "Double"         => (i: Int) => q"castOptManyApplyDouble(row, $i)"
      case "java.util.Date" => (i: Int) => q"castOptManyApply[java.util.Date](row, $i)"
      case "java.util.UUID" => (i: Int) => q"castOptManyApply[java.util.UUID](row, $i)"
      case "java.net.URI"   => (i: Int) => q"castOptManyApplyURI(row, $i)"
      case "BigInt"         => (i: Int) => q"castOptManyApplyBigInt(row, $i)"
      case "BigDecimal"     => (i: Int) => q"castOptManyApplyBigDecimal(row, $i)"
    }
  }

  val castOptionalRefAttr: richTree => Int => Tree = (t: richTree) => if (t.card == 1)
    (i: Int) => q"castOptOneRefAttr(row, $i)"
  else
    (i: Int) => q"castOptManyRefAttr(row, $i)"

  val castEnum: richTree => Int => Tree = (t: richTree) => if (t.card == 1)
    (i: Int) => q"row.get($i).asInstanceOf[String]"
  else
    (i: Int) => q"castMany[String](row, $i)"

  val castEnumOpt: richTree => Int => Tree = (t: richTree) => if (t.card == 1)
    (i: Int) => q"castOptOneEnum(row, $i)"
  else
    (i: Int) => q"castOptManyEnum(row, $i)"

  val castMandatoryMapAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
    case "String"         => (i: Int) => q"castMapString(row, $i)"
    case "Int"            => (i: Int) => q"castMapInt(row, $i)"
    case "Float"          => (i: Int) => q"castMapFloat(row, $i)"
    case "Boolean"        => (i: Int) => q"castMapBoolean(row, $i)"
    case "Long"           => (i: Int) => q"castMapLong(row, $i)"
    case "Double"         => (i: Int) => q"castMapDouble(row, $i)"
    case "java.util.Date" => (i: Int) => q"castMapDate(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castMapUUID(row, $i)"
    case "java.net.URI"   => (i: Int) => q"castMapURI(row, $i)"
    case "BigInt"         => (i: Int) => q"castMapBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castMapBigDecimal(row, $i)"
  }

  val castOptionalMapAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
    case "String"         => (i: Int) => q"castOptMapString(row, $i)"
    case "Int"            => (i: Int) => q"castOptMapInt(row, $i)"
    case "Float"          => (i: Int) => q"castOptMapFloat(row, $i)"
    case "Boolean"        => (i: Int) => q"castOptMapBoolean(row, $i)"
    case "Long"           => (i: Int) => q"castOptMapLong(row, $i)"
    case "Double"         => (i: Int) => q"castOptMapDouble(row, $i)"
    case "java.util.Date" => (i: Int) => q"castOptMapDate(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castOptMapUUID(row, $i)"
    case "java.net.URI"   => (i: Int) => q"castOptMapURI(row, $i)"
    case "BigInt"         => (i: Int) => q"castOptMapBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castOptMapBigDecimal(row, $i)"
  }

  val castOptionalMapApplyAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
    case "String"         => (i: Int) => q"castOptMapApplyString(row, $i)"
    case "Int"            => (i: Int) => q"castOptMapApplyInt(row, $i)"
    case "Float"          => (i: Int) => q"castOptMapApplyFloat(row, $i)"
    case "Boolean"        => (i: Int) => q"castOptMapApplyBoolean(row, $i)"
    case "Long"           => (i: Int) => q"castOptMapApplyLong(row, $i)"
    case "Double"         => (i: Int) => q"castOptMapApplyDouble(row, $i)"
    case "java.util.Date" => (i: Int) => q"castOptMapApplyDate(row, $i)"
    case "java.util.UUID" => (i: Int) => q"castOptMapApplyUUID(row, $i)"
    case "java.net.URI"   => (i: Int) => q"castOptMapApplyURI(row, $i)"
    case "BigInt"         => (i: Int) => q"castOptMapApplyBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castOptMapApplyBigDecimal(row, $i)"
  }

  def castKeyedMapAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"row.get($i).toString"
    case "Int"            => (i: Int) => q"row.get($i).toString.toInt"
    case "Long"           => (i: Int) => q"row.get($i).toString.toLong"
    case "Float"          => (i: Int) => q"row.get($i).toString.toFloat"
    case "Double"         => (i: Int) => q"row.get($i).toString.toDouble"
    case "Boolean"        => (i: Int) => q"row.get($i).toString.toBoolean"
    case "java.util.Date" => (i: Int) => q"molecule.core.util.fns.str2date(row.get($i).toString)"
    case "java.util.UUID" => (i: Int) => q"java.util.UUID.fromString(row.get($i).toString)"
    case "java.net.URI"   => (i: Int) => q"new java.net.URI(row.get($i).toString)"
    case "BigInt"         => (i: Int) => q"BigInt(row.get($i).toString)"
    case "BigDecimal"     => (i: Int) => q"BigDecimal(row.get($i).toString)"
    case "Any"            => (i: Int) => q"row.get($i)"
  }
}
