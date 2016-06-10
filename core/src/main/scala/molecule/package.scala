
import java.net.URI
import java.util.{Date, UUID}
import datomic._
import molecule.api._
import molecule.dsl._
import molecule.ast.model._
import molecule.factory._
import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.language.higherKinds

package object molecule {

  // Generic interfaces
//import dsl.schemaDSL._

  implicit def m[T1, T2](dsl: Free2[T1, T2]): Molecule2[T1, T2] = macro MakeMolecule.from2tuples[T1, T2]
  implicit def m[T1, T2, T3](dsl: Free3[T1, T2, T3]): Molecule3[T1, T2, T3] = macro MakeMolecule.from3tuples[T1, T2, T3]

  implicit def m[Ns0, Ns1[_], In1_0[_], In1_1[_,_]](dsl: Out_0[Ns0, Ns1, In1_0, In1_1]): Molecule0 = macro MakeMolecule.from0attr[Ns0, Ns1, In1_0, In1_1]
  implicit def m[Ns1[_], Ns2[_,_], In1_1[_,_], In1_2[_,_,_], A](dsl: Out_1[Ns1, Ns2, In1_1, In1_2, A]): Molecule1[A] = macro MakeMolecule.from1attr[Ns1, Ns2, In1_1, In1_2, A]
  implicit def m[Ns2[_,_], Ns3[_,_,_], In1_2[_,_,_], In1_3[_,_,_,_], A, B](dsl: Out_2[Ns2, Ns3, In1_2, In1_3, A, B]): Molecule2[A, B] = macro MakeMolecule.from2attr[Ns2, Ns3, In1_2, In1_3, A, B]
  implicit def m[Ns3[_,_,_], Ns4[_,_,_,_], In1_3[_,_,_,_], In1_4[_,_,_,_,_], A, B, C](dsl: Out_3[Ns3, Ns4, In1_3, In1_4, A, B, C]): Molecule3[A, B, C] = macro MakeMolecule.from3attr[Ns3, Ns4, In1_3, In1_4, A, B, C]
  implicit def m[Ns4[_,_,_,_], Ns5[_,_,_,_,_], In1_4[_,_,_,_,_], In1_5[_,_,_,_,_,_], A, B, C, D](dsl: Out_4[Ns4, Ns5, In1_4, In1_5, A, B, C, D]): Molecule4[A, B, C, D] = macro MakeMolecule.from4attr[Ns4, Ns5, In1_4, In1_5, A, B, C, D]
  implicit def m[Ns5[_,_,_,_,_], Ns6[_,_,_,_,_,_], In1_5[_,_,_,_,_,_], In1_6[_,_,_,_,_,_,_], A, B, C, D, E](dsl: Out_5[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]): Molecule5[A, B, C, D, E] = macro MakeMolecule.from5attr[Ns5, Ns6, In1_5, In1_6, A, B, C, D, E]
  implicit def m[Ns6[_,_,_,_,_,_], Ns7[_,_,_,_,_,_,_], In1_6[_,_,_,_,_,_,_], In1_7[_,_,_,_,_,_,_,_], A, B, C, D, E, F](dsl: Out_6[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]): Molecule6[A, B, C, D, E, F] = macro MakeMolecule.from6attr[Ns6, Ns7, In1_6, In1_7, A, B, C, D, E, F]
  implicit def m[Ns7[_,_,_,_,_,_,_], Ns8[_,_,_,_,_,_,_,_], In1_7[_,_,_,_,_,_,_,_], In1_8[_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G](dsl: Out_7[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]): Molecule7[A, B, C, D, E, F, G] = macro MakeMolecule.from7attr[Ns7, Ns8, In1_7, In1_8, A, B, C, D, E, F, G]
  implicit def m[Ns8[_,_,_,_,_,_,_,_], Ns9[_,_,_,_,_,_,_,_,_], In1_8[_,_,_,_,_,_,_,_,_], In1_9[_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H](dsl: Out_8[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]): Molecule8[A, B, C, D, E, F, G, H] = macro MakeMolecule.from8attr[Ns8, Ns9, In1_8, In1_9, A, B, C, D, E, F, G, H]
  implicit def m[Ns9[_,_,_,_,_,_,_,_,_], Ns10[_,_,_,_,_,_,_,_,_,_], In1_9[_,_,_,_,_,_,_,_,_,_], In1_10[_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I](dsl: Out_9[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]): Molecule9[A, B, C, D, E, F, G, H, I] = macro MakeMolecule.from9attr[Ns9, Ns10, In1_9, In1_10, A, B, C, D, E, F, G, H, I]
  implicit def m[Ns10[_,_,_,_,_,_,_,_,_,_], Ns11[_,_,_,_,_,_,_,_,_,_,_], In1_10[_,_,_,_,_,_,_,_,_,_,_], In1_11[_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J](dsl: Out_10[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]): Molecule10[A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule.from10attr[Ns10, Ns11, In1_10, In1_11, A, B, C, D, E, F, G, H, I, J]
  implicit def m[Ns11[_,_,_,_,_,_,_,_,_,_,_], Ns12[_,_,_,_,_,_,_,_,_,_,_,_], In1_11[_,_,_,_,_,_,_,_,_,_,_,_], In1_12[_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K](dsl: Out_11[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule.from11attr[Ns11, Ns12, In1_11, In1_12, A, B, C, D, E, F, G, H, I, J, K]
  implicit def m[Ns12[_,_,_,_,_,_,_,_,_,_,_,_], Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], In1_12[_,_,_,_,_,_,_,_,_,_,_,_,_], In1_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L](dsl: Out_12[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule.from12attr[Ns12, Ns13, In1_12, In1_13, A, B, C, D, E, F, G, H, I, J, K, L]
  implicit def m[Ns13[_,_,_,_,_,_,_,_,_,_,_,_,_], Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: Out_13[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule.from13attr[Ns13, Ns14, In1_13, In1_14, A, B, C, D, E, F, G, H, I, J, K, L, M]
  implicit def m[Ns14[_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: Out_14[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule.from14attr[Ns14, Ns15, In1_14, In1_15, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  implicit def m[Ns15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: Out_15[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule.from15attr[Ns15, Ns16, In1_15, In1_16, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  implicit def m[Ns16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: Out_16[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule.from16attr[Ns16, Ns17, In1_16, In1_17, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  implicit def m[Ns17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: Out_17[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule.from17attr[Ns17, Ns18, In1_17, In1_18, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  implicit def m[Ns18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: Out_18[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule.from18attr[Ns18, Ns19, In1_18, In1_19, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  implicit def m[Ns19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: Out_19[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule.from19attr[Ns19, Ns20, In1_19, In1_20, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  implicit def m[Ns20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: Out_20[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule.from20attr[Ns20, Ns21, In1_20, In1_21, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  implicit def m[Ns21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: Out_21[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule.from21attr[Ns21, Ns22, In1_21, In1_22, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  implicit def m[Ns22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], Ns23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](dsl: Out_22[Ns22, Ns23, In1_22, In1_23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeMolecule.from22attr[Ns22, Ns23, In1_22, In1_23, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]


  // Input molecules awaiting 1 input

  implicit def m[In1_0[_], In1_1[_,_], In2_0[_,_], In2_1[_,_,_], I1] (inputDsl: In_1_0[In1_0, In1_1, In2_0, In2_1, I1]): InputMolecule_1_0[I1] = macro MakeInputMolecule.await_1_0[In1_0, In1_1, In2_0, In2_1, I1]
  implicit def m[In1_1[_,_], In1_2[_,_,_], In2_1[_,_,_], In2_2[_,_,_,_], I1 , A] (inputDsl: In_1_1[In1_1, In1_2, In2_1, In2_2, I1, A]): InputMolecule_1_1[I1, A] = macro MakeInputMolecule.await_1_1[In1_1, In1_2, In2_1, In2_2, I1, A]
  implicit def m[In1_2[_,_,_], In1_3[_,_,_,_], In2_2[_,_,_,_], In2_3[_,_,_,_,_], I1 , A, B] (inputDsl: In_1_2[In1_2, In1_3, In2_2, In2_3, I1, A, B]): InputMolecule_1_2[I1, A, B] = macro MakeInputMolecule.await_1_2[In1_2, In1_3, In2_2, In2_3, I1, A, B]
  implicit def m[In1_3[_,_,_,_], In1_4[_,_,_,_,_], In2_3[_,_,_,_,_], In2_4[_,_,_,_,_,_], I1 , A, B, C] (inputDsl: In_1_3[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]): InputMolecule_1_3[I1, A, B, C] = macro MakeInputMolecule.await_1_3[In1_3, In1_4, In2_3, In2_4, I1, A, B, C]
  implicit def m[In1_4[_,_,_,_,_], In1_5[_,_,_,_,_,_], In2_4[_,_,_,_,_,_], In2_5[_,_,_,_,_,_,_], I1 , A, B, C, D] (inputDsl: In_1_4[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]): InputMolecule_1_4[I1, A, B, C, D] = macro MakeInputMolecule.await_1_4[In1_4, In1_5, In2_4, In2_5, I1, A, B, C, D]
  implicit def m[In1_5[_,_,_,_,_,_], In1_6[_,_,_,_,_,_,_], In2_5[_,_,_,_,_,_,_], In2_6[_,_,_,_,_,_,_,_], I1 , A, B, C, D, E] (inputDsl: In_1_5[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]): InputMolecule_1_5[I1, A, B, C, D, E] = macro MakeInputMolecule.await_1_5[In1_5, In1_6, In2_5, In2_6, I1, A, B, C, D, E]
  implicit def m[In1_6[_,_,_,_,_,_,_], In1_7[_,_,_,_,_,_,_,_], In2_6[_,_,_,_,_,_,_,_], In2_7[_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F] (inputDsl: In_1_6[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]): InputMolecule_1_6[I1, A, B, C, D, E, F] = macro MakeInputMolecule.await_1_6[In1_6, In1_7, In2_6, In2_7, I1, A, B, C, D, E, F]
  implicit def m[In1_7[_,_,_,_,_,_,_,_], In1_8[_,_,_,_,_,_,_,_,_], In2_7[_,_,_,_,_,_,_,_,_], In2_8[_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G] (inputDsl: In_1_7[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]): InputMolecule_1_7[I1, A, B, C, D, E, F, G] = macro MakeInputMolecule.await_1_7[In1_7, In1_8, In2_7, In2_8, I1, A, B, C, D, E, F, G]
  implicit def m[In1_8[_,_,_,_,_,_,_,_,_], In1_9[_,_,_,_,_,_,_,_,_,_], In2_8[_,_,_,_,_,_,_,_,_,_], In2_9[_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H] (inputDsl: In_1_8[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_8[I1, A, B, C, D, E, F, G, H] = macro MakeInputMolecule.await_1_8[In1_8, In1_9, In2_8, In2_9, I1, A, B, C, D, E, F, G, H]
  implicit def m[In1_9[_,_,_,_,_,_,_,_,_,_], In1_10[_,_,_,_,_,_,_,_,_,_,_], In2_9[_,_,_,_,_,_,_,_,_,_,_], In2_10[_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I] (inputDsl: In_1_9[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_9[I1, A, B, C, D, E, F, G, H, I] = macro MakeInputMolecule.await_1_9[In1_9, In1_10, In2_9, In2_10, I1, A, B, C, D, E, F, G, H, I]
  implicit def m[In1_10[_,_,_,_,_,_,_,_,_,_,_], In1_11[_,_,_,_,_,_,_,_,_,_,_,_], In2_10[_,_,_,_,_,_,_,_,_,_,_,_], In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J] (inputDsl: In_1_10[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeInputMolecule.await_1_10[In1_10, In1_11, In2_10, In2_11, I1, A, B, C, D, E, F, G, H, I, J]
  implicit def m[In1_11[_,_,_,_,_,_,_,_,_,_,_,_], In1_12[_,_,_,_,_,_,_,_,_,_,_,_,_], In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K] (inputDsl: In_1_11[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeInputMolecule.await_1_11[In1_11, In1_12, In2_11, In2_12, I1, A, B, C, D, E, F, G, H, I, J, K]
  implicit def m[In1_12[_,_,_,_,_,_,_,_,_,_,_,_,_], In1_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L] (inputDsl: In_1_12[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeInputMolecule.await_1_12[In1_12, In1_13, In2_12, In2_13, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  implicit def m[In1_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M](inputDsl: In_1_13[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeInputMolecule.await_1_13[In1_13, In1_14, In2_13, In2_14, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  implicit def m[In1_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N](inputDsl: In_1_14[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeInputMolecule.await_1_14[In1_14, In1_15, In2_14, In2_15, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  implicit def m[In1_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](inputDsl: In_1_15[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeInputMolecule.await_1_15[In1_15, In1_16, In2_15, In2_16, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  implicit def m[In1_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](inputDsl: In_1_16[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeInputMolecule.await_1_16[In1_16, In1_17, In2_16, In2_17, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  implicit def m[In1_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](inputDsl: In_1_17[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeInputMolecule.await_1_17[In1_17, In1_18, In2_17, In2_18, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  implicit def m[In1_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](inputDsl: In_1_18[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeInputMolecule.await_1_18[In1_18, In1_19, In2_18, In2_19, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  implicit def m[In1_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](inputDsl: In_1_19[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeInputMolecule.await_1_19[In1_19, In1_20, In2_19, In2_20, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  implicit def m[In1_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](inputDsl: In_1_20[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeInputMolecule.await_1_20[In1_20, In1_21, In2_20, In2_21, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  implicit def m[In1_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](inputDsl: In_1_21[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeInputMolecule.await_1_21[In1_21, In1_22, In2_21, In2_22, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  implicit def m[In1_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In1_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1 , A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](inputDsl: In_1_22[In1_22, In1_23, In2_22, In2_23, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeInputMolecule.await_1_22[In1_22, In1_23, In2_22, In2_23, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]


  // Input molecules awaiting 2 inputs

  implicit def m[In2_0[_,_], In2_1[_,_,_], In3_0[_,_,_], In3_1[_,_,_,_], I1, I2] (inputDsl: In_2_0[In2_0, In2_1, In3_0, In3_1, I1, I2]): InputMolecule_2_0[I1, I2] = macro MakeInputMolecule.await_2_0[In2_0, In2_1, In3_0, In3_1, I1, I2]
  implicit def m[In2_1[_,_,_], In2_2[_,_,_,_], In3_1[_,_,_,_], In3_2[_,_,_,_,_], I1, I2, A] (inputDsl: In_2_1[In2_1, In2_2, In3_1, In3_2, I1, I2, A]): InputMolecule_2_1[I1, I2, A] = macro MakeInputMolecule.await_2_1[In2_1, In2_2, In3_1, In3_2, I1, I2, A]
  implicit def m[In2_2[_,_,_,_], In2_3[_,_,_,_,_], In3_2[_,_,_,_,_], In3_3[_,_,_,_,_,_], I1, I2, A, B] (inputDsl: In_2_2[In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]): InputMolecule_2_2[I1, I2, A, B] = macro MakeInputMolecule.await_2_2[In2_2, In2_3, In3_2, In3_3, I1, I2, A, B]
  implicit def m[In2_3[_,_,_,_,_], In2_4[_,_,_,_,_,_], In3_3[_,_,_,_,_,_], In3_4[_,_,_,_,_,_,_], I1, I2, A, B, C] (inputDsl: In_2_3[In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]): InputMolecule_2_3[I1, I2, A, B, C] = macro MakeInputMolecule.await_2_3[In2_3, In2_4, In3_3, In3_4, I1, I2, A, B, C]
  implicit def m[In2_4[_,_,_,_,_,_], In2_5[_,_,_,_,_,_,_], In3_4[_,_,_,_,_,_,_], In3_5[_,_,_,_,_,_,_,_], I1, I2, A, B, C, D] (inputDsl: In_2_4[In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]): InputMolecule_2_4[I1, I2, A, B, C, D] = macro MakeInputMolecule.await_2_4[In2_4, In2_5, In3_4, In3_5, I1, I2, A, B, C, D]
  implicit def m[In2_5[_,_,_,_,_,_,_], In2_6[_,_,_,_,_,_,_,_], In3_5[_,_,_,_,_,_,_,_], In3_6[_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E] (inputDsl: In_2_5[In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]): InputMolecule_2_5[I1, I2, A, B, C, D, E] = macro MakeInputMolecule.await_2_5[In2_5, In2_6, In3_5, In3_6, I1, I2, A, B, C, D, E]
  implicit def m[In2_6[_,_,_,_,_,_,_,_], In2_7[_,_,_,_,_,_,_,_,_], In3_6[_,_,_,_,_,_,_,_,_], In3_7[_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F] (inputDsl: In_2_6[In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]): InputMolecule_2_6[I1, I2, A, B, C, D, E, F] = macro MakeInputMolecule.await_2_6[In2_6, In2_7, In3_6, In3_7, I1, I2, A, B, C, D, E, F]
  implicit def m[In2_7[_,_,_,_,_,_,_,_,_], In2_8[_,_,_,_,_,_,_,_,_,_], In3_7[_,_,_,_,_,_,_,_,_,_], In3_8[_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G] (inputDsl: In_2_7[In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]): InputMolecule_2_7[I1, I2, A, B, C, D, E, F, G] = macro MakeInputMolecule.await_2_7[In2_7, In2_8, In3_7, In3_8, I1, I2, A, B, C, D, E, F, G]
  implicit def m[In2_8[_,_,_,_,_,_,_,_,_,_], In2_9[_,_,_,_,_,_,_,_,_,_,_], In3_8[_,_,_,_,_,_,_,_,_,_,_], In3_9[_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H] (inputDsl: In_2_8[In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]): InputMolecule_2_8[I1, I2, A, B, C, D, E, F, G, H] = macro MakeInputMolecule.await_2_8[In2_8, In2_9, In3_8, In3_9, I1, I2, A, B, C, D, E, F, G, H]
  implicit def m[In2_9[_,_,_,_,_,_,_,_,_,_,_], In2_10[_,_,_,_,_,_,_,_,_,_,_,_], In3_9[_,_,_,_,_,_,_,_,_,_,_,_], In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I] (inputDsl: In_2_9[In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]): InputMolecule_2_9[I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeInputMolecule.await_2_9[In2_9, In2_10, In3_9, In3_10, I1, I2, A, B, C, D, E, F, G, H, I]
  implicit def m[In2_10[_,_,_,_,_,_,_,_,_,_,_,_], In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J] (inputDsl: In_2_10[In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]): InputMolecule_2_10[I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeInputMolecule.await_2_10[In2_10, In2_11, In3_10, In3_11, I1, I2, A, B, C, D, E, F, G, H, I, J]
  implicit def m[In2_11[_,_,_,_,_,_,_,_,_,_,_,_,_], In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K] (inputDsl: In_2_11[In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_2_11[I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeInputMolecule.await_2_11[In2_11, In2_12, In3_11, In3_12, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  implicit def m[In2_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] (inputDsl: In_2_12[In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_2_12[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeInputMolecule.await_2_12[In2_12, In2_13, In3_12, In3_13, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  implicit def m[In2_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](inputDsl: In_2_13[In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_2_13[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeInputMolecule.await_2_13[In2_13, In2_14, In3_13, In3_14, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  implicit def m[In2_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](inputDsl: In_2_14[In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_2_14[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeInputMolecule.await_2_14[In2_14, In2_15, In3_14, In3_15, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  implicit def m[In2_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](inputDsl: In_2_15[In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_2_15[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeInputMolecule.await_2_15[In2_15, In2_16, In3_15, In3_16, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  implicit def m[In2_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](inputDsl: In_2_16[In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_2_16[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeInputMolecule.await_2_16[In2_16, In2_17, In3_16, In3_17, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  implicit def m[In2_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](inputDsl: In_2_17[In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_2_17[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeInputMolecule.await_2_17[In2_17, In2_18, In3_17, In3_18, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  implicit def m[In2_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](inputDsl: In_2_18[In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_2_18[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeInputMolecule.await_2_18[In2_18, In2_19, In3_18, In3_19, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  implicit def m[In2_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](inputDsl: In_2_19[In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_2_19[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeInputMolecule.await_2_19[In2_19, In2_20, In3_19, In3_20, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  implicit def m[In2_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](inputDsl: In_2_20[In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_2_20[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeInputMolecule.await_2_20[In2_20, In2_21, In3_20, In3_21, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  implicit def m[In2_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](inputDsl: In_2_21[In2_21, In2_22, In3_21, In3_22, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_2_21[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeInputMolecule.await_2_21[In2_21, In2_22, In3_21, In3_22, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  implicit def m[In2_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In2_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](inputDsl: In_2_22[In2_22, In2_23, In3_22, In3_23, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): InputMolecule_2_22[I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeInputMolecule.await_2_22[In2_22, In2_23, In3_22, In3_23, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]


  // Input molecules awaiting 3 inputs

  implicit def m[In3_0[_,_,_], In3_1[_,_,_,_], In4_0[_,_,_,_], In4_1[_,_,_,_,_], I1, I2, I3] (inputDsl: In_3_0[In3_0, In3_1, In4_0, In4_1, I1, I2, I3]): InputMolecule_3_0[I1, I2, I3] = macro MakeInputMolecule.await_3_0[In3_0, In3_1, In4_0, In4_1, I1, I2, I3]
  implicit def m[In3_1[_,_,_,_], In3_2[_,_,_,_,_], In4_1[_,_,_,_,_], In4_2[_,_,_,_,_,_], I1, I2, I3, A] (inputDsl: In_3_1[In3_1, In3_2, In4_1, In4_2, I1, I2, I3, A]): InputMolecule_3_1[I1, I2, I3, A] = macro MakeInputMolecule.await_3_1[In3_1, In3_2, In4_1, In4_2, I1, I2, I3, A]
  implicit def m[In3_2[_,_,_,_,_], In3_3[_,_,_,_,_,_], In4_2[_,_,_,_,_,_], In4_3[_,_,_,_,_,_,_], I1, I2, I3, A, B] (inputDsl: In_3_2[In3_2, In3_3, In4_2, In4_3, I1, I2, I3, A, B]): InputMolecule_3_2[I1, I2, I3, A, B] = macro MakeInputMolecule.await_3_2[In3_2, In3_3, In4_2, In4_3, I1, I2, I3, A, B]
  implicit def m[In3_3[_,_,_,_,_,_], In3_4[_,_,_,_,_,_,_], In4_3[_,_,_,_,_,_,_], In4_4[_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C] (inputDsl: In_3_3[In3_3, In3_4, In4_3, In4_4, I1, I2, I3, A, B, C]): InputMolecule_3_3[I1, I2, I3, A, B, C] = macro MakeInputMolecule.await_3_3[In3_3, In3_4, In4_3, In4_4, I1, I2, I3, A, B, C]
  implicit def m[In3_4[_,_,_,_,_,_,_], In3_5[_,_,_,_,_,_,_,_], In4_4[_,_,_,_,_,_,_,_], In4_5[_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D] (inputDsl: In_3_4[In3_4, In3_5, In4_4, In4_5, I1, I2, I3, A, B, C, D]): InputMolecule_3_4[I1, I2, I3, A, B, C, D] = macro MakeInputMolecule.await_3_4[In3_4, In3_5, In4_4, In4_5, I1, I2, I3, A, B, C, D]
  implicit def m[In3_5[_,_,_,_,_,_,_,_], In3_6[_,_,_,_,_,_,_,_,_], In4_5[_,_,_,_,_,_,_,_,_], In4_6[_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E] (inputDsl: In_3_5[In3_5, In3_6, In4_5, In4_6, I1, I2, I3, A, B, C, D, E]): InputMolecule_3_5[I1, I2, I3, A, B, C, D, E] = macro MakeInputMolecule.await_3_5[In3_5, In3_6, In4_5, In4_6, I1, I2, I3, A, B, C, D, E]
  implicit def m[In3_6[_,_,_,_,_,_,_,_,_], In3_7[_,_,_,_,_,_,_,_,_,_], In4_6[_,_,_,_,_,_,_,_,_,_], In4_7[_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F] (inputDsl: In_3_6[In3_6, In3_7, In4_6, In4_7, I1, I2, I3, A, B, C, D, E, F]): InputMolecule_3_6[I1, I2, I3, A, B, C, D, E, F] = macro MakeInputMolecule.await_3_6[In3_6, In3_7, In4_6, In4_7, I1, I2, I3, A, B, C, D, E, F]
  implicit def m[In3_7[_,_,_,_,_,_,_,_,_,_], In3_8[_,_,_,_,_,_,_,_,_,_,_], In4_7[_,_,_,_,_,_,_,_,_,_,_], In4_8[_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G] (inputDsl: In_3_7[In3_7, In3_8, In4_7, In4_8, I1, I2, I3, A, B, C, D, E, F, G]): InputMolecule_3_7[I1, I2, I3, A, B, C, D, E, F, G] = macro MakeInputMolecule.await_3_7[In3_7, In3_8, In4_7, In4_8, I1, I2, I3, A, B, C, D, E, F, G]
  implicit def m[In3_8[_,_,_,_,_,_,_,_,_,_,_], In3_9[_,_,_,_,_,_,_,_,_,_,_,_], In4_8[_,_,_,_,_,_,_,_,_,_,_,_], In4_9[_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H] (inputDsl: In_3_8[In3_8, In3_9, In4_8, In4_9, I1, I2, I3, A, B, C, D, E, F, G, H]): InputMolecule_3_8[I1, I2, I3, A, B, C, D, E, F, G, H] = macro MakeInputMolecule.await_3_8[In3_8, In3_9, In4_8, In4_9, I1, I2, I3, A, B, C, D, E, F, G, H]
  implicit def m[In3_9[_,_,_,_,_,_,_,_,_,_,_,_], In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], In4_9[_,_,_,_,_,_,_,_,_,_,_,_,_], In4_10[_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I] (inputDsl: In_3_9[In3_9, In3_10, In4_9, In4_10, I1, I2, I3, A, B, C, D, E, F, G, H, I]): InputMolecule_3_9[I1, I2, I3, A, B, C, D, E, F, G, H, I] = macro MakeInputMolecule.await_3_9[In3_9, In3_10, In4_9, In4_10, I1, I2, I3, A, B, C, D, E, F, G, H, I]
  implicit def m[In3_10[_,_,_,_,_,_,_,_,_,_,_,_,_], In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_10[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J] (inputDsl: In_3_10[In3_10, In3_11, In4_10, In4_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]): InputMolecule_3_10[I1, I2, I3, A, B, C, D, E, F, G, H, I, J] = macro MakeInputMolecule.await_3_10[In3_10, In3_11, In4_10, In4_11, I1, I2, I3, A, B, C, D, E, F, G, H, I, J]
  implicit def m[In3_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_11[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K] (inputDsl: In_3_11[In3_11, In3_12, In4_11, In4_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_3_11[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K] = macro MakeInputMolecule.await_3_11[In3_11, In3_12, In4_11, In4_12, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K]
  implicit def m[In3_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_12[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L] (inputDsl: In_3_12[In3_12, In3_13, In4_12, In4_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_3_12[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeInputMolecule.await_3_12[In3_12, In3_13, In4_12, In4_13, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L]
  implicit def m[In3_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_13[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M](inputDsl: In_3_13[In3_13, In3_14, In4_13, In4_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_3_13[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeInputMolecule.await_3_13[In3_13, In3_14, In4_13, In4_14, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M]
  implicit def m[In3_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_14[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N](inputDsl: In_3_14[In3_14, In3_15, In4_14, In4_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_3_14[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeInputMolecule.await_3_14[In3_14, In3_15, In4_14, In4_15, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  implicit def m[In3_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](inputDsl: In_3_15[In3_15, In3_16, In4_15, In4_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_3_15[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeInputMolecule.await_3_15[In3_15, In3_16, In4_15, In4_16, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  implicit def m[In3_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](inputDsl: In_3_16[In3_16, In3_17, In4_16, In4_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_3_16[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeInputMolecule.await_3_16[In3_16, In3_17, In4_16, In4_17, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  implicit def m[In3_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](inputDsl: In_3_17[In3_17, In3_18, In4_17, In4_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_3_17[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeInputMolecule.await_3_17[In3_17, In3_18, In4_17, In4_18, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  implicit def m[In3_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](inputDsl: In_3_18[In3_18, In3_19, In4_18, In4_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_3_18[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeInputMolecule.await_3_18[In3_18, In3_19, In4_18, In4_19, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  implicit def m[In3_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](inputDsl: In_3_19[In3_19, In3_20, In4_19, In4_20, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_3_19[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeInputMolecule.await_3_19[In3_19, In3_20, In4_19, In4_20, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  implicit def m[In3_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](inputDsl: In_3_20[In3_20, In3_21, In4_20, In4_21, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_3_20[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeInputMolecule.await_3_20[In3_20, In3_21, In4_20, In4_21, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  implicit def m[In3_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](inputDsl: In_3_21[In3_21, In3_22, In4_21, In4_22, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_3_21[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeInputMolecule.await_3_21[In3_21, In3_22, In4_21, In4_22, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  implicit def m[In3_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In3_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], In4_23[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_], I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](inputDsl: In_3_22[In3_22, In3_23, In4_22, In4_23, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): InputMolecule_3_22[I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeInputMolecule.await_3_22[In3_22, In3_23, In4_22, In4_23, I1, I2, I3, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]


  // Attribute expressions

  implicit def string2Model (v: String ): TermValue[String]  = TermValue(v)
  implicit def int2Model    (v: Int    ): TermValue[Int]     = TermValue(v)
  implicit def long2Model   (v: Long   ): TermValue[Long]    = TermValue(v)
  implicit def float2Model  (v: Float  ): TermValue[Float]   = TermValue(v)
  implicit def double2Model (v: Double ): TermValue[Double]  = TermValue(v)
  implicit def boolean2Model(v: Boolean): TermValue[Boolean] = TermValue(v)
  implicit def date2Model   (v: Date   ): TermValue[Date]    = TermValue(v)
  implicit def uuid2Model   (v: UUID   ): TermValue[UUID]    = TermValue(v)
  implicit def uri2Model    (v: URI    ): TermValue[URI]     = TermValue(v)

  implicit def stringSet2Model (set: Set[String] ): TermValue[Set[String]]  = TermValue(set)
  implicit def intSet2Model    (set: Set[Int]    ): TermValue[Set[Int]]     = TermValue(set)
  implicit def longSet2Model   (set: Set[Long]   ): TermValue[Set[Long]]    = TermValue(set)
  implicit def floatSet2Model  (set: Set[Float]  ): TermValue[Set[Float]]   = TermValue(set)
  implicit def doubleSet2Model (set: Set[Double] ): TermValue[Set[Double]]  = TermValue(set)
  implicit def booleanSet2Model(set: Set[Boolean]): TermValue[Set[Boolean]] = TermValue(set)
  implicit def dateSet2Model   (set: Set[Date]   ): TermValue[Set[Date]]    = TermValue(set)
  implicit def uuidSet2Model   (set: Set[UUID]   ): TermValue[Set[UUID]]    = TermValue(set)
  implicit def uriSet2Model    (set: Set[URI]    ): TermValue[Set[URI]]     = TermValue(set)

//  implicit def strPair2Model    (pair: (String, String)): TermValue[(String, String)]  = TermValue(pair)
//  implicit def intPair2Model    (pair: (String, Int   )): TermValue[(String, Int)]     = TermValue(pair)

  implicit def tuple2Model[A, B](tpl: (A, B)): TermValue[(A, B)] = TermValue(tpl)

  // Entity api
  implicit def long2Entity(id: Long)(implicit conn: Connection): EntityFacade = EntityFacade(conn.db.entity(id), conn, id.asInstanceOf[Object])


  // Input marker
  trait ?
  object ? extends ?


  // Null marker (for non-asserted facts)
  trait nil
  object nil extends nil

  // Unifying marker for attributes to be unified in self-joind
  trait unify
  object unify extends unify


  // Aggregates ==========================================================

  // Aggregate attribute (singular/multiple)

  trait distinct
  object distinct extends distinct

  trait max
  private[molecule] trait maxs
  object max extends max { def apply(i: Int): maxs = ???}

  trait min
  private[molecule] trait mins
  object min extends min {def apply(i: Int): mins = ???}

  trait rand
  private[molecule] trait rands
  object rand extends rand { def apply(i: Int): rands = ???}

  trait sample
  private[molecule] trait samples
  object sample extends sample { def apply(i: Int): samples = ???}


  // Aggregate calculation

  trait count
  object count extends count

  trait countDistinct
  object countDistinct extends countDistinct

  trait sum
  object sum extends sum

  trait avg
  object avg extends avg

  trait median
  object median extends median

  trait variance
  object variance extends variance

  trait stddev
  object stddev extends stddev


  // Todo - should we have this one somewhere else?
  // Runtime organizing of mapped values
  implicit class MapAttr2MapOps[T](mapAttr: Option[Map[String, T]]) {

    def at(key: String): Option[T] = mapAttr.flatMap(_.get(key))

    def atOrElse(key: String, default: T): T = at(key).getOrElse(default)

    def mapAt(key1: String, keyN: String*): Map[String, T] = {
      val keys = key1 +: keyN
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => m.toList.foldLeft(Map[String, T]()) {
          case (acc, (k, v)) if keys.contains(k) => acc + (k -> v)
          case (acc, _)                          => acc
        }
      }
    }

    def values(needle1: T, needleN: T*): Map[String, T] = {
      val needles = needle1 +: needleN
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => needle1 match {
          case _: String => {
            val pattern = s"(?i).*(${needles.mkString("|")}).*".r.pattern
            m.toList.foldLeft(Map[String, T]()) {
              case (acc, (k, v)) if pattern.matcher(v.toString).matches => acc + (k -> v)
              case (acc, _)                                             => acc
            }
          }
          case _         => m.toList.foldLeft(Map[String, T]()) {
            case (acc, (k, v)) if needles.contains(v) => acc + (k -> v)
            case (acc, _)                             => acc
          }
        }
      }
    }

    def keyValue(keyNeedles: (String, T)*): Map[String, T] = {
      val subpatterns = keyNeedles.map { case (k, v) => s"$k.*$v.*" }.mkString("|")
      val pattern = s"(?i)($subpatterns)".r.pattern
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => m.toList.foldLeft(Map[String, T]()) {
          case (acc, (k, v)) if pattern.matcher(k + v).matches => acc + (k -> v)
          case (acc, _)                                        => acc
        }
      }
    }

    def keyValues(keyNeedle1: (String, Seq[T]), keyNeedleN: (String, Seq[T])*): Map[String, T] = {
      val keyNeedles = keyNeedle1 +: keyNeedleN
      val subpatterns = keyNeedles.map { case (k, vs) => s"$k.*(${vs.mkString("|")}).*" }.mkString("|")
      val pattern = s"(?i)($subpatterns)".r.pattern
      mapAttr match {
        case None    => Map[String, T]()
        case Some(m) => m.toList.foldLeft(Map[String, T]()) {
          case (acc, (k, v)) if pattern.matcher(k + v).matches => acc + (k -> v)
          case (acc, _)                                        => acc
        }
      }
    }
  }
}
