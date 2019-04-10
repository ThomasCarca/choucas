package com

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._
import com.annotate.AnnotateRouter
import com.hike.HikeRouter
import com.pinpoint.PinpointRouter
import com.tile.TileRouter
import com.box.BoxRouter
import com.queue.QueueRouter
import com.sentinel.SentinelRouter
import com.save.SaveRouter
import com.shared.JobQueue

trait RootRoutes {

  implicit def system: ActorSystem

  def annotateRegistryActor: ActorRef
  def hikeRegistryActor: ActorRef
  def pinpointRegistryActor: ActorRef
  def boxRegistryActor: ActorRef
  def sentinelRegistryActor: ActorRef
  def tileRegistryActor: ActorRef
  def downloadRegistryActor: ActorRef
  def queueRegistryActor: ActorRef
  def saveRegistryActor: ActorRef

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
      pathPrefix("sentinel") {
        new SentinelRouter(sentinelRegistryActor).route
      } ~
      pathPrefix("tile") {
        new TileRouter(downloadRegistryActor, tileRegistryActor).route
      } ~
      pathPrefix("queue") {
        new QueueRouter(queueRegistryActor).route
      } ~
      pathPrefix("save") {
        new SaveRouter(saveRegistryActor).route
      } ~
      pathEndOrSingleSlash {
        get {
          complete("Hello root endpoint !")
        }
      }
}
