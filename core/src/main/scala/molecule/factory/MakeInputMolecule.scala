package molecule.factory
import molecule._
import molecule.api._
import molecule.dsl.schemaDSL._
import molecule.ops.QueryOps._
import molecule.ops.TreeOps
import molecule.dsl._
import molecule.transform._
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context


trait MakeInputMolecule[Ctx <: Context] extends FactoryBase[Ctx] {
  import c.universe._
//  val x = Debug("BuildInputMolecule", 1, 60)


  def await_in_x_out_0(inputDsl: c.Expr[NS], InTypes: Type*) = {
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, 1)
    val InputTypes = if (InTypes.length == 1) tq"Seq[..$InTypes]" else tq"Seq[(..$InTypes)]"
    expr( q"""
      ..${basics(inputDsl)}
      new $InputMoleculeTpe[..$InTypes](model, query) {
        def apply(args: $InputTypes)(implicit conn: Connection): Molecule0 = {
          val query1 = bindValues1(args)
          new Molecule0(model, query1) {
            def getD(implicit conn: Connection): Unit = debugMolecule(conn, model, query1)
          }
        }
      }
    """)
  }

  def await_in_x_out_1(inputDsl: c.Expr[NS], A: Type, InTypes: Type*) = {
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, 1)
    val InputTypes = if (InTypes.length == 1) tq"Seq[..$InTypes]" else tq"Seq[(..$InTypes)]"
    val OutTypes2 = Seq(c.typeOf[Long], A)

    val bindValues2 = if (InTypes.size > 1) {
      val (inTerms, inParams) = InTypes.zipWithIndex.map { case (t, i) =>
        val inTerm = TermName(s"in$i")
        val InType = tq"Seq[$t]"
        (inTerm, q"$inTerm: $InType")
      }.unzip
      q"""
        def apply(..$inParams)(implicit conn: Connection): Molecule1[$A] = {
          val query2 = bindValues2(..$inTerms)
          new Molecule1[$A](model, query2) {
            protected def getE(implicit conn: Connection): Seq[(..$OutTypes2)] = results(conn, modelE, queryE).toList.map(row => (..${castTpl(q"queryE", q"row", OutTypes2)}))

            def get(implicit conn: Connection): Seq[$A] = results(conn, model, query2).toList.map(row => ${cast(q"query", q"row", A, 0)}.asInstanceOf[$A])
            def getD(implicit conn: Connection): Unit   = debugMolecule(conn, model, query2)
          }
        }
      """
    } else q""

    expr( q"""
      ..${basics(inputDsl)}
      new $InputMoleculeTpe[..$InTypes, $A](model, query) {
        def apply(args: $InputTypes)(implicit conn: Connection): Molecule1[$A] = {
          val query1 = bindValues1(args)
          new Molecule1[$A](model, query1) {
            protected def getE(implicit conn: Connection): Seq[(..$OutTypes2)] = results(conn, modelE, queryE).toList.map(row => (..${castTpl(q"queryE", q"row", OutTypes2)}))

            def get(implicit conn: Connection): Seq[$A] = results(conn, model, query1).toList.map(row => ${cast(q"query", q"row", A, 0)}.asInstanceOf[$A])
            def getD(implicit conn: Connection): Unit   = debugMolecule(conn, model, query1)
          }
        }
        $bindValues2
      }
    """)
  }

  def await(inputDsl: c.Expr[NS], InTypes: Type*)(OutTypes: Type*) = {
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, OutTypes.size)
    val MoleculeTpe = molecule_o(OutTypes.size)
    val OutTypes2 = if(OutTypes.size == 22) OutTypes else c.typeOf[Long] +: OutTypes

    val bindValues2 = if (InTypes.size > 1) {
      val (inTerms, inParams) = InTypes.zipWithIndex.map { case (t, i) =>
        val inTerm = TermName(s"in$i")
        val inType = tq"Seq[$t]"
        (inTerm, q"$inTerm: $inType")
      }.unzip
      q"""
        def apply(..$inParams)(implicit conn: Connection): $MoleculeTpe[..$OutTypes] = {
          val query2 = bindValues2(..$inTerms)
          new $MoleculeTpe[..$OutTypes](model, query2) {
            protected def getE(implicit conn: Connection): Seq[(..$OutTypes2)] = results(conn, modelE, queryE).toList.map(row => (..${castTpl(q"queryE", q"row", OutTypes2)}))

            def get(implicit conn: Connection): Seq[(..$OutTypes)] = results(conn, model, query2).toList.map(row => (..${castTpl(q"query", q"row", OutTypes)}))
            def getD(implicit conn: Connection): Unit              = debugMolecule(conn, model, query2)
          }
        }
      """
    } else q""

    expr( q"""
      ..${basics(inputDsl)}
      new $InputMoleculeTpe[..$InTypes, ..$OutTypes](model, query) {
        def apply(args: Seq[(..$InTypes)])(implicit conn: Connection): $MoleculeTpe[..$OutTypes] = {
          val query1 = bindValues1(args)
          new $MoleculeTpe[..$OutTypes](model, query1) {
            protected def getE(implicit conn: Connection): Seq[(..$OutTypes2)] = results(conn, modelE, queryE).toList.map(row => (..${castTpl(q"queryE", q"row", OutTypes2)}))

            def get(implicit conn: Connection): Seq[(..$OutTypes)] = results(conn, model, query1).toList.map(row => (..${castTpl(q"query", q"row", OutTypes)}))
            def getD(implicit conn: Connection): Unit              = debugMolecule(conn, model, query1)
          }
        }
        $bindValues2
      }
    """)
  }
}

