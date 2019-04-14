package com.elasticSearch

import com.shared._
import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.analyzers.StopAnalyzer
import com.sksamuel.elastic4s.http.search.SearchResponse
import com.sksamuel.elastic4s.http.{ ElasticClient, ElasticProperties, RequestFailure, RequestSuccess }
import com.sksamuel.elastic4s.http.index.CreateIndexResponse
import org.elasticsearch.client.Response
import play.api.libs.json.{ Json, JsValue => PlayValue }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success, Try }

object ElasticSearchService {

  import com.sksamuel.elastic4s.http.ElasticDsl._

  def runAddMapping(): String = {

    val client = ElasticClient(ElasticProperties("http://localhost:9200"))

    System.out.println("create Index")

    val resp = client.execute(
      createIndex("choucas").source(
        """ {"mappings": {"metadata" : { "properties" :{ "coordonates" : { "type": "geo_point" },"tuile" : {"properties": { "download": { "type": "text" },"name": { "type": "text" },"metadata": { "type": "text" }}},"image" : {"properties": { "download": { "type": "text" },"preview": { "type": "text" },"cloud" : { "type": "float" },"metadata":{ "type": "text" }}},"metadata": { "type": "text" }}}}}
        """.stripMargin))

    resp.onComplete({
      case Success(resuls) => {
        resuls match {
          case failure: RequestFailure =>
            client.close()
            System.out.println("Fail")
            System.out.println(failure.status.toString)
          case results: RequestSuccess[CreateIndexResponse] =>
            System.out.println("Map create")
            client.close()
            System.out.println(results.status.toString)
        }
      }
      case Failure(exception) => {
        client.close()
        System.out.println(exception.toString)
      }
    })

    return "Request sended"

  }

  def runSearchRequest(boundingBox: BoundingBox): String = {

    val client = ElasticClient(ElasticProperties("http://localhost:9200"))

    println("---- Search Results ----")
    var resp = client.execute(
      search("choucas" / "metadata").source(boundingBox.toJsonQuery())).await

    resp match {
      case failure: RequestFailure =>
        println("We failed " + failure.error)
        client.close()
        return "{}"
      case results: RequestSuccess[SearchResponse] => results.body match {
        case Some(data) =>
          client.close()
          return Json.parse(data.toString).\\("hits").map(
            data => data.\\("hits")).flatten.head.toString()
        case None => "{}"
      }
    }

  }

  def runAddRequest(dataElastic: DataElastic): String = {

    // val data = DataElastic(Coordinates(1.0.toFloat, 1.0.toFloat), Tuile("toto", "titi", "pokgpozkpokg"), ImageInfo("toto", "titi", 1.toFloat, new Date()), "rrjgeirjgie")

    val client = ElasticClient(ElasticProperties("http://localhost:9200"))

    System.out.println(dataElastic.toJsonIndexCreate())

    val resp = client.execute(
      indexInto("choucas" / "metadata").source(dataElastic.toJsonIndexCreate())).await

    resp match {
      case failure: RequestFailure => println("We failed " + failure.error)
      case results: RequestSuccess[SearchResponse] => results.body match {
        case Some(data) => println(Json.parse(data.toString))
        case None => return ""
      }
    }

    client.close()
    return "done"
  }
}
