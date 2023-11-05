package com.rap.common.logging

import org.typelevel.log4cats.SelfAwareStructuredLogger

private[logging] class ContextWithSourceSelfAwareStructuredLogger[F[_]](sl: SelfAwareStructuredLogger[F])
  extends SelfAwareStructuredLogger[ContextWithSourceSelfAwareStructuredLogger.SourceContextual[F, *]]:

  import ContextWithSourceSelfAwareStructuredLogger.SourceContextual

  private def prependSourceCtx(ctx: Map[String, String])(using s: Source): Map[String, String] =
    Map("file" -> s.file, "line" -> s.line.toString) ++ ctx

  def error(message: => String): SourceContextual[F, Unit] = sl.error(prependSourceCtx(Map.empty))(message)
  def warn(message: => String): SourceContextual[F, Unit]  = sl.warn(prependSourceCtx(Map.empty))(message)
  def info(message: => String): SourceContextual[F, Unit]  = sl.info(prependSourceCtx(Map.empty))(message)
  def debug(message: => String): SourceContextual[F, Unit] = sl.debug(prependSourceCtx(Map.empty))(message)
  def trace(message: => String): SourceContextual[F, Unit] = sl.trace(prependSourceCtx(Map.empty))(message)

  def trace(ctx: Map[String, String])(msg: => String): SourceContextual[F, Unit] =
    sl.trace(prependSourceCtx(ctx))(msg)
  def debug(ctx: Map[String, String])(msg: => String): SourceContextual[F, Unit] =
    sl.debug(prependSourceCtx(ctx))(msg)
  def info(ctx: Map[String, String])(msg: => String): SourceContextual[F, Unit] =
    sl.info(prependSourceCtx(ctx))(msg)
  def warn(ctx: Map[String, String])(msg: => String): SourceContextual[F, Unit] =
    sl.warn(prependSourceCtx(ctx))(msg)
  def error(ctx: Map[String, String])(msg: => String): SourceContextual[F, Unit] =
    sl.error(prependSourceCtx(ctx))(msg)

  def isTraceEnabled: SourceContextual[F, Boolean] = sl.isTraceEnabled
  def isDebugEnabled: SourceContextual[F, Boolean] = sl.isDebugEnabled
  def isInfoEnabled: SourceContextual[F, Boolean]  = sl.isInfoEnabled
  def isWarnEnabled: SourceContextual[F, Boolean]  = sl.isWarnEnabled
  def isErrorEnabled: SourceContextual[F, Boolean] = sl.isErrorEnabled

  def error(t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.error(prependSourceCtx(Map.empty), t)(message)
  def warn(t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.warn(prependSourceCtx(Map.empty), t)(message)
  def info(t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.info(prependSourceCtx(Map.empty), t)(message)
  def debug(t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.debug(prependSourceCtx(Map.empty), t)(message)
  def trace(t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.trace(prependSourceCtx(Map.empty), t)(message)

  def error(ctx: Map[String, String], t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.error(prependSourceCtx(ctx), t)(message)
  def warn(ctx: Map[String, String], t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.warn(prependSourceCtx(ctx), t)(message)
  def info(ctx: Map[String, String], t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.info(prependSourceCtx(ctx), t)(message)
  def debug(ctx: Map[String, String], t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.debug(prependSourceCtx(ctx), t)(message)
  def trace(ctx: Map[String, String], t: Throwable)(message: => String): SourceContextual[F, Unit] =
    sl.trace(prependSourceCtx(ctx), t)(message)


object ContextWithSourceSelfAwareStructuredLogger:
  private type SourceContextual[F[_], A] = Source ?=> F[A]
