import sbt.*

object Libraries {
  lazy val scalaVersion = "3.3.1"

  lazy val cats = "org.typelevel" %% "cats-core" % "2.12.0" withSources ()

  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "3.5.2" withSources ()

  lazy val catsRetry = "com.github.cb372" %% "alleycats-retry" % "3.1.3" withSources ()

  lazy val fs2Version = "3.10.2"
  lazy val fs2        = "co.fs2" %% "fs2-core" % fs2Version withSources ()
  lazy val fs2IO      = "co.fs2" %% "fs2-io"   % fs2Version withSources ()

  lazy val circeVersion = "0.14.9"
  lazy val circe        = "io.circe" %% "circe-core"    % circeVersion withSources ()
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion withSources ()
  lazy val circeParser  = "io.circe" %% "circe-parser"  % circeVersion withSources ()

  lazy val cirisVersion = "3.6.0"
  lazy val ciris        = "is.cir" %% "ciris"        % cirisVersion withSources ()
  lazy val cirisCirce   = "is.cir" %% "ciris-circe"  % cirisVersion withSources ()
  lazy val cirisHttp4s  = "is.cir" %% "ciris-http4s" % cirisVersion withSources ()

  lazy val http4sVersion     = "0.23.27"
  lazy val http4s            = "org.http4s" %% "http4s-ember-server" % http4sVersion withSources ()
  lazy val http4sCirce       = "org.http4s" %% "http4s-circe"        % http4sVersion withSources ()
  lazy val http4sEmberClient = "org.http4s" %% "http4s-ember-client" % http4sVersion withSources ()
  lazy val http4sEmberServer = "org.http4s" %% "http4s-dsl"          % http4sVersion withSources ()

  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.5.6" withSources ()

  lazy val log4cats = "org.typelevel" %% "log4cats-slf4j" % "2.6.0" withSources ()

  lazy val sourcecode = "com.lihaoyi" %% "sourcecode" % "0.4.2" withSources ()
}
