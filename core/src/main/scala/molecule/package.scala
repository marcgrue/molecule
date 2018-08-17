
import molecule.action.{CompositeInserts, EntityOps, OptionalMapOps}
import molecule.expression.{AggregateKeywords, AttrExpressions}
import molecule.facade.Datomic
import molecule.factory._

/** [[http://www.scalamolecule.org Molecule]] library - a Scala meta DSL for the [[https://www.datomic.com Datomic]] database.
  * <br><br>
  * This base package includes a single [[molecule.api api object]] that is used to bring Molecule functionality into a project:
  * {{{
  *   import molecule.api._
  * }}}
  *
  * == Sub-packages ==
  * <table>
  *   <tr>
  *     <td><a href="action/index.html">action</a><td>
  *     <td>Actions on molecules and entities.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="ast/index.html">ast</a><td>
  *     <td style="color:#888">Internal Molecule ASTs.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="boilerplate/index.html">boilerplate</a><td>
  *     <td style="color:#888">Internal interfaces for auto-generated DSL boilerplate code.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="composition/index.html">composition</a>&nbsp;&nbsp;&nbsp;<td>
  *     <td>Builder methods to compose molecules.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="exception/index.html">exception</a><td>
  *     <td>Exceptions thrown by Molecule.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="expression/index.html">expression</a><td>
  *     <td>Attribute expressions and operations.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="facade/index.html">facade</a><td>
  *     <td>Molecule facades to Datomic.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="factory/index.html">factory</a><td>
  *     <td>Implicit macro methods `m` to instantiate molecules from custom DSL molecule constructs.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="generic/index.html">generic</a><td>
  *     <td>Interfaces to generic information about datoms and Datomic database.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="input/index.html">input</a><td>
  *     <td>Input molecules awaiting input.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="macro/index.html">macro</a><td>
  *     <td style="color:#888">Internal macros generating molecule code from custom DSL molecule constructs.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="ops/index.html">ops</a><td>
  *     <td style="color:#888">Internal operational helpers for transforming DSL to molecule.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="schema/index.html">schema</a><td>
  *     <td>Schema definition DSL.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="ops/index.html">transform</a><td>
  *     <td style="color:#888">Internal transformers from DSL to Model/Query/Transaction.</td>
  *   </tr>
  *   <tr>
  *     <td><a href="util/index.html">util</a><td>
  *     <td style="color:#888">Internal Java database functions for Datomic.</td>
  *   </tr>
  * </table>
  */
package object molecule {

  /** Molecule API to be imported into your project to use Molecule.
    * <br><br>
    * To start using Molecule involves 2 initial steps:
    *
    *  - Define your schema: [[molecule.schema.definition Docs]] | [[http://www.scalamolecule.org/manual/schema/ Manual]]
    *  - `sbt compile` your project to let the sbt-molecule plugin generate your custom molecule DSL.
    *
    * Then you can start using your DSL and create molecules by importing the api, your DSL
    * and assign a Datomic connection to an implicit val:
    * {{{
    *   import molecule.api._                  // import Molecule API
    *   import path.to.dsl.yourDomain._        // auto-generated custom DSL
    *   import path.to.schema.YourDomainSchema // auto-generated custom Schema Transaction data
    *
    *   implicit val conn = recreateDbFrom(YourDomainDefiniton) // Only once
    *
    *   // Create molecules
    *   Person.name("Ben").age(42).save
    *   val benAge = Person.name_("Ben").age.get.head // 42
    *   // etc..
    * }}}
    * For brevity, arity 3-22 interfaces and empty companion traits are left ungrouped.
    * */
  object api
    extends Datomic
      with AttrExpressions
      with AggregateKeywords
      with CompositeInserts
      with EntityOps
      with OptionalMapOps
      with Molecule_Factory
      with Molecule_In_1_Factory
      with Molecule_In_2_Factory
      with Molecule_In_3_Factory
      with Composite_Factory
      with Composite_In_1_Factory
      with Composite_In_2_Factory
      with Composite_In_3_Factory
}


