package molecule.transform

import java.util.{Date, List => jList, Map => jMap}
import clojure.lang.{Keyword, LazySeq, PersistentHashSet, PersistentVector}
import molecule.util.Helpers

/** Core molecule interface defining actions that can be called on molecules.
  *
  * Generally we could often have made higher-order methods taking a jsoning function for
  * variating cases. But we prioritize minimizing the macro-generated code as much as possible
  * to lower compile time overhead.
  */
private[molecule] trait JsonBuilder extends Helpers {

  // Macro-materialized row-to-json engine used by `molecule.api.Molecule.getJsonFlat`
  // Adds row as json to a mutable StringBuilder for fast build-up.
  protected def row2json(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = ???


  // Shamelessly adopted from lift-json:
  // https://github.com/lift/framework/blob/db05d863c290c5fd1081a7632263433153fc9fe3/core/json/src/main/scala/net/liftweb/json/JsonAST.scala#L813-L883

  private val jsEscapeChars: Set[Char] =
    List(('\u00ad', '\u00ad'),
      ('\u0600', '\u0604'),
      ('\u070f', '\u070f'),
      ('\u17b4', '\u17b5'),
      ('\u200c', '\u200f'),
      ('\u2028', '\u202f'),
      ('\u2060', '\u206f'),
      ('\ufeff', '\ufeff'),
      ('\ufff0', '\uffff'))
      .foldLeft(Set[Char]()) {
        case (set, (start, end)) =>
          set ++ (start to end).toSet
      }

  private def appendEscapedString(sb: StringBuilder, s: String): Unit = {
    s.foreach { c =>
      val strReplacement = c match {
        case '"'  => "\\\""
        case '\\' => "\\\\"
        case '\b' => "\\b"
        case '\f' => "\\f"
        case '\n' => "\\n"
        case '\r' => "\\r"
        case '\t' => "\\t"
        // Set.contains will cause boxing of c to Character, try and avoid this
        case c if (c >= '\u0000' && c < '\u0020') || jsEscapeChars.contains(c) =>
          "\\u%04x".format(c: Int)

        case _ => ""
      }

      // Use Char version of append if we can, as it's cheaper.
      if (strReplacement.isEmpty) {
        sb.append(c)
      } else {
        sb.append(strReplacement)
      }
    }
  }

  protected def quote(sb: StringBuilder, s: String): StringBuilder = {
    sb.append('"') //open quote
    appendEscapedString(sb, s)
    sb.append('"') //close quote
  }

  protected def quotedPair(sb: StringBuilder, field: String, value: String): StringBuilder = {
    quote(sb, field)
    sb.append(": ")
    quote(sb, value)
  }

  protected def pair(sb: StringBuilder, field: String, value: Any): StringBuilder = {
    quote(sb, field)
    sb.append(": ")
    sb.append(value)
  }


  // One ===========================================================================================

  protected def jsonOneQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, row.get(i).toString)
  }

  protected def jsonOne(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i))
  }

  protected def jsonOneToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i).toString)
  }

  protected def jsonOneDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, date2str(row.get(i).asInstanceOf[Date]))
  }

  protected def jsonOneAny(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = row.get(i) match {
    case value: String         => quotedPair(sb, field, value)
    case value: Int            => pair(sb, field, value)
    case value: Float          => pair(sb, field, value)
    case value: Boolean        => pair(sb, field, value)
    case value: Long           => pair(sb, field, value)
    case value: Double         => pair(sb, field, value)
    case value: java.util.Date => quotedPair(sb, field, date2str(value))
    case value: java.util.UUID => quotedPair(sb, field, value.toString)
    case value: java.net.URI   => quotedPair(sb, field, value.toString)
    case value: BigInt         => pair(sb, field, value)
    case value: BigDecimal     => pair(sb, field, value)
    case valueOfUnknownType    => quotedPair(sb, field, valueOfUnknownType.toString)
  }


  // Many ===========================================================================================

  protected def jsonManyQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      quote(sb, it.next.toString)
    }
    sb.append("]")
  }

  protected def jsonMany(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      sb.append(it.next)
    }
    sb.append("]")
  }

  protected def jsonManyToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      sb.append(it.next.toString)
    }
    sb.append("]")
  }

  protected def jsonManyDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      quote(sb, date2str(it.next.asInstanceOf[Date]))
    }
    sb.append("]")
  }


  // Aggregates ===========================================================================================

  protected def jsonAggrListQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentVector].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      quote(sb, it.next.toString)
    }
    sb.append("]")
  }

  protected def jsonAggrList(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentVector].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      sb.append(it.next)
    }
    sb.append("]")
  }

  protected def jsonAggrListToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentVector].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      sb.append(it.next.toString)
    }
    sb.append("]")
  }

  protected def jsonAggrListDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentVector].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      quote(sb, date2str(it.next.asInstanceOf[Date]))
    }
    sb.append("]")
  }


  protected def jsonAggrListRandQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[LazySeq].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      quote(sb, it.next.toString)
    }
    sb.append("]")
  }

  protected def jsonAggrListRand(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[LazySeq].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      sb.append(it.next)
    }
    sb.append("]")
  }

  protected def jsonAggrListRandToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[LazySeq].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      sb.append(it.next.toString)
    }
    sb.append("]")
  }

  protected def jsonAggrListRandDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": [")
    var subsequent = false
    val it = row.get(i).asInstanceOf[LazySeq].iterator
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      quote(sb, date2str(it.next.asInstanceOf[Date]))
    }
    sb.append("]")
  }


  protected def jsonAggrQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, row.get(i).toString)
  }

  protected def jsonAggr(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i))
  }

  protected def jsonAggrToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i).toString)
  }

  protected def jsonAggrDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, date2str(row.get(i).asInstanceOf[Date]))
  }


  protected def jsonAggrSingleSampleQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, row.get(i).asInstanceOf[PersistentVector].iterator.next.toString)
  }

  protected def jsonAggrSingleSample(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i).asInstanceOf[PersistentVector].iterator.next)
  }

  protected def jsonAggrSingleSampleToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i).asInstanceOf[PersistentVector].iterator.next.toString)
  }

  protected def jsonAggrSingleSampleDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, date2str(row.get(i).asInstanceOf[PersistentVector].iterator.next.asInstanceOf[Date]))
  }


  protected def jsonAggrLazySeqQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, row.get(i).asInstanceOf[LazySeq].iterator.next.toString)
  }

  protected def jsonAggrLazySeq(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i).asInstanceOf[LazySeq].iterator.next)
  }

  protected def jsonAggrLazySeqToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    pair(sb, field, row.get(i).asInstanceOf[LazySeq].iterator.next.toString)
  }

  protected def jsonAggrLazySeqDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quotedPair(sb, field, date2str(row.get(i).asInstanceOf[LazySeq].iterator.next.asInstanceOf[Date]))
  }


  // Optional card one ===========================================================================================

  protected def jsonOptOneQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quotedPair(sb, field, value.asInstanceOf[jMap[String, AnyRef]].values.iterator.next.toString)
    }
  }

  protected def jsonOptOne(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      pair(sb, field, value.asInstanceOf[jMap[String, AnyRef]].values.iterator.next)
    }
  }

  protected def jsonOptOneToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      pair(sb, field, value.asInstanceOf[jMap[String, AnyRef]].values.iterator.next.toString)
    }
  }

  protected def jsonOptOneDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quotedPair(sb, field, date2str(value.asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[Date]))
    }
  }


  // Optional card many ===========================================================================================


  protected def jsonOptManyQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": [")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        quote(sb, it.next.toString)
      }
      sb.append("]")
    }
  }

  protected def jsonOptMany(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": [")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        sb.append(it.next)
      }
      sb.append("]")
    }
  }

  protected def jsonOptManyToString(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": [")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        sb.append(it.next.toString)
      }
      sb.append("]")
    }
  }

  protected def jsonOptManyDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": [")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        quote(sb, date2str(it.next.asInstanceOf[Date]))
      }
      sb.append("]")
    }
  }


  // Enum ===========================================================================================

  protected def jsonOptOneEnum(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quotedPair(sb, field, value.asInstanceOf[jMap[String, AnyRef]].values.iterator.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName)
    }
  }

  protected def jsonOptManyEnum(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": [")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        quote(sb, it.next.asInstanceOf[jMap[String, Keyword]].values.iterator.next.getName)
      }
      sb.append("]")
    }
  }


  // Map ===========================================================================================

  protected def jsonMapQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": {")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var vs = new Array[String](2)
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      vs = it.next.toString.split("@", 2)
      quote(sb, vs(0))
      sb.append(": ")
      quote(sb, vs(1))
    }
    sb.append("}")
  }

  protected def jsonMap(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": {")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var vs = new Array[String](2)
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      vs = it.next.toString.split("@", 2)
      quote(sb, vs(0))
      sb.append(": ")
      sb.append(vs(1))
    }
    sb.append("}")
  }

  protected def jsonMapDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    quote(sb, field)
    sb.append(": {")
    var subsequent = false
    val it = row.get(i).asInstanceOf[PersistentHashSet].iterator
    var vs = new Array[String](2)
    while (it.hasNext) {
      if (subsequent) sb.append(", ") else subsequent = true
      vs = it.next.toString.split("@", 2)
      quote(sb, vs(0))
      sb.append(": ")
      quote(sb, vs(1))
    }
    sb.append("}")
  }


  // Optional Map ===========================================================================================

  protected def jsonOptMapQuoted(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": {")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      var vs = new Array[String](2)
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        vs = it.next.toString.split("@", 2)
        quote(sb, vs(0))
        sb.append(": ")
        quote(sb, vs(1))
      }
      sb.append("}")
    }
  }

  protected def jsonOptMap(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": {")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      var vs = new Array[String](2)
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        vs = it.next.toString.split("@", 2)
        quote(sb, vs(0))
        sb.append(": ")
        sb.append(vs(1))
      }
      sb.append("}")
    }
  }

  protected def jsonOptMapDate(sb: StringBuilder, field: String, row: jList[_], i: Int): StringBuilder = {
    val value = row.get(i)
    if (value == null) {
      pair(sb, field, "null")
    } else {
      quote(sb, field)
      sb.append(": {")
      var subsequent = false
      val it = value.asInstanceOf[jMap[String, PersistentVector]].values.iterator.next.iterator
      var vs = new Array[String](2)
      while (it.hasNext) {
        if (subsequent) sb.append(", ") else subsequent = true
        vs = it.next.toString.split("@", 2)
        quote(sb, vs(0))
        sb.append(": ")
        quote(sb, vs(1))
      }
      sb.append("}")
    }
  }
}
