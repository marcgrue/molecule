package molecule.core.generic.schema

import java.util.Date
import molecule.core.api.Keywords
import molecule.core.boilerplate.base.NS09
import molecule.core.boilerplate.dummyTypes._
import molecule.core.boilerplate.outSchema._
import scala.language.higherKinds


trait Schema_9[obj[_], props, A, B, C, D, E, F, G, H, I] extends Schema with NS09[obj, props, A, B, C, D, E, F, G, H, I] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_10[obj, props with Prop, A, B, C, D, E, F, G, H, I, Tpe], _] with Schema_10[obj, props with Prop, A, B, C, D, E, F, G, H, I, Tpe]
  type Stay[Attr[_, _]           ] = Attr[Schema_9[obj, props          , A, B, C, D, E, F, G, H, I     ], _] with Schema_9[obj, props          , A, B, C, D, E, F, G, H, I]
  
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

  def apply(v: Keywords.count): Schema_9[obj, props, A, B, C, D, E, F, G, H, Int] = ???
}
         