package molecule.generic.dsl.index

import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.out._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericVAET {
  object VAET extends VAET_0 with FirstNS {
    final override def apply(eid: Long, eids: Long*): VAET_0 = ???
    final override def apply(eids: Iterable[Long]): VAET_0 = ???
  }
}

trait VAET extends GenericNs

trait VAET_0 extends VAET with Out_0[VAET_0, VAET_1, P1, P2]
trait VAET_1[A] extends VAET with Out_1[VAET_1, VAET_2, P2, P3, A]
trait VAET_2[A, B] extends VAET with Out_2[VAET_2, VAET_3, P3, P4, A, B]
trait VAET_3[A, B, C] extends VAET with Out_3[VAET_3, VAET_4, P4, P5, A, B, C]
trait VAET_4[A, B, C, D] extends VAET with Out_4[VAET_4, VAET_5, P5, P6, A, B, C, D]
trait VAET_5[A, B, C, D, E] extends VAET with Out_5[VAET_5, VAET_6, P6, P7, A, B, C, D, E]
trait VAET_6[A, B, C, D, E, F] extends VAET with Out_6[VAET_6, VAET_7, P7, P8, A, B, C, D, E, F]
trait VAET_7[A, B, C, D, E, F, G] extends VAET with Out_7[VAET_7, VAET_8, P8, P9, A, B, C, D, E, F, G]
trait VAET_8[A, B, C, D, E, F, G, H] extends VAET with Out_8[VAET_8, P9, P9, P10, A, B, C, D, E, F, G, H]
