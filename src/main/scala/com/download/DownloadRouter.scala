package com.download

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.box.BoundingBox
import com.box.BoxJsonSupport
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._

class DownloadRouter(downloadRegistryActor: ActorRef) extends BoxJsonSupport {


  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route = pathEndOrSingleSlash {
    post {
      entity(as[BoundingBox]) { boundingBox =>
        complete(StatusCodes.OK, boundingBox)
      }
    }
  }
}