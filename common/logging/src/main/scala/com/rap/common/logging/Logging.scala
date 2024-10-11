package com.rap.common.logging

import cats.effect.Sync
import cats.implicits.*
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.StructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import scala.util.Try

trait Logging:
  private type Logger[F[_]] = ContextWithSourceSelfAwareStructuredLogger[F]

  private def loggerName =
    val nameSegments = getClass.getName.replaceAll("[^a-zA-Z0-9.]", "").split("\\.").toList
    val size         = nameSegments.size
    assert(size >= 1 && !nameSegments.contains(""))

    nameSegments.take(size - 1).map(_.charAt(0).toString()).reduce(_ + "." + _) + s".${nameSegments.drop(size - 1).head}"

  def logger[F[_]: Sync]: F[Logger[F]] =
    Slf4jLogger.fromName(Try(loggerName).getOrElse("UnknownLogger")).map(l => new Logger[F](l))

  def getLogger[F[_]: Sync]: Logger[F] =
    new Logger[F](Slf4jLogger.getLoggerFromName(Try(loggerName).getOrElse("UnknownLogger")))
