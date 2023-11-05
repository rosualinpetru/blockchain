val Http4sVersion = "1.0.0-M40"
val MunitVersion = "1.0.0-M10"
val LogbackVersion = "1.4.11"
val MunitCatsEffectVersion = "2.0.0-M3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.rap",
    name := "blockchain",
    version := "0.0.1",
    scalaVersion := "3.3.1",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
