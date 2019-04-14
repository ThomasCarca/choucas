package com.hike

import akka.actor.{Actor, ActorLogging, Props}

object HikeRegistryActor {
  final case object GetHikes
  final case class GetHikeById(id: String)
  def props: Props = Props[HikeRegistryActor]
}

class HikeRegistryActor extends Actor with ActorLogging {
  import HikeRegistryActor._


  def receive: Receive = {
    case GetHikes =>
      sender() ! HikeService.getAllHikes
    case GetHikeById(id) =>
      sender() ! HikeService.getHikeById(id)

  }
}