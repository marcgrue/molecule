package molecule.core.generic.schema

import java.util.Date
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.outSchema._
import scala.language.higherKinds


trait Schema_22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] extends Schema with OutSchema_22[Schema_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] {
  type Stay[Attr[_, _], Type] = Attr[Schema_22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V], P24[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Schema_22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]

  final lazy val id_          : Stay[id           , Long   ] = ???
  final lazy val a_           : Stay[a            , String ] = ???
  final lazy val part_        : Stay[part         , String ] = ???
  final lazy val nsFull_      : Stay[nsFull       , String ] = ???
  final lazy val ns_          : Stay[ns           , String ] = ???
  final lazy val attr_        : Stay[attr         , String ] = ???
  final lazy val tpe_         : Stay[tpe          , String ] = ???
  final lazy val card_        : Stay[card         , String ] = ???
  final lazy val doc_         : Stay[doc          , String ] = ???
  final lazy val index_       : Stay[index        , Boolean] = ???
  final lazy val unique_      : Stay[unique       , String ] = ???
  final lazy val fulltext_    : Stay[fulltext     , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent  , Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory    , Boolean] = ???
  final lazy val enum_        : Stay[enum         , String ] = ???
  final lazy val t_           : Stay[t            , Long   ] = ???
  final lazy val tx_          : Stay[tx           , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant    , Date   ] = ???
}