package com.rap.blockchain.server

import cats.effect.Async
import cats.implicits.*
import com.rap.blockchain.algebra.BlockchainAlgebra
import com.rap.blockchain.config.AppConfig
import com.rap.common.logging.Logging
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import cats.effect.Sync
import java.time.ZonedDateTime

object BlockchainServer extends Logging:
  def run[F[_]: Async]: F[Unit] = for {
    logger         <- logger
    config         <- AppConfig[F].load
    _              <- logger.info("Config loaded")
    initialBlockTS <- Sync[F].delay(ZonedDateTime.now())
    blockchainAlgebra <- BlockchainAlgebra[F](config.maxAttempts, initialBlockTS)
    httpApp           = BlockchainRoutes[F](blockchainAlgebra).orNotFound
    _ <- EmberServerBuilder.default(Async[F], Network.forAsync[F])
      .withHost(config.serverHost)
      .withPort(config.serverPort)
      .withHttpApp(httpApp)
      .build
      .useForever
  } yield ()
