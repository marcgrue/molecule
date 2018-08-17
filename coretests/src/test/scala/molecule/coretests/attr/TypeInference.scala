package molecule.coretests.attr

import molecule.api._

import java.net.URI
import java.util.{Date, UUID}

import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class TypeInference extends CoreSpec {

  "Infer return types of cardinality-one attributes" in new CoreSetup {

    // Arity 1
    typed[String](Ns.str.get.head)
    typed[Int](Ns.int.get.head)
    typed[Long](Ns.long.get.head)
    typed[Float](Ns.float.get.head)
    typed[Double](Ns.double.get.head)
    typed[Boolean](Ns.bool.get.head)
    typed[Date](Ns.date.get.head)
    typed[UUID](Ns.uuid.get.head)
    typed[URI](Ns.uri.get.head)
    typed[String](Ns.enum.get.head)
    typed[Long](Ns.ref1.get.head)

//    // Arity 2
//    typed[(String, Int)](Ns.str.int.get.head)
//    typed[(Long, Float)](Ns.long.float.get.head)
//    typed[(Double, Boolean)](Ns.double.bool.get.head)
//    typed[(Date, UUID)](Ns.date.uuid.get.head)
//    typed[(URI, String)](Ns.uri.enum.get.head)
//
//    // Arity 3
//    typed[(String, Int, Long)](Ns.str.int.long.get.head)
//    typed[(Float, Double, Boolean)](Ns.float.double.bool.get.head)
//    typed[(Date, UUID, URI)](Ns.date.uuid.uri.get.head)
//    // looping from the first types...
//    typed[(String, String, Int)](Ns.enum.str.int.get.head)
//
//    // Arity 4
//    typed[(String, Int, Long, Float)](Ns.str.int.long.float.get.head)
//    typed[(Double, Boolean, Date, UUID)](Ns.double.bool.date.uuid.get.head)
//    typed[(URI, String, String, Int)](Ns.uri.enum.str.int.get.head)
//
//    // Arity 5
//    typed[(String, Int, Long, Float, Double)](Ns.str.int.long.float.double.get.head)
//    typed[(Boolean, Date, UUID, URI, String)](Ns.bool.date.uuid.uri.enum.get.head)
//
//    // Arity 6
//    typed[(String, Int, Long, Float, Double, Boolean)](Ns.str.int.long.float.double.bool.get.head)
//    typed[(Date, UUID, URI, String, String, Int)](Ns.date.uuid.uri.enum.str.int.get.head)
//
//    // Arity 7
//    typed[(String, Int, Long, Float, Double, Boolean, Date)](Ns.str.int.long.float.double.bool.date.get.head)
//    typed[(UUID, URI, String, String, Int, Long, Float)](Ns.uuid.uri.enum.str.int.long.float.get.head)
//
//    // Arity 8
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID)](Ns.str.int.long.float.double.bool.date.uuid.get.head)
//    typed[(URI, String, String, Int, Long, Float, Double, Boolean)](Ns.uri.enum.str.int.long.float.double.bool.get.head)
//
//    // Arity 9
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI)](Ns.str.int.long.float.double.bool.date.uuid.uri.get.head)
//    typed[(String, String, Int, Long, Float, Double, Boolean, Date, UUID)](Ns.enum.str.int.long.float.double.bool.date.uuid.get.head)
//
//    // Arity 10
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.get.head)
//
//    // Arity 11
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.get.head)
//
//    // Arity 12
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.get.head)
//
//    // Arity 13
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.get.head)
//
//    // Arity 14
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.get.head)
//
//    // Arity 15
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.get.head)
//
//    // Arity 16
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.get.head)
//
//    // Arity 17
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean, Date)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.date.get.head)
//
//    // Arity 18
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean, Date, UUID)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.date.uuid.get.head)
//
//    // Arity 19
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean, Date, UUID, URI)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.date.uuid.uri.get.head)
//
//    // Arity 20
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.date.uuid.uri.enum.get.head)
//
//    // Arity 21
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.date.uuid.uri.enum.str.get.head)
//
//    // Arity 22
//    typed[(String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int, Long, Float, Double, Boolean, Date, UUID, URI, String, String, Int)](
//      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.long.float.double.bool.date.uuid.uri.enum.str.int.get.head)

    // All types have been correctly inferred
    ok
  }


  "Infer return types of cardinality-many attributes" in new CoreSetup {

    // Arity 1
    typed[Set[String]](Ns.strs.get.head)
    typed[Set[Int]](Ns.ints.get.head)
    typed[Set[Long]](Ns.longs.get.head)
    typed[Set[Float]](Ns.floats.get.head)
    typed[Set[Double]](Ns.doubles.get.head)
    typed[Set[Date]](Ns.dates.get.head)
    typed[Set[UUID]](Ns.uuids.get.head)
    typed[Set[URI]](Ns.uris.get.head)
    typed[Set[String]](Ns.enums.get.head)
    typed[Set[Long]](Ns.refs1.get.head)

//    // Arity 2
//    typed[(Set[String], Set[Int])](Ns.strs.ints.get.head)
//    typed[(Set[Long], Set[Float])](Ns.longs.floats.get.head)
//    typed[(Set[Double], Set[Date])](Ns.doubles.dates.get.head)
//    typed[(Set[UUID], Set[URI])](Ns.uuids.uris.get.head)
//    typed[(Set[String], Set[String])](Ns.enums.strs.get.head)
//
//    // Arity 3
//    typed[(Set[String], Set[Int], Set[Long])](Ns.strs.ints.longs.get.head)
//    typed[(Set[Float], Set[Double], Set[Date])](Ns.floats.doubles.dates.get.head)
//    typed[(Set[UUID], Set[URI], Set[String])](Ns.uuids.uris.enums.get.head)
//    typed[(Set[String], Set[String], Set[Int])](Ns.enums.strs.ints.get.head)
//
//    // Arity 4
//    typed[(Set[String], Set[Int], Set[Long], Set[Float])](Ns.strs.ints.longs.floats.get.head)
//    typed[(Set[Double], Set[Date], Set[UUID], Set[URI])](Ns.doubles.dates.uuids.uris.get.head)
//    typed[(Set[String], Set[String], Set[Int], Set[Long])](Ns.enums.strs.ints.longs.get.head)
//
//    // Arity 5
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double])](Ns.strs.ints.longs.floats.doubles.get.head)
//    typed[(Set[Date], Set[UUID], Set[URI], Set[String], Set[String])](Ns.dates.uuids.uris.enums.strs.get.head)
//
//    // Arity 6
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date])](Ns.strs.ints.longs.floats.doubles.dates.get.head)
//    typed[(Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long])](Ns.uuids.uris.enums.strs.ints.longs.get.head)
//
//    // Arity 7
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID])](Ns.strs.ints.longs.floats.doubles.dates.uuids.get.head)
//    typed[(Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double])](Ns.uris.enums.strs.ints.longs.floats.doubles.get.head)
//
//    // Arity 8
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI])](Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.get.head)
//    typed[(Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID])](Ns.enums.strs.ints.longs.floats.doubles.dates.uuids.get.head)
//
//    // Arity 9
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head)
//
//    // Arity 10
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.get.head)
//
//    // Arity 11
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.get.head)
//
//    // Arity 12
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.get.head)
//
//    // Arity 13
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.get.head)
//
//    // Arity 14
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.get.head)
//
//    // Arity 15
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.get.head)
//
//    // Arity 16
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.get.head)
//
//    // Arity 17
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.uris.get.head)
//
//    // Arity 18
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head)
//
//    // Arity 19
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.get.head)
//
//    // Arity 20
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.get.head)
//
//    // Arity 21
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.get.head)
//
//    // Arity 22
//    typed[(Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float], Set[Double], Set[Date], Set[UUID], Set[URI], Set[String], Set[String], Set[Int], Set[Long], Set[Float])](
//      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.strs.ints.longs.floats.get.head)

    // All types have been correctly inferred
    ok
  }
}