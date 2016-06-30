package molecule.bidirectional

import molecule.dsl.schemaDefinition._

@InOut(2, 2)
object BidirectionalDefinition {

  trait Ns1 {

    // self-ref ..........................

//    val one   = oneBi[Ns1]
//    val `one` = oneBi[Ns1]

//    val many   = manyBi[Ns1]
//    val `many` = manyBi[Ns1]

    val z = oneInt
    //    val one = oneBi

    //    val one3 = oneBi[Ns1.str.type]
    //    val one4 = oneBi[Ns1.otherRef.type]


    // ref to other ns ...................

  }
  object Ns1 extends Ns1



  // Property edge namespace
//  trait Ns2 {
//
//    // Mandatory card-one reverse ref pointing back to `Ns1`
////    val ns1 = one[Ns1]
//    // works also
//    //    val buddy = one[Ns1]
//    //    val `ns1` = one[Ns1]
//    // No reverse ref
//    //      [error] (moleculeCoretest/compile:managedSources) Error in ModernGraphDefinition:
//    //      [error] Can't find reverse ref in namespace `Ns2` pointing back to `Ns1`.
//    //      [error] Expecting something like:
//    //      [error]   val ns1 = one[Ns1]
//
//  }
//  object Ns2 extends Ns2
}