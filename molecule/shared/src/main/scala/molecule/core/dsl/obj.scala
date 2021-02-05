package molecule.core.dsl

object obj {

  trait Obj[props] {
    def getObj: props = ???
  }

  trait Composite[o0[_], p0] extends Obj[p0] {
    def +[o1[_], p](next: Composite[o1, p]): Composite[o0, p0 with o1[p]] = ???
  }
}
