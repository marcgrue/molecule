package molecule.generic

import molecule.boilerplate.attributes._
import molecule.boilerplate.base.FirstNS
import molecule.boilerplate.dummyTypes._
import molecule.boilerplate.out._
import molecule.boilerplate.in1._
import molecule.boilerplate.in2._
import molecule.boilerplate.in3._

/** Datomic Db structure facade constructor */
object Db extends Db_0 with FirstNS {

  /** Filter generic Db molecule by applying an entity id of type `Long`.
    *
    * @param eid  Entity id
    * @return Generic Db molecule to be further expanded with more generic attributes.
    */
  def apply(eid: Long): Db_0 = ???
}

/** Special API to query the Datomic Db structure.
  *
  * @see [[https://github.com/scalamolecule/molecule/tree/master/coretests/src/test/scala/molecule/coretests/generic/DbSchema.scala"L1 Manual]]
  * */
trait Db {
  /** Find attribute ident. */
  def ident: AnyRef = ???


  /** Adds value type to the generic Db molecule. */
  def valueType: AnyRef = ???

  /** Adds tacit value type to the generic Db molecule. */
  def valueType_ : AnyRef = ???
}

trait Db_0 extends Db with Out_0[Db_0, Db_1, Db_In_1_0, Db_In_1_1] {
  override def ident: OneString[Db_1[String], Db_In_1_1[String, String]] with Db_1[String] = ???
  override def valueType: OneString[Db_1[String], Db_In_1_1[String, String]] with Db_1[String] = ???

  override def valueType_ : OneString[Db_0, Db_In_1_0[String]] with Db_0 = ???
}

trait Db_1[A] extends Db with Out_1[Db_1, Db_2, Db_In_1_1, Db_In_1_2, A] {
  override def valueType  : OneString[Db_2[A, String], Db_In_1_2[String, A, String]] with Db_2[A, String] = ???

  override def valueType_ : OneString[Db_1[A], Db_In_1_1[String, A]] with Db_1[A] = ???
}

trait Db_2[A, B] extends Db with Out_2[Db_2, Db_3, Db_In_1_2, Db_In_1_3, A, B] {
  override def valueType  : OneString[Db_3[A, B, String], Db_In_1_3[String, A, B, String]] with Db_3[A, B, String] = ???

  override def valueType_ : OneString[Db_2[A, B], Db_In_1_2[String, A, B]] with Db_2[A, B] = ???
}

trait Db_3[A, B, C] extends Db with Out_3[Db_3, Db_4, Db_In_1_3, Db_In_1_4, A, B, C] {
  override def valueType  : OneString[Db_4[A, B, C, String], Db_In_1_4[String, A, B, C, String]] with Db_4[A, B, C, String] = ???

  override def valueType_ : OneString[Db_3[A, B, C], Db_In_1_3[String, A, B, C]] with Db_3[A, B, C] = ???
}

trait Db_4[A, B, C, D] extends Db with Out_4[Db_4, Db_5, Db_In_1_4, Db_In_1_5, A, B, C, D] {
  override def valueType  : OneString[Db_5[A, B, C, D, String], Db_In_1_5[String, A, B, C, D, String]] with Db_5[A, B, C, D, String] = ???

  override def valueType_ : OneString[Db_4[A, B, C, D], Db_In_1_4[String, A, B, C, D]] with Db_4[A, B, C, D] = ???
}

trait Db_5[A, B, C, D, E] extends Db with Out_5[Db_5, Db_6, Db_In_1_5, Db_In_1_6, A, B, C, D, E] {
  override def valueType  : OneString[Db_6[A, B, C, D, E, String], Db_In_1_6[String, A, B, C, D, E, String]] with Db_6[A, B, C, D, E, String] = ???

  override def valueType_ : OneString[Db_5[A, B, C, D, E], Db_In_1_5[String, A, B, C, D, E]] with Db_5[A, B, C, D, E] = ???
}

trait Db_6[A, B, C, D, E, F] extends Db with Out_6[Db_6, P7, Db_In_1_6, P8, A, B, C, D, E, F] {
  override def valueType_ : OneString[Db_6[A, B, C, D, E, F], Db_In_1_6[String, A, B, C, D, E, F]] with Db_6[A, B, C, D, E, F] = ???
}


