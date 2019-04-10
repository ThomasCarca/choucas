package com.tile

import akka.actor.{Actor, ActorLogging, Props}
import SaveService

object SaveRegistryActor {
  final case class SaveImageWithHdfs(imageName: String)
  def props: Props = Props[SaveRegistryActor]
}

class SaveRegistryActor extends Actor with ActorLogging{
  import SaveRegistryActor._

  def receive: Receive = {
    case SaveImageWithHdfs(imageName) =>
      SaveService.SaveImageWithHdfs(imageName)
  }
}