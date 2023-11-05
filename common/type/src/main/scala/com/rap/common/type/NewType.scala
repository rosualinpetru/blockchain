package com.rap.common.`type`

trait NewType[Src] {
  opaque type Type = Src

  def apply(value: Src): Type = value

  def unapply(self: Type): Option[Src] = Some(self.value)

  extension (self: Type)
    def value: Src = self
}
