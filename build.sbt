import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

val scalaVersions = Seq("2.12.21", "2.13.18", "3.3.7")

lazy val unapply = projectMatrix
  .in(file("core"))
  .defaultAxes()
  .jvmPlatform(scalaVersions = scalaVersions)
  .jsPlatform(scalaVersions = scalaVersions)
  .nativePlatform(scalaVersions = scalaVersions)
  .settings(
    name := "unapply",
    organization := "com.github.xuwei-k",
    publishTo := (if (isSnapshot.value) None else localStaging.value),
    libraryDependencies += "org.scalatest" %%% "scalatest-freespec" % "3.2.19" % Test,
    libraryDependencies ++= {
      if (scalaBinaryVersion.value == "3") {
        Nil
      } else {
        Seq("com.chuusai" %%% "shapeless" % "2.3.13")
      }
    },
    pomExtra := (
      <developers>
        <developer>
          <id>xuwei-k</id>
          <name>Kenji Yoshida</name>
          <url>https://github.com/xuwei-k</url>
        </developer>
      </developers>
        <scm>
          <url>git@github.com:xuwei-k/unapply.git</url>
          <connection>scm:git:git@github.com:xuwei-k/unapply.git</connection>
        </scm>
    ),
    homepage := Some(url("https://github.com/xuwei-k/unapply")),
    licenses := List(
      "MIT License" -> url("https://opensource.org/licenses/mit-license")
    ),
    scalacOptions ++= Seq(
      "-deprecation",
    ),
    scalacOptions += {
      scalaBinaryVersion.value match {
        case "2.12" =>
          "-Ywarn-unused-import"
        case _ =>
          "-Wunused:imports"
      }
    },
    scalacOptions ++= {
      scalaBinaryVersion.value match {
        case "2.12" =>
          Seq(
            "-Xsource:3",
          )
        case "2.13" =>
          Seq(
            "-Xsource:3-cross",
          )
        case _ =>
          Nil
      }
    },
    Compile / sourceGenerators += task {
      if (scalaBinaryVersion.value == "3") {
        Seq.empty[File]
      } else {
        val values = (2 to 22).map { n =>
          val x = (1 to n).map("A" + _)
          val y = (1 to n).map("a" + _)
          s"""
  implicit final def unapplyInstance$n[${x.mkString(", ")}]: Unapply.Aux[${x.mkString("", " :: ", " :: HNil")}, (${x
              .mkString(", ")})] =
    Unapply.instance[${x.mkString("", " :: ", " :: HNil")}, (${x
              .mkString(", ")})] { case ${y.mkString("", " :: ", " :: HNil")} =>
      (${y.mkString(", ")})
    }"""
        }
        val src = s"""package unapply

import shapeless.HNil
import shapeless.::

trait UnapplyInstances { self: Unapply.type =>

  implicit final def unapplyInstance1[A1]: Unapply.Aux[A1 :: HNil, A1] =
    Unapply.instance[A1 :: HNil, A1] { case a1 :: HNil =>
      a1
    }
${values.mkString("\n")}
}
"""
        val f = (Compile / sourceManaged).value / "unapply" / "UnapplyInstances.scala"
        IO.write(f, src)
        Seq(f)
      }
    }
  )

publish / skip := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("publishSigned"),
  releaseStepCommandAndRemaining("sonaRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

ThisBuild / scalafixDependencies += "com.github.xuwei-k" %% "scalafix-rules" % "0.6.23"
