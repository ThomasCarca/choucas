package com.elasticSearch

import akka.actor.{ Actor, ActorLogging, Props }
import com.shared.{ BoundingBox, DataElastic }

object ElasticSearchActor {
  case class getMetaData(msg: BoundingBox)
  case class postMetaData(msg: DataElastic)
  case class postMapping()
  def props: Props = Props[ElasticSearchActor]
}

class ElasticSearchActor extends Actor with ActorLogging {

  import ElasticSearchActor._

  def receive = {
    case getMetaData(jsonData) => sender() ! ElasticSearchService.runSearchRequest(jsonData)
    case postMetaData(jsonData) => sender() ! ElasticSearchService.runAddRequest(jsonData)
    case postMapping() => sender() ! ElasticSearchService.runAddMapping()
  }

}