object MakeInputMolecule {
  def inst(c0: Context) = new {val c: c0.type = c0} with MakeInputMolecule[c0.type]

  // Input molecules with 0 output (update templates)

  def await_1_0[In1_0[_], In1_1[_, _], In2_0[_, _], In2_1[_, _, _], I1: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_0[In1_0, In1_1, In2_0, In2_1, I1]])
  : c.Expr[InputMolecule_1_0[I1]] =
    inst(c).await_in_x_out_0(inputDsl, c.weakTypeOf[I1])

  def await_2_0[In2_0[_, _], In2_1[_, _, _], In3_0[_, _, _], In3_1[_, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_0[In2_0, In2_1, In3_0, In3_1, I1, I2]])
  : c.Expr[InputMolecule_2_0[I1, I2]] =
    inst(c).await_in_x_out_0(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])

  def await_3_0[In3_0[_, _, _], In3_1[_, _, _, _], In4_0[_, _, _, _], In4_1[_, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_0[In3_0, In3_1, In4_0, In4_1, I1, I2, I3]])
  : c.Expr[InputMolecule_3_0[I1, I2, I3]] =
    inst(c).await_in_x_out_0(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])


  // Input molecules with 1 output (have simplified api)

  def await_1_1[In1_1[_, _], In1_2[_, _, _], In2_1[_, _, _], In2_2[_, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_1[In1_1, In1_2, In2_1, In2_2, I1, A]])
  : c.Expr[InputMolecule_1_1[I1, A]] =
    inst(c).await_in_x_out_1(inputDsl, c.weakTypeOf[A], c.weakTypeOf[I1])

  def await_2_1[In2_1[_, _, _], In2_2[_, _, _, _], In3_1[_, _, _, _], In3_2[_, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_1[In2_1, In2_2, In3_1, In3_2, I1, I2, A]])
  : c.Expr[InputMolecule_2_1[I1, I2, A]] =
    inst(c).await_in_x_out_1(inputDsl, c.weakTypeOf[A], c.weakTypeOf[I1], c.weakTypeOf[I2])

  def await_3_1[In3_1[_, _, _, _], In3_2[_, _, _, _, _], In4_1[_, _, _, _, _], In4_2[_, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_1[In3_1, In3_2, In4_1, In4_2, I1, I2, I3, A]])
  : c.Expr[InputMolecule_3_1[I1, I2, I3, A]] =
    inst(c).await_in_x_out_1(inputDsl, c.weakTypeOf[A], c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])


  // 1 input - multiple outputs

  def await_1_2[In1_2[_, _, _], In1_3[_, _, _, _], In2_2[_, _, _, _], In2_3[_, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_2[In1_2, In1_3, In2_2, In2_3, I1, A, B]])
  : c.Expr[InputMolecule_1_2[I1, A, B]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B])

  def await_1_3[In1_3[_, _, _, _], In1_4[_, _, _, _, _], In2_3[_, _, _, _, _], In2_4[_, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_3[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]])
  : c.Expr[InputMolecule_1_3[I1, A, B, C]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C])

  def await_1_4[In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], In2_4[_, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_4[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]])
  : c.Expr[InputMolecule_1_4[I1, A, B, C, D]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D])

  def await_1_5[In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_5[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]])
  : c.Expr[InputMolecule_1_5[I1, A, B, C, D, E]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E])

  def await_1_6[In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_6[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]])
  : c.Expr[InputMolecule_1_6[I1, A, B, C, D, E, F]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F])

  def await_1_7[In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_7[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]])
  : c.Expr[InputMolecule_1_7[I1, A, B, C, D, E, F, G]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G])

  def await_1_8[In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_8[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]])
  : c.Expr[InputMolecule_1_8[I1, A, B, C, D, E, F, G, H]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H])

  def await_1_9[In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_9[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]])
  : c.Expr[InputMolecule_1_9[I1, A, B, C, D, E, F, G, H, I]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I])

  def await_1_10[In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_10[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]])
  : c.Expr[InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J])

  def await_1_11[In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_11[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]])
  : c.Expr[InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K])

  def await_1_12[In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_12[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]])
  : c.Expr[InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L])

  def await_1_13[In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_13[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]])
  : c.Expr[InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M])

  def await_1_14[In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_14[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]])
  : c.Expr[InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N])

  def await_1_15[In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_15[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]])
  : c.Expr[InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O])

  def await_1_16[In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_16[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]])
  : c.Expr[InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P])

  def await_1_17[In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_17[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]])
  : c.Expr[InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q])

  def await_1_18[In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_18[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]])
  : c.Expr[InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R])

  def await_1_19[In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_19[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]])
  : c.Expr[InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S])

  def await_1_20[In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_20[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]])
  : c.Expr[InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T])

  def await_1_21[In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_21[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]])
  : c.Expr[InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U])

  def await_1_22[In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag, V: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_1_22[In1_22, In1_23, In2_22, In2_23, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]])
  : c.Expr[InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U], c.weakTypeOf[V])


  // 2 inputs - multiple outputs

  def await_2_2[In2_2[_, _, _, _], In2_3[_, _, _, _, _], In3_2[_, _, _, _, _], In3_3[_, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_2[In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]])
  : c.Expr[InputMolecule_2_2[I1, I2, A, B]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B])

  def await_2_3[In2_3[_, _, _, _, _], In2_4[_, _, _, _, _, _], In3_3[_, _, _, _, _, _], In3_4[_, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_3[In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]])
  : c.Expr[InputMolecule_2_3[I1, I2, A, B, C]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C])

  def await_2_4[In2_4[_, _, _, _, _, _], In2_5[_, _, _, _, _, _, _], In3_4[_, _, _, _, _, _, _], In3_5[_, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_4[In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]])
  : c.Expr[InputMolecule_2_4[I1, I2, A, B, C, D]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D])

  def await_2_5[In2_5[_, _, _, _, _, _, _], In2_6[_, _, _, _, _, _, _, _], In3_5[_, _, _, _, _, _, _, _], In3_6[_, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_5[In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]])
  : c.Expr[InputMolecule_2_5[I1, I2, A, B, C, D, E]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E])

  def await_2_6[In2_6[_, _, _, _, _, _, _, _], In2_7[_, _, _, _, _, _, _, _, _], In3_6[_, _, _, _, _, _, _, _, _], In3_7[_, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_6[In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]])
  : c.Expr[InputMolecule_2_6[I1, I2, A, B, C, D, E, F]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F])

  def await_2_7[In2_7[_, _, _, _, _, _, _, _, _], In2_8[_, _, _, _, _, _, _, _, _, _], In3_7[_, _, _, _, _, _, _, _, _, _], In3_8[_, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_7[In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]])
  : c.Expr[InputMolecule_2_7[I1, I2, A, B, C, D, E, F, G]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G])

  def await_2_8[In2_8[_, _, _, _, _, _, _, _, _, _], In2_9[_, _, _, _, _, _, _, _, _, _, _], In3_8[_, _, _, _, _, _, _, _, _, _, _], In3_9[_, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_8[In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]])
  : c.Expr[InputMolecule_2_8[I1, I2, A, B, C, D, E, F, G, H]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H])

  def await_2_9[In2_9[_, _, _, _, _, _, _, _, _, _, _], In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In3_9[_, _, _, _, _, _, _, _, _, _, _, _], In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_9[In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]])
  : c.Expr[InputMolecule_2_9[I1, I2, A, B, C, D, E, F, G, H, I]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I])

  def await_2_10[In2_10[_, _, _, _, _, _, _, _, _, _, _, _], In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_10[In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]])
  : c.Expr[InputMolecule_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J])

  def await_2_11[In2_11[_, _, _, _, _, _, _, _, _, _, _, _, _], In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_11[In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]])
  : c.Expr[InputMolecule_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K])

  def await_2_12[In2_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_12[In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]])
  : c.Expr[InputMolecule_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L])

  def await_2_13[In2_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_13[In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]])
  : c.Expr[InputMolecule_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M])

  def await_2_14[In2_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_14[In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]])
  : c.Expr[InputMolecule_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N])

  def await_2_15[In2_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_15[In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]])
  : c.Expr[InputMolecule_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O])

  def await_2_16[In2_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_16[In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]])
  : c.Expr[InputMolecule_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P])

  def await_2_17[In2_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_17[In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]])
  : c.Expr[InputMolecule_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q])

  def await_2_18[In2_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_18[In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]])
  : c.Expr[InputMolecule_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R])

  def await_2_19[In2_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_19[In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]])
  : c.Expr[InputMolecule_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S])

  def await_2_20[In2_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_20[In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]])
  : c.Expr[InputMolecule_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T])

  def await_2_21[In2_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_21[In2_21, In2_22, In3_21, In3_22, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]])
  : c.Expr[InputMolecule_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U])

  def await_2_22[In2_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In2_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag, V: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_2_22[In2_22, In2_23, In3_22, In3_23, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]])
  : c.Expr[InputMolecule_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U], c.weakTypeOf[V])


  // 3 inputs - multiple outputs

  def await_3_2[In3_2[_, _, _, _, _], In3_3[_, _, _, _, _, _], In4_2[_, _, _, _, _, _], In4_3[_, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_2[In3_2, In3_3, In4_2, In4_3, I1, I2, I3, A, B]])
  : c.Expr[InputMolecule_3_2[I1, I2, I3, A, B]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B])

  def await_3_3[In3_3[_, _, _, _, _, _], In3_4[_, _, _, _, _, _, _], In4_3[_, _, _, _, _, _, _], In4_4[_, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_3[In3_3, In3_4, In4_3, In4_4, I1, I2, I3, A, B, C]])
  : c.Expr[InputMolecule_3_3[I1, I2, I3, A, B, C]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C])

  def await_3_4[In3_4[_, _, _, _, _, _, _], In3_5[_, _, _, _, _, _, _, _], In4_4[_, _, _, _, _, _, _, _], In4_5[_, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_4[In3_4, In3_5, In4_4, In4_5, I1, I2, I3, A, B, C, D]])
  : c.Expr[InputMolecule_3_4[I1, I2, I3, A, B, C, D]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D])

  def await_3_5[In3_5[_, _, _, _, _, _, _, _], In3_6[_, _, _, _, _, _, _, _, _], In4_5[_, _, _, _, _, _, _, _, _], In4_6[_, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_5[In3_5, In3_6, In4_5, In4_6, I1, I2, I3, A, B, C, D, E]])
  : c.Expr[InputMolecule_3_5[I1, I2, I3, A, B, C, D, E]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E])

  def await_3_6[In3_6[_, _, _, _, _, _, _, _, _], In3_7[_, _, _, _, _, _, _, _, _, _], In4_6[_, _, _, _, _, _, _, _, _, _], In4_7[_, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_6[In3_6, In3_7, In4_6, In4_7, I1, I2, I3, A, B, C, D, E, F]])
  : c.Expr[InputMolecule_3_6[I1, I2, I3, A, B, C, D, E, F]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F])

  def await_3_7[In3_7[_, _, _, _, _, _, _, _, _, _], In3_8[_, _, _, _, _, _, _, _, _, _, _], In4_7[_, _, _, _, _, _, _, _, _, _, _], In4_8[_, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_7[In3_7, In3_8, In4_7, In4_8, I1, I2, I3, A, B, C, D, E, F, G]])
  : c.Expr[InputMolecule_3_7[I1, I2, I3, A, B, C, D, E, F, G]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G])

  def await_3_8[In3_8[_, _, _, _, _, _, _, _, _, _, _], In3_9[_, _, _, _, _, _, _, _, _, _, _, _], In4_8[_, _, _, _, _, _, _, _, _, _, _, _], In4_9[_, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_8[In3_8, In3_9, In4_8, In4_9, I1, I2, I3, A, B, C, D, E, F, G, H]])
  : c.Expr[InputMolecule_3_8[I1, I2, I3, A, B, C, D, E, F, G, H]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H])

  def await_3_9[In3_9[_, _, _, _, _, _, _, _, _, _, _, _], In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _], In4_9[_, _, _, _, _, _, _, _, _, _, _, _, _], In4_10[_, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_9[In3_9, In3_10, In4_9, In4_10, I1, I2, I3, A, B, C, D, E, F, G, H, I]])
  : c.Expr[InputMolecule_3_9[I1, I2, I3, A, B, C, D, E, F, G, H, I]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I])

  def await_3_10[In3_10[_, _, _, _, _, _, _, _, _, _, _, _, _], In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_10[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_10[In3_10, In3_11, In4_10, In4_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]])
  : c.Expr[InputMolecule_3_10[I1, I2, I3, A, B, C, D, E, F, G, H, I, J]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J])

  def await_3_11[In3_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_11[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_11[In3_11, In3_12, In4_11, In4_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]])
  : c.Expr[InputMolecule_3_11[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K])

  def await_3_12[In3_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_12[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_12[In3_12, In3_13, In4_12, In4_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]])
  : c.Expr[InputMolecule_3_12[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L])

  def await_3_13[In3_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_13[In3_13, In3_14, In4_13, In4_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]])
  : c.Expr[InputMolecule_3_13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M])

  def await_3_14[In3_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_14[In3_14, In3_15, In4_14, In4_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]])
  : c.Expr[InputMolecule_3_14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N])

  def await_3_15[In3_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_15[In3_15, In3_16, In4_15, In4_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]])
  : c.Expr[InputMolecule_3_15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O])

  def await_3_16[In3_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_16[In3_16, In3_17, In4_16, In4_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]])
  : c.Expr[InputMolecule_3_16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P])

  def await_3_17[In3_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_17[In3_17, In3_18, In4_17, In4_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]])
  : c.Expr[InputMolecule_3_17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q])

  def await_3_18[In3_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_18[In3_18, In3_19, In4_18, In4_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]])
  : c.Expr[InputMolecule_3_18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R])

  def await_3_19[In3_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_19[In3_19, In3_20, In4_19, In4_20, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]])
  : c.Expr[InputMolecule_3_19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S])

  def await_3_20[In3_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_20[In3_20, In3_21, In4_20, In4_21, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]])
  : c.Expr[InputMolecule_3_20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T])

  def await_3_21[In3_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_21[In3_21, In3_22, In4_21, In4_22, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]])
  : c.Expr[InputMolecule_3_21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U])

  def await_3_22[In3_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In3_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In4_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], I1: c.WeakTypeTag, I2: c.WeakTypeTag, I3: c.WeakTypeTag, A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag, V: c.WeakTypeTag]
  (c: Context)(inputDsl: c.Expr[In_3_22[In3_22, In3_23, In4_22, In4_23, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]])
  : c.Expr[InputMolecule_3_22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]] =
    inst(c).await(inputDsl, c.weakTypeOf[I1], c.weakTypeOf[I2], c.weakTypeOf[I3])(c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U], c.weakTypeOf[V])
}