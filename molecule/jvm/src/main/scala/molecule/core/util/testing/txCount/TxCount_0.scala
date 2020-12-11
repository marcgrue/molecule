package molecule.core.util.testing
package txCount
import scala.language.higherKinds
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.out._
import molecule.core.expression.AttrExpressions.?


trait TxCount_0 extends TxCount with Out_0[TxCount_0, TxCount_1, P1, P2] {
  type Next[Attr[_, _], Type] = Attr[TxCount_1[Type], P2[_,_]] with TxCount_1[Type]
  type Stay[Attr[_, _], Type] = Attr[TxCount_0, P1[_]] with TxCount_0

  final lazy val db      : Next[db     , String] = ???
  final lazy val basisT  : Next[basisT , Long  ] = ???

  final lazy val db$     : Next[db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, Option[Long]  ] = ???

  final lazy val db_     : Stay[db     , String] = ???
  final lazy val basisT_ : Stay[basisT , Long  ] = ???
}