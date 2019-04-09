package com.tile

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.tile.TileRegistryActor.TileImage
import scala.concurrent.duration._

class TileRouter(tileRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(180.seconds)

  lazy val route: Route = get {
    path(Segment) { imageName =>
       tileRegistryActor ! TileImage(imageName)
      complete(StatusCodes.Accepted)
    }
  }
}
