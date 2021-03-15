package molecule.core.ops

import molecule.core.ast.elements._


/** Model operations */
object ModelOps {

  def convert(value: Any): Any = value match {
    case set: scala.collection.Set[_]  => set map convert
    case seq: scala.collection.Seq[_]  => seq map convert
    case m: scala.collection.Map[_, _] => m.toSeq map convert
    case (k, v)                        => (convert(k), convert(v))
    case Some(v)                       => convert(v)
    case f: Float                      => f.toString.toDouble
    case unchanged                     => unchanged
  }

  def resolveIdentifiers(model: Model, identMap: Map[String, Any]): Model = {
    def flatSeq(a: Any): scala.collection.Seq[Any] = (a match {
      case seq: scala.collection.Seq[_] => seq
      case set: scala.collection.Set[_] => set.toSeq
      case v                            => Seq(v)
    }) map convert

    def getKeys(keyIdents: Seq[String]): Seq[String] = getValues(keyIdents).flatMap {
      case keys: scala.collection.Seq[_] => keys
      case key                           => Seq(key)
    }.asInstanceOf[Seq[String]]

    def getValues(idents: Seq[Any]): Seq[Any] = idents.flatMap {
      case set: scala.collection.Set[_] if set.nonEmpty => Seq(set.flatMap {
        case ident if ident.toString.startsWith("__ident__") => flatSeq(identMap(ident.toString))
        case value                                           => Seq(convert(value))
      })

      case v: String if v.startsWith("__ident__")                                           => flatSeq(identMap(v))
      case (k: String, "__pair__") if k.startsWith("__ident__")                             => flatSeq(identMap(k))
      case (k: String, v: String) if k.startsWith("__ident__") && v.startsWith("__ident__") => Seq((identMap(k), identMap(v)))
      case (k: String, v: Any) if k.startsWith("__ident__")                                 => Seq((identMap(k), convert(v)))
      case (k: Any, v: String) if v.startsWith("__ident__")                                 => Seq((convert(k), identMap(v)))
      case (k, v)                                                                           => Seq((convert(k), convert(v)))
      case seq: scala.collection.Seq[_]                                                     => seq map convert
      case v                                                                                => Seq(convert(v))
    }

    def resolve(elements: Seq[Element]): Seq[Element] = elements map {
      case a@Atom(_, _, _, _, MapEq(idents), _, _, keyIdents)           => idents match {
        case List((ident, "__pair__"))
          if ident.startsWith("__ident__") && getValues(Seq(ident)) == Seq(None) =>
          a.copy(value = Fn("not", None), keys = getKeys(keyIdents))

        case idents =>
          a.copy(value = MapEq(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
      }
      case a@Atom(_, _, _, 2, Eq(idents), _, _, _)                      => getValues(idents) match {
        case Seq(None) => a.copy(value = Fn("not", None))
        case values    => a.copy(value = Eq(values))
      }
      case a@Atom(_, _, _, _, Eq(idents), _, _, keyIdents)              => getValues(idents) match {
        case Seq(None) => a.copy(value = Fn("not", None), keys = getKeys(keyIdents))
        case values    => a.copy(value = Eq(values), keys = getKeys(keyIdents))
      }
      case g@Generic(_, _, _, Eq(idents))                               => getValues(idents) match {
        case Seq(None) => g.copy(value = Fn("not", None))
        case values    => g.copy(value = Eq(values))
      }
      case a@Atom(_, _, _, _, Neq(idents), _, _, keyIdents)             => a.copy(value = Neq(getValues(idents)), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, And(idents), _, _, keyIdents)             => a.copy(value = And(getValues(idents)), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, Lt(ident), _, _, keyIdents)               => a.copy(value = Lt(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, Gt(ident), _, _, keyIdents)               => a.copy(value = Gt(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, Le(ident), _, _, keyIdents)               => a.copy(value = Le(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, Ge(ident), _, _, keyIdents)               => a.copy(value = Ge(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, Fulltext(idents), _, _, keyIdents)        => a.copy(value = Fulltext(getValues(idents)), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, AssertValue(idents), _, _, _)             => a.copy(value = AssertValue(getValues(idents)))
      case a@Atom(_, _, _, _, RetractValue(idents), _, _, _)            => a.copy(value = RetractValue(getValues(idents)))
      case a@Atom(_, _, _, _, ReplaceValue(oldNew), _, _, _)            => a.copy(value = ReplaceValue(getValues(oldNew).asInstanceOf[Seq[(Any, Any)]]))
      case a@Atom(_, _, _, _, AssertMapPairs(idents), _, _, keyIdents)  => a.copy(value = AssertMapPairs(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, ReplaceMapPairs(idents), _, _, keyIdents) => a.copy(value = ReplaceMapPairs(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, RetractMapKeys(idents), _, _, keyIdents)  => a.copy(value = RetractMapKeys(getValues(idents).map(_.toString)), keys = getKeys(keyIdents))
      case a@Atom(_, _, _, _, MapKeys(idents), _, _, _)                 => a.copy(value = MapKeys(getValues(idents).asInstanceOf[Seq[String]]))
      case g@Generic(_, _, _, Neq(idents))                              => g.copy(value = Neq(getValues(idents)))
      case g@Generic(_, _, _, Lt(ident))                                => g.copy(value = Lt(getValues(Seq(ident)).head))
      case g@Generic(_, _, _, Gt(ident))                                => g.copy(value = Gt(getValues(Seq(ident)).head))
      case g@Generic(_, _, _, Le(ident))                                => g.copy(value = Le(getValues(Seq(ident)).head))
      case g@Generic(_, _, _, Ge(ident))                                => g.copy(value = Ge(getValues(Seq(ident)).head))
      case Nested(bond, nestedElements)                                 => Nested(bond, resolve(nestedElements))
      case Composite(compositeElements)                                 => Composite(resolve(compositeElements))
      case TxMetaData(txElements)                                       => TxMetaData(resolve(txElements))
      case other                                                        => other
    }

    if (identMap.isEmpty) model else Model(resolve(model.elements))
  }
}