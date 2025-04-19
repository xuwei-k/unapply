# Scala case class unapply compatibility https://github.com/scala/scala3/issues/2335

build.sbt

```scala
libraryDependencies += "com.github.xuwei-k" %% "unapply" % "version"
```

```scala
scala> import unapply.syntax._
import unapply.syntax._

scala> case class A(x: Int, y: String)
class A

scala> A(2, "x").asTuple
val res0: (Int, String) = (2,x)

scala> A(2, "x").asTupleOption
val res1: Option[(Int, String)] = Some((2,x))

scala> case class B(x: Int)
class B

scala> B(3).asTuple
val res2: Int = 3

scala> B(3).asTupleOption
val res3: Option[Int] = Some(3)
```
