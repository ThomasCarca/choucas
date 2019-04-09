package com.pinpoint

import spray.json._
import akka.actor.{ Actor, ActorLogging, PoisonPill, Props }
import play.api.libs.json.{ JsValue => PlayValue, Json }
import scalaj.http.{ Http, HttpResponse }

object PinpointService {

  def processJsonData(msg: String): String = {

    val msgJson = msg.parseJson.convertTo[UriList]

    val rowdata = msgJson.items.map(uri => processData(uri.toString)).filterNot(uri => uri.equals(Coord("", "")))

    val dataContainer = new Container("Coordonne", rowdata)
    val jsonReturn = Json.toJson(dataContainer)
    return jsonReturn.toString()

  }

  def processData(uri: String): Coord = {
    val httpString: String = buildHttpString(uri)
    val response: HttpResponse[String] = sendRequest(httpString)
    val body = Json.parse(response.body)
    val arraybuff = body.\\("bindings")
    return buildCoords(arraybuff)
  }

  def buildHttpString(msg: String): String = {
    val placeToReaserch = msg.split("/").last
    val query = "select+%3Fcoor+where+%7Bdbpedia-fr%3A" + placeToReaserch + "+georss%3Apoint+%3Fcoor%7D&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on"
    val url = "http://fr.dbpedia.org/sparql?default-graph-uri=&query=" + query
    return url
  }

  def buildCoords(uriResources: Seq[PlayValue]): Coord = {
    uriResources match {
      case List(element) =>
        val value = uriResources.map(coord => coord.\\("value")).flatten
        if (value.nonEmpty) {
          val coord = value.head.toString().split(" ")
          Coord(coord.head.replace(""""""", ""), coord.last.replace(""""""", ""))
        } else {
          Coord("", "")
        }
      case _ => Coord("", "")
    }
  }

  def sendRequest(url: String): HttpResponse[String] = {
    val response = Http(url).asString
    return response
  }

}
