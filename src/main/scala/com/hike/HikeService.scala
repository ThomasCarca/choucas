package com.hike

import scalaj.http._
import spray.json._

object HikeService extends DefaultJsonProtocol {

  def getAllHikes() : String = {
    val response = Http("https://choucas.blqn.fr/data/outing/").header("Accept", "application/json").asString
    filterIdOrigin(response).toJson.prettyPrint
  }

  def filterIdOrigin(response: HttpResponse[String]): Vector[JsValue] = {
    val body = response.body.parseJson
    body match {
      case JsArray(elements) => elements.flatMap(e => e.asJsObject.fields.get("idOrigin"))
      case _ => Vector.empty[JsValue]
    }
  }

}