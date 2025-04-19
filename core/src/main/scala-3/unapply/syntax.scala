package unapply

import scala.deriving.Mirror

object syntax {
  private type MirrorElem[A] = ({ type MirroredElemTypes }) { type MirroredElemTypes = A }

  type Type[A <: Mirror] = A match {
    case MirrorElem[b] =>
      b match {
        case Tuple1[c] =>
          c
        case _ =>
          b
      }
  }

  implicit class UnapplySyntax[A <: Product](private val value: A) extends AnyVal {
    def asTuple(using instance: Mirror.ProductOf[A]): Type[instance.type] =
      Tuple.fromProductTyped(value) match {
        case Tuple1(a) =>
          a.asInstanceOf[Type[instance.type]]
        case other =>
          other.asInstanceOf[Type[instance.type]]
      }

    def asTupleOption(using instance: Mirror.ProductOf[A]): Option[Type[instance.type]] =
      Option(asTuple)
  }
}
