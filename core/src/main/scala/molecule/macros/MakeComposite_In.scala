package molecule.macros
import molecule.boilerplate.base.NS
import molecule.composition.Composite_In_1._
import molecule.composition.Composite_In_2._
import molecule.composition.Composite_In_3._
import molecule.input.InputMolecule_1._
import molecule.input.InputMolecule_2._
import molecule.input.InputMolecule_3._
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

/** Macro to make composite input molecules. */
trait MakeComposite_In[Ctx <: Context] extends GetTuples[Ctx] {
  import c.universe._

  def generateComposite_In_Molecule(inputDsl: c.Expr[NS], InTypes: Type*)(OutTypes: Type*) = {
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, OutTypes.size)
    val MoleculeTpe = molecule_o(OutTypes.size)

    val applySeqs = InTypes match {
      case Seq(it0, it1, it2) =>
        val (i0, i1, i2) = (TermName(s"in0"), TermName(s"in1"), TermName(s"in2"))
        val (t0, t1, t2) = (tq"Seq[$it0]", tq"Seq[$it1]", tq"Seq[$it2]")
        val (inParams, inTerm1, inTerm2, inTerm3) = (Seq(q"$i0: $t0", q"$i1: $t1", q"$i2: $t2"), i0, i1, i2)
        q""

      case Seq(it0, it1) =>
        val (i0, i1) = (TermName(s"in0"), TermName(s"in1"))
        val (t0, t1) = (tq"Seq[$it0]", tq"Seq[$it1]")
        val (inParams, inTerm1, inTerm2) = (Seq(q"$i0: $t0", q"$i1: $t1"), i0, i1)
        q"""
          // Apply separate lists of input
          def apply(..$inParams)(implicit conn: Conn): $MoleculeTpe[..$OutTypes] = {
            def query2 = bindSeqs(_query, $inTerm1, $inTerm2)

            new $MoleculeTpe[..$OutTypes](_model, query2) with Util {

              override def getIterable(implicit conn: Conn): Iterable[(..$OutTypes)] = new Iterable[(..$OutTypes)] {
                private val jColl: jCollection[jList[AnyRef]] = conn.query(_model, _query)
                override def isEmpty = jColl.isEmpty
                override def size = jColl.size
                override def iterator = new Iterator[(..$OutTypes)] {
                  private val jIter: jIterator[jList[AnyRef]] = jColl.iterator
                  override def hasNext = jIter.hasNext
                  private var row: jList[AnyRef] = null
                  override def next() = {
                    row = jIter.next()
                    (..${compositeTuple(q"_query", q"row", OutTypes)})
                  }
                }
              }
              override def getRaw(implicit conn: Conn): jCollection[jList[AnyRef]] = conn.query(_model, _query)

              override def getJson        (implicit conn: Conn): String = ${compositeJson(q"_model", q"_query", q"conn.query(_model, _query).asScala", OutTypes)}
              override def getJson(n: Int)(implicit conn: Conn): String = ${compositeJson(q"_model", q"_query", q"conn.query(_model, _query).asScala.take(n)", OutTypes)}

              override def debugGet(implicit conn: Conn) = debugGet_(conn)
            }
          }
        """

      case _             => q""
    }

    expr(
      q"""
        ..${basics(inputDsl)._2}
        new $InputMoleculeTpe[..$InTypes, ..$OutTypes](r.model, r.query) {
          private val _modelE = r.modelE
          private val _queryE = r.queryE

          def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $MoleculeTpe[..$OutTypes] = {
            val query1 = bindValues(_query, args)

            new $MoleculeTpe[..$OutTypes](_model, query1) with Util {

              override def getIterable(implicit conn: Conn): Iterable[(..$OutTypes)] = new Iterable[(..$OutTypes)] {
                private val jColl: jCollection[jList[AnyRef]] = conn.query(_model, _query)
                override def isEmpty = jColl.isEmpty
                override def size = jColl.size
                override def iterator = new Iterator[(..$OutTypes)] {
                  private val jIter: jIterator[jList[AnyRef]] = jColl.iterator
                  override def hasNext = jIter.hasNext
                  private var row: jList[AnyRef] = null
                  override def next() = {
                    row = jIter.next()
                    (..${compositeTuple(q"_query", q"row", OutTypes)})
                  }
                }
              }
              override def getRaw(implicit conn: Conn): jCollection[jList[AnyRef]] = conn.query(_model, _query)

              override def getJson        (implicit conn: Conn): String = ${compositeJson(q"_model", q"_query", q"conn.query(_model, _query).asScala", OutTypes)}
              override def getJson(n: Int)(implicit conn: Conn): String = ${compositeJson(q"_model", q"_query", q"conn.query(_model, _query).asScala.take(n)", OutTypes)}

              override def debugGet(implicit conn: Conn) = debugGet_(conn)
            }
          }

          $applySeqs
        }
      """)
  }
}


