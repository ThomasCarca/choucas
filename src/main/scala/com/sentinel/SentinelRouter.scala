package com.sentinel

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.sentinel.SentinelRegistryActor.SentinelInfo
import com.shared.{DatedBoundingBox, ImageInfo, JsonSupport}

import scala.concurrent.Future
import scala.concurrent.duration._

class SentinelRouter(sentinelRegistryActor: ActorRef) extends JsonSupport {

  import spray.json.DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route = pathEndOrSingleSlash {

    post {
      entity(as[DatedBoundingBox]) { box =>
        val sentinelInfo: Future[Vector[ImageInfo]] = (sentinelRegistryActor ? SentinelInfo(box)).mapTo[Vector[ImageInfo]]
        complete(StatusCodes.OK, sentinelInfo)
      }
    }
  }
}