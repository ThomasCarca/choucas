package com.pinpoint

import spray.json.{ JsArray, JsObject, JsValue }
import akka.actor.{ Actor, ActorLogging, PoisonPill, Props }
import com.annotate.URIs
import scalaj.http.{ Http, HttpResponse }
import spray.json._

object PinpointService {

  def processJsonData(msg: String): Coord = {

    val mapOfUri = msg.asInstanceOf[URIs]

    val coords = mapOfUri.uris.map(uri => processData(uri.toString))

    return coords.asInstanceOf[Coord]

  }

  def processData(uri: String): Coord = {
    val httpString: String = buildHttpString(uri)
    val response: HttpResponse[String] = sendRequest(httpString)
    val body: JsObject = response.body.parseJson.asJsObject
    val uriResources: Option[JsValue] = body.fields.get("Resources")
    return buildCoords(uriResources)
  }

  def buildHttpString(msg: String): String = {
    val placeToReserch = msg.split("/").last
    val query = "select+%3Fcoor+where+{dbpedia-fr%3A" + placeToReserch + "+georss%3Apoint+%3Fcoor}&format=application%2Fsparql-results%2Bjson"
    val url = "https://dbpedia.org/sparql?default-graph-uri=http://dbpedia.org&query=" + query
    return url
  }

  def buildCoords(uriResources: Option[JsValue]): Coord = {
    uriResources match {
      case Some(resources) => {
        resources match {
          case JsArray(elements) => {
            val coordJSon = elements.map(e => e.asJsObject.fields.get("@value")).flatten
            val coord = coordJSon.map(text => text.toString())
            return Coord(coord.distinct)
          }
          case _ => Coord(Vector.empty[String])
        }
      }
      case None => Coord(Vector.empty[String])
    }
  }

  def sendRequest(url: String): HttpResponse[String] = {
    val response = Http(url).asString
    return response
  }

}
