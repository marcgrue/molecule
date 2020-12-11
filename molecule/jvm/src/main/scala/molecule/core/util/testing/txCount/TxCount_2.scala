package molecule.core.util.testing
package txCount
import scala.language.higherKinds
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.out._
import molecule.core.expression.AttrExpressions.?


trait TxCount_2[A, B] extends TxCount with Out_2[TxCount_2, P3, P3, P4, A, B] {
  type Stay[Attr[_, _], Type] = Attr[TxCount_2[A, B], P4[_,_,_,_]] with TxCount_2[A, B]

  final lazy val db_     : Stay[db     , String] = ???
  final lazy val basisT_ : Stay[basisT , Long  ] = ???
}