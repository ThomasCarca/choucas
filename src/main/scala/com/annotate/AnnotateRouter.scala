package com.annotate

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

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
    val annotation: Future[String] = (annotateRegistryActor ? Annotate("Pau est une ville du sud-ouest de la France")).mapTo[String]
    complete(annotation)
  }
}