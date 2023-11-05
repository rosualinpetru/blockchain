package com.rap.common.`type`

trait SafeNewType[E, S] {
  opaque type T = S

  protected def check(value: S): Either[E, S]

  def apply(value: S): Either[E, T] = check(value)
  
  def unapply(self: T): Option[S] = Some(self.value)

  extension (self: T)
    def value: S = self
}
