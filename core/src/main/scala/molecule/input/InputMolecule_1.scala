package molecule.input

import molecule.action.Molecule._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query._
import molecule.facade.Conn
import molecule.input.exception.InputMolecule_1_Exception


/** Shared interfaces of input molecules awaiting 1 input.
  * {{{
  *   // Sample data set
  *   Person.name.age insert List(
  *     ("Joe", 42),
  *     ("Liz", 34)
  *   )
  *
  *   // Input molecule awaiting 1 input for `name`
  *   val ageOfPersons = m(Person.name_(?).age)
  *
  *   // Resolve input molecule with name input in various ways
  *   ageOfPersons("Joe").get === List(42)
  *   ageOfPersons("Joe" or "Liz").get === List(42, 34)
  *   ageOfPersons("Joe", "Liz").get === List(42, 34)
  *   ageOfPersons(Seq("Joe", "Liz")).get === List(42, 34) // Accepts Iterable[I1]
  * }}}
  *
  * @see [[molecule.input.InputMolecule]]
  *      | [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  * @tparam I1 Type of input matching attribute with `?` marker
  */
trait InputMolecule_1[I1] extends InputMolecule {


  protected def bindValues(query: Query, inputs0: Seq[I1]): Query = {
    val inputs = inputs0.distinct

    query.i.inputs.size match {

      // Mapped attributes
      case 2 => {
        val inVars = query.i.inputs.collect { case Placeholder(_, _, v, _) => v }
        val Placeholder(_, KW(ns, attr, _), _, _) = query.i.inputs.head
        val values = inputs.flatMap {
          case map: Map[_, _] => map.toSeq.map { case (k, v) => Seq(k, v) }
          case other          => throw new InputMolecule_1_Exception(s"Unexpected input for mapped attribute `:$ns/$attr`: " + other)
        }
        query.copy(i = In(Seq(InVar(RelationBinding(inVars), values)), query.i.rules, query.i.ds))
      }

      // Card-one/many/mapK
      case 1 => {
        val List(ph: Placeholder) = query.i.inputs
        // Remove placeholder
        val q1 = query.copy(i = In(Nil, query.i.rules, query.i.ds))
        resolveInput(q1, ph, inputs)
      }
    }
  }


  /** Apply one or more input values to resolve input molecule.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply one or more input value(s)
    *   ageOfPersons.apply("Ben", "Liz") // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons(Set("Ben", "Liz"))
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(arg: I1)(implicit conn: Conn): MoleculeBase


  /** Apply one or more input values to resolve input molecule.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply one or more input value(s)
    *   ageOfPersons.apply("Ben", "Liz") // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons(Set("Ben", "Liz"))
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): MoleculeBase


  /** Apply Seq of input values with OR semantics to resolve input molecule.
    * <br><br>
    * Resolve input molecule by applying a Set of values that the attribute is expected to have (OR semantics).
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply Seq of one or more input value(s)
    *   ageOfPersons.apply(Seq("Ben", "Liz"))
    *
    *   // Same as
    *   ageOfPersons(Set("Ben", "Liz"))
    *   ageOfPersons("Ben" or "Liz")
    *   ageOfPersons("Ben", "Liz")
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the value(s) applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(args: Seq[I1])(implicit conn: Conn): MoleculeBase


  /** Apply OR expression of input values to resolve input molecule.
    * <br><br>
    * Input value type matches attribute having `?` marker.
    * {{{
    *   // Input molecule awaiting name input
    *   val ageOfPersons = Person.name_(?).age
    *
    *   // Apply OR expression of two or more input values
    *   ageOfPersons.apply("Ben" or "Liz") // (one or more input values...)
    *
    *   // Same as
    *   ageOfPersons("Ben", "Liz")
    *   ageOfPersons(Seq("Ben", "Liz"))
    *   ageOfPersons(Set("Ben", "Liz"))
    * }}}
    * Querying the resolved molecule will match all entities having `name` set to the values applied.
    *
    * @note Only distinct values are matched.
    * @return Resolved molecule that can be queried
    */
  def apply(or: Or[I1])(implicit conn: Conn): MoleculeBase
}


/** Implementations of input molecules awaiting 1 input, output arity 1-22 */
object InputMolecule_1 {

  abstract class InputMolecule_1_01[I1, A](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule01[A] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule01[A] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule01[A] // generated by macros
    def apply(or: Or[I1])(implicit conn: Conn): Molecule01[A] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_02[I1, A, B](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule02[A, B] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule02[A, B] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule02[A, B]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule02[A, B] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_03[I1, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule03[A, B, C] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule03[A, B, C] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule03[A, B, C]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_04[I1, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_05[I1, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_06[I1, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_07[I1, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_08[I1, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(or: Or[I1])(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr(or))
  }
}

/*

{

  abstract class InputMolecule_1_01[I1, A](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule01[A] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule01[A] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule01[A] // generated by macros
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule01[A] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_02[I1, A, B](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule02[A, B] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule02[A, B] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule02[A, B]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule02[A, B] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_03[I1, A, B, C](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule03[A, B, C] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule03[A, B, C] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule03[A, B, C]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule03[A, B, C] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_04[I1, A, B, C, D](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule04[A, B, C, D] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule04[A, B, C, D] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule04[A, B, C, D]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule04[A, B, C, D] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_05[I1, A, B, C, D, E](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule05[A, B, C, D, E]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule05[A, B, C, D, E] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_06[I1, A, B, C, D, E, F](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule06[A, B, C, D, E, F]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule06[A, B, C, D, E, F] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_07[I1, A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule07[A, B, C, D, E, F, G] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_08[I1, A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule08[A, B, C, D, E, F, G, H] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule09[A, B, C, D, E, F, G, H, I] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule10[A, B, C, D, E, F, G, H, I, J] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule11[A, B, C, D, E, F, G, H, I, J, K] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule12[A, B, C, D, E, F, G, H, I, J, K, L] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = apply(resolveOr(or))
  }

  abstract class InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends InputMolecule_1[I1] {
    def apply(arg: I1)                         (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(List(arg))
    def apply(arg: I1, arg2: I1, moreArgs: I1*)(implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(arg +: arg2 +: moreArgs)
    def apply(args: Seq[I1])                   (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
    def apply(or: Or[I1])                      (implicit conn: Conn): Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = apply(resolveOr(or))
  }
}


*/