/********* Input molecules awaiting 1 input *******************************/

trait Db_In_1_0[I1] extends Db with In_1_0[Db_In_1_0, Db_In_1_1, Db_In_2_0, Db_In_2_1, I1] {
  override def valueType  : OneString[Db_In_1_1[I1, String], Db_In_2_1[I1, String, String]] with Db_In_1_1[I1, String] = ???

  override def valueType_ : OneString[Db_In_1_0[I1], Db_In_2_0[I1, String]] with Db_In_1_0[I1] = ???
}

trait Db_In_1_1[I1, A] extends Db with In_1_1[Db_In_1_1, Db_In_1_2, Db_In_2_1, Db_In_2_2, I1, A] {
  override def valueType  : OneString[Db_In_1_2[I1, A, String], Db_In_2_2[I1, String, A, String]] with Db_In_1_2[I1, A, String] = ???

  override def valueType_ : OneString[Db_In_1_1[I1, A], Db_In_2_1[I1, String, A]] with Db_In_1_1[I1, A] = ???
}

trait Db_In_1_2[I1, A, B] extends Db with In_1_2[Db_In_1_2, Db_In_1_3, Db_In_2_2, Db_In_2_3, I1, A, B] {
  override def valueType  : OneString[Db_In_1_3[I1, A, B, String], Db_In_2_3[I1, String, A, B, String]] with Db_In_1_3[I1, A, B, String] = ???

  override def valueType_ : OneString[Db_In_1_2[I1, A, B], Db_In_2_2[I1, String, A, B]] with Db_In_1_2[I1, A, B] = ???
}

trait Db_In_1_3[I1, A, B, C] extends Db with In_1_3[Db_In_1_3, Db_In_1_4, Db_In_2_3, Db_In_2_4, I1, A, B, C] {
  override def valueType  : OneString[Db_In_1_4[I1, A, B, C, String], Db_In_2_4[I1, String, A, B, C, String]] with Db_In_1_4[I1, A, B, C, String] = ???

  override def valueType_ : OneString[Db_In_1_3[I1, A, B, C], Db_In_2_3[I1, String, A, B, C]] with Db_In_1_3[I1, A, B, C] = ???
}

trait Db_In_1_4[I1, A, B, C, D] extends Db with In_1_4[Db_In_1_4, Db_In_1_5, Db_In_2_4, Db_In_2_5, I1, A, B, C, D] {
  override def valueType  : OneString[Db_In_1_5[I1, A, B, C, D, String], Db_In_2_5[I1, String, A, B, C, D, String]] with Db_In_1_5[I1, A, B, C, D, String] = ???

  override def valueType_ : OneString[Db_In_1_4[I1, A, B, C, D], Db_In_2_4[I1, String, A, B, C, D]] with Db_In_1_4[I1, A, B, C, D] = ???
}

trait Db_In_1_5[I1, A, B, C, D, E] extends Db with In_1_5[Db_In_1_5, Db_In_1_6, Db_In_2_5, Db_In_2_6, I1, A, B, C, D, E] {
  override def valueType  : OneString[Db_In_1_6[I1, A, B, C, D, E, String], Db_In_2_6[I1, String, A, B, C, D, E, String]] with Db_In_1_6[I1, A, B, C, D, E, String] = ???

  override def valueType_ : OneString[Db_In_1_5[I1, A, B, C, D, E], Db_In_2_5[I1, String, A, B, C, D, E]] with Db_In_1_5[I1, A, B, C, D, E] = ???
}

trait Db_In_1_6[I1, A, B, C, D, E, F] extends Db with In_1_6[Db_In_1_6, P8, Db_In_2_6, P9, I1, A, B, C, D, E, F] {
  override def valueType_ : OneString[Db_In_1_6[I1, A, B, C, D, E, F], Db_In_2_6[I1, String, A, B, C, D, E, F]] with Db_In_1_6[I1, A, B, C, D, E, F] = ???
}


/********* Input molecules awaiting 2 inputs *******************************/

