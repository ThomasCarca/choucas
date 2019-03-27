package com.utm

import akka.actor.ActorRef
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.utm.UtmRegistryActor.ToUTM

import scala.concurrent.Future
import scala.concurrent.duration._

class UtmRouter(utmRegistryActor: ActorRef) {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route = get {
    val utm: Future[String] = (utmRegistryActor ? ToUTM("", "")).mapTo[String]
    complete(utm)
    }
}