package com.sentinel

import akka.actor.{Actor, ActorLogging, Props}
import com.box.BoundingBox


object SentinelRegistryActor {
  final case class SentinelInfo(box: BoundingBox)
  def props: Props = Props[SentinelRegistryActor]
}


class SentinelRegistryActor extends Actor with ActorLogging {
  import SentinelRegistryActor._

  def receive: Receive = {
    case SentinelInfo(box) =>
      sender() ! SentinelService.fetchImagesInfo(box)
  }
}