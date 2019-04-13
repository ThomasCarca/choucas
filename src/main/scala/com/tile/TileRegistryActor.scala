package com.tile

import java.util.UUID.randomUUID

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.{Job, JobQueue}

import scala.concurrent.Future

object TileRegistryActor {
  final case class TileImage(queue: Future[JobQueue])
  def props: Props = Props[TileRegistryActor]
}

class TileRegistryActor extends Actor with ActorLogging {
  import TileRegistryActor._

  def receive: Receive = {
      TileService.tileImage(queue)
  }
}
