package com.queue

import akka.actor.{Actor, ActorLogging, Props}
import com.shared.JobQueues

object QueueRegistryActor {
  final case class GetQueue(uuid: String)
  def props: Props = Props[QueueRegistryActor]
}

class QueueRegistryActor extends Actor with ActorLogging {
  import QueueRegistryActor._

  def receive: Receive = {
    case GetQueue(uuid) => JobQueues.queues.get(uuid) match {
      case Some(queue) => sender() ! Some(queue.asStaticJobQueue)
      case None => sender() ! None
    }
  }
}
