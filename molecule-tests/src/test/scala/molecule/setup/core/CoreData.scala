package molecule.setup.core

import java.net.URI
import java.util.{Date, UUID}
import java.util.UUID._
import molecule.core.util.DateHandling

trait CoreData extends DateHandling {

  private def da(i: Int): Date = {
    // Alternate between winter/summer time to test daylight savings too
    val month = i % 2 * 6 + 1
    str2date((2000 + i).toString + "-" + month)
  }
  private def uu: UUID = randomUUID()
  private def ur(i: Int): URI = new URI("uri" + i)
  private def r(i: Int): Long = 17194139534365L + i // dummy ref/entity id

  // Sample data

  lazy val (date0, date1, date2, date3, date4, date5, date6, date7, date8, date9, date10, date11, date12, date13, date14, date15, date16, date17, date18) =
    (da(0), da(1), da(2), da(3), da(4), da(5), da(6), da(7), da(8), da(9), da(10), da(11), da(12), da(13), da(14), da(15), da(16), da(17), da(18))


  lazy val List(uuid0, uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7, uuid8, uuid9, uuid10, uuid11, uuid12, uuid13, uuid14, uuid15, uuid16, uuid17, uuid18) =
    List(uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu, uu).sortBy(_.toString)

  lazy val List(uri0, uri1, uri2, uri3, uri4, uri5, uri6, uri7, uri8, uri9, uri10, uri11, uri12, uri13, uri14, uri15, uri16, uri17, uri18) =
    List(ur(0), ur(1), ur(2), ur(3), ur(4), ur(5), ur(6), ur(7), ur(8), ur(9), ur(10), ur(11), ur(12), ur(13), ur(14), ur(15), ur(16), ur(17), ur(18))

  lazy val (str0, int0, long0, float0, double0, bigInt0, bigDec0, bool0, enum0, r0) = (" ", 0, 0L, 0.0f, 0.0, BigInt(0), BigDecimal(0.0), false, "enum0", r(0))
  lazy val (str1, int1, long1, float1, double1, bigInt1, bigDec1, bool1, enum1, r1) = ("a", 1, 1L, 1.0f, 1.0, BigInt(1), BigDecimal(1.0), true, "enum1", r(1))
  lazy val (str2, int2, long2, float2, double2, bigInt2, bigDec2, bool2, enum2, r2) = ("b", 2, 2L, 2.0f, 2.0, BigInt(2), BigDecimal(2.0), false, "enum2", r(2))
  lazy val (str3, int3, long3, float3, double3, bigInt3, bigDec3, bool3, enum3, r3) = ("c", 3, 3L, 3.0f, 3.0, BigInt(3), BigDecimal(3.0), true, "enum3", r(3))
  lazy val (str4, int4, long4, float4, double4, bigInt4, bigDec4, bool4, enum4, r4) = ("d", 4, 4L, 4.0f, 4.0, BigInt(4), BigDecimal(4.0), false, "enum4", r(4))
  lazy val (str5, int5, long5, float5, double5, bigInt5, bigDec5, bool5, enum5, r5) = ("e", 5, 5L, 5.0f, 5.0, BigInt(5), BigDecimal(5.0), true, "enum5", r(5))
  lazy val (str6, int6, long6, float6, double6, bigInt6, bigDec6, bool6, enum6, r6) = ("f", 6, 6L, 6.0f, 6.0, BigInt(6), BigDecimal(6.0), false, "enum6", r(6))
  lazy val (str7, int7, long7, float7, double7, bigInt7, bigDec7, bool7, enum7, r7) = ("g", 7, 7L, 7.0f, 7.0, BigInt(7), BigDecimal(7.0), true, "enum7", r(7))
  lazy val (str8, int8, long8, float8, double8, bigInt8, bigDec8, bool8, enum8, r8) = ("h", 8, 8L, 8.0f, 8.0, BigInt(8), BigDecimal(8.0), false, "enum8", r(8))
  lazy val (str9, int9, long9, float9, double9, bigInt9, bigDec9, bool9, enum9, r9) = ("i", 9, 9L, 9.0f, 9.0, BigInt(9), BigDecimal(9.0), true, "enum9", r(9))

  lazy val (enum10, enum11, enum12) = ("enum10", "enum11", "enum12")
  lazy val (enum20, enum21, enum22) = ("enum20", "enum21", "enum22")

  lazy val (strs0, ints0, longs0, floats0, doubles0, bools0, dates0, uuids0, uris0, enums0, rs0) = (
    Set(str0), Set(int0), Set(long0), Set(float0), Set(double0), Set(bool0), Set(date0), Set(uuid0), Set(uri0), Set(enum0), Set(r0)
  )

  lazy val (strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1, rs1) = (
    Set(str1), Set(int1), Set(long1), Set(float1), Set(double1), Set(bool1), Set(date1), Set(uuid1), Set(uri1), Set(enum1), Set(r1)
  )

  lazy val (strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2, rs2) = (
    Set(str1, str2), Set(int1, int2), Set(long1, long2), Set(float1, float2), Set(double1, double2),
    Set(bool1, bool2), Set(date1, date2), Set(uuid1, uuid2), Set(uri1, uri2), Set(enum1, enum2), Set(r1, r2)
  )

}