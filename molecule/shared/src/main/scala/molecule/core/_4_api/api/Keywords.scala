package molecule.core._4_api.api

import molecule.core._2_dsl.expression.{AggregateKeywords, AttrExpressions}

private[molecule] trait Keywords
  extends AggregateKeywords
  with AttrExpressions
{
  object ? extends molecule.core._2_dsl.expression.AttrExpressions.?
  object unify extends molecule.core._2_dsl.expression.AttrExpressions.unify

  object count extends molecule.core._4_api.api.Keywords.count
  object countDistinct extends molecule.core._4_api.api.Keywords.countDistinct
  object distinct extends molecule.core._4_api.api.Keywords.distinct
  object max extends molecule.core._4_api.api.Keywords.max
  object min extends molecule.core._4_api.api.Keywords.min
  object rand extends molecule.core._4_api.api.Keywords.rand
  object sample extends molecule.core._4_api.api.Keywords.sample
  object avg extends molecule.core._4_api.api.Keywords.avg
  object median extends molecule.core._4_api.api.Keywords.median
  object stddev extends molecule.core._4_api.api.Keywords.stddev
  object sum extends molecule.core._4_api.api.Keywords.sum
  object variance extends molecule.core._4_api.api.Keywords.variance
}

object Keywords extends Keywords
