import sbt.Project.projectToRef

import NativePackagerHelper.*

lazy val root = Project(id = "blockchain", base = file("."))
  .aggregate(projects.map(projectToRef) *)
  .settings(sharedSettings)
  .settings(
    name := "blockchain",
    Compile / compile := (Compile / compile)
      .dependsOn(Compile / scalafmtSbt)
      .value
  )

lazy val projects = List[Project](
  // COMMON
  `common`,
  `logging`,
  `type`,
  // APP
  `blockchain-app`
)

lazy val sharedSettings = Seq(
  version := "latest",
  organization := "com.rap",
  scalaVersion := Libraries.scalaVersion,
  scalacOptions ++= Seq("-Xmax-inlines:50"),
  libraryDependencies ++= Seq(
    Libraries.cats,
    Libraries.catsRetry,
    Libraries.catsEffect,
    Libraries.fs2,
    Libraries.fs2IO
  )
)

def module(path: List[String], baseDir: Option[String] = None): Project = {
  val id   = path.map(_.replaceAll("[^\\w-]+", "")).mkString("-")
  val base = path.foldLeft(file(baseDir.getOrElse("."))) { (dir, file) => dir / file }

  Project(id = id, base = base)
    .settings(sharedSettings)
    .settings(
      initialize := {
        val _ = initialize.value
        if (!projects.exists(_.id == id))
          sys.error(s"Project `$id` was not declared in `projects`")
      }
    )
}

// * ------------------------------------------------------- *
// * ------------------------------------------------------- *
//     Common Modules
// * ------------------------------------------------------- *
// * ------------------------------------------------------- *

def commonModule(path: String*): Project =
  module("common" :: path.toList)

lazy val `common` = commonModule()
  .dependsOn(`type`, `logging`)
  .aggregate(`type`, `logging`)

lazy val `type` = commonModule("type")

lazy val `logging` = commonModule("logging")
  .settings(
    libraryDependencies ++= Seq(
      Libraries.logback,
      Libraries.log4cats,
      Libraries.sourcecode
    )
  )

// * ------------------------------------------------------- *
// * ------------------------------------------------------- *
//     Blockchain Modules
// * ------------------------------------------------------- *
// * ------------------------------------------------------- *

def blockchainModule(path: String*): Project =
  module("blockchain" :: path.toList)

lazy val `blockchain-app` =
  blockchainModule("app")
    .dependsOn(`common`)
    .aggregate(`common`)
    .settings(
      libraryDependencies ++= Seq(
        Libraries.http4s,
        Libraries.http4sCirce,
        Libraries.http4sEmberClient,
        Libraries.http4sEmberServer,
        Libraries.circe,
        Libraries.circeGeneric,
        Libraries.circeParser,
        Libraries.ciris,
        Libraries.cirisCirce,
        Libraries.cirisHttp4s
      )
    )
