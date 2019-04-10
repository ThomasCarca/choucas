package com.tile

import akka.actor.{Actor, ActorLogging, Props}

object DownloadRegistryActor {
  final case class DownloadImages(urls: Vector[String])
  def props: Props = Props[TileRegistryActor]
}


class DownloadRegistryActor extends Actor with ActorLogging {
  import DownloadRegistryActor._

  def receive: Receive = {
    case DownloadImages(urls) =>
      DownloadService.downloadImages(urls)
  }
}
