package molecule.macros
import molecule.action.Molecule._
import molecule.boilerplate.base.NS
import molecule.boilerplate.out._
import molecule.composition.Composite._
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

/** Macro to make molecules. */
trait MakeMolecule[Ctx <: Context] extends GetTuples[Ctx] {
  import c.universe._

  def generateMolecule(dsl: c.Expr[NS], OutTypes: Type*) = {
    val MoleculeTpe = molecule_o(OutTypes.size)
    val (model, basicsTree) = basics(dsl)
    val isNested = model.elements.collectFirst {
      case nested: molecule.ast.model.Nested if !nested.bond.ns.isEmpty => true
    } getOrElse false

    if (isNested) {
      expr(
        q"""
          ..$basicsTree
          new $MoleculeTpe[..$OutTypes](r.model, r.query) with Util {
            val _modelE = r.modelE
            val _queryE = r.queryE

            override def getRaw(implicit conn: Conn): jCollection[jList[AnyRef]] = conn.query(_model, _query)

            override def getIterable(implicit conn: Conn): Iterable[(..$OutTypes)] = ${nestedTuples(q"_queryE", q"conn.query(_modelE, _queryE).asScala", OutTypes)}

            import molecule.macros.NestedJson
            override def getJson        (implicit conn: Conn): String = NestedJson(_modelE, _queryE).nestedJson(conn.query(_modelE, _queryE).asScala)
            override def getJson(n: Int)(implicit conn: Conn): String = NestedJson(_modelE, _queryE).nestedJson(conn.query(_modelE, _queryE).asScala.take(n))

            override def debugGet(implicit conn: Conn) = debugGet_(conn)
          }
        """
      )
    } else {
      expr(
        q"""
          ..$basicsTree
          new $MoleculeTpe[..$OutTypes](r.model, r.query) with Util {

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
                  (..${tuple(q"_query", q"row", OutTypes)})
                }
              }
            }
            override def getRaw(implicit conn: Conn): jCollection[jList[AnyRef]] = conn.query(_model, _query)

            override def getJson        (implicit conn: Conn): String = ${json(q"_model", q"_query", q"conn.query(_model, _query).asScala", OutTypes)}
            override def getJson(n: Int)(implicit conn: Conn): String = ${json(q"_model", q"_query", q"conn.query(_model, _query).asScala.take(n)", OutTypes)}

            override def debugGet(implicit conn: Conn) = debugGet_(conn)
          }
        """
      )
    }
  }
}

/** Macro calls of arity 1-22 to build molecules. */
object MakeMolecule {

  def build(c0: Context) = new {val c: c0.type = c0} with MakeMolecule[c0.type]


  // Molecules ....................................................

  def from1attr[Ns1[_], Ns2[_, _], In1_1[_, _], In1_2[_, _, _],
  A: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_1[Ns1, Ns2, In1_1, In1_2, A]])
  : c.Expr[Molecule01[A]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A])


  def from2attr[Ns2[_, _], Ns3[_, _, _], In1_2[_, _, _], In1_3[_, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_2[Ns2, Ns3, In1_2, In1_3, A, B]])
  : c.Expr[Molecule02[A, B]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B])


  def from3attr[Ns3[_, _, _], Ns4[_, _, _, _], In1_3[_, _, _, _], In1_4[_, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_3[Ns3, Ns4, In1_3, In1_4, A, B, C]])
  : c.Expr[Molecule03[A, B, C]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C])


  def from4attr[Ns4[_, _, _, _], Ns5[_, _, _, _, _], In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_4[Ns4, Ns5, In1_4, In1_5, A, B, C, D]])
  : c.Expr[Molecule04[A, B, C, D]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D])


  def from5attr[Ns5[_, _, _, _, _], Ns6[_, _, _, _, _, _], In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_5[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]])
  : c.Expr[Molecule05[A, B, C, D, E]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E])


  def from6attr[Ns6[_, _, _, _, _, _], Ns7[_, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_6[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]])
  : c.Expr[Molecule06[A, B, C, D, E, F]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F])


  def from7attr[Ns7[_, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_7[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]])
  : c.Expr[Molecule07[A, B, C, D, E, F, G]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G])


  def from8attr[Ns8[_, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_8[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]])
  : c.Expr[Molecule08[A, B, C, D, E, F, G, H]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H])


  def from9attr[Ns9[_, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_9[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]])
  : c.Expr[Molecule09[A, B, C, D, E, F, G, H, I]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I])


  def from10attr[Ns10[_, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_10[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]])
  : c.Expr[Molecule10[A, B, C, D, E, F, G, H, I, J]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J])

  def from11attr[Ns11[_, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_11[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]])
  : c.Expr[Molecule11[A, B, C, D, E, F, G, H, I, J, K]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K])


  def from12attr[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_12[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]])
  : c.Expr[Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L])


  def from13attr[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_13[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]])
  : c.Expr[Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M])


  def from14attr[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_14[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]])
  : c.Expr[Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N])


  def from15attr[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_15[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]])
  : c.Expr[Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O])


  def from16attr[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_16[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]])
  : c.Expr[Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P])


  def from17attr[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_17[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]])
  : c.Expr[Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q])


  def from18attr[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_18[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]])
  : c.Expr[Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R])


  def from19attr[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_19[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]])
  : c.Expr[Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S])


  def from20attr[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_20[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]])
  : c.Expr[Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T])


  def from21attr[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_21[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]])
  : c.Expr[Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U])


  def from22attr[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag, V: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_22[Ns22, Ns23, In1_22, In1_23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]])
  : c.Expr[Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]] =
    build(c).generateMolecule(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U], c.weakTypeOf[V])

}