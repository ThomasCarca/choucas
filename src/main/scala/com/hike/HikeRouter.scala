package com.hike

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.hike.HikeRegistryActor._
import com.shared.JsonSupport
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future


class HikeRouter(hikeRegistryActor: ActorRef) extends JsonSupport{

  import DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(180.seconds)

  lazy val route: Route = get {
    pathEndOrSingleSlash {
      val hikes: Future[Vector[String]] = (hikeRegistryActor ? GetHikes).mapTo[Vector[String]]
      complete(hikes)
    } ~ pathPrefix(Segment) { id =>
      val hike: Future[String] = (hikeRegistryActor ? GetHikeById(id)).mapTo[String]
      complete(hike)
    }
  }

}