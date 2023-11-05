package com.rap.blockchain

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = BlockchainServer.run[IO]
