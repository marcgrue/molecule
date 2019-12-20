package molecule.macros
import molecule.ops.TreeOps
import scala.reflect.macros.blackbox

private[molecule] trait CastOptNested extends TreeOps {
  val c: blackbox.Context
  import c.universe._


  def castOptNestedOneAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castOptNestedOne[String](${TermName("it" + i)})"
    case "Int"            => (i: Int) => q"castOptNestedOneInt(${TermName("it" + i)})"
    case "Int2"           => (i: Int) => q"castOptNestedOneInt2(${TermName("it" + i)})"
    case "Float"          => (i: Int) => q"castOptNestedOneFloat(${TermName("it" + i)})"
    case "Boolean"        => (i: Int) => q"castOptNestedOne[Boolean](${TermName("it" + i)})"
    case "Long"           => (i: Int) => q"castOptNestedOne[Long](${TermName("it" + i)})"
    case "Double"         => (i: Int) => q"castOptNestedOne[Double](${TermName("it" + i)})"
    case "java.util.Date" => (i: Int) => q"castOptNestedOne[java.util.Date](${TermName("it" + i)})"
    case "java.util.UUID" => (i: Int) => q"castOptNestedOne[java.util.UUID](${TermName("it" + i)})"
    case "java.net.URI"   => (i: Int) => q"castOptNestedOne[java.net.URI](${TermName("it" + i)})"
    case "BigInt"         => (i: Int) => q"castOptNestedOneBigInt(${TermName("it" + i)})"
    case "BigDecimal"     => (i: Int) => q"castOptNestedOneBigDecimal(${TermName("it" + i)})"
    case "Any"            => (i: Int) => q"row.get($i)"
  }

  def castOptNestedManyAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castOptNestedMany[String](${TermName("it" + i)})"
    case "Int"            => (i: Int) => q"castOptNestedManyInt(${TermName("it" + i)})"
    case "Float"          => (i: Int) => q"castOptNestedManyFloat(${TermName("it" + i)})"
    case "Boolean"        => (i: Int) => q"castOptNestedMany[Boolean](${TermName("it" + i)})"
    case "Long"           => (i: Int) => q"castOptNestedMany[Long](${TermName("it" + i)})"
    case "Double"         => (i: Int) => q"castOptNestedMany[Double](${TermName("it" + i)})"
    case "java.util.Date" => (i: Int) => q"castOptNestedMany[java.util.Date](${TermName("it" + i)})"
    case "java.util.UUID" => (i: Int) => q"castOptNestedMany[java.util.UUID](${TermName("it" + i)})"
    case "java.net.URI"   => (i: Int) => q"castOptNestedMany[java.net.URI](${TermName("it" + i)})"
    case "BigInt"         => (i: Int) => q"castOptNestedManyBigInt(${TermName("it" + i)})"
    case "BigDecimal"     => (i: Int) => q"castOptNestedManyBigDecimal(${TermName("it" + i)})"
  }

  val castOptNestedMandatoryAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) castOptNestedOneAttr(t.tpeS) else castOptNestedManyAttr(t.tpeS)

  val castOptNestedMandatoryRefAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1)
      (i: Int) => q"castOptNestedOneRefAttr(${TermName("it" + i)})"
    else
      (i: Int) => q"castOptNestedManyRefAttr(${TermName("it" + i)})"


  val castOptNestedOptionalAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) {
      // Optional, card one
      t.tpeS match {
        case "String"         => (i: Int) => q"castOptNestedOptOne[String](${TermName("it" + i)})"
        case "Int"            => (i: Int) => q"castOptNestedOptOneInt(${TermName("it" + i)})"
        case "Float"          => (i: Int) => q"castOptNestedOptOneFloat(${TermName("it" + i)})"
        case "Boolean"        => (i: Int) => q"castOptNestedOptOne[Boolean](${TermName("it" + i)})"
        case "Long"           => (i: Int) => q"castOptNestedOptOneLong(${TermName("it" + i)})"
        case "Double"         => (i: Int) => q"castOptNestedOptOneDouble(${TermName("it" + i)})"
        case "java.util.Date" => (i: Int) => q"castOptNestedOptOne[java.util.Date](${TermName("it" + i)})"
        case "java.util.UUID" => (i: Int) => q"castOptNestedOptOne[java.util.UUID](${TermName("it" + i)})"
        case "java.net.URI"   => (i: Int) => q"castOptNestedOptOne[java.net.URI](${TermName("it" + i)})"
        case "BigInt"         => (i: Int) => q"castOptNestedOptOneBigInt(${TermName("it" + i)})"
        case "BigDecimal"     => (i: Int) => q"castOptNestedOptOneBigDecimal(${TermName("it" + i)})"
      }
    } else {
      // Optional, card many
      t.tpeS match {
        case "String"         => (i: Int) => q"castOptNestedOptMany[String](${TermName("it" + i)})"
        case "Int"            => (i: Int) => q"castOptNestedOptManyInt(${TermName("it" + i)})"
        case "Float"          => (i: Int) => q"castOptNestedOptManyFloat(${TermName("it" + i)})"
        case "Boolean"        => (i: Int) => q"castOptNestedOptMany[Boolean](${TermName("it" + i)})"
        case "Long"           => (i: Int) => q"castOptNestedOptManyLong(${TermName("it" + i)})"
        case "Double"         => (i: Int) => q"castOptNestedOptManyDouble(${TermName("it" + i)})"
        case "java.util.Date" => (i: Int) => q"castOptNestedOptMany[java.util.Date](${TermName("it" + i)})"
        case "java.util.UUID" => (i: Int) => q"castOptNestedOptMany[java.util.UUID](${TermName("it" + i)})"
        case "java.net.URI"   => (i: Int) => q"castOptNestedOptMany[java.net.URI](${TermName("it" + i)})"
        case "BigInt"         => (i: Int) => q"castOptNestedOptManyBigInt(${TermName("it" + i)})"
        case "BigDecimal"     => (i: Int) => q"castOptNestedOptManyBigDecimal(${TermName("it" + i)})"
      }
    }


  val castOptNestedOptionalRefAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1)
      (i: Int) => q"castOptNestedOptOneRefAttr(${TermName("it" + i)})"
    else
      (i: Int) => q"castOptNestedOptManyRefAttr(${TermName("it" + i)})"

  val castOptNestedEnum: richTree => Int => Tree =
    (t: richTree) =>
      if (t.card == 1) {
        (i: Int) => q"castOptNestedOneEnum(${TermName("it" + i)})"
      } else
        (i: Int) => q"castOptNestedManyEnum(${TermName("it" + i)})"


  val castOptNestedEnumOpt: richTree => Int => Tree = (t: richTree) => {
    if (t.card == 1)
      (i: Int) => q"castOptNestedOptOneEnum(${TermName("it" + i)})"
    else
      (i: Int) => q"castOptNestedOptManyEnum(${TermName("it" + i)})"
  }

    val castOptNestedMandatoryMapAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
      case "String"         => (i: Int) => q"castOptNestedMapString(${TermName("it" + i)})"
      case "Int"            => (i: Int) => q"castOptNestedMapInt(${TermName("it" + i)})"
      case "Float"          => (i: Int) => q"castOptNestedMapFloat(${TermName("it" + i)})"
      case "Boolean"        => (i: Int) => q"castOptNestedMapBoolean(${TermName("it" + i)})"
      case "Long"           => (i: Int) => q"castOptNestedMapLong(${TermName("it" + i)})"
      case "Double"         => (i: Int) => q"castOptNestedMapDouble(${TermName("it" + i)})"
      case "java.util.Date" => (i: Int) => q"castOptNestedMapDate(${TermName("it" + i)})"
      case "java.util.UUID" => (i: Int) => q"castOptNestedMapUUID(${TermName("it" + i)})"
      case "java.net.URI"   => (i: Int) => q"castOptNestedMapURI(${TermName("it" + i)})"
      case "BigInt"         => (i: Int) => q"castOptNestedMapBigInt(${TermName("it" + i)})"
      case "BigDecimal"     => (i: Int) => q"castOptNestedMapBigDecimal(${TermName("it" + i)})"
    }

    val castOptNestedOptionalMapAttr: richTree => Int => Tree = (t: richTree) => t.tpeS match {
      case "String"         => (i: Int) => q"castOptNestedOptMapString(${TermName("it" + i)})"
      case "Int"            => (i: Int) => q"castOptNestedOptMapInt(${TermName("it" + i)})"
      case "Float"          => (i: Int) => q"castOptNestedOptMapFloat(${TermName("it" + i)})"
      case "Boolean"        => (i: Int) => q"castOptNestedOptMapBoolean(${TermName("it" + i)})"
      case "Long"           => (i: Int) => q"castOptNestedOptMapLong(${TermName("it" + i)})"
      case "Double"         => (i: Int) => q"castOptNestedOptMapDouble(${TermName("it" + i)})"
      case "java.util.Date" => (i: Int) => q"castOptNestedOptMapDate(${TermName("it" + i)})"
      case "java.util.UUID" => (i: Int) => q"castOptNestedOptMapUUID(${TermName("it" + i)})"
      case "java.net.URI"   => (i: Int) => q"castOptNestedOptMapURI(${TermName("it" + i)})"
      case "BigInt"         => (i: Int) => q"castOptNestedOptMapBigInt(${TermName("it" + i)})"
      case "BigDecimal"     => (i: Int) => q"castOptNestedOptMapBigDecimal(${TermName("it" + i)})"
    }
}
