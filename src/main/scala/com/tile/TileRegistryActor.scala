package com.tile

import akka.actor.{Actor, ActorLogging, Props}

object TileRegistryActor {
  final case class TileImage(imageName: String)
  def props: Props = Props[TileRegistryActor]
}

class TileRegistryActor extends Actor with ActorLogging {
  import TileRegistryActor._

  def receive: Receive = {
    case TileImage(imageName) =>
      sender() ! TileService.TileImage(imageName)
  }
}
