package com.rap.blockchain.model

import com.rap.common.`type`.NewType

type Hash = Hash.Type
object Hash extends NewType[String]

type Proof = Proof.Type
object Proof extends NewType[BigInt]
