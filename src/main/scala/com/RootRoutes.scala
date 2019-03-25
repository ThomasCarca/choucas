package com

import akka.actor.ActorSystem
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete

import akka.util.Timeout

trait RootRoutes {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[RootRoutes])

  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val rootRoutes: Route =
    get {
      complete("Hello world !")
    }
}
