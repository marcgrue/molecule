//package molecule.factory
//
//import java.lang.{Boolean => jBoolean, Double => jDouble, Long => jLong}
//import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
//import java.util.{List => jList, Map => jMap}
//
//import molecule.ast.model._
//import molecule.ast.query.Query
//import molecule.util.Debug
//
//import scala.language.experimental.macros
//import scala.language.higherKinds
//
////trait NestedResolver[Ctx <: Context] extends ValueCaster[Ctx] {
////  import c.universe._
//
//trait NestedResolver[OuterTpl] extends ValueCaster { //self: molecule.api.MoleculeBase =>
//
//  val xx = Debug("NestedResolver", 1)
//
//  // Compile time
//
//  val _modelE: Model //= ???
//  val _queryE: Query //= ???
//
//  val flatModel = {
//    def recurse(element: Element): Seq[Element] = element match {
//      case n: Nested                                             => n.elements flatMap recurse
//      case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
//      case a: Atom                                               => Seq(a)
//      case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
//      case m: Meta                                               => Seq(m)
//      case other                                                 => Seq()
//    }
//
//    val elements = _modelE.elements flatMap recurse
//    if (elements.size != _queryE.f.outputs.size)
//      sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + _queryE.f.outputs.size + "):\n" +
//        _modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + _queryE + "\n----------------\n")
//
//    elements
//  }
//
//  val entityIndexes = flatModel.zipWithIndex.collect {
//    case (Meta(_, _, _, _, IndexVal), i) => i
//  }
//  val maxDepth      = entityIndexes.size - 1
//
//  val manyRefIndexes = flatModel.zipWithIndex.collect {
//    case (Meta(_, "many-ref", _, _, IndexVal), i) => i
//  }
//
//  val indexMap = flatModel.zipWithIndex.foldLeft(0, Seq.empty[(Int, Int)]) {
//    case ((rawIndex, indexMap), (meta, i)) => meta match {
//      case Meta(_, "many-ref", _, _, IndexVal) => (rawIndex, indexMap :+ (rawIndex, i))
//      case Meta(_, _, _, _, IndexVal)          => (rawIndex + 1, indexMap :+ (rawIndex, i))
//      case _                                   => (rawIndex + 1, indexMap :+ (rawIndex, i))
//    }
//  }._2.toMap
//
//
//  // Runtime
//
//  //  def getNestedTuples[OuterTpl <: Product : WeakTypeTag](rows: Iterable[jList[AnyRef]]): Iterable[OuterTpl] = {
////  def getNestedTuples[OuterTpl <: Product](rows: Iterable[jList[AnyRef]]): Iterable[OuterTpl] = {
//  def getNestedTuples(rows: Iterable[jList[AnyRef]]): Iterable[OuterTpl] = {
//    if (rows.isEmpty)
//      Iterable.empty[OuterTpl]
//    else
//      getNestedTuples1(rows)
//  }
//
//  //  private def getNestedTuples1[OuterTpl <: Product : WeakTypeTag](rows: Iterable[jList[AnyRef]]): Iterable[OuterTpl] = {
////  private def getNestedTuples1[OuterTpl](rows: Iterable[jList[AnyRef]]): Iterable[OuterTpl] = {
//  private def getNestedTuples1(rows: Iterable[jList[AnyRef]]): Iterable[OuterTpl] = {
//
//    //println("===================================================================================")
//    //println(model)
//    //    println(_modelE)
//    //println(_queryE)
//    //println(_queryE.datalog)
//    //println("---- ")
//    flatModel foreach println
//    //println("---- " + entityIndexes)
//    //println("---- " + indexMap)
//
//    val rowSeq = rows.toSeq
//    val sortedRows = entityIndexes match {
//      case List(a)                               => rowSeq.sortBy(row => row.get(a).asInstanceOf[Long])
//      case List(a, b)                            => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long]))
//      case List(a, b, c)                         => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long]))
//      case List(a, b, c, d)                      => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long]))
//      case List(a, b, c, d, e)                   => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f)                => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g)             => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g, h)          => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g, h, i)       => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g, h, i, j)    => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => row.get(j).asInstanceOf[Long])
//      case List(a, b, c, d, e, f, g, h, i, j, k) => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => (row.get(j).asInstanceOf[Long], row.get(k).asInstanceOf[Long]))
//    }
//
//    val rowCount = sortedRows.length
//
//    /*
//    * e0 v1 e1 v2 v3
//    *
//    * 1  a  5  42  x
//    * 1  a  6  43  y
//    * 2  b  7  44  z
//    * 3  c  8  45  q
//    *
//    * */
//
//    val acc = new Array[OuterTpl](rowCount)
//
//    var i = 0
//    val nested = sortedRows.foldLeft(
//      null: AnyRef, null: AnyRef, // Entity indexes
//      null: String, // Level 1 attributes
//      Seq.empty[(Int, String)] // Level 2 tuple
//    ) {
//      case ((e1_, _,_,_,_, tt2), r) if e1_ == null =>
//
//      case ((e1_, e2_, v1_, v2_, v3_, tt2), r) =>
//        val (e1, e2) = (r.get(0), r.get(2))
//        val (v1, v2, v3) = (
//          r.get(1).asInstanceOf[String],
//          (r.get(3) match {
//            case l: jLong  => l.toInt
//            case s: String => s.toInt
//            case other     => other
//          }).asInstanceOf[Int],
//          r.get(4).asInstanceOf[String]
//        )
//        val t2 = (v2, v3)
//
//        val prevValues = if (e1_ == null) {
//          // First row
//          //
//          (e1, e2, v1, tt2 :+ t2)
//
//        } else if (e1_ != e1) {
//          // New level 1 - save accumulated row
//          acc(i) = (v1_, tt1).asInstanceOf[OuterTpl]
//          // Pass on current values and start over with nested tuples
//          (e1, e2, v1, v2, v3, Seq(t1))
//
//        } else if (e2_ != e2) {
//          // New level 2 - save accumulated tuple 1
//          val t1 = (v1_, t1_).asInstanceOf[OuterTpl]
//          // Pass on current values
//          (e1, e2, v1, v2, v3, t1)
//
//        } else {
//          (e1, e2, v1, v2, v3, t1)
//        }
//        i += 1
//        prevValues
//    }
//
////    val nested = sortedRows.foldLeft(
////      null: AnyRef, null: AnyRef, // Entity indexes
////      null: String, // Level 1 attributes
////      Seq.empty[(Int, String)] // Level 2 tuple
////    ) {
////      case ((e1_, _,_,_,_, tt2), r) if e1_ == null =>
////
////      case ((e1_, e2_, v1_, v2_, v3_, tt2), r) =>
////        val (e1, e2) = (r.get(0), r.get(2))
////        val (v1, v2, v3) = (
////          r.get(1).asInstanceOf[String],
////          (r.get(3) match {
////            case l: jLong  => l.toInt
////            case s: String => s.toInt
////            case other     => other
////          }).asInstanceOf[Int],
////          r.get(4).asInstanceOf[String]
////        )
////        val t2 = (v2, v3)
////
////        val prevValues = if (e1_ == null) {
////          // First row
////          //
////          (e1, e2, v1, tt2 :+ t2)
////
////        } else if (e1_ != e1) {
////          // New level 1 - save accumulated row
////          acc(i) = (v1_, tt1).asInstanceOf[OuterTpl]
////          // Pass on current values and start over with nested tuples
////          (e1, e2, v1, v2, v3, Seq(t1))
////
////        } else if (e2_ != e2) {
////          // New level 2 - save accumulated tuple 1
////          val t1 = (v1_, t1_).asInstanceOf[OuterTpl]
////          // Pass on current values
////          (e1, e2, v1, v2, v3, t1)
////
////        } else {
////          (e1, e2, v1, v2, v3, t1)
////        }
////        i += 1
////        prevValues
////    }
//
//    acc.toIterable
//  }
//
//
//  //    def resolveNested[Tpl <: Product : WeakTypeTag](prevTpl: Option[Tpl], prevRow: Seq[Any], row: Seq[Any], entityIndex: Int, depth: Int, shift: Int): Tpl = {
//  //
//  //      def resolve[NestedTpes](typeIndex: Int): NestedTpes = {
//  //
//  //        //        val rowIndex = entityIndex + shift + typeIndex
//  //        //        val prevEnt = if (prevRow.head == 0L) 0L else prevRow.apply(entityIndex).asInstanceOf[Long]
//  //        //        val curEnt = row.apply(entityIndex).asInstanceOf[Long]
//  //        //        val isNewNested = if (prevEnt == 0L) true else prevEnt != curEnt
//  //        //        val isNewManyRef = manyRefIndexes.nonEmpty && prevEnt != 0L && prevRow.apply(manyRefIndexes.head) != row.apply(manyRefIndexes.head)
//  //        //
//  //        //        if (prevTpl.isEmpty || isNewNested || isNewManyRef) {
//  //        //          val toAdd = resolveNested[NestedTpes](Option.empty[NestedTpes], prevRow, row, rowIndex, depth + 1, shift)
//  //        //          Seq(toAdd)
//  //        //
//  //        //        } else if (prevTpl.get.isInstanceOf[Seq[_]]) {
//  //        //
//  //        //          val prevTpl2 = Some(prevTpl.get.asInstanceOf[Seq[NestedTpes]].last)
//  //        //          val toAdd = resolveNested[NestedTpes](prevTpl2, prevRow, row, rowIndex, depth + 1, shift)
//  //        //
//  //        //          prevTpl.get.asInstanceOf[Seq[NestedTpes]] :+ toAdd
//  //        //
//  //        //        } else {
//  //        //
//  //        //          val tpl1 = prevTpl.get
//  //        //          val tpl2 = tpl1.productElement(typeIndex).asInstanceOf[Seq[NestedTpes]]
//  //        //
//  //        //          val prevTpl2 = Some(prevTpl.get.productElement(typeIndex).asInstanceOf[Seq[NestedTpes]].last)
//  //        //
//  //        //          val toAdd = resolveNested[NestedTpes](prevTpl2, prevRow, row, rowIndex, depth + 1, shift)
//  //        //
//  //        //          val adjustedIndex = indexMap(rowIndex)
//  //        //          val newNested = prevRow.apply(adjustedIndex).asInstanceOf[Long] != row.apply(adjustedIndex).asInstanceOf[Long] || depth == maxDepth
//  //        //          //              val isNewManyRef = manyRefIndexes.nonEmpty && prevRow.apply(manyRefIndexes.head) != row.apply(manyRefIndexes.head)
//  //        //
//  //        //          val added = if (newNested) {
//  //        //            tpl2 :+ toAdd
//  //        //          } else {
//  //        //            tpl2.init :+ toAdd
//  //        //          }
//  //        //          added
//  //        //        }
//  //
//  //        null.asInstanceOf[NestedTpes]
//  //      }
//  //
//  //
//  //      //      def x[T: TypeTag](tpe: T, typeIndex: Int): T = tpe match {
//  //      //      def x[T: TypeTag](tpe: T, typeIndex: Int): T = typeOf[T] match {
//  //      //      def x[T: WeakTypeTag](tpe: T, typeIndex: Int)(implicit ev: TypeTag[T]): T = weakTypeOf[T] match {
//  //      //      def x[T](tpe: T, typeIndex: Int)(implicit ev: WeakTypeTag[T]): T = weakTypeOf[T] match {
//  //      def x(tpe: Type, typeIndex: Int): tpe.type = tpe match {
//  //        case t if t <:< weakTypeOf[Seq[Product]] =>
//  //          xx(2
//  //            , t
//  //            , t.typeArgs.head
//  //            , t.baseType(weakTypeOf[Seq[_]].typeSymbol)
//  //          )
//  //
//  //          resolve[tpe.type](typeIndex)
//  //          //          resolve[T](typeIndex)
//  //
//  //          //        case t if weakTypeOf[T] <:< weakTypeOf[Seq[Product]] =>
//  //          //          xx(2
//  //          //            , t
//  //          //            , t.typeArgs.head
//  //          //            , t.baseType(weakTypeOf[Seq[_]].typeSymbol)
//  //          //          )
//  //
//  //          resolve[tpe.type](typeIndex)
//  //        //          resolve[T](typeIndex)
//  //
//  //        case t if t <:< weakTypeOf[Seq[_]] =>
//  //
//  //          xx(3
//  //            , t
//  //            , t.typeArgs.head
//  //            , t.baseType(weakTypeOf[Seq[_]].typeSymbol)
//  //          )
//  //          resolve[tpe.type](typeIndex)
//  //        //          resolve[T](typeIndex)
//  //        //          val nestedTpe = t.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head
//  //        //          (shift + 1, vs :+ resolve(Seq(nestedTpe), typeIndex))
//  //
//  //        case t =>
//  //
//  //          xx(1
//  //            , t
//  //            , t.typeArgs
//  //            , tpe
//  //            , tpe.isInstanceOf[Seq[_]]
//  //            , tpe
//  //            , tpe.widen
//  //            , tpe.resultType
//  //            , tpe.dealias
//  //            , tpe.typeSymbol
//  //            , tpe
//  //            , weakTypeOf[tpe.type]
//  //            , typeOf[tpe.type]
//  //            //            , casted
//  //            //            , weakTypeOf[T]
//  //            //            , weakTypeOf[T].typeArgs
//  //            //            , weakTypeOf[T].baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs
//  //            //            , typeOf[T]
//  //            //            , typeOf[T].typeArgs
//  //          )
//  //          //          resolve[tpe.type](typeIndex)
//  //          //          cast[T](_queryE, row, indexMap(entityIndex + shift + typeIndex))
//  //
//  //          val casted = cast[tpe.type](_queryE, row, indexMap(entityIndex + shift + typeIndex))
//  //          casted
//  //      }
//  //
//  //      def y[T: TypeTag](tpe: T, typeIndex: Int): T = resolve[T](typeIndex)
//  //
//  //      val values: Tpl = (weakTypeOf[Tpl].typeArgs match {
//  //        case List(a, b, c) => (x(a, 0), x(b, 1), x(c, 2))
//  //        case List(a, b)    =>
//  //
//  //          xx(4
//  //            , a
//  //            , b
//  //          )
//  //          (x(a, 0), x(b, 1))
//  //        case List(a)       => x(a, 0)
//  //        //        case Nil           => x(typeOf[Tpl], 0)
//  //      }).asInstanceOf[Tpl]
//  //
//  //
//  //      //      val values: Tpl = (
//  //      //
//  //      //        typeOf[Tpl] match {
//  //      //          case t0 if t0 <:< weakTypeOf[Product] => t0.typeArgs match {
//  //      //            case List(a, b) => (k(a, 0), resolve(b, 1))
//  //      //            case List(a)    => resolve(a, 0)
//  //      //          }
//  //      //
//  //      //          case tpe if tpe <:< weakTypeOf[Seq[_]] =>
//  //      //
//  //      //          case tpe =>
//  //      //
//  //      //        }
//  //      //        ).asInstanceOf[Tpl]
//  //
//  //      //      val s1 = Seq(1)
//  //      //      val t1 = Tuple1(1)
//  //      //      val t2 = (2, 20)
//  //
//  //      xx(7
//  //        //        , typeOf[Tpl].typeArgs
//  //        //        , typeOf[Seq[(Int, String)]].typeArgs
//  //        //        , typeOf[Seq[(Int)]].typeArgs
//  //        //        , typeOf[Seq[Int]].typeArgs
//  //        //        , typeOf[Int].typeArgs
//  //        //        , j(typeOf[String])
//  //        //        , row
//  //        //        , row(0)
//  //        //        , row(1)
//  //        , values
//  //      )
//  //
//  //      null.asInstanceOf[Tpl]
//  //    }
//  //
//  //    val resolvedTuples = sortedRows.foldLeft((Seq.empty[OuterTpl], None: Option[OuterTpl], Seq(0L), Seq[Any](0L), 1)) { case ((accTpls0, prevTpl, prevEntities, prevRow, r), row0) =>
//  //
//  //      val row = row0.asScala.asInstanceOf[Seq[Any]]
//  //
//  //      //println("--- " + r + " ---------------------------------------------------")
//  //      val currentEntities = entityIndexes.map(i => row.apply(i).asInstanceOf[Long])
//  //      val manyRefEntities = manyRefIndexes.map(i => row.apply(i).asInstanceOf[Long])
//  //
//  //
//  //      val isLastRow = rowCount == r
//  //      val isNewTpl = prevEntities.head != 0 && currentEntities.head != prevEntities.head
//  //      val isNewManyRef = prevEntities.head != 0 && manyRefEntities.nonEmpty && !prevEntities.contains(manyRefEntities.head)
//  //      //println("currentEntities: " + currentEntities)
//  //      //println("manyRefEntities: " + manyRefEntities)
//  //      //println("TPL0 " + prevTpl + "    " + isNewTpl + "    " + isNewManyRef)
//  //
//  //      // Recurse tuples
//  //      //                    val tpl = resolveNested[Tpl](query, prevTpl, prevRow, row, 0, 1, maxDepth, 1, entityIndexes, manyRefIndexes, indexMap)
//  //      val tpl: OuterTpl = resolveNested[OuterTpl](prevTpl, prevRow, row, 0, 1, 1)
//  //
//  //      //println("TPL1 " + tpl)
//  //
//  //      val accTpls = if (isLastRow && (isNewTpl || isNewManyRef)) {
//  //        // Add previous and current tuple
//  //        //println("TPL2 " + (accTpls0 ++ Seq(prevTpl.get, tpl)).toString)
//  //        accTpls0 ++ Seq(prevTpl.get, tpl)
//  //
//  //      } else if (isLastRow) {
//  //        // Add current tuple
//  //        //println("TPL3 " + (accTpls0 :+ tpl).toString)
//  //        accTpls0 :+ tpl
//  //
//  //      } else if (isNewTpl || isNewManyRef) {
//  //        // Add finished previous tuple
//  //        //println("TPL4 " + (accTpls0 :+ prevTpl.get).toString)
//  //        accTpls0 :+ prevTpl.get
//  //
//  //      } else {
//  //        // Continue building current tuple
//  //        //println("TPL5 " + accTpls0.toString)
//  //        accTpls0
//  //      }
//  //
//  //      //println("ACC " + accTpls)
//  //
//  //      (accTpls, Some(tpl), currentEntities, row, r + 1)
//  //    }._1
//  //
//  //
//  //    resolvedTuples
//
//
//}
