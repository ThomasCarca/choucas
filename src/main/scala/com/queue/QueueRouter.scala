package com.queue

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FutureDirectives.onComplete
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.queue.QueueRegistryActor.GetQueue
import com.shared.{JobQueue, JobQueues, JsonSupport, StaticJobQueue}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class QueueRouter(queueRegistryActor: ActorRef) extends JsonSupport {

  import DefaultJsonProtocol._

  implicit lazy val timeout: Timeout = Timeout(180.seconds)

  lazy val route: Route =
    get {
      path(Segment) { uuid =>
        val futureMaybeQueue: Future[Option[StaticJobQueue]] = (queueRegistryActor ? GetQueue(uuid)).mapTo[Option[StaticJobQueue]]
        onComplete(futureMaybeQueue) {
          case Failure(_) => complete(StatusCodes.ServiceUnavailable)
          case Success(maybeQueue) => maybeQueue match {
            case Some(queue) => complete(StatusCodes.OK, queue)
            case None => complete(StatusCodes.NotFound)
          }
        }
      }
    }

}
