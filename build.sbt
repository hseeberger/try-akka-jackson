// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `try-akka-jackson` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.akkaSerializationJackson,
      ),
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val akka = "2.6.4"
    }
    val akkaSerializationJackson = "com.typesafe.akka" %% "akka-serialization-jackson" % Version.akka
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  scalafmtSettings

lazy val commonSettings =
  Seq(
    scalaVersion := "2.13.1",
    organization := "rocks.heikoseeberger",
    organizationName := "Heiko Seeberger",
    startYear := Some(2020),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-encoding", "UTF-8",
      "-Ywarn-unused:imports",
    ),
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )
