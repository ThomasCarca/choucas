package com.tile

import java.util.UUID.randomUUID

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.{Job, JobQueue}

object TileRegistryActor {
  final case class TileImage(Urls: Vector[String])
  def props: Props = Props[TileRegistryActor]
}

class TileRegistryActor extends Actor with ActorLogging {
  import TileRegistryActor._
  def createJobQueue(urls: Vector[String]): JobQueue = {
    val jobs = urls.map(url => new Job(createUUID(url), url))
    val queue = new JobQueue
    queue.total = urls.length
    queue.jobs = jobs
    return queue
  }

  def createUUID(url: String): String = {
    val uuidRegex = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"
    val urlPattern = s"https:\\/\\/peps.cnes.fr\\/resto\\/collections\\/S2\\/($uuidRegex)\\/download".r
    url match {
      case urlPattern(uuid) => uuid
      case _ => randomUUID().toString
    }
  }

  def receive: Receive = {
    case TileImage(urls) =>
      val queue = createJobQueue(urls)
      sender() ! queue
      TileService.tileImage(queue)
  }
}
