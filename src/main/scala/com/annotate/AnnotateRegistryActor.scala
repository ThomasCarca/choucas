package com.annotate

import akka.actor.{ Actor, ActorLogging, Props }

object AnnotateRegistryActor {
  final case class Annotate(text: String)
  def props: Props = Props[AnnotateRegistryActor]
}

class AnnotateRegistryActor extends Actor with ActorLogging {
  import AnnotateRegistryActor._

  var products: Seq[Product] = Nil

  def receive: Receive = {
    case Annotate(text) =>
      sender() ! AnnotateService.fetchAnnotations(text)
  }
}