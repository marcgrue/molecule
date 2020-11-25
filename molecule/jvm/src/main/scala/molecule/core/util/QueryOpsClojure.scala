package molecule.core.util

import datomic.Util
import molecule.core.ast.query.{CollectionBinding, InDataSource, InVar, Query, RelationBinding}
import molecule.core.ops.exception.QueryOpsException

case class QueryOpsClojure(q: Query) {


  private def cast(a: Any): AnyRef = a match {
    case i: Int                                => i.toLong.asInstanceOf[Object]
    case f: Float                              => f.toDouble.asInstanceOf[Object]
    case bigI: BigInt                          => bigI.bigInteger
    case bigD: BigDecimal                      => bigD.bigDecimal
    case s: String if s.startsWith("__enum__") => Util.read(s.drop(8)) // clojure Keyword
    case other                                 => other.asInstanceOf[Object]
  }

  def inputsWithKeyword: Seq[AnyRef] = q.i.inputs.map {
    case InVar(RelationBinding(_), Nil)         => Util.list()
    case InVar(RelationBinding(_), argss)       => Util.list(argss.map(args => Util.list(args map cast: _*)): _*)
    case InVar(CollectionBinding(_), Nil)       => Util.list()
    case InVar(CollectionBinding(_), argss)     => Util.list(argss.head map cast: _*)
    case InVar(_, Nil)                          => Util.list()
    case InVar(_, argss) if argss.head.size > 1 => Nil
    case InVar(_, argss)                        => cast(argss.head.head)
    case InDataSource(_, Nil)                   => Util.list()
    case InDataSource(_, argss)                 => cast(argss.head.head)
    case other                                  => throw new QueryOpsException(s"UNEXPECTED input: $other\nquery:\n$q")
  }
}
