package com.tile

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.JobQueue

object TileRegistryActor {
  final case class TileImage(queue: JobQueue)
  def props: Props = Props[TileRegistryActor]
}

class TileRegistryActor extends Actor with ActorLogging {
  import TileRegistryActor._

  def receive: Receive = {
    case TileImage(queue) =>
      sender() ! TileService.tileImage(queue)
  }
}
