package com.annotate

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.annotate.AnnotateRegistryActor.Annotate
import com.shared.URIs

import scala.concurrent.Future

class AnnotateRouter(annotateRegistryActor: ActorRef) extends AnnotateJsonSupport {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route =
    pathEndOrSingleSlash {
      post {
        entity(as[String]) { text =>
          val annotations: Future[URIs] = (annotateRegistryActor ? Annotate(text)).mapTo[URIs]
          complete(annotations)
        }
      }
    }
}