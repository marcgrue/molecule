
import molecule.api.{EntityOps, OptionalMapOps}
import molecule.expression.{AggregateKeywords, AttrExpressions}
import molecule.facade.Datomic
import molecule.factory._

/** [[http://www.scalamolecule.org Molecule]] library - a Scala meta-DSL for the [[https://www.datomic.com Datomic]] database.
  *
  * See [[molecule.api api]] package for various api imports to start using Molecule.
  *
  * == Sub-packages ==
  * <table>
  *   <tr>
  *     <td><a href="api/index.html">api</a><td>
  *     <td>Molecule API.</td>
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
  *     <td><a href="exception/index.html">exceptions</a><td>
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
package object molecule


