package com

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import com.annotate.AnnotateRouter
import com.hike.HikeRouter
import com.pinpoint.PinpointRouter
import com.box.BoxRouter

trait RootRoutes {

  implicit def system: ActorSystem

  def annotateRegistryActor: ActorRef
  def hikeRegistryActor: ActorRef
  def pinpointRegistryActor: ActorRef
  def boxRegistryActor: ActorRef

  implicit lazy val timeout = Timeout(180.seconds)

  lazy val rootRoutes: Route =
    pathPrefix("annotate") {
      new AnnotateRouter(annotateRegistryActor).route
    } ~
      pathPrefix("hike") {
        new HikeRouter(hikeRegistryActor).route
      } ~
      pathPrefix("pinpoint") {
        new PinpointRouter(pinpointRegistryActor).route
      } ~
      pathPrefix("box") {
        new BoxRouter(boxRegistryActor).route
      } ~
      pathEndOrSingleSlash {
        get {
          complete("Hello root endpoint !")
        }
      }
}
