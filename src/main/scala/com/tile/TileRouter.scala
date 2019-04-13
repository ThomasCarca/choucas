package com.tile

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import akka.http.scaladsl.server.directives.FutureDirectives.onComplete
import akka.pattern.ask
import com.shared.{JobQueue, JobQueueLocation, JobQueues, JsonSupport}
import com.tile.DownloadRegistryActor.DownloadImages
import com.tile.TileRegistryActor.TileImage
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class TileRouter(downloadRegistryActor: ActorRef, tileRegistryActor: ActorRef) extends JsonSupport {

  import DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(180.seconds)

  lazy val route: Route = {
    //    get {
    //      path(Segment) { imageName =>
    //        tileRegistryActor ! TileImage(imageName)
    //        complete(StatusCodes.Accepted)
    //      }
    //    }
    post {
      entity(as[Vector[String]]) { urls =>
        val futureQueue: Future[JobQueue] = (downloadRegistryActor ? DownloadImages(urls)).mapTo[JobQueue]
        onComplete(futureQueue) {
          case Failure(_) => complete(StatusCodes.ServiceUnavailable)
          case Success(queue) => {
            JobQueues.queues = JobQueues.queues + (queue.uuid -> queue)

            (tileRegistryActor ? TileImage(futureQueue))
            complete(StatusCodes.Accepted, new JobQueueLocation(s"queue/${queue.uuid}"))
            val
          }
        }

      }
    }
  }
}
