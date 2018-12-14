package molecule.generic.dsl.index

import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.out._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericAEVT {
  object AEVT extends AEVT_0 with FirstNS {
    final override def apply(eid: Long, eids: Long*): AEVT_0 = ???
    final override def apply(eids: Iterable[Long]): AEVT_0 = ???
  }
}

trait AEVT extends GenericNs

trait AEVT_0 extends AEVT with Out_0[AEVT_0, AEVT_1, P1, P2]
trait AEVT_1[A] extends AEVT with Out_1[AEVT_1, AEVT_2, P2, P3, A]
trait AEVT_2[A, B] extends AEVT with Out_2[AEVT_2, AEVT_3, P3, P4, A, B]
trait AEVT_3[A, B, C] extends AEVT with Out_3[AEVT_3, AEVT_4, P4, P5, A, B, C]
trait AEVT_4[A, B, C, D] extends AEVT with Out_4[AEVT_4, AEVT_5, P5, P6, A, B, C, D]
trait AEVT_5[A, B, C, D, E] extends AEVT with Out_5[AEVT_5, AEVT_6, P6, P7, A, B, C, D, E]
trait AEVT_6[A, B, C, D, E, F] extends AEVT with Out_6[AEVT_6, AEVT_7, P7, P8, A, B, C, D, E, F]
trait AEVT_7[A, B, C, D, E, F, G] extends AEVT with Out_7[AEVT_7, AEVT_8, P8, P9, A, B, C, D, E, F, G]
trait AEVT_8[A, B, C, D, E, F, G, H] extends AEVT with Out_8[AEVT_8, P9, P9, P10, A, B, C, D, E, F, G, H]
