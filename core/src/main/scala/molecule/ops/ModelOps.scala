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

    def getGenerics(gs: Seq[Generic]): Seq[Generic] = gs map {
      case TxTValue(Some(ident))        => TxTValue(Some(getValues(Seq(ident)).head))
      case TxTValue_(Some(ident))       => TxTValue_(Some(getValues(Seq(ident)).head))
      case TxInstantValue(Some(ident))  => TxInstantValue(Some(getValues(Seq(ident)).head))
      case TxInstantValue_(Some(ident)) => TxInstantValue_(Some(getValues(Seq(ident)).head))
      case OpValue(Some(ident))         => OpValue(Some(getValues(Seq(ident)).head))
      case OpValue_(Some(ident))        => OpValue_(Some(getValues(Seq(ident)).head))
      case otherGeneric                 => otherGeneric
    }

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
      case atom@Atom(_, _, _, _, MapEq(idents), _, gs2, keyIdents)           => idents match {
        case List((ident, "__pair__"))
          if ident.startsWith("__ident__") && getValues(Seq(ident)) == Seq(None) => atom.copy(value = Fn("not", None), gs = getGenerics(gs2), keys = getKeys(keyIdents))
        case idents                                                              => atom.copy(value = MapEq(getValues(idents).asInstanceOf[Seq[(String, Any)]]), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      }
      case atom@Atom(_, _, _, 2, Eq(idents), _, gs2, keyIdents)              => getValues(idents) match {
        case Seq(None) => atom.copy(value = Fn("not", None), gs = getGenerics(gs2))
        case values    => atom.copy(value = Eq(values), gs = getGenerics(gs2))
      }
      case atom@Atom(_, _, _, _, Eq(idents), _, gs2, keyIdents)              => getValues(idents) match {
        case Seq(None) => atom.copy(value = Fn("not", None), gs = getGenerics(gs2), keys = getKeys(keyIdents))
        case values    => atom.copy(value = Eq(values), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      }
      case meta@Meta(_, _, _, _, Eq(idents))              => getValues(idents) match {
        case Seq(None) => meta.copy(value = Fn("not", None))
        case values    => meta.copy(value = Eq(values))
      }
      case atom@Atom(_, _, _, _, Neq(idents), _, gs2, keyIdents)             => atom.copy(value = Neq(getValues(idents)), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, And(idents), _, gs2, keyIdents)             => atom.copy(value = And(getValues(idents)), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Lt(ident), _, gs2, keyIdents)               => atom.copy(value = Lt(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Gt(ident), _, gs2, keyIdents)               => atom.copy(value = Gt(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Le(ident), _, gs2, keyIdents)               => atom.copy(value = Le(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, Ge(ident), _, gs2, keyIdents)               => atom.copy(value = Ge(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, AssertValue(idents), _, gs2, _)             => atom.copy(value = AssertValue(getValues(idents)), gs = getGenerics(gs2))
      case atom@Atom(_, _, _, _, RetractValue(idents), _, gs2, _)            => atom.copy(value = RetractValue(getValues(idents)), gs = getGenerics(gs2))
      case atom@Atom(_, _, _, _, ReplaceValue(oldNew), _, gs2, _)            => atom.copy(value = ReplaceValue(getValues(oldNew).asInstanceOf[Seq[(Any, Any)]]), gs = getGenerics(gs2))
      case atom@Atom(_, _, _, _, AssertMapPairs(idents), _, gs2, keyIdents)  => atom.copy(value = AssertMapPairs(getValues(idents).asInstanceOf[Seq[(String, Any)]]), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, ReplaceMapPairs(idents), _, gs2, keyIdents) => atom.copy(value = ReplaceMapPairs(getValues(idents).asInstanceOf[Seq[(String, Any)]]), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, RetractMapKeys(idents), _, gs2, keyIdents)  => atom.copy(value = RetractMapKeys(getValues(idents).map(_.toString)), gs = getGenerics(gs2), keys = getKeys(keyIdents))
      case atom@Atom(_, _, _, _, MapKeys(idents), _, gs2, _)                 => atom.copy(value = MapKeys(getValues(idents).asInstanceOf[Seq[String]]), gs = getGenerics(gs2))
      case atom@Atom(_, _, _, _, _, _, gs2, _)                               => atom.copy(gs = getGenerics(gs2))
      case meta@Meta(_, _, _, _, Neq(idents))                                => meta.copy(value = Neq(getValues(idents)))
      case meta@Meta(_, _, _, Id(eid), _)                                    => meta.copy(generic = Id(getValues(Seq(eid)).head))
      case Nested(ns, nestedElements)                                        => Nested(ns, resolve(nestedElements))
      case Composite(compositeElements)                                      => Composite(resolve(compositeElements))
      case TxMetaData(txElements)                                            => TxMetaData(resolve(txElements))
      case other                                                             => other
    }

    if (identMap.isEmpty) model else Model(resolve(model.elements))
  }
}