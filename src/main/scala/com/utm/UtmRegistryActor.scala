package com.utm

import akka.actor.{ Actor, ActorLogging, Props }

object UtmRegistryActor {
  final case class ToUTM(coordinates: Vector[Coordinate])
  def props: Props = Props[UtmRegistryActor]
}

class UtmRegistryActor extends Actor with ActorLogging {
  import UtmRegistryActor._

  def receive: Receive = {
    case ToUTM(coordinates) =>
      sender() ! UtmService.fetchUTM(coordinates)
  }
}