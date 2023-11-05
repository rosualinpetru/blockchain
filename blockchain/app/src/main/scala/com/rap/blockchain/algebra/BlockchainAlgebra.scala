package com.rap.blockchain.algebra

import cats.effect.Ref
import cats.effect.Sync
import cats.implicits.*
import com.rap.blockchain.model.*
import io.circe.syntax.*

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.ZonedDateTime
import scala.annotation.tailrec
import cats.effect.std.Random
import cats.effect.std.SecureRandom

trait BlockchainAlgebra[F[_]](val blockchain: Ref[F, Block]) {
  def append(proof: Proof): F[Block]
  def proof(): F[Option[Proof]]
  def check(): F[Boolean]
  def liniarize(): F[List[Block]]
}

object BlockchainAlgebra:
  def apply[F[_]: Sync](maxAttempts: Int, initialBlockTS: ZonedDateTime): F[BlockchainAlgebra[F]] =
    Ref.of(Block(0, initialBlockTS, Proof(1), None, None)).map(ref =>
      new BlockchainAlgebra[F](ref):
        def append(proof: Proof): F[Block] =
          for {
            blockchain <- ref.get
            ts         <- Sync[F].delay(ZonedDateTime.now())
            block = Block(blockchain.index + 1, ts, proof, Some(hash(blockchain)), Some(blockchain))
            _ <- ref.set(block)
          } yield block

        def proof(): F[Option[Proof]] =
          def recurse(previousProof: Proof, currentProof: Proof, attempt: Int): F[Option[Proof]] =
            if (attempt == 0)
              None.pure[F]
            else if (checkProofs(previousProof, currentProof))
              Some(currentProof).pure[F]
            else
              SecureRandom.javaSecuritySecureRandom.flatMap(_.nextInt).flatMap(i =>
                recurse(previousProof, Proof(BigInt(i)), attempt - 1)
              )
          ref.get.flatMap(blockchain => recurse(blockchain.proof, Proof(BigInt(1)), maxAttempts))

        def check(): F[Boolean] =
          @tailrec
          def recurse(blockchain: Block): Boolean =
            (blockchain.previousHash, blockchain.previousBlock) match
              case (Some(h), Some(previous))
                  if hash(previous) == h && checkProofs(previous.proof, blockchain.proof) => recurse(previous)
              case (None, None) => blockchain.index == 0
              case _            => false

          ref.get.map(recurse)

        def liniarize(): F[List[Block]] =
          def recurse(blockchain: Block, acc: List[Block] = List()): List[Block] =
            blockchain.previousBlock match {
              case None           => blockchain :: acc
              case Some(previous) => recurse(previous, blockchain :: acc)
            }
          ref.get.map(blockchain => recurse(blockchain))

        private def hash(block: Block): Hash =
          Hash(hash(block.asJson.toString))

        private def checkProofs(previous: Proof, current: Proof): Boolean =
          val h = hash((current.value.pow(2) - previous.value.pow(2)).toString())
          h.startsWith("0000")

        private def hash(input: String): String =
          val digest = MessageDigest.getInstance("SHA-256")
          val bytes  = digest.digest(input.getBytes(StandardCharsets.UTF_8))
          bytes.map(b => Integer.toHexString(0xff & b)).flatMap(hex =>
            if (hex.length() == 1) List("0", hex) else List(hex)
          ).reduce(_ + _)
    )
