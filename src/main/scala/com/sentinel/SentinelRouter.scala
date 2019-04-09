package com.sentinel

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.box.BoxJsonSupport
import com.sentinel.SentinelRegistryActor.SentinelInfo
import com.shared.{BoundingBox, ImageInfo}

import scala.concurrent.Future
import scala.concurrent.duration._

class SentinelRouter(sentinelRegistryActor: ActorRef) extends SentinelJsonSupport with BoxJsonSupport {

  import spray.json.DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route = pathEndOrSingleSlash {

    post {
      entity(as[BoundingBox]) { boundingBox =>
        val sentinelInfo: Future[Vector[ImageInfo]] = (sentinelRegistryActor ? SentinelInfo(boundingBox)).mapTo[Vector[ImageInfo]]
        complete(StatusCodes.OK, sentinelInfo)
      }
    }
  }
}