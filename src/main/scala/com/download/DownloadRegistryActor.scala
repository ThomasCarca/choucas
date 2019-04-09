package com.download

import akka.actor.{Actor, ActorLogging, Props}
import com.box.BoundingBox


object DownloadRegistryActor {
  final case class DownloadImages(box: BoundingBox)
  def props: Props = Props[DownloadRegistryActor]
}


class DownloadRegistryActor extends Actor with ActorLogging {
  import DownloadRegistryActor._

  def receive: Receive = {
    case DownloadImages(box) =>
      sender() ! DownloadService.fetchDownloadUrls(box)
  }
}