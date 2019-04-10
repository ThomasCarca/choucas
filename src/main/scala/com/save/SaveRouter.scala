package com.save

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.save.SaveRegistryActor.SaveImageWithHdfs
import scala.concurrent.duration._
import scala.concurrent.Future

class SaveRouter(saveRegistryActor: ActorRef) {
  implicit lazy val timeout: Timeout = Timeout(600.seconds)

  lazy val route: Route = get {
    path(Segment) { imageName =>
      val save: Future[String] = (saveRegistryActor ? SaveImageWithHdfs(imageName)).mapTo[String]
      complete(save)
    }
  }
}
