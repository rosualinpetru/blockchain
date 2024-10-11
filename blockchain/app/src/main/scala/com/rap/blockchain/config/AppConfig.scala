package com.rap.blockchain.config

import cats.effect.Sync
import cats.implicits.*
import ciris.{ConfigDecoder, ConfigValue, Effect}
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import com.comcast.ip4s.ipv4
import com.comcast.ip4s.port

final case class AppConfig(
  serverHost: Host,
  serverPort: Port,
  maxAttempts: Int
)

object AppConfig {
  import com.rap.blockchain.environment.Environment.*
  import com.rap.blockchain.environment.Environment.EnvironmentVariable.*

  def apply[F[_]: Sync]: ConfigValue[Effect, AppConfig] = {
    implicit val _: ConfigDecoder[String, Host] =
      ConfigDecoder[String].mapOption("com.comcast.ip4s.Host")(Host.fromString)
    implicit val _: ConfigDecoder[Int, Port] =
      ConfigDecoder[Int].mapOption("com.comcast.ip4s.Port")(Port.fromInt)
    (
      env(SERVER_HOST).as[String].as[Host].default(ipv4"0.0.0.0"),
      env(SERVER_PORT).as[Int].as[Port].default(port"8080"),
      env(MAX_ATTEMPTS).as[Int].default(50000)
    ).parMapN((host, port, maxAttempts) => new AppConfig(host, port, maxAttempts))
  }
}
