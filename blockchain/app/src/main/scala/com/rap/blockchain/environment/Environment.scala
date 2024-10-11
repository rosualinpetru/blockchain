package com.rap.blockchain.environment

import ciris.{ConfigValue, Effect}

object Environment {
  enum EnvironmentVariable {
    case SERVER_HOST
    case SERVER_PORT
    case MAX_ATTEMPTS
  }

  def env(envVar: EnvironmentVariable): ConfigValue[Effect, String] = ciris.env(envVar.toString)
}
