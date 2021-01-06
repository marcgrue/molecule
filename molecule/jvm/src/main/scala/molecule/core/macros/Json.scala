package molecule.core.macros
import molecule.core.ops.TreeOps
import scala.reflect.macros.blackbox

private[molecule] trait Json extends TreeOps {
  val c: blackbox.Context
  import c.universe._

  val z = InspectMacro("Json", 1)


  val jsonOneAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Int2"           => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonOneDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "Any"            => (i: Int) => q"jsonOneAny(sb, $field, row, $i)"
  }
  val jsonManyAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonManyQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonMany(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonMany(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonManyToString(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonManyToString(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonManyToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonManyDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonManyQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonManyQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonManyToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonManyToString(sb, $field, row, $i)"
  }


  val jsonOptOneAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonOptOneQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonOptOne(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonOptOne(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonOptOneToString(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonOptOneToString(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonOptOneToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonOptOneDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonOptOneQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonOptOneQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonOptOneToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonOptOneToString(sb, $field, row, $i)"
  }
  val jsonOptManyAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonOptManyQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonOptMany(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonOptMany(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonOptManyToString(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonOptManyToString(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonOptManyToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonOptManyDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonOptManyQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonOptManyQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonOptManyToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonOptManyToString(sb, $field, row, $i)"
  }

  val jsonAggrList    : (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrList(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrList(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrList(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrList(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrListQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrListToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrListToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrListDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrListToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrListQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrListQuoted(sb, $field, row, $i)"
  }
  val jsonAggrListRand: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrListRand(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrListRand(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrListRand(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrListRand(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrListRandQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrListRandToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrListRandToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrListRandDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrListRandToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrListRandQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrListRandQuoted(sb, $field, row, $i)"
  }

  val jsonAggrSingle: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonOneDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
  }

  val jsonAggrSingleSample: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrSingleSample(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrSingleSample(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrSingleSample(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrSingleSample(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrSingleSampleQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrSingleSampleToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrSingleSampleToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrSingleSampleDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrSingleSampleToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrSingleSampleQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrSingleSampleQuoted(sb, $field, row, $i)"
  }
  val jsonAggrLazySeq: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrLazySeq(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrLazySeq(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrLazySeq(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrLazySeq(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrLazySeqQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrLazySeqToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrLazySeqToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrLazySeqDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrLazySeqToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrLazySeqQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrLazySeqQuoted(sb, $field, row, $i)"
  }

  val jsonOptOneEnum: (String, String) => Int => Tree = (tpe: String, field: String) => {
    i: Int => q"jsonOptOneEnum(sb, $field, row, $i)"
  }
  val jsonOptManyEnum: (String, String) => Int => Tree = (tpe: String, field: String) => {
    i: Int => q"jsonOptManyEnum(sb, $field, row, $i)"
  }

  val jsonMandatoryMapAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonMapQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonMap(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonMap(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonMap(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonMap(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonMap(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonMapDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonMapQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonMapQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonMap(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonMap(sb, $field, row, $i)"
  }

  val jsonOptionalMapAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonOptMapQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonOptMapDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonOptMapQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonOptMapQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonOptMap(sb, $field, row, $i)"
  }

  val jsonKeyedMapAttr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "String"         => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "Int"            => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonOne(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonOneDate(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonOneQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonOneToString(sb, $field, row, $i)"
    case "Any"            => (i: Int) => q"jsonOneAny(sb, $field, row, $i)"
  }
}