trait Db_In_2_0[I1, I2] extends Db with In_2_0[Db_In_2_0, Db_In_2_1, Db_In_3_0, Db_In_3_1, I1, I2] {
  override def valueType  : OneString[Db_In_2_1[I1, I2, String], Db_In_3_1[I1, I2, String, String]] with Db_In_2_1[I1, I2, String] = ???

  override def valueType_ : OneString[Db_In_2_0[I1, I2], Db_In_3_0[I1, I2, String]] with Db_In_2_0[I1, I2] = ???
}

trait Db_In_2_1[I1, I2, A] extends Db with In_2_1[Db_In_2_1, Db_In_2_2, Db_In_3_1, Db_In_3_2, I1, I2, A] {
  override def valueType  : OneString[Db_In_2_2[I1, I2, A, String], Db_In_3_2[I1, I2, String, A, String]] with Db_In_2_2[I1, I2, A, String] = ???

  override def valueType_ : OneString[Db_In_2_1[I1, I2, A], Db_In_3_1[I1, I2, String, A]] with Db_In_2_1[I1, I2, A] = ???
}

trait Db_In_2_2[I1, I2, A, B] extends Db with In_2_2[Db_In_2_2, Db_In_2_3, Db_In_3_2, Db_In_3_3, I1, I2, A, B] {
  override def valueType  : OneString[Db_In_2_3[I1, I2, A, B, String], Db_In_3_3[I1, I2, String, A, B, String]] with Db_In_2_3[I1, I2, A, B, String] = ???

  override def valueType_ : OneString[Db_In_2_2[I1, I2, A, B], Db_In_3_2[I1, I2, String, A, B]] with Db_In_2_2[I1, I2, A, B] = ???
}

trait Db_In_2_3[I1, I2, A, B, C] extends Db with In_2_3[Db_In_2_3, Db_In_2_4, Db_In_3_3, Db_In_3_4, I1, I2, A, B, C] {
  override def valueType  : OneString[Db_In_2_4[I1, I2, A, B, C, String], Db_In_3_4[I1, I2, String, A, B, C, String]] with Db_In_2_4[I1, I2, A, B, C, String] = ???

  override def valueType_ : OneString[Db_In_2_3[I1, I2, A, B, C], Db_In_3_3[I1, I2, String, A, B, C]] with Db_In_2_3[I1, I2, A, B, C] = ???
}

trait Db_In_2_4[I1, I2, A, B, C, D] extends Db with In_2_4[Db_In_2_4, Db_In_2_5, Db_In_3_4, Db_In_3_5, I1, I2, A, B, C, D] {
  override def valueType  : OneString[Db_In_2_5[I1, I2, A, B, C, D, String], Db_In_3_5[I1, I2, String, A, B, C, D, String]] with Db_In_2_5[I1, I2, A, B, C, D, String] = ???

  override def valueType_ : OneString[Db_In_2_4[I1, I2, A, B, C, D], Db_In_3_4[I1, I2, String, A, B, C, D]] with Db_In_2_4[I1, I2, A, B, C, D] = ???
}

trait Db_In_2_5[I1, I2, A, B, C, D, E] extends Db with In_2_5[Db_In_2_5, Db_In_2_6, Db_In_3_5, Db_In_3_6, I1, I2, A, B, C, D, E] {
  override def valueType  : OneString[Db_In_2_6[I1, I2, A, B, C, D, E, String], Db_In_3_6[I1, I2, String, A, B, C, D, E, String]] with Db_In_2_6[I1, I2, A, B, C, D, E, String] = ???

  override def valueType_ : OneString[Db_In_2_5[I1, I2, A, B, C, D, E], Db_In_3_5[I1, I2, String, A, B, C, D, E]] with Db_In_2_5[I1, I2, A, B, C, D, E] = ???
}

trait Db_In_2_6[I1, I2, A, B, C, D, E, F] extends Db with In_2_6[Db_In_2_6, P9, Db_In_3_6, P10, I1, I2, A, B, C, D, E, F] {
  override def valueType_ : OneString[Db_In_2_6[I1, I2, A, B, C, D, E, F], Db_In_3_6[I1, I2, String, A, B, C, D, E, F]] with Db_In_2_6[I1, I2, A, B, C, D, E, F] = ???
}


/********* Input molecules awaiting 3 inputs *******************************/

