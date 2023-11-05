package com.rap.blockchain.environment

enum EnvironmentVariable {
  case SERVER_HOST
  case SERVER_PORT
  case MAX_ATEMPTS
}

object Environment {
  def env(envVar: EnvironmentVariable) = ciris.env(envVar.toString())
}
