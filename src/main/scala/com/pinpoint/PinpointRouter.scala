package com.pinpoint

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.pinpoint.PinpointRegistryActor.ToCoordinates
import com.shared.{Coordinates, JsonSupport, URIs}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future

class PinpointRouter(PinpointRegistryActor: ActorRef) extends JsonSupport{

  import DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route =
    pathEndOrSingleSlash {
      post {
        entity(as[URIs]) { uris =>
          val coordinates: Future[Vector[Coordinates]] = (PinpointRegistryActor ? ToCoordinates(uris)).mapTo[Vector[Coordinates]]
          complete(coordinates)
        }
      }
    }
}

