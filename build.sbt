import Dependencies.Library._
import Dependencies.Plugin._

name := "Workshop Functor/Applicative/Traverse"

lazy val root = (project in file("."))
  .settings(generalSettings)
  .aggregate(dontTouch, updateMe, exerciceFunctor, exerciceApplicative, exerciceTraverse)

lazy val dontTouch = (project in file("modules/core/dont-touch"))
  .settings(generalSettings)
  .dependsOn(updateMe)

lazy val updateMe = (project in file("modules/core/update-me"))
  .settings(generalSettings)
  .settings(generalImport)

lazy val exerciceFunctor = (project in file("exercice/functor"))
  .settings(generalSettings)
  .settings(libraryDependencies ++= circe)
  .dependsOn(dontTouch)

lazy val exerciceApplicative = (project in file("exercice/applicative"))
  .settings(generalSettings)
  .dependsOn(dontTouch)

lazy val exerciceTraverse = (project in file("exercice/traverse"))
  .settings(generalSettings)
  .dependsOn(dontTouch)

lazy val generalImport = libraryDependencies ++= cats :: scalatest :: scalacheck :: enumeratum :: Nil

lazy val generalSettings = Seq(
  organization := "com.mrgueritte",
  scalaVersion := "2.12.8",
  version      := "0.0.1",
  scalacOptions ++= Seq(
    "-deprecation", // Warn when deprecated API are used
    "-feature", // Warn for usages of features that should be importer explicitly
    "-unchecked", // Warn when generated code depends on assumptions
    "-Ywarn-dead-code", // Warn when dead code is identified
    "-Ywarn-numeric-widen", // Warn when numeric are widened
    "-Xlint", // Additional warnings (see scalac -Xlint:help)
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receive
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:existentials",
    "-language:higherKinds",
    "-language:experimental.macros"
  ),
  addCompilerPlugin(kindProjector),
  fork in Test := true
)