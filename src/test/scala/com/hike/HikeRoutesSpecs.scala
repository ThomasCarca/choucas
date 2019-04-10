package com.hike

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.RootRoutes
import com.shared.{JobQueue, JsonSupport}
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures

class HikeRoutesSpecs extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with SprayJsonSupport
  with RootRoutes with JsonSupport{
  override val annotateRegistryActor: ActorRef = null

  override val hikeRegistryActor: ActorRef =
    system.actorOf(HikeRegistryActor.props, "hikeRegistry")

  override val pinpointRegistryActor: ActorRef = null

  override val boxRegistryActor: ActorRef = null

  override val sentinelRegistryActor: ActorRef = null

  override val tileRegistryActor: ActorRef = null

  override val downloadRegistryActor: ActorRef = null

  override val saveRegistryActor: ActorRef = null

  override val queueRegistryActor: ActorRef = null

  override val elasticSearchActor: ActorRef = null

  lazy val routes: Route = rootRoutes

  "GET /hike" should {
    "return a list of ids" in {
      val request = Get("/hike")

      request  ~> routes ~> check {
        status should be(StatusCodes.OK)
      }
    }
  }
}
