package com.elasticSearch

import com.shared.{ BoundingBox, DataElastic }
import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.http.search.SearchResponse

import com.sksamuel.elastic4s.http.{ ElasticClient, ElasticProperties, RequestFailure, RequestSuccess }

object ElasticSearchService {

  import com.sksamuel.elastic4s.http.ElasticDsl._

  def runSearchRequest(boundingBox: BoundingBox): String = {

    val client = ElasticClient(ElasticProperties("http://localhost:9200"))
    /*
    client.execute {
      createIndex("artists").mappings(
        mapping("modern").fields(
          textField("name")))
    }
    client.execute {
      indexInto("artists" / "modern").fields("name" -> "L.S. Lowry").refresh(RefreshPolicy.Immediate)
    }
  */
    val resp = client.execute {
      search("artists") query "lowry"
    }.await

    println("---- Search Results ----")
    resp match {
      case failure: RequestFailure => println("We failed " + failure.error)
      case results: RequestSuccess[SearchResponse] => println(results.result.hits.hits.head.sourceAsString)
      case results: RequestSuccess[_] => println(results.result)
    }

    // Response also supports familiar combinators like map / flatMap / foreach:
    resp foreach (search => println(s"There were ${search.totalHits} total hits"))
    client.close()
    return "done"
  }
  def runAddRequest(dataElastic: DataElastic): String = ???
}
