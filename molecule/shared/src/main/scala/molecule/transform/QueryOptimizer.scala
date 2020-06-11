package molecule.transform

import molecule.ast.query.{Clause, _}
import scala.collection.mutable.ListBuffer

object QueryOptimizer {

  def apply(q: Query): Query = {

    val grounds          = new ListBuffer[(String, Option[Clause])]
    val equalsOne        = new ListBuffer[(String, Option[Clause])]
    val equalsMany       = new ListBuffer[(String, Option[Clause])]
    val comparisons      = new ListBuffer[(String, Option[Clause])]
    val fulltextSearches = new ListBuffer[(String, Option[Clause])]
    val remainingClauses = new ListBuffer[(Set[String], Clause)]
    val optimizedClauses = new ListBuffer[Clause]

    q.i.inputs.foreach {
      case InVar(ScalarBinding(Var(v)), _)     => equalsOne += v -> None
      case InVar(CollectionBinding(Var(v)), _) => equalsMany += v -> None
      case _                                   =>
    }

    q.wh.clauses.foreach {
      case cl@Funct("molecule.util.fns/bind", Seq(Var(_)), ScalarBinding(Var(v))) =>
        grounds += v -> Some(cl)

      case cl@Funct(fn, _, ScalarBinding(Var(v))) if fn.startsWith("ground") =>
        grounds += v -> Some(cl)

      case cl@Funct(fn, Seq(Var(v), _), _) if fn.startsWith(".compareTo") =>
        comparisons += v -> Some(cl)

      case cl@Funct(_, _, NoBinding) =>
        comparisons += "" -> Some(cl)

      case cl@Funct("fulltext", _, RelationBinding(Seq(Var(v), _))) =>
        fulltextSearches += v -> Some(cl)

      case cl@NotClause(Var(v), _) =>
        comparisons += v -> Some(cl)

      case cl@DataClause(_, Var(v), _, Val(_), _, _) =>
        equalsOne += v -> Some(cl)

      case cl@DataClause(_, Var(v1), _, Var(v2), _, _) =>
        remainingClauses += Set(v1, v2) -> cl

      case cl =>
        remainingClauses += Set.empty[String] -> cl
    }

    val limiterCoordinates =
      grounds ++ equalsOne ++ equalsMany ++ comparisons ++ fulltextSearches


    def connectToLimitVar(matchVars: Set[String]): Unit = {
      remainingClauses.collectFirst {
        case (curVars, cl) if curVars.intersect(matchVars).nonEmpty =>
          // match
          optimizedClauses += cl
          remainingClauses -= curVars -> cl

          remainingClauses.headOption.foreach(head =>
            if (head._1.intersect(matchVars).isEmpty) {
              // Keep looking to connect with remaining clauses
              connectToLimitVar(curVars)
            }
          )
      }
    }

    def getOptimizeClauses: Seq[Clause] = {
      limiterCoordinates.foreach {
        case ("", _)       =>
        case (limitVar, _) => connectToLimitVar(Set(limitVar))
      }
      val a = limiterCoordinates.flatMap(_._2)
      val b = optimizedClauses
      val c = remainingClauses.map(_._2)
      (a ++ b ++ c).toSeq // needed for 2.13
    }

    val optimizedClauses_ = getOptimizeClauses
    q.copy(wh = Where(optimizedClauses_))
  }
}
