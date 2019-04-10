package com.tile

import java.util.UUID.randomUUID

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.{Job, JobQueue}

object DownloadRegistryActor {
  final case class DownloadImages(urls: Vector[String])
  def props: Props = Props[DownloadRegistryActor]
}


class DownloadRegistryActor extends Actor with ActorLogging {
  import DownloadRegistryActor._

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
    case DownloadImages(urls) => {
      val queue = createJobQueue(urls)
      sender() ! queue
      DownloadService.downloadImages(queue)
    }

  }
}
