package molecule.core.macros

import scala.reflect.macros.blackbox


trait ObjBuilder extends Cast {
  val c: blackbox.Context

  import c.universe._

  lazy val p = InspectMacro("ObjBuilder", 1, 900)

  sealed trait Node
  case class Prop(
    cls: String,
    prop: String,
    tpe: Tree,
    cast: Int => Tree,
    optAggrTpe: Option[String] = None
  ) extends Node {
    override def toString: String = {
      // Since the cast lambda is just an object reference, we simply add null so that we can copy/paste
      s"""Prop("$cls", "$prop", "$tpe", null, $optAggrTpe)"""
    }
  }
  case class Obj(
    cls: String,
    ref: String,
    card: Int,
    props: List[Node]
  ) extends Node {
    override def toString: String = {
      def draw(nodes: Seq[Node], indent: Int): Seq[String] = {
        val s = "  " * indent
        nodes map {
          case Obj(cls, ref, card, props) =>
            s"""|${s}Obj("$cls", "$ref", $card, List(
                |${draw(props, indent + 1).mkString(s",\n")}))""".stripMargin
          case prop                       => s"$s$prop"
        }
      }
      draw(Seq(this), 0).head
    }
  }

  def classes(nodes: List[Node]): List[Tree] = {
    var prevClasses = List.empty[String]
    nodes.flatMap {
      case Prop(cls, _, _, _, _) =>
        if (!prevClasses.contains(cls)) {
          prevClasses = prevClasses :+ cls
          Some(tq"${TypeName(cls)}")
        } else None

      case Obj(cls, _, 2, props) => classes(props) match {
        case Nil                                                                    => Some(tq"${TypeName(cls)}[Seq[Init]]")
        case List(a)                                                                => Some(tq"${TypeName(cls)}[Seq[Init with $a]]")
        case List(a, b)                                                             => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b]]")
        case List(a, b, c)                                                          => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c]]")
        case List(a, b, c, d)                                                       => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d]]")
        case List(a, b, c, d, e)                                                    => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e]]")
        case List(a, b, c, d, e, f)                                                 => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f]]")
        case List(a, b, c, d, e, f, g)                                              => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g]]")
        case List(a, b, c, d, e, f, g, h)                                           => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h]]")
        case List(a, b, c, d, e, f, g, h, i)                                        => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i]]")
        case List(a, b, c, d, e, f, g, h, i, j)                                     => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j]]")
        case List(a, b, c, d, e, f, g, h, i, j, k)                                  => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l)                               => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m)                            => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n)                         => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)                      => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)                   => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)                => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)             => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)          => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)       => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)    => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u]]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => Some(tq"${TypeName(cls)}[Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u with $v]]")
        case _                                                                      => None
      }

      case Obj(cls, _, _, props) => classes(props) match {
        case Nil                                                                    => Some(tq"${TypeName(cls)}[Init]")
        case List(a)                                                                => Some(tq"${TypeName(cls)}[Init with $a]")
        case List(a, b)                                                             => Some(tq"${TypeName(cls)}[Init with $a with $b]")
        case List(a, b, c)                                                          => Some(tq"${TypeName(cls)}[Init with $a with $b with $c]")
        case List(a, b, c, d)                                                       => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d]")
        case List(a, b, c, d, e)                                                    => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e]")
        case List(a, b, c, d, e, f)                                                 => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f]")
        case List(a, b, c, d, e, f, g)                                              => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g]")
        case List(a, b, c, d, e, f, g, h)                                           => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h]")
        case List(a, b, c, d, e, f, g, h, i)                                        => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i]")
        case List(a, b, c, d, e, f, g, h, i, j)                                     => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j]")
        case List(a, b, c, d, e, f, g, h, i, j, k)                                  => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l)                               => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m)                            => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n)                         => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)                      => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)                   => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)                => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)             => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)          => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)       => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)    => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u]")
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => Some(tq"${TypeName(cls)}[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u with $v]")
        case _                                                                      => None
      }
    }
  }

  def objCode(obj: Obj, i0: Int = -1, isNested: Boolean = false): (Tree, Int) = {
    // Property index of row/tuple
    var i = if (isNested && i0 == -1) -1 else i0

    def properties(nodes: List[Node]): List[Tree] = {
      var propNames = List.empty[String]
      val propDefs  = nodes.flatMap {
        case Prop(_, prop, tpe, cast, optAggr) =>
          i += 1
          // Only generate 1 property, even if attribute is repeated in molecule
          if (!propNames.contains(prop)) {
            propNames = propNames :+ prop
            optAggr match {
              case None if isNested => Some(q"final override lazy val ${TermName(prop)}: $tpe = tpl.productElement($i).asInstanceOf[$tpe]")
              case None             => Some(q"final override lazy val ${TermName(prop)}: $tpe = ${cast(i)}")

              case Some(aggrTpe) if aggrTpe == tpe.toString() =>
                if (isNested)
                  Some(q"final override lazy val ${TermName(prop)}: $tpe = tpl.productElement($i).asInstanceOf[$tpe]")
                else
                  Some(q"final override lazy val ${TermName(prop)}: $tpe = ${cast(i)}")

              case Some(aggrTpe) =>
                val err = s"""Object property `$prop` not available since the aggregate changes its type to `$aggrTpe`. Please use tuple output instead to access aggregate value."""
                Some(q"""final override lazy val ${TermName(prop)}: $tpe = throw new MoleculeException($err)""")
            }
          } else None

        case o@Obj(_, ref, 2, props) =>
          i += 1
          val productTpe = if (props.length == 1) {
            props.head match {
              case Prop(_, _, tpe, _, _) => tq"Tuple1[$tpe]"
              case Obj(_, _, _, _)       => tq"Tuple1[Product]"
            }
          } else {
            tq"Product"
          }
          val subObj     = q"tpl.productElement($i).asInstanceOf[Seq[$productTpe]].map( tpl => ${objCode(o, -1, isNested)._1} )"
          classes(props) match {
            case Nil                                                                    => Some(q"final override def ${TermName(ref)}: Seq[Init] = $subObj")
            case List(a)                                                                => Some(q"final override def ${TermName(ref)}: Seq[Init with $a] = $subObj")
            case List(a, b)                                                             => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b] = $subObj")
            case List(a, b, c)                                                          => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c] = $subObj")
            case List(a, b, c, d)                                                       => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d] = $subObj")
            case List(a, b, c, d, e)                                                    => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e] = $subObj")
            case List(a, b, c, d, e, f)                                                 => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f] = $subObj")
            case List(a, b, c, d, e, f, g)                                              => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g] = $subObj")
            case List(a, b, c, d, e, f, g, h)                                           => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h] = $subObj")
            case List(a, b, c, d, e, f, g, h, i)                                        => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j)                                     => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k)                                  => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l)                               => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m)                            => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n)                         => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)                      => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)                   => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)                => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)             => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)          => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)       => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)    => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u] = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => Some(q"final override def ${TermName(ref)}: Seq[Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u with $v] = $subObj")
            case _                                                                      => None
          }

        case o@Obj(_, ref, _, props) =>
          val (subObj, j) = objCode(o, i, isNested)
          i = j
          classes(props) match {
            case Nil                                                                    => Some(q"final override def ${TermName(ref)}: Init = $subObj")
            case List(a)                                                                => Some(q"final override def ${TermName(ref)}: Init with $a = $subObj")
            case List(a, b)                                                             => Some(q"final override def ${TermName(ref)}: Init with $a with $b = $subObj")
            case List(a, b, c)                                                          => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c = $subObj")
            case List(a, b, c, d)                                                       => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d = $subObj")
            case List(a, b, c, d, e)                                                    => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e = $subObj")
            case List(a, b, c, d, e, f)                                                 => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f = $subObj")
            case List(a, b, c, d, e, f, g)                                              => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g = $subObj")
            case List(a, b, c, d, e, f, g, h)                                           => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h = $subObj")
            case List(a, b, c, d, e, f, g, h, i)                                        => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j)                                     => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k)                                  => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l)                               => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m)                            => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n)                         => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)                      => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)                   => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)                => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)             => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)          => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)       => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)    => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u = $subObj")
            case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => Some(q"final override def ${TermName(ref)}: Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u with $v = $subObj")
            case _                                                                      => None
          }
      }
      propDefs
    }

    def hasSameNss: Boolean = {
      var dupNss: Set[String] = Set.empty
      def isDupe(ns: String) = if (dupNss(ns)) true else {
        dupNss += ns;
        false
      }
      obj.props.exists {
        case Obj(ns, _, _, _) if isDupe(ns) => true
        case _                              => false
      }
    }

    val tree = if (hasSameNss) {
      q"""throw new MoleculeException(
            "Please compose multiple same-name namespaces with `++` instead of `+` to access object properties."
          )"""
    } else {
      classes(obj.props) match {
        case Nil                                                                    => q"new Init {}"
        case List(a)                                                                => q"new DynamicMolecule with Init with $a { ..${properties(obj.props)} }"
        case List(a, b)                                                             => q"new Init with $a with $b { ..${properties(obj.props)} }"
        case List(a, b, c)                                                          => q"new Init with $a with $b with $c { ..${properties(obj.props)} }"
        case List(a, b, c, d)                                                       => q"new Init with $a with $b with $c with $d { ..${properties(obj.props)} }"
        case List(a, b, c, d, e)                                                    => q"new Init with $a with $b with $c with $d with $e { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f)                                                 => q"new Init with $a with $b with $c with $d with $e with $f { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g)                                              => q"new Init with $a with $b with $c with $d with $e with $f with $g { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h)                                           => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i)                                        => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j)                                     => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k)                                  => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l)                               => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m)                            => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n)                         => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)                      => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)                   => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)                => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)             => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)          => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)       => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)    => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u { ..${properties(obj.props)} }"
        case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => q"new Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u with $v { ..${properties(obj.props)} }"
        case list                                                                   => abort("Unexpected list of types:\n  " + list.mkString("\n  "))
      }
    }
    (tree, i)
  }


  def addNode(obj: Obj, node: Node, level: Int): Obj = {
    val newProps = level match {
      case 0 =>
        node :: obj.props

      case 1 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj1a = obj1.copy(props = node :: obj1.props)
        obj1a :: obj.props.tail

      case 2 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj2a = obj2.copy(props = node :: obj2.props)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 3 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj3a = obj3.copy(props = node :: obj3.props)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 4 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj4a = obj4.copy(props = node :: obj4.props)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 5 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj5  = obj4.props.head.asInstanceOf[Obj]
        val obj5a = obj5.copy(props = node :: obj5.props)
        val obj4a = obj4.copy(props = obj5a :: obj4.props.tail)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 6 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj5  = obj4.props.head.asInstanceOf[Obj]
        val obj6  = obj5.props.head.asInstanceOf[Obj]
        val obj6a = obj6.copy(props = node :: obj6.props)
        val obj5a = obj5.copy(props = obj6a :: obj5.props.tail)
        val obj4a = obj4.copy(props = obj5a :: obj4.props.tail)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 7 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj5  = obj4.props.head.asInstanceOf[Obj]
        val obj6  = obj5.props.head.asInstanceOf[Obj]
        val obj7  = obj6.props.head.asInstanceOf[Obj]
        val obj7a = obj7.copy(props = node :: obj7.props)
        val obj6a = obj6.copy(props = obj7a :: obj6.props.tail)
        val obj5a = obj5.copy(props = obj6a :: obj5.props.tail)
        val obj4a = obj4.copy(props = obj5a :: obj4.props.tail)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail
    }
    obj.copy(props = newProps)
  }


  def addRef(obj: Obj, refCls: String, refName: String, card: Int, objLevel: Int): Obj = {
    val newProps = objLevel match {
      case 0 =>
        List(Obj(refCls, refName, card, obj.props))

      case 1 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj1a = obj1.copy(cls = refCls, ref = refName, card = card)
        obj1a :: obj.props.tail

      case 2 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj2a = obj2.copy(cls = refCls, ref = refName, card = card)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 3 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj3a = obj3.copy(cls = refCls, ref = refName, card = card)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 4 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj4a = obj4.copy(cls = refCls, ref = refName, card = card)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 5 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj5  = obj4.props.head.asInstanceOf[Obj]
        val obj5a = obj5.copy(cls = refCls, ref = refName, card = card)
        val obj4a = obj4.copy(props = obj5a :: obj4.props.tail)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 6 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj5  = obj4.props.head.asInstanceOf[Obj]
        val obj6  = obj5.props.head.asInstanceOf[Obj]
        val obj6a = obj6.copy(cls = refCls, ref = refName, card = card)
        val obj5a = obj5.copy(props = obj6a :: obj5.props.tail)
        val obj4a = obj4.copy(props = obj5a :: obj4.props.tail)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail

      case 7 =>
        val obj1  = obj.props.head.asInstanceOf[Obj]
        val obj2  = obj1.props.head.asInstanceOf[Obj]
        val obj3  = obj2.props.head.asInstanceOf[Obj]
        val obj4  = obj3.props.head.asInstanceOf[Obj]
        val obj5  = obj4.props.head.asInstanceOf[Obj]
        val obj6  = obj5.props.head.asInstanceOf[Obj]
        val obj7  = obj6.props.head.asInstanceOf[Obj]
        val obj7a = obj7.copy(cls = refCls, ref = refName, card = card)
        val obj6a = obj6.copy(props = obj7a :: obj6.props.tail)
        val obj5a = obj5.copy(props = obj6a :: obj5.props.tail)
        val obj4a = obj4.copy(props = obj5a :: obj4.props.tail)
        val obj3a = obj3.copy(props = obj4a :: obj3.props.tail)
        val obj2a = obj2.copy(props = obj3a :: obj2.props.tail)
        val obj1a = obj1.copy(props = obj2a :: obj1.props.tail)
        obj1a :: obj.props.tail
    }
    obj.copy(props = newProps)
  }
}
