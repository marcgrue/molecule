package molecule.generic.schema

import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.generic.GenericNs
import scala.language.higherKinds


/** Container for Schema object. */
trait GenericSchema {

  /** Schema object to start Schema molecule. */
  object Schema extends Schema_0 with FirstNS
}

/** Base Schema trait with attribute types shared by all arity interfaces. */
trait Schema extends GenericNs {

  /** Attribute definition entity id. */
  final class id           [Ns, In] extends OneLong   [Ns, In] with Indexed

  /** (Partition-)Namespace-prefixed attribute name (":part_Ns/attr" or ":ns/attr" if no partitions). */
  final class a            [Ns, In] extends OneString [Ns, In] with Indexed

  /** Partition name (if partitions are not defined it will be an empty string ""). */
  final class part         [Ns, In] extends OneString [Ns, In] with Indexed

  /** Namespace name with partition prefix ("part_Ns" or simply "ns" if partitions are not defined). */
  final class nsFull       [Ns, In] extends OneString [Ns, In] with Indexed

  /** Namespace name ("ns"). */
  final class ns           [Ns, In] extends OneString [Ns, In] with Indexed

  /** Isolated attribute name without namespace prefix ("attr"). */
  final class attr         [Ns, In] extends OneString [Ns, In] with Indexed

  /** Datomic attribute type.
    *
    * Some Datomic types map to two Scala types:
    *
    * Datomic/Scala types:
    *
    *  - '''string''' - String
    *  - '''boolean''' - Boolean
    *  - '''long''' - Int, Long
    *  - '''float''' - Float
    *  - '''double''' - Double
    *  - '''bigint''' - BigInt
    *  - '''bigdec''' - BigDecimal
    *  - '''instant''' - java.util.Date
    *  - '''uuid''' - java.util.UUID
    *  - '''uri''' - java.net.URI
    *  - '''ref''' - Long
    * */
  final class tpe          [Ns, In] extends OneString [Ns, In] with Indexed

  /** Cardinality: one/many. */
  final class card         [Ns, In] extends OneString [Ns, In] with Indexed

  /** Documentation string. */
  final class doc          [Ns, In] extends OneString [Ns, In] with Indexed with Fulltext[Ns, In]

  /** Attribute index status (true/not set). */
  final class index        [Ns, In] extends OneBoolean[Ns, In] with Indexed

  /** Attribute uniqueness status (true/not set). */
  final class unique       [Ns, In] extends OneString [Ns, In] with Indexed

  /** Attribute fulltext search status (true/not set). */
  final class fulltext     [Ns, In] extends OneBoolean[Ns, In] with Indexed

  /** Attribute isComponent status (true/not set). */
  final class isComponent  [Ns, In] extends OneBoolean[Ns, In] with Indexed

  /** Attribute noHistory status (true/not set). */
  final class noHistory    [Ns, In] extends OneBoolean[Ns, In] with Indexed

  /** Enum attributes. */
  final class enum         [Ns, In] extends OneString [Ns, In] with Indexed

  /** Attribute definition transaction point in time. */
  final class t            [Ns, In] extends OneLong   [Ns, In] with Indexed

  /** Attribute definition transaction entity id. */
  final class tx           [Ns, In] extends OneLong   [Ns, In] with Indexed

  /** Attribute definition transaction wall-clock time. */
  final class txInstant    [Ns, In] extends OneDate   [Ns, In] with Indexed


  // Optional generic Schema attributes

  /** Optional documentation string. */
  final class doc$         [Ns, In] extends OneString$ [Ns] with Indexed with Fulltext[Ns, In]

  /** Optional attribute index status (true/not set). */
  final class index$       [Ns, In] extends OneBoolean$[Ns] with Indexed

  /** Optional attribute uniqueness status (true/not set). */
  final class unique$      [Ns, In] extends OneString$ [Ns] with Indexed

  /** Optional attribute fulltext search status (true/not set). */
  final class fulltext$    [Ns, In] extends OneBoolean$[Ns] with Indexed

  /** Optional attribute isComponent status (true/not set). */
  final class isComponent$ [Ns, In] extends OneBoolean$[Ns] with Indexed

  /** Optional attribute noHistory status (true/not set). */
  final class noHistory$   [Ns, In] extends OneBoolean$[Ns] with Indexed
}