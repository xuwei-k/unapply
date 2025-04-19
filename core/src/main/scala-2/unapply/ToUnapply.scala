package unapply

import shapeless.DepFn1
import shapeless.Generic
import shapeless.HList

sealed abstract class ToUnapply[P] extends DepFn1[P]

object ToUnapply {
  def apply[P](implicit to: ToUnapply[P]): Aux[P, to.Out] = to

  type Aux[P, Out0] = ToUnapply[P] { type Out = Out0 }

  implicit def instance[P, L <: HList, T, Out0](implicit
    gen: Generic.Aux[P, L],
    instance: Unapply.Aux[L, T],
    ev: T <:< Out0
  ): Aux[P, Out0] =
    new ToUnapply[P] {
      type Out = Out0
      def apply(p: P) = ev(instance.convert(gen.to(p)))
    }
}
