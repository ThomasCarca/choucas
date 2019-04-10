package com.pinpoint

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.URIs

object PinpointRegistryActor {
  final case class ToCoordinates(uris: URIs)
  def props: Props = Props[PinpointRegistryActor]
}

class PinpointRegistryActor extends Actor with ActorLogging {
  import PinpointRegistryActor._

  def receive: Receive = {
    case ToCoordinates(uris) =>
      sender() ! PinpointService.fetchCoordinates(uris)
  }

}
