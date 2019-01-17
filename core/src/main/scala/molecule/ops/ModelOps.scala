package molecule.ops

import molecule.ast.model._


/** Model operations */
object ModelOps {

  def convert(value: Any): Any = value match {
    case set: Set[_]  => set map convert
    case seq: Seq[_]  => seq map convert
    case m: Map[_, _] => m.toSeq map convert
    case (k, v)       => (convert(k), convert(v))
    case Some(v)      => convert(v)
    case f: Float     => f.toDouble
    case unchanged    => unchanged
  }


  def resolveIdentifiers(model: Model, identMap: Map[String, Any]): Model = {

    def flatSeq(a: Any): Seq[Any] = (a match {
      case seq: Seq[_] => seq
      case set: Set[_] => set.toSeq
      case v           => Seq(v)
    }) map convert

    def getKeys(keyIdents: Seq[String]): Seq[String] = getValues(keyIdents).flatMap {
      case keys: Seq[_] => keys
      case key          => Seq(key)
    }.asInstanceOf[Seq[String]]

    def getValues(idents: Seq[Any]) = idents.flatMap {
      case set: Set[_] if set.nonEmpty                                                      => Seq(set.flatMap {
        case ident if ident.toString.startsWith("__ident__") => flatSeq(identMap.get(ident.toString).get)
        case value                                           => Seq(convert(value))
      })
      case v: String if v.startsWith("__ident__")                                           => flatSeq(identMap.get(v).get)
      case (k: String, "__pair__") if k.startsWith("__ident__")                             => flatSeq(identMap.get(k).get)
      case (k: String, v: String) if k.startsWith("__ident__") && v.startsWith("__ident__") => Seq((identMap.get(k).get, identMap.get(v).get))
      case (k: String, v: Any) if k.startsWith("__ident__")                                 => Seq((identMap.get(k).get, convert(v)))
      case (k: Any, v: String) if v.startsWith("__ident__")                                 => Seq((convert(k), identMap.get(v).get))
      case (k, v)                                                                           => Seq((convert(k), convert(v)))
      case seq: Seq[_]                                                                      => seq map convert
      case v                                                                                => Seq(convert(v))
    }

    def resolve(elements: Seq[Element]): Seq[Element] = elements map {
      case atom@Atom(_, _, _, _, MapEq(idents), _, _, keyIdents)           => idents match {
        case List((ident, "__pair__"))
          if ident.startsWith("__ident__") && getValues(Seq(ident)) == Seq(None) => atom.copy(value = Fn("not", None), keys = getKeys(keyIdents))
        case idents                                                              => atom.copy(value = MapEq(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
      }
      case atom@Atom(_, _, _, 2, Eq(idents), _, _, keyIdents)              => getValues(idents) match {
        case Seq(None) => atom.copy(value = Fn("not", None))
        case values    => atom.copy(value = Eq(values))
      }
      case atom@Atom(_, _, _, _, Eq(idents), _, _, keyIdents)              => getValues(idents) match {
        case Seq(None) => atom.copy(value = Fn("not", None), keys = getKeys(keyIdents))
        case values    => atom.copy(value = Eq(values), keys = getKeys(keyIdents))
      }
      case meta@Meta(_, _, _, Eq(idents))                                  => getValues(idents) match {
        case Seq(None) => meta.copy(value = Fn("not", None))
        case values    => meta.copy(value = Eq(values))
      }
      case atom@Atom(_, _, _, _, Neq(idents), _, _, keyIdents)             => atom.copy(value = Neq(getValues(idents)), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, And(idents), _, _, keyIdents)             => atom.copy(value = And(getValues(idents)), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Lt(ident), _, _, keyIdents)               => atom.copy(value = Lt(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Gt(ident), _, _, keyIdents)               => atom.copy(value = Gt(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Le(ident), _, _, keyIdents)               => atom.copy(value = Le(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Ge(ident), _, _, keyIdents)               => atom.copy(value = Ge(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, AssertValue(idents), _, _, _)             => atom.copy(value = AssertValue(getValues(idents)))
      case atom@Atom(_, _, _, _, RetractValue(idents), _, _, _)            => atom.copy(value = RetractValue(getValues(idents)))
      case atom@Atom(_, _, _, _, ReplaceValue(oldNew), _, _, _)            => atom.copy(value = ReplaceValue(getValues(oldNew).asInstanceOf[Seq[(Any, Any)]]))
      case atom@Atom(_, _, _, _, AssertMapPairs(idents), _, _, keyIdents)  => atom.copy(value = AssertMapPairs(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, ReplaceMapPairs(idents), _, _, keyIdents) => atom.copy(value = ReplaceMapPairs(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, RetractMapKeys(idents), _, _, keyIdents)  => atom.copy(value = RetractMapKeys(getValues(idents).map(_.toString)), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, MapKeys(idents), _, _, _)                 => atom.copy(value = MapKeys(getValues(idents).asInstanceOf[Seq[String]]))
      case meta@Meta(_, _, _, Neq(idents))                                 => meta.copy(value = Neq(getValues(idents)))
      case meta@Meta(_, _, _, Lt(ident))                                   => meta.copy(value = Lt(getValues(Seq(ident)).head))
      case meta@Meta(_, _, _, Gt(ident))                                   => meta.copy(value = Gt(getValues(Seq(ident)).head))
      case meta@Meta(_, _, _, Le(ident))                                   => meta.copy(value = Le(getValues(Seq(ident)).head))
      case meta@Meta(_, _, _, Ge(ident))                                   => meta.copy(value = Ge(getValues(Seq(ident)).head))
      case Nested(ns, nestedElements)                                      => Nested(ns, resolve(nestedElements))
      case Composite(compositeElements)                                    => Composite(resolve(compositeElements))
      case TxMetaData(txElements)                                          => TxMetaData(resolve(txElements))
      case other                                                           => other
    }

    if (identMap.isEmpty) model else Model(resolve(model.elements))
  }
}