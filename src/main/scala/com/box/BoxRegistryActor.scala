package com.box

import akka.actor.{ Actor, ActorLogging, Props }
import spray.json._

object BoxRegistryActor {
  final case class ToBoundingBox(coordinates: Vector[Coordinate])
  def props: Props = Props[BoxRegistryActor]
}

class BoxRegistryActor extends Actor with ActorLogging with BoxJsonSupport {
  import BoxRegistryActor._

  def receive: Receive = {
    case ToBoundingBox(coordinates) =>
      sender() ! BoxService.createBoundingBox(coordinates).toJson.prettyPrint
  }
}