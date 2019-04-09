package com.annotate

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Route
import com.RootRoutes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import spray.json.JsObject
import spray.json.DefaultJsonProtocol._

class AnnotateRoutesSpecs extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest with SprayJsonSupport
  with RootRoutes with AnnotateJsonSupport {

  override val annotateRegistryActor: ActorRef =
    system.actorOf(AnnotateRegistryActor.props, "annotateRegistry")

  override val hikeRegistryActor: ActorRef = null

  override val pinpointRegistryActor: ActorRef = null

  override val boxRegistryActor: ActorRef = null

  override val downloadRegistryActor: ActorRef = null

  override val tileRegistryActor: ActorRef = null

  override val saveRegistryActor: ActorRef = null

  lazy val routes: Route = rootRoutes

  "POST /annotate" should {

    "return an empty list when no entity is found in the text" in {

      val text = "test test test test test test test test test"
      val entity = HttpEntity(ContentTypes.`application/json`, s"""{ "text": "$text"}""")
      val request = Post("/annotate").withEntity(entity)

      request ~> routes ~> check {
        val uris = entityAs[URIs]
        status should be(StatusCodes.OK)
        contentType should be(ContentTypes.`application/json`)
        uris.uris.length should be(0)
      }
    }

    "return a non-empty list when entities should be found in the text" in {

      val text = "Entities such as Paris or France should be found in this text"
      val entity = HttpEntity(ContentTypes.`application/json`, s"""{ "text": "$text"}""")
      val request = Post("/annotate").withEntity(entity)

      request ~> routes ~> check {
        val uris = entityAs[URIs]
        status should be(StatusCodes.OK)
        contentType should be(ContentTypes.`application/json`)
        uris.uris.length should be > 0
      }
    }

  }
}
