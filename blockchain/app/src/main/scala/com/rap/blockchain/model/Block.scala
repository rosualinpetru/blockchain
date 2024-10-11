package com.rap.blockchain.model

import io.circe.syntax.*
import cats.effect.Sync
import cats.implicits.*
import java.security.Timestamp
import java.time.ZonedDateTime
import scala.quoted.ToExpr.NoneToExpr
import java.security.MessageDigest
import io.circe.Encoder
import java.nio.charset.StandardCharsets
import io.circe.Json

final case class Block(
  index: BigInt,
  timestamp: ZonedDateTime,
  proof: Proof,
  previousHash: Option[Hash],
  previousBlock: Option[Block]
)

object Block {
  given Encoder[Block] = block => {
    import block.*
    Json.obj(
      "index" -> Json.fromBigInt(index),
      "timestamp" -> Json.fromString(timestamp.toString),
      "proof" -> Json.fromBigInt(proof.value),
      "previousHash" -> previousHash.map(hash => Json.fromString(hash.value)).getOrElse(Json.Null)
    ).deepDropNullValues
  }
}
