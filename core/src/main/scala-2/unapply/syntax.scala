package unapply

object syntax {
  implicit class UnapplySyntax[A](private val value: A) extends AnyVal {
    def asTuple[B](implicit instance: ToUnapply.Aux[A, B]): B =
      instance.apply(value)

    def asTupleOption[B](implicit instance: ToUnapply.Aux[A, B]): Option[B] =
      Option(asTuple[B])
  }
}
