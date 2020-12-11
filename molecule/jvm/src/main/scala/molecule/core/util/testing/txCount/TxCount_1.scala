package molecule.core.util.testing
package txCount
import scala.language.higherKinds
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.out._
import molecule.core.expression.AttrExpressions.?


trait TxCount_1[A] extends TxCount with Out_1[TxCount_1, TxCount_2, P2, P3, A] {
  type Next[Attr[_, _], Type] = Attr[TxCount_2[A, Type], P3[_,_,_]] with TxCount_2[A, Type]
  type Stay[Attr[_, _], Type] = Attr[TxCount_1[A], P2[_,_]] with TxCount_1[A]

  final lazy val db      : Next[db     , String] = ???
  final lazy val basisT  : Next[basisT , Long  ] = ???

  final lazy val db$     : Next[db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, Option[Long]  ] = ???

  final lazy val db_     : Stay[db     , String] = ???
  final lazy val basisT_ : Stay[basisT , Long  ] = ???

  final def Self : TxCount_1[A] with SelfJoin = ???
}