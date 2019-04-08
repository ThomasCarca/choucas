package com.tile

import akka.actor.ActorRef
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.tile.TileRegistryActor.TileImage

import scala.concurrent.Future
import scala.concurrent.duration._

class TileRouter(tileRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(180.seconds)

  lazy val route: Route = get {
    parameters('imageName) { imageName =>
       val tiledImage: Future[String] = (tileRegistryActor ? TileImage(imageName)).mapTo[String]
       complete(tiledImage)
    }
  }
}