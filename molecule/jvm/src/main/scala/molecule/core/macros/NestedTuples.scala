package molecule.core.macros
import java.lang.{Long => jLong}
import java.util.{ArrayList => jArrayList, Comparator => jComparator, List => jList}
import molecule.core.api.Molecule
import molecule.datomic.base.facade.Conn

/** Builder classes of various arity of nested tuples. */
trait NestedTuples[Obj, OuterTpl] extends jComparator[jList[AnyRef]] { self: Molecule[Obj, OuterTpl] =>

  // Lazily re-use nested list
  final override def getIterable(implicit conn: Conn): Iterable[OuterTpl] = get(conn)

  val levels = _nestedQuery.fold(0)(_.f.outputs.size - _query.f.outputs.size)

  protected def castBranch0(row: jList[AnyRef], leaf: List[Any]): OuterTpl = ???
  protected def castBranch1(row: jList[AnyRef], leaf: List[Any]): Any = ???
  protected def castBranch2(row: jList[AnyRef], leaf: List[Any]): Any = ???
  protected def castBranch3(row: jList[AnyRef], leaf: List[Any]): Any = ???
  protected def castBranch4(row: jList[AnyRef], leaf: List[Any]): Any = ???
  protected def castBranch5(row: jList[AnyRef], leaf: List[Any]): Any = ???
  protected def castBranch6(row: jList[AnyRef], leaf: List[Any]): Any = ???

  protected def castLeaf1(row: jList[AnyRef]): Any = ???
  protected def castLeaf2(row: jList[AnyRef]): Any = ???
  protected def castLeaf3(row: jList[AnyRef]): Any = ???
  protected def castLeaf4(row: jList[AnyRef]): Any = ???
  protected def castLeaf5(row: jList[AnyRef]): Any = ???
  protected def castLeaf6(row: jList[AnyRef]): Any = ???
  protected def castLeaf7(row: jList[AnyRef]): Any = ???

  protected var row    : jList[AnyRef] = new jArrayList[AnyRef]()
  protected var prevRow: jList[AnyRef] = new jArrayList[AnyRef]()

  protected var p0: jLong = 0L
  protected var p1: jLong = 0L
  protected var p2: jLong = 0L
  protected var p3: jLong = 0L
  protected var p4: jLong = 0L
  protected var p5: jLong = 0L
  protected var p6: jLong = 0L

  protected var e0: jLong = 0L
  protected var e1: jLong = 0L
  protected var e2: jLong = 0L
  protected var e3: jLong = 0L
  protected var e4: jLong = 0L
  protected var e5: jLong = 0L
  protected var e6: jLong = 0L

  protected var acc0: List[OuterTpl] = List.empty[OuterTpl]
  protected var acc1: List[Any]      = List.empty[Any]
  protected var acc2: List[Any]      = List.empty[Any]
  protected var acc3: List[Any]      = List.empty[Any]
  protected var acc4: List[Any]      = List.empty[Any]
  protected var acc5: List[Any]      = List.empty[Any]
  protected var acc6: List[Any]      = List.empty[Any]
  protected var acc7: List[Any]      = List.empty[Any]

  protected var subsequentRow: Boolean = false
  protected var i                      = 0
  protected var sortIndex              = 0
  protected var result                 = 0
  protected var descending             = true

  protected def resetCastVars: Unit = {
    p0 = 0L
    p1 = 0L
    p2 = 0L
    p3 = 0L
    p4 = 0L
    p5 = 0L
    p6 = 0L

    e0 = 0L
    e1 = 0L
    e2 = 0L
    e3 = 0L
    e4 = 0L
    e5 = 0L
    e6 = 0L

    acc0 = Nil
    acc1 = Nil
    acc2 = Nil
    acc3 = Nil
    acc4 = Nil
    acc5 = Nil
    acc6 = Nil
    acc7 = Nil

    // Traverse backwards through rows
    descending = true
    subsequentRow = false
    i = 0
  }

