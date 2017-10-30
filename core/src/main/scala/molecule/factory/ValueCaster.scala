package molecule.factory

import java.util.{List => jList}

import molecule.ast.model._
import molecule.ast.query.Query

import scala.collection.JavaConverters._

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

//trait ValueCaster[Ctx <: Context] extends GetJson[Ctx] {
//  import c.universe._

trait ValueCaster {
  import scala.reflect.runtime.universe._

//  def cast[T: TypeTag](query: Query, row: Seq[Any], i: Int): T = {
//  def cast[T: WeakTypeTag](query: Query, row: Seq[Any], i: Int): T = {
  def cast[T](query: Query, row: Seq[Any], i: Int): T = {
    val value: Any = if (i >= row.size) null else row(i)
    weakTypeOf[T] match {
      case t => castType[T](query, value, i)
    }
  }


//  def castType[T: WeakTypeTag](query: Query, value: Any, i: Int) = weakTypeOf[T] match {
  def castType[T](query: Query, value: Any, i: Int) = weakTypeOf[T] match {
    case t if t <:< typeOf[Int]   => value.asInstanceOf[T]
    case t if t <:< typeOf[Float] => value.asInstanceOf[T]
    case t /* String */           => value.asInstanceOf[T]
  }




}