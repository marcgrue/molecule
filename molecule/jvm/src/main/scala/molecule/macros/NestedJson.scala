package molecule.macros
import java.lang.{Long => jLong}
import java.util.{Comparator => jComparator, List => jList}
import molecule.api.Molecule
import molecule.facade.Conn
import molecule.macros.NestedTuples._

/** Builder classes of various arity of nested JSON. */
private[molecule] trait NestedJson[OuterTpl] extends NestedTuples[OuterTpl] with jComparator[jList[AnyRef]] { self: Molecule[OuterTpl] =>

  protected def jsonBranch0(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???
  protected def jsonBranch1(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???
  protected def jsonBranch2(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???
  protected def jsonBranch3(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???
  protected def jsonBranch4(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???
  protected def jsonBranch5(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???
  protected def jsonBranch6(sb: StringBuilder, row: jList[AnyRef], leaf: StringBuilder): StringBuilder = ???

  protected def jsonLeaf1(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???
  protected def jsonLeaf2(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???
  protected def jsonLeaf3(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???
  protected def jsonLeaf4(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???
  protected def jsonLeaf5(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???
  protected def jsonLeaf6(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???
  protected def jsonLeaf7(sb: StringBuilder, row: jList[AnyRef]): StringBuilder = ???

  protected val sb0: StringBuilder = new StringBuilder("")
  protected val sb1: StringBuilder = new StringBuilder("")
  protected val sb2: StringBuilder = new StringBuilder("")
  protected val sb3: StringBuilder = new StringBuilder("")
  protected val sb4: StringBuilder = new StringBuilder("")
  protected val sb5: StringBuilder = new StringBuilder("")
  protected val sb6: StringBuilder = new StringBuilder("")
  protected val sb7: StringBuilder = new StringBuilder("")

  protected var firstLevel0      = true
  protected val firstJsonObjects = new Array[Boolean](7)

  protected def resetJsonVars: Unit = {
    resetCastVars
    // Traverse forward through rows (tuples require traversing backwards)
    descending = false
    firstLevel0 = true

    sb0.clear()
    sb0.append("[\n")
    sb1.clear()
    sb2.clear()
    sb3.clear()
    sb4.clear()
    sb5.clear()
    sb6.clear()
    sb7.clear()

    for (lvl <- 0 to 6) {firstJsonObjects(lvl) = true}
  }

  def branch(level: Int, sb: StringBuilder, branchPairs: => StringBuilder, refAttr: String, leaf: StringBuilder, post: => StringBuilder = new StringBuilder()): StringBuilder = {
    // Reset sub levels
    for (lvl <- (level + 1) to 6) {firstJsonObjects(lvl) = true}
    if (firstJsonObjects(level)) {
      firstJsonObjects(level) = false
    } else {
      sb.append(s",\n")
      sb.append("   " * level)
    }
    sb.append("{")
    branchPairs // adds branch pairs to `sb`
    if (sb.last != '{')
      sb.append(s", ") // when no pairs before nested
    quote(sb, refAttr)
    sb.append(": [\n")
    sb.append("   " * (level + 1))
    sb.append(leaf.toString)
    leaf.clear()
    sb.append("]")
    post // adds post fields to `sb`
    sb.append("}")
  }

  def leaf(level: Int, sb: StringBuilder, leafPairs: => StringBuilder): StringBuilder = {
    if (sb.nonEmpty) {
      sb.append(s",\n")
      sb.append("   " * level)
    }
    sb.append("{")
    leafPairs.append("}")
  }
}

object NestedJson {

  trait NestedJson1[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples1[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonLeaf1(sb1, row)).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch0(sb0, prevRow, sb1)
            }
            jsonLeaf1(sb1, row)
            if (i == last) {
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf1(sb1, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
        }
        sb0.append("\n]").toString()
      }
    }
  }

  trait NestedJson2[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples2[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonBranch1(sb1, row,
            jsonLeaf2(sb2, row))).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch1(sb1, prevRow, sb2)
              jsonBranch0(sb0, prevRow, sb1)
            } else if (e1 != p1) {
              jsonBranch1(sb1, prevRow, sb2)
            }
            jsonLeaf2(sb2, row)
            if (i == last) {
              jsonBranch1(sb1, row, sb2)
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf2(sb2, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
        }
        sb0.append("\n]").toString()
      }
    }
  }


  trait NestedJson3[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples3[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonBranch1(sb1, row,
            jsonBranch2(sb2, row,
              jsonLeaf3(sb3, row)))).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]
          e2 = row.get(2).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
              jsonBranch0(sb0, prevRow, sb1)
            } else if (e1 != p1) {
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
            } else if (e2 != p2) {
              jsonBranch2(sb2, prevRow, sb3)
            }
            jsonLeaf3(sb3, row)
            if (i == last) {
              jsonBranch2(sb2, row, sb3)
              jsonBranch1(sb1, row, sb2)
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf3(sb3, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
        }
        sb0.append("\n]").toString()
      }
    }
  }


  trait NestedJson4[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples4[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonBranch1(sb1, row,
            jsonBranch2(sb2, row,
              jsonBranch3(sb3, row,
                jsonLeaf4(sb4, row))))).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]
          e2 = row.get(2).asInstanceOf[jLong]
          e3 = row.get(3).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
              jsonBranch0(sb0, prevRow, sb1)
            } else if (e1 != p1) {
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
            } else if (e2 != p2) {
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
            } else if (e3 != p3) {
              jsonBranch3(sb3, prevRow, sb4)
            }
            jsonLeaf4(sb4, row)
            if (i == last) {
              jsonBranch3(sb3, row, sb4)
              jsonBranch2(sb2, row, sb3)
              jsonBranch1(sb1, row, sb2)
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf4(sb4, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
          p3 = e3
        }
        sb0.append("\n]").toString()
      }
    }
  }


  trait NestedJson5[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples5[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonBranch1(sb1, row,
            jsonBranch2(sb2, row,
              jsonBranch3(sb3, row,
                jsonBranch4(sb4, row,
                  jsonLeaf5(sb5, row)))))).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]
          e2 = row.get(2).asInstanceOf[jLong]
          e3 = row.get(3).asInstanceOf[jLong]
          e4 = row.get(4).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
              jsonBranch0(sb0, prevRow, sb1)
            } else if (e1 != p1) {
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
            } else if (e2 != p2) {
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
            } else if (e3 != p3) {
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
            } else if (e4 != p4) {
              jsonBranch4(sb4, prevRow, sb5)
            }
            jsonLeaf5(sb5, row)
            if (i == last) {
              jsonBranch4(sb4, row, sb5)
              jsonBranch3(sb3, row, sb4)
              jsonBranch2(sb2, row, sb3)
              jsonBranch1(sb1, row, sb2)
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf5(sb5, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
          p3 = e3
          p4 = e4
        }
        sb0.append("\n]").toString()
      }
    }
  }


  trait NestedJson6[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples6[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonBranch1(sb1, row,
            jsonBranch2(sb2, row,
              jsonBranch3(sb3, row,
                jsonBranch4(sb4, row,
                  jsonBranch5(sb5, row,
                    jsonLeaf6(sb6, row))))))).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]
          e2 = row.get(2).asInstanceOf[jLong]
          e3 = row.get(3).asInstanceOf[jLong]
          e4 = row.get(4).asInstanceOf[jLong]
          e5 = row.get(5).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
              jsonBranch0(sb0, prevRow, sb1)
            } else if (e1 != p1) {
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
            } else if (e2 != p2) {
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
            } else if (e3 != p3) {
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
            } else if (e4 != p4) {
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
            } else if (e5 != p5) {
              jsonBranch5(sb5, prevRow, sb6)
            }
            jsonLeaf6(sb6, row)
            if (i == last) {
              jsonBranch5(sb5, row, sb6)
              jsonBranch4(sb4, row, sb5)
              jsonBranch3(sb3, row, sb4)
              jsonBranch2(sb2, row, sb3)
              jsonBranch1(sb1, row, sb2)
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf6(sb6, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
          p3 = e3
          p4 = e4
          p5 = e5
        }
        sb0.append("\n]").toString()
      }
    }
  }


  trait NestedJson7[OuterTpl] extends NestedJson[OuterTpl] with NestedTuples7[OuterTpl] { self: Molecule[OuterTpl] =>

    final override def getJson(implicit conn: Conn): String = {
      resetJsonVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        ""

      } else if (last == 1) {
        row = rows.iterator.next
        jsonBranch0(sb0, row,
          jsonBranch1(sb1, row,
            jsonBranch2(sb2, row,
              jsonBranch3(sb3, row,
                jsonBranch4(sb4, row,
                  jsonBranch5(sb5, row,
                    jsonBranch6(sb6, row,
                      jsonLeaf7(sb7, row)))))))).append("\n]").toString

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]
          e2 = row.get(2).asInstanceOf[jLong]
          e3 = row.get(3).asInstanceOf[jLong]
          e4 = row.get(4).asInstanceOf[jLong]
          e5 = row.get(5).asInstanceOf[jLong]
          e6 = row.get(6).asInstanceOf[jLong]

          if (subsequentRow) {
            if (e0 != p0) {
              jsonBranch6(sb6, prevRow, sb7)
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
              jsonBranch0(sb0, prevRow, sb1)
            } else if (e1 != p1) {
              jsonBranch6(sb6, prevRow, sb7)
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
              jsonBranch1(sb1, prevRow, sb2)
            } else if (e2 != p2) {
              jsonBranch6(sb6, prevRow, sb7)
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
              jsonBranch2(sb2, prevRow, sb3)
            } else if (e3 != p3) {
              jsonBranch6(sb6, prevRow, sb7)
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
              jsonBranch3(sb3, prevRow, sb4)
            } else if (e4 != p4) {
              jsonBranch6(sb6, prevRow, sb7)
              jsonBranch5(sb5, prevRow, sb6)
              jsonBranch4(sb4, prevRow, sb5)
            } else if (e5 != p5) {
              jsonBranch6(sb6, prevRow, sb7)
              jsonBranch5(sb5, prevRow, sb6)
            } else if (e6 != p6) {
              jsonBranch6(sb6, prevRow, sb7)
            }
            jsonLeaf7(sb7, row)
            if (i == last) {
              jsonBranch6(sb6, row, sb7)
              jsonBranch5(sb5, row, sb6)
              jsonBranch4(sb4, row, sb5)
              jsonBranch3(sb3, row, sb4)
              jsonBranch2(sb2, row, sb3)
              jsonBranch1(sb1, row, sb2)
              jsonBranch0(sb0, row, sb1)
            }
          } else {
            jsonLeaf7(sb7, row)
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
          p3 = e3
          p4 = e4
          p5 = e5
          p6 = e6
        }
        sb0.append("\n]").toString()
      }
    }
  }
}