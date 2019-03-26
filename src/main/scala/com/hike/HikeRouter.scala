package com.hike

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.hike.HikeRegistryActor.GetHikes

import scala.concurrent.Future


class HikeRouter(hikeRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(20.seconds)

  lazy val route: Route = get {
    val hike: Future[String] = (hikeRegistryActor ? GetHikes).mapTo[String]
    complete(hike)
  }
}