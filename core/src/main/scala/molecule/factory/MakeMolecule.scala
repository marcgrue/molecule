package molecule.factory
import molecule.api._
import molecule.boilerplate._

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context


trait MakeMolecule[Ctx <: Context] extends GetTuples[Ctx] {
  import c.universe._


  def from0attr(dsl: c.Expr[NS]) = {
    expr(
      q"""
      ..${basics(dsl)._2}
      new molecule.api.Molecule0(r.model, r.query) with Util
    """)
  }

  def fromXattrs(dsl: c.Expr[NS], OutTypes: Type*) = {
    val MoleculeTpe = molecule_o(OutTypes.size)
    val (model, basicsTree) = basics(dsl)

    model.elements.collectFirst {
      case nested: molecule.ast.model.Nested if nested.bond.ns.nonEmpty => {
        expr(
          q"""
            ..$basicsTree
            new $MoleculeTpe[..$OutTypes](r.model, r.query) with Util {
              val _modelE = r.modelE
              val _queryE = r.queryE

              private def nestedTuples(n: Int) = ${nestedTuples(q"_queryE", q"conn.query(_modelE, _queryE, n)", OutTypes)}
              def get        (implicit conn: Conn): Iterable[(..$OutTypes)] = nestedTuples(-1) // All
              def get(n: Int)(implicit conn: Conn): Iterable[(..$OutTypes)] = nestedTuples(n)

              import molecule.factory.JsonBaseNested
              private def json(n: Int): String = JsonBaseNested(_modelE, _queryE).nestedJson(conn.query(_modelE, _queryE, n))
              def getJson        (implicit conn: Conn): String = json(-1) // All
              def getJson(n: Int)(implicit conn: Conn): String = json(n)
            }
          """
        )
      }
    } getOrElse {
      expr(
        q"""
          ..$basicsTree
          new $MoleculeTpe[..$OutTypes](r.model, r.query) with Util {
            private def getTuple(row: jList[AnyRef]) = (..${tuple(q"_query", q"row", OutTypes)})
            def get        (implicit conn: Conn): Iterable[(..$OutTypes)] = conn.query(_model, _query   ).map(row => getTuple(row))
            def get(n: Int)(implicit conn: Conn): Iterable[(..$OutTypes)] = conn.query(_model, _query, n).map(row => getTuple(row))
            
            private def json(n: Int): String = ${json(q"_model", q"_query", q"conn.query(_model, _query, n)", OutTypes)}
            def getJson        (implicit conn: Conn): String = json(-1) // All
            def getJson(n: Int)(implicit conn: Conn): String = json(n)
          }
        """
      )
    }
  }

  def fromXtuples(dsl: c.Expr[NS], OutTypes: Type*) = {
    val MoleculeTpe = molecule_o(OutTypes.size)
    expr(
      q"""
        ..${basics(dsl)._2}
        new $MoleculeTpe[..$OutTypes](r.model, r.query) with Util {
          private def compositeTuple(row: jList[AnyRef]) = (..${compositeTuple(q"_query", q"row", OutTypes)})
          def get        (implicit conn: Conn): Iterable[(..$OutTypes)] = conn.query(_model, _query   ).map(row => compositeTuple(row))
          def get(n: Int)(implicit conn: Conn): Iterable[(..$OutTypes)] = conn.query(_model, _query, n).map(row => compositeTuple(row))

          def getJson        (implicit conn: Conn): String = throw new IllegalArgumentException("`getJson` not (yet) implemented for composite data structures.")
          def getJson(n: Int)(implicit conn: Conn): String = throw new IllegalArgumentException("`getJson` not (yet) implemented for composite data structures.")
        }
      """
    )
  }
}

object MakeMolecule {

  def build(c0: Context) = new {val c: c0.type = c0} with MakeMolecule[c0.type]


  def from0attr[Ns0: c.WeakTypeTag, Ns1[_], In1_0[_], In1_1[_, _]]
  (c: Context)(dsl: c.Expr[Out_0[Ns0, Ns1, In1_0, In1_1]])
  : c.Expr[Molecule0] =
    build(c).from0attr(dsl)


