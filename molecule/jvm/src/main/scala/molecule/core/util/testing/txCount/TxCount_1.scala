package molecule.core.util.testing
package txCount
import scala.language.higherKinds
import molecule.core._2_dsl.boilerplate.attributes._
import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.boilerplate.dummyTypes._
import molecule.core._2_dsl.boilerplate.api._
import molecule.core._2_dsl.expression.AttrExpressions.?


trait TxCount_1[Obj, A] extends TxCount {
  private type Next[Attr[_, _], Prop, Tpe] = Attr[TxCount_2[Obj, A, Tpe], _] with TxCount_2[Obj with Prop, A, Tpe]
  private type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_1[Obj, A]     , _] with TxCount_1[Obj, A]

  final lazy val db      : Next[db     , TxCount_db    , String] = ???
  final lazy val basisT  : Next[basisT , TxCount_basisT, Long  ] = ???

  final lazy val db$     : Next[db$    , TxCount_db$    , Option[String]] = ???
  final lazy val basisT$ : Next[basisT$, TxCount_basisT$, Option[Long]  ] = ???

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???

  final def Self : TxCount_1[Obj, A] with SelfJoin = ???
}