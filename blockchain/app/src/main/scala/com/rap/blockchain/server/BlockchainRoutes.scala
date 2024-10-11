package com.rap.blockchain.server

import cats.effect.Sync
import cats.implicits._
import com.rap.blockchain.algebra.BlockchainAlgebra
import io.circe.Json
import io.circe.syntax.*
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl

object BlockchainRoutes:
  def apply[F[_]: Sync](alg: BlockchainAlgebra[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "mine" =>
        for {
          attemptMining <- alg.proof()
          resp <- attemptMining match
            case None =>
              Ok(Json.obj("message" -> Json.fromString("Max attempts reached. Try again later...")))
            case Some(proof) =>
              for {
                blockchain <- alg.append(proof)
                resp <- Ok(
                  Json.obj(
                    "message" -> Json.fromString("Congratulations!"),
                    "block" -> blockchain.asJson
                  )
                )
              } yield resp

        } yield resp

      case GET -> Root / "check" =>
        alg.check().flatMap(b => Ok(Json.fromBoolean(b)))

      case GET -> Root / "chain" =>
        for {
          blocks <- alg.linearize()
          resp <- Ok(Json.obj(
            "len" -> Json.fromInt(blocks.size),
            "blocks" -> Json.fromValues(blocks.map(_.asJson))
          ))
        } yield resp
    }
