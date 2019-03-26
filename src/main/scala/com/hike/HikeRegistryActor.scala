package com.hike

import akka.actor.{Actor, ActorLogging, Props}

final case class Hike(idOrigin: Long, description: String)
final case class Hikes(hikes: Seq[Hike])

object HikeRegistryActor {
  final case object GetHikes
  def props: Props = Props[HikeRegistryActor]
}

class HikeRegistryActor extends Actor with ActorLogging {
  import HikeRegistryActor._


  def receive: Receive = {
    case GetHikes =>
      sender() ! HikeService.getHikes
  }
}