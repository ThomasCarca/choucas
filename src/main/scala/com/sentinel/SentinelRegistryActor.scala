package com.sentinel

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.DatedBoundingBox


object SentinelRegistryActor {
  final case class SentinelInfo(box: DatedBoundingBox)
  def props: Props = Props[SentinelRegistryActor]
}


class SentinelRegistryActor extends Actor with ActorLogging {
  import SentinelRegistryActor._

  def receive: Receive = {
    case SentinelInfo(box) =>
      sender() ! SentinelService.fetchImagesInfo(box)
  }
}