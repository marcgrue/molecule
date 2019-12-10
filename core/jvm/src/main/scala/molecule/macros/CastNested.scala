package molecule.macros
import molecule.ops.TreeOps
import scala.reflect.macros.blackbox

private[molecule] trait CastNested extends TreeOps {
  val c: blackbox.Context
  import c.universe._


  def castNestedOneAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castNestedOne[String](it)"
    case "Int"            => (i: Int) => q"castNestedOneInt(it)"
    case "Int2"           => (i: Int) => q"castNestedOneInt2(it)"
    case "Float"          => (i: Int) => q"castNestedOneFloat(it)"
    case "Boolean"        => (i: Int) => q"castNestedOne[Boolean](it)"
    case "Long"           => (i: Int) => q"castNestedOne[Long](it)"
    case "Double"         => (i: Int) => q"castNestedOne[Double](it)"
    case "java.util.Date" => (i: Int) => q"castNestedOne[java.util.Date](it)"
    case "java.util.UUID" => (i: Int) => q"castNestedOne[java.util.UUID](it)"
    case "java.net.URI"   => (i: Int) => q"castNestedOne[java.net.URI](it)"
    case "BigInt"         => (i: Int) => q"castNestedOneBigInt(it)"
    case "BigDecimal"     => (i: Int) => q"castNestedOneBigDecimal(it)"
    case "Any"            => (i: Int) => q"row.get($i)"
  }

  def castNestedManyAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castNestedMany[String](it)"
    case "Int"            => (i: Int) => q"castNestedManyInt(it)"
    case "Float"          => (i: Int) => q"castNestedManyFloat(it)"
    case "Boolean"        => (i: Int) => q"castNestedMany[Boolean](it)"
    case "Long"           => (i: Int) => q"castNestedMany[Long](it)"
    case "Double"         => (i: Int) => q"castNestedMany[Double](it)"
    case "java.util.Date" => (i: Int) => q"castNestedMany[java.util.Date](it)"
    case "java.util.UUID" => (i: Int) => q"castNestedMany[java.util.UUID](it)"
    case "java.net.URI"   => (i: Int) => q"castNestedMany[java.net.URI](it)"
    case "BigInt"         => (i: Int) => q"castNestedManyBigInt(it)"
    case "BigDecimal"     => (i: Int) => q"castNestedManyBigDecimal(it)"
  }

  val castNestedMandatoryAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) castNestedOneAttr(t.tpeS) else castNestedManyAttr(t.tpeS)


  val castNestedOptionalAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) {
      // Optional, card one
      t.tpeS match {
        case "String"         => (i: Int) => q"castNestedOptOne[String](it)"
        case "Int"            => (i: Int) => q"castNestedOptOneInt(it)"
        case "Float"          => (i: Int) => q"castNestedOptOneFloat(it)"
        case "Boolean"        => (i: Int) => q"castNestedOptOne[Boolean](it)"
        case "Long"           => (i: Int) => q"castNestedOptOneLong(it)"
        case "Double"         => (i: Int) => q"castNestedOptOneDouble(it)"
        case "java.util.Date" => (i: Int) => q"castNestedOptOne[java.util.Date](it)"
        case "java.util.UUID" => (i: Int) => q"castNestedOptOne[java.util.UUID](it)"
        case "java.net.URI"   => (i: Int) => q"castNestedOptOne[java.net.URI](it)"
        case "BigInt"         => (i: Int) => q"castNestedOptOneBigInt(it)"
        case "BigDecimal"     => (i: Int) => q"castNestedOptOneBigDecimal(it)"
      }
    } else {
      // Optional, card many
      t.tpeS match {
        case "String"         => (i: Int) => q"castNestedOptMany[String](it)"
        case "Int"            => (i: Int) => q"castNestedOptManyInt(it)"
        case "Float"          => (i: Int) => q"castNestedOptManyFloat(it)"
        case "Boolean"        => (i: Int) => q"castNestedOptMany[Boolean](it)"
        case "Long"           => (i: Int) => q"castNestedOptManyLong(it)"
        case "Double"         => (i: Int) => q"castNestedOptManyDouble(it)"
        case "java.util.Date" => (i: Int) => q"castNestedOptMany[java.util.Date](it)"
        case "java.util.UUID" => (i: Int) => q"castNestedOptMany[java.util.UUID](it)"
        case "java.net.URI"   => (i: Int) => q"castNestedOptMany[java.net.URI](it)"
        case "BigInt"         => (i: Int) => q"castNestedOptManyBigInt(it)"
        case "BigDecimal"     => (i: Int) => q"castNestedOptManyBigDecimal(it)"
      }
    }



