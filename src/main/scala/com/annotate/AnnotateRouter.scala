package com.annotate

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.annotate.AnnotateRegistryActor.Annotate

import scala.concurrent.Future


class AnnotateRouter(annotateRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route = get {
    parameters('text.as[String]) { text =>
      val annotation: Future[String] = (annotateRegistryActor ? Annotate(text)).mapTo[String]
      complete(annotation)
    }
  }
}