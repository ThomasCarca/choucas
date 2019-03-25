package com

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import com.annotate.AnnotateRouter


trait RootRoutes {

  implicit def system: ActorSystem

  def annotateRegistryActor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val rootRoutes: Route =
    pathPrefix("annotate") {
      new AnnotateRouter(annotateRegistryActor).route
    } ~
    pathEndOrSingleSlash {
      get {
        complete("Hello root endpoint !")
      }
    }
}
