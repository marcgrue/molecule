package molecule.generic.dsl.index
import java.util.Date
import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outIndex._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericAEVT {
  object AEVT extends AEVT_0 with FirstNS {
    final def apply(a: String): AEVT_0 = ???
  }
}

trait AEVT extends GenericNs {
  final class e        [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class a        [Ns, In] extends OneString [Ns, In] with Indexed
  final class v        [Ns, In] extends OneAny    [Ns, In] with Indexed
  final class t        [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class tx       [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class txInstant[Ns, In] extends OneDate   [Ns, In] with Indexed
  final class op       [Ns, In] extends OneBoolean[Ns, In] with Indexed
}

trait AEVT_0 extends AEVT with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[AEVT_1[Type], P2[_,_]] with AEVT_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_1[A] extends AEVT with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_2[A, Type], P3[_,_,_]] with AEVT_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_2[A, B] extends AEVT with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_3[A, B, Type], P4[_,_,_,_]] with AEVT_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_3[A, B, C] extends AEVT with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_4[A, B, C, Type], P5[_,_,_,_,_]] with AEVT_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_4[A, B, C, D] extends AEVT with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with AEVT_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_5[A, B, C, D, E] extends AEVT with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with AEVT_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_6[A, B, C, D, E, F] extends AEVT with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[AEVT_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with AEVT_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait AEVT_7[A, B, C, D, E, F, G] extends AEVT with OutIndex_7[A, B, C, D, E, F, G]

