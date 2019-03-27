package com.pinpoint

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.QuickstartServer.system
import com.pinpoint.PinpointRegistry.getPinpointFromUri

import scala.concurrent.{ ExecutionContext, Future }

class PinpointRouter(PinpointRegistry: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val route: Route = pathPrefix(Segment) {
    jsonData =>
      get {
        val respose: Future[String] = (PinpointRegistry ? getPinpointFromUri(jsonData)).mapTo[String]
        complete(respose)
      }
  }
}

