package molecule.generic.dsl.index

import molecule.boilerplate.base._
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.out._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericAVET {
  object AVET extends AVET_0 with FirstNS {
    final override def apply(eid: Long, eids: Long*): AVET_0 = ???
    final override def apply(eids: Iterable[Long]): AVET_0 = ???
  }
}

trait AVET extends GenericNs

trait AVET_0 extends AVET with Out_0[AVET_0, AVET_1, P1, P2]
trait AVET_1[A] extends AVET with Out_1[AVET_1, AVET_2, P2, P3, A]
trait AVET_2[A, B] extends AVET with Out_2[AVET_2, AVET_3, P3, P4, A, B]
trait AVET_3[A, B, C] extends AVET with Out_3[AVET_3, AVET_4, P4, P5, A, B, C]
trait AVET_4[A, B, C, D] extends AVET with Out_4[AVET_4, AVET_5, P5, P6, A, B, C, D]
trait AVET_5[A, B, C, D, E] extends AVET with Out_5[AVET_5, AVET_6, P6, P7, A, B, C, D, E]
trait AVET_6[A, B, C, D, E, F] extends AVET with Out_6[AVET_6, AVET_7, P7, P8, A, B, C, D, E, F]
trait AVET_7[A, B, C, D, E, F, G] extends AVET with Out_7[AVET_7, AVET_8, P8, P9, A, B, C, D, E, F, G]
trait AVET_8[A, B, C, D, E, F, G, H] extends AVET with Out_8[AVET_8, P9, P9, P10, A, B, C, D, E, F, G, H]
