package molecule.core.macros

import scala.reflect.macros.blackbox


trait ObjBuilder extends Cast {
  val c: blackbox.Context
  import c.universe._

  lazy val p = InspectMacro("ObjBuilder", 1, 900)

  sealed trait Node
  case class Prop(cls: String, prop: String, tpe: String, cast: Int => Tree) extends Node {
    override def toString: String = {
      // Since we can't use the lambda object reference, we simply add null so that we can copy/paste
      s"""Prop("$cls", "$prop", "$tpe", null)"""
      //      s"""Prop("$cls", "$prop", "$tpe", $cast)"""
    }
  }
  case class Obj(cls: String, ref: String, card: Int, props: List[Node]) extends Node {
    override def toString: String = {
      def draw(nodes: Seq[Node], indent: Int): Seq[String] = {
        val s = "  " * indent
        nodes map {
          case Obj(cls, ref, card, props) =>
            s"""|${s}Obj("$cls", "$ref", $card, List(
                |${draw(props, indent + 1).mkString(s",\n")}))""".stripMargin
          case prop                  => s"$s$prop"
        }
      }
      draw(Seq(this), 0).head
    }
  }

  def objCode(obj: Obj, i0: Int = -1): (Tree, Int) = {
    // Property index of row
    var i = i0

    def classes(nodes: List[Node]): List[Tree] = {
      nodes map {
        case Prop(cls, _, _, _) => tq"${TypeName(cls)}"

        case Obj(cls, _, _, props) => props.length match {
          case 1 =>
            val t1 = classes(props).head
            tq"${TypeName(cls)}[Init with $t1]"
          case 2 =>
            val List(t1, t2) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2]"
          case 3 =>
            val List(t1, t2, t3) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3]"
          case 4 =>
            val List(t1, t2, t3, t4) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4]"
          case 5 =>
            val List(t1, t2, t3, t4, t5) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5]"
          case 6 =>
            val List(t1, t2, t3, t4, t5, t6) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6]"
          case 7 =>
            val List(t1, t2, t3, t4, t5, t6, t7) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7]"
          case 8 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8]"
          case 9 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9]"
          case 10 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10]"
          case 11 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11]"
          case 12 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12]"
          case 13 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13]"
          case 14 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14]"
          case 15 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15]"
          case 16 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16]"
          case 17 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17]"
          case 18 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18]"
          case 19 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19]"
          case 20 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20]"
          case 21 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 with $t21]"
          case 22 =>
            val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) = classes(props)
            tq"${TypeName(cls)}[Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 with $t21 with $t22]"
        }
      }
    }


    def properties(nodes: List[Node]): List[Tree] = {
      nodes.map {
        case Prop(_, prop, tpe, cast) =>
          i += 1
          q"final override lazy val ${TermName(prop)}: ${TypeName(tpe)} = ${cast(i)}"

        case o@Obj(_, ref, _, props) =>
          val (subObj, j) = objCode(o, i)
          i = j
          props.length match {
            case 1 =>
              val t1 = classes(props).head
              q"final override def ${TermName(ref)}: Init with $t1 = $subObj"
            case 2 =>
              val List(t1, t2) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 = $subObj"
            case 3 =>
              val List(t1, t2, t3) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 = $subObj"
            case 4 =>
              val List(t1, t2, t3, t4) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 = $subObj"
            case 5 =>
              val List(t1, t2, t3, t4, t5) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 = $subObj"
            case 6 =>
              val List(t1, t2, t3, t4, t5, t6) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 = $subObj"
            case 7 =>
              val List(t1, t2, t3, t4, t5, t6, t7) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 = $subObj"
            case 8 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 = $subObj"
            case 9 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 = $subObj"
            case 10 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 = $subObj"
            case 11 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 = $subObj"
            case 12 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 = $subObj"
            case 13 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 = $subObj"
            case 14 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 = $subObj"
            case 15 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 = $subObj"
            case 16 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 = $subObj"
            case 17 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 = $subObj"
            case 18 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 = $subObj"
            case 19 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 = $subObj"
            case 20 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 = $subObj"
            case 21 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 with $t21 = $subObj"
            case 22 =>
              val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) = classes(props)
              q"final override def ${TermName(ref)}: Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 with $t21 with $t22 = $subObj"
          }
      }
    }

    val tree = obj.props.length match {
      case 1 =>
        val t1 = classes(obj.props).head
        q"new Init with $t1 { ..${properties(obj.props)} }"
      case 2 =>
        val List(t1, t2) = classes(obj.props)
        q"new Init with $t1 with $t2 { ..${properties(obj.props)} }"
      case 3 =>
        val List(t1, t2, t3) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 { ..${properties(obj.props)} }"
      case 4 =>
        val List(t1, t2, t3, t4) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 { ..${properties(obj.props)} }"
      case 5 =>
        val List(t1, t2, t3, t4, t5) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 { ..${properties(obj.props)} }"
      case 6 =>
        val List(t1, t2, t3, t4, t5, t6) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 { ..${properties(obj.props)} }"
      case 7 =>
        val List(t1, t2, t3, t4, t5, t6, t7) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 { ..${properties(obj.props)} }"
      case 8 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 { ..${properties(obj.props)} }"
      case 9 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 { ..${properties(obj.props)} }"
      case 10 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 { ..${properties(obj.props)} }"
      case 11 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 { ..${properties(obj.props)} }"
      case 12 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 { ..${properties(obj.props)} }"
      case 13 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 { ..${properties(obj.props)} }"
      case 14 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 { ..${properties(obj.props)} }"
      case 15 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 { ..${properties(obj.props)} }"
      case 16 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 { ..${properties(obj.props)} }"
      case 17 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 { ..${properties(obj.props)} }"
      case 18 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 { ..${properties(obj.props)} }"
      case 19 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 { ..${properties(obj.props)} }"
      case 20 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 { ..${properties(obj.props)} }"
      case 21 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 with $t21 { ..${properties(obj.props)} }"
      case 22 =>
        val List(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) = classes(obj.props)
        q"new Init with $t1 with $t2 with $t3 with $t4 with $t5 with $t6 with $t7 with $t8 with $t9 with $t10 with $t11 with $t12 with $t13 with $t14 with $t15 with $t16 with $t17 with $t18 with $t19 with $t20 with $t21 with $t22 { ..${properties(obj.props)} }"
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
