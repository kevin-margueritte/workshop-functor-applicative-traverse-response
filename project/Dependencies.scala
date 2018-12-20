import sbt._

object Dependencies {

  object Version {
    val enumeratumV     = "1.5.13"
    val catsV           = "1.1.0"
    val kindProjectorV  = "0.9.8"
    val scalaTestV      = "3.0.5"
    val scalacheckV     = "1.14.0"
    val circeVersion    = "0.10.1"
  }

  object Library {
    val enumeratum  = "com.beachape"    %% "enumeratum"   % Version.enumeratumV
    val cats        = "org.typelevel"   %% "cats-effect"  % Version.catsV
    val scalatest   = "org.scalatest"   %% "scalatest"    % Version.scalaTestV  % "test"
    val scalacheck  = "org.scalacheck"  %% "scalacheck"   % Version.scalacheckV % "test"
    val circe       = Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % Version.circeVersion)
  }

  object Plugin {
    val kindProjector = "org.spire-math" %% "kind-projector" % Version.kindProjectorV
  }

}
