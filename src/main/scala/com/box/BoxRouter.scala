package com.box

import akka.actor.ActorRef
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.box.BoxRegistryActor.ToBoundingBox
import com.shared.{BoundingBox, Coordinates, JsonSupport}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._

class BoxRouter(boxRegistryActor: ActorRef) extends JsonSupport {

  import DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route = pathEndOrSingleSlash {
    post {
      entity(as[Vector[Coordinates]]) { coordinates =>
        val box: Future[BoundingBox] = (boxRegistryActor ? ToBoundingBox(coordinates)).mapTo[BoundingBox]
        complete(box)
      }
    }
  }
}