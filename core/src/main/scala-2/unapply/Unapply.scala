package unapply

import shapeless.HList

object Unapply extends UnapplyInstances {
  type Aux[X <: HList, Y] = Unapply[X] { type B = Y }

  private[unapply] def instance[X <: HList, Y](f: X => Y): Aux[X, Y] =
    new Unapply[X] {
      override type B = Y
      override def convert(a: X): Y = f(a)
    }
}

sealed abstract class Unapply[A <: HList] {
  type B
  def convert(a: A): B
}
