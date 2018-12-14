package molecule.generic.dsl
package schema
import java.util.Date
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outSchema._
import scala.language.higherKinds


trait Schema_16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] extends Schema with OutSchema_16[Schema_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] {
  type Stay[Attr[_, _], Type] = Attr[Schema_16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P], P18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_]] with Schema_16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  
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