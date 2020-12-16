package molecule.tests.core.nested.schema

import molecule.core.schema.definition._

@InOut(0, 3)
object NestedDefinition {

  trait Ns0 {
    val s0 = oneString
    val i0 = oneInt
    val r1 = many[Ns1]
  }

  trait Ns1 {
    val s1 = oneString
    val i1 = oneInt
    val r2 = many[Ns2]
  }

  trait Ns2 {
    val s2 = oneString
    val i2 = oneInt
    val r3 = many[Ns3]
  }

  trait Ns3 {
    val s3 = oneString
    val i3 = oneInt
    val r4 = many[Ns4]
  }

  trait Ns4 {
    val s4 = oneString
    val i4 = oneInt
    val r5 = many[Ns5]
  }

  trait Ns5 {
    val s5 = oneString
    val i5 = oneInt
    val r6 = many[Ns6]
  }

  trait Ns6 {
    val s6 = oneString
    val i6 = oneInt
    val r7 = many[Ns7]
  }

  trait Ns7 {
    val s7 = oneString
    val i7 = oneInt
  }
}