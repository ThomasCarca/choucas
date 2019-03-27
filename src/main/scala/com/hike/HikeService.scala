package com.hike

import scalaj.http._
import spray.json._
import play.api.libs.json.{Json, JsValue => PlayValue}

import scala.collection.mutable.ArrayBuffer

object HikeService extends DefaultJsonProtocol {

  def getAllHikes() : String = {
    val response = Http("https://choucas.blqn.fr/data/outing/").header("Accept", "application/json").asString
    filterIdOrigin(response).toJson.prettyPrint
  }

  def GetHikeById(id: String) : String = {
    val response = Http("https://choucas.blqn.fr/data/outing/".concat(id)).header("Accept", "application/json").asString
    filterDescription(response).toString()
  }

  def filterIdOrigin(response: HttpResponse[String]): Vector[JsValue] = {
    val body = response.body.parseJson
    body match {
      case JsArray(elements) => elements.flatMap(e => e.asJsObject.fields.get("idOrigin"))
      case _ => Vector.empty[JsValue]
    }
  }

  def filterDescription(response: HttpResponse[String]): PlayValue = {
    val body : PlayValue = Json.parse(response.body)
    val arraybuff = body.\\("description")
    arraybuff match {
      case ArrayBuffer(element) => element
      case _ => Json.parse("{}")
    }
  }

}