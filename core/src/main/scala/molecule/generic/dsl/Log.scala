package molecule.generic.dsl

import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.out._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericLog {
  object Log extends Log_0 with FirstNS {
    final override def apply(eid: Long, eids: Long*): Log_0 = ???
    final override def apply(eids: Iterable[Long]): Log_0 = ???
  }
}

trait Log extends GenericNs

trait Log_0 extends Log with Out_0[Log_0, Log_1, P1, P2]
trait Log_1[A] extends Log with Out_1[Log_1, Log_2, P2, P3, A]
trait Log_2[A, B] extends Log with Out_2[Log_2, Log_3, P3, P4, A, B]
trait Log_3[A, B, C] extends Log with Out_3[Log_3, Log_4, P4, P5, A, B, C]
trait Log_4[A, B, C, D] extends Log with Out_4[Log_4, Log_5, P5, P6, A, B, C, D]
trait Log_5[A, B, C, D, E] extends Log with Out_5[Log_5, Log_6, P6, P7, A, B, C, D, E]
trait Log_6[A, B, C, D, E, F] extends Log with Out_6[Log_6, Log_7, P7, P8, A, B, C, D, E, F]
trait Log_7[A, B, C, D, E, F, G] extends Log with Out_7[Log_7, Log_8, P8, P9, A, B, C, D, E, F, G]
trait Log_8[A, B, C, D, E, F, G, H] extends Log with Out_8[Log_8, P9, P9, P10, A, B, C, D, E, F, G, H]
