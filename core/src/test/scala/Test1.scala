import org.scalatest.freespec.AnyFreeSpec
import unapply.syntax._

class Test1 extends AnyFreeSpec {
  private def typed[A](a: A): Unit = ()

  "test" in {
    typed[(Int, String)](Test1.a)

    assert(Test1.a == (2, "a"))

    typed[Option[(Int, String)]](Test1.aOption)

    typed[(Boolean, List[Boolean], Option[Boolean])](Test1.b)

    typed[Int](Test1.c)

    assert(Test1.c == 2)

    assert(Test1.a.getClass == classOf[Tuple2[?, ?]])
    assert(Test1.b.getClass == classOf[Tuple3[?, ?, ?]])
    assert(Test1.c.getClass == classOf[Int])

    val class22 = Class22[Long](
      "1",
      2,
      3,
      "4",
      5L,
      6,
      "7",
      8,
      9,
      "10",
      11,
      12,
      "13",
      14,
      15,
      "16",
      17,
      18,
      "19",
      20,
      21,
      "22"
    )
    type X = (
      String,
      Long,
      Int,
      String,
      Long,
      Int,
      String,
      Long,
      Int,
      String,
      Long,
      Int,
      String,
      Long,
      Int,
      String,
      Long,
      Int,
      String,
      Long,
      Int,
      String
    )
    typed[X](class22.asTuple)
    typed[Option[X]](class22.asTupleOption)

    assert(
      class22.asTuple == (
        "1",
        2,
        3,
        "4",
        5L,
        6,
        "7",
        8,
        9,
        "10",
        11,
        12,
        "13",
        14,
        15,
        "16",
        17,
        18,
        "19",
        20,
        21,
        "22"
      )
    )
  }
}

object Test1 {
  case class A(x1: Int, x2: String)

  case class B[T](x1: T, x2: List[T], x3: Option[T])

  case class C(x: Int)

  def a = A(2, "a").asTuple

  def aOption = A(2, "a").asTupleOption

  def b = B[Boolean](false, List(true, false), Option(true)).asTuple

  def c = C(2).asTuple
}
