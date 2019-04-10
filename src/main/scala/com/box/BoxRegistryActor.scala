package com.box

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.Coordinates

object BoxRegistryActor {
  final case class ToBoundingBox(coordinates: Vector[Coordinates])
  def props: Props = Props[BoxRegistryActor]
}

class BoxRegistryActor extends Actor with ActorLogging {
  import BoxRegistryActor._

  def receive: Receive = {
    case ToBoundingBox(coordinates) =>
      sender() ! BoxService.createBoundingBox(coordinates)
  }
}