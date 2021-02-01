package molecule.core._2_dsl.generic.schema

import java.util.Date
import molecule.core._4_api.api.Keywords
import molecule.core._2_dsl.boilerplate.base.NS_0_21
import molecule.core._2_dsl.boilerplate.dummyTypes._
import molecule.core._2_dsl.boilerplate.outSchema._
import molecule.core._2_dsl.generic.Schema
import scala.language.higherKinds


trait Schema_21[obj[_], props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] extends Schema with NS_0_21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_22[obj, props with Prop, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Tpe], _] with Schema_22[obj, props with Prop, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Tpe]
  type Stay[Attr[_, _]           ] = Attr[Schema_21[obj, props          , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U     ], _] with Schema_21[obj, props          , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  
  final lazy val id           : Next[id          , Schema_id          , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a           , String ] = ???
  final lazy val part         : Next[part        , Schema_part        , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull      , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns          , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr        , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe         , String ] = ???
  final lazy val card         : Next[card        , Schema_card        , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc         , String ] = ???
  final lazy val index        : Next[index       , Schema_index       , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique      , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext    , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent , Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory   , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum        , String ] = ???
  final lazy val t            : Next[t           , Schema_t           , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx          , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant   , Date   ] = ???

  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String] ] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String] ] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???

  final lazy val id_          : Stay[id         ] = ???
  final lazy val a_           : Stay[a          ] = ???
  final lazy val part_        : Stay[part       ] = ???
  final lazy val nsFull_      : Stay[nsFull     ] = ???
  final lazy val ns_          : Stay[ns         ] = ???
  final lazy val attr_        : Stay[attr       ] = ???
  final lazy val tpe_         : Stay[tpe        ] = ???
  final lazy val card_        : Stay[card       ] = ???
  final lazy val doc_         : Stay[doc        ] = ???
  final lazy val index_       : Stay[index      ] = ???
  final lazy val unique_      : Stay[unique     ] = ???
  final lazy val fulltext_    : Stay[fulltext   ] = ???
  final lazy val isComponent_ : Stay[isComponent] = ???
  final lazy val noHistory_   : Stay[noHistory  ] = ???
  final lazy val enum_        : Stay[enum       ] = ???
  final lazy val t_           : Stay[t          ] = ???
  final lazy val tx_          : Stay[tx         ] = ???
  final lazy val txInstant_   : Stay[txInstant  ] = ???

  def apply(v: Keywords.count): Schema_21[obj, props, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Int] = ???
}
         