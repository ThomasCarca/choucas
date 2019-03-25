package com.annotate

import akka.actor.{ Actor, ActorLogging, Props }

object AnnotateRegistryActor {
  final case class AnnotateText(text: String)
  def props: Props = Props[AnnotateRegistryActor]
}

class AnnotateRegistryActor extends Actor with ActorLogging {
  import AnnotateRegistryActor._

  var products: Seq[Product] = Nil

  def receive: Receive = {
    case AnnotateText(_) =>
      sender() ! "I just annotated your text !"
  }
}