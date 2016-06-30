package molecule.util.schema

import molecule.dsl.schemaDefinition._

@InOut(3, 22)
object CoreTestDefinition {

  trait Ns {
    val str     = oneString.fullTextSearch
    val int     = oneInt
    val long    = oneLong
    val float   = oneFloat
    val double  = oneDouble
    val bool    = oneBoolean
    val date    = oneDate
    val uuid    = oneUUID
    val uri     = oneURI
    val enum    = oneEnum('enum0, 'enum1, 'enum2, 'enum3, 'enum4, 'enum5, 'enum6, 'enum7, 'enum8, 'enum9)
    val parent  = one[Ns]
    val ref1    = one[Ref1]
    val refSub1 = one[Ref1].subComponent
//    val oneNs   = oneBi[Ns]

    val strs     = manyString.fullTextSearch
    val ints     = manyInt
    val longs    = manyLong
    val floats   = manyFloat
    val doubles  = manyDouble
    val bools    = manyBoolean
    val dates    = manyDate
    val uuids    = manyUUID
    val uris     = manyURI
    val enums    = manyEnum('enum0, 'enum1, 'enum2, 'enum3, 'enum4, 'enum5, 'enum6, 'enum7, 'enum8, 'enum9)
    val parents  = many[Ns]
    val refs1    = many[Ref1]
    val refsSub1 = many[Ref1].subComponents
//    val manyNs   = manyBi[Ns]

    val strMap    = mapString.fullTextSearch
    val intMap    = mapInt
    val longMap   = mapLong
    val floatMap  = mapFloat
    val doubleMap = mapDouble
    val boolMap   = mapBoolean
    val dateMap   = mapDate
    val uuidMap   = mapUUID
    val uriMap    = mapURI
  }
  object Ns extends Ns

  trait Ref1 {
    val str1     = oneString
    val int1     = oneInt
    val enum1    = oneEnum('enum10, 'enum11, 'enum12)
    val ref2     = one[Ref2]
    val refSub2  = one[Ref2].subComponent

    val strs1    = manyString
    val ints1    = manyInt
    val refs2    = many[Ref2]
    val refsSub2 = many[Ref2].subComponents
  }
  object Ref1 extends Ref1

  trait Ref2 {
    val str2  = oneString
    val int2  = oneInt
    val enum2 = oneEnum('enum20, 'enum21, 'enum22)
    val strs2 = manyString
    val ints2 = manyInt
  }
}