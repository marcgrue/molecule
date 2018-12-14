package molecule.macros
import molecule.ops.TreeOps
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox


private[molecule] trait Json extends TreeOps {
  val c: blackbox.Context
  import c.universe._

  val z = DebugMacro("Json", 1)


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

  val jsonAggrListVector : (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrListVector(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrListVector(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrListVector(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrListVector(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrListVectorQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrListVectorToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrListVectorToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrListVectorDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrListVectorToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrListVectorQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrListVectorQuoted(sb, $field, row, $i)"
  }
  val jsonAggrListLazySeq: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrListLazySeq(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrListLazySeq(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrListLazySeq(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrListLazySeq(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrListLazySeqQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrListLazySeqToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrListLazySeqToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrListLazySeqDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrListLazySeqToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrListLazySeqQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrListLazySeqQuoted(sb, $field, row, $i)"
  }

  val jsonAggr: (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
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

  val jsonAggrVector : (String, String) => Int => Tree = (tpe: String, field: String) => tpe match {
    case "Int"            => (i: Int) => q"jsonAggrVector(sb, $field, row, $i)"
    case "Long"           => (i: Int) => q"jsonAggrVector(sb, $field, row, $i)"
    case "Float"          => (i: Int) => q"jsonAggrVector(sb, $field, row, $i)"
    case "Double"         => (i: Int) => q"jsonAggrVector(sb, $field, row, $i)"
    case "String"         => (i: Int) => q"jsonAggrVectorQuoted(sb, $field, row, $i)"
    case "BigInt"         => (i: Int) => q"jsonAggrVectorToString(sb, $field, row, $i)"
    case "BigDecimal"     => (i: Int) => q"jsonAggrVectorToString(sb, $field, row, $i)"
    case "java.util.Date" => (i: Int) => q"jsonAggrVectorDate(sb, $field, row, $i)"
    case "Boolean"        => (i: Int) => q"jsonAggrVectorToString(sb, $field, row, $i)"
    case "java.net.URI"   => (i: Int) => q"jsonAggrVectorQuoted(sb, $field, row, $i)"
    case "java.util.UUID" => (i: Int) => q"jsonAggrVectorQuoted(sb, $field, row, $i)"
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
