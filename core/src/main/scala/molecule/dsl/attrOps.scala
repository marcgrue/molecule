package molecule.dsl
import molecule.ast.model.{Exp1, Exp2, Exp3, Or}
import molecule.boilerplate.attributes._
import molecule.Imports._


trait RefAttrOps[Ns, T] {
  // Empty `apply` in update molecule retracts reference(s)!
  def apply()                 : Ns with Attr = ???

  // Null reference (ref datom not asserted)
  def apply(noValue: nil)     : Ns with Attr = ???

  // Unifying marker for attributes to be unified in self-joins
  def apply(unifyThis: unify) : Ns with Attr = ???

  // Negation
  def not(one: T, more: T*)   : Ns with Attr = ???
  def not(many: Iterable[T])  : Ns with Attr = ???
  // Same as
  def != (one: T, more: T*)   : Ns with Attr = ???
  def != (one: T)             : Ns with Attr = ??? // Hack to satisfy intellij inferrence
  def != (many: Iterable[T])  : Ns with Attr = ???
}


trait OneRefAttrOps[Ns, In] {
  def apply(ref: Long, moreRefs: Long*) : Ns with Attr = ???
  def apply(refs: Iterable[Long])       : Ns with Attr = ???
}

trait ManyRefAttrOps[Ns, In] {
  // Replaces all existing values with the applied new value(s)
  def apply(ref: Long, moreRefs: Long*)  : Ns with Attr = ???
  def apply(refs: Iterable[Long])        : Ns with Attr = ???

  def add(ref: Long, moreRefs: Long*)    : Ns with Attr = ???
  def add(refs: Iterable[Long])          : Ns with Attr = ???

  def replace(oldNew: (Long, Long), oldNewMore: (Long, Long)*) : Ns with Attr = ???
  def replace(oldNews: Iterable[(Long, Long)])                 : Ns with Attr = ???

  def remove(ref: Long, moreRefs: Long*) : Ns with Attr = ???
  def remove(refs : Iterable[Long])      : Ns with Attr = ???
}

trait BackRefAttrOps[Ns, In] {
  def apply(ref: Long): Ns with Attr = ???
}


trait ValueAttrOps[Ns, In, T, U] {

  // Null (datom not asserted)
  def apply(noValue: nil)      : Ns with Attr = ???

  // Unifying marker for attributes to be unified in self-joins
  def apply(unifyThis: unify)  : Ns with Attr = ???

  // Negation
  def not(value: T, more: T*)  : Ns with Attr = ???
  def not(values: Iterable[T]) : Ns with Attr = ???
  // Same as
  def != (value: T, more: T*)  : Ns with Attr = ???
  // Todo: remove this method when Intellij can infer from the above method alone...
  def != (value: T)            : Ns with Attr = ???
  def != (values: Iterable[T]) : Ns with Attr = ???

  // Comparison
  def <  (value: T) : Ns with Attr = ???
  def >  (value: T) : Ns with Attr = ???
  def <= (value: T) : Ns with Attr = ???
  def >= (value: T) : Ns with Attr = ???

  // Input
  def apply(in: ?) : In with Attr = ???
  def not  (in: ?) : In with Attr = ???
  def !=   (in: ?) : In with Attr = ???
  def <    (in: ?) : In with Attr = ???
  def >    (in: ?) : In with Attr = ???
  def <=   (in: ?) : In with Attr = ???
  def >=   (in: ?) : In with Attr = ???

  def apply(expr1: Exp1[T])       : Ns with Attr = ???
  def apply(expr2: Exp2[T, T])    : Ns with Attr = ???
  def apply(expr3: Exp3[T, T, T]) : Ns with Attr = ???
}


trait OneOps[Ns, In, T] {
  // Empty `apply` in update molecule retracts value!
  def apply()                    : Ns with Attr = ???
  def apply(value: T, more: T*)  : Ns with Attr = ???
  def apply(values: Iterable[T]) : Ns with Attr = ???
}

trait ManyOps[Ns, In, S, T] {
  // Empty `apply` in update molecule retracts all values!
  def apply()                                      : Ns with Attr = ???
  def apply(values: T*)                            : Ns with Attr = ???
  def apply(vs: Iterable[T], vss: Iterable[T]*)    : Ns with Attr = ???

  def add(value: T, more: T*)                      : Ns with Attr = ???
  def add(values: Iterable[T])                     : Ns with Attr = ???

  def replace(oldNew: (T, T), oldNewMore: (T, T)*) : Ns with Attr = ???
  def replace(oldNew: Iterable[(T, T)])            : Ns with Attr = ???

  def remove(value: T, more: T*)                   : Ns with Attr = ???
  def remove(values: Iterable[T])                  : Ns with Attr = ???
}


trait MapAttrOps[Ns, In, M, T] {
  // Empty `apply` in update molecule retracts all key/values!
  def apply()                                                 : Ns with Attr = ???
  def apply(pair: (String, T), morePairs: (String, T)*)       : Ns with Attr = ???
  def apply(pairs: Iterable[(String, T)])                     : Ns with Attr = ???
  def apply(pairs: Or[(String, T)])                           : Ns with Attr = ???
  def apply(map: Map[String, T])                              : Ns with Attr = ???

  def add(pair: (String, T), morePairs: (String, T)*)         : Ns with Attr = ???
  def add(pairs: Iterable[(String, T)])                       : Ns with Attr = ???

  def replace(newValue: (String, T), newValues: (String, T)*) : Ns with Attr = ???
  def replace(newValues: Iterable[(String, T)])               : Ns with Attr = ???

  def remove(key: String, moreKeys: String*)                  : Ns with Attr = ???
  def remove(keys: Iterable[String])                          : Ns with Attr = ???

  // todo: exclude all methods below from manipulation...

  // Value filters
  def apply(value: T, more: T*)                            : Ns with Attr = ???
  def apply(values: Iterable[T], moreValues: Iterable[T]*) : Ns with Attr = ???

  // Keys
  def k(value: String)                : Values with Ns with Attr = ???
  def k(value: String, more: String*) : Values with Ns with Attr = ???
  def k(values: Iterable[String])     : Values with Ns with Attr = ???
  def k(or: Or[String])               : Values with Ns with Attr = ???

  // Keyed attribute value methods
  trait Values {
    def apply(value: T, more: T*)  : Ns with Attr = ???
    def apply(values: Iterable[T]) : Ns with Attr = ???
    def apply(or: Or[T])           : Ns with Attr = ???

    // Negation
    def not(value: T, more: T*) : Ns with Attr = ???
    def != (value: T, more: T*) : Ns with Attr = ???

    // Comparison
    def <  (value: T) : Ns with Attr = ???
    def >  (value: T) : Ns with Attr = ???
    def <= (value: T) : Ns with Attr = ???
    def >= (value: T) : Ns with Attr = ???

    // Todo: How can we make those available to String type only?
    def contains(that: T): Ns with Attr = ???
    def contains(in: ?)  : In with Attr = ???
  }
}


trait OneOptional[Ns, T] {
  def apply(some: Option[T]) : Ns with Attr = ???
}
