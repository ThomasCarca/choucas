package com.tile

import akka.actor.{Actor, ActorLogging, Props}
import SaveService._
import com.shared.JobQueue

object SaveRegistryActor {
  final case class SaveImageWithHdfs(queue: JobQueue)
  def props: Props = Props[SaveRegistryActor]
}

class SaveRegistryActor extends Actor with ActorLogging{
  import SaveRegistryActor._

  def receive: Receive = {
    case SaveImageWithHdfs(queue) =>
      sender() ! SaveService.saveImageWithHdfs(queue)
  }
}