  def from1attr[Ns1[_], Ns2[_, _], In1_1[_, _], In1_2[_, _, _],
  A: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_1[Ns1, Ns2, In1_1, In1_2, A]])
  : c.Expr[Molecule1[A]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A])


  def from2attr[Ns2[_, _], Ns3[_, _, _], In1_2[_, _, _], In1_3[_, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_2[Ns2, Ns3, In1_2, In1_3, A, B]])
  : c.Expr[Molecule2[A, B]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B])


  def from3attr[Ns3[_, _, _], Ns4[_, _, _, _], In1_3[_, _, _, _], In1_4[_, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_3[Ns3, Ns4, In1_3, In1_4, A, B, C]])
  : c.Expr[Molecule3[A, B, C]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C])


  def from4attr[Ns4[_, _, _, _], Ns5[_, _, _, _, _], In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_4[Ns4, Ns5, In1_4, In1_5, A, B, C, D]])
  : c.Expr[Molecule4[A, B, C, D]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D])


  def from5attr[Ns5[_, _, _, _, _], Ns6[_, _, _, _, _, _], In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_5[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]])
  : c.Expr[Molecule5[A, B, C, D, E]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E])


  def from6attr[Ns6[_, _, _, _, _, _], Ns7[_, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_6[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]])
  : c.Expr[Molecule6[A, B, C, D, E, F]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F])


  def from7attr[Ns7[_, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_7[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]])
  : c.Expr[Molecule7[A, B, C, D, E, F, G]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G])


  def from8attr[Ns8[_, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_8[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]])
  : c.Expr[Molecule8[A, B, C, D, E, F, G, H]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H])


  def from9attr[Ns9[_, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_9[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]])
  : c.Expr[Molecule9[A, B, C, D, E, F, G, H, I]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I])


  def from10attr[Ns10[_, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_10[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]])
  : c.Expr[Molecule10[A, B, C, D, E, F, G, H, I, J]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J])

  def from11attr[Ns11[_, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_11[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]])
  : c.Expr[Molecule11[A, B, C, D, E, F, G, H, I, J, K]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K])


  def from12attr[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_12[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]])
  : c.Expr[Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L])


  def from13attr[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_13[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]])
  : c.Expr[Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M])


  def from14attr[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_14[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]])
  : c.Expr[Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N])


  def from15attr[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_15[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]])
  : c.Expr[Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O])


  def from16attr[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_16[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]])
  : c.Expr[Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P])


  def from17attr[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_17[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]])
  : c.Expr[Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q])


  def from18attr[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_18[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]])
  : c.Expr[Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R])


  def from19attr[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_19[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]])
  : c.Expr[Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S])


  def from20attr[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_20[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]])
  : c.Expr[Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T])


  def from21attr[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_21[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]])
  : c.Expr[Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U])


  def from22attr[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
  A: c.WeakTypeTag, B: c.WeakTypeTag, C: c.WeakTypeTag, D: c.WeakTypeTag, E: c.WeakTypeTag, F: c.WeakTypeTag, G: c.WeakTypeTag, H: c.WeakTypeTag, I: c.WeakTypeTag, J: c.WeakTypeTag, K: c.WeakTypeTag, L: c.WeakTypeTag, M: c.WeakTypeTag, N: c.WeakTypeTag, O: c.WeakTypeTag, P: c.WeakTypeTag, Q: c.WeakTypeTag, R: c.WeakTypeTag, S: c.WeakTypeTag, T: c.WeakTypeTag, U: c.WeakTypeTag, V: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Out_22[Ns22, Ns23, In1_22, In1_23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]])
  : c.Expr[Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]] =
    build(c).fromXattrs(dsl, c.weakTypeOf[A], c.weakTypeOf[B], c.weakTypeOf[C], c.weakTypeOf[D], c.weakTypeOf[E], c.weakTypeOf[F], c.weakTypeOf[G], c.weakTypeOf[H], c.weakTypeOf[I], c.weakTypeOf[J], c.weakTypeOf[K], c.weakTypeOf[L], c.weakTypeOf[M], c.weakTypeOf[N], c.weakTypeOf[O], c.weakTypeOf[P], c.weakTypeOf[Q], c.weakTypeOf[R], c.weakTypeOf[S], c.weakTypeOf[T], c.weakTypeOf[U], c.weakTypeOf[V])


  // Composites ....................................................

  def from1tuple[T1: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite1[T1]])
  : c.Expr[Molecule1[T1]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1])

  def from2tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite2[T1, T2]])
  : c.Expr[Molecule2[T1, T2]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2])

  def from3tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite3[T1, T2, T3]])
  : c.Expr[Molecule3[T1, T2, T3]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3])

  def from4tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite4[T1, T2, T3, T4]]): c.Expr[Molecule4[T1, T2, T3, T4]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4])

  def from5tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite5[T1, T2, T3, T4, T5]])
  : c.Expr[Molecule5[T1, T2, T3, T4, T5]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5])

  def from6tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite6[T1, T2, T3, T4, T5, T6]])
  : c.Expr[Molecule6[T1, T2, T3, T4, T5, T6]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6])

  def from7tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite7[T1, T2, T3, T4, T5, T6, T7]])
  : c.Expr[Molecule7[T1, T2, T3, T4, T5, T6, T7]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7])

  def from8tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite8[T1, T2, T3, T4, T5, T6, T7, T8]])
  : c.Expr[Molecule8[T1, T2, T3, T4, T5, T6, T7, T8]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8])

  def from9tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite9[T1, T2, T3, T4, T5, T6, T7, T8, T9]])
  : c.Expr[Molecule9[T1, T2, T3, T4, T5, T6, T7, T8, T9]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9])

  def from10tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]])
  : c.Expr[Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10])

  def from11tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]])
  : c.Expr[Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11])

  def from12tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]])
  : c.Expr[Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12])

  def from13tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]])
  : c.Expr[Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13])

  def from14tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]])
  : c.Expr[Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14])

  def from15tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]])
  : c.Expr[Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15])

  def from16tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]])
  : c.Expr[Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16])

  def from17tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]])
  : c.Expr[Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17])

  def from18tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]])
  : c.Expr[Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18])

  def from19tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]])
  : c.Expr[Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19])

  def from20tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]])
  : c.Expr[Molecule20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20])

  def from21tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]])
  : c.Expr[Molecule21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21])

  def from22tuples[T1: c.WeakTypeTag, T2: c.WeakTypeTag, T3: c.WeakTypeTag, T4: c.WeakTypeTag, T5: c.WeakTypeTag, T6: c.WeakTypeTag, T7: c.WeakTypeTag, T8: c.WeakTypeTag, T9: c.WeakTypeTag, T10: c.WeakTypeTag, T11: c.WeakTypeTag, T12: c.WeakTypeTag, T13: c.WeakTypeTag, T14: c.WeakTypeTag, T15: c.WeakTypeTag, T16: c.WeakTypeTag, T17: c.WeakTypeTag, T18: c.WeakTypeTag, T19: c.WeakTypeTag, T20: c.WeakTypeTag, T21: c.WeakTypeTag, T22: c.WeakTypeTag]
  (c: Context)(dsl: c.Expr[Composite22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]])
  : c.Expr[Molecule22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]] =
    build(c).fromXtuples(dsl, c.weakTypeOf[T1], c.weakTypeOf[T2], c.weakTypeOf[T3], c.weakTypeOf[T4], c.weakTypeOf[T5], c.weakTypeOf[T6], c.weakTypeOf[T7], c.weakTypeOf[T8], c.weakTypeOf[T9], c.weakTypeOf[T10], c.weakTypeOf[T11], c.weakTypeOf[T12], c.weakTypeOf[T13], c.weakTypeOf[T14], c.weakTypeOf[T15], c.weakTypeOf[T16], c.weakTypeOf[T17], c.weakTypeOf[T18], c.weakTypeOf[T19], c.weakTypeOf[T20], c.weakTypeOf[T21], c.weakTypeOf[T22])
}