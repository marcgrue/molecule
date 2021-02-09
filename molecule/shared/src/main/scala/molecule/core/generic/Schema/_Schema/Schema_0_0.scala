/*
* AUTO-GENERATED Molecule DSL for namespace `Schema`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/SchemaDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Schema

import java.util.Date
import molecule.core.dsl.base._
import scala.language.higherKinds

trait Schema_0_0[o0[_], p0] extends Schema_[p0] with Schema with NS_0_00[o0, p0]

trait Schema_0_0_L0[o0[_], p0] extends Schema_0_0[o0, p0] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L0[o0, p0 with Prop, Tpe], Nothing] with Schema_0_1_L0[o0, p0 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L0[o0, p0               ], Nothing] with Schema_0_0_L0[o0, p0               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L1[o0[_], p0, o1[_], p1] extends Schema_0_0[o0, p0 with o1[p1]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L1[o0, p0, o1, p1 with Prop, Tpe], Nothing] with Schema_0_1_L1[o0, p0, o1, p1 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L1[o0, p0, o1, p1               ], Nothing] with Schema_0_0_L1[o0, p0, o1, p1               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L2[o0[_], p0, o1[_], p1, o2[_], p2] extends Schema_0_0[o0, p0 with o1[p1 with o2[p2]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe], Nothing] with Schema_0_1_L2[o0, p0, o1, p1, o2, p2 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L2[o0, p0, o1, p1, o2, p2               ], Nothing] with Schema_0_0_L2[o0, p0, o1, p1, o2, p2               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L3[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3] extends Schema_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe], Nothing] with Schema_0_1_L3[o0, p0, o1, p1, o2, p2, o3, p3 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ], Nothing] with Schema_0_0_L3[o0, p0, o1, p1, o2, p2, o3, p3               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L4[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4] extends Schema_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe], Nothing] with Schema_0_1_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ], Nothing] with Schema_0_0_L4[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L5[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5] extends Schema_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe], Nothing] with Schema_0_1_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ], Nothing] with Schema_0_0_L5[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L6[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6] extends Schema_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe], Nothing] with Schema_0_1_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ], Nothing] with Schema_0_0_L6[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}


trait Schema_0_0_L7[o0[_], p0, o1[_], p1, o2[_], p2, o3[_], p3, o4[_], p4, o5[_], p5, o6[_], p6, o7[_], p7] extends Schema_0_0[o0, p0 with o1[p1 with o2[p2 with o3[p3 with o4[p4 with o5[p5 with o6[p6 with o7[p7]]]]]]]] {
  type Next[Attr[_, _], Prop, Tpe] = Attr[Schema_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe], Nothing] with Schema_0_1_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7 with Prop, Tpe]
  type Stay[Attr[_, _], Prop, Tpe] = Attr[Schema_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ], Nothing] with Schema_0_0_L7[o0, p0, o1, p1, o2, p2, o3, p3, o4, p4, o5, p5, o6, p6, o7, p7               ]

  final lazy val id           : Next[id          , Schema_id         , Long   ] = ???
  final lazy val a            : Next[a           , Schema_a          , String ] = ???
  final lazy val part         : Next[part        , Schema_part       , String ] = ???
  final lazy val nsFull       : Next[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns           : Next[ns          , Schema_ns         , String ] = ???
  final lazy val ident        : Next[ident       , Schema_ident      , String ] = ???
  final lazy val attr         : Next[attr        , Schema_attr       , String ] = ???
  final lazy val tpe          : Next[tpe         , Schema_tpe        , String ] = ???
  final lazy val card         : Next[card        , Schema_card       , String ] = ???
  final lazy val doc          : Next[doc         , Schema_doc        , String ] = ???
  final lazy val index        : Next[index       , Schema_index      , Boolean] = ???
  final lazy val unique       : Next[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext     : Next[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent  : Next[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory    : Next[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum         : Next[enum        , Schema_enum       , String ] = ???
  final lazy val t            : Next[t           , Schema_t          , Long   ] = ???
  final lazy val tx           : Next[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant    : Next[txInstant   , Schema_txInstant  , Date   ] = ???
  
  final lazy val doc$         : Next[doc$        , Schema_doc$        , Option[String ]] = ???
  final lazy val index$       : Next[index$      , Schema_index$      , Option[Boolean]] = ???
  final lazy val unique$      : Next[unique$     , Schema_unique$     , Option[String ]] = ???
  final lazy val fulltext$    : Next[fulltext$   , Schema_fulltext$   , Option[Boolean]] = ???
  final lazy val isComponent$ : Next[isComponent$, Schema_isComponent$, Option[Boolean]] = ???
  final lazy val noHistory$   : Next[noHistory$  , Schema_noHistory$  , Option[Boolean]] = ???
  
  final lazy val id_          : Stay[id          , Schema_id         , Long   ] = ???
  final lazy val a_           : Stay[a           , Schema_a          , String ] = ???
  final lazy val part_        : Stay[part        , Schema_part       , String ] = ???
  final lazy val nsFull_      : Stay[nsFull      , Schema_nsFull     , String ] = ???
  final lazy val ns_          : Stay[ns          , Schema_ns         , String ] = ???
  final lazy val ident_       : Stay[ident       , Schema_ident      , String ] = ???
  final lazy val attr_        : Stay[attr        , Schema_attr       , String ] = ???
  final lazy val tpe_         : Stay[tpe         , Schema_tpe        , String ] = ???
  final lazy val card_        : Stay[card        , Schema_card       , String ] = ???
  final lazy val doc_         : Stay[doc         , Schema_doc        , String ] = ???
  final lazy val index_       : Stay[index       , Schema_index      , Boolean] = ???
  final lazy val unique_      : Stay[unique      , Schema_unique     , String ] = ???
  final lazy val fulltext_    : Stay[fulltext    , Schema_fulltext   , Boolean] = ???
  final lazy val isComponent_ : Stay[isComponent , Schema_isComponent, Boolean] = ???
  final lazy val noHistory_   : Stay[noHistory   , Schema_noHistory  , Boolean] = ???
  final lazy val enum_        : Stay[enum        , Schema_enum       , String ] = ???
  final lazy val t_           : Stay[t           , Schema_t          , Long   ] = ???
  final lazy val tx_          : Stay[tx          , Schema_tx         , Long   ] = ???
  final lazy val txInstant_   : Stay[txInstant   , Schema_txInstant  , Date   ] = ???
}

     
