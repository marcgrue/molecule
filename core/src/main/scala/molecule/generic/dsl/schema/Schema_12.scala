package molecule.generic.dsl
package schema
import java.util.Date
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outSchema._
import scala.language.higherKinds


trait Schema_12[A, B, C, D, E, F, G, H, I, J, K, L] extends Schema with OutSchema_12[Schema_12, A, B, C, D, E, F, G, H, I, J, K, L] {
  type Next[Attr[_, _], Type] = Attr[Schema_13[A, B, C, D, E, F, G, H, I, J, K, L, Type], P14[_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Schema_13[A, B, C, D, E, F, G, H, I, J, K, L, Type]
  type Stay[Attr[_, _], Type] = Attr[Schema_12[A, B, C, D, E, F, G, H, I, J, K, L], P13[_,_,_,_,_,_,_,_,_,_,_,_,_]] with Schema_12[A, B, C, D, E, F, G, H, I, J, K, L]
  
  final lazy val id              : Next[id             , Long       ] = ???
  final lazy val ident           : Next[ident          , String     ] = ???
  final lazy val ns              : Next[ns             , String     ] = ???
  final lazy val a               : Next[a              , String     ] = ???
  final lazy val tpe             : Next[tpe            , String     ] = ???
  final lazy val card            : Next[card           , String     ] = ???
  final lazy val doc             : Next[doc            , String     ] = ???
  final lazy val indexed         : Next[indexed        , Boolean    ] = ???
  final lazy val unique          : Next[unique         , String     ] = ???
  final lazy val fulltextSearch  : Next[fulltextSearch , Boolean    ] = ???
  final lazy val isComponent     : Next[isComponent    , Boolean    ] = ???
  final lazy val noHistory       : Next[noHistory      , Boolean    ] = ???
  final lazy val enums           : Next[enums          , Set[String]] = ???
  final lazy val t               : Next[t              , Long       ] = ???
  final lazy val tx              : Next[tx             , Long       ] = ???
  final lazy val txInstant       : Next[txInstant      , Date       ] = ???
  
  final lazy val id$             : Next[id$            , Option[Long]       ] = ???
  final lazy val ident$          : Next[ident$         , Option[String]     ] = ???
  final lazy val ns$             : Next[ns$            , Option[String]     ] = ???
  final lazy val a$              : Next[a$             , Option[String]     ] = ???
  final lazy val tpe$            : Next[tpe$           , Option[String]     ] = ???
  final lazy val card$           : Next[card$          , Option[String]     ] = ???
  final lazy val doc$            : Next[doc$           , Option[String]     ] = ???
  final lazy val indexed$        : Next[indexed$       , Option[Boolean]    ] = ???
  final lazy val unique$         : Next[unique$        , Option[String]     ] = ???
  final lazy val fulltextSearch$ : Next[fulltextSearch$, Option[Boolean]    ] = ???
  final lazy val isComponent$    : Next[isComponent$   , Option[Boolean]    ] = ???
  final lazy val noHistory$      : Next[noHistory$     , Option[Boolean]    ] = ???
  final lazy val enums$          : Next[enums$         , Option[Set[String]]] = ???
  final lazy val t$              : Next[t$             , Option[Long]       ] = ???
  final lazy val tx$             : Next[tx$            , Option[Long]       ] = ???
  final lazy val txInstant$      : Next[txInstant$     , Option[Date]       ] = ???
  
  final lazy val id_             : Stay[id             , Long       ] = ???
  final lazy val ident_          : Stay[ident          , String     ] = ???
  final lazy val ns_             : Stay[ns             , String     ] = ???
  final lazy val a_              : Stay[a              , String     ] = ???
  final lazy val tpe_            : Stay[tpe            , String     ] = ???
  final lazy val card_           : Stay[card           , String     ] = ???
  final lazy val doc_            : Stay[doc            , String     ] = ???
  final lazy val indexed_        : Stay[indexed        , Boolean    ] = ???
  final lazy val unique_         : Stay[unique         , String     ] = ???
  final lazy val fulltextSearch_ : Stay[fulltextSearch , Boolean    ] = ???
  final lazy val isComponent_    : Stay[isComponent    , Boolean    ] = ???
  final lazy val noHistory_      : Stay[noHistory      , Boolean    ] = ???
  final lazy val enums_          : Stay[enums          , Set[String]] = ???
  final lazy val t_              : Stay[t              , Long       ] = ???
  final lazy val tx_             : Stay[tx             , Long       ] = ???
  final lazy val txInstant_      : Stay[txInstant      , Date       ] = ???
}
         