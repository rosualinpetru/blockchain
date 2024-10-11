package com.rap.blockchain

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.unsafe.IORuntimeConfig
import com.rap.blockchain.server.BlockchainServer
import com.rap.common.logging.Logging

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.*
import scala.util.Try

object Main extends IOApp.Simple with Logging:
  override def runtimeConfig: IORuntimeConfig = 
    val DEFAULT_SHUTDOWN_HOOK_TIMEOUT = 30.seconds
    IORuntimeConfig().copy(
      shutdownHookTimeout =
        Option(System.getenv("SHUTDOWN_HOOK_TIMEOUT"))
          .filterNot(_.isEmpty)
          .flatMap { str =>
            Try(Duration(str))
              .orElse(Try(Duration(str.toLong, TimeUnit.SECONDS)))
              .toOption
          }
          .getOrElse(DEFAULT_SHUTDOWN_HOOK_TIMEOUT)
    )


  override def reportFailure(err: Throwable): IO[Unit] =
    getLogger[IO].error(err)("Uncaught error in thread-pool").as(())

  val run: IO[Unit] = BlockchainServer.run[IO]
