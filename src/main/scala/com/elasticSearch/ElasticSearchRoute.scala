package com.elasticSearch

import akka.actor.ActorRef

import scala.concurrent.duration._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives.{ as, entity, pathEndOrSingleSlash, post, _ }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import com.elasticSearch.ElasticSearchActor.{ getMetaData, postMapping, postMetaData }
import com.shared.{ BoundingBox, DataElastic, JsonSupport }

import scala.concurrent.Future

class ElasticSearchRoute(ElasticSearchActor: ActorRef) extends JsonSupport {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val route: Route =
    pathPrefix("mapping") {
      pathEndOrSingleSlash {
        val respose: Future[String] = (ElasticSearchActor ? postMapping()).mapTo[String]
        complete(respose)
      }
    } ~
      pathEndOrSingleSlash {
        get {
          entity(as[BoundingBox]) { box =>
            val respose: Future[String] = (ElasticSearchActor ? getMetaData(box)).mapTo[String]
            complete(respose)
          }
        } ~
          post {
            entity(as[DataElastic]) { box =>
              val respose: Future[String] = (ElasticSearchActor ? postMetaData(box)).mapTo[String]
              complete(respose)
            }
          }
      }
}

