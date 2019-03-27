package com.pinpoint

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.QuickstartServer.system
import com.pinpoint.PinpointRegistryActor.getPinpointFromUri

import scala.concurrent.{ ExecutionContext, Future }

class PinpointRouter(PinpointRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val route: Route = pathPrefix(Segment) {
    entity(as[String])
    jsonData => {
      val respose: Future[String] = (PinpointRegistryActor ? getPinpointFromUri(jsonData)).mapTo[String]
      complete(respose)
    }
  }
}