/** Macro calls of arity 1-22 for 1, 2 and 3 inputs to build composite input molecules. */
object MakeComposite_In {

  def build(c0: Context) = new {val c: c0.type = c0} with MakeComposite_In[c0.type]

  // Composite molecules ....................................................


  def await_1_1[I1: c.WeakTypeTag, T1: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_01[I1, T1]])
  : c.Expr[InputMolecule_1_01[I1, T1]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1])

  def await_1_2[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_02[I1, T1, T2]])
  : c.Expr[InputMolecule_1_02[I1, T1, T2]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2])

  def await_1_3[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_03[I1, T1, T2, T3]])
  : c.Expr[InputMolecule_1_03[I1, T1, T2, T3]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3])

  def await_1_4[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_04[I1, T1, T2, T3, T4]])
  : c.Expr[InputMolecule_1_04[I1, T1, T2, T3, T4]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4])

  def await_1_5[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_05[I1, T1, T2, T3, T4, T5]])
  : c.Expr[InputMolecule_1_05[I1, T1, T2, T3, T4, T5]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5])

  def await_1_6[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_06[I1, T1, T2, T3, T4, T5, T6]])
  : c.Expr[InputMolecule_1_06[I1, T1, T2, T3, T4, T5, T6]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6])

  def await_1_7[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_07[I1, T1, T2, T3, T4, T5, T6, T7]])
  : c.Expr[InputMolecule_1_07[I1, T1, T2, T3, T4, T5, T6, T7]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7])

  def await_1_8[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_08[I1, T1, T2, T3, T4, T5, T6, T7, T8]])
  : c.Expr[InputMolecule_1_08[I1, T1, T2, T3, T4, T5, T6, T7, T8]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8])

  def await_1_9[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_09[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]])
  : c.Expr[InputMolecule_1_09[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9])

  def await_1_10[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_10[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]])
  : c.Expr[InputMolecule_1_10[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10])

  def await_1_11[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_11[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]])
  : c.Expr[InputMolecule_1_11[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11])

  def await_1_12[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_12[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]])
  : c.Expr[InputMolecule_1_12[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12])

  def await_1_13[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_13[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]])
  : c.Expr[InputMolecule_1_13[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13])

  def await_1_14[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_14[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]])
  : c.Expr[InputMolecule_1_14[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14])

  def await_1_15[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_15[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]])
  : c.Expr[InputMolecule_1_15[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15])

  def await_1_16[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_16[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]])
  : c.Expr[InputMolecule_1_16[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16])

  def await_1_17[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_17[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]])
  : c.Expr[InputMolecule_1_17[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17])

  def await_1_18[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_18[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]])
  : c.Expr[InputMolecule_1_18[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18])

  def await_1_19[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_19[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]])
  : c.Expr[InputMolecule_1_19[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19])

  def await_1_20[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_20[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]])
  : c.Expr[InputMolecule_1_20[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20])

  def await_1_21[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_21[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]])
  : c.Expr[InputMolecule_1_21[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21])

  def await_1_22[I1: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, T22: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_1_22[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]])
  : c.Expr[InputMolecule_1_22[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21], c.weakTypeOf[T22])



  def await_2_1[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_01[I1, I2, T1]])
  : c.Expr[InputMolecule_2_01[I1, I2, T1]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1])

  def await_2_2[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_02[I1, I2, T1, T2]])
  : c.Expr[InputMolecule_2_02[I1, I2, T1, T2]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2])

  def await_2_3[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_03[I1, I2, T1, T2, T3]])
  : c.Expr[InputMolecule_2_03[I1, I2, T1, T2, T3]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3])

  def await_2_4[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_04[I1, I2, T1, T2, T3, T4]])
  : c.Expr[InputMolecule_2_04[I1, I2, T1, T2, T3, T4]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4])

  def await_2_5[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]])
  : c.Expr[InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5])

  def await_2_6[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]])
  : c.Expr[InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6])

  def await_2_7[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]])
  : c.Expr[InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7])

  def await_2_8[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]])
  : c.Expr[InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8])

  def await_2_9[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]])
  : c.Expr[InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9])

  def await_2_10[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]])
  : c.Expr[InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10])

  def await_2_11[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]])
  : c.Expr[InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11])

  def await_2_12[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]])
  : c.Expr[InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12])

  def await_2_13[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]])
  : c.Expr[InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13])

  def await_2_14[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]])
  : c.Expr[InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14])

  def await_2_15[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]])
  : c.Expr[InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15])

  def await_2_16[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]])
  : c.Expr[InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16])

  def await_2_17[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]])
  : c.Expr[InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17])

  def await_2_18[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]])
  : c.Expr[InputMolecule_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18])

  def await_2_19[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]])
  : c.Expr[InputMolecule_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19])

  def await_2_20[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]])
  : c.Expr[InputMolecule_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20])

  def await_2_21[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]])
  : c.Expr[InputMolecule_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21])

  def await_2_22[I1: c.WeakTypeTag, I2: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, T22: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_2_22[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]])
  : c.Expr[InputMolecule_2_22[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21], c.weakTypeOf[T22])


  def await_3_1[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_01[I1, I2, I3, T1]])
  : c.Expr[InputMolecule_3_01[I1, I2, I3, T1]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1])

  def await_3_2[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_02[I1, I2, I3, T1, T2]])
  : c.Expr[InputMolecule_3_02[I1, I2, I3, T1, T2]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2])

  def await_3_3[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_03[I1, I2, I3, T1, T2, T3]])
  : c.Expr[InputMolecule_3_03[I1, I2, I3, T1, T2, T3]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3])

  def await_3_4[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_04[I1, I2, I3, T1, T2, T3, T4]])
  : c.Expr[InputMolecule_3_04[I1, I2, I3, T1, T2, T3, T4]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4])

  def await_3_5[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_05[I1, I2, I3, T1, T2, T3, T4, T5]])
  : c.Expr[InputMolecule_3_05[I1, I2, I3, T1, T2, T3, T4, T5]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5])

  def await_3_6[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_06[I1, I2, I3, T1, T2, T3, T4, T5, T6]])
  : c.Expr[InputMolecule_3_06[I1, I2, I3, T1, T2, T3, T4, T5, T6]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6])

  def await_3_7[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_07[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]])
  : c.Expr[InputMolecule_3_07[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7])

  def await_3_8[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_08[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]])
  : c.Expr[InputMolecule_3_08[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8])

  def await_3_9[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_09[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]])
  : c.Expr[InputMolecule_3_09[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9])

  def await_3_10[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_10[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]])
  : c.Expr[InputMolecule_3_10[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10])

  def await_3_11[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_11[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]])
  : c.Expr[InputMolecule_3_11[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11])

  def await_3_12[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_12[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]])
  : c.Expr[InputMolecule_3_12[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12])

  def await_3_13[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_13[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]])
  : c.Expr[InputMolecule_3_13[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13])

  def await_3_14[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_14[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]])
  : c.Expr[InputMolecule_3_14[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14])

  def await_3_15[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_15[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]])
  : c.Expr[InputMolecule_3_15[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15])

  def await_3_16[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_16[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]])
  : c.Expr[InputMolecule_3_16[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16])

  def await_3_17[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_17[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]])
  : c.Expr[InputMolecule_3_17[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17])

  def await_3_18[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_18[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]])
  : c.Expr[InputMolecule_3_18[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18])

  def await_3_19[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_19[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]])
  : c.Expr[InputMolecule_3_19[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19])

  def await_3_20[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_20[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]])
  : c.Expr[InputMolecule_3_20[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20])

  def await_3_21[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_21[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]])
  : c.Expr[InputMolecule_3_21[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21])

  def await_3_22[I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, T22: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[Composite_In_3_22[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]])
  : c.Expr[InputMolecule_3_22[I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]] =
    build(c).generateComposite_In_Molecule(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21], c.weakTypeOf[T22])



}