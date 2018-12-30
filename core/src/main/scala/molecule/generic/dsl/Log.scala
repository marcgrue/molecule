package molecule.generic.dsl
import java.util.Date
import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.outIndex._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericLog {
  object Log extends Log_0 with FirstNS {
    final def apply(e: Long): Log_0 = ???
    final def apply(e: Long, a: String): Log_0 = ???
    final def apply(e: Long, a: String, v: Any): Log_0 = ???
    final def apply(e: Long, a: String, v: Any, t: Long): Log_0 = ???
    final def apply(e: Long, a: String, v: Any, txInstant: Date): Log_0 = ???
  }
}

trait Log extends GenericNs {
  final class e        [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class a        [Ns, In] extends OneString [Ns, In] with Indexed
  final class v        [Ns, In] extends OneAny    [Ns, In] with Indexed
  final class t        [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class tx       [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class txInstant[Ns, In] extends OneDate   [Ns, In] with Indexed
  final class op       [Ns, In] extends OneBoolean[Ns, In] with Indexed
}

trait Log_0 extends Log with OutIndex_0 {
  type Next_[Attr[_, _], Type] = Attr[Log_1[Type], P2[_,_]] with Log_1[Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_1[A] extends Log with OutIndex_1[A] {
  type Next_[Attr[_, _], Type] = Attr[Log_2[A, Type], P3[_,_,_]] with Log_2[A, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_2[A, B] extends Log with OutIndex_2[A, B] {
  type Next_[Attr[_, _], Type] = Attr[Log_3[A, B, Type], P4[_,_,_,_]] with Log_3[A, B, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_3[A, B, C] extends Log with OutIndex_3[A, B, C] {
  type Next_[Attr[_, _], Type] = Attr[Log_4[A, B, C, Type], P5[_,_,_,_,_]] with Log_4[A, B, C, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_4[A, B, C, D] extends Log with OutIndex_4[A, B, C, D] {
  type Next_[Attr[_, _], Type] = Attr[Log_5[A, B, C, D, Type], P6[_,_,_,_,_,_]] with Log_5[A, B, C, D, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_5[A, B, C, D, E] extends Log with OutIndex_5[A, B, C, D, E] {
  type Next_[Attr[_, _], Type] = Attr[Log_6[A, B, C, D, E, Type], P7[_,_,_,_,_,_,_]] with Log_6[A, B, C, D, E, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_6[A, B, C, D, E, F] extends Log with OutIndex_6[A, B, C, D, E, F] {
  type Next_[Attr[_, _], Type] = Attr[Log_7[A, B, C, D, E, F, Type], P8[_,_,_,_,_,_,_,_]] with Log_7[A, B, C, D, E, F, Type]

  final lazy val e          : Next_[e         , Long   ] = ???
  final lazy val a          : Next_[a         , String ] = ???
  final lazy val v          : Next_[v         , Any    ] = ???
  final lazy val t          : Next_[t         , Long   ] = ???
  final lazy val tx         : Next_[tx        , Long   ] = ???
  final lazy val txInstant  : Next_[txInstant , Date   ] = ???
  final lazy val op         : Next_[op        , Boolean] = ???
}

trait Log_7[A, B, C, D, E, F, G] extends Log with OutIndex_7[A, B, C, D, E, F, G]

