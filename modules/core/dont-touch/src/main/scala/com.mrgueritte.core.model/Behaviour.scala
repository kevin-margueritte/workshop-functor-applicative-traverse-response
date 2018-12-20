package com.mrgueritte.core.model

import enumeratum._

sealed trait Behaviour extends EnumEntry

object Behaviour extends Enum[Behaviour] {
  def values = findValues

  case object Cloudy    extends Behaviour
  case object Rainy     extends Behaviour
  case object Sunshine  extends Behaviour
}
