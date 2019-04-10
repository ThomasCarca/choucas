package com.save

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.save.SaveRegistryActor.SaveImageWithHdfs

import scala.concurrent.duration._

class SaveRouter(saveRegistryActor: ActorRef) {
  implicit lazy val timeout: Timeout = Timeout(600.seconds)

  lazy val route: Route = get {
    path(Segment) { imageName =>
      saveRegistryActor ! SaveImageWithHdfs(imageName)
      complete(StatusCodes.Accepted)
    }
  }
}