//  val castOptionalRefAttr: richTree => Int => Tree = (t: richTree) => if (t.card == 1)
//    (i: Int) => q"castOptOneRefAttr(row, $i)"
//  else
//    (i: Int) => q"castOptManyRefAttr(row, $i)"
//
//  def castEnum(t: richTree): Int => Tree = if (t.card == 1)
//    (i: Int) => q"row.get($i).asInstanceOf[String]"
//  else
//    (i: Int) => q"castMany[String](row, $i)"
//
//  val castEnumOpt: richTree => Int => Tree = (t: richTree) => if (t.card == 1)
//    (i: Int) => q"castOptOneEnum(row, $i)"
//  else
//    (i: Int) => q"castOptManyEnum(row, $i)"
//
//  val castMandatoryMapAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
//    case "String"         => (i: Int) => q"castMapString(row, $i)"
//    case "Int"            => (i: Int) => q"castMapInt(row, $i)"
//    case "Float"          => (i: Int) => q"castMapFloat(row, $i)"
//    case "Boolean"        => (i: Int) => q"castMapBoolean(row, $i)"
//    case "Long"           => (i: Int) => q"castMapLong(row, $i)"
//    case "Double"         => (i: Int) => q"castMapDouble(row, $i)"
//    case "java.util.Date" => (i: Int) => q"castMapDate(row, $i)"
//    case "java.util.UUID" => (i: Int) => q"castMapUUID(row, $i)"
//    case "java.net.URI"   => (i: Int) => q"castMapURI(row, $i)"
//    case "BigInt"         => (i: Int) => q"castMapBigInt(row, $i)"
//    case "BigDecimal"     => (i: Int) => q"castMapBigDecimal(row, $i)"
//  }
//
//  val castOptionalMapAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
//    case "String"         => (i: Int) => q"castOptMapString(row, $i)"
//    case "Int"            => (i: Int) => q"castOptMapInt(row, $i)"
//    case "Float"          => (i: Int) => q"castOptMapFloat(row, $i)"
//    case "Boolean"        => (i: Int) => q"castOptMapBoolean(row, $i)"
//    case "Long"           => (i: Int) => q"castOptMapLong(row, $i)"
//    case "Double"         => (i: Int) => q"castOptMapDouble(row, $i)"
//    case "java.util.Date" => (i: Int) => q"castOptMapDate(row, $i)"
//    case "java.util.UUID" => (i: Int) => q"castOptMapUUID(row, $i)"
//    case "java.net.URI"   => (i: Int) => q"castOptMapURI(row, $i)"
//    case "BigInt"         => (i: Int) => q"castOptMapBigInt(row, $i)"
//    case "BigDecimal"     => (i: Int) => q"castOptMapBigDecimal(row, $i)"
//  }
//
//  val castOptionalMapApplyAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
//    case "String"         => (i: Int) => q"castOptMapApplyString(row, $i)"
//    case "Int"            => (i: Int) => q"castOptMapApplyInt(row, $i)"
//    case "Float"          => (i: Int) => q"castOptMapApplyFloat(row, $i)"
//    case "Boolean"        => (i: Int) => q"castOptMapApplyBoolean(row, $i)"
//    case "Long"           => (i: Int) => q"castOptMapApplyLong(row, $i)"
//    case "Double"         => (i: Int) => q"castOptMapApplyDouble(row, $i)"
//    case "java.util.Date" => (i: Int) => q"castOptMapApplyDate(row, $i)"
//    case "java.util.UUID" => (i: Int) => q"castOptMapApplyUUID(row, $i)"
//    case "java.net.URI"   => (i: Int) => q"castOptMapApplyURI(row, $i)"
//    case "BigInt"         => (i: Int) => q"castOptMapApplyBigInt(row, $i)"
//    case "BigDecimal"     => (i: Int) => q"castOptMapApplyBigDecimal(row, $i)"
//  }
//
//  def castKeyedMapAttr(tpe: String): Int => Tree = tpe match {
//    case "String"         => (i: Int) => q"row.get($i).toString"
//    case "Int"            => (i: Int) => q"row.get($i).toString.toInt"
//    case "Long"           => (i: Int) => q"row.get($i).toString.toLong"
//    case "Float"          => (i: Int) => q"row.get($i).toString.toFloat"
//    case "Double"         => (i: Int) => q"row.get($i).toString.toDouble"
//    case "Boolean"        => (i: Int) => q"row.get($i).toString.toBoolean"
//    case "java.util.Date" => (i: Int) => q"molecule.util.fns.str2date(row.get($i).toString)"
//    case "java.util.UUID" => (i: Int) => q"java.util.UUID.fromString(row.get($i).toString)"
//    case "java.net.URI"   => (i: Int) => q"new java.net.URI(row.get($i).toString)"
//    case "BigInt"         => (i: Int) => q"BigInt(row.get($i).toString)"
//    case "BigDecimal"     => (i: Int) => q"BigDecimal(row.get($i).toString)"
//    case "Any"            => (i: Int) => q"row.get($i)"
//  }
}