trait Db_In_3_0[I1, I2, I3] extends Db with In_3_0[Db_In_3_0, Db_In_3_1, P4, P5, I1, I2, I3] {
  override def valueType  : OneString[Db_In_3_1[I1, I2, I3, String], P5[_,_,_,_,_]] with Db_In_3_1[I1, I2, I3, String] = ???

  override def valueType_ : OneString[Db_In_3_0[I1, I2, I3], P4[_,_,_,_]] with Db_In_3_0[I1, I2, I3] = ???
}

trait Db_In_3_1[I1, I2, I3, A] extends Db with In_3_1[Db_In_3_1, Db_In_3_2, P5, P6, I1, I2, I3, A] {
  override def valueType  : OneString[Db_In_3_2[I1, I2, I3, A, String], P6[_,_,_,_,_,_]] with Db_In_3_2[I1, I2, I3, A, String] = ???

  override def valueType_ : OneString[Db_In_3_1[I1, I2, I3, A], P5[_,_,_,_,_]] with Db_In_3_1[I1, I2, I3, A] = ???
}

trait Db_In_3_2[I1, I2, I3, A, B] extends Db with In_3_2[Db_In_3_2, Db_In_3_3, P6, P7, I1, I2, I3, A, B] {
  override def valueType  : OneString[Db_In_3_3[I1, I2, I3, A, B, String], P7[_,_,_,_,_,_,_]] with Db_In_3_3[I1, I2, I3, A, B, String] = ???

  override def valueType_ : OneString[Db_In_3_2[I1, I2, I3, A, B], P6[_,_,_,_,_,_]] with Db_In_3_2[I1, I2, I3, A, B] = ???
}

trait Db_In_3_3[I1, I2, I3, A, B, C] extends Db with In_3_3[Db_In_3_3, Db_In_3_4, P7, P8, I1, I2, I3, A, B, C] {
  override def valueType  : OneString[Db_In_3_4[I1, I2, I3, A, B, C, String], P8[_,_,_,_,_,_,_,_]] with Db_In_3_4[I1, I2, I3, A, B, C, String] = ???

  override def valueType_ : OneString[Db_In_3_3[I1, I2, I3, A, B, C], P7[_,_,_,_,_,_,_]] with Db_In_3_3[I1, I2, I3, A, B, C] = ???
}

trait Db_In_3_4[I1, I2, I3, A, B, C, D] extends Db with In_3_4[Db_In_3_4, Db_In_3_5, P8, P9, I1, I2, I3, A, B, C, D] {
  override def valueType  : OneString[Db_In_3_5[I1, I2, I3, A, B, C, D, String], P9[_,_,_,_,_,_,_,_,_]] with Db_In_3_5[I1, I2, I3, A, B, C, D, String] = ???

  override def valueType_ : OneString[Db_In_3_4[I1, I2, I3, A, B, C, D], P8[_,_,_,_,_,_,_,_]] with Db_In_3_4[I1, I2, I3, A, B, C, D] = ???
}

trait Db_In_3_5[I1, I2, I3, A, B, C, D, E] extends Db with In_3_5[Db_In_3_5, Db_In_3_6, P9, P10, I1, I2, I3, A, B, C, D, E] {
  override def valueType  : OneString[Db_In_3_6[I1, I2, I3, A, B, C, D, E, String], P10[_,_,_,_,_,_,_,_,_,_]] with Db_In_3_6[I1, I2, I3, A, B, C, D, E, String] = ???

  override def valueType_ : OneString[Db_In_3_5[I1, I2, I3, A, B, C, D, E], P9[_,_,_,_,_,_,_,_,_]] with Db_In_3_5[I1, I2, I3, A, B, C, D, E] = ???
}

trait Db_In_3_6[I1, I2, I3, A, B, C, D, E, F] extends Db with In_3_6[Db_In_3_6, P10, P10, P11, I1, I2, I3, A, B, C, D, E, F] {
  override def valueType_ : OneString[Db_In_3_6[I1, I2, I3, A, B, C, D, E, F], P10[_,_,_,_,_,_,_,_,_,_]] with Db_In_3_6[I1, I2, I3, A, B, C, D, E, F] = ???
}