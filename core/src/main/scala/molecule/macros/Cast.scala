package molecule.macros
import molecule.ops.TreeOps
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox


private[molecule] trait Cast extends TreeOps {
  val c: blackbox.Context
  import c.universe._

  val y = DebugMacro("Cast", 1)


  def castOneAttr(tpe: String): Int => Tree = tpe match {
    case "String"         => (i: Int) => q"castOne[String](row, $i)"
    case "Int"            => (i: Int) => q"castOneInt(row, $i)"
    case "Float"          => (i: Int) => q"castOneFloat(row, $i)"
    case "Boolean"        => (i: Int) => q"castOne[Boolean](row, $i)"
    case "Long"           => (i: Int) => q"castOne[Long](row, $i)"
    case "Double"         => (i: Int) => q"castOne[Double](row, $i)"
    case "java.util.Date" => (i: Int) => q"castOne[java.util.Date](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castOne[java.util.UUID](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castOne[java.net.URI](row, $i)"
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
    case "java.net.URI"   => (i: Int) => q"castMany[java.net.URI](row, $i)"
    case "BigInt"         => (i: Int) => q"castManyBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castManyBigDecimal(row, $i)"
  }

  val castMandatoryAttr: richTree => Int => Tree = (t: richTree) =>
    if (t.card == 1) castOneAttr(t.tpeS) else castManyAttr(t.tpeS)


  def castAggrListVector(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrListVectorInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrListVector[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrListVectorFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrListVector[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrListVector[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrListVectorBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrListVectorBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrListVector[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrListVector[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrListVector[java.net.URI](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrListVector[java.util.UUID](row, $i)"
  }
  def castAggrListHashSet(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrListHashSetInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrListHashSet[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrListHashSetFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrListHashSet[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrListHashSet[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrListHashSetBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrListHashSetBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrListHashSet[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrListHashSet[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrListHashSet[java.net.URI](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrListHashSet[java.util.UUID](row, $i)"
  }
  def castAggrListLazySeq(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrListLazySeqInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrListLazySeq[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrListLazySeqFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrListLazySeq[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrListLazySeq[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrListLazySeqBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrListLazySeqBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrListLazySeq[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrListLazySeq[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrListLazySeq[java.net.URI](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrListLazySeq[java.util.UUID](row, $i)"
  }

  def castAggrInt: Int => Tree = (i: Int) => q"row.get($i).asInstanceOf[Int]"
  def castAggrDouble: Int => Tree = (i: Int) => q"row.get($i).asInstanceOf[Double]"

  def castAggr(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castOneInt(row, $i)"
    case "Long"           => (i: Int) => q"castOne[Long](row, $i)"
    case "Float"          => (i: Int) => q"castOneFloat(row, $i)"
    case "Double"         => (i: Int) => q"castOne[Double](row, $i)"
    case "String"         => (i: Int) => q"castOne[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castOneBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castOneBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castOne[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castOne[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castOne[java.net.URI](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castOne[java.util.UUID](row, $i)"
  }

  def castAggrVector(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrVectorInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrVector[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrVectorFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrVector[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrVector[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrVectorBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrVectorBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrVector[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrVector[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrVector[java.net.URI](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrVector[java.util.UUID](row, $i)"
  }
  def castAggrLazySeq(tpe: String): Int => Tree = tpe match {
    case "Int"            => (i: Int) => q"castAggrLazySeqInt(row, $i)"
    case "Long"           => (i: Int) => q"castAggrLazySeq[Long](row, $i)"
    case "Float"          => (i: Int) => q"castAggrLazySeqFloat(row, $i)"
    case "Double"         => (i: Int) => q"castAggrLazySeq[Double](row, $i)"
    case "String"         => (i: Int) => q"castAggrLazySeq[String](row, $i)"
    case "BigInt"         => (i: Int) => q"castAggrLazySeqBigInt(row, $i)"
    case "BigDecimal"     => (i: Int) => q"castAggrLazySeqBigDecimal(row, $i)"
    case "java.util.Date" => (i: Int) => q"castAggrLazySeq[java.util.Date](row, $i)"
    case "Boolean"        => (i: Int) => q"castAggrLazySeq[Boolean](row, $i)"
    case "java.net.URI"   => (i: Int) => q"castAggrLazySeq[java.net.URI](row, $i)"
    case "java.util.UUID" => (i: Int) => q"castAggrLazySeq[java.util.UUID](row, $i)"
  }

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
        case "java.net.URI"   => (i: Int) => q"castOptOne[java.net.URI](row, $i)"
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
        case "java.net.URI"   => (i: Int) => q"castOptMany[java.net.URI](row, $i)"
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
      case "java.net.URI"   => (i: Int) => q"castOptOneApply[java.net.URI](row, $i)"
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
      case "java.net.URI"   => (i: Int) => q"castOptManyApply[java.net.URI](row, $i)"
      case "BigInt"         => (i: Int) => q"castOptManyApplyBigInt(row, $i)"
      case "BigDecimal"     => (i: Int) => q"castOptManyApplyBigDecimal(row, $i)"
    }
  }

  val castOptionalRefAttr: richTree => Int => Tree = (t: richTree) => if (t.card == 1)
    (i: Int) => q"castOptOneRefAttr(row, $i)"
  else
    (i: Int) => q"castOptManyRefAttr(row, $i)"

  def castEnum(t: richTree): Int => Tree = if (t.card == 1)
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
    case "java.util.Date" => (i: Int) => q"date(row.get($i).toString)"
    case "java.util.UUID" => (i: Int) => q"java.util.UUID.fromString(row.get($i).toString)"
    case "java.net.URI"   => (i: Int) => q"new java.net.URI(row.get($i).toString)"
    case "BigInt"         => (i: Int) => q"BigInt(row.get($i).toString)"
    case "BigDecimal"     => (i: Int) => q"BigDecimal(row.get($i).toString)"
    case "Any"            => (i: Int) => q"row.get($i)"
  }
}
