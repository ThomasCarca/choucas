package com.pinpoint

import akka.actor.{ Actor, ActorLogging, PoisonPill }

import com.pinpoint.PinpointRegistry.getPinpointFromUri

class PinpointRegistryActor extends Actor with ActorLogging {

  def receive = {
    case "Kill yourself" => self ! PoisonPill
    case getPinpointFromUri(jsonData) => sender() ! PinpointService.processJsonData(jsonData)
  }

}
