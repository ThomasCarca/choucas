package com.pinpoint

import akka.actor.Props

object PinpointRegistry {
  case class getPinpointFromUri(msg: String)
  def props: Props = Props[PinpointRegistryActor]
}