  // java.util.Comparator sorting interface implemented by NestedTuples subclasses
  protected def compare(a: jList[AnyRef], b: jList[AnyRef]): Int = {
    sortIndex = 0
    result = 0
    if (descending) {
      do {
        result = (-a.get(sortIndex).asInstanceOf[jLong]).compareTo(-b.get(sortIndex).asInstanceOf[jLong])
        sortIndex += 1
      } while (sortIndex < levels && result == 0)
    } else {
      do {
        result = a.get(sortIndex).asInstanceOf[jLong].compareTo(b.get(sortIndex).asInstanceOf[jLong])
        sortIndex += 1
      } while (sortIndex < levels && result == 0)
    }
    result
  }
}


object NestedTuples {

  trait NestedTuples1[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castLeaf1(row))))

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]

          if (subsequentRow) {
            if (i == last) {
              if (e0 != p0) {
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc1 = List(castLeaf1(row))
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e1 != p1 */ {
                acc1 = castLeaf1(row) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc1 = List(castLeaf1(row))

            } else /* e1 != p1 */ {
              acc1 = castLeaf1(row) :: acc1
            }
          } else {
            acc1 = List(castLeaf1(row))
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
        }
        acc0
      }
    }
  }


  trait NestedTuples2[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castBranch1(row,
            List(castLeaf2(row))))))

      } else {
        rows.sort(this)
        val it = rows.iterator
        while (it.hasNext) {
          i += 1
          row = it.next
          e0 = row.get(0).asInstanceOf[jLong]
          e1 = row.get(1).asInstanceOf[jLong]

          if (subsequentRow) {
            if (i == last) {
              if (e0 != p0) {
                acc1 = castBranch1(prevRow, acc2) :: acc1
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc2 = List(castLeaf2(row))
                acc1 = List(castBranch1(row, acc2))
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e1 != p1) {
                acc1 = castBranch1(prevRow, acc2) :: acc1

                acc2 = List(castLeaf2(row))
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e2 != p2 */ {
                acc2 = castLeaf2(row) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc1 = castBranch1(prevRow, acc2) :: acc1
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc2 = List(castLeaf2(row))
              acc1 = Nil

            } else if (e1 != p1) {
              acc1 = castBranch1(prevRow, acc2) :: acc1

              acc2 = List(castLeaf2(row))

            } else /* e2 != p2 */ {
              acc2 = castLeaf2(row) :: acc2
            }
          } else {
            acc2 = List(castLeaf2(row))
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
        }
        acc0
      }
    }
  }


  trait NestedTuples3[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] with jComparator[jList[AnyRef]] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castBranch1(row,
            List(castBranch2(row,
              List(castLeaf3(row))))))))

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
            if (i == last) {
              if (e0 != p0) {
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc3 = List(castLeaf3(row))
                acc2 = List(castBranch2(row, acc3))
                acc1 = List(castBranch1(row, acc2))
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e1 != p1) {
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1

                acc3 = List(castLeaf3(row))
                acc2 = List(castBranch2(row, acc3))
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e2 != p2) {
                acc2 = castBranch2(prevRow, acc3) :: acc2

                acc3 = List(castLeaf3(row))
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e3 != p3 */ {
                acc3 = castLeaf3(row) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc3 = List(castLeaf3(row))
              acc2 = Nil
              acc1 = Nil

            } else if (e1 != p1) {
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1

              acc3 = List(castLeaf3(row))
              acc2 = Nil

            } else if (e2 != p2) {
              acc2 = castBranch2(prevRow, acc3) :: acc2

              acc3 = List(castLeaf3(row))

            } else /* e3 != p3 */ {
              acc3 = castLeaf3(row) :: acc3
            }
          } else {
            acc3 = List(castLeaf3(row))
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
        }
        acc0
      }
    }
  }


  trait NestedTuples4[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] with jComparator[jList[AnyRef]] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castBranch1(row,
            List(castBranch2(row,
              List(castBranch3(row,
                List(castLeaf4(row))))))))))

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
            if (i == last) {
              if (e0 != p0) {
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc4 = List(castLeaf4(row))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = List(castBranch1(row, acc2))
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e1 != p1) {
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1

                acc4 = List(castLeaf4(row))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e2 != p2) {
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2

                acc4 = List(castLeaf4(row))
                acc3 = List(castBranch3(row, acc4))
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e3 != p3) {
                acc3 = castBranch3(prevRow, acc4) :: acc3

                acc4 = List(castLeaf4(row))
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e4 != p4 */ {
                acc4 = castLeaf4(row) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc4 = List(castLeaf4(row))
              acc3 = Nil
              acc2 = Nil
              acc1 = Nil

            } else if (e1 != p1) {
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1

              acc4 = List(castLeaf4(row))
              acc3 = Nil
              acc2 = Nil

            } else if (e2 != p2) {
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2

              acc4 = List(castLeaf4(row))
              acc3 = Nil

            } else if (e3 != p3) {
              acc3 = castBranch3(prevRow, acc4) :: acc3

              acc4 = List(castLeaf4(row))

            } else /* e4 != p4 */ {
              acc4 = castLeaf4(row) :: acc4
            }
          } else {
            acc4 = List(castLeaf4(row))
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
          p3 = e3
        }
        acc0
      }
    }
  }


  trait NestedTuples5[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] with jComparator[jList[AnyRef]] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castBranch1(row,
            List(castBranch2(row,
              List(castBranch3(row,
                List(castBranch4(row,
                  List(castLeaf5(row))))))))))))

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
            if (i == last) {
              if (e0 != p0) {
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc5 = List(castLeaf5(row))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = List(castBranch1(row, acc2))
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e1 != p1) {
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1

                acc5 = List(castLeaf5(row))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e2 != p2) {
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2

                acc5 = List(castLeaf5(row))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e3 != p3) {
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3

                acc5 = List(castLeaf5(row))
                acc4 = List(castBranch4(row, acc5))
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e4 != p4) {
                acc4 = castBranch4(prevRow, acc5) :: acc4

                acc5 = List(castLeaf5(row))
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e5 != p5 */ {
                acc5 = castLeaf5(row) :: acc5
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc5 = List(castLeaf5(row))
              acc4 = Nil
              acc3 = Nil
              acc2 = Nil
              acc1 = Nil

            } else if (e1 != p1) {
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1

              acc5 = List(castLeaf5(row))
              acc4 = Nil
              acc3 = Nil
              acc2 = Nil

            } else if (e2 != p2) {
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2

              acc5 = List(castLeaf5(row))
              acc4 = Nil
              acc3 = Nil

            } else if (e3 != p3) {
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3

              acc5 = List(castLeaf5(row))
              acc4 = Nil

            } else if (e4 != p4) {
              acc4 = castBranch4(prevRow, acc5) :: acc4

              acc5 = List(castLeaf5(row))

            } else /* e5 != p5 */ {
              acc5 = castLeaf5(row) :: acc5
            }
          } else {
            acc5 = List(castLeaf5(row))
            subsequentRow = true
          }

          prevRow = row
          p0 = e0
          p1 = e1
          p2 = e2
          p3 = e3
          p4 = e4
        }
        acc0
      }
    }
  }


  trait NestedTuples6[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] with jComparator[jList[AnyRef]] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castBranch1(row,
            List(castBranch2(row,
              List(castBranch3(row,
                List(castBranch4(row,
                  List(castBranch5(row,
                    List(castLeaf6(row))))))))))))))

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
            if (i == last) {
              if (e0 != p0) {
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc6 = List(castLeaf6(row))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = List(castBranch1(row, acc2))
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e1 != p1) {
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1

                acc6 = List(castLeaf6(row))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e2 != p2) {
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2

                acc6 = List(castLeaf6(row))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e3 != p3) {
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3

                acc6 = List(castLeaf6(row))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e4 != p4) {
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4

                acc6 = List(castLeaf6(row))
                acc5 = List(castBranch5(row, acc6))
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e5 != p5) {
                acc5 = castBranch5(prevRow, acc6) :: acc5

                acc6 = List(castLeaf6(row))
                acc5 = castBranch5(row, acc6) :: acc5
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e6 != p6 */ {
                acc6 = castLeaf6(row) :: acc6
                acc5 = castBranch5(row, acc6) :: acc5
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc6 = List(castLeaf6(row))
              acc5 = Nil
              acc4 = Nil
              acc3 = Nil
              acc2 = Nil
              acc1 = Nil

            } else if (e1 != p1) {
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1

              acc6 = List(castLeaf6(row))
              acc5 = Nil
              acc4 = Nil
              acc3 = Nil
              acc2 = Nil

            } else if (e2 != p2) {
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2

              acc6 = List(castLeaf6(row))
              acc5 = Nil
              acc4 = Nil
              acc3 = Nil

            } else if (e3 != p3) {
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3

              acc6 = List(castLeaf6(row))
              acc5 = Nil
              acc4 = Nil

            } else if (e4 != p4) {
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4

              acc6 = List(castLeaf6(row))
              acc5 = Nil

            } else if (e5 != p5) {
              acc5 = castBranch5(prevRow, acc6) :: acc5

              acc6 = List(castLeaf6(row))

            } else /* e6 != p6 */ {
              acc6 = castLeaf6(row) :: acc6
            }
          } else {
            acc6 = List(castLeaf6(row))
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
        acc0
      }
    }
  }


  trait NestedTuples7[Obj, OuterTpl] extends NestedTuples[Obj, OuterTpl] with jComparator[jList[AnyRef]] { self: Molecule[Obj, OuterTpl] =>

    final override def get(implicit conn: Conn): List[OuterTpl] = {
      resetCastVars
      val rows: java.util.ArrayList[jList[AnyRef]] = new java.util.ArrayList(conn.query(_model, _nestedQuery.get))
      val last = rows.size

      if (last == 0) {
        List.empty[OuterTpl]

      } else if (last == 1) {
        row = rows.iterator.next
        List(castBranch0(row,
          List(castBranch1(row,
            List(castBranch2(row,
              List(castBranch3(row,
                List(castBranch4(row,
                  List(castBranch5(row,
                    List(castBranch6(row,
                      List(castLeaf7(row))))))))))))))))

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
            if (i == last) {
              if (e0 != p0) {
                acc6 = castBranch6(prevRow, acc7) :: acc6
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1
                acc0 = castBranch0(prevRow, acc1) :: acc0

                acc7 = List(castLeaf7(row))
                acc6 = List(castBranch6(row, acc7))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = List(castBranch1(row, acc2))
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e1 != p1) {
                acc6 = castBranch6(prevRow, acc7) :: acc6
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2
                acc1 = castBranch1(prevRow, acc2) :: acc1

                acc7 = List(castLeaf7(row))
                acc6 = List(castBranch6(row, acc7))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = List(castBranch2(row, acc3))
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e2 != p2) {
                acc6 = castBranch6(prevRow, acc7) :: acc6
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3
                acc2 = castBranch2(prevRow, acc3) :: acc2

                acc7 = List(castLeaf7(row))
                acc6 = List(castBranch6(row, acc7))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = List(castBranch3(row, acc4))
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e3 != p3) {
                acc6 = castBranch6(prevRow, acc7) :: acc6
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4
                acc3 = castBranch3(prevRow, acc4) :: acc3

                acc7 = List(castLeaf7(row))
                acc6 = List(castBranch6(row, acc7))
                acc5 = List(castBranch5(row, acc6))
                acc4 = List(castBranch4(row, acc5))
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e4 != p4) {
                acc6 = castBranch6(prevRow, acc7) :: acc6
                acc5 = castBranch5(prevRow, acc6) :: acc5
                acc4 = castBranch4(prevRow, acc5) :: acc4

                acc7 = List(castLeaf7(row))
                acc6 = List(castBranch6(row, acc7))
                acc5 = List(castBranch5(row, acc6))
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e5 != p5) {
                acc6 = castBranch6(prevRow, acc7) :: acc6
                acc5 = castBranch5(prevRow, acc6) :: acc5

                acc7 = List(castLeaf7(row))
                acc6 = List(castBranch6(row, acc7))
                acc5 = castBranch5(row, acc6) :: acc5
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else if (e6 != p6) {
                acc6 = castBranch6(prevRow, acc7) :: acc6

                acc7 = List(castLeaf7(row))
                acc6 = castBranch6(row, acc7) :: acc6
                acc5 = castBranch5(row, acc6) :: acc5
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0

              } else /* e7 != p7 */ {
                acc7 = castLeaf7(row) :: acc7
                acc6 = castBranch6(row, acc7) :: acc6
                acc5 = castBranch5(row, acc6) :: acc5
                acc4 = castBranch4(row, acc5) :: acc4
                acc3 = castBranch3(row, acc4) :: acc3
                acc2 = castBranch2(row, acc3) :: acc2
                acc1 = castBranch1(row, acc2) :: acc1
                acc0 = castBranch0(row, acc1) :: acc0
              }

            } else if (e0 != p0) {
              acc6 = castBranch6(prevRow, acc7) :: acc6
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1
              acc0 = castBranch0(prevRow, acc1) :: acc0

              acc7 = List(castLeaf7(row))
              acc6 = Nil
              acc5 = Nil
              acc4 = Nil
              acc3 = Nil
              acc2 = Nil
              acc1 = Nil

            } else if (e1 != p1) {
              acc6 = castBranch6(prevRow, acc7) :: acc6
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2
              acc1 = castBranch1(prevRow, acc2) :: acc1

              acc7 = List(castLeaf7(row))
              acc6 = Nil
              acc5 = Nil
              acc4 = Nil
              acc3 = Nil
              acc2 = Nil

            } else if (e2 != p2) {
              acc6 = castBranch6(prevRow, acc7) :: acc6
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3
              acc2 = castBranch2(prevRow, acc3) :: acc2

              acc7 = List(castLeaf7(row))
              acc6 = Nil
              acc5 = Nil
              acc4 = Nil
              acc3 = Nil

            } else if (e3 != p3) {
              acc6 = castBranch6(prevRow, acc7) :: acc6
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4
              acc3 = castBranch3(prevRow, acc4) :: acc3

              acc7 = List(castLeaf7(row))
              acc6 = Nil
              acc5 = Nil
              acc4 = Nil

            } else if (e4 != p4) {
              acc6 = castBranch6(prevRow, acc7) :: acc6
              acc5 = castBranch5(prevRow, acc6) :: acc5
              acc4 = castBranch4(prevRow, acc5) :: acc4

              acc7 = List(castLeaf7(row))
              acc6 = Nil
              acc5 = Nil

            } else if (e5 != p5) {
              acc6 = castBranch6(prevRow, acc7) :: acc6
              acc5 = castBranch5(prevRow, acc6) :: acc5

              acc7 = List(castLeaf7(row))
              acc6 = Nil

            } else if (e6 != p6) {
              acc6 = castBranch6(prevRow, acc7) :: acc6

              acc7 = List(castLeaf7(row))

            } else /* e7 != p7 */ {
              acc7 = castLeaf7(row) :: acc7
            }
          } else {
            acc7 = List(castLeaf7(row))
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
        acc0
      }
    }
  }
}















































