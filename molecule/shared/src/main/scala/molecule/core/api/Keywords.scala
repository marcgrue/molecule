package molecule.core.api

import molecule.core.expression.{AggregateKeywords, AttrExpressions}

private[molecule] trait Keywords
  extends AggregateKeywords
  with AttrExpressions
{
//  object ? extends molecule.core.expression.AttrExpressions.?
  object unify extends molecule.core.expression.AttrExpressions.unify

  object count extends molecule.core.api.Keywords.count
  object countDistinct extends molecule.core.api.Keywords.countDistinct
  object distinct extends molecule.core.api.Keywords.distinct
  object max extends molecule.core.api.Keywords.max
  object min extends molecule.core.api.Keywords.min
  object rand extends molecule.core.api.Keywords.rand
  object sample extends molecule.core.api.Keywords.sample
  object avg extends molecule.core.api.Keywords.avg
  object median extends molecule.core.api.Keywords.median
  object stddev extends molecule.core.api.Keywords.stddev
  object sum extends molecule.core.api.Keywords.sum
  object variance extends molecule.core.api.Keywords.variance
}

object Keywords extends Keywords
