package com.hike

import scalaj.http._
import play.api.libs.json.{JsArray, JsDefined, JsNumber, Json, JsValue => PlayValue}


import scala.collection.mutable.ArrayBuffer

object HikeService {

  def getAllHikes() : Vector[String] = {
    val response = Http("https://choucas.blqn.fr/data/outing/").header("Accept", "application/json").asString
    filterIdOrigin(response)
  }

  def getHikeById(id: String) : String = {
    val response = Http("https://choucas.blqn.fr/data/outing/".concat(id)).header("Accept", "application/json").asString
    filterDescription(response).toString()
  }

  def filterIdOrigin(response: HttpResponse[String]): Vector[String] = {
    val body : PlayValue = Json.parse(response.body)
    body match {
      case JsArray(hikes) => hikes.map(hike => (hike \ "idOrigin").get.as[String]).toVector
      case _ => Vector.empty
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