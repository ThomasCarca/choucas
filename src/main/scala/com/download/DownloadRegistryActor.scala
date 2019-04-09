package com.download

import akka.actor.{Actor, ActorLogging, Props}


object DownloadRegistryActor {
  def props: Props = Props[DownloadRegistryActor]
}


class DownloadRegistryActor extends Actor with ActorLogging {

  def receive: Receive = {case _ => ""}
}