package molecule.core.util.testing
package txCount
import scala.language.higherKinds
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.dsl.dummyTypes._
import molecule.core.dsl.api._
import molecule.core.expression.AttrExpressions.?


trait TxCount_0 extends TxCount {
  private type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_1[Prop, Tpe], _] with TxCount_1[Prop, Tpe]
  private type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_0           , _] with TxCount_0

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???

  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long]  ] = ???

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}