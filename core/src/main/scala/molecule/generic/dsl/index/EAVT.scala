package molecule.generic.dsl.index

import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.out._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericEAVT {
  object EAVT extends EAVT_0 with FirstNS {
    final override def apply(eid: Long, eids: Long*): EAVT_0 = ???
    final override def apply(eids: Iterable[Long]): EAVT_0 = ???
  }
}

trait EAVT extends GenericNs

trait EAVT_0 extends EAVT with Out_0[EAVT_0, EAVT_1, P1, P2]
trait EAVT_1[A] extends EAVT with Out_1[EAVT_1, EAVT_2, P2, P3, A]
trait EAVT_2[A, B] extends EAVT with Out_2[EAVT_2, EAVT_3, P3, P4, A, B]
trait EAVT_3[A, B, C] extends EAVT with Out_3[EAVT_3, EAVT_4, P4, P5, A, B, C]
trait EAVT_4[A, B, C, D] extends EAVT with Out_4[EAVT_4, EAVT_5, P5, P6, A, B, C, D]
trait EAVT_5[A, B, C, D, E] extends EAVT with Out_5[EAVT_5, EAVT_6, P6, P7, A, B, C, D, E]
trait EAVT_6[A, B, C, D, E, F] extends EAVT with Out_6[EAVT_6, EAVT_7, P7, P8, A, B, C, D, E, F]
trait EAVT_7[A, B, C, D, E, F, G] extends EAVT with Out_7[EAVT_7, EAVT_8, P8, P9, A, B, C, D, E, F, G]
trait EAVT_8[A, B, C, D, E, F, G, H] extends EAVT with Out_8[EAVT_8, P9, P9, P10, A, B, C, D, E, F, G, H]
