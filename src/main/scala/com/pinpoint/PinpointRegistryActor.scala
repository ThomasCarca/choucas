package com.pinpoint

import akka.actor.{ Actor, ActorLogging, PoisonPill, Props }

object PinpointRegistryActor {
  case class getPinpointFromUri(msg: String)
  def props: Props = Props[PinpointRegistryActor]
}

class PinpointRegistryActor extends Actor with ActorLogging {

  import PinpointRegistryActor._

  def receive = {
    case getPinpointFromUri(jsonData) => sender() ! PinpointService.processJsonData(jsonData)
  }

}
