package molecule.core.util.testing
package txCount
import scala.language.higherKinds
import molecule.core._2_dsl.boilerplate.attributes._
import molecule.core._2_dsl.boilerplate.base._
import molecule.core._2_dsl.boilerplate.dummyTypes._
import molecule.core._2_dsl.boilerplate.api._
import molecule.core._2_dsl.expression.AttrExpressions.?


trait TxCount_2[Obj, A, B] extends TxCount {
  private type Stay[Attr[_, _], Prop, Tpe] = Attr[TxCount_2[Obj, A, B], _] with TxCount_2[Obj with Prop, A, B]

  final lazy val db_     : Stay[db     , TxCount_db    , String] = ???
  final lazy val basisT_ : Stay[basisT , TxCount_basisT, Long  ] = ???
}