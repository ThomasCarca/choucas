package com.pinpoint

import com.shared.{Coordinates, URIs}
import play.api.libs.json.{Json, JsValue => PlayValue}
import scalaj.http.{Http, HttpResponse}

object PinpointService {

  def fetchCoordinates(uris: URIs): Vector[Coordinates] = {
    uris.uris.map(toCoordinates(_)).filterNot(uri => uri.equals(Coordinates(Long.MaxValue, Long.MaxValue)))
  }

  def toCoordinates(uri: String): Coordinates = {
    val url: String = buildUrl(uri)
    val response: HttpResponse[String] = Http(url).asString
    val body = Json.parse(response.body)
    val bindings = body.\\("bindings")
    return buildCoordinates(bindings)
  }

  def buildUrl(uri: String): String = {
    val placeToResearch = uri.split("/").last
    val query = "select+%3Fcoor+where+%7Bdbpedia-fr%3A" + placeToResearch + "+georss%3Apoint+%3Fcoor%7D&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on"
    val url = "http://fr.dbpedia.org/sparql?default-graph-uri=&query=" + query
    return url
  }

  def buildCoordinates(uriResources: Seq[PlayValue]): Coordinates = {
    uriResources match {
      case List(_) =>
        val value = uriResources.map(coordinates => coordinates.\\("value")).flatten
        if (value.nonEmpty) {
          val coordinates = value.head.toString().split(" ")
          Coordinates(coordinates.head.replace(""""""", "").toFloat, coordinates.last.replace(""""""", "").toFloat)
        } else {
          Coordinates(Long.MaxValue, Long.MaxValue)
        }
      case _ => Coordinates(Long.MaxValue, Long.MaxValue)
    }
  }

}
