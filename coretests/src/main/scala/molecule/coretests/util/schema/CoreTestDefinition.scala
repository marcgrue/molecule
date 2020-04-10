package molecule.coretests.util.schema
import molecule.schema.definition._

@InOut(3, 22)
object CoreTestDefinition {

  trait Ns {
    val str     = oneString.fulltext.doc("Card one String attribute")
    val int     = oneInt.doc("Card one Int attribute")
    val long    = oneLong
    val float   = oneFloat
    val double  = oneDouble
    val bool    = oneBoolean
    val bigInt  = oneBigInt
    val bigDec  = oneBigDecimal
    val date    = oneDate
    val uuid    = oneUUID
    val uri     = oneURI
    val enum    = oneEnum("enum0", "enum1", "enum2", "enum3", "enum4", "enum5", "enum6", "enum7", "enum8", "enum9")
    val byte    = oneByte
    val parent  = one[Ns]
    val ref1    = one[Ref1]
    val refSub1 = one[Ref1].isComponent

    val strs     = manyString.fulltext
    val ints     = manyInt
    val longs    = manyLong
    val floats   = manyFloat
    val doubles  = manyDouble
    val bools    = manyBoolean
    val bigInts  = manyBigInt
    val bigDecs  = manyBigDecimal
    val dates    = manyDate
    val uuids    = manyUUID
    val uris     = manyURI
    val enums    = manyEnum("enum0", "enum1", "enum2", "enum3", "enum4", "enum5", "enum6", "enum7", "enum8", "enum9")
    val bytes    = manyByte
    val parents  = many[Ns]
    val refs1    = many[Ref1]
    val refsSub1 = many[Ref1].isComponent

    val strMap    = mapString.fulltext
    val intMap    = mapInt
    val longMap   = mapLong
    val floatMap  = mapFloat
    val doubleMap = mapDouble
    val boolMap   = mapBoolean
    val bigIntMap = mapBigInt
    val bigDecMap = mapBigDecimal
    val dateMap   = mapDate
    val uuidMap   = mapUUID
    val uriMap    = mapURI
    val byteMap   = mapByte
  }

  trait Ref1 {
    val str1    = oneString.fulltext
    val int1    = oneInt
    val enum1   = oneEnum("enum10", "enum11", "enum12")
    val ref2    = one[Ref2]
    val refSub2 = one[Ref2].isComponent

    val strs1    = manyString
    val ints1    = manyInt
    val enums1   = manyEnum("enum10", "enum11", "enum12")
    val refs2    = many[Ref2]
    val refsSub2 = many[Ref2].isComponent

    val intMap1 = mapInt
  }

  trait Ref2 {
    val str2  = oneString.uniqueIdentity.fulltext
    val int2  = oneInt.uniqueValue
    val enum2 = oneEnum("enum20", "enum21", "enum22")
    val strs2 = manyString
    val ints2 = manyInt.noHistory
    val ref3  = one[Ref3]
    val refs3 = many[Ref3]
  }

  trait Ref3 {
    val str3  = oneString
    val int3  = oneInt
    val enum3 = oneEnum("enum30", "enum31", "enum32")
    val strs3 = manyString
    val ints3 = manyInt
    val ref3  = one[Ref3]
    val refs3 = many[Ref3]
  }

  trait Ref4 {
    val str4  = oneString
    val int4  = oneInt
    val enum4 = oneEnum("enum40", "enum41", "enum42")
    val strs4 = manyString
    val ints4 = manyInt
  }
}