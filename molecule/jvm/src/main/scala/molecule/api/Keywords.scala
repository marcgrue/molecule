package molecule.api

import molecule.expression.{AggregateKeywords, AttrExpressions}

private[molecule] trait Keywords
  extends AggregateKeywords
  with AttrExpressions
{
  object ? extends molecule.expression.AttrExpressions.?
  object unify extends molecule.expression.AttrExpressions.unify

  object count extends molecule.api.Keywords.count
  object countDistinct extends molecule.api.Keywords.countDistinct
  object distinct extends molecule.api.Keywords.distinct
  object max extends molecule.api.Keywords.max
  object min extends molecule.api.Keywords.min
  object rand extends molecule.api.Keywords.rand
  object sample extends molecule.api.Keywords.sample
  object avg extends molecule.api.Keywords.avg
  object median extends molecule.api.Keywords.median
  object stddev extends molecule.api.Keywords.stddev
  object sum extends molecule.api.Keywords.sum
  object variance extends molecule.api.Keywords.variance
}

object Keywords extends Keywords
