package com.box

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.RootRoutes
import com.shared.{BoundingBox, JobQueue, JsonSupport}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class BoxRoutesSpecs extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with SprayJsonSupport
  with RootRoutes with JsonSupport {

  override val boxRegistryActor: ActorRef =
    system.actorOf(BoxRegistryActor.props, "boxRegistry")

  override val hikeRegistryActor: ActorRef = null

  override val pinpointRegistryActor: ActorRef = null

  override val annotateRegistryActor: ActorRef = null

  override val sentinelRegistryActor: ActorRef = null

  override val tileRegistryActor: ActorRef = null

  override val downloadRegistryActor: ActorRef = null

  override val saveRegistryActor: ActorRef = null

  override val queueRegistryActor: ActorRef = null

  override val elasticSearchActor: ActorRef = null

  lazy val routes: Route = rootRoutes

  "POST /box" should {

    "return a bounding box" in {

      val points = "[{\"lat\": 43.260076,\"lon\": -0.418779},{\"lat\": 43.321055,\"lon\": -0.313064},{\"lat\": 43.279068,\"lon\": -0.305776}]"
      val entity = HttpEntity(ContentTypes.`application/json`, points)
      val request = Post("/box").withEntity(entity)

      request ~> routes ~> check {
        val boundingBox = entityAs[BoundingBox]
        status should be(StatusCodes.OK)
        contentType should be(ContentTypes.`application/json`)
      }
    }

  }
}
