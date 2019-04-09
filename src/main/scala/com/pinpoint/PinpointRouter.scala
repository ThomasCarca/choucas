package com.pinpoint

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.pinpoint.PinpointRegistryActor.getPinpointFromUri

import scala.concurrent.Future

class PinpointRouter(PinpointRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route =
    pathEndOrSingleSlash {
      post {
        entity(as[String]) { jsonData =>
          val respose: Future[String] = (PinpointRegistryActor ? getPinpointFromUri(jsonData)).mapTo[String]
          complete(respose)
        }
      }
    }
}

