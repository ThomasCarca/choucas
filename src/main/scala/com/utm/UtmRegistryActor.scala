package com.utm

import akka.actor.{Actor, ActorLogging, Props}

object UtmRegistryActor {
  final case class ToUTM(lat: String, lon: String)
  def props: Props = Props[UtmRegistryActor]
}

class UtmRegistryActor extends Actor with ActorLogging {
  import UtmRegistryActor._
  
  def receive: Receive = {
    case ToUTM(lat, lon) =>
      sender() ! ""
  }
